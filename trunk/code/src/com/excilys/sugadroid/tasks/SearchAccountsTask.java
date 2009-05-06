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

package com.excilys.sugadroid.tasks;

import java.util.List;

import com.excilys.sugadroid.activities.AccountListActivity;
import com.excilys.sugadroid.activities.GeneralSettings;
import com.excilys.sugadroid.activities.delegates.DialogManager;
import com.excilys.sugadroid.beans.AccountBean;
import com.excilys.sugadroid.beans.SessionBeanImpl;
import com.excilys.sugadroid.di.BeanHolder;
import com.excilys.sugadroid.services.exceptions.InvalidResponseException;
import com.excilys.sugadroid.services.exceptions.ServiceException;

public class SearchAccountsTask implements Runnable {

	private final String search;
	private final AccountListActivity activity;

	public SearchAccountsTask(AccountListActivity activity, String search) {
		this.search = search;
		this.activity = activity;
	}

	public void run() {
		List<AccountBean> accounts;
		try {
			accounts = BeanHolder.getInstance().getAccountServices()
					.searchAccounts(SessionBeanImpl.getInstance(), search,
							activity.getOffset(),
							GeneralSettings.getSearchListMaxResults(activity));
		} catch (InvalidResponseException e) {
			activity
					.postShowDialog(DialogManager.DIALOG_ERROR_INVALID_RESPONSE);
			return;
		} catch (ServiceException e) {
			activity.postShowCustomDialog(e.getMessage(), e.getDescription());
			return;
		}
		activity.updateItemList(accounts);
	}

}
