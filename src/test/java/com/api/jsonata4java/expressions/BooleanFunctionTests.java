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
 * <div class="block">From http://docs.jsonata.org/boolean-functions.html
 *  <p>
 *    $boolean(arg)
 *  </p>
 *  <table><caption>Casts the argument to a Boolean using the following rules</caption><thead>
 *   <tr><th>Argument type</th><th>Result</th></tr>
 *  </thead><tbody>
 *    <tr><td>Boolean</td><td>unchanged</td></tr>
 *    <tr><td>string: empty</td><td>false</td></tr>
 *    <tr><td>string: non-empty</td><td>true</td></tr>
 *    <tr><td>number: 0</td><td>false</td></tr>
 *    <tr><td>number: non-zero</td><td>true</td></tr>
 *    <tr><td>null</td><td>false</td></tr>
 *    <tr><td>array: empty</td><td>false</td></tr>
 *    <tr><td>array: contains a member that casts to true</td><td>true</td></tr>
 *    <tr><td>array: all members cast to false</td><td>false</td></tr>
 *    <tr><td>object: empty</td><td>false</td></tr>
 *    <tr><td>object: non-empty</td><td>true</td></tr>
 *    <tr><td>function: (functions are not currently supported)</td><td>false</td></tr>
 *   </tbody>
 *  </table>
 * </div>
 */
@RunWith(Parameterized.class)
public class BooleanFunctionTests implements Serializable {

    private static final long serialVersionUID = -5484976092435425868L;

    private static final String ERR_MSG_NUMBER_OUT_OF_RANGE = String.format(Constants.ERR_MSG_NUMBER_OUT_OF_RANGE,
        "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
    private static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_BOOLEAN);

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
                "$boolean()", null, null
            }, // jsonata 1.8.2 ERR_BAD_CONTEXT }, //
            {
                "$boolean(true, false)", null, ERR_ARG2BADTYPE
            }, //
            {
                "$boolean(a.b.c)", null, null
            }, // // because a.b.c doesn't exist
            {
                "$boolean(true)", "true", null
            }, //
            {
                "$boolean(false)", "false", null
            }, //
            {
                "$boolean('')", "false", null
            }, //
            {
                "$boolean('a')", "true", null
            }, //
            {
                "$boolean('false')", "true", null
            }, //
            {
                "$boolean('true')", "true", null
            }, //
            {
                "$boolean(0)", "false", null
            }, //
            {
                "$boolean(1)", "true", null
            }, //
            {
                "$boolean(1.0)", "true", null
            }, //
            {
                "$boolean(-1)", "true", null
            }, //
            {
                "$boolean(-1.0)", "true", null
            }, //
            {
                "$boolean(2)", "true", null
            }, //
            {
                "$boolean(2.0)", "true", null
            }, //
            {
                "$boolean(-2)", "true", null
            }, //
            {
                "$boolean(-2.0)", "true", null
            }, //
            {
                "$boolean(null)", "false", null
            }, //
            {
                "$boolean([])", "false", null
            }, //
            {
                "$boolean([0])", "false", null
            }, // // single member that casts to false -> true
            {
                "$boolean([1])", "true", null
            }, // // single member that casts to true -> false
            {
                "$boolean([false, '', false, [], [0], null])", "false", null
            }, // // all members cast to false ->
            // true
            {
                "$boolean([true, 1, 'a', [1]])", "true", null
            }, // // all members cast to true -> false
            {
                "$boolean([false, '', false, [], [0], true, null])", "true", null
            }, // // contains a member that
            // casts to true -> false
            {
                "$boolean([false, '', false, [], [1], null])", "true", null
            }, // // contains a member that casts to
            // true -> false
            {
                "$boolean({})", "false", null
            }, //
            {
                "$boolean({'hello':true})", "true", null
            }, //
            {
                "$boolean({'0':false})", "true", null
            }, //
            {
                "$boolean([9223372036854775807])", "true", null
            }, // // Long.MAX_VALUE
            {
                "$boolean([-9223372036854775808])", "true", null
            }, // // Long.MIN_VALUE
            {
                "$boolean([9223372036854775809])", "true", null
            }, //
            {
                "$boolean([9223372036854775899.5])", "true", null
            }, //
            {
                "$boolean(1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890)",
                null, ERR_MSG_NUMBER_OUT_OF_RANGE
            }, //
            {
                "a.b.c~>$boolean()", null, null
            }, //
            {
                "true~>$boolean()", "true", null
            }, //
            {
                "false~>$boolean()", "false", null
            }, //
            {
                "''~>$boolean()", "false", null
            }, //
            {
                "'a'~>$boolean(      )", "true", null
            }, //
            {
                "'false'~>$boolean()", "true", null
            }, //
            {
                "'true'~>$boolean()", "true", null
            }, //
            {
                "0~>$boolean()", "false", null
            }, //
            {
                "1~>$boolean()", "true", null
            }, //
            {
                "1.0~>$boolean()", "true", null
            }, //
            {
                "-1~>$boolean()", "true", null
            }, //
            {
                "-1.0~>$boolean()", "true", null
            }, //
            {
                "2~>$boolean()", "true", null
            }, //
            {
                "2.0~>$boolean()", "true", null
            }, //
            {
                "-2~>$boolean()", "true", null
            }, //
            {
                "-2.0~>$boolean()", "true", null
            }, //
            {
                "null~>$boolean()", "false", null
            }, //
            {
                "[]~>$boolean()", "false", null
            }, //
            {
                "[0]~>$boolean()", "false", null
            }, // // single member that casts to false -> true
            {
                "[1]~>$boolean()", "true", null
            }, // // single member that casts to true -> false
            {
                "[false, '', false, [], [0], null]~>$boolean()", "false", null
            }, // all members cast to false -> true
            {
                "[true, 1, 'a', [1]]~>$boolean()", "true", null
            }, // // all members cast to true -> false
            {
                "[false, '', false, [], [0], true, null]~>$boolean()", "true", null
            }, // contains a member that casts to true -> false
            {
                "[false, '', false, [], [1], null]~>$boolean()", "true", null
            }, // contains a member that casts to true -> false
            {
                "{}~>$boolean()", "false", null
            }, //
            {
                "{'hello':true}~>$boolean()", "true", null
            }, //
            {
                "{'0':false}~>$boolean(      )", "true", null
            }, //
            {
                "[9223372036854775807]~>$boolean(      )", "true", null
            }, // Long.MAX_VALUE
            {
                "[-9223372036854775808]~>$boolean(      )", "true", null
            }, // Long.MIN_VALUE
            {
                "[9223372036854775809]~>$boolean(      )", "true", null
            }, //
            {
                "[9223372036854775899.5]~>$boolean(      )", "true", null
            } //

        });
    }

    @Test
    public void runTest() throws Exception {
        test(this.expression, expectedResultJsonString, expectedRuntimeExceptionMessage, null);
    }

}
