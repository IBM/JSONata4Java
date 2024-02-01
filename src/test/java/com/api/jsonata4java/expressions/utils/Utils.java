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

package com.api.jsonata4java.expressions.utils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;
import org.junit.Assert;
import com.api.jsonata4java.expressions.EvaluateException;
import com.api.jsonata4java.expressions.Expressions;
import com.api.jsonata4java.expressions.ParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Utils implements Serializable {

    private static final long serialVersionUID = -143332860982622898L;

    private static final char[] symbols;

    static {
        StringBuilder tmp = new StringBuilder();
        for (char ch = '0'; ch <= '9'; ++ch)
            tmp.append(ch);
        for (char ch = 'a'; ch <= 'z'; ++ch)
            tmp.append(ch);
        symbols = tmp.toString().toCharArray();
    }

    private final static Random random = new Random();

    public static String generateOrgId() {
        final char[] buf = new char[6];

        for (int i = 0; i < buf.length; i++) {
            buf[i] = symbols[random.nextInt(symbols.length)];
        }

        return new String(buf);
    }

    public static JsonNode getJson(String filePath) throws JsonProcessingException, IOException {
        ObjectMapper m = new ObjectMapper();
        return m.readTree(new File(filePath));
    }

    public static final ObjectMapper mapper = new ObjectMapper();

    public static void simpleTest(String expression, String expected) throws Exception {
        test(expression, expected, null, null);
    }

    public static void simpleTest(String expression, int expected, JsonNode jsonObj)
        throws ParseException, EvaluateException, IOException {
        JsonNode expectObj = JsonNodeFactory.instance.numberNode(expected);
        simpleTest(expression, expectObj, jsonObj);
    }

    public static void simpleTest(String expression, String expected, JsonNode jsonObj)
        throws ParseException, EvaluateException, IOException {
        JsonNode expectObj = JsonNodeFactory.instance.textNode(expected);
        simpleTest(expression, expectObj, jsonObj);
    }

    public static void simpleTest(String expression, ObjectNode expected, JsonNode jsonObj)
        throws ParseException, EvaluateException, IOException {
        Expressions expr = Expressions.parse(expression);
        JsonNode result = expr.evaluate(jsonObj);
        Assert.assertEquals(mapper.writeValueAsString(expected), (mapper.writeValueAsString(result)));
    }

    public static void simpleTest(String expression, JsonNode expected, JsonNode jsonObj)
        throws ParseException, EvaluateException, IOException {
        Expressions expr = Expressions.parse(expression);
        JsonNode result = expr.evaluate(jsonObj);
        Assert.assertEquals(mapper.writeValueAsString(expected), mapper.writeValueAsString(result));
    }

    public static void simpleTestExpectException(String expression, String expectedExceptionMessage) throws Exception {
        test(expression, (JsonNode) null, expectedExceptionMessage, null);
    }

    /**
     * Tests evaluation of the expression against the rootContext to determine if we get the expected
     * results or exception to be thrown
     * @param expression expression to be tested
     * @param expected result expected
     * @param expectedExceptionMsg the text of an expected exception
     * @param rootContext the json object against which the expression is evaluated
     * @throws Exception if expected values are not provided
     */
    public static void test(String expression, String expected, String expectedExceptionMsg, String rootContext)
        throws Exception {
        test(expression, expected != null ? mapper.readTree(expected) : null, expectedExceptionMsg,
            rootContext == null ? null : mapper.readTree(rootContext));
    }

    /**
     * Tests evaluation of the expression against the rootContext to determine if we get the expected
     * results or exception to be thrown
     * @param expression expression to be tested
     * @param expected result expected
     * @param expectedEvaluateExceptionMsg the text of an expected exception
     * @param rootContext the json object against which the expression is evaluated
     * @throws Exception if expected values are not provided
     */
    public static void test(String expression, JsonNode expected, String expectedEvaluateExceptionMsg,
        JsonNode rootContext) throws Exception {
        if (expected != null)
            expected = ensureAllIntegralsAreLongs(expected);
        if (rootContext != null)
            rootContext = ensureAllIntegralsAreLongs(rootContext);

        Expressions e = null;
        try {
            e = Expressions.parse(expression);
        } catch (ParseException pe) {
            if (expectedEvaluateExceptionMsg == null) {
                Assert.fail("EvaluateException was thrown but was not expected: " + pe.getLocalizedMessage());
            } else {
                if (expectedEvaluateExceptionMsg.equals(pe.getClass().getName()) == false) {
                    pe.printStackTrace();
                    throw pe;
                }
                System.err.println("* " + expression + " threw exception: " + pe.getClass().getName());
                return;
            }
        }
        JsonNode actual = null;
        try {
            actual = e.evaluate(rootContext);
            if (expectedEvaluateExceptionMsg != null) {
                Assert.fail("EvaluateException \"" + expectedEvaluateExceptionMsg
                    + "\" was not thrown but we expected it to be. Got actual=\"" + actual + "\"");
            }
            Assert.assertEquals(expected, actual);
        } catch (EvaluateException ex) {
            if (expectedEvaluateExceptionMsg == null) {
                throw ex;
            } else {
                Assert.assertEquals("EvaluateException was thrown but expected message: "
                    + expectedEvaluateExceptionMsg + "> got message <" + ex.getLocalizedMessage() + ">",
                    expectedEvaluateExceptionMsg, ex.getMessage());
            }
        }

    }

    /**
     * 
     * Return an JSON such that any integral numbers in input (including those
     * nested inside other arrays/objects) are of type LongNode. All other node
     * types are untouched.
     * 
     * Our expressions visitor returns all integral numbers as Longs. This method is
     * used for normalizing expected results produced using mapper.readTree() for
     * comparison against actual results using .equals().
     * 
     * @param input ObjectNode whose fields will be converted to longs
     * @return updated ObjectNode with converted integers to long values
     */
    @SuppressWarnings("unchecked")
    public static <X extends JsonNode> X ensureAllIntegralsAreLongs(JsonNode input) {
        switch (input.getNodeType()) {
            case OBJECT: {
                ObjectNode output = JsonNodeFactory.instance.objectNode();
                Iterator<Entry<String, JsonNode>> it = ((ObjectNode) input).fields();
                while (it.hasNext()) {
                    Entry<String, JsonNode> e = it.next();
                    String field = e.getKey();
                    JsonNode value = e.getValue();
                    output.set(field, ensureAllIntegralsAreLongs(value));
                }
                return (X) output;
            }
            case ARRAY: {
                ArrayNode output = JsonNodeFactory.instance.arrayNode();
                for (JsonNode a : (ArrayNode) input) {
                    output.add(ensureAllIntegralsAreLongs(a));
                }
                return (X) output;
            }
            case NUMBER: {

                if (input.isIntegralNumber()) {
                    if (input.isBigInteger()) {
                        return (X) input;
                    } else {
                        return (X) new LongNode(input.longValue());
                    }
                } else {
                    return (X) input;
                }
            }
            default: {
                // add everything else as-is
                return (X) input;
            }
        }
    }

    public static String readFile(String filePath) throws Exception {
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        String s = new String(bytes, StandardCharsets.UTF_8);
        return s;
    }

    public static JsonNode toJson(String json) throws Exception {
        return new ObjectMapper().readTree(json);
    }

}
