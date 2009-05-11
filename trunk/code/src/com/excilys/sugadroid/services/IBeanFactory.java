package com.excilys.sugadroid.services;

import java.util.Map;

import com.excilys.sugadroid.services.impl.ksoap2.beanFactories.exceptions.ParsingException;

public interface IBeanFactory {

	public abstract <T> T parseBean(Map<String, String> properties,
			Class<T> beanClass) throws ParsingException;

}