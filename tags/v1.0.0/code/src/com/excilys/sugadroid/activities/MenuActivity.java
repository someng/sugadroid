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

import java.util.concurrent.RejectedExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.excilys.sugadroid.R;
import com.excilys.sugadroid.activities.delegates.DialogManager;
import com.excilys.sugadroid.beans.SessionBean;
import com.excilys.sugadroid.tasks.GetInitialCalendarTask;
import com.excilys.sugadroid.tasks.LoginInTask;
import com.excilys.sugadroid.util.EagerLoadingCalendar;

public class MenuActivity extends CommonActivity {

	private static final String TAG = MenuActivity.class.getSimpleName();

	private Button accountListButton;
	private Button contactListButton;
	private Button appointmentsButton;
	private TextView loggedInText;
	private TextView loadingText;

	private Runnable loginTask;
	private Runnable getInitialCalendarTask;

	private Class<? extends Activity> forward;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.menu);

		findViews();
		setListeners();
		setTasks();

	}

	public void postSetMessageLoggedIn() {
		runOnUiThread(new Runnable() {
			public void run() {
				setMessageLoggedIn();
			}
		});
	}

	public void setMessageLoggedIn() {
		StringBuilder sb = new StringBuilder(
				getString(R.string.logged_in_message));
		sb.append(" (").append(SessionBean.getInstance().getUsername()).append(
				")");
		loggedInText.setText(sb.toString());
	}

	public void postSetMessageNotLoggedIn() {
		runOnUiThread(new Runnable() {
			public void run() {
				setMessageNotLoggedIn();
			}
		});
	}

	public void setMessageNotLoggedIn() {
		loggedInText.setText(R.string.logged_out_message);
	}

	public void setMessageLogingIn() {
		loggedInText.setText(R.string.loging_in_message);
	}

	private void findViews() {
		accountListButton = (Button) findViewById(R.id.account_list_button);
		contactListButton = (Button) findViewById(R.id.contact_list_button);
		loggedInText = (TextView) findViewById(R.id.logged_in_message);
		appointmentsButton = (Button) findViewById(R.id.appointments_button);
		loadingText = (TextView) findViewById(R.id.loading);

		loadingText.setVisibility(View.INVISIBLE);
	}

	private void setListeners() {
		accountListButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.d(TAG, "account_list button clicked");

				logoutIfParametersChanged();

				if (SessionBean.getInstance().isLoggedIn()) {
					Intent i = new Intent(MenuActivity.this,
							AccountListActivity.class);
					startActivity(i);
				} else {
					String username = ConnectionSettings
							.getUsername(MenuActivity.this);
					if (username != null && !username.equals("")) {
						forward = AccountListActivity.class;
						threadManager.queueUpdate(0, loginTask);
					} else {
						showDialog(DialogManager.DIALOG_ERROR_NOT_LOGGED_IN);
					}
				}
			}
		});

		contactListButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.d(TAG, "contact_list button clicked");

				logoutIfParametersChanged();

				if (SessionBean.getInstance().isLoggedIn()) {
					Intent i = new Intent(MenuActivity.this,
							ContactListActivity.class);
					startActivity(i);
				} else {
					String username = ConnectionSettings
							.getUsername(MenuActivity.this);
					if (username != null && !username.equals("")) {
						forward = ContactListActivity.class;
						threadManager.queueUpdate(0, loginTask);
					} else {
						showDialog(DialogManager.DIALOG_ERROR_NOT_LOGGED_IN);
					}
				}
			}
		});

		appointmentsButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.d(TAG, "appointments button clicked");

				logoutIfParametersChanged();

				if (SessionBean.getInstance().isLoggedIn()) {
					threadManager.queueUpdate(0, getInitialCalendarTask);
				} else {
					String username = ConnectionSettings
							.getUsername(MenuActivity.this);
					if (username != null && !username.equals("")) {
						forward = AppointmentsActivity.class;
						threadManager.queueUpdate(0, loginTask);
					} else {
						showDialog(DialogManager.DIALOG_ERROR_NOT_LOGGED_IN);
					}
				}
			}
		});

	}

	private void setTasks() {

		loginTask = new Runnable() {
			public void run() {

				String username = ConnectionSettings
						.getUsername(MenuActivity.this);
				String password = ConnectionSettings
						.getPassword(MenuActivity.this);

				LoginInTask task = new LoginInTask(MenuActivity.this, username,
						password);
				setMessageLogingIn();
				// Begin task now but don't wait for it
				try {
					threadManager.submitTask(task);
				} catch (RejectedExecutionException e) {
					showDialog(DialogManager.DIALOG_ERROR_CANNOT_LAUNCH_TASK);
					setMessageNotLoggedIn();
				}
			}
		};

		getInitialCalendarTask = new Runnable() {
			public void run() {
				GetInitialCalendarTask task = new GetInitialCalendarTask(
						MenuActivity.this);

				loadingText.setVisibility(View.VISIBLE);
				try {
					threadManager.submitTask(task);
				} catch (RejectedExecutionException e) {
					showDialog(DialogManager.DIALOG_ERROR_CANNOT_LAUNCH_TASK);
				}

			}
		};
	}

	@Override
	protected int getMenuId() {
		return R.menu.menu;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.connection_settings:
			// Launch the connection settings activity
			startActivity(new Intent(this, ConnectionSettings.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void forwardAfterLogin() {
		if (forward != null) {

			if (forward.equals(AppointmentsActivity.class)) {
				threadManager.queueUpdate(0, getInitialCalendarTask);
			} else {

				runOnUiThread(new Runnable() {

					public void run() {
						Intent i = new Intent(MenuActivity.this, forward);
						forward = null;
						startActivity(i);
					}
				});
			}
		}
	}

	public void forwardAppointmentsActivity(final EagerLoadingCalendar calendar) {
		runOnUiThread(new Runnable() {
			public void run() {
				Intent i = new Intent(MenuActivity.this,
						AppointmentsActivity.class);
				i.putExtra(AppointmentsActivity.CALENDAR, calendar);
				loadingText.setVisibility(View.INVISIBLE);
				startActivity(i);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		logoutIfParametersChanged();
		if (!SessionBean.getInstance().isLoggedIn()) {
			setMessageNotLoggedIn();

			String username = ConnectionSettings.getUsername(this);
			if (username != null && !username.equals("")) {
				forward = null;
				threadManager.queueUpdate(0, loginTask);
			}
		} else {
			setMessageLoggedIn();
		}
	}

	private void logoutIfParametersChanged() {

		SessionBean session = SessionBean.getInstance();

		Log.d(TAG, "Checking if parameters changed");

		if (session.isLoggedIn()) {

			if (session.checkLoginParamsChanged(ConnectionSettings
					.getUsername(this), ConnectionSettings
					.getSugarSoapUrl(this), ConnectionSettings
					.getPassword(this).hashCode())) {

				session.logout();
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Do nothing => just overriding to avoid the default behavior
	}

}
