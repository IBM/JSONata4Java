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
 * http://docs.jsonata.org/numeric-functions.html
 * 
 * $toMillis(timestamp)
 * 
 * Convert a timestamp string in the ISO 8601 format to the number of
 * milliseconds since the Unix Epoch (1 January, 1970 UTC) as a number.
 * 
 * An error is thrown if the string is not in the correct format.
 * 
 * Examples
 * 
 * $toMillis("2017-11-07T15:07:54.972Z")==1510067274972
 * 
 */
@RunWith(Parameterized.class)
public class ToMillisFunctionTests {
	private static final String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT,
			Constants.FUNCTION_TO_MILLIS);
	private static final String ERR_MSG_ARG1_BAD_TYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE,
			Constants.FUNCTION_TO_MILLIS);
	private static final String ERR_MSG_TO_MILLIS_ISO_8601_FORMAT_FOO = String
			.format(Constants.ERR_MSG_TO_MILLIS_ISO_8601_FORMAT, "foo");

	@Parameter(0)
	public String expression;

	@Parameter(1)
	public String expectedResultJsonString;

	@Parameter(2)
	public String expectedRuntimeExceptionMessage;

	@Parameters(name = "{index}: {0} -> {1} ({2})")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] { { "$toMillis()", null, ERR_BAD_CONTEXT }, //
				{ "$toMillis(1)", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$toMillis(-1)", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$toMillis({})", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$toMillis({\"hello\": 1})", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$toMillis([])", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$toMillis([\"hello\", 1])", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$toMillis(true)", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$toMillis(null)", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$toMillis('foo')", null, ERR_MSG_TO_MILLIS_ISO_8601_FORMAT_FOO }, //
				{ "$toMillis(a.b.c)", null, null }, //
				{ "$toMillis('2018-01-22T10:02:09.240Z')", "1516615329240", null }, //
				{ "$toMillis('2018-01-22T10:02:09.240+01:00')", "1516611729240", null }, //
				{ "$toMillis('2018-01-22T10:02:09.240-01:00')", "1516618929240", null }, //
				{ "$toMillis('2018-01-22T10:02:09.240+10:00')", "1516579329240", null }, //
				{ "$toMillis('2018-01-22T10:02:09.240-10:00')", "1516651329240", null } });
	}

	@Test
	public void runTest() throws Exception {
		test(this.expression, expectedResultJsonString, expectedRuntimeExceptionMessage, null);
	}
}
