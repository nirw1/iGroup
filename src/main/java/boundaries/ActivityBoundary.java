package boundaries;

import java.util.Date;
import java.util.Map;

import integrative.ActivityId;
import integrative.CreatedBy;
import integrative.InstanceId;

public class ActivityBoundary {
	private ActivityId activityId;
	private Map<String, InstanceId> instance;
	private String type;
	private Date createdTimestamp;
	private CreatedBy invokedBy;
	private Map<String, Object> activityAttributes;

	public ActivityBoundary() {
		createdTimestamp = new Date();
	}

	public ActivityBoundary(ActivityId activityId, Map<String, InstanceId> instance, String type, CreatedBy invokedBy,
			Map<String, Object> activityAttributes) {
		this();
		this.activityId = activityId;
		this.instance = instance;
		this.type = type;
		this.invokedBy = invokedBy;
		this.activityAttributes = activityAttributes;
	}

	public Map<String, InstanceId> getInstance() {
		return instance;
	}

	public void setInstance(Map<String, InstanceId> instance) {
		this.instance = instance;
	}

	public ActivityId getActivityId() {
		return activityId;
	}

	public void setActivityId(ActivityId activityId) {
		this.activityId = activityId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public CreatedBy getInvokedBy() {
		return invokedBy;
	}

	public void setInvokedBy(CreatedBy invokedBy) {
		this.invokedBy = invokedBy;
	}

	public Map<String, Object> getActivityAttributes() {
		return activityAttributes;
	}

	public void setActivityAttributes(Map<String, Object> activityAttributes) {
		this.activityAttributes = activityAttributes;
	}
}
