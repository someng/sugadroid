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
 * Exception thrown when the session token is not valid anymore, and the user
 * needs to login again
 * 
 * @author Pierre-Yves Ricau
 * 
 */
public class InvalidSessionException extends ServiceException {

	private static final long serialVersionUID = 1L;

	private String sessionId;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public InvalidSessionException() {

	}

	public InvalidSessionException(String detailMessage) {
		super(detailMessage);

	}

	public InvalidSessionException(Throwable throwable) {
		super(throwable);

	}

	public InvalidSessionException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);

	}

	public InvalidSessionException(String error, String description) {
		super(error, description);

	}

	public InvalidSessionException(String error, String description,
			String errorNumber) {
		super(error, description, errorNumber);

	}

	@Override
	public String getDescription() {
		if (sessionId != null) {
			return super.getDescription() + " | Session id: " + sessionId;
		} else {
			return super.getDescription();
		}
	}

}
