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
import com.api.jsonata4java.expressions.generated.MappingExpressionParser;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprValuesContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.utils.ArrayUtils;
import com.api.jsonata4java.expressions.utils.Constants;
import com.api.jsonata4java.expressions.utils.FunctionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public class SortFunction extends FunctionBase implements Function {

	public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_SORT);
	public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_SORT);
	public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_SORT);
	public static String ERR_FCTNOTFOUND = String.format(Constants.ERR_MSG_FCT_NOT_FOUND, Constants.FUNCTION_SORT);
	public static String ERR_ARG1BADARRAYTYPE = String.format(Constants.ERR_MSG_ARG1_MUST_BE_ARRAY,
			Constants.FUNCTION_SORT);

	public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
		ArrayNode result = JsonNodeFactory.instance.arrayNode();
		// Retrieve the number of arguments
		JsonNode arg = JsonNodeFactory.instance.nullNode();
		boolean useContext = FunctionUtils.useContextVariable(ctx, getSignature());
		int argCount = getArgumentCount(ctx);
		if (useContext) {
			arg = FunctionUtils.getContextVariable(expressionVisitor);
			argCount++;
		}

		// Make sure that we have the right number of arguments
		if (argCount == 1) {
			if (!useContext) {
				// use the default comparator
				arg = expressionVisitor.visit(ctx.exprValues().exprList().expr(0));
			}
			// if arg is an array, return its length. Any other type of input
			// returns 1.
			if (arg == null) {
				return null; // throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
			} else if (arg.isArray()) {
				ArrayNode array = (ArrayNode) arg;
				for (int i = 0; i < array.size(); i++) {
					result.add(array.get(i));
				}
				msort(result, null, expressionVisitor, ctx);
			} else {
				// allow to work with any input
				result.add(arg);
				return result;
			}
		} else if (argCount == 2) {
			// use the defined function comparator
			DeclaredFunction fct = null;
			if (!useContext) {
				// use the default comparator
				arg = expressionVisitor.visit(ctx.exprValues().exprList().expr(0));
			}
			// if arg is an array, return its length. Any other type of input
			// returns 1.
			if (arg == null) {
				throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
			}

			ExprContext fctCtx = ctx.exprValues().exprList().expr(useContext ? 0 : 1);
			if (fctCtx == null) {
				throw new EvaluateRuntimeException(ERR_ARG2BADTYPE);
			}

			if (!(fctCtx instanceof MappingExpressionParser.Function_declContext)) {
				if (!(fctCtx instanceof MappingExpressionParser.Var_recallContext)) {
					throw new EvaluateRuntimeException("Expected an function declaration reference but got "
							+ fctCtx.getText() + " that is an " + fctCtx.getClass().getName());
				}
				String varID = ((MappingExpressionParser.Var_recallContext) fctCtx).VAR_ID().getText();
				fct = expressionVisitor.getFunctionMap().get(varID);
				if (fct == null) {
					throw new EvaluateRuntimeException(
							String.format(Constants.ERR_MSG_VARIABLE_FCT_NOT_FOUND, varID, Constants.FUNCTION_SORT));
				}
			} else {
				MappingExpressionParser.Function_declContext fctDeclCtx = (MappingExpressionParser.Function_declContext) fctCtx;
				MappingExpressionParser.VarListContext varList = fctDeclCtx.varList();
				MappingExpressionParser.ExprListContext exprList = fctDeclCtx.exprList();
				try {
					fct = new DeclaredFunction(varList, exprList);
				} catch (EvaluateRuntimeException e) {
					throw new EvaluateRuntimeException(ERR_FCTNOTFOUND);
				}
			}
			// if arg is an array, return its length. Any other type of input
			// returns 1.
			if (arg.isArray()) {
				ArrayNode array = (ArrayNode) arg;
				for (int i = 0; i < array.size(); i++) {
					result.add(array.get(i));
				}
				msort(result, fct, expressionVisitor, ctx);
			} else {
				result.add(arg);
			}

		} else {
			throw new EvaluateRuntimeException(argCount == 0 ? ERR_ARG1BADTYPE : ERR_ARG2BADTYPE);
		}

		return result;
	}

	@Override
	public String getSignature() {
		// accepts an array (or context variable), an optional function, returns an
		// array
		return "<x-f?:n>";
	}

	void msort(ArrayNode array, DeclaredFunction fct, ExpressionsVisitor exprVisitor, Function_callContext ctx) {
		int n = array.size();
		if (array == null || n < 2) {
			return;
		}
		int middle = (int) Math.floor(n / 2);
		ArrayNode left = ArrayUtils.slice(array, 0, middle);
		ArrayNode right = ArrayUtils.slice(array, middle);
		msort(left, fct, exprVisitor, ctx);
		msort(right, fct, exprVisitor, ctx);
		merge(array, left, right, fct, exprVisitor, ctx);
	}

	void merge(ArrayNode array, ArrayNode left, ArrayNode right, DeclaredFunction fct, ExpressionsVisitor exprVisitor,
			Function_callContext ctx) {
		int lSize = left.size();
		int rSize = right.size();
		int i = 0, j = 0, k = 0;
		if (fct == null) {
			while (i < lSize && j < rSize) {
				if (ArrayUtils.compare(left.get(i), right.get(j))) {
					array.set(k++, right.get(j++));
				} else {
					array.set(k++, left.get(i++));
				}
			}
			while (i < lSize) {
				array.set(k++, left.get(i++));
			}
			while (j < rSize) {
				array.set(k++, right.get(j++));
			}
		} else {
			while (i < lSize && j < rSize) {
				// set up the variables for the function then call it
				ExprValuesContext evc = new ExprValuesContext(ctx.getParent(), ctx.invokingState);
				evc = FunctionUtils.fillExprVarContext(ctx, left.get(i), right.get(j));
				JsonNode comp = fct.invoke(exprVisitor, evc);
				if (comp != null && comp.asBoolean()) {
					array.set(k++, right.get(j++));
				} else {
					array.set(k++, left.get(i++));
				}
			}
			while (i < lSize) {
				array.set(k++, left.get(i++));
			}
			while (j < rSize) {
				array.set(k++, right.get(j++));
			}
		}
	}
}
