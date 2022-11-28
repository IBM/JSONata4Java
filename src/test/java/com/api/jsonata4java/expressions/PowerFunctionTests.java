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
 * $power(base, exponent)
 * 
 * Returns the value of base raised to the power of exponent (baseexponent).
 * 
 * If base is not specified (i.e. this function is invoked with one argument),
 * then the context value is used as the value of base.
 * 
 * An error is thrown if the values of base and exponent lead to a value that
 * cannot be represented as a JSON number (e.g. Infinity, complex numbers).
 * 
 * Examples
 * 
 * $power(2, 8)==8 $power(2, 0.5)==1.414213562373 $power(2, -2)==0.25
 * 
 */
@RunWith(Parameterized.class)
public class PowerFunctionTests implements Serializable {

    private static final long serialVersionUID = -3750235183291268523L;

    private static final String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT,
        Constants.FUNCTION_POWER);
    private static final String ERR_MSG_ARG1_BAD_TYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE,
        Constants.FUNCTION_POWER);
    private static final String ERR_MSG_ARG2_BAD_TYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE,
        Constants.FUNCTION_POWER);
    private static final String ERR_MSG_ARG3_BAD_TYPE = String.format(Constants.ERR_MSG_ARG3_BAD_TYPE,
        Constants.FUNCTION_POWER);
    private static final String ERR_MSG_NUMBER_OUT_OF_RANGE = String.format(Constants.ERR_MSG_NUMBER_OUT_OF_RANGE,
        "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890.5");
    private static final double base = 9223372036854775809123456789.0;
    private static final double exponent = 9223372036854775809123456789.0;
    private static final String ERR_MSG_POWER_FUNC_NOT_NUMBER = String
        .format(Constants.ERR_MSG_POWER_FUNC_RESULT_NOT_NUMBER, base, exponent);

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
                "$power()", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$power({})", null, ERR_BAD_CONTEXT
            }, //
            {
                "$power({}, 1)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$power(1, {})", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$power([])", null, ERR_BAD_CONTEXT
            }, //
            {
                "$power([], 1)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$power(1, [])", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$power({\"hello\": 1})", null, ERR_BAD_CONTEXT
            }, //
            {
                "$power({\"hello\": 1}, 1)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$power(1, {\"hello\": 1})", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$power([\"hello\", 1])", null, ERR_BAD_CONTEXT
            }, //
            {
                "$power([\"hello\", 1], 1)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$power(1, [\"hello\", 1])", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$power(true)", null, ERR_BAD_CONTEXT
            }, //
            {
                "$power(true, 1)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$power(1, true)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$power(null)", null, ERR_BAD_CONTEXT
            }, //
            {
                "$power(null, 1)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$power(1, null)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$power('foo')", null, ERR_BAD_CONTEXT
            }, //
            {
                "$power('foo', 1)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$power(1, 'foo')", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$power(a.b.c)", null, ERR_BAD_CONTEXT
            }, //
            {
                "$power(1, a.b.c)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$power(a.b.c, 1)", null, null
            }, //
            {
                "$power(0)", null, ERR_BAD_CONTEXT
            }, //
            {
                "$power(0.0)", null, ERR_BAD_CONTEXT
            }, //
            {
                "$power(1)", null, ERR_BAD_CONTEXT
            }, //
            {
                "$power(-1)", null, ERR_BAD_CONTEXT
            }, //
            {
                "$power(123.456)", null, ERR_BAD_CONTEXT
            }, //
            {
                "$power(123.456,2,0)", null, ERR_MSG_ARG3_BAD_TYPE
            }, //
            {
                "$power(0, 2)", Long.toString((long) Math.pow(0, 2)), null
            }, // jsonata 1.8.2 Double.toString(Math.pow(0, 2)), null }, //
            {
                "$power(0.0, 2)", Long.toString((long) Math.pow(0.0, 2)), null
            }, // jsonata 1.8.2 Double.toString(Math.pow(0.0, 2)), null }, //
            {
                "$power(123.456, 2)", Double.toString(Math.pow(123.456, 2)), null
            }, //
            {
                "$power(123.456, -1)", Double.toString(Math.pow(123.456, -1)), null
            }, //
            {
                "$power(123.456, -2)", Double.toString(Math.pow(123.456, -2)), null
            }, //
            {
                "$power(125, -1)", Double.toString(Math.pow(125, -1)), null
            }, //
            {
                "$power(123.456, 0.2)", Double.toString(Math.pow(123.456, 0.2)), null
            }, //
            {
                "$power(123.456, -0.2)", Double.toString(Math.pow(123.456, -0.2)), null
            }, //
            {
                "$power(123.456, 2.5)", Double.toString(Math.pow(123.456, 2.5)), null
            }, //
            {
                "$power(125, -2.5)", Double.toString(Math.pow(125, -2.5)), null
            }, //
            {
                "$power(2147483647, 2)", Long.toString((long) Math.pow(2147483647, 2)), null
            }, // jsonata 1.8.2 Double.toString(Math.pow(2147483647, 2)), null }, //
            {
                "$power(-2147483640, 2)", Long.toString((long) Math.pow(-2147483640, 2)), null
            }, // jsonata 1.8.2 Double.toString(Math.pow(-2147483640, 2)), null }, //
            {
                "$power(21474836471234, 2)", Double.toString(Math.pow(21474836471234D, 2)), null
            }, //
            {
                "$power(-21474836471234, 2)", Double.toString(Math.pow(-21474836471234D, 2)), null
            }, //
            {
                "$power(10/3.0, 1)", Double.toString(Math.pow(10 / 3.0, 1)), null
            }, //
            {
                "$power(-10/3.0, 1)", Double.toString(Math.pow(-10 / 3.0, 1)), null
            }, //
            {
                "$power(1, 10/3.0)", Long.toString((long) Math.pow(1, 10 / 3.0)), null
            }, // jsonata 1.8.2 Double.toString(Math.pow(1, 10 / 3.0)), null }, //
            {
                "$power(1, -10/3.0)", Long.toString((long) Math.pow(1, -10 / 3.0)), null
            }, // jsonata 1.8.2 Double.toString(Math.pow(1, -10 / 3.0)), null }, //
            {
                "$power(9223372036854775807, 2)", Double.toString(Math.pow(9223372036854775807D, 2)), null
            }, //
            {
                "$power(-9223372036854775807, 2)", Double.toString(Math.pow(-9223372036854775807D, 2)), null
            }, //
            {
                "$power(-9223372036854775808, 3)", Double.toString(Math.pow(-9223372036854775808D, 3)), null
            }, //
            {
                "$power(9223372036854775809, 2)", Double.toString(Math.pow(9223372036854775809D, 2)), null
            }, //
            {
                "$power(9223372036854775899.5, 2)", Double.toString(Math.pow(9223372036854775899.5, 2)), null
            }, //
            {
                "$power(-9223372036854775899.5, 2)", Double.toString(Math.pow(-9223372036854775899.5, 2)), null
            }, //
            {
                "$power(9223372036854775809123456789, 2)", //
                Double.toString(Math.pow(9223372036854775809123456789D, 2)), null
            }, //
            {
                "$power(-9223372036854775809123456789, 2)", //
                Double.toString(Math.pow(-9223372036854775809123456789D, 2)), null
            }, //
            {
                "$power(9223372036854775809123456789.1, 2)", //
                Double.toString(Math.pow(9223372036854775809123456789.1, 2)), null
            }, //
            {
                "$power(-9223372036854775809123456789.1, 2)", //
                Double.toString(Math.pow(-9223372036854775809123456789.1, 2)), null
            }, //
            {
                "$power(9223372036854775809123456789, 9223372036854775809123456789)", null, //
                ERR_MSG_POWER_FUNC_NOT_NUMBER
            }, //
            {
                "$power(1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890.5, 2)", //
                null, ERR_MSG_NUMBER_OUT_OF_RANGE
            }
        });
    }

    @Test
    public void runTest() throws Exception {
        test(this.expression, expectedResultJsonString, expectedRuntimeExceptionMessage, null);
    }
}
