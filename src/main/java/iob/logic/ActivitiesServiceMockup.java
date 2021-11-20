package iob.logic;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import iob.data.ActivityEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import iob.attributes.CreatedBy;
import iob.attributes.UserId;
import iob.boundaries.ActivityBoundary;
import iob.converters.ActivityConverter;

@Service
public class ActivitiesServiceMockup implements ActivitiesService {
	private Map<String, ActivityEntity> storage;
	private ActivityConverter converter;
	private AtomicLong counter;
	private String appName;

	@Autowired
	public ActivitiesServiceMockup(ActivityConverter converter) {
		this.converter = converter;
	}

	@Value("${spring.application.name}") // read this value from Spring Configuration
	public void setAppName(String appName) {
		this.appName = appName;
	}

	@PostConstruct
	public void init() {
		this.storage = Collections.synchronizedMap(new HashMap<>());
		this.counter = new AtomicLong(1l);
	}

	@Override
	public Object invokeActivity(ActivityBoundary activity) {
		ActivityEntity entityToStore = this.converter.convertToEntity(activity);
		entityToStore.setInitId(this.counter.getAndIncrement());
		entityToStore.setCreatedTimestamp(new Date());
		entityToStore.setActivityDomain(appName);
		String key = this.converter.convertPropertiesToKey(appName, entityToStore.getInitId());
		this.storage.put(key, entityToStore);
		return this.converter.convertToBoundary(entityToStore);
	}

	@Override
	public List<ActivityBoundary> gatAllActivities(String adminDomain, String adminEmail) {
		String invokeBy = new CreatedBy(new UserId(adminDomain, adminEmail)).toString();
		return this.storage.values().stream().filter(i -> i.getInvokedBy().equals(invokeBy))
				.map(this.converter::convertToBoundary).collect(Collectors.toList());
	}

	@Override
	public void deleteAllActivities(String adminDomain, String adminEmail) {
//		String invokeBy = new CreatedBy(new UserId(adminDomain, adminEmail)).toString();
		// TODO Auto-generated method stub
		this.storage.clear();

	}

}
