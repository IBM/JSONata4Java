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
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_declContext;
import com.api.jsonata4java.expressions.utils.BooleanUtils;
import com.api.jsonata4java.expressions.utils.Constants;
import com.api.jsonata4java.expressions.utils.FunctionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

/**
 * From http://docs.jsonata.org/boolean-functions.html
 * 
 * $boolean(arg)
 * 
 * Casts the argument to a Boolean using the following rules:
 * 
 * Boolean: unchanged string: empty false string: non-empty true number: 0 false
 * number: non-zero true null: false array: empty false array: contains a member
 * that casts to true true array: all members cast to false false object: empty
 * false object: non-empty true function: (functions are not currently
 * supported) false
 * 
 */
public class BooleanFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_BOOLEAN);
    public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_BOOLEAN);

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
        // Create the variable to return
        JsonNode result = null;

        // Retrieve the number of arguments
        JsonNode arg = JsonNodeFactory.instance.nullNode();
        boolean useContext = FunctionUtils.useContextVariable(this, ctx, getSignature());
        int argCount = getArgumentCount(ctx);
        if (useContext) {
            arg = FunctionUtils.getContextVariable(expressionVisitor);
            if (arg != null && (arg.isNull() == false || argCount == 0)) {
                argCount++;
            } else {
                useContext = false;
            }
        }
        if (argCount == 0 && arg == null) {
            return null;
        }

        // Make sure that we have the right number of arguments
        if (argCount == 1) {
            if (!useContext) {
                arg = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, 0);
            }
            if (arg != null) {
                result = BooleanUtils.convertJsonNodeToBoolean(arg) ? BooleanNode.TRUE : BooleanNode.FALSE;
            } else {
                // special test to see if there is a function that exists with this name
                ExprContext exprCtx = ctx.exprValues().exprList().expr(0);
                if (exprCtx instanceof Function_declContext) {
                    result = BooleanNode.FALSE;
                } else if (exprCtx != null) {
                    String functionName = exprCtx.getText();
                    if (expressionVisitor.getDeclaredFunction(functionName) != null) {
                        result = BooleanNode.FALSE;
                    } else if (expressionVisitor.getJsonataFunction(functionName) != null) {
                        result = BooleanNode.FALSE;
                    }
                }
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
        // accepts anything (or context variable), returns a boolean
        return "<x-:b>";
    }
}
