package iob.converters;

import org.springframework.stereotype.Component;

import iob.attributes.CreatedBy;
import iob.attributes.Location;
import iob.boundaries.InstanceBoundary;
import iob.boundaries.InstanceIdBoundary;
import iob.data.InstanceEntity;

@Component
public class InstanceConverter {
	public InstanceBoundary convertToBoundary(InstanceEntity entity) {
		InstanceBoundary boundary = new InstanceBoundary();

		boundary.setActive(entity.getActive());
		boundary.setCreatedBy(new CreatedBy(entity.getCreatedBy()));
		boundary.setCreatedTimestamp(entity.getCreatedTimestamp());
		boundary.setInstanceAttributes(entity.getInstanceAttributes());
		boundary.setInstanceId(new InstanceIdBoundary(entity.getDomain(), entity.getId().toString()));
		boundary.setLocation(new Location(entity.getLatitude(), entity.getLongitude()));
		boundary.setName(entity.getName());
		boundary.setType(entity.getType());

		return boundary;
	}

	public InstanceEntity convertToEntity(InstanceBoundary boundary) {
		InstanceEntity entity = new InstanceEntity();

		entity.setActive(boundary.getActive());

		if (boundary.getCreatedBy() != null) {
			entity.setCreatedBy(boundary.getCreatedBy().toString());
		} else {
			entity.setCreatedBy("");
		}

		entity.setCreatedTimestamp(boundary.getCreatedTimestamp());
		entity.setInstanceAttributes(boundary.getInstanceAttributes());

		// ignore instance Id

		if (boundary.getLocation() != null) {
			entity.setLatitude(boundary.getLocation().getLat());
			entity.setLongitude(boundary.getLocation().getLng());
		} else {
			entity.setLatitude(0.0);
			entity.setLongitude(0.0);
		}

		entity.setName(boundary.getName());
		entity.setType(boundary.getType());

		return entity;
	}

	public String convertPropertiesToKey(String domain, String id) {
		return id + '@' + domain;
	}
}
