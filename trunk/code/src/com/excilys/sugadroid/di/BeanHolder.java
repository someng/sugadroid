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

package com.excilys.sugadroid.di;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.ksoap2.transport.Transport;

import com.excilys.sugadroid.beans.SessionBeanImpl;
import com.excilys.sugadroid.beans.interfaces.ISessionBean;
import com.excilys.sugadroid.services.IBeanFactory;
import com.excilys.sugadroid.services.SugarBeanFactoryImpl;
import com.excilys.sugadroid.services.impl.ksoap2.AccountServicesKsoap2Impl;
import com.excilys.sugadroid.services.impl.ksoap2.AppointmentServicesKsoap2Impl;
import com.excilys.sugadroid.services.impl.ksoap2.ContactServicesKsoap2Impl;
import com.excilys.sugadroid.services.impl.ksoap2.HttpClientTransportAndroid;
import com.excilys.sugadroid.services.impl.ksoap2.LoginServicesKsoap2Impl;
import com.excilys.sugadroid.services.impl.ksoap2.beanFactories.Ksoap2BeanFactory;
import com.excilys.sugadroid.services.interfaces.IAccountServices;
import com.excilys.sugadroid.services.interfaces.IAppointmentServices;
import com.excilys.sugadroid.services.interfaces.IContactServices;
import com.excilys.sugadroid.services.interfaces.ILoginServices;
import com.excilys.sugadroid.services.util.HTTPSHackUtil;

/**
 * This class deals with dependency injection. It could be quite easily replaced
 * by Spring (if needed).
 * 
 * @author Pierre-Yves Ricau
 * 
 */
public class BeanHolder {

	private static final BeanHolder instance = new BeanHolder();

	/**
	 * This bean is a singleton
	 * 
	 * @return
	 */
	public static BeanHolder getInstance() {
		return instance;
	}

	private HttpClient transportHttpClient;
	private Transport transport;
	private HTTPSHackUtil hackUtil;
	private ILoginServices loginServices;
	private IContactServices contactServices;
	private IAccountServices accountServices;
	private IAppointmentServices appointmentServices;
	private ISessionBean sessionBean;
	private IBeanFactory beanFactory;
	private Ksoap2BeanFactory ksoap2BeanFactory;

	private BeanHolder() {

		// Creating sessionBean bean
		sessionBean = new SessionBeanImpl();

		// Creating transportHttpClient bean (for transport)
		transportHttpClient = new DefaultHttpClient();

		/* Uncomment this line to use SSL Hacks */
		// hackUtil = new HTTPSHackUtil();
		/*
		 * Uncommenting the following line will disable hostname verifying for
		 * SSL handshake (however, you still need a valid certificate).
		 */
		// hackUtil.allowAllHostname();
		/*
		 * Uncommenting the following line will allow any SSL certificate, even
		 * self-signed.
		 */
		// hackUtil.httpClientAllowAllSSL(transportHttpClient);
		// Creating transport bean (for facade)
		HttpClientTransportAndroid transportAndroid = new HttpClientTransportAndroid();
		transportAndroid.setHttpClient(transportHttpClient);
		transport = transportAndroid;
		// TODO set to false when debug is over.
		// if set to true, the response of the server will be dumped in the log
		transport.debug = false;

		String namespace = "http://www.sugarcrm.com/sugarcrm";

		LoginServicesKsoap2Impl loginServicesKsoap2Impl = new LoginServicesKsoap2Impl();
		loginServicesKsoap2Impl.setTransport(transport);
		loginServices = loginServicesKsoap2Impl;
		loginServices.setNamespace(namespace);

		beanFactory = new SugarBeanFactoryImpl();

		ksoap2BeanFactory = new Ksoap2BeanFactory();
		ksoap2BeanFactory.setBeanFactory(beanFactory);

		ContactServicesKsoap2Impl contactServicesKsoap2Impl = new ContactServicesKsoap2Impl();
		contactServicesKsoap2Impl.setTransport(transport);
		contactServicesKsoap2Impl.setSessionBean(sessionBean);
		contactServicesKsoap2Impl.setKsoap2BeanFactory(ksoap2BeanFactory);
		contactServices = contactServicesKsoap2Impl;
		contactServices.setNamespace(namespace);

		AccountServicesKsoap2Impl accountServicesKsoap2Impl = new AccountServicesKsoap2Impl();
		accountServicesKsoap2Impl.setTransport(transport);
		accountServicesKsoap2Impl.setSessionBean(sessionBean);
		accountServicesKsoap2Impl.setKsoap2BeanFactory(ksoap2BeanFactory);
		accountServices = accountServicesKsoap2Impl;
		accountServices.setNamespace(namespace);

		AppointmentServicesKsoap2Impl appointmentServicesKsoap2Impl = new AppointmentServicesKsoap2Impl();
		appointmentServicesKsoap2Impl.setTransport(transport);
		appointmentServicesKsoap2Impl.setSessionBean(sessionBean);
		appointmentServicesKsoap2Impl.setKsoap2BeanFactory(ksoap2BeanFactory);
		appointmentServices = appointmentServicesKsoap2Impl;
		appointmentServices.setNamespace(namespace);

	}

	// GETTERS

	public HttpClient getTransportHttpClient() {
		return transportHttpClient;
	}

	public HTTPSHackUtil getHackUtil() {
		return hackUtil;
	}

	public ILoginServices getLoginServices() {
		return loginServices;
	}

	public IContactServices getContactServices() {
		return contactServices;
	}

	public IAccountServices getAccountServices() {
		return accountServices;
	}

	public IAppointmentServices getAppointmentServices() {
		return appointmentServices;
	}

	public ISessionBean getSessionBean() {
		return sessionBean;
	}

	public Transport getTransport() {
		return transport;
	}

	public IBeanFactory getBeanFactory() {
		return beanFactory;
	}

	public Ksoap2BeanFactory getKsoap2BeanFactory() {
		return ksoap2BeanFactory;
	}

}
