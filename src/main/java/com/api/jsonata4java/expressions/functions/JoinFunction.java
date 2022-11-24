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

import java.util.Iterator;
import java.util.StringJoiner;
import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.api.jsonata4java.expressions.ExpressionsVisitor;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.utils.Constants;
import com.api.jsonata4java.expressions.utils.FunctionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * From http://docs.jsonata.org/string-functions.html:
 * 
 * $join(array[, separator])
 * 
 * Joins an array of component strings into a single concatenated string with
 * each component string separated by the optional separator parameter.
 * 
 * It is an error if the input array contains an item which isn’t a string.
 * 
 * If separator is not specified, then it is assumed to be the empty string,
 * i.e. no separator between the component strings. It is an error if separator
 * is not a string.
 * 
 * Examples
 * 
 * $join(['a','b','c'])=="abc" $split("too much, punctuation. hard; to read",
 * /[ ,.;]+/, 3) ~&GT; $join(', ')=="too, much, punctuation"
 * 
 */
public class JoinFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_JOIN);
    public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_JOIN);
    public static String ERR_ARG3BADTYPE = String.format(Constants.ERR_MSG_ARG3_BAD_TYPE, Constants.FUNCTION_JOIN);
    public static String ERR_MSG_ARG1_ARR_STR = String.format(Constants.ERR_MSG_ARG1_ARR_STR, Constants.FUNCTION_JOIN);

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
        // Create the variable to return
        JsonNode result = null;

        // Retrieve the number of arguments
        JsonNode argArray = null;
        boolean useContext = FunctionUtils.useContextVariable(this, ctx, getSignature());
        int argCount = getArgumentCount(ctx);
        if (useContext) {
            argArray = FunctionUtils.getContextVariable(expressionVisitor);
            if (argArray != null && argArray.isNull() == false) {
                argCount++;
            } else {
                useContext = false;
            }
        }

        // Make sure that we have the right number of arguments
        if (argCount == 1 || argCount == 2) {
            if (!useContext) {
                argArray = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, 0);
            }
            if (argArray == null) {
                return null;
            }
            if (!argArray.isArray()) {
                if (!argArray.isTextual()) {
                    throw new EvaluateRuntimeException(ERR_MSG_ARG1_ARR_STR);
                }
                ArrayNode newArray = JsonNodeFactory.instance.arrayNode();
                newArray.add(argArray);
                argArray = newArray;
            }

            // Read the separator argument, if present
            String separator = "";
            if (argCount == 2) {
                final JsonNode argSeparator = FunctionUtils.getValuesListExpression(expressionVisitor, ctx,
                    useContext ? 0 : 1);
                if (argSeparator != null) {
                    if (argSeparator.isTextual()) {
                        separator = argSeparator.textValue();
                    } else {
                        throw new EvaluateRuntimeException(ERR_ARG2BADTYPE);
                    }
                }
            }

            // Join the elements of the array argument
            StringJoiner stringJoiner = new StringJoiner(separator);
            Iterator<JsonNode> elements = ((ArrayNode) argArray).elements();
            while (elements.hasNext()) {
                JsonNode element = elements.next();
                if (element.isTextual()) {
                    stringJoiner.add(element.asText());
                } else if (element.isArray()) {
                    for (Iterator<JsonNode> it = ((ArrayNode) element).iterator(); it.hasNext();) {
                        stringJoiner.add(it.next().textValue());
                    }
                } else {
                    throw new EvaluateRuntimeException(ERR_MSG_ARG1_ARR_STR);
                }
            } // WHILE

            // Create the result from the joined string
            result = new TextNode(stringJoiner.toString());
        } else {
            if (argCount != 0 && argArray == null) {
                return null;
            }
            throw new EvaluateRuntimeException(
                argCount == 0 ? ERR_BAD_CONTEXT : argCount == 1 ? ERR_MSG_ARG1_ARR_STR : ERR_ARG3BADTYPE);
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
        // accepts an array of strings, an optional string, returns a string
        return "<a<s>s?:s>";
    }
}
