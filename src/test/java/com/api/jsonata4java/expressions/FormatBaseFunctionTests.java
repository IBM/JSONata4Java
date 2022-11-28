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
 * $formatBase(number [, radix])
 * 
 * Casts the number to a string and formats it to an integer represented in the
 * number base specified by the radix argument. If radix is not specified, then
 * it defaults to base 10. radix can be between 2 and 36, otherwise an error is
 * thrown.
 * 
 * $formatBase(100, 2)==“1100100” $formatBase(2555, 16)=="9fb"
 * 
 */
@RunWith(Parameterized.class)
public class FormatBaseFunctionTests implements Serializable {

    private static final long serialVersionUID = 2635621643258378128L;

    private static final String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT,
        Constants.FUNCTION_FORMAT_BASE);
    private static final String ERR_MSG_ARG1_BAD_TYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE,
        Constants.FUNCTION_FORMAT_BASE);
    private static final String ERR_MSG_ARG2_BAD_TYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE,
        Constants.FUNCTION_FORMAT_BASE);
    private static final String ERR_MSG_ARG3_BAD_TYPE = String.format(Constants.ERR_MSG_ARG3_BAD_TYPE,
        Constants.FUNCTION_FORMAT_BASE);
    private static final String ERR_MSG_INVALID_RADIX = Constants.ERR_MSG_INVALID_RADIX;

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
                "$formatBase()", null, ERR_BAD_CONTEXT
            }, //
            {
                "$formatBase({})", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$formatBase({}, 2)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$formatBase(2, {})", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$formatBase([])", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$formatBase([], 2)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$formatBase(2, [])", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$formatBase({\"hello\": 1})", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$formatBase({\"hello\": 1}, 2)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$formatBase(2, {\"hello\": 1})", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$formatBase([\"hello\", 1])", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$formatBase([\"hello\", 1], 2)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$formatBase(2, [\"hello\", 1])", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$formatBase(true)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$formatBase(true, 2)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$formatBase(2, true)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$formatBase(null)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$formatBase(null, 2)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$formatBase(2, null)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$formatBase(' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$formatBase(2, ' ')", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$formatBase(' ', 2)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$formatBase(-1, ' ')", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$formatBase(' ', -1)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$formatBase(10/3.0, ' ')", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$formatBase(' ',  10/3.0)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$formatBase(100,  2, 2)", null, ERR_MSG_ARG3_BAD_TYPE
            }, //
            {
                "$formatBase(100,  1)", null, ERR_MSG_INVALID_RADIX
            }, //
            {
                "$formatBase(100,  37)", null, ERR_MSG_INVALID_RADIX
            }, //
            {
                "$formatBase(a.b.c)", null, null
            }, //
            {
                "$formatBase(a.b.c, 2)", null, null
            }, //
            {
                "$formatBase(100, a.b.c)", "\"100\"", null
            }, //
            {
                "$formatBase(100, \"a\")", "\"100\"", ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$formatBase(100,  2)", "\"1100100\"", null
            }, //
            {
                "$formatBase(100,  3)", "\"10201\"", null
            }, //
            {
                "$formatBase(100,  4)", "\"1210\"", null
            }, //
            {
                "$formatBase(100,  5)", "\"400\"", null
            }, //
            {
                "$formatBase(100,  6)", "\"244\"", null
            }, //
            {
                "$formatBase(100,  7)", "\"202\"", null
            }, //
            {
                "$formatBase(100,  8)", "\"144\"", null
            }, //
            {
                "$formatBase(100,  9)", "\"121\"", null
            }, //
            {
                "$formatBase(100,  10)", "\"100\"", null
            }, //
            {
                "$formatBase(100,  11)", "\"91\"", null
            }, //
            {
                "$formatBase(100,  12)", "\"84\"", null
            }, //
            {
                "$formatBase(100,  13)", "\"79\"", null
            }, //
            {
                "$formatBase(100,  14)", "\"72\"", null
            }, //
            {
                "$formatBase(100,  15)", "\"6a\"", null
            }, //
            {
                "$formatBase(100,  16)", "\"64\"", null
            }, //
            {
                "$formatBase(100,  17)", "\"5f\"", null
            }, //
            {
                "$formatBase(100,  18)", "\"5a\"", null
            }, //
            {
                "$formatBase(100,  19)", "\"55\"", null
            }, //
            {
                "$formatBase(100,  20)", "\"50\"", null
            }, //
            {
                "$formatBase(100,  21)", "\"4g\"", null
            }, //
            {
                "$formatBase(100,  22)", "\"4c\"", null
            }, //
            {
                "$formatBase(100,  23)", "\"48\"", null
            }, //
            {
                "$formatBase(100,  24)", "\"44\"", null
            }, //
            {
                "$formatBase(100,  25)", "\"40\"", null
            }, //
            {
                "$formatBase(100,  26)", "\"3m\"", null
            }, //
            {
                "$formatBase(100,  27)", "\"3j\"", null
            }, //
            {
                "$formatBase(100,  28)", "\"3g\"", null
            }, //
            {
                "$formatBase(100,  29)", "\"3d\"", null
            }, //
            {
                "$formatBase(100,  30)", "\"3a\"", null
            }, //
            {
                "$formatBase(100,  31)", "\"37\"", null
            }, //
            {
                "$formatBase(100,  32)", "\"34\"", null
            }, //
            {
                "$formatBase(100,  33)", "\"31\"", null
            }, //
            {
                "$formatBase(100,  34)", "\"2w\"", null
            }, //
            {
                "$formatBase(100,  35)", "\"2u\"", null
            }, //
            {
                "$formatBase(100,  36)", "\"2s\"", null
            } //
        });
    }

    @Test
    public void runTest() throws Exception {
        test(this.expression, expectedResultJsonString, expectedRuntimeExceptionMessage, null);
    }
}
