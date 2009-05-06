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

import org.ksoap2.serialization.SoapObject;

import com.excilys.sugadroid.beans.ContactBean;
import com.excilys.sugadroid.services.exceptions.ServiceException;
import com.excilys.sugadroid.services.interfaces.IContactServices;

public class ContactServicesKsoap2Impl extends
		AuthenticatedSugarServiceClientKsoap2Impl implements IContactServices {

	@SuppressWarnings("unused")
	private static String TAG = ContactServicesKsoap2Impl.class.getSimpleName();

	@Override
	public ContactBean getContactDetails(String contactId)
			throws ServiceException {

		checkLoggedIn();

		SoapObject request = newEntryRequest();

		request.addProperty("module_name", "Contacts");
		request.addProperty("id", contactId);

		List<String> t = new Vector<String>();
		// I found the names of the fields in the table "contacts" in the db.
		t.add("id");
		t.add("first_name");
		t.add("last_name");
		t.add("phone_mobile");
		t.add("phone_work");
		t.add("email1");
		t.add("account_name");
		t.add("account_id");

		request.addProperty("select_fields", t);

		return getEntry(request, ContactBean.class);
	}

	@Override
	public List<ContactBean> searchContacts(String search, Integer offset,
			Integer maxResults) throws ServiceException {

		checkLoggedIn();

		String query = "contacts.first_name LIKE '%" + search
				+ "%' OR contacts.last_name LIKE '%" + search + "%'";

		return getContactListWithQuery(query, offset, maxResults);

	}

	@Override
	public List<ContactBean> getAccountContacts(String accountId,
			Integer offset, Integer maxResults) throws ServiceException {

		checkLoggedIn();

		String query = "contacts.id = accounts_contacts.contact_id AND accounts_contacts.account_id = '"
				+ accountId + "'";

		return getContactListWithQuery(query, offset, maxResults);
	}

	@Override
	public List<ContactBean> getAppointmentContacts(String appointmentId,
			Integer offset, Integer maxResults) throws ServiceException {

		checkLoggedIn();

		String query = "contacts.id in (select contact_id from meetings_contacts where meeting_id = '"
				+ appointmentId + "')";

		return getContactListWithQuery(query, offset, maxResults);
	}

	private List<ContactBean> getContactListWithQuery(String query,
			Integer offset, Integer maxResults) throws ServiceException {

		SoapObject request = newEntryListRequest();

		request.addProperty("module_name", "Contacts");
		request.addProperty("query", query);
		request.addProperty("order_by", "contacts.last_name asc");

		request.addProperty("offset", offset.toString());

		List<String> t = new Vector<String>();

		t.add("id");
		t.add("first_name");
		t.add("last_name");

		request.addProperty("select_fields", t);
		request.addProperty("max_results", maxResults.toString());
		request.addProperty("deleted", "0");

		return getEntryList(request, ContactBean.class);
	}
}
