package iob.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class UpdateFailedException extends RuntimeException {
	private static final long serialVersionUID = 5384801618173120541L;

	public UpdateFailedException() {
		super();
	}

	public UpdateFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public UpdateFailedException(String message) {
		super(message);
	}

	public UpdateFailedException(Throwable cause) {
		super(cause);
	}
	
	

}
