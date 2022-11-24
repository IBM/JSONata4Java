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
import com.api.jsonata4java.expressions.utils.NumberUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.LongNode;

/**
 * http://docs.jsonata.org/numeric-functions.html
 * 
 * Casts the arg parameter to a number using the following casting rules:
 * 
 * - Numbers are unchanged - Strings that contain a sequence of characters that
 * represent a legal JSON number are converted to that number - All other values
 * cause an error to be thrown.
 * 
 * If arg is not specified (i.e. this function is invoked with no arguments),
 * then the context value is used as the value of arg.
 * 
 * Examples
 * 
 * $number("5")==5 ["1", "2", "3", "4", "5"].$number()==[1, 2, 3, 4, 5]
 * 
 */
public class NumberFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_NUMBER);
    public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_NUMBER);
    public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_NUMBER);

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
        if (argCount == 1 || useContext) {
            if (!useContext) {
                argString = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, 0);
            }
            if (argString == null) {
                return null;
            }
            // Check the type of the argument
            if (argString.isObject()) {
                throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
            } else if (argString.isTextual()) {
                /*
                 * For consistency with the JavaScript implementation of JSONata, we limit the
                 * size of the numbers that we handle to be within the range Double.MAX_VALUE
                 * and -Double.MAX_VALUE. If we did not do this we would need to implement a lot
                 * of extra code to handle BigInteger and BigDecimal. The
                 * NumberUtils::convertNumberToValueNode will check whether the number is within
                 * the valid range and throw a suitable exception if it is not.
                 */
                result = NumberUtils.convertNumberToValueNode(argString.asText());
            } else if (argString.isNumber()) {
                if (!argString.isIntegralNumber()) {
                    // Math.ceil only accepts a double
                    Double ceil = argString.doubleValue();
                    if (ceil - ceil.longValue() == 0.0d) {
                        // Create the node to return
                        result = new LongNode(ceil.longValue());
                    } else {
                        result = new DoubleNode(ceil);
                    }
                } else {
                    if (argString.isLong()) {
                        double ceil = argString.doubleValue();

                        // Create the node to return
                        result = new LongNode((long) ceil);
                    } else {
                        // The argument is already an integer... simply return the
                        // node
                        result = argString;
                    }
                }
            } else if (argString.isArray()) {
                throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
            } else if (argString.isBoolean()) {
                result = argString.asBoolean() ? new LongNode(1) : new LongNode(0);
            } else {
                // The argument is a neither a number or a string. Throw a
                // suitable exception.
                throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
            }
        } else {
            throw new EvaluateRuntimeException(argCount == 0 ? ERR_BAD_CONTEXT : ERR_ARG2BADTYPE);
        }

        return result;
    }

    @Override
    public int getMaxArgs() {
        return 1;
    }

    @Override
    public int getMinArgs() {
        return 0; // account for context variable
    }

    @Override
    public String getSignature() {
        // accepts a number or string or boolean (or context variable), returns a number
        return "<(nsb)-:n>";
    }
}
