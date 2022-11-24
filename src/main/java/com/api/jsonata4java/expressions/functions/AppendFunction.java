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

import org.antlr.v4.runtime.ParserRuleContext;
import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.api.jsonata4java.expressions.ExpressionsVisitor;
// import com.api.jsonata4java.expressions.ExpressionsVisitor.SelectorArrayNode;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Fct_chainContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.utils.ArrayUtils;
import com.api.jsonata4java.expressions.utils.Constants;
import com.api.jsonata4java.expressions.utils.FunctionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public class AppendFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_APPEND);
    public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_APPEND);
    public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_APPEND);
    public static String ERR_ARG3BADTYPE = String.format(Constants.ERR_MSG_ARG3_BAD_TYPE, Constants.FUNCTION_APPEND);

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {

        // SelectorArrayNode result = new SelectorArrayNode(JsonNodeFactory.instance);
        ArrayNode result = JsonNodeFactory.instance.arrayNode();

        // Retrieve the number of arguments
        JsonNode argArray = JsonNodeFactory.instance.nullNode();
        boolean useContext = FunctionUtils.useContextVariable(this, ctx, getSignature());
        int argCount = getArgumentCount(ctx);
        if (useContext) {
            argArray = FunctionUtils.getContextVariable(expressionVisitor);
            ParserRuleContext prc = ctx.getParent();
            if ((prc != null && prc instanceof Fct_chainContext) || (argArray != null && argArray.isNull() == false)) {
                argCount++;
            } else {
                useContext = false;
            }
        }

        // Make sure that we have the right number of arguments
        if (argCount == 2) {
            if (!useContext) {
                argArray = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, 0);
            }
            JsonNode arg2Array = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, useContext ? 0 : 1);

            ArrayNode a = null;
            if (argArray != null) {
                a = ArrayUtils.ensureArray(argArray);
            } else {
                // TODO: remove this once jsonata is updated
                return arg2Array;
            }
            ArrayNode b = null;
            if (arg2Array != null) {
                b = ArrayUtils.ensureArray(arg2Array);
            } else {
                // TODO: remove this once jsonata is updated
                return argArray;
            }

            if (a != null) {
                result.addAll(a);
            }
            if (b != null) {
                result.addAll(b);
            }

        } else {
            throw new EvaluateRuntimeException(
                argCount == 0 ? ERR_BAD_CONTEXT : argCount == 1 ? ERR_ARG2BADTYPE : ERR_ARG3BADTYPE);
        }

        return result;
    }

    @Override
    public String getSignature() {
        // accepts anything, anything, returns an array
        return "<xx:a>";
    }

    @Override
    public int getMaxArgs() {
        return 2;
    }

    @Override
    public int getMinArgs() {
        return 2;
    }

}
