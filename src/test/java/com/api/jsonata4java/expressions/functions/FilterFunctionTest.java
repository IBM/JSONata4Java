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
    public void nullInput() throws Exception {
        test("$filter()", null, null, (String) null);
    }

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
    public void filterNumbers() throws Exception {
        test("($x:=function($l){$l>2};$filter([1,5,3,4,2],$x))", "[5, 3, 4]", null, null);
    }

    @Test
    public void filterNumbersOdd() throws Exception {
        test("($x:=function($l){$l%2=1};$filter([1,5,3,4,2],$x))", "[1, 5, 3]", null, null);
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
    public void filterMissingFunctionArg() throws Exception {
        test("$filter([1,5,3,4,2])", null, FilterFunction.ERR_ARG2BADTYPE, (String) null);
    }

    @Test
    public void filterMissingArrayArg() throws Exception {
        test("($x:=function($l){$l>2};$filter($x))", null, null, (String) null);
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
}
