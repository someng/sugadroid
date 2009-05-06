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

		hackUtil = new HTTPSHackUtil();
		// Not linked to beans, should be placed somewhere else
		// May be useful for HttpUrlConnection connections
		hackUtil.allowAllSSL();

		// Creating sessionBean bean
		sessionBean = new SessionBeanImpl();

		// Creating transportHttpClient bean (for transport)
		transportHttpClient = new DefaultHttpClient();
		hackUtil.httpClientAllowAllSSL(transportHttpClient);

		// Creating transport bean (for facade)
		HttpClientTransportAndroid transportAndroid = new HttpClientTransportAndroid();
		transportAndroid.setHttpClient(transportHttpClient);
		transport = transportAndroid;
		// TODO set to false when debug is over.
		// if set to true, the response of the server will be dumped in the log
		transport.debug = true;

		String namespace = "http://www.sugarcrm.com/sugarcrm";

		LoginServicesKsoap2Impl loginServicesKsoap2Impl = new LoginServicesKsoap2Impl();
		loginServicesKsoap2Impl.setTransport(transport);
		loginServices = loginServicesKsoap2Impl;
		loginServices.setNamespace(namespace);

		ContactServicesKsoap2Impl contactServicesKsoap2Impl = new ContactServicesKsoap2Impl();
		contactServicesKsoap2Impl.setTransport(transport);
		contactServicesKsoap2Impl.setSessionBean(sessionBean);
		contactServices = contactServicesKsoap2Impl;
		contactServices.setNamespace(namespace);

		AccountServicesKsoap2Impl accountServicesKsoap2Impl = new AccountServicesKsoap2Impl();
		accountServicesKsoap2Impl.setTransport(transport);
		accountServicesKsoap2Impl.setSessionBean(sessionBean);
		accountServices = accountServicesKsoap2Impl;
		accountServices.setNamespace(namespace);

		AppointmentServicesKsoap2Impl appointmentServicesKsoap2Impl = new AppointmentServicesKsoap2Impl();
		appointmentServicesKsoap2Impl.setTransport(transport);
		appointmentServicesKsoap2Impl.setSessionBean(sessionBean);
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

}
