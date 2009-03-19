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

public class GeneralSettings extends PreferenceActivity {

	private static final String PREF_SEARCH_LIST_MAX_RESULTS = "search_list_max_results";
	private static final String PREF_ACCOUNT_MAX_RESULTS = "account_max_results";
	private static final String PREF_GMT = "gmt";
	private static final String PREF_APPOINTMENTS_LOADING_BEFORE = "appointments_loading_before";
	private static final String PREF_APPOINTMENTS_LOADING_AFTER = "appointments_loading_after";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}

	public static int getSearchListMaxResults(Context context) {
		String result = PreferenceManager
				.getDefaultSharedPreferences(context)
				.getString(
						PREF_SEARCH_LIST_MAX_RESULTS,
						context
								.getString(R.string.default_search_list_max_results));
		return Integer.parseInt(result);
	}

	public static int getAccountMaxResults(Context context) {
		String result = PreferenceManager
				.getDefaultSharedPreferences(context)
				.getString(PREF_ACCOUNT_MAX_RESULTS,
						context.getString(R.string.default_account_max_results));
		return Integer.parseInt(result);
	}

	public static int getGMT(Context context) {
		String result = PreferenceManager.getDefaultSharedPreferences(context)
				.getString(PREF_GMT, context.getString(R.string.default_gmt));
		return Integer.parseInt(result);
	}

	public static int getAppointmentsLoadingBefore(Context context) {
		String result = PreferenceManager
				.getDefaultSharedPreferences(context)
				.getString(
						PREF_APPOINTMENTS_LOADING_BEFORE,
						context
								.getString(R.string.default_appointments_loading_before));
		return Integer.parseInt(result);
	}

	public static int getAppointmentsLoadingAfter(Context context) {
		String result = PreferenceManager
				.getDefaultSharedPreferences(context)
				.getString(
						PREF_APPOINTMENTS_LOADING_AFTER,
						context
								.getString(R.string.default_appointments_loading_after));
		return Integer.parseInt(result);
	}

}
