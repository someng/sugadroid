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

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.excilys.sugadroid.R;

public class ConnectionSettings extends PreferenceActivity {

	private static final String PREF_USERNAME = "username";
	private static final String PREF_PASSWORD = "password";
	private static final String PREF_URL = "sugar_soap_url";

	private static final Map<String, String> savedSettings = new HashMap<String, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.connection_settings);
	}

	/**
	 * This methods saves the current settings, to be able to check later if
	 * settings changed
	 * 
	 * @param context
	 */
	public static void saveCurrentSettings(Context context) {
		savedSettings.clear();
		savedSettings.put(PREF_USERNAME, getUsername(context));
		savedSettings.put(PREF_PASSWORD, getPassword(context));
		savedSettings.put(PREF_URL, getSugarSoapUrl(context));
	}

	/**
	 * This methods tells is the settings changed. It is used by a callback
	 * called when resuming from the ConnectionSettingsActivity
	 * 
	 * @param context
	 *            an activity
	 * @return
	 */
	public static boolean currentSettingsChanged(Context context) {

		if (savedSettings.isEmpty()) {
			return false;
		}

		try {
			if (!getUsername(context).equals(savedSettings.get(PREF_USERNAME))) {
				return true;
			}

			if (!getPassword(context).equals(savedSettings.get(PREF_PASSWORD))) {
				return true;
			}

			if (!getSugarSoapUrl(context).equals(savedSettings.get(PREF_URL))) {
				return true;
			}

			return false;
		} finally {
			savedSettings.clear();
		}
	}

	// Static getters (extracting data from context)

	public static String getUsername(Context context) {

		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(PREF_USERNAME,
						context.getString(R.string.default_username));
	}

	public static String getPassword(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(PREF_PASSWORD,
						context.getString(R.string.default_password));
	}

	public static String getSugarSoapUrl(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(PREF_URL, context.getString(R.string.default_url));
	}

}
