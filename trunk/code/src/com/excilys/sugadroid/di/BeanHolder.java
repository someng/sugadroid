/* ==================================import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.ksoap2.transport.Transport;

import com.excilys.sugadroid.services.impl.ksoap2.AccountServicesKsoap2Impl;
import com.excilys.sugadroid.services.impl.ksoap2.AppointmentServicesKsoap2Impl;
import com.excilys.sugadroid.services.impl.ksoap2.ContactServicesKsoap2Impl;
import com.excilys.sugadroid.services.impl.ksoap2.HttpClientTransportAndroid;
import com.excilys.sugadroid.services.impl.ksoap2.LoginServicesKsoap2Impl;
import com.excilys.sugadroid.services.interfaces.IAccountServices;
import com.excilys.sugadroid.services.interfaces.IAppointmentServices;
import com.excilys.sugadroid.services.interfaces.IContactServices;
import com.excilys.sugadroid.services.interfaces.ILoginServices;
import com.excilys.sugadroid.services.util.HTTPSHackUtil;
 not, see <http://www.gnu.org/licenses/>.
 * ============================================================================
 */

package com.excilys.sugadroid.di;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.ksoap2.transport.Transport;

import com.excilys.sugadroid.beans.ISessionBean;
import com.excilys.sugadroid.beans.SessionBeanImpl;
import com.excilys.sugadroid.services.impl.ksoap2.AccountServicesKsoap2Impl;
import com.excilys.sugadroid.services.impl.ksoap2.AppointmentServicesKsoap2Impl;
import com.excilys.sugadroid.services.impl.ksoap2.ContactServicesKsoap2Impl;
import com.excilys.sugadroid.services.impl.ksoap2.HttpClientTransportAndroid;
import com.excilys.sugadroid.services.impl.ksoap2.LoginServicesKsoap2Impl;
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

	private BeanHolder() {

		sessionBean = new SessionBeanImpl();

		hackUtil = new HTTPSHackUtil();
		// Not linked to beans, should be placed somewhere else
		// May be useful for HttpUrlConnection connections
		hackUtil.allowAllSSL();

		// Creating sessionBean bean
		// sessionBean = new SessionBeanImpl();

		// Creating transportHttpClient bean (for transport)
		transportHttpClient = new DefaultHttpClient();
		hackUtil.httpClientAllowAllSSL(transportHttpClient);

		// Creating transport bean (for facade)
		HttpClientTransportAndroid transportAndroid = new HttpClientTransportAndroid();
		transportAndroid.setHttpClient(transportHttpClient);
		transport = transportAndroid;
		// TODO set to false when debug is over.
		// if set to true, the response of the server will be dumped in the log
		transport.debug = false;

		// Creating facade bean
		/*
		 * FacadeKsoap2Impl facadeKsoap2 = new FacadeKsoap2Impl();
		 * facadeKsoap2.setSessionBean(sessionBean);
		 * facadeKsoap2.setTransport(transport);
		 * facadeKsoap2.setNamespace("http://service.bridge.maestro.excilys.com/"
		 * ); facade = facadeKsoap2;
		 */

		loginServices = new LoginServicesKsoap2Impl();
		contactServices = new ContactServicesKsoap2Impl();
		accountServices = new AccountServicesKsoap2Impl();
		appointmentServices = new AppointmentServicesKsoap2Impl();

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

}
