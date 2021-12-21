package iob.logic;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iob.annotations.RolePermission;
import iob.boundaries.ActivityBoundary;
import iob.converters.ActivityConverter;
import iob.daos.ActivityDao;
import iob.daos.IdGeneratorDao;
import iob.daos.InstanceDao;
import iob.daos.UserDao;
import iob.data.ActivityEntity;
import iob.data.IdGenerator;
import iob.data.UserRole;
import iob.errors.BadRequestException;

@Service
public class ActivitiesServiceJpa implements ActivitiesService {
	private IdGeneratorDao idGenerator;
	private InstanceDao instanceDao;
	private UserDao userDao;
	private ActivityDao activityDao;
	private ActivityConverter converter;
	private String appName;

	@Autowired
	public ActivitiesServiceJpa(InstanceDao instanceDao, UserDao userDao, ActivityDao activityDao,
			ActivityConverter converter, IdGeneratorDao idGenerator) {
		this.instanceDao = instanceDao;
		this.userDao = userDao;
		this.activityDao = activityDao;
		this.converter = converter;
		this.idGenerator = idGenerator;
	}

	@Value("${spring.application.name}") // read this value from Spring Configuration
	public void setAppName(String appName) {
		this.appName = appName;
	}

	@Override
	@Transactional // (readOnly = false)
	public Object invokeActivity(ActivityBoundary activity) {
		// Check that type is valid
		if (activity.getType() == null || activity.getType().isEmpty()) {
			throw new BadRequestException("Type cannot be empty or null");
		}

		// Check if InstanceId is null
		if (activity.getInstance() == null || activity.getInstance().getInstanceId() == null) {
			throw new BadRequestException("InstanceId can't be null");
		}

		// InstanceId not null, check if exist in domain
		if (!instanceDao.findById(activity.getInstance().getInstanceId()).isPresent()) {
			throw new BadRequestException("Instance " + activity.getInstance().getInstanceId().getId()
					+ " doesn't exist in domain " + activity.getInstance().getInstanceId().getDomain());
		}

		// Check if UserId is null
		if (activity.getInvokedBy() == null || activity.getInvokedBy().getUserId() == null) {
			throw new BadRequestException("UserId can't be null");
		}

		// UserId not null, check if exist in domain
		if (!userDao.findById(activity.getInvokedBy().getUserId()).isPresent()) {
			throw new BadRequestException("User " + activity.getInvokedBy().getUserId().getEmail()
					+ " doesn't exist in domain " + activity.getInvokedBy().getUserId().getDomain());
		}

		ActivityEntity entityToStore = this.converter.convertToEntity(activity);
		entityToStore.setDomain(appName);

		IdGenerator id = new IdGenerator();
		id = this.idGenerator.save(id);
		entityToStore.setId(id.getId());
		this.idGenerator.delete(id);

		entityToStore.setCreatedTimestamp(new Date());
		entityToStore = this.activityDao.save(entityToStore);
		return this.converter.convertToBoundary(entityToStore);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActivityBoundary> getAllActivities(String adminDomain, String adminEmail) {
		return StreamSupport.stream(this.activityDao.findAll().spliterator(), false)
				.map(this.converter::convertToBoundary).collect(Collectors.toList());
	}

	@Override
	@Transactional // (readOnly = false)
	@RolePermission(roles = { UserRole.ADMIN })
	public void deleteAllActivities(String adminDomain, String adminEmail) {
		this.activityDao.deleteAll();

	}

}
