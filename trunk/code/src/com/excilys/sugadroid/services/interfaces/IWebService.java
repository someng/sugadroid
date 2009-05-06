package com.excilys.sugadroid.services.interfaces;

/**
 * Interface for Web Service clients : define entryPoint getters and setters
 * 
 * @author Pierre-Yves Ricau
 * 
 */
public interface IWebService {

	public String getEntryPoint();

	public void setEntryPoint(String entryPoint);

	public String getNamespace();

	public void setNamespace(String namespace);

}
