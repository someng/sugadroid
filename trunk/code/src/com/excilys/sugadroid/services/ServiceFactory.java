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

import com.excilys.sugadroid.di.BeanHolder;
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
		return BeanHolder.getInstance().getLoginServices();
	}

	public IContactServices getContactServices() {
		return BeanHolder.getInstance().getContactServices();
	}

	public IAccountServices getAccountServices() {
		return BeanHolder.getInstance().getAccountServices();
	}

	public IAppointmentServices getAppointmentServices() {
		return BeanHolder.getInstance().getAppointmentServices();
	}

}
