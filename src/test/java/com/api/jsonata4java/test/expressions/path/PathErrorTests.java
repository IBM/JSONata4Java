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

package com.api.jsonata4java.test.expressions.path;

import static com.api.jsonata4java.text.expressions.utils.Utils.test;

import org.junit.Test;

/**
 * A JUnit test class used in order to ease debugging of particular problematic
 * cases.
 * 
 * @author Martin Bluemel
 */
public class PathErrorTests {

	@Test
	public void testPathWithParent1() throws Exception {
		test("Account.Order.Product.{\n"
				+ "  'XProduct': $.'Product Name',\n"
				+ "  'XOrder': %.OrderID,\n"
				+ "  'XAccount': %.%.'Account Name'\n"
				+ "}",
				"["
				+ "  {\"XProduct\":\"Bowler Hat\"},"
				+ "  {\"XProduct\":\"Trilby hat\"},"
				+ "  {\"XProduct\":\"Bowler Hat\"},{\"XProduct\":\"Cloak\"}"
				+ "]",
				null, "{\n"
						+ "  \"Account\": {\n"
						+ "    \"Account Name\": \"Firefly\",\n"
						+ "    \"Order\": [\n"
						+ "      {\n"
						+ "        \"OrderID\": \"order103\",\n"
						+ "        \"Product\": [\n"
						+ "          {\n"
						+ "            \"Product Name\": \"Bowler Hat\"\n"
						+ "          },\n"
						+ "          {\n"
						+ "            \"Product Name\": \"Trilby hat\"\n"
						+ "          }\n"
						+ "        ]\n"
						+ "      },\n"
						+ "      {\n"
						+ "        \"OrderID\": \"order104\",\n"
						+ "        \"Product\": [\n"
						+ "          {\n"
						+ "            \"Product Name\": \"Bowler Hat\"\n"
						+ "          },\n"
						+ "          {\n"
						+ "            \"Product Name\": \"Cloak\"\n"
						+ "          }\n"
						+ "        ]\n"
						+ "      }\n"
						+ "    ]\n"
						+ "  }\n"
						+ "}");
	}

	@Test
	public void testPathWithParent2() throws Exception {
		test("Account.{\n"
				+ "  \"Products\": Order.Product.{\n"
				+ "    'XProduct': $.'Product Name',\n"
				+ "    'XOrder': %.OrderID,\n"
				+ "    'XAccount': %.%.'Account Name'\n"
				+ "  },"
				+ "\"YAccount\": $.'Account Name'"
				+ "}",
				"{"
				+ "  \"Products\":["
				+ "    {\"XProduct\":\"Bowler Hat\"},"
				+ "    {\"XProduct\":\"Trilby hat\"},"
				+ "    {\"XProduct\":\"Bowler Hat\"},{\"XProduct\":\"Cloak\"}"
				+ "  ],"
				+ "  \"YAccount\":\"Firefly\""
				+ "}",
				null, "{\n"
						+ "  \"Account\": {\n"
						+ "    \"Account Name\": \"Firefly\",\n"
						+ "    \"Order\": [\n"
						+ "      {\n"
						+ "        \"OrderID\": \"order103\",\n"
						+ "        \"Product\": [\n"
						+ "          {\n"
						+ "            \"Product Name\": \"Bowler Hat\"\n"
						+ "          },\n"
						+ "          {\n"
						+ "            \"Product Name\": \"Trilby hat\"\n"
						+ "          }\n"
						+ "        ]\n"
						+ "      },\n"
						+ "      {\n"
						+ "        \"OrderID\": \"order104\",\n"
						+ "        \"Product\": [\n"
						+ "          {\n"
						+ "            \"Product Name\": \"Bowler Hat\"\n"
						+ "          },\n"
						+ "          {\n"
						+ "            \"Product Name\": \"Cloak\"\n"
						+ "          }\n"
						+ "        ]\n"
						+ "      }\n"
						+ "    ]\n"
						+ "  }\n"
						+ "}");
	}
}
