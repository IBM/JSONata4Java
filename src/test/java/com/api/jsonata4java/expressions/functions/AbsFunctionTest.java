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

package com.api.jsonata4java.expressions.functions;

import static com.api.jsonata4java.text.expressions.utils.Utils.test;
import org.junit.jupiter.api.Test;
import com.api.jsonata4java.expressions.utils.Constants;

public class AbsFunctionTest {

    @Test
    void nullInput() throws Exception {
        test("$abs()", null, null, (String) null);
    }

    @Test
    void test02() throws Exception {
        test("$abs()", null, AbsFunction.ERR_ARG1BADTYPE, "{}");
    }

    @Test
    void test03() throws Exception {
        test("$abs()", null, AbsFunction.ERR_ARG1BADTYPE, "[]");
    }

    @Test
    void test04() throws Exception {
        test("$abs()", null, AbsFunction.ERR_BAD_CONTEXT, "null");
    }

    @Test
    void test05() throws Exception {
        test("$abs(null)", null, AbsFunction.ERR_ARG1BADTYPE, (String) null);
    }

    @Test
    void test06() throws Exception {
        test("$abs({})", null, AbsFunction.ERR_ARG1BADTYPE, (String) null);
    }

    @Test
    void test07() throws Exception {
        test("$abs([])", null, AbsFunction.ERR_ARG1BADTYPE, (String) null);
    }

    @Test
    void test08() throws Exception {
        test("$abs('1')", null, AbsFunction.ERR_ARG1BADTYPE, (String) null);
    }

    @Test
    void test09() throws Exception {
        test("$abs(\"234\")", null, AbsFunction.ERR_ARG1BADTYPE, (String) null);
    }

    @Test
    void test10() throws Exception {
        test("$abs(true)", null, AbsFunction.ERR_ARG1BADTYPE, (String) null);
    }

    @Test
    void test11() throws Exception {
        test("$abs(a.b.c)", null, null, (String) null);
    }

    @Test
    void test12() throws Exception {
        test("$abs(1)", "1", null, null);
    }

    @Test
    void test13() throws Exception {
        test("$abs(-1)", "1", null, null);
    }

    @Test
    void test14() throws Exception {
        test("$abs(2147483647)", "2147483647", null, null);
    }

    @Test
    void test15() throws Exception {
        test("$abs(-2147483640)", "2147483640", null, null);
    }

    @Test
    void test16() throws Exception {
        test("$abs(21474836471234)", "21474836471234", null, null);
    }

    @Test
    void test17() throws Exception {
        test("$abs(-21474836471234)", "21474836471234", null, null);
    }

    @Test
    void test18() throws Exception {
        test("$abs(1.0)", "1", null, null);
    }

    @Test
    void test19() throws Exception {
        test("$abs(-1.0)", "1", null, null);
    }

    @Test
    void test20() throws Exception {
        test("$abs(1.23456)", "1.23456", null, null);
    }

    @Test
    void test21() throws Exception {
        test("$abs(-1.23456)", "1.23456", null, null);
    }

    @Test
    void test22() throws Exception {
        test("$abs(1.234567890123)", "1.234567890123", null, null);
    }

    @Test
    void test23() throws Exception {
        test("$abs(-1.234567890123)", "1.234567890123", null, null);
    }

    @Test
    void test24() throws Exception {
        test("$abs(-9.123E99)", "9.123e+99", null, null);
    }

    @Test
    void test25() throws Exception {
        test("$abs(-1.79769E308)", "1.79769e+308", null, null);
    }

    @Test
    void test26() throws Exception {
        test("$abs(10/3.0)", "3.3333333333333333", null, null);
    }

    @Test
    void test27() throws Exception {
        test("$abs(-10/3.0)", "3.3333333333333333", null, null);
    }

    @Test
    void test29() throws Exception {
        test("$abs(9223372036854775807)", "9223372036854775807", null, null);
    }

    @Test
    void test30() throws Exception {
        test("$abs(-9223372036854775807)", "9223372036854775807", null, null);
    }

    @Test
    void test31() throws Exception {
        test("$abs(9223372036854775808)", "9223372036854775807", null, null);
    }

    @Test
    void test32() throws Exception {
        test("$abs(-9223372036854775808)", "9223372036854775807", null, null);
    }

    @Test
    void test33() throws Exception {
        test("$abs(9223372036854775809)", "9223372036854775807", null, null);
    }

    @Test
    void test34() throws Exception {
        test("$abs(-9223372036854775809)", "9223372036854775807", null, null);
    }

    @Test
    void test35() throws Exception {
        test("$abs(9223372036854775899.5)", "9223372036854775807", null, null);
    }

    @Test
    void test36() throws Exception {
        test("$abs(-9223372036854775899.5)", Long.toString(Math.abs(Long.MAX_VALUE)), null, null);
    }

    @Test
    void test37() throws Exception {
        test("$abs(9223372036854775809123456789)", "9.223372036854776E27", null, null);
    }

    @Test
    void test38() throws Exception {
        test("$abs(-9223372036854775809123456789)", Double.toString(Math.abs(-9223372036854775809123456789D)), null, null);
    }

    @Test
    void test39() throws Exception {
        test("$abs(9223372036854775809123456789.5)", Double.toString(Math.abs(9223372036854775809123456789.5)), null, null);
    }

    @Test
    void test40() throws Exception {
        test("$abs(-9223372036854775809123456789.5)", Double.toString(Math.abs(-9223372036854775809123456789.5)), null, null);
    }

    @Test
    void test41() throws Exception {
        test("$abs(1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
            + "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
            + "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789)",
            "1.2345678901234567e+308", null, null); // original JSONata 1.8.6 is less precise 1.234567890123e+308
    }

    @Test
    void test42() throws Exception {
        test("$abs(1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
            + "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
            + "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890)",
            "1.2345678901234567e+308",
            String.format(Constants.ERR_MSG_NUMBER_OUT_OF_RANGE,
                "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
                    + "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
                    + "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"),
            null);
    }

    @Test
    void test43() throws Exception {
        test("{}~>$abs()", null, AbsFunction.ERR_ARG1BADTYPE, (String) null);
    }

    @Test
    void test44() throws Exception {
        test("[]~>$abs()", null, AbsFunction.ERR_ARG1BADTYPE, (String) null);
    }

    @Test
    void test45() throws Exception {
        test("'1'~>$abs()", null, AbsFunction.ERR_ARG1BADTYPE, (String) null);
    }

    @Test
    void test46() throws Exception {
        test("true~>$abs()", null, AbsFunction.ERR_ARG1BADTYPE, (String) null);
    }

    @Test
    void test47() throws Exception {
        test("null~>$abs()", null, AbsFunction.ERR_BAD_CONTEXT, (String) null);
    }

    @Test
    void test48() throws Exception {
        test("a.b.c~>$abs()", null, null, (String) null);
    }

    @Test
    void test49() throws Exception {
        test("1~>$abs()", "1", null, null);
    }

    @Test
    void test50() throws Exception {
        test("-1~>$abs()", "1", null, null);
    }

    @Test
    void test51() throws Exception {
        test("2147483647~>$abs()", "2147483647", null, null);
    }

    @Test
    void test53() throws Exception {
        test("-2147483640~>$abs()", "2147483640", null, null);
    }

    @Test
    void test54() throws Exception {
        test("21474836471234~>$abs()", "21474836471234", null, null);
    }

    @Test
    void test55() throws Exception {
        test("-21474836471234~>$abs()", "21474836471234", null, null);
    }

    @Test
    void test56() throws Exception {
        test("1.0~>$abs()", "1", null, null);
    }

    @Test
    void test57() throws Exception {
        test("-1.0~>$abs()", "1", null, null);
    }

    @Test
    void test58() throws Exception {
        test("1.23456~>$abs()", "1.23456", null, null);
    }

    @Test
    void test59() throws Exception {
        test("-1.23456~>$abs()", "1.23456", null, null);
    }

    @Test
    void test60() throws Exception {
        test("1.234567890123~>$abs()", "1.234567890123", null, null);
    }

    @Test
    void test61() throws Exception {
        test("-1.234567890123~>$abs()", "1.234567890123", null, null);
    }

    @Test
    void test62() throws Exception {
        test("10/3.0~>$abs()", "3.3333333333333333", null, null);
    }

    @Test
    void test63() throws Exception {
        test("-10/3.0~>$abs()", "3.3333333333333333", null, null);
    }

    @Test
    void test64() throws Exception {
        test("9223372036854775807~>$abs()", "9223372036854775807", null, null);
    }

    @Test
    void test65() throws Exception {
        test("-9223372036854775808~>$abs()", "9223372036854775807", null, null);
    }

    @Test
    void test66() throws Exception {
        test("9223372036854775809~>$abs()", "9223372036854775807", null, null);
    }

    @Test
    void test67() throws Exception {
        test("-9223372036854775809~>$abs()", "9223372036854775807", null, null);
    }

    @Test
    void test68() throws Exception {
        test("9223372036854775899.5~>$abs()", "9223372036854775807", null, null);
    }

    @Test
    void test69() throws Exception {
        test("-9223372036854775899.5~>$abs()", "9223372036854775807", null, null);
    }

    @Test
    void test70() throws Exception {
        test("9223372036854775809123456789~>$abs()", "9.223372036854776E27", null, null);
    }

    @Test
    void test71() throws Exception {
        test("-9223372036854775809123456789~>$abs()", "9.223372036854776E27", null, null);
    }

    @Test
    void test72() throws Exception {
        test("9223372036854775809123456789.5~>$abs()", "9.223372036854776E27", null, null);
    }

    @Test
    void test73() throws Exception {
        test("-9223372036854775809123456789.5~>$abs()", "9.223372036854776E27", null, null);
    }

    @Test
    void testWith2Args() throws Exception {
        test("$abs(1, 2)", null, AbsFunction.ERR_ARG2BADTYPE, (String) null);
    }

    @Test
    void testWithChainPlus1Arg() throws Exception {
        test("-1 ~> $abs(-2)", null, AbsFunction.ERR_ARG1BADTYPE, (String) null);
    }
}
