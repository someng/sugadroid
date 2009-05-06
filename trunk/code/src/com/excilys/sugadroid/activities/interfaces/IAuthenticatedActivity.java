package com.excilys.sugadroid.activities.interfaces;

/**
 * An activity that provides callbacks to show informations when authenticated
 * tasks perform
 * 
 * @author Pierre-Yves Ricau
 * 
 */
public interface IAuthenticatedActivity {

	public abstract void onNotLoggedIn();

	public abstract void onSessionInvalid();

	public abstract void onServiceCallFailed(String message);

	public abstract void onServiceCallFailedNoNetwork();

}
