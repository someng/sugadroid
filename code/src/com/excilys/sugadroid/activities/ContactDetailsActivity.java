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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.excilys.sugadroid.R;
import com.excilys.sugadroid.activities.delegates.DialogManager;
import com.excilys.sugadroid.activities.interfaces.CallingGetItemDetailsActivity;
import com.excilys.sugadroid.beans.AccountBean;
import com.excilys.sugadroid.beans.ContactBean;
import com.excilys.sugadroid.tasks.GetAccountDetailsTask;

public class ContactDetailsActivity extends CommonActivity implements
		CallingGetItemDetailsActivity<AccountBean> {

	private static final String TAG = ContactDetailsActivity.class
			.getSimpleName();
	public static final String CONTACT = "contact";

	private ContactBean contact;

	private Button accountButton;
	private Button phoneMobileButton;
	private Button phoneWorkButton;
	private Button emailButton;
	private TextView nameText;
	private TextView contactInfoText;

	private Runnable getItemDetailsTask;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.contact_details);

		contact = (ContactBean) getIntent().getSerializableExtra(
				CommonActivity.ITEM_IDENTIFIER);

		findViews();
		setContents();
		setListeners();
		setTasks();

	}

	private void findViews() {
		nameText = (TextView) findViewById(R.id.contact_name_text);
		contactInfoText = (TextView) findViewById(R.id.contact_info_text);
		accountButton = (Button) findViewById(R.id.contact_account_button);
		phoneMobileButton = (Button) findViewById(R.id.contact_phone_mobile_button);
		phoneWorkButton = (Button) findViewById(R.id.contact_phone_work_button);
		emailButton = (Button) findViewById(R.id.contact_email_button);
	}

	private void setContents() {
		nameText.setText(contact.toString());
		contactInfoText.setText("");

		if (contact.getAccountName() != null
				&& !contact.getAccountName().equals("")) {
			accountButton.setText(contact.getAccountName());
		} else {
			accountButton.setText(R.string.not_available);
			accountButton.setClickable(false);
		}

		if (contact.getPhoneMobile() != null
				&& !contact.getPhoneMobile().equals("")) {
			phoneMobileButton.setText(contact.getPhoneMobile());
		} else {
			phoneMobileButton.setText(R.string.not_available);
			phoneMobileButton.setClickable(false);
		}

		if (contact.getPhoneWork() != null
				&& !contact.getPhoneWork().equals("")) {
			phoneWorkButton.setText(contact.getPhoneWork());
		} else {
			phoneWorkButton.setText(R.string.not_available);
			phoneWorkButton.setClickable(false);
		}

		if (contact.getEmail1() != null && !contact.getEmail1().equals("")) {
			emailButton.setText(contact.getEmail1());
		} else {
			emailButton.setText(R.string.not_available);
			emailButton.setClickable(false);
		}
	}

	private void setListeners() {

		if (phoneMobileButton.isClickable()) {
			phoneMobileButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					callNumber(contact.getPhoneMobile());
				}
			});
		}

		if (phoneWorkButton.isClickable()) {
			phoneWorkButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					callNumber(contact.getPhoneWork());
				}
			});
		}

		if (accountButton.isClickable()) {
			accountButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					threadManager.queueUpdate(0, getItemDetailsTask);
				}
			});
		}

		if (emailButton.isClickable()) {
			emailButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					sendMail(contact.getEmail1());
				}
			});
		}

	}

	private void setTasks() {
		getItemDetailsTask = new Runnable() {
			public void run() {
				GetAccountDetailsTask task = new GetAccountDetailsTask(
						ContactDetailsActivity.this, contact.getAccountId());

				// Let user know we're doing something
				contactInfoText.setText(R.string.loading_text);
				try {
					threadManager.submitTask(task);
				} catch (RejectedExecutionException e) {
					if (!ContactDetailsActivity.this.isFinishing()) {
						showDialog(DialogManager.DIALOG_ERROR_CANNOT_LAUNCH_TASK);
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

	public void sendMail(String emailAddress) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:"
				+ emailAddress));
		startActivity(intent);
	}

	@Override
	public void forwardItemDetailsActivity(final AccountBean account) {
		runOnUiThread(new Runnable() {
			public void run() {
				Log.d(TAG, "forwarding to item details activity");
				Intent intent = new Intent(ContactDetailsActivity.this,
						AccountDetailsActivity.class);
				intent.putExtra(CommonActivity.ITEM_IDENTIFIER, account);
				contactInfoText.setText("");
				startActivity(intent);
			}
		});
	}

}
