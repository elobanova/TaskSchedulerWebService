package com.elobanova.websiteanalyzer.model;

public enum StatusEnum {
	PROCESSING("processing"), ERROR("error"), DONE("done");

	private final String name;

	private StatusEnum(String name) {
		this.name = name;
	}

	/**
	 * Returns the string representation of the status state.
	 *
	 * @return the string representation of the status state.
	 */
	public String getName() {
		return this.name;
	}
}
