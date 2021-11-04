package boundaries;

import java.util.Date;
import java.util.Map;

import integrative.CreatedBy;
import integrative.InstanceId;

public class ActivityBoundary {
	private InstanceId activityId;
	private String type;
	private Date createdTimestamp;
	private CreatedBy invokedBy;
	private Map<String, Object> activityAttributes;
	
	public ActivityBoundary() {
		createdTimestamp = new Date();
	}

	public ActivityBoundary(InstanceId activityId, String type, CreatedBy invokedBy, Map<String, Object> activityAttributes) {
		this();
		this.activityId = activityId;
		this.type = type;
		this.invokedBy = invokedBy;
		this.activityAttributes = activityAttributes;
	}

	public InstanceId getActivityId() {
		return activityId;
	}

	public void setActivityId(InstanceId activityId) {
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
