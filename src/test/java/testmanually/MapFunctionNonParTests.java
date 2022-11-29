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

import static com.api.jsonata4java.expressions.utils.Utils.test;
import org.junit.Test;

/**
 * Agnostic (black box) tests for function $map with extra input.
 * 
 * @author Martin Bluemel
 */
public class MapFunctionNonParTests {

    //    @Test
    //    public void testExample() throws Exception {
    //        test("<JSONata expression>",
    //                "<output JSON>",
    //                null, "<input JSON>");
    //    }

    @Test
    public void test_issue228() throws Exception {
        test("${\n"
            + "    parentId: $map($, function($v, $i) {\n"
            + "        {\n"
            + "            \"id\" : $v.id,\n"
            + "            \"index\": $i\n"
            + "        }\n"
            + "    })\n"
            + "}",
            "{\n"
                + "  \"p1\" : [ {\n"
                + "    \"id\" : \"id1\",\n"
                + "    \"index\" : 0\n"
                + "  }, {\n"
                + "    \"id\" : \"id3\",\n"
                + "    \"index\" : 0\n"
                + "  } ],\n"
                + "  \"p2\" : {\n"
                + "    \"id\" : \"id2\",\n"
                + "    \"index\" : 0\n"
                + "  },\n"
                + "  \"p3\" : {\n"
                + "    \"id\" : \"i4\",\n"
                + "    \"index\" : 0\n"
                + "  },\n"
                + "  \"p4\" : [ {\n"
                + "    \"id\" : \"id5\",\n"
                + "    \"index\" : 0\n"
                + "  }, {\n"
                + "    \"id\" : \"id6\",\n"
                + "    \"index\" : 0\n"
                + "  } ]\n"
                + "}",
            null,
            "[\n"
                + "    {\n"
                + "      \"id\": \"id1\",\n"
                + "      \"parentId\": \"p1\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"id\": \"id2\",\n"
                + "      \"parentId\": \"p2\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"id\": \"id3\",\n"
                + "      \"parentId\": \"p1\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"id\": \"i4\",\n"
                + "      \"parentId\": \"p3\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"id\": \"id5\",\n"
                + "      \"parentId\": \"p4\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"id\": \"id6\",\n"
                + "      \"parentId\": \"p4\"\n"
                + "    }\n"
                + "]");
    }

    @Test
    public void test_issue228_2() throws Exception {
        test("[\n"
            + "    activities\n"
            + "    {\n"
            + "        activityParent: $map($,function($v, $i) {\n"
            + "            {\n"
            + "                \"number\": ($i+1),\n"
            + "                \"activityId\" : $v.activityId,\n"
            + "                \"params\" : ($i+1) = 1 ? \"param-for-one\" : \"param-for-two\"\n"
            + "            }\n"
            + "        })\n"
            + "    }\n"
            + "]",
            "[ {\n"
                + "  \"43e3b05b-8b9f-42d2-80ed-38b7294710ee\" : [ {\n"
                + "    \"number\" : 1,\n"
                + "    \"activityId\" : \"a021e643-e9e3-4ae3-afdf-5ad028b984fa\",\n"
                + "    \"params\" : \"param-for-one\"\n"
                + "  }, {\n"
                + "    \"number\" : 1,\n"
                + "    \"activityId\" : \"51a4553c-7b69-44db-a0d8-e3ca2fbf338e\",\n"
                + "    \"params\" : \"param-for-one\"\n"
                + "  } ],\n"
                + "  \"57bbb43e-ee4c-4448-a676-a6f261ccd959\" : {\n"
                + "    \"number\" : 1,\n"
                + "    \"activityId\" : \"a3bff694-2308-4e53-9f44-0d0460a8a389\",\n"
                + "    \"params\" : \"param-for-one\"\n"
                + "  },\n"
                + "  \"26cfafb1-34c3-4d1f-b973-20535b0b490e\" : {\n"
                + "    \"number\" : 1,\n"
                + "    \"activityId\" : \"79f29232-f47c-445e-8e02-2c0d665e38ac\",\n"
                + "    \"params\" : \"param-for-one\"\n"
                + "  },\n"
                + "  \"d21b315c-a2db-447a-a470-74756365cf5d\" : [ {\n"
                + "    \"number\" : 1,\n"
                + "    \"activityId\" : \"8c91cfc6-8a2d-4fc8-a624-dac0a8d75a3a\",\n"
                + "    \"params\" : \"param-for-one\"\n"
                + "  }, {\n"
                + "    \"number\" : 1,\n"
                + "    \"activityId\" : \"0f152bff-8021-4296-b73c-ef7fce1dbf7b\",\n"
                + "    \"params\" : \"param-for-one\"\n"
                + "  } ]\n"
                + "} ]",
            null,
            "{\n"
                + "  \"activities\": [\n"
                + "    {\n"
                + "      \"activityId\": \"a021e643-e9e3-4ae3-afdf-5ad028b984fa\",\n"
                + "      \"activityParent\": \"43e3b05b-8b9f-42d2-80ed-38b7294710ee\",\n"
                + "      \"id\": \"7c8cf666-6c25-42d3-899a-ba74161143d0\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"activityId\": \"a3bff694-2308-4e53-9f44-0d0460a8a389\",\n"
                + "      \"activityParent\": \"57bbb43e-ee4c-4448-a676-a6f261ccd959\",\n"
                + "      \"id\": \"60a4987e-6322-4393-9f28-6cb7c0aa013c\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"activityId\": \"51a4553c-7b69-44db-a0d8-e3ca2fbf338e\",\n"
                + "      \"activityParent\": \"43e3b05b-8b9f-42d2-80ed-38b7294710ee\",\n"
                + "      \"id\": \"d37d12d9-4555-4292-a197-d59066813ff8\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"activityId\": \"79f29232-f47c-445e-8e02-2c0d665e38ac\",\n"
                + "      \"activityParent\": \"26cfafb1-34c3-4d1f-b973-20535b0b490e\",\n"
                + "      \"id\": \"19e6308e-cd4f-4c46-a50e-a9d9eaa52d8f\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"activityId\": \"8c91cfc6-8a2d-4fc8-a624-dac0a8d75a3a\",\n"
                + "      \"activityParent\": \"d21b315c-a2db-447a-a470-74756365cf5d\",\n"
                + "      \"id\": \"003f6c20-a2fd-4458-98cd-287e931a9bd4\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"activityId\": \"0f152bff-8021-4296-b73c-ef7fce1dbf7b\",\n"
                + "      \"activityParent\": \"d21b315c-a2db-447a-a470-74756365cf5d\",\n"
                + "      \"id\": \"7aceeed9-f5ae-4888-9429-5fcf882c7045\"\n"
                + "    }\n"
                + "  ]\n"
                + "}");
    }
}
