package boundaries;

import java.util.Date;

public class MessageBoundary {
	private String message;
	private Boolean important;
	private Date messageTimestamp;
	private HelperBoundary helper;

	public MessageBoundary() {
		this.helper = new HelperBoundary();
		this.helper.setContent("no content");
	}

	public MessageBoundary(String message) {
		this();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getImportant() {
		return important;
	}

	public void setImportant(Boolean important) {
		this.important = important;
	}

	public Date getMessageTimestamp() {
		return messageTimestamp;
	}

	public void setMessageTimestamp(Date messageTimestamp) {
		this.messageTimestamp = messageTimestamp;
	}

	public HelperBoundary getHelper() {
		return helper;
	}

	public void setHelper(HelperBoundary helper) {
		this.helper = helper;
	}

}
