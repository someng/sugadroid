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

package com.excilys.sugadroid.services.impl.ksoap2;

import org.ksoap2.serialization.SoapObject;

import android.util.Log;

import com.excilys.sugadroid.services.exceptions.ServiceException;

public abstract class SugarServiceClientKsoap2Impl extends
		ServiceClientKsoap2Impl {

	private static String TAG = SugarServiceClientKsoap2Impl.class
			.getSimpleName();

	public void checkErrorValue(SoapObject error) throws ServiceException {
		String errorNumber = (String) error.getProperty("number");

		if (!errorNumber.equals("0")) {

			String errorName = (String) error.getProperty("name") + " ("
					+ errorNumber + ")";
			String description = (String) error.getProperty("description");

			Log.i(TAG, errorName);
			Log.i(TAG, description);

			throw new ServiceException(errorName, description, errorNumber);
		}

	}

}
