package com.elobanova.websiteanalyzer.test.exporter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.elobanova.websiteanalyzer.exporter.JSONExporter;
import com.elobanova.websiteanalyzer.model.AnalysisTask;
import com.elobanova.websiteanalyzer.model.DocumentInfo;
import com.elobanova.websiteanalyzer.model.DocumentInfo.DocumentInfoBuilder;
import com.elobanova.websiteanalyzer.model.HeadingInfo;
import com.elobanova.websiteanalyzer.model.StatusEnum;

public class JSONExporterTest {
	private static final int NOT_ACCESSIBLE_LINKS_NUMBER = 2;
	private static final int INTERNAL_LINKS_NUMBER = 10;
	private static final int EXTERNAL_LINKS_NUMBER = 5;
	private static final String HTML_VERSION = "HTML 4.01";
	private static final boolean IS_LOGIN_FORM_PRESENT = true;
	private static final String DOCUMENT_INFO_TITLE = "Google";
	private static final String TASK_URL = "https://www.campus.rwth-aachen.de/office/";
	private JSONExporter jsonExporter;
	private AnalysisTask analysisTask;
	private DocumentInfo documentInfo;
	private List<HeadingInfo> headings;

	@Before
	public void setUp() throws IOException {
		jsonExporter = JSONExporter.getInstance();
		analysisTask = new AnalysisTask();
		DocumentInfoBuilder builder = new DocumentInfoBuilder();
		headings = new ArrayList<>();
		HeadingInfo headingInfoOne = new HeadingInfo("h1", 12);

		HeadingInfo headingInfoAnother = new HeadingInfo("h2", 33);

		headings.add(headingInfoOne);
		headings.add(headingInfoAnother);

		builder.setHTMLVersion(HTML_VERSION).setLoginFormPresent(IS_LOGIN_FORM_PRESENT)
				.setNumberOfExternalLinks(EXTERNAL_LINKS_NUMBER).setNumberOfInternalLinks(INTERNAL_LINKS_NUMBER)
				.setNumberOfNotAccessibleLinks(NOT_ACCESSIBLE_LINKS_NUMBER).setTitle(DOCUMENT_INFO_TITLE)
				.setHeadings(headings);
		analysisTask.setUrl(TASK_URL);
		analysisTask.setStatus(StatusEnum.PROCESSING);
		documentInfo = builder.build();
		analysisTask.setDocumentInfo(documentInfo);
	}

	@Test
	public void testExportTaskToJSON() {
		JSONObject exportObject = jsonExporter.exportToJSON(analysisTask);
		assertNotNull(exportObject);

		assertEquals(TASK_URL, exportObject.get(JSONExporter.URL_PROPERTY));
		assertEquals(StatusEnum.PROCESSING.getName(), exportObject.get(JSONExporter.STATUS_PROPERTY));
	}

	@Test
	public void testExportTaskWhenTaskIsNull() {
		AnalysisTask taskToExport = null;
		JSONObject exportObject = jsonExporter.exportToJSON(taskToExport);
		assertNotNull(exportObject);

		assertTrue(exportObject.isNull(JSONExporter.URL_PROPERTY));
		assertTrue(exportObject.isNull(JSONExporter.STATUS_PROPERTY));
		assertTrue(exportObject.isNull(JSONExporter.DOCUMENT_INFO_PROPERTY));
	}

	@Test
	public void testExportTaskWhenTaskHasNoStatus() {
		analysisTask.setStatus(null);
		JSONObject exportObject = jsonExporter.exportToJSON(analysisTask);
		assertNotNull(exportObject);

		assertTrue(exportObject.isNull(JSONExporter.STATUS_PROPERTY));
		assertFalse(exportObject.isNull(JSONExporter.URL_PROPERTY));
		assertFalse(exportObject.isNull(JSONExporter.DOCUMENT_INFO_PROPERTY));
	}

	@Test
	public void testExportDocumentInfoToJSON() {
		JSONObject exportObject = jsonExporter.exportToJSON(documentInfo);
		assertNotNull(exportObject);

		assertEquals(DOCUMENT_INFO_TITLE, exportObject.get(JSONExporter.TITLE_PROPERTY));
		assertEquals(IS_LOGIN_FORM_PRESENT, exportObject.get(JSONExporter.IS_LOGIN_PRESENT_PROPERTY));
		assertEquals(HTML_VERSION, exportObject.get(JSONExporter.HTML_VERSION_PROPERTY));
		assertEquals(EXTERNAL_LINKS_NUMBER, exportObject.get(JSONExporter.EXTERNAL_LINKS_NUMBER_PROPERTY));
		assertEquals(INTERNAL_LINKS_NUMBER, exportObject.get(JSONExporter.INTERNAL_LINKS_NUMBER_PROPERTY));
		assertEquals(NOT_ACCESSIBLE_LINKS_NUMBER, exportObject.get(JSONExporter.INACCESSIBLE_LINKS_NUMBER_PROPERTY));
		assertNotNull(exportObject.get(JSONExporter.HEADINGS_PROPERTY));
	}

	@Test
	public void testExportDocumentInfoToJSONWhenInfoIsNull() {
		DocumentInfo infoToExport = null;
		JSONObject exportObject = jsonExporter.exportToJSON(infoToExport);
		assertNotNull(exportObject);

		assertTrue(exportObject.isNull(JSONExporter.TITLE_PROPERTY));
		assertTrue(exportObject.isNull(JSONExporter.IS_LOGIN_PRESENT_PROPERTY));
		assertTrue(exportObject.isNull(JSONExporter.HTML_VERSION_PROPERTY));
		assertTrue(exportObject.isNull(JSONExporter.EXTERNAL_LINKS_NUMBER_PROPERTY));
		assertTrue(exportObject.isNull(JSONExporter.INTERNAL_LINKS_NUMBER_PROPERTY));
		assertTrue(exportObject.isNull(JSONExporter.INACCESSIBLE_LINKS_NUMBER_PROPERTY));
		assertTrue(exportObject.isNull(JSONExporter.HEADINGS_PROPERTY));
	}

	@Test
	public void testExportDocumentInfoToJSONWhenHeadingsIsNull() {
		DocumentInfoBuilder builder = new DocumentInfoBuilder();
		builder.setTitle(DOCUMENT_INFO_TITLE);
		DocumentInfo infoToExport = builder.build();
		JSONObject exportObject = jsonExporter.exportToJSON(infoToExport);
		assertNotNull(exportObject);

		assertEquals(DOCUMENT_INFO_TITLE, exportObject.get(JSONExporter.TITLE_PROPERTY));
		assertTrue(exportObject.isNull(JSONExporter.HEADINGS_PROPERTY));
	}

	@Test
	public void testExportHeading() {
		String expectedHeadingName = "h1";
		int expectedHeadingCount = 10;
		HeadingInfo heading = new HeadingInfo(expectedHeadingName, expectedHeadingCount);
		JSONObject exportObject = jsonExporter.exportHeading(heading);
		assertNotNull(exportObject);

		assertEquals(expectedHeadingName, exportObject.get(JSONExporter.HEADING_NODE_NAME_PROPERTY));
		assertEquals(expectedHeadingCount, exportObject.get(JSONExporter.HEADING_COUNT_PROPERTY));
	}

	@Test
	public void testExportHeadingWhenHeadingIsNull() {
		HeadingInfo heading = null;
		JSONObject exportObject = jsonExporter.exportHeading(heading);
		assertNotNull(exportObject);

		assertTrue(exportObject.isNull(JSONExporter.HEADING_NODE_NAME_PROPERTY));
		assertTrue(exportObject.isNull(JSONExporter.HEADING_COUNT_PROPERTY));
	}
}
