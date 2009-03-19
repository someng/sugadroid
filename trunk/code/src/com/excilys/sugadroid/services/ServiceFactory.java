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

package com.excilys.sugadroid.services;

import com.excilys.sugadroid.services.impl.ksoap2.AccountServicesKsoap2Impl;
import com.excilys.sugadroid.services.impl.ksoap2.AppointmentServicesKsoap2Impl;
import com.excilys.sugadroid.services.impl.ksoap2.ContactServicesKsoap2Impl;
import com.excilys.sugadroid.services.impl.ksoap2.LoginServicesKsoap2Impl;
import com.excilys.sugadroid.services.interfaces.IAccountServices;
import com.excilys.sugadroid.services.interfaces.IAppointmentServices;
import com.excilys.sugadroid.services.interfaces.IContactServices;
import com.excilys.sugadroid.services.interfaces.ILoginServices;

/**
 * This factory is a singleton, and provide access to the implementations of the
 * services
 * 
 * @author Pierre-Yves Ricau
 * 
 */
public class ServiceFactory {

	private static ServiceFactory singleton;

	private ServiceFactory() {
	}

	public static ServiceFactory getInstance() {
		if (singleton == null) {
			singleton = new ServiceFactory();
		}
		return singleton;
	}

	public ILoginServices getLoginServices() {
		return LoginServicesKsoap2Impl.getInstance();
	}

	public IContactServices getContactServices() {
		return ContactServicesKsoap2Impl.getInstance();
	}

	public IAccountServices getAccountServices() {
		return AccountServicesKsoap2Impl.getInstance();
	}

	public IAppointmentServices getAppointmentServices() {
		return AppointmentServicesKsoap2Impl.getInstance();
	}

}
