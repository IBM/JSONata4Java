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
import com.api.jsonata4java.expressions.functions.NotFunction;

/**
 * For simplicity, these tests don't rely on $state/$event/$instance access;
 * instead providing the "input" inlined with the expression itself (e.g. ["a",
 * "b"][0]=="a"). Separate test cases verify that variable access works as
 * expected.
 */
@RunWith(Parameterized.class)
public class NotFunctionTests implements Serializable {

    private static final long serialVersionUID = -5800003677588428417L;

    @Parameter(0)
    public String expression;

    @Parameter(1)
    public String expectedResultJsonString;

    @Parameter(2)
    public String expectedRuntimeExceptionMessage;

    @Parameters(name = "{index}: {0} -> {1} ({2})")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            // $boolean
            // From http://docs.jsonata.org/boolean-functions.html
            //			Boolean	unchanged
            //			string: empty	false
            //			string: non-empty	true
            //			number: 0	false
            //			number: non-zero	true
            //			null	false
            //			array: empty	false
            //			array: contains a member that casts to true	true
            //			array: all members cast to false	false
            //			object: empty	false
            //			object: non-empty	true
            //			function	false
            // $not (inverse of boolean casting rules shown above)
            //			Boolean	unchanged
            {
                "$not(true)", "false", null
            }, //
            {
                "$not(false)", "true", null
            }, //
            //			string: empty	false -> true
            {
                "$not(\"\")", "true", null
            }, //
            //			string: non-empty	true -> false
            {
                "$not(\"a\")", "false", null
            }, //
            {
                "$not(\"false\")", "false", null
            }, //
            {
                "$not(\"true\")", "false", null
            }, //
            //			number: 0	false -> true
            {
                "$not(0)", "true", null
            }, //
            //			number: non-zero	true -> false
            {
                "$not(1)", "false", null
            }, //
            {
                "$not(2)", "false", null
            }, //
            //			null	false -> true
            {
                "$not(null)", "true", null
            }, //
            //			array: empty	false -> true
            {
                "$not([])", "true", null
            }, //
            // non-empty arrays
            {
                "$not([0])", "true", null
            }, // single member that casts to false -> true
            {
                "$not([1])", "false", null
            }, // single member that casts to true -> false
            {
                "$not([false, \"\", false, [], [0], null])", "true", null
            }, // all members cast to false -> true
            {
                "$not([true, 1, \"a\", [1]])", "false", null
            }, // all members cast to true -> false
            {
                "$not([false, \"\", false, [], [0], true, null])", "false", null
            }, // contains a member that casts to true -> false
            {
                "$not([false, \"\", false, [], [1], null])", "false", null
            }, // // contains a member that casts to true -> false 
            //			object: empty	false -> true
            {
                "$not({})", "true", null
            }, //
            //			object: non-empty	true -> false
            {
                "$not({\"hello\":true})", "false", null
            }, //
            {
                "$not({\"0\":false})", "false", null
            }, //
            //			function	false -> true
            //			We don't yet support functions as first-class entities
            //			{"$not($exists)", "true", null}, //
            {
                "$not()", "false", null
            }, //
            {
                "$not(true, false)", null, NotFunction.ERR_ARG2BADTYPE
            }, //
        });
    }

    @Test
    public void runTest() throws Exception {
        test(this.expression, expectedResultJsonString, expectedRuntimeExceptionMessage, null);
    }

}
