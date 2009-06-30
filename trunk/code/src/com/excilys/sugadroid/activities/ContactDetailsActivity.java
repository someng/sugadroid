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

import info.piwai.yasdic.YasdicContainer;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.excilys.sugadroid.R;
import com.excilys.sugadroid.activities.delegates.DialogManager.DialogValues;
import com.excilys.sugadroid.activities.interfaces.ICallingGetItemDetailsActivity;
import com.excilys.sugadroid.beans.AccountBean;
import com.excilys.sugadroid.beans.ContactBean;
import com.excilys.sugadroid.di.BeanContainerHolder.LogBeanDef;
import com.excilys.sugadroid.services.interfaces.IAccountServices;
import com.excilys.sugadroid.tasks.GetAccountDetailsTask;

public class ContactDetailsActivity extends CommonActivity implements ICallingGetItemDetailsActivity<AccountBean> {

	private static final String	TAG		= ContactDetailsActivity.class.getSimpleName();
	public static final String	CONTACT	= "contact";

	private ContactBean			contact;

	private Button				accountButton;
	private Button				phoneMobileButton;
	private Button				phoneWorkButton;
	private Button				emailButton;
	private Button				addButton;
	private TextView			nameText;

	private Runnable			getItemDetailsTask;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.contact_details);

		contact = (ContactBean) getIntent().getSerializableExtra(CommonActivity.ITEM_IDENTIFIER);

		findViews();
		setContents();
		setListeners();
		setTasks();

	}

	private void findViews() {
		nameText = (TextView) findViewById(R.id.contact_name_text);
		accountButton = (Button) findViewById(R.id.contact_account_button);
		phoneMobileButton = (Button) findViewById(R.id.contact_phone_mobile_button);
		phoneWorkButton = (Button) findViewById(R.id.contact_phone_work_button);
		emailButton = (Button) findViewById(R.id.contact_email_button);
		addButton = (Button) findViewById(R.id.contact_add_button);
	}

	private void setContents() {
		nameText.setText(contact.toString());
		hideLoadingText();

		if (contact.getAccountName() != null && !contact.getAccountName().equals("")) {
			accountButton.setText(contact.getAccountName());
		} else {
			accountButton.setText(R.string.not_available);
			accountButton.setClickable(false);
		}

		if (contact.getPhoneMobile() != null && !contact.getPhoneMobile().equals("")) {
			phoneMobileButton.setText(contact.getPhoneMobile());
		} else {
			phoneMobileButton.setText(R.string.not_available);
			phoneMobileButton.setClickable(false);
		}

		if (contact.getPhoneWork() != null && !contact.getPhoneWork().equals("")) {
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
					executeOnGuiThreadAuthenticatedTask(getItemDetailsTask);
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

		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addContact(contact);
			}
		});

	}

	private void setTasks() {

		getItemDetailsTask = new Runnable() {
			public void run() {

				submitRejectableTask((GetAccountDetailsTask) container.getBean("getAccountDetailsTask"));

			}

		};

		container.define("getAccountDetailsTask", false, new LogBeanDef<GetAccountDetailsTask>() {

			@Override
			protected GetAccountDetailsTask newBean(YasdicContainer arg0) {
				return new GetAccountDetailsTask(ContactDetailsActivity.this, (IAccountServices) container.getBean("accountServices"),
						contact.getAccountId());
			}

		});
	}

	public void callNumber(String number) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
		startActivity(intent);
	}

	public void sendMail(String emailAddress) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + emailAddress));
		startActivity(intent);
	}

	@Override
	public void onItemDetailsLoaded(final AccountBean account) {
		runOnUiThread(new Runnable() {
			public void run() {
				Log.d(TAG, "forwarding to item details activity");
				Intent intent = new Intent(ContactDetailsActivity.this, AccountDetailsActivity.class);
				intent.putExtra(CommonActivity.ITEM_IDENTIFIER, account);
				startActivity(intent);
			}
		});
	}

	// TODO : clean this demo code.
	private void addContact(ContactBean contact) {

		ContentValues personValues = new ContentValues();
		personValues.put(Contacts.People.NAME, contact.toString());
		/* STARRED 0 = Contacts, 1 = Favorites */
		personValues.put(Contacts.People.STARRED, 0);

		// worked in SDK 1.0 R2 but not in SDK 1.1 R1 anymore
		// Uri newPersonUri = getContentResolver()
		// .insert(Contacts.People.CONTENT_URI, personValues);

		Uri newPersonUri = Contacts.People.createPersonInMyContactsGroup(getContentResolver(), personValues);

		if (newPersonUri != null) {

			// add account
			if (contact.getAccountName() != null && !contact.getAccountName().equals("")) {
				ContentValues organisationValues = new ContentValues();
				Uri orgUri = Uri.withAppendedPath(newPersonUri, Contacts.Organizations.CONTENT_DIRECTORY);
				organisationValues.put(Contacts.Organizations.COMPANY, contact.getAccountName());
				organisationValues.put(Contacts.Organizations.TYPE, Contacts.Organizations.TYPE_WORK);
				Uri orgUpdate = getContentResolver().insert(orgUri, organisationValues);
				if (orgUpdate == null) {
					Log.e(TAG, "Could not insert contact's account name");
				}
			}

			if (contact.getPhoneMobile() != null && !contact.getPhoneMobile().equals("")) {
				// add mobile phone number
				ContentValues mobileValues = new ContentValues();
				Uri mobileUri = Uri.withAppendedPath(newPersonUri, Contacts.People.Phones.CONTENT_DIRECTORY);
				mobileValues.put(Contacts.Phones.NUMBER, contact.getPhoneMobile());
				mobileValues.put(Contacts.Phones.TYPE, Contacts.Phones.TYPE_MOBILE);
				Uri phoneUpdate = getContentResolver().insert(mobileUri, mobileValues);
				if (phoneUpdate == null) {
					Log.e(TAG, "Could not insert contact's mobile phone number");
				}
			}

			if (contact.getPhoneWork() != null && !contact.getPhoneWork().equals("")) {
				// add work phone number
				ContentValues workValues = new ContentValues();
				Uri faxUri = Uri.withAppendedPath(newPersonUri, Contacts.People.Phones.CONTENT_DIRECTORY);
				workValues.put(Contacts.Phones.NUMBER, contact.getPhoneWork());
				workValues.put(Contacts.Phones.TYPE, Contacts.Phones.TYPE_WORK);
				Uri phoneUpdate = getContentResolver().insert(faxUri, workValues);
				if (phoneUpdate == null) {
					Log.e(TAG, "Could not insert contact's work phone number");
				}
			}

			if (contact.getEmail1() != null && !contact.getEmail1().equals("")) {
				// add email
				ContentValues emailValues = new ContentValues();
				Uri emailUri = Uri.withAppendedPath(newPersonUri, Contacts.People.ContactMethods.CONTENT_DIRECTORY);
				emailValues.put(Contacts.ContactMethods.KIND, Contacts.KIND_EMAIL);
				emailValues.put(Contacts.ContactMethods.TYPE, Contacts.ContactMethods.TYPE_HOME);
				emailValues.put(Contacts.ContactMethods.DATA, contact.getEmail1());
				Uri emailUpdate = getContentResolver().insert(emailUri, emailValues);
				if (emailUpdate == null) {
					Log.e(TAG, "Could not insert contact's email");
				}
			}

			showDialog(DialogValues.CONTACT_ADDED);

		}
	}
}
