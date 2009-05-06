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

package com.excilys.sugadroid.services.interfaces;

import java.util.List;

import com.excilys.sugadroid.beans.ContactBean;
import com.excilys.sugadroid.services.exceptions.ServiceException;

/**
 * This class represents the services to access the contacts
 * 
 * @author Pierre-Yves Ricau
 * 
 */
public interface IContactServices extends IWebService {

	public List<ContactBean> searchContacts(String search, Integer offset,
			Integer maxResults) throws ServiceException;

	public List<ContactBean> getAccountContacts(String accountId,
			Integer offset, Integer maxResults) throws ServiceException;

	public List<ContactBean> getAppointmentContacts(String appointmentId,
			Integer offset, Integer maxResults) throws ServiceException;

	public ContactBean getContactDetails(String contactId)
			throws ServiceException;
}
