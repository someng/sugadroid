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
import java.util.Map;

import org.joda.time.LocalDate;

import com.excilys.sugadroid.activities.GeneralSettings;
import com.excilys.sugadroid.activities.MenuActivity;
import com.excilys.sugadroid.beans.interfaces.IAppointmentBean;
import com.excilys.sugadroid.di.BeanHolder;
import com.excilys.sugadroid.services.exceptions.ServiceException;
import com.excilys.sugadroid.util.EagerLoadingCalendar;

public class GetInitialCalendarTask extends AuthenticatedTask<MenuActivity> {

	public GetInitialCalendarTask(MenuActivity activity) {
		super(activity);
	}

	@Override
	public void doRun() throws ServiceException {

		Map<LocalDate, List<IAppointmentBean>> initialDaysAppointments;

		LocalDate today = new LocalDate();
		LocalDate before = today.minusDays(GeneralSettings
				.getAppointmentsLoadingBefore(activity));
		LocalDate after = today.plusDays(GeneralSettings
				.getAppointmentsLoadingAfter(activity));

		initialDaysAppointments = BeanHolder.getInstance()
				.getAppointmentServices().getAppointmentsInInterval(before,
						after);

		EagerLoadingCalendar calendar = new EagerLoadingCalendar(before, after,
				initialDaysAppointments);

		activity.onInitialCalendarLoaded(calendar);

	}
}
