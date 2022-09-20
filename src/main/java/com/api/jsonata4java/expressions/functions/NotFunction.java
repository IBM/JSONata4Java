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
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.utils.BooleanUtils;
import com.api.jsonata4java.expressions.utils.Constants;
import com.api.jsonata4java.expressions.utils.FunctionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public class NotFunction extends FunctionBase implements Function {

	private static final long serialVersionUID = -5381498407253835525L;

	public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_NOT);
	public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_NOT);

	public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
		// Create the variable to return
		final JsonNode result;

		// Retrieve the number of arguments
		JsonNode arg = JsonNodeFactory.instance.nullNode();
		boolean useContext = FunctionUtils.useContextVariable(this, ctx, getSignature());
		int argCount = getArgumentCount(ctx);
		if (useContext) {
			arg = FunctionUtils.getContextVariable(expressionVisitor);
			if (arg != null && arg.isNull() == false) {
				argCount++;
			} else {
				useContext = false;
			}
		}

		// Make sure that we have the right number of arguments
		if (argCount == 1) {
			if (!useContext) {
				arg = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, 0);
			}
			if (arg == null) {
				return null;
			}
			result = !BooleanUtils.convertJsonNodeToBoolean(arg) ? BooleanNode.TRUE : BooleanNode.FALSE;
		} else {
			if (argCount == 0) {
				return BooleanNode.FALSE;
			}
			throw new EvaluateRuntimeException(ERR_ARG2BADTYPE);
		}

		return result;
	}

	@Override
	public int getMaxArgs() {
		return 1;
	}
	@Override
	public int getMinArgs() {
		return 0; // account for context variable
	}

	@Override
	public String getSignature() {
		// accepts anything (or context variable), returns a boolean
		return "<x-:b>";
	}
}
