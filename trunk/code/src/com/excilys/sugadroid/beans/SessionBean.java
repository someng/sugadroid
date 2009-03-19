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

public class SessionBean {

	private static SessionBean	singleton;

	private String				sessionId;
	private String				username;
	private String				userId;
	private String				url;
	private String				version;
	private boolean				version4_5;
	private int					passwordHash;

	private SessionBean() {
	}

	public static synchronized SessionBean getInstance() {
		if (singleton == null) {
			singleton = new SessionBean();
		}
		return singleton;
	}

	public String getSessionId() {
		return sessionId;
	}

	public String getUsername() {
		return username;
	}

	public String getUserId() {
		return userId;
	}

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
	}

	public boolean isLoggedIn() {
		return sessionId != null && userId != null;
	}

	public synchronized void logout() {
		sessionId = null;
		username = null;
		userId = null;
		url = null;
		version = null;
		passwordHash = 0;
	}

	public String getUrl() {
		return url;
	}

	public String getVersion() {
		return version;
	}

	public boolean isVersion4_5() {
		return version4_5;
	}

	public int getPasswordHash() {
		return passwordHash;
	}

	public boolean checkLoginParamsChanged(String username, String url,
			int passwordHash) {

		return (!this.username.equals(username) || !this.url.equals(url) || this.passwordHash != passwordHash);
	}
}
