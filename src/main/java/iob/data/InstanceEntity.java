package iob.data;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class InstanceEntity {
	private Long id;
	private String domain;
	private String type;
	private String name;
	private boolean active;
	private Date createdTimestamp;
	private String createdBy;
	private double latitude;
	private double longitude;
	private Map<String, Object> instanceAttributes;
	private Set<InstanceEntity> children;
	private Set<InstanceEntity> parents;

	public InstanceEntity() {
		children = new HashSet<InstanceEntity>();
		parents = new HashSet<InstanceEntity>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public Map<String, Object> getInstanceAttributes() {
		return instanceAttributes;
	}

	public void setInstanceAttributes(Map<String, Object> instanceAttributes) {
		this.instanceAttributes = instanceAttributes;
	}
	
	public Set<InstanceEntity> getChildren() {
		return children;
	}

	public void setChildren(Set<InstanceEntity> children) {
		this.children = children;
	}
	
	public Set<InstanceEntity> getParents() {
		return parents;
	}

	public void setParents(Set<InstanceEntity> parents) {
		this.parents = parents;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(domain, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InstanceEntity other = (InstanceEntity) obj;
		return Objects.equals(domain, other.domain) && Objects.equals(id, other.id);
	}
}
