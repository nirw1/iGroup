package iob.logic;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iob.annotations.RolePermission;
import iob.attributes.CreatedBy;
import iob.attributes.InstanceId;
import iob.attributes.UserId;
import iob.boundaries.InstanceBoundary;
import iob.boundaries.InstanceIdBoundary;
import iob.converters.InstanceConverter;
import iob.daos.InstanceDao;
import iob.daos.IdGeneratorDao;
import iob.data.InstanceEntity;
import iob.data.UserRole;
import iob.data.IdGenerator;
import iob.errors.BadRequestException;
import iob.errors.NotFoundException;

@Service // declaration of Spring Bean of Business Logic (BL) layer
public class InstancesServiceJpa implements EnhancedInstancesWithChildrenService {
	private IdGeneratorDao idGenerator;
	private InstanceDao instanceDao;
	private InstanceConverter converter;
	private String appName;

	@Autowired
	public InstancesServiceJpa(InstanceDao instanceDao, InstanceConverter converter, IdGeneratorDao idGenerator) {
		this.instanceDao = instanceDao;
		this.converter = converter;
		this.idGenerator = idGenerator;
	}

	@Value("${spring.application.name}") // read this value from Spring Configuration
	public void setAppName(String appName) {
		this.appName = appName;
	}

	@Override
	@Transactional
	public InstanceBoundary createInstance(String userDomain, String userEmail, InstanceBoundary instance) {

		if (instance.getType() == null || instance.getType().isEmpty()) {
			throw new BadRequestException("instance type cannot be empty or null");
		}

		if (instance.getName() == null || instance.getName().isEmpty()) {
			throw new BadRequestException("instance name cannot be empty or null");
		}

		InstanceEntity entityToStore = this.converter.convertToEntity(instance);
		IdGenerator id = new IdGenerator();

		id = this.idGenerator.save(id);
		entityToStore.setId(String.valueOf(id.getId()));
		this.idGenerator.delete(id);

		entityToStore.setDomain(this.appName);
		entityToStore.setCreatedBy(new CreatedBy(new UserId(userDomain, userEmail)).toString());
		entityToStore.setCreatedTimestamp(new Date());

		this.instanceDao.save(entityToStore);
		return this.converter.convertToBoundary(entityToStore);
	}

	@Override
	@Transactional
	public InstanceBoundary updateInstance(String userDomain, String userEmail, String instanceDomain,
			String instanceId, InstanceBoundary update) {
		Optional<InstanceEntity> op = this.instanceDao.findById(new InstanceId(instanceDomain, instanceId));
		if (op.isPresent()) {
			InstanceEntity existing = op.get();

			if (update.getActive() != null) {
				existing.setActive(update.getActive());
			}

			// Note that id, domain, createdBy and timestamp must not be changed using PUT
			// operation

			if (update.getInstanceAttributes() != null) {
				existing.setInstanceAttributes(update.getInstanceAttributes());
			}

			if (update.getLocation() != null) {
				existing.setLatitude(update.getLocation().getLat());
				existing.setLongitude(update.getLocation().getLng());
			}

			if (update.getName() != null && !update.getName().isEmpty()) {
				existing.setName(update.getName());
			}

			if (update.getType() != null) {
				existing.setType(update.getType());
			}

			// convert entity to boundary and return it
			return this.converter.convertToBoundary(this.instanceDao.save(existing));

		} else {
			throw new NotFoundException(
					"Could not find instance with id: " + instanceId + " in domain: " + instanceDomain);
		}

	}

	@Override
	@Transactional(readOnly = true)
	@Deprecated
	public List<InstanceBoundary> getAllInstances(String userDomain, String userEmail) {
		throw new RuntimeException("Unimplemented deprecated operation");
	}

	@Override
	@Transactional(readOnly = true)
	public List<InstanceBoundary> getAllInstances(String userDomain, String userEmail, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Direction.DESC, "id");
		return StreamSupport.stream(this.instanceDao.findAll(pageable).spliterator(), false)
				.map(this.converter::convertToBoundary).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public InstanceBoundary getSpecificInstance(String userDomain, String userEmail, String instanceDomain,
			String instanceId) {
		return this.converter.convertToBoundary(this.instanceDao.findById(new InstanceId(instanceDomain, instanceId))
				.orElseThrow(() -> new NotFoundException(
						"Could not find instance with id: " + instanceId + " in domain: " + instanceDomain)));
	}

	@Override
	@Transactional
	@RolePermission(role = UserRole.ADMIN)
	public void deleteAllInstances(String adminDomain, String adminEmail) {
		this.instanceDao.deleteAll();
	}

	@Override
	@Transactional
	public void bindChild(String userDomain, String userEmail, String instanceDomain, String instanceId,
			InstanceIdBoundary childBoundary) {
		InstanceEntity parent = this.instanceDao.findById(new InstanceId(appName, instanceId))
				.orElseThrow(() -> new NotFoundException(
						"Could not find instance with id: " + instanceId + " in domain: " + appName));

		InstanceEntity child = this.instanceDao.findById(new InstanceId(appName, childBoundary.getId()))
				.orElseThrow(() -> new NotFoundException(
						"Could not find instance with id: " + childBoundary.getId() + " in domain: " + appName));

		parent.addChildren(child); // A value can be added only once to an HashSet
		child.addParent(parent); // A value can be added only once to an HashSet

		parent = this.instanceDao.save(parent);
		if (parent == null) {
			throw new RuntimeException("Error while updating database");
		}

		child = this.instanceDao.save(child);
		if (child == null) {
			throw new RuntimeException("Error while updating database");
		}
	}

	@Override
	@Transactional(readOnly = true)
	@Deprecated
	public Set<InstanceBoundary> getAllChildren(String userDomain, String userEmail, String instanceDomain,
			String instanceId) {
		throw new RuntimeException("Unimplemented deprecated operation");
	}

	@Override
	@Transactional(readOnly = true)
	public Set<InstanceBoundary> getAllChildren(String userDomain, String userEmail, String instanceDomain,
			String instanceId, int page, int size) {
		// TODO: implement pagination - what we have here is the old implementation
		InstanceEntity entity = this.instanceDao.findById(new InstanceId(instanceDomain, instanceId))
				.orElseThrow(() -> new NotFoundException(
						"Could not find instance with id: " + instanceId + " in domain: " + instanceDomain));

		return entity.getChildren().stream().map(this.converter::convertToBoundary).collect(Collectors.toSet());
	}

	@Override
	@Transactional(readOnly = true)
	@Deprecated
	public Set<InstanceBoundary> getAllParents(String userDomain, String userEmail, String instanceDomain,
			String instanceId) {
		throw new RuntimeException("Unimplemented deprecated operation");
	}

	@Override
	@Transactional(readOnly = true)
	public Set<InstanceBoundary> getAllParents(String userDomain, String userEmail, String instanceDomain,
			String instanceId, int page, int size) {
		// TODO: implement pagination - what we have here is the old implementation
		InstanceEntity entity = this.instanceDao.findById(new InstanceId(instanceDomain, instanceId))
				.orElseThrow(() -> new NotFoundException(
						"Could not find instance with id: " + instanceId + " in domain: " + instanceDomain));

		return entity.getParents().stream().map(this.converter::convertToBoundary).collect(Collectors.toSet());
	}
}