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

import org.joda.time.LocalDate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemClickListener;

import com.excilys.sugadroid.R;
import com.excilys.sugadroid.activities.delegates.DialogManager.DialogValues;
import com.excilys.sugadroid.activities.interfaces.CallingGetItemDetailsActivity;
import com.excilys.sugadroid.beans.interfaces.IAppointmentBean;
import com.excilys.sugadroid.tasks.GetAppointmentDetailsTask;
import com.excilys.sugadroid.util.EagerLoadingCalendar;
import com.excilys.sugadroid.util.exceptions.DayNotLoadedException;

public class AppointmentsActivity extends CommonActivity implements
		CallingGetItemDetailsActivity<IAppointmentBean> {

	private static final String TAG = AppointmentsActivity.class
			.getSimpleName();

	public static final String CALENDAR = "calendar";

	private ViewFlipper pageFlipper;

	private List<View> views;

	private EagerLoadingCalendar calendar;

	private LocalDate currentDay;

	private Button previousDayButton;
	private Button nextDayButton;

	private TextView currentDayText;

	private IAppointmentBean selectedItem;

	private Runnable getItemDetailsTask;

	private TextView loadingText;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.appointments);

		findViews();
		setListeners();
		setTasks();

		calendar = (EagerLoadingCalendar) getIntent().getSerializableExtra(
				CALENDAR);

		currentDay = new LocalDate();

		pageFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.slide_in_left));
		pageFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.slide_out_right));

		views = new ArrayList<View>();

		for (int i = 0; i < 3; i++) {
			views.add(View.inflate(this, R.layout.appointments_page, null));
		}

		for (View view : views) {
			pageFlipper.addView(view);
			initFlippingView(view);
		}

		setDayTextView(currentDay);

		fillFlippingView(pageFlipper.getCurrentView(), currentDay);
	}

	private void initFlippingView(View view) {
		ListView listView = (ListView) view
				.findViewById(R.id.appointments_list);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				selectedItem = (IAppointmentBean) arg0.getAdapter().getItem(
						position);
				Log.d(TAG, "Item clicked: " + selectedItem.getName());
				threadManager.queueUpdate(500, getItemDetailsTask);
			}
		});

	}

	private void fillFlippingView(View view, LocalDate day) {
		ListView listView = (ListView) view
				.findViewById(R.id.appointments_list);

		ArrayAdapter<IAppointmentBean> adapter;
		try {
			adapter = new ArrayAdapter<IAppointmentBean>(this,
					android.R.layout.simple_list_item_1, calendar
							.getDayAppointments(day)) {

				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {

					if (convertView == null) {
						convertView = View.inflate(getContext(),
								R.layout.appointments_row, null);
					}

					TextView appointmentSubject = (TextView) convertView
							.findViewById(R.id.appointment_subject);

					TextView appointmentTime = (TextView) convertView
							.findViewById(R.id.appointment_time);

					IAppointmentBean bean = getItem(position);

					appointmentSubject.setText(bean.getName());

					appointmentTime.setText(bean.getTimeStart().plusHours(
							GeneralSettings.getGMT(AppointmentsActivity.this))
							.toString(getString(R.string.time_format)));

					return convertView;
				}

			};
		} catch (DayNotLoadedException e) {
			Log.e(TAG, "Day not loaded, something bad happened");
			return;
		}

		listView.setAdapter(adapter);

	}

	private void findViews() {
		pageFlipper = (ViewFlipper) findViewById(R.id.appointments_flipper);
		previousDayButton = (Button) findViewById(R.id.previous_day_button);
		nextDayButton = (Button) findViewById(R.id.next_day_button);
		loadingText = (TextView) findViewById(R.id.loading);
		currentDayText = (TextView) findViewById(R.id.appointments_day);

		loadingText.setVisibility(View.INVISIBLE);
	}

	private void setListeners() {
		pageFlipper.setOnTouchListener(new OnTouchListener() {

			private boolean moved = false;
			private float oldX;

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				int action = event.getAction();

				switch (action) {
				case MotionEvent.ACTION_DOWN:
					oldX = event.getX();
					moved = false;
					return true;
				case MotionEvent.ACTION_MOVE:
					moved = true;
					return true;
				case MotionEvent.ACTION_UP:
					if (!moved) {
						return true;
					}
					moved = false;
					if (oldX - event.getX() > 0) {
						Log.d(TAG, "Left !");
						moveFlipperToNext();
					} else {
						Log.d(TAG, "Right !");
						moveFlipperToPrevious();
					}
					return false;
				default:
					return true;
				}
			}

		});
		previousDayButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				moveFlipperToPrevious();
			}
		});

		nextDayButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				moveFlipperToNext();
			}
		});

	}

	private void setTasks() {
		getItemDetailsTask = new Runnable() {
			public void run() {
				GetAppointmentDetailsTask task = new GetAppointmentDetailsTask(
						AppointmentsActivity.this, selectedItem.getId());

				// Let user know we're doing something
				loadingText.setVisibility(View.VISIBLE);
				try {
					threadManager.submitTask(task);
				} catch (RejectedExecutionException e) {
					if (!AppointmentsActivity.this.isFinishing()) {
						showDialog(DialogValues.ERROR_CANNOT_LAUNCH_TASK);
					}
				}
			}
		};
	}

	private void moveFlipperToPrevious() {

		if (!calendar.isDayLoaded(currentDay.minusDays(1))) {
			showDialog(DialogValues.ERROR_DAY_NOT_LOADED);
			return;
		}

		View currentView = pageFlipper.getCurrentView();
		int currentIndex = views.indexOf(currentView);

		int previousIndex = currentIndex - 1;
		if (previousIndex < 0) {
			previousIndex += 3;
		}

		View previousView = views.get(previousIndex);

		currentDay = currentDay.minusDays(1);

		fillFlippingView(previousView, currentDay);

		setDayTextView(currentDay);

		pageFlipper.showPrevious();

	}

	private void moveFlipperToNext() {

		if (!calendar.isDayLoaded(currentDay.plusDays(1))) {
			showDialog(DialogValues.ERROR_DAY_NOT_LOADED);
			return;
		}

		View currentView = pageFlipper.getCurrentView();
		int currentIndex = views.indexOf(currentView);

		int nextIndex = currentIndex + 1;
		nextIndex %= 3;
		View nextView = views.get(nextIndex);

		currentDay = currentDay.plusDays(1);

		fillFlippingView(nextView, currentDay);

		setDayTextView(currentDay);

		pageFlipper.showNext();

	}

	@Override
	public void forwardItemDetailsActivity(final IAppointmentBean appointment) {
		runOnUiThread(new Runnable() {
			public void run() {
				Log.d(TAG, "forwarding to item details activity");
				Intent intent = new Intent(AppointmentsActivity.this,
						AppointmentDetailsActivity.class);
				intent.putExtra(CommonActivity.ITEM_IDENTIFIER, appointment);
				loadingText.setVisibility(View.INVISIBLE);
				startActivity(intent);
			}
		});
	}

	private void setDayTextView(LocalDate day) {
		String dayString;
		if (day.equals(new LocalDate())) {
			dayString = getString(R.string.today) + " "
					+ day.toString(getString(R.string.day_date_format));
		} else {
			dayString = day.toString(getString(R.string.day_date_format));
		}

		currentDayText.setText(dayString);
	}

}
