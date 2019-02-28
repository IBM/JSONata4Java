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
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * From http://docs.jsonata.org/string-functions.html:
 * 
 * $substringAfter(str, chars)
 * 
 * Returns the substring after the first occurrence of the character sequence
 * chars in str. If str is not specified (i.e. this function is invoked with
 * only one argument), then the context value is used as the value of str. If
 * str does not contain chars, then it returns str. An error is thrown if str
 * and chars are not strings.
 * 
 * Examples
 * 
 * $substringAfter("Hello World", " ")=="World"
 * 
 */
public class SubstringAfterFunction extends FunctionBase implements Function {

	public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT,
			Constants.FUNCTION_SUBSTRING_AFTER);
	public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE,
			Constants.FUNCTION_SUBSTRING_AFTER);
	public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE,
			Constants.FUNCTION_SUBSTRING_AFTER);
	public static String ERR_ARG3BADTYPE = String.format(Constants.ERR_MSG_ARG3_BAD_TYPE,
			Constants.FUNCTION_SUBSTRING_AFTER);

	public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
		// Create the variable to return
		JsonNode result = null;

		// Retrieve the number of arguments
		JsonNode argString = JsonNodeFactory.instance.nullNode();
		boolean useContext = FunctionUtils.useContextVariable(ctx, getSignature());
		int argCount = getArgumentCount(ctx);
		if (useContext) {
			argString = FunctionUtils.getContextVariable(expressionVisitor);
			argCount++;
		}

		// Make sure that we have the right number of arguments
		if (argCount == 1 || argCount == 2) {
			if (!useContext) {
				argString = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, 0);
			}
			if (argCount == 1) {
				if (argString == null || argString.isTextual()) {
					throw new EvaluateRuntimeException(ERR_BAD_CONTEXT);
				}
				throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
			}
			// else argCount == 2
			JsonNode argChars = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, useContext ? 0 : 1);
			// check validity of 2nd argument first
			if (argChars == null) {
				if (argString == null) {
					return null;
				} else if (argString.isTextual()) {
					// just return the string value of argString
					return new TextNode(argString.textValue());
				} else {
					// invalid argString
					throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
				}
			}
			if (argString != null) {
				if (!argString.isTextual()) {
					throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
				}
				if (!argChars.isTextual()) {
					throw new EvaluateRuntimeException(ERR_ARG2BADTYPE);
				}
				final String str = argString.textValue();
				final String chars = argChars.textValue();

				// Find chars in str
				final int index = str.indexOf(chars);
				if (index != -1) {
					result = new TextNode(str.substring(index + 1));
				} else {
					// argChars is not present... just return argString
					result = new TextNode(str);
				}
			}
		} else {
			throw new EvaluateRuntimeException(argCount == 0 ? ERR_ARG1BADTYPE : ERR_ARG3BADTYPE);
		}

		return result;
	}

	@Override
	public String getSignature() {
		// accepts a string (or context variable), a string, returns a string
		return "<s-s:s>";
	}
}
