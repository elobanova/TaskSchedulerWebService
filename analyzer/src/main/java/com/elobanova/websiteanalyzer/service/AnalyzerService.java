package com.elobanova.websiteanalyzer.service;

public class AnalyzerService {
	private static AnalyzerService instance = null;

	private AnalyzerService() {
	}

	public static synchronized AnalyzerService getInstance() {
		if (instance == null) {
			instance = new AnalyzerService();
		}

		return instance;
	}

	public void process() {
		// TODO Auto-generated method stub
		
	}
}
