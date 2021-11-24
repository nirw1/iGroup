package iob.attributes;

import java.io.Serializable;

public class UserId implements Serializable{
	private String domain;
	private String email;

	public UserId() {

	}

	public UserId(String domain, String email) {
		this.domain = domain;
		this.email = email;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
