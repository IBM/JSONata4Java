package com.api.jsonata4java.test.expressions;

import static com.api.jsonata4java.text.expressions.utils.Utils.test;

import org.junit.Test;

public class FunctionErrorTests {

	@Test
	public void containsCaseInsensitive() throws Exception {
		test("$contains(\"Hello World\", /wo/i)", "true", null, null);
	}

	@Test
	public void containsStartingFromLine() throws Exception {
		test("$contains(\"abracadabra\", /^abracadabra$/)", "true", null, null);
	}

	@Test
	public void containsWithSlashLine() throws Exception {
		test("$contains(\"abra/cadabra\", /^abra\\/cadabra$/)", "true", null, null);
	}

	@Test
	public void containsWithBlank() throws Exception {
		test("$contains(\"abra cadabra\", /^abra cadabra$/)", "true", null, null);
	}

	@Test
	public void containsWithBlank2() throws Exception {
		test("$contains(\"abra   cadabra\", /^abra *cadabra$/)", "true", null, null);
	}

	@Test
	public void replaceWithRegexSimple() throws Exception {
		test("$replace('foox123xfuox456xfiox789xfoo', /x?f[a-z]ox?/, '---')", "\"---123---456---789---\"", null, null);
	}

	@Test
	public void repaceWithRegexUnderscore() throws Exception {
		test("$replace('foo_123_fuo_456_fio_789_foo', /_?f[a-z]o_?/, '---')", "\"---123---456---789---\"", null, null);
	}

	@Test
	public void repaceWithRegexWithBlank() throws Exception {
		test("$replace('foo 123 fuo 456 fio 789 foo',/  */, '-')", "\"---123---456---789---\"", null, null);
	}
}
