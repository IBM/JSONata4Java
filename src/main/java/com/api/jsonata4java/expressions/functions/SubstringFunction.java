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

import com.api.jsonata4java.JSONataUtils;
import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.api.jsonata4java.expressions.ExpressionsVisitor;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.utils.Constants;
import com.api.jsonata4java.expressions.utils.FunctionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * Complies with javascript substr (and thus JSONata $substring). See
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String/substr
 */
public class SubstringFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_SUBSTRING);
    public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_SUBSTRING);
    public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_SUBSTRING);
    public static String ERR_ARG3BADTYPE = String.format(Constants.ERR_MSG_ARG3_BAD_TYPE, Constants.FUNCTION_SUBSTRING);
    public static String ERR_ARG4BADTYPE = String.format(Constants.ERR_MSG_ARG4_BAD_TYPE, Constants.FUNCTION_SUBSTRING);

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
        // Create the variable to return
        JsonNode result = null;

        // Retrieve the number of arguments
        JsonNode argString = JsonNodeFactory.instance.nullNode();
        boolean useContext = FunctionUtils.useContextVariable(this, ctx, getSignature());
        int argCount = getArgumentCount(ctx);
        if (useContext) {
            argString = FunctionUtils.getContextVariable(expressionVisitor);
            if (argString != null && argString.isNull() == false) {
                argCount++;
            } else {
                useContext = false;
            }
        }

        // Make sure that we have the right number of arguments
        if (argCount >= 1 && argCount <= 3) {
            if (!useContext) {
                argString = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, 0);
            }
            // handle single value errors
            if (argCount == 1) {
                if (argString == null || argString.isNumber()) {
                    throw new EvaluateRuntimeException(ERR_BAD_CONTEXT);
                } else if (!argString.isTextual()) {
                    throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
                }
                // else while a string, assumes this is 2nd arg with first from context
                throw new EvaluateRuntimeException(ERR_ARG2BADTYPE);
            }
            // else we have at least two arguments
            final JsonNode argStart = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, useContext ? 0 : 1);
            // ensure valid 2nd argument
            if (argString == null) {
                if (argStart == null || argStart.isNumber()) {
                    return null;
                }
                throw new EvaluateRuntimeException(ERR_ARG2BADTYPE);
            } else if (!argString.isTextual()) {
                throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
            }
            // argString is valid now test for other conditions
            final String str = argString.textValue();

            final int start;
            if (argStart == null) {
                start = 0;
            } else {
                if (!argStart.isNumber()) {
                    throw new EvaluateRuntimeException(ERR_ARG2BADTYPE);
                }
                start = argStart.asInt();
            }

            final Integer length;
            if (argCount == 3) {
                JsonNode argLength = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, useContext ? 1 : 2);
                if (argLength == null) {
                    length = null;
                } else {
                    if (!argLength.isNumber()) {
                        throw new EvaluateRuntimeException(ERR_ARG3BADTYPE);
                    }
                    length = argLength.asInt();
                }
            } else {
                length = null;
            }

            result = new TextNode(JSONataUtils.substr(str, start, length));
        } else {
            throw new EvaluateRuntimeException(argCount == 0 ? ERR_ARG1BADTYPE : ERR_ARG4BADTYPE);
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
        // accepts a string (or context variable), a number, an optional number, returns
        // a string
        return "<s-nn?:s>";
    }
}
