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

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.api.jsonata4java.expressions.utils.Constants;

/**
 * For simplicity, these tests don't rely on $state/$event/$instance access;
 * instead providing the "input" inlined with the expression itself (e.g. ["a",
 * "b"][0]=="a"). Separate test cases verify that variable access works as
 * expected.
 * 
 * From http://docs.jsonata.org/string-functions.html:
 * 
 * $fromMillis(number)
 * 
 * Convert a number representing milliseconds since the Unix Epoch (1 January,
 * 1970 UTC) to a timestamp string in the ISO 8601 format.
 * 
 * Examples
 * 
 * $fromMillis(1510067557121)=="2017-11-07T15:12:37.121Z"
 *
 */
@RunWith(Parameterized.class)
public class FromMillisFunctionTests {

	private static final String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT,
			Constants.FUNCTION_FROM_MILLIS);
	private static final String ERR_MSG_ARG1_BAD_TYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE,
			Constants.FUNCTION_FROM_MILLIS);

	@Parameter(0)
	public String expression;

	@Parameter(1)
	public String expectedResultJsonString;

	@Parameter(2)
	public String expectedRuntimeExceptionMessage;

	@Parameters(name = "{index}: {0} -> {1} ({2})")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] { { "$fromMillis()", null, ERR_BAD_CONTEXT }, //
				{ "$fromMillis({})", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$fromMillis([])", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$fromMillis('1')", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$fromMillis(true)", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$fromMillis(null)", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$fromMillis(a.b.c)", null, null }, //
				{ "$fromMillis(1512159584136)", "\"2017-12-01T20:19:44.136Z\"", null }, //
				{ "$fromMillis(1)", "\"1970-01-01T00:00:00.001Z\"", null }, //
				{ "$fromMillis(-1)", "\"1969-12-31T23:59:59.999Z\"", null }, //
				{ "$fromMillis(10/3.0)", "\"1970-01-01T00:00:00.003Z\"", null } });
	}

	@Test
	public void runTest() throws Exception {
		test(this.expression, expectedResultJsonString, expectedRuntimeExceptionMessage, null);
	}
}
