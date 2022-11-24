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

public class ShuffleFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_SHUFFLE);
    public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_SHUFFLE);
    public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_SHUFFLE);
    public static String ERR_ARG1_MUST_BE_ARRAY = String.format(Constants.ERR_MSG_ARG1_MUST_BE_ARRAY,
        Constants.FUNCTION_SHUFFLE);

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
        ArrayNode result = JsonNodeFactory.instance.arrayNode();

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
            // if arg is an array, return its length. Any other type of
            // input returns 1.
            if (argArray == null || argArray.isNull()) {
                if (useContext == true) {
                    throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
                } else {
                    return null;
                }
            } else if (argArray.isArray()) {
                ArrayNode array = (ArrayNode) argArray;
                result = JsonNodeFactory.instance.arrayNode(array.size());
                for (int i = 0; i < array.size(); i++) {
                    result.add(0); // simulating initializing in JavaScript
                }
                for (int i = 0; i < array.size(); i++) {
                    int j = Double.valueOf(Math.floor(Math.random() * (i + 1))).intValue();
                    if (i != j) {
                        result.set(i, result.get(j));
                    }
                    result.set(j, array.get(i));
                }
            } else {
                result.add(argArray);
                return result;
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
        return 1;
    }

    @Override
    public String getSignature() {
        // accepts array, returns an array
        return "<a:a>";
    }

}
