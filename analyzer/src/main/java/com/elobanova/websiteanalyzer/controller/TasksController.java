package com.elobanova.websiteanalyzer.controller;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;

import com.elobanova.websiteanalyzer.exporter.JSONExporter;
import com.elobanova.websiteanalyzer.parser.NetworkUtils;
import com.elobanova.websiteanalyzer.service.AnalysisTaskService;

/**
 * A controller class to handle RESTful requests. The operations supported are
 * returning a collection of tasks on GET and scheduling a task on POST.
 * 
 * @author Ekaterina Lobanova
 *
 */
@Path("/tasks")
public class TasksController {

	/**
	 * A GET request under the path /tasks.
	 * 
	 * @return a collection of all tasks as a JSON array
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/")
	public Response getListOfTasks() {
		JSONArray tasksList = JSONExporter.getInstance().exportToJSON(AnalysisTaskService.getInstance().getValues());
		return Response.status(200).entity(tasksList.toString()).build();
	}

	/**
	 * A POST request under the path /tasks.
	 * 
	 * @param json
	 *            a json with a URL to analyze
	 * @return a status ok (200) or error (500) if the url is invalid
	 */
	@POST
	@Path("/")
	public Response processTask(String json) {
		String taskURL = JSONExporter.getInstance().exportToUrl(json);
		if (NetworkUtils.isValidURL(taskURL)) {
			AnalysisTaskService.getInstance().processTask(taskURL);
			return Response.status(200).build();
		}

		return Response.status(500).build();
	}
}
