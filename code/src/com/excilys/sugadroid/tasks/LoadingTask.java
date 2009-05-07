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

import com.excilys.sugadroid.activities.interfaces.ICallingLoadingTasksActivity;

public abstract class LoadingTask<T extends ICallingLoadingTasksActivity>
		implements Runnable {

	protected T	activity;

	public LoadingTask(T activity) {
		this.activity = activity;
	}

	@Override
	public void run() {
		activity.onLoadingStarting();
		try {
			doRunLoadingTask();
		} finally {
			activity.onLoadingDone();
		}

	}

	public abstract void doRunLoadingTask();

}
