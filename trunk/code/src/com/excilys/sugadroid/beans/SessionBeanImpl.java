/* ============================================================================
 *
 * Copyright 2009 eBusiness Information - Excilys group
 *
 * Author: Pierre-Yves Ricau (py.ricau+sugadroid@gmail.com)
 *
 * Company contact: ebi@ebusinessinformation.fr
 *
 * This file is part of SugaDroid.
 *
 * SugaDroid is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SugaDroid is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SugaDroid.  If not, see <http://www.gnu.org/licenses/>.
 * ============================================================================
 */

package com.excilys.sugadroid.beans;

import java.util.StringTokenizer;

public class SessionBeanImpl implements ISessionBean {

	private String sessionId;
	private String username;
	private String userId;
	private String url;
	private String version;
	private boolean version4_5;
	private int passwordHash;
	private SessionState state = SessionState.NOT_LOGGED_IN;

	/**
	 * 
	 * @see com.excilys.sugadroid.beans.ISessionBean#getSessionId()
	 */
	public synchronized String getSessionId() {
		return sessionId;
	}

	/**
	 * @see com.excilys.sugadroid.beans.ISessionBean#getUsername()
	 */
	public synchronized String getUsername() {
		return username;
	}

	/**
	 * 
	 * @see com.excilys.sugadroid.beans.ISessionBean#getUserId()
	 */
	public synchronized String getUserId() {
		return userId;
	}

	/**
	 * 
	 * @see com.excilys.sugadroid.beans.ISessionBean#setLoggedIn(java.lang.String,
	 *      java.lang.String, java.lang.String, int, java.lang.String,
	 *      java.lang.String)
	 */
	public synchronized void setLoggedIn(String sessionId, String userId,
			String username, int passwordHash, String url, String version) {
		this.sessionId = sessionId;
		this.username = username;
		this.userId = userId;
		this.url = url;
		this.version = version;
		this.passwordHash = passwordHash;

		StringTokenizer stk = new StringTokenizer(version, ".");

		if (stk.nextToken().equals("4") && stk.nextToken().equals("5")) {
			version4_5 = true;
		} else {
			version4_5 = false;
		}
		state = SessionState.LOGGED_IN;
	}

	/**
	 * 
	 * @see com.excilys.sugadroid.beans.ISessionBean#isLoggedIn()
	 */
	public synchronized boolean isLoggedIn() {
		return state == SessionState.LOGGED_IN;
	}

	/**
	 * 
	 * @see com.excilys.sugadroid.beans.ISessionBean#logout()
	 */
	public synchronized void logout() {
		sessionId = null;
		username = null;
		userId = null;
		url = null;
		version = null;
		passwordHash = 0;
		state = SessionState.NOT_LOGGED_IN;
	}

	/**
	 * 
	 * @see com.excilys.sugadroid.beans.ISessionBean#getUrl()
	 */
	public synchronized String getUrl() {
		return url;
	}

	/**
	 * 
	 * @see com.excilys.sugadroid.beans.ISessionBean#getVersion()
	 */
	public synchronized String getVersion() {
		return version;
	}

	/**
	 * 
	 * @see com.excilys.sugadroid.beans.ISessionBean#isVersion4_5()
	 */
	public synchronized boolean isVersion4_5() {
		return version4_5;
	}

	/**
	 * 
	 * @see com.excilys.sugadroid.beans.ISessionBean#getPasswordHash()
	 */
	public synchronized int getPasswordHash() {
		return passwordHash;
	}

	/**
	 * s
	 * 
	 * @see com.excilys.sugadroid.beans.ISessionBean#checkLoginParamsChanged(java
	 *      .lang.String, java.lang.String, int)
	 */
	public synchronized boolean checkLoginParamsChanged(String username,
			String url, int passwordHash) {

		return !this.username.equals(username) || !this.url.equals(url)
				|| this.passwordHash != passwordHash;
	}

	public synchronized SessionState getState() {
		return state;
	}

	public synchronized void setLoginIn() {
		logout();
		state = SessionState.LOGIN_IN;
	}
}
