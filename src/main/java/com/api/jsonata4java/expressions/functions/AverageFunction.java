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
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

/**
 * Always returns as a DoubleNode (regardless of input)
 */
public class AverageFunction extends FunctionBase implements Function {

	public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_AVERAGE);
	public static final String ERR_ARG_TYPE = String.format(Constants.ERR_MSG_ARG1_MUST_BE_ARRAY_OF_NUMBER,
			Constants.FUNCTION_AVERAGE);
	public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_AVERAGE);
	public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_AVERAGE);

	public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {

		// Retrieve the number of arguments
		JsonNode arg = JsonNodeFactory.instance.nullNode();
		boolean useContext = FunctionUtils.useContextVariable(ctx, getSignature());
		int argCount = getArgumentCount(ctx);
		if (useContext) {
			arg = FunctionUtils.getContextVariable(expressionVisitor);
			if (arg == null) {
				if (argCount > 0) {
					throw new EvaluateRuntimeException(ERR_ARG2BADTYPE);
				}
				return null;
			}
			// check to see if there is a valid context value
			if (!arg.isArray()) {
				throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
			}
			argCount++;
		}

		// Make sure that we have the right number of arguments
		if (argCount == 1) {
			if (!useContext) {
				FunctionUtils.validateArguments(ERR_ARG_TYPE, expressionVisitor, ctx, 0, getSignature());
				arg = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, 0);
			}
			if (arg == null) {
				return null; // throw new EvaluateRuntimeException(ERR_ARG_TYPE);
			} else if (arg.isArray()) {
				ArrayNode arr = (ArrayNode) arg;

				if (arr.size() == 0) {// avoid divide by 0 errors
					// we could just return "0" here, but this is not how
					// http://try.jsonata.org/ behaves
					throw new EvaluateRuntimeException(ERR_ARG_TYPE);
				}

				double sum = 0;
				for (JsonNode a : arr) {
					if (a.isNumber()) {
						sum += a.asDouble(); // asDouble() won't throw an exception
												// even if non-numeric (that's why we do
												// the isNumber() check above)
					} else {
						// also complain if any non-numeric types are included in the
						// array
						throw new EvaluateRuntimeException(ERR_ARG_TYPE);
					}
				}

				double avg = sum / arr.size();

				return new DoubleNode(avg);

			} else if (arg.isNumber()) {
				return new DoubleNode(arg.asDouble());
			} else {
				throw new EvaluateRuntimeException(ERR_ARG_TYPE);
			}

		} else {
			throw new EvaluateRuntimeException(
					argCount == 0 ? (useContext ? ERR_BAD_CONTEXT : ERR_ARG1BADTYPE) : ERR_ARG2BADTYPE);
		}
	}

	@Override
	public String getSignature() {
		// accepts an array of numbers, returns a number
		return "<a<n>-:n>";
	}
}
