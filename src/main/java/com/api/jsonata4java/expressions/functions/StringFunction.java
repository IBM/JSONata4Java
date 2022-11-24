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
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Var_recallContext;
import com.api.jsonata4java.expressions.utils.BooleanUtils;
import com.api.jsonata4java.expressions.utils.Constants;
import com.api.jsonata4java.expressions.utils.FunctionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * From http://docs.jsonata.org/string-functions.html:
 * 
 * $string(arg)
 * 
 * Casts the arg parameter to a string using the following casting rules
 * 
 * Strings are unchanged Functions are converted to an empty string Numeric
 * infinity and NaN throw an error because they cannot be represented as a JSON
 * number All other values are converted to a JSON string using the
 * JSON.stringify function If arg is not specified (i.e. this function is
 * invoked with no arguments), then the context value is used as the value of
 * arg.
 * 
 * Examples
 * 
 * $string(5)=="5" [1..5].$string()==["1", "2", "3", "4", "5"]
 * $string([1..5])=="[1,2,3,4,5]"
 */
public class StringFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_STRING);
    public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_STRING);

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
        // Create the variable to return
        JsonNode result = null;

        // Retrieve the number of arguments
        JsonNode arg = JsonNodeFactory.instance.nullNode();
        boolean useContext = FunctionUtils.useContextVariable(this, ctx, getSignature());
        int argCount = getArgumentCount(ctx);
        if (useContext) {
            arg = FunctionUtils.getContextVariable(expressionVisitor);
            // $string only reads context if no parameters are passed and can print NullNodes
            if (arg != null && argCount == 0) {
                argCount++;
            } else {
                useContext = false;
            }
        }

        // Make sure that we have the right number of arguments
        if (argCount >= 1 && argCount <= 2) {
            if (!useContext) {
                /**
                 * need to peek at the expression context since Function_callContext evaluates
                 * to ""
                 */
                ExprContext exprCtx = ctx.exprValues().exprList().expr(0);
                arg = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, 0);
                if (arg == null) {
                    if (exprCtx instanceof Function_callContext || exprCtx instanceof Function_declContext) {
                        arg = new TextNode("");
                    }
                    if (exprCtx instanceof Var_recallContext) {
                        String varName = ((Var_recallContext) exprCtx).VAR_ID().getText();
                        DeclaredFunction declFct = expressionVisitor.getDeclaredFunction(varName);
                        if (declFct != null) {
                            arg = new TextNode("");
                        } else {
                            FunctionBase fct = expressionVisitor.getJsonataFunction(varName);
                            if (fct != null) {
                                arg = new TextNode("");
                            } else {
                                arg = null;
                            }
                        }
                    }
                }
            }
            if (arg == null || (arg.isNull() && useContext)) {
                return null;
            }
            boolean prettify = false;
            if (argCount == 2) {
                JsonNode arg2 = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, useContext ? 0 : 1);
                if (arg2 != null && arg2.isBoolean()) {
                    prettify = BooleanUtils.convertJsonNodeToBoolean(arg2);
                } else {
                    throw new EvaluateRuntimeException(ERR_ARG2BADTYPE);
                }
            }
            String asString = ExpressionsVisitor.castString(arg, prettify);
            if (asString == null) {
                result = null;
            } else {
                result = new TextNode(asString);
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
