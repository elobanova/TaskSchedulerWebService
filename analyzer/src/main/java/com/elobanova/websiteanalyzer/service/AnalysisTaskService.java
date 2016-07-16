package com.elobanova.websiteanalyzer.service;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.elobanova.websiteanalyzer.model.AnalysisTask;
import com.elobanova.websiteanalyzer.model.StatusEnum;

/**
 * A service class to perform operations from controller. There is an in-memory
 * storage and a pool with a limit to preven overloading.
 * 
 * @author Ekaterina Lobanova
 *
 */
public class AnalysisTaskService {
	private static final int POOL_LIMIT = 5;
	private ConcurrentMap<String, AnalysisTask> tasks;
	private ExecutorService executor;

	private static AnalysisTaskService instance;

	private AnalysisTaskService() {
		this.executor = Executors.newFixedThreadPool(POOL_LIMIT);
		this.tasks = new ConcurrentHashMap<>();
	}

	public static AnalysisTaskService getInstance() {
		if (instance == null) {
			synchronized (AnalysisTaskService.class) {
				if (instance == null) {
					instance = new AnalysisTaskService();
				}
			}
		}

		return instance;
	}

	/**
	 * Retrieves the instance of AnalysisTask by given url
	 * 
	 * @param url
	 *            a url of the web site to analyze
	 * @return an instance of AnalysisTask by given url
	 */
	public AnalysisTask getTaskByURL(String url) {
		return tasks.get(url);
	}

	/**
	 * Shuts down the executor.
	 */
	public void shutdown() {
		if (this.executor != null) {
			executor.shutdown();
		}
	}

	/**
	 * Handles processing of the analysis task. Due to the significant time
	 * required to process the HTML document, a job is run in a dedicated
	 * thread.
	 * 
	 * @param pageurl
	 */
	public void processTask(String pageurl) {
		final AnalysisTask analysisTask = new AnalysisTask();
		analysisTask.setUrl(pageurl);
		analysisTask.setStatus(StatusEnum.PROCESSING);
		tasks.put(analysisTask.getUrl(), analysisTask);
		AnalysisTaskThread taskRunnable = new AnalysisTaskThread(analysisTask);
		taskRunnable.setOnResponseListener(new OnResponseListener<AnalysisTask>() {

			@Override
			public void onResponse(AnalysisTask response) {
				if (response != null) {
					String keyUrl = response.getUrl();
					AnalysisTask processedTask = tasks.containsKey(keyUrl) ? tasks.get(keyUrl) : response;
					processedTask.setStatus(StatusEnum.DONE);
					tasks.put(keyUrl, processedTask);
				}
			}

			@Override
			public void onError(String errorMessage) {
				analysisTask.setStatus(StatusEnum.ERROR);
			}
		});
		executor.execute(taskRunnable);
	}

	public Collection<? extends AnalysisTask> getValues() {
		return tasks.values();
	}
}
