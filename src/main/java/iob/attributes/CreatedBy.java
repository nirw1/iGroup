package iob.attributes;

public class CreatedBy {
	private UserId userId = null;

	public CreatedBy() {
		userId = new UserId();
	}

	public CreatedBy(UserId userId) {
		this.userId = userId;
	}
	
	public CreatedBy(String userId) {
		String[] userIdSplit = userId.split("@@");
		if (userIdSplit.length == 2) {
			this.userId = new UserId(userIdSplit[1], userIdSplit[0]);
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
}
