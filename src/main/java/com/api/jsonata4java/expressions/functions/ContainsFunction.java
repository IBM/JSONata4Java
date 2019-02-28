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
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

/**
 * From http://docs.jsonata.org/string-functions.html:
 * 
 * $contains(str, pattern)
 * 
 * Returns true if str is matched by pattern, otherwise it returns false. If str
 * is not specified (i.e. this function is invoked with one argument), then the
 * context value is used as the value of str.
 * 
 * The pattern parameter can either be a string or a regular expression (regex).
 * If it is a string, the function returns true if the characters within pattern
 * are contained contiguously within str. If it is a regex, the function will
 * return true if the regex matches the contents of str.
 * 
 * Examples
 * 
 * $contains("abracadabra", "bra")==true $contains("abracadabra", /a.*a/) ==
 * true $contains("abracadabra", /ar.*a/)==false $contains("Hello World",
 * /wo/)==false $contains("Hello World", /wo/i)==true
 * Phone[$contains(number, /^077/)]=={ "type": "mobile", "number": "077 7700
 * 1234" }
 * 
 */
public class ContainsFunction extends FunctionBase implements Function {

	public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_CONTAINS);
	public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_CONTAINS);
	public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_CONTAINS);
	public static String ERR_ARG3BADTYPE = String.format(Constants.ERR_MSG_ARG3_BAD_TYPE, Constants.FUNCTION_CONTAINS);

	public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
		// Create the variable to return
		JsonNode result = null;

		// Retrieve the number of arguments
		JsonNode argString = JsonNodeFactory.instance.nullNode();
		boolean useContext = FunctionUtils.useContextVariable(ctx, getSignature());
		int argCount = getArgumentCount(ctx);
		if (useContext) {
			argString = FunctionUtils.getContextVariable(expressionVisitor);
			if (argString == null) {
				return null;
			}
			// check to see if there is a valid context value
			if (!argString.isTextual()) {
				throw new EvaluateRuntimeException(ERR_BAD_CONTEXT);
			}
			argCount++;
		}

		// Make sure that we have the right number of arguments
		if (argCount == 2) {
			if (!useContext) {
				argString = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, 0);
			}
			if (argString == null) {
				return null;
			}
			if (!argString.isTextual()) {
				throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
			}
			final JsonNode argPattern = FunctionUtils.getValuesListExpression(expressionVisitor, ctx,
					useContext ? 0 : 1);

			if (argString != null) {
				if (!argString.isTextual()) {
					throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
				}
				// Make sure argPattern is not null
				if (argPattern != null) {
					// Check to see if the pattern is just a string
					if (argPattern.isTextual()) {
						final String str = argString.textValue();
						final String pattern = argPattern.textValue();

						// Do a simple String::contains
						result = str.contains(pattern) ? BooleanNode.TRUE : BooleanNode.FALSE;
					} else {
						/*
						 * TODO: Add support for regex patterns once the grammar has been updated. For
						 * now, simply throw an exception.
						 */
						throw new EvaluateRuntimeException(ERR_ARG2BADTYPE);
					}
				} else {
					throw new EvaluateRuntimeException(ERR_ARG2BADTYPE);
				}
			}
		} else {
			throw new EvaluateRuntimeException(argCount < 2 ? ERR_BAD_CONTEXT : ERR_ARG3BADTYPE);
		}

		return result;
	}

	@Override
	public String getSignature() {
		// accepts a string (or context variable), a string or function, returns a
		// boolean
		return "<s-(sf):b>";
	}

}
