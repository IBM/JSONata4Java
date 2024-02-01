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

import java.util.regex.Pattern;
import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.api.jsonata4java.expressions.ExpressionsVisitor;
import com.api.jsonata4java.expressions.RegularExpression;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.utils.Constants;
import com.api.jsonata4java.expressions.utils.FunctionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.POJONode;

/**
 * From http://docs.jsonata.org/string-functions.html:
 * 
 * $split(str, separator [, limit])
 * 
 * Splits the str parameter into an array of substrings. If str is not
 * specified, then the context value is used as the value of str. It is an error
 * if str is not a string.
 * 
 * The separator parameter can either be a string or a regular expression
 * (regex). If it is a string, it specifies the characters within str about
 * which it should be split. If it is the empty string, str will be split into
 * an array of single characters. If it is a regex, it splits the string around
 * any sequence of characters that match the regex.
 * 
 * The optional limit parameter is a number that specifies the maximum number of
 * substrings to include in the resultant array. Any additional substrings are
 * discarded. If limit is not specified, then str is fully split with no limit
 * to the size of the resultant array. It is an error if limit is not a
 * non-negative number.
 * 
 * Examples
 * 
 * $split("so many words", " ")==[ "so", "many", "words" ] $split("so many
 * words", " ", 2)==[ "so", "many" ] $split("too much, punctuation. hard; to
 * read", "[ ,.;]+")==["too", "much", "punctuation", "hard", "to", "read"]
 * 
 */
public class SplitFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String
        .format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_SPLIT);
    public static String ERR_ARG1BADTYPE = String
        .format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_SPLIT);
    public static String ERR_ARG2BADTYPE = String
        .format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_SPLIT);
    public static String ERR_ARG3BADTYPE = String
        .format(Constants.ERR_MSG_ARG3_BAD_TYPE, Constants.FUNCTION_SPLIT);
    public static String ERR_ARG4BADTYPE = String
        .format(Constants.ERR_MSG_ARG4_BAD_TYPE, Constants.FUNCTION_SPLIT);

    public JsonNode invoke(ExpressionsVisitor expressionVisitor,
        Function_callContext ctx) {
        // Create the variable to return
        JsonNode result = null;

        // Retrieve the number of arguments
        JsonNode argString = JsonNodeFactory.instance.nullNode();
        boolean useContext = FunctionUtils.useContextVariable(this, ctx,
            getSignature());
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
                argString = FunctionUtils
                    .getValuesListExpression(expressionVisitor, ctx, 0);
            }
            if (argCount < 2) {
                if (argString == null || argString.isTextual()) {
                    throw new EvaluateRuntimeException(ERR_BAD_CONTEXT);
                }
                throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
            }
            JsonNode argSeparator = null;
            ExprContext exprCtx = ctx.exprValues().exprList()
                .expr(useContext ? 0 : 1);
            if ("$".equals(exprCtx.getChild(0).toString())) {
                argSeparator = FunctionUtils.getValuesListExpression(
                    expressionVisitor, ctx, useContext ? 1 : 2);
                if (useContext) {
                    // since we used context the $ reference is superfluous
                    argCount--;
                }
            } else {
                // check to see if we got a textual reference
                argSeparator = FunctionUtils.getValuesListExpression(
                    expressionVisitor, ctx, useContext ? 0 : 1);
            }
            int limit = -1; // assume unlimited
            // Make sure that the separator is not null
            if (argSeparator == null || !(argSeparator.isTextual()
                || argSeparator instanceof POJONode)) {
                if (argString == null) {
                    if (useContext) {
                        throw new EvaluateRuntimeException(ERR_BAD_CONTEXT);
                    }
                    throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
                }
                throw new EvaluateRuntimeException(ERR_ARG2BADTYPE);
            }
            if (argString == null) {
                return null;
            }
            if (!argString.isTextual()) {
                throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
            }
            // Check to see if the separator is just a string
            final String str = argString.textValue();
            final RegularExpression regex = argSeparator instanceof POJONode
                ? (RegularExpression) ((POJONode) argSeparator).getPojo()
                : null;
            final String separator = regex != null ? regex.toString()
                : argSeparator.textValue();

            if (argCount == 3) {
                final JsonNode argLimit = FunctionUtils.getValuesListExpression(
                    expressionVisitor, ctx, useContext ? 1 : 2);

                // Check to see if we have an optional limit argument we check
                // it
                if (argLimit != null) {
                    if (argLimit.isNumber() && argLimit.asInt() >= 0) {
                        limit = argLimit.asInt();
                    } else {
                        throw new EvaluateRuntimeException(ERR_ARG3BADTYPE);
                    }
                }
            }

            /*
             * Split the string using a simple String::split... but do not
             * specify the limit. This is because the String::split function in
             * Java behaves differently to the JavaScript String::split
             * function... and the JSONata $split function is defined to behave
             * like the JavaScript version.
             * 
             * If the limit is zero, we do not add any strings in the output
             * array to the ArrayNode object.
             * 
             * If the limit is non-zero, we add up to the specified number of
             * strings to the ArrayNode object.
             */
            result = JsonNodeFactory.instance.arrayNode();
            if (!str.isEmpty()) {
                String[] items;
                if (regex != null) {
                    items = regex.getPattern().split(str);
                } else {
                    items = str.split(Pattern.quote(separator));
                }
                for (int i = 0; i < items.length; i++) {
                    if (limit == -1 || i < limit) {
                        ((ArrayNode) result).add(items[i]);
                    } else {
                        break;
                    }
                } // FOR
            }
        } else {
            throw new EvaluateRuntimeException(
                argCount == 0 ? ERR_BAD_CONTEXT : ERR_ARG4BADTYPE);
        }

        return result;
    }

    @Override
    public int getMaxArgs() {
        return 3;
    }

    @Override
    public int getMinArgs() {
        return 1; // account for context variable
    }

    @Override
    public String getSignature() {
        // accepts a string (or context variable), a string or function, an
        // optional
        // number, returns an array of strings
        return "<s-(sf)n?:a<s>>";
    }
}
