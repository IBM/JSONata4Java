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

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.api.jsonata4java.expressions.ExpressionsVisitor;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprListContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprValuesContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.VarListContext;
import com.fasterxml.jackson.databind.JsonNode;

public class DeclaredFunction {

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

	public JsonNode invoke(ExpressionsVisitor expressionVisitor, ParserRuleContext ruleValues) {
		JsonNode result = null;
		ExprValuesContext exprValues = null;
		if (ruleValues instanceof ExprValuesContext) {
		   exprValues = (ExprValuesContext)ruleValues;
   		List<TerminalNode> varListCtx = _varList.VAR_ID();
   		List<ExprContext> exprValuesCtx = exprValues.exprList().expr();
   		int varListCount = varListCtx.size();
   		int exprListCount = exprValuesCtx.size();
   		// ensure a direct mapping is possible
   		if (varListCount != exprListCount) {
   			throw new EvaluateRuntimeException(
   					"Expected equal counts for varibles (" + varListCount + ") and values (" + exprListCount + ")");
   		}
   		for (int i = 0; i < varListCount; i++) {
   			String varID = varListCtx.get(i).getText();
   			JsonNode value = expressionVisitor.visit(exprValuesCtx.get(i));
   			expressionVisitor.getVariableMap().put(varID, value);
   		}
		} // else EmptyValuesContext
      result = expressionVisitor.visit(_exprList);
		return result;
	}
}
