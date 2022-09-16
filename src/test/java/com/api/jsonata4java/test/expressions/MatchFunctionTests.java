/**
 * (c) Copyright 2018, 2019 IBM Corporation
 * 1 New Orchard Road, 
 * Armonk, New York, 10504-1722
 * United States
 * +1 914 499 1900
 * support: Nathaniel Mills wnm3@us.ibm.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.api.jsonata4java.test.expressions;

import static com.api.jsonata4java.text.expressions.utils.Utils.test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.api.jsonata4java.expressions.utils.Constants;

/**
 * For simplicity, these tests provide the "input" inlined with the expression
 * itself (e.g. ["a", "b"][0]=="a"). Separate test cases verify that variable
 * access works as expected.
 * 
 * $match(str, pattern [, limit])
 * 
 * Finds occurrences of pattern within str and reports the match, location and
 * group.
 * 
 * If str is not specified, then the context value is used as the value of str.
 * It is an error if str is not a string.
 * 
 * The pattern parameter can either be a string or a regular expression (regex).
 * If it is a string, it specifies the substring(s) within str which should be
 * reported. If it is a regex, its is used to find.
 * 
 * The optional limit parameter, is a number that specifies the maximum number
 * of reports to make before stopping.
 * 
 * Examples
 * 
 * $match("John Smith and John Jones", "John") ==
 * "[{"match":"John","index":0,"groups":["John"]},
 * //{"match":"John","index":15,"groups":["John"]}]
 *
 */
@RunWith(Parameterized.class)
public class MatchFunctionTests implements Serializable {

	private static final long serialVersionUID = -3302876811482172935L;

	private static final String ERR_MSG_ARG1_BAD_TYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE,
			Constants.FUNCTION_MATCH);
	private static final String ERR_MSG_ARG2_BAD_TYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE,
			Constants.FUNCTION_MATCH);
	private static final String ERR_MSG_ARG3_BAD_TYPE = String.format(Constants.ERR_MSG_ARG3_BAD_TYPE,
			Constants.FUNCTION_MATCH);

	@Parameter(0)
	public String expression;

	@Parameter(1)
	public String expectedResultJsonString;

	@Parameter(2)
	public String expectedRuntimeExceptionMessage;

	@Parameters(name = "{index}: {0} -> {1} ({2})")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] { { "$match()", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$match({})", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$match({}, ' ')", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$match(' ', {})", null, ERR_MSG_ARG2_BAD_TYPE }, //

				{ "$match([])", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$match([], ' ')", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$match(' ', [])", null, ERR_MSG_ARG2_BAD_TYPE }, //

				{ "$match({\"hello\": 1})", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$match({\"hello\": 1}, ' ')", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$match(' ', {\"hello\": 1})", null, ERR_MSG_ARG2_BAD_TYPE }, //

				{ "$match(true)", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$match(true, ' ')", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$match(' ', true)", null, ERR_MSG_ARG2_BAD_TYPE }, //

				{ "$match(null)", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$match(null, ' ')", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$match(' ', null)", null, ERR_MSG_ARG2_BAD_TYPE }, //

				{ "$match(5)", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$match(5, ' ')", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$match(' ', 5)", null, ERR_MSG_ARG2_BAD_TYPE }, //

				{ "$match(-5)", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$match(-5, ' ')", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$match(' ', -5)", null, ERR_MSG_ARG2_BAD_TYPE }, //

				{ "$match(10/3.0)", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$match(10/3.0, ' ')", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$match(' ', 10/3.0)", null, ERR_MSG_ARG2_BAD_TYPE }, //

				{ "$match(a.b.c)", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$match(a.b.c, ' ')", null, ERR_MSG_ARG1_BAD_TYPE }, // TODO: issue #71 ERR_MSG_ARG2_BAD_TYPE }, //
				{ "$match(' ', a.b.c)", null, ERR_MSG_ARG2_BAD_TYPE }, //

				{ "$match('foo bar', 'a')", "{\"match\":\"a\",\"start\":5,\"groups\":[\"a\"]}", null }, // TODO: see below
				/**
				 * jsonata 1.8.2 throws exception bad arg2 but if changed to /a/ doesn't have
				 * the array just the object
				 * "[{\"match\":\"a\",\"start\":5,\"groups\":[\"a\"]}]", null }, //
				 */
				{ "$match('foo bar', 'o', 0)", null, null }, //
				{ "$match('foo bar', 'o', 1)", "{\"match\":\"o\",\"start\":1,\"groups\":[\"o\"]}", null }, // see below
				/**
				 * jsonata 1.8.2 throws exception bad arg2 but if changed to /a/ doesn't have
				 * the array "[{\"match\":\"o\",\"start\":1,\"groups\":[\"o\"]}]", null }, //
				 */
				{ "$match('foo bar', 'o', {})", null, ERR_MSG_ARG3_BAD_TYPE }, //
				{ "$match('foo bar', 'o', [])", null, ERR_MSG_ARG3_BAD_TYPE }, //
				{ "$match('foo bar', 'o', true)", null, ERR_MSG_ARG3_BAD_TYPE }, //
				{ "$match('foo bar', 'o', null)", null, ERR_MSG_ARG3_BAD_TYPE }, //
				{ "$match('foo bar', 'o', -1)", null, ERR_MSG_ARG3_BAD_TYPE }, //
				{ "$match('ababbabbcc',/a(b+)/)",
						"[{\"match\":\"ab\",\"start\":0,\"groups\":[\"ab\"]},{\"match\":\"abb\",\"start\":2,\"groups\":[\"abb\"]},{\"match\":\"abb\",\"start\":5,\"groups\":[\"abb\"]}]",
						null },
				{ "$match('ababbabbcc','a(b+)')",
						"[{\"match\":\"ab\",\"start\":0,\"groups\":[\"ab\"]},{\"match\":\"abb\",\"start\":2,\"groups\":[\"abb\"]},{\"match\":\"abb\",\"start\":5,\"groups\":[\"abb\"]}]",
						null },
//				{ "$match('abbbaabbaaabcc', /(a+)(b+)/)",
//							"[{\"match\":\"ab\",\"start\":0,\"groups\":[\"a\"\"bbb\"]},{\"match\":\"abb\",\"start\":2,\"groups\":[\"abb\"]},{\"match\":\"abb\",\"start\":5,\"groups\":[\"abb\"]}]",
//							null },
		});
	}

	@Test
	public void runTest() throws Exception {
		test(this.expression, expectedResultJsonString, expectedRuntimeExceptionMessage, null);
	}
}
