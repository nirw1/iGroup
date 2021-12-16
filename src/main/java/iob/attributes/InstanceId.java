package iob.attributes;

import java.io.Serializable;
import java.util.Objects;

public class InstanceId implements Serializable{
	private static final long serialVersionUID = -238147502679170859L;
	private String domain;
	private String id;

	public InstanceId() {
	}
	
	public InstanceId(String domain, String id) {
		this.domain = domain;
		this.id = id;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
		InstanceId other = (InstanceId) obj;
		return Objects.equals(domain, other.domain) && Objects.equals(id, other.id);
	}
}
