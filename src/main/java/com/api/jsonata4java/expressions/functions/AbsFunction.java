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
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Fct_chainContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.utils.Constants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.LongNode;

/**
 * Returns the absolute value of the number parameter, i.e. if the number is
 * negative, it returns the positive value.<br>
 * <br>
 * http://docs.jsonata.org/numeric-functions.html<br>
 * $abs(number)<br>
 * If number is not specified (i.e. this function is invoked with no arguments),
 * then the context value is used as the value of number.<br>
 * <br>
 * Examples:<br> 
 * $abs(5)==5<br>
 * $abs(-5)==5<br>
 * <p>
 * NOTE: FROM THE java.math::abs() FUNCTION JAVADOC:<br>
 * Note that if the argument is equal to the value of {@link Long#MIN_VALUE},
 * the most negative representable {@code long} value, the result is that same
 * value, which is negative.
 * </p>
 */
public class AbsFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_ABS);
    public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_ABS);
    public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_ABS);

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
        JsonNode result = null;
        final CtxEvalResult ctxEvalResult = evalContext(expressionVisitor, ctx);
        final JsonNode arg = ctxEvalResult.arg;
        final int argCount = ctxEvalResult.argumentCount;

        switch (argCount) {
            case 0:
                if (arg != null) {
                    throw new EvaluateRuntimeException(ERR_BAD_CONTEXT);
                }
                // else signal no match (result = null)
                break;
            case 1:
                if (arg == null) {
                    return null;
                }
                if (!arg.isNumber()) {
                    throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
                }
                if (arg.isLong()) {
                    final long number = arg.longValue();
                    if (number == Long.MIN_VALUE) {
                        result = new LongNode(Long.MAX_VALUE);
                    } else {
                        result = new LongNode(Math.abs(number));
                    }
                } else { // arg is Double
                    final double number = arg.doubleValue();
                    result = new DoubleNode(Math.abs(number));
                }
                break;
            default: // argCount > 1
                if (ctx.getParent() instanceof Fct_chainContext) {
                    throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
                }
                throw new EvaluateRuntimeException(ERR_ARG2BADTYPE);
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
