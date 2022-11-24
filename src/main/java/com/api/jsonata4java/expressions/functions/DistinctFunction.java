/**
 * (c) Copyright 2021 IBM Corporation
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

import java.util.LinkedHashSet;
import java.util.Set;
import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.api.jsonata4java.expressions.ExpressionsVisitor;
import com.api.jsonata4java.expressions.ExpressionsVisitor.SelectorArrayNode;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.utils.Constants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

/**
 * From https://docs.jsonata.org/array-functions#distinct
 * 
 * $distinct(array)
 * 
 * Returns an array containing all the values from the array parameter, 
 * but with any duplicates removed. Values are tested for deep equality 
 * as if by using the equality operator.
 *
 * Examples
 *
 * $distinct([1,2,3,3,4,3,5]) k]] results in [1, 2, 3, 4, 5]
 * $distinct(Account.Order.Product.Description.Colour) results in [ "Purple", "Orange", "Black" ]
 *
 */
public class DistinctFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_DISTINCT);
    public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_DISTINCT);
    public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_DISTINCT);
    public static String ERR_ARG1_MUST_BE_ARRAY_OF_OBJECTS = String
        .format(Constants.ERR_MSG_ARG1_MUST_BE_ARRAY_OF_OBJECTS, Constants.FUNCTION_DISTINCT);

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
        ArrayNode result = null;
        final CtxEvalResult ctxEvalResult = evalContext(expressionVisitor, ctx);
        final JsonNode arg = ctxEvalResult.arg;
        final int argCount = ctxEvalResult.argumentCount;
        final boolean useContext = ctxEvalResult.useContext;

        if (argCount == 0 && arg == null) {
            // signal no match (result = null)
        } else if (argCount == 1) {
            if (arg == null || arg.isNull()) {
                if (useContext) {
                    throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
                }
                // else signal no match (result = null)
            } else if (arg.isArray()) {
                result = distinct((ArrayNode) arg);
            } else {
                result = JsonNodeFactory.instance.arrayNode();
                result.add(arg);
            }
        } else {
            throw new EvaluateRuntimeException(argCount == 0 ? ERR_ARG1BADTYPE : ERR_ARG2BADTYPE);
        }

        return result;
    }

    private ArrayNode distinct(ArrayNode arg) {
        final ArrayNode result = ((arg instanceof SelectorArrayNode)
            ? new SelectorArrayNode(JsonNodeFactory.instance)
            : JsonNodeFactory.instance.arrayNode());
        final Set<JsonNode> nodeSet = new LinkedHashSet<>();
        for (final JsonNode node : arg) {
            nodeSet.add(node);
        }
        for (final JsonNode node : nodeSet) {
            result.add(node);
        }
        return result;
    }

    @Override
    public int getMaxArgs() {
        return 1;
    }

    @Override
    public int getMinArgs() {
        return 1; // account for context variable
    }

    @Override
    public String getSignature() {
        // accepts anything, returns an array
        return "<x:a>";
    }
}
