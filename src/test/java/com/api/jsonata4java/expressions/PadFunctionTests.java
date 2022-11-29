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
 * $pad(str, width [, char])
 * 
 * Returns a copy of the string str with extra padding, if necessary, so that
 * its total number of characters is at least the absolute value of the width
 * parameter. If width is a positive number, then the string is padded to the
 * right; if negative, it is padded to the left. The optional char argument
 * specifies the padding character(s) to use. If not specified, it defaults to
 * the space character.
 * 
 * Examples
 * 
 * $pad("foo", 5)=="foo " $pad("foo", -5)==" foo" $pad("foo", -5, "#") ==
 * "##foo" $formatBase(35, 2) ~&GT; $pad(-8, '0')=="00100011"
 *
 */
@RunWith(Parameterized.class)
public class PadFunctionTests implements Serializable {

    private static final long serialVersionUID = 1038258856595831073L;

    private static final String ERR_MSG_ARG1_BAD_TYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE,
        Constants.FUNCTION_PAD);
    private static final String ERR_MSG_ARG2_BAD_TYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE,
        Constants.FUNCTION_PAD);
    private static final String ERR_MSG_ARG3_BAD_TYPE = String.format(Constants.ERR_MSG_ARG3_BAD_TYPE,
        Constants.FUNCTION_PAD);

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
                "$pad()", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$pad({})", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$pad({}, 5)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$pad(' ', {})", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$pad(' ', 5, {})", null, ERR_MSG_ARG3_BAD_TYPE
            }, //
            {
                "$pad([])", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$pad([], 5)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$pad(' ', [])", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$pad(' ', 5, [])", null, ERR_MSG_ARG3_BAD_TYPE
            }, //
            {
                "$pad({\"hello\": 1})", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$pad({\"hello\": 1}, 5)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$pad(' ', {\"hello\": 1})", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$pad(' ', 5, {\"hello\": 1})", null, ERR_MSG_ARG3_BAD_TYPE
            }, //
            {
                "$pad([\"hello\", 1])", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$pad([\"hello\", 1], 5)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$pad(' ', [\"hello\", 1])", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$pad(' ', 5, [\"hello\", 1])", null, ERR_MSG_ARG3_BAD_TYPE
            }, //
            {
                "$pad(true)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$pad(true, 5)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$pad(' ', true)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$pad(' ', 5, true)", null, ERR_MSG_ARG3_BAD_TYPE
            }, //
            {
                "$pad(null)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$pad(null, 5)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$pad(' ', null)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$pad(' ', 5, null)", null, ERR_MSG_ARG3_BAD_TYPE
            }, //
            {
                "$pad(5)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$pad(5, 5)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$pad(' ', 5, 5)", null, ERR_MSG_ARG3_BAD_TYPE
            }, //
            {
                "$pad(-5)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$pad(-5, 5)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$pad(' ', 5, -5)", null, ERR_MSG_ARG3_BAD_TYPE
            }, //
            {
                "$pad(10/3.0)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$pad(10/3.0, 5)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$pad(' ', 5, 10/3.0)", null, ERR_MSG_ARG3_BAD_TYPE
            }, //
            {
                "$pad(' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$pad(' ', ' ')", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$pad(-22.2)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$pad(a.b.c)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$pad(a.b.c, 5)", null, null
            }, //
            {
                "$pad('foo', 5)", "\"foo  \"", null
            }, //
            {
                "$pad('foo', 5, 2)", null, ERR_MSG_ARG3_BAD_TYPE
            }, //
            {
                "$pad('foo', 5, '#')", "\"foo##\"", null
            }, //
            {
                "$pad('foo', 10, '#$%^&')", "\"foo#$%^&#$\"", null
            }, //
            {
                "$pad('foo', -5)", "\"  foo\"", null
            }, //
            {
                "$pad('foo', -5, '#')", "\"##foo\"", null
            }, //
            {
                "$pad('foo', -10, '#$%^&')", "\"#$%^&#$foo\"", null
            }
        });
    }

    @Test
    public void runTest() throws Exception {
        test(this.expression, expectedResultJsonString, expectedRuntimeExceptionMessage, null);
    }
}
