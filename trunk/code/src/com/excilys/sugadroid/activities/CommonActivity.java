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

import org.ksoap2.transport.Transport;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.excilys.sugadroid.R;
import com.excilys.sugadroid.activities.delegates.DialogManager;
import com.excilys.sugadroid.activities.delegates.ThreadPostingManager;
import com.excilys.sugadroid.activities.delegates.DialogManager.DialogValues;
import com.excilys.sugadroid.activities.interfaces.IAuthenticatedActivity;
import com.excilys.sugadroid.activities.interfaces.IAuthenticatingActivity;
import com.excilys.sugadroid.beans.ISessionBean;
import com.excilys.sugadroid.beans.ISessionBean.SessionState;
import com.excilys.sugadroid.di.BeanHolder;
import com.excilys.sugadroid.services.interfaces.ILoginServices;
import com.excilys.sugadroid.tasks.LoginInTask;

/**
 * This activity should be the common activity to all application activities. It
 * enables dialogs, threaded tasks launching, authenticating and authenticated
 * tasks launching
 * 
 * @author Pierre-Yves Ricau
 * 
 */
public abstract class CommonActivity extends Activity implements
		IAuthenticatingActivity, IAuthenticatedActivity {

	private static final String TAG = CommonActivity.class.getSimpleName();

	private ThreadPostingManager threadManager;
	protected DialogManager dialogManager;

	// The bean that keeps the session informations
	protected ISessionBean sessionBean;

	// The runnable used to authenticate
	private Runnable loginTask;

	// A task waiting for authentication to proceed before being launched (via
	// callbacks on this activity)
	private Runnable pendingAuthenticatedTask;

	private TextView loadingText;

	protected int menuId = R.menu.back_to_menu;

	public static final String ITEM_IDENTIFIER = "ITEM";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final BeanHolder holder = BeanHolder.getInstance();

		initActivity(new DialogManager(this), new ThreadPostingManager(),
				holder.getSessionBean(), holder.getLoginServices(), holder
						.getTransport());

	}

	/**
	 * The method that really inits the activity. It is set public so that an
	 * activity could be created without launching the application, for example
	 * for testing purposes.
	 * 
	 * @param dialogManager
	 * @param threadManager
	 * @param sessionBean
	 * @param casClient
	 * @param service
	 */
	public void initActivity(DialogManager dialogManager,
			ThreadPostingManager threadManager, ISessionBean sessionBean,
			ILoginServices loginService, Transport transport) {

		this.sessionBean = sessionBean;

		this.dialogManager = dialogManager;
		this.threadManager = threadManager;

		initLoginTask(loginService, transport);
	}

	/**
	 * Method that creates the login task. This task deals with the GUI, and
	 * calls the Web Service login task
	 * 
	 * @param casClient
	 * @param service
	 */
	private void initLoginTask(final ILoginServices loginService,
			final Transport transport) {
		loginTask = new Runnable() {

			public void run() {

				// Set in session bean that we are not logged in anymore, and we
				// that
				// are trying to login
				sessionBean.setLoginIn();

				// Update interface to show that login is occurring
				showLoginIn();
				showShortInfo(R.string.toast_login_in);

				// Retrieve login informations from settings
				String username = ConnectionSettings
						.getUsername(CommonActivity.this);
				String password = ConnectionSettings
						.getPassword(CommonActivity.this);

				// Update bean informations (url)
				transport.setUrl(ConnectionSettings
						.getSugarSoapUrl(CommonActivity.this));

				// Create task
				LoginInTask loginTask = new LoginInTask(CommonActivity.this,
						username, password, loginService, sessionBean);

				// launch task
				try {
					threadManager.submitTask(loginTask);
				} catch (RejectedExecutionException e) {
					// Update interface to show that we are not login in and not
					// logged in
					Log.e(TAG, Log.getStackTraceString(e));
					showNotLoggedIn();
					// Show toast message to say that an error occurred due to
					// RejectedExecutionExceptions
					showShortInfo(e.getMessage());
				}
			}
		};
	}

	/**
	 * Executes the login task, only if login is not already occurring
	 */
	protected void login() {
		if (!sessionBean.getState().equals(SessionState.LOGIN_IN)) {
			threadManager.postOnGuiThread(loginTask);
		}
	}

	/**
	 * Executes on the GUI Thread a Runnable that requires the user to be
	 * authenticated before being executed. This task will probably do something
	 * like retrieving information from the GUI, and launching another runnable
	 * that will make calls to Web Services using these informations.
	 * 
	 * @param task
	 */
	protected void executeOnGuiThreadAuthenticatedTask(Runnable task) {

		switch (sessionBean.getState()) {
		case LOGGED_IN:
			pendingAuthenticatedTask = null;
			threadManager.postOnGuiThread(task);
			break;
		case LOGIN_IN:
			pendingAuthenticatedTask = task;
			break;
		case NOT_LOGGED_IN:
			pendingAuthenticatedTask = task;
			login();
		}
	}

	/**
	 * Executes on the GUI Thread with a delay a Runnable that requires the user
	 * to be authenticated before being executed. This task will probably do
	 * something like retrieving information from the GUI, and launching another
	 * runnable that will make calls to Web Services using these informations.
	 * 
	 * @param task
	 */
	protected void executeDelayedOnGuiThreadAuthenticatedTask(int delayMillis,
			Runnable task) {

		switch (sessionBean.getState()) {
		case LOGGED_IN:
			pendingAuthenticatedTask = null;
			threadManager.postDelayedOnGuiThread(delayMillis, task);
			break;
		case LOGIN_IN:
			// No delay will be applied if authenticating
			pendingAuthenticatedTask = task;
			break;
		case NOT_LOGGED_IN:
			// No delay will be applied if not authenticated
			pendingAuthenticatedTask = task;
			login();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		threadManager.onDestroy();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		return dialogManager.onCreateDialog(id);
	}

	/**
	 * Show a custom dialog box
	 * 
	 * @param title
	 * @param message
	 */
	public void showCustomDialog(String title, String message) {
		// Will call the callback onCreateDialog (on a thread)
		dialogManager.showCustomDialog(title, message);
	}

	/**
	 * Show a dialog box (chosen from the available ones in DialogValues)
	 * 
	 * @param dialog
	 */
	public void showDialog(DialogValues dialog) {
		showDialog(dialog.ordinal());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(menuId, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.connection_settings:
			ConnectionSettings.saveCurrentSettings(this);
			startActivity(new Intent(this, ConnectionSettings.class));
			return true;
		case R.id.back_to_menu_settings:
			// Launch the settings activity
			// startActivity(new Intent(this, MenuActivity.class));
			backToMenuActivity();
			return true;
		case R.id.about:
			showCustomDialog(getString(R.string.about_title),
					getString(R.string.about_text));
			return true;
		case R.id.settings:
			// Launch the settings activity
			startActivity(new Intent(this, GeneralSettings.class));
			return true;
		}
		return false;
	}

	// We want to be able to know when we are back from an activity.
	// However, we do not care about which activity called which activity.
	@Override
	public void startActivity(Intent intent) {
		super.startActivityForResult(intent, 1);
	}

	public void backToMenuActivity() {
		setResult(RESULT_OK);
		finish();
	}

	// This method has to be overridden by the MenuActivity (the first activity
	// on the stack)
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			backToMenuActivity();
		}
	}

	/**
	 * This method is used to submit a task to the threadManager, and deal with
	 * the potential RejectedExecutionException, showing the error in the UI.
	 * 
	 * @param task
	 */
	protected void submitRejectableTask(Runnable task) {
		try {
			threadManager.submitTask(task);
		} catch (RejectedExecutionException e) {
			if (!isFinishing()) {
				Log.e(TAG, Log.getStackTraceString(e));
				showShortInfo(e.getMessage());
			}
		}
	}

	/**
	 * Show a temporary info message that does not disturb user interaction, for
	 * a short time
	 * 
	 * @param stringId
	 * 
	 */
	public void showShortInfo(int stringId) {
		Toast.makeText(this, stringId, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Show a temporary info message that does not disturb user interaction, for
	 * a long time
	 * 
	 * @param stringId
	 * 
	 */
	public void showLongInfo(int stringId) {
		Toast.makeText(this, stringId, Toast.LENGTH_LONG).show();
	}

	/**
	 * Show a temporary info message that does not disturb user interaction, for
	 * a short time
	 * 
	 * @param stringId
	 * 
	 */
	public void showShortInfo(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Show a temporary info message that does not disturb user interaction, for
	 * a long time
	 * 
	 * @param stringId
	 * 
	 */
	public void showLongInfo(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	/**
	 * This is a callback called by a LoginTask. It updates the interface to
	 * show that the login occurred successfully, and executes the pending
	 * authenticated task if any.
	 */
	public void onLoginSuccessful() {
		runOnUiThread(new Runnable() {
			public void run() {
				showLoggedIn();
				showShortInfo(R.string.toast_login_successful);
				if (pendingAuthenticatedTask != null) {
					submitRejectableTask(pendingAuthenticatedTask);
					pendingAuthenticatedTask = null;
				}
			}
		});
	}

	/**
	 * This is a callback called by a LoginTask. It updates the interface to
	 * show that the login failed. The pendingAuthenticatedTask, if any, is
	 * canceled.
	 */
	public void onLoginFailed(final String message) {
		runOnUiThread(new Runnable() {
			public void run() {
				showNotLoggedIn();
				showShortInfo(getText(R.string.toast_login_failed) + " "
						+ message);
				pendingAuthenticatedTask = null;
			}
		});
	}

	/**
	 * This is a callback called by a LoginTask. It updates the interface to
	 * show that the login failed due to bad credentials. The
	 * pendingAuthenticatedTask, if any, is canceled.
	 */
	public void onLoginFailedBadCredentials() {
		runOnUiThread(new Runnable() {
			public void run() {
				showNotLoggedIn();
				showShortInfo(R.string.toast_login_failed_bad_credentials);
				pendingAuthenticatedTask = null;
			}
		});
	}

	/**
	 * This is a callback called by a LoginTask. It updates the interface to
	 * show that the login failed due to a network problem. The
	 * pendingAuthenticatedTask, if any, is canceled.
	 */
	public void onLoginFailedNoNetwork() {
		runOnUiThread(new Runnable() {
			public void run() {
				showNotLoggedIn();
				showShortInfo(R.string.toast_login_failed_no_network);
				pendingAuthenticatedTask = null;
			}
		});
	}

	/**
	 * Updates UI to show that user is not authenticated (post on thread)
	 */
	protected void showNotLoggedIn() {
		((TextView) findViewById(R.id.login_info))
				.setText(R.string.top_not_logged_in);
	}

	/**
	 * Updates UI to show that user is authenticated (post on thread)
	 */
	protected void showLoggedIn() {
		((TextView) findViewById(R.id.login_info))
				.setText(getText(R.string.top_logged_in) + " ("
						+ sessionBean.getUsername() + ")");
	}

	/**
	 * Updates UI to show that user is authenticating (post on thread)
	 */
	protected void showLoginIn() {
		((TextView) findViewById(R.id.login_info))
				.setText(R.string.top_login_in);
	}

	/**
	 * Logout the user, updates the UI to show that the user is not logged in,
	 * and show a message
	 */
	public void onNotLoggedIn() {
		runOnUiThread(new Runnable() {
			public void run() {
				sessionBean.logout();
				showNotLoggedIn();
				showShortInfo(R.string.toast_service_no_session);
			}
		});
	}

	/**
	 * Show a message to say that the call to the service failed, and gives the
	 * precise message
	 */
	public void onServiceCallFailed(final String message) {
		runOnUiThread(new Runnable() {
			public void run() {
				showShortInfo(getText(R.string.toast_service_call_failed) + " "
						+ message);
			}
		});
	}

	/**
	 * Show a message to say that the call to the service failed, due to network
	 * errors
	 */
	public void onServiceCallFailedNoNetwork() {
		runOnUiThread(new Runnable() {
			public void run() {
				showShortInfo(R.string.toast_service_call_failed_no_network);
			}
		});
	}

	/**
	 * Logout the user, updates the UI to show that the user is not logged in,
	 * and show a message saying that the session is invalid
	 */
	public void onSessionInvalid() {
		runOnUiThread(new Runnable() {
			public void run() {
				sessionBean.logout();
				showNotLoggedIn();
				showShortInfo(R.string.toast_service_session_invalid);
			}
		});
	}

	/**
	 * This is were we deal with coming back from the ConnectionSettings
	 * activity, trying to login if parameters changed. We also deal with the
	 * login info, updated if needed.
	 */
	@Override
	public void onResume() {
		super.onResume();

		switch (sessionBean.getState()) {
		case LOGIN_IN:
			showLoggedIn();
			break;
		case NOT_LOGGED_IN:
			showNotLoggedIn();
			break;
		case LOGGED_IN:
			showLoggedIn();
		}

		if (ConnectionSettings.currentSettingsChanged(this)) {
			pendingAuthenticatedTask = null;
			login();
		}
	}

	protected void showLoadingText() {
		if (loadingText == null) {
			loadingText = (TextView) findViewById(R.id.loading_information);
		}
		loadingText.setVisibility(View.VISIBLE);
	}

	protected void hideLoadingText() {
		if (loadingText == null) {
			loadingText = (TextView) findViewById(R.id.loading_information);
		}
		loadingText.setVisibility(View.INVISIBLE);
	}

}
