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
 * For simplicity, these tests don't rely on $state/$event/$instance access; instead
 * providing the "input" inlined with the expression itself (e.g. ["a", "b"][0]=="a").
 * Separate test cases verify that variable access works as expected.
 * 
 * From http://docs.jsonata.org/string-functions.html:
 * 
 * $now()
 * 
 * Generates a UTC timestamp in ISO 8601 compatible format and returns it as a string. All invocations of $now()
 * within an evaluation of an expression will all return the same timestamp value
 * 
 * Examples
 * 
 * $now()=="2017-05-15T15:12:59.152Z"
 *
 */
@RunWith(Parameterized.class)
public class NowFunctionTests {

   private static String ERR_ARG1BADTYPE = String
            .format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_NOW);

	@Parameter(0)
	public String expression;
	
	@Parameter(1)
	public String expectedResultJsonString;
	
	@Parameter(2)
	public String expectedRuntimeExceptionMessage;
	
	@Parameters(name = "{index}: {0} -> {1} ({2})")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
            {"$now({})",     null, ERR_ARG1BADTYPE}, //
            {"$now([])",     null, ERR_ARG1BADTYPE}, //
            {"$now(1)",      null, ERR_ARG1BADTYPE}, //
            {"$now(-1)",     null, ERR_ARG1BADTYPE}, //
            {"$now(10/3.0)", null, ERR_ARG1BADTYPE}, //
            {"$now('1')",    null, ERR_ARG1BADTYPE}, //
            {"$now(a.b.c)",  null, ERR_ARG1BADTYPE}, //
			{ "$now(null)", null, ERR_ARG1BADTYPE } //
            // TODO: {"$now(\"[Y0000]\")", "2019", null}
            // TODO: Need to work out how to test the function since we cannot predict the string that is returned
            // {"$now()",    null, null}
		});
	}
	
    @Test
    public void runTest() throws Exception {
    	test(this.expression, expectedResultJsonString, expectedRuntimeExceptionMessage, null);
    }
}
