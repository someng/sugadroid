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

package com.excilys.sugadroid.services.exceptions;

/**
 * Exception thrown when the login failed for a specific reason
 * 
 * @author Pierre-Yves Ricau
 * 
 */
public class LoginFailedException extends ServiceException {

	private static final long serialVersionUID = 1L;

	public LoginFailedException(String detailMessage) {
		super(detailMessage);
	}

	public LoginFailedException(Throwable throwable) {
		super(throwable);
	}

	public LoginFailedException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public LoginFailedException(String error, String description) {
		super(error, description);
	}

}
