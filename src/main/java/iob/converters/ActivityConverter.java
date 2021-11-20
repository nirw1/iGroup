package iob.converters;

import org.springframework.stereotype.Component;

import iob.attributes.ActivityId;
import iob.attributes.CreatedBy;
import iob.attributes.InstanceId;
import iob.boundaries.ActivityBoundary;
import iob.data.ActivityEntity;

@Component
public class ActivityConverter {
	
	public ActivityBoundary convertToBoundary(ActivityEntity entity) {
		ActivityBoundary boundary = new ActivityBoundary();
		
		boundary.setActivityAttributes(entity.getActivityAttributes());
		boundary.setActivityId(new ActivityId(entity.getActivityDomain(), entity.getInitId().toString()));
		boundary.setCreatedTimestamp(entity.getCreatedTimestamp());
		boundary.setInstance(new InstanceId(entity.getInstanceDomain(), entity.getInstanceId()));
		boundary.setInvokedBy(new CreatedBy(entity.getInvokedBy()));
		boundary.setType(entity.getType());
		return boundary;
	}
	
	public ActivityEntity convertToEntity(ActivityBoundary boundary) {
		ActivityEntity entity = new ActivityEntity();
		
		//ActivityAttributes
		if(boundary.getActivityAttributes() != null)
			entity.setActivityAttributes(boundary.getActivityAttributes());
		else
			entity.setActivityAttributes(null);

		
		//TimeStamp
		if(boundary.getCreatedTimestamp() != null)
			entity.setCreatedTimestamp(boundary.getCreatedTimestamp());
		else
			entity.setCreatedTimestamp(null);
		

		//InstanceId
		if(boundary.getInstance().getDomain() != null)
			entity.setInstanceDomain(boundary.getInstance().getDomain());
		else
			entity.setInstanceDomain(null);
	
		
		if(boundary.getInstance().getId() != null)
			entity.setInstanceId(boundary.getInstance().getDomain());
		else
			entity.setInstanceId(null);
		
		
		//InvokedBy
		if(boundary.getInvokedBy() != null)
			entity.setInvokedBy(boundary.getInvokedBy().toString());
		else
			entity.setInvokedBy(null);
		
		
		//Type
		if(boundary.getType() != null)
			entity.setType(boundary.getType());
		else
			entity.setType(null);
		
		
		return entity;	
	}
	
	public String convertPropertiesToKey(String domain, String id) {
		return id + '@' + domain;
	}

}
