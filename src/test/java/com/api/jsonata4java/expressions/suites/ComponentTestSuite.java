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

package com.api.jsonata4java.expressions.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.api.jsonata4java.test.expressions.AbsFunctionTests;
import com.api.jsonata4java.test.expressions.Base64DecodeFunctionTests;
import com.api.jsonata4java.test.expressions.Base64EncodeFunctionTests;
import com.api.jsonata4java.test.expressions.BasicExpressionsTest;
import com.api.jsonata4java.test.expressions.BooleanFunctionTests;
import com.api.jsonata4java.test.expressions.CeilFunctionTests;
import com.api.jsonata4java.test.expressions.ContainsFunctionTests;
import com.api.jsonata4java.test.expressions.CountFunctionTests;
import com.api.jsonata4java.test.expressions.ExpressionsTests;
import com.api.jsonata4java.test.expressions.FloorFunctionTests;
import com.api.jsonata4java.test.expressions.FormatBaseFunctionTests;
import com.api.jsonata4java.test.expressions.FormatNumberFunctionTests;
import com.api.jsonata4java.test.expressions.FromMillisFunctionTests;
import com.api.jsonata4java.test.expressions.InvalidSyntaxTest;
import com.api.jsonata4java.test.expressions.JoinFunctionTests;
import com.api.jsonata4java.test.expressions.JsonataDotOrgTests;
import com.api.jsonata4java.test.expressions.LengthFunctionTests;
import com.api.jsonata4java.test.expressions.LowercaseFunctionTests;
import com.api.jsonata4java.test.expressions.MatchFunctionTests;
import com.api.jsonata4java.test.expressions.MaxFunctionTests;
import com.api.jsonata4java.test.expressions.MillisFunctionTests;
import com.api.jsonata4java.test.expressions.MinFunctionTests;
import com.api.jsonata4java.test.expressions.NotFunctionTests;
import com.api.jsonata4java.test.expressions.NowFunctionTests;
import com.api.jsonata4java.test.expressions.NumberFunctionTests;
import com.api.jsonata4java.test.expressions.NumericCoercionTests;
import com.api.jsonata4java.test.expressions.PadFunctionTests;
import com.api.jsonata4java.test.expressions.PowerFunctionTests;
import com.api.jsonata4java.test.expressions.RandomFunctionTests;
import com.api.jsonata4java.test.expressions.ReplaceFunctionTests;
import com.api.jsonata4java.test.expressions.RoundFunctionTests;
import com.api.jsonata4java.test.expressions.SingletonArrayHandlingTests;
import com.api.jsonata4java.test.expressions.SplitFunctionTests;
import com.api.jsonata4java.test.expressions.SqrtFunctionTests;
import com.api.jsonata4java.test.expressions.StringFunctionTests;
import com.api.jsonata4java.test.expressions.SubstringAfterFunctionTests;
import com.api.jsonata4java.test.expressions.SubstringBeforeFunctionTests;
import com.api.jsonata4java.test.expressions.SubstringFunctionTests;
import com.api.jsonata4java.test.expressions.SumFunctionTests;
import com.api.jsonata4java.test.expressions.ToMillisFunctionTests;
import com.api.jsonata4java.test.expressions.TrimFunctionTests;
import com.api.jsonata4java.test.expressions.UnpackFunctionTests;
import com.api.jsonata4java.test.expressions.UppercaseFunctionTests;
import com.api.jsonata4java.test.expressions.path.PathExpressionSyntaxTests;
import com.api.jsonata4java.test.expressions.path.PathExpressionTests;
import com.api.jsonata4java.text.expressions.utils.JsonMergeUtilsTest;

@RunWith(Suite.class)
@SuiteClasses({ BasicExpressionsTest.class, JsonataDotOrgTests.class, ExpressionsTests.class, InvalidSyntaxTest.class,
		SingletonArrayHandlingTests.class, UnpackFunctionTests.class, StringFunctionTests.class,
		SubstringFunctionTests.class, NotFunctionTests.class, CountFunctionTests.class, LengthFunctionTests.class,
		SubstringBeforeFunctionTests.class, SubstringAfterFunctionTests.class, UppercaseFunctionTests.class,
		LowercaseFunctionTests.class, TrimFunctionTests.class, PadFunctionTests.class, ContainsFunctionTests.class,
		SplitFunctionTests.class, JoinFunctionTests.class, ReplaceFunctionTests.class, NowFunctionTests.class,
		MatchFunctionTests.class, FromMillisFunctionTests.class, FormatNumberFunctionTests.class,
		FormatBaseFunctionTests.class, Base64EncodeFunctionTests.class, Base64DecodeFunctionTests.class,
		NumericCoercionTests.class, NumberFunctionTests.class, AbsFunctionTests.class, FloorFunctionTests.class,
		CeilFunctionTests.class, RoundFunctionTests.class, PowerFunctionTests.class, SqrtFunctionTests.class,
		RandomFunctionTests.class, MillisFunctionTests.class, ToMillisFunctionTests.class, MinFunctionTests.class,
		MaxFunctionTests.class, BooleanFunctionTests.class, JsonMergeUtilsTest.class, PathExpressionTests.class,
		PathExpressionSyntaxTests.class, SumFunctionTests.class

})
public class ComponentTestSuite {

}
