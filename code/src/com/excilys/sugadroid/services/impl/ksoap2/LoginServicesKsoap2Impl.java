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

package com.excilys.sugadroid.services.impl.ksoap2;

import org.ksoap2.serialization.SoapObject;

import android.util.Log;

import com.excilys.sugadroid.services.exceptions.LoginFailedException;
import com.excilys.sugadroid.services.exceptions.ServiceException;
import com.excilys.sugadroid.services.interfaces.ILoginServices;

public class LoginServicesKsoap2Impl extends SugarServiceClientKsoap2Impl
		implements ILoginServices {

	private static String TAG = LoginServicesKsoap2Impl.class.getSimpleName();

	public LoginServicesKsoap2Impl() {
	};

	@Override
	public String login(String username, String password, String url)
			throws ServiceException {

		Log.d(TAG, "login called");

		final String SOAP_ACTION = "login";
		final String METHOD_NAME = "login";

		SoapObject request = new SoapObject(namespace, METHOD_NAME);

		// Creating input parameters
		SoapObject userAuth = new SoapObject(namespace, "user_auth");
		userAuth.addProperty("user_name", username);
		userAuth.addProperty("password", password);
		userAuth.addProperty("version", "1.0");

		request.addProperty("user_auth", userAuth);
		SoapObject response;

		response = (SoapObject) sendRequest(request, SOAP_ACTION);

		try {
			checkErrorValue((SoapObject) response.getProperty("error"));
		} catch (ServiceException e) {
			if (e.getErrorNumber() != null && e.getErrorNumber().equals("10")) {
				throw new LoginFailedException(e.getMessage(), e
						.getDescription());
			} else {
				throw e;
			}
		}

		Log.d(TAG, "Logged in.");

		return (String) response.getProperty("id");

	}

	@Override
	public String getUserId(String sessionId, String url)
			throws ServiceException {

		Log.d(TAG, "getUserId called");

		final String SOAP_ACTION = "get_user_id";
		final String METHOD_NAME = "get_user_id";

		SoapObject request = new SoapObject(namespace, METHOD_NAME);

		request.addProperty("session", sessionId);

		String userId;

		userId = (String) sendRequest(request, SOAP_ACTION);

		Log.d(TAG, "user id: " + userId);

		return userId;
	}

	@Override
	public String getServerVersion(String url) throws ServiceException {
		final String SOAP_ACTION = "get_server_version";
		final String METHOD_NAME = "get_server_version";

		SoapObject request = new SoapObject(namespace, METHOD_NAME);

		String serverVersion;

		serverVersion = (String) sendRequest(request, SOAP_ACTION);

		return serverVersion;
	}

}
