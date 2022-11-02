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
 * $merge(array<object>)
 * 
 * Returns the merger of the objects in an array of objects. It is an error if
 * the input is not an array of objects.
 * 
 * Example
 * 
 * $merge([{"a":1},{"b":2}])=={"a":1, "b":2}
 * 
 */
public class MergeFunction extends FunctionBase implements Function {

    private static final long serialVersionUID = -99966498930256770L;

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_MERGE);
    public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_MERGE);
    public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_MERGE);
    public static String ERR_ARG1_MUST_BE_OBJECT_ARRAY = String.format(Constants.ERR_MSG_ARG1_MUST_BE_ARRAY_OF_OBJECTS,
        Constants.FUNCTION_MERGE);

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
        // Create the variable to return
        ObjectNode result = JsonNodeFactory.instance.objectNode();

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
            String key = null;
            if (argArray == null) {
                return null;
            }
            if (argArray.isArray()) {
                ArrayNode array = (ArrayNode) argArray;
                for (int i = 0; i < array.size(); i++) {
                    JsonNode obj = array.get(i);
                    if (obj.isObject()) {
                        ObjectNode cell = (ObjectNode) obj;
                        for (Iterator<String> it = cell.fieldNames(); it.hasNext();) {
                            key = it.next();
                            result.set(key, cell.get(key));
                        }
                    } else {
                        throw new EvaluateRuntimeException(ERR_ARG1_MUST_BE_OBJECT_ARRAY);
                    }
                }
            } else {
                if (argArray.isObject()) {
                    result = (ObjectNode) argArray;
                } else {
                    /*
                     * The input argument is not an array. Throw a suitable exception
                     */
                    throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
                }
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
        // accepts an array of objects, returns an object
        return "<a<o>:o>";
    }
}
