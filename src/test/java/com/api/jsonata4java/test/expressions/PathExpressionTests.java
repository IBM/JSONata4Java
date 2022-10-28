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
 * A JUnit test class used in order to ease debugging of particular problematic
 * cases.
 * 
 * @author Martin Bluemel
 */
public class PathExpressionTests {

	@Test
	public void testPathWithParent1() throws Exception {
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
	public void testPathWithParent2() throws Exception {
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
	public void testPathWithParent3() throws Exception {

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
