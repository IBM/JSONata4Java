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
 * http://docs.jsonata.org/numeric-functions.html
 * 
 * $sqrt(number)
 * 
 * Returns the square root of the value of the number parameter.
 * 
 * If number is not specified (i.e. this function is invoked with one argument),
 * then the context value is used as the value of number.
 * 
 * An error is thrown if the value of number is negative.
 * 
 * Examples
 * 
 * $sqrt(4)==2 $sqrt(2)==1.414213562373
 * 
 */
@RunWith(Parameterized.class)
public class SqrtFunctionTests implements Serializable {

    private static final long serialVersionUID = -5513416069993702347L;

    private static final String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_SQRT);
    private static final String ERR_MSG_ARG1_BAD_TYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE,
        Constants.FUNCTION_SQRT);
    private static final String ERR_MSG_FUNC_CANNOT_BE_APPLIED = String
        .format(Constants.ERR_MSG_FUNC_CANNOT_BE_APPLIED_NEG_NUM, Constants.FUNCTION_SQRT, "-1.0");
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
                "$sqrt()", null, ERR_BAD_CONTEXT
            }, //
            {
                "$sqrt({})", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$sqrt([])", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$sqrt('1')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$sqrt(true)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$sqrt(null)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$sqrt(-1)", null, ERR_MSG_FUNC_CANNOT_BE_APPLIED
            }, //
            {
                "$sqrt(a.b.c)", null, null
            }, //
            {
                "$sqrt(1)", "1", null
            }, // jsonata 1.8.2 Double.toString(Math.sqrt(1)), null }, //
            {
                "$sqrt(2147483647)", Double.toString(Math.sqrt(2147483647)), null
            }, //
            {
                "$sqrt(21474836471234)", Double.toString(Math.sqrt(21474836471234D)), null
            }, //
            {
                "$sqrt(1.0)", "1", null
            }, // jsonata 1.8.2 Double.toString(Math.sqrt(1.0)), null }, //
            {
                "$sqrt(1.23456)", Double.toString(Math.sqrt(1.23456)), null
            }, //
            {
                "$sqrt(1.234567890123)", Double.toString(Math.sqrt(1.234567890123)), null
            }, //
            {
                "$sqrt(10/3.0)", Double.toString(Math.sqrt(10 / 3.0)), null
            }, //
            {
                "$sqrt(9223372036854775807)", Double.toString(Math.sqrt(Long.MAX_VALUE)), null
            }, // // Long.MAX_VALUE
            {
                "$sqrt(9223372036854775809)", Double.toString(Math.sqrt(9223372036854775809D)), null
            }, //
            {
                "$sqrt(9223372036854775899.5)", Double.toString(Math.sqrt(9223372036854775899.5)), null
            }, //
            {
                "$sqrt(9223372036854775809123456789)", Double.toString(Math.sqrt(9223372036854775809123456789D)), //
                null
            }, //
            {
                "$sqrt(9223372036854775809123456789.5)", Double.toString(Math.sqrt(9223372036854775809123456789.5)), //
                null
            }, //
            {
                "$sqrt(1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890)",
                null, ERR_MSG_NUMBER_OUT_OF_RANGE
            } //
        });
    }

    @Test
    public void runTest() throws Exception {
        test(this.expression, expectedResultJsonString, expectedRuntimeExceptionMessage, null);
    }
}
