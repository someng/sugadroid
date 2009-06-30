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

import info.piwai.yasdic.YasdicContainer;
import info.piwai.yasdic.YasdicContainer.BeanDef;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.ksoap2.transport.Transport;

import android.util.Log;

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
import com.excilys.sugadroid.services.util.HTTPSHackUtil;

/**
 * This class is a holder on the YasdicContainer, designed with the singleton pattern.
 * 
 * @author Pierre-Yves Ricau
 * 
 */
public class BeanContainerHolder {

	// Singleton holder
	private static YasdicContainer	container;

	/**
	 * Returns a singleton instance of the container. The first call on this method creates the singleton container (and insert the bean
	 * definitions into it). The next calls return the same instance of the container.
	 * 
	 * @return
	 */
	public static YasdicContainer getInstance() {
		if (container == null) {
			container = new YasdicContainer();
			defineBeans(container);
			/*
			 * Comment the following line to disable Https Hacking (used for dev)
			 */
			// hackHttps(container);
			/*
			 * Comment the following line to disable logging of network communications (overriding default value)
			 */
			// container.define("logCommunications", true);
		}
		return container;
	}

	public static void hackHttps(YasdicContainer container) {
		/*
		 * Uncommenting the following line will disable hostname verifying for SSL handshake (however, you still need a valid certificate).
		 */
		((HTTPSHackUtil) container.getBean("hackUtil")).allowAllSSL();
		// Overriding default value
		container.define("hackHttpsClients", true);
	}

	/**
	 * This method is used to define the beans commons to all activities
	 * 
	 * @param container
	 *            The Yasdic Container
	 */
	public static void defineBeans(YasdicContainer container) {

		// Defining default values for properties
		container.define("hackHttpsClients", false);
		container.define("logCommunications", false);

		// Creating sessionBean bean
		container.define("sessionBean", new LogBeanDef<ISessionBean>() {
			@Override
			public ISessionBean newBean(YasdicContainer c) {
				return new SessionBeanImpl();
			}
		});

		container.define("transportHttpClient", new LogBeanDef<HttpClient>() {
			@Override
			public HttpClient newBean(YasdicContainer c) {
				return new DefaultHttpClient();
			}

			@Override
			protected void initBean(YasdicContainer c, HttpClient bean) {
				/*
				 * Uncommenting the following line will allow any SSL certificate, even self-signed.
				 */
				if ((Boolean) c.getBean("hackHttpsClients")) {
					((HTTPSHackUtil) c.getBean("hackUtil")).httpClientAllowAllSSL(bean);
				}
			}
		});

		container.define("transport", new LogBeanDef<HttpClientTransportAndroid>() {
			@Override
			public HttpClientTransportAndroid newBean(YasdicContainer c) {
				return new HttpClientTransportAndroid();
			}

			@Override
			protected void initBean(YasdicContainer c, HttpClientTransportAndroid bean) {
				bean.setHttpClient((HttpClient) c.getBean("transportHttpClient"));
				/*
				 * if set to true, the response of the server will be dumped in the log
				 */
				if ((Boolean) c.getBean("logCommunications")) {
					bean.debug = true;
				} else {
					bean.debug = false;
				}
			}
		});

		container.define("loginServices", new LogBeanDef<LoginServicesKsoap2Impl>() {

			@Override
			protected LoginServicesKsoap2Impl newBean(YasdicContainer arg0) {
				return new LoginServicesKsoap2Impl();
			}

			@Override
			protected void initBean(YasdicContainer c, LoginServicesKsoap2Impl bean) {
				bean.setTransport((Transport) c.getBean("transport"));
				bean.setNamespace((String) c.getBean("namespace"));
			}

		});

		container.define("namespace", "http://www.sugarcrm.com/sugarcrm");

		container.define("beanFactory", new LogBeanDef<SugarBeanFactoryImpl>() {
			@Override
			protected SugarBeanFactoryImpl newBean(YasdicContainer arg0) {
				return new SugarBeanFactoryImpl();
			}
		});

		container.define("ksoap2BeanFactory", new LogBeanDef<Ksoap2BeanFactory>() {
			@Override
			protected Ksoap2BeanFactory newBean(YasdicContainer arg0) {
				return new Ksoap2BeanFactory();
			}

			@Override
			protected void initBean(YasdicContainer c, Ksoap2BeanFactory bean) {
				bean.setBeanFactory((IBeanFactory) c.getBean("beanFactory"));
			}
		});

		container.define("contactServices", new LogBeanDef<ContactServicesKsoap2Impl>() {
			@Override
			protected ContactServicesKsoap2Impl newBean(YasdicContainer arg0) {
				return new ContactServicesKsoap2Impl();
			}

			@Override
			protected void initBean(YasdicContainer c, ContactServicesKsoap2Impl bean) {
				bean.setTransport((Transport) c.getBean("transport"));
				bean.setSessionBean((ISessionBean) c.getBean("sessionBean"));
				bean.setKsoap2BeanFactory((Ksoap2BeanFactory) c.getBean("ksoap2BeanFactory"));
				bean.setNamespace((String) c.getBean("namespace"));
			}
		});

		container.define("accountServices", new LogBeanDef<AccountServicesKsoap2Impl>() {
			@Override
			protected AccountServicesKsoap2Impl newBean(YasdicContainer arg0) {
				return new AccountServicesKsoap2Impl();
			}

			@Override
			protected void initBean(YasdicContainer c, AccountServicesKsoap2Impl bean) {
				bean.setTransport((Transport) c.getBean("transport"));
				bean.setSessionBean((ISessionBean) c.getBean("sessionBean"));
				bean.setKsoap2BeanFactory((Ksoap2BeanFactory) c.getBean("ksoap2BeanFactory"));
				bean.setNamespace((String) c.getBean("namespace"));
			}
		});

		container.define("appointmentServices", new LogBeanDef<AppointmentServicesKsoap2Impl>() {
			@Override
			protected AppointmentServicesKsoap2Impl newBean(YasdicContainer arg0) {
				return new AppointmentServicesKsoap2Impl();
			}

			@Override
			protected void initBean(YasdicContainer c, AppointmentServicesKsoap2Impl bean) {
				bean.setTransport((Transport) c.getBean("transport"));
				bean.setSessionBean((ISessionBean) c.getBean("sessionBean"));
				bean.setKsoap2BeanFactory((Ksoap2BeanFactory) c.getBean("ksoap2BeanFactory"));
				bean.setNamespace((String) c.getBean("namespace"));
			}
		});

	}

	/**
	 * This class extends BeanDef to add logging ability with the Android Logger. You should use Logcat when running the Android
	 * application, to see when instances are created. You should notice that instances are created only when they are needed.
	 * 
	 * @author Pierre-Yves Ricau
	 * 
	 * @param <T>
	 */
	public static abstract class LogBeanDef<T> extends BeanDef<T> {
		private static final String	TAG	= LogBeanDef.class.getSimpleName();

		@Override
		protected final T callNewBean(YasdicContainer c, String id) {
			// Calling the newBean method (to actually instanciate the bean)
			T bean = newBean(c);

			// Building the log String
			StringBuilder sb = new StringBuilder("******** Bean [");
			sb.append(id).append("] created, instance of [").append(bean.getClass().getSimpleName()).append("]");

			Log.d(TAG, sb.toString());
			return bean;
		}
	}

}
