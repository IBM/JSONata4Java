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
 * $round(number [, precision])
 * 
 * Returns the value of the number parameter rounded to the number of decimal
 * places specified by the optional precision parameter.
 * 
 * The precision parameter (which must be an integer) species the number of
 * decimal places to be present in the rounded number. If precision is not
 * specified then it defaults to the value 0 and the number is rounded to the
 * nearest integer. If precision is negative, then its value specifies which
 * column to round to on the left side of the decimal place
 * 
 * This function uses the Round half to even strategy to decide which way to
 * round numbers that fall exactly between two candidates at the specified
 * precision. This strategy is commonly used in financial calculations and is
 * the default rounding mode in IEEE 754.
 * 
 * Examples
 * 
 * $round(123.456)==123 $round(123.456, 2)==123.46 $round(123.456, -1) ==
 * 120 $round(123.456, -2)==100 $round(11.5)==12 $round(12.5)==12
 * $round(125, -1)==120
 * 
 */
@RunWith(Parameterized.class)
public class RoundFunctionTests implements Serializable {

    private static final long serialVersionUID = -3165697820916311720L;

    private static final String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT,
        Constants.FUNCTION_ROUND);
    private static final String ERR_MSG_ARG1_BAD_TYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE,
        Constants.FUNCTION_ROUND);
    private static final String ERR_MSG_ARG2_BAD_TYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE,
        Constants.FUNCTION_ROUND);
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
                "$round()", null, ERR_BAD_CONTEXT
            }, //
            {
                "$round(a.b.c)", null, null
            }, //
            {
                "$round({})", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            //	TODO Issue #71			{ "$round({}, // 1)", null, ERR_MSG_ARG1_BAD_TYPE }, //
            {
                "$round(1, {})", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$round([])", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$round([], 1)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$round(1, [])", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$round({\"hello\": 1})", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            //	TODO Issue #71				{ "$round({\"hello\": 1}, // 1)", null, ERR_MSG_ARG1_BAD_TYPE }, //
            {
                "$round(1, {\"hello\": 1})", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$round([\"hello\", 1])", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$round([\"hello\", 1], 1)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$round(1, [\"hello\", 1])", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$round(true)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$round(true, 1)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$round(1, true)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$round(null)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$round(null, 1)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$round(1, null)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$round('foo')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$round('foo', 1)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$round(1, 'foo')", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$round(a.b.c)", null, null
            }, //
            {
                "$round(a.b.c, 1)", null, null
            }, //
            {
                "$round(1, a.b.c)", "1", null
            }, //
            {
                "$round(1)", "1", null
            }, //
            {
                "$round(-1)", "-1", null
            }, //
            {
                "$round(123.456)", "123", null
            }, //
            {
                "$round(123.456, 2)", "123.46", null
            }, //
            {
                "$round(123.456, -1)", "120", null
            }, //
            {
                "$round(123.456, -2)", "100", null
            }, //
            {
                "$round(11.5)", "12", null
            }, //
            {
                "$round(12.5)", "12", null
            }, //
            {
                "$round(125, -1)", "120", null
            }, //
            {
                "$round(2147483647)", "2147483647", null
            }, //
            {
                "$round(-2147483640)", "-2147483640", null
            }, //
            {
                "$round(21474836471234)", "21474836471234", null
            }, //
            {
                "$round(-21474836471234)", "-21474836471234", null
            }, //
            {
                "$round(0)", "0", null
            }, //
            {
                "$round(0.0)", "0", null
            }, //
            {
                "$round(1.0)", "1", null
            }, //
            {
                "$round(-1.0)", "-1", null
            }, //
            {
                "$round(1.23456)", "1", null
            }, //
            {
                "$round(-1.23456)", "-1", null
            }, //
            {
                "$round(1.234567890123)", "1", null
            }, //
            {
                "$round(-1.234567890123)", "-1", null
            }, //
            {
                "$round(10/3.0)", "3", null
            }, //
            {
                "$round(-10/3.0)", "-3", null
            }, //
            {
                "$round(9223372036854775807)", "9223372036854775807", null
            }, // // Long.MAX_VALUE
            {
                "$round(-9223372036854775808)", Long.toString(Long.MIN_VALUE), null
            }, // jsonata 1.8.2 "-9.223372036854776E18", null }, // // Long.MIN_VALUE
            {
                "$round(9223372036854775809)", Long.toString(Long.MAX_VALUE), null
            }, // jsonata 1.8.2 "9.223372036854776E18", null }, //
            {
                "$round(9223372036854775899.5)", Long.toString(Long.MAX_VALUE), null
            }, // jsonata 1.8.2 "9.223372036854776E18", null }, //
            {
                "$round(-9223372036854775899.5)", Long.toString(Long.MIN_VALUE), null
            }, // jsonata 1.8.2 "-9.223372036854776E18", null }, //
            {
                "$round(9223372036854775809123456789)", "9.223372036854776E27", null
            }, //
            {
                "$round(-9223372036854775809123456789)", "-9.223372036854776E27", null
            }, //
            {
                "$round(9223372036854775809123456789.1)", "9.223372036854776E27", null
            }, //
            {
                "$round(-9223372036854775809123456789.1)", "-9.223372036854776E27", null
            }, //
            {
                "$round(9223372036854775809123456789.3)", "9.223372036854776E27", null
            }, //
            {
                "$round(-9223372036854775809123456789.3)", "-9.223372036854776E27", null
            }, //
            {
                "$round(9223372036854775809123456789.5)", "9.223372036854776E27", null
            }, //
            {
                "$round(-9223372036854775809123456789.5)", "-9.223372036854776E27", null
            }, //
            {
                "$round(9223372036854775809123456789.7)", "9.223372036854776E27", null
            }, //
            {
                "$round(-9223372036854775809123456789.7)", "-9.223372036854776E27", null
            }, //
            {
                "$round(9223372036854775809123456789.9)", "9.223372036854776E27", null
            }, //
            {
                "$round(-9223372036854775809123456789.9)", "-9.223372036854776E27", null
            }, //
            {
                "$round(1234567890123456789012.5)", "1.2345678901234568E21", null
            }, //
            {
                "$round(1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890.5)", //
                null, ERR_MSG_NUMBER_OUT_OF_RANGE
            }
        });
    }

    @Test
    public void runTest() throws Exception {
        try {
            test(this.expression, expectedResultJsonString, expectedRuntimeExceptionMessage, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
