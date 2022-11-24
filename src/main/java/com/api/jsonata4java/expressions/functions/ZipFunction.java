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
import com.api.jsonata4java.expressions.utils.ArrayUtils;
import com.api.jsonata4java.expressions.utils.Constants;
import com.api.jsonata4java.expressions.utils.FunctionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public class ZipFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_ZIP);
    public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_ZIP);

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
        ArrayNode result = JsonNodeFactory.instance.arrayNode();
        ArrayNode inputArrays = JsonNodeFactory.instance.arrayNode();

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
        if (argCount > 0) {
            int minSize = Integer.MAX_VALUE;
            // handle case where initial argObject is from context
            ArrayNode a = null;
            if (useContext) {
                a = ArrayUtils.ensureArray(argObject);
                if (a.size() < minSize) {
                    minSize = a.size();
                }
                inputArrays.add(a);
            }
            for (int i = useContext ? 1 : 0; i < argCount; i++) {
                a = ArrayUtils
                    .ensureArray(expressionVisitor.visit(ctx.exprValues().exprList().expr(useContext ? i - 1 : i)));
                if (a.size() < minSize) {
                    minSize = a.size();
                }
                inputArrays.add(a);
            }
            for (int j = 0; j < minSize; j++) {
                ArrayNode cell = JsonNodeFactory.instance.arrayNode();
                for (int i = 0; i < inputArrays.size(); i++) {
                    cell.add(((ArrayNode) inputArrays.get(i)).get(j));
                }
                result.add(cell);
            }
        } else {
            throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
        }

        //		ArrayNode temp = JsonNodeFactory.instance.arrayNode();
        //		temp.add(result);
        //		result = temp;
        return result;
    }

    @Override
    public int getMaxArgs() {
        return 10000;
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public String getSignature() {
        // accepts one or more arrays, returns an array
        return "<a+>";
    }
}
