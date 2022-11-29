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
 * For simplicity, these tests don't rely on $state/$event/$instance access;
 * instead providing the "input" inlined with the expression itself (e.g. ["a",
 * "b"][0]=="a"). Separate test cases verify that variable access works as
 * expected.
 * 
 * From http://docs.jsonata.org/string-functions.html:
 * 
 * $string(arg)
 * 
 * Casts the arg parameter to a string using the following casting rules
 * 
 * Strings are unchanged Functions are converted to an empty string Numeric
 * infinity and NaN throw an error because they cannot be represented as a JSON
 * number All other values are converted to a JSON string using the
 * JSON.stringify function If arg is not specified (i.e. this function is
 * invoked with no arguments), then the context value is used as the value of
 * arg.
 * 
 * Examples
 * 
 * $string(5)=="5" [1..5].$string()==["1", "2", "3", "4", "5"]
 */
@RunWith(Parameterized.class)
public class StringFunctionTests implements Serializable {

    private static final long serialVersionUID = -312249015654495365L;

    @Parameter(0)
    public String expression;

    @Parameter(1)
    public String expectedResultJsonString;

    @Parameter(2)
    public String expectedRuntimeExceptionMessage;

    @Parameters(name = "{index}: {0} -> {1} ({2})")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            { "$string()", null, null }, // jsonata 1.8.2 StringFunction.ERR_BAD_CONTEXT }, //
            { "$string({})", "\"{}\"", null }, //
            { "$string([])", "\"[]\"", null }, //
            { "$string({\"hello\": 1})", "\"{\\\"hello\\\":1}\"", null }, //
            { "$string($string([\"hello\", 1]))", "\"[\\\"hello\\\",1]\"", null }, //
            { "$string(1)", "\"1\"", null }, //
            { "$string(-22.2)", "\"-22.2\"", null }, //
            { "$string(10/3.0)", "\"3.33333333333333\"", null }, // jsonata 1.8.2 "\"3.3333333333333335\"", null }, //
            { "$string(xxxx)", null, null }, //
            { "$string(null)", "\"null\"", null } //
        });
    }

    @Test
    public void runTest() throws Exception {
        test(this.expression, expectedResultJsonString, expectedRuntimeExceptionMessage, null);
    }

}
