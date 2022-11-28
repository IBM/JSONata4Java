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
 * $join(array[, separator])
 * 
 * Joins an array of component strings into a single concatenated string with
 * each component string separated by the optional separator parameter.
 * 
 * It is an error if the input array contains an item which isn’t a string.
 * 
 * If separator is not specified, then it is assumed to be the empty string,
 * i.e. no separator between the component strings. It is an error if separator
 * is not a string.
 * 
 * Examples
 * 
 * $join(['a','b','c'])=="abc" $split("too much, punctuation. hard; to read",
 * /[ ,.;]+/, 3) ~&GT; $join(', ')=="too, much, punctuation"
 * 
 */
@RunWith(Parameterized.class)
public class JoinFunctionTests implements Serializable {

    private static final long serialVersionUID = 80095363933224585L;

    private static final String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_JOIN);
    private static final String ERR_MSG_ARG1_ARR_STR = String.format(Constants.ERR_MSG_ARG1_ARR_STR,
        Constants.FUNCTION_JOIN);
    private static final String ERR_MSG_ARG2_BAD_TYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE,
        Constants.FUNCTION_JOIN);

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
                "$join()", null, ERR_BAD_CONTEXT
            }, //
            {
                "$join({})", null, ERR_MSG_ARG1_ARR_STR
            }, //
            {
                "$join({},  ' ')", null, ERR_MSG_ARG1_ARR_STR
            }, //
            {
                "$join([], {})", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$join({\"hello\": 1})", null, ERR_MSG_ARG1_ARR_STR
            }, //
            {
                "$join({\"hello\": 1}, ' ')", null, ERR_MSG_ARG1_ARR_STR
            }, //
            {
                "$join([], {\"hello\": 1})", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$join([], [])", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$join([], [\"hello\"])", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$join(1)", null, ERR_MSG_ARG1_ARR_STR
            }, //
            {
                "$join(1, ' ')", null, ERR_MSG_ARG1_ARR_STR
            }, //
            {
                "$join([], 1)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$join('1')", "\"1\"", null
            }, // jsonata 1.8.2 null, ERR_MSG_ARG1_ARR_STR }, //
            {
                "$join('1', ' ')", "\"1\"", null
            }, // jsonata 1.8.2 null, ERR_MSG_ARG1_ARR_STR }, //
            {
                "$join(10/3.0)", null, ERR_MSG_ARG1_ARR_STR
            }, //
            {
                "$join(10/3.0, ' ')", null, ERR_MSG_ARG1_ARR_STR
            }, //
            {
                "$join([],  10/3.0)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$join(null)", null, ERR_MSG_ARG1_ARR_STR
            }, //
            {
                "$join(null, ' ')", null, ERR_MSG_ARG1_ARR_STR
            }, //
            {
                "$join([], null)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$join(a.b.c)", null, null
            }, //
            {
                "$join(a.b.c, ' ')", null, null
            }, //
            {
                "$join([], a.b.c)", "\"\"", null
            }, //
            {
                "$join(['f', 'o', 'o'], a.b.c)", "\"foo\"", null
            }, //
            {
                "$join([])", "\"\"", null
            }, //
            {
                "$join([], ' ')", "\"\"", null
            }, //
            {
                "$join(['f', 'o', 'o'], '')", "\"foo\"", null
            }, //
            {
                "$join(['f', 'o', 'o'], ',')", "\"f,o,o\"", null
            }, //
            {
                "$join([\"hello\"], ' ')", "\"hello\"", null
            }
        });
    }

    @Test
    public void runTest() throws Exception {
        test(this.expression, expectedResultJsonString, expectedRuntimeExceptionMessage, null);
    }
}
