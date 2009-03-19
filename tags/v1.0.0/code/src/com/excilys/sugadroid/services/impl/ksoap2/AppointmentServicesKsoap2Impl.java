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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.joda.time.LocalDate;
import org.ksoap2.serialization.SoapObject;

import android.util.Log;

import com.excilys.sugadroid.beans.AppointmentBeanV4_5;
import com.excilys.sugadroid.beans.AppointmentBeanV5;
import com.excilys.sugadroid.beans.SessionBean;
import com.excilys.sugadroid.beans.interfaces.IAppointmentBean;
import com.excilys.sugadroid.services.exceptions.ServiceException;
import com.excilys.sugadroid.services.interfaces.IAppointmentServices;

public class AppointmentServicesKsoap2Impl extends BaseServicesKsoap2Impl
		implements IAppointmentServices {

	private static AppointmentServicesKsoap2Impl singleton;
	private static String TAG = AppointmentServicesKsoap2Impl.class
			.getSimpleName();

	private AppointmentServicesKsoap2Impl() {
	};

	public static AppointmentServicesKsoap2Impl getInstance() {
		if (singleton == null) {
			singleton = new AppointmentServicesKsoap2Impl();
		}
		return singleton;
	}

	@Override
	public IAppointmentBean getAppointmentDetails(SessionBean session,
			String appointmentId) throws ServiceException {

		Log.d(TAG, "getAppointmentDetails called, appointmentId: "
				+ appointmentId);

		boolean version4_5 = session.isVersion4_5();

		SoapObject request = newEntryRequest();

		request.addProperty("session", session.getSessionId());
		request.addProperty("module_name", "Meetings");
		request.addProperty("id", appointmentId);

		List<String> t = new Vector<String>();
		// I found the names of the fields in the table "contacts" in the db.
		t.add("id");
		t.add("name");
		t.add("description");
		t.add("date_start");
		if (version4_5) {
			t.add("time_start");
		}
		t.add("date_end");
		t.add("duration_hours");
		t.add("duration_minutes");

		request.addProperty("select_fields", t);

		Class<? extends IAppointmentBean> appointmentBeanClass;

		if (version4_5) {
			appointmentBeanClass = AppointmentBeanV4_5.class;
		} else {
			appointmentBeanClass = AppointmentBeanV5.class;
		}

		return getEntry(request, appointmentBeanClass, session.getUrl());

	}

	@Override
	public List<IAppointmentBean> getDayAppointments(SessionBean session,
			LocalDate day) throws ServiceException {

		Log.d(TAG, "getDayAppointments called, date: " + day.toString());

		boolean version4_5 = session.isVersion4_5();

		SoapObject request = newEntryListRequest();

		request.addProperty("session", session.getSessionId());
		request.addProperty("module_name", "Meetings");

		String query;

		query = "meetings.id = meetings_users.meeting_id AND meetings_users.user_id='"
				+ session.getUserId() + "' AND ";

		if (version4_5) {
			query += "meetings.date_start='" + day.toString("yyyy-MM-dd") + "'";
		} else {
			query += "meetings.date_start>='" + day.toString("yyyy-MM-dd")
					+ "' AND meetings.date_start<'"
					+ day.plusDays(1).toString("yyyy-MM-dd") + "'";
		}

		request.addProperty("query", query);

		if (version4_5) {
			request.addProperty("order_by", "meetings.time_start asc");
		} else {
			request.addProperty("order_by", "meetings.date_start asc");
		}

		request.addProperty("offset", "0");

		List<String> t = new Vector<String>();

		t.add("id");
		t.add("name");

		request.addProperty("select_fields", t);
		request.addProperty("max_results", "100");
		request.addProperty("deleted", "0");

		List<IAppointmentBean> appointments = new ArrayList<IAppointmentBean>();

		if (version4_5) {
			appointments.addAll(getEntryList(request,
					AppointmentBeanV4_5.class, session.getUrl()));
		} else {
			appointments.addAll(getEntryList(request, AppointmentBeanV5.class,
					session.getUrl()));
		}

		return appointments;
	}

	@Override
	public Map<LocalDate, List<IAppointmentBean>> getAppointmentsInInterval(
			SessionBean session, String userId, LocalDate start, LocalDate end)
			throws ServiceException {
		Log.d(TAG, "getAppointmentsInInterval called, days " + start.toString()
				+ " " + end.toString());

		if (start.compareTo(end) > 0) {
			throw new ServiceException(
					"start day should be before or equal to end day");
		}

		boolean version4_5 = session.isVersion4_5();

		StringBuilder sb = new StringBuilder(
				"meetings_users.meeting_id=meetings.id AND meetings_users.user_id='")
				.append(userId).append("' AND ");

		if (version4_5) {
			sb.append("meetings.date_start>='").append(
					start.toString("yyyy-MM-dd")).append(
					"' AND meetings.date_start<'").append(
					end.plusDays(1).toString("yyyy-MM-dd")).append("'");
		} else {
			sb.append("meetings.date_start>='").append(
					start.toString("yyyy-MM-dd")).append(
					"' AND meetings.date_start<='").append(
					end.toString("yyyy-MM-dd")).append("'");
		}

		String query = sb.toString();

		SoapObject request = newEntryListRequest();

		request.addProperty("session", session.getSessionId());
		request.addProperty("module_name", "Meetings");
		request.addProperty("query", query);
		if (version4_5) {
			request.addProperty("order_by",
					"meetings.date_start asc, meetings.time_start asc");
		} else {
			request.addProperty("order_by", "meetings.date_start asc");
		}

		request.addProperty("offset", "0");

		List<String> t = new Vector<String>();

		t.add("id");
		t.add("name");
		t.add("date_start");
		if (version4_5) {
			t.add("time_start");
		}

		request.addProperty("select_fields", t);
		request.addProperty("max_results", "1000");
		request.addProperty("deleted", "0");

		List<IAppointmentBean> appointments = new ArrayList<IAppointmentBean>();

		if (version4_5) {
			appointments.addAll(getEntryList(request,
					AppointmentBeanV4_5.class, session.getUrl()));
		} else {
			appointments.addAll(getEntryList(request, AppointmentBeanV5.class,
					session.getUrl()));
		}

		Map<LocalDate, List<IAppointmentBean>> appointmentsInInterval = new HashMap<LocalDate, List<IAppointmentBean>>();

		appointmentsInInterval.put(start, new ArrayList<IAppointmentBean>());

		LocalDate day = start;

		while (!day.equals(end)) {
			day = day.plusDays(1);
			appointmentsInInterval.put(day, new ArrayList<IAppointmentBean>());
		}

		for (IAppointmentBean appointment : appointments) {
			appointmentsInInterval.get(appointment.getDayStart()).add(
					appointment);
		}

		return appointmentsInInterval;
	}
}
