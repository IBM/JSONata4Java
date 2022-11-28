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
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Fct_chainContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.utils.Constants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Returns the merger of the objects in an array of objects. It is an error if
 * the input is not an array of objects.<br>
 * <br>
 * From http://docs.jsonata.org/object-functions.html
 *<br> 
 * $merge(array<object>)<br> 
 * Example:<br>
 * $merge([{"a":1},{"b":2}])=={"a":1, "b":2}
 */
public class MergeFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_MERGE);
    public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_MERGE);
    public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_MERGE);
    public static String ERR_ARG1_MUST_BE_OBJECT_ARRAY = String.format(Constants.ERR_MSG_ARG1_MUST_BE_ARRAY_OF_OBJECTS,
        Constants.FUNCTION_MERGE);

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
        JsonNode result = null;
        final CtxEvalResult ctxEvalResult = evalContext(expressionVisitor, ctx);
        final JsonNode arg = ctxEvalResult.arg;
        final int argCount = ctxEvalResult.argumentCount;

        switch (argCount) {
            case 0:
                if (arg != null) {
                    throw new EvaluateRuntimeException(ERR_BAD_CONTEXT);
                }
                // else signal no match (result = null)
                break;
            case 1:
                if (arg == null) {
                    return null;
                }
                if (arg.isArray()) {
                    result = mergeArray(arg);
                } else if (arg.isObject()) {
                    result = (ObjectNode) arg;
                } else {
                    throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
                }
                break;
            default:
                if (ctx.getParent() instanceof Fct_chainContext) {
                    throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
                }
                throw new EvaluateRuntimeException(ERR_ARG2BADTYPE);
        }

        return result;
    }

    private JsonNode mergeArray(final JsonNode arg) {
        final ObjectNode arrayResult = JsonNodeFactory.instance.objectNode();
        final ArrayNode array = (ArrayNode) arg;
        for (int i = 0; i < array.size(); i++) {
            final JsonNode obj = array.get(i);
            if (obj.isObject()) {
                ObjectNode cell = (ObjectNode) obj;
                for (Iterator<String> it = cell.fieldNames(); it.hasNext();) {
                    final String key = it.next();
                    arrayResult.set(key, cell.get(key));
                }
            } else {
                throw new EvaluateRuntimeException(ERR_ARG1_MUST_BE_OBJECT_ARRAY);
            }
        }
        return arrayResult;
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
