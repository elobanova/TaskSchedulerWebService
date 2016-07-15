package com.elobanova.websiteanalyzer.model;

public class HeadingInfo {
	private final String headingName;
	private final int headingCount;

	public HeadingInfo(String headingName, int headingCount) {
		this.headingName = headingName;
		this.headingCount = headingCount;
	}

	public String getHeadingName() {
		return headingName;
	}

	public int getHeadingCount() {
		return headingCount;
	}
}
