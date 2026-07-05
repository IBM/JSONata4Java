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

import java.util.Optional;
import java.util.regex.Pattern;
import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.api.jsonata4java.expressions.ExpressionsVisitor;
import com.api.jsonata4java.expressions.RegularExpression;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.regex.JdkRegexPattern;
import com.api.jsonata4java.expressions.regex.RegexFlags;
import com.api.jsonata4java.expressions.regex.RegexMatch;
import com.api.jsonata4java.expressions.regex.RegexPattern;
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
                            final RegexPattern pattern = regex != null
                                ? regex.getPattern()
                                // A plain-string pattern is treated as an exact literal to
                                // search for, not as a user-authored regex, so this
                                // intentionally always uses the default engine's quoting
                                // rather than routing through a pluggable RegexEngine
                                // (whose escaping dialect may not understand
                                // Pattern.quote()'s \Q...\E syntax).
                                : new JdkRegexPattern(Pattern.quote(argPattern.textValue()), new RegexFlags(false, false));
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

                            result = new TextNode(replaceMatches(str, pattern, replacement, limit));
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

    /**
     * Replaces up to {@code limit} (or all, if {@code limit == -1}) non-overlapping
     * matches of {@code regex} in {@code str} with {@code replacement}, expanding
     * any {@code $N}/{@code $$} group references along the way. Implemented
     * directly against {@link RegexPattern}'s match results (rather than delegating to
     * {@link java.util.regex.Matcher#replaceAll(String)}'s own replacement-string
     * syntax) so that it works the same way regardless of which {@link
     * com.api.jsonata4java.expressions.regex.RegexEngine} produced {@code regex}.
     */
    private static String replaceMatches(String str, RegexPattern regex, String replacement, int limit) {
        StringBuilder sb = new StringBuilder();
        int pos = 0;
        int count = 0;
        while (limit == -1 || count < limit) {
            final Optional<RegexMatch> found = regex.findFirst(str, pos);
            if (!found.isPresent()) {
                break;
            }
            final RegexMatch m = found.get();
            sb.append(str, pos, m.getIndex());
            sb.append(expandReplacement(replacement, m));
            pos = m.getIndex() + m.getLength();
            if (m.getLength() == 0) {
                // Avoid getting stuck on a zero-length match: copy the next
                // character through unchanged and advance by one, mirroring
                // java.util.regex.Matcher's own empty-match handling.
                if (pos < str.length()) {
                    sb.append(str.charAt(pos));
                }
                pos++;
            }
            count++;
        }
        if (pos < str.length()) {
            sb.append(str, pos, str.length());
        }
        return sb.toString();
    }

    /**
     * Expands {@code $N} (Nth captured group, or the whole match for N=0) and
     * {@code $$} (a literal $) references in {@code replacement} against
     * {@code match}. Any other {@code $} is copied through literally. A group
     * number beyond the number of captured groups expands to the empty string;
     * a group that did not participate in the match also expands to the empty
     * string.
     */
    static String expandReplacement(String replacement, RegexMatch match) {
        StringBuilder sb = new StringBuilder();
        int len = replacement.length();
        int i = 0;
        while (i < len) {
            char c = replacement.charAt(i);
            if (c == '$' && i + 1 < len) {
                char next = replacement.charAt(i + 1);
                if (next == '$') {
                    sb.append('$');
                    i += 2;
                    continue;
                }
                if (Character.isDigit(next)) {
                    // Cap the digit run considered for a group number to avoid
                    // Integer.parseInt overflowing below (e.g. "$99999999999999"):
                    // no real regex has anywhere near this many capture groups, so
                    // anything longer than this is certain to fail the "<=
                    // groupCount" check regardless of exactly how many digits it has.
                    int hardLimit = Math.min(len, i + 1 + 9);
                    int maxEnd = i + 1;
                    while (maxEnd < hardLimit && Character.isDigit(replacement.charAt(maxEnd))) {
                        maxEnd++;
                    }
                    // Greedily consume digits for the group number, backing off
                    // one digit at a time if the resulting number isn't a valid
                    // group, mirroring java.util.regex.Matcher's own $N group
                    // reference parsing (e.g. with 13 groups, "$123" means group
                    // 12 followed by literal "3", not a non-existent group 123).
                    int groupCount = match.getGroups().size();
                    int end = maxEnd;
                    int groupNum = -1;
                    while (end > i + 1) {
                        int candidate = Integer.parseInt(replacement.substring(i + 1, end));
                        if (candidate <= groupCount) {
                            groupNum = candidate;
                            break;
                        }
                        end--;
                    }
                    if (groupNum == -1) {
                        // Not even a single digit is a valid group number;
                        // copy the $ through literally rather than throwing.
                        sb.append(c);
                        i++;
                        continue;
                    }
                    if (groupNum == 0) {
                        sb.append(match.getMatch());
                    } else {
                        String group = match.getGroups().get(groupNum - 1);
                        if (group != null) {
                            sb.append(group);
                        }
                    }
                    i = end;
                    continue;
                }
            }
            sb.append(c);
            i++;
        }
        return sb.toString();
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
