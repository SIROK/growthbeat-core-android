package jp.co.sirok.hub;

public class HubException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public HubException() {
		super();
	}

	public HubException(String message) {
		super(message);
	}

	public HubException(Throwable throwable) {
		super(throwable);
	}

	public HubException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
