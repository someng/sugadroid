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

import com.excilys.sugadroid.activities.AppointmentDetailsActivity;
import com.excilys.sugadroid.beans.ContactBean;
import com.excilys.sugadroid.services.exceptions.ServiceException;
import com.excilys.sugadroid.services.interfaces.IContactServices;

public class GetAppointmentContactsTask extends
		AuthenticatedTask<AppointmentDetailsActivity> {

	private String appointmentId;
	private int offset;
	private IContactServices contactServices;
	private int maxResults;

	public GetAppointmentContactsTask(AppointmentDetailsActivity activity,
			IContactServices contactServices, String appointmentId, int offset,
			int maxResults) {
		super(activity);
		this.appointmentId = appointmentId;
		this.offset = offset;
		this.contactServices = contactServices;
		this.maxResults = maxResults;
	}

	@Override
	public void doRun() throws ServiceException {
		List<ContactBean> contacts = contactServices.getAppointmentContacts(
				appointmentId, offset, maxResults);

		activity.updateContactList(contacts);
	}
}
