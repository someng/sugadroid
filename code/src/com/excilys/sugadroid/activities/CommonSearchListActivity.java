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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.excilys.sugadroid.R;
import com.excilys.sugadroid.activities.interfaces.ICallingGetItemDetailsActivity;

public abstract class CommonSearchListActivity<Bean extends Serializable, GetItemDetailsTask extends Runnable, SearchItemsTask extends Runnable>
		extends CommonActivity implements ICallingGetItemDetailsActivity<Bean> {

	private final static String TAG = CommonSearchListActivity.class
			.getSimpleName();

	protected EditText itemSearchInput;
	protected ListView itemSearchList;
	protected TextView itemSearchInfoMessage;
	protected Runnable searchItemsTask;
	protected Runnable getItemDetailsTask;
	protected ArrayAdapter<Bean> itemAdapter;
	protected List<Bean> items;
	protected Bean selectedItem;
	protected Bean moreResults;
	protected String lastSearch;
	protected int offset;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.item_list);

		findViews();
		setListeners();
		setTasks();

		initMoreResultsBean();
		items = new ArrayList<Bean>();
	}

	public abstract void initMoreResultsBean();

	private void findViews() {
		itemSearchInput = (EditText) findViewById(R.id.item_search_edit);
		itemSearchList = (ListView) findViewById(R.id.item_search_list);
		itemSearchInfoMessage = (TextView) findViewById(R.id.item_search_info_message);
	}

	public int getOffset() {
		return offset;
	}

	private void setOffset(int offset) {
		this.offset = offset;
	}

	public void updateItemList(final List<Bean> items) {
		final List<Bean> allItems = this.items;

		runOnUiThread(new Runnable() {
			public void run() {
				if (allItems.contains(moreResults)) {
					allItems.remove(moreResults);
				}
				allItems.addAll(items);
				int oldOffset = getOffset();
				setOffset(allItems.size());
				Log.d(TAG, "size: " + allItems.size());
				if (items.size() == GeneralSettings
						.getSearchListMaxResults(CommonSearchListActivity.this)) {
					allItems.add(moreResults);
				}
				if (itemAdapter == null) {
					itemAdapter = new ArrayAdapter<Bean>(
							CommonSearchListActivity.this,
							android.R.layout.simple_list_item_1, allItems);
				}
				itemSearchList.setAdapter(itemAdapter);
				itemSearchList.setSelection(oldOffset);
				if (allItems.size() == 0) {
					itemSearchInfoMessage
							.setText(R.string.item_search_no_result);
				} else {
					itemSearchInfoMessage.setText("");
				}
			}
		});
	}

	@Override
	public void onItemDetailsLoaded(final Bean item) {
		runOnUiThread(new Runnable() {
			public void run() {
				Log.d(TAG, "forwarding to item details activity");
				Intent intent = new Intent(CommonSearchListActivity.this,
						getItemDetailsActivity());
				intent.putExtra(CommonActivity.ITEM_IDENTIFIER, item);
				itemSearchInfoMessage.setText("");
				startActivity(intent);
			}
		});
	}

	public abstract Class<? extends Activity> getItemDetailsActivity();

	private void setListeners() {

		itemSearchList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Log.d(TAG, "item clicked " + position + " " + arg3);

				if (position == getOffset()) {
					executeDelayedOnGuiThreadAuthenticatedTask(500,
							searchItemsTask);
				} else {
					selectedItem = items.get(position);
					executeDelayedOnGuiThreadAuthenticatedTask(500,
							getItemDetailsTask);
				}
			}
		});
		itemSearchInput.addTextChangedListener(new TextWatcher() {
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				/* Do nothing */
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				executeDelayedOnGuiThreadAuthenticatedTask(800, searchItemsTask);
			}

			public void afterTextChanged(Editable s) {
				/* Do nothing */
			}
		});

	}

	private void setTasks() {
		searchItemsTask = new Runnable() {
			public void run() {
				// Get the text from the inputs
				String search = itemSearchInput.getText().toString();

				if (!search.equals("")) {

					if (!search.equals(lastSearch)) {
						items.clear();
						setOffset(0);
					}

					lastSearch = search;

					// Begin task now but don't wait for it
					SearchItemsTask task = getSearchItemTaskInstance(search);

					submitRejectableTask(task);

				}
			}
		};
		getItemDetailsTask = new Runnable() {
			public void run() {
				GetItemDetailsTask task = getItemDetailsTaskInstance(selectedItem);

				submitRejectableTask(task);

			}

		};
	}

	public abstract SearchItemsTask getSearchItemTaskInstance(String search);

	public abstract GetItemDetailsTask getItemDetailsTaskInstance(
			Bean selectedItem);

}
