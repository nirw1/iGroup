package iob.attributes;

import java.io.Serializable;
import java.util.Objects;

public class ActivityId implements Serializable {
	private static final long serialVersionUID = 1L;
	private String domain;
	private long id;

	public ActivityId() {
	}

	public ActivityId(String domain, long id) {
		this.domain = domain;
		this.id = id;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
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
		ActivityId other = (ActivityId) obj;
		return Objects.equals(domain, other.domain) && id == other.id;
	}

}
