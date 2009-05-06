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

	public enum DialogValues {
		ERROR_DAY_NOT_LOADED, CUSTOM, CONTACT_ADDED;

		public static DialogValues valueOf(int i) {
			return DialogValues.values()[i];
		}

	};

	public DialogManager(Activity activity) {
		this.activity = activity;
	}

	public Dialog onCreateDialog(int dialogId) {
		return onCreateDialog(DialogValues.valueOf(dialogId));
	}

	public Dialog onCreateDialog(DialogValues dialogId) {

		int title;
		int message;

		switch (dialogId) {

		// More dialogs here :
		// http://developer.android.com/guide/samples/ApiDemos/src/com/example/android/apis/app/AlertDialogSamples.html

		case CUSTOM:
			return new AlertDialog.Builder(activity).setTitle(
					customOkDialogTitle).setMessage(customOkDialogMessage)
					.setNeutralButton(R.string.dialog_OK_button, null).create();

		case ERROR_DAY_NOT_LOADED:
			title = R.string.day_not_loaded_title;
			message = R.string.day_not_loaded_message;
			break;
		case CONTACT_ADDED:
			title = R.string.contact_added_title;
			message = R.string.contact_added_message;
			break;
		default:
			title = R.string.dialog_unknow_error_title;
			message = R.string.dialog_unknow_error_message;
		}

		return new AlertDialog.Builder(activity).setTitle(title).setMessage(
				message).setNeutralButton(R.string.dialog_OK_button, null)
				.create();

	}

	public void showCustomDialog(String title, String message) {
		customOkDialogTitle = title;
		customOkDialogMessage = message;
		activity.showDialog(DialogValues.CUSTOM.ordinal());
	}

}
