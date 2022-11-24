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
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprListContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprValuesContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_declContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.NumberContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.VarListContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Var_recallContext;
import com.api.jsonata4java.expressions.utils.Constants;
import com.api.jsonata4java.expressions.utils.FunctionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * From http://docs.jsonata.org/higher-order-functions#reduce
 * 
 * Signature: $reduce(array, function [, init])
 *
 * Returns an aggregated value derived from applying the function parameter
 * successively to each value in array in combination with the result of the
 * previous application of the function.
 *
 * The function must accept at least two arguments, and behaves like an infix
 * operator between each value within the array. The signature of this supplied
 * function must be of the form:
 *
 * myfunc($accumulator, $value[, $index[, $array]])
 *
 * Example
 *
 * ( $product := function($i, $j){$i * $j}; $reduce([1..5], $product) ) This
 * multiplies all the values together in the array [1..5] to return 120.
 *
 * If the optional init parameter is supplied, then that value is used as the
 * initial value in the aggregation (fold) process. If not supplied, the initial
 * value is the first value in the array parameter.
 */
public class ReduceFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_REDUCE);
    public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_REDUCE);
    public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_REDUCE);
    public static String ERR_ARG1_MUST_BE_ARRAY_OF_OBJECTS = String
        .format(Constants.ERR_MSG_ARG1_MUST_BE_ARRAY_OF_OBJECTS, Constants.FUNCTION_REDUCE);

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
        boolean useContext = FunctionUtils.useContextVariable(this, ctx, getSignature());
        //            ((ctx.getParent() instanceof MappingExpressionParser.Fct_chainContext)
        //            || (ctx.getParent() instanceof MappingExpressionParser.PathContext));
        JsonNode arrNode = null;
        ExprValuesContext valuesCtx = ctx.exprValues();
        ExprListContext exprList = valuesCtx.exprList();
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
        JsonNode prevResult = null;
        if (argCount == 2 || argCount == 3) {
            if (!useContext) {
                arrNode = expressionVisitor.visit(exprList.expr(0));
            }
            // expect something that evaluates to an object and either a variable
            // pointing to a function, or a function declaration

            if (arrNode == null /* || !arrNode.isArray() */ ) {
                // throw new
                // EvaluateRuntimeException(String.format(Constants.ERR_MSG_ARG1_BAD_TYPE,
                // Constants.FUNCTION_REDUCE));
                return null;
            }
            arrNode = ExpressionsVisitor.ensureArray(arrNode);
            ArrayNode mapArray = (ArrayNode) arrNode;

            int startIndex = 1;
            NumberContext init = null;
            prevResult = mapArray.get(0);
            ExprContext initCtx = exprList.expr((useContext ? 1 : 2));
            if (initCtx != null) {
                init = (NumberContext) initCtx;
                prevResult = expressionVisitor.visit(init);
                startIndex = 0;
            }

            ExprContext varid = exprList.expr((useContext ? 0 : 1));
            if (varid instanceof Var_recallContext) {
                TerminalNode VAR_ID = ((Var_recallContext) varid).VAR_ID();
                String varID = varid.getText();
                // get the function to be executed from the functionMap and execute
                DeclaredFunction fct = expressionVisitor.getDeclaredFunction(varID);
                int maxArgs = 0;
                if (fct != null) {
                    int fctVarCount = fct.getMaxArgs();
                    for (int i = startIndex; i < mapArray.size(); i++) {
                        JsonNode element = mapArray.get(i);
                        ExprValuesContext evc = new ExprValuesContext(ctx, ctx.invokingState);
                        maxArgs = fct.getMaxArgs();
                        if (maxArgs <= 2) {
                            evc = FunctionUtils.fillExprVarContext(fctVarCount, ctx, prevResult, element);
                        } else if (maxArgs == 3) {
                            evc = FunctionUtils.fillExprVarContext(fctVarCount, ctx, prevResult, element,
                                JsonNodeFactory.instance.numberNode(i));
                        } else if (maxArgs == 4) {
                            evc = FunctionUtils.fillExprVarContext(fctVarCount, ctx, prevResult, element,
                                JsonNodeFactory.instance.numberNode(i), mapArray);
                        } else {
                            evc = FunctionUtils.fillExprVarContext(fctVarCount, ctx, prevResult, element,
                                JsonNodeFactory.instance.numberNode(i), mapArray);
                        }
                        prevResult = fct.invoke(expressionVisitor, evc);
                    }
                } else {
                    FunctionBase function = expressionVisitor.getJsonataFunction(varid.getText());
                    if (function != null) {
                        for (int i = startIndex; i < mapArray.size(); i++) {
                            Function_callContext callCtx = new Function_callContext(ctx);
                            // note: callCtx.children should be empty unless carrying an
                            // exception
                            JsonNode element = mapArray.get(i);
                            int optionalArgs = FunctionUtils.getOptionalArgCount(function.getSignature());
                            maxArgs = function.getMaxArgs() - optionalArgs;
                            if (maxArgs <= 2) {
                                prevResult = FunctionUtils.processVariablesCallFunction(expressionVisitor, function, VAR_ID,
                                    callCtx, prevResult, element);
                            } else if (maxArgs == 3) {
                                prevResult = FunctionUtils.processVariablesCallFunction(expressionVisitor, function, VAR_ID,
                                    callCtx, prevResult, element, JsonNodeFactory.instance.numberNode(i));
                            } else { // if (maxArgs >= 4) {
                                prevResult = FunctionUtils.processVariablesCallFunction(expressionVisitor, function, VAR_ID,
                                    callCtx, prevResult, element, JsonNodeFactory.instance.numberNode(i), mapArray);
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
                if (varList.getChildCount() < 5) { // ( $x , $y )
                    throw new EvaluateRuntimeException(
                        "The second argument of reduce function must be a function with at least two arguments");
                }
                ExprListContext fctBody = fctDeclCtx.exprList();
                DeclaredFunction fct = new DeclaredFunction(varList, fctBody);
                int fctVarCount = fct.getMaxArgs();
                int maxArgs = 0;
                for (int i = startIndex; i < mapArray.size(); i++) {
                    JsonNode element = mapArray.get(i);
                    ExprValuesContext evc = new ExprValuesContext(ctx, ctx.invokingState);
                    // reduce functions can take an optional index value, and an optional array
                    // value
                    maxArgs = fct.getMaxArgs();
                    if (maxArgs <= 2) {
                        evc = FunctionUtils.fillExprVarContext(fctVarCount, ctx, prevResult, element);
                    } else if (maxArgs == 3) {
                        evc = FunctionUtils.fillExprVarContext(fctVarCount, ctx, prevResult, element,
                            JsonNodeFactory.instance.numberNode(i));
                    } else if (maxArgs == 4) {
                        evc = FunctionUtils.fillExprVarContext(fctVarCount, ctx, prevResult, element,
                            JsonNodeFactory.instance.numberNode(i), mapArray);
                    } else {
                        evc = FunctionUtils.fillExprVarContext(fctVarCount, ctx, prevResult, element,
                            JsonNodeFactory.instance.numberNode(i), mapArray);
                    }
                    prevResult = fct.invoke(expressionVisitor, evc);
                }
            }
        } else {
            throw new EvaluateRuntimeException(argCount == 0 ? ERR_BAD_CONTEXT : ERR_ARG2BADTYPE);
        }
        return prevResult;
    }

    @Override
    public int getMaxArgs() {
        return 3;
    }

    @Override
    public int getMinArgs() {
        return 1; // account for context variable
    }

    @Override
    public String getSignature() {
        // accepts anything (or context variable), returns an array of objects
        return "<afj?:j";
    }

    public void addObject(ArrayNode result, ObjectNode obj) {
        for (Iterator<String> it = obj.fieldNames(); it.hasNext();) {
            String key = it.next();
            ObjectNode cell = JsonNodeFactory.instance.objectNode();
            cell.set(key, obj.get(key));
            result.add(cell);
        }
    }
}
