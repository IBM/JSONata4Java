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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;
import com.api.jsonata4java.JSONataUtils;
import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.api.jsonata4java.expressions.ExpressionsVisitor;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.utils.Constants;
import com.api.jsonata4java.expressions.utils.FunctionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

@SuppressWarnings("unused")
public class UnpackFunction extends FunctionBase {

    private static final long serialVersionUID = 1415132777843221239L;

    public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_UNPACK);
    public static String ERR_ARG1BADTYPE = "Argument 1 of function $unpack does not match function signature";
    public static String ERR_ARG2BADTYPE = "Argument 2 of function $unpack does not match function signature";
    public static String ERR_ARG1INVALIDHEX = "Argument 1 of function $unpack is not valid hexadecimal";
    public static String ERR_ARG2INVALIDPATTERN = "Argument 2 of function $unpack is not valid decode pattern";
    public static String ERR_ARG3BADTYPE = "Argument 3 of function $unpack does not match function signature";

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
        // Create the variable to return
        JsonNode result = null;

        // Retrieve the number of arguments
        JsonNode argInput = JsonNodeFactory.instance.nullNode();
        boolean useContext = FunctionUtils.useContextVariable(this, ctx, getSignature());
        int argCount = getArgumentCount(ctx);
        if (useContext) {
            argInput = FunctionUtils.getContextVariable(expressionVisitor);
            if (argInput != null && argInput.isNull() == false) {
                argCount++;
            } else {
                useContext = false;
            }
        }

        // Make sure that we have the right number of arguments
        if (argCount == 2) {
            if (!useContext) {
                argInput = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, 0);
            }
            final JsonNode argDecodePattern = FunctionUtils.getValuesListExpression(expressionVisitor, ctx,
                useContext ? 0 : 1);

            if (argInput == null)
                return null;
            if (argDecodePattern == null)
                return null;
            if (!argInput.isTextual())
                throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
            if (!argDecodePattern.isTextual())
                throw new EvaluateRuntimeException(ERR_ARG2BADTYPE);

            // first, attempt to read in the input as hex-encoded binary data
            byte[] decoded = null;
            try {
                decoded = JSONataUtils.hexDecode(argInput.asText());
            } catch (Exception e) {
                throw new EvaluateRuntimeException(ERR_ARG1INVALIDHEX);
            }

            final String argDecodePatternStr = argDecodePattern.textValue();

            // now interpret the decode pattern string
            // in this first pass of the function, this is a single token
            // indicating the output type
            final DecodePattern decodePattern = DecodePattern.fromString(argDecodePatternStr);

            result = decodePattern.decode(decoded);

        } else {
            throw new EvaluateRuntimeException(argCount == 0 ? ERR_BAD_CONTEXT : ERR_ARG3BADTYPE);
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
        // accepts an object (or context variable), returns an array of strings
        return "<o-:a<s>>";
    }

    private final static Map<String, SupportedDatatype> REGISTRY = new HashMap<>();

    enum SupportedDatatype {

            _double("64f") {

                @Override
                public JsonNode decode(ByteBuffer bb) {
                    return JsonNodeFactory.instance.numberNode(bb.getDouble());
                }
            },
            _long("64") {

                @Override
                public JsonNode decode(ByteBuffer bb) {
                    return JsonNodeFactory.instance.numberNode(bb.getLong());
                }
            },
            _float("32f") {

                @Override
                public JsonNode decode(ByteBuffer bb) {
                    return JsonNodeFactory.instance.numberNode(bb.getFloat());
                }
            },
            _int("32") {

                @Override
                public JsonNode decode(ByteBuffer bb) {
                    return JsonNodeFactory.instance.numberNode(bb.getInt());
                }
            },
            _short("16") {

                @Override
                public JsonNode decode(ByteBuffer bb) {
                    return JsonNodeFactory.instance.numberNode(bb.getShort());
                }
            },
            _byte("8") {

                @Override
                public JsonNode decode(ByteBuffer bb) {
                    return JsonNodeFactory.instance.numberNode(bb.get());
                }
            };

        private final String token;

        private SupportedDatatype(String token) {
            this.token = token;
            REGISTRY.put(token, this);
        }

        public abstract JsonNode decode(ByteBuffer bb);

        public static SupportedDatatype fromToken(String token) {
            return REGISTRY.get(token);
        }

    }

    public static class DecodePattern {

        private final boolean isBigEndian;

        private final SupportedDatatype datatype;

        public DecodePattern(boolean isBigEndian, SupportedDatatype datatype) {
            super();
            this.isBigEndian = isBigEndian;
            this.datatype = datatype;
        }

        public JsonNode decode(byte[] input) {
            ByteBuffer bb = ByteBuffer.wrap(input);
            bb.order(this.isBigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
            return this.datatype.decode(bb);
        }

        public static DecodePattern fromString(String str) throws EvaluateRuntimeException {
            // sanitize
            str = str.trim().toLowerCase();
            final char endianness = str.charAt(0);
            final String datatypeToken = str.substring(1);

            final boolean bigEndian;

            switch (endianness) {
                case 'l':
                    bigEndian = false;
                    break;
                case 'b':
                    bigEndian = true;
                    break;
                default:
                    // can't actually happen
                    throw new EvaluateRuntimeException(ERR_ARG2INVALIDPATTERN);
            }

            final SupportedDatatype datatype = SupportedDatatype.fromToken(datatypeToken);
            if (datatype == null) {
                throw new EvaluateRuntimeException(ERR_ARG2INVALIDPATTERN);
            }

            return new DecodePattern(bigEndian, datatype);
        }
    }

}
