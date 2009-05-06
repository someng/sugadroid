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

import android.util.Log;

import com.excilys.sugadroid.beans.AccountBean;
import com.excilys.sugadroid.services.exceptions.ServiceException;
import com.excilys.sugadroid.services.interfaces.IAccountServices;

public class AccountServicesKsoap2Impl extends
		AuthenticatedSugarServiceClientKsoap2Impl implements IAccountServices {

	private static String TAG = AccountServicesKsoap2Impl.class.getSimpleName();

	@Override
	public AccountBean getAccountDetails(String accountId)
			throws ServiceException {

		Log.d(TAG, "getAccountDetails called, accountId: " + accountId);

		checkLoggedIn();

		SoapObject request = newEntryRequest();

		request.addProperty("module_name", "Accounts");
		request.addProperty("id", accountId);

		List<String> t = new Vector<String>();
		// I found the names of the fields in the table "accounts" in the db.
		t.add("id");
		t.add("name");
		t.add("billing_address_street");
		t.add("billing_address_city");
		t.add("billing_address_state");
		t.add("billing_address_postalcode");
		t.add("billing_address_country");
		t.add("phone_office");

		request.addProperty("select_fields", t);

		return getEntry(request, AccountBean.class);
	}

	@Override
	public List<AccountBean> searchAccounts(String search, Integer offset,
			Integer maxResults) throws ServiceException {
		Log.d(TAG, "searchAccounts called, search: " + search);

		checkLoggedIn();

		SoapObject request = newEntryListRequest();

		request.addProperty("module_name", "Accounts");
		request.addProperty("query", "accounts.name LIKE '%" + search + "%'");
		request.addProperty("order_by", "accounts.name asc");

		request.addProperty("offset", offset.toString());

		List<String> t = new Vector<String>();

		t.add("id");
		t.add("name");

		request.addProperty("select_fields", t);
		request.addProperty("max_results", maxResults.toString());
		request.addProperty("deleted", "0");

		return getEntryList(request, AccountBean.class);
	}
}
