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

package com.excilys.sugadroid.activities.delegates;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.os.Handler;

/**
 * This class is a delegate that provides methods to deal with threads in
 * Android
 * 
 * @author Pierre-Yves Ricau
 * 
 */
public class ThreadPostingManager {

	// All the tasks taking time will be executed on this thread
	private ExecutorService executor;

	// Task that might be canceled if replace by a new task
	// This task is run on the GUI thread
	private Runnable updateTask;

	// GUI Thread
	private Handler guiThread;

	// Task that is being executed by the executor, not on the GUI Thread
	// Also enables the program to know when a task is done using the get()
	// method.
	@SuppressWarnings(value = "unchecked")
	private Future taskPending;

	public ThreadPostingManager() {
		guiThread = new Handler();
		executor = Executors.newSingleThreadExecutor();
	}

	public void submitTask(Runnable task) {
		// Cancel previous call if there is one
		if (taskPending != null) {
			taskPending.cancel(true);
		}

		taskPending = executor.submit(task);
	}

	/** Request an update to start after a short delay */
	public void queueUpdate(long delayMillis, Runnable task) {
		// Cancel previous update if it hasn't started yet
		if (updateTask != null) {
			guiThread.removeCallbacks(updateTask);
		}

		updateTask = task;

		// Start an update if nothing happens after a few milliseconds
		guiThread.postDelayed(task, delayMillis);
	}

	public void onDestroy() {
		// Terminate extra threads here
		executor.shutdownNow();
	}

}
