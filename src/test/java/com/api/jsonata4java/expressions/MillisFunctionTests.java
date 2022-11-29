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
 * $millis()
 * 
 * Returns the number of milliseconds since the Unix Epoch (1 January, 1970 UTC)
 * as a number. All invocations of $millis() within an evaluation of an
 * expression will all return the same value
 * 
 * Examples
 * 
 * $millis()==1502700297574
 *
 */
@RunWith(Parameterized.class)
public class MillisFunctionTests implements Serializable {

    private static final long serialVersionUID = 1713974085461064412L;

    private static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_MILLIS);

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
                "{\"now\":$millis()}.(now > 1549400775880)", "true", null
            }, //
            {
                "$millis({})", null, ERR_ARG1BADTYPE
            }, //
            {
                "$millis([])", null, ERR_ARG1BADTYPE
            }, //
            {
                "$millis(1)", null, ERR_ARG1BADTYPE
            }, //
            {
                "$millis(-1)", null, ERR_ARG1BADTYPE
            }, //
            {
                "$millis(10/3.0)", null, ERR_ARG1BADTYPE
            }, //
            {
                "$millis('1')", null, ERR_ARG1BADTYPE
            }, //
            {
                "$millis(a.b.c)", null, ERR_ARG1BADTYPE
            }, //
            {
                "$millis(null)", null, ERR_ARG1BADTYPE
            } //
                        // TODO: Need to work out how to test the function since we cannot predict the
                        // long that is returned
                        // {"$millis()", Long.toString(Instant.now().toEpochMilli()), null}
                        // below may prove false on slower machines...
                        // TODO timing specific				{ "{\"now\": $millis(), \"delay\": $sum([1..4]), \"later\": $millis()}.(later = now)", "true", null }, //
                        // TODO timing specific				{ "{\"now\": $millis(), \"delay\": $sum([1..10000]), \"later\": $millis()}.(later - now < 10)", "true", null }
        });
    }

    @Test
    public void runTest() throws Exception {
        test(this.expression, expectedResultJsonString, expectedRuntimeExceptionMessage, null);
    }
}
