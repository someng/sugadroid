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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.excilys.sugadroid.R;
import com.excilys.sugadroid.activities.delegates.DialogManager.DialogValues;
import com.excilys.sugadroid.activities.interfaces.CallingGetItemDetailsActivity;
import com.excilys.sugadroid.beans.AccountBean;
import com.excilys.sugadroid.beans.ContactBean;
import com.excilys.sugadroid.tasks.GetAccountContactsTask;
import com.excilys.sugadroid.tasks.GetContactDetailsTask;

public class AccountDetailsActivity extends CommonListActivity implements
		CallingGetItemDetailsActivity<ContactBean> {

	private static final String TAG = AccountDetailsActivity.class
			.getSimpleName();

	private AccountBean account;

	private TextView nameText;
	private TextView loadingText;
	private TextView addressText;
	private Button addressButton;
	private Button phoneButton;

	private ArrayAdapter<ContactBean> itemAdapter;

	private Runnable getAccountContactsTask;

	private Runnable getItemDetailsTask;

	private ContactBean selectedItem;

	private ContactBean moreResults;

	private List<ContactBean> accountContacts;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.account_details);

		account = (AccountBean) getIntent().getSerializableExtra(
				CommonActivity.ITEM_IDENTIFIER);

		moreResults = new ContactBean();
		moreResults.setFirstName(getString(R.string.item_search_more_result));

		findViews();
		setContents();
		setTasks();
		setAdapters();
		setListeners();

		if (accountContacts.size() == 0) {
			threadManager.queueUpdate(0, getAccountContactsTask);
		}

	}

	@Override
	protected void findViews() {
		super.findViews();
		nameText = (TextView) findViewById(R.id.account_name_text);

		loadingText = (TextView) findViewById(R.id.info_message);

		addressButton = (Button) findViewById(R.id.address_button);
		phoneButton = (Button) findViewById(R.id.phone_button);
		addressText = (TextView) findViewById(R.id.address_text);
	}

	private void setContents() {
		nameText.setText(account.toString());
		nameText.setVisibility(View.VISIBLE);
		loadingText.setVisibility(View.INVISIBLE);

		if (account.getPhoneOffice() != null
				&& !account.getPhoneOffice().equals("")) {
			phoneButton.setText(account.getPhoneOffice());
		} else {
			phoneButton.setText(R.string.not_available);
			phoneButton.setClickable(false);
		}

		String billingFullAddress = account.getBillingFullAddress();
		if (billingFullAddress != null && !billingFullAddress.equals("")) {
			addressText.setText(billingFullAddress);
		} else {
			addressText.setText(R.string.not_available);
			addressButton.setText(R.string.not_available);
			addressButton.setClickable(false);
		}
	}

	private void setAdapters() {

		accountContacts = new ArrayList<ContactBean>();

		itemAdapter = new ArrayAdapter<ContactBean>(this,
				android.R.layout.simple_list_item_1, accountContacts);

		setListAdapter(itemAdapter);
	}

	private void setListeners() {

		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				List<ContactBean> contacts = accountContacts;
				if (position == contacts.size() - 1
						&& contacts.get(position).equals(moreResults)) {
					threadManager.queueUpdate(0, getAccountContactsTask);
				} else {
					selectedItem = itemAdapter.getItem(position);
					threadManager.queueUpdate(500, getItemDetailsTask);
				}
			}
		});

		if (addressButton.isClickable()) {
			addressButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showAddress(account.getBillingFullAddress());
				}

			});
		}

		if (phoneButton.isClickable()) {
			phoneButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					callNumber(account.getPhoneOffice());
				}

			});
		}
	}

	protected void setTasks() {
		getAccountContactsTask = new Runnable() {
			public void run() {

				List<ContactBean> contacts = accountContacts;

				int size = contacts.size();
				if (size > 0 && contacts.get(size - 1).equals(moreResults)) {
					size -= 1;
				}

				GetAccountContactsTask task = new GetAccountContactsTask(
						AccountDetailsActivity.this, account.getId(), size);

				loadingText.setVisibility(View.VISIBLE);
				hideEmpty();
				try {
					threadManager.submitTask(task);
				} catch (RejectedExecutionException e) {
					showDialog(DialogValues.ERROR_CANNOT_LAUNCH_TASK);
				}
			}
		};

		getItemDetailsTask = new Runnable() {
			public void run() {

				GetContactDetailsTask task = new GetContactDetailsTask(
						AccountDetailsActivity.this, selectedItem.getId());

				// Let user know we're doing something
				loadingText.setVisibility(View.VISIBLE);

				try {
					threadManager.submitTask(task);
				} catch (RejectedExecutionException e) {
					if (!AccountDetailsActivity.this.isFinishing()) {
						showDialog(DialogValues.ERROR_CANNOT_LAUNCH_TASK);
					}
				}

			}
		};
	}

	public void callNumber(String number) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
				+ number));
		startActivity(intent);
	}

	public void showAddress(String address) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="
				+ address));
		startActivity(intent);
	}

	public void updateContactList(final List<ContactBean> contacts) {
		runOnUiThread(new Runnable() {
			public void run() {

				List<ContactBean> allContacts = accountContacts;

				if (allContacts.contains(moreResults)) {
					allContacts.remove(moreResults);
				}

				allContacts.addAll(contacts);

				loadingText.setVisibility(View.INVISIBLE);
				showEmpty();
				itemAdapter.notifyDataSetChanged();

				if (contacts.size() == GeneralSettings
						.getAccountMaxResults(AccountDetailsActivity.this)) {
					allContacts.add(moreResults);
				}
			}
		});
	}

	@Override
	public void forwardItemDetailsActivity(final ContactBean contact) {
		runOnUiThread(new Runnable() {
			public void run() {
				Log.d(TAG, "forwarding to item details activity");
				Intent intent = new Intent(AccountDetailsActivity.this,
						ContactDetailsActivity.class);
				intent.putExtra(CommonActivity.ITEM_IDENTIFIER, contact);
				loadingText.setVisibility(View.INVISIBLE);
				startActivity(intent);
			}
		});
	}

}
