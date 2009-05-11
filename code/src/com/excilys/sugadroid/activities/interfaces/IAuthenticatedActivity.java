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

package com.excilys.sugadroid.activities.interfaces;

/**
 * An activity that provides callbacks to show informations when authenticated
 * tasks perform
 * 
 * @author Pierre-Yves Ricau
 * 
 */
public interface IAuthenticatedActivity extends ICallingLoadingTasksActivity {

	/**
	 * Callback when the action could not be performed because the user is not
	 * logged in
	 */
	public abstract void onNotLoggedIn();

	/**
	 * Callback when the action could not be performed because the user has an
	 * invalid session
	 */
	public abstract void onSessionInvalid();

	/**
	 * Callback when the action could not be performed because of a specific
	 * error
	 * 
	 * @param message
	 */
	public abstract void onServiceCallFailed(String message);

	/**
	 * Callback when the action could not be performed because of a network
	 * error
	 */
	public abstract void onServiceCallFailedNoNetwork();

}
