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

package com.api.jsonata4java.test.expressions;

import static com.api.jsonata4java.text.expressions.utils.Utils.test;

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
 * $abs(number)
 * 
 * Returns the absolute value of the number parameter, i.e. if the number is
 * negative, it returns the positive value.
 * 
 * If number is not specified (i.e. this function is invoked with no arguments),
 * then the context value is used as the value of number.
 * 
 * Examples
 * 
 * $abs(5)==5 $abs(-5)==5
 * 
 * 
 * NOTE:
 * 
 * FROM THE java.math::abs() FUNCTION JAVADOC:
 * 
 * <p>
 * Note that if the argument is equal to the value of {@link Long#MIN_VALUE},
 * the most negative representable {@code long} value, the result is that same
 * value, which is negative.
 * 
 */
@RunWith(Parameterized.class)
public class AbsFunctionTests {

	private static final String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_ABS);
	private static final String ERR_MSG_ARG1_BAD_TYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE,
			Constants.FUNCTION_ABS);
	private static final String ERR_MSG_NUMBER_OUT_OF_RANGE = String.format(Constants.ERR_MSG_NUMBER_OUT_OF_RANGE,
			"1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");

	@Parameter(0)
	public String expression;

	@Parameter(1)
	public String expectedResultJsonString;

	@Parameter(2)
	public String expectedRuntimeExceptionMessage;

	@Parameters(name = "{index}: {0} -> {1} ({2})")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] { { "$abs()", null, ERR_BAD_CONTEXT }, //
				{ "$abs({})", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$abs([])", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$abs('1')", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$abs(true)", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$abs(null)", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "$abs(a.b.c)", null, null }, //
				{ "$abs(1)", Long.toString(Math.abs(1)), null }, //
				{ "$abs(-1)", Long.toString(Math.abs(-1)), null }, //
				{ "$abs(2147483647)", Long.toString(Math.abs(2147483647)), null }, //
				{ "$abs(-2147483640)", Long.toString(Math.abs(-2147483640)), null }, //
				{ "$abs(21474836471234)", Long.toString((long) Math.abs(21474836471234D)), null }, //
				{ "$abs(-21474836471234)", Long.toString((long) Math.abs(-21474836471234D)), null }, //
				{ "$abs(1.0)", Double.toString(Math.abs(1.0)), null }, //
				{ "$abs(-1.0)", Double.toString(Math.abs(-1.0)), null }, //
				{ "$abs(1.23456)", Double.toString(Math.abs(1.23456)), null }, //
				{ "$abs(-1.23456)", Double.toString(Math.abs(-1.23456)), null }, //
				{ "$abs(1.234567890123)", Double.toString(Math.abs(1.234567890123)), null }, //
				{ "$abs(-1.234567890123)", Double.toString(Math.abs(-1.234567890123)), null }, //
				{ "$abs(10/3.0)", Double.toString(Math.abs(10 / 3.0)), null }, //
				{ "$abs(-10/3.0)", Double.toString(Math.abs(-10 / 3.0)), null }, //
				{ "$abs(9223372036854775807)", Long.toString(Math.abs(Long.MAX_VALUE)), null }, //
				{ "$abs(-9223372036854775808)", Double.toString(Math.abs(-9223372036854775808D)), null }, //
				{ "$abs(9223372036854775809)", Double.toString(Math.abs(9223372036854775809D)), null }, //
				{ "$abs(-9223372036854775809)", Double.toString(Math.abs(-9223372036854775809D)), null }, //
				{ "$abs(9223372036854775899.5)", Double.toString(Math.abs(9223372036854775899.5)), null }, //
				{ "$abs(-9223372036854775899.5)", Double.toString(Math.abs(-9223372036854775899.5)), null }, //
				{ "$abs(9223372036854775809123456789)", Double.toString(Math.abs(9223372036854775809123456789D)), //
						null }, //
				{ "$abs(-9223372036854775809123456789)", Double.toString(Math.abs(-9223372036854775809123456789D)), //
						null }, //
				{ "$abs(9223372036854775809123456789.5)", Double.toString(Math.abs(9223372036854775809123456789.5)), //
						null }, //
				{ "$abs(-9223372036854775809123456789.5)", Double.toString(Math.abs(-9223372036854775809123456789.5)), //
						null }, //
				{ "$abs(1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890)", //
						null, ERR_MSG_NUMBER_OUT_OF_RANGE }, //
				{ "{}~>$abs()", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "[]~>$abs()", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "'1'~>$abs()", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "true~>$abs()", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "null~>$abs()", null, ERR_MSG_ARG1_BAD_TYPE }, //
				{ "a.b.c~>$abs()", null, null }, //
				{ "1~>$abs()", Long.toString(Math.abs(1)), null }, //
				{ "-1~>$abs()", Long.toString(Math.abs(-1)), null }, //
				{ "2147483647~>$abs()", Long.toString(Math.abs(2147483647)), null }, //
				{ "-2147483640~>$abs()", Long.toString(Math.abs(-2147483640)), null }, //
				{ "21474836471234~>$abs()", Long.toString((long) Math.abs(21474836471234D)), null }, //
				{ "-21474836471234~>$abs()", Long.toString((long) Math.abs(-21474836471234D)), null }, //
				{ "1.0~>$abs()", Double.toString(Math.abs(1.0)), null }, //
				{ "-1.0~>$abs()", Double.toString(Math.abs(-1.0)), null }, //
				{ "1.23456~>$abs()", Double.toString(Math.abs(1.23456)), null }, //
				{ "-1.23456~>$abs()", Double.toString(Math.abs(-1.23456)), null }, //
				{ "1.234567890123~>$abs()", Double.toString(Math.abs(1.234567890123)), null }, //
				{ "-1.234567890123~>$abs()", Double.toString(Math.abs(-1.234567890123)), null }, //
				{ "10/3.0~>$abs()", Double.toString(Math.abs(10 / 3.0)), null }, //
				{ "-10/3.0~>$abs()", Double.toString(Math.abs(-10 / 3.0)), null }, //
				{ "9223372036854775807~>$abs()", Long.toString(Math.abs(Long.MAX_VALUE)), null }, //
				{ "-9223372036854775808~>$abs()", Double.toString(Math.abs(-9223372036854775808D)), null }, //
				{ "9223372036854775809~>$abs()", Double.toString(Math.abs(9223372036854775809D)), null }, //
				{ "-9223372036854775809~>$abs()", Double.toString(Math.abs(-9223372036854775809D)), null }, //
				{ "9223372036854775899.5~>$abs()", Double.toString(Math.abs(9223372036854775899.5)), null }, //
				{ "-9223372036854775899.5~>$abs()", Double.toString(Math.abs(-9223372036854775899.5)), null }, //
				{ "9223372036854775809123456789~>$abs()", Double.toString(Math.abs(9223372036854775809123456789D)), //
						null }, //
				{ "-9223372036854775809123456789~>$abs()", Double.toString(Math.abs(-9223372036854775809123456789D)), //
						null }, //
				{ "9223372036854775809123456789.5~>$abs()", Double.toString(Math.abs(9223372036854775809123456789.5)), //
						null }, //
				{ "-9223372036854775809123456789.5~>$abs()", Double.toString(Math.abs(-9223372036854775809123456789.5)), //
						null } //

		});
	}

	@Test
	public void runTest() throws Exception {
		test(this.expression, expectedResultJsonString, expectedRuntimeExceptionMessage, null);
	}
}
