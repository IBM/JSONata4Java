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
import com.fasterxml.jackson.databind.node.ObjectNode;

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

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
        SelectorArrayNode result = new SelectorArrayNode(JsonNodeFactory.instance);
        final CtxEvalResult ctxEvalResult = evalContext(expressionVisitor, ctx);
        final JsonNode arg = ctxEvalResult.arg;
        final int argCount = ctxEvalResult.argumentCount;
        final boolean useContext = ctxEvalResult.useContext;

        switch (argCount) {
            case 0:
                if (arg != null) {
                    throw new EvaluateRuntimeException(ERR_BAD_CONTEXT);
                }
                // else signal no match (result = null)
                break;
            case 1:
                final DeclaredFunction fctArg = FunctionUtils.getFunctionArgFromCtx(expressionVisitor, ctx, true);
                if (fctArg == null) {
                    // this error message might be not so precise but it is exactly what original JSONata (1.8.6) does
                    throw new EvaluateRuntimeException(ERR_ARG2BADTYPE);
                }
                // unfortunately we cannot (or at least I do not see how) differ between
                // - context evaluates to null
                // - no array argument specified at all
                // so in every case we signal no match (result = null)
                break;
            case 2:
                // expect something that evaluates to an object and either a variable
                // pointing to a function, or a function declaration
                if (arg == null) {
                    throw new EvaluateRuntimeException(String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_FILTER));
                }

                ArrayNode mapArray = (ArrayNode) ExpressionsVisitor.ensureArray(arg);

                ExprValuesContext valuesCtx = ctx.exprValues();
                ExprListContext exprList = valuesCtx.exprList();
                ExprContext varid = exprList.expr((useContext ? 0 : 1));
                if (varid instanceof Var_recallContext) {
                    TerminalNode VAR_ID = ((Var_recallContext) varid).VAR_ID();
                    String varID = varid.getText();
                    // get the function to be executed from the functionMap and execute
                    DeclaredFunction fct = expressionVisitor.getDeclaredFunction(varID);
                    if (fct != null) {
                        int varCount = fct.getVariableCount();
                        int fctVarCount = fct.getMaxArgs();
                        if (varCount > fctVarCount) {
                            // only send variables function can consume
                            varCount = fctVarCount;
                        }
                        for (int i = 0; i < mapArray.size(); i++) {
                            JsonNode element = mapArray.get(i);
                            ExprValuesContext evc = new ExprValuesContext(ctx, ctx.invokingState);
                            switch (varCount) {
                                case 1: {
                                    // just pass the mapArray variable
                                    evc = FunctionUtils.fillExprVarContext(varCount, ctx, element);
                                    break;
                                }
                                case 2: {
                                    // pass the mapArray variable and index
                                    evc = FunctionUtils.fillExprVarContext(varCount, ctx, element);
                                    evc = FunctionUtils.addIndexExprVarContext(ctx, evc, i);
                                    break;
                                }
                                case 3: {
                                    // pass the mapArray variable, index, and array
                                    evc = FunctionUtils.fillExprVarContext(varCount, ctx, element);
                                    evc = FunctionUtils.addIndexExprVarContext(ctx, evc, i);
                                    evc = FunctionUtils.addArrayExprVarContext(ctx, evc, mapArray);
                                    break;
                                }
                            }
                            JsonNode fctResult = fct.invoke(expressionVisitor, evc);
                            if (fctResult != null && fctResult.asBoolean()) {
                                result.addAsSelectionGroup(element);
                            }
                        }
                    } else {
                        FunctionBase function = expressionVisitor.getJsonataFunction(varid.getText());
                        if (function != null) {
                            int optionalArgs = FunctionUtils.getOptionalArgCount(function.getSignature());
                            int maxArgs = function.getMaxArgs() - optionalArgs;
                            for (int i = 0; i < mapArray.size(); i++) {
                                Function_callContext callCtx = new Function_callContext(ctx);
                                // note: callCtx.children should be empty unless carrying an
                                // exception
                                JsonNode element = mapArray.get(i);

                                JsonNode fctResult = null;
                                if (maxArgs <= 1) {
                                    fctResult = FunctionUtils.processVariablesCallFunction(expressionVisitor, function, VAR_ID, callCtx, element);
                                } else if (maxArgs == 2) {
                                    fctResult = FunctionUtils.processVariablesCallFunction(expressionVisitor, function, VAR_ID, callCtx, element,
                                        JsonNodeFactory.instance.numberNode(i));
                                } else { // if (maxArgs >= 3) {
                                    fctResult = FunctionUtils.processVariablesCallFunction(expressionVisitor, function, VAR_ID, callCtx, element,
                                        JsonNodeFactory.instance.numberNode(i), mapArray);
                                }
                                if (fctResult != null && fctResult.asBoolean()) {
                                    result.addAsSelectionGroup(element);
                                }
                            }
                        } else {
                            throw new EvaluateRuntimeException(
                                "Expected function variable reference " + varID + " to resolve to a declared nor Jsonata function.");
                        }
                    }
                } else if (varid instanceof Function_declContext) {
                    Function_declContext fctDeclCtx = (Function_declContext) exprList.expr((useContext ? 0 : 1));

                    // we have a declared function for filter
                    VarListContext varList = fctDeclCtx.varList();
                    ExprListContext fctBody = fctDeclCtx.exprList();
                    DeclaredFunction fct = new DeclaredFunction(varList, fctBody);
                    int varCount = fct.getVariableCount();
                    int fctVarCount = fct.getMaxArgs();
                    if (varCount > fctVarCount) {
                        // only send variables function can consume
                        varCount = fctVarCount;
                    }
                    for (int i = 0; i < mapArray.size(); i++) {
                        JsonNode element = mapArray.get(i);
                        ExprValuesContext evc = new ExprValuesContext(ctx, ctx.invokingState);
                        switch (varCount) {
                            case 1: {
                                // just pass the mapArray variable
                                evc = FunctionUtils.fillExprVarContext(varCount, ctx, element);
                                break;
                            }
                            case 2: {
                                // pass the mapArray variable and index
                                evc = FunctionUtils.fillExprVarContext(varCount, ctx, element);
                                evc = FunctionUtils.addIndexExprVarContext(ctx, evc, i);
                                break;
                            }
                            case 3: {
                                // pass the mapArray variable, index, and array
                                evc = FunctionUtils.fillExprVarContext(varCount, ctx, element);
                                evc = FunctionUtils.addIndexExprVarContext(ctx, evc, i);
                                evc = FunctionUtils.addArrayExprVarContext(ctx, evc, mapArray);
                                break;
                            }
                        }
                        JsonNode fctResult = fct.invoke(expressionVisitor, evc);
                        // Issue #250 need to improve upon the asBoolean so a non-empty string is true
                        if (fctResult != null && fctResult.asBoolean(true)) {
                            result.addAsSelectionGroup(element);
                        }
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

    public void addObject(SelectorArrayNode result, ObjectNode obj) {
        for (Iterator<String> it = obj.fieldNames(); it.hasNext();) {
            String key = it.next();
            ObjectNode cell = JsonNodeFactory.instance.objectNode();
            cell.set(key, obj.get(key));
            result.add(cell);
        }
    }
}
