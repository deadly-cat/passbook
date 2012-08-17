package home.ingvar.passbook.dao;

@SuppressWarnings("serial")
public class ResultException extends Exception {

	public ResultException() {
		super();
	}

	public ResultException(String message) {
		super(message);
	}

	public ResultException(Throwable cause) {
		super(cause);
	}

	public ResultException(String message, Throwable cause) {
		super(message, cause);
	}

}
