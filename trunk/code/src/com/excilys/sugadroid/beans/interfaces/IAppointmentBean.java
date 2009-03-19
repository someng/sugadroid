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

package com.excilys.sugadroid.beans.interfaces;

import java.io.Serializable;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import com.excilys.sugadroid.beans.ContactBean;

public interface IAppointmentBean extends Serializable {

	public LocalDate getDayStart();

	public LocalTime getTimeStart();

	public LocalDate getDateEnd();

	public Integer getDurationHours();

	public Integer getDurationMinutes();

	public List<ContactBean> getContacts();

	public String getId();

	public String getName();

	public String getDescription();

	public void setContacts(List<ContactBean> contacts);

}
