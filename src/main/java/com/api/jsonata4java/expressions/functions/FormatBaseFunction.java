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
 * $formatBase(number [, radix])
 * 
 * Casts the number to a string and formats it to an integer represented in the
 * number base specified by the radix argument. If radix is not specified, then
 * it defaults to base 10. radix can be between 2 and 36, otherwise an error is
 * thrown.
 * 
 * $formatBase(100, 2)==“1100100” $formatBase(2555, 16)=="9fb"
 * 
 */
public class FormatBaseFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_FORMAT_BASE);
    public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE,
        Constants.FUNCTION_FORMAT_BASE);
    public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE,
        Constants.FUNCTION_FORMAT_BASE);
    public static String ERR_ARG3BADTYPE = String.format(Constants.ERR_MSG_ARG3_BAD_TYPE,
        Constants.FUNCTION_FORMAT_BASE);

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
        if (argCount == 1 || argCount == 2) {
            if (!useContext) {
                argNumber = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, 0);
            }

            // Make sure that the first argument is a number
            if (argNumber == null) {
                return null;
            }
            if (argNumber.isNumber()) {
                // Read the number from the argument
                Double d = argNumber.asDouble();
                Long l = Math.round(d);
                if (Math.abs((double) l - d) == 0.5 && l % 2 == 1) {
                    l--;
                }
                final int number = (int) l.longValue();

                // Check to see if we have an radix argument and read it if we do
                int radix = 10;
                if (argCount == 2) {
                    final JsonNode argRadix = FunctionUtils.getValuesListExpression(expressionVisitor, ctx,
                        useContext ? 0 : 1);
                    if (argRadix != null) {
                        if (argRadix.isNumber()) {
                            d = argRadix.asDouble();
                            l = Math.round(d);
                            if (Math.abs((double) l - d) == 0.5 && l % 2 == 1) {
                                l--;
                            }
                            radix = (int) l.longValue();

                            // Make sure that the radix specified is valid
                            if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX) {
                                throw new EvaluateRuntimeException(Constants.ERR_MSG_INVALID_RADIX);
                            }
                        } else {
                            throw new EvaluateRuntimeException(ERR_ARG2BADTYPE);
                        }
                    }
                }

                // Convert the number to a string in the specified base
                result = new TextNode(Integer.toString(number, radix));
            } else {
                throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
            }
        } else {
            throw new EvaluateRuntimeException(argCount == 0 ? ERR_BAD_CONTEXT : ERR_ARG3BADTYPE);
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
        // accepts a number (or context variable), an optional number, returns a string
        return "<n-n?:s>";
    }
}
