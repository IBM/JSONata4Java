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

    private static final String INPUT_JSON_02 = "{\n"
        + "  \"root\": {\n"
        + "      \"services\": [\n"
        + "      {\n"
        + "        \"accountId\": 3,\n"
        + "        \"products\": [\n"
        + "          { \"contractId\": \"978999007\" },\n"
        + "          { \"contractId\": \"978999007\" }\n"
        + "        ]\n"
        + "      },\n"
        + "      {\n"
        + "        \"accountId\": 6,\n"
        + "        \"products\": [\n"
        + "          { \"contractId\": \"978999004\" }\n"
        + "        ]\n"
        + "      },\n"
        + "      {\n"
        + "        \"accountId\": 3,\n"
        + "        \"products\": [\n"
        + "          { \"contractId\": \"978999003\" },\n"
        + "          { \"contractId\": \"978999004\" },\n"
        + "          { \"contractId\": \"978999004\" }\n"
        + "        ]\n"
        + "      }\n"
        + "    ]\n"
        + "  }\n"
        + "}";

    @Test
    public void test_lowercase() throws Exception {
        test("{\n"
            + "    \"contacts\": [\n"
            + "        (groups.members.name\n"
            + "            ~> $distinct()\n"
            + "        ).($string()\n"
            + "             ~> $lowercase()"
            + "        )\n"
            + "    ]\n"
            + "}",
            "{\n"
                + "  \"contacts\": [\n"
                + "    \"olaf\",\n"
                + "    \"wladimir\",\n"
                + "    \"eugene\",\n"
                + "    \"joe\",\n"
                + "    \"kenta\"\n"
                + "  ]\n"
                + "}",
            null,
            INPUT_JSON_01);
    }

    @Test
    public void test_contains() throws Exception {
        test("{\n"
            + "    \"contacts\": [\n"
            + "        (groups.members.name\n"
            + "            ~> $distinct()\n"
            + "        ).($string()\n"
            + "             ~> $contains(/la/)"
            + "        )\n"
            + "    ]\n"
            + "}",
            "{\n"
                + "  \"contacts\": [\n"
                + "    true,\n"
                + "    true,\n"
                + "    false,\n"
                + "    false,\n"
                + "    false\n"
                + "  ]\n"
                + "}",
            null,
            INPUT_JSON_01);
    }

    @Test
    public void test_replace() throws Exception {
        test("{\n"
            + "    \"contacts\": [\n"
            + "        (groups.members.name\n"
            + "            ~> $distinct()\n"
            + "        ).($string()\n"
            + "             ~> $replace(/la/, \"OO\")"
            + "        )\n"
            + "    ]\n"
            + "}",
            "{\n"
                + "  \"contacts\": [\n"
                + "    \"OOOf\",\n"
                + "    \"WOOdimir\",\n"
                + "    \"Eugene\",\n"
                + "    \"Joe\",\n"
                + "    \"Kenta\"\n"
                + "  ]\n"
                + "}",
            null,
            INPUT_JSON_01);
    }

    @Test
    public void test_uppercase() throws Exception {
        test("{\n"
            + "    \"contacts\": [\n"
            + "        (groups.members.name\n"
            + "            ~> $distinct()\n"
            + "        ).("
            + "             $string()\n"
            + "             ~> $uppercase()"
            + "        )\n"
            + "    ]\n"
            + "}",
            "{\n"
                + "  \"contacts\": [\n"
                + "    \"OLAF\",\n"
                + "    \"WLADIMIR\",\n"
                + "    \"EUGENE\",\n"
                + "    \"JOE\",\n"
                + "    \"KENTA\"\n"
                + "  ]\n"
                + "}",
            null,
            INPUT_JSON_01);
    }

    @Test
    public void testFunctionNesting2SortDistinctInCustomFunction() throws Exception {
        test("(\n"
            + "    $root := root;\n"
            + "    $mapAccount := function($accountNumber) {\n"
            + "        {\n"
            + "            \"id\": $accountNumber,\n"
            + "            \"contractNumber\": [(\n"
            + "                $root.services[accountId = $accountNumber].products.contractId\n"
            + "                    ~> $distinct()\n"
            + "                    ~> $sort()\n"
            + "                )\n"
            + "            ]\n"
            + "        }\n"
            + "    };\n"
            + "    {\n"
            + "        \"account\": [(\n"
            + "            root.services.accountId\n"
            + "                ~> $distinct()\n"
            + "                ~> $sort()\n"
            + "            )\n"
            + "            .$mapAccount($)\n"
            + "        ]\n"
            + "    }\n"
            + ")",
            "{\n"
                + "  \"account\": [\n"
                + "    {\n"
                + "      \"id\": 3,\n"
                + "      \"contractNumber\": [\n"
                + "        \"978999003\",\n"
                + "        \"978999004\",\n"
                + "        \"978999007\"\n"
                + "      ]\n"
                + "    },\n"
                + "    {\n"
                + "      \"id\": 6,\n"
                + "      \"contractNumber\": [\n"
                + "        \"978999004\"\n"
                + "      ]\n"
                + "    }\n"
                + "  ]\n"
                + "}",
            null,
            INPUT_JSON_02);
    }

    @Test
    public void testFunctionNesting2SortDistinctInCustomFunctionSwapped() throws Exception {
        test("(\n"
            + "    $root := root;\n"
            + "    $mapAccount := function($accountNumber) {\n"
            + "        {\n"
            + "            \"id\": $accountNumber,\n"
            + "            \"contractNumber\": [(\n"
            + "                $root.services[accountId = $accountNumber].products.contractId\n"
            + "                    ~> $sort()\n"
            + "                    ~> $distinct()\n"
            + "                )\n"
            + "            ]\n"
            + "        }\n"
            + "    };\n"
            + "    {\n"
            + "        \"account\": [(\n"
            + "            root.services.accountId\n"
            + "                ~> $distinct()\n"
            + "                ~> $sort()\n"
            + "            )\n"
            + "            .$mapAccount($)\n"
            + "        ]\n"
            + "    }\n"
            + ")",
            "{\n"
                + "  \"account\": [\n"
                + "    {\n"
                + "      \"id\": 3,\n"
                + "      \"contractNumber\": [\n"
                + "        \"978999003\",\n"
                + "        \"978999004\",\n"
                + "        \"978999007\"\n"
                + "      ]\n"
                + "    },\n"
                + "    {\n"
                + "      \"id\": 6,\n"
                + "      \"contractNumber\": [\n"
                + "        \"978999004\"\n"
                + "      ]\n"
                + "    }\n"
                + "  ]\n"
                + "}",
            null,
            INPUT_JSON_02);
    }

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

    @Test
    public void testChainWithDistinctSortAndFilter() throws Exception {
        test("(\n"
            + "    $vkdGatewayResponse := vkdGatewayResponse;\n"
            + "    {\n"
            + "        \"account\": [\n"
            + "            (vkdGatewayResponse.customerInventory.serviceLines.recurringChargeSubAccountId\n"
            + "                ~> $distinct()\n"
            + "                ~> $sort()\n"
            + "                ~> $filter(function($v) {\n"
            + "                       $v > 1\n"
            + "                   })\n"
            + "            ).($n := $;\n"
            + "        {\n"
            + "            \"id\": $vkdGatewayResponse.customer.customerId & \"-\" & $n\n"
            + "        })]\n"
            + "    }\n"
            + ")\n"
            + "",
            "{\n"
                + "  \"account\": [\n"
                + "    {\n"
                + "      \"id\": \"131679999-2\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"id\": \"131679999-3\"\n"
                + "    }\n"
                + "  ]\n"
                + "}",
            null,
            "{\n"
                + "  \"vkdGatewayResponse\": {\n"
                + "    \"customer\": {\n"
                + "      \"customerId\": \"131679999\",\n"
                + "      \"crm\": \"VKD\"\n"
                + "     },\n"
                + "    \"customerInventory\": {\n"
                + "       \"serviceLines\": [\n"
                + "         {\n"
                + "           \"recurringChargeSubAccountId\": 3\n"
                + "         },\n"
                + "         {\n"
                + "           \"recurringChargeSubAccountId\": 1\n"
                + "         },\n"
                + "         {\n"
                + "           \"recurringChargeSubAccountId\": 2\n"
                + "         }\n"
                + "      ]\n"
                + "    }\n"
                + "  },\n"
                + "  \"customerBillingCreditResponse\": {\n"
                + "    \"status\": \"SUCCESS\",\n"
                + "    \"balanceList\": [\n"
                + "      {\n"
                + "        \"accountNumber\": 131679999,\n"
                + "        \"subAccountNumber\": 2,\n"
                + "        \"balanceValue\": \"72.64\"\n"
                + "      }\n"
                + "    ],\n"
                + "    \"totalBalance\": \"72.64\"\n"
                + "  }\n"
                + "}\n"
                + "");
    }

    @Test
    public void testChainWithDistinctAndSort2() throws Exception {
        test("(\n"
            + "    $vkdGatewayResponse := vkdGatewayResponse;\n"
            + "    {\n"
            + "        \"account\": [\n"
            + "            (vkdGatewayResponse.customerInventory.serviceLines.recurringChargeSubAccountId\n"
            + "                ~> $distinct()\n"
            + "                ~> $sort()\n"
            + "            ).($n := $;\n"
            + "        {\n"
            + "            \"id\": $vkdGatewayResponse.customer.customerId & \"-\" & $n\n"
            + "        })]\n"
            + "    }\n"
            + ")\n"
            + "",
            "{\n"
                + "  \"account\": [\n"
                + "  ]\n"
                + "}",
            null,
            "{\n"
                + "  \"vkdGatewayResponse\": {\n"
                + "    \"customer\": {\n"
                + "      \"customerId\": \"131679999\",\n"
                + "      \"crm\": \"VKD\"\n"
                + "     },\n"
                + "    \"customerInventory\": {\n"
                + "       \"serviceLines\": [\n"
                + "      ]\n"
                + "    }\n"
                + "  },\n"
                + "  \"customerBillingCreditResponse\": {\n"
                + "    \"status\": \"SUCCESS\",\n"
                + "    \"balanceList\": [\n"
                + "      {\n"
                + "        \"accountNumber\": 131679999,\n"
                + "        \"subAccountNumber\": 2,\n"
                + "        \"balanceValue\": \"72.64\"\n"
                + "      }\n"
                + "    ],\n"
                + "    \"totalBalance\": \"72.64\"\n"
                + "  }\n"
                + "}\n"
                + "");
    }
}
