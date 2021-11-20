package iob.attributes;

public class ActivityId {
	private String domain;
	private String id;

	public ActivityId() {

	}

	public ActivityId(String domain, String id) {
		this.domain = domain;
		this.id = id;
	}
	
	public ActivityId(String domain, long id) {
		this.domain = domain;
		this.id = String.valueOf(id);
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
