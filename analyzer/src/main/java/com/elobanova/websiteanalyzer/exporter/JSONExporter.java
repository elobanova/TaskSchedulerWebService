package com.elobanova.websiteanalyzer.exporter;

import java.util.Collection;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.elobanova.websiteanalyzer.model.AnalysisTask;
import com.elobanova.websiteanalyzer.model.DocumentInfo;
import com.elobanova.websiteanalyzer.model.HeadingInfo;
import com.elobanova.websiteanalyzer.model.StatusEnum;
import com.elobanova.websiteanalyzer.parser.Connection;

/**
 * A class which exports the model objects into a JSON representation.
 * 
 * @author Ekaterina Lobanova
 */
public class JSONExporter {
	public static final String URL_PROPERTY = "url";
	public static final String STATUS_PROPERTY = "status";
	public static final String DOCUMENT_INFO_PROPERTY = "info";
	public static final String TITLE_PROPERTY = "title";
	public static final String IS_LOGIN_PRESENT_PROPERTY = "isLoginFormPresent";
	public static final String HTML_VERSION_PROPERTY = "htmlVersion";
	public static final String HEADINGS_PROPERTY = "headings";
	public static final String HEADING_NODE_NAME_PROPERTY = "headingNodeName";
	public static final String HEADING_COUNT_PROPERTY = "count";
	public static final String INTERNAL_LINKS_NUMBER_PROPERTY = "internalLinksNumber";
	public static final String EXTERNAL_LINKS_NUMBER_PROPERTY = "externalLinksNumber";
	public static final String INACCESSIBLE_LINKS_NUMBER_PROPERTY = "inaccessibleLinksNumber";

	private static JSONExporter instance = null;

	private JSONExporter() {
	}

	public static JSONExporter getInstance() {
		if (instance == null) {
			synchronized (JSONExporter.class) {
				if (instance == null) {
					instance = new JSONExporter();
				}
			}
		}

		return instance;
	}

	/**
	 * Performs an exporting of an object of AnalysisTask to a JSON
	 * representation.
	 * 
	 * @param analysisTask
	 *            an object of AnalysisTask to export
	 * @return a resulting JSON object with the task's information or an empty
	 *         object
	 */
	public JSONObject exportToJSON(AnalysisTask analysisTask) {
		JSONObject taskObject = new JSONObject();
		if (analysisTask != null) {
			taskObject.put(URL_PROPERTY, analysisTask.getUrl());
			StatusEnum status = analysisTask.getStatus();
			if (status != null) {
				taskObject.put(STATUS_PROPERTY, status.getName());
			}
			taskObject.put(DOCUMENT_INFO_PROPERTY, exportToJSON(analysisTask.getDocumentInfo()));
		}
		return taskObject;
	}

	/**
	 * Performs an exporting of an object of DocumentInfo to a JSON
	 * representation.
	 * 
	 * @param documentInfo
	 *            an object of DocumentInfo to export
	 * @return a resulting JSON object with the document's information or an
	 *         empty object
	 */
	public JSONObject exportToJSON(DocumentInfo documentInfo) {
		JSONObject infoObject = new JSONObject();
		if (documentInfo != null) {
			infoObject.put(TITLE_PROPERTY, documentInfo.getTitle());
			infoObject.put(IS_LOGIN_PRESENT_PROPERTY, documentInfo.isLoginFormPresent());
			infoObject.put(HTML_VERSION_PROPERTY, documentInfo.getHtmlVersion());
			infoObject.put(INTERNAL_LINKS_NUMBER_PROPERTY, documentInfo.getInternalLinksNumber());
			infoObject.put(EXTERNAL_LINKS_NUMBER_PROPERTY, documentInfo.getExternalLinksNumber());
			infoObject.put(INACCESSIBLE_LINKS_NUMBER_PROPERTY, documentInfo.getInaccessibleLinksNumber());

			List<HeadingInfo> headings = documentInfo.getHeadings();
			if (headings != null) {
				JSONArray headingsObject = new JSONArray();
				headings.stream().forEach(heading -> headingsObject.put(exportHeading(heading)));
				infoObject.put(HEADINGS_PROPERTY, headingsObject);
			}
		}
		return infoObject;
	}

	/**
	 * Performs an exporting of an object of HeadingInfo to a JSON
	 * representation.
	 * 
	 * @param heading
	 *            an object of HeadingInfo to export
	 * @return a resulting JSON object with the heading's information or an
	 *         empty object
	 */
	public JSONObject exportHeading(HeadingInfo heading) {
		JSONObject headingObject = new JSONObject();
		if (heading != null) {
			headingObject.put(HEADING_NODE_NAME_PROPERTY, heading.getHeadingName());
			headingObject.put(HEADING_COUNT_PROPERTY, heading.getHeadingCount());
		}
		return headingObject;
	}

	/**
	 * Builds a JSONArray object from a set of analysis task entries.
	 * 
	 * @param entrySet
	 *            a set of analysis task entries
	 * @return a JSONArray object for that set or an empty object
	 */
	public JSONArray exportToJSON(Collection<? extends AnalysisTask> analysisTasks) {
		JSONArray tasksList = new JSONArray();
		if (analysisTasks != null) {
			analysisTasks.stream().forEach(entry -> tasksList.put(exportToJSON(entry)));
		}
		return tasksList;
	}

	/**
	 * Retrieves a url from a json string.
	 * 
	 * @param json
	 *            a json string which contains a url
	 * @return a url path if a json string has this property
	 */
	public String exportToUrl(String json) {
		if (json != null) {
			JSONObject jsonObj = new JSONObject(json);
			if (jsonObj.has(URL_PROPERTY)) {
				return jsonObj.getString(JSONExporter.URL_PROPERTY);
			}
		}

		return null;
	}

	public JSONObject exportToJSON(Connection checkURL) {
		JSONObject connectionJSON = new JSONObject();
		if (checkURL != null) {
			connectionJSON.put(STATUS_PROPERTY, checkURL.getStatusCode());
		}
		return connectionJSON;
	}
}
