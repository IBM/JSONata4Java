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

public class EachFunctionTest {

    @Test
    public void eachWithFunctionInlinedWith1Arg() throws Exception {
        test("$each(Address, function($val) {$val})",
            "[\n"
                + "  \"Hursley Park\",\n"
                + "  \"Winchester\",\n"
                + "  \"SO21 2JN\"\n"
                + "]",
            null,
            "{\n"
                + "  \"FirstName\": \"Fred\",\n"
                + "  \"Address\": {\n"
                + "    \"Street\": \"Hursley Park\",\n"
                + "    \"City\": \"Winchester\",\n"
                + "    \"Postcode\": \"SO21 2JN\"\n"
                + "  }\n"
                + "}");
    }

    @Test
    public void eachWithFunctionInlinedWith1ArgReturnNull() throws Exception {
        test("$each(Address, function($val) {null})",
            "[\n"
                + "  null,\n"
                + "  null,\n"
                + "  null\n"
                + "]",
            null,
            "{\n"
                + "  \"FirstName\": \"Fred\",\n"
                + "  \"Address\": {\n"
                + "    \"Street\": \"Hursley Park\",\n"
                + "    \"City\": \"Winchester\",\n"
                + "    \"Postcode\": \"SO21 2JN\"\n"
                + "  }\n"
                + "}");
    }

    @Test
    public void eachWithFunctionInlinedWith2Args() throws Exception {
        test("$each(Address, function($val, $key) {$key & \": \" & $val})",
            "[\n"
                + "  \"Street: Hursley Park\",\n"
                + "  \"City: Winchester\",\n"
                + "  \"Postcode: SO21 2JN\"\n"
                + "]",
            null,
            "{\n"
                + "  \"FirstName\": \"Fred\",\n"
                + "  \"Address\": {\n"
                + "    \"Street\": \"Hursley Park\",\n"
                + "    \"City\": \"Winchester\",\n"
                + "    \"Postcode\": \"SO21 2JN\"\n"
                + "  }\n"
                + "}");
    }

    @Test
    public void eachWithFunctionInlinedWith3Args() throws Exception {
        test("$each(Address, function($val, $key, $obj) {{ $key & \"|\" & $val : $obj }})",
            "[\n"
                + "  {\n"
                + "    \"Street|Hursley Park\": {\n"
                + "      \"Street\": \"Hursley Park\",\n"
                + "      \"City\": \"Winchester\",\n"
                + "      \"Postcode\": \"SO21 2JN\"\n"
                + "    }\n"
                + "  },\n"
                + "  {\n"
                + "    \"City|Winchester\": {\n"
                + "      \"Street\": \"Hursley Park\",\n"
                + "      \"City\": \"Winchester\",\n"
                + "      \"Postcode\": \"SO21 2JN\"\n"
                + "    }\n"
                + "  },\n"
                + "  {\n"
                + "    \"Postcode|SO21 2JN\": {\n"
                + "      \"Street\": \"Hursley Park\",\n"
                + "      \"City\": \"Winchester\",\n"
                + "      \"Postcode\": \"SO21 2JN\"\n"
                + "    }\n"
                + "  }\n"
                + "]",
            null,
            "{\n"
                + "  \"FirstName\": \"Fred\",\n"
                + "  \"Address\": {\n"
                + "    \"Street\": \"Hursley Park\",\n"
                + "    \"City\": \"Winchester\",\n"
                + "    \"Postcode\": \"SO21 2JN\"\n"
                + "  }\n"
                + "}");
    }

    @Test
    public void eachWithFunctionInlinedWith4Args() throws Exception {
        test("$each(Address, function($val, $key, $obj, $x) {{ $key & \"|\" & $val : $obj & $x}})",
            "[\n"
                + "  {\n"
                + "    \"Street|Hursley Park\": \"{\\\"Street\\\":\\\"Hursley Park\\\",\\\"City\\\":\\\"Winchester\\\",\\\"Postcode\\\":\\\"SO21 2JN\\\"}\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"City|Winchester\": \"{\\\"Street\\\":\\\"Hursley Park\\\",\\\"City\\\":\\\"Winchester\\\",\\\"Postcode\\\":\\\"SO21 2JN\\\"}\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"Postcode|SO21 2JN\": \"{\\\"Street\\\":\\\"Hursley Park\\\",\\\"City\\\":\\\"Winchester\\\",\\\"Postcode\\\":\\\"SO21 2JN\\\"}\"\n"
                + "  }\n"
                + "]",
            null,
            "{\n"
                + "  \"FirstName\": \"Fred\",\n"
                + "  \"Address\": {\n"
                + "    \"Street\": \"Hursley Park\",\n"
                + "    \"City\": \"Winchester\",\n"
                + "    \"Postcode\": \"SO21 2JN\"\n"
                + "  }\n"
                + "}");
    }

    @Test
    public void eachWithFunctionWith1Arg() throws Exception {
        test("($func := function($val) {$val};\n"
            + "$each(Address, $func))",
            "[\n"
                + "  \"Hursley Park\",\n"
                + "  \"Winchester\",\n"
                + "  \"SO21 2JN\"\n"
                + "]",
            null,
            "{\n"
                + "  \"FirstName\": \"Fred\",\n"
                + "  \"Address\": {\n"
                + "    \"Street\": \"Hursley Park\",\n"
                + "    \"City\": \"Winchester\",\n"
                + "    \"Postcode\": \"SO21 2JN\"\n"
                + "  }\n"
                + "}");
    }

    @Test
    public void eachWithFunctionWith2Args() throws Exception {
        test("($mapKeyValue := function($v, $k) {$k & \": \" & $v};\n"
            + "$each(Address, $mapKeyValue))",
            "[\n"
                + "  \"Street: Hursley Park\",\n"
                + "  \"City: Winchester\",\n"
                + "  \"Postcode: SO21 2JN\"\n"
                + "]",
            null,
            "{\n"
                + "  \"FirstName\": \"Fred\",\n"
                + "  \"Address\": {\n"
                + "    \"Street\": \"Hursley Park\",\n"
                + "    \"City\": \"Winchester\",\n"
                + "    \"Postcode\": \"SO21 2JN\"\n"
                + "  }\n"
                + "})");
    }

    @Test
    public void eachWithFunctionWith3Args() throws Exception {
        test("($myfunc := function($val, $key, $obj) {{ $key & \"|\" & $val : $obj }};\n"
            + "$each(Address, $myfunc))",
            "[\n"
                + "  {\n"
                + "    \"Street|Hursley Park\": {\n"
                + "      \"Street\": \"Hursley Park\",\n"
                + "      \"City\": \"Winchester\",\n"
                + "      \"Postcode\": \"SO21 2JN\"\n"
                + "    }\n"
                + "  },\n"
                + "  {\n"
                + "    \"City|Winchester\": {\n"
                + "      \"Street\": \"Hursley Park\",\n"
                + "      \"City\": \"Winchester\",\n"
                + "      \"Postcode\": \"SO21 2JN\"\n"
                + "    }\n"
                + "  },\n"
                + "  {\n"
                + "    \"Postcode|SO21 2JN\": {\n"
                + "      \"Street\": \"Hursley Park\",\n"
                + "      \"City\": \"Winchester\",\n"
                + "      \"Postcode\": \"SO21 2JN\"\n"
                + "    }\n"
                + "  }\n"
                + "]",
            null,
            "{\n"
                + "  \"FirstName\": \"Fred\",\n"
                + "  \"Address\": {\n"
                + "    \"Street\": \"Hursley Park\",\n"
                + "    \"City\": \"Winchester\",\n"
                + "    \"Postcode\": \"SO21 2JN\"\n"
                + "  }\n"
                + "}");
    }

    @Test
    public void eachWithFunctionWith4Args() throws Exception {
        test("($func := function($val, $key, $obj, $x) {{ $key & \"|\" & $val : $obj & $x}};"
            + "$each(Address, $func))",
            "[\n"
                + "  {\n"
                + "    \"Street|Hursley Park\": \"{\\\"Street\\\":\\\"Hursley Park\\\",\\\"City\\\":\\\"Winchester\\\",\\\"Postcode\\\":\\\"SO21 2JN\\\"}\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"City|Winchester\": \"{\\\"Street\\\":\\\"Hursley Park\\\",\\\"City\\\":\\\"Winchester\\\",\\\"Postcode\\\":\\\"SO21 2JN\\\"}\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"Postcode|SO21 2JN\": \"{\\\"Street\\\":\\\"Hursley Park\\\",\\\"City\\\":\\\"Winchester\\\",\\\"Postcode\\\":\\\"SO21 2JN\\\"}\"\n"
                + "  }\n"
                + "]",
            null,
            "{\n"
                + "  \"FirstName\": \"Fred\",\n"
                + "  \"Address\": {\n"
                + "    \"Street\": \"Hursley Park\",\n"
                + "    \"City\": \"Winchester\",\n"
                + "    \"Postcode\": \"SO21 2JN\"\n"
                + "  }\n"
                + "}");
    }

    @Test
    public void eachKeyValuePairIntoArrayWithJsonataFunction() throws Exception {
        test("$each(Address, $uppercase))",
            "[\n"
                + "  \"HURSLEY PARK\",\n"
                + "  \"WINCHESTER\",\n"
                + "  \"SO21 2JN\"\n"
                + "]",
            null,
            "{\n"
                + "  \"Address\": {\n"
                + "    \"Street\": \"Hursley Park\",\n"
                + "    \"City\": \"Winchester\",\n"
                + "    \"Postcode\": \"SO21 2JN\"\n"
                + "  }\n"
                + "})");
    }

    @Test
    public void combineWithMap() throws Exception {
        test("$map([{\"a\":1,\"value\":2},{\"b\":3,\"value\":4}],function($obj1){$each($obj1,function($v,$k){$k&\"=\"&$v})})",
            "[[\"a=1\", \"value=2\"], [\"b=3\", \"value=4\"]]", null, (String) null);
    }

    @Test
    public void agnosticTestSuiteCase002() throws Exception {
        test("$each(function($v, $k) {$k[$v>2]})",
            "[\"c\", \"d\"]",
            null,
            "{ \"a\": 1, \"b\": 2, \"c\": 3, \"d\": 4 }");
    }

    @Test
    public void undefindedFunction() throws Exception {
        test("$each(Address, $xyz))",
            null,
            String.format(EachFunction.ERR_ARG2_FUNCTION_RESOLVE, "$xyz"),
            "{\n"
                + "  \"Address\": {\n"
                + "    \"Street\": \"Hursley Park\",\n"
                + "    \"City\": \"Winchester\",\n"
                + "    \"Postcode\": \"SO21 2JN\"\n"
                + "  }\n"
                + "})");
    }

    @Test
    public void nullInput() throws Exception {
        test("$each()", null, EachFunction.ERR_ARG1BADTYPE, (String) null);
    }

    public void nullInput2() throws Exception {
        test("$each()", null, null, (String) null);
    }

    @Test
    public void nullInputFromChain() throws Exception {
        test("null ~> $each()", null, EachFunction.ERR_ARG1BADTYPE, (String) null);
    }

    @Test
    public void oneArgNoFunctionArg() throws Exception {
        test("$each(object)", null, EachFunction.ERR_ARG2BADTYPE, (String) null);
    }

    @Test
    public void oneArgNoObjectArg() throws Exception {
        test("$each(function($v, $k){{$k: $v}})", null, null, (String) null);
    }

    @Test
    public void twoArgsObjectNoMatch() throws Exception {
        test("$each(obj, function($v, $k){{$k: $v}})", null, null, (String) null);
    }

    @Test
    public void twoArgs1stWrongType() throws Exception {
        test("$each(2, function($v, $k){{$k: $v}})", null, EachFunction.ERR_ARG1BADTYPE, (String) null);
    }

    @Test
    public void twoArgs2ndWrongType() throws Exception {
        test("$each({\"key\":\"value\"}, 2)", "[]", EachFunction.ERR_ARG2BADTYPE, (String) null);
    }

    @Test
    public void twoArgs1stNoMatch2ndWrongType() throws Exception {
        test("$each(obj, 2)", null, EachFunction.ERR_ARG2BADTYPE, (String) null);
    }

    @Test
    public void threeArgs() throws Exception {
        test("$each({\"key\":\"value\"}, function($v, $k){{$k: $v}}, 2)", null, EachFunction.ERR_ARG3BADTYPE, (String) null);
    }

    @Test
    public void threeArgs1stNoMatch() throws Exception {
        test("$each(object, function($v, $k){{$k: $v}}, 2)", null, EachFunction.ERR_ARG3BADTYPE, (String) null);
    }

    @Test
    public void eachKeyValuePairIntoArray() throws Exception {
        test("$each(Address, function($v, $k) {$k & \": \" & $v})",
            "[\n"
                + "  \"Street: Hursley Park\",\n"
                + "  \"City: Winchester\",\n"
                + "  \"Postcode: SO21 2JN\"\n"
                + "]",
            null,
            "{\n"
                + "  \"FirstName\": \"Fred\",\n"
                + "  \"Address\": {\n"
                + "    \"Street\": \"Hursley Park\",\n"
                + "    \"City\": \"Winchester\",\n"
                + "    \"Postcode\": \"SO21 2JN\"\n"
                + "  }\n"
                + "}");
    }
}
