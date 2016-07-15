package com.elobanova.websiteanalyzer.service;

import java.io.IOException;

import com.elobanova.websiteanalyzer.model.AnalysisTask;
import com.elobanova.websiteanalyzer.parser.JsoupParser;

/**
 * A worker for performing the analysis task. The information is being collected
 * in the run method and the callback with results is triggered.
 * 
 * @author Ekaterina Lobanova
 */
public class AnalysisTaskThread implements Runnable {
	private final AnalysisTask analysisTask;
	private OnResponseListener<AnalysisTask> onResponseListener;

	public AnalysisTaskThread(AnalysisTask analysisTask) {
		this.analysisTask = analysisTask;
	}

	/**
	 * Assigns a response listener. Once the task is processed, a listener's
	 * onResponse() will be called.
	 * 
	 * @param onResponseListener
	 *            a listener to handle a response
	 */
	public void setOnResponseListener(OnResponseListener<AnalysisTask> onResponseListener) {
		this.onResponseListener = onResponseListener;
	}

	/**
	 * Collects the information for the task and calls a listener's onResponse()
	 * or onError(), if the error occurs.
	 */
	@Override
	public void run() {
		try {
			JsoupParser jsoupParser = new JsoupParser(this.analysisTask.getUrl());
			analysisTask.setDocumentInfo(jsoupParser.parseDocument());
			onResponseListener.onResponse(analysisTask);
		} catch (IOException e) {
			onResponseListener.onError(e.getMessage());
		}
	}

}
