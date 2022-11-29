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
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * For simplicity, these tests don't rely on $state/$event/$instance access; instead
 * providing the "input" inlined with the expression itself (e.g. ["a", "b"][0]=="a").
 * Separate test cases verify that variable access works as expected.
 * 
 * From http://docs.jsonata.org/string-functions.html:
 * 
 * $now()
 * 
 * Generates a UTC timestamp in ISO 8601 compatible format and returns it as a string. All invocations of $now()
 * within an evaluation of an expression will all return the same timestamp value
 * 
 * Examples
 * 
 * $now()=="2017-05-15T15:12:59.152Z"
 *
 */
@RunWith(Parameterized.class)
public class NowFunctionTests implements Serializable {

    private static final long serialVersionUID = -8084233629920007085L;

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
                "$now(\"[Y0000]\")", new SimpleDateFormat("\"yyyy\"").format(Calendar.getInstance().getTime()), null
            }
                        // {"$now()",    null, null}
        });
    }

    @Test
    public void runTest() throws Exception {
        test(this.expression, expectedResultJsonString, expectedRuntimeExceptionMessage, null);
    }
}
