package com.elobanova.websiteanalyzer.controller;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.elobanova.websiteanalyzer.model.AnalysisTask;
import com.elobanova.websiteanalyzer.model.StatusEnum;
import com.elobanova.websiteanalyzer.service.AnalysisTaskThread;
import com.elobanova.websiteanalyzer.service.OnResponseListener;

/**
 * Servlet implementation class WebSiteAnalyzerServlet
 */
public class WebSiteAnalyzerServlet extends HttpServlet {
	private static final int POOL_LIMIT = 5;
	private static final long serialVersionUID = 1L;
	private ConcurrentMap<String, AnalysisTask> tasks = new ConcurrentHashMap<>();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		String pageurl = request.getParameter("pageurl");
		if (pageurl == null || pageurl.isEmpty()) {
			log("PageUrl was empty");
		} else {
			ExecutorService executor = Executors.newFixedThreadPool(POOL_LIMIT);
			AnalysisTask analysisTask = new AnalysisTask();
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
					log(errorMessage);
				}
			});
			executor.execute(taskRunnable);
		}
	}

}
