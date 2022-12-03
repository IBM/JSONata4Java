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

package com.api.jsonata4java.expressions.functions;

import static com.api.jsonata4java.expressions.utils.Utils.test;
import org.junit.Test;

public class FilterFunctionTest {

    @Test
    public void filterObjects() throws Exception {
        test(/* expression */ "{\n"
            + "    \"objs\": [\n"
            + "        objects.id ~> $filter(function($v){$v > 1})\n"
            + "    ]\n"
            + "}",
            /* expected output */
            "{ \"objs\" : [ 2, 3 ] }",
            null,
            /* input */ "{\n"
                + "\"objects\": [\n"
                + "  {\"id\": 0},\n"
                + "  {\"id\": 1},\n"
                + "  {\"id\": 2},\n"
                + "  {\"id\": 3}\n"
                + "  ]\n"
                + "}");
    }

    @Test
    public void filterNumbersFuncInlined() throws Exception {
        test("$filter([1,5,3,4,2], function($n){$n>2})", "[5,3,4]", null, null);
    }

    @Test
    public void filterNumbersFuncDeclared() throws Exception {
        test("($x:=function($n){$n>2};$filter([1,5,3,4,2], $x))", "[5,3,4]", null, null);
    }

    @Test
    public void filterNumbersFuncJsonata1() throws Exception {
        test("$filter([-2,-1,0,1,2], $abs)", "[-2,-1,1,2]", null, null);
    }

    @Test
    public void filterNumbersFuncJsonataIncompatible() throws Exception {
        test("$filter([1,5,3,4,2], $substring)", null, "Context value is not a compatible type with argument 1 of function \"$substring\"", (String) null);
    }

    @Test
    public void filterNumbersFuncJsonataArgNotMatch1() throws Exception {
        test("$filter([1,5,3,4,2], $pad)", null, "Argument 1 of function $pad does not match function signature", (String) null);
    }

    @Test
    public void filterNumbersFuncJsonataArgNotMatch2() throws Exception {
        test("$filter([1,5,3,4,2], $split)", null, "Argument 2 of function $split does not match function signature", (String) null);
    }

    @Test
    public void filterNumbersFuncJsonata2() throws Exception {
        test("$filter([1,5,3,4,2], $reduce)", "[1,5,3,4,2]", null, (String) null);
    }

    @Test
    public void filterNumbersOdd() throws Exception {
        test("($x:=function($n){$n%2=1};$filter([1,5,3,4,2],$x))", "[1,5,3]", null, null);
    }

    @Test
    public void filterNumbersByIndex() throws Exception {
        test("$filter([10,4,45,2,13,7], function($n, $i){$i < 3})", "[10,4,45]", null, null);
    }

    @Test
    public void filterNumbersByIndexAndCompleteArray() throws Exception {
        test("$filter([10,4,45,2,13,7], function($n, $i, $a){$i > 0 and $a[$i - 1] >= 10})", "[4,2,7]", null, null);
    }

    @Test
    public void filterNumbersByIndexAndCompleteArrayPlusArg() throws Exception {
        test("$filter([10,4,45,2,13,7], function($n, $i, $a, $s){$i > 0 and $a[$i - 1] >= 10 and $s})", null, null, (String) null);
    }

    @Test
    public void filterSimple() throws Exception {
        test(/* expression */ "$filter([{\"entity\":{\"filter\":true}},{\"entity\":{\"filter\":false}},"
            + "{\"entity\":{\"missingfilter\":true}}],\n"
            + "function($v){$v.entity.filter=true})",
            /* expected output */" {\"entity\":{\"filter\":true}}", null, null);
    }

    @Test
    public void filterAll() throws Exception {
        test("$filter([{\"entity\":{\"filter\":true}},{\"entity\":{\"filter\":false}},{\"entity\":{\"missingfilter\":true}}],\n"
            + "   function($v){$v.filter=true})", null, null, (String) null);
    }

    @Test
    public void noInput() throws Exception {
        test("$filter()", null, FilterFunction.ERR_ARG1BADTYPE, (String) null);
    }

    @Test
    public void nullInput() throws Exception {
        test("$filter(null)", null, FilterFunction.ERR_ARG2BADTYPE, (String) null);
    }

    @Test
    public void nullInputChain() throws Exception {
        test("null ~> $filter()", null, FilterFunction.ERR_ARG1BADTYPE, (String) null);
    }

    @Test
    public void missingFunctionArg() throws Exception {
        test("$filter([1,5,3,4,2])", null, FilterFunction.ERR_ARG2BADTYPE, (String) null);
    }

    // Original JSONata FilterFunction.ERR_ARG2BADTYPE
    // FIXME how to differ this case from chainedArrayArgNullFuncInlined()
    @Test
    public void missingArrayArgFuncInlined() throws Exception {
        test("$filter(function($n){$n>2})", null, null, (String) null);
    }

    // Original JSONata FilterFunction.ERR_ARG2BADTYPE
    // FIXME how to differ this case from chainedArrayArgNullFuncInlined()
    @Test
    public void missingArrayArgFuncDeclared() throws Exception {
        test("($func:=function($n){$n>2};$filter($func))", null, null, (String) null);
    }

    @Test
    public void filterNumbersFuncNotDeclared() throws Exception {
        test("$filter([1,5,3,4,2], $func)", "[5,3,4]", String.format(FilterFunction.ERR_ARG2_FUNCTION_RESOLVE, "$func"), null);
    }

    @Test
    public void filterNumbersFuncReturnsNothing() throws Exception {
        test("$filter([1,5,3,4,2], function($n){null})", null, null, (String) null);
    }

    @Test
    public void filterNumbersFuncReturnsNoBoolean() throws Exception {
        test("$filter([1,5,3,4,2], function($n){\"foo\"})", null, null, (String) null);
    }

    @Test
    public void chainedArrayArgNullFuncInlined() throws Exception {
        test("null ~> $filter(function($n){$n>2})", null, null, (String) null);
    }

    @Test
    public void chainedArgEmptyFuncInlined() throws Exception {
        test("[] ~> $filter(function($n){$n>2})", null, null, (String) null);
    }

    @Test
    public void filterNumbersEmpty() throws Exception {
        test(/* expression */ "{\n"
            + "    \"objs\": [\n"
            + "        objects.id ~> $filter(function($v){$v > 1})\n"
            + "    ]\n"
            + "}",
            /* expected output */
            "{ \"objs\" : [ ] }",
            null,
            /* input */ "{\n"
                + "\"objects\": [\n"
                + "  ]\n"
                + "}");
    }

    @Test
    public void nullArrayArg() throws Exception {
        test("$filter(null, function($n){$n>2})", null,
            "The expressions either side of operator \">\" must evaluate to numeric or string values",
            (String) null);
    }

    @Test
    public void noMatchArrayArg() throws Exception {
        test("$filter(n, function($n){$n>2})", null, null, (String) null);
    }

    @Test
    public void tooMuchArgs() throws Exception {
        test("$filter([1,5,3], function($n){$n>2}, \"test\")", null, FilterFunction.ERR_ARG3BADTYPE, (String) null);
    }
}
