package com.elobanova.websiteanalyzer.test.exporter;

import static org.junit.Assert.assertNotNull;

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
		HeadingInfo headingInfoOne = new HeadingInfo();
		headingInfoOne.setHeadingCount(12);
		headingInfoOne.setHeadingName("h1");

		HeadingInfo headingInfoAnother = new HeadingInfo();
		headingInfoAnother.setHeadingCount(33);
		headingInfoAnother.setHeadingName("h2");

		headings.add(headingInfoOne);
		headings.add(headingInfoAnother);

		builder.setHTMLVersion("HTML 4.01").setLoginFormPresent(true).setNumberOfExternalLinks(5)
				.setNumberOfInternalLinks(10).setNumberOfNotAccessibleLinks(2).setTitle("Google").setHeadings(headings);
		analysisTask.setUrl("https://www.campus.rwth-aachen.de/office/");
		analysisTask.setStatus(StatusEnum.PROCESSING);
		documentInfo = builder.build();
		analysisTask.setDocumentInfo(documentInfo);
	}

	@Test
	public void testExportToJSON() {
		JSONObject exportObject = jsonExporter.exportToJSON(analysisTask);
		assertNotNull(exportObject);
	}
}
