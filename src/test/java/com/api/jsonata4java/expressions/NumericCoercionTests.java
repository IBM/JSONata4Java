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

/**
 * For simplicity, these tests don't rely on $state/$event/$instance access;
 * instead providing the "input" inlined with the expression itself (e.g. ["a",
 * "b"][0]=="a"). Separate test cases verify that variable access works as
 * expected.
 */
@RunWith(Parameterized.class)
public class NumericCoercionTests implements Serializable {

    private static final long serialVersionUID = -9152353250593798501L;

    @Parameter(0)
    public String expression;

    @Parameter(1)
    public String expectedResultJsonString;

    @Parameter(2)
    public String expectedRuntimeExceptionMessage;

    @Parameters(name = "{index}: {0} -> {1} ({2})")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            // -----------------------
            // - numeric coercion
            // ------------------------

            // divisions with integral results produce a LongNode (regardless of numeric
            // type of inputs)
            {
                "30/10", "3", null
            }, //
            {
                "30.0/10", "3", null
            }, //
            {
                "30/10.0", "3", null
            }, //
            {
                "30.0/10.0", "3", null
            }, //

            {
                "-30/10", "-3", null
            }, //
            {
                "-30.0/10", "-3", null
            }, //
            {
                "-30/10.0", "-3", null
            }, //
            {
                "-30.0/10.0", "-3", null
            }, //

            {
                "30/-10", "-3", null
            }, //
            {
                "30.0/-10", "-3", null
            }, //
            {
                "30/-10.0", "-3", null
            }, //
            {
                "30.0/-10.0", "-3", null
            }, //

            {
                "-30/-10", "3", null
            }, //
            {
                "-30.0/-10", "3", null
            }, //
            {
                "-30/-10.0", "3", null
            }, //
            {
                "-30.0/-10.0", "3", null
            }, //

            // divisions with non-integral results produce a DoubleNode (regardless of
            // numeric type of inputs)
            {
                "3/10", "0.3", null
            }, //
            {
                "3.0/10", "0.3", null
            }, //
            {
                "3/10.0", "0.3", null
            }, //
            {
                "3.0/10.0", "0.3", null
            }, //

            {
                "-3/10", "-0.3", null
            }, //
            {
                "-3.0/10", "-0.3", null
            }, //
            {
                "-3/10.0", "-0.3", null
            }, //
            {
                "-3.0/10.0", "-0.3", null
            }, //

            {
                "3/-10", "-0.3", null
            }, //
            {
                "3.0/-10", "-0.3", null
            }, //
            {
                "3/-10.0", "-0.3", null
            }, //
            {
                "3.0/-10.0", "-0.3", null
            }, //

            {
                "-3/-10", "0.3", null
            }, //
            {
                "-3.0/-10", "0.3", null
            }, //
            {
                "-3/-10.0", "0.3", null
            }, //
            {
                "-3.0/-10.0", "0.3", null
            }, //

            // multiplications with integral results produce a LongNode (regardless of
            // numeric type of inputs)
            {
                "3*10", "30", null
            }, //
            {
                "3.0*10", "30", null
            }, //
            {
                "3*10.0", "30", null
            }, //
            {
                "3.0*10.0", "30", null
            }, //

            {
                "-3*10", "-30", null
            }, //
            {
                "-3.0*10", "-30", null
            }, //
            {
                "-3*10.0", "-30", null
            }, //
            {
                "-3.0*10.0", "-30", null
            }, //

            {
                "3*-10", "-30", null
            }, //
            {
                "3.0*-10", "-30", null
            }, //
            {
                "3*-10.0", "-30", null
            }, //
            {
                "3.0*-10.0", "-30", null
            }, //

            {
                "-3*-10", "30", null
            }, //
            {
                "-3.0*-10", "30", null
            }, //
            {
                "-3*-10.0", "30", null
            }, //
            {
                "-3.0*-10.0", "30", null
            }, //

            // multiplications with non-integral results produce a DoubleNode (regardless of
            // numeric type of inputs)
            {
                "4*1.2", "4.8", null
            }, //
            {
                "1.2*4", "4.8", null
            }, //
            {
                "1.2*4.2", "5.04", null
            }, //

            {
                "-4*1.2", "-4.8", null
            }, //
            {
                "-1.2*4", "-4.8", null
            }, //
            {
                "-1.2*4.2", "-5.04", null
            }, //

            {
                "4*-1.2", "-4.8", null
            }, //
            {
                "1.2*-4", "-4.8", null
            }, //
            {
                "1.2*-4.2", "-5.04", null
            }, //

            {
                "-4*-1.2", "4.8", null
            }, //
            {
                "-1.2*-4", "4.8", null
            }, //
            {
                "-1.2*-4.2", "5.04", null
            }, //

            {
                "3/10*100", "30", null
            }, //

            // modulos with integral results produce a LongNode (regardless of numeric type
            // of inputs)
            {
                "10%2", "0", null
            }, //
            {
                "10.0%2", "0", null
            }, //
            {
                "10%2.0", "0", null
            }, //
            {
                "10.0%2.0", "0", null
            }, //

            {
                "-10%2", "0", null
            }, //
            {
                "-10.0%2", "0", null
            }, //
            {
                "-10%2.0", "0", null
            }, //
            {
                "-10.0%2.0", "0", null
            }, //

            {
                "10%-2", "0", null
            }, //
            {
                "10.0%-2", "0", null
            }, //
            {
                "10%-2.0", "0", null
            }, //
            {
                "10.0%-2.0", "0", null
            }, //

            {
                "-10%-2", "0", null
            }, //
            {
                "-10.0%-2", "0", null
            }, //
            {
                "-10%-2.0", "0", null
            }, //
            {
                "-10.0%-2.0", "0", null
            }, //

            // modulos with non-integral results produce a DoubleNode (regardless of numeric
            // type of inputs)
            {
                "10%2.8", "1.6000000000000005", null
            }, //
            {
                "2.2%2", "0.20000000000000018", null
            }, //
            {
                "10.1%2.1", "1.6999999999999993", null
            }, //

            {
                "-10%2.8", "-1.6000000000000005", null
            }, //
            {
                "-2.2%2", "-0.20000000000000018", null
            }, //
            {
                "-10.1%2.1", "-1.6999999999999993", null
            }, //

            {
                "10%-2.8", "1.6000000000000005", null
            }, //
            {
                "2.2%-2", "0.20000000000000018", null
            }, //
            {
                "10.1%-2.1", "1.6999999999999993", null
            }, //

            {
                "-10%-2.8", "-1.6000000000000005", null
            }, //
            {
                "-2.2%-2", "-0.20000000000000018", null
            }, //
            {
                "-10.1%-2.1", "-1.6999999999999993", null
            }, //

            // additions with integral results produce a LongNode (regardless of numeric
            // type of inputs)
            {
                "10+2", "12", null
            }, //
            {
                "10.0+2", "12", null
            }, //
            {
                "10+2.0", "12", null
            }, //
            {
                "9.5+2.5", "12", null
            }, //

            {
                "-10+2", "-8", null
            }, //
            {
                "-10.0+2", "-8", null
            }, //
            {
                "-10+2.0", "-8", null
            }, //
            {
                "-9.5+1.5", "-8", null
            }, //

            {
                "10+-2", "8", null
            }, //
            {
                "10.0+-2", "8", null
            }, //
            {
                "10+-2.0", "8", null
            }, //
            {
                "9.5+-1.5", "8", null
            }, //

            {
                "-10+-2", "-12", null
            }, //
            {
                "-10.0+-2", "-12", null
            }, //
            {
                "-10+-2.0", "-12", null
            }, //
            {
                "-9.5+-2.5", "-12", null
            }, //

            // additions with non-integral results produce a DoubleNode (regardless of
            // numeric type of inputs)
            {
                "10+2.8", "12.8", null
            }, //
            {
                "2.2+2", "4.2", null
            }, //
            {
                "10.1+2.1", "12.2", null
            }, //

            {
                "-10+2.8", "-7.2", null
            }, //
            {
                "-2.2+2", "-0.20000000000000018", null
            }, //
            {
                "-10.2+2.1", "-8.1", null
            }, //

            {
                "10+-2.8", "7.2", null
            }, //
            {
                "2.2+-2", "0.20000000000000018", null
            }, //
            {
                "10.2+-2.1", "8.1", null
            }, //

            {
                "10+2.8", "12.8", null
            }, //
            {
                "2.2+2", "4.2", null
            }, //
            {
                "10.1+2.1", "12.2", null
            }, //

            // subtractions with integral results produce a LongNode (regardless of numeric
            // type of inputs)
            {
                "10-2", "8", null
            }, //
            {
                "10.0-2", "8", null
            }, //
            {
                "10-2.0", "8", null
            }, //
            {
                "9.5-1.5", "8", null
            }, //

            {
                "-10-2", "-12", null
            }, //
            {
                "-10.0-2", "-12", null
            }, //
            {
                "-10-2.0", "-12", null
            }, //
            {
                "-9.5-2.5", "-12", null
            }, //

            {
                "10--2", "12", null
            }, //
            {
                "10.0--2", "12", null
            }, //
            {
                "10--2.0", "12", null
            }, //
            {
                "9.5--2.5", "12", null
            }, //

            {
                "-10--2", "-8", null
            }, //
            {
                "-10.0--2", "-8", null
            }, //
            {
                "-10--2.0", "-8", null
            }, //
            {
                "-9.5--1.5", "-8", null
            }, //

            // subtractions with non-integral results produce a DoubleNode (regardless of
            // numeric type of inputs)
            {
                "10-2.8", "7.2", null
            }, //
            {
                "2.2-2", "0.20000000000000018", null
            }, //
            {
                "10.1-2.3", "7.8", null
            }, //

            {
                "-10-2.8", "-12.8", null
            }, //
            {
                "-2.2-2", "-4.2", null
            }, //
            {
                "-10.2-2.3", "-12.5", null
            }, //

            {
                "10--2.8", "12.8", null
            }, //
            {
                "2.2--2", "4.2", null
            }, //
            {
                "10.2--2.3", "12.5", null
            }, //

            {
                "-10--2.8", "-7.2", null
            }, //
            {
                "-2.2--2", "-0.20000000000000018", null
            }, //
            {
                "-10.1--2.3", "-7.8", null
            }, //

        });
    }

    @Test
    public void runTest() throws Exception {
        test(this.expression, expectedResultJsonString, expectedRuntimeExceptionMessage, null);
    }

}
