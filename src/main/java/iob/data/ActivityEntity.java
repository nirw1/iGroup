package iob.data;

import java.util.Date;
import java.util.Map;


public class ActivityEntity {
	private Long initId;
	private String activityDomain;
	private String instanceDomain;
	private String instanceId;
	private String type;
	private Date createdTimestamp;
	private String invokedBy;
	private Map<String, Object> activityAttributes;
	


	public ActivityEntity() {
	}



	public Long getInitId() {
		return initId;
	}



	public void setInitId(Long initId) {
		this.initId = initId;
	}



	public String getActivityDomain() {
		return activityDomain;
	}



	public void setActivityDomain(String activityDomain) {
		this.activityDomain = activityDomain;
	}



	public String getInstanceDomain() {
		return instanceDomain;
	}



	public void setInstanceDomain(String instanceDomain) {
		this.instanceDomain = instanceDomain;
	}



	public String getInstanceId() {
		return instanceId;
	}



	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
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



	public String getInvokedBy() {
		return invokedBy;
	}



	public void setInvokedBy(String invokedBy) {
		this.invokedBy = invokedBy;
	}



	public Map<String, Object> getActivityAttributes() {
		return activityAttributes;
	}



	public void setActivityAttributes(Map<String, Object> activityAttributes) {
		this.activityAttributes = activityAttributes;
	}



	@Override
	public String toString() {
		return "ActivityEntity [initId=" + initId + ", activityDomain=" + activityDomain + ", instanceDomain="
				+ instanceDomain + ", instanceId=" + instanceId + ", type=" + type + ", createdTimestamp="
				+ createdTimestamp + ", invokedBy=" + invokedBy + ", activityAttributes=" + activityAttributes + "]";
	}




	
}
