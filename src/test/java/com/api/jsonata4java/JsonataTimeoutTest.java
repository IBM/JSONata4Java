package com.api.jsonata4java;

import org.junit.Test;
import org.junit.Assert;
import com.api.jsonata4java.expressions.Expressions;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonataTimeoutTest {
    // issue 245
    @Test
    public void testTimeouts() throws Exception {
        long timeout = 100;
        int maxDepth = 100;
        JsonNode jn = new ObjectMapper().readTree("{ \"a\": \"stuff\" }");

        Expressions expr = Expressions.parse("{ \"b\": a }");
        JsonNode result1 = expr.evaluate(jn);
        Assert.assertEquals("stuff", result1.get("b").asText());

        // sleep some time longer than the timeout
        Thread.sleep(timeout * 2);

        JsonNode result2 = expr.evaluate(jn, timeout, maxDepth); // this throws the EvaluateException
        Assert.assertEquals("stuff", result2.get("b").asText());
    }
    
}
