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
 * http://docs.jsonata.org/numeric-functions.html
 * 
 * $random()
 * 
 * Returns a pseudo random number greater than or equal to zero and less than one (0 ≤ n &LT; 1)
 * 
 * Examples
 * 
 * $random()==0.7973541067127 
 * $random()==0.4029142127028 
 * $random()==0.6558078550072
 *
 */
@RunWith(Parameterized.class)
public class RandomFunctionTests implements Serializable {

    private static final long serialVersionUID = 5173172444319908151L;

    private static String ERR_ARG1BADTYPE = String
        .format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_RANDOM);

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
                "$random({})", null, ERR_ARG1BADTYPE
            }, //
            {
                "$random([])", null, ERR_ARG1BADTYPE
            }, //
            {
                "$random(1)", null, ERR_ARG1BADTYPE
            }, //
            {
                "$random(-1)", null, ERR_ARG1BADTYPE
            }, //
            {
                "$random(10/3.0)", null, ERR_ARG1BADTYPE
            }, //
            {
                "$random('1')", null, ERR_ARG1BADTYPE
            }, //
            {
                "$random(a.b.c)", null, ERR_ARG1BADTYPE
            }, //
            {
                "$random(null)", null, ERR_ARG1BADTYPE
            }, //
            {
                "$random() >= 0", "true", null
            }, //
            {
                "$random() < 1", "true", null
            } //
        });
    }

    @Test
    public void runTest() throws Exception {
        test(this.expression, expectedResultJsonString, expectedRuntimeExceptionMessage, null);
    }
}
