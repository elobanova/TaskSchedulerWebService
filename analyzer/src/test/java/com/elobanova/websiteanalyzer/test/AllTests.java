package com.elobanova.websiteanalyzer.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.elobanova.websiteanalyzer.test.exporter.AllExporterTests;
import com.elobanova.websiteanalyzer.test.parser.AllParserTests;

/**
 * @author Ekaterina Lobanova
 */
@RunWith(Suite.class)
@SuiteClasses({ AllParserTests.class, AllExporterTests.class })
public class AllTests {

}
