package com.elobanova.websiteanalyzer.service;

/**
 * A listener for handling responses and error events from the tasks *
 */
public interface OnResponseListener<T> {

	/**
	 * Is being triggered when the task has been executed.
	 *
	 * @param response
	 *            a returned item of type T
	 */
	void onResponse(T response);

	/**
	 * Is being triggered when the error occurs during the task execution.
	 *
	 * @param errorMessage
	 *            a message to display to the user
	 */
	void onError(String errorMessage);
}
