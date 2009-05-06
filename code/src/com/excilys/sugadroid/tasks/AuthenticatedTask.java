package com.excilys.sugadroid.tasks;

import android.util.Log;

import com.excilys.sugadroid.activities.interfaces.IAuthenticatedActivity;
import com.excilys.sugadroid.services.exceptions.InvalidResponseException;
import com.excilys.sugadroid.services.exceptions.InvalidSessionException;
import com.excilys.sugadroid.services.exceptions.NotLoggedInException;
import com.excilys.sugadroid.services.exceptions.ServiceException;

/**
 * An abstract Runnable task that makes calls to an authenticated Web Services,
 * delegating the call in the method doRun(). It's main purpose is to catch
 * runtime exceptions linked authentication, authorization and network issues.
 * 
 * @author Pierre-Yves Ricau
 * 
 */
public abstract class AuthenticatedTask<T extends IAuthenticatedActivity>
		implements Runnable {

	private static final String TAG = AuthenticatedTask.class.getSimpleName();

	T activity;

	public AuthenticatedTask(T activity) {
		this.activity = activity;
	}

	@Override
	public void run() {
		try {
			doRun();
		} catch (InvalidSessionException e) {
			Log.e(TAG, Log.getStackTraceString(e));
			activity.onSessionInvalid();
		} catch (InvalidResponseException e) {
			Log.e(TAG, Log.getStackTraceString(e));
			activity.onServiceCallFailedNoNetwork();
		} catch (NotLoggedInException e) {
			Log.e(TAG, Log.getStackTraceString(e));
			activity.onNotLoggedIn();
		} catch (ServiceException e) {
			Log.e(TAG, Log.getStackTraceString(e));
			activity.onServiceCallFailed(e.getMessage());
		}

	}

	protected abstract void doRun() throws ServiceException;

}
