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

import com.excilys.sugadroid.R;
import com.excilys.sugadroid.activities.interfaces.ICallingGetItemDetailsActivity;
import com.excilys.sugadroid.beans.ContactBean;
import com.excilys.sugadroid.di.BeanHolder;
import com.excilys.sugadroid.services.interfaces.IContactServices;
import com.excilys.sugadroid.tasks.GetContactDetailsTask;
import com.excilys.sugadroid.tasks.SearchContactsTask;

public class ContactListActivity
		extends
		CommonSearchListActivity<ContactBean, GetContactDetailsTask, SearchContactsTask>
		implements ICallingGetItemDetailsActivity<ContactBean> {

	@Override
	public Class<ContactDetailsActivity> getItemDetailsActivity() {
		return ContactDetailsActivity.class;
	}

	@Override
	public GetContactDetailsTask getItemDetailsTaskInstance(
			ContactBean selectedItem) {
		IContactServices contactServices = BeanHolder.getInstance()
				.getContactServices();
		return new GetContactDetailsTask(this, contactServices, selectedItem
				.getId());
	}

	@Override
	public SearchContactsTask getSearchItemTaskInstance(String search) {
		IContactServices contactServices = BeanHolder.getInstance()
				.getContactServices();
		return new SearchContactsTask(this, contactServices, search,
				GeneralSettings.getSearchListMaxResults(this));
	}

	@Override
	public void initMoreResultsBean() {
		moreResults = new ContactBean();
		moreResults.setFirstName(getString(R.string.item_search_more_result));
	}
}
