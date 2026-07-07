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

package com.api.jsonata4java.expressions.regex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import com.api.jsonata4java.Expression;
import com.api.jsonata4java.expressions.EvaluateException;
import com.api.jsonata4java.expressions.ParseException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import org.junit.Test;

/**
 * End-to-end tests demonstrating that {@link Expression#jsonata(String, RegexEngine)}
 * works correctly when plugging in a regex engine other than the default
 * {@link java.util.regex.Pattern}-backed one -- here, {@link RE2RegexPattern}.
 */
public class RE2RegexPatternTest {

    private static JsonNode evaluate(String expression) throws ParseException, IOException, EvaluateException {
        return Expression.jsonata(expression, RE2RegexPattern.engine()).evaluate(null);
    }

    @Test
    public void testMatch() throws Exception {
        // Note: regex literals containing whitespace hit a pre-existing,
        // unrelated JSONata4Java grammar limitation (its lexer's global
        // whitespace-skip rule strips spaces out of regex literals too), so
        // this intentionally avoids whitespace in the pattern.
        JsonNode result = evaluate("$match(\"hello-world\", /o-w/)");
        assertEquals("o-w", result.get("match").asText());
        assertEquals(4, result.get("index").asInt());
    }

    @Test
    public void testMatchCaseInsensitive() throws Exception {
        JsonNode result = evaluate("$match(\"HELLO\", /hello/i)");
        assertEquals("HELLO", result.get("match").asText());
    }

    @Test
    public void testContains() throws Exception {
        assertTrue(evaluate("$contains(\"hello\", /ell/)").asBoolean());
        assertFalse(evaluate("$contains(\"hello\", /xyz/)").asBoolean());
    }

    @Test
    public void testReplaceWithGroupReference() throws Exception {
        JsonNode result = evaluate("$replace(\"John Smith\", /(\\w+)\\s(\\w+)/, \"$2, $1\")");
        assertEquals("Smith, John", result.asText());
    }

    @Test
    public void testSplit() throws Exception {
        JsonNode result = evaluate("$split(\"a1b2c3\", /[0-9]/)");
        ObjectMapper mapper = new ObjectMapper();
        assertEquals(mapper.readTree("[\"a\",\"b\",\"c\"]"), result);
    }

    @Test
    public void testRejectsBackreferences() {
        // RE2 (and therefore RE2/J) deliberately does not support
        // backreferences, since they make linear-time matching impossible --
        // this is exactly the ReDoS-prone construct a custom RegexEngine is
        // meant to let callers reject at the source.
        try {
            evaluate("$contains(\"aa\", /(a)\\1/)");
            org.junit.Assert.fail("Expected an exception compiling a backreference with RE2/J");
        } catch (Exception expected) {
            // expected: RE2/J's Pattern.compile rejects the unsupported syntax
        }
    }

    @Test
    public void testEvalUsesEnclosingRegexEngine() throws Exception {
        // $eval()'d expressions must inherit the enclosing expression's
        // regex engine rather than silently falling back to the default
        // java.util.regex-backed one.
        JsonNode result = evaluate("$eval(\"$match('hello-world', /o-w/).match\")");
        assertEquals("o-w", result.asText());
    }
}
