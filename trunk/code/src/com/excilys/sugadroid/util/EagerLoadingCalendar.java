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

package com.excilys.sugadroid.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.LocalDate;

import android.util.Log;

import com.excilys.sugadroid.activities.AppointmentsActivity;
import com.excilys.sugadroid.beans.interfaces.IAppointmentBean;
import com.excilys.sugadroid.util.exceptions.DayNotLoadedException;

/**
 * This class is a calendar deailing with AppointmentBeans, it is used by the
 * activity {@link AppointmentsActivity}
 * 
 * 
 * This class should have all days set between minDay and maxDay, always.
 * 
 * @author pricau
 * 
 */
public class EagerLoadingCalendar implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String TAG = EagerLoadingCalendar.class
			.getSimpleName();

	private Map<LocalDate, List<IAppointmentBean>> daysAppointments = new HashMap<LocalDate, List<IAppointmentBean>>();

	private LocalDate minDay;
	private LocalDate maxDay;
	private LocalDate minPastLoadingDay;
	private LocalDate maxFutureLoadingDay;

	public EagerLoadingCalendar(LocalDate minDay, LocalDate maxDay,
			Map<LocalDate, List<IAppointmentBean>> initialDayAppointments) {

		daysAppointments.putAll(initialDayAppointments);
		this.minDay = minDay;
		this.maxDay = maxDay;
		minPastLoadingDay = minDay;
		maxFutureLoadingDay = maxDay;
	}

	public synchronized void setDayAppointments(LocalDate day,
			List<IAppointmentBean> appointments) {

		if (day.compareTo(minDay) < 0) {
			minDay = day;
		} else if (day.compareTo(maxDay) > 0) {
			maxDay = day;
		}

		List<IAppointmentBean> dayAppointments = new ArrayList<IAppointmentBean>();

		dayAppointments.addAll(appointments);

		daysAppointments.put(day, dayAppointments);

	}

	public synchronized void setDaysAppointments(
			Map<LocalDate, List<IAppointmentBean>> daysAppointments) {
		Set<LocalDate> days = this.daysAppointments.keySet();

		for (LocalDate day : days) {
			setDayAppointments(day, daysAppointments.get(day));
		}
	}

	public boolean isDayLoaded(LocalDate day) {
		return day.compareTo(minDay) >= 0 && day.compareTo(maxDay) <= 0;
	}

	public boolean isDayBeingLoaded(LocalDate day) {

		return !isDayLoaded(day) && day.compareTo(minPastLoadingDay) >= 0
				&& day.compareTo(maxFutureLoadingDay) <= 0;
	}

	public List<IAppointmentBean> getDayAppointments(LocalDate day)
			throws DayNotLoadedException {

		if (!isDayLoaded(day)) {
			throw new DayNotLoadedException(
					"This day has not been loaded yet: " + day.toString());
		}

		List<IAppointmentBean> dayAppointments = daysAppointments.get(day);

		// Fixing missing values
		if (dayAppointments == null) {
			Log.i(TAG, "This day does not exist yet, created: "
					+ day.toString());

			dayAppointments = new ArrayList<IAppointmentBean>();
			daysAppointments.put(day, dayAppointments);
		}

		return dayAppointments;
	}

	public LocalDate getMinPastLoadingDay() {
		return minPastLoadingDay;
	}

	public void setMinPastLoadingDay(LocalDate minPastLoadingDay) {

		if (minPastLoadingDay.compareTo(this.minPastLoadingDay) < 0) {
			this.minPastLoadingDay = minPastLoadingDay;
		} else {
			Log.i(TAG, "old minPastLoadingDay before new one, old: "
					+ this.minPastLoadingDay + ", new: " + minPastLoadingDay);
		}
	}

	public LocalDate getMaxFutureLoadingDay() {
		return maxFutureLoadingDay;
	}

	public void setMaxFutureLoadingDay(LocalDate maxFutureLoadingDay) {
		if (maxFutureLoadingDay.compareTo(this.maxFutureLoadingDay) > 0) {
			this.maxFutureLoadingDay = maxFutureLoadingDay;
		} else {
			Log.i(TAG, "old maxFutureLoadingDay after new one, old: "
					+ this.maxFutureLoadingDay + ", new: "
					+ maxFutureLoadingDay);
		}
	}

}
