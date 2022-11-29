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
 * Casts the arg parameter to a number using the following casting rules:
 * 
 * - Numbers are unchanged - Strings that contain a sequence of characters that
 * represent a legal JSON number are converted to that number - All other values
 * cause an error to be thrown.
 * 
 * If arg is not specified (i.e. this function is invoked with no arguments),
 * then the context value is used as the value of arg.
 * 
 * Examples
 * 
 * $number("5")==5 ["1", "2", "3", "4", "5"].$number()==[1, 2, 3, 4, 5]
 * 
 */
@RunWith(Parameterized.class)
public class NumberFunctionTests implements Serializable {

    private static final long serialVersionUID = -3074768813815418777L;

    private static final String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT,
        Constants.FUNCTION_NUMBER);
    private static final String ERR_MSG_ARG1_BAD_TYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE,
        Constants.FUNCTION_NUMBER);
    private static final String ERR_MSG_ARG2_BAD_TYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE,
        Constants.FUNCTION_NUMBER);
    private static final String ERR_MSG_UNABLE_TO_CAST_FOO = String
        .format(Constants.ERR_MSG_UNABLE_TO_CAST_VALUE_TO_NUMBER, "foo");
    private static final String ERR_MSG_UNABLE_TO_CAST_EXP = String
        .format(Constants.ERR_MSG_UNABLE_TO_CAST_VALUE_TO_NUMBER, "10/3.0");
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
                "$number()", null, ERR_BAD_CONTEXT
            }, //
            {
                "$number({})", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$number({\"hello\": 1})", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$number([])", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$number([\"hello\", 1])", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$number('foo')", null, ERR_MSG_UNABLE_TO_CAST_FOO
            }, //
            {
                "$number(true)", "1", null
            }, // jsonata 1.8.2 null, ERR_MSG_ARG1_BAD_TYPE }, //
            {
                "$number(null)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$number('10/3.0')", null, ERR_MSG_UNABLE_TO_CAST_EXP
            }, //
            {
                "$number(a.b.c)", null, null
            }, //
            {
                "$number(1)", "1", null
            }, //
            {
                "$number(-22.2)", "-22.2", null
            }, //
            {
                "$number(10/3.0)", "3.3333333333333335", null
            }, //
            {
                "$number(10/3)", "3.3333333333333335", null
            }, //
            {
                "$number(0)", "0", null
            }, //
            {
                "$number(0.0)", "0", null
            }, // jsonata 1.8.2 "0.0", null }, //
            {
                "$number('0')", "0", null
            }, //
            {
                "$number('0.0')", "0", null
            }, // jsonata 1.8.2 "0.0", null }, //
            {
                "$number('1')", "1", null
            }, //
            {
                "$number('1',\"a\")", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$number('1.0')", "1", null
            }, // jsonata 1.8.2 "1.0", null }, //
            {
                "$number('1.00000')", "1", null
            }, // jsonata 1.8.2 "1.00000", null }, //
            {
                "$number('-22.2')", "-22.2", null
            }, //
            {
                "$number(9223372036854775807)", "9223372036854775807", null
            }, // // Long.MAX_VALUE
            {
                "$number(-9223372036854775808)", "-9223372036854775808", null
            }, // jsonata 1.8.2 "-9.223372036854776E18", null }, // // Long.MIN_VALUE
            {
                "$number('9223372036854775807')", "9223372036854775807", null
            }, // // Long.MAX_VALUE
            {
                "$number('-9223372036854775808')", "-9223372036854775808", null
            }, // // Long.MIN_VALUE
            {
                "$number(9223372036854775809)", "9223372036854775807", null
            }, // jsonata 1.8.2 "9.223372036854776E18", null }, //
            {
                "$number(-9223372036854775809)", "-9223372036854775808", null
            }, // jsonata 1.8.2 "-9.223372036854776E18", null }, //
            {
                "$number('9223372036854775809')", "9223372036854775807", null
            }, // jsonata 1.8.2 "9.223372036854776E18", null }, //
            {
                "$number('-9223372036854775809')", "-9223372036854775808", null
            }, // jsonata 1.8.2 "-9.223372036854776E18", null }, //
            {
                "$number(9223372036854775899.5)", "9223372036854775807", null
            }, // jsonata 1.8.2 "9.223372036854776E18", null }, //
            {
                "$number(-9223372036854775899.5)", "-9223372036854775808", null
            }, // jsonata 1.8.2 "-9.223372036854776E18", null }, //
            {
                "$number('9223372036854775899.5')", "9223372036854775807", null
            }, // jsonata 1.8.2 "9.223372036854776E18", null }, //
            {
                "$number('-9223372036854775899.5')", "-9223372036854775808", null
            }, // jsonata 1.8.2 "-9.223372036854776E18", null }, //
            {
                "$number(9223372036854775809123456789)", "9.223372036854776E27", null
            }, //
            {
                "$number(-9223372036854775809123456789)", "-9.223372036854776E27", null
            }, //
            {
                "$number('9223372036854775809123456789')", "9.223372036854776E27", null
            }, //
            {
                "$number('-9223372036854775809123456789')", "-9.223372036854776E27", null
            }, //
            {
                "$number(9223372036854775809123456789.5)", "9223372036854775809123456789.5", null
            }, //
            {
                "$number(-9223372036854775809123456789.5)", "-9223372036854775809123456789.5", null
            }, //
            {
                "$number('9223372036854775809123456789.5')", "9223372036854775809123456789.5", null
            }, //
            {
                "$number('-9223372036854775809123456789.5')", "-9223372036854775809123456789.5", null
            }, //
            {
                "$number(1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890)",
                null, ERR_MSG_NUMBER_OUT_OF_RANGE
            }
        });
    }

    @Test
    public void runTest() throws Exception {
        test(this.expression, expectedResultJsonString, expectedRuntimeExceptionMessage, null);
    }
}
