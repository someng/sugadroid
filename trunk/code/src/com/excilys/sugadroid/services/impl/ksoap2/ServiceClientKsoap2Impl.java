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

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.Transport;

import android.util.Log;

import com.excilys.sugadroid.services.exceptions.InvalidResponseException;
import com.excilys.sugadroid.services.exceptions.InvalidSessionException;
import com.excilys.sugadroid.services.exceptions.ServiceException;
import com.excilys.sugadroid.services.impl.ksoap2.beanFactories.Ksoap2BeanFactory;
import com.excilys.sugadroid.services.impl.ksoap2.beanFactories.exceptions.ParsingException;
import com.excilys.sugadroid.services.util.HTTPSHackUtil;

public abstract class ServiceClientKsoap2Impl {

	private static String TAG = ServiceClientKsoap2Impl.class.getSimpleName();
	protected static final String NAMESPACE = "http://www.sugarcrm.com/sugarcrm";

	private final String GET_ENTRY_METHOD_NAME = "get_entry";
	private final String GET_ENTRY_SOAP_ACTION = "get_entry";
	private final String GET_ENTRY_LIST_METHOD_NAME = "get_entry_list";
	private final String GET_ENTRY_LIST_SOAP_ACTION = "get_entry_list";

	private Transport androidHttpTransport;

	public ServiceClientKsoap2Impl() {

	}

	public Object sendRequest(final SoapObject request,
			final String soapAction, final String Url)
			throws InvalidResponseException {
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);

		if (androidHttpTransport == null) {

			HttpClientTransportAndroid androidTransport = new HttpClientTransportAndroid(
					Url);

			HttpClient client = new DefaultHttpClient();
			new HTTPSHackUtil().httpClientAllowAllSSL(client);
			androidTransport.setHttpClient(client);

			androidHttpTransport = androidTransport;

		}

		// TODO : set to false when debug is over.
		// if set to true, the response of the server will be dumped in the log
		// (if any error)
		androidHttpTransport.debug = false;

		Object response;
		try {
			androidHttpTransport.call(soapAction, envelope);
			response = envelope.getResponse();
		} catch (Exception e) {
			if (androidHttpTransport.debug) {
				Log.e(TAG, "response: \n" + androidHttpTransport.responseDump);
			}
			Log.e(TAG, Log.getStackTraceString(e));
			throw new InvalidResponseException(e);
		}

		// Uncomment to have permanent log of the response (only if
		// androidHttpTransport.debug = true)
		// Log.i(TAG, "response: \n" + androidHttpTransport.responseDump);

		return response;
	}

	public <Bean> Bean getEntry(SoapObject request, Class<Bean> beanClass,
			final String url) throws ServiceException {

		SoapObject response;

		response = (SoapObject) sendRequest(request, GET_ENTRY_SOAP_ACTION, url);

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

	public SoapObject newEntryRequest() {
		return new SoapObject(NAMESPACE, GET_ENTRY_METHOD_NAME);
	}

	public <Bean> List<Bean> getEntryList(SoapObject request,
			Class<Bean> beanClass, final String url) throws ServiceException {

		SoapObject response;

		response = (SoapObject) sendRequest(request,
				GET_ENTRY_LIST_SOAP_ACTION, url);

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

	public void checkErrorValue(SoapObject error) throws ServiceException {
		String errorNumber = (String) error.getProperty("number");

		if (!errorNumber.equals("0")) {

			String errorName = (String) error.getProperty("name") + " ("
					+ errorNumber + ")";
			String description = (String) error.getProperty("description");

			Log.i(TAG, errorName);
			Log.i(TAG, description);

			throw new ServiceException(errorName, description, errorNumber);
		}

	}

	public SoapObject newEntryListRequest() {
		return new SoapObject(NAMESPACE, GET_ENTRY_LIST_METHOD_NAME);
	}

}
