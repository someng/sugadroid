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

import com.excilys.sugadroid.activities.interfaces.CallingGetItemDetailsActivity;
import com.excilys.sugadroid.beans.ContactBean;
import com.excilys.sugadroid.di.BeanHolder;
import com.excilys.sugadroid.services.exceptions.ServiceException;

public class GetContactDetailsTask extends
		AuthenticatedTask<CallingGetItemDetailsActivity<ContactBean>> {

	private final String contactId;

	public GetContactDetailsTask(
			CallingGetItemDetailsActivity<ContactBean> activity,
			String contactId) {
		super(activity);
		this.contactId = contactId;
	}

	@Override
	public void doRun() throws ServiceException {
		ContactBean contact = BeanHolder.getInstance().getContactServices()
				.getContactDetails(contactId);

		activity.forwardItemDetailsActivity(contact);
	}
}
