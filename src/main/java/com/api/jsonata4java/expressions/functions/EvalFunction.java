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

import java.io.IOException;
import com.api.jsonata4java.Expression;
import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.api.jsonata4java.expressions.ExpressionsVisitor;
import com.api.jsonata4java.expressions.ParseException;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.utils.Constants;
import com.api.jsonata4java.expressions.utils.FunctionUtils;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * From http://docs.jsonata.org/string-functions.html:
 *
 Signature: $eval(expr [, context])

 Parses and evaluates the string expr which contains literal JSON or a JSONata expression using the current context as the context for evaluation.

 $eval("[1,2,3]") 			= [1, 2, 3]
 $eval('[1,$string(2),3]') 	= [1,"2",3]
 */
public class EvalFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_STRING);
    public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_STRING);
    public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_STR_OR_EXPR, Constants.FUNCTION_STRING);

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
        // Create the variable to return
        JsonNode result = null;

        JsonNode context = null;
        boolean useContext = FunctionUtils.useContextVariable(this, ctx, getSignature());
        int argCount = getArgumentCount(ctx);
        if (useContext) {
            context = FunctionUtils.getContextVariable(expressionVisitor);
            // $string only reads context if no parameters are passed and can print NullNodes
            if (context != null && argCount == 0) {
                argCount++;
            } else {
                useContext = false;
            }
        }

        String expression = "{}";
        if (argCount >= 1 && argCount <= 2) {
            if (!useContext) {
                JsonNode arg = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, 0);
                if (arg != null && arg.isTextual()) {
                    expression = arg.asText();
                }
            }
            if (argCount == 2) {
                context = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, useContext ? 0 : 1);
            }
            try {
                Expression expr = Expression.jsonata(expression);
                result = expr.evaluate(context);
            } catch (ParseException | IOException e) {
                throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
            }
        } else {
            if (argCount == 0) {
                return null;
            }
            throw new EvaluateRuntimeException(ERR_ARG2BADTYPE);
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
        // accepts any value or context, an optional boolean, and returns a string
        return "<x-b?:s>";
    }
}
