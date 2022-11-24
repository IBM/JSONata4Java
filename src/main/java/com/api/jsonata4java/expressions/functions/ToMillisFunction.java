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

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.api.jsonata4java.expressions.ExpressionsVisitor;
import com.api.jsonata4java.expressions.JS4JDate;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.utils.Constants;
import com.api.jsonata4java.expressions.utils.DateTimeUtils;
import com.api.jsonata4java.expressions.utils.FunctionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.LongNode;

/**
 * http://docs.jsonata.org/numeric-functions.html
 * 
 * $toMillis(timestamp)
 * 
 * Convert a timestamp string in the ISO 8601 format to the number of
 * milliseconds since the Unix Epoch (1 January, 1970 UTC) as a number.
 * 
 * An error is thrown if the string is not in the correct format.
 * 
 * Examples
 * 
 * $toMillis("2017-11-07T15:07:54.972Z")==1510067274972
 */
public class ToMillisFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_TO_MILLIS);
    public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_TO_MILLIS);
    public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_TO_MILLIS);

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
        // Create the variable to return
        JsonNode result = null;

        // Retrieve the number of arguments
        JsonNode argTimestamp = JsonNodeFactory.instance.nullNode();
        boolean useContext = FunctionUtils.useContextVariable(this, ctx, getSignature());
        int argCount = getArgumentCount(ctx);
        if (useContext) {
            argTimestamp = FunctionUtils.getContextVariable(expressionVisitor);
            if (argTimestamp != null && argTimestamp.isNull() == false) {
                argCount++;
            } else {
                useContext = false;
            }
        }

        // Make sure that we have the right number of arguments
        if (argCount >= 1 && argCount <= 2) {
            if (!useContext) {
                argTimestamp = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, 0);
            }

            JsonNode picture = JsonNodeFactory.instance.nullNode();
            if (argCount == 2) {
                picture = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, 1);
            }
            // if arg is an array, return its length. Any other type of
            // input returns 1.
            if (argTimestamp == null) {
                return null;
            }
            // Check the type of the argument
            if (argTimestamp.isTextual()) {
                try {
                    /*
                     * The string passed to us might contain an ISO 8601 format string that
                     * specifies an offset element. The java.time.Instant class will throw a
                     * DateTimeParseException when attempting to parse these strings. We need to use
                     * the java.time.OffsetDateTime class instead.
                     */
                    Long millis;
                    if (picture != null && !picture.isNull()) {
                        millis = DateTimeUtils.parseDateTime(argTimestamp.asText(), picture.asText());
                    } else {
                        millis = OffsetDateTime.parse(argTimestamp.asText()).toInstant().toEpochMilli();
                    }
                    if (millis == null) {
                        result = null;
                    } else {
                        result = new LongNode(millis);
                    }
                } catch (DateTimeParseException e) {
                    /*
                     * The string argument does not contain a valid ISO 8601 format datetime string.
                     * Throw a suitable exception.
                     */
                    JS4JDate testDate = new JS4JDate();
                    try {
                        testDate = new JS4JDate(argTimestamp.asText());
                    } catch (Exception e1) {
                        final String msg = String.format(Constants.ERR_MSG_TO_MILLIS_ISO_8601_FORMAT,
                            argTimestamp.asText());
                        throw new EvaluateRuntimeException(msg);
                    }
                    Long millis = testDate.getTime();
                    result = new LongNode(millis);
                }
            } else {
                // The argument is a neither a number or a string. Throw a suitable
                // exception.
                throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
            }
        } else {
            throw new EvaluateRuntimeException(ERR_BAD_CONTEXT);
        }

        return result;
    }

    @Override
    public int getMaxArgs() {
        return 2;
    }

    @Override
    public int getMinArgs() {
        return 0; // account for context variable
    }

    @Override
    public String getSignature() {
        // accepts a string (or context variable), an optional string, returns a number
        return "<s-s?:n>";
    }
}
