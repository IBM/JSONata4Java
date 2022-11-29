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
 * $split(str, separator [, limit])
 * 
 * Splits the str parameter into an array of substrings. If str is not
 * specified, then the context value is used as the value of str. It is an error
 * if str is not a string.
 * 
 * The separator parameter can either be a string or a regular expression
 * (regex). If it is a string, it specifies the characters within str about
 * which it should be split. If it is the empty string, str will be split into
 * an array of single characters. If it is a regex, it splits the string around
 * any sequence of characters that match the regex.
 * 
 * The optional limit parameter is a number that specifies the maximum number of
 * substrings to include in the resultant array. Any additional substrings are
 * discarded. If limit is not specified, then str is fully split with no limit
 * to the size of the resultant array. It is an error if limit is not a
 * non-negative number.
 * 
 * Examples
 * 
 * $split("so many words", " ")==[ "so", "many", "words" ] $split("so many
 * words", " ", 2)==[ "so", "many" ] $split("too much, punctuation. hard; to
 * read", /[ ,.;]+/)==["too", "much", "punctuation", "hard", "to", "read"]
 *
 */
@RunWith(Parameterized.class)
public class SplitFunctionTests implements Serializable {

    private static final long serialVersionUID = -6212117204138504697L;

    private static final String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT,
        Constants.FUNCTION_SPLIT);
    private static final String ERR_MSG_ARG1_BAD_TYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE,
        Constants.FUNCTION_SPLIT);
    private static final String ERR_MSG_ARG2_BAD_TYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE,
        Constants.FUNCTION_SPLIT);
    private static final String ERR_MSG_ARG3_BAD_TYPE = String.format(Constants.ERR_MSG_ARG3_BAD_TYPE,
        Constants.FUNCTION_SPLIT);

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
                "$split()", null, ERR_BAD_CONTEXT
            }, //
            {
                "$split({})", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$split({}, ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$split(' ', {})", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$split(' ', ' ', {})", null, ERR_MSG_ARG3_BAD_TYPE
            }, //
            {
                "$split([])", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$split([], ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$split(' ', [])", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$split(' ', ' ', [])", null, ERR_MSG_ARG3_BAD_TYPE
            }, //
            {
                "$split({\"hello\": 1})", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$split({\"hello\": 1}, ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$split(' ', {\"hello\": 1})", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$split(' ', ' ', {\"hello\": 1})", null, ERR_MSG_ARG3_BAD_TYPE
            }, //
            {
                "$split([\"hello\", 1])", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$split([\"hello\", 1], ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$split(' ', [\"hello\", 1])", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$split(' ', ' ', [\"hello\", 1])", null, ERR_MSG_ARG3_BAD_TYPE
            }, //
            {
                "$split(true)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$split(true, ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$split(' ', true)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$split(' ', ' ', true)", null, ERR_MSG_ARG3_BAD_TYPE
            }, //
            {
                "$split(null)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$split(null, ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$split(' ', null)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$split(' ', ' ', null)", null, ERR_MSG_ARG3_BAD_TYPE
            }, //
            {
                "$split(5)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$split(5, ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$split(-5)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$split(-5, ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$split(' ', ' ', -5)", null, ERR_MSG_ARG3_BAD_TYPE
            }, //
            {
                "$split(10/3.0)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$split(10/3.0, ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$split(' ', ' ', 10/3.0)", "[]", null
            }, //
            {
                "$split(a.b.c)", null, ERR_BAD_CONTEXT
            }, //
            {
                "$split(a.b.c, ' ')", null, null
            }, //
            {
                "$split(' ')", null, ERR_BAD_CONTEXT
            }, //
            {
                "$split(' ', ' ')", "[]", null
            }, //
            {
                "$split('so many words', ' ')", "[ \"so\", \"many\", \"words\" ]", null
            }, //
            {
                "$split('so many words', ' ', 2)", "[ \"so\", \"many\" ]", null
            }, //
            {
                "$split('foo', '')", "[ \"f\", \"o\", \"o\" ]", null
            }, //
            {
                "$split('', '')", "[]", null
            }, //
            {
                "$split('this     is   a simple  test', /\\s+/)", "[ \"this\", \"is\", \"a\", \"simple\", \"test\" ]", null
            },
            {
                "$split('this@_@is@_@a@_@simple@_@test', '@_@')", "[ \"this\", \"is\", \"a\", \"simple\", \"test\" ]", null
            },
            {
                "$split('this     is   a +simple  test', ' +')", "[\"this     is   a\",\"simple  test\"]", null
            },
            {
                "$split('thisOOOOOisOOOaOsimpleOOtest', /O+/)", "[ \"this\", \"is\", \"a\", \"simple\", \"test\" ]", null
            },
            {
                "$split('thisOoOooisOoOaOsimpleOotest', /O+/i)", "[ \"this\", \"is\", \"a\", \"simple\", \"test\" ]", null
            },
            // $split() seems to work out equally with and without /m
            // This seems to hold for original JSONata as well as for Java regular expressions
            {
                "$split('this   is a  simple   test\\nwith a    second     line.', /[\\s]+/)",
                "[ \"this\", \"is\", \"a\", \"simple\", \"test\", \"with\", \"a\", \"second\", \"line.\" ]", null
            },
            {
                "$split('this   is a  simple   test\\nwith a    second     line.', /[\\s]+/m)",
                "[ \"this\", \"is\", \"a\", \"simple\", \"test\", \"with\", \"a\", \"second\", \"line.\" ]", null
            },
        });
    }

    @Test
    public void runTest() throws Exception {
        test(this.expression, expectedResultJsonString, expectedRuntimeExceptionMessage, null);
    }
}
