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

package com.excilys.sugadroid.tasks;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.excilys.sugadroid.activities.ConnectionSettings;
import com.excilys.sugadroid.activities.MenuActivity;
import com.excilys.sugadroid.activities.delegates.DialogManager;
import com.excilys.sugadroid.beans.SessionBean;
import com.excilys.sugadroid.services.ServiceFactory;
import com.excilys.sugadroid.services.exceptions.InvalidResponseException;
import com.excilys.sugadroid.services.exceptions.LoginFailedException;
import com.excilys.sugadroid.services.exceptions.ServiceException;
import com.excilys.sugadroid.services.interfaces.ILoginServices;

public class LoginInTask implements Runnable {

	private final String username;
	private final String password;
	private final MenuActivity activity;

	public LoginInTask(MenuActivity activity, String username, String password) {
		this.username = username;
		this.password = password;
		this.activity = activity;
	}

	public void run() {
		String sessionId;
		String userId;
		String version;

		String url = ConnectionSettings.getSugarSoapUrl(activity);

		try {

			ILoginServices services = ServiceFactory.getInstance()
					.getLoginServices();

			// If you want to login sending the MD5 of the password, just change
			// to the following code:
			// sessionId = services.login(username, getMD5(password), url);
			sessionId = services.login(username, password, url);

			userId = services.getUserId(sessionId, url);

			version = services.getServerVersion(url);

		} catch (LoginFailedException e) {
			rollbackFromLogin();
			activity.postShowDialog(DialogManager.DIALOG_ERROR_LOGIN_FAILED);
			return;
		} catch (InvalidResponseException e) {
			rollbackFromLogin();
			activity
					.postShowDialog(DialogManager.DIALOG_ERROR_INVALID_RESPONSE);
			return;
		} catch (ServiceException e) {
			rollbackFromLogin();
			activity.postShowCustomDialog(e.getMessage(), e.getDescription());
			return;
		}

		SessionBean.getInstance().setLoggedIn(sessionId, userId, username,
				password.hashCode(), url, version);

		activity.postSetMessageLoggedIn();

		activity.forwardAfterLogin();

	}

	private void rollbackFromLogin() {
		SessionBean.getInstance().logout();
		activity.postSetMessageNotLoggedIn();
	}

	/**
	 * This method could be helpful for some SugarCRM setups that use MD5
	 * passwords
	 * 
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unused")
	private String getMD5(String value) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			return "";
		}
		md5.reset();
		md5.update(value.getBytes());
		byte[] messageDigest = md5.digest();

		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < messageDigest.length; i++) {
			hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
		}
		String hashedPassword = hexString.toString();

		return hashedPassword;
	}
}
