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

import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CommonListActivity extends CommonActivity {

	private ListView listView;
	private TextView empty;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	// Should always be called by child
	protected void findViews() {
		listView = (ListView) findViewById(android.R.id.list);
		empty = (TextView) findViewById(android.R.id.empty);
		listView.setEmptyView(empty);
	}

	public ListView getListView() {
		return listView;
	}

	public void hideEmpty() {
		empty.setVisibility(View.INVISIBLE);
	}

	public void showEmpty() {
		empty.setVisibility(View.VISIBLE);
	}

	public void setListAdapter(ListAdapter adapter) {
		listView.setAdapter(adapter);
	}

}
