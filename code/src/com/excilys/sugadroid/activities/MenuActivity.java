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

package com.excilys.sugadroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.excilys.sugadroid.R;
import com.excilys.sugadroid.beans.ISessionBean.SessionState;
import com.excilys.sugadroid.di.BeanHolder;
import com.excilys.sugadroid.services.interfaces.IAppointmentServices;
import com.excilys.sugadroid.tasks.GetInitialCalendarTask;
import com.excilys.sugadroid.util.EagerLoadingCalendar;

public class MenuActivity extends CommonActivity {

	private static final String TAG = MenuActivity.class.getSimpleName();

	private Button accountListButton;
	private Button contactListButton;
	private Button appointmentsButton;

	protected int menuId = R.menu.menu;

	private Runnable getInitialCalendarTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.menu);

		findViews();
		setListeners();
		setTasks();

		if (sessionBean.getState().equals(SessionState.NOT_LOGGED_IN)) {
			login();
		}

	}

	private void findViews() {
		accountListButton = (Button) findViewById(R.id.account_list_button);
		contactListButton = (Button) findViewById(R.id.contact_list_button);
		appointmentsButton = (Button) findViewById(R.id.appointments_button);
		hideLoadingText();
	}

	private void setListeners() {
		accountListButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				Log.d(TAG, "account_list button clicked");

				executeOnGuiThreadAuthenticatedTask(new Runnable() {
					public void run() {

						Intent i = new Intent(MenuActivity.this,
								AccountListActivity.class);
						startActivity(i);

					}
				});
			}
		});

		contactListButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.d(TAG, "contact_list button clicked");

				executeOnGuiThreadAuthenticatedTask(new Runnable() {
					public void run() {

						Intent i = new Intent(MenuActivity.this,
								ContactListActivity.class);
						startActivity(i);

					}
				});
			}
		});

		appointmentsButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.d(TAG, "appointments button clicked");

				executeOnGuiThreadAuthenticatedTask(getInitialCalendarTask);
			}
		});

	}

	private void setTasks() {

		final IAppointmentServices appointmentServices = BeanHolder
				.getInstance().getAppointmentServices();

		getInitialCalendarTask = new Runnable() {
			public void run() {
				GetInitialCalendarTask task = new GetInitialCalendarTask(
						MenuActivity.this,
						appointmentServices,
						GeneralSettings
								.getAppointmentsLoadingBefore(MenuActivity.this),
						GeneralSettings
								.getAppointmentsLoadingAfter(MenuActivity.this));

				showLoadingText();

				submitRejectableTask(task);

			}
		};
	}

	public void onInitialCalendarLoaded(final EagerLoadingCalendar calendar) {
		runOnUiThread(new Runnable() {
			public void run() {
				Intent i = new Intent(MenuActivity.this,
						AppointmentsActivity.class);
				i.putExtra(AppointmentsActivity.CALENDAR, calendar);
				hideLoadingText();
				startActivity(i);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Do nothing => just overriding to avoid the CommonActivity behavior
	}

}
