package com.elobanova.websiteanalyzer.service;

import java.io.IOException;

import com.elobanova.websiteanalyzer.model.AnalysisTask;
import com.elobanova.websiteanalyzer.parser.JsoupParser;

public class AnalysisTaskThread implements Runnable {
	private final AnalysisTask analysisTask;
	private OnResponseListener<AnalysisTask> onResponseListener;

	public AnalysisTaskThread(AnalysisTask analysisTask) {
		this.analysisTask = analysisTask;
	}

	public void setOnResponseListener(OnResponseListener<AnalysisTask> onResponseListener) {
		this.onResponseListener = onResponseListener;
	}

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
