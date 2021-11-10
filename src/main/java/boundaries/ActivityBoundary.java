package boundaries;

import java.util.Date;

import integrative.ActivityId;
import integrative.Attributes;
import integrative.CreatedBy;
import integrative.InstanceId;

public class ActivityBoundary {
	private ActivityId activityId;
	private InstanceId instance;
	private String type;
	private Date createdTimestamp;
	private CreatedBy invokedBy;
	private Attributes activityAttributes;

	public ActivityBoundary() {
		createdTimestamp = new Date();
	}

	public ActivityBoundary(ActivityId activityId, InstanceId instance, String type, Date createdTimestamp,
			CreatedBy invokedBy, Attributes activityAttributes) {
		super();
		this.activityId = activityId;
		this.instance = instance;
		this.type = type;
		this.createdTimestamp = createdTimestamp;
		this.invokedBy = invokedBy;
		this.activityAttributes = activityAttributes;
	}

	public InstanceId getInstance() {
		return instance;
	}

	public void setInstance(InstanceId instance) {
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

	public Attributes getActivityAttributes() {
		return activityAttributes;
	}

	public void setActivityAttributes(Attributes activityAttributes) {
		this.activityAttributes = activityAttributes;
	}
}
