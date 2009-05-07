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

import java.util.List;
import java.util.Vector;

import org.ksoap2.serialization.SoapObject;

import android.util.Log;

import com.excilys.sugadroid.beans.ISessionBean;
import com.excilys.sugadroid.beans.ISessionBean.SessionState;
import com.excilys.sugadroid.services.exceptions.InvalidSessionException;
import com.excilys.sugadroid.services.exceptions.NotLoggedInException;
import com.excilys.sugadroid.services.exceptions.ServiceException;
import com.excilys.sugadroid.services.impl.ksoap2.beanFactories.Ksoap2BeanFactory;
import com.excilys.sugadroid.services.impl.ksoap2.beanFactories.exceptions.ParsingException;

public abstract class AuthenticatedSugarServiceClientKsoap2Impl extends
		SugarServiceClientKsoap2Impl {

	private static String TAG = AuthenticatedSugarServiceClientKsoap2Impl.class
			.getSimpleName();

	private final String GET_ENTRY_METHOD_NAME = "get_entry";
	private final String GET_ENTRY_SOAP_ACTION = "get_entry";
	private final String GET_ENTRY_LIST_METHOD_NAME = "get_entry_list";
	private final String GET_ENTRY_LIST_SOAP_ACTION = "get_entry_list";

	protected ISessionBean sessionBean;

	protected void checkLoggedIn() throws NotLoggedInException {
		if (sessionBean.getState() != SessionState.LOGGED_IN) {
			throw new NotLoggedInException(
					"User should be logged in before trying to call this service");
		}
	}

	protected <Bean> Bean getEntry(SoapObject request, Class<Bean> beanClass)
			throws ServiceException {

		SoapObject response;

		response = (SoapObject) sendRequest(request, GET_ENTRY_SOAP_ACTION);

		try {
			checkErrorValue((SoapObject) response.getProperty("error"));
		} catch (ServiceException e) {
			if (e.getErrorNumber() != null && e.getErrorNumber().equals("10")) {
				InvalidSessionException exception = new InvalidSessionException(
						e.getMessage(), e.getDescription());
				exception.setSessionId((String) request.getProperty("session"));
				throw exception;
			} else {
				throw e;
			}
		}

		@SuppressWarnings("unchecked")
		SoapObject entryList = ((Vector<SoapObject>) response
				.getProperty("entry_list")).get(0);

		Bean bean;
		try {
			bean = Ksoap2BeanFactory.getInstance().parseBean(entryList,
					beanClass);
		} catch (ParsingException e) {
			throw new ServiceException(e);
		}

		return bean;
	}

	protected SoapObject newEntryRequest() {
		return newAuthenticatedRequest(GET_ENTRY_METHOD_NAME);
	}

	protected <Bean> List<Bean> getEntryList(SoapObject request,
			Class<Bean> beanClass) throws ServiceException {

		SoapObject response;

		response = (SoapObject) sendRequest(request, GET_ENTRY_LIST_SOAP_ACTION);

		try {
			checkErrorValue((SoapObject) response.getProperty("error"));
		} catch (ServiceException e) {
			if (e.getErrorNumber() != null && e.getErrorNumber().equals("10")) {
				InvalidSessionException exception = new InvalidSessionException(
						e.getMessage(), e.getDescription());
				exception.setSessionId((String) request.getProperty("session"));
				throw exception;
			} else {
				throw e;
			}
		}

		@SuppressWarnings("unchecked")
		Vector<SoapObject> entryList = (Vector<SoapObject>) response
				.getProperty("entry_list");

		List<Bean> beans;
		try {
			beans = Ksoap2BeanFactory.getInstance().parseBeanList(entryList,
					beanClass);
		} catch (ParsingException e) {
			throw new ServiceException(e);
		}

		Log.d(TAG, beans.size() + " elements found");

		return beans;
	}

	protected SoapObject newEntryListRequest() {
		return newAuthenticatedRequest(GET_ENTRY_LIST_METHOD_NAME);
	}

	protected SoapObject newAuthenticatedRequest(String methodName) {
		SoapObject request = new SoapObject(namespace, methodName);
		request.addProperty("session", sessionBean.getSessionId());
		return request;
	}

	public ISessionBean getSessionBean() {
		return sessionBean;
	}

	public void setSessionBean(ISessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

}
