package com.api.jsonata4java.test.expressions;

import static com.api.jsonata4java.text.expressions.utils.Utils.test;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.api.jsonata4java.expressions.functions.ReplaceFunction;

/**
 * A JUnit test class used in order to ease debugging of particular prblematic
 * cases.
 * 
 * @author Martin Bluemel
 */
public class FunctionErrorTests {

	// From JSONata test group "regex"

	// in contrary to JSONata Java does not allow to use capturing group numbers in the replacement
	// you have not defined in the regular expression... don't know any sensible use of the JSONata behavior.
	@Test
	public void testRegex_case15() throws Exception {
		test("$replace(\"265USD\", /([0-9]+)USD/, \"$0$1$2\")", "\"265USD265\"", null, null);
		// this one works out equally in JSONata and Java
		test("$replace(\"265USD\", /([0-9]+)USD(.*)/, \"$0$1$2\")", "\"265USD265\"", null, null);
	}

	
	@Test
	public void testRegex_case16() throws Exception {
		assertEquals("[1=ab][2=]cd", "abcd".replaceAll("(ab|a)", "[1=$1][2=]"));
		test("$replace(\"abcd\", /(ab|a)/, \"[1=$1][2=]\")", "\"[1=ab][2=]cd\"", null, null);
		// test("$replace(\"abcd\", /(ab)|(a)/, \"[1=$1][2=$2]\")", "\"[1=ab][2=]cd\"", null, null);
	}

	// Fixed

	// JSNOata test suite group "regex" case012
	// $replace("265USD", /([0-9]+)USD/, "$$$1") evaluates to "$265" in JSONata
	// in JSONata4Java / Java regular expression you have to write "\$" instead of
	// $$
	// in order to escape the capturing group replacement start "$" and to replace a
	// real dollar
	@Test
	public void testRegex_case012() throws Exception {
		// assertEquals("$$", "\\$$".replaceAll("\\\\\\$([^<^0])", "\\$$1"));
		// "$$" -> "\$"
		// "$w" -> "\$w"
		// preseve $<digit> and $<
		assertEquals("abc\\$$1xyz", "abc$$$1xyz".replaceAll("\\$\\$", "\\\\\\$"));
		assertEquals("abc\\$wxyz", "abc$wxyz".replaceAll("\\$([^0-9^<])", "\\\\\\$$1"));
		assertEquals("abc\\$$1efg\\$wxyz", "abc$$$1efg$wxyz"
				.replaceAll("\\$\\$", "\\\\\\$")
				.replaceAll("([^\\\\]|^)\\$([^0-9^<])", "$1\\\\\\$$2")
				);

		assertEquals("abc\\$$1xyz", ReplaceFunction.jsonata2JavaReplacement("abc$$$1xyz"));
		assertEquals("abc\\$wxyz", ReplaceFunction.jsonata2JavaReplacement("abc$wxyz"));
		assertEquals("abc\\$$1efg\\$wxyz", ReplaceFunction.jsonata2JavaReplacement("abc$$$1efg$wxyz"));

		assertEquals("$265", "265USD".replaceAll("([0-9]+)USD", "\\$$1"));
		test("$replace(\"265USD\", /([0-9]+)USD/, \"$$$1\")", "\"$265\"", null, null);
	}

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
