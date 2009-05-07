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
import com.excilys.sugadroid.beans.AccountBean;
import com.excilys.sugadroid.services.exceptions.ServiceException;
import com.excilys.sugadroid.services.interfaces.IAccountServices;

public class SearchAccountsTask extends AuthenticatedTask<AccountListActivity> {

	private String search;
	private IAccountServices accountServices;
	private int maxResults;

	public SearchAccountsTask(AccountListActivity activity,
			IAccountServices accountServices, String search, int maxResults) {
		super(activity);
		this.search = search;
		this.accountServices = accountServices;
		this.maxResults = maxResults;
	}

	@Override
	public void doRunAuthenticatedTask() throws ServiceException {
		List<AccountBean> accounts = accountServices.searchAccounts(search,
				activity.getOffset(), maxResults);

		activity.updateItemList(accounts);
	}

}
