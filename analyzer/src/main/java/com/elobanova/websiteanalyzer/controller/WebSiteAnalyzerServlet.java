package com.elobanova.websiteanalyzer.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.elobanova.websiteanalyzer.service.AnalyzerService;

/**
 * Servlet implementation class WebSiteAnalyzerServlet
 */
public class WebSiteAnalyzerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String pageurl = request.getParameter("pageurl");
		if (pageurl == null || pageurl.isEmpty()) {
			out.println("<footer>pageurl is missing</footer>");	
		}
		else {
			AnalyzerService.getInstance().process();
		}
	}

}
