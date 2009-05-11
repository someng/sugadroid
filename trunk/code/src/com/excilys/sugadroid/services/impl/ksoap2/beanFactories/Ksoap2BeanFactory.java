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

package com.excilys.sugadroid.services.impl.ksoap2.beanFactories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.ksoap2.serialization.SoapObject;

import com.excilys.sugadroid.services.IBeanFactory;
import com.excilys.sugadroid.services.impl.ksoap2.beanFactories.exceptions.ParsingException;

/**
 * A bean factory that convert Ksoap2 Objects to specific beans, using a bean
 * factory
 * 
 * @author Pierre-Yves Ricau
 * 
 */

public class Ksoap2BeanFactory {

	private IBeanFactory beanFactory;

	public <T> T parseBean(SoapObject response, Class<T> beanClass)
			throws ParsingException {

		Map<String, String> properties = new HashMap<String, String>();

		@SuppressWarnings("unchecked")
		Vector<SoapObject> vectorItem = (Vector<SoapObject>) response
				.getProperty("name_value_list");

		for (SoapObject item : vectorItem) {
			String field = getCamelCase((String) item.getProperty("name"));
			String value = (String) item.getProperty("value");
			properties.put(field, value);
		}

		return beanFactory.parseBean(properties, beanClass);
	}

	public <T> List<T> parseBeanList(Vector<SoapObject> response,
			Class<T> beanClass) throws ParsingException {

		List<T> beanList = new ArrayList<T>();

		for (SoapObject item : response) {
			beanList.add(parseBean(item, beanClass));
		}
		return beanList;
	}

	public String getCamelCase(String dbCase) {

		StringBuilder result = new StringBuilder();

		int length = dbCase.length();
		boolean upperNext = true;
		for (int i = 0; i < length; i++) {
			Character c = dbCase.charAt(i);
			if (c.equals('_')) {
				upperNext = true;
			} else {
				if (upperNext == true) {
					c = Character.toUpperCase(c);
				}
				upperNext = false;
				result.append(c);
			}
		}

		return result.toString();
	}

	public IBeanFactory getBeanFactory() {
		return beanFactory;
	}

	public void setBeanFactory(IBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

}
