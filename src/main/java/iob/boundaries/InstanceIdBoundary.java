package iob.boundaries;

public class InstanceIdBoundary {
	private String domain;
	private String id;

	public InstanceIdBoundary() {
	}

	public InstanceIdBoundary(String domain) {
		this.domain = domain;
	}
	
	public InstanceIdBoundary(String domain, String id) {
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

}
