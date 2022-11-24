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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.api.jsonata4java.expressions.ExpressionsVisitor;
import com.api.jsonata4java.expressions.ExpressionsVisitor.SelectorArrayNode;
import com.api.jsonata4java.expressions.RegularExpression;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.utils.Constants;
import com.api.jsonata4java.expressions.utils.FunctionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.POJONode;

/**
 * From http://docs.jsonata.org/string-functions.html:
 * 
 * $match(str, pattern [, limit])
 * 
 * Finds occurrences of pattern within str and reports them with their index and
 * group.
 * 
 * If str is not specified, then the context value is used as the value of str.
 * It is an error if str is not a string.
 * 
 * The pattern parameter must be a regular expression (regex) String used for
 * searching the string.
 * 
 * The optional limit parameter, is a number that specifies the maximum number
 * of results to be found before stopping.
 * 
 * Examples
 * 
 * $match("John Smith and John Jones", "John") ==
 * "[{"match":"John","index":0,"groups":["John"]},{"match":"John","index":15,"groups":["John"]}]
 * 
 */
public class MatchFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_MATCH);
    public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_MATCH);
    public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_MATCH);
    public static String ERR_ARG3BADTYPE = String.format(Constants.ERR_MSG_ARG3_BAD_TYPE, Constants.FUNCTION_MATCH);
    public static String ERR_ARG4BADTYPE = String.format(Constants.ERR_MSG_ARG4_BAD_TYPE, Constants.FUNCTION_MATCH);

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
        // Create the variable to return
        SelectorArrayNode result = null;

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
        if (argCount == 2 || argCount == 3) {
            if (!useContext) {
                argString = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, 0);
            }
            final JsonNode argPattern = FunctionUtils.getValuesListExpression(expressionVisitor, ctx,
                useContext ? 0 : 1);
            int limit = -1;
            // Make sure that we have the right number of arguments
            if (argString == null || !argString.isTextual() || argString.asText().isEmpty()) {
                throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
            }
            // Make sure that the pattern is a non-empty string
            if (argPattern != null && (argPattern.isTextual() || argPattern instanceof POJONode) && !(argPattern.asText().isEmpty())) {
                RegularExpression regex = null;
                if (argPattern instanceof POJONode) {
                    regex = (RegularExpression) ((POJONode) argPattern).getPojo();
                }
                // final String patternText = regex != null ? regex.toString() : Pattern.quote(argPattern.textValue());
                Pattern regexPattern;
                if (regex != null) {
                    regexPattern = regex.getPattern();
                } else {
                    regexPattern = Pattern.compile(Pattern.quote(argPattern.textValue()));
                }
                // Check to see if the separator is just a string
                final String str = argString.textValue();

                if (argCount == 3) {
                    final JsonNode argLimit = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, useContext ? 1 : 2);

                    // Check to see if we have an optional limit argument we check it
                    if (argLimit != null) {
                        if (argLimit.isNumber() && argLimit.asInt() >= 0) {
                            limit = argLimit.asInt();
                        } else {
                            throw new EvaluateRuntimeException(ERR_ARG3BADTYPE);
                        }
                    }
                }

                final Matcher matcher = regexPattern.matcher(str);

                // Check to see if a limit was specified
                result = new SelectorArrayNode(JsonNodeFactory.instance);
                if (limit == -1) {
                    // No limits... match all occurrences in the string
                    while (matcher.find()) {
                        final ObjectNode obj = JsonNodeFactory.instance.objectNode();
                        obj.put("match", str.substring(matcher.start(), matcher.end()));
                        obj.put("index", Long.valueOf(matcher.start()));
                        final ArrayNode groups = JsonNodeFactory.instance.arrayNode();
                        obj.set("groups", groups);
                        final int groupCount = matcher.groupCount();
                        for (int i = 1; i <= groupCount; i++) {
                            groups.add(matcher.group(i));
                        }
                        result.add(obj);
                    }
                } else if (limit > 0) {
                    int count = 0;
                    while (matcher.find() && count < limit) {
                        count++;
                        final ObjectNode obj = JsonNodeFactory.instance.objectNode();
                        obj.put("match", str.substring(matcher.start(), matcher.end()));
                        obj.put("index", Long.valueOf(matcher.start()));
                        final ArrayNode groups = JsonNodeFactory.instance.arrayNode();
                        obj.set("groups", groups);
                        final int groupCount = matcher.groupCount();
                        for (int i = 1; i <= groupCount; i++) {
                            groups.add(matcher.group(i));
                        }
                        result.add(obj);
                    }
                } else {
                    return null;
                }
            } else {
                // check for a function call context as 2nd parameter
                ExprContext exprCtx = ctx.exprValues().exprList().expr(useContext ? 0 : 1);
                if (exprCtx instanceof Function_callContext) {
                    // try for declared then jsonata
                    Function_callContext fctCallCtx = (Function_callContext) exprCtx;
                    String fctName = fctCallCtx.VAR_ID().getText();
                    DeclaredFunction declFct = expressionVisitor.getDeclaredFunction(fctName);
                    if (declFct == null) {
                        FunctionBase function = expressionVisitor.getJsonataFunction(fctName);
                        if (function != null) {
                            result = (SelectorArrayNode) function.invoke(expressionVisitor, fctCallCtx);
                        } else {
                            throw new EvaluateRuntimeException("Unknown function: " + fctName);
                        }
                    } else {
                        result = (SelectorArrayNode) declFct.invoke(expressionVisitor, fctCallCtx);
                    }
                } else {
                    throw new EvaluateRuntimeException(ERR_ARG2BADTYPE);
                }
            }
        } else {
            throw new EvaluateRuntimeException(argCount <= 1 ? ERR_ARG1BADTYPE : ERR_ARG4BADTYPE);
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
        // accepts a string (or context variable), a function (that accepts a string and
        // returns an object), an optional number, returns an array of objects
        return "<s-f<s:o>n?:a<o>>";
    }
}
