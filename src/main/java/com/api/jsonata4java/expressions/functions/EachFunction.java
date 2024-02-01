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
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Returns an array containing the values return by the function when applied to
 * each key/value pair in the object.<br>
 * The function parameter will get invoked with two arguments:<br>
 * function(value, name)<br>
 * where the value parameter is the value of each name/value pair in the object
 * and name is its name. The name parameter is optional.<br>
 * <br>
 * From https://docs.jsonata.org/object-functions#each<br>
 * $each(object,function($value, $key)<br>
 * <br>
 * Examples<br>
 * $each(Address, function($v, $k) {$k &amp; ": " &amp; $v})<br>
 * results in
 * [ "Street: Hursley Park", "City: Winchester", "Postcode: SO21 2JN" ]
 */
public class EachFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_EACH);
    public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_EACH);
    public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_EACH);
    public static String ERR_ARG3BADTYPE = String.format(Constants.ERR_MSG_ARG3_BAD_TYPE, Constants.FUNCTION_EACH);
    public static String ERR_ARG1_MUST_BE_ARRAY_OF_OBJECTS = String
        .format(Constants.ERR_MSG_ARG1_MUST_BE_ARRAY_OF_OBJECTS, Constants.FUNCTION_EACH);

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
        SelectorArrayNode result = null;
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
                // expect something that evaluates to an object and a function declaration
                if (arg == null || !arg.isObject()) {
                    throw new EvaluateRuntimeException(String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_EACH));
                }
                final ObjectNode object = (ObjectNode) arg;
                result = new SelectorArrayNode(JsonNodeFactory.instance);
                final ExprContext varid = ctx.exprValues() != null
                    && ctx.exprValues().exprList() != null
                        ? ctx.exprValues().exprList().expr(useContext ? 0 : 1)
                        : null;
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
                        for (Iterator<String> it = object.fieldNames(); it.hasNext();) {
                            String key = it.next();
                            JsonNode field = object.get(key);
                            ExprValuesContext evc = new ExprValuesContext(ctx, ctx.invokingState);
                            switch (varCount) {
                                case 1: {
                                    // just pass the field value
                                    evc = FunctionUtils.fillExprVarContext(varCount, ctx, field);
                                    break;
                                }
                                case 2: {
                                    // pass the field value and key
                                    evc = FunctionUtils.fillExprVarContext(varCount, ctx, field);
                                    evc = FunctionUtils.addStringExprVarContext(ctx, evc, key);
                                    break;
                                }
                                case 3: {
                                    // pass the field value, key, and object
                                    evc = FunctionUtils.fillExprVarContext(varCount, ctx, field);
                                    evc = FunctionUtils.addStringExprVarContext(ctx, evc, key);
                                    evc = FunctionUtils.addObjectExprVarContext(ctx, evc, object);
                                    break;
                                }
                            }
                            JsonNode fctResult = fct.invoke(expressionVisitor, evc);
                            if (fctResult != null) {
                                result.add(fctResult);
                            }
                        }
                    } else {
                        FunctionBase function = expressionVisitor.getJsonataFunction(varid.getText());
                        if (function != null) {
                            for (Iterator<String> it = object.fieldNames(); it.hasNext();) {
                                String key = it.next();
                                JsonNode field = object.get(key);
                                Function_callContext callCtx = new Function_callContext(ctx);
                                // note: callCtx.children should be empty unless carrying an
                                // exception
                                result.add(FunctionUtils.processVariablesCallFunction(expressionVisitor, function, VAR_ID, callCtx, field));
                            }
                        } else {
                            throw new EvaluateRuntimeException(
                                "Expected function variable reference " + varID + " to resolve to a declared nor Jsonata function.");
                        }
                    }
                } else if (varid instanceof Function_declContext) {
                    final Function_declContext fctDeclCtx = ctx.exprValues() != null
                        && ctx.exprValues().exprList() != null
                            ? (Function_declContext) ctx.exprValues().exprList().expr(useContext ? 0 : 1)
                            : null;
                    if (fctDeclCtx != null) {
                        // we have a declared function for each
                        VarListContext varList = fctDeclCtx.varList();
                        ExprListContext fctBody = fctDeclCtx.exprList();
                        DeclaredFunction fct = new DeclaredFunction(varList, fctBody);
                        int varCount = fct.getVariableCount();
                        int fctVarCount = fct.getMaxArgs();
                        if (varCount > fctVarCount) {
                            // only send variables function can consume
                            varCount = fctVarCount;
                        }
                        for (Iterator<String> it = object.fieldNames(); it.hasNext();) {
                            String key = it.next();
                            JsonNode field = object.get(key);
                            ExprValuesContext evc = new ExprValuesContext(ctx, ctx.invokingState);
                            switch (varCount) {
                                case 1: {
                                    // just pass the field value
                                    evc = FunctionUtils.fillExprVarContext(varCount, ctx, field);
                                    break;
                                }
                                case 2: {
                                    // pass the field value and key
                                    evc = FunctionUtils.fillExprVarContext(varCount, ctx, field);
                                    evc = FunctionUtils.addStringExprVarContext(ctx, evc, key);
                                    break;
                                }
                                case 3: {
                                    // pass the field value, key, and object
                                    evc = FunctionUtils.fillExprVarContext(varCount, ctx, field);
                                    evc = FunctionUtils.addStringExprVarContext(ctx, evc, key);
                                    evc = FunctionUtils.addObjectExprVarContext(ctx, evc, object);
                                    break;
                                }
                            }
                            JsonNode fctResult = fct.invoke(expressionVisitor, evc);
                            if (fctResult != null) {
                                result.add(fctResult);
                            }
                        }
                    }
                }
                break;
            default:
                throw new EvaluateRuntimeException(ERR_ARG3BADTYPE);
        }
        return result;
    }

    @Override
    public int getMaxArgs() {
        return 2;
    }

    @Override
    public int getMinArgs() {
        return 1; // account for context variable
    }

    @Override
    public String getSignature() {
        // accepts anything (or context variable), and a function, returns an array of objects
        return "<o-f:a<o>";
    }
}
