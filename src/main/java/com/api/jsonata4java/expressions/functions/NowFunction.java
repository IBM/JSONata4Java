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

import java.time.Instant;
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
 * $now()
 * 
 * Generates a UTC timestamp in ISO 8601 compatible format and returns it as a
 * string. All invocations of $now() within an evaluation of an expression will
 * all return the same timestamp value
 * 
 * Examples
 * 
 * $now()=="2017-05-15T15:12:59.152Z"
 *
 */
public class NowFunction extends FunctionBase {

    public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_NOW);

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
        // Create the variable to return
        JsonNode result = null;

        // Retrieve the number of arguments
        int argCount = getArgumentCount(ctx);

        JsonNode picture = JsonNodeFactory.instance.nullNode();
        if (argCount >= 1) {
            picture = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, 0);
        }
        JsonNode timezone = JsonNodeFactory.instance.nullNode();
        if (argCount == 2) {
            timezone = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, 1);
        }
        String pictureStr = null;
        if (picture != null && picture.isNull() == false) {
            pictureStr = picture.asText();
        }
        String timezoneStr = null;
        if (timezone != null && timezone.isNull() == false) {
            timezoneStr = timezone.asText();
        }
        result = new TextNode(DateTimeUtils.formatDateTime(Instant.now().toEpochMilli(), pictureStr, timezoneStr));

        return result;
    }

    @Override
    public int getMaxArgs() {
        return 2;
    }

    @Override
    public int getMinArgs() {
        return 0;
    }

    @Override
    public String getSignature() {
        // accepts a number (or context variable), returns a number
        return "<s?s?:s>";
    }
}
