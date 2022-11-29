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
 * $fromMillis(number)
 * 
 * Convert a number representing milliseconds since the Unix Epoch (1 January,
 * 1970 UTC) to a timestamp string in the ISO 8601 format.
 * 
 * Examples
 * 
 * $fromMillis(1510067557121)=="2017-11-07T15:12:37.121Z"
 *
 */
@RunWith(Parameterized.class)
public class FromMillisFunctionTests implements Serializable {

    private static final long serialVersionUID = 1715600213181460445L;

    private static final String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT,
        Constants.FUNCTION_FROM_MILLIS);
    private static final String ERR_MSG_ARG1_BAD_TYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE,
        Constants.FUNCTION_FROM_MILLIS);

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
                "$fromMillis()", null, ERR_BAD_CONTEXT
            }, //
            {
                "$fromMillis({})", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$fromMillis([])", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$fromMillis('1')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$fromMillis(true)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$fromMillis(null)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$fromMillis(a.b.c)", null, null
            }, //
            {
                "$fromMillis(1512159584136)", "\"2017-12-01T20:19:44.136Z\"", null
            }, //
            {
                "$fromMillis(1)", "\"1970-01-01T00:00:00.001Z\"", null
            }, //
            {
                "$fromMillis(-1)", "\"1969-12-31T23:59:59.999Z\"", null
            }, //
            {
                "$fromMillis(10/3.0)", "\"1970-01-01T00:00:00.003Z\"", null
            },
            {
                "$fromMillis(1521801216617, \"Hello\")", "\"Hello\"", null
            },
            {
                "$fromMillis(1521801216617, \"Year: [Y0001]\")", "\"Year: 2018\"", null
            },
            {
                "$fromMillis(1521801216617, \"Year: <[Y0001]>\")", "\"Year: <2018>\"", null
            },
            {
                "$fromMillis(1521801216617, \"Year: <[Y9,999,*]>\")", "\"Year: <2,018>\"", null
            },
            {
                "$fromMillis(1521801216617, \"[[Year: <[Y0001]>\")", "\"[Year: <2018>\"", null
            },
            {
                "$fromMillis(1521801216617, \"Year]]: <[Y0001]>\")", "\"Year]: <2018>\"", null
            },
            {
                "$fromMillis(1521801216617, \"[[Year]]: <[[[Y0001]]]>\")", "\"[Year]: <[2018]>\"", null
            },
            {
                "$fromMillis(1521801216617, \"[D#1]/[M#1]/[Y0001]\")", "\"23/3/2018\"", null
            },
            {
                "$fromMillis(1522616700000, \"[F0] [FNn]\")", "\"7 Sunday\"", null
            },
            {
                "$fromMillis(1522703100000, \"[F0] [FNn]\")", "\"1 Monday\"", null
            },
            {
                "$fromMillis(1521801216617, \"[Y0001]-[M01]-[D01]\")", "\"2018-03-23\"", null
            },
            {
                "$fromMillis(1521801216617, \"[M01]/[D01]/[Y0001] at [H01]:[m01]:[s01]\")", "\"03/23/2018 at 10:33:36\"", null
            },
            {
                "$fromMillis(1521801216617, \"[Y]-[M01]-[D01]T[H01]:[m]:[s].[f001][Z01:01t]\")", "\"2018-03-23T10:33:36.617Z\"", null
            },
            {
                "$fromMillis(1521801216617, '[Y]-[ M01]-[D 01]T[H01 ]:[ m   ]:[s].[f0  01][Z01:\n 01t]')", "\"2018-03-23T10:33:36.617Z\"", null
            },
            {
                "$fromMillis(1521801216617, \"[Y]-[M01]-[D01]T[H01]:[m]:[s].[f001][Z0101t]\", \"+0100\")", "\"2018-03-23T11:33:36.617+0100\"", null
            },
            {
                "$fromMillis(1531310400000, \"[Y]-[M01]-[D01]T[H01]:[m]:[s][Z01:01]\")", "\"2018-07-11T12:00:00+00:00\"", null
            },
            {
                "$fromMillis(1531310400000, \"[Y]-[M01]-[D01]T[H01]:[m]:[s][Z01:01t]\")", "\"2018-07-11T12:00:00Z\"", null
            },
            {
                "$fromMillis(1531310400000, \"[Y]-[M01]-[D01]T[H01]:[m]:[s][Z]\", \"-0500\")", "\"2018-07-11T07:00:00-05:00\"", null
            },
            {
                "$fromMillis(1531310400000, \"[Y]-[M01]-[D01]T[H01]:[m]:[s][Z0]\", \"-0500\")", "\"2018-07-11T07:00:00-5\"", null
            },
            {
                "$fromMillis(1531310400000, \"[Y]-[M01]-[D01]T[H01]:[m]:[s][Z]\", \"+1000\")", "\"2018-07-11T22:00:00+10:00\"", null
            },
            {
                "$fromMillis(1531310400000, \"[Y]-[M01]-[D01]T[H01]:[m]:[s][Z0]\", \"+1000\")", "\"2018-07-11T22:00:00+10\"", null
            },
            {
                "$fromMillis(1531310400000, \"[Y]-[M01]-[D01]T[H01]:[m]:[s][Z0]\", \"+0530\")", "\"2018-07-11T17:30:00+5:30\"", null
            },
            {
                "$fromMillis(1531310400000, \"[Y]-[M01]-[D01]T[H01]:[m]:[s][z]\", \"-0500\")", "\"2018-07-11T07:00:00GMT-05:00\"", null
            },
            {
                "$fromMillis(1204405500000, \"[Y]-[M01]-[D01]T[H01]:[m]:[s].[f001][Z0101t]\", \"+0530\")", "\"2008-03-02T02:35:00.000+0530\"", null
            },
            {
                "$fromMillis(1230757500000, \"[Y]-[M01]-[D01]T[H01]:[m]:[s].[f001][Z0101t]\", \"+0530\")", "\"2009-01-01T02:35:00.000+0530\"", null
            },
            //TODO need to handle error better
            {
                "$fromMillis(1230757500000, \"[Y]-[M01]-[D01]T[H01]:[m]:[s].[f001][Z010101t]\", \"+0530\")", null, Constants.ERR_MSG_TIMEZONE_FORMAT
            },
            {
                "$fromMillis(1521801216617, \"[D#1,2]/[M1,2]/[Y,2]\")", "\"23/03/18\"", null
            },
            {
                "$fromMillis(1521801216617, \"[D#1,2]/[M1,2]/[Y0001,2]\")", "\"23/03/2018\"", null
            },
            {
                "$fromMillis(1521801216617, \"[D#1,2]/[M1,2]/[Y##01,2-2]\")", "\"23/03/18\"", null
            },
            {
                "$fromMillis(1521801216617, \"[D#1,2]/[M1,2]/[Y0001,2-2]\")", "\"23/03/18\"", null
            },
            {
                "$fromMillis(1521801216617, \"[D1] [M01] [YI]\")", "\"23 03 MMXVIII\"", null
            },
            {
                "$fromMillis(1521801216617, \"[Da] [MA] [Yi]\")", "\"w C mmxviii\"", null
            },
            {
                "$fromMillis(1521801216617, \"[D1o] [M01] [Y]\")", "\"23rd 03 2018\"", null
            },
            {
                "$fromMillis(1521801216617, \"[Yw]\")", "\"two thousand and eighteen\"", null
            },
            {
                "$fromMillis(1521801216617, \"[Dwo] [M01] [Y]\")", "\"twenty-third 03 2018\"", null
            },
            {
                "$fromMillis(1521801216617, \"[D1o] [MNn] [Y]\")", "\"23rd March 2018\"", null
            },
            {
                "$fromMillis(1521801216617, \"[D1o] [MN] [Y]\")", "\"23rd MARCH 2018\"", null
            },
            {
                "$fromMillis(1521801216617, \"[FNn], [D1o] [MNn] [Y]\")", "\"Friday, 23rd March 2018\"", null
            },
            {
                "$fromMillis(1521801216617, \"[FNn], the [Dwo] of [MNn] [Y] [E]\")", "\"Friday, the twenty-third of March 2018 ISO\"", null
            },
            {
                "$fromMillis(1521801216617, \"[FNn,3-3], [D1o] [MNn,3-3] [Y] [C]\")", "\"Fri, 23rd Mar 2018 ISO\"", null
            },
            {
                "$fromMillis(1521801216617, \"[F], [D]/[M]/[Y] [h]:[m]:[s] [P]\")", "\"friday, 23/3/2018 10:33:36 am\"", null
            },
            {
                "$fromMillis(1204405500000, \"[F], [D]/[M]/[Y] [h]:[m]:[s] [P]\")", "\"saturday, 1/3/2008 9:05:00 pm\"", null
            },
            {
                "$fromMillis(1199664000000, \"[F], [D]/[M]/[Y] [h]:[m]:[s] [P]\")", "\"monday, 7/1/2008 12:00:00 am\"", null
            },
            {
                "$fromMillis(1514808000000, \"[dwo] day of the year\")", "\"first day of the year\"", null
            },
            {
                "$fromMillis(1546257600000, \"[d] days in [Y0001]\")", "\"365 days in 2018\"", null
            },
            {
                "$fromMillis(1483185600000, \"[d] days in [Y0001]\")", "\"366 days in 2016\"", null
            },
            {
                "$fromMillis(1514808000000, \"Week: [W]\")", "\"Week: 1\"", null
            },
            {
                "$fromMillis(1515326400000, \"Week: [W]\")", "\"Week: 1\"", null
            },
            {
                "$fromMillis(1545739200000, \"Week: [W]\")", "\"Week: 52\"", null
            },
            {
                "$fromMillis(1388577600000, \"Week: [W]\")", "\"Week: 1\"", null
            },
            {
                "$fromMillis(1419854400000, \"Week: [W]\")", "\"Week: 1\"", null
            },
            {
                "$fromMillis(1419768000000, \"Week: [W]\")", "\"Week: 52\"", null
            },
            {
                "$fromMillis(1419336000000, \"Week: [W]\")", "\"Week: 52\"", null
            },
            {
                "$fromMillis(1420113600000, \"Week: [W]\")", "\"Week: 1\"", null
            },
            {
                "$fromMillis(1420459200000, \"Week: [W]\")", "\"Week: 2\"", null
            },
            {
                "$fromMillis(1451304000000, \"Week: [W]\")", "\"Week: 53\"", null
            },
            {
                "$fromMillis(1451563200000, \"Week: [W]\")", "\"Week: 53\"", null
            },
            {
                "$fromMillis(1451736000000, \"Week: [W]\")", "\"Week: 53\"", null
            },
            {
                "$fromMillis(1419940800000, \"[YN]-[M]-[D]\")", null, String.format(Constants.ERR_MSG_INVALID_NAME_MODIFIER, "Y")
            },
            {
                "$fromMillis(1419940800000, \"[YN]-[M\")", null, Constants.ERR_MSG_NO_CLOSING_BRACKET
            },

                        //TODO need to consider the undocumented xX values from jsonata
                        // { "$fromMillis(1359460800000, \"Week: [w] of [xNn]\")","\"Week: 5 of January\"",null},
                        // { "$fromMillis(1359633600000, \"Week: [w] of [xNn]\")","\"Week: 5 of January\"",null},
                        // { "$fromMillis(1359720000000, \"Week: [w] of [xNn]\")","\"Week: 5 of January\"",null},
                        // { "$fromMillis(1514808000000, \"Week: [w] of [xNn]\")","\"Week: 1 of January\"",null},
                        // { "$fromMillis(1483272000000, \"Week: [w] of [xNn]\")","\"Week: 5 of December\"",null},
                        // { "$fromMillis(1533038400000, \"Week: [w] of [xNn]\")","\"Week: 1 of August\"",null},
                        // { "$fromMillis(1419940800000, \"Week: [w] of [xNn]\")","\"Week: 1 of January\"",null},
        });
    }

    @Test
    public void runTest() throws Exception {
        test(this.expression, expectedResultJsonString, expectedRuntimeExceptionMessage, null);
    }
}
