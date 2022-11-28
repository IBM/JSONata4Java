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

package com.api.jsonata4java.expressions;

import static com.api.jsonata4java.expressions.utils.Utils.test;
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
 * For simplicity, these tests don't rely on $state/$event/$instance access;
 * instead providing the "input" inlined with the expression itself (e.g. ["a",
 * "b"][0]=="a"). Separate test cases verify that variable access works as
 * expected.
 * 
 * From http://docs.jsonata.org/string-functions.html:
 * 
 * $length(str)
 * 
 * Returns the number of characters in the string str. If str is not specified
 * (i.e. this function is invoked with no arguments), then the context value is
 * used as the value of str. An error is thrown if str is not a string.
 * 
 * Examples
 * 
 * $length("Hello World")==11
 *
 */
@RunWith(Parameterized.class)
public class LengthFunctionTests implements Serializable {

    private static final long serialVersionUID = -170444975332410511L;

    private static final String ERR_MSG_ARG1_BAD_TYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE,
        Constants.FUNCTION_LENGTH);;
    private static final String ERR_MSG_ARG2_BAD_TYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE,
        Constants.FUNCTION_LENGTH);;

    @Parameter(0)
    public String expression;

    @Parameter(1)
    public String expectedResultJsonString;

    @Parameter(2)
    public String expectedRuntimeExceptionMessage;

    @Parameters(name = "{index}: {0} -> {1} ({2})")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            {
                "$length()", null, null
            }, // jsonata 1.8.2 ERR_BAD_CONTEXT }, //
            {
                "$length({})", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$length([])", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$length({\"hello\": 1})", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$length([\"hello\", 1])", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$length(1)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$length(-22.2)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$length(10/3.0)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$length(true)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$length(null)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$length(a.b.c)", null, null
            }, //
            {
                "$length(\"\")", "0", null
            }, //
            {
                "$length(\"Hello World\")", "11", null
            }, //
            {
                "$length(\"abc\",2)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "a.b.c~>$length()", null, null
            }
        });
    }

    @Test
    public void runTest() throws Exception {
        test(this.expression, expectedResultJsonString, expectedRuntimeExceptionMessage, null);
    }
}
