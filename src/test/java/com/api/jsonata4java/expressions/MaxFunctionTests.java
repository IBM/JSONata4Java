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
 * From http://docs.jsonata.org/aggregation-functions.html
 * 
 * $max(array)
 * 
 * Returns the maximum number in an array of numbers. It is an error if the
 * input array contains an item which isn’t a number.
 * 
 * Example
 * 
 * $max([5,1,3,7,4])==7
 * 
 */
@RunWith(Parameterized.class)
public class MaxFunctionTests implements Serializable {

    private static final long serialVersionUID = 6903650980833036471L;

    private static final String ERR_MSG_ARG1_BAD_TYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE,
        Constants.FUNCTION_MAX);
    private static final String ERR_MSG_ARG1_ARR_TYPE = String.format(Constants.ERR_MSG_ARG1_MUST_BE_ARRAY_OF_NUMBER,
        Constants.FUNCTION_MAX);
    private static final String ERR_MSG_NUMBER_OUT_OF_RANGE = String.format(Constants.ERR_MSG_NUMBER_OUT_OF_RANGE,
        "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");

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
                "$max()", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$max({})", null, ERR_MSG_ARG1_ARR_TYPE
            }, //
            {
                "$max('1')", null, ERR_MSG_ARG1_ARR_TYPE
            }, //
            {
                "$max(true)", null, ERR_MSG_ARG1_ARR_TYPE
            }, //
            {
                "$max(null)", null, ERR_MSG_ARG1_ARR_TYPE
            }, //
            {
                "$max(1)", "1", null
            }, // jsonata 1.8.2 null, ERR_MSG_ARG1_ARR_TYPE }, //
            {
                "$max(-1)", "-1", null
            }, // jsonata 1.8.2 null, ERR_MSG_ARG1_ARR_TYPE }, //
            {
                "$max(1.0)", "1", null
            }, // jsonata 1.8.2 null, ERR_MSG_ARG1_ARR_TYPE }, //
            {
                "$max(-1.0)", "-1", null
            }, // jsonata 1.8.2 null, ERR_MSG_ARG1_ARR_TYPE }, //
            {
                "$max([])", null, null
            }, // jsonata 1.8.2 null, ERR_MSG_ARG1_ARR_TYPE }, //
            {
                "$max([1, {}])", null, ERR_MSG_ARG1_ARR_TYPE
            }, //
            {
                "$max([1, []])", null, ERR_MSG_ARG1_ARR_TYPE
            }, //
            {
                "$max([1, 'foo'])", null, ERR_MSG_ARG1_ARR_TYPE
            }, //
            {
                "$max([1, true])", null, ERR_MSG_ARG1_ARR_TYPE
            }, //
            {
                "$max([1, null])", null, ERR_MSG_ARG1_ARR_TYPE
            }, //
            {
                "$max(a.b.c)", null, null
            }, //
            {
                "$max([5,1,3,7,4])", "7", null
            }, //
            {
                "$max([5.2 , 1.0, 3 , 7, 4])", "7", null
            }, //
            {
                "$max([5.2 , 1.0, 3 , 7.0, 4])", "7", null
            }, // jsonata 1.8.2 "7.0", null }, //
            {
                "$max([10/3.0, 1, 3])", Double.toString(10 / 3.0), null
            }, //
            {
                "$max([9223372036854775807])", Long.toString(Long.MAX_VALUE), null
            }, // // Long.MAX_VALUE
            {
                "$max([-9223372036854775808])", Long.toString(Long.MIN_VALUE), null
            }, // jsonata 1.8.2 Double.toString(Long.MIN_VALUE), null }, // // Long.MIN_VALUE
            {
                "$max([9223372036854775809])", Long.toString(Long.MAX_VALUE), null
            }, // jsonata 1.8.2Double.toString(9223372036854775809D), null }, //
            {
                "$max([9223372036854775899.5])", Long.toString(Long.MAX_VALUE), null
            }, // jsonata 1.8.2 Double.toString(9223372036854775899.5), null }, //
            {
                "$max([9223372036854775809123456789])", Double.toString(9223372036854775809123456789D), null
            }, //
            {
                "$max([9223372036854775809123456789.5])", Double.toString(9223372036854775809123456789.5), null
            }, //
            {
                "$max([1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890])",
                null, ERR_MSG_NUMBER_OUT_OF_RANGE
            }, //
        });
    }

    @Test
    public void runTest() throws Exception {
        test(this.expression, expectedResultJsonString, expectedRuntimeExceptionMessage, null);
    }
}
