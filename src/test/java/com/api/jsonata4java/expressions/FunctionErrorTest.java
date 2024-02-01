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
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.api.jsonata4java.expressions.functions.ReplaceFunction;

/**
 * A JUnit test class used in order to ease debugging of particular problematic cases.
 * 
 * @author Martin Bluemel
 */
public class FunctionErrorTest {

    @Test
    public void testToMillis_failingInEuropeBetweenMidnightAnd2() throws Exception {
        test("($toMillis('13:45', '[H]:[m]') ~> $fromMillis() ~> $substringBefore('T')) = $substringBefore($now(), 'T')", "true", null, null);
    }

    // From JSONata test group "regex"

    // in contrary to JSONata Java does not allow to use capturing group numbers in
    // the replacement you have not defined in the regular expression... don't know any sensible use
    // of the JSONata behavior.
    @Test
    public void testRegex_case15() throws Exception {
        // test("$replace(\"265USD\", /([0-9]+)USD/, \"$0$1$2\")", "\"265USD265\"",
        // null, null);

        // these expressions work out equally in JSONata and Java
        test("$replace(\"265USD\", /([0-9]+)USD/, \"$0$1\")", "\"265USD265\"", null, null);
        test("$replace(\"265USD\", /([0-9]+)USD(.*)/, \"$0$1$2\")", "\"265USD265\"", null, null);
    }

    @Test
    public void testRegex_case16() throws Exception {
        // test("$replace(\"abcd\", /(ab)|(a)/, \"[1=$1][2=$2]\")",
        // "\"[1=ab][2=$2]cd\"", null, null);

        // these expressions work out equally in JSONata and Java
        assertEquals("[1=ab][2=]cd", "abcd".replaceAll("(ab|a)", "[1=$1][2=]"));
        test("$replace(\"abcd\", /(ab|a)/, \"[1=$1][2=]\")", "\"[1=ab][2=]cd\"", null, null);
        test("$replace(\"abcd\", /(ab|a)(x?)/, \"[1=$1][2=$2]\")", "\"[1=ab][2=]cd\"", null, null);
    }

    @Test
    public void testRegex_case22() throws Exception {
        // JSONata case regex/case022 expects an error with code D1004 here
        // test("$replace(\"abracadabra\", /.*?/, \"$1\")", (String) null, null, null);
        // Java regex simply expects a group in the regular expression if it is
        // referenced in the replace text
        test("$replace(\"abracadabra\", /(.*?)/, \"$1\")", "\"abracadabra\"", null, null);
    }

    @Test
    public void testRegex_case28() throws Exception {
        // JSONATA evaluates only the first digit after $ as group number.
        // $replace("abcdefghijklmno", /ijk/, "$8$5$12$12$18$123") => abcdefgh22823lmno
        // Java regex allows higher numbers than 9 for groups
        test("$replace(\"abcdefghijkl\", /(a)(b)(c)(d)(e)(f)(g)(h)(i)(j)(k)(l)/, \"$12$11$10$9$8$7$6$5$4$3$2$1\")", "\"lkjihgfedcba\"", null, null);
    }

    @Test
    public void testRegex_case31() throws Exception {
        // JSONATA evaluates $replace("abcdefghijklmno", /(ijk)/, "$x$") to "abcdefgh$x$lmno"
        // We could argue if this is a bug or a feature
        // but since replacement "$x$$" leads to the same result we might all agree that supporting $x$$ is sufficient.
        test("$replace(\"abcdefghijklmno\", /(ijk)/, \"$x$$\")", "\"abcdefgh$x$lmno\"", null, null);
    }

    // JSONAata expression
    // Account.Order.Product.$replace($."Product Name", /hat/i, function($match) { "foo" })
    // running against the "invoice" example of the JSONata Exerciser - works fine there
    // In JSONata4Java the arguments for the $replace() function get mixed up
    // argCount = 4
    // arg 1 (string): {"Product Name":"Bowler Hat"}
    // arg 2 (pattern): "Bowler Hat"
    // arg 3 (replacement): {"type":"CASEINSENSITIVE","pattern":"hat"}
    // Since the same problem occurs also if the 2nd $replace() arg is not a regular expression
    // I suppose that this problem also existed before the changes for regular expression support
    @Test(expected = EvaluateException.class)
    public void testRegex_case32() throws Exception {
        test("Account.Order.Product.$replace($.\"Product Name\", /hat/i, function($match) { \"foo\" })", "\"\"", null,
            "{\n"
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

    @Test(expected = EvaluateException.class)
    public void testRegex_case34() throws Exception {
        // This is a heavy expression.
        // To be honest I do not know the usage at all.
        // If it is meant to convert degrees of F into C it definitely does not work.
        // But basically the processing has the same problem that 33
        test("$replace(\"temperature = 68F today\", /(-?\\\\d+(?:\\\\.\\\\d*)?)F\\\\b/, function($m) { ($number($m.groups[0]) - 32) * 5/9 & \"C\" })",
            "\"temperature = 68F today\"", null, null);
    }

    @Test(expected = AssertionError.class)
    public void testRegex_case37() throws Exception {
        // This one seems to be a general problem with JSONata4Java's parser.
        // It has problems to parse the first match argument ?.
        // I suppose that this problem also existed before the changes for regular expression support.
        test("$map($, $match(?, /^(\\w*\\s\\w*)/)).match",
            "[\"Felicia Saunders\",\"Jimmy Schultz\"]", null,
            "[\"Felicia Saunders\",\"Jimmy Schultz\"]");
    }

    // Fixed

    @Test
    public void testRegex_case30() throws Exception {
        test("$replace(\"abcdefghijklmno\", /ijk/, \"$x\")", "\"abcdefgh$xlmno\"", null, null);
    }

    // JSNOata test suite group "regex" case012
    // $replace("265USD", /([0-9]+)USD/, "$$$1") evaluates to "$265" in JSONata
    // in JSONata4Java / Java regular expression you have to write "\$" instead of
    // $$
    // in order to escape the capturing group replacement start "$" and to replace a
    // real dollar
    @Test
    public void testRegex_case012() throws Exception {
        // assertEquals("$$", "\\$$".replaceAll("\\\\\\$([^<^0])", "\\$$1"));
        // "$$" -> "\$"
        // "$w" -> "\$w"
        // preserve $<digit> and $<
        assertEquals("abc\\$$1xyz", "abc$$$1xyz".replaceAll("\\$\\$", "\\\\\\$"));
        assertEquals("abc\\$wxyz", "abc$wxyz".replaceAll("\\$([^0-9^<])", "\\\\\\$$1"));
        assertEquals("abc\\$$1efg\\$wxyz", "abc$$$1efg$wxyz"
            .replaceAll("\\$\\$", "\\\\\\$")
            .replaceAll("([^\\\\]|^)\\$([^0-9^<])", "$1\\\\\\$$2"));

        assertEquals("abc\\$$1xyz", ReplaceFunction.jsonata2JavaReplacement("abc$$$1xyz"));
        assertEquals("abc\\$wxyz", ReplaceFunction.jsonata2JavaReplacement("abc$wxyz"));
        assertEquals("abc\\$$1efg\\$wxyz", ReplaceFunction.jsonata2JavaReplacement("abc$$$1efg$wxyz"));

        assertEquals("$265", "265USD".replaceAll("([0-9]+)USD", "\\$$1"));
        test("$replace(\"265USD\", /([0-9]+)USD/, \"$$$1\")", "\"$265\"", null, null);
    }

    @Test
    public void testToMillis() throws Exception {
        test("($toMillis('13:45', '[H]:[m]') ~> $fromMillis() ~> $substringBefore('T')) = $substringBefore($now(), 'T')", "true", null, null);
    }

    @Test
    public void containsWithBlank() throws Exception {
        test("$contains(\"abra cadabra\", /^abra\\scadabra$/)", "true", null, null);
        test("$contains(\"abra   cadabra\", /^abra\\s*cadabra$/)", "true", null, null);
        test("$contains(\"abra   cadabra\", /^abra\\040*cadabra$/)", "true", null, null);
        test("$contains(\"abra   cadabra\", /^abra\\x20*cadabra$/)", "true", null, null);
    }

    @Test
    public void replaceWithRegexWithBlank() throws Exception {
        test("$replace('foo 123   fuo  456 fio    789 foo',/\\s+/, '--')", "\"foo--123--fuo--456--fio--789--foo\"", null, null);
    }

    @Test
    public void containsCaseInsensitively() throws Exception {
        test("$contains('xyxyABCdefxyxxy', /ab.*ef/)", "false", null, null);
        test("$contains('xyxyABCdefxyxxy', /ab.*ef/i)", "true", null, null);
        test("$contains('ABCdef', /^ab.*ef$/)", "false", null, null);
        test("$contains('ABCdef', /^ab.*ef$/i)", "true", null, null);
    }

    @Test
    public void replaceWithMultilinedText() throws Exception {
        test("$replace('1234sjdffjf\\n5678jkfjf\\n9999fg grrs', /^([0-9]+)(.*)$/, '$1---$2')", "\"1234sjdffjf\\n5678jkfjf\\n9999fg grrs\"", null, null);
        test("$replace('1234sjdffjf\\n5678jkfjf\\n9999fg grrs', /^([0-9]+)(.*)$/m, '$1---$2')", "\"1234---sjdffjf\\n5678---jkfjf\\n9999---fg grrs\"", null, null);
        test("$replace('1234sjdffjf\\njkfjf\\n9999fg grrs', /^([0-9]+)(.*)$/m, '$1---$2')", "\"1234---sjdffjf\\njkfjf\\n9999---fg grrs\"", null, null);
    }

    // while $replace() works out fine (original JSONata like) with "multilined" strings
    // $contains has problems
    @Test
    public void containsWithMultilinedText() throws Exception {
        test("$contains('12xyzabc3def', /[0-9]+/)", "true", null, null);
        test("$contains('12xyz\\nabc\\n3def', /[0-9]+/)", "true", null, null);
        test("$contains('12xyz\\nabc\\n3def', /^[0-9]+.*$/)", "false", null, null);
        test("$contains('12xyz\\nabc\\n3def', /^[0-9]+.*$/m)", "true", null, null);
        test("$contains('12xyz\\nabc\\n3def', /[0-9]+/)", "true", null, null);
        test("$contains('1234sjdffjf\\n5678jkfjf\\n9999fg grrs', /([0-9]+)/)", "true", null, null);
        test("$contains('1234sjdffjf\\n5678jkfjf\\n9999fg grrs', /(^[0-9]+)/)", "true", null, null);
        test("$contains('1234sjdffjf\\n5678jkfjf\\n9999fg grrs', /^([0-9]+)(.*)$/)", "false", null, null);
        test("$contains('1234sjdffjf\\n5678jkfjf\\n9999fg grrs', /^([0-9]+)(.*)$/m)", "true", null, null);
    }

    @Test
    public void splitWithRegex() throws Exception {
        test("$split('this     is   a simple  test', /\\s+/)", "[ \"this\", \"is\", \"a\", \"simple\", \"test\" ]", null, null);
    }

    @Test
    public void match() throws Exception {
        test("$match('ababbabbbcc',/a(b+)/)",
            "[{\"match\":\"ab\",\"index\":0,\"groups\":[\"b\"]},"
                + "{\"match\":\"abb\",\"index\":2,\"groups\":[\"bb\"]},"
                + "{\"match\":\"abbb\",\"index\":5,\"groups\":[\"bbb\"]}]",
            null, null);
    }

    @Test
    public void matchCaseInsensitively() throws Exception {
        test("$match('abBbAabbaAaBcc', /(a+)(b+)/i))",
            "[{\"match\":\"abBb\",\"index\":0,\"groups\":[\"a\",\"bBb\"]},"
                + "{\"match\":\"Aabb\",\"index\":4,\"groups\":[\"Aa\",\"bb\"]},"
                + "{\"match\":\"aAaB\",\"index\":8,\"groups\":[\"aAa\",\"B\"]}]",
            null, null);
    }
}
