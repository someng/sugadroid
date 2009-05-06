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

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.excilys.sugadroid.R;
import com.excilys.sugadroid.activities.delegates.DialogManager;
import com.excilys.sugadroid.activities.delegates.ThreadPostingManager;
import com.excilys.sugadroid.activities.delegates.DialogManager.DialogValues;
import com.excilys.sugadroid.activities.interfaces.BaseActivity;
import com.excilys.sugadroid.beans.ISessionBean;

/**
 * This activity should be the common activity to all application activities. It
 * enables dialogs, threaded tasks launching, authenticating and authenticated
 * tasks launching
 * 
 * @author Pierre-Yves Ricau
 * 
 */
public abstract class CommonActivity extends Activity implements BaseActivity {

	protected ThreadPostingManager threadManager;
	protected DialogManager dialogManager;

	// The bean that keeps the session informations
	protected ISessionBean sessionBean;

	// The runnable used to authenticate
	private Runnable loginTask;

	// A task waiting for authentication to proceed before being launched (via
	// callbacks on this activity)
	private Runnable pendingAuthenticatedTask;

	public static final String ITEM_IDENTIFIER = "ITEM";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		threadManager = new ThreadPostingManager();
		dialogManager = new DialogManager(this);

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

	public void postShowCustomDialog(final String title, final String message) {
		runOnUiThread(new Runnable() {
			public void run() {
				showCustomDialog(title, message);
			}
		});
	}

	public void postShowDialog(final int id) {
		runOnUiThread(new Runnable() {
			public void run() {
				showDialog(id);
			}
		});
	}

	public void postShowDialog(final DialogValues dialog) {
		runOnUiThread(new Runnable() {
			public void run() {
				showDialog(dialog);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(getMenuId(), menu);
		return true;
	}

	protected int getMenuId() {
		return R.menu.back_to_menu;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

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

}
