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
public class PathExpressionParentTests {

    //    @Test
    //    public void testExample() throws Exception {
    //        test("<JSONata expression>",
    //                "<output JSON>",
    //                null, "<input JSON>");
    //    }

    @Test
    public void testParentPathWithParentSolitary() throws Exception {
        test("root.%",
            "{\n"
                + "  \"root\": {\n"
                + "    \"a\": \"xxx\"\n"
                + "  }\n"
                + "}",
            null, "{\n"
                + "  \"root\": {\n"
                + "    \"a\": \"xxx\"\n"
                + "  }\n"
                + "}");
    }

    @Test
    public void testParentPathWithParentSimple() throws Exception {
        test("{\n"
            + "    \"contacts\": [\n"
            + "        groups.members.{\n"
            + "            \"name\": name,\n"
            + "            \"city\": address.city,\n"
            + "            \"group\": %.name,\n"
            + "            \"list\": %.%.listname\n"
            + "        }\n"
            + "    ]\n"
            + "}",
            "{\n"
                + "  \"contacts\": [\n"
                + "    {\n"
                + "      \"name\": \"Eugene\",\n"
                + "      \"city\": \"Mannheim\",\n"
                + "      \"group\": \"friends\",\n"
                + "      \"list\": \"myAddressBook\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"name\": \"Kenji\",\n"
                + "      \"city\": \"Tokyo\",\n"
                + "      \"group\": \"friends\",\n"
                + "      \"list\": \"myAddressBook\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"name\": \"Olaf\",\n"
                + "      \"city\": \"Berlin\",\n"
                + "      \"group\": \"contacts\",\n"
                + "      \"list\": \"myAddressBook\"\n"
                + "    }\n"
                + "  ]\n"
                + "}",
            null,
            "{\n"
                + "  \"listname\": \"myAddressBook\",\n"
                + "  \"groups\": [\n"
                + "    {\n"
                + "      \"name\": \"friends\",\n"
                + "      \"members\": [\n"
                + "        {\n"
                + "          \"name\": \"Eugene\",\n"
                + "          \"address\": {\n"
                + "            \"city\": \"Mannheim\"\n"
                + "          }\n"
                + "        },\n"
                + "        {\n"
                + "          \"name\": \"Kenji\",\n"
                + "          \"address\": {\n"
                + "            \"city\": \"Tokyo\"\n"
                + "          }\n"
                + "        }\n"
                + "      ]\n"
                + "    },\n"
                + "    {\n"
                + "      \"name\": \"contacts\",\n"
                + "      \"members\": [\n"
                + "        {\n"
                + "          \"name\": \"Olaf\",\n"
                + "          \"address\": {\n"
                + "            \"city\": \"Berlin\"\n"
                + "          }\n"
                + "        }\n"
                + "      ]\n"
                + "    }\n"
                + "  ]\n"
                + "}");
    }

    @Test
    public void testPathWithParent01() throws Exception {
        test("Account.Order.Product.{"
            + "  'XProduct': $.'Product Name',"
            + "  'XOrder': %.OrderID,"
            + "  'XAccount': %.%.'Account Name'"
            + "}",
            "["
                + "  {\"XProduct\":\"Bowler Hat\",\"XOrder\":\"order104\",\"XAccount\":\"Account Name\"},"
                + "  {\"XProduct\":\"Trilby hat\",\"XOrder\":\"order103\",\"XAccount\":\"Account Name\"},"
                + "  {\"XProduct\":\"Bowler Hat\",\"XOrder\":\"order104\",\"XAccount\":\"Account Name\"},"
                + "  {\"XProduct\":\"Cloak\",\"XOrder\":\"order104\",\"XAccount\":\"Account Name\"}"
                + "]",
            null, "{"
                + "  \"Account\": {"
                + "    \"Account Name\": \"Firefly\","
                + "    \"Order\": ["
                + "      {"
                + "        \"OrderID\": \"order103\","
                + "        \"Product\": ["
                + "          {"
                + "            \"Product Name\": \"Bowler Hat\""
                + "          },"
                + "          {"
                + "            \"Product Name\": \"Trilby hat\""
                + "          }"
                + "        ]"
                + "      },"
                + "      {"
                + "        \"OrderID\": \"order104\","
                + "        \"Product\": ["
                + "          {"
                + "            \"Product Name\": \"Bowler Hat\""
                + "          },"
                + "          {"
                + "            \"Product Name\": \"Cloak\""
                + "          }"
                + "        ]"
                + "      }"
                + "    ]"
                + "  }"
                + "}");
    }

    @Test
    public void testPathWithParent02() throws Exception {
        test("Account.{"
            + "  \"Products\": Order.Product.{"
            + "    'XProduct': $.'Product Name',"
            + "    'XOrder': %.OrderID,"
            + "    'XAccount': %.%.'Account Name'"
            + "  },"
            + "\"YAccount\": $.'Account Name'"
            + "}",
            "{"
                + "  \"Products\":["
                + "    {\"XProduct\":\"Bowler Hat\",\"XOrder\":\"order104\",\"XAccount\":\"Account Name\"},"
                + "    {\"XProduct\":\"Trilby hat\",\"XOrder\":\"order103\",\"XAccount\":\"Account Name\"},"
                + "    {\"XProduct\":\"Bowler Hat\",\"XOrder\":\"order104\",\"XAccount\":\"Account Name\"},"
                + "    {\"XProduct\":\"Cloak\",\"XOrder\":\"order104\",\"XAccount\":\"Account Name\"}"
                + "  ],"
                + "  \"YAccount\":\"Firefly\""
                + "}",
            null, "{"
                + "  \"Account\": {"
                + "    \"Account Name\": \"Firefly\","
                + "    \"Order\": ["
                + "      {"
                + "        \"OrderID\": \"order103\","
                + "        \"Product\": ["
                + "          {"
                + "            \"Product Name\": \"Bowler Hat\""
                + "          },"
                + "          {"
                + "            \"Product Name\": \"Trilby hat\""
                + "          }"
                + "        ]"
                + "      },"
                + "      {"
                + "        \"OrderID\": \"order104\","
                + "        \"Product\": ["
                + "          {"
                + "            \"Product Name\": \"Bowler Hat\""
                + "          },"
                + "          {"
                + "            \"Product Name\": \"Cloak\""
                + "          }"
                + "        ]"
                + "      }"
                + "    ]"
                + "  }"
                + "}");
    }

    @Test
    public void testPathWithParent03() throws Exception {

        test("data.xxb.datavalue.{"
            + "    \"properties\": {\"value\":value},"
            + "    \"timestamp\": timestamp,"
            + "    \"mId\": %.%.xxa.module,"
            + "    \"oid\": %.%.name"
            + "}",
            "["
                + "  {"
                + "    \"properties\": {"
                + "      \"value\": 0.24"
                + "    },"
                + "    \"timestamp\": 1664339583,"
                + "    \"mId\": 2122,"
                + "    \"oid\": \"root-type:p\""
                + "  },"
                + "  {"
                + "    \"properties\": {"
                + "      \"value\": 0.25"
                + "    },"
                + "    \"timestamp\": 1664341325,"
                + "    \"mId\": 2122,"
                + "    \"oid\": \"root-type:p\""
                + "  }"
                + "]",
            null, "{"
                + "  \"data\": ["
                + "    {"
                + "      \"name\": \"root-type:p\","
                + "      \"xxa\": {"
                + "        \"module\": 2122"
                + "      },"
                + "      \"xxb\": ["
                + "        {"
                + "          \"datavalue\": ["
                + "            {"
                + "              \"value\": 0.24,"
                + "              \"timestamp\": 1664339583"
                + "            },"
                + "            {"
                + "              \"value\": 0.25,"
                + "              \"timestamp\": 1664341325"
                + "            }"
                + "          ]"
                + "        }"
                + "      ]"
                + "    }"
                + "  ]"
                + "}");
    }

    @Test
    public void testPathWithParent4() throws Exception {

        test("data.metrics.history.{"
            + "       \"objectId\":%.%.tags[0],"
            + "       \"test\":\"test\""
            + "}",
            "["
                + "  {"
                + "    \"objectId\": \"module=yq01-d2\","
                + "    \"test\": \"test\""
                + "  },"
                + "  {"
                + "    \"objectId\": \"module=yq01-d2\","
                + "    \"test\": \"test\""
                + "  }"
                + "]",
            null, "{"
                + "    \"status\": 0,"
                + "    \"message\": \"OK\","
                + "    \"data\": ["
                + "        {"
                + "            \"product\": \"ipcd\","
                + "            \"namespace\": \"carr2_amps_%.pv\","
                + "            \"tags\": ["
                + "                \"module=yq01-d2\",\"module=yq01-d3\""
                + "            ],"
                + "            \"metrics\": ["
                + "                {"
                + "                    \"metric\": \"value\","
                + "                    \"histoCount\": 1,"
                + "                    \"history\": ["
                + "                        {"
                + "                            \"value\": 0.0,"
                + "                            \"timestamp\": 1660046403"
                + "                        },"
                + "                        {"
                + "                            \"value\": 0.0,"
                + "                            \"timestamp\": 1660046414"
                + "                        }"
                + "                    ]"
                + "                }"
                + "            ]"
                + "        }"
                + "    ]"
                + "}");
    }
}
