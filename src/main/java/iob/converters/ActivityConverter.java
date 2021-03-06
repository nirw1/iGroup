package iob.converters;

import org.springframework.stereotype.Component;

import iob.attributes.ActivityId;
import iob.attributes.CreatedBy;
import iob.attributes.Instance;
import iob.attributes.InstanceId;
import iob.boundaries.ActivityBoundary;
import iob.data.ActivityEntity;

@Component
public class ActivityConverter {

	public ActivityBoundary convertToBoundary(ActivityEntity entity) {
		ActivityBoundary boundary = new ActivityBoundary();
		boundary.setActivityAttributes(entity.getActivityAttributes());
		boundary.setActivityId(new ActivityId(entity.getDomain(), entity.getId()));
		boundary.setCreatedTimestamp(entity.getCreatedTimestamp());
		boundary.setInstance(new Instance(new InstanceId(entity.getInstanceDomain(), entity.getInstanceId())));
		boundary.setInvokedBy(new CreatedBy(entity.getInvokedBy()));
		boundary.setType(entity.getType());
		return boundary;
	}

	public ActivityEntity convertToEntity(ActivityBoundary boundary) {
		ActivityEntity entity = new ActivityEntity();

		// ActivityAttributes
		if (boundary.getActivityAttributes() != null) {
			entity.setActivityAttributes(boundary.getActivityAttributes());
		} else
			entity.setActivityAttributes(null);

		// TimeStamp
		if (boundary.getCreatedTimestamp() != null)
			entity.setCreatedTimestamp(boundary.getCreatedTimestamp());
		else
			entity.setCreatedTimestamp(null);

		// InstanceId
		if (boundary.getInstance() != null) {
			entity.setInstanceDomain(boundary.getInstance().domain());
			entity.setInstanceId(boundary.getInstance().id());
		} else {
			entity.setInstanceDomain("");
			entity.setInstanceId("");
		}

		// InvokedBy
		if (boundary.getInvokedBy() != null)
			entity.setInvokedBy(boundary.getInvokedBy().toString());
		else
			entity.setInvokedBy("");

		// Type
		if (boundary.getType() != null)
			entity.setType(boundary.getType());
		else
			entity.setType("");

		return entity;
	}

	public String convertPropertiesToKey(String domain, long id) {
		return String.valueOf(id) + "@@" + domain;
	}

}
