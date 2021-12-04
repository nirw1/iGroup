package iob.logic;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iob.attributes.CreatedBy;
import iob.attributes.InstanceId;
import iob.attributes.UserId;
import iob.boundaries.InstanceBoundary;
import iob.boundaries.InstanceIdBoundary;
import iob.converters.InstanceConverter;
import iob.daos.InstanceDao;
import iob.data.InstanceEntity;
import iob.errors.BadRequestException;
import iob.errors.NotFoundException;

@Service // declaration of Spring Bean of Business Logic (BL) layer
public class InstancesServiceJpa implements InstancesWithChildrenService {
	private InstanceDao instanceDao;
	private InstanceConverter converter;
	private AtomicLong counter;
	private String appName;

	@Autowired
	public InstancesServiceJpa(InstanceDao instanceDao, InstanceConverter converter) {
		this.instanceDao = instanceDao;
		this.converter = converter;
	}

	@Value("${spring.application.name}") // read this value from Spring Configuration
	public void setAppName(String appName) {
		this.appName = appName;
	}

	@PostConstruct
	public void init() {
		// initialize counter
		this.counter = new AtomicLong(1L);
	}

	@Override
	@Transactional
	public InstanceBoundary createInstance(String userDomain, String userEmail, InstanceBoundary instance) {
		
		if (instance.getType() == null || instance.getType().isEmpty()) {
			throw new BadRequestException("instance type cannot be empty or null");
		}
		
		InstanceEntity entityToStore = this.converter.convertToEntity(instance);
		entityToStore.setId(String.valueOf(this.counter.getAndIncrement()));
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
		InstanceEntity existing = this.instanceDao
			.findById(new InstanceId(instanceDomain, instanceId))
			.orElseThrow(()->new NotFoundException("Could not find instance with id: " + instanceId + "in domain: " + instanceDomain));

		// if entity exists update only non null fields from updatedMessage
		boolean dirty = false;
		if (update.getActive() != null) {
			existing.setActive(update.getActive());
			dirty = true;
		}

		// Note that id, domain, createdBy and timestamp must not be changed using PUT operation

		if (update.getInstanceAttributes() != null) {
			existing.setInstanceAttributes(update.getInstanceAttributes());
			dirty = true;
		}

		if (update.getLocation() != null) {
			existing.setLatitude(update.getLocation().getLat());
			existing.setLongitude(update.getLocation().getLng());
			dirty = true;
		}
		
		if (update.getName() != null) {
			existing.setName(update.getName());
			dirty = true;
		}
		
		if (update.getType() != null) {
			existing.setType(update.getType());
			dirty = true;
		}

		if (dirty) {			
			// DB update ONLY if the data was actually modified
			existing = this.instanceDao.save(existing);
			
			if (existing == null) {
				throw new RuntimeException("Error while updating database");
			}
		}

		// convert entity to boundary and return it
		return this.converter.convertToBoundary(existing);
	}

	@Override
	@Transactional(readOnly = true)
	public List<InstanceBoundary> getAllInstances(String userDomain, String userEmail) {
		return StreamSupport.stream(this.instanceDao.findAll().spliterator(), false).map(this.converter::convertToBoundary)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public InstanceBoundary getSpecificInstance(String userDomain, String userEmail, String instanceDomain,
			String instanceId) {
		return this.converter
				.convertToBoundary(this.instanceDao
					.findById(new InstanceId(instanceDomain, instanceId))
					  .orElseThrow(()->new NotFoundException("Could not find instance with id: " + instanceId + " in domain: " + instanceDomain)));
	}

	@Override
	@Transactional
	public void deleteAllInstances(String adminDomain, String adminEmail) {
		this.instanceDao.deleteAll();
	}

	@Override
	@Transactional
	public void bindChild(String userDomain, String userEmail, String instanceDomain, String instanceId,
			InstanceIdBoundary childBoundary) {		
		InstanceEntity parent = this.instanceDao
				.findById(new InstanceId(appName, instanceId))
				.orElseThrow(()->new NotFoundException("Could not find instance with id: " + instanceId + " in domain: " + appName));
		
		InstanceEntity child = this.instanceDao
				.findById(new InstanceId(appName, childBoundary.getId()))
				.orElseThrow(()->new NotFoundException("Could not find instance with id: " + childBoundary.getId() + " in domain: " + appName));

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
	public Set<InstanceBoundary> getAllChildren(String userDomain, String userEmail, String instanceDomain,
			String instanceId) {
		InstanceEntity entity = this.instanceDao
				.findById(new InstanceId(instanceDomain, instanceId))
				.orElseThrow(()->new NotFoundException("Could not find instance with id: " + instanceId + " in domain: " + instanceDomain));
		
		return entity.getChildren()
				.stream()
				.map(this.converter::convertToBoundary)
				.collect(Collectors.toSet());
	}

	@Override
	@Transactional(readOnly = true)
	public Set<InstanceBoundary> getAllParents(String userDomain, String userEmail, String instanceDomain,
			String instanceId) {
		InstanceEntity entity = this.instanceDao
				.findById(new InstanceId(instanceDomain, instanceId))
				.orElseThrow(()->new NotFoundException("Could not find instance with id: " + instanceId + " in domain: " + instanceDomain));
		
		return entity.getParents()
				.stream()
				.map(this.converter::convertToBoundary)
				.collect(Collectors.toSet());
	}
}
