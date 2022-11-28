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

/**
 * For simplicity, these tests provide the "input" inlined with the expression
 * itself (e.g. ["a", "b"][0]=="a"). Separate test cases verify that variable
 * access works as expected.
 * 
 * $map(array, function)
 * 
 * Returns an array containing the results of applying the function parameter to each value in the array parameter.
 * The function that is supplied as the second parameter must have the following signature:
 * function(value [, index [, array]])
 * Each value in the input array is passed in as the first parameter in the supplied function.
 * The index (position) of that value in the input array is passed in as the second parameter, if specified.
 * The whole input array is passed in as the third parameter, if specified.
 * 
 * Examples
 * 
 * $map([1..5], $string) => ["1", "2", "3", "4", "5"]
 * $map(Email.address, function($v, $i, $a) {
 *   'Item ' & ($i+1) & ' of ' & $count($a) & ': ' & $v
 * })
 * evaluates to:
 * [
 *   "Item 1 of 4: fred.smith@my-work.com",
 *   "Item 2 of 4: fsmith@my-work.com",
 *   "Item 3 of 4: freddy@my-social.com",
 *   "Item 4 of 4: frederic.smith@very-serious.com"
 * ]
 */
@RunWith(Parameterized.class)
public class MapFunctionTests implements Serializable {

    @Parameter(0)
    public String expression;

    @Parameter(1)
    public String expectedResultJsonString;

    @Parameter(2)
    public String expectedRuntimeExceptionMessage;

    @Parameters(name = "{index}: {0} -> {1} ({2})")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            { "$map()", null, "Context value is not a compatible type with argument 1 of function \"$map\"" }, //
            // $map({})", null, ERR_MSG_ARG1_BAD_TYPE
            // $map({}, ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            // $map(' ', {})", null, ERR_MSG_ARG2_BAD_TYPE
            // $map([])", null, ERR_MSG_ARG1_BAD_TYPE
            // $map([], ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            // $map(' ', [])", null, ERR_MSG_ARG2_BAD_TYPE
            // $map({\"hello\": 1})", null, ERR_MSG_ARG1_BAD_TYPE
            // $map({\"hello\": 1}, ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            // $map(' ', {\"hello\": 1})", null, ERR_MSG_ARG2_BAD_TYPE
            // $map(true)", null, ERR_MSG_ARG1_BAD_TYPE
            // $map(true, ' ')", null, ERR_MSG_ARG1_BAD_TYPE

            { "$map([1..5], $string)", "[\"1\", \"2\", \"3\", \"4\", \"5\"]", null }, //
            // works as well in a chain
            { "[1..5] ~> $map($string)", "[\"1\", \"2\", \"3\", \"4\", \"5\"]", null }, //
            { "[\"fred.smith@my-work.com\","
                + "\"fsmith@my-work.com\","
                + "\"freddy@my-social.com\","
                + "\"frederic.smith@very-serious.com\""
                + "] ~>"
                + "$map(function($v, $i, $a) {"
                + "  'Item ' & ($i+1) & ' of ' & $count($a) & ': ' & $v"
                + "})",
                "[\"Item 1 of 4: fred.smith@my-work.com\","
                    + "\"Item 2 of 4: fsmith@my-work.com\","
                    + "\"Item 3 of 4: freddy@my-social.com\","
                    + "\"Item 4 of 4: frederic.smith@very-serious.com\"]",
                null }, //
        });
    }

    @Test
    public void runTest() throws Exception {
        test(this.expression, expectedResultJsonString, expectedRuntimeExceptionMessage, null);
    }
}
