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

@Path("/tasks")
public class TasksController {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/")
	public Response getListOfTasks() {
		JSONArray tasksList = JSONExporter.getInstance().exportToJSON(AnalysisTaskService.getInstance().getValues());
		return Response.status(200).entity(tasksList.toString()).build();
	}

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
