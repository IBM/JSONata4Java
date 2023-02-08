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

package com.api.jsonata4java.expressions.utils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Runs the test cases defined in src/test/resources/JsonMergeUtilsTest.json
 */
@RunWith(Parameterized.class)
public class JsonMergeUtilsTest implements Serializable {

    private static final long serialVersionUID = -9110750617460131902L;

    public static String TESTCASES_JSON_PATH = "./src/test/resources/JsonMergeUtilsTest.json";
    static ObjectMapper OM = new ObjectMapper();

    @Parameter(0)
    public JsonNode x;

    @Parameter(1)
    public JsonNode y;

    @Parameter(2)
    public JsonNode expected;

    @Parameters(name = "{0} <- {1} = {2}")
    public static Collection<Object[]> data() throws Exception {
        List<Object[]> data = new ArrayList<>();

        JsonNode testCasesJson = OM.readTree(new File(TESTCASES_JSON_PATH));

        for (JsonNode testCaseJson : testCasesJson) {
            data.add(new Object[] {
                testCaseJson.get("x"),
                testCaseJson.get("y"),
                testCaseJson.get("expected"),
            });
        }

        return data;
    }

    @Test
    public void testCase() throws Exception {

        final JsonNode actual = JsonMergeUtils.merge(x, y);
        Assert.assertEquals(expected, actual);

    }

    //	public static JsonNode merge(String x, String y) throws JsonProcessingException, IOException {
    //		return merge(, OM.readTree(y));
    //	}
    //	
    //	// TODO: turn these into unit tests
    //	public static void main(String[] args) throws Exception{
    //		System.out.println( merge("{\"a\":1}", "{\"a\":2, \"b\":3}") );
    //		System.out.println( merge("{\"a\":{\"x\":2}}", "{\"a\":{\"x\":4, \"y\":5}, \"b\":3}") );
    //		System.out.println( merge("[1,2]", "[3,4,5]") );
    //		System.out.println( merge("[1,2,6]", "[1,2]") );
    //		System.out.println( merge("[1,2,6]", "[1,2,5]") );
    //		System.out.println( merge("[{\"a\":1}]", "[{\"b\":2}, {\"a\":1}]") );
    //	}

    //	private static void runTest(String xstr, String ystr, String expectedstr) throws Exception{
    //		final JsonNode x = OM.readTree(xstr);
    //		final JsonNode y = OM.readTree(ystr);
    //		final JsonNode expected = OM.readTree(expectedstr);
    //		final JsonNode actual = JsonMergeUtils.merge(x,y);
    //		Assert.assertEquals(expected, actual);
    //	}
}
