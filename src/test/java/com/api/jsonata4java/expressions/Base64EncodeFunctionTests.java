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
 * $base64encode(str)
 * 
 * Converts an ASCII string to a base 64 representation. Each each character in
 * the string is treated as a byte of binary data. This requires that all
 * characters in the string are in the 0x00 to 0xFF range, which includes all
 * characters in URI encoded strings. Unicode characters outside of that range
 * are not supported.
 * 
 * Examples
 * 
 * $base64encode("myuser:mypass")=="bXl1c2VyOm15cGFzcw=="
 *
 */
@RunWith(Parameterized.class)
public class Base64EncodeFunctionTests implements Serializable {

    private static final long serialVersionUID = 6922163901207591187L;

    private static final String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT,
        Constants.FUNCTION_BASE64_ENCODE);
    private static final String ERR_MSG_ARG1_BAD_TYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE,
        Constants.FUNCTION_BASE64_ENCODE);
    private static final String ERR_MSG_ARG2_BAD_TYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE,
        Constants.FUNCTION_BASE64_ENCODE);

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
                "$base64encode()", null, ERR_BAD_CONTEXT
            }, //
            {
                "$base64encode({})", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$base64encode([])", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$base64encode({\"hello\": 1})", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$base64encode([\"hello\", 1])", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$base64encode(1)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$base64encode(-22.2)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$base64encode(10/3.0)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$base64encode(null)", null, ERR_MSG_ARG1_BAD_TYPE
            }, //
            {
                "$base64encode('', '')", null, ERR_MSG_ARG2_BAD_TYPE
            }, //
            {
                "$base64encode(a.b.c)", null, null
            }, //
            {
                "$base64encode('Hello World')", "\"SGVsbG8gV29ybGQ=\"", null
            }, //
            {
                "$base64encode('myuser:mypass')", "\"bXl1c2VyOm15cGFzcw==\"", null
            } //
        });
    }

    @Test
    public void runTest() throws Exception {
        test(this.expression, expectedResultJsonString, expectedRuntimeExceptionMessage, null);
    }
}
