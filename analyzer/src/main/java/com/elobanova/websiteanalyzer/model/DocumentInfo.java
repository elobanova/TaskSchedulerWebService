package com.elobanova.websiteanalyzer.model;

import java.util.List;

/**
 * A class of the model to provide details of an HTML document.
 * 
 * @author Ekaterina Lobanova
 */
public class DocumentInfo {
	private final String title;
	private final boolean isLoginFormPresent;
	private final String htmlVersion;
	private final int internalLinksNumber;
	private final int externalLinksNumber;
	private final int inaccessibleLinksNumber;
	private final List<HeadingInfo> headings;

	private DocumentInfo(DocumentInfoBuilder builder) {
		this.title = builder.title;
		this.isLoginFormPresent = builder.isLoginFormPresent;
		this.htmlVersion = builder.htmlVersion;
		this.internalLinksNumber = builder.internalLinksNumber;
		this.externalLinksNumber = builder.externalLinksNumber;
		this.inaccessibleLinksNumber = builder.inaccessibleLinksNumber;
		this.headings = builder.headings;
	}

	public boolean isLoginFormPresent() {
		return isLoginFormPresent;
	}

	public String getTitle() {
		return title;
	}

	public String getHtmlVersion() {
		return htmlVersion;
	}

	public int getInternalLinksNumber() {
		return internalLinksNumber;
	}

	public int getExternalLinksNumber() {
		return externalLinksNumber;
	}

	public int getInaccessibleLinksNumber() {
		return inaccessibleLinksNumber;
	}

	public List<HeadingInfo> getHeadings() {
		return headings;
	}

	/**
	 * A builder class for DocumentInfo to avoid unreadable setter calls.
	 * 
	 * @author Ekaterina Lobanova
	 */
	public static class DocumentInfoBuilder {
		private String title;
		private boolean isLoginFormPresent;
		private String htmlVersion;
		private int internalLinksNumber;
		private int externalLinksNumber;
		private int inaccessibleLinksNumber;
		private List<HeadingInfo> headings;

		public DocumentInfoBuilder setTitle(String title) {
			this.title = title;
			return this;
		}

		public DocumentInfoBuilder setLoginFormPresent(boolean isLoginFormPresent) {
			this.isLoginFormPresent = isLoginFormPresent;
			return this;
		}

		public DocumentInfoBuilder setHTMLVersion(String htmlVersion) {
			this.htmlVersion = htmlVersion;
			return this;
		}

		public DocumentInfoBuilder setNumberOfInternalLinks(int internalLinksNumber) {
			this.internalLinksNumber = internalLinksNumber;
			return this;
		}

		public DocumentInfoBuilder setNumberOfExternalLinks(int externalLinksNumber) {
			this.externalLinksNumber = externalLinksNumber;
			return this;
		}

		public DocumentInfoBuilder setNumberOfNotAccessibleLinks(int inaccessibleLinksNumber) {
			this.inaccessibleLinksNumber = inaccessibleLinksNumber;
			return this;
		}

		public DocumentInfoBuilder setHeadings(List<HeadingInfo> headings) {
			this.headings = headings;
			return this;
		}

		public DocumentInfo build() {
			return new DocumentInfo(this);
		}
	}
}
