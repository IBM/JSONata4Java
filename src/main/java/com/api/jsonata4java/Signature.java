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

package com.api.jsonata4java;

import java.io.Serializable;
import java.util.Iterator;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.api.jsonata4java.expressions.ExpressionsVisitor;
import com.api.jsonata4java.expressions.functions.DeclaredFunction;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprListContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Manages signature related functions
 */
public class Signature implements Serializable {

    private static final long serialVersionUID = -450755246855587271L;

    // A mapping between the function signature symbols and the full plural of the
    // type
    // Expected to be used in error messages
    static ObjectNode s_arraySignatureMapping = new ObjectNode(JsonNodeFactory.instance);

    static {
        s_arraySignatureMapping.put("a", "arrays");
        s_arraySignatureMapping.put("b", "booleans");
        s_arraySignatureMapping.put("f", "functions");
        s_arraySignatureMapping.put("n", "numbers");
        s_arraySignatureMapping.put("o", "objects");
        s_arraySignatureMapping.put("s", "strings");
    };

    ObjectNode _param = JsonNodeFactory.instance.objectNode();

    ArrayNode _params = JsonNodeFactory.instance.arrayNode();
    ObjectNode _prevParam = _param;
    Pattern _regex = null;
    String _signature = "";

    public Signature(String signature) {
        parseSignature(signature);
    }

    int findClosingBracket(String str, int start, char openSymbol, char closeSymbol) {
        // returns the position of the closing symbol (e.g. bracket) in a string
        // that balances the opening symbol at position start
        int depth = 1;
        int position = start;
        while (position < str.length()) {
            position++;
            char symbol = str.charAt(position);
            if (symbol == closeSymbol) {
                depth--;
                if (depth == 0) {
                    // we're done
                    break; // out of while loop
                }
            } else if (symbol == openSymbol) {
                depth++;
            }
        }
        return position;
    };

    String getSymbol(Object value) {
        String symbol;
        if (value == null) {
            symbol = "m";
        } else {
            // first check to see if this is a function
            if (value instanceof DeclaredFunction || value instanceof Function) {
                symbol = "f";
            } else if (value instanceof JsonNode) {
                switch (((JsonNode) value).getNodeType()) {
                    case STRING: {
                        symbol = "s";
                        break;
                    }
                    case NUMBER: {
                        symbol = "n";
                        break;
                    }
                    case BOOLEAN: {
                        symbol = "b";
                        break;
                    }
                    case NULL: {
                        symbol = "l";
                        break;
                    }
                    case ARRAY: {
                        symbol = "a";
                        break;
                    }
                    case OBJECT: {
                        symbol = "o";
                        break;
                    }
                    default: {
                        // any value can be undefined, but should be allowed to match
                        symbol = "m"; // m for missing
                        break;
                    }
                }
            } else {
                symbol = "m"; // m for missing
            }
        }
        return symbol;
    };

    void next() {
        _params.add(_param);
        _prevParam = _param;
        _param = JsonNodeFactory.instance.objectNode();
    }

    /**
     * Parses a function signature definition and returns a validation function
     * 
     * @param {string}
     *                 signature - the signature between the <angle brackets>
     * @returns validation pattern
     */
    Pattern parseSignature(String signature) {
        // create a Regex that represents this signature and return a function that when
        // invoked,
        // returns the validated (possibly fixed-up) arguments, or throws a validation
        // error
        // step through the signature, one symbol at a time
        int position = 1;
        while (position < signature.length()) {
            char symbol = signature.charAt(position);
            if (symbol == ':') {
                // TODO figure out what to do with the return type
                // ignore it for now
                break;
            }

            switch (symbol) {
                case 's': // string
                case 'n': // number
                case 'b': // boolean
                case 'l': // not so sure about expecting null?
                case 'o': { // object
                    _param.put("regex", "[" + symbol + "m]");
                    _param.put("type", symbol);
                    next();
                    break;
                }
                case 'a': { // array
                    // normally treat any value as singleton array
                    _param.put("regex", "[asnblfom]");
                    _param.put("type", symbol);
                    _param.put("array", true);
                    next();
                    break;
                }
                case 'f': { // function
                    _param.put("regex", "f");
                    _param.put("type", symbol);
                    next();
                    break;
                }
                case 'j': { // any JSON type
                    _param.put("regex", "[asnblom]");
                    _param.put("type", symbol);
                    next();
                    break;
                }
                case 'x': { // any type
                    _param.put("regex", "[asnblfom]");
                    _param.put("type", symbol);
                    next();
                    break;
                }
                case '-': { // use context if _param not supplied
                    _prevParam.put("context", true);
                    _prevParam.put("regex", _prevParam.get("regex").asText() + "?");
                    break;
                }
                case '?': // optional _param
                case '+': { // one or more
                    _prevParam.put("regex", _prevParam.get("regex").asText() + symbol);
                    break;
                }
                case '(': { // choice of types
                    // search forward for matching ')'
                    int endParen = findClosingBracket(signature, position, '(', ')');
                    String choice = signature.substring(position + 1, endParen);
                    if (choice.indexOf("<") == -1) {
                        // no _parameterized types, simple regex
                        _param.put("regex", "[" + choice + "m]");
                    } else {
                        // TODO harder
                        throw new EvaluateRuntimeException("Choice groups containing parameterized types are not supported");
                    }
                    _param.put("type", "(" + choice + ")");
                    position = endParen;
                    next();
                    break;
                }
                case '<': { // type _parameter - can only be applied to 'a' and 'f'
                    JsonNode test = _prevParam.get("type");
                    if (test != null) {
                        String type = test.asText();
                        if (type.equals("a") || type.equals("f")) {
                            // search forward for matching '>'
                            int endPos = findClosingBracket(signature, position, '<', '>');
                            _prevParam.put("subtype", signature.substring(position + 1, endPos));
                            position = endPos;
                        } else {
                            throw new EvaluateRuntimeException("Type parameters can only be applied to functions and arrays");
                        }
                    } else {
                        throw new EvaluateRuntimeException("Type parameters can only be applied to functions and arrays");
                    }
                    break;
                }
            }
            position++;
        } // end while processing symbols in signature

        String regexStr = "^";

        for (Iterator<JsonNode> it = _params.iterator(); it.hasNext();) {
            ObjectNode param = (ObjectNode) it.next();
            regexStr += "(" + param.get("regex").asText() + ")";
        }
        regexStr += "$";

        _regex = null;
        try {
            _regex = Pattern.compile(regexStr);
            _signature = regexStr;
        } catch (PatternSyntaxException pse) {
            throw new EvaluateRuntimeException(pse.getLocalizedMessage());
        }
        return _regex;
    }

    void throwValidationError(ExprListContext badArgs, String badSig, String functionName) {
        // to figure out where this went wrong we need apply each component of the
        // regex to each argument until we get to the one that fails to match
        String partialPattern = "^";

        int goodTo = 0;
        for (int index = 0; index < _params.size(); index++) {
            partialPattern += ((ObjectNode) _params.get(index)).get("regex");
            Pattern tester = Pattern.compile(partialPattern);
            Matcher match = tester.matcher(badSig);
            if (match.matches() == false) {
                // failed here
                throw new EvaluateRuntimeException("Argument \"" + (goodTo + 1) + "\" of function \"" + functionName
                    + "\" does not match function signature");
            }
            goodTo = match.end();
        }
        // if it got this far, it's probably because of extraneous arguments (we
        // haven't added the trailing '$' in the regex yet.
        throw new EvaluateRuntimeException(
            "Argument \"" + (goodTo + 1) + "\" of function \"" + functionName + "\" does not match function signature");
    }

    ArrayNode validate(String functionName, ExprListContext args, ExpressionsVisitor expressionVisitor) {
        ArrayNode result = JsonNodeFactory.instance.arrayNode();
        String suppliedSig = "";
        for (Iterator<ExprContext> it = args.expr().iterator(); it.hasNext();) {
            ExprContext arg = it.next();
            suppliedSig += getSymbol(arg);
        }
        Matcher isValid = _regex.matcher(suppliedSig);
        if (isValid != null) {
            ArrayNode validatedArgs = JsonNodeFactory.instance.arrayNode();
            int argIndex = 0;
            int index = 0;
            for (Iterator<JsonNode> it = _params.iterator(); it.hasNext();) {
                ObjectNode param = (ObjectNode) it.next();
                JsonNode arg = expressionVisitor.visit(args.expr(argIndex));
                String match = isValid.group(index + 1);
                if ("".equals(match)) {
                    boolean useContext = (param.get("context") != null && param.get("context").asBoolean());
                    if (useContext) {
                        // substitute context value for missing arg
                        // first check that the context value is the right type
                        JsonNode context = expressionVisitor.getVariable("$");
                        String contextType = getSymbol(context);
                        // test contextType against the regex for this arg (without the trailing ?)
                        if (Pattern.matches(param.get("regex").asText(), contextType)) {
                            validatedArgs.add(context);
                        } else {
                            // context value not compatible with this argument
                            throw new EvaluateRuntimeException("Context value is not a compatible type with argument \""
                                + argIndex + 1 + "\" of function \"" + functionName + "\"");
                        }
                    } else {
                        validatedArgs.add(arg);
                        argIndex++;
                    }
                } else {
                    // may have matched multiple args (if the regex ends with a '+'
                    // split into single tokens
                    String[] singles = match.split("");
                    for (String single : singles) {
                        if ("a".equals(param.get("type").asText())) {
                            if ("m".equals(single)) {
                                // missing (undefined)
                                arg = null;
                            } else {
                                arg = expressionVisitor.visit(args.expr(argIndex));
                                boolean arrayOK = true;
                                // is there type information on the contents of the array?
                                String subtype = "undefined";
                                JsonNode testSubType = param.get("subtype");
                                if (testSubType != null) {
                                    subtype = testSubType.asText();
                                    if ("a".equals(single) == false && match.equals(subtype) == false) {
                                        arrayOK = false;
                                    } else if ("a".equals(single)) {
                                        ArrayNode argArray = (ArrayNode) arg;
                                        if (argArray.size() > 0) {
                                            String itemType = getSymbol(argArray.get(0));
                                            if (itemType.equals(subtype) == false) { // TODO recurse further
                                                arrayOK = false;
                                            } else {
                                                // make sure every item in the array is this type
                                                ArrayNode differentItems = JsonNodeFactory.instance.arrayNode();
                                                for (Object val : argArray) {
                                                    if (itemType.equals(getSymbol(val)) == false) {
                                                        differentItems.add(expressionVisitor.visit((ExprListContext) val));
                                                    }
                                                }
                                                ;
                                                arrayOK = (differentItems.size() == 0);
                                            }
                                        }
                                    }
                                }
                                if (!arrayOK) {
                                    JsonNode type = s_arraySignatureMapping.get(subtype);
                                    if (type == null) {
                                        type = JsonNodeFactory.instance.nullNode();
                                    }
                                    throw new EvaluateRuntimeException("Argument \"" + (argIndex + 1) + "\" of function \""
                                        + functionName + "\" must be an array of \"" + type.asText() + "\"");
                                }
                                // the function expects an array. If it's not one, make it so
                                if ("a".equals(single) == false) {
                                    ArrayNode wrappedArg = JsonNodeFactory.instance.arrayNode();
                                    wrappedArg.add(arg);
                                    arg = wrappedArg;
                                }
                            }
                            validatedArgs.add(arg);
                            argIndex++;
                        } else {
                            validatedArgs.add(arg);
                            argIndex++;
                        }
                    }
                }
                index++;
            }
            return validatedArgs;
        }
        throwValidationError(args, suppliedSig, functionName);
        // below just for the compiler as a runtime exception is thrown above
        return result;
    };

}
