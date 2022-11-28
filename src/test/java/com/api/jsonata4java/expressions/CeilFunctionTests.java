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
 * $ceil(number)
 * 
 * Returns the value of number rounded up to the nearest integer that is greater than or equal to number.
 * 
 * If number is not specified (i.e. this function is invoked with no arguments), then the context value is used as the
 * value of number.
 * 
 * Examples 
 * 
 * $ceil(5)==5
 * $ceil(5.3)==6
 * $ceil(5.8)==6
 * $ceil(-5.3)==-5
 *  
 */
@RunWith(Parameterized.class)
public class CeilFunctionTests implements Serializable {

    private static final long serialVersionUID = 2780892287570157399L;

    // private static final String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_CEIL);
    private static final String ERR_MSG_ARG1_BAD_TYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_CEIL);
    private static final String ERR_MSG_ARG2_BAD_TYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_CEIL);
    private static final String ERR_MSG_NUMBER_OUT_OF_RANGE = String.format(Constants.ERR_MSG_NUMBER_OUT_OF_RANGE,
        "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890.5");

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
                "$ceil()", null, null
            }, // jsonata 1.8.2 ERR_BAD_CONTEXT}, //
            {
                "$ceil({})", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$ceil([])", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$ceil('1')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$ceil(true)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$ceil(null)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$ceil(a.b.c)", null, null
            }, //
            {
                "a.b.c~>$ceil()", null, null
            }, //
            {
                "$ceil(1)", Long.toString(1), null
            }, //
            {
                "$ceil(-1)", Long.toString(-1), null
            }, //
            {
                "$ceil(2147483647)", Long.toString(2147483647), null
            }, //
            {
                "$ceil(-2147483640)", Long.toString(-2147483640), null
            }, //
            {
                "$ceil(21474836471234)", Long.toString((long) Math.ceil(21474836471234D)), null
            }, //
            {
                "$ceil(-21474836471234)", Long.toString((long) Math.ceil(-21474836471234D)), null
            }, //
            {
                "$ceil(0)", Long.toString(0), null
            }, //
            {
                "$ceil(0.0)", Long.toString((long) Math.ceil(0.0)), null
            }, //
            {
                "$ceil(1.0)", Long.toString((long) Math.ceil(1.0)), null
            }, //
            {
                "$ceil(-1.0)", Long.toString((long) Math.ceil(-1.0)), null
            }, //
            {
                "$ceil(1.23456)", Long.toString((long) Math.ceil(1.23456)), null
            }, //
            {
                "$ceil(-1.23456)", Long.toString((long) Math.ceil(-1.23456)), null
            }, //
            {
                "$ceil(1.234567890123)", Long.toString((long) Math.ceil(1.234567890123)), null
            }, //
            {
                "$ceil(-1.234567890123)", Long.toString((long) Math.ceil(-1.234567890123)), null
            }, //
            {
                "$ceil(10/3.0)", Long.toString((long) Math.ceil(10 / 3.0)), null
            }, //
            {
                "$ceil(-10/3.0)", Long.toString((long) Math.ceil(-10 / 3.0)), null
            }, //
            {
                "$ceil(9223372036854775807)", Long.toString((long) Math.ceil(9223372036854775807D)), null
            }, // // Long.MAX_VALUE
            {
                "$ceil(-9223372036854775808)", Long.toString((long) Math.ceil(-9223372036854775808D)), null
            }, // // Long.MIN_VALUE
            {
                "$ceil(9223372036854775809)", Long.toString((long) Math.ceil(9223372036854775809D)), null
            }, //
            {
                "$ceil(9223372036854775899.5)", Long.toString((long) Math.ceil(9223372036854775899.5)), null
            }, //
            {
                "$ceil(9223372036854775809123456789)", Long.toString((long) Math.ceil(9223372036854775809123456789D)), null
            }, //
            {
                "$ceil(-9223372036854775809123456789)", Long.toString((long) Math.ceil(-9223372036854775809123456789D)), null
            }, //
            {
                "$ceil(-9223372036854775899.5)", Long.toString((long) Math.ceil(-9223372036854775899.5)), null
            }, //
            {
                "$ceil(9223372036854775809123456789.5)", Long.toString((long) Math.ceil(9223372036854775809123456789.5)), null
            }, //
            {
                "$ceil(-9223372036854775809123456789.5)", Long.toString((long) Math.ceil(-9223372036854775809123456789.5)), null
            }, //
            {
                "$ceil(1234567890123456789012.5)", Long.toString((long) Math.ceil(1234567890123456789012.5)), null
            }, //
            {
                "$ceil(1,2)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$ceil(1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890.5)",
                null, ERR_MSG_NUMBER_OUT_OF_RANGE
            }
        });
    }

    @Test
    public void runTest() throws Exception {
        test(this.expression, expectedResultJsonString, expectedRuntimeExceptionMessage, null);
    }
}
