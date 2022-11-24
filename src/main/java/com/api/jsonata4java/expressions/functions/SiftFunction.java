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
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.VarListContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Var_recallContext;
import com.api.jsonata4java.expressions.utils.Constants;
import com.api.jsonata4java.expressions.utils.FunctionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * From http://docs.jsonata.org/object-functions.html
 * 
 * $spread(object)
 * 
 * Splits an object containing key/value pairs into an array of objects, each of
 * which has a single key/value pair from the input object. If the parameter is
 * an array of objects, then the resultant array contains an object for every
 * key/value pair in every object in the supplied array.
 * 
 */
public class SiftFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_SIFT);
    public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_SIFT);
    public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_SIFT);
    public static String ERR_ARG1_MUST_BE_ARRAY_OF_OBJECTS = String
        .format(Constants.ERR_MSG_ARG1_MUST_BE_ARRAY_OF_OBJECTS, Constants.FUNCTION_SIFT);

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
        ObjectNode resultObject = new ObjectNode(JsonNodeFactory.instance);
        boolean useContext = FunctionUtils.useContextVariable(this, ctx, getSignature());
        //      		((ctx.getParent() instanceof MappingExpressionParser.Fct_chainContext)
        //            || (ctx.getParent() instanceof MappingExpressionParser.PathContext));
        JsonNode objNode = null;
        ExprValuesContext valuesCtx = ctx.exprValues();
        ExprListContext exprList = valuesCtx.exprList();
        int argCount = getArgumentCount(ctx);
        if (useContext) {
            // pop context var from stack
            objNode = FunctionUtils.getContextVariable(expressionVisitor);
            if (objNode != null && objNode.isNull() == false) {
                argCount++;
            } else {
                useContext = false;
            }
        }

        if (argCount == 1 || argCount == 2) {
            if (!useContext) {
                objNode = expressionVisitor.visit(exprList.expr(0));
            }
            // expect something that evaluates to an object and either a variable
            // pointing to a function, or a function declaration

            if (objNode == null || !objNode.isObject()) {
                throw new EvaluateRuntimeException(String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_SIFT));
            }
            ObjectNode object = (ObjectNode) objNode;

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
                        if (fctResult != null && fctResult.asBoolean()) {
                            resultObject.set(key, field);
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
                            JsonNode fctResult = FunctionUtils.processFctCallVariables(expressionVisitor, function, VAR_ID, callCtx, field, key, object);
                            if (fctResult != null && fctResult.asBoolean()) {
                                resultObject.set(key, field);
                            }
                        }
                    } else {
                        throw new EvaluateRuntimeException(
                            "Expected function variable reference " + varID + " to resolve to a declared nor Jsonata function.");
                    }
                }
            } else if (varid instanceof Function_declContext) {
                Function_declContext fctDeclCtx = (Function_declContext) exprList.expr((useContext ? 0 : 1));
                // we have a declared function for sifting
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
                    if (fctResult != null && fctResult.asBoolean()) {
                        resultObject.set(key, field);
                    }
                }
            }
        } else {
            throw new EvaluateRuntimeException(argCount == 0 ? ERR_ARG1BADTYPE
                : argCount == 1 ? ERR_ARG1BADTYPE : ERR_ARG2BADTYPE);
        }
        return resultObject;
    }

    @Override
    public int getMaxArgs() {
        return 2;
    }

    @Override
    public int getMinArgs() {
        return 0; // account for context variable
    }

    @Override
    public String getSignature() {
        // accepts object (or context variable) and a function, returns an array of objects
        return "<o-f?:o>";
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
