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
import com.excilys.sugadroid.beans.AccountBean;
import com.excilys.sugadroid.di.BeanHolder;
import com.excilys.sugadroid.services.interfaces.IAccountServices;
import com.excilys.sugadroid.tasks.GetAccountDetailsTask;
import com.excilys.sugadroid.tasks.SearchAccountsTask;

public class AccountListActivity
		extends
		CommonSearchListActivity<AccountBean, GetAccountDetailsTask, SearchAccountsTask>
		implements ICallingGetItemDetailsActivity<AccountBean> {

	@Override
	public Class<AccountDetailsActivity> getItemDetailsActivity() {
		return AccountDetailsActivity.class;
	}

	@Override
	public GetAccountDetailsTask getItemDetailsTaskInstance(
			AccountBean selectedItem) {

		IAccountServices accountServices = BeanHolder.getInstance()
				.getAccountServices();

		return new GetAccountDetailsTask(this, accountServices, selectedItem
				.getId());
	}

	@Override
	public SearchAccountsTask getSearchItemTaskInstance(String search) {
		IAccountServices accountServices = BeanHolder.getInstance()
				.getAccountServices();
		return new SearchAccountsTask(this, accountServices, search,
				GeneralSettings.getSearchListMaxResults(this));
	}

	@Override
	public void initMoreResultsBean() {
		moreResults = new AccountBean();
		moreResults.setName(getString(R.string.item_search_more_result));
	}

}
