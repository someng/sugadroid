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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.excilys.sugadroid.R;
import com.excilys.sugadroid.activities.delegates.DialogManager;
import com.excilys.sugadroid.activities.interfaces.CallingGetItemDetailsActivity;
import com.excilys.sugadroid.beans.ContactBean;
import com.excilys.sugadroid.beans.interfaces.IAppointmentBean;
import com.excilys.sugadroid.tasks.GetAppointmentContactsTask;
import com.excilys.sugadroid.tasks.GetContactDetailsTask;

public class AppointmentDetailsActivity extends CommonListActivity implements
		CallingGetItemDetailsActivity<ContactBean> {

	private static final String TAG = AppointmentDetailsActivity.class
			.getSimpleName();

	private IAppointmentBean appointment;

	private TextView loadingText;
	private TextView nameText;
	private TextView dateStartText;
	private TextView durationText;
	private TextView descriptionText;

	private List<ContactBean> appointmentContacts;

	private ArrayAdapter<ContactBean> itemAdapter;

	private Runnable getItemDetailsTask;

	private Runnable getAppointmentContactsTask;

	private ContactBean selectedItem;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.appointment_details);

		appointment = (IAppointmentBean) getIntent().getSerializableExtra(
				CommonActivity.ITEM_IDENTIFIER);

		findViews();
		setTasks();
		setAdapters();
		setListeners();

		threadManager.queueUpdate(0, getAppointmentContactsTask);

	}

	@Override
	protected void findViews() {
		super.findViews();
		nameText = (TextView) findViewById(R.id.appointment_name_text);
		loadingText = (TextView) findViewById(R.id.appointment_info_text);
		dateStartText = (TextView) findViewById(R.id.appointment_date_start);
		durationText = (TextView) findViewById(R.id.appointment_duration);
		descriptionText = (TextView) findViewById(R.id.appointment_description_text);

		String appointmentName = getString(R.string.appointment_name);
		nameText.setText(appointmentName + " " + appointment.getName());

		loadingText.setVisibility(View.INVISIBLE);

		StringBuilder sb = new StringBuilder(
				getString(R.string.appointment_date_start));
		sb.append(" ").append(
				appointment.getDayStart().toString(
						getString(R.string.day_date_format))).append(" ")
				.append(getString(R.string.appointment_time_start)).append(" ")
				.append(
						appointment.getTimeStart().plusHours(
								GeneralSettings.getGMT(this)).toString(
								getString(R.string.time_format)));

		dateStartText.setText(sb.toString());

		durationText.setText(getString(R.string.appointment_duration) + " "
				+ appointment.getDurationHours()
				+ getString(R.string.appointment_hours)
				+ appointment.getDurationMinutes().toString());

		descriptionText.setText(appointment.getDescription());

	}

	private void setAdapters() {

		appointmentContacts = new ArrayList<ContactBean>();

		itemAdapter = new ArrayAdapter<ContactBean>(this,
				android.R.layout.simple_list_item_1, appointmentContacts);

		setListAdapter(itemAdapter);
	}

	protected void setListeners() {
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				selectedItem = itemAdapter.getItem(position);
				threadManager.queueUpdate(500, getItemDetailsTask);
			}

		});
	}

	protected void setTasks() {
		getItemDetailsTask = new Runnable() {
			public void run() {
				GetContactDetailsTask task = new GetContactDetailsTask(
						AppointmentDetailsActivity.this, selectedItem.getId());

				// Let user know we're doing something
				loadingText.setVisibility(View.VISIBLE);
				try {
					threadManager.submitTask(task);
				} catch (RejectedExecutionException e) {
					loadingText.setVisibility(View.INVISIBLE);
					if (!AppointmentDetailsActivity.this.isFinishing()) {
						showDialog(DialogManager.DIALOG_ERROR_CANNOT_LAUNCH_TASK);
					}
				}
			}

		};
		getAppointmentContactsTask = new Runnable() {
			public void run() {

				GetAppointmentContactsTask task = new GetAppointmentContactsTask(
						AppointmentDetailsActivity.this, appointment.getId(), 0);

				loadingText.setVisibility(View.VISIBLE);
				hideEmpty();
				try {
					threadManager.submitTask(task);
				} catch (RejectedExecutionException e) {
					if (!AppointmentDetailsActivity.this.isFinishing()) {
						showDialog(DialogManager.DIALOG_ERROR_CANNOT_LAUNCH_TASK);
					}
				}
			}
		};
	}

	public void updateContactList(final List<ContactBean> contacts) {
		runOnUiThread(new Runnable() {
			public void run() {

				List<ContactBean> allContacts = appointmentContacts;

				allContacts.addAll(contacts);

				loadingText.setVisibility(View.INVISIBLE);
				showEmpty();
				itemAdapter.notifyDataSetChanged();

			}
		});
	}

	@Override
	public void forwardItemDetailsActivity(final ContactBean contact) {
		runOnUiThread(new Runnable() {
			public void run() {
				Log.d(TAG, "forwarding to item details activity");
				Intent intent = new Intent(AppointmentDetailsActivity.this,
						ContactDetailsActivity.class);
				intent.putExtra(CommonActivity.ITEM_IDENTIFIER, contact);
				loadingText.setVisibility(View.INVISIBLE);
				startActivity(intent);
			}
		});
	}

}
