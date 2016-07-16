package com.elobanova.websiteanalyzer.model;

/**
 * An enum with the status of a task. A task can be either under processing
 * (PROCESSING), done with the job (DONE) or results in an error (ERROR).
 * 
 * @author Ekaterina Lobanova
 */
public enum StatusEnum {
	PROCESSING("progress"), ERROR("error"), DONE("done");

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
