package iob.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidInfoException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidInfoException() {
		super();
	}

	public InvalidInfoException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidInfoException(String message) {
		super(message);
	}

	public InvalidInfoException(Throwable cause) {
		super(cause);
	}

}
