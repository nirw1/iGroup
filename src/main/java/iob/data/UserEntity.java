package iob.data;

public class UserEntity {
	private String domain;
	private String email;
	private String role;
	private String username;
	private String avatar;
	
	public UserEntity() {
		super();
	}
	
	public UserEntity(String domain, String email, String role, String username, String avatar) {
		super();
		this.domain = domain;
		this.email = email;
		this.role = role;
		this.username = username;
		this.avatar = avatar;
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	
	
	
}
