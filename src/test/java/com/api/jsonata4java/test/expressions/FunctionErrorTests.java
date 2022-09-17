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
	public void testToMillis() throws Exception {
		test("($toMillis('13:45', '[H]:[m]') ~> $fromMillis() ~> $substringBefore('T')) = $substringBefore($now(), 'T')", "true", null, null);
	}

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
	
	@Test
	public void splitWithRegex() throws Exception {
		test("$split('this     is   a simple  test', /\\s+/)", "[ \"this\", \"is\", \"a\", \"simple\", \"test\" ]", null, null);
	}

	@Test
	public void match() throws Exception {
		test("$match('ababbabbbcc',/a(b+)/)",
				"[{\"match\":\"ab\",\"index\":0,\"groups\":[\"b\"]},"
				+ "{\"match\":\"abb\",\"index\":2,\"groups\":[\"bb\"]},"
				+ "{\"match\":\"abbb\",\"index\":5,\"groups\":[\"bbb\"]}]",
				null, null);
	}

	@Test
	public void matchCaseInsensitively() throws Exception {
		test("$match('abBbAabbaAaBcc', /(a+)(b+)/i))",
				"[{\"match\":\"abBb\",\"index\":0,\"groups\":[\"a\",\"bBb\"]},"
				+ "{\"match\":\"Aabb\",\"index\":4,\"groups\":[\"Aa\",\"bb\"]},"
				+ "{\"match\":\"aAaB\",\"index\":8,\"groups\":[\"aAa\",\"B\"]}]",
				null, null);
	}
}
