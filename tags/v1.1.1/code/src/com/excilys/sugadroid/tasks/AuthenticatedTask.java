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

import android.util.Log;

import com.excilys.sugadroid.activities.interfaces.IAuthenticatedActivity;
import com.excilys.sugadroid.services.exceptions.InvalidResponseException;
import com.excilys.sugadroid.services.exceptions.InvalidSessionException;
import com.excilys.sugadroid.services.exceptions.NotLoggedInException;
import com.excilys.sugadroid.services.exceptions.ServiceException;

/**
 * An abstract Runnable task that makes calls to an authenticated Web Services,
 * delegating the call in the method doRun(). It's main purpose is to catch
 * runtime exceptions linked authentication, authorization and network issues.
 * 
 * @author Pierre-Yves Ricau
 * 
 */
public abstract class AuthenticatedTask<T extends IAuthenticatedActivity>
		extends LoadingTask<T> {

	private static final String TAG = AuthenticatedTask.class.getSimpleName();

	public AuthenticatedTask(T activity) {
		super(activity);
	}

	@Override
	public void doRunLoadingTask() {
		try {
			doRunAuthenticatedTask();
		} catch (InvalidSessionException e) {
			Log.e(TAG, Log.getStackTraceString(e));
			activity.onSessionInvalid();
		} catch (InvalidResponseException e) {
			Log.e(TAG, Log.getStackTraceString(e));
			activity.onServiceCallFailedNoNetwork();
		} catch (NotLoggedInException e) {
			Log.e(TAG, Log.getStackTraceString(e));
			activity.onNotLoggedIn();
		} catch (ServiceException e) {
			Log.e(TAG, Log.getStackTraceString(e));
			activity.onServiceCallFailed(e.getMessage());
		}

	}

	protected abstract void doRunAuthenticatedTask() throws ServiceException;

}
