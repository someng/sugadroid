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
 * An activity that provides callbacks to show informations after authenticating
 * 
 * @author Pierre-Yves Ricau
 * 
 */
public interface IAuthenticatingActivity extends ICallingLoadingTasksActivity {

	/**
	 * Callback when login occurred successfully
	 */
	public abstract void onLoginSuccessful();

	/**
	 * Callback when login action failed due to a network error
	 */
	public abstract void onLoginFailedNoNetwork();

	/**
	 * Callback when login action failed due to wrong username/password
	 * combination
	 */
	public abstract void onLoginFailedBadCredentials();

	/**
	 * Callback when login action failed due to a specific error
	 * 
	 * @param message
	 */
	public abstract void onLoginFailed(String message);

}