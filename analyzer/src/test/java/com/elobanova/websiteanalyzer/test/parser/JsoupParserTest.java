package com.elobanova.websiteanalyzer.test.parser;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

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
