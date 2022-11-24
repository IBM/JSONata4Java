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
 * $power(base, exponent)
 * 
 * Returns the value of base raised to the power of exponent (baseexponent).
 * 
 * If base is not specified (i.e. this function is invoked with one argument),
 * then the context value is used as the value of base.
 * 
 * An error is thrown if the values of base and exponent lead to a value that
 * cannot be represented as a JSON number (e.g. Infinity, complex numbers).
 * 
 * Examples
 * 
 * $power(2, 8)==8 $power(2, 0.5)==1.414213562373 $power(2, -2)==0.25
 * 
 */
public class PowerFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_POWER);
    public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_POWER);
    public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_POWER);
    public static String ERR_ARG3BADTYPE = String.format(Constants.ERR_MSG_ARG3_BAD_TYPE, Constants.FUNCTION_POWER);

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
        if (argCount == 2) {
            if (!useContext) {
                argNumber = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, 0);
            }
            final JsonNode argExponent = FunctionUtils.getValuesListExpression(expressionVisitor, ctx,
                useContext ? 0 : 1);

            // Make sure that we have the base argument
            if (argNumber == null) {
                return null;
            }
            if (argNumber.isNumber()) {
                /*
                 * We do not need to check whether the base and exponent arguments specified are
                 * within the acceptable range of Double.MAX_VALUE and -Double.MAX_VALUE. This
                 * will already have been done in the ExpressionsVisitor::visitNumber method.
                 * 
                 * Make that we have the exponent argument.
                 */
                if (argExponent != null) {
                    if (argExponent.isNumber()) {
                        // Calculate the result and create the node to return
                        Double power = Math.pow(argNumber.doubleValue(), argExponent.doubleValue());

                        if (power.isInfinite() == false // != Double.POSITIVE_INFINITY && power != Double.NEGATIVE_INFINITY
                            && power.isNaN() == false) {
                            if (power - power.longValue() == 0.0) {
                                result = new LongNode(power.longValue());
                            } else {
                                result = new DoubleNode(power);
                            }
                        } else {
                            /*
                             * The result cannot be represented as a number. Throw a suitable exception.
                             */
                            final String msg = String.format(Constants.ERR_MSG_POWER_FUNC_RESULT_NOT_NUMBER,
                                argNumber.asText(), argExponent.asText());
                            throw new EvaluateRuntimeException(msg);
                        }
                    } else {
                        throw new EvaluateRuntimeException(ERR_ARG2BADTYPE);
                    }
                } else {
                    throw new EvaluateRuntimeException(ERR_ARG2BADTYPE);
                }
            } else {
                throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
            }
        } else {
            throw new EvaluateRuntimeException(
                argCount == 0 ? ERR_ARG1BADTYPE : argCount == 1 ? ERR_BAD_CONTEXT : ERR_ARG3BADTYPE);
        }

        return result;
    }

    @Override
    public int getMaxArgs() {
        return 2;
    }

    @Override
    public int getMinArgs() {
        return 1; // account for context variable
    }

    @Override
    public String getSignature() {
        // accepts a number (or context variable), a number, returns a number
        return "<n-n:n>";
    }
}
