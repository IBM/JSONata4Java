package com.api.jsonata4java.expressions.functions;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Optimized version of {@link FunctionBase} where parameters don't have to be converted
 * from {@link JsonNode} to call context
 */
public interface NoContextFunction {
    JsonNode invoke(JsonNode... elements);
}
