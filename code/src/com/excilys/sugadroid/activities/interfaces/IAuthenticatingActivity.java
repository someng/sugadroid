package com.excilys.sugadroid.activities.interfaces;

/**
 * An activity that provides callbacks to show informations after authenticating
 * 
 * @author Pierre-Yves Ricau
 * 
 */
public interface IAuthenticatingActivity {

	public abstract void onLoginSuccessful();

	public abstract void onLoginFailedNoNetwork();

	public abstract void onLoginFailedBadCredentials();

	public abstract void onLoginFailed(String message);

}