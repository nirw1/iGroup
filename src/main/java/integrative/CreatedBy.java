package integrative;

public class CreatedBy {
	private UserId userId;

	public CreatedBy() {
		userId = new UserId();
	}

	public CreatedBy(UserId userId) {
		this.userId = userId;
	}

	public UserId getUserId() {
		return userId;
	}

	public void setUserId(UserId userId) {
		this.userId = userId;
	}

}
