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
import com.api.jsonata4java.expressions.utils.Constants;
import com.api.jsonata4java.expressions.utils.FunctionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.LongNode;

public class IndexOfFunction extends FunctionBase implements Function {

	public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_INDEX_OF);
	public static final String ERR_ARG1_TYPE = String.format(Constants.ERR_MSG_ARG1_MUST_BE_ARRAY,
			Constants.FUNCTION_INDEX_OF);
	public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_INDEX_OF);
	public static String ERR_ARG3BADTYPE = String.format(Constants.ERR_MSG_ARG3_BAD_TYPE, Constants.FUNCTION_INDEX_OF);

	public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {

		// Retrieve the number of arguments
		JsonNode arg = JsonNodeFactory.instance.nullNode();
		boolean useContext = FunctionUtils.useContextVariable(ctx, getSignature());
		int argCount = getArgumentCount(ctx);
		if (useContext) {
			arg = FunctionUtils.getContextVariable(expressionVisitor);
			argCount++;
		}

		// Make sure that we have the right number of arguments
		if (argCount == 2) {
			if (!useContext) {
				arg = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, 0);
			}
			JsonNode searchVar = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, useContext ? 0 : 1);

			// if arg is an array, search for the variable and return its index. Any other
			// type of input
			// returns -1.
			if (arg == null || searchVar == null) {
				return new LongNode(-1);
			} else if (arg.isArray()) {
				ArrayNode array = (ArrayNode) arg;
				// poor man's forEach
				JsonNode val = null;
				for (int i = 0; i < array.size(); i++) {
					val = array.get(i);
					if (val != null && val.equals(searchVar)) {
						return new LongNode(i);
					}
				}
			}
		} else {
			throw new EvaluateRuntimeException(
					argCount == 0 ? ERR_BAD_CONTEXT : argCount == 1 ? ERR_ARG2BADTYPE : ERR_ARG3BADTYPE);
		}
		return new LongNode(-1);
	}

	@Override
	public int getMaxArgs() {
		return 1;
	}
	@Override
	public int getMinArgs() {
		return 1;
	}

	@Override
	public String getSignature() {
		// takes an array, returns a number
		return "<ax:n>";
	}

}
