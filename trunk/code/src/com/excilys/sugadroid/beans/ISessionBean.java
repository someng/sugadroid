package com.excilys.sugadroid.beans;


public interface ISessionBean {

	public enum SessionState {
		LOGGED_IN, NOT_LOGGED_IN, LOGIN_IN;
	}

	public abstract String getSessionId();

	public abstract String getUsername();

	public abstract String getUserId();

	public abstract void setLoggedIn(String sessionId, String userId,
			String username, int passwordHash, String url, String version);

	public abstract boolean isLoggedIn();

	public abstract void logout();

	public abstract String getUrl();

	public abstract String getVersion();

	public abstract boolean isVersion4_5();

	public abstract int getPasswordHash();

	public abstract boolean checkLoginParamsChanged(String username,
			String url, int passwordHash);

	/**
	 * Record in session bean that the user is currently login in. Used to avoid
	 * multiple login attempts at the same time.
	 */
	public abstract void setLoginIn();

	public abstract SessionState getState();

}