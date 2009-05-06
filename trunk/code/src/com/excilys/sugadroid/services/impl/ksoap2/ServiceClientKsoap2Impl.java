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

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.Transport;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.excilys.sugadroid.services.exceptions.InvalidResponseException;
import com.excilys.sugadroid.services.interfaces.IWebService;

public abstract class ServiceClientKsoap2Impl implements IWebService {

	private static String TAG = ServiceClientKsoap2Impl.class.getSimpleName();

	protected String namespace;

	private Transport transport;

	public Object sendRequest(final SoapObject request, final String soapAction)
			throws InvalidResponseException {

		Log.d(TAG, "Calling action [" + soapAction + "] on service ["
				+ transport.getUrl() + "]");

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);

		envelope.setOutputSoapObject(request);

		Object response;
		try {
			transport.call(soapAction, envelope);
			response = envelope.getResponse();
		} catch (IOException e) {
			throw new InvalidResponseException(e);
		} catch (XmlPullParserException e) {
			throw new InvalidResponseException(e);
		} finally {
			logRawResponse();
		}

		return response;
	}

	/**
	 * Log raw response from server, if transport.debug is true
	 */
	protected void logRawResponse() {
		if (transport.debug) {
			Log.i(TAG, "Raw response from server: \n" + transport.responseDump);
		}
	}

	// GETTERS AND SETTERS

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getEntryPoint() {
		return transport.getUrl();
	}

	public void setEntryPoint(String entryPoint) {
		transport.setUrl(entryPoint);
	}

	public Transport getTransport() {
		return transport;
	}

	public void setTransport(Transport transport) {
		this.transport = transport;
	}

}
