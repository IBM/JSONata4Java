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
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.LongNode;

/**
 * http://docs.jsonata.org/numeric-functions.html
 * 
 * $sqrt(number)
 * 
 * Returns the square root of the value of the number parameter.
 * 
 * If number is not specified (i.e. this function is invoked with one argument),
 * then the context value is used as the value of number.
 * 
 * An error is thrown if the value of number is negative.
 * 
 * Examples
 * 
 * $sqrt(4)==2 $sqrt(2)==1.414213562373
 * 
 */
public class SqrtFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_SQRT);
    public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_SQRT);
    public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_SQRT);

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
        // Create the variable to return
        JsonNode result = null;

        // Retrieve the number of arguments
        JsonNode argNumber = JsonNodeFactory.instance.nullNode();
        boolean useContext = FunctionUtils.useContextVariable(this, ctx, getSignature());
        int argCount = getArgumentCount(ctx);
        if (useContext) {
            argNumber = FunctionUtils.getContextVariable(expressionVisitor);
            if (argNumber != null && argNumber.isNull() == false) {
                argCount++;
            } else {
                useContext = false;
            }
        }

        // Make sure that we have the right number of arguments
        if (argCount == 1) {
            if (!useContext) {
                argNumber = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, 0);
            }
            if (argNumber == null) {
                return null;
            }
            // Check the type of the argument
            if (argNumber.isNumber()) {
                // Make sure that the number is a valid positive number
                Double number = argNumber.doubleValue();
                if (number >= 0 && number.isNaN() == false && number.isInfinite() == false // Should not be possible
                // because it should be
                // caught in
                // ExpressionsVisitor::visitNumber
                ) {
                    // Calculate the result and create the node to return
                    Double sqrt = Math.sqrt(argNumber.doubleValue());
                    if (sqrt - sqrt.longValue() == 0.0) {
                        result = new LongNode(sqrt.longValue());
                    } else {
                        result = new DoubleNode(sqrt);
                    }
                } else {
                    /*
                     * The sqrt function cannot be applied to the argument. Throw a suitable
                     * exception.
                     */
                    final String msg = String.format(Constants.ERR_MSG_FUNC_CANNOT_BE_APPLIED_NEG_NUM,
                        Constants.FUNCTION_SQRT, argNumber.doubleValue());
                    throw new EvaluateRuntimeException(msg);
                }
            } else {
                throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
            }
        } else {
            throw new EvaluateRuntimeException(argCount == 0 ? ERR_BAD_CONTEXT : ERR_ARG2BADTYPE);
        }

        return result;
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
        // accepts a number (or context variable), returns a number
        return "<n-:n>";
    }
}
