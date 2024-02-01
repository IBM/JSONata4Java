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
import com.api.jsonata4java.expressions.utils.Constants;

/**
 * For simplicity, these tests don't rely on $state/$event/$instance access;
 * instead providing the "input" inlined with the expression itself (e.g. ["a",
 * "b"][0]=="a"). Separate test cases verify that variable access works as
 * expected.
 * 
 * $replace(str, pattern, replacement [, limit])
 * 
 * Finds occurrences of pattern within str and replaces them with replacement.
 * 
 * If str is not specified, then the context value is used as the value of str.
 * It is an error if str is not a string.
 * 
 * The pattern parameter can either be a string or a regular expression (regex).
 * If it is a string, it specifies the substring(s) within str which should be
 * replaced. If it is a regex, its is used to find.
 * 
 * The replacement parameter can either be a string or a function. If it is a
 * string, it specifies the sequence of characters that replace the substring(s)
 * that are matched by pattern. If pattern is a regex, then the replacement
 * string can refer to the characters that were matched by the regex as well as
 * any of the captured groups using a S followed by a number N:
 * 
 * * If N = 0, then it is replaced by substring matched by the regex as a whole.
 * * If N &GT; 0, then it is replaced by the substring captured by the Nth
 * parenthesised group in the regex. * If N is greater than the number of
 * captured groups, then it is replaced by the empty string. * A literal $
 * character must be written as $$ in the replacement string
 * 
 * If the replacement parameter is a function, then it is invoked for each match
 * occurrence of the pattern regex. The replacement function must take a single
 * parameter which will be the object structure of a regex match as described in
 * the $match function; and must return a string.
 * 
 * The optional limit parameter, is a number that specifies the maximum number
 * of replacements to make before stopping. The remainder of the input beyond
 * this limit will be copied to the output unchanged.
 * 
 * Examples
 * 
 * $replace("John Smith and John Jones", "John", "Mr")=="Mr Smith and Mr
 * Jones" $replace("John Smith and John Jones", "John", "Mr", 1)=="Mr Smith
 * and John Jones" $replace("abracadabra", /a.*?a/, "*")=="*c*bra"
 * $replace("John Smith", /(\w+)\s(\w+)/, "$2, $1")=="Smith, John"
 * $replace("265USD", /([0-9]+)USD/, "$$$1")=="$265"
 *
 */
@RunWith(Parameterized.class)
public class ReplaceFunctionTests implements Serializable {

    private static final long serialVersionUID = -382714591856328657L;

    private static final String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT,
        Constants.FUNCTION_REPLACE);
    private static final String ERR_MSG_ARG1_BAD_TYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE,
        Constants.FUNCTION_REPLACE);
    private static final String ERR_MSG_ARG2_BAD_TYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE,
        Constants.FUNCTION_REPLACE);
    private static final String ERR_MSG_ARG3_BAD_TYPE = String.format(Constants.ERR_MSG_ARG3_BAD_TYPE,
        Constants.FUNCTION_REPLACE);
    private static final String ERR_MSG_ARG4_BAD_TYPE = String.format(Constants.ERR_MSG_ARG4_BAD_TYPE,
        Constants.FUNCTION_REPLACE);
    private static final String ERR_MSG_ARG2_EMPTY_STR = String.format(Constants.ERR_MSG_ARG2_EMPTY_STR,
        Constants.FUNCTION_REPLACE);

    @Parameter(0)
    public String expression;

    @Parameter(1)
    public String expectedResultJsonString;

    @Parameter(2)
    public String expectedRuntimeExceptionMessage;

    @Parameters(name = "{index}: {0} -> {1} ({2})")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            {
                "$replace()", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$replace({})", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$replace({}, ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$replace({}, ' ', ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$replace(' ', {})", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$replace(' ', {}, ' ')", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$replace(' ', ' ', {})", null, ERR_MSG_ARG3_BAD_TYPE
            }, //

            {
                "$replace([])", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$replace([], ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$replace([], ' ', ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$replace(' ', [])", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$replace(' ', [], ' ')", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$replace(' ', ' ', [])", null, ERR_MSG_ARG3_BAD_TYPE
            }, //

            {
                "$replace({\"hello\": 1})", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$replace({\"hello\": 1}, ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$replace({\"hello\": 1}, ' ', ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$replace(' ', {\"hello\": 1})", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$replace(' ', {\"hello\": 1}, ' ')", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$replace(' ', ' ', {\"hello\": 1})", null, ERR_MSG_ARG3_BAD_TYPE
            }, //

            {
                "$replace([\"hello\", 1])", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$replace([\"hello\", 1], ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$replace([\"hello\", 1], ' ', ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$replace(' ', [\"hello\", 1])", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$replace(' ', [\"hello\", 1], ' ')", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$replace(' ', ' ', [\"hello\", 1])", null, ERR_MSG_ARG3_BAD_TYPE
            }, //

            {
                "$replace(true)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$replace(true, ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$replace(true, ' ', ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$replace(' ', true)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$replace(' ', true, ' ')", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$replace(' ', ' ', true)", null, ERR_MSG_ARG3_BAD_TYPE
            }, //

            {
                "$replace(null)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$replace(null, ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$replace(null, ' ', ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$replace(' ', null)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$replace(' ', null, ' ')", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$replace(' ', ' ', null)", null, ERR_MSG_ARG3_BAD_TYPE
            }, //

            {
                "$replace(5)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$replace(5, ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$replace(5, ' ', ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$replace(' ', 5)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$replace(' ', 5, ' ')", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$replace(' ', ' ', 5)", null, ERR_MSG_ARG3_BAD_TYPE
            }, //

            {
                "$replace(-5)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$replace(-5, ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$replace(-5, ' ', ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$replace(' ', -5)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$replace(' ', -5, ' ')", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$replace(' ', ' ', -5)", null, ERR_MSG_ARG3_BAD_TYPE
            }, //

            {
                "$replace(10/3.0)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$replace(10/3.0, ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$replace(10/3.0, ' ', ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$replace(' ', 10/3.0)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$replace(' ', 10/3.0, ' ')", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$replace(' ', ' ', 10/3.0)", null, ERR_MSG_ARG3_BAD_TYPE
            }, //

            // jsonata 1.8.2 complains about argument 2 because it assumes use of context
            {
                "$replace(a.b.c)", null, ERR_MSG_ARG2_BAD_TYPE
            }, // jsonata 1.8.2 ERR_MSG_ARG1_BAD_TYPE }, //
            {
                "$replace(a.b.c, ' ')", null, ERR_BAD_CONTEXT
            }, // jsonata 1.8.2 MSG_ARG1_BAD_TYPE }, //
            {
                "$replace(a.b.c, ' ', ' ')", null, null
            }, // jsonata 1.8.2 ERR_MSG_ARG1_BAD_TYPE }, //
            {
                "$replace(' ', a.b.c)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$replace(' ', a.b.c, ' ')", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$replace(' ', ' ', a.b.c)", null, ERR_MSG_ARG3_BAD_TYPE
            }, //

            {
                "$replace('', '', '')", null, ERR_MSG_ARG2_EMPTY_STR
            }, //
            {
                "$replace(' ', ' ', ' ')", "\" \"", null
            }, //
            {
                "$replace('foo bar', 'o', 'a')", "\"faa bar\"", null
            }, //
            {
                "$replace('foo bar', 'o', 'aa')", "\"faaaa bar\"", null
            }, //
            {
                "$replace('foo bar', 'o', 'a', 0)", "\"foo bar\"", null
            }, //
            {
                "$replace('foo bar', 'o', 'a', 1)", "\"fao bar\"", null
            }, //
            {
                "$replace('foo bar', 'o', 'a', 2)", "\"faa bar\"", null
            }, //
            {
                "$replace('foo bar', 'o', 'a', {})", null, ERR_MSG_ARG4_BAD_TYPE
            }, //
            {
                "$replace('foo bar', 'o', 'a', [])", null, ERR_MSG_ARG4_BAD_TYPE
            }, //
            {
                "$replace('foo bar', 'o', 'a', true)", null, ERR_MSG_ARG4_BAD_TYPE
            }, //
            {
                "$replace('foo bar', 'o', 'a', null)", null, ERR_MSG_ARG4_BAD_TYPE
            }, //
            {
                "$replace('foo bar', 'o', 'a', -1)", null, ERR_MSG_ARG4_BAD_TYPE
            }, //
            {
                "$replace('foox123xfuox456xfiox789xfoo', /x?f[a-z]ox?/, '---')", "\"---123---456---789---\"", null
            }, //
            {
                "$replace('foox123xfuox456xfiox789xfoo', /x?f[a-z]ox?/, '---')", "\"---123---456---789---\"", null
            }, //
            {
                "$replace('foo_123_fuo_456_fio_789_foo', /_?f[a-z]o_?/, '---')", "\"---123---456---789---\"", null
            }, //
            {
                "$replace('foo_123_fuo_?f[a-z]o_?456_fio_789_foo', /_?f[a-z]o_?/, '---')", "\"---123---?f[a-z]o_?456---789---\"", null
            }, //
            {
                "$replace('foo_123_fuo_?f[a-z]o_?456_fio_789_foo', '_?f[a-z]o_?', '---')", "\"foo_123_fuo---456_fio_789_foo\"", null
            }, //
            {
                "$replace('a-b---+c--d', /-+/, '_', 2)", "\"a_b_+c--d\"", null
            }, //
            {
                "$replace('a-b---+c--d', '-+', '_', 2)", "\"a-b--_c--d\"", null
            }, //
            {
                "$replace('fooa123aAafuoAa456aaAfioAaAa789afoo', /a+/i, '--')", "\"foo--123--fuo--456--fio--789--foo\"", null
            }, //
            {
                "$replace('fooa123aAafuoAa456aaAfioAaAa789afoo', /a+/i, '--', 4)", "\"foo--123--fuo--456--fioAaAa789afoo\"", null
            }, //
            {
                "$replace('1234sjdffjf\\n5678jkfjf\\n9999fg grrs', /^([0-9]+)(.*)$/, '$1---$2')", "\"1234sjdffjf\\n5678jkfjf\\n9999fg grrs\"", null
            }, //
            {
                "$replace('1234sjdffjf\\n5678jkfjf\\n9999fg grrs', /^([0-9]+)(.*)$/m, '$1---$2')", "\"1234---sjdffjf\\n5678---jkfjf\\n9999---fg grrs\"", null
            }, //
            {
                "$replace('1234sjdffjf\\njkfjf\\n9999fg grrs', /^([0-9]+)(.*)$/m, '$1---$2')", "\"1234---sjdffjf\\njkfjf\\n9999---fg grrs\"", null
            }, //
            // JSNOata test suite group "regex" case012
            {
                "$replace(\"265USD\", /([0-9]+)USD/, \"$$$1\")", "\"$265\"", null
            }, //
            // JSNOata test suite group "regex" case013
            {
                "$replace(\"265USD\", /([0-9]+)USD/, \"$w\")", "\"$w\"", null
            }, //
        });
    }

    @Test
    public void runTest() throws Exception {
        test(this.expression, expectedResultJsonString, expectedRuntimeExceptionMessage, null);
    }
}
