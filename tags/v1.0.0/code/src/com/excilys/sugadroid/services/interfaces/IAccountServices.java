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

package com.excilys.sugadroid.services.interfaces;

import java.util.List;

import com.excilys.sugadroid.beans.AccountBean;
import com.excilys.sugadroid.beans.SessionBean;
import com.excilys.sugadroid.services.exceptions.ServiceException;
import com.excilys.sugadroid.services.impl.ksoap2.AccountServicesKsoap2Impl;

/**
 * This class represents the services to access the accounts
 * 
 * @author Pierre-Yves Ricau
 * 
 */
public interface IAccountServices {

	/**
	 * This method return a list of accounts that match the search string
	 * 
	 * @param session
	 *            the session bean instance
	 * @param search
	 *            name of the account to search. Search is like %search%
	 * @param offset
	 *            the offset for the first result
	 * @param maxResults
	 *            maximum number of results
	 * @return a list of account beans, the response from the service
	 * @throws ServiceException
	 */
	public List<AccountBean> searchAccounts(SessionBean session, String search,
			Integer offset, Integer maxResults) throws ServiceException;

	/**
	 * This method return the account bean that matches the id
	 * 
	 * @see AccountServicesKsoap2Impl
	 * @param session
	 *            the session bean instance
	 * @param accountId
	 *            the id of the account to retrieve
	 * @return the account bean
	 * @throws ServiceException
	 */
	public AccountBean getAccountDetails(SessionBean session, String accountId)
			throws ServiceException;
}
