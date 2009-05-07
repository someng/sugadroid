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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.transport.Transport;
import org.xmlpull.v1.XmlPullParserException;

/**
 * This class is an equivalent to HttpTransportSE, but it uses the HttpClient
 * provided by Android instead of HTTPUrlConnection. It corrects a bug from
 * HttpTransportSE, that makes every 2 requests fail with HTTPS under specific
 * conditions.
 * 
 * @author Pierre-Yves Ricau
 * 
 */
public class HttpClientTransportAndroid extends Transport {

	private HttpClient httpClient;

	public HttpClientTransportAndroid(String url) {
		super(url);
	}

	public HttpClientTransportAndroid() {
		super();
	}

	@Override
	public void call(String soapAction, SoapEnvelope envelope)
			throws IOException, XmlPullParserException {
		if (soapAction == null) {
			soapAction = "\"\"";
		}

		byte[] requestData = createRequestData(envelope);

		requestDump = debug ? new String(requestData) : null;
		responseDump = null;

		HttpPost method = new HttpPost(url);

		method.addHeader("User-Agent", "kSOAP/2.0-Excilys");
		method.addHeader("SOAPAction", "soapAction");
		method.addHeader("Content-Type", "text/xml");

		HttpEntity entity = new ByteArrayEntity(requestData);

		method.setEntity(entity);

		HttpResponse response = httpClient.execute(method);

		InputStream inputStream = response.getEntity().getContent();

		if (debug) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buf = new byte[256];
			while (true) {
				int rd = inputStream.read(buf, 0, 256);
				if (rd == -1) {
					break;
				}
				bos.write(buf, 0, rd);
			}
			bos.flush();
			buf = bos.toByteArray();
			responseDump = new String(buf);
			inputStream.close();
			inputStream = new ByteArrayInputStream(buf);
		}

		parseResponse(envelope, inputStream);

	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

}
