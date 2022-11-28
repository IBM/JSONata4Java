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
import com.api.jsonata4java.expressions.functions.SubstringFunction;

/**
 * For simplicity, these tests don't rely on $state/$event/$instance access;
 * instead providing the "input" inlined with the expression itself (e.g. ["a",
 * "b"][0]=="a"). Separate test cases verify that variable access works as
 * expected.
 */
@RunWith(Parameterized.class)
public class SubstringFunctionTests implements Serializable {

    private static final long serialVersionUID = 7950762591649916806L;

    @Parameter(0)
    public String expression;

    @Parameter(1)
    public String expectedResultJsonString;

    @Parameter(2)
    public String expectedRuntimeExceptionMessage;

    @Parameters(name = "{index}: {0} -> {1} ({2})")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            // $substring
            {
                "$substring(\"hello\",0)", "\"hello\"", null
            }, //
            {
                "$substring(\"hello\",1)", "\"ello\"", null
            }, //
            {
                "$substring(\"hello\",4)", "\"o\"", null
            }, // 
            {
                "$substring(\"hello\",5)", "\"\"", null
            }, //
            {
                "$substring(\"hello\",6)", "\"\"", null
            }, // 
            {
                "$substring(\"hello\",66)", "\"\"", null
            }, //
            {
                "$substring(\"hello\",1,1)", "\"e\"", null
            }, // 
            {
                "$substring(\"hello\",-1,1)", "\"o\"", null
            }, //
            {
                "$substring(\"hello\",2,2)", "\"ll\"", null
            }, // 
            {
                "$substring(\"hello\",0,1)", "\"h\"", null
            }, //
            {
                "$substring(\"hello\",0,4)", "\"hell\"", null
            }, // 
            {
                "$substring(\"hello\",0,5)", "\"hello\"", null
            }, //
            {
                "$substring(\"hello\",0,6)", "\"hello\"", null
            }, // 
            {
                "$substring(\"hello\",0,66)", "\"hello\"", null
            }, //
            {
                "$substring(\"hello\",-1)", "\"o\"", null
            }, // 
            {
                "$substring(\"hello\",-2)", "\"lo\"", null
            }, //
            {
                "$substring(\"hello\",-4)", "\"ello\"", null
            }, // 
            {
                "$substring(\"hello\",-5)", "\"hello\"", null
            }, //
            {
                "$substring(\"hello\",-6)", "\"hello\"", null
            }, // 
            {
                "$substring(\"hello\",-66)", "\"hello\"", null
            }, //

            // length <= returns empty string
            {
                "$substring(\"hello\",0,0)", "\"\"", null
            }, // 
            {
                "$substring(\"hello\",0,-1)", "\"\"", null
            }, //
            {
                "$substring(\"hello\",0,-66)", "\"\"", null
            }, //

            // non-integer start values are rounded down
            {
                "$substring(\"hello\",1.0)", "\"ello\"", null
            }, // 
            {
                "$substring(\"hello\",1.9)", "\"ello\"", null
            }, //
            {
                "$substring(\"hello\",2.0)", "\"llo\"", null
            }, // 
            {
                "$substring(\"hello\",2.1)", "\"llo\"", null
            }, //
            {
                "$substring(\"hello\",-0.9)", "\"hello\"", null
            }, //
            {
                "$substring(\"hello\",-1.0)", "\"o\"", null
            }, //
            {
                "$substring(\"hello\",-1.1)", "\"o\"", null
            }, // 
            {
                "$substring(\"hello\",-1.9)", "\"o\"", null
            }, //
            {
                "$substring(\"hello\",-2.0)", "\"lo\"", null
            }, // 
            {
                "$substring(\"hello\",-2.1)", "\"lo\"", null
            }, //
            // non-integer length values are rounded down
            {
                "$substring(\"hello\",0,0.1)", "\"\"", null
            }, //
            {
                "$substring(\"hello\",0,0.9)", "\"\"", null
            }, //
            {
                "$substring(\"hello\",0,1.0)", "\"h\"", null
            }, //
            {
                "$substring(\"hello\",0,1.1)", "\"h\"", null
            }, //
            {
                "$substring(\"hello\",0,1.9999)", "\"h\"", null
            }, //
            {
                "$substring()", null, SubstringFunction.ERR_ARG1BADTYPE
            }, //
            {
                "$substring(\"hello\")", null, SubstringFunction.ERR_ARG2BADTYPE
            }, //
            {
                "$substring(\"hello\", 1, 1, 1)", null, SubstringFunction.ERR_ARG4BADTYPE
            }, //
            {
                "$substring(null, 1)", null, SubstringFunction.ERR_ARG1BADTYPE
            }, //
            {
                "$substring(22, 1)", null, SubstringFunction.ERR_ARG1BADTYPE
            }, //
            {
                "$substring([\"hello\"], 1)", null, SubstringFunction.ERR_ARG1BADTYPE
            }, //
            {
                "$substring(\"hello\", null)", null, SubstringFunction.ERR_ARG2BADTYPE
            }, //
            {
                "$substring(\"hello\", \"2\")", null, SubstringFunction.ERR_ARG2BADTYPE
            }, //
            {
                "$substring(\"hello\", [2])", null, SubstringFunction.ERR_ARG2BADTYPE
            }, //
            {
                "$substring(\"hello\", 1, null)", null, SubstringFunction.ERR_ARG3BADTYPE
            }, //
            {
                "$substring(\"hello\", 1, \"2\")", null, SubstringFunction.ERR_ARG3BADTYPE
            }, //
            {
                "$substring(\"hello\", 1, [2])", null, SubstringFunction.ERR_ARG3BADTYPE
            }, //

            // test behavior when "undefined" (i.e. Java null is supplied in one or more of
            // the arguments)
            {
                "$substring(noexisty, 0)", null, null
            },
            {
                "$substring(\"hello\", noexisty)", "\"hello\"", null
            },
            {
                "$substring(\"hello\", 1, noexisty)", "\"ello\"", null
            }
        }); //
    }

    @Test
    public void runTest() throws Exception {
        test(this.expression, expectedResultJsonString, expectedRuntimeExceptionMessage, null);
    }

}
