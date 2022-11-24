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

import org.antlr.v4.runtime.ParserRuleContext;
import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.api.jsonata4java.expressions.ExpressionsVisitor;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Fct_chainContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.utils.Constants;
import com.api.jsonata4java.expressions.utils.FunctionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.LongNode;

/**
 * Always returns as a DoubleNode (regardless of input)
 */
public class AverageFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_AVERAGE);
    public static final String ERR_ARG_TYPE = String.format(Constants.ERR_MSG_ARG1_MUST_BE_ARRAY_OF_NUMBER,
        Constants.FUNCTION_AVERAGE);
    public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_AVERAGE);
    public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_AVERAGE);

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {

        // Retrieve the number of arguments
        JsonNode arg = JsonNodeFactory.instance.nullNode();
        boolean useContext = FunctionUtils.useContextVariable(this, ctx, getSignature());
        int argCount = getArgumentCount(ctx);
        if (useContext) {
            arg = FunctionUtils.getContextVariable(expressionVisitor);
            ParserRuleContext prc = ctx.getParent();
            if ((prc != null && prc instanceof Fct_chainContext) || (arg != null && arg.isNull() == false)) {
                argCount++;
            } else {
                useContext = false;
            }
        }

        // Make sure that we have the right number of arguments
        if (argCount == 1) {
            if (!useContext) {
                arg = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, 0);
            }
            if (arg == null) {
                return null;
            } else if (useContext == false) {
                FunctionUtils.validateArguments(ERR_ARG_TYPE, expressionVisitor, ctx, 0, getSignature());
            }
            if (arg.isArray()) {
                ArrayNode arr = (ArrayNode) arg;

                if (arr.size() == 0) { // avoid divide by 0 errors
                    return null;
                }

                double sum = 0;
                for (JsonNode a : arr) {
                    if (a.isNumber()) {
                        sum += a.asDouble(); // asDouble() won't throw an exception
                                             // even if non-numeric (that's why we do
                                             // the isNumber() check above)
                    } else {
                        // also complain if any non-numeric types are included in the
                        // array
                        throw new EvaluateRuntimeException(ERR_ARG_TYPE);
                    }
                }

                Double avg = sum / arr.size();
                if (avg - avg.longValue() == 0.0) {
                    return new LongNode(avg.longValue());
                } else {
                    return new DoubleNode(avg);
                }
            } else if (arg.isNumber()) {
                if (arg.isLong()) {
                    return arg;
                } else {
                    Double avg = arg.asDouble();
                    if (avg - avg.longValue() == 0.0) {
                        return new LongNode(avg.longValue());
                    } else {
                        return new DoubleNode(avg);
                    }
                }
            } else {
                throw new EvaluateRuntimeException(ERR_ARG_TYPE);
            }

        } else {
            throw new EvaluateRuntimeException(
                argCount == 0 ? (useContext ? ERR_BAD_CONTEXT : ERR_ARG1BADTYPE) : ERR_ARG2BADTYPE);
        }
    }

    @Override
    public int getMaxArgs() {
        return 1;
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public String getSignature() {
        // accepts an array of numbers (or context variable), returns a number
        return "<a<n>:n>";
    }
}
