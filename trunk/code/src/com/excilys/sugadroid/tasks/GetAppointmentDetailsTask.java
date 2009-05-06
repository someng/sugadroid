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

import java.util.ArrayList;

import com.excilys.sugadroid.activities.interfaces.ICallingGetItemDetailsActivity;
import com.excilys.sugadroid.beans.ContactBean;
import com.excilys.sugadroid.beans.interfaces.IAppointmentBean;
import com.excilys.sugadroid.services.exceptions.ServiceException;
import com.excilys.sugadroid.services.interfaces.IAppointmentServices;

public class GetAppointmentDetailsTask extends
		AuthenticatedTask<ICallingGetItemDetailsActivity<IAppointmentBean>> {

	private String appointmentId;
	private IAppointmentServices appointmentServices;

	public GetAppointmentDetailsTask(
			ICallingGetItemDetailsActivity<IAppointmentBean> activity,
			IAppointmentServices appointmentServices, String appointmentId) {
		super(activity);
		this.appointmentId = appointmentId;
		this.appointmentServices = appointmentServices;
	}

	@Override
	public void doRunAuthenticatedTask() throws ServiceException {
		IAppointmentBean appointment = appointmentServices
				.getAppointmentDetails(appointmentId);

		appointment.setContacts(new ArrayList<ContactBean>());

		activity.onItemDetailsLoaded(appointment);
	}
}
