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

package com.api.jsonata4java.test.expressions;

import static com.api.jsonata4java.text.expressions.utils.Utils.test;
import org.junit.Test;

/**
 * Agnostic (black box) tests with parent path operator %.
 * 
 * @author Martin Bluemel
 */
public class FunctionChainingTests {

    //    @Test
    //    public void testExample() throws Exception {
    //        test("<JSONata expression>",
    //                "<output JSON>",
    //                null, "<input JSON>");
    //    }

    private static final String INPUT_JSON_01 = "{\n"
        + "  \"listname\": \"myAddressBook\",\n"
        + "  \"groups\": [\n"
        + "    {\n"
        + "      \"name\": \"contacts\",\n"
        + "      \"members\": [\n"
        + "        {\n"
        + "          \"name\": \"Olaf\"\n"
        + "        },\n"
        + "        {\n"
        + "          \"name\": \"Wladimir\"\n"
        + "        },\n"
        + "        {\n"
        + "          \"name\": \"Eugene\"\n"
        + "        },\n"
        + "        {\n"
        + "          \"name\": \"Joe\"\n"
        + "        }\n"
        + "      ]\n"
        + "    },\n"
        + "    {\n"
        + "      \"name\": \"friends\",\n"
        + "      \"members\": [\n"
        + "        {\n"
        + "          \"name\": \"Eugene\"\n"
        + "        },\n"
        + "        {\n"
        + "          \"name\": \"Kenta\"\n"
        + "        }\n"
        + "      ]\n"
        + "    }\n"
        + "  ]\n"
        + "}";

    @Test
    public void testFunctionNesting1Sort() throws Exception {
        test("{\n"
            + "    \"contacts\": [\n"
            + "        $sort(\n"
            + "          groups.members.name"
            + "        )\n"
            + "    ]\n"
            + "}",
            "{\n"
                + "  \"contacts\": [\n"
                + "    \"Eugene\",\n"
                + "    \"Eugene\",\n"
                + "    \"Joe\",\n"
                + "    \"Kenta\",\n"
                + "    \"Olaf\",\n"
                + "    \"Wladimir\"\n"
                + "  ]\n"
                + "}",
            null,
            INPUT_JSON_01);
    }

    @Test
    public void testFunctionNesting1Distinct() throws Exception {
        test("{\n"
            + "    \"contacts\": [\n"
            + "        $distinct(\n"
            + "          groups.members.name"
            + "        )\n"
            + "    ]\n"
            + "}",
            "{\n"
                + "  \"contacts\": [\n"
                + "    \"Olaf\",\n"
                + "    \"Wladimir\",\n"
                + "    \"Eugene\",\n"
                + "    \"Joe\",\n"
                + "    \"Kenta\"\n"
                + "  ]\n"
                + "}",
            null,
            INPUT_JSON_01);
    }

    @Test
    public void testFunctionNesting2() throws Exception {
        test("{\n"
            + "    \"contacts\": [\n"
            + "        $sort(\n"
            + "          $distinct(\n"
            + "            groups.members.name"
            + "          )\n"
            + "        )\n"
            + "    ]\n"
            + "}",
            "{\n"
                + "  \"contacts\": [\n"
                + "    \"Eugene\",\n"
                + "    \"Joe\",\n"
                + "    \"Kenta\",\n"
                + "    \"Olaf\",\n"
                + "    \"Wladimir\"\n"
                + "  ]\n"
                + "}",
            null,
            INPUT_JSON_01);
    }

    @Test
    public void testChainWith1Sort() throws Exception {
        test("{\n"
            + "    \"contacts\": [\n"
            + "        groups.members.name\n"
            + "            ~> $sort()\n"
            + "    ]\n"
            + "}",
            "{\n"
                + "  \"contacts\": [\n"
                + "    \"Eugene\",\n"
                + "    \"Eugene\",\n"
                + "    \"Joe\",\n"
                + "    \"Kenta\",\n"
                + "    \"Olaf\",\n"
                + "    \"Wladimir\"\n"
                + "  ]\n"
                + "}",
            null,
            INPUT_JSON_01);
    }

    @Test
    public void testChainWith1Distinct() throws Exception {
        test("{\n"
            + "    \"contacts\": [\n"
            + "        groups.members.name\n"
            + "            ~> $distinct()\n"
            + "    ]\n"
            + "}",
            "{\n"
                + "  \"contacts\": [\n"
                + "    \"Olaf\",\n"
                + "    \"Wladimir\",\n"
                + "    \"Eugene\",\n"
                + "    \"Joe\",\n"
                + "    \"Kenta\"\n"
                + "  ]\n"
                + "}",
            null,
            INPUT_JSON_01);
    }

    @Test
    public void testChainWith2() throws Exception {
        test("(\n"
            + "    $replaceX := function($urn) {\n"
            + "      $replace($urn, /[Oo]/, \"0\")"
            + "    };\n"
            + "{\n"
            + "    \"contacts\": [\n"
            + "        (groups.members.name\n"
            + "            ~> $sort()"
            + "            ~> $distinct()\n"
            + "        ).$replaceX($)\n"
            + "    ]\n"
            + "})",
            "{\n"
                + "  \"contacts\": [\n"
                + "    \"Eugene\",\n"
                + "    \"J0e\",\n"
                + "    \"Kenta\",\n"
                + "    \"0laf\",\n"
                + "    \"Wladimir\"\n"
                + "  ]\n"
                + "}",
            null,
            INPUT_JSON_01);
    }

    @Test
    public void testChainWith2swap() throws Exception {
        test("{\n"
            + "    \"contacts\": [\n"
            + "        groups.members.name\n"
            + "            ~> $sort()\n"
            + "            ~> $distinct()\n"
            + "    ]\n"
            + "}",
            "{\n"
                + "  \"contacts\": [\n"
                + "    \"Eugene\",\n"
                + "    \"Joe\",\n"
                + "    \"Kenta\",\n"
                + "    \"Olaf\",\n"
                + "    \"Wladimir\"\n"
                + "  ]\n"
                + "}",
            null,
            INPUT_JSON_01);
    }
}
