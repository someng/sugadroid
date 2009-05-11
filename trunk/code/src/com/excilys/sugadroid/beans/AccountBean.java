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

package com.excilys.sugadroid.beans;

import java.io.Serializable;
import java.util.List;

/**
 * A bean representing an account, as given by SugarCRM
 * 
 * @author Pierre-Yves Ricau
 * 
 */
public class AccountBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String shippingAddressStreet;
	private String shippingAddressCity;
	private String shippingAddressState;
	private String shippingAddressPostalcode;
	private String shippingAddressCountry;
	private String billingAddressStreet;
	private String billingAddressCity;
	private String billingAddressState;
	private String billingAddressPostalcode;
	private String billingAddressCountry;
	private String phone_office;

	public String getShippingAddressStreet() {
		return shippingAddressStreet;
	}

	public void setShippingAddressStreet(String shippingAddressStreet) {
		this.shippingAddressStreet = shippingAddressStreet;
	}

	public String getShippingAddressCity() {
		return shippingAddressCity;
	}

	public void setShippingAddressCity(String shippingAddressCity) {
		this.shippingAddressCity = shippingAddressCity;
	}

	public String getShippingAddressState() {
		return shippingAddressState;
	}

	public void setShippingAddressState(String shippingAddressState) {
		this.shippingAddressState = shippingAddressState;
	}

	public String getShippingAddressPostalcode() {
		return shippingAddressPostalcode;
	}

	public void setShippingAddressPostalcode(String shippingAddressPostalcode) {
		this.shippingAddressPostalcode = shippingAddressPostalcode;
	}

	public String getShippingAddressCountry() {
		return shippingAddressCountry;
	}

	public void setShippingAddressCountry(String shippingAddressCountry) {
		this.shippingAddressCountry = shippingAddressCountry;
	}

	List<ContactBean> contacts;

	public List<ContactBean> getContacts() {
		return contacts;
	}

	public void setContacts(List<ContactBean> contacts) {
		this.contacts = contacts;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneOffice() {
		return phone_office;
	}

	public void setPhoneOffice(String phone_office) {
		this.phone_office = phone_office;
	}

	/**
	 * Constructs the shipping address, based on the informations available
	 * 
	 * @return
	 */
	public String getShippingFullAddress() {

		StringBuilder sb = new StringBuilder("");

		boolean first = true;
		if (shippingAddressStreet != null && !shippingAddressStreet.equals("")) {
			first = false;
			sb.append(shippingAddressStreet);
		}

		if (shippingAddressCity != null && !shippingAddressCity.equals("")) {
			if (first == true) {
				first = false;
			} else {
				sb.append(", ");
			}
			sb.append(shippingAddressCity);
		}

		if (shippingAddressState != null && !shippingAddressState.equals("")) {
			if (first == true) {
				first = false;
			} else {
				sb.append(", ");
			}
			sb.append(shippingAddressCity);
		}

		if (shippingAddressPostalcode != null
				&& !shippingAddressPostalcode.equals("")) {
			if (first == true) {
				first = false;
			} else {
				sb.append(", ");
			}
			sb.append(shippingAddressPostalcode);
		}

		if (shippingAddressCountry != null
				&& !shippingAddressCountry.equals("")) {
			if (first == true) {
				first = false;
			} else {
				sb.append(", ");
			}
			sb.append(shippingAddressCountry);
		}

		return sb.toString();
	}

	public String getBillingFullAddress() {

		StringBuilder sb = new StringBuilder("");

		boolean first = true;
		if (billingAddressStreet != null && !billingAddressStreet.equals("")) {
			first = false;
			sb.append(billingAddressStreet);
		}

		if (billingAddressCity != null && !billingAddressCity.equals("")) {
			if (first == true) {
				first = false;
			} else {
				sb.append(", ");
			}
			sb.append(billingAddressCity);
		}

		if (billingAddressState != null && !billingAddressState.equals("")) {
			if (first == true) {
				first = false;
			} else {
				sb.append(", ");
			}
			sb.append(billingAddressCity);
		}

		if (billingAddressPostalcode != null
				&& !billingAddressPostalcode.equals("")) {
			if (first == true) {
				first = false;
			} else {
				sb.append(", ");
			}
			sb.append(billingAddressPostalcode);
		}

		if (billingAddressCountry != null && !billingAddressCountry.equals("")) {
			if (first == true) {
				first = false;
			} else {
				sb.append(", ");
			}
			sb.append(billingAddressCountry);
		}

		return sb.toString();
	}

	@Override
	public String toString() {
		if (name == null) {
			return "";
		} else {
			return name;
		}
	}

	public String getBillingAddressStreet() {
		return billingAddressStreet;
	}

	public void setBillingAddressStreet(String billingAddressStreet) {
		this.billingAddressStreet = billingAddressStreet;
	}

	public String getBillingAddressCity() {
		return billingAddressCity;
	}

	public void setBillingAddressCity(String billingAddressCity) {
		this.billingAddressCity = billingAddressCity;
	}

	public String getBillingAddressState() {
		return billingAddressState;
	}

	public void setBillingAddressState(String billingAddressState) {
		this.billingAddressState = billingAddressState;
	}

	public String getBillingAddressPostalcode() {
		return billingAddressPostalcode;
	}

	public void setBillingAddressPostalcode(String billingAddressPostalcode) {
		this.billingAddressPostalcode = billingAddressPostalcode;
	}

	public String getBillingAddressCountry() {
		return billingAddressCountry;
	}

	public void setBillingAddressCountry(String billingAddressCountry) {
		this.billingAddressCountry = billingAddressCountry;
	}

}
