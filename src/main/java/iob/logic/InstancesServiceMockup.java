package iob.logic;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import iob.attributes.CreatedBy;
import iob.attributes.UserId;
import iob.boundaries.InstanceBoundary;
import iob.converters.InstanceConverter;
import iob.data.InstanceEntity;
import iob.errors.NotFoundException;


@Service // declaration of Spring Bean of Business Logic (BL) layer
public class InstancesServiceMockup implements InstancesService {
	private Map<String, InstanceEntity> storage;
	private InstanceConverter converter;
	private AtomicLong counter;
	private String appName;
	
	@Autowired
	public InstancesServiceMockup(InstanceConverter converter) {
		this.converter = converter;
	}
	
	@Value("${spring.application.name}") // read this value from Spring Configuration
	public void setAppName(String appName) {
		this.appName = appName;
	}

	@PostConstruct
	public void init() {
		// initialize thread safe storage
		this.storage = Collections.synchronizedMap(new HashMap<>());
		
		// initialize counter
		this.counter = new AtomicLong(1L);		
	}
	
	@Override
	public InstanceBoundary createInstance(String userDomain, String userEmail, InstanceBoundary instance) {
		InstanceEntity entityToStore = this.converter
				.convertToEntity(instance);
		entityToStore.setId(this.counter.getAndIncrement());
		entityToStore.setDomain(this.appName);
		entityToStore.setCreatedBy(new CreatedBy(new UserId(userDomain, userEmail)).toString());
		entityToStore.setCreatedTimestamp(new Date());
		
		String key = this.converter.convertPropertiesToKey(appName, entityToStore.getId().toString());		
		this.storage.put(key, entityToStore);
			
		return this.converter.convertToBoundary(entityToStore);
	}
	
	@Override
	public InstanceBoundary updateInstance(String userDomain, String userEmail, String instanceDomain, String instanceId, InstanceBoundary update) {
		// a mockup implementation of the update operation
		
		String key = this.converter.convertPropertiesToKey(appName, instanceId);
		InstanceEntity existing = this.storage.get(key);
		
		// if original entity doesn't exist -> return http status 404
		if (existing == null) {
			throw new NotFoundException("Could not find instance with id: " + instanceId + "in domain: " + instanceDomain);
		}
		
		String createdBy = new CreatedBy(new UserId(userDomain, userEmail)).toString();
		if (!existing.getCreatedBy().equals(createdBy)) {
			throw new RuntimeException("The requested instance was created by: " + existing.getCreatedBy() + " but requested update for: " + createdBy);
		}
		
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
		
		if (dirty) {
			// update map => db update mockup
			this.storage.put(key, existing);

			// read again updated value from map
			existing = this.storage.get(key);
			if (existing == null) {
				throw new RuntimeException("Error while updating database");
			}
		}
		
		
		// convert entity to boundary and return it
		return this.converter.convertToBoundary(existing);
	}
	
	@Override
	public List<InstanceBoundary> getAllInstances(String userDomain, String userEmail) {
		String createdBy = new CreatedBy(new UserId(userDomain, userEmail)).toString();
		return this.storage // Map<String, InstanceEntity>
				    .values() // Collection<InstanceEntity>
					.stream() 	// Stream<InstanceEntity>
					.filter(i -> i.getCreatedBy().equals(createdBy)) // filter only instances created by userEmail @ userDomain
					.map(this.converter::convertToBoundary) // method reference that converts each boundary to entity and resulting Stream<InstanceEntity>
					.collect(Collectors.toList()); // List<InstanceEntity>
	}
	
	@Override
	public InstanceBoundary getSpecificInstance(String userDomain, String userEmail, String instanceDomain,	String instanceId) {
		String key = this.converter.convertPropertiesToKey(instanceDomain, instanceId);
		InstanceEntity entity = this.storage.get(key);
		
		if (entity == null) {	
			throw new NotFoundException("Could not find instance with id: " + instanceId + "in domain: " + instanceDomain);
		}
		
		String createdBy = new CreatedBy(new UserId(userDomain, userEmail)).toString();
		if (!entity.getCreatedBy().equals(createdBy)) {
			throw new RuntimeException("The requested instance was created by: " + entity.getCreatedBy() + " but requested for: " + createdBy);
		}
		
		return this.converter.convertToBoundary(entity);			
	}
	
	@Override
	public void deleteAllInstances(String adminDomain, String adminEmail) {
		this.storage.clear();
	}
}


