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

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.excilys.sugadroid.R;

public class ConnectionSettings extends PreferenceActivity {

	private static final String PREF_USERNAME = "username";
	private static final String PREF_PASSWORD = "password";
	private static final String PREF_URL = "sugar_soap_url";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.connection_settings);
	}

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
