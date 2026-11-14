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
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_declContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Var_recallContext;
import com.api.jsonata4java.expressions.utils.Constants;
import com.api.jsonata4java.expressions.utils.FunctionUtils;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.JsonNodeFactory;
import tools.jackson.databind.node.StringNode;

/**
 * From http://docs.jsonata.org/string-functions.html:
 * 
 * $substringAfter(str, chars)
 * 
 * Returns the substring after the first occurrence of the character sequence
 * chars in str. If str is not specified (i.e. this function is invoked with
 * only one argument), then the context value is used as the value of str. If
 * str does not contain chars, then it returns str. An error is thrown if str
 * and chars are not strings.
 * 
 * Examples
 * 
 * $substringAfter("Hello World", " ")=="World"
 * 
 */
public class SubstringAfterFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT,
        Constants.FUNCTION_SUBSTRING_AFTER);
    public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE,
        Constants.FUNCTION_SUBSTRING_AFTER);
    public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE,
        Constants.FUNCTION_SUBSTRING_AFTER);
    public static String ERR_ARG3BADTYPE = String.format(Constants.ERR_MSG_ARG3_BAD_TYPE,
        Constants.FUNCTION_SUBSTRING_AFTER);

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
        // Create the variable to return
        JsonNode result = null;

        // Retrieve the number of arguments
        JsonNode argString = JsonNodeFactory.instance.nullNode();
        boolean useContext = FunctionUtils.useContextVariable(this, ctx, getSignature());
        int argCount = getArgumentCount(ctx);
        if (useContext) {
            argString = FunctionUtils.getContextVariable(expressionVisitor);
            if (argString != null && argCount <= 1) {
                argCount++;
            } else {
                useContext = false;
            }
        }

        // Make sure that we have the right number of arguments
        if (argCount == 1 || argCount == 2) {
            if (!useContext) {
                /**
                 * need to peek at the expression context since Function_callContext evaluates
                 * to ""
                 */
                ExprContext exprCtx = ctx.exprValues().exprList().expr(0);
                argString = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, 0);
                if (argString == null) {
                    if (exprCtx instanceof Function_callContext || exprCtx instanceof Function_declContext) {
                        argString = new StringNode("");
                    }
                    if (exprCtx instanceof Var_recallContext) {
                        String varName = ((Var_recallContext) exprCtx).VAR_ID().getText();
                        DeclaredFunction declFct = expressionVisitor.getDeclaredFunction(varName);
                        if (declFct != null) {
                            argString = new StringNode("");
                        } else {
                            FunctionBase fct = expressionVisitor.getJsonataFunction(varName);
                            if (fct != null) {
                                argString = new StringNode("");
                            } else {
                                argString = null;
                            }
                        }
                    }
                }
            }
            if (argString == null || (argString.isNull() && useContext)) {
                return null;
            }
            if (argCount == 1) {
                if (argString == null || argString.isTextual()) {
                    return null; // throw new EvaluateRuntimeException(ERR_BAD_CONTEXT);
                }
                throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
            }
            // else argCount == 2
            JsonNode argChars = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, useContext ? 0 : 1);
            // check validity of 2nd argument first
            if (argChars == null) {
                if (argString.isTextual()) {
                    // just return the string value of argString
                    return new StringNode(argString.textValue());
                } else {
                    // invalid argString
                    throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
                }
            }
            if (argString != null) {
                if (!argString.isTextual()) {
                    throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
                }
                if (!argChars.isTextual()) {
                    throw new EvaluateRuntimeException(ERR_ARG2BADTYPE);
                }
                final String str = argString.textValue();
                final String chars = argChars.textValue();

                // Find chars in str
                final int index = str.indexOf(chars);
                if (index != -1) {
                    int utf16After = index + chars.length();
                    if (utf16After < str.length()) {
                        int cpStart = str.codePointCount(0, utf16After);
                        result = new StringNode(JSONataUtils.substr(str, cpStart, null));
                    } else {
                        result = new StringNode("");
                    }
                } else {
                    // argChars is not present... just return argString
                    result = new StringNode(str);
                }
            }
        } else {
            throw new EvaluateRuntimeException(argCount == 0 ? ERR_ARG1BADTYPE : ERR_ARG3BADTYPE);
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
        // accepts a string (or context variable), a string, returns a string
        return "<s-s:s>";
    }
}
