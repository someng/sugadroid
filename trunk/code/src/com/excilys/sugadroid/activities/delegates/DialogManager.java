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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;

import com.excilys.sugadroid.R;

/**
 * This class is a delegate that deals with dialog windows (only OK windows
 * until now)
 * 
 * @author Pierre-Yves Ricau
 * 
 */
public class DialogManager {

	private String customOkDialogTitle;
	private String customOkDialogMessage;

	private Activity activity;

	public static final int DIALOG_ERROR_DAY_NOT_LOADED = 1;
	public static final int DIALOG_ERROR_CANNOT_LAUNCH_TASK = 2;
	public static final int DIALOG_ERROR_CUSTOM = 3;
	public static final int DIALOG_ERROR_LOGIN_FAILED = 4;
	public static final int DIALOG_ERROR_NOT_LOGGED_IN = 5;
	public static final int DIALOG_ERROR_INVALID_RESPONSE = 6;

	public DialogManager(Activity activity) {
		this.activity = activity;
	}

	public Dialog onCreateDialog(int id) {

		int title;
		int message;

		switch (id) {

		// More dialogs here :
		// http://developer.android.com/guide/samples/ApiDemos/src/com/example/android/apis/app/AlertDialogSamples.html
		case DIALOG_ERROR_CANNOT_LAUNCH_TASK:
			title = R.string.error_launch_task_title;
			message = R.string.error_launch_task_message;
			break;
		case DIALOG_ERROR_CUSTOM:
			return new AlertDialog.Builder(activity).setTitle(
					customOkDialogTitle).setMessage(customOkDialogMessage)
					.setNeutralButton(R.string.error_ok_button,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

								}
							}).create();
		case DIALOG_ERROR_LOGIN_FAILED:
			title = R.string.error_login_failed_title;
			message = R.string.error_login_failed_message;
			break;
		case DIALOG_ERROR_NOT_LOGGED_IN:
			title = R.string.not_logged_in_title;
			message = R.string.not_logged_in_message;
			break;
		case DIALOG_ERROR_DAY_NOT_LOADED:
			title = R.string.day_not_loaded_title;
			message = R.string.day_not_loaded_message;
			break;
		case DIALOG_ERROR_INVALID_RESPONSE:
			title = R.string.invalid_response_title;
			message = R.string.invalid_response_message;
			break;
		default:
			return null;
		}

		return new AlertDialog.Builder(activity).setTitle(title).setMessage(
				message).setNeutralButton(R.string.error_ok_button,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				}).create();

	}

	public void showCustomDialog(String title, String message) {
		customOkDialogTitle = title;
		customOkDialogMessage = message;
		activity.showDialog(DIALOG_ERROR_CUSTOM);
	}

}
