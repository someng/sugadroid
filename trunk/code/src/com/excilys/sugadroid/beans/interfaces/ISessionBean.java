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

package com.excilys.sugadroid.beans.interfaces;

public interface ISessionBean {

	public enum SessionState {
		LOGGED_IN, NOT_LOGGED_IN, LOGIN_IN;
	}

	public abstract String getSessionId();

	public abstract String getUsername();

	public abstract String getUserId();

	/**
	 * Register session informations
	 * 
	 * @param sessionId
	 * @param userId
	 * @param username
	 * @param url
	 * @param version
	 */
	public abstract void setLoggedIn(String sessionId, String userId,
			String username, String url, String version);

	/**
	 * Clean session informations from session bean
	 * 
	 */
	public abstract void logout();

	public abstract String getUrl();

	public abstract String getVersion();

	public abstract boolean isVersion4_5();

	/**
	 * Record in session bean that the user is currently login in. Used to avoid
	 * multiple login attempts at the same time.
	 */
	public abstract void setLoginIn();

	public abstract SessionState getState();

}