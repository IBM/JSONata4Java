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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.api.jsonata4java.expressions.ExpressionsVisitor;
import com.api.jsonata4java.expressions.ExpressionsVisitor.SelectorArrayNode;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.utils.Constants;
import com.api.jsonata4java.expressions.utils.FunctionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * From http://docs.jsonata.org/string-functions.html:
 * 
 * $match(str, pattern [, limit])
 * 
 * Finds occurrences of pattern within str and reports them with their index and
 * group.
 * 
 * If str is not specified, then the context value is used as the value of str.
 * It is an error if str is not a string.
 * 
 * The pattern parameter must be a regular expression (regex) String used for
 * searching the string.
 * 
 * The optional limit parameter, is a number that specifies the maximum number
 * of results to be found before stopping.
 * 
 * Examples
 * 
 * $match("John Smith and John Jones", "John") ==
 * "[{"match":"John","index":0,"groups":["John"]},{"match":"John","index":15,"groups":["John"]}]
 * 
 */
public class MatchFunction extends FunctionBase implements Function {

	public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_MATCH);
	public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_MATCH);
	public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_MATCH);
	public static String ERR_ARG3BADTYPE = String.format(Constants.ERR_MSG_ARG3_BAD_TYPE, Constants.FUNCTION_MATCH);
	public static String ERR_ARG4BADTYPE = String.format(Constants.ERR_MSG_ARG4_BAD_TYPE, Constants.FUNCTION_MATCH);

	public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
		// Create the variable to return
		SelectorArrayNode result = null;

		// Retrieve the number of arguments
		JsonNode argString = JsonNodeFactory.instance.nullNode();
		boolean useContext = FunctionUtils.useContextVariable(ctx, getSignature());
		int argCount = getArgumentCount(ctx);
		if (useContext) {
			argString = FunctionUtils.getContextVariable(expressionVisitor);
			argCount++;
		}

		// Make sure that we have the right number of arguments
		if (argCount == 2 || argCount == 3) {
			if (!useContext) {
				argString = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, 0);
			}
			if (argString != null) {
				if (!argString.isTextual()) {
					throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
				}
				final JsonNode argPattern = FunctionUtils.getValuesListExpression(expressionVisitor, ctx,
						useContext ? 0 : 1);
				int limit = -1;
				// Make sure that we have the right number of arguments
				if (argString == null || !argString.isTextual() || argString.asText().isEmpty()) {
					throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
				}
				// Make sure that the pattern is a non-empty string
				if (argPattern != null && argPattern.isTextual() && !(argPattern.asText().isEmpty())) {
					// Check to see if the separator is just a string
					final String str = argString.textValue();
					final String pattern = argPattern.textValue();

					if (argCount == 3) {
						final JsonNode argLimit = FunctionUtils.getValuesListExpression(expressionVisitor, ctx,
								useContext ? 1 : 2);

						// Check to see if we have an optional limit argument we check it
						if (argLimit != null) {
							if (argLimit.isNumber() && argLimit.asInt() >= 0) {
								limit = argLimit.asInt();
							} else {
								throw new EvaluateRuntimeException(ERR_ARG3BADTYPE);
							}
						}
					}

					final Pattern regexPattern = Pattern.compile(pattern);
					final Matcher matcher = regexPattern.matcher(str);

					// Check to see if a limit was specified
					result = new SelectorArrayNode(JsonNodeFactory.instance);
					if (limit == -1) {
						// No limits... match all occurrences in the string
						while (matcher.find()) {
							ObjectNode obj = JsonNodeFactory.instance.objectNode();
							obj.put("match", str.substring(matcher.start(), matcher.end()));
							obj.put("start", new Long(matcher.start()));
							ArrayNode groups = JsonNodeFactory.instance.arrayNode();
							obj.set("groups", groups);
							groups.add(matcher.group());
							result.add(obj);
						}
					} else if (limit > 0) {
						int count = 0;
						while (matcher.find() && count < limit) {
							count++;
							ObjectNode obj = JsonNodeFactory.instance.objectNode();
							obj.put("match", str.substring(matcher.start(), matcher.end()));
							obj.put("start", new Long(matcher.start()));
							ArrayNode groups = JsonNodeFactory.instance.arrayNode();
							obj.set("groups", groups);
							groups.add(matcher.group());
							result.add(obj);
						}
					} else {
						return null;
					}
				} else {
					throw new EvaluateRuntimeException(ERR_ARG2BADTYPE);
				}
			} else {
				/*
				 * TODO: Add support for regex patterns using / delimiters once the grammar has
				 * been updated. For now, simply throw an exception.
				 */
				throw new EvaluateRuntimeException(ERR_ARG2BADTYPE);
			}
		} else {
			throw new EvaluateRuntimeException(argCount <= 1 ? ERR_ARG1BADTYPE : ERR_ARG4BADTYPE);
		}

		return result;
	}

	@Override
	public int getMaxArgs() {
		return 3;
	}
	@Override
	public int getMinArgs() {
		return 2;
	}

	@Override
	public String getSignature() {
		// accepts a string (or context variable), a function (that accepts a string and
		// returns an object), an optional number, returns an array of objects
		return "<s-f<s:o>n?:a<o>>";
	}
}
