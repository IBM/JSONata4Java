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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;
import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.api.jsonata4java.expressions.ExpressionsVisitor;
import com.api.jsonata4java.expressions.FrameEnvironment;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprListContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprValuesContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.VarListContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Var_recallContext;
import com.api.jsonata4java.expressions.utils.Constants;
import com.fasterxml.jackson.databind.JsonNode;

public class DeclaredFunction implements Serializable {

    private static final long serialVersionUID = -7984628726118649985L;

    VarListContext _varList;
    ExprListContext _exprList;

    public DeclaredFunction(VarListContext varList, ExprListContext exprList) {
        if (varList == null) {
            throw new EvaluateRuntimeException("VarListContext passed is null.");
        }
        if (exprList == null) {
            throw new EvaluateRuntimeException("ExprListContext passed is null.");
        }
        _varList = varList;
        _exprList = exprList;
    }

    public ExprListContext getExpressionList() {
        return _exprList;
    }

    public List<TerminalNode> getVariables() {
        List<TerminalNode> variables = _varList.getTokens(MappingExpressionParser.VAR_ID);
        if (variables == null) {
            variables = new ArrayList<TerminalNode>();
        }
        return variables;
    }

    public int getVariableCount() {
        return getVariables().size();
    }

    public int getMaxArgs() {
        return getVariableCount();
    }

    public int getMinArgs() {
        return getVariableCount();
    }

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, ParserRuleContext ruleValues) {
        // generate a new frameEnvironment for life of this block
        FrameEnvironment oldEnvironment = expressionVisitor.setNewEnvironment();

        JsonNode result = null;
        ExprValuesContext exprValues = null;
        Function_callContext fctCallValues = null;
        if (ruleValues instanceof ExprValuesContext) {
            exprValues = (ExprValuesContext) ruleValues;
            List<TerminalNode> varListCtx = _varList.VAR_ID();
            List<ExprContext> exprValuesCtx = new ArrayList<ExprContext>();
            if (exprValues != null) {
                exprValuesCtx = exprValues.exprList().expr();
            }
            int varListCount = varListCtx.size();
            int exprListCount = exprValuesCtx.size();
            // ensure a direct mapping is possible
            if (varListCount != exprListCount) {
                //   			throw new EvaluateRuntimeException(
                //   					"Expected equal counts for variables (" + varListCount + ") and values (" + exprListCount + ")");
            }
            for (int i = 0; i < varListCount; i++) {
                String varID = varListCtx.get(i).getText();
                JsonNode value = expressionVisitor.visit(exprValuesCtx.get(i));
                expressionVisitor.setVariable(varID, value);
            }
            result = expressionVisitor.visit(_exprList);
        } else if (ruleValues instanceof Function_callContext) {
            fctCallValues = (Function_callContext) ruleValues;
            List<TerminalNode> varListCtx = _varList.VAR_ID();
            exprValues = (ExprValuesContext) fctCallValues.exprValues();
            List<ExprContext> exprValuesCtx = new ArrayList<ExprContext>();
            if (exprValues != null) {
                exprValuesCtx = exprValues.exprList().expr();
            }
            int varListCount = varListCtx.size();
            int exprListCount = exprValuesCtx.size();
            // ensure a direct mapping is possible filling with nulls where needed
            if (varListCount < exprListCount) {
                throw new EvaluateRuntimeException(
                    "Expected equal counts for variables (" + varListCount + ") and values (" + exprListCount + ")");
            }
            for (int i = 0; i < varListCount; i++) {
                String varID = varListCtx.get(i).getText();
                if (i < exprListCount) {
                    JsonNode value = expressionVisitor.visit(exprValuesCtx.get(i));
                    expressionVisitor.setVariable(varID, value);
                } else {
                    expressionVisitor.setVariable(varID, null);
                }
            }
            result = expressionVisitor.visit(_exprList);
        } else if (ruleValues instanceof Var_recallContext) {
            String fctName = ((Var_recallContext) ruleValues).VAR_ID().getText();
            // assume this is a variable pointing to a function
            DeclaredFunction declFct = expressionVisitor.getDeclaredFunction(fctName);
            if (declFct == null) {
                FunctionBase function = expressionVisitor.getJsonataFunction(fctName);
                if (function == null) {
                    function = Constants.FUNCTIONS.get(fctName);
                }
                if (function != null) {
                    result = function.invoke(expressionVisitor, (Function_callContext) ruleValues.getRuleContext());
                } else {
                    throw new EvaluateRuntimeException("Unknown function: " + fctName);
                }
            } else {
                for (int i = 0; i < declFct.getVariableCount(); i++) {
                    String varID = declFct._varList.VAR_ID().get(i).getText();
                    JsonNode value = expressionVisitor.visit(_exprList.expr().get(i));
                    expressionVisitor.setVariable(varID, value);
                }
                result = expressionVisitor.visit(declFct._exprList);
            }
        } else {
            result = expressionVisitor.visit(_exprList);
        }
        expressionVisitor.resetOldEnvironment(oldEnvironment);
        return result;
    }
}
