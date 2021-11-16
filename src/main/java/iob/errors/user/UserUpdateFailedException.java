package iob.errors.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class UserUpdateFailedException extends RuntimeException {
	private static final long serialVersionUID = 5384801618173120541L;

	public UserUpdateFailedException() {
		super();
	}

	public UserUpdateFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserUpdateFailedException(String message) {
		super(message);
	}

	public UserUpdateFailedException(Throwable cause) {
		super(cause);
	}
	
	

}
