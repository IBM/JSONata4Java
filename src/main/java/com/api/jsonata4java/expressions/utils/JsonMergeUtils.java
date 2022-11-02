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

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * A utility for merging arbitrary JSON trees. Used during default state
 * generation to combine explicit and implicit default values where necessary.
 */
public class JsonMergeUtils implements Serializable {

    private static final long serialVersionUID = 6579954130589053721L;

    /**
     * Returns x "merged into" y
     * 
     * If x is null (java null, not JSON null!), return y
     * 
     * If y is (java null, not JSON null!), return x
     * 
     * If both x and y are primitive (including JSON null!), return x (i.e. values
     * in x take precedence over those in y)
     * 
     * If x and y are of different types, return x
     * 
     * If x and y are both objects, merge each field of x and y
     * 
     * If an array field is set in both x and y, merge will be run on each element
     * in turn. the length of the returned array will be max( length(x), length(y) )
     * 
     * 
     * See src/test/resources/JsonMergeUtilsTest.json for examples.
     * 
     * @param x Json object
     * @param y Json object
     * @return merged Json object
     */
    public static JsonNode merge(JsonNode x, JsonNode y) {
        if (x == null) {
            return y;
        } else if (y == null) {
            return x;
        } else if (x.isObject() && y.isObject()) {
            return merge((ObjectNode) x, (ObjectNode) y);
        } else if (x.isArray() && y.isArray()) {
            return merge((ArrayNode) x, (ArrayNode) y);
        } else {
            return x;
        }
    }

    private static ObjectNode merge(ObjectNode x, ObjectNode y) {

        final Iterator<Map.Entry<String, JsonNode>> yit = y.fields();
        while (yit.hasNext()) {
            final Map.Entry<String, JsonNode> ye = yit.next();
            final String f = ye.getKey();
            final JsonNode yv = ye.getValue();
            final JsonNode xv = x.get(f);
            x.set(f, merge(xv, yv));
        }

        // and return the merged view
        return x;

    }

    private static ArrayNode merge(ArrayNode x, ArrayNode y) {
        final ArrayNode output = JsonNodeFactory.instance.arrayNode();

        for (int i = 0; i < x.size() || i < y.size(); i++) {
            JsonNode xv = i < x.size() ? x.get(i) : null;
            JsonNode yv = i < y.size() ? y.get(i) : null;
            output.add(merge(xv, yv));

        }
        return output;

    }
}
