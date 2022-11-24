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

import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.api.jsonata4java.expressions.ExpressionsVisitor;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.utils.Constants;
import com.api.jsonata4java.expressions.utils.FunctionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.LongNode;

public class SumFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_SUM);
    public static final String ERR_ARG1ARRTYPE = String.format(Constants.ERR_MSG_ARG1_MUST_BE_ARRAY_OF_NUMBER,
        Constants.FUNCTION_SUM);
    public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_SUM);
    public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_SUM);

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {

        // Retrieve the number of arguments
        JsonNode argArray = JsonNodeFactory.instance.nullNode();
        boolean useContext = FunctionUtils.useContextVariable(this, ctx, getSignature());
        int argCount = getArgumentCount(ctx);
        if (useContext) {
            argArray = FunctionUtils.getContextVariable(expressionVisitor);
            if (argArray != null && argArray.isNull() == false) {
                argCount++;
            } else {
                useContext = false;
            }
        }

        // Make sure that we have the right number of arguments
        if (argCount == 1) {
            if (!useContext) {
                argArray = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, 0);
                FunctionUtils.validateArguments(ERR_ARG1ARRTYPE, expressionVisitor, ctx, 0, getSignature());
            }
            // if arg is an array, sum its values
            if (argArray == null) {
                return null;
            } else if (argArray.isArray()) {
                ArrayNode arr = (ArrayNode) argArray;

                // if ALL of the array members are integral, return as a LongNode
                // if ANY of the array members are floats, return as a DoubleNode
                // this is important since floating point math has rounding errors
                // (e.g. 64.1-0.1 = 63.99999999999999, not 64.0)
                // provided we return sums of integrals as a long, customers can
                // expect = to work reliably when comparing sums of integrals
                // against another integral
                boolean shouldReturnAsLong = true;
                for (JsonNode a : arr) {
                    if (a.isFloatingPointNumber() || a.isDouble()) {
                        shouldReturnAsLong = false;
                    } else if (!a.isIntegralNumber()) {
                        // also complain if any non-numeric types are included in the
                        // array
                        throw new EvaluateRuntimeException(ERR_ARG1ARRTYPE);
                    }
                }

                if (shouldReturnAsLong) {
                    long sum = 0;
                    for (JsonNode a : arr) {
                        sum += a.asLong();
                    }
                    return new LongNode(sum);
                } else {
                    double sum = 0.0d;
                    for (JsonNode a : arr) {
                        sum += a.asDouble();
                    }
                    return new DoubleNode(sum);
                }

            } else if (argArray.isIntegralNumber()) {
                return new LongNode(argArray.asLong());
            } else if (argArray.isFloatingPointNumber() || argArray.isDouble()) {
                return new DoubleNode(argArray.asDouble());
            } else {
                throw new EvaluateRuntimeException(ERR_ARG1ARRTYPE);
            }

        } else {
            throw new EvaluateRuntimeException(argCount == 0 ? ERR_ARG1BADTYPE : ERR_ARG2BADTYPE);
        }
    }

    public static void main(String[] args) {

        for (double i = 0; i < 100; i++) {
            System.out.println((0.1 + i) + "-" + 0.1 + " = " + ((0.1 + i) - 0.1));
        }

        // System.out.println(10.0d==10l);

    }

    @Override
    public int getMaxArgs() {
        return 1;
    }

    @Override
    public int getMinArgs() {
        return 0; // account for context variable
    }

    @Override
    public String getSignature() {
        // takes an array of numbers, returns a number
        return "<a<n>:n>";
    }

}
