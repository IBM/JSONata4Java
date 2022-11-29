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
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * Check we produce the same results as those given in the examples at
 * http://docs.jsonata.org. Although these examples do not achieve exhaustive
 * test coverage, they are useful for checking equivalence with JSONata and they
 * should make understanding failures a bit easier in some cases.
 * 
 */
@RunWith(Parameterized.class)
public class JsonataDotOrgTests implements Serializable {

    private static final long serialVersionUID = -6055514340828059658L;

    private static final String PERSON = "{\n" // 
        + "  \"FirstName\": \"Fred\",\n" // 
        + "  \"Surname\": \"Smith\",\n" //
        + "  \"Age\": 28,\n" // 
        + "  \"Address\": {\n" // 
        + "    \"Street\": \"Hursley Park\",\n" //
        + "    \"City\": \"Winchester\",\n" // 
        + "    \"Postcode\": \"SO21 2JN\"\n" // 
        + "  }, \n" // 
        + "  \"Phone\": [\n" //
        + "    {\n" // 
        + "      \"type\": \"home\",\n" //
        + "      \"number\": \"0203 544 1234\"\n" //
        + "    }, \n" //
        + "    {\n" //
        + "      \"type\": \"office\",\n" //
        + "      \"number\": \"01962 001234\"\n" //
        + "    }, \n" //
        + "    {\n" //
        + "      \"type\": \"office\",\n" //
        + "      \"number\": \"01962 001235\"\n" //
        + "    }, \n" //
        + "    {\n" //
        + "      \"type\": \"mobile\",\n" //
        + "      \"number\": \"077 7700 1234\"\n" //
        + "    }\n" //
        + "  ],\n" //
        + "  \"Email\": [\n" //
        + "    {\n" //
        + "      \"type\": \"work\",\n" //
        + "      \"address\": [\"fred.smith@my-work.com\", \"fsmith@my-work.com\"]\n" //
        + "    }, \n" //
        + "    {\n" //
        + "      \"type\": \"home\",\n" //
        + "      \"address\": [\"freddy@my-social.com\", \"frederic.smith@very-serious.com\"]\n" //
        + "    }\n" //
        + "  ],\n" //
        + "  \"Other\": {\n" //
        + "    \"Over 18 ?\": true,\n" //
        + "    \"Misc\": null,\n" //
        + "    \"Alternative.Address\": {\n" //
        + "      \"Street\": \"Brick Lane\",\n" //
        + "      \"City\": \"London\",\n" //
        + "      \"Postcode\": \"E1 6RF\"\n" //
        + "    }\n" //
        + "  }\n" //
        + "}"; //

    public static final String TOP_LEVEL_ARRAY = //
        "[\n" //
            + "  { \"ref\": [ 1,2 ] }, \n" //
            + "  { \"ref\": [ 3,4 ] }\n" //
            + "]"; //

    public static final String NUMBERS = //
        "{\"Numbers\": [1, 2.4, 3.5, 10, 20.9, 30] }";

    public static final String ACCOUNT = //
        "{\n" //
            + "  \"Account\": {\n" //
            + "    \"Account Name\": \"Firefly\",\n" //
            + "    \"Order\": [\n" //
            + "      {\n" //
            + "        \"OrderID\": \"order103\",\n" //
            + "        \"Product\": [\n" //
            + "          {\n" //
            + "            \"Product Name\": \"Bowler Hat\",\n" //
            + "            \"ProductID\": 858383,\n" //
            + "            \"SKU\": \"0406654608\",\n" //
            + "            \"Description\": {\n" //
            + "              \"Colour\": \"Purple\",\n" //
            + "              \"Width\": 300,\n" //
            + "              \"Height\": 200,\n" //
            + "              \"Depth\": 210,\n" //
            + "              \"Weight\": 0.75\n" //
            + "            }, \n" //
            + "            \"Price\": 34.45,\n" //
            + "            \"Quantity\": 2\n" //
            + "          }, \n" //
            + "          {\n" //
            + "            \"Product Name\": \"Trilby hat\",\n" //
            + "            \"ProductID\": 858236,\n" //
            + "            \"SKU\": \"0406634348\",\n" //
            + "            \"Description\": {\n" //
            + "              \"Colour\": \"Orange\",\n" //
            + "              \"Width\": 300,\n" //
            + "              \"Height\": 200,\n" //
            + "              \"Depth\": 210,\n" //
            + "              \"Weight\": 0.6\n" //
            + "            }, \n" //
            + "            \"Price\": 21.67,\n" //
            + "            \"Quantity\": 1\n" //
            + "          }\n" //
            + "        ]\n" //
            + "      },\n" //
            + "      {\n" //
            + "        \"OrderID\": \"order104\",\n" //
            + "        \"Product\": [\n" //
            + "          {\n" //
            + "            \"Product Name\": \"Bowler Hat\",\n" //
            + "            \"ProductID\": 858383,\n" //
            + "            \"SKU\": \"040657863\",\n" //
            + "            \"Description\": {\n" //
            + "              \"Colour\": \"Purple\",\n" //
            + "              \"Width\": 300,\n" //
            + "              \"Height\": 200,\n" //
            + "              \"Depth\": 210,\n" //
            + "              \"Weight\": 0.75\n" //
            + "            }, \n" //
            + "            \"Price\": 34.45,\n" //
            + "            \"Quantity\": 4\n" //
            + "          }, \n" //
            + "          {\n" //
            + "            \"ProductID\": 345664,\n" //
            + "            \"SKU\": \"0406654603\",\n" //
            + "            \"Product Name\": \"Cloak\",\n" //
            + "            \"Description\": {\n" //
            + "              \"Colour\": \"Black\",\n" //
            + "              \"Width\": 30,\n" //
            + "              \"Height\": 20,\n" //
            + "              \"Depth\": 210,\n" //
            + "              \"Weight\": 2\n" //
            + "            }, \n" //
            + "            \"Price\": 107.99,\n" //
            + "            \"Quantity\": 1\n" //
            + "          }\n" //
            + "        ]\n" //
            + "      }\n" //
            + "    ]\n" //
            + "  }\n" //
            + "}"; //

    public static String LIBRARY = //
        "	 {\n" //
            + "	   \"library\":{\n" //
            + "	   		\"books\":[\n" //
            + "	   			{\n" //
            + "	   				\"title\": \"The Big Bad World\",\n" //
            + "	   				\"authors\": [\"Aha\", \"Aho\"],\n" //
            + "	   				\"price\": 49,\n" //
            + "	   				\"section\":\"factual\"\n" //
            + "	   			}, \n" //
            + "	   			{\n" //
            + "	   				\"title\": \"Shelves for idiots\",\n" //
            + "	   				\"authors\": [\"Ben\"],\n" //
            + "	   				\"price\": 15,\n" //
            + "	   				\"section\":\"diy\"\n" //
            + "	   			}, \n" //
            + "	   			{\n" //
            + "	   				\"title\": \"Moby Dick\",\n" //
            + "	   				\"authors\": [\"Herman Melville\"],\n" //
            + "	   				\"price\": 9,\n" //
            + "	   				\"section\":\"fiction\"\n" //
            + "	   			}\n" //
            + "	   		]\n" //
            + "	   }\n" //
            + "	 }"; //

    @Parameter(0)
    public String stateJsonDoc;

    @Parameter(1)
    public String expression;

    @Parameter(2)
    public String expectedResultJsonString;

    @Parameters(name = "{1} -> {2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {

            // ====================================
            // = Basic Selection
            // = http://docs.jsonata.org/basic.html
            // ====================================

            // Navigating JSON Objects
            {
                PERSON, "Surname", "\"Smith\""
            }, //
            {
                PERSON, "Age", "28"
            }, //
            {
                PERSON, "Address.City", "\"Winchester\""
            }, //
            {
                PERSON, "Other.Misc", "null"
            }, // NullNode, not null!
            // {"Nothing", null} // we differ slightly from JSONata semantics here - JSONata
            // returns "undefined" (shown as *no match* in the exerciser), we throw a
            // runtime exception
            {
                PERSON, "Other.`Over 18 ?`", "true"
            }, //

            // Navigating JSON Arrays
            {
                PERSON, "Phone[0]", "{ \"type\": \"home\", \"number\": \"0203 544 1234\" }"
            }, //
            {
                PERSON, "Phone[1]", "{ \"type\": \"office\", \"number\": \"01962 001234\" }"
            }, //
            {
                PERSON, "Phone[-1]", "{ \"type\": \"mobile\", \"number\": \"077 7700 1234\" }"
            }, //
            {
                PERSON, "Phone[-2]", "{ \"type\": \"office\", \"number\": \"01962 001235\" }"
            }, //
            {
                PERSON, "Phone[8]", null
            }, //
            {
                PERSON, "Phone[0].number", "\"0203 544 1234\""
            }, //
            {
                PERSON, "Phone.number",
                "[ \"0203 544 1234\", \"01962 001234\", \"01962 001235\", \"077 7700 1234\" ]"
            }, //
            {
                PERSON, "Phone.number[0]",
                "[ \"0203 544 1234\", \"01962 001234\", \"01962 001235\", \"077 7700 1234\" ]"
            }, //
            {
                PERSON, "(Phone.number)[0]", "\"0203 544 1234\""
            }, //

            // Top level arrays, nested arrays and array flattening
            // NOT SUPPORTED AT PRESENT
            // {TOP_LEVEL_ARRAY, "[0]", "{ \"ref\": [ 1,2 ] }"}, //
            // {TOP_LEVEL_ARRAY, "[0].ref", "[1,2]"}, //
            // {TOP_LEVEL_ARRAY, "[0].ref[0]", "[1]"}, //
            // {TOP_LEVEL_ARRAY, "ref[0]", "[1,2,3,4]"}, //

            // ====================================
            // = Complex Selection
            // = http://docs.jsonata.org/complex.html
            // ====================================
            // Wildcards - not supported
            // Navigate arbitrary depths - not supported

            // Predicates
            {
                PERSON, "Phone[type='mobile']", "{ \"type\": \"mobile\",  \"number\": \"077 7700 1234\" }"
            }, //
            {
                PERSON, "Phone[type='mobile'].number", "\"077 7700 1234\""
            }, //
            {
                PERSON, "Phone[type='office'].number", "[ \"01962 001234\",  \"01962 001235\" ]"
            }, //

            // Singleton array and value equivalence
            // The first group of examples are just repeats from "Navigating JSON Objects"
            // The second group, however, demonstrate use of [] to modify the output
            // We don't support this yet - these statements won't parse yet
            //			{PERSON, "Address[].City", "[\"Winchester\"]"}, //
            //			{PERSON, "Address.City[]", "[\"Winchester\"]"}, //
            //			{PERSON, "Phone[0][].number", "[ \"0203 544 1234\" ]"}, //
            //			{PERSON, "Phone[][type='home'].number", "[ \"0203 544 1234\" ]"}, //
            //			{PERSON, "Phone[type='office'].number[]", "[\"01962001234\",\"01962001235\"]"}, //
            // etc...

            // ====================================
            // = Combining Values
            // = http://docs.jsonata.org/combining.html
            // ====================================

            // String expressions
            {
                PERSON, "FirstName & ' ' & Surname", "\"Fred Smith\""
            }, //
            {
                PERSON, "Address.(Street & ', ' & City)", "\"Hursley Park, Winchester\""
            }, //
            // not supported yet

            // Numeric expressions
            {
                NUMBERS, "Numbers[0] + Numbers[1]", "3.4"
            }, //
            {
                NUMBERS, "Numbers[0] - Numbers[4]", "-19.9"
            }, //
            {
                NUMBERS, "Numbers[0] * Numbers[5]", "30"
            }, //
            {
                NUMBERS, "Numbers[0] / Numbers[4]", "0.04784688995215311"
            }, //
            {
                NUMBERS, "Numbers[2] % Numbers[5]", "3.5"
            }, //

            // Comparison expressions
            {
                NUMBERS, "Numbers[0] = Numbers[5]", "false"
            }, //
            {
                NUMBERS, "Numbers[0] != Numbers[4]", "true"
            }, //
            {
                NUMBERS, "Numbers[0] < Numbers[5]", "true"
            }, //
            {
                NUMBERS, "Numbers[1] <= Numbers[5]", "true"
            }, //
            {
                NUMBERS, "Numbers[2] > Numbers[4]", "false"
            }, //
            {
                NUMBERS, "Numbers[2] >= Numbers[4]", "false"
            }, //
            {
                PERSON, "\"01962 001234\" in Phone.number", "true"
            }, //

            // Boolean expressions
            {
                NUMBERS, "(Numbers[2] != 0) and (Numbers[5] != Numbers[1])", "true"
            }, //
            {
                NUMBERS, "(Numbers[2] != 0) or (Numbers[5] = Numbers[1])", "true"
            }, //

            // ====================================
            // = Constructing output
            // = http://docs.jsonata.org/construction.html
            // ====================================

            // Array constructors
            // Although we support array constructors, the particular syntax used in the
            // examples is not yet supported

            // Array constructors can also be used within location paths for making multiple
            // selections without the broad brush use of wildcards.
            {
                PERSON, "[Address, Other.`Alternative.Address`].City", "[ \"Winchester\", \"London\" ]"
            }, //
            {
                PERSON, "Email.[address]",
                "[ [ \"fred.smith@my-work.com\",  \"fsmith@my-work.com\" ], [ \"freddy@my-social.com\", \"frederic.smith@very-serious.com\" ] ]"
            }, //

            // Object constructors
            // Although we support object constructors, the particular syntax used in the
            // examples is not yet supported
            // {PEOPLE, "Phone{type: number}", "{ \"home\": \"0203 544 1234\", \"office\":
            // \"01962 001235\", \"mobile\": \"077 7700 1234\" }"}, //
            // {PEOPLE, "Phone.{type: number}", "[ { \"home\": \"0203 544 1234\" }, // {
            // \"office\": \"01962 001234\" }, // { \"office\": \"01962 001235\" }, // {
            // \"mobile\": \"077 7700 1234\" } ]"}

            // JSON literals ("... any valid JSON document is also a valid expression")
            {
                null, "\"hello world\"", "\"hello world\""
            }, //
            {
                null, "34.5", "34.5"
            }, //
            {
                null, "true", "true"
            }, //
            {
                null, "false", "false"
            }, //
            // {null, "null", "null"} // not supported yet
            {
                null, "{\"key1\": \"value1\", \"key2\": \"value2\"}",
                "{\"key1\": \"value1\", \"key2\": \"value2\"}"
            }, //
            {
                null, "[\"value1\", \"value2\"]", "[\"value1\", \"value2\"]"
            }, //

            // ====================================
            // = Operators
            // = http://docs.jsonata.org/operators.html
            // ====================================

            // . (dot)
            {
                PERSON, "Address.City", "\"Winchester\""
            }, //
            {
                PERSON, "Phone.number",
                "[ \"0203 544 1234\", \"01962 001234\", \"01962 001235\", \"077 7700 1234\" ]"
            }, //
            {
                ACCOUNT, "Account.Order.Product.(Price * Quantity)", "[ 68.9, 21.67, 137.8, 107.99 ]"
            }, //
            // {ACCOUNT, "Account.Order.OrderID.$uppercase()", "[ \"ORDER103\",
            // \"ORDER104\"]"}, //

            // ~> (function chaining)
            // not supported

            // ^(…) (order-by)
            // not supported

            // & + - * / % = != > < >= <=
            {
                null, "\"Hello\" & \"World\"", "\"HelloWorld\""
            }, //
            {
                null, "5 + 2", "7"
            }, //
            {
                null, "5 - 2", "3"
            }, //
            {
                null, "- 42", "-42"
            }, //
            {
                null, "5 * 2", "10"
            }, //
            {
                null, "5.0 / 2", "2.5"
            }, //
            {
                null, "5 % 2", "1"
            }, //
            {
                null, "1+1 = 2", "true"
            }, //
            {
                null, "1+1 != 3", "true"
            }, //
            {
                null, "\"Hello\" != \"World\"", "true"
            }, //
            {
                null, "22.0/7 > 3", "true"
            }, //
            {
                null, "5>5", "false"
            }, //
            {
                null, "22.0/7 < 3", "false"
            }, //
            {
                null, "5<5", "false"
            }, //
            {
                null, "22.0/7 >= 3", "true"
            }, //
            {
                null, "5>=5", "true"
            }, //
            {
                null, "22.0/7 <= 3", "false"
            }, //
            {
                null, "5<=5", "true"
            }, //

            // in
            {
                null, "\"world\" in [\"hello\", \"world\"]", "true"
            }, //
            {
                null, "\"hello\" in \"hello\"", "true"
            }, //

            // and
            {
                LIBRARY, "library.books[\"Aho\" in authors and price < 50].title", "\"The Big Bad World\""
            }, //

            // or
            {
                LIBRARY, "library.books[price < 10 or section=\"diy\"].title",
                "[\"Shelves for idiots\", \"Moby Dick\"]"
            }, //

            // .. (sequence)
            {
                null, "[1..5]", "[1,2,3,4,5]"
            }, //
            {
                null, "[1..3, 7..9]", "[1, 2, 3, 7, 8, 9]"
            }, //
            // [1..$count(Items)].("Item " & $)==["Item 1","Item 2","Item 3"]
            // [1..5].($*$)==[1, 4, 9, 16, 25]

            // ? : (conditional ternary)
            {
                LIBRARY, "library.books[0].price < 50 ? \"Cheap\" : \"Expensive\"", "\"Cheap\""
            }, //

            // := (variable binding)
            // not supported

            // ====================================
            // = Function Library
            // = http://docs.jsonata.org/operators.html
            // ====================================
            // $substring
            {
                null, "$substring(\"Hello World\", 3)", "\"lo World\""
            }, //
            {
                null, "$substring(\"Hello World\", 3, 5)", "\"lo Wo\""
            }, //
            {
                null, "$substring(\"Hello World\", -4)", "\"orld\""
            }, //
            {
                null, "$substring(\"Hello World\", -4, 2)", "\"or\""
            }, //

        });
    }

    @Test
    public void runTest() throws Exception {
        test(this.expression, expectedResultJsonString, null, stateJsonDoc);
    }

}
