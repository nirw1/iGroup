package iob.attributes;

import java.io.Serializable;
import java.util.Objects;

public class UserId implements Serializable {
	private static final long serialVersionUID = 1L;
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

	@Override
	public int hashCode() {
		return Objects.hash(domain, email);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserId other = (UserId) obj;
		return Objects.equals(domain, other.domain) && Objects.equals(email, other.email);
	}

}
