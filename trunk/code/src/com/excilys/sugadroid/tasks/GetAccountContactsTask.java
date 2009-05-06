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

package com.excilys.sugadroid.tasks;

import java.util.List;

import com.excilys.sugadroid.activities.AccountDetailsActivity;
import com.excilys.sugadroid.activities.GeneralSettings;
import com.excilys.sugadroid.activities.delegates.DialogManager;
import com.excilys.sugadroid.beans.ContactBean;
import com.excilys.sugadroid.di.BeanHolder;
import com.excilys.sugadroid.services.exceptions.InvalidResponseException;
import com.excilys.sugadroid.services.exceptions.ServiceException;

public class GetAccountContactsTask implements Runnable {

	private final String accountId;
	private final AccountDetailsActivity activity;
	private final int offset;

	public GetAccountContactsTask(AccountDetailsActivity activity,
			String accountId, int offset) {
		this.activity = activity;
		this.accountId = accountId;
		this.offset = offset;
	}

	@Override
	public void run() {
		List<ContactBean> contacts;
		try {
			contacts = BeanHolder.getInstance().getContactServices()
					.getAccountContacts(accountId, offset,
							GeneralSettings.getAccountMaxResults(activity));
		} catch (InvalidResponseException e) {
			activity
					.postShowDialog(DialogManager.DIALOG_ERROR_INVALID_RESPONSE);
			return;
		} catch (ServiceException e) {

			activity.postShowCustomDialog(e.getMessage(), e.getDescription());
			return;
		}
		activity.updateContactList(contacts);
	}
}
