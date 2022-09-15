package com.api.jsonata4java.test.expressions;

import static com.api.jsonata4java.text.expressions.utils.Utils.test;

import org.junit.Test;

/**
 * A JUnit test class used in order to ease debugging of particular prblematic cases.
 *  
 * @author bluemel
 */
public class FunctionErrorTests {

	@Test
	public void containsWithBlank() throws Exception {
		test("$contains(\"abra cadabra\", /^abra\\scadabra$/)", "true", null, null);
		test("$contains(\"abra   cadabra\", /^abra\\s*cadabra$/)", "true", null, null);
		test("$contains(\"abra   cadabra\", /^abra\\040*cadabra$/)", "true", null, null);
		test("$contains(\"abra   cadabra\", /^abra\\x20*cadabra$/)", "true", null, null);
	}

	@Test
	public void replaceWithRegexWithBlank() throws Exception {
		test("$replace('foo 123   fuo  456 fio    789 foo',/\\s+/, '--')", "\"foo--123--fuo--456--fio--789--foo\"", null, null);
	}
}
