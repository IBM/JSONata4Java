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

import java.math.BigDecimal;
import java.math.RoundingMode;
import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.api.jsonata4java.expressions.ExpressionsVisitor;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.utils.Constants;
import com.api.jsonata4java.expressions.utils.FunctionUtils;
import com.api.jsonata4java.expressions.utils.NumberUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

/**
 * http://docs.jsonata.org/numeric-functions.html
 *
 * $round(number [, precision])
 * 
 * Returns the value of the number parameter rounded to the number of decimal
 * places specified by the optional precision parameter.
 * 
 * The precision parameter (which must be an integer) species the number of
 * decimal places to be present in the rounded number. If precision is not
 * specified then it defaults to the value 0 and the number is rounded to the
 * nearest integer. If precision is negative, then its value specifies which
 * column to round to on the left side of the decimal place
 * 
 * This function uses the Round half to even strategy to decide which way to
 * round numbers that fall exactly between two candidates at the specified
 * precision. This strategy is commonly used in financial calculations and is
 * the default rounding mode in IEEE 754.
 * 
 * Examples
 * 
 * $round(123.456)==123 $round(123.456, 2)==123.46 $round(123.456, -1) ==
 * 120 $round(123.456, -2)==100 $round(11.5)==12 $round(12.5)==12
 * $round(125, -1)==120
 * 
 */
public class RoundFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_ROUND);
    public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_ROUND);
    public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_ROUND);
    public static String ERR_ARG3BADTYPE = String.format(Constants.ERR_MSG_ARG3_BAD_TYPE, Constants.FUNCTION_ROUND);

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
        // Create the variable to return
        JsonNode result = null;

        // Retrieve the number of arguments
        JsonNode argNumber = JsonNodeFactory.instance.nullNode();
        JsonNode argPrecision = JsonNodeFactory.instance.nullNode();
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
        if (argCount == 1 || argCount == 2) {
            if (!useContext) {
                argNumber = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, 0);
            }

            // Make sure that we have the number argument
            if (argNumber == null) {
                return null;
            }
            if (argNumber.isNumber()) {
                /*
                 * We do not need to check whether the number specified is within the acceptable
                 * range of Double.MAX_VALUE and -Double.MAX_VALUE. This will already have been
                 * done in the ExpressionsVisitor::visitNumber method.
                 * 
                 * For now, we simply need to convert the number to a BigDecimal. We use
                 * BigDecimal because it allows us to specify the required rounding strategy.
                 */
                BigDecimal bigDec = argNumber.decimalValue();

                // If argPrecision is null... default to zero
                int precision = 0;
                if (argCount == 2) {
                    argPrecision = FunctionUtils.getValuesListExpression(expressionVisitor, ctx,
                        useContext ? 0 : 1);
                    if (argPrecision != null) {
                        // Make sure that precision is an integer
                        if (argPrecision.isIntegralNumber()) {
                            precision = argPrecision.intValue();
                        } else {
                            throw new EvaluateRuntimeException(ERR_ARG2BADTYPE);
                        }
                    }
                }

                // Now round the number and create the node to return
                BigDecimal round = bigDec.setScale(precision, RoundingMode.HALF_EVEN);
                result = NumberUtils.convertNumberToValueNode(round.toPlainString());
            } else {
                throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
            }
        } else {
            throw new EvaluateRuntimeException(argCount == 0 ? ERR_BAD_CONTEXT : ERR_ARG3BADTYPE);
        }

        return result;
    }

    @Override
    public int getMaxArgs() {
        return 2;
    }

    @Override
    public int getMinArgs() {
        return 0; // account for context variable
    }

    @Override
    public String getSignature() {
        // accepts a number (or context variable), an optional number, returns a number
        return "<n-n?:n>";
    }
}
