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
import com.api.jsonata4java.expressions.utils.DateTimeUtils;
import com.api.jsonata4java.expressions.utils.FunctionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * From http://docs.jsonata.org/string-functions.html:
 * 
 * $fromMillis(number)
 * 
 * Convert a number representing milliseconds since the Unix Epoch (1 January,
 * 1970 UTC) to a timestamp string in the ISO 8601 format.
 * 
 * Examples
 * 
 * $fromMillis(1510067557121)=="2017-11-07T15:12:37.121Z"
 * 
 */
public class FromMillisFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_FROM_MILLIS);
    public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE,
        Constants.FUNCTION_FROM_MILLIS);
    public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE,
        Constants.FUNCTION_FROM_MILLIS);

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
        // Create the variable to return
        JsonNode result = null;

        // Retrieve the number of arguments
        JsonNode argNumber = JsonNodeFactory.instance.nullNode();
        boolean useContext = FunctionUtils.useContextVariable(this, ctx, getSignature());
        int argCount = getArgumentCount(ctx);
        if (useContext) {
            argNumber = FunctionUtils.getContextVariable(expressionVisitor);
            if (argNumber != null && argNumber.isNull() == false) {
                argCount++;
            } else {
                useContext = false;
            }
        }

        // Make sure that we have the right number of arguments
        if (argCount >= 1 && argCount <= 3) {
            if (!useContext) {
                argNumber = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, 0);
            }
            JsonNode picture = JsonNodeFactory.instance.nullNode();
            if (argCount >= 2) {
                picture = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, useContext ? 0 : 1);
            }
            JsonNode timezone = JsonNodeFactory.instance.nullNode();
            if (argCount == 3) {
                timezone = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, useContext ? 1 : 2);
            }
            if (argNumber == null) {
                return null;
            }
            if (argNumber.isNumber()) {
                final Long millis = argNumber.asLong();
                String pictureStr = null;
                if (picture != null && picture.isNull() == false) {
                    pictureStr = picture.asText();
                }
                String timezoneStr = null;
                if (timezone != null && timezone.isNull() == false) {
                    timezoneStr = timezone.asText();
                }
                result = new TextNode(DateTimeUtils.formatDateTime(millis, pictureStr, timezoneStr));
            } else {
                throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
            }
        } else {
            throw new EvaluateRuntimeException(ERR_BAD_CONTEXT);
        }

        return result;
    }

    @Override
    public int getMaxArgs() {
        return 3;
    }

    @Override
    public int getMinArgs() {
        return 0; // account for context variable
    }

    @Override
    public String getSignature() {
        // accepts a number (or context variable), an optional string, another optional
        // string, returns a number
        return "<n-s?s?:s>";
    }
}
