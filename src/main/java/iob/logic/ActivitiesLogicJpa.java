package iob.logic;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iob.boundaries.ActivityBoundary;
import iob.converters.ActivityConverter;
import iob.daos.ActivityDao;
import iob.data.ActivityEntity;
import iob.errors.BadRequestException;

@Service
public class ActivitiesLogicJpa implements ActivitiesService {
	private ActivityDao activityDao;
	private ActivityConverter converter;
	private String appName;

	@Autowired
	public ActivitiesLogicJpa(ActivityDao activityDao, ActivityConverter converter) {
		this.activityDao = activityDao;
		this.converter = converter;
	}

	@Value("${spring.application.name}") // read this value from Spring Configuration
	public void setAppName(String appName) {
		this.appName = appName;
	}

	@Override
	@Transactional // (readOnly = false)
	public Object invokeActivity(ActivityBoundary activity) {
		// check that the type of the activity is valid
		if (activity.getType() == null || activity.getType().isEmpty()) {
			throw new BadRequestException("Type cannot be empty or null");
		}
		// check that the id of the instance that the activity invokes on exist
		if (activity.getInstance().getInstanceId() == null) {
			throw new BadRequestException("There is no instance that match to this activity");
		}
		// check that the details of the invoker user exist
		if (activity.getInvokedBy().getUserId() == null) {
			throw new BadRequestException("Invoker details are missing");
		}
		ActivityEntity entityToStore = this.converter.convertToEntity(activity);
		entityToStore.setDomain(appName);
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
	public void deleteAllActivities(String adminDomain, String adminEmail) {
		this.activityDao.deleteAll();

	}

}
