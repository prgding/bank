package exceptions;

public class UnknownException extends Exception {
	public UnknownException() {
	}

	public UnknownException(String message) {
		super(message);
	}
}
