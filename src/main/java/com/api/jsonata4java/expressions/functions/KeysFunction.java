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
 * $keys(object)
 * 
 * Returns the keys in an array of Strings. It is an error if the input is not
 * an object.
 * 
 * Example
 * 
 * $keys({"a":1,"b":2})==["a", "b"]
 * 
 */
public class KeysFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_KEYS);
    public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_KEYS);
    public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_KEYS);

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
        // Create the variable to return
        ObjectNode result = new ObjectNode(JsonNodeFactory.instance);

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
        if (argCount == 1) {
            if (!useContext) {
                argObject = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, 0);
            }
            if (argObject == null) {
                return null;
            }
            // Check the type of the argument
            String key = "";
            if (argObject.isObject()) {
                ObjectNode obj = (ObjectNode) argObject;
                for (Iterator<String> it = obj.fieldNames(); it.hasNext();) {
                    key = it.next();
                    if (result.get(key) == null) {
                        result.put(key, true);
                    }
                }
            } else {
                if (argObject.isArray()) {
                    findObjects((ArrayNode) argObject, result);
                } else {
                    /*
                     * The input argument is not an array. Throw a suitable exception
                     */
                    return null;
                    // throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
                }
            }
        } else {
            throw new EvaluateRuntimeException(argCount == 0 ? ERR_BAD_CONTEXT : ERR_ARG2BADTYPE);
        }
        if (result.size() == 0) {
            return null;
        }
        SelectorArrayNode output = new SelectorArrayNode(JsonNodeFactory.instance);
        for (Iterator<String> it = result.fieldNames(); it.hasNext();) {
            output.add(it.next());
        }
        return ExpressionsVisitor.unwrapArray(output);
    }

    static void findObjects(ArrayNode array, ObjectNode result) {
        for (int i = 0; i < array.size(); i++) {
            JsonNode arrayNode = array.get(i);
            if (arrayNode != null) {
                if (arrayNode.isArray()) {
                    // Not implemented in jsonata.js
                    // findObjects((ArrayNode)arrayNode,result);
                } else if (arrayNode.isObject()) {
                    captureKeys((ObjectNode) arrayNode, result);
                }
            }
        }
    }

    static void captureKeys(ObjectNode argObject, ObjectNode result) {
        //		JsonNode value = null;
        String key = null;
        ObjectNode obj = (ObjectNode) argObject;
        for (Iterator<String> it = obj.fieldNames(); it.hasNext();) {
            key = it.next();
            if (result.get(key) == null) {
                result.put(key, true);
            }
            //			value = obj.get(key);
            //			if (value != null) {
            //				if (value.isArray()) {
            //					ArrayNode subArray = JsonNodeFactory.instance.arrayNode();
            //					findObjects((ArrayNode)value,subArray);
            //					if (subArray.size() > 0) {
            //						result.add(subArray);
            //					}
            //				} else if (value.isObject()) {
            //					// not implemented in jsonata.js
            //				}
            //			}
        }
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
        // accepts an object (or context variable), returns an array of strings
        return "<x-:a<s>>";
    }
}
