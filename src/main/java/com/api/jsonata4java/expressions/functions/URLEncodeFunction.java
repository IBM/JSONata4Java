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

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
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
 * $length(str)
 * 
 * Returns the number of characters in the string str. If str is not specified
 * (i.e. this function is invoked with no arguments), then the context value is
 * used as the value of str. An error is thrown if str is not a string.
 * 
 * Examples
 * 
 * $length("Hello World")==11
 *
 */
public class URLEncodeFunction extends FunctionBase {

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_URL_ENCODE);
    public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_URL_ENCODE);
    public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_URL_ENCODE);

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
        if (argCount == 1) {
            if (!useContext) {
                argString = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, 0);
            }
            if (argString == null) {
                return null;
            }
            if (argString.isTextual()) {
                final String str = argString.textValue();
                try {
                    String strResult = "";
                    URL url = new URL(str);
                    String query = url.getQuery();
                    if (query != null) {
                        int offset = str.indexOf(query);
                        if (offset > 0) {
                            strResult = str.substring(0, offset);
                            result = new TextNode(strResult + encodeURI(query));
                        }
                    } else {
                        result = new TextNode(str);
                    }
                } catch (MalformedURLException e) {
                    throw new EvaluateRuntimeException("Malformed URL passed to " + Constants.FUNCTION_URL_ENCODE + ": \"" + str + "\"");
                }
            } else {
                throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
            }
        } else {
            throw new EvaluateRuntimeException(argCount == 0 ? ERR_BAD_CONTEXT : ERR_ARG2BADTYPE);
        }

        return result;
    }

    String encodeURI(String uri) {
        String result = null;
        if (uri != null) {
            try {
                // https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/encodeURI
                // Not encoded: A-Z a-z 0-9 ; , / ? : @ & = + $ - _ . ! ~ * ' ( ) #
                result = URLEncoder.encode(uri, "UTF-8").replaceAll("\\+", "%20")
                    .replaceAll("%20", " ").replaceAll("\\%21", "!")
                    .replaceAll("\\%23", "#").replaceAll("\\%24", "$")
                    .replaceAll("\\%26", "&").replaceAll("\\%27", "'")
                    .replaceAll("\\%28", "(").replaceAll("\\%29", ")")
                    .replaceAll("\\%2A", "*").replaceAll("\\%2B", "+")
                    .replaceAll("\\%2C", ",").replaceAll("\\%2D", "-")
                    .replaceAll("\\%2E", ".").replaceAll("\\%2F", "/")
                    .replaceAll("\\%3A", ":").replaceAll("\\%3B", ";")
                    .replaceAll("\\%3D", "=").replaceAll("\\%3F", "?")
                    .replaceAll("\\%40", "@").replaceAll("\\%5F", "_")
                    .replaceAll("\\%7E", "~");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
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
        // accepts a string (or context variable), returns a number
        return "<s-:n>";
    }
}
