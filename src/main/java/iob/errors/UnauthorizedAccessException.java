package iob.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class UnauthorizedAccessException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UnauthorizedAccessException() {
		super();
	}

	public UnauthorizedAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnauthorizedAccessException(String message) {
		super(message);
	}

	public UnauthorizedAccessException(Throwable cause) {
		super(cause);
	}

}
