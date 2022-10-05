package com.api.jsonata4java;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

public class TesterUITest {

	TesterUI testerUi;

	@Before
	public void setUp() throws IOException {
		testerUi = new TesterUI();
	}

	@Test
	public void testJsonToXml() throws IOException {
		System.out.println(testerUi.jsonToXml(TesterUI.readFile("src/test/resources/exerciser/address.json")));
	}

	@Test
	public void testXmlToJson() throws IOException {
		assertEquals(minifyJson(TesterUI.readFile("src/test/resources/exerciser/address.json")),
				minifyJson(testerUi.xmlToJson(TesterUI.readFile("src/test/resources/exerciser/address.xml"))));
	}

	public static String minifyJson(final String in) {
		final StringWriter sw = new StringWriter();
		final JsonFactory factory = new JsonFactory();
		try (final JsonGenerator gen = factory.createGenerator(sw)) {
			final JsonParser parser = factory.createParser(in);
			while (parser.nextToken() != null) {
				gen.copyCurrentEvent(parser);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return sw.getBuffer().toString();
	}

//	public static String minifyXml(final String in) {
//		return XalanWrapper.xslt(in, /* style sheet */
//				"<?xml version=\"1.0\"?>"
//						+ "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">"
//						+ "<xsl:output indent=\"no\"/>" + "<xsl:strip-space elements=\"*\"/>"
//						+ "<xsl:template match=\"@*|node()\">"
//						+ "<xsl:copy><xsl:apply-templates select=\"@*|node()\"/></xsl:copy>" + "</xsl:template>"
//						+ "</xsl:stylesheet>",
//				/* no params */ null, "xml", "UTF-8", false, null, !in.strip().startsWith("<?xml"));
//	}
}
