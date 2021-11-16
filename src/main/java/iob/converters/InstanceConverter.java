package iob.converters;

import org.springframework.stereotype.Component;

import iob.attributes.CreatedBy;
import iob.attributes.InstanceId;
import iob.attributes.Location;
import iob.attributes.UserId;
import iob.boundaries.InstanceBoundary;
import iob.data.InstanceEntity;

@Component
public class InstanceConverter {
	public InstanceBoundary convertToBoundary(InstanceEntity entity) {
		InstanceBoundary boundary = new InstanceBoundary();
		
		boundary.setActive(entity.getActive());
		
		String[] createdBy = entity.getCreatedBy().split("@@");
		boundary.setCreatedBy(new CreatedBy(new UserId(createdBy[1], createdBy[0])));
		
		boundary.setCreatedTimestamp(entity.getCreatedTimestamp());	
		boundary.setInstanceAttributes(entity.getInstanceAttributes());
		
		String[] instanceId = entity.getInstanceId().split("@");
		boundary.setInstanceId(new InstanceId(instanceId[1], instanceId[0]));
		
		boundary.setLocation(new Location(entity.getLatitude(), entity.getLongitude()));
		boundary.setName(entity.getName());
		boundary.setType(entity.getType());
		
		return boundary;
	}
	
	public InstanceEntity convertToEntity (InstanceBoundary boundary) {
		InstanceEntity entity = new InstanceEntity();

		entity.setActive(boundary.getActive());
		
		if (boundary.getCreatedBy() != null) {
			entity.setCreatedBy(String.format("%s@@%s", boundary.getCreatedBy().getUserId().getEmail(), boundary.getCreatedBy().getUserId().getDomain()));
		}
		else {
			entity.setCreatedBy("");
		}
		
		entity.setCreatedTimestamp(boundary.getCreatedTimestamp());
		entity.setInstanceAttributes(boundary.getInstanceAttributes());
		
		if (boundary.getInstanceId() != null) {
			entity.setInstanceId(String.format("%s@%s", boundary.getInstanceId().getId(), boundary.getInstanceId().getDomain()));
		}
		else {
			entity.setInstanceId("");
		}
		
		if (boundary.getLocation() != null) {
			entity.setLatitude(boundary.getLocation().getLat());
			entity.setLongitude(boundary.getLocation().getLng());
		}
		else {
			entity.setLatitude(0.0);
			entity.setLongitude(0.0);
		}

		entity.setName(boundary.getName());
		entity.setType(boundary.getType());
		
		return entity;
	}
}
