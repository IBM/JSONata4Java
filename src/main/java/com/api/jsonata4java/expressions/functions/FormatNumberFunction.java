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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Iterator;
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
 * $formatNumber(number, picture [, options])
 * 
 * Casts the number to a string and formats it to a decimal representation as
 * specified by the picture string.
 * 
 * The behaviour of this function is consistent with the XPath/XQuery function
 * fn:format-number as defined in the XPath F&amp;O 3.1 specification. The
 * picture string parameter defines how the number is formatted and has the same
 * syntax as fn:format-number.
 * 
 * The optional third argument options is used to override the default locale
 * specific formatting characters such as the decimal separator. If supplied,
 * this argument must be an object containing name/value pairs specified in the
 * decimal format section of the XPath F&amp;O 3.1 specification.
 * 
 * Examples
 * 
 * $formatNumber(12345.6, '#,###.00')=="12,345.60" $formatNumber(1234.5678,
 * "00.000e0")=="12.346e2" $formatNumber(34.555, "#0.00;(#0.00)")=="34.56"
 * $formatNumber(-34.555, "#0.00;(#0.00)")=="(34.56)" $formatNumber(0.14,
 * "01%")=="14%" $formatNumber(0.14, "###pm", {"per-mille": "pm"})=="140pm"
 * $formatNumber(1234.5678, "①①.①①①e①", {"zero-digit": "\u245f"})=="①②.③④⑥e②"
 * 
 */
public class FormatNumberFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT,
        Constants.FUNCTION_FORMAT_NUMBER);
    public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE,
        Constants.FUNCTION_FORMAT_NUMBER);
    public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE,
        Constants.FUNCTION_FORMAT_NUMBER);
    public static String ERR_ARG3BADTYPE = String.format(Constants.ERR_MSG_ARG3_BAD_TYPE,
        Constants.FUNCTION_FORMAT_NUMBER);
    public static String ERR_ARG4BADTYPE = String.format(Constants.ERR_MSG_ARG4_BAD_TYPE,
        Constants.FUNCTION_FORMAT_NUMBER);

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
                // check to see if there is a valid context value
                if (!argNumber.isNumber()) {
                    throw new EvaluateRuntimeException(ERR_BAD_CONTEXT);
                }
                argCount++;
            } else {
                useContext = false;
            }
        }

        // Make sure that we have the right number of arguments
        if (argCount == 2 || argCount == 3) {
            /*
             * Read the first argument. When we attempt to read it, an ArithmeticException
             * may be thrown if, for example, it is an expression that performs a
             * "divide by zero". In this scenario, we need to return the relevant
             * representation of infinity so that it can be rendered in the response.
             */
            if (!useContext) {
                argNumber = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, 0);
            }

            // Make sure that the first argument is a number
            if (argNumber == null) {
                return null;
            }
            /*
             * Now make sure that the first argument is a number. If it is not, we need to
             * return the relevant representation of NaN (not a number) so that it can be
             * rendered in the response.
             */
            final double number;
            if (argNumber.isNumber()) {
                // Read the number
                number = argNumber.asDouble();
            } else {
                // The number argument specified is not a number
                number = Double.NaN;
            }

            // Make sure that the picture string is not null
            final JsonNode argPicture = FunctionUtils.getValuesListExpression(expressionVisitor, ctx,
                useContext ? 0 : 1);
            if (argPicture != null) {
                // Check to see if the picture string is just a string
                if (argPicture.isTextual()) {
                    // final double number = argNumber.asDouble();
                    final String picture = argPicture.textValue();

                    // Check to see if we have an optional options argument and
                    // read it if we do
                    DecimalFormatSymbols symbols = Constants.DEFAULT_DECIMAL_FORMAT_SYMBOLS;
                    if (argCount == 3) {
                        final JsonNode argOptions = FunctionUtils.getValuesListExpression(expressionVisitor, ctx,
                            useContext ? 1 : 2);
                        if (argOptions != null && argOptions.isObject()) {
                            symbols = processOptionsArg(argOptions);
                        } else {
                            throw new EvaluateRuntimeException(ERR_ARG3BADTYPE);
                        }
                    }

                    // Create the formatter and format the number
                    DecimalFormat formatter = new DecimalFormat();
                    formatter.setDecimalFormatSymbols(symbols);
                    String fixedPicture = picture.replaceAll("9", "0");
                    formatter.applyLocalizedPattern(fixedPicture);
                    result = new TextNode(formatter.format(number));
                } else {
                    // Non-textual picture argument
                    throw new EvaluateRuntimeException(ERR_ARG2BADTYPE);
                }
            } else {
                // Null picture argument
                throw new EvaluateRuntimeException(ERR_ARG2BADTYPE);
            }
        } else {
            throw new EvaluateRuntimeException(
                argCount == 0 ? ERR_BAD_CONTEXT : argCount == 1 ? ERR_ARG2BADTYPE : ERR_ARG4BADTYPE);
        }

        return result;
    }

    private DecimalFormatSymbols processOptionsArg(JsonNode argOptions) {
        // Create the variable return
        DecimalFormatSymbols symbols = (DecimalFormatSymbols) Constants.DEFAULT_DECIMAL_FORMAT_SYMBOLS.clone();

        // Iterate over the formatting character overrides
        Iterator<String> fieldNames = argOptions.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode valueNode = argOptions.get(fieldName);
            // String value = getFormattingCharacter(valueNode);
            switch (fieldName) {
                case Constants.SYMBOL_DECIMAL_SEPARATOR: {
                    String value = getFormattingCharacter(valueNode, Constants.SYMBOL_DECIMAL_SEPARATOR, true);
                    symbols.setDecimalSeparator(value.charAt(0));
                    break;
                }

                case Constants.SYMBOL_GROUPING_SEPARATOR: {
                    String value = getFormattingCharacter(valueNode, Constants.SYMBOL_GROUPING_SEPARATOR, true);
                    symbols.setGroupingSeparator(value.charAt(0));
                    break;
                }

                case Constants.SYMBOL_INFINITY: {
                    String value = getFormattingCharacter(valueNode, Constants.SYMBOL_INFINITY, false);
                    symbols.setInfinity(value);
                    break;
                }

                case Constants.SYMBOL_MINUS_SIGN: {
                    String value = getFormattingCharacter(valueNode, Constants.SYMBOL_MINUS_SIGN, true);
                    symbols.setMinusSign(value.charAt(0));
                    break;
                }

                case Constants.SYMBOL_NAN: {
                    String value = getFormattingCharacter(valueNode, Constants.SYMBOL_NAN, false);
                    symbols.setNaN(value);
                    break;
                }

                case Constants.SYMBOL_PERCENT: {
                    String value = getFormattingCharacter(valueNode, Constants.SYMBOL_PERCENT, true);
                    symbols.setPercent(value.charAt(0));
                    break;
                }

                case Constants.SYMBOL_PER_MILLE: {
                    String value = getFormattingCharacter(valueNode, Constants.SYMBOL_PER_MILLE, true);
                    symbols.setPerMill(value.charAt(0));
                    break;
                }

                case Constants.SYMBOL_ZERO_DIGIT: {
                    String value = getFormattingCharacter(valueNode, Constants.SYMBOL_ZERO_DIGIT, true);
                    symbols.setZeroDigit(value.charAt(0));
                    break;
                }

                case Constants.SYMBOL_DIGIT: {
                    String value = getFormattingCharacter(valueNode, Constants.SYMBOL_DIGIT, true);
                    symbols.setDigit(value.charAt(0));
                    break;
                }

                case Constants.SYMBOL_PATTERN_SEPARATOR: {
                    String value = getFormattingCharacter(valueNode, Constants.SYMBOL_PATTERN_SEPARATOR, true);
                    symbols.setPatternSeparator(value.charAt(0));
                    break;
                }

                default: {
                    final String msg = String.format(Constants.ERR_MSG_INVALID_OPTIONS_UNKNOWN_PROPERTY,
                        Constants.FUNCTION_FORMAT_NUMBER, fieldName);
                    throw new EvaluateRuntimeException(msg);
                }
            } // SWITCH
        } // WHILE

        return symbols;
    }

    private String getFormattingCharacter(JsonNode valueNode, String propertyName, boolean isChar) {
        // Create the variable to return
        String formattingChar = null;

        // Make sure that we have a valid node and that its content is textual
        if (valueNode != null && valueNode.isTextual()) {
            // Read the value
            String value = valueNode.textValue();
            if (value != null && !value.isEmpty()) {

                // If the target property requires a single char, check the length
                if (isChar) {
                    if (value.length() == 1) {
                        formattingChar = value;
                    } else {
                        final String msg = String.format(Constants.ERR_MSG_INVALID_OPTIONS_SINGLE_CHAR,
                            Constants.FUNCTION_FORMAT_NUMBER, propertyName);
                        throw new EvaluateRuntimeException(msg);
                    }
                } else {
                    formattingChar = value;
                }
            } else {
                final String msgTemplate;
                if (isChar) {
                    msgTemplate = Constants.ERR_MSG_INVALID_OPTIONS_SINGLE_CHAR;
                } else {
                    msgTemplate = Constants.ERR_MSG_INVALID_OPTIONS_STRING;
                }
                final String msg = String.format(msgTemplate, Constants.FUNCTION_FORMAT_NUMBER, propertyName);
                throw new EvaluateRuntimeException(msg);
            }
        } else {
            final String msgTemplate;
            if (isChar) {
                msgTemplate = Constants.ERR_MSG_INVALID_OPTIONS_SINGLE_CHAR;
            } else {
                msgTemplate = Constants.ERR_MSG_INVALID_OPTIONS_STRING;
            }
            final String msg = String.format(msgTemplate, Constants.FUNCTION_FORMAT_NUMBER, propertyName);
            throw new EvaluateRuntimeException(msg);
        }

        return formattingChar;
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
        // accepts a number (or context variable), a string, an optional object, returns
        // a string
        return "<n-so?:s>";
    }
}
