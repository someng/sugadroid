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

package com.excilys.sugadroid.services;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import com.excilys.sugadroid.services.impl.ksoap2.beanFactories.exceptions.ParsingException;

/**
 * This class provide generic methods that use reflection to build the beans The
 * only public method is parseBean, and use tuples of property name/value to
 * fill the beans.
 * 
 * @author Pierre-Yves Ricau
 * 
 */
public class SugarBeanFactoryImpl implements IBeanFactory {

	/**
	 * 
	 * @see com.excilys.sugadroid.services.IBeanFactory#parseBean(java.util.Map,
	 *      java.lang.Class)
	 */
	public <T> T parseBean(Map<String, String> properties, Class<T> beanClass)
			throws ParsingException {

		T bean;
		try {
			bean = beanClass.newInstance();

			Set<String> keys = properties.keySet();

			for (String key : keys) {
				Method m;

				m = getMethod("set" + key, beanClass);

				Object parameter = getSetterParameter(m, properties.get(key));

				m.invoke(bean, parameter);

			}
		} catch (Exception e) {
			throw new ParsingException(e);
		}

		return bean;
	}

	/**
	 * Returns the first occurrence of a method with the good name that have one
	 * parameter
	 * 
	 * @param name
	 * @param beanClass
	 * @return
	 */
	private Method getMethod(String name, Class<?> beanClass)
			throws NoSuchMethodException {

		Method[] methods = beanClass.getMethods();

		for (Method method : methods) {
			if (method.getName().equals(name)
					&& method.getParameterTypes().length == 1) {
				return method;
			}
		}

		throw new NoSuchMethodException("The method " + name
				+ " could not be found in the class "
				+ beanClass.getSimpleName());
	}

	// TODO: This method is quite crappy, needs some refactoring, and to be more
	// generic !
	private Object getSetterParameter(Method m, String value) throws Exception {
		Class<?>[] params = m.getParameterTypes();

		if (params.length != 1) {
			throw new Exception("The setter should have only one parameter");
		}

		Class<?> parameterClass = params[0];

		if (parameterClass.equals(String.class)) {
			return value;
		} else if (parameterClass.equals(LocalDate.class)) {
			StringTokenizer stk = new StringTokenizer(value, "-");
			int year = Integer.parseInt(stk.nextToken());
			int month = Integer.parseInt(stk.nextToken());
			int day = Integer.parseInt(stk.nextToken());
			return new LocalDate(year, month, day);
		} else if (parameterClass.equals(LocalTime.class)) {
			StringTokenizer stk = new StringTokenizer(value, ":");
			int hour = Integer.parseInt(stk.nextToken());
			int minutes = Integer.parseInt(stk.nextToken());
			return new LocalTime(hour, minutes);
		} else if (parameterClass.equals(Integer.class)) {
			return new Integer(value);
		} else if (parameterClass.equals(LocalDateTime.class)) {
			StringTokenizer stk = new StringTokenizer(value, " ");
			String date = stk.nextToken();
			String time = stk.nextToken();
			stk = new StringTokenizer(date, "-");
			int year = Integer.parseInt(stk.nextToken());
			int month = Integer.parseInt(stk.nextToken());
			int day = Integer.parseInt(stk.nextToken());
			stk = new StringTokenizer(time, ":");
			int hour = Integer.parseInt(stk.nextToken());
			int minutes = Integer.parseInt(stk.nextToken());
			return new LocalDateTime(year, month, day, hour, minutes);
		}

		throw new Exception("Unknown type: " + parameterClass.getSimpleName());
	}
}
