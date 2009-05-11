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

package com.excilys.sugadroid.beans;

import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import com.excilys.sugadroid.beans.interfaces.IAppointmentBean;

/**
 * A bean representing an appointment, as given by SugarCRM (v4.5 and v5).
 * 
 * Child classes should have a setDateStart method, with a different parameter
 * depending on the version
 * 
 * @author Pierre-Yves Ricau
 * 
 */

public abstract class GenericAppointmentBean implements IAppointmentBean {

	private static final long serialVersionUID = 1L;

	private String id;

	private List<ContactBean> contacts;

	private String name;
	private String description;
	protected LocalDate dateStart;
	protected LocalTime timeStart;
	private LocalDate dateEnd;
	private Integer durationHours;
	private Integer durationMinutes;

	public LocalTime getTimeStart() {
		return timeStart;
	}

	public LocalDate getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(LocalDate dateEnd) {
		this.dateEnd = dateEnd;
	}

	public Integer getDurationHours() {
		return durationHours;
	}

	public void setDurationHours(Integer durationHours) {
		this.durationHours = durationHours;
	}

	public Integer getDurationMinutes() {
		return durationMinutes;
	}

	public void setDurationMinutes(Integer durationMinutes) {
		this.durationMinutes = durationMinutes;
	}

	public List<ContactBean> getContacts() {
		return contacts;
	}

	public void setContacts(List<ContactBean> contacts) {
		this.contacts = contacts;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		if (name != null) {
			return name;
		} else {
			return "";
		}
	}

	@Override
	public LocalDate getDayStart() {
		return dateStart;
	}

}
