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

import org.antlr.v4.runtime.tree.TerminalNode;
import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.api.jsonata4java.expressions.ExpressionsVisitor;
import com.api.jsonata4java.expressions.ExpressionsVisitor.SelectorArrayNode;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprListContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprValuesContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_declContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.VarListContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Var_recallContext;
import com.api.jsonata4java.expressions.utils.Constants;
import com.api.jsonata4java.expressions.utils.FunctionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

/**
 * From http://docs.jsonata.org/higher-order-functions#filter
 * 
 * Signature: $filter(array, function)
 *
 * Returns an array containing only the values in the array parameter that
 * satisfy the function predicate (i.e. function returns Boolean true when
 * passed the value).
 *
 * The function that is supplied as the second parameter must have the following
 * signature:
 *
 * function(value [, index [, array]])
 *
 * Each value in the input array is passed in as the first parameter in the
 * supplied function. The index (position) of that value in the input array is
 * passed in as the second parameter, if specified. The whole input array is
 * passed in as the third parameter, if specified.
 *
 * Example The following expression returns all the products whose price is
 * higher than average:
 *
 * $filter(Account.Order.Product, function($v, $i, $a) { $v.Price &gt;
 * $average($a.Price) })
 */
public class FilterFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_FILTER);
    public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_FILTER);
    public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_FILTER);
    public static String ERR_ARG3BADTYPE = String.format(Constants.ERR_MSG_ARG3_BAD_TYPE, Constants.FUNCTION_FILTER);
    public static String ERR_ARG1_MUST_BE_ARRAY_OF_OBJECTS = String
        .format(Constants.ERR_MSG_ARG1_MUST_BE_ARRAY_OF_OBJECTS, Constants.FUNCTION_SPREAD);
    public static String ERR_ARG2_FUNCTION_RESOLVE = "Could not resolve function variable reference \"%s\".";

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
        SelectorArrayNode result = new SelectorArrayNode(JsonNodeFactory.instance);
        final CtxEvalResult ctxEvalResult = evalContext(expressionVisitor, ctx);
        final JsonNode arg = ctxEvalResult.arg;
        final int argCount = ctxEvalResult.argumentCount;
        final boolean useContext = ctxEvalResult.useContext;

        switch (argCount) {
            case 0:
                throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
            case 1:
                final DeclaredFunction fctArg = FunctionUtils.getFunctionArgFromCtx(expressionVisitor, ctx, true);
                if (fctArg == null) {
                    // this error message might be not so precise but it is exactly what original JSONata (1.8.6) does
                    throw new EvaluateRuntimeException(ERR_ARG2BADTYPE);
                }
                // TODO
                // unfortunately we can not (or at least I do not see how) differ between
                // - context evaluates to null: chainedArrayArgNullFuncInlined()
                // - no array argument specified at all: missingArrayArgFuncInlined()
                // so currently in every case we signal no match (result = null)
                break;
            case 2:
                // expect something that evaluates to an object and either a variable
                // pointing to a function, or a function declaration
                if (arg != null) {
                    ArrayNode mapArray = (ArrayNode) ExpressionsVisitor.ensureArray(arg);
                    ExprValuesContext valuesCtx = ctx.exprValues();
                    ExprListContext exprList = valuesCtx.exprList();
                    ExprContext varid = exprList.expr((useContext ? 0 : 1));
                    if (varid instanceof Var_recallContext) {
                        invokeDeclaredFunction(expressionVisitor, ctx, result, mapArray, varid);
                    } else if (varid instanceof Function_declContext) {
                        invokeInlinedFunction(expressionVisitor, ctx, result, useContext, mapArray, exprList);
                    }
                }
                break;
            default: // argCount > 2
                throw new EvaluateRuntimeException(ERR_ARG3BADTYPE);
        }
        if (result.size() == 0) {
            result = null;
        }
        return result;
    }

    private void invokeDeclaredFunction(ExpressionsVisitor expressionVisitor, Function_callContext ctx, SelectorArrayNode result, ArrayNode mapArray, ExprContext varid) {
        // get the function to be executed from the functionMap and execute
        final DeclaredFunction fct = expressionVisitor.getDeclaredFunction(varid.getText());
        if (fct != null) {
            invokeFunction(expressionVisitor, ctx, result, mapArray, fct);
        } else {
            final FunctionBase function = expressionVisitor.getJsonataFunction(varid.getText());
            if (function != null) {
                invokeJsonataFunction(expressionVisitor, ctx, result, mapArray, ((Var_recallContext) varid).VAR_ID(), function);
            } else {
                throw new EvaluateRuntimeException(String.format(ERR_ARG2_FUNCTION_RESOLVE, varid.getText()));
            }
        }
    }

    private void invokeInlinedFunction(final ExpressionsVisitor expressionVisitor, final Function_callContext ctx, final SelectorArrayNode result,
        final boolean useContext, ArrayNode mapArray, final ExprListContext exprList) {
        final Function_declContext fctDeclCtx = (Function_declContext) exprList.expr((useContext ? 0 : 1));
        final VarListContext varList = fctDeclCtx.varList();
        final ExprListContext fctBody = fctDeclCtx.exprList();
        final DeclaredFunction fct = new DeclaredFunction(varList, fctBody);
        invokeFunction(expressionVisitor, ctx, result, mapArray, fct);
    }

    private void invokeJsonataFunction(final ExpressionsVisitor expressionVisitor, final Function_callContext ctx, final SelectorArrayNode result,
        final ArrayNode mapArray, final TerminalNode VAR_ID, final FunctionBase function) {
        final int optionalArgs = FunctionUtils.getOptionalArgCount(function.getSignature());
        final int maxArgs = function.getMaxArgs() - optionalArgs;
        for (int i = 0; i < mapArray.size(); i++) {
            final Function_callContext callCtx = new Function_callContext(ctx);
            // note: callCtx.children should be empty unless carrying an exception
            final JsonNode element = mapArray.get(i);
            JsonNode fctResult = null;
            if (maxArgs <= 1) {
                fctResult = FunctionUtils.processVariablesCallFunction(expressionVisitor, function, VAR_ID, callCtx, element);
            } else if (maxArgs == 2) {
                fctResult = FunctionUtils.processVariablesCallFunction(expressionVisitor, function, VAR_ID, callCtx, element,
                    JsonNodeFactory.instance.numberNode(i));
            } else { // if (maxArgs >= 3) {
                // Probably never used: TODO: find a JSONata function with fitting signature
                fctResult = FunctionUtils.processVariablesCallFunction(expressionVisitor, function, VAR_ID, callCtx, element,
                    JsonNodeFactory.instance.numberNode(i), mapArray);
            }
            if (fctResult != null && fctResult.asBoolean()) {
                result.addAsSelectionGroup(element);
            }
        }
    }

    private void invokeFunction(ExpressionsVisitor expressionVisitor, Function_callContext ctx, SelectorArrayNode result, ArrayNode mapArray, DeclaredFunction fct) {
        final int varCount = fct.getVariableCount() > fct.getMaxArgs() ? fct.getMaxArgs() : fct.getVariableCount();
        for (int i = 0; i < mapArray.size(); i++) {
            final JsonNode element = mapArray.get(i);
            ExprValuesContext evc = new ExprValuesContext(ctx, ctx.invokingState);
            switch (varCount) {
                case 1:
                    // just pass the array element
                    evc = FunctionUtils.fillExprVarContext(varCount, ctx, element);
                    break;
                case 2:
                    // pass the array element and index
                    evc = FunctionUtils.fillExprVarContext(varCount, ctx, element);
                    evc = FunctionUtils.addIndexExprVarContext(ctx, evc, i);
                    break;
                case 3:
                    // pass the array element, index, and the complete array
                    evc = FunctionUtils.fillExprVarContext(varCount, ctx, element);
                    evc = FunctionUtils.addIndexExprVarContext(ctx, evc, i);
                    evc = FunctionUtils.addArrayExprVarContext(ctx, evc, mapArray);
                    break;
                default:
                    // pass the array element, index, and array and empty values
                    evc = FunctionUtils.fillExprVarContext(varCount, ctx, element);
                    evc = FunctionUtils.addIndexExprVarContext(ctx, evc, i);
                    evc = FunctionUtils.addArrayExprVarContext(ctx, evc, mapArray);
                    for (int j = 3; j < varCount; j++) {
                        evc = FunctionUtils.addStringExprVarContext(ctx, evc, "");
                    }
                    break;
            }
            final JsonNode fctResult = fct.invoke(expressionVisitor, evc);
            if (fctResult != null && fctResult.asBoolean()) {
                result.addAsSelectionGroup(element);
            }
        }
    }

    @Override
    public int getMaxArgs() {
        return 2;
    }

    @Override
    public int getMinArgs() {
        return 2;
    }

    @Override
    public String getSignature() {
        // accepts anything, returns an array of objects
        return "<af:a>";
    }
}
