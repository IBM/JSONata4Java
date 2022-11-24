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

import java.util.List;
import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.api.jsonata4java.expressions.ExpressionsVisitor;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprListContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprValuesContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Var_recallContext;
import com.api.jsonata4java.expressions.utils.Constants;
import com.api.jsonata4java.expressions.utils.FunctionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * From http://docs.jsonata.org/string-functions.html:
 * 
 * $trim(str)
 * 
 * Normalizes and trims all whitespace characters in str by applying the
 * following steps:
 * 
 * * All tabs, carriage returns, and line feeds are replaced with spaces. *
 * Contiguous sequences of spaces are reduced to a single space. * Trailing and
 * leading spaces are removed.
 * 
 * If str is not specified (i.e. this function is invoked with no arguments),
 * then the context value is used as the value of str. An error is thrown if str
 * is not a string.
 * 
 * Examples
 * 
 * $trim(" Hello \n World ")=="Hello World"
 *
 */
public class TypeFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_TYPE);
    public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_TYPE);
    public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_TYPE);

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
        // Create the variable to return
        JsonNode result = null;

        // Retrieve the number of arguments
        JsonNode arg = JsonNodeFactory.instance.nullNode();
        boolean useContext = FunctionUtils.useContextVariable(this, ctx, getSignature());
        int argCount = getArgumentCount(ctx);
        if (useContext) {
            arg = FunctionUtils.getContextVariable(expressionVisitor);
            if (arg != null && arg.isNull() == false) {
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
            // test for functions first
            ExprValuesContext valuesCtx = ctx.exprValues();
            ExprListContext exprList = valuesCtx.exprList();
            List<ExprContext> exprCtxList = exprList.expr();
            if (exprCtxList.size() > 0) {
                ExprContext exprCtx = exprCtxList.get(0);
                if (exprCtx instanceof Function_callContext) {
                    result = new TextNode("function");
                } else if (exprCtx instanceof Var_recallContext) {
                    if (exprCtx.getChildCount() > 0) {
                        String varID = exprCtx.getChild(0).getText();
                        // determine what this references
                        DeclaredFunction declFct = expressionVisitor.getDeclaredFunction(varID);
                        if (declFct != null) {
                            result = new TextNode("function");
                        } else {
                            FunctionBase fct = expressionVisitor.getJsonataFunction(varID);
                            if (fct != null) {
                                result = new TextNode("function");
                            } else {
                                arg = expressionVisitor.getVariable(varID);
                            }
                        }
                    }
                }
            }
            if (result == null) {
                if (arg != null) {
                    switch (arg.getNodeType()) {
                        case ARRAY: {
                            result = new TextNode("array");
                            break;
                        }
                        case OBJECT: {
                            result = new TextNode("object");
                            break;
                        }
                        case STRING: {
                            result = new TextNode("string");
                            break;
                        }
                        case NUMBER: {
                            result = new TextNode("number");
                            break;
                        }
                        case BOOLEAN: {
                            result = new TextNode("boolean");
                            break;
                        }
                        case BINARY: {
                            break;
                        }
                        case NULL: {
                            result = new TextNode("null");
                            break;
                        }
                        case POJO: {
                            result = new TextNode("undefined");
                            break;
                        }
                        case MISSING: {
                            result = new TextNode("undefined");
                            break;
                        }
                        default: {
                            result = new TextNode("undefined");
                            break;
                        }
                    }
                } else {
                    result = null;
                }
            }
        } else {
            if (argCount != 0) {
                throw new EvaluateRuntimeException(argCount == 0 ? ERR_BAD_CONTEXT : ERR_ARG2BADTYPE);
            } // else returns null
        }

        return result;
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
        // accepts anything, returns a number
        return "<x:n>";
    }
}
