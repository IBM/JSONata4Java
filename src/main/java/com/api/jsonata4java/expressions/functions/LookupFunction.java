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
import com.api.jsonata4java.expressions.ExpressionsVisitor.SelectorArrayNode;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.utils.Constants;
import com.api.jsonata4java.expressions.utils.FunctionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * From http://docs.jsonata.org/object-functions.html
 * 
 * $lookup(object,key)
 * 
 * Returns the value corresponding to the key in the supplied object. It is an
 * error if the input is not an object, and if the key is not a String.
 * 
 * Example
 * 
 * $lookup({"a":1,"b":2},"b")==2
 * 
 */
public class LookupFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_LOOKUP);
    public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_LOOKUP);
    public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_LOOKUP);
    public static String ERR_ARG3BADTYPE = String.format(Constants.ERR_MSG_ARG3_BAD_TYPE, Constants.FUNCTION_LOOKUP);

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
        // Create the variable to return
        JsonNode result = null;

        // Retrieve the number of arguments
        JsonNode argObject = JsonNodeFactory.instance.nullNode();
        boolean useContext = FunctionUtils.useContextVariable(this, ctx, getSignature());
        int argCount = getArgumentCount(ctx);
        if (useContext) {
            argObject = FunctionUtils.getContextVariable(expressionVisitor);
            if (argObject != null && argObject.isNull() == false) {
                argCount++;
            } else {
                useContext = false;
            }
        }

        // Make sure that we have the right number of arguments
        if (argCount >= 1 && argCount <= 3) {
            if (!useContext) {
                argObject = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, 0);
            }
            if (argObject == null) {
                return null;
            }
            if (argCount == 2) {
                JsonNode keyObj = FunctionUtils.getValuesListExpression(expressionVisitor, ctx,
                    useContext ? 0 : 1);
                if (keyObj == null || !keyObj.isTextual()) {
                    throw new EvaluateRuntimeException(ERR_ARG2BADTYPE);
                }
                final String key = keyObj.asText();
                SelectorArrayNode array = new SelectorArrayNode(JsonNodeFactory.instance);
                // Check the type of the argument
                if (argObject.isObject()) {
                    ObjectNode obj = (ObjectNode) argObject;
                    captureKeyValues(obj, key, array);
                    if (array.size() != 1) {
                        result = array;
                    } else {
                        // make singleton
                        result = array.get(0);
                    }
                } else {
                    if (argObject.isArray()) {
                        findObjects((ArrayNode) argObject, key, array);
                        if (array.size() == 0) {
                            result = null;
                        } else if (array.size() != 1) {
                            result = array;
                        } else {
                            // make singleton
                            result = array.get(0);
                        }
                    } else {
                        /*
                         * The input argument is not an array. Throw a suitable exception
                         */
                        throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
                    }
                }
            } else {
                throw new EvaluateRuntimeException(ERR_ARG2BADTYPE);
            }
        } else {
            throw new EvaluateRuntimeException(argCount == 0 ? ERR_BAD_CONTEXT : ERR_ARG3BADTYPE);
        }

        if (result != null && result.isArray() && result.size() == 0) {
            result = null;
        }
        return result;
    }

    static void findObjects(ArrayNode array, String key, ArrayNode result) {
        for (int i = 0; i < array.size(); i++) {
            JsonNode arrayNode = array.get(i);
            if (arrayNode != null) {
                if (arrayNode.isArray()) {
                    ArrayNode subResult = new ArrayNode(JsonNodeFactory.instance);
                    findObjects((ArrayNode) arrayNode, key, subResult);
                    // if (subResult.size() != 0) {
                    result.add(subResult);
                    // }
                } else if (arrayNode.isObject()) {
                    captureKeyValues((ObjectNode) arrayNode, key, result);
                }
            }
        }
    }

    static void captureKeyValues(ObjectNode obj, String key, ArrayNode result) {
        JsonNode value = obj.get(key);
        if (value != null) {
            result.add(value);
        }
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
        // accepts an anything (or context variable), a string, returns anything
        return "<x-s:x>";
    }
}
