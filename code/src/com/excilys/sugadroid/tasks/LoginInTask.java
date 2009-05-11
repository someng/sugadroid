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

import android.util.Log;

import com.excilys.sugadroid.activities.interfaces.IAuthenticatingActivity;
import com.excilys.sugadroid.beans.interfaces.ISessionBean;
import com.excilys.sugadroid.services.exceptions.InvalidResponseException;
import com.excilys.sugadroid.services.exceptions.LoginFailedException;
import com.excilys.sugadroid.services.exceptions.ServiceException;
import com.excilys.sugadroid.services.interfaces.ILoginServices;

public class LoginInTask extends LoadingTask<IAuthenticatingActivity> {

	private static final String TAG = LoginInTask.class.getSimpleName();

	private final String username;
	private final String password;
	private final ILoginServices loginServices;
	private final ISessionBean sessionBean;

	public LoginInTask(IAuthenticatingActivity activity, String username,
			String password, ILoginServices loginServices,
			ISessionBean sessionBean) {
		super(activity);
		this.username = username;
		this.password = password;
		this.activity = activity;
		this.loginServices = loginServices;
		this.sessionBean = sessionBean;
	}

	@Override
	public void doRunLoadingTask() {
		String sessionId;
		String userId;
		String version;

		try {

			// If you want to login sending the MD5 of the password, just change
			// to the following code:
			// sessionId = loginServices.login(username, getMD5(password));
			sessionId = loginServices.login(username, password);

			userId = loginServices.getUserId(sessionId);

			version = loginServices.getServerVersion();

			sessionBean.setLoggedIn(sessionId, userId, username, loginServices
					.getEntryPoint(), version);

			activity.onLoginSuccessful();
		} catch (LoginFailedException e) {
			Log.e(TAG, Log.getStackTraceString(e));
			sessionBean.logout();
			activity.onLoginFailedBadCredentials();
		} catch (InvalidResponseException e) {
			Log.e(TAG, Log.getStackTraceString(e));
			sessionBean.logout();
			activity.onLoginFailedNoNetwork();
		} catch (ServiceException e) {
			Log.e(TAG, Log.getStackTraceString(e));
			sessionBean.logout();
			activity.onLoginFailed(e.getMessage());
		}

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
