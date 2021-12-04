package iob.data;

import java.util.Date;
import java.util.Map;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import iob.attributes.ActivityId;
import iob.converters.MapToStringConverter;

@Entity
@Table(name = "ACTIVITIES")
@IdClass(ActivityId.class)
public class ActivityEntity {
	private long id;
	private String domain;
	private String instanceDomain;
	private String instanceId;
	private String type;
	private Date createdTimestamp;
	private String invokedBy;
	private Map<String, Object> activityAttributes;

	public ActivityEntity(long id, String domain, String instanceDomain, String instanceId, String type,
			Date createdTimestamp, String invokedBy, Map<String, Object> activityAttributes) {
		super();
		this.id = id;
		this.domain = domain;
		this.instanceDomain = instanceDomain;
		this.instanceId = instanceId;
		this.type = type;
		this.createdTimestamp = createdTimestamp;
		this.invokedBy = invokedBy;
		this.activityAttributes = activityAttributes;
	}

	public ActivityEntity() {
	}

	@Id
	public long getId() {
		return id;
	}

	public void setId(long initId) {
		this.id = initId;
	}

	@Id
	public String getDomain() {
		return domain;
	}

	public void setDomain(String activityDomain) {
		this.domain = activityDomain;
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

	@Temporal(TemporalType.TIMESTAMP)
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

	// store this value as a very long string
	@Lob
	@Convert(converter = MapToStringConverter.class)
	public Map<String, Object> getActivityAttributes() {
		return activityAttributes;
	}

	public void setActivityAttributes(Map<String, Object> activityAttributes) {
		this.activityAttributes = activityAttributes;
	}

//	@Override
//	public String toString() {
//		return "ActivityEntity [initId=" + initId + ", activityDomain=" + activityDomain + ", instanceDomain="
//				+ instanceDomain + ", instanceId=" + instanceId + ", type=" + type + ", createdTimestamp="
//				+ createdTimestamp + ", invokedBy=" + invokedBy + ", activityAttributes=" + activityAttributes + "]";
//	}

	@Override
	public String toString() {
		return instanceDomain + instanceId;
	}

}
