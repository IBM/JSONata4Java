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

import java.io.Serializable;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import com.api.jsonata4java.test.expressions.AbsFunctionTests;
import com.api.jsonata4java.test.expressions.Base64DecodeFunctionTests;
import com.api.jsonata4java.test.expressions.Base64EncodeFunctionTests;
import com.api.jsonata4java.test.expressions.BasicExpressionsTests;
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
import com.api.jsonata4java.test.expressions.TestBindingReference;
import com.api.jsonata4java.test.expressions.ToMillisFunctionTests;
import com.api.jsonata4java.test.expressions.TrimFunctionTests;
import com.api.jsonata4java.test.expressions.UnpackFunctionTests;
import com.api.jsonata4java.test.expressions.UppercaseFunctionTests;
import com.api.jsonata4java.test.expressions.path.PathExpressionSyntaxTests;
import com.api.jsonata4java.test.expressions.path.PathExpressionTests;
import com.api.jsonata4java.text.expressions.utils.JsonMergeUtilsTest;

/* @deprecated these tests have been incorporated in the AgnosticTestSuit */

@RunWith(Suite.class)
@SuiteClasses({
    AbsFunctionTests.class, //
    Base64DecodeFunctionTests.class, //
    Base64EncodeFunctionTests.class, //
    BasicExpressionsTests.class, //
    BooleanFunctionTests.class, //
    CeilFunctionTests.class, //
    ContainsFunctionTests.class, //
    CountFunctionTests.class, //
    ExpressionsTests.class, //
    FloorFunctionTests.class, //
    FormatBaseFunctionTests.class, //
    FormatNumberFunctionTests.class, //
    FromMillisFunctionTests.class, //
    InvalidSyntaxTest.class, //
    JoinFunctionTests.class, //
    JsonMergeUtilsTest.class, //
    JsonataDotOrgTests.class, //
    LengthFunctionTests.class, //
    LowercaseFunctionTests.class, //
    MatchFunctionTests.class, //
    MaxFunctionTests.class, //
    MillisFunctionTests.class, //
    MinFunctionTests.class, //
    NotFunctionTests.class, //
    NowFunctionTests.class, //
    NumberFunctionTests.class, //
    NumericCoercionTests.class, //
    PadFunctionTests.class, //
    PathExpressionSyntaxTests.class, //
    PathExpressionTests.class, //
    PowerFunctionTests.class, //
    RandomFunctionTests.class, //
    ReplaceFunctionTests.class, //
    RoundFunctionTests.class, //
    SingletonArrayHandlingTests.class, //
    SplitFunctionTests.class, //
    SqrtFunctionTests.class, //
    StringFunctionTests.class, //
    SubstringAfterFunctionTests.class, //
    SubstringBeforeFunctionTests.class, //
    SubstringFunctionTests.class, //
    SumFunctionTests.class, //
    ToMillisFunctionTests.class, //
    TrimFunctionTests.class, //
    UnpackFunctionTests.class, //
    UppercaseFunctionTests.class, //
    TestBindingReference.class //
})

public class ComponentTestSuite implements Serializable {

    private static final long serialVersionUID = 2033074749160537540L;
}
