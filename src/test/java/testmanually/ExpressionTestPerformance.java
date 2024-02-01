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

package testmanually;

import org.junit.Test;
import com.api.jsonata4java.expressions.Jsonata4JavaTestMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Performance test for big expressions.
 * 
 * @author Martin Bluemel
 */
public class ExpressionTestPerformance {

    private final ObjectMapper jsonMapper = new ObjectMapper();

    // measured on my Windows I7 developer box with openjdk 19
    // JSON nodes | generation | mapping
    // ---------------------------------
    //    500 000 |      0,5 s |   1,0 s
    //  1 000 000 |      1,0 s |   2,1 s
    //  2 000 000 |      1,9 s |   4,3 s
    //  3 000 000 |      3,0 s |   6,0 s
    @Test
    public void testWithBigInput() throws Exception {

        Jsonata4JavaTestMapper mapper = new Jsonata4JavaTestMapper("{"
            + "\"output\": [list.complex.{"
            + "\"name\": $"
            + "}]"
            + "}");

        System.out.println("generating big JSON");
        long startMillis = System.currentTimeMillis();
        JsonNode inputRootNode = generateBigJson();
        System.out.println("END: generating big JSON, took " + (System.currentTimeMillis() - startMillis) + " ms");

        System.out.println("mapping");
        startMillis = System.currentTimeMillis();
        mapper.map(inputRootNode);
        System.out.println("END: mapping, took " + (System.currentTimeMillis() - startMillis) + " ms");
    }

    // measured on my Windows I7 developer box with openjdk 19
    // JSON nodes | generation | mapping | indexing | ind % 
    // ----------------------------------------------------
    //    500 000 |      0,5 s |   2,2 s |  0,6 s   | 30 %
    //  1 000 000 |      0,8 s |   4,6 s |  1,3 s   | 30 %
    //  2 000 000 |      1,9 s |   8,7 s |  2,9 s   | 30 %
    //  2 500 000 |      2,0 s |   9,4 s |  3,1 s   | 30 %
    // indexof:    3,9 | 3,7 | 3,6 | 3,6 | 3,5
    // isObject(): 3,8 | 3,7 | 3,7 | 3,6 | 3,8
    @Test
    public void testWithBigInput_withParentPath() throws Exception {

        Jsonata4JavaTestMapper mapper = new Jsonata4JavaTestMapper("{"
            + "\"output\": [list.complex.{"
            + "\"id\": %.id,"
            + "\"name\": $"
            + "}]"
            + "}");

        System.out.println("generating big JSON");
        long startMillis = System.currentTimeMillis();
        JsonNode inputRootNode = generateBigJson();
        System.out.println("END: generating big JSON, took " + (System.currentTimeMillis() - startMillis) + " ms");

        System.out.println("mapping");
        startMillis = System.currentTimeMillis();
        mapper.map(inputRootNode);
        System.out.println("END: mapping, took " + (System.currentTimeMillis() - startMillis) + " ms");
    }

    private JsonNode generateBigJson() throws JsonProcessingException, JsonMappingException {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"list\":[");
        for (int i = 0; i < 2500000; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append("{");
            sb.append("\"id\": \"ID_" + Integer.toString(i) + "\",");
            sb.append("\"complex\": {");
            sb.append("\"name\": \"name_" + Integer.toString(i) + "\"");
            sb.append("}");
            sb.append("}");
        }
        sb.append("]}");
        JsonNode inputRootNode = jsonMapper.readTree(sb.toString());
        return inputRootNode;
    }
}
