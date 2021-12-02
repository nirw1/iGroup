package iob.attributes;

import java.io.Serializable;

public class InstanceId implements Serializable{
	private static final long serialVersionUID = -238147502679170859L;
	private String domain;
	private String id;

	public InstanceId() {
	}

	public InstanceId(String domain) {
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
