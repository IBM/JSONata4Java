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
 * From http://docs.jsonata.org/higher-order-functions#map
 * 
 * Signature: $map(array, function)
 *
 * Returns an array containing the results of applying the function parameter to
 * each value in the array parameter.
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
 * Examples
 *
 * $map([1..5], $string) results in ["1", "2", "3", "4", "5"] With user-defined (lambda)
 * function:
 *
 * $map(Email.address, function($v, $i, $a) { 'Item ' &amp; ($i+1) &amp; ' of ' &amp;
 * $count($a) &amp; ': ' &amp; $v }) evaluates to:
 *
 * [ "Item 1 of 4: fred.smith@my-work.com", "Item 2 of 4: fsmith@my-work.com",
 * "Item 3 of 4: freddy@my-social.com", "Item 4 of 4:
 * frederic.smith@very-serious.com" ]
 */
public class MapFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_MAP);
    public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_MAP);
    public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_MAP);
    public static String ERR_ARG1_MUST_BE_ARRAY_OF_OBJECTS = String
        .format(Constants.ERR_MSG_ARG1_MUST_BE_ARRAY_OF_OBJECTS, Constants.FUNCTION_MAP);

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
        SelectorArrayNode resultArray = new SelectorArrayNode(JsonNodeFactory.instance);
        boolean useContext = FunctionUtils.useContextVariable(this, ctx, getSignature());
        //      		((ctx.getParent() instanceof MappingExpressionParser.Fct_chainContext)
        //            || (ctx.getParent() instanceof MappingExpressionParser.PathContext));
        JsonNode arrNode = null;
        int argCount = getArgumentCount(ctx);
        if (useContext) {
            // pop context var from stack
            arrNode = FunctionUtils.getContextVariable(expressionVisitor);
            if (arrNode != null && arrNode.isNull() == false) {
                argCount++;
            } else {
                useContext = false;
            }
        }

        if (argCount == 2) {
            final ExprListContext exprList = ctx.exprValues().exprList();
            if (!useContext) {
                arrNode = expressionVisitor.visit(exprList.expr(0));
            }
            // expect something that evaluates to an object and either a variable
            // pointing to a function, or a function declaration

            if (arrNode == null) {
                return null;
            }

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
                    if (arrNode.isArray()) {
                        ArrayNode mapArray = (ArrayNode) arrNode;

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
                            resultArray.add(fct.invoke(expressionVisitor, evc));
                        }
                    } else {
                        JsonNode element = (JsonNode) arrNode;
                        int i = 0;
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
                                evc = FunctionUtils.addObjectExprVarContext(ctx, evc, (ObjectNode) arrNode);
                                break;
                            }
                        }
                        resultArray.add(fct.invoke(expressionVisitor, evc));
                    }
                } else {
                    FunctionBase function = expressionVisitor.getJsonataFunction(varid.getText());
                    if (function != null) {
                        int optionalArgs = FunctionUtils.getOptionalArgCount(function.getSignature());
                        int maxArgs = function.getMaxArgs() - optionalArgs;
                        if (arrNode.isArray()) {
                            ArrayNode mapArray = (ArrayNode) arrNode;

                            for (int i = 0; i < mapArray.size(); i++) {
                                Function_callContext callCtx = new Function_callContext(ctx);
                                // note: callCtx.children should be empty unless carrying an
                                // exception
                                JsonNode element = mapArray.get(i);
                                if (maxArgs <= 1) {
                                    resultArray.add(FunctionUtils.processVariablesCallFunction(expressionVisitor, function, VAR_ID,
                                        callCtx, element));
                                } else if (maxArgs == 2) {
                                    resultArray.add(FunctionUtils.processVariablesCallFunction(expressionVisitor, function, VAR_ID,
                                        callCtx, element, JsonNodeFactory.instance.numberNode(i)));
                                } else { // if (maxArgs >= 3) {
                                    resultArray.add(FunctionUtils.processVariablesCallFunction(expressionVisitor, function, VAR_ID,
                                        callCtx, element, JsonNodeFactory.instance.numberNode(i), mapArray));
                                }
                            }
                        } else {
                            JsonNode element = (JsonNode) arrNode;
                            int i = 0;
                            Function_callContext callCtx = new Function_callContext(ctx);
                            // note: callCtx.children should be empty unless carrying an
                            // exception
                            if (maxArgs <= 1) {
                                resultArray.add(FunctionUtils.processVariablesCallFunction(expressionVisitor, function, VAR_ID,
                                    callCtx, element));
                            } else if (maxArgs == 2) {
                                resultArray.add(FunctionUtils.processVariablesCallFunction(expressionVisitor, function, VAR_ID,
                                    callCtx, element, JsonNodeFactory.instance.numberNode(i)));
                            } else { // if (maxArgs >= 3) {
                                resultArray.add(FunctionUtils.processVariablesCallFunction(expressionVisitor, function, VAR_ID,
                                    callCtx, element, JsonNodeFactory.instance.numberNode(i), (ObjectNode) element));
                            }
                        }
                    } else {
                        throw new EvaluateRuntimeException("Expected function variable reference " + varID
                            + " to resolve to a declared nor Jsonata function.");
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
                if (arrNode.isArray()) {
                    ArrayNode mapArray = (ArrayNode) arrNode;
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
                        if (fctResult != null) {
                            resultArray.add(fctResult);
                        }
                    }
                } else {
                    JsonNode element = (JsonNode) arrNode;
                    int i = 0;
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
                            evc = FunctionUtils.addObjectExprVarContext(ctx, evc, (ObjectNode) element);
                            break;
                        }
                    }
                    JsonNode fctResult = fct.invoke(expressionVisitor, evc);
                    if (fctResult != null) {
                        resultArray.add(fctResult);
                    }
                }
            }
        } else {
            throw new EvaluateRuntimeException(argCount <= 1 ? ERR_BAD_CONTEXT : ERR_ARG2BADTYPE);
        }
        return resultArray;
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
        // accepts an array and a function, returns an array of objects
        return "<af>";
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
