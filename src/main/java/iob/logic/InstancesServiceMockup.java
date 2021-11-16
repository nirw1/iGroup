package iob.logic;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import iob.attributes.CreatedBy;
import iob.attributes.UserId;
import iob.boundaries.InstanceBoundary;
import iob.converters.InstanceConverter;
import iob.data.InstanceEntity;


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
	public void setAfekaBasicMessage(String appName) {
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
		entityToStore.setInstanceId(String.format("%d@%s", this.counter.getAndIncrement(), appName));
		
		this.storage.put(entityToStore.getInstanceId(), entityToStore);
			
		return this.converter.convertToBoundary(entityToStore);
	}
	
	@Override
	public InstanceBoundary updateInstance(String userDomain, String userEmail, String instanceDomain, String instanceId,
			InstanceBoundary update) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<InstanceBoundary> getAllInstances(String userDomain, String userEmail) {
		List<InstanceBoundary> allInstances;
		return null;
	}
	
	@Override
	public InstanceBoundary getSpecificInstance(String userDomain, String userEmail, String instanceDomain,	String instanceId) {
		InstanceEntity entity = this.storage.get(String.format("%s@%s", instanceId, instanceDomain));
		
		if (entity != null) {	
			InstanceBoundary boundary = this.converter.convertToBoundary(entity);			
			return boundary;
		}else {
			//throw new MessageNotFoundException("Could not find message with id: " + id);
			throw new RuntimeException("Could not find message with id: " + instanceId + "in domain: " + instanceDomain); // returns error status 500
		}
	}
	
	@Override
	public void deleteAllInstances(String adminDomain, String adminEmail) {
		// TODO Auto-generated method stub
	}

	
}


