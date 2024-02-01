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
 * $toMillis(timestamp)
 * 
 * Convert a timestamp string in the ISO 8601 format to the number of
 * milliseconds since the Unix Epoch (1 January, 1970 UTC) as a number.
 * 
 * An error is thrown if the string is not in the correct format.
 * 
 * Examples
 * 
 * $toMillis("2017-11-07T15:07:54.972Z")==1510067274972
 * 
 */
@RunWith(Parameterized.class)
public class ToMillisFunctionTests implements Serializable {

    private static final long serialVersionUID = -5047961727712114236L;

    private static final String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT,
        Constants.FUNCTION_TO_MILLIS);
    private static final String ERR_MSG_ARG1_BAD_TYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE,
        Constants.FUNCTION_TO_MILLIS);
    private static final String ERR_MSG_TO_MILLIS_ISO_8601_FORMAT_FOO = String
        .format(Constants.ERR_MSG_TO_MILLIS_ISO_8601_FORMAT, "foo");

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
                "$toMillis()", null, ERR_BAD_CONTEXT
            }, //
            {
                "$toMillis(1)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$toMillis(-1)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$toMillis({})", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$toMillis({\"hello\": 1})", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$toMillis([])", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$toMillis([\"hello\", 1])", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$toMillis(true)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$toMillis(null)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$toMillis('foo')", null, ERR_MSG_TO_MILLIS_ISO_8601_FORMAT_FOO
            }, //
            {
                "$toMillis(a.b.c)", null, null
            }, //
            {
                "$toMillis('2018-01-22T10:02:09.240Z')", "1516615329240", null
            }, //
            {
                "$toMillis('2018-01-22T10:02:09.240+01:00')", "1516611729240", null
            }, //
            {
                "$toMillis('2018-01-22T10:02:09.240-01:00')", "1516618929240", null
            }, //
            {
                "$toMillis('2018-01-22T10:02:09.240+10:00')", "1516579329240", null
            }, //
            {
                "$toMillis('2018-01-22T10:02:09.240-10:00')", "1516651329240", null
            },
            {
                "$toMillis('2018', '[Y1]')", "1514764800000", null
            },
            {
                "$toMillis('2018-03-27', '[Y1]-[M01]-[D01]')", "1522108800000", null
            },
            {
                "$toMillis('2018-03-27T14:03:00.123Z', '[Y0001]-[M01]-[D01]T[H01]:[m01]:[s01].[f001]Z')", "1522159380123", null
            },
            {
                "$toMillis('27th 3 1976', '[D1o] [M#1] [Y0001]')", "196732800000", null
            },
            {
                "$toMillis('21st 12 1881', '[D1o] [M01] [Y0001]')", "-2777932800000", null
            },
            {
                "$toMillis('2nd 12 2012', '[D1o] [M01] [Y0001]')", "1354406400000", null
            },
            {
                "$toMillis('MCMLXXXIV', '[YI]')", "441763200000", null
            },
            {
                "$toMillis('27 03 MMXVIII', '[D1] [M01] [YI]')", "1522108800000", null
            },
            {
                "$toMillis('27 iii MMXVIII', '[D1] [Mi] [YI]')", "1522108800000", null
            },
            {
                "$toMillis('w C mmxviii', '[Da] [MA] [Yi]')", "1521763200000", null
            },
            {
                "$toMillis('ae C mmxviii', '[Da] [MA] [Yi]')", "1522454400000", null
            },
            {
                "$toMillis('27th April 2008', '[D1o] [MNn] [Y0001]')", "1209254400000", null
            },
            {
                "$toMillis('21 August 2017', '[D1] [MNn] [Y0001]')", "1503273600000", null
            },
            {
                "$toMillis('2 Feb 2012', '[D1] [MNn,3-3] [Y0001]')", "1328140800000", null
            },
            {
                "$toMillis('one thousand, nine hundred and eighty-four', '[Yw]')", "441763200000", null
            },
            {
                "$toMillis('nineteen hundred and eighty-four', '[Yw]')", "441763200000", null
            },
            {
                "$toMillis('twenty-seven April 2008', '[Dw] [MNn] [Y0001]')", "1209254400000", null
            },
            {
                "$toMillis('twenty-seventh April 2008', '[Dw] [MNn] [Y0001]')", "1209254400000", null
            },
            {
                "$toMillis('twenty-first August two thousand and seventeen', '[Dw] [MNn] [Yw]')", "1503273600000", null
            },
            {
                "$toMillis('TWENTY-SECOND August two thousand and seventeen', '[DW] [MNn] [Yw]')", "1503360000000", null
            },
            {
                "$toMillis('Twentieth of August, two thousand and seventeen', '[DW] of [MNn], [Yw]')", "1503187200000", null
            },
            {
                "$toMillis('4/4/2018 12:06 am', '[D1]/[M1]/[Y0001] [h]:[m] [P]')", "1522800360000", null
            },
            {
                "$toMillis('4/4/2018 06:30 am', '[D1]/[M1]/[Y0001] [h]:[m] [P]')", "1522823400000", null
            },
            {
                "$toMillis('4/4/2018 12:06 pm', '[D1]/[M1]/[Y0001] [h]:[m] [P]')", "1522843560000", null
            },
            {
                "$toMillis('4/4/2018 11:30 pm', '[D1]/[M1]/[Y0001] [h]:[m] [P]')", "1522884600000", null
            },
            {
                "$toMillis('2018-094', '[Y0001]-[d001]')", "1522800000000", null
            },
            {
                "($toMillis('13:45', '[H]:[m]') ~> $fromMillis() ~> $substringBefore('T')) = $substringBefore($now(), 'T')", "true", null
            },
            {
                "$toMillis('13:45', '[H]:[m]') ~> $fromMillis() ~> $substringAfter('T')", "\"13:45:00.000Z\"", null
            },
            {
                "$toMillis('Wednesday, 14th November 2018', '[FNn], [D1o] [MNn] [Y]') ~> $fromMillis()", "\"2018-11-14T00:00:00.000Z\"", null
            },
            {
                "$toMillis('Mon, Twelfth November 2018', '[FNn,*-3], [DWwo] [MNn] [Y]') ~> $fromMillis()", "\"2018-11-12T00:00:00.000Z\"", null
            },
            {
                "$toMillis('2018--180', '[Y]--[d]') ~> $fromMillis()", "\"2018-06-29T00:00:00.000Z\"", null
            },
            {
                "$toMillis('three hundred and sixty-fifth day of 2018', '[dwo] day of [Y]') ~> $fromMillis()", "\"2018-12-31T00:00:00.000Z\"", null
            },
            {
                "$toMillis('irrelevant string', '[Y]-[M]-[D]')", null, null
            },
            {
                "$toMillis('2018-05-22', '[Y]-[M]-[q]')", null, String.format(Constants.ERR_MSG_UNKNOWN_COMPONENT_SPECIFIER, 'q')
            },
            {
                "$toMillis('2018-05-22', '[YN]-[M]-[D]')", null, String.format(Constants.ERR_MSG_INVALID_NAME_MODIFIER, 'Y')
            },
            {
                "$toMillis('2018-22', '[Y]-[D]')", null, Constants.ERR_MSG_MISSING_FORMAT
            },
            {
                "$toMillis('5-22 23:59', '[M]-[D] [m]:[s]')", null, Constants.ERR_MSG_MISSING_FORMAT
            },
            {
                "$toMillis('2018-3-2-5', '[X]-[x]-[w]-[F1]')", null, Constants.ERR_MSG_MISSING_FORMAT
            },
            {
                "$toMillis('2018-32-5', '[X]-[W]-[F1]')", null, Constants.ERR_MSG_MISSING_FORMAT
            },
            {
                "$toMillis('2020-09-09 08:00:00 +02:00', '[Y0001]-[M01]-[D01] [H01]:[m01]:[s01] [Z]') ~> $fromMillis()", "\"2020-09-09T06:00:00.000Z\"", null
            },
            {
                "$toMillis('2020-09-09 08:00:00 GMT-05:00', '[Y0001]-[M01]-[D01] [H01]:[m01]:[s01] [z]') ~> $fromMillis()", "\"2020-09-09T13:00:00.000Z\"", null
            },
            {
                "$toMillis('2020-09-09 12:00:00 +05:30', '[Y0001]-[M01]-[D01] [H01]:[m01]:[s01] [Z]') ~> $fromMillis()", "\"2020-09-09T06:30:00.000Z\"", null
            },
            {
                "$toMillis('2020-09-09 12:00:00 GMT-5', '[Y0001]-[M01]-[D01] [H01]:[m01]:[s01] [z01]') ~> $fromMillis()", "\"2020-09-09T17:00:00.000Z\"", null
            },
            {
                "$toMillis('2020-09-09 12:00:00 +0530', '[Y0001]-[M01]-[D01] [H01]:[m01]:[s01] [Z0001]') ~> $fromMillis()", "\"2020-09-09T06:30:00.000Z\"", null
            },
        });
    }

    @Test
    public void runTest() throws Exception {
        test(this.expression, expectedResultJsonString, expectedRuntimeExceptionMessage, null);
    }
}
