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

import java.util.regex.Pattern;
import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.api.jsonata4java.expressions.ExpressionsVisitor;
import com.api.jsonata4java.expressions.RegularExpression;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.utils.Constants;
import com.api.jsonata4java.expressions.utils.FunctionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * From http://docs.jsonata.org/string-functions.html:
 * 
 * $replace(str, pattern, replacement [, limit])
 * 
 * Finds occurrences of pattern within str and replaces them with replacement.
 * 
 * If str is not specified, then the context value is used as the value of str.
 * It is an error if str is not a string.
 * 
 * The pattern parameter can either be a string or a regular expression (regex).
 * If it is a string, it specifies the substring(s) within str which should be
 * replaced. If it is a regex, its is used to find.
 * 
 * The replacement parameter can either be a string or a function. If it is a
 * string, it specifies the sequence of characters that replace the substring(s)
 * that are matched by pattern. If pattern is a regex, then the replacement
 * string can refer to the characters that were matched by the regex as well as
 * any of the captured groups using a S followed by a number N:
 * 
 * * If N = 0, then it is replaced by substring matched by the regex as a whole.
 * * If N &GT; 0, then it is replaced by the substring captured by the Nth
 * parenthesised group in the regex. * If N is greater than the number of
 * captured groups, then it is replaced by the empty string. * A literal $
 * character must be written as $$ in the replacement string
 * 
 * If the replacement parameter is a function, then it is invoked for each match
 * occurrence of the pattern regex. The replacement function must take a single
 * parameter which will be the object structure of a regex match as described in
 * the $match function; and must return a string.
 * 
 * The optional limit parameter, is a number that specifies the maximum number
 * of replacements to make before stopping. The remainder of the input beyond
 * this limit will be copied to the output unchanged.
 * 
 * Examples
 * 
 * $replace("John Smith and John Jones", "John", "Mr")=="Mr Smith and Mr Jones"
 * $replace("John Smith and John Jones", "John", "Mr", 1)=="Mr Smith and John
 * Jones" $replace("abracadabra", "a.*?a", "*")=="*c*bra" $replace("John Smith",
 * "(\w+)\s(\w+)", "$2, $1")=="Smith, John" $replace("265USD", "([0-9]+)USD",
 * "$$$1")=="$265"
 * 
 */
public class ReplaceFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_REPLACE);
    public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_REPLACE);
    public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_REPLACE);
    public static String ERR_ARG3BADTYPE = String.format(Constants.ERR_MSG_ARG3_BAD_TYPE, Constants.FUNCTION_REPLACE);
    public static String ERR_ARG4BADTYPE = String.format(Constants.ERR_MSG_ARG4_BAD_TYPE, Constants.FUNCTION_REPLACE);
    public static String ERR_ARG5BADTYPE = String.format(Constants.ERR_MSG_ARG5_BAD_TYPE, Constants.FUNCTION_REPLACE);
    public static final String ERR_MSG_ARG2_EMPTY_STR = String.format(Constants.ERR_MSG_ARG2_EMPTY_STR,
        Constants.FUNCTION_REPLACE);

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
        // Create the variable to return
        JsonNode result = null;

        // Retrieve the number of arguments
        JsonNode argString = JsonNodeFactory.instance.nullNode();
        boolean useContext = FunctionUtils.useContextVariable(this, ctx, getSignature());
        int argCount = getArgumentCount(ctx);
        if (useContext) {
            argString = FunctionUtils.getContextVariable(expressionVisitor);
            if (argString != null && argString.isNull() == false) {
                argCount++;
            } else {
                useContext = false;
            }
        }

        // Make sure that we have the right number of arguments
        if (argCount >= 1 && argCount <= 4) {
            if (!useContext) {
                argString = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, 0);
            }
            if (argString == null) {
                if (argCount < 2) {
                    throw new EvaluateRuntimeException(ERR_ARG2BADTYPE);
                } else if (argCount == 2) {
                    throw new EvaluateRuntimeException(ERR_BAD_CONTEXT);
                }
                return null; // throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
            }
            if (argCount >= 2) {
                final JsonNode argPattern = FunctionUtils.getValuesListExpression(expressionVisitor, ctx,
                    useContext ? 0 : 1);
                int limit = -1;
                // Make sure that the separator is not null
                if (argPattern != null && (argPattern.isTextual() || argPattern instanceof POJONode)) {
                    if (argPattern.asText().isEmpty()) {
                        throw new EvaluateRuntimeException(ERR_MSG_ARG2_EMPTY_STR);
                    }
                    if (argCount >= 3) {
                        final JsonNode argReplacement = FunctionUtils.getValuesListExpression(expressionVisitor, ctx,
                            useContext ? 1 : 2);
                        // Check to see if the pattern is just a string
                        if (argReplacement != null && (argReplacement.isTextual())) {
                            if (!argString.isTextual()) {
                                throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
                            }
                            final String str = argString.textValue();
                            final RegularExpression regex = argPattern instanceof POJONode
                                ? (RegularExpression) ((POJONode) argPattern).getPojo()
                                : null;
                            final String pattern = regex != null
                                ? regex.toString()
                                : argPattern.textValue();
                            final String replacement = argReplacement.textValue();

                            if (argCount == 4) {
                                final JsonNode argLimit = FunctionUtils.getValuesListExpression(expressionVisitor, ctx,
                                    useContext ? 2 : 3);

                                // Check to see if we have an optional limit argument we check it
                                if (argLimit != null) {
                                    if (argLimit.isNumber() && argLimit.asInt() >= 0) {
                                        limit = argLimit.asInt();
                                    } else {
                                        throw new EvaluateRuntimeException(ERR_ARG4BADTYPE);
                                    }
                                }
                            }

                            // Check to see if a limit was specified
                            if (limit == -1) {
                                // No limits... replace all occurrences in the string
                                if (regex != null) {
                                    result = new TextNode(regex.getPattern().matcher(str).replaceAll(jsonata2JavaReplacement(replacement)));
                                } else {
                                    result = new TextNode(str.replaceAll(Pattern.quote(pattern), jsonata2JavaReplacement(replacement)));
                                }
                            } else {
                                // Only perform the replace the specified number of times
                                String retString = new String(str);
                                for (int i = 0; i < limit; i++) {
                                    if (regex != null) {
                                        retString = regex.getPattern().matcher(retString).replaceFirst(jsonata2JavaReplacement(replacement));
                                    } else {
                                        retString = retString.replaceFirst(Pattern.quote(pattern), jsonata2JavaReplacement(replacement));
                                    }
                                } // FOR
                                result = new TextNode(retString);
                            }
                        } else {
                            throw new EvaluateRuntimeException(ERR_ARG3BADTYPE);
                        }
                    } else {
                        if (argPattern == null || !argPattern.isTextual()) {
                            throw new EvaluateRuntimeException(ERR_ARG2BADTYPE);
                        }
                        if (!argString.isTextual()) {
                            throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
                        }
                    }
                } else {
                    throw new EvaluateRuntimeException(ERR_ARG2BADTYPE);
                }
            } else if (!argString.isTextual()) {
                throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
            }
        } else {
            throw new EvaluateRuntimeException(argCount == 0 ? ERR_ARG1BADTYPE
                : argCount == 1 ? ERR_ARG1BADTYPE : argCount == 2 ? ERR_ARG2BADTYPE : ERR_ARG5BADTYPE);
        }

        return result;
    }

    public static String jsonata2JavaReplacement(String in) {
        // In JSONata and in Java the $ in the replacement test usually starts the insertion of a capturing group
        // In order to replace a simple $ in Java you have to escape the $ with "\$"
        // in JSONata you do this with a '$$'
        // "\$" followed any character besides '<' and a digit into $ + this character  
        return in.replaceAll("\\$\\$", "\\\\\\$")
            .replaceAll("([^\\\\]|^)\\$([^0-9^<])", "$1\\\\\\$$2");
    }

    @Override
    public int getMaxArgs() {
        return 3;
    }

    @Override
    public int getMinArgs() {
        return 1; // account for context variable
    }

    @Override
    public String getSignature() {
        // accepts a string (or context variable), a string or function, an optional
        // number, returns a string
        return "<s-(sf)(sf)n?:s>";
    }
}
