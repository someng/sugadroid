package com.excilys.sugadroid.services.exceptions;

public class InvalidSessionException extends ServiceException {

	private static final long serialVersionUID = 1L;

	private String sessionId;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public InvalidSessionException() {
		// TODO Auto-generated constructor stub
	}

	public InvalidSessionException(String detailMessage) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}

	public InvalidSessionException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

	public InvalidSessionException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
		// TODO Auto-generated constructor stub
	}

	public InvalidSessionException(String error, String description) {
		super(error, description);
		// TODO Auto-generated constructor stub
	}

	public InvalidSessionException(String error, String description,
			String errorNumber) {
		super(error, description, errorNumber);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getDescription() {
		if (sessionId != null) {
			return super.getDescription() + " | Session id: " + sessionId;
		} else {
			return super.getDescription();
		}
	}

}
