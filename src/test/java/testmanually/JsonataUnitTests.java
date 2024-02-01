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
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Verify behavior when access special $state, $event and $instance variables.
 */
@RunWith(Parameterized.class)
public class JsonataUnitTests implements Serializable {

    private static final long serialVersionUID = -862598258330086928L;

    public static String testdata1 = "    \"foo\": {\n" + "        \"bar\": 42,\n"
        + "        \"blah\": [{\"baz\": {\"fud\": \"hello\"}}, // {\"baz\": {\"fud\": \"world\"}}, // {\"bazz\": \"gotcha\"}],\n"
        + "        \"blah.baz\": \"here\"\n" + "    }, // \"bar\": 98";

    public static String testdata1a = "{\n" + "    \"foo\": {\n" + "        \"bar\": 42,\n"
        + "        \"blah\": [{\"baz\": {\"fud\": \"hello\"}}, // {\"buz\": {\"fud\": \"world\"}}, // {\"bazz\": \"gotcha\"}],\n"
        + "        \"blah.baz\": \"here\"\n" + "    }, // \"bar\": 98\n" + "}";

    public static String testdata2 = "   \"Account\": {\n" + "        \"Account Name\": \"Firefly\",\n"
        + "        \"Order\": [\n" + "            {\n" + "                \"OrderID\": \"order103\",\n"
        + "                \"Product\": [\n" + "                    {\n"
        + "                        \"Product Name\": \"Bowler Hat\",\n"
        + "                        \"ProductID\": 858383,\n" + "                        \"SKU\": \"0406654608\",\n"
        + "                        \"Description\": {\n" + "                            \"Colour\": \"Purple\",\n"
        + "                            \"Width\": 300,\n" + "                            \"Height\": 200,\n"
        + "                            \"Depth\": 210,\n" + "                            \"Weight\": 0.75\n"
        + "                        }, //\n" + "                        \"Price\": 34.45,\n"
        + "                        \"Quantity\": 2\n" + "                    }, //\n" + "                    {\n"
        + "                        \"Product Name\": \"Trilby hat\",\n"
        + "                        \"ProductID\": 858236,\n" + "                        \"SKU\": \"0406634348\",\n"
        + "                        \"Description\": {\n" + "                            \"Colour\": \"Orange\",\n"
        + "                            \"Width\": 300,\n" + "                            \"Height\": 200,\n"
        + "                            \"Depth\": 210,\n" + "                            \"Weight\": 0.6\n"
        + "                        }, //\n" + "                        \"Price\": 21.67,\n"
        + "                        \"Quantity\": 1\n" + "                    }\n" + "                ]\n"
        + "            }, //\n" + "            {\n" + "                \"OrderID\": \"order104\",\n"
        + "                \"Product\": [\n" + "                    {\n"
        + "                        \"Product Name\": \"Bowler Hat\",\n"
        + "                        \"ProductID\": 858383,\n" + "                        \"SKU\": \"040657863\",\n"
        + "                        \"Description\": {\n" + "                            \"Colour\": \"Purple\",\n"
        + "                            \"Width\": 300,\n" + "                            \"Height\": 200,\n"
        + "                            \"Depth\": 210,\n" + "                            \"Weight\": 0.75\n"
        + "                        }, //\n" + "                        \"Price\": 34.45,\n"
        + "                        \"Quantity\": 4\n" + "                    }, //\n" + "                    {\n"
        + "                        \"ProductID\": 345664,\n" + "                        \"SKU\": \"0406654603\",\n"
        + "                        \"Product Name\": \"Cloak\",\n" + "                        \"Description\": {\n"
        + "                            \"Colour\": \"Black\",\n" + "                            \"Width\": 30,\n"
        + "                            \"Height\": 20,\n" + "                            \"Depth\": 210,\n"
        + "                            \"Weight\": 2.0\n" + "                        }, //\n"
        + "                        \"Price\": 107.99,\n" + "                        \"Quantity\": 1\n"
        + "                    }\n" + "                ]\n" + "            }\n" + "        ]\n" + "    }\n" + "}";

    public static String testdata3 = "{\n" + "    \"nest0\": [\n"
        + "        {\"nest1\": [{\"nest2\": [{\"nest3\": [1]}, // {\"nest3\": [2]}]}, // {\"nest2\": [{\"nest3\": [3]}, // {\"nest3\": [4]}]}]}, //\n"
        + "        {\"nest1\": [{\"nest2\": [{\"nest3\": [5]}, // {\"nest3\": [6]}]}, // {\"nest2\": [{\"nest3\": [7]}, // {\"nest3\": [8]}]}]}\n"
        + "    ]\n" + "}";

    public static String testdata3a = "[\n" + "    {\"nest0\": [1, 2]}, //\n" + "    {\"nest0\": [3, 4]}\n" + "]";

    public static String testdata3b = "[\n" + "    {\"nest0\": [{\"nest1\": [1, 2]}, // {\"nest1\": [3, 4]}]}, //\n"
        + "    {\"nest0\": [{\"nest1\": [5]}, // {\"nest1\": [6]}]}\n" + "]";

    public static String testdata4 = "{\n" + "    \"FirstName\": \"Fred\",\n" + "    \"Surname\": \"Smith\",\n"
        + "    \"Age\": 28,\n" + "    \"Address\": {\n" + "        \"Street\": \"Hursley Park\",\n"
        + "        \"City\": \"Winchester\",\n" + "        \"Postcode\": \"SO21 2JN\"\n" + "    }, //\n"
        + "    \"Phone\": [\n" + "        {\n" + "            \"type\": \"home\",\n"
        + "            \"number\": \"0203 544 1234\"\n" + "        }, //\n" + "        {\n"
        + "            \"type\": \"office\",\n" + "            \"number\": \"01962 001234\"\n" + "        }, //\n"
        + "        {\n" + "            \"type\": \"office\",\n" + "            \"number\": \"01962 001235\"\n"
        + "        }, //\n" + "        {\n" + "            \"type\": \"mobile\",\n"
        + "            \"number\": \"077 7700 1234\"\n" + "        }\n" + "    ],\n" + "    \"Email\": [\n"
        + "        {\n" + "            \"type\": \"work\",\n"
        + "            \"address\": [\"fred.smith@my-work.com\", \"fsmith@my-work.com\"]\n" + "        }, //\n"
        + "        {\n" + "            \"type\": \"home\",\n"
        + "            \"address\": [\"freddy@my-social.com\", \"frederic.smith@very-serious.com\"]\n"
        + "        }\n" + "    ],\n" + "    \"Other\": {\n" + "        \"Over 18 ?\": true,\n"
        + "        \"Misc\": null,\n" + "        \"Alternative.Address\": {\n"
        + "            \"Street\": \"Brick Lane\",\n" + "            \"City\": \"London\",\n"
        + "            \"Postcode\": \"E1 6RF\"\n" + "        }\n" + "    }\n" + "}";

    public static String testdata5 = "{\n" + "    \"library\": {\n" + "        \"books\": [\n" + "            {\n"
        + "                \"title\": \"Structure and Interpretation of Computer Programs\",\n"
        + "                \"authors\": [\"Abelson\", \"Sussman\"],\n"
        + "                \"isbn\": \"9780262510875\",\n" + "                \"price\": 38.90,\n"
        + "                \"copies\": 2\n" + "            }, //\n" + "            {\n"
        + "                \"title\": \"The C Programming Language\",\n"
        + "                \"authors\": [\"Kernighan\", \"Richie\"],\n"
        + "                \"isbn\": \"9780131103627\",\n" + "                \"price\": 33.59,\n"
        + "                \"copies\": 3\n" + "            }, //\n" + "            {\n"
        + "                \"title\": \"The AWK Programming Language\",\n"
        + "                \"authors\": [\"Aho\", \"Kernighan\", \"Weinberger\"],\n"
        + "                \"isbn\": \"9780201079814\",\n" + "                \"copies\": 1\n"
        + "            }, //\n" + "            {\n"
        + "                \"title\": \"Compilers: Principles, Techniques, and Tools\",\n"
        + "                \"authors\": [\"Aho\", \"Lam\", \"Sethi\", \"Ullman\"],\n"
        + "                \"isbn\": \"9780201100884\",\n" + "                \"price\": 23.38,\n"
        + "                \"copies\": 1\n" + "            }\n" + "        ],\n" + "        \"loans\": [\n"
        + "            {\n" + "                \"customer\": \"10001\",\n"
        + "                \"isbn\": \"9780262510875\",\n" + "                \"return\": \"2016-12-05\"\n"
        + "            }, //\n" + "            {\n" + "                \"customer\": \"10003\",\n"
        + "                \"isbn\": \"9780201100884\",\n" + "                \"return\": \"2016-10-22\"\n"
        + "            }\n" + "        ],\n" + "        \"customers\": [\n" + "            {\n"
        + "                \"id\": \"10001\",\n" + "                \"name\": \"Joe Doe\",\n"
        + "                \"address\": {\n" + "                    \"street\": \"2 Long Road\",\n"
        + "                    \"city\": \"Winchester\",\n" + "                    \"postcode\": \"SO22 5PU\"\n"
        + "                }\n" + "            }, //\n" + "            {\n" + "                \"id\": \"10002\",\n"
        + "                \"name\": \"Fred Bloggs\",\n" + "                \"address\": {\n"
        + "                    \"street\": \"56 Letsby Avenue\",\n"
        + "                    \"city\": \"Winchester\",\n" + "                    \"postcode\": \"SO22 4WD\"\n"
        + "                }\n" + "            }, //\n" + "            {\n" + "                \"id\": \"10003\",\n"
        + "                \"name\": \"Jason Arthur\",\n" + "                \"address\": {\n"
        + "                    \"street\": \"1 Preddy Gate\",\n"
        + "                    \"city\": \"Southampton\",\n" + "                    \"postcode\": \"SO14 0MG\"\n"
        + "                }\n" + "            }\n" + "        ]\n" + "    }\n" + "}";

    public static String data1 = "{\n" + "    doc: 23,\n" + "    detail: {\n" + "        contents: 'stuff',\n"
        + "        meta: 5\n" + "    }\n" + "}";

    public static String data2 = "{\n" + "    doc: 89,\n" + "    detail: {\n" + "        contents: 'some more stuff',\n"
        + "        meta: 'boo'\n" + "    }\n" + "}";

    public static String person = "{\n" + "    \"Salutation\": \"Mr\",\n" + "    \"Name\": \"Alexander\",\n"
        + "    \"MiddleName\": \"John\",\n" + "    \"Surname\": \"Smith\",\n" + "    \"Cars\": 3,\n"
        + "    \"Employment\": {\"Name\": \"IBM UK\", \"ContractType\": \"permanent\", \"Role\": \"Senior Physician\", \"Years\": 12,\n"
        + "        \"Executive.Compensation\":1.4e6}, //\n"
        + "    \"Qualifications\": [\"GP\", \"Consultant Opthalmologist\"],\n" + "    \"Salary\": null,\n"
        + "    \"NI.Number\":\"NO10FURBZNESS\",\n" + "    \"敷\": \"Steve\",\n" + "    \"Español\" : \"/ˈspænɪʃ/\"\n"
        + "}";

    @Parameter(0)
    public String input;

    @Parameter(1)
    public String expression;

    @Parameter(2)
    public String expected;

    @Parameters(name = "{index}: {1} -> {2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            {
                testdata1, "\"hello\"", "\"hello\""
            }, //
            {
                testdata1, "null", "\"hello\""
            }, //
            {
                testdata1, "\"Wayne\\", "\"Wayne\"s World\""
            }, //
            {
                testdata1, "42", "42"
            }, //
            {
                testdata1, "-42", "-42"
            }, //
            {
                testdata1, "3.14159", "3.14159"
            }, //
            {
                testdata1, "6.022e23", "6.022e23"
            }, //
            {
                testdata1, "1.602E-19", "1.602E-19"
            }, //
            {
                null, "10e1000", null
            }, //
            {
                testdata1, "\"hello\\\\tworld\"", "\"hello\\tworld\""
            }, //
            {
                testdata1, "\"hello\\\\nworld\"", "\"hello\\nworld\""
            }, //
            {
                testdata1, "\"hello \\\\\"world\\\\\"\"", "\"hello \"world\"\""
            }, //
            {
                testdata1, "\"C:\\\\\\\\Test\\\\\\\\test.txt\"", "\"C:\\\\Test\\\\test.txt\""
            }, //
            {
                testdata1, "\"\\\\u03BB-calculus rocks\"", "\"\u03BB-calculus rocks\""
            }, //
            {
                testdata1, "\"\\uD834\\uDD1E\"", "\"\uD834\uDD1E\""
            }, //
            {
                null, "\"\\\\y\"", null
            }, //
            {
                null, "\"\\\\u\"", null
            }, //
            {
                null, "\"\\\\u123t\"", null
            }, //
            {
                testdata1, "foo.bar", "42"
            }, //
            {
                testdata1, "foo.blah",
                "[{baz: {fud: \"hello\"}}, //\n                {baz: {fud: \"world\"}}, //\n                {bazz: \"gotcha\"}]"
            }, //
            {
                testdata1, "foo.blah.bazz", "\"gotcha\""
            }, //
            {
                testdata1, "foo.blah.baz", "[{fud: \"hello\"}, // {fud: \"world\"}]"
            }, //
            {
                testdata1, "foo.blah.baz.fud", "[\"hello\", \"world\"]"
            }, //
            {
                testdata4, "Other.Misc", "null"
            }, //
            {
                "[\n                [\n                    {\n                        \"baz\": {\n                            \"fud\": \"hello\"\n                        }\n                    }, //\n                    {\n                        \"baz\": {\n                            \"fud\": \"world\"\n                        }\n                    }, //\n                    {\n                        \"bazz\": \"gotcha\"\n                    }\n                ]\n            ]",
                "bazz", "\"gotcha\""
            }, //
            {
                "[\n                42,\n                [\n                    {\n                        \"baz\": {\n                            \"fud\": \"hello\"\n                        }\n                    }, //\n                    {\n                        \"baz\": {\n                            \"fud\": \"world\"\n                        }\n                    }, //\n                    {\n                        \"bazz\": \"gotcha\"\n                    }\n                ],\n                \"here\",\n                {\n                    \"fud\": \"hello\"\n                }, //\n                \"hello\",\n                {\n                    \"fud\": \"world\"\n                }, //\n                \"world\",\n                \"gotcha\"\n            ]",
                "fud", "[\"hello\", \"world\"]"
            }, //
            {
                testdata1, "foo.(blah).baz.fud", "[\"hello\", \"world\"]"
            }, //
            {
                testdata1, "foo.(blah.baz).fud", "[\"hello\", \"world\"]"
            }, //
            {
                testdata1, "(foo.blah.baz).fud", "[\"hello\", \"world\"]"
            }, //
            {
                testdata1, "foo.blah.(baz.fud)", "[\"hello\", \"world\"]"
            }, //
            {
                testdata1, "(foo.blah.baz.fud)", "[\"hello\", \"world\"]"
            }, //
            {
                testdata1, "(foo).(blah).baz.(fud)", "[\"hello\", \"world\"]"
            }, //
            {
                testdata1, "(foo.(blah).baz.fud)", "[\"hello\", \"world\"]"
            }, //
            {
                testdata1, "(4 + 2) / 2", "3"
            }, //
            {
                testdata3b, "nest0.nest1[0]", "[1, 3, 5, 6]"
            }, //
            {
                testdata1, "foo.blah[0].baz.fud", "\"hello\""
            }, //
            {
                testdata1, "foo.blah[1].baz.fud", "\"world\""
            }, //
            {
                testdata1, "foo.blah[-1].bazz", "\"gotcha\""
            }, //
            {
                testdata1, "(foo.blah)[1].baz.fud", "\"world\""
            }, //
            {
                testdata1, "foo.blah.baz.fud[0]", "[\"hello\", \"world\"]"
            }, //
            {
                testdata1, "foo.blah.baz.fud[-1]", "[\"hello\", \"world\"]"
            }, //
            {
                testdata1, "(foo.blah.baz.fud)[0]", "\"hello\""
            }, //
            {
                testdata1, "(foo.blah.baz.fud)[1]", "\"world\""
            }, //
            {
                testdata1, "(foo.blah.baz.fud)[5 * 0.2]", "\"world\""
            }, //
            {
                testdata1, "(foo.blah.baz.fud)[-1]", "\"world\""
            }, //
            {
                testdata1, "(foo.blah.baz.fud)[-2]", "\"hello\""
            }, //
            {
                testdata1, "(foo.blah.baz.fud)[2-4]", "\"hello\""
            }, //
            {
                testdata1, "(foo.blah.baz.fud)[-(4-2)]", "\"hello\""
            }, //
            {
                testdata1, "(foo.blah.baz.fud)[$$.foo.bar / 30]", "\"world\""
            }, //
            {
                testdata1, "foo.blah[0].baz", "{fud: \"hello\"}"
            }, //
            {
                testdata1, "foo.blah.baz[0]", "[{fud: \"hello\"}, // {fud: \"world\"}]"
            }, //
            {
                testdata1, "(foo.blah.baz)[0]", "{fud: \"hello\"}"
            }, //
            {
                "[[1, 2], [3, 4]]", "$[0]", "[1, 2]"
            }, //
            {
                "[[1, 2], [3, 4]]", "$[1]", "[3, 4]"
            }, //
            {
                "[[1, 2], [3, 4]]", "$[-1]", "[3, 4]"
            }, //
            {
                "[[1, 2], [3, 4]]", "$[1][0]", "3"
            }, //
            {
                "[[1, 2], [3, 4]]", "$[1.1][0.9]", "3"
            }, //
            {
                null, "[1..10][[1..3,8,-1]]", "[2, 3, 4, 9, 10]"
            }, //
            {
                null, "[1..10][[1..3,8,5]]", "[2, 3, 4, 6, 9]"
            }, //
            {
                null, "[1..10][[1..3,8,false]]", "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]"
            }, //
            {
                testdata1, "foo.\"blah\"",
                "[{baz: {fud: \"hello\"}}, //\n                {baz: {fud: \"world\"}}, //\n                {bazz: \"gotcha\"}]"
            }, //
            {
                testdata1, "foo.\"blah\".baz.\\", "[\"hello\", \"world\"]"
            }, //
            {
                testdata1, "\"foo\".\"blah\".\"baz\".\"fud\"", "[\"hello\", \"world\"]"
            }, //
            {
                testdata1, "foo.\"blah.baz\"", "\"here\""
            }, //
            {
                testdata1, "foo.`blah`",
                "[{baz: {fud: \"hello\"}}, //\n                {baz: {fud: \"world\"}}, //\n                {bazz: \"gotcha\"}]"
            }, //
            {
                testdata1, "foo.`blah`.baz.\\", "[\"hello\", \"world\"]"
            }, //
            {
                testdata1, "`foo`.`blah`.`baz`.`fud`", "[\"hello\", \"world\"]"
            }, //
            {
                testdata1, "foo.`blah.baz`", "\"here\""
            }, //
            {
                testdata1, "foo.bar + bar", "140"
            }, //
            {
                testdata1, "bar + foo.bar", "140"
            }, //
            {
                testdata1, "foo.bar - bar", "-56"
            }, //
            {
                testdata1, "bar - foo.bar", "56"
            }, //
            {
                testdata1, "foo.bar * bar", "4116"
            }, //
            {
                testdata1, "bar * foo.bar", "4116"
            }, //
            {
                testdata1, "foo.bar / bar", "0.42857142857142855"
            }, //
            {
                testdata1, "bar / foo.bar", "2.3333333333333335"
            }, //
            {
                testdata1, "foo.bar % bar", "42"
            }, //
            {
                testdata1, "bar % foo.bar", "14"
            }, //
            {
                testdata1, "bar + foo.bar * bar", "4214"
            }, //
            {
                testdata1, "foo.bar * bar + bar", "4214"
            }, //
            {
                testdata1, "24 * nonexistent", "undefined"
            }, //
            {
                testdata1, "nonexistent + 1", "undefined"
            }, //
            {
                null, "1/(10e300 * 10e100) ", null
            }, //
            {
                null, "\"5\" + \"5\"", null
            }, //
            {
                null, "3>-3", "true"
            }, //
            {
                null, "3>3", "false"
            }, //
            {
                null, "3=3", "true"
            }, //
            {
                null, "\"3\"=\"3\"", "true"
            }, //
            {
                null, "\"3\"=3", "false"
            }, //
            {
                null, "\"hello\" = \"hello\"", "true"
            }, //
            {
                null, "\"hello\" != \"world\"", "true"
            }, //
            {
                null, "\"hello\" < \"world\"", "true"
            }, //
            {
                null, "\"32\" < 42", null
            }, //
            {
                null, "null <= \"world\"", null
            }, //
            {
                null, "3 >= true", null
            }, //
            {
                testdata1, "foo.bar > bar", "false"
            }, //
            {
                testdata1, "foo.bar >= bar", "false"
            }, //
            {
                testdata1, "foo.bar<bar", "true"
            }, //
            {
                testdata1, "foo.bar<=bar", "true"
            }, //
            {
                testdata1, "bar>foo.bar", "true"
            }, //
            {
                testdata1, "bar < foo.bar", "false"
            }, //
            {
                testdata1, "foo.bar = bar", "false"
            }, //
            {
                testdata1, "foo.bar!= bar", "true"
            }, //
            {
                testdata1, "bar = foo.bar + 56", "true"
            }, //
            {
                testdata1, "bar !=foo.bar + 56", "false"
            }, //
            {
                testdata1, "foo.blah.baz[fud = \"hello\"]", "{fud: \"hello\"}"
            }, //
            {
                testdata1, "foo.blah.baz[fud != \"world\"]", "{fud: \"hello\"}"
            }, //
            {
                testdata2, "Account.Order.Product[Price > 30].Price", "[34.45, 34.45, 107.99]"
            }, //
            {
                testdata2, "Account.Order.Product.Price[$<=35]", "[34.45, 21.67, 34.45]"
            }, //
            {
                null, "1 in [1,2]", "true"
            }, //
            {
                null, "3 in [1,2]", "false"
            }, //
            {
                null, "\"hello\" in [1,2]", "false"
            }, //
            {
                null, "\"world\" in [\"hello\", \"world\"]", "true"
            }, //
            {
                null, "in in [\"hello\", \"world\"]", "false"
            }, //
            {
                null, "\"world\" in in", "false"
            }, //
            {
                null, "\"hello\" in \"hello\"", "true"
            }, //
            {
                testdata5, "library.books[\"Aho\" in authors].title",
                "[\n                \"The AWK Programming Language\",\n                \"Compilers: Principles, Techniques, and Tools\"\n            ]"
            }, //
            {
                "[{\"content\":{\"integration\":{\"name\":\"fakeIntegrationName\"}}}]",
                "content.integration.$lowercase(name)", "\"fakeintegrationname\""
            }, //
            {
                null, "nothing[x=6][y=3].number", "undefined"
            }, //
            {
                testdata2, "Account.Order.Product[$lowercase(Description.Colour) = \"purple\"][0].Price",
                "[34.45, 34.45]"
            }, //
            {
                testdata1, "foo.*",
                "[42, {\"baz\": {\"fud\": \"hello\"}}, // {\"baz\": {\"fud\": \"world\"}}, // {\"bazz\": \"gotcha\"}, // \"here\"]"
            }, //
            {
                testdata1, "foo.*.baz", "[{fud: \"hello\"}, // {fud: \"world\"}]"
            }, //
            {
                testdata1, "foo.*.bazz", "\"gotcha\""
            }, //
            {
                testdata1, "foo.*.baz.*", "[\"hello\", \"world\"]"
            }, //
            {
                testdata1, "foo.*.baz.*", "[\"hello\", \"world\"]"
            }, //
            {
                testdata1, "foo.*.baz.*", "[\"hello\", \"world\"]"
            }, //
            {
                testdata1, "foo.*[0]", "42"
            }, //
            {
                testdata4, "*[type=\"home\"]",
                "[\n                {\n                    \"type\": \"home\",\n                    \"number\": \"0203 544 1234\"\n                }, //\n                {\n                    \"type\": \"home\",\n                    \"address\": [\n                        \"freddy@my-social.com\",\n                        \"frederic.smith@very-serious.com\"\n                    ]\n                }\n            ]"
            }, //
            {
                testdata2, "Account[$$.Account.\"Account Name\" = \"Firefly\"].*[OrderID=\"order104\"].Product.Price",
                "[\n                34.45,\n                107.99\n            ]"
            }, //
            {
                testdata2, "Account[$$.Account.`Account Name` = \"Firefly\"].*[OrderID=\"order104\"].Product.Price",
                "[\n                34.45,\n                107.99\n            ]"
            }, //
            {
                testdata1, "foo.**.blah",
                "[{\"baz\": {\"fud\": \"hello\"}}, // {\"baz\": {\"fud\": \"world\"}}, // {\"bazz\": \"gotcha\"}]"
            }, //
            {
                testdata1, "foo.**.baz", "[{\"fud\": \"hello\"}, // {\"fud\": \"world\"}]"
            }, //
            {
                testdata1, "foo.**.fud", "[\"hello\", \"world\"]"
            }, //
            {
                testdata1, "\"foo\".**.fud", "[\"hello\", \"world\"]"
            }, //
            {
                testdata1, "foo.**.\"fud\"", "[\"hello\", \"world\"]"
            }, //
            {
                testdata1, "\"foo\".**.\"fud\"", "[\"hello\", \"world\"]"
            }, //
            {
                testdata1, "foo.*.**.fud", "[\"hello\", \"world\"]"
            }, //
            {
                testdata1, "foo.**.*.fud", "[\"hello\", \"world\"]"
            }, //
            {
                testdata2, "Account.Order.**.Colour", "[\"Purple\", \"Orange\", \"Purple\", \"Black\"]"
            }, //
            {
                testdata1, "foo.**.fud[0]", "[\"hello\", \"world\"]"
            }, //
            {
                testdata1, "(foo.**.fud)[0]", "\"hello\""
            }, //
            {
                testdata1, "(**.fud)[0]", "\"hello\""
            }, //
            {
                testdata2, "**.Price",
                "[\n                34.45,\n                21.67,\n                34.45,\n                107.99\n            ]"
            }, //
            {
                testdata2, "**.Price[0]",
                "[\n                34.45,\n                21.67,\n                34.45,\n                107.99\n            ]"
            }, //
            {
                testdata2, "(**.Price)[0]", "34.45"
            }, //
            {
                testdata2, "**[2]", "\"Firefly\""
            }, //
            {
                testdata2, "Account.Order.blah.**", "undefined"
            }, //
            {
                null, "**", "undefined"
            }, //
            {
                testdata1, "\"foo\" & \"bar\"", "\"foobar\""
            }, //
            {
                testdata1, "\"foo\"&\"bar\"", "\"foobar\""
            }, //
            {
                testdata1, "foo.blah[0].baz.fud &foo.blah[1].baz.fud", "\"helloworld\""
            }, //
            {
                testdata1, "foo.(blah[0].baz.fud & blah[1].baz.fud)", "\"helloworld\""
            }, //
            {
                testdata1, "foo.(blah[0].baz.fud & none)", "\"hello\""
            }, //
            {
                testdata1, "foo.(none.here & blah[1].baz.fud)", "\"world\""
            }, //
            {
                testdata1, "[1,2]&[3,4]", "\"[1,2][3,4]\""
            }, //
            {
                testdata1, "[1,2]&3", "\"[1,2]3\""
            }, //
            {
                testdata1, "1&2", "\"12\""
            }, //
            {
                testdata1, "1&[2]", "\"1[2]\""
            }, //
            {
                testdata1, "\"hello\"&5", "\"hello5\""
            }, //
            {
                testdata2, "\"Prices: \" & Account.Order.Product.Price", "\"Prices: [34.45,21.67,34.45,107.99]\""
            }, //
            {
                testdata2, "Account.Order.[Product.Price]", "[[34.45, 21.67], [34.45, 107.99]]"
            }, //
            {
                testdata3a, "$.nest0", "[1, 2, 3, 4]"
            }, //
            {
                testdata3a, "nest0", "[1, 2, 3, 4]"
            }, //
            {
                testdata3a, "$[0]", "{\"nest0\": [1, 2]}"
            }, //
            {
                testdata3a, "$[1]", "{\"nest0\": [3, 4]}"
            }, //
            {
                testdata3a, "$[-1]", "{\"nest0\": [3, 4]}"
            }, //
            {
                testdata3a, "$[0].nest0", "[1, 2]"
            }, //
            {
                testdata3a, "$[1].nest0", "[3, 4]"
            }, //
            {
                testdata3a, "$[0].nest0[0]", "1"
            }, //
            {
                testdata3, "nest0.[nest1.[nest2.[nest3]]]", "[[[[1], [2]], [[3], [4]]], [[[5], [6]], [[7], [8]]]]"
            }, //
            {
                testdata3, "nest0.nest1.[nest2.[nest3]]", "[[[1], [2]], [[3], [4]], [[5], [6]], [[7], [8]]]"
            }, //
            {
                testdata3, "nest0.[nest1.nest2.[nest3]]", "[[[1], [2], [3], [4]], [[5], [6], [7], [8]]]"
            }, //
            {
                testdata3, "nest0.[nest1.[nest2.nest3]]", "[[[1, 2], [3, 4]], [[5, 6], [7, 8]]]"
            }, //
            {
                testdata3, "nest0.[nest1.nest2.nest3]", "[[1, 2, 3, 4], [5, 6, 7, 8]]"
            }, //
            {
                testdata3, "nest0.nest1.[nest2.nest3]", "[[1, 2], [3, 4], [5, 6], [7, 8]]"
            }, //
            {
                testdata3, "nest0.nest1.nest2.[nest3]", "[[1], [2], [3], [4], [5], [6], [7], [8]]"
            }, //
            {
                testdata3, "nest0.nest1.nest2.nest3", "[1, 2, 3, 4, 5, 6, 7, 8]"
            }, //
            {
                testdata4, "Phone[type=\"mobile\"].number", "\"077 7700 1234\""
            }, //
            {
                testdata4, "Phone[type=\"mobile\"][].number", "[\"077 7700 1234\"]"
            }, //
            {
                testdata4, "Phone[][type=\"mobile\"].number", "[\"077 7700 1234\"]"
            }, //
            {
                testdata4, "Phone[type=\"office\"][].number",
                "[\n                \"01962 001234\",\n                \"01962 001235\"\n            ]"
            }, //
            {
                testdata4, "Phone{type: number}",
                "{\n                \"home\": \"0203 544 1234\",\n                \"office\": [\n                    \"01962 001234\",\n                    \"01962 001235\"\n                ],\n                \"mobile\": \"077 7700 1234\"\n            }"
            }, //
            {
                testdata4, "Phone{type: number[]}",
                "{\n                \"home\": [\n                    \"0203 544 1234\"\n                ],\n                \"office\": [\n                    \"01962 001234\",\n                    \"01962 001235\"\n                ],\n                \"mobile\": [\n                    \"077 7700 1234\"\n                ]\n            }"
            }, //
            {
                testdata2, "$price.foo.bar", "45"
            }, //
            {
                testdata2, "$var[1]", "2"
            }, //
            {
                testdata1, "$.foo.bar", "42"
            }, //
            {
                null, "$a := 5", "5"
            }, //
            {
                null, "$a := $b := 5", "5"
            }, //
            {
                null, "($a := $b := 5; $a)", "5"
            }, //
            {
                null, "($a := $b := 5; $b)", "5"
            }, //
            {
                null, "( $a := 5; $a := $a + 2; $a )", "7"
            }, //
            {
                null, "[1,2,3].$v", "undefined"
            }, //
            {
                null, "( $foo := \"defined\"; ( $foo := nothing ); $foo )", "\"defined\""
            }, //
            {
                null, "( $foo := \"defined\"; ( $foo := nothing; $foo ) )", "undefined"
            }, //
            {
                testdata2, "$sum(Account.Order.Product.(Price * Quantity))", "336.36"
            }, //
            {
                testdata2, "Account.Order.$sum(Product.(Price * Quantity))",
                "[90.57000000000001, 245.79000000000002]"
            }, //
            {
                testdata2, "Account.Order.(OrderID & \": \" & $sum(Product.(Price*Quantity)))",
                "[\"order103: 90.57\", \"order104: 245.79\"]"
            }, //
            {
                testdata2, "$sum()", null
            }, //
            {
                null, "$sum(1)", "1"
            }, //
            {
                testdata2, "$sum(Account.Order)", null
            }, //
            {
                null, "$sum(undefined)", "undefined"
            }, //
            {
                testdata2, "$count(Account.Order.Product.(Price * Quantity))", "4"
            }, //
            {
                testdata2, "Account.Order.$count(Product.(Price * Quantity))", "[2, 2]"
            }, //
            {
                testdata2, "Account.Order.(OrderID & \": \" & $count(Product.(Price*Quantity)))",
                "[\"order103: 2\",\"order104: 2\"]"
            }, //
            {
                null, "$count([])", "0"
            }, //
            {
                null, "$count([1,2,3])", "3"
            }, //
            {
                null, "$count([\"1\",\"2\",\"3\"])", "3"
            }, //
            {
                null, "$count([\"1\",\"2\",3])", "3"
            }, //
            {
                null, "$count(1)", "1"
            }, //
            {
                null, "$count([],[])", null
            }, //
            {
                null, "$count([1,2,3],[])", null
            }, //
            {
                null, "$count([],[],[])", null
            }, //
            {
                null, "$count([1,2],[],[])", null
            }, //
            {
                null, "$count(undefined)", "0"
            }, //
            {
                null, "$count([1,2,3,4]) / 2", "2"
            }, //
            {
                testdata2, "$max(Account.Order.Product.(Price * Quantity))", "137.8"
            }, //
            {
                testdata2, "Account.Order.$max(Product.(Price * Quantity))", "[68.9,137.8]"
            }, //
            {
                testdata2, "Account.Order.(OrderID & \": \" & $count(Product.(Price*Quantity)))",
                "[\"order103: 2\",\"order104: 2\"]"
            }, //
            {
                null, "$max([])", "undefined"
            }, //
            {
                null, "$max([1,2,3])", "3"
            }, //
            {
                null, "$max([\"1\",\"2\",\"3\"])", null
            }, //
            {
                null, "$max([\"1\",\"2\",3])", null
            }, //
            {
                null, "$max(1)", "1"
            }, //
            {
                null, "$max([-1,-5])", "-1"
            }, //
            {
                null, "$max([],[])", null
            }, //
            {
                null, "$max([1,2,3],[])", null
            }, //
            {
                null, "$max([],[],[])", null
            }, //
            {
                null, "$max([1,2],[],[])", null
            }, //
            {
                null, "$max(undefined)", "undefined"
            }, //
            {
                testdata2, "$min(Account.Order.Product.(Price * Quantity))", "21.67"
            }, //
            {
                testdata2, "Account.Order.$min(Product.(Price * Quantity))", "[21.67,107.99]"
            }, //
            {
                testdata2, "Account.Order.(OrderID & \": \" & $min(Product.(Price*Quantity)))",
                "[\"order103: 21.67\",\"order104: 107.99\"]"
            }, //
            {
                null, "$min([])", "undefined"
            }, //
            {
                null, "$min([1,2,3])", "1"
            }, //
            {
                null, "$min([\"1\",\"2\",\"3\"])", null
            }, //
            {
                null, "$min([\"1\",\"2\",3])", null
            }, //
            {
                null, "$min(1)", "1"
            }, //
            {
                null, "$min([],[])", null
            }, //
            {
                null, "$min([1,2,3],[])", null
            }, //
            {
                null, "$min([],[],[])", null
            }, //
            {
                null, "$min([1,2],[],[])", null
            }, //
            {
                null, "$min(undefined)", "undefined"
            }, //
            {
                testdata2, "$average(Account.Order.Product.(Price * Quantity))", "84.09"
            }, //
            {
                testdata2, "Account.Order.$average(Product.(Price * Quantity))",
                "[45.285000000000004,122.89500000000001]"
            }, //
            {
                testdata2, "Account.Order.(OrderID & \": \" & $average(Product.(Price*Quantity)))",
                "[\"order103: 45.285\",\"order104: 122.895\"]"
            }, //
            {
                null, "$average([])", "undefined"
            }, //
            {
                null, "$average([1,2,3])", "2"
            }, //
            {
                null, "$average([\"1\",\"2\",\"3\"])", null
            }, //
            {
                null, "$average([\"1\",\"2\",3])", null
            }, //
            {
                null, "$average(1)", "1"
            }, //
            {
                null, "$average([],[])", null
            }, //
            {
                null, "$average([1,2,3],[])", null
            }, //
            {
                null, "$average([],[],[])", null
            }, //
            {
                null, "$average([1,2],[],[])", null
            }, //
            {
                null, "$average(undefined)", "undefined"
            }, //
            {
                testdata2, "$exists(\"Hello World\")", "true"
            }, //
            {
                testdata2, "$exists(\"\")", "true"
            }, //
            {
                testdata2, "$exists(true)", "true"
            }, //
            {
                testdata2, "$exists(false)", "true"
            }, //
            {
                testdata2, "$exists(0)", "true"
            }, //
            {
                testdata2, "$exists(-0.5)", "true"
            }, //
            {
                testdata2, "$exists(null)", "true"
            }, //
            {
                testdata2, "$exists([])", "true"
            }, //
            {
                testdata2, "$exists([0])", "true"
            }, //
            {
                testdata2, "$exists([1,2,3])", "true"
            }, //
            {
                testdata2, "$exists([[]])", "true"
            }, //
            {
                testdata2, "$exists([[null]])", "true"
            }, //
            {
                testdata2, "$exists([[[true]]])", "true"
            }, //
            {
                testdata2, "$exists({})", "true"
            }, //
            {
                testdata2, "$exists({\"hello\":\"world\"})", "true"
            }, //
            {
                testdata2, "$exists(Account)", "true"
            }, //
            {
                testdata2, "$exists(Account.Order.Product.Price)", "true"
            }, //
            {
                testdata2, "$exists($exists)", "true"
            }, //
            {
                testdata2, "$exists(function(){true})", "true"
            }, //
            {
                testdata2, "$exists(blah)", "false"
            }, //
            {
                testdata2, "$exists(Account.blah)", "false"
            }, //
            {
                testdata2, "$exists(Account.Order[2])", "false"
            }, //
            {
                testdata2, "$exists(Account.Order[0].blah)", "false"
            }, //
            {
                null, "$exists(2,3)", null
            }, //
            {
                null, "$exists()", null
            }, //
            {
                testdata2, "$spread(\"Hello World\")", "\"Hello World\""
            }, //
            {
                testdata2, "$spread((Account.Order.Product.Description))",
                "[\n                {\"Colour\": \"Purple\"}, //\n                {\"Width\": 300}, //\n                {\"Height\": 200}, //\n                {\"Depth\": 210}, //\n                {\"Weight\": 0.75}, //\n                {\"Colour\": \"Orange\"}, //\n                {\"Width\": 300}, //\n                {\"Height\": 200}, //\n                {\"Depth\": 210}, //\n                {\"Weight\": 0.6}, //\n                {\"Colour\": \"Purple\"}, //\n                {\"Width\": 300}, //\n                {\"Height\": 200}, //\n                {\"Depth\": 210}, //\n                {\"Weight\": 0.75}, //\n                {\"Colour\": \"Black\"}, //\n                {\"Width\": 30}, //\n                {\"Height\": 20}, //\n                {\"Depth\": 210}, //\n                {\"Weight\": 2}\n            ]"
            }, //
            {
                testdata2, "$spread(blah)", "undefined"
            }, //
            {
                testdata2, "$string($spread(function($x){$x*$x}))", "\"\""
            }, //
            {
                testdata2, "$string(5)", "\"5\""
            }, //
            {
                testdata2, "$string(22/7)", "\"3.142857142857\""
            }, //
            {
                testdata2, "$string(1e100)", "\"1e+100\""
            }, //
            {
                null, "$string(1e-100)", "\"1e-100\""
            }, //
            {
                null, "$string(1e-6)", "\"0.000001\""
            }, //
            {
                null, "$string(1e-7)", "\"1e-7\""
            }, //
            {
                null, "$string(1e20)", "\"100000000000000000000\""
            }, //
            {
                null, "$string(1e21)", "\"1e+21\""
            }, //
            {
                testdata2, "Account.Order.$string($sum(Product.(Price* Quantity)))",
                "[\n                \"90.57\",\n                \"245.79\"\n            ]"
            }, //
            {
                testdata2, "$string(true)", "\"true\""
            }, //
            {
                testdata2, "$string(false)", "\"false\""
            }, //
            {
                testdata2, "$string(null)", "\"null\""
            }, //
            {
                testdata2, "$string(blah)", "undefined"
            }, //
            {
                testdata2, "$string($string)", "\"\""
            }, //
            {
                testdata2, "$string(function(){true})", "\"\""
            }, //
            {
                testdata2, "$string(function(){1})", "\"\""
            }, //
            {
                null, "$string({\"string\": \"hello\"})", "\"{\"string\":\"hello\"}\""
            }, //
            {
                null, "$string([\"string\", 5])", "\"[\"string\",5]\""
            }, //
            {
                null, "$string({",
                "\"{\"string\":\"hello\",\"number\":39.4,\"null\":null,\"boolean\":false,\"function\":\"\",\"lambda\":\"\",\"object\":{\"str\":\"another\",\"lambda2\":\"\"}, //\"array\":[]}\""
            }, //
            {
                null, "$string(1/0)", null
            }, //
            {
                null, "$string({\"inf\": 1/0})", null
            }, //
            {
                null, "$string(2,3)", null
            }, //
            {
                null, "$string()", "undefined"
            }, //
            {
                testdata2, "$substring(\"hello world\", 0, 5)", "\"hello\""
            }, //
            {
                testdata2, "$substring(\"hello world\", -5, 5)", "\"world\""
            }, //
            {
                testdata2, "$substring(\"hello world\", 6)", "\"world\""
            }, //
            {
                testdata2, "$substring(blah, 6)", "undefined"
            }, //
            {
                testdata2, "$substringBefore(\"Hello World\", \" \")", "\"Hello\""
            }, //
            {
                testdata2, "$substringBefore(\"Hello World\", \"l\")", "\"He\""
            }, //
            {
                testdata2, "$substringBefore(\"Hello World\", \"f\")", "\"Hello World\""
            }, //
            {
                testdata2, "$substringBefore(\"Hello World\", \"He\")", "\"\""
            }, //
            {
                testdata2, "$substringBefore(blah, \"He\")", "undefined"
            }, //
            {
                testdata2, "$substringAfter(\"Hello World\", \" \")", "\"World\""
            }, //
            {
                testdata2, "$substringAfter(\"Hello World\", \"l\")", "\"lo World\""
            }, //
            {
                testdata2, "$substringAfter(\"Hello World\", \"f\")", "\"Hello World\""
            }, //
            {
                testdata2, "$substringAfter(\"Hello World\", \"ld\")", "\"\""
            }, //
            {
                testdata2, "$substringAfter(blah, \"ld\")", "undefined"
            }, //
            {
                testdata2, "$lowercase(\"Hello World\")", "\"hello world\""
            }, //
            {
                testdata2, "$lowercase(blah)", "undefined"
            }, //
            {
                testdata2, "$uppercase(\"Hello World\")", "\"HELLO WORLD\""
            }, //
            {
                testdata2, "$uppercase(blah)", "undefined"
            }, //
            {
                null, "$length(\"\")", "0"
            }, //
            {
                null, "$length(\"hello\")", "5"
            }, //
            {
                null, "$length(missing)", "undefined"
            }, //
            {
                null, "$length(\"\\\\u03BB-calculus\")", "10"
            }, //
            {
                null, "$length(\"\\\\uD834\\\\uDD1E\")", "2"
            }, //
            {
                null, "$length(\"\uD834\uDD1E\")", "2"
            }, //
            {
                null, "$length(\"\u8D85\u660E\u9AD4\u7E41\")", "4"
            }, //
            {
                null, "$length(\"\\\\t\")", "1"
            }, //
            {
                null, "$length(\"\\\\n\")", "1"
            }, //
            {
                null, "$length(1234)", null
            }, //
            {
                null, "$length(null)", null
            }, //
            {
                null, "$length(true)", null
            }, //
            {
                null, "$length([\"str\"])", null
            }, //
            {
                "23", "$length()", null
            }, //
            {
                testdata2, "$length()", null
            }, //
            {
                null, "$length(\"Hello\", \"World\")", null
            }, //
            {
                null, "$trim(\"Hello World\")", "\"Hello World\""
            }, //
            {
                null, "$trim(\"   Hello  \\n  \\t World  \\t \")", "\"Hello World\""
            }, //
            {
                null, "$trim()", "undefined"
            }, //
            {
                null, "$pad(\"foo\", 5)", null
            }, //
            {
                null, "$pad(\"foo\", -5)", null
            }, //
            {
                null, "$pad(\"foo\", -5, \"#\")", null
            }, //
            {
                null, "$pad(\"foo\", 5, \"\")", null
            }, //
            {
                null, "$pad(\"foo\", 1)", null
            }, //
            {
                null, "$pad(\"foo\", 8, \"-+\")", null
            }, //
            {
                null, "$pad(nothing, 1)", null
            }, //
            {
                null, "$contains(\"Hello World\", \"lo\")", "true"
            }, //
            {
                null, "$contains(\"Hello World\", \"World\")", "true"
            }, //
            {
                null, "$contains(\"Hello World\", \"world\")", "false"
            }, //
            {
                null, "$contains(\"Hello World\", \"Word\")", "false"
            }, //
            {
                null, "$contains(nothing, \"World\")", "undefined"
            }, //
            {
                null, "$contains(23, 3)", null
            }, //
            {
                null, "$contains(\"23\", 3)", null
            }, //
            {
                null, "$replace(\"Hello World\", \"World\", \"Everyone\")", "\"Hello Everyone\""
            }, //
            {
                null, "$replace(\"the cat sat on the mat\", \"at\", \"it\")", "\"the cit sit on the mit\""
            }, //
            {
                null, "$replace(\"the cat sat on the mat\", \"at\", \"it\", 0)", "\"the cat sat on the mat\""
            }, //
            {
                null, "$replace(\"the cat sat on the mat\", \"at\", \"it\", 2)", "\"the cit sit on the mat\""
            }, //
            {
                null, "$replace(nothing, \"at\", \"it\", 2)", "undefined"
            }, //
            {
                null, "$replace(\"hello\")", null
            }, //
            {
                null, "$replace(\"hello\", \"l\", \"1\", null)", null
            }, //
            {
                null, "$replace(\"hello\", \"l\", \"1\", -2)", null
            }, //
            {
                "\"hello\"", "$replace(\"hello\", 1)", null
            }, //
            {
                null, "$replace(\"hello\", \"\", \"bye\")", null
            }, //
            {
                null, "$replace(\"hello\", 2, 1)", null
            }, //
            {
                null, "$replace(123, 2, 1)", null
            }, //
            {
                null, "$split(\"Hello World\", \" \")", "[\"Hello\", \"World\"]"
            }, //
            {
                null, "$split(\"Hello\", \" \")", "[\"Hello\"]"
            }, //
            {
                null, "$split(\"Hello  World\", \" \")", "[\"Hello\", \"\", \"World\"]"
            }, //
            {
                null, "$split(\"Hello\", \"\")", "[\"H\", \"e\", \"l\", \"l\", \"o\"]"
            }, //
            {
                null, "$sum($split(\"12345\", \"\").$number($))", "15"
            }, //
            {
                null, "$split(\"a, b, c, d\", \", \")", "[\"a\", \"b\", \"c\", \"d\"]"
            }, //
            {
                null, "$split(\"a, b, c, d\", \", \", 2)", "[\"a\", \"b\"]"
            }, //
            {
                null, "$split(\"a, b, c, d\", \", \", 2.5)", "[\"a\", \"b\"]"
            }, //
            {
                null, "$split(\"a, b, c, d\", \", \", 10)", "[\"a\", \"b\", \"c\", \"d\"]"
            }, //
            {
                null, "$split(\"a, b, c, d\", \", \", 0)", "[]"
            }, //
            {
                null, "$split(nothing, \" \")", "undefined"
            }, //
            {
                null, "$split(\"a, b, c, d\", \", \", -3)", null
            }, //
            {
                null, "$split(\"a, b, c, d\", \", \", null)", null
            }, //
            {
                null, "$split(\"a, b, c, d\", \", \", -5)", null
            }, //
            {
                null, "$split(\"a, b, c, d\", \", \", \"2\")", null
            }, //
            {
                null, "$split(\"a, b, c, d\", true)", null
            }, //
            {
                null, "$split(12345, 3)", null
            }, //
            {
                null, "$split(12345)", null
            }, //
            {
                null, "$join(\"hello\")", "\"hello\""
            }, //
            {
                null, "$join([\"hello\"])", "\"hello\""
            }, //
            {
                null, "$join([\"hello\", \"world\"])", "\"helloworld\""
            }, //
            {
                null, "$join([\"hello\", \"world\"], \", \")", "\"hello, world\""
            }, //
            {
                null, "$join([], \", \")", "\"\""
            }, //
            {
                testdata2, "$join(Account.Order.Product.Description.Colour, \", \")",
                "\"Purple, Orange, Purple, Black\""
            }, //
            {
                testdata2, "$join(Account.Order.Product.Description.Colour, no.sep)", "\"PurpleOrangePurpleBlack\""
            }, //
            {
                testdata2, "$join(Account.blah.Product.Description.Colour, \", \")", "undefined"
            }, //
            {
                null, "$join(true, \", \")", null
            }, //
            {
                null, "$join([1,2,3], \", \")", null
            }, //
            {
                null, "$join([\"hello\"], 3)", null
            }, //
            {
                null, "$join()", null
            }, //
            {
                null, "$formatNumber(12345.6, \"#,###.00\")", "\"12,345.60\""
            }, //
            {
                null, "$formatNumber(12345678.9, \"9,999.99\")", "\"12,345,678.90\""
            }, //
            {
                null, "$formatNumber(123412345678.9, \"9,9,99.99\")", "\"123412345,6,78.90\""
            }, //
            {
                null, "$formatNumber(1234.56789, \"9,999.999,999\")", "\"1,234.567,890\""
            }, //
            {
                null, "$formatNumber(123.9, \"9999\")", "\"0124\""
            }, //
            {
                null, "$formatNumber(0.14, \"01%\")", "\"14%\""
            }, //
            {
                null, "$formatNumber(0.4857,\"###.###\u2030\")", "\"485.7\u2030\""
            }, //
            {
                null, "$formatNumber(0.14, \"###pm\", {\"per-mille\": \"pm\"})", "\"140pm\""
            }, //
            {
                null, "$formatNumber(-6, \"000\")", "\"-006\""
            }, //
            {
                null, "$formatNumber(1234.5678, \"00.000e0\")", "\"12.346e2\""
            }, //
            {
                null, "$formatNumber(1234.5678, \"00.000e000\")", "\"12.346e002\""
            }, //
            {
                null, "$formatNumber(1234.5678, \"\u2460\u2460.\u2460\u2460\u2460e\u2460\", {\"zero-digit\": \"\\u245f\"})",
                "\"\u2460\u2461.\u2462\u2463\u2465e\u2461\""
            }, //
            {
                null, "$formatNumber(0.234, \"0.0e0\")", "\"2.3e-1\""
            }, //
            {
                null, "$formatNumber(0.234, \"#.00e0\")", "\"0.23e0\""
            }, //
            {
                null, "$formatNumber(0.123, \"#.e9\")", "\"0.1e0\""
            }, //
            {
                null, "$formatNumber(0.234, \".00e0\")", "\".23e0\""
            }, //
            {
                null, "$formatNumber(2392.14*(-36.58), \"000,000.000###;###,###.000###\")", "\"87,504.4812\""
            }, //
            {
                null, "$formatNumber(2.14*86.58,\"PREFIX##00.000###SUFFIX\")", "\"PREFIX185.2812SUFFIX\""
            }, //
            {
                null, "$formatNumber(1E20,\"#,######\")", "\"100,000000,000000,000000\""
            }, //
            {
                null, "$formatNumber(20,\"#;#;#\")", null
            }, //
            {
                null, "$formatBase(100)", "\"100\""
            }, //
            {
                null, "$formatBase(nothing)", "undefined"
            }, //
            {
                null, "$formatBase(100, 2)", "\"1100100\""
            }, //
            {
                null, "$formatBase(-100, 2)", "\"-1100100\""
            }, //
            {
                null, "$formatBase(100, 36)", "\"2s\""
            }, //
            {
                null, "$formatBase(99.5, 2.5)", "\"1100100\""
            }, //
            {
                null, "$formatBase(100, 1)", null
            }, //
            {
                null, "$formatBase(100, 37)", null
            }, //
            {
                null, "$number(0)", "0"
            }, //
            {
                null, "$number(10)", "10"
            }, //
            {
                null, "$number(-0.05)", "-0.05"
            }, //
            {
                null, "$number(\"0\")", "0"
            }, //
            {
                null, "$number(\"-0.05\")", "-0.05"
            }, //
            {
                null, "$number(\"1e2\")", "100"
            }, //
            {
                null, "$number(\"1.0e-2\")", "0.01"
            }, //
            {
                null, "$number(\"1e0\")", "1"
            }, //
            {
                null, "$number(\"10e500\")", null
            }, //
            {
                null, "$number(\"Hello world\")", null
            }, //
            {
                null, "$number(\"1/2\")", null
            }, //
            {
                null, "$number(\"1234 hello\")", null
            }, //
            {
                null, "$number(\"\")", null
            }, //
            {
                null, "$number(true)", null
            }, //
            {
                null, "$number(false)", null
            }, //
            {
                testdata2, "$number(Account.blah)", "undefined"
            }, //
            {
                null, "$number(null)", null
            }, //
            {
                null, "$number([])", null
            }, //
            {
                null, "$number(\"[1]\")", null
            }, //
            {
                null, "$number([1,2])", null
            }, //
            {
                null, "$number([\"hello\"])", null
            }, //
            {
                null, "$number([\"2\"])", null
            }, //
            {
                null, "$number({})", null
            }, //
            {
                null, "$number({\"hello\":\"world\"})", null
            }, //
            {
                null, "$number($number)", null
            }, //
            {
                null, "$number(function(){5})", null
            }, //
            {
                null, "$number(1,2)", null
            }, //
            {
                null, "$abs(3.7)", "3.7"
            }, //
            {
                null, "$abs(-3.7)", "3.7"
            }, //
            {
                null, "$abs(0)", "0"
            }, //
            {
                null, "$abs(nothing)", "undefined"
            }, //
            {
                null, "$floor(3.7)", "3"
            }, //
            {
                null, "$floor(-3.7)", "-4"
            }, //
            {
                null, "$floor(0)", "0"
            }, //
            {
                null, "$floor(nothing)", "undefined"
            }, //
            {
                null, "$ceil(3.7)", "4"
            }, //
            {
                null, "$ceil(-3.7)", "-3"
            }, //
            {
                null, "$ceil(0)", "0"
            }, //
            {
                null, "$ceil(nothing)", "undefined"
            }, //
            {
                null, "$round(4)", "4"
            }, //
            {
                null, "$round(2.3)", "2"
            }, //
            {
                null, "$round(2.7)", "3"
            }, //
            {
                null, "$round(2.5)", "2"
            }, //
            {
                null, "$round(3.5)", "4"
            }, //
            {
                null, "$round(-0.5)", "0"
            }, //
            {
                null, "$round(-0.3)", "0"
            }, //
            {
                null, "$round(0.5)", "0"
            }, //
            {
                null, "$round(-7.5)", "-8"
            }, //
            {
                null, "$round(-8.5)", "-8"
            }, //
            {
                null, "$round(4.49, 1)", "4.5"
            }, //
            {
                null, "$round(4.525, 2)", "4.52"
            }, //
            {
                null, "$round(4.515, 2)", "4.52"
            }, //
            {
                null, "$round(12345, -2)", "12300"
            }, //
            {
                null, "$round(12450, -2)", "12400"
            }, //
            {
                null, "$round(12350, -2)", "12400"
            }, //
            {
                null, "$round(6.022e-23, 24)", "6.0e-23"
            }, //
            {
                null, "$round(unknown)", "undefined"
            }, //
            {
                null, "$sqrt(4)", "2"
            }, //
            {
                null, "$sqrt(2)", "Math.sqrt(2)"
            }, //
            {
                null, "$sqrt(10) * $sqrt(10)", "10"
            }, //
            {
                null, "$sqrt(nothing)", "undefined"
            }, //
            {
                null, "$sqrt(-2)", null
            }, //
            {
                null, "$power(4, 2)", "16"
            }, //
            {
                null, "$power(4, 0.5)", "2"
            }, //
            {
                null, "$power(10, -2)", "0.01"
            }, //
            {
                null, "$power(-2, 3)", "-8"
            }, //
            {
                null, "$power(nothing, 3)", "undefined"
            }, //
            {
                null, "$power(-2, 1/3)", null
            }, //
            {
                null, "$power(100, 1000)", null
            }, //
            {
                null, "$random()", "result >= 0 && result < 1"
            }, //
            {
                null, "$random() = $random()", "false"
            }, //
            {
                testdata2, "$boolean(\"Hello World\")", "true"
            }, //
            {
                testdata2, "$boolean(\"\")", "false"
            }, //
            {
                testdata2, "$boolean(true)", "true"
            }, //
            {
                testdata2, "$boolean(false)", "false"
            }, //
            {
                testdata2, "$boolean(0)", "false"
            }, //
            {
                testdata2, "$boolean(10)", "true"
            }, //
            {
                testdata2, "$boolean(-0.5)", "true"
            }, //
            {
                testdata2, "$boolean(null)", "false"
            }, //
            {
                testdata2, "$boolean([])", "false"
            }, //
            {
                testdata2, "$boolean([0])", "false"
            }, //
            {
                testdata2, "$boolean([1])", "true"
            }, //
            {
                testdata2, "$boolean([1,2,3])", "true"
            }, //
            {
                testdata2, "$boolean([0,0])", "false"
            }, //
            {
                testdata2, "$boolean([[]])", "false"
            }, //
            {
                testdata2, "$boolean([[null]])", "false"
            }, //
            {
                testdata2, "$boolean([[[true]]])", "true"
            }, //
            {
                testdata2, "$boolean({})", "false"
            }, //
            {
                testdata2, "$boolean({\"hello\":\"world\"})", "true"
            }, //
            {
                testdata2, "$boolean(Account)", "true"
            }, //
            {
                testdata2, "$boolean(Account.Order.Product.Price)", "true"
            }, //
            {
                testdata2, "$boolean(Account.blah)", "undefined"
            }, //
            {
                testdata2, "$boolean($boolean)", "false"
            }, //
            {
                testdata2, "$boolean(function(){true})", "false"
            }, //
            {
                null, "$boolean(2,3)", null
            }, //
            {
                testdata2, "$keys(Account)",
                "[\n                \"Account Name\",\n                \"Order\"\n            ]"
            }, //
            {
                testdata2, "$keys(Account.Order.Product)",
                "[\n                \"Product Name\",\n                \"ProductID\",\n                \"SKU\",\n                \"Description\",\n                \"Price\",\n                \"Quantity\"\n            ]"
            }, //
            {
                null, "$keys({})", "undefined"
            }, //
            {
                null, "$keys({\"foo\":{}})", "[\"foo\"]"
            }, //
            {
                null, "$keys(\"foo\")", "undefined"
            }, //
            {
                null, "$keys(function(){1})", "undefined"
            }, //
            {
                null, "$keys([\"foo\", \"bar\"])", "undefined"
            }, //
            {
                testdata2, "$lookup(Account, \"Account Name\")", "\"Firefly\""
            }, //
            {
                testdata2, "$lookup(Account.Order.Product, \"Product Name\")",
                "[\n                \"Bowler Hat\",\n                \"Trilby hat\",\n                \"Bowler Hat\",\n                \"Cloak\"\n            ]"
            }, //
            {
                testdata2, "$lookup(Account.Order.Product.ProductID, \"Product Name\")", "undefined"
            }, //
            {
                testdata2, "$append([1,2], [3,4])", "[1, 2, 3, 4]"
            }, //
            {
                testdata2, "$append(1, [3,4])", "[1, 3, 4]"
            }, //
            {
                testdata2, "$append(1,2)", "[1, 2]"
            }, //
            {
                testdata2, "$append(1,nonexistent)", "1"
            }, //
            {
                testdata2, "$append(nonexistent, [2,3,4])", "[2, 3, 4]"
            }, //
            {
                testdata4, "$each(Address, \u03BB($v, $k) {$k & \": \" & $v})",
                "[\n                \"Street: Hursley Park\",\n                \"City: Winchester\",\n                \"Postcode: SO21 2JN\"\n            ]"
            }, //
            {
                testdata4, "$reverse([1..5])", "[5,4,3,2,1]"
            }, //
            {
                "[1,2,3]", "[[$], [$reverse($)], [$]]", "[[1,2,3], [3,2,1], [1,2,3]]"
            }, //
            {
                testdata4, "$reverse(nothing)", "undefined"
            }, //
            {
                testdata4, "$reverse([1])", "[1]"
            }, //
            {
                null, "$zip([1,2,3],[4,5,6])", "[[1,4],[2,5],[3,6]]"
            }, //
            {
                null, "$zip([1,2,3],[4,5,6],[7,8,9])", "[[1,4,7],[2,5,8],[3,6,9]]"
            }, //
            {
                null, "$zip([1,2,3],[4,5],[7,8,9])", "[[1,4,7],[2,5,8]]"
            }, //
            {
                null, "$zip([1,2,3])", "[[1],[2],[3]]"
            }, //
            {
                null, "$zip(1,2,3)", "[[1,2,3]]"
            }, //
            {
                null, "$zip([1,2,3], [4,5,6], nothing)", "[]"
            }, //
            {
                testdata4, "$count($shuffle([1..10]))", "10"
            }, //
            {
                testdata4, "$sort($shuffle([1..10]))", "[1,2,3,4,5,6,7,8,9,10]"
            }, //
            {
                testdata4, "$shuffle(nothing)", "undefined"
            }, //
            {
                testdata4, "$shuffle([1])", "[1]"
            }, //
            {
                testdata2, "$merge(nothing)", "undefined"
            }, //
            {
                testdata2, "$merge({\"a\":1})", "{\"a\": 1}"
            }, //
            {
                testdata2, "$merge([{\"a\":1}, // {\"b\":2}])", "{\"a\": 1, \"b\": 2}"
            }, //
            {
                testdata2, "$merge([{\"a\": 1}, // {\"b\": 2, \"c\": 3}])", "{\"a\": 1, \"b\": 2, \"c\": 3}"
            }, //
            {
                testdata2, "$merge([{\"a\": 1}, // {\"b\": 2, \"a\": 3}])", "{\"a\": 3, \"b\": 2}"
            }, //
            {
                testdata2, "$sort(nothing)", "undefined"
            }, //
            {
                testdata2, "$sort(1)", "[1]"
            }, //
            {
                testdata2, "$sort([1,3,2])", "[1,2,3]"
            }, //
            {
                testdata2, "$sort([1,3,22,11])", "[1,3,11,22]"
            }, //
            {
                "[1,3,2]", "[[$], [$sort($)], [$]]",
                "[\n                [1, 3, 2],\n                [1, 2, 3],\n                [1, 3, 2]\n            ]"
            }, //
            {
                testdata2, "$sort(Account.Order.Product.Price)", "[21.67, 34.45, 34.45, 107.99]"
            }, //
            {
                testdata2, "$sort(Account.Order.Product.\"Product Name\")",
                "[\"Bowler Hat\", \"Bowler Hat\", \"Cloak\", \"Trilby hat\"]"
            }, //
            {
                testdata2, "$sort(Account.Order.Product)", null
            }, //
            {
                testdata2,
                "$sort(Account.Order.Product, function($a, $b) { $a.(Price * Quantity) > $b.(Price * Quantity) }).(Price & \" x \" & Quantity)",
                "[\n                \"21.67 x 1\",\n                \"34.45 x 2\",\n                \"107.99 x 1\",\n                \"34.45 x 4\"\n            ]"
            }, //
            {
                testdata2, "$sort(Account.Order.Product, function($a, $b) { $a.Price > $b.Price }).SKU",
                "[\n                \"0406634348\",\n                \"0406654608\",\n                \"040657863\",\n                \"0406654603\"\n            ]"
            }, //
            {
                testdata2, "null",
                "[\n                \"0406634348\",\n                \"040657863\",\n                \"0406654608\",\n                \"0406654603\"\n            ]"
            }, //
            {
                testdata4, "$sift(\u03BB($v){$v.**.Postcode})",
                "{\n                \"Address\": {\n                    \"Street\": \"Hursley Park\",\n                    \"City\": \"Winchester\",\n                    \"Postcode\": \"SO21 2JN\"\n                }, //\n                \"Other\": {\n                    \"Over 18 ?\": true,\n                    \"Misc\": null,\n                    \"Alternative.Address\": {\n                        \"Street\": \"Brick Lane\",\n                        \"City\": \"London\",\n                        \"Postcode\": \"E1 6RF\"\n                    }\n                }\n            }"
            }, //
            {
                testdata4, "**[*].$sift(\u03BB($v){$v.Postcode})",
                "[\n                {\n                    \"Address\": {\n                        \"Street\": \"Hursley Park\",\n                        \"City\": \"Winchester\",\n                        \"Postcode\": \"SO21 2JN\"\n                    }\n                }, //\n                {\n                    \"Alternative.Address\": {\n                        \"Street\": \"Brick Lane\",\n                        \"City\": \"London\",\n                        \"Postcode\": \"E1 6RF\"\n                    }\n                }\n            ]"
            }, //
            {
                testdata4, "$sift(\u03BB($v, $k){$k ~> /^A/})",
                "{\n                \"Age\": 28,\n                \"Address\": {\n                    \"Street\": \"Hursley Park\",\n                    \"City\": \"Winchester\",\n                    \"Postcode\": \"SO21 2JN\"\n                }\n            }"
            }, //
            {
                testdata2, "{\"now\": $now(), \"delay\": $sum([1..10000]), \"later\": $now()}.(now = later)",
                "true"
            }, //
            {
                testdata2, "($sum([1..10000]); $now())", null
            }, //
            {
                testdata2, "$now()", null
            }, //
            {
                testdata2, "$millis()", "result > 1502264152715 && result < 2000000000000"
            }, //
            {
                testdata2, "{\"now\": $millis(), \"delay\": $sum([1..10000]), \"later\": $millis()}.(now = later)",
                "true"
            }, //
            {
                testdata2, "($sum([1..10000]); $millis())", null
            }, //
            {
                testdata2, "$toMillis(\"1970-01-01T00:00:00.001Z\")", "1"
            }, //
            {
                testdata2, "$toMillis(\"2017-10-30T16:25:32.935Z\")", "1509380732935"
            }, //
            {
                testdata2, "$toMillis(foo)", "undefined"
            }, //
            {
                testdata2, "$toMillis(\"foo\")", null
            }, //
            {
                testdata2, "$fromMillis(1)", "\"1970-01-01T00:00:00.001Z\""
            }, //
            {
                testdata2, "$fromMillis(1509380732935)", "\"2017-10-30T16:25:32.935Z\""
            }, //
            {
                testdata2, "$fromMillis(foo)", "undefined"
            }, //
            {
                testdata2, "$clone(foo)", "undefined"
            }, //
            {
                testdata2, "$clone({})", "{}"
            }, //
            {
                testdata2, "$clone({\"a\": 1})", "{\"a\": 1}"
            }, //
            {
                testdata2, "\"s\" - 1", null
            }, //
            {
                testdata2, "1 + null", null
            }, //
            {
                testdata2, "\"no closing quote", null
            }, //
            {
                testdata2, "`no closing backtick", null
            }, //
            {
                testdata2, "- \"s\"", null
            }, //
            {
                testdata2, "unknown(function)", null
            }, //
            {
                testdata2, "sum(Account.Order.OrderID)", null
            }, //
            {
                null, "[1,2)", null
            }, //
            {
                null, "[1:2]", null
            }, //
            {
                null, "$replace(\"foo\", \"o, \"rr\")", null
            }, //
            {
                null, "[1!2]", null
            }, //
            {
                null, "@ bar", null
            }, //
            {
                null, "2(blah)", null
            }, //
            {
                null, "2()", null
            }, //
            {
                null, "3(?)", null
            }, //
            {
                null, "1=", null
            }, //
            {
                null, "function(x){$x}(3)", null
            }, //
            {
                null, "x:=1", null
            }, //
            {
                null, "2:=1", null
            }, //
            {
                null, "$foo()", null
            }, //
            {
                null, "55=>5", null
            }, //
            {
                null, "Ssum(:)", null
            }, //
            {
                null, "[1,2,3]{\"num\": $}[true]", null
            }, //
            {
                null, "[1,2,3]{\"num\": $}{\"num\": $}", null
            }, //
            {
                null, "Account.Order[0].Product;", null
            }, //
            {
                testdata2, "[]", "[]"
            }, //
            {
                testdata2, "[1]", "[1]"
            }, //
            {
                testdata2, "[1, 2]", "[1, 2]"
            }, //
            {
                testdata2, "[1, 2,3]", "[1, 2, 3]"
            }, //
            {
                testdata2, "[1, 2, [3, 4]]", "[1, 2, [3, 4]]"
            }, //
            {
                testdata2, "[1, \"two\", [\"three\", 4]]", "[1, \"two\", [\"three\", 4]]"
            }, //
            {
                testdata2, "[1, $two, [\"three\", $four]]", "[1, 2, [\"three\", \"four\"]]"
            }, //
            {
                testdata1, "[\"foo.bar\", foo.bar, [\"foo.baz\", foo.blah.baz]]",
                "[\"foo.bar\", 42, [\"foo.baz\", {\"fud\": \"hello\"}, // {\"fud\": \"world\"}]]"
            }, //
            {
                testdata2, "[1, 2, 3][0]", "1"
            }, //
            {
                testdata2, "[1, 2, [3, 4]][-1]", "[3, 4]"
            }, //
            {
                testdata2, "[1, 2, [3, 4]][-1][-1]", "4"
            }, //
            {
                testdata1, "foo.blah.baz.[fud, fud]", "[[\"hello\",\"hello\"],[\"world\",\"world\"]]"
            }, //
            {
                testdata1, "foo.blah.baz.[[fud, fud]]", "[[[\"hello\",\"hello\"]],[[\"world\",\"world\"]]]"
            }, //
            {
                testdata1a, "foo.blah.[baz].fud", "\"hello\""
            }, //
            {
                testdata1a, "foo.blah.[baz, buz].fud", "[\"hello\", \"world\"]"
            }, //
            {
                testdata4, "[Address, Other.\"Alternative.Address\"].City", "[\"Winchester\", \"London\"]"
            }, //
            {
                testdata4, "[Address, Other.`Alternative.Address`].City", "[\"Winchester\", \"London\"]"
            }, //
            {
                "null", "[0,1,2,3,4,5,6,7,8,9][$ % 2 = 0]", "[0, 2, 4, 6, 8]"
            }, //
            {
                null, "[1, 2, 3].$", "[1,2,3]"
            }, //
            {
                "[]", "[1, 2, 3].$", "[1,2,3]"
            }, //
            {
                "[4,5,6]", "[1, 2, 3].$", "[1,2,3]"
            }, //
            {
                testdata1, "[0..9]", "[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]"
            }, //
            {
                testdata1, "[0..9][$ % 2 = 0]", "[0, 2, 4, 6, 8]"
            }, //
            {
                testdata1, "[0, 4..9, 20, 22]", "[0, 4, 5, 6, 7, 8, 9, 20, 22]"
            }, //
            {
                testdata1, "[5..2]", "[]"
            }, //
            {
                testdata1, "[5..2, 2..5]", "[2, 3, 4, 5]"
            }, //
            {
                testdata1, "[-2..2]", "[-2, -1, 0, 1, 2]"
            }, //
            {
                testdata1, "[-2..2].($*$)", "[4, 1, 0, 1, 4]"
            }, //
            {
                null, "[-2..blah]", "[]"
            }, //
            {
                null, "[blah..5, 3, -2..blah]", "[3]"
            }, //
            {
                null, "[1.1 .. 5]", null
            }, //
            {
                null, "[1 .. 5.5]", null
            }, //
            {
                testdata1, "{}", "{}"
            }, //
            {
                testdata1, "{\"key\": \"value\"}", "{\"key\": \"value\"}"
            }, //
            {
                testdata1, "{\"one\": 1, \"two\": 2}", "{\"one\": 1, \"two\": 2}"
            }, //
            {
                testdata1, "{\"one\": 1, \"two\": 2}.two", "2"
            }, //
            {
                testdata1, "{\"one\": 1, \"two\": {\"three\": 3, \"four\": \"4\"}}",
                "{\"one\": 1, \"two\": {\"three\": 3, \"four\": \"4\"}}"
            }, //
            {
                testdata1, "{\"one\": 1, \"two\": [3, \"four\"]}", "{\"one\": 1, \"two\": [3, \"four\"]}"
            }, //
            {
                "null", "{\"test\": ()}", "{}"
            }, //
            {
                testdata1, "blah.{}", "undefined"
            }, //
            {
                testdata2, "Account.Order{OrderID: Product.\"Product Name\"}",
                "{\"order103\": [\"Bowler Hat\", \"Trilby hat\"], \"order104\": [\"Bowler Hat\", \"Cloak\"]}"
            }, //
            {
                testdata2, "Account.Order.{OrderID: Product.\"Product Name\"}",
                "[{\"order103\": [\"Bowler Hat\", \"Trilby hat\"]}, // {\"order104\": [\"Bowler Hat\", \"Cloak\"]}]"
            }, //
            {
                testdata2, "Account.Order.Product{$string(ProductID): Price}",
                "{\"345664\": 107.99, \"858236\": 21.67, \"858383\": [34.45, 34.45]}"
            }, //
            {
                testdata2, "Account.Order.Product{$string(ProductID): (Price)[0]}",
                "{\"345664\": 107.99, \"858236\": 21.67, \"858383\": 34.45}"
            }, //
            {
                testdata2, "Account.Order.Product.{$string(ProductID): Price}",
                "[{\"858383\": 34.45}, // {\"858236\": 21.67}, // {\"858383\": 34.45}, // {\"345664\": 107.99}]"
            }, //
            {
                testdata2, "Account.Order.Product{ProductID: \"Product Name\"}", null
            }, //
            {
                testdata2, "Account.Order.Product.{ProductID: \"Product Name\"}", null
            }, //
            {
                testdata2, "Account.Order{OrderID: $sum(Product.(Price*Quantity))}",
                "{\"order103\": 90.57000000000001, \"order104\": 245.79000000000002}"
            }, //
            {
                testdata2, "Account.Order.{OrderID: $sum(Product.(Price*Quantity))}",
                "[{\"order103\": 90.57000000000001}, // {\"order104\": 245.79000000000002}]"
            }, //
            {
                testdata2, "Account.Order.Product{$.\"Product Name\": Price, $.\"Product Name\": Price}",
                "{\n                \"Bowler Hat\": [\n                    34.45,\n                    34.45,\n                    34.45,\n                    34.45\n                ],\n                \"Trilby hat\": [\n                    21.67,\n                    21.67\n                ],\n                \"Cloak\": [\n                    107.99,\n                    107.99\n                ]\n            }"
            }, //
            {
                testdata2, "Account.Order.Product{`Product Name`: Price, `Product Name`: Price}",
                "{\n                \"Bowler Hat\": [\n                    34.45,\n                    34.45,\n                    34.45,\n                    34.45\n                ],\n                \"Trilby hat\": [\n                    21.67,\n                    21.67\n                ],\n                \"Cloak\": [\n                    107.99,\n                    107.99\n                ]\n            }"
            }, //
            {
                testdata2, "Account.Order{",
                "{\n                \"order103\": {\n                    \"TotalPrice\": 90.57000000000001,\n                    \"Items\": [\n                        \"Bowler Hat\",\n                        \"Trilby hat\"\n                    ]\n                }, //\n                \"order104\": {\n                    \"TotalPrice\": 245.79000000000002,\n                    \"Items\": [\n                        \"Bowler Hat\",\n                        \"Cloak\"\n                    ]\n                }\n            }"
            }, //
            {
                testdata2, "{",
                "{\n                \"Order\": [\n                    {\n                        \"ID\": \"order103\",\n                        \"Product\": [\n                            {\n                                \"Name\": \"Bowler Hat\",\n                                \"SKU\": 858383,\n                                \"Details\": {\n                                    \"Weight\": 0.75,\n                                    \"Dimensions\": \"300 x 200 x 210\"\n                                }\n                            }, //\n                            {\n                                \"Name\": \"Trilby hat\",\n                                \"SKU\": 858236,\n                                \"Details\": {\n                                    \"Weight\": 0.6,\n                                    \"Dimensions\": \"300 x 200 x 210\"\n                                }\n                            }\n                        ],\n                        \"Total Price\": 90.57000000000001\n                    }, //\n                    {\n                        \"ID\": \"order104\",\n                        \"Product\": [\n                            {\n                                \"Name\": \"Bowler Hat\",\n                                \"SKU\": 858383,\n                                \"Details\": {\n                                    \"Weight\": 0.75,\n                                    \"Dimensions\": \"300 x 200 x 210\"\n                                }\n                            }, //\n                            {\n                                \"Name\": \"Cloak\",\n                                \"SKU\": 345664,\n                                \"Details\": {\n                                    \"Weight\": 2,\n                                    \"Dimensions\": \"30 x 20 x 210\"\n                                }\n                            }\n                        ],\n                        \"Total Price\": 245.79000000000002\n                    }\n                ]\n            }"
            }, //
            {
                testdata2, "{",
                "{\n                \"Order\": [\n                    {\n                        \"ID\": \"order103\",\n                        \"Product\": [\n                            {\n                                \"Name\": \"Bowler Hat\",\n                                \"SKU\": 858383,\n                                \"Details\": {\n                                    \"Weight\": 0.75,\n                                    \"Dimensions\": \"300 x 200 x 210\"\n                                }\n                            }, //\n                            {\n                                \"Name\": \"Trilby hat\",\n                                \"SKU\": 858236,\n                                \"Details\": {\n                                    \"Weight\": 0.6,\n                                    \"Dimensions\": \"300 x 200 x 210\"\n                                }\n                            }\n                        ],\n                        \"Total Price\": 90.57000000000001\n                    }, //\n                    {\n                        \"ID\": \"order104\",\n                        \"Product\": [\n                            {\n                                \"Name\": \"Bowler Hat\",\n                                \"SKU\": 858383,\n                                \"Details\": {\n                                    \"Weight\": 0.75,\n                                    \"Dimensions\": \"300 x 200 x 210\"\n                                }\n                            }, //\n                            {\n                                \"Name\": \"Cloak\",\n                                \"SKU\": 345664,\n                                \"Details\": {\n                                    \"Weight\": 2,\n                                    \"Dimensions\": \"30 x 20 x 210\"\n                                }\n                            }\n                        ],\n                        \"Total Price\": 245.79000000000002\n                    }\n                ]\n            }"
            }, //
            {
                testdata4, "Phone{type: $join(number, \", \"), \"phone\":number}",
                "{\n                \"home\": \"0203 544 1234\",\n                \"phone\": [\n                    \"0203 544 1234\",\n                    \"01962 001234\",\n                    \"01962 001235\",\n                    \"077 7700 1234\"\n                ],\n                \"office\": \"01962 001234, 01962 001235\",\n                \"mobile\": \"077 7700 1234\"\n            }"
            }, //
            {
                testdata1, "fdf", null
            }, //
            {
                testdata1, "fdf.ett", null
            }, //
            {
                testdata1, "fdf.ett[10]", null
            }, //
            {
                testdata1, "fdf.ett[vc > 10]", null
            }, //
            {
                testdata1, "fdf.ett + 27", null
            }, //
            {
                testdata1, "$fdsd", null
            }, //
            {
                testdata1, "true", "true"
            }, //
            {
                testdata1, "false", "false"
            }, //
            {
                testdata1, "false or false", "false"
            }, //
            {
                testdata1, "false or true", "true"
            }, //
            {
                testdata1, "true or false", "true"
            }, //
            {
                testdata1, "true or true", "true"
            }, //
            {
                testdata1, "false and false", "false"
            }, //
            {
                testdata1, "false and true", "false"
            }, //
            {
                testdata1, "true and false", "false"
            }, //
            {
                testdata1, "true and true", "true"
            }, //
            {
                testdata1, "$not(false)", "true"
            }, //
            {
                testdata1, "$not(true)", "false"
            }, //
            {
                "{\"and\": 1, \"or\": 2}", "and=1 and or=2", "true"
            }, //
            {
                "{\"and\": 1, \"or\": 2}", "and>1 or or<=2", "true"
            }, //
            {
                "{\"and\": 1, \"or\": 2}", "and>1 or or!=2", "false"
            }, //
            {
                "{\"and\": 1, \"or\": 2}", "and and and", "true"
            }, //
            {
                "[{\"content\":{\"origin\":{\"name\":\"fakeIntegrationName\"}}}]",
                "$[].content.origin.$lowercase(name)", "[\"fakeintegrationname\"]"
            }, //
            {
                testdata1, "null", "null"
            }, //
            {
                testdata1, "[null]", "[null]"
            }, //
            {
                testdata1, "[null, null]", "[null, null]"
            }, //
            {
                testdata1, "$not(null)", "true"
            }, //
            {
                testdata1, "null = null", "true"
            }, //
            {
                testdata1, "null != null", "false"
            }, //
            {
                testdata1, "{\"true\": true, \"false\":false, \"null\": null}",
                "{\"true\": true, \"false\": false, \"null\": null}"
            }, //
            {
                testdata2, "Account.Order.Product.Price^($)", "[21.67, 34.45, 34.45, 107.99]"
            }, //
            {
                testdata2, "Account.Order.Product.Price^(<$)", "[21.67, 34.45, 34.45, 107.99]"
            }, //
            {
                testdata2, "Account.Order.Product.Price^(>$)", "[107.99, 34.45, 34.45, 21.67]"
            }, //
            {
                testdata2, "Account.Order.Product^(Price).Description.Colour",
                "[\"Orange\", \"Purple\", \"Purple\", \"Black\"]"
            }, //
            {
                testdata2, "Account.Order.Product^(Price).SKU",
                "[\"0406634348\", \"0406654608\", \"040657863\", \"0406654603\"]"
            }, //
            {
                testdata2, "Account.Order.Product^(Price * Quantity).Description.Colour",
                "[\"Orange\", \"Purple\", \"Black\", \"Purple\"]"
            }, //
            {
                testdata2, "Account.Order.Product^(Quantity, Description.Colour).Description.Colour",
                "[\"Black\", \"Orange\", \"Purple\", \"Purple\"]"
            }, //
            {
                testdata2, "Account.Order.Product^(Quantity, >Description.Colour).Description.Colour",
                "[\"Orange\", \"Black\", \"Purple\", \"Purple\"]"
            }, //
            {
                testdata2, "$ ~> |Account.Order.Product|{\"Total\":Price*Quantity}, //[\"Description\", \"SKU\"]|",
                "{\n                \"Account\": {\n                    \"Account Name\": \"Firefly\",\n                    \"Order\": [\n                        {\n                            \"OrderID\": \"order103\",\n                            \"Product\": [\n                                {\n                                    \"Product Name\": \"Bowler Hat\",\n                                    \"ProductID\": 858383,\n                                    \"Price\": 34.45,\n                                    \"Quantity\": 2,\n                                    \"Total\": 68.9\n                                }, //\n                                {\n                                    \"Product Name\": \"Trilby hat\",\n                                    \"ProductID\": 858236,\n                                    \"Price\": 21.67,\n                                    \"Quantity\": 1,\n                                    \"Total\": 21.67\n                                }\n                            ]\n                        }, //\n                        {\n                            \"OrderID\": \"order104\",\n                            \"Product\": [\n                                {\n                                    \"Product Name\": \"Bowler Hat\",\n                                    \"ProductID\": 858383,\n                                    \"Price\": 34.45,\n                                    \"Quantity\": 4,\n                                    \"Total\": 137.8\n                                }, //\n                                {\n                                    \"ProductID\": 345664,\n                                    \"Product Name\": \"Cloak\",\n                                    \"Price\": 107.99,\n                                    \"Quantity\": 1,\n                                    \"Total\": 107.99\n                                }\n                            ]\n                        }\n                    ]\n                }\n            }"
            }, //
            {
                testdata2, "$ ~> |Account.Order.Product|{\"Total\":Price*Quantity, \"Price\": Price * 1.2}|",
                "{\n                \"Account\": {\n                    \"Account Name\": \"Firefly\",\n                    \"Order\": [\n                        {\n                            \"OrderID\": \"order103\",\n                            \"Product\": [\n                                {\n                                    \"Product Name\": \"Bowler Hat\",\n                                    \"ProductID\": 858383,\n                                    \"SKU\": \"0406654608\",\n                                    \"Description\": {\n                                        \"Colour\": \"Purple\",\n                                        \"Width\": 300,\n                                        \"Height\": 200,\n                                        \"Depth\": 210,\n                                        \"Weight\": 0.75\n                                    }, //\n                                    \"Price\": 41.34,\n                                    \"Quantity\": 2,\n                                    \"Total\": 68.9\n                                }, //\n                                {\n                                    \"Product Name\": \"Trilby hat\",\n                                    \"ProductID\": 858236,\n                                    \"SKU\": \"0406634348\",\n                                    \"Description\": {\n                                        \"Colour\": \"Orange\",\n                                        \"Width\": 300,\n                                        \"Height\": 200,\n                                        \"Depth\": 210,\n                                        \"Weight\": 0.6\n                                    }, //\n                                    \"Price\": 26.004,\n                                    \"Quantity\": 1,\n                                    \"Total\": 21.67\n                                }\n                            ]\n                        }, //\n                        {\n                            \"OrderID\": \"order104\",\n                            \"Product\": [\n                                {\n                                    \"Product Name\": \"Bowler Hat\",\n                                    \"ProductID\": 858383,\n                                    \"SKU\": \"040657863\",\n                                    \"Description\": {\n                                        \"Colour\": \"Purple\",\n                                        \"Width\": 300,\n                                        \"Height\": 200,\n                                        \"Depth\": 210,\n                                        \"Weight\": 0.75\n                                    }, //\n                                    \"Price\": 41.34,\n                                    \"Quantity\": 4,\n                                    \"Total\": 137.8\n                                }, //\n                                {\n                                    \"ProductID\": 345664,\n                                    \"SKU\": \"0406654603\",\n                                    \"Product Name\": \"Cloak\",\n                                    \"Description\": {\n                                        \"Colour\": \"Black\",\n                                        \"Width\": 30,\n                                        \"Height\": 20,\n                                        \"Depth\": 210,\n                                        \"Weight\": 2\n                                    }, //\n                                    \"Price\": 129.588,\n                                    \"Quantity\": 1,\n                                    \"Total\": 107.99\n                                }\n                            ]\n                        }\n                    ]\n                }\n            }"
            }, //
            {
                testdata2, "$ ~> |Account.Order.Product|{}, //\"Description\"|",
                "{\n                \"Account\": {\n                    \"Account Name\": \"Firefly\",\n                    \"Order\": [\n                        {\n                            \"OrderID\": \"order103\",\n                            \"Product\": [\n                                {\n                                    \"Product Name\": \"Bowler Hat\",\n                                    \"ProductID\": 858383,\n                                    \"SKU\": \"0406654608\",\n                                    \"Price\": 34.45,\n                                    \"Quantity\": 2\n                                }, //\n                                {\n                                    \"Product Name\": \"Trilby hat\",\n                                    \"ProductID\": 858236,\n                                    \"SKU\": \"0406634348\",\n                                    \"Price\": 21.67,\n                                    \"Quantity\": 1\n                                }\n                            ]\n                        }, //\n                        {\n                            \"OrderID\": \"order104\",\n                            \"Product\": [\n                                {\n                                    \"Product Name\": \"Bowler Hat\",\n                                    \"ProductID\": 858383,\n                                    \"SKU\": \"040657863\",\n                                    \"Price\": 34.45,\n                                    \"Quantity\": 4\n                                }, //\n                                {\n                                    \"ProductID\": 345664,\n                                    \"SKU\": \"0406654603\",\n                                    \"Product Name\": \"Cloak\",\n                                    \"Price\": 107.99,\n                                    \"Quantity\": 1\n                                }\n                            ]\n                        }\n                    ]\n                }\n            }"
            }, //
            {
                testdata2, "$ ~> |Account.Order.Product|nomatch,\"Description\"|",
                "{\n                \"Account\": {\n                    \"Account Name\": \"Firefly\",\n                    \"Order\": [\n                        {\n                            \"OrderID\": \"order103\",\n                            \"Product\": [\n                                {\n                                    \"Product Name\": \"Bowler Hat\",\n                                    \"ProductID\": 858383,\n                                    \"SKU\": \"0406654608\",\n                                    \"Price\": 34.45,\n                                    \"Quantity\": 2\n                                }, //\n                                {\n                                    \"Product Name\": \"Trilby hat\",\n                                    \"ProductID\": 858236,\n                                    \"SKU\": \"0406634348\",\n                                    \"Price\": 21.67,\n                                    \"Quantity\": 1\n                                }\n                            ]\n                        }, //\n                        {\n                            \"OrderID\": \"order104\",\n                            \"Product\": [\n                                {\n                                    \"Product Name\": \"Bowler Hat\",\n                                    \"ProductID\": 858383,\n                                    \"SKU\": \"040657863\",\n                                    \"Price\": 34.45,\n                                    \"Quantity\": 4\n                                }, //\n                                {\n                                    \"ProductID\": 345664,\n                                    \"SKU\": \"0406654603\",\n                                    \"Product Name\": \"Cloak\",\n                                    \"Price\": 107.99,\n                                    \"Quantity\": 1\n                                }\n                            ]\n                        }\n                    ]\n                }\n            }"
            }, //
            {
                testdata2, "$ ~> |(Account.Order.Product)[0]|{\"Description\":\"blah\"}|",
                "{\n                \"Account\": {\n                    \"Account Name\": \"Firefly\",\n                    \"Order\": [\n                        {\n                            \"OrderID\": \"order103\",\n                            \"Product\": [\n                                {\n                                    \"Product Name\": \"Bowler Hat\",\n                                    \"ProductID\": 858383,\n                                    \"SKU\": \"0406654608\",\n                                    \"Description\": \"blah\",\n                                    \"Price\": 34.45,\n                                    \"Quantity\": 2\n                                }, //\n                                {\n                                    \"Product Name\": \"Trilby hat\",\n                                    \"ProductID\": 858236,\n                                    \"SKU\": \"0406634348\",\n                                    \"Description\": {\n                                        \"Colour\": \"Orange\",\n                                        \"Width\": 300,\n                                        \"Height\": 200,\n                                        \"Depth\": 210,\n                                        \"Weight\": 0.6\n                                    }, //\n                                    \"Price\": 21.67,\n                                    \"Quantity\": 1\n                                }\n                            ]\n                        }, //\n                        {\n                            \"OrderID\": \"order104\",\n                            \"Product\": [\n                                {\n                                    \"Product Name\": \"Bowler Hat\",\n                                    \"ProductID\": 858383,\n                                    \"SKU\": \"040657863\",\n                                    \"Description\": {\n                                        \"Colour\": \"Purple\",\n                                        \"Width\": 300,\n                                        \"Height\": 200,\n                                        \"Depth\": 210,\n                                        \"Weight\": 0.75\n                                    }, //\n                                    \"Price\": 34.45,\n                                    \"Quantity\": 4\n                                }, //\n                                {\n                                    \"ProductID\": 345664,\n                                    \"SKU\": \"0406654603\",\n                                    \"Product Name\": \"Cloak\",\n                                    \"Description\": {\n                                        \"Colour\": \"Black\",\n                                        \"Width\": 30,\n                                        \"Height\": 20,\n                                        \"Depth\": 210,\n                                        \"Weight\": 2\n                                    }, //\n                                    \"Price\": 107.99,\n                                    \"Quantity\": 1\n                                }\n                            ]\n                        }\n                    ]\n                }\n            }"
            }, //
            {
                testdata2, "$ ~> |foo.bar|{\"Description\":\"blah\"}|", "testdata2"
            }, //
            {
                testdata2, "Account ~> |Order|{\"Product\":\"blah\"}, //nomatch|",
                "{\n                \"Account Name\": \"Firefly\",\n                \"Order\": [\n                    {\n                        \"OrderID\": \"order103\",\n                        \"Product\": \"blah\"\n                    }, //\n                    {\n                        \"OrderID\": \"order104\",\n                        \"Product\": \"blah\"\n                    }\n                ]\n            }"
            }, //
            {
                testdata2, "foo ~> |foo.bar|{\"Description\":\"blah\"}|", "undefined"
            }, //
            {
                testdata2, "Account ~> |Order|5|", null
            }, //
            {
                testdata2, "Account ~> |Order|{}, //5|", null
            }, //
            {
                testdata2, "Account ~> |Order|{\"Product\":\"blah\"}, //nomatch|",
                "{\n                \"Account Name\": \"Firefly\",\n                \"Order\": [\n                    {\n                        \"OrderID\": \"order103\",\n                        \"Product\": \"blah\"\n                    }, //\n                    {\n                        \"OrderID\": \"order104\",\n                        \"Product\": \"blah\"\n                    }\n                ]\n            }"
            }, //
            {
                testdata2, "( $clone := 5; $ ~> |Account.Order.Product|{\"blah\":\"foo\"}| )", null
            }, //
            {
                "\"Bus\"", "[\"Red\"[$$=\"Bus\"], \"White\"[$$=\"Police Car\"]][0]", "\"Red\""
            }, //
            {
                "\"Police Car\"", "[\"Red\"[$$=\"Bus\"], \"White\"[$$=\"Police Car\"]][0]", "\"White\""
            }, //
            {
                "\"Tuk tuk\"", "[\"Red\"[$$=\"Bus\"], \"White\"[$$=\"Police Car\"]][0]", "undefined"
            }, //
            {
                "\"Bus\"", "$lookup({\"Bus\": \"Red\", \"Police Car\": \"White\"}, // $$)", "\"Red\""
            }, //
            {
                "\"Police Car\"", "$lookup({\"Bus\": \"Red\", \"Police Car\": \"White\"}, // $$)", "\"White\""
            }, //
            {
                "\"Tuk tuk\"", "$lookup({\"Bus\": \"Red\", \"Police Car\": \"White\"}, // $$)", "undefined"
            }, //
            {
                testdata2, "Account.Order.Product.(Price < 30 ? \"Cheap\")", "\"Cheap\""
            }, //
            {
                testdata2, "Account.Order.Product.(Price < 30 ? \"Cheap\" : \"Expensive\")",
                "[\"Expensive\", \"Cheap\", \"Expensive\", \"Expensive\"]"
            }, //
            {
                testdata2,
                "Account.Order.Product.(Price < 30 ? \"Cheap\" : Price < 100 ? \"Expensive\" : \"Rip off\")",
                "[\"Expensive\", \"Cheap\", \"Expensive\", \"Rip off\"]"
            }, //
            {
                "null", "function($x){$x*$x}(5)", "25"
            }, //
            {
                "null", "function($x){$x>5 ? \"foo\"}(6)", "\"foo\""
            }, //
            {
                "null", "function($x){$x>5 ? \"foo\"}(3)", "undefined"
            }, //
            {
                "null", "($factorial:= function($x){$x <= 1 ? 1 : $x * $factorial($x-1)}; $factorial(4))", "24"
            }, //
            {
                "null",
                "($fibonacci := function($x){$x <= 1 ? $x : $fibonacci($x-1) + $fibonacci($x-2)}; [1,2,3,4,5,6,7,8,9].$fibonacci($))",
                "[1, 1, 2, 3, 5, 8, 13, 21, 34]"
            }, //
            {
                testdata2, "($nth_price := function($n) { (Account.Order.Product.Price)[$n] }; $nth_price(1) )",
                "21.67"
            }, //
            {
                null, "            (", "true"
            }, //
            {
                null, "            (", "false"
            }, //
            {
                null, "        (", "true"
            }, //
            {
                null, "(", "[4,3]"
            }, //
            {
                null, "(", "[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]"
            }, //
            {
                null, "(", "[0, 2, 4, 6, 8, 10, 12, 14]"
            }, //
            {
                null, "            (", "undefined"
            }, //
            {
                null, "(", "9.33262154439441e+155"
            }, //
            {
                null, "(", null
            }, //
            {
                null, "(", "120"
            }, //
            {
                null, "(", "5.7133839564458575e+262"
            }, //
            {
                null, "(", null
            }, //
            {
                null, "(", null
            }, //
            {
                null, "        (", "true"
            }, //
            {
                "null",
                "($twice:=function($f){function($x){$f($f($x))}}; $add3:=function($y){$y+3}; $add6:=$twice($add3); $add6(7))",
                "13"
            }, //
            {
                "null",
                "\u03BB($f) { \u03BB($x) { $x($x) }( \u03BB($g) { $f( (\u03BB($a) {$g($g)($a)}))})}(\u03BB($f) { \u03BB($n) { $n < 2 ? 1 : $n * $f($n - 1) } })(6)",
                "720"
            }, //
            {
                "null",
                "\u03BB($f) { \u03BB($x) { $x($x) }( \u03BB($g) { $f( (\u03BB($a) {$g($g)($a)}))})}(\u03BB($f) { \u03BB($n) { $n <= 1 ? $n : $f($n-1) + $f($n-2) } })(6) ",
                "8"
            }, //
            {
                "null", "()", "undefined"
            }, //
            {
                "null", "(1; 2; 3)", "3"
            }, //
            {
                "null", "(1; 2; 3;)", "3"
            }, //
            {
                "null", "($a:=1; $b:=2; $c:=($a:=4; $a+$b); $a+$c)", "7"
            }, //
            {
                testdata2, "Account.Order.Product.($var1 := Price ; $var2:=Quantity; $var1 * $var2)",
                "[68.9, 21.67, 137.8, 107.99]"
            }, //
            {
                testdata2, "(", "\"order103\""
            }, //
            {
                testdata2, "(", "\"order103\""
            }, //
            {
                testdata2, "Account.(",
                "{\n                \"Account\": \"Firefly\",\n                \"SKU-858383\": \"Bowler Hat\",\n                \"SKU-345664\": \"Cloak\"\n            }"
            }, //
            {
                testdata2, "Account.(",
                "{\n                \"Account\": \"Firefly\",\n                \"SKU-858383\": \"Bowler Hat\",\n                \"SKU-345664\": \"Cloak\"\n            }"
            }, //
            {
                testdata2, "(", "5"
            }, //
            {
                testdata2, "(", "6"
            }, //
            {
                testdata2, "(", "\"Hello\""
            }, //
            {
                testdata2, "(", "\"Hello\""
            }, //
            {
                "null", "(", "[1, 4, 9, 16, 25]"
            }, //
            {
                null, "(", null
            }, //
            {
                "null", "$map([1,2,3], $string)", "[\"1\",\"2\",\"3\"]"
            }, //
            {
                testdata2,
                "Account.Order.Product ~> $map(\u03BB($prod, $index) { $index+1 & \": \" & $prod.\"Product Name\" })",
                "[\n                \"1: Bowler Hat\",\n                \"2: Trilby hat\",\n                \"3: Bowler Hat\",\n                \"4: Cloak\"\n            ]"
            }, //
            {
                testdata2, "$map([1,4,9,16], $squareroot)", "[1,2,3,4]"
            }, //
            {
                testdata2, "$map([1,4,9,16], $squareroot)", "[1,2,3,4]"
            }, //
            {
                testdata2, "$map([1,4,9,16], $squareroot)", "[1,2,3,4]"
            }, //
            {
                testdata4, "$map(Phone, function($v, $i) {$i[$v.type=\"office\"]})", "[1,2]"
            }, //
            {
                testdata4, "$map(Phone, function($v, $i) {$v.type=\"office\" ? $i})", "[1,2]"
            }, //
            {
                testdata4, "$map(Phone, function($v, $i) {$v.type=\"office\" ? $i: null})", "[null, 1, 2, null]"
            }, //
            {
                testdata2, "$map(Phone, function($v, $i) {$v.type=\"office\" ? $i: null})", "undefined"
            }, //
            {
                testdata5, "(library.books~>$filter(\u03BB($v, $i, $a) {$v.price = $max($a.price)})).isbn",
                "\"9780262510875\""
            }, //
            {
                testdata5, "nothing~>$filter(\u03BB($v, $i, $a) {$v.price = $max($a.price)})", "undefined"
            }, //
            {
                "null", "(", "[6, 6, 6, 6, 6]"
            }, //
            {
                "null", "(", "[6, 6, 6, 6, 6]"
            }, //
            {
                "null", "(", "[6]"
            }, //
            {
                "null", "(", "[6]"
            }, //
            {
                "null", "(", "15"
            }, //
            {
                "null", "null", "\"1 ... 2 ... 3 ... 4 ... 5\""
            }, //
            {
                "null", "(", "17"
            }, //
            {
                "null", "(", "1"
            }, //
            {
                "null", "(", "1"
            }, //
            {
                testdata2, "$reduce(Account.Order.Product.Quantity, $append)", "[2, 1, 4, 1]"
            }, //
            {
                testdata4, "$reduce(Account.Order.Product.Quantity, $append)", "undefined"
            }, //
            {
                null, "($product := function($a, $b) { $a * $b };", "[1, 2, 4, 8, 16, 32]"
            }, //
            {
                null, "(", null
            }, //
            {
                null, "/ab/ (\"ab\")", "{\"match\": \"ab\", \"start\": 0, \"end\": 2, \"groups\": []}"
            }, //
            {
                null, "/ab/ ()", "undefined"
            }, //
            {
                null, "/ab+/ (\"ababbabbcc\")", "{\"match\": \"ab\", \"start\": 0, \"end\": 2, \"groups\": []}"
            }, //
            {
                null, "/a(b+)/ (\"ababbabbcc\")",
                "{\"match\": \"ab\", \"start\": 0, \"end\": 2, \"groups\": [\"b\"]}"
            }, //
            {
                null, "/a(b+)/ (\"ababbabbcc\").next()",
                "{\"match\": \"abb\", \"start\": 2, \"end\": 5, \"groups\": [\"bb\"]}"
            }, //
            {
                null, "/a(b+)/ (\"ababbabbcc\").next().next()",
                "{\"match\": \"abb\", \"start\": 5, \"end\": 8, \"groups\": [\"bb\"]}"
            }, //
            {
                null, "/a(b+)/ (\"ababbabbcc\").next().next().next()", "undefined"
            }, //
            {
                null, "/a(b+)/i (\"Ababbabbcc\")",
                "{\"match\": \"Ab\", \"start\": 0, \"end\": 2, \"groups\": [\"b\"]}"
            }, //
            {
                null, "//", null
            }, //
            {
                null, "/", null
            }, //
            {
                null, "$match(\"ababbabbcc\",/ab/)",
                "[{\"match\": \"ab\", \"index\": 0, \"groups\": []}, // {\n                    \"match\": \"ab\",\n                    \"index\": 2,\n                    \"groups\": []\n                }, // {\"match\": \"ab\", \"index\": 5, \"groups\": []}]"
            }, //
            {
                null, "$match(\"ababbabbcc\",/a(b+)/)",
                "[{\"match\": \"ab\", \"index\": 0, \"groups\": [\"b\"]}, // {\n                    \"match\": \"abb\",\n                    \"index\": 2,\n                    \"groups\": [\"bb\"]\n                }, // {\"match\": \"abb\", \"index\": 5, \"groups\": [\"bb\"]}]"
            }, //
            {
                null, "$match(\"ababbabbcc\",/a(b+)/, 1)",
                "[{\"match\": \"ab\", \"index\": 0, \"groups\": [\"b\"]}]"
            }, //
            {
                null, "$match(\"ababbabbcc\",/a(b+)/, 0)", "[]"
            }, //
            {
                null, "$match(nothing,/a(xb+)/)", "undefined"
            }, //
            {
                null, "$match(\"ababbabbcc\",/a(xb+)/)", "[]"
            }, //
            {
                null, "$match(\"a, b, c, d\", /ab/, -3)", null
            }, //
            {
                null, "$match(\"a, b, c, d\", /ab/, null)", null
            }, //
            {
                null, "$match(\"a, b, c, d\", /ab/, \"2\")", null
            }, //
            {
                null, "$match(\"a, b, c, d\", \"ab\")", null
            }, //
            {
                null, "$match(\"a, b, c, d\", true)", null
            }, //
            {
                null, "$match(12345, 3)", null
            }, //
            {
                null, "$match(12345)", null
            }, //
            {
                null, "$split(\"ababbxabbcc\",/b+/)", "[\"a\", \"a\", \"xa\", \"cc\"]"
            }, //
            {
                null, "$split(\"ababbxabbcc\",/b+/, 2)", "[\"a\", \"a\"]"
            }, //
            {
                null, "$split(\"ababbxabbcc\",/d+/)", "[\"ababbxabbcc\"]"
            }, //
            {
                null, "$contains(\"ababbxabbcc\",/ab+/)", "true"
            }, //
            {
                null, "$contains(\"ababbxabbcc\",/ax+/)", "false"
            }, //
            {
                testdata2, "Account.Order.Product[$contains($.\"Product Name\", /hat/)].ProductID", "858236"
            }, //
            {
                testdata2, "Account.Order.Product[$contains($.\"Product Name\", /hat/i)].ProductID",
                "[858383, 858236, 858383]"
            }, //
            {
                null, "$replace(\"ababbxabbcc\",/b+/, \"yy\")", "\"ayyayyxayycc\""
            }, //
            {
                null, "$replace(\"ababbxabbcc\",/b+/, \"yy\", 2)", "\"ayyayyxabbcc\""
            }, //
            {
                null, "$replace(\"ababbxabbcc\",/b+/, \"yy\", 0)", "\"ababbxabbcc\""
            }, //
            {
                null, "$replace(\"ababbxabbcc\",/d+/, \"yy\")", "\"ababbxabbcc\""
            }, //
            {
                null, "$replace(\"John Smith\", /(\\\\w+)\\\\s(\\\\w+)/, \"$2, $1\")", "\"Smith, John\""
            }, //
            {
                null, "$replace(\"265USD\", /([0-9]+)USD/, \"$$$1\")", "\"$265\""
            }, //
            {
                null, "$replace(\"265USD\", /([0-9]+)USD/, \"$w\")", "\"$w\""
            }, //
            {
                null, "$replace(\"265USD\", /([0-9]+)USD/, \"$0 -> $$$1\")", "\"265USD -> $265\""
            }, //
            {
                null, "$replace(\"265USD\", /([0-9]+)USD/, \"$0$1$2\")", "\"265USD265\""
            }, //
            {
                null, "$replace(\"abcd\", /(ab)|(a)/, \"[1=$1][2=$2]\")", "\"[1=ab][2=]cd\""
            }, //
            {
                null, "$replace(\"abracadabra\", /bra/, \"*\")", "\"a*cada*\""
            }, //
            {
                null, "$replace(\"abracadabra\", /a.*a/, \"*\")", "\"*\""
            }, //
            {
                null, "$replace(\"abracadabra\", /a.*?a/, \"*\")", "\"*c*bra\""
            }, //
            {
                null, "$replace(\"abracadabra\", /a/, \"\")", "\"brcdbr\""
            }, //
            {
                null, "$replace(\"abracadabra\", /a(.)/, \"a$1$1\")", "\"abbraccaddabbra\""
            }, //
            {
                null, "$replace(\"abracadabra\", /.*?/, \"$1\")", null
            }, //
            {
                null, "$replace(\"AAAA\", /A+/, \"b\")", "\"b\""
            }, //
            {
                null, "$replace(\"AAAA\", /A+?/, \"b\")", "\"bbbb\""
            }, //
            {
                null, "$replace(\"darted\", /^(.*?)d(.*)$/, \"$1c$2\")", "\"carted\""
            }, //
            {
                null, "$replace(\"abcdefghijklmno\", /(a)(b)(c)(d)(e)(f)(g)(h)(i)(j)(k)(l)(m)/, \"$8$5$12$12$18$123\")",
                "\"hella8l3no\""
            }, //
            {
                null, "$replace(\"abcdefghijklmno\", /xyz/, \"$8$5$12$12$18$123\")", "\"abcdefghijklmno\""
            }, //
            {
                null, "$replace(\"abcdefghijklmno\", /ijk/, \"$8$5$12$12$18$123\")", "\"abcdefgh22823lmno\""
            }, //
            {
                null, "$replace(\"abcdefghijklmno\", /(ijk)/, \"$8$5$12$12$18$123\")",
                "\"abcdefghijk2ijk2ijk8ijk23lmno\""
            }, //
            {
                null, "$replace(\"abcdefghijklmno\", /ijk/, \"$x\")", "\"abcdefgh$xlmno\""
            }, //
            {
                null, "$replace(\"abcdefghijklmno\", /(ijk)/, \"$x$\")", "\"abcdefgh$x$lmno\""
            }, //
            {
                testdata2, "Account.Order.Product.$replace($.\"Product Name\", /hat/i, function($match) { \"foo\" })",
                "[\"Bowler foo\", \"Trilby foo\", \"Bowler foo\", \"Cloak\"]"
            }, //
            {
                testdata2,
                "Account.Order.Product.$replace($.\"Product Name\", /(h)(at)/i, function($match) { $uppercase($match.match) })",
                "[\"Bowler HAT\", \"Trilby HAT\", \"Bowler HAT\", \"Cloak\"]"
            }, //
            {
                null, "$replace(\"temperature = 68F today\", /(-?\\\\d+(?:\\\\.\\\\d*)?)F\\\\b/, function($m) { ($number($m.groups[0]) - 32) * 5/9 & \"C\" })",
                "\"temperature = 20C today\""
            }, //
            {
                testdata2, "Account.Order.Product.$replace($.\"Product Name\", /hat/i, function($match) { true })",
                null
            }, //
            {
                testdata2, "Account.Order.Product.$replace($.\"Product Name\", /hat/i, function($match) { 42 })",
                null
            }, //
            {
                null, "$base64encode(\"hello:world\")", "\"aGVsbG86d29ybGQ=\""
            }, //
            {
                null, "$base64encode()", "undefined"
            }, //
            {
                null, "$base64decode(\"aGVsbG86d29ybGQ=\")", "\"hello:world\""
            }, //
            {
                null, "$base64decode()", "undefined"
            }, //
            {
                testdata2, "Account.Order[0].OrderID ~> $uppercase()", "\"ORDER103\""
            }, //
            {
                testdata2, "Account.Order[0].OrderID ~> $uppercase() ~> $lowercase()", "\"order103\""
            }, //
            {
                testdata2, "Account.Order.OrderID ~> $join()", "\"order103order104\""
            }, //
            {
                testdata2, "Account.Order.OrderID ~> $join(\", \")", "\"order103, order104\""
            }, //
            {
                testdata2, "Account.Order.Product.(Price * Quantity) ~> $sum()", "336.36"
            }, //
            {
                null, "( $uppertrim := $trim ~> $uppercase;  $uppertrim(\"   Hello    World   \") )",
                "\"HELLO WORLD\""
            }, //
            {
                null, "\"john@example.com\" ~> $substringAfter(\"@\") ~> $substringBefore(\".\") ", "\"example\""
            }, //
            {
                "\"test\"", "\"\" ~> $substringAfter(\"@\") ~> $substringBefore(\".\") ", "\"\""
            }, //
            {
                "\"test\"", "foo ~> $substringAfter(\"@\") ~> $substringBefore(\".\") ", "undefined"
            }, //
            {
                null, "( $domain := $substringAfter(?,\"@\") ~> $substringBefore(?,\".\"); $domain(\"john@example.com\") )",
                "\"example\""
            }, //
            {
                null, "( $square := function($x){$x*$x}; [1..5] ~> $map($square) ) ", "[1, 4, 9, 16, 25]"
            }, //
            {
                null, "( $square := function($x){$x*$x}; [1..5] ~> $map($square) ~> $sum() ) ", "55"
            }, //
            {
                null, "(", "\"foo\""
            }, //
            {
                null, "(", "225"
            }, //
            {
                null, "(", "\"225\""
            }, //
            {
                null, "(", "225"
            }, //
            {
                null, "(", "55"
            }, //
            {
                null, "(", "14400"
            }, //
            {
                null, "(", "14400"
            }, //
            {
                testdata2, "(", "336.36"
            }, //
            {
                testdata2, "42 ~> \"hello\"", null
            }, //
            {
                testdata2, "Account.Order.Product[$.\"Product Name\" ~> /hat/i].ProductID",
                "[858383,858236,858383]"
            }, //
            {
                null, "\u03BB($arg)<b:b>{$not($arg)}(true)", "false"
            }, //
            {
                null, "\u03BB($arg)<b:b>{$not($arg)}(foo)", "true"
            }, //
            {
                null, "\u03BB($arg)<x:b>{$not($arg)}(null)", "true"
            }, //
            {
                null, "function($x,$y)<n-n:n>{$x+$y}(2, 6)", "8"
            }, //
            {
                null, "[1..5].function($x,$y)<n-n:n>{$x+$y}(6)", "[7,8,9,10,11]"
            }, //
            {
                null, "[1..5].function($x,$y)<n-n:n>{$x+$y}(2, 6)", "[8,8,8,8,8]"
            }, //
            {
                testdata4, "Age.function($x,$y)<n-n:n>{$x+$y}(6)", "34"
            }, //
            {
                null, "\u03BB($str)<s->{$uppercase($str)}(\"hello\")", "\"HELLO\""
            }, //
            {
                testdata2, "Account.Order.Product.Description.Colour.\u03BB($str)<s->{$uppercase($str)}()",
                "[\"PURPLE\", \"ORANGE\", \"PURPLE\", \"BLACK\"]"
            }, //
            {
                null, "\u03BB($str, $prefix)<s-s>{$prefix & $str}(\"World\", \"Hello \")", "\"Hello World\""
            }, //
            {
                testdata4, "FirstName.\u03BB($str, $prefix)<s-s>{$prefix & $str}(\"Hello \")", "\"Hello Fred\""
            }, //
            {
                null, "\u03BB($arr, $sep)<a<s>s?:s>{$join($arr, $sep)}(\"a\")", "\"a\""
            }, //
            {
                testdata4, "\u03BB($arr, $sep)<a<s>s?:s>{$join($arr, $sep)}([\"a\"])", "\"a\""
            }, //
            {
                null, "\u03BB($arr, $sep)<a<s>s?:s>{$join($arr, $sep)}(\"a\", \"-\")", "\"a\""
            }, //
            {
                null, "\u03BB($arr, $sep)<a<s>s?:s>{$join($arr, $sep)}([\"a\"], \"-\")", "\"a\""
            }, //
            {
                null, "\u03BB($arr, $sep)<a<s>s?:s>{$join($arr, $sep)}([\"a\", \"b\"], \"-\")", "\"a-b\""
            }, //
            {
                null, "\u03BB($arr, $sep)<as?:s>{$join($arr, $sep)}([\"a\", \"b\"], \"-\")", "\"a-b\""
            }, //
            {
                null, "\u03BB($arr, $sep)<a<s>s?:s>{$join($arr, $sep)}([], \"-\")", "\"\""
            }, //
            {
                null, "\u03BB($arr, $sep)<a<s>s?:s>{$join($arr, $sep)}(foo, \"-\")", "undefined"
            }, //
            {
                null, "\u03BB($obj)<o>{$obj}({\"hello\": \"world\"})", "{\"hello\": \"world\"}"
            }, //
            {
                null, "\u03BB($arr)<a<a<n>>>{$arr}([[1]])", "[[1]]"
            }, //
            {
                null, "\u03BB($num)<(ns)-:n>{$number($num)}(5)", "5"
            }, //
            {
                null, "\u03BB($num)<(ns)-:n>{$number($num)}(\"5\")", "5"
            }, //
            {
                null, "[1..5].\u03BB($num)<(ns)-:n>{$number($num)}()", "[1,2,3,4,5]"
            }, //
            {
                null, "(", "9"
            }, //
            {
                null, "(", "9"
            }, //
            {
                null, "\u03BB($arg)<n<n>>{$arg}(5)", null
            }, //
            {
                null, "\u03BB($arg1, $arg2)<nn:a>{[$arg1, $arg2]}(1,\"2\")", null
            }, //
            {
                null, "\u03BB($arg1, $arg2)<nn:a>{[$arg1, $arg2]}(1,3,\"2\")", null
            }, //
            {
                null, "\u03BB($arg1, $arg2)<nn+:a>{[$arg1, $arg2]}(1,3, 2,\"g\")", null
            }, //
            {
                null, "\u03BB($arr)<a<n>>{$arr}([\"3\"]) ", null
            }, //
            {
                null, "\u03BB($arr)<a<n>>{$arr}([1, 2, \"3\"]) ", null
            }, //
            {
                null, "\u03BB($arr)<a<n>>{$arr}(\"f\")", null
            }, //
            {
                null, "(", null
            }, //
            {
                null, "\u03BB($arr)<(sa<n>)>>{$arr}([[1]])", null
            }, //
            {
                null, "[1..5].$string()", "[\"1\", \"2\", \"3\", \"4\", \"5\"]"
            }, //
            {
                null, "[1..5].(\"Item \" & $string())", "[\"Item 1\",\"Item 2\",\"Item 3\",\"Item 4\",\"Item 5\"]"
            }, //
            {
                testdata2, "Account.Order.Product.\"Product Name\".$uppercase().$substringBefore(\" \")",
                "[\"BOWLER\", \"TRILBY\", \"BOWLER\", \"CLOAK\"]"
            }, //
            {
                testdata2, "null",
                "{\n                \"Order\": [\n                    {\n                        \"ID\": \"order103\",\n                        \"Product\": [\n                            {\n                                \"SKU\": 858383,\n                                \"Details\": {\n                                    \"Weight\": 0.75,\n                                    \"Dimensions\": \"300 x 200 x 210\"\n                                }\n                            }, //\n                            {\n                                \"SKU\": 858236,\n                                \"Details\": {\n                                    \"Weight\": 0.6,\n                                    \"Dimensions\": \"300 x 200 x 210\"\n                                }\n                            }\n                        ],\n                        \"Total Price\": 90.57000000000001\n                    }, //\n                    {\n                        \"ID\": \"order104\",\n                        \"Product\": [\n                            {\n                                \"SKU\": 858383,\n                                \"Details\": {\n                                    \"Weight\": 0.75,\n                                    \"Dimensions\": \"300 x 200 x 210\"\n                                }\n                            }, //\n                            {\n                                \"SKU\": 345664,\n                                \"Details\": {\n                                    \"Weight\": 2,\n                                    \"Dimensions\": \"30 x 20 x 210\"\n                                }\n                            }\n                        ],\n                        \"Total Price\": 245.79000000000002\n                    }\n                ]\n            }"
            }, //
            {
                null, "null", null
            }, //
            {
                null, "$", "{output: {is: {same: {as: \"input\"}}}}"
            }, //
            {
                null, "maz.rar", "42"
            }, //
            {
                null, "foo", null
            }, //
            {
                null, "$", null
            }, //
            {
                null, "$substringAfter(Salutation & \" \" & MiddleName &\" \" & Surname, MiddleName)", null
            }, //
            {
                null, "$lowercase(\"Missing close brackets\"", null
            }, //
            {
                null, "$lowercase(\"Coca\", \"Cola\")", null
            }, //
            {
                null, "$uppercase(\"Coca\", \"Cola\")", null
            }, //
            {
                null, "$substringBefore(\"Coca\" & \"ca\")", null
            }, //
            {
                null, "$substringAfter(\"Coca\" & \"ca\")", null
            }, //
            {
                null, "$substring(\"Coca\" & \"ca\", 2, 4, 5)", null
            }, //
            {
                null, "detail.contents", "\"stuff\""
            }, //

        });
    }

    @Test
    public void runTest() throws Exception {
        ObjectMapper m = new ObjectMapper();
        test(this.expression, expected == null ? null : m.readTree(expected), null,
            input == null ? null : m.readTree(input));

    }

}
