package com.elobanova.websiteanalyzer.test.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.elobanova.websiteanalyzer.model.HeadingInfo;
import com.elobanova.websiteanalyzer.parser.JsoupParser;

public class JsoupParserTest {
	private JsoupParser jsoupParser;

	@Before
	public void setUp() throws IOException {
		jsoupParser = new JsoupParser("https://www.campus.rwth-aachen.de/office/");
	}

	@Test
	public void testParseTitle() {
		String expectedTitle = "campusOffice";
		String actualTitle = jsoupParser.parseTitle();
		assertEquals(expectedTitle, actualTitle);
	}

	@Test
	public void testParseNumberOfInternalLinks() {
		int expectedNumberOfInternalLinks = 16;
		int actualNumberOfInternalLinks = jsoupParser.parseNumberOfInternalLinks();
		assertEquals(expectedNumberOfInternalLinks, actualNumberOfInternalLinks);
	}

	@Test
	public void testParseNumberOfExternalLinks() {
		int expectedNumberOfExternalLinks = 2;
		int actualNumberOfExternalLinks = jsoupParser.parseNumberOfExternalLinks();
		assertEquals(expectedNumberOfExternalLinks, actualNumberOfExternalLinks);
	}
	
	@Test
	public void testParseLoginFormIsPresent() {
		boolean expectedLoginIsPresent = true;
		boolean actualLoginIsPresent = jsoupParser.parseLoginFormIsPresent();
		assertEquals(expectedLoginIsPresent, actualLoginIsPresent);
	}
	
	@Test
	public void testParseHeadings() {
		List<HeadingInfo> actualHeadings = jsoupParser.parseHeadings();
		
		assertNotNull(actualHeadings);
		assertEquals(JsoupParser.HEADING_LEVELS_NUMBER_IN_HTML, actualHeadings.size());
	}

	@Test
	public void testParseHTMLVersion() {
		String expectedHTMLVersion = "-//W3C//DTD HTML 4.01 Transitional//EN";
		String actualHTMLVersion = jsoupParser.parseHTMLVersion();
		assertEquals(expectedHTMLVersion, actualHTMLVersion);
	}

	@Test
	public void testParseNumberOfNotAccessableLinks() {
		int expectedNumberOfNotAccessableLinks = 7;
		int actualNumberOfNotAccessableLinks = jsoupParser.parseNumberOfNotAccessableLinks();
		assertEquals(expectedNumberOfNotAccessableLinks, actualNumberOfNotAccessableLinks);
	}
}
