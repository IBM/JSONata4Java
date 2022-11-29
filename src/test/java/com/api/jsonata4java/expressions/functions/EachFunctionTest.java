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
    public void nullInput() throws Exception {
        test("$each()", null, null, (String) null);
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

    @Test
    public void combineWithMap() throws Exception {
        test("$map([{\"a\":1,\"value\":2},{\"b\":3,\"value\":4}],function($obj1){$each($obj1,function($v,$k){$k&\"=\"&$v})})",
            "[[\"a=1\", \"value=2\"], [\"b=3\", \"value=4\"]]", null, (String) null);
    }
}
