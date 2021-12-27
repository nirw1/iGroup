package iob.attributes;

import java.util.Objects;

public class CreatedBy {
	private UserId userId = null;

	public CreatedBy() {
		userId = new UserId();
	}

	public CreatedBy(UserId userId) {
		this.userId = userId;
	}

	public CreatedBy(String userId) {
		String[] userIdSplit;
		try {
			userIdSplit = userId.split("@@");
			if (userIdSplit.length == 2) {
				this.userId = new UserId(userIdSplit[1], userIdSplit[0]);
			}
		} catch (NullPointerException e) {
			this.userId = new UserId();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public UserId getUserId() {
		return userId;
	}

	public void setUserId(UserId userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		if (userId == null)
			return "";

		return this.userId.getEmail() + "@@" + this.userId.getDomain();
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreatedBy other = (CreatedBy) obj;
		return Objects.equals(userId, other.userId);
	}

}
