package integrative;

public class InstanceId {
	private static int instanceCount = 0;
	private String domain;
	private String id;

	public InstanceId() {
		this.id = Integer.toString(++instanceCount);
	}

	public InstanceId(String domain) {
		this();
		this.domain = domain;
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

}
