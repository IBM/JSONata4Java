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

public class MergeFunctionTest {

    @Test
    public void nullInput() throws Exception {
        test("$merge()", null, null, (String) null);
    }

    @Test
    public void emptyInput() throws Exception {
        test("null ~> $merge()", null, MergeFunction.ERR_BAD_CONTEXT, (String) null);
    }

    @Test
    public void inputDoesNotExist() throws Exception {
        test("$merge(a.b)", null, null, (String) null);
    }

    @Test
    public void mergeObject() throws Exception {
        test("$merge({\"k1\": \"v1\", \"k2\": \"v2\"})", "{\"k1\": \"v1\", \"k2\": \"v2\"}", null, (String) null);
    }

    @Test
    public void mergeTwoDifferentKeyValuePairs() throws Exception {
        test("$merge([{\"a\":1},{\"b\":2}])", "{\"a\":1, \"b\":2}", null, (String) null);
    }

    @Test
    public void mergeSameKeyValuePairs() throws Exception {
        test("$merge([{\"a\":1},{\"a\":2}])", "{\"a\":2}", null, (String) null);
    }

    @Test
    public void mergeMixed() throws Exception {
        test("$merge([{\"a\":1,\"value\":2},{\"b\":{\"value\":{\"d\":5},\"c\":5}},{\"a\":2}])",
            "{\"a\":2, \"value\":2, \"b\":{\"value\":{\"d\":5},\"c\":5}}", null, (String) null);
    }

    @Test
    public void badArgType() throws Exception {
        test("$merge(2)", null, MergeFunction.ERR_ARG1BADTYPE, (String) null);
    }

    @Test
    public void twoArguments() throws Exception {
        test("$merge({\"k1\":\"v1\"},{\"k2\":\"v2\"})", null, MergeFunction.ERR_ARG2BADTYPE, (String) null);
    }

    @Test
    public void twoArgumentsOnePerChain() throws Exception {
        test("{\"k2\":\"v2\"} ~> $merge({\"k1\":\"v1\"})", null, MergeFunction.ERR_ARG1BADTYPE, (String) null);
    }

    @Test
    public void arrayWithOtherThanObject() throws Exception {
        test("$merge([1, 2])", null, MergeFunction.ERR_ARG1_MUST_BE_OBJECT_ARRAY, (String) null);
    }

    @Test
    public void issue237InconsistentQuotationMarkSanitization() throws Exception {
        test("$each($, function($v, $k) {({ $k: $v})}) ~> $merge()", "{\"key1\":\"\\\"v1\\\"\"}", null, "{ \"key1\": \"\\\"v1\\\"\" }");
    }
}
