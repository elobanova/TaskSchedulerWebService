package com.elobanova.websiteanalyzer.model;

/**
 * A class representing a task analyzing an HTML document with a url, status
 * information and extracted document details.
 * 
 * @author Ekaterina Lobanova
 */
public class AnalysisTask {
	private String url;
	private StatusEnum status;
	private DocumentInfo documentInfo;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public DocumentInfo getDocumentInfo() {
		return documentInfo;
	}

	public void setDocumentInfo(DocumentInfo documentInfo) {
		this.documentInfo = documentInfo;
	}

}
