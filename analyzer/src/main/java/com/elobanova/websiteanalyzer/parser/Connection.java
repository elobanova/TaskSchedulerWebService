package com.elobanova.websiteanalyzer.parser;

/**
 * A connection details class.
 * 
 * @author Ekaterina Lobanova
 */
public class Connection {
	private boolean isValid;
	private int statusCode;

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
}
