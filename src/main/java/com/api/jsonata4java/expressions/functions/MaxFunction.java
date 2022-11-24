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
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.utils.Constants;
import com.api.jsonata4java.expressions.utils.FunctionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

/**
 * From http://docs.jsonata.org/aggregation-functions.html
 * 
 * $max(array)
 * 
 * Returns the maximum number in an array of numbers. It is an error if the
 * input array contains an item which isn’t a number.
 * 
 * Example
 * 
 * $max([5,1,3,7,4])==7
 * 
 */
public class MaxFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_MAX);
    public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_MAX);
    public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_MAX);
    public static final String ERR_ARG_TYPE = String.format(Constants.ERR_MSG_ARG1_MUST_BE_ARRAY_OF_NUMBER,
        Constants.FUNCTION_MAX);

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
        // Create the variable to return
        JsonNode result = null;

        // Retrieve the number of arguments
        JsonNode argArray = JsonNodeFactory.instance.nullNode();
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
        if (argCount == 1) {
            if (!useContext) {
                argArray = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, 0);
            }
            if (argArray == null) {
                return null;
            }
            if (argArray.isArray() == false) {
                argArray = ExpressionsVisitor.ensureArray(argArray);
            }
            // Convert the input node to an ArrayNode and make check that the
            // array is not empty
            ArrayNode items = (ArrayNode) argArray;
            if (items.size() > 0) {
                JsonNode max = null;
                for (JsonNode item : items) {
                    if (item.isNumber()) {
                        // Check whether the current item is more than the
                        // current max
                        if (max == null || item.asDouble() > max.asDouble()) {
                            max = item;
                        }
                    } else {
                        /*
                         * The input array contains an item that is not a number. Throw a suitable
                         * exception
                         */
                        throw new EvaluateRuntimeException(ERR_ARG_TYPE);
                    }
                } // FOR

                // Return the node that represents the maximum value
                result = max;
            } else {
                /*
                 * The input array is empty. Single with undefined
                 */
                // throw new EvaluateRuntimeException(ERR_ARG_TYPE);
                return null;
            }
        } else {
            throw new EvaluateRuntimeException(argCount == 0 ? ERR_ARG1BADTYPE : ERR_ARG2BADTYPE);
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
        // accepts an array of numbers, returns a number
        return "<a<n>:n>";
    }
}
