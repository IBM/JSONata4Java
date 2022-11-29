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
 * $formatNumber(number, picture [, options])
 * 
 * Casts the number to a string and formats it to a decimal representation as
 * specified by the picture string.
 * 
 * The behaviour of this function is consistent with the XPath/XQuery function
 * fn:format-number as defined in the XPath F%amp;O 3.1 specification. The picture
 * string parameter defines how the number is formatted and has the same syntax
 * as fn:format-number.
 * 
 * The optional third argument options is used to override the default locale
 * specific formatting characters such as the decimal separator. If supplied,
 * this argument must be an object containing name/value pairs specified in the
 * decimal format section of the XPath F%amp;O 3.1 specification.
 * 
 * Examples
 * 
 * $formatNumber(12345.6, '#,###.00')=="12,345.60" $formatNumber(1234.5678,
 * "00.000e0")=="12.346e2" $formatNumber(34.555, "#0.00;(#0.00)")=="34.56"
 * $formatNumber(-34.555, "#0.00;(#0.00)")=="(34.56)" $formatNumber(0.14,
 * "01%")=="14%" $formatNumber(0.14, "###pm", {"per-mille": "pm"})=="140pm"
 * $formatNumber(1234.5678, "①①.①①①e①", {"zero-digit": "\u245f"})=="①②.③④⑥e②"
 * 
 */
@RunWith(Parameterized.class)
public class FormatNumberFunctionTests implements Serializable {

    private static final long serialVersionUID = 1723911430381612327L;

    private static final String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT,
        Constants.FUNCTION_FORMAT_NUMBER);
    // private static final String ERR_MSG_ARG1_BAD_TYPE =
    // String.format(Constants.ERR_MSG_ARG1_BAD_TYPE,
    // Constants.FUNCTION_FORMAT_NUMBER);
    private static final String ERR_MSG_ARG2_BAD_TYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE,
        Constants.FUNCTION_FORMAT_NUMBER);
    private static final String ERR_MSG_ARG3_BAD_TYPE = String.format(Constants.ERR_MSG_ARG3_BAD_TYPE,
        Constants.FUNCTION_FORMAT_NUMBER);
    private static final String ERR_MSG_ARG4_BAD_TYPE = String.format(Constants.ERR_MSG_ARG4_BAD_TYPE,
        Constants.FUNCTION_FORMAT_NUMBER);

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
                "$formatNumber()", null, ERR_BAD_CONTEXT
            }, //
            {
                "$formatNumber({})", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$formatNumber(1, {})", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$formatNumber([])", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$formatNumber(1, [])", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$formatNumber(1, '#', [])", null, ERR_MSG_ARG3_BAD_TYPE
            }, //
            {
                "$formatNumber({\"hello\": 1})", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$formatNumber(1, {\"hello\": 1})", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$formatNumber([\"hello\", 1])", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$formatNumber(1, [\"hello\", 1])", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$formatNumber(1, '# ', [\"hello\", 1])", null, ERR_MSG_ARG3_BAD_TYPE
            }, //
            {
                "$formatNumber(true)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$formatNumber(1, true)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$formatNumber(1, '#', true)", null, ERR_MSG_ARG3_BAD_TYPE
            }, //
            {
                "$formatNumber(null)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$formatNumber(1, null)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$formatNumber(1, '#', null)", null, ERR_MSG_ARG3_BAD_TYPE
            }, //
            {
                "$formatNumber(5)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$formatNumber(5, 5)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$formatNumber(5, '#', 5)", null, ERR_MSG_ARG3_BAD_TYPE
            }, //
            {
                "$formatNumber(-5)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$formatNumber(-5, -5)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$formatNumber(-5, '#', -5)", null, ERR_MSG_ARG3_BAD_TYPE
            }, //
            {
                "$formatNumber(' ')", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$formatNumber(1, '#', ' ')", null, ERR_MSG_ARG3_BAD_TYPE
            }, //
            {
                "$formatNumber(10/3.0)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$formatNumber(1, 10/3.0)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$formatNumber(1, '#', 10/3.0)", null, ERR_MSG_ARG3_BAD_TYPE
            }, //
            {
                "$formatNumber(a.b.c)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$formatNumber(1, a.b.c)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$formatNumber(1, '#', a.b.c)", null, ERR_MSG_ARG3_BAD_TYPE
            }, //
            {
                "$formatNumber(1, '#', {'decimal-separator': '!!'})", null,
                String.format(Constants.ERR_MSG_INVALID_OPTIONS_SINGLE_CHAR, Constants.FUNCTION_FORMAT_NUMBER,
                    Constants.SYMBOL_DECIMAL_SEPARATOR)
            }, //
            {
                "$formatNumber(1, '#', {'grouping-separator': '!!'})", null,
                String.format(Constants.ERR_MSG_INVALID_OPTIONS_SINGLE_CHAR, Constants.FUNCTION_FORMAT_NUMBER,
                    Constants.SYMBOL_GROUPING_SEPARATOR)
            }, //
            {
                "$formatNumber(1, '#', {'infinity': ''})", null,
                String.format(Constants.ERR_MSG_INVALID_OPTIONS_STRING, Constants.FUNCTION_FORMAT_NUMBER,
                    Constants.SYMBOL_INFINITY)
            }, //
            {
                "$formatNumber(1, '#', {'minus-sign': '!!'})", null,
                String.format(Constants.ERR_MSG_INVALID_OPTIONS_SINGLE_CHAR, Constants.FUNCTION_FORMAT_NUMBER,
                    Constants.SYMBOL_MINUS_SIGN)
            }, //
            {
                "$formatNumber(1, '#', {'NaN': ''})", null,
                String.format(Constants.ERR_MSG_INVALID_OPTIONS_STRING, Constants.FUNCTION_FORMAT_NUMBER,
                    Constants.SYMBOL_NAN)
            }, //
            {
                "$formatNumber(1, '#', {'percent': '!!'})", null,
                String.format(Constants.ERR_MSG_INVALID_OPTIONS_SINGLE_CHAR, Constants.FUNCTION_FORMAT_NUMBER,
                    Constants.SYMBOL_PERCENT)
            }, //
            {
                "$formatNumber(1, '#', {'per-mille': '!!'})", null,
                String.format(Constants.ERR_MSG_INVALID_OPTIONS_SINGLE_CHAR, Constants.FUNCTION_FORMAT_NUMBER,
                    Constants.SYMBOL_PER_MILLE)
            }, //
            {
                "$formatNumber(1, '#', {'zero-digit': '!!'})", null,
                String.format(Constants.ERR_MSG_INVALID_OPTIONS_SINGLE_CHAR, Constants.FUNCTION_FORMAT_NUMBER,
                    Constants.SYMBOL_ZERO_DIGIT)
            }, //
            {
                "$formatNumber(1, '#', {'digit': '!!'})", null,
                String.format(Constants.ERR_MSG_INVALID_OPTIONS_SINGLE_CHAR, Constants.FUNCTION_FORMAT_NUMBER,
                    Constants.SYMBOL_DIGIT)
            }, //
            {
                "$formatNumber(1, '#', {'pattern-separator': 1})", null,
                String.format(Constants.ERR_MSG_INVALID_OPTIONS_SINGLE_CHAR, Constants.FUNCTION_FORMAT_NUMBER,
                    Constants.SYMBOL_PATTERN_SEPARATOR)
            }, //
            {
                "$formatNumber(1, '#', {'decimal-separator': 1})", null,
                String.format(Constants.ERR_MSG_INVALID_OPTIONS_SINGLE_CHAR, Constants.FUNCTION_FORMAT_NUMBER,
                    Constants.SYMBOL_DECIMAL_SEPARATOR)
            }, //
            {
                "$formatNumber(1, '#', {'grouping-separator': 1})", null,
                String.format(Constants.ERR_MSG_INVALID_OPTIONS_SINGLE_CHAR, Constants.FUNCTION_FORMAT_NUMBER,
                    Constants.SYMBOL_GROUPING_SEPARATOR)
            }, //
            {
                "$formatNumber(1, '#', {'infinity': 1})", null,
                String.format(Constants.ERR_MSG_INVALID_OPTIONS_STRING, Constants.FUNCTION_FORMAT_NUMBER,
                    Constants.SYMBOL_INFINITY)
            }, //
            {
                "$formatNumber(1, '#', {'minus-sign': 1})", null,
                String.format(Constants.ERR_MSG_INVALID_OPTIONS_SINGLE_CHAR, Constants.FUNCTION_FORMAT_NUMBER,
                    Constants.SYMBOL_MINUS_SIGN)
            }, //
            {
                "$formatNumber(1, '#', {'NaN': 1})", null,
                String.format(Constants.ERR_MSG_INVALID_OPTIONS_STRING, Constants.FUNCTION_FORMAT_NUMBER,
                    Constants.SYMBOL_NAN)
            }, //
            {
                "$formatNumber(1, '#', {'percent': 1})", null,
                String.format(Constants.ERR_MSG_INVALID_OPTIONS_SINGLE_CHAR, Constants.FUNCTION_FORMAT_NUMBER,
                    Constants.SYMBOL_PERCENT)
            }, //
            {
                "$formatNumber(1, '#', {'per-mille': 1})", null,
                String.format(Constants.ERR_MSG_INVALID_OPTIONS_SINGLE_CHAR, Constants.FUNCTION_FORMAT_NUMBER,
                    Constants.SYMBOL_PER_MILLE)
            }, //
            {
                "$formatNumber(1, '#', {'zero-digit': 1})", null,
                String.format(Constants.ERR_MSG_INVALID_OPTIONS_SINGLE_CHAR, Constants.FUNCTION_FORMAT_NUMBER,
                    Constants.SYMBOL_ZERO_DIGIT)
            }, //
            {
                "$formatNumber(1, '#', {'digit': 1})", null,
                String.format(Constants.ERR_MSG_INVALID_OPTIONS_SINGLE_CHAR, Constants.FUNCTION_FORMAT_NUMBER,
                    Constants.SYMBOL_DIGIT)
            }, //
            {
                "$formatNumber(1, '#', {'pattern-separator': 1})", null,
                String.format(Constants.ERR_MSG_INVALID_OPTIONS_SINGLE_CHAR, Constants.FUNCTION_FORMAT_NUMBER,
                    Constants.SYMBOL_PATTERN_SEPARATOR)
            }, //

            {
                "$formatNumber(a.b.c, '#')", null, null
            }, //
            {
                "$formatNumber({}, '#')", "\"NaN\"", null
            }, //
            {
                "$formatNumber({'hello': 1}, '#')", "\"NaN\"", null
            }, //
            {
                "$formatNumber([], '#')", "\"NaN\"", null
            }, //
            {
                "$formatNumber(['hello', 1], '#')", "\"NaN\"", null
            }, //
            {
                "$formatNumber(true, '#')", "\"NaN\"", null
            }, //
            {
                "$formatNumber(null, '#')", "\"NaN\"", null
            }, //
            {
                "$formatNumber(' ', '#')", "\"NaN\"", null
            }, //
            {
                "$formatNumber(1 / 0, '#')", "\"Infinity\"", null
            }, //
            {
                "$formatNumber(1 % 0, '#')", "\"NaN\"", null
            }, //
            {
                "$formatNumber(1234, '00000000')", "\"00001234\"", null
            }, //
            {
                "$formatNumber(-123.45, '###,###.00')", "\"-123.45\"", null
            }, //
            {
                "$formatNumber(-123.45, '###,###.00;(###,###.00)')", "\"(123.45)\"", null
            }, //
            // { "$formatNumber(193 / 200, '###.#%')", "\"96.5%\"", null}, //
            {
                "$formatNumber(193.00 / 200.00, '###.#%')", "\"96.5%\"", null
            }, //
            {
                "$formatNumber(12345.6, '#,###.00')", "\"12,345.60\"", null
            }, //
            {
                "$formatNumber(1234.5678, '00.000e0')", "\"12.346e2\"", null
            }, //
            {
                "$formatNumber(34.5555, '#0.00;(#0.00)')", "\"34.56\"", null
            }, //
            {
                "$formatNumber(-34.5555, '#0.00;(#0.00)')", "\"(34.56)\"", null
            }, //
            {
                "$formatNumber(0.14, '0%')", "\"14%\"", null
            }, //
            {
                "$formatNumber(12345.6, '#,###!00', {'decimal-separator': '!'})", "\"12,345!60\"", null
            }, //
            {
                "$formatNumber(12345.6, '#!###.00', {'grouping-separator': '!'})", "\"12!345.60\"", null
            }, //
            {
                "$formatNumber(1/0, '#', {'infinity': 'foo'})", "\"foo\"", null
            }, //
            {
                "$formatNumber(-12345, '#', {'minus-sign': '@'})", "\"@12345\"", null
            }, //
            {
                "$formatNumber({}, '#', {'NaN': 'foo'})", "\"foo\"", null
            }, //
            {
                "$formatNumber(193.00 / 200.00, '###.#@', {'percent': '@'})", "\"96.5@\"", null
            }, //
            {
                "$formatNumber(0.14, '###m', {'per-mille': 'm'})", "\"140m\"", null
            }, //
            {
                "$formatNumber(1, '\u245f\u245f', {'zero-digit': '\u245f'})", "\"\u245f①\"", null
            }, //
            {
                "$formatNumber(1234, '****', {'digit': '*'})", "\"1234\"", null
            }, //
            {
                "$formatNumber(-1.2, '#.0@(#.0)', {'pattern-separator': '@'})", "\"(1.2)\"", null
            }, //
            {
                "$formatNumber(100,\"##\",{\"zero-digit\": \"\\u245f\"},2)", null, ERR_MSG_ARG4_BAD_TYPE
            }, //
            {
                "$formatNumber(100,\"##\",{\"zero-digit\": \"\\u245f\"})", "\"①\u245f\u245f\"", null
            } //
                        // { "$formatNumber(1234.5678, '①①.①①①e①', {'zero-digit': '\u245f'})",
                        // "\"①②.③④⑥e②\"", null} //
        });
    }

    @Test
    public void runTest() throws Exception {
        test(this.expression, expectedResultJsonString, expectedRuntimeExceptionMessage, null);
    }
}
