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
 * For simplicity, these tests provide the "input" inlined with the expression
 * itself (e.g. ["a", "b"][0]=="a"). Separate test cases verify that variable
 * access works as expected.
 * 
 * $match(str, pattern [, limit])
 * 
 * Finds occurrences of pattern within str and reports the match, location and
 * group.
 * 
 * If str is not specified, then the context value is used as the value of str.
 * It is an error if str is not a string.
 * 
 * The pattern parameter can either be a string or a regular expression (regex).
 * If it is a string, it specifies the substring(s) within str which should be
 * reported. If it is a regex, its is used to find.
 * 
 * The optional limit parameter, is a number that specifies the maximum number
 * of reports to make before stopping.
 * 
 * Examples
 * 
 * $match("John Smith and John Jones", "John") ==
 * "[{"match":"John","index":0,"groups":["John"]},
 * //{"match":"John","index":15,"groups":["John"]}]
 *
 */
@RunWith(Parameterized.class)
public class MatchFunctionTests implements Serializable {

    private static final long serialVersionUID = -3302876811482172935L;

    private static final String ERR_MSG_ARG1_BAD_TYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE,
        Constants.FUNCTION_MATCH);
    private static final String ERR_MSG_ARG2_BAD_TYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE,
        Constants.FUNCTION_MATCH);
    private static final String ERR_MSG_ARG3_BAD_TYPE = String.format(Constants.ERR_MSG_ARG3_BAD_TYPE,
        Constants.FUNCTION_MATCH);

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
                "$match()", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$match({})", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$match({}, ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$match(' ', {})", null, ERR_MSG_ARG2_BAD_TYPE
            }, //

            {
                "$match([])", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$match([], ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$match(' ', [])", null, ERR_MSG_ARG2_BAD_TYPE
            }, //

            {
                "$match({\"hello\": 1})", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$match({\"hello\": 1}, ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$match(' ', {\"hello\": 1})", null, ERR_MSG_ARG2_BAD_TYPE
            }, //

            {
                "$match(true)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$match(true, ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$match(' ', true)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //

            {
                "$match(null)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$match(null, ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$match(' ', null)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //

            {
                "$match(5)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$match(5, ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$match(' ', 5)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //

            {
                "$match(-5)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$match(-5, ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$match(' ', -5)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //

            {
                "$match(10/3.0)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$match(10/3.0, ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$match(' ', 10/3.0)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //

            {
                "$match(a.b.c)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$match(a.b.c, ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$match(' ', a.b.c)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //

            {
                "$match('foo bar', 'a')", "{\"match\":\"a\",\"index\":5,\"groups\":[]}", null
            }, //
            {
                "$match('foo bar', /a/)", "{\"match\":\"a\",\"index\":5,\"groups\":[]}", null
            }, //
            {
                "$match('foo bar', /(a)/)", "{\"match\":\"a\",\"index\":5,\"groups\":[\"a\"]}", null
            }, //
            {
                "$match('foo bar', 'o', 0)", null, null
            }, //
            {
                "$match('foo bar', 'o', 1)", "{\"match\":\"o\",\"index\":1,\"groups\":[]}", null
            }, //
            {
                "$match('foo bar', /o/, 1)", "{\"match\":\"o\",\"index\":1,\"groups\":[]}", null
            }, //
            {
                "$match('foo bar', /(o)/, 1)", "{\"match\":\"o\",\"index\":1,\"groups\":[\"o\"]}", null
            }, //
            {
                "$match('foo bar', 'o', {})", null, ERR_MSG_ARG3_BAD_TYPE
            }, //
            {
                "$match('foo bar', 'o', [])", null, ERR_MSG_ARG3_BAD_TYPE
            }, //
            {
                "$match('foo bar', 'o', true)", null, ERR_MSG_ARG3_BAD_TYPE
            }, //
            {
                "$match('foo bar', 'o', null)", null, ERR_MSG_ARG3_BAD_TYPE
            }, //
            {
                "$match('foo bar', 'o', -1)", null, ERR_MSG_ARG3_BAD_TYPE
            }, //
            {
                "$match('ababbabbbcc',/a(b+)/)",
                "[{\"match\":\"ab\",\"index\":0,\"groups\":[\"b\"]},"
                    + "{\"match\":\"abb\",\"index\":2,\"groups\":[\"bb\"]},"
                    + "{\"match\":\"abbb\",\"index\":5,\"groups\":[\"bbb\"]}]",
                null
            },
            {
                "$match('ababbabbcc','a(b+)')", "[]", null
            },
            {
                "$match('aba(b+)babbcc','a(b+)')", "{\"match\":\"a(b+)\",\"index\":2,\"groups\":[]}", null
            },
            {
                "$match('abbbaabbaaabcc', /(a+)(b+)/)",
                "[{\"match\":\"abbb\",\"index\":0,\"groups\":[\"a\",\"bbb\"]},"
                    + "{\"match\":\"aabb\",\"index\":4,\"groups\":[\"aa\",\"bb\"]},"
                    + "{\"match\":\"aaab\",\"index\":8,\"groups\":[\"aaa\",\"b\"]}]",
                null
            },
            {
                "$match('abBbAabbaAaBcc', /(a+)(b+)/)",
                "[{\"match\":\"ab\",\"index\":0,\"groups\":[\"a\",\"b\"]},"
                    + "{\"match\":\"abb\",\"index\":5,\"groups\":[\"a\",\"bb\"]}]",
                null
            },
            {
                "$match('abBbAabbaAaBcc', /(a+)(b+)/i)",
                "[{\"match\":\"abBb\",\"index\":0,\"groups\":[\"a\",\"bBb\"]},"
                    + "{\"match\":\"Aabb\",\"index\":4,\"groups\":[\"Aa\",\"bb\"]},"
                    + "{\"match\":\"aAaB\",\"index\":8,\"groups\":[\"aAa\",\"B\"]}]",
                null
            },
            // $match() seems to work out equally with and without /m
            // This seems to hold for original JSONata as well as for Java regular expressions
            {
                "$match('ex12am345\\n6ple89', /[0-9]+/)",
                "[{\"match\":\"12\",\"index\":2,\"groups\":[]},"
                    + "{\"match\":\"345\",\"index\":6,\"groups\":[]},"
                    + "{\"match\":\"6\",\"index\":10,\"groups\":[]},"
                    + "{\"match\":\"89\",\"index\":14,\"groups\":[]}]",
                null
            },
            {
                "$match('ex12am345\\n6ple89', /[0-9]+/m)",
                "[{\"match\":\"12\",\"index\":2,\"groups\":[]},"
                    + "{\"match\":\"345\",\"index\":6,\"groups\":[]},"
                    + "{\"match\":\"6\",\"index\":10,\"groups\":[]},"
                    + "{\"match\":\"89\",\"index\":14,\"groups\":[]}]",
                null
            },
        });
    }

    @Test
    public void runTest() throws Exception {
        test(this.expression, expectedResultJsonString, expectedRuntimeExceptionMessage, null);
    }
}
