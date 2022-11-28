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
 * $contains(str, pattern)
 * 
 * Returns true if str is matched by pattern, otherwise it returns false. If str
 * is not specified (i.e. this function is invoked with one argument), then the
 * context value is used as the value of str.
 * 
 * The pattern parameter can either be a string or a regular expression (regex).
 * If it is a string, the function returns true if the characters within pattern
 * are contained contiguously within str. If it is a regex, the function will
 * return true if the regex matches the contents of str.
 * 
 * Examples
 * 
 * $contains("abracadabra", "bra")==true $contains("abracadabra", /a.*a/) ==
 * true $contains("abracadabra", /ar.*a/)==false $contains("Hello World",
 * /wo/)==false $contains("Hello World", /wo/i)==true
 * Phone[$contains(number, /^077/)]=={ "type": "mobile", "number": "077 7700
 * 1234" }
 * 
 */
@RunWith(Parameterized.class)
public class ContainsFunctionTests implements Serializable {

    private static final long serialVersionUID = -8188414859745251903L;

    private static final String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT,
        Constants.FUNCTION_CONTAINS);
    private static final String ERR_MSG_ARG1_BAD_TYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE,
        Constants.FUNCTION_CONTAINS);
    private static final String ERR_MSG_ARG2_BAD_TYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE,
        Constants.FUNCTION_CONTAINS);

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
                "$contains()", null, ERR_BAD_CONTEXT
            }, //
            {
                "$contains(a.b.c)", null, ERR_BAD_CONTEXT
            }, //
            {
                "$contains(a.b.c,\"a\")", null, null
            }, //
            {
                "$contains({})", null, ERR_BAD_CONTEXT
            }, //
            {
                "$contains({}, ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$contains(' ', {})", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$contains([])", null, ERR_BAD_CONTEXT
            }, //
            {
                "$contains([], ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$contains(' ', [])", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$contains({\"hello\": 1})", null, ERR_BAD_CONTEXT
            }, //
            {
                "$contains({\"hello\": 1}, ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$contains(' ', {\"hello\": 1})", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$contains([\"hello\", 1])", null, ERR_BAD_CONTEXT
            }, //
            {
                "$contains([\"hello\", 1], ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$contains(' ', [\"hello\", 1])", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$contains(true)", null, ERR_BAD_CONTEXT
            }, //
            {
                "$contains(true, ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$contains(' ', true)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$contains(null)", null, ERR_BAD_CONTEXT
            }, //
            {
                "$contains(null, ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$contains(' ', null)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$contains(1)", null, ERR_BAD_CONTEXT
            }, //
            {
                "$contains(1, ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$contains(' ', 1)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$contains(-1)", null, ERR_BAD_CONTEXT
            }, //
            {
                "$contains(-1, ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$contains(' ', -1)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$contains(10/3.0)", null, ERR_BAD_CONTEXT
            }, //
            {
                "$contains(10/3.0, ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$contains(' ',  10/3.0)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$contains(null)", null, ERR_BAD_CONTEXT
            }, //
            {
                "$contains(null, ' ')", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$contains(' ', null)", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$contains(a.b.c)", null, ERR_BAD_CONTEXT
            }, //
            {
                "$contains(' ', a.b.c)", "\"\"", ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$contains(a.b.c, ' ')", null, null
            }, //
            {
                "$contains('',  ' ')", "false", null
            }, //
            {
                "$contains('abracadabra',  ' ')", "false", null
            }, //
            {
                "$contains('abracadabra', 'bra')", "true", null
            },
            {
                "$contains(\"abracadabra\", \"bra\")", "true", null
            },
            {
                "$contains(\"abracadabra\", /.*bra.*/)", "true", null
            },
            {
                "$contains(\"abracadabra\", /bra/)", "true", null
            },
            {
                "$contains(\"abracadabra\", /^abracadabra$/)", "true", null
            },
            {
                "$contains(\"abra/cadabra\", /^abra\\/cadabra$/)", "true", null
            },
            {
                "$contains(\"abracadabra\", /[abcdr]*/)", "true", null
            },
            {
                "$contains(\"abra cadabra\", /^abra\\scadabra$/)", "true", null,
            },
            {
                "$contains(\"abra   cadabra\", /^abra\\s*cadabra$/)", "true", null,
            },
            {
                "$contains(\"abra   cadabra\", /^abra\\040*cadabra$/)", "true", null,
            },
            {
                "$contains(\"abra   cadabra\", /^abra\\x20*cadabra$/)", "true", null,
            },
            {
                "($compute := function($val1, $val2) { $val1 + $val2}; $contains($string($compute(120000 / 3 / 2, 10000)), /30+/))", "true", null
            },
            {
                "$contains('Hello World', /wo/i)", "true", null
            },
            {
                "$contains('Hello World Games\\nHello Europe Games', /World/)", "true", null
            },
            {
                "$contains('12xyzabc3def', /[0-9]+/)", "true", null
            },
            {
                "$contains('12xyz\\nabc\\n3def', /[0-9]+/)", "true", null
            },
            {
                "$contains('12xyz\\nabc\\n3def', /^[0-9]+.*$/)", "false", null
            },
            {
                "$contains('12xyz\\nabc\\n3def', /^[0-9]+.*$/m)", "true", null
            },
            {
                "$contains('12xyz\\nabc\\n3def', /[0-9]+/)", "true", null
            },
            {
                "$contains('1234sjdffjf\\n5678jkfjf\\n9999fg grrs', /([0-9]+)/)", "true", null
            },
            {
                "$contains('1234sjdffjf\\n5678jkfjf\\n9999fg grrs', /(^[0-9]+)/)", "true", null
            },
            {
                "$contains('1234sjdffjf\\n5678jkfjf\\n9999fg grrs', /^([0-9]+)(.*)$/)", "false", null
            },
            {
                "$contains('1234sjdffjf\\n5678jkfjf\\n9999fg grrs', /^([0-9]+)(.*)$/m)", "true", null
            },
        });
    }

    @Test
    public void runTest() throws Exception {
        test(this.expression, expectedResultJsonString, expectedRuntimeExceptionMessage, null);
    }
}
