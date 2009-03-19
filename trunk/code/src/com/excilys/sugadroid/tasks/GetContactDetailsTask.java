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

import com.excilys.sugadroid.activities.delegates.DialogManager;
import com.excilys.sugadroid.activities.interfaces.CallingGetItemDetailsActivity;
import com.excilys.sugadroid.beans.ContactBean;
import com.excilys.sugadroid.beans.SessionBean;
import com.excilys.sugadroid.services.ServiceFactory;
import com.excilys.sugadroid.services.exceptions.InvalidResponseException;
import com.excilys.sugadroid.services.exceptions.ServiceException;

public class GetContactDetailsTask implements Runnable {

	private final String contactId;
	private final CallingGetItemDetailsActivity<ContactBean> activity;

	public GetContactDetailsTask(
			CallingGetItemDetailsActivity<ContactBean> activity,
			String contactId) {
		this.activity = activity;
		this.contactId = contactId;
	}

	@Override
	public void run() {
		ContactBean contact;

		try {
			contact = ServiceFactory.getInstance().getContactServices()
					.getContactDetails(SessionBean.getInstance(), contactId);
		} catch (InvalidResponseException e) {
			activity
					.postShowDialog(DialogManager.DIALOG_ERROR_INVALID_RESPONSE);
			return;
		} catch (ServiceException e) {
			activity.postShowCustomDialog(e.getMessage(), e.getDescription());
			return;
		}
		activity.forwardItemDetailsActivity(contact);
	}
}
