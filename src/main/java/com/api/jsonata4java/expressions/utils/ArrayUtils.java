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
import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ValueNode;

public class ArrayUtils implements Serializable {

    private static final long serialVersionUID = -6185850803487454927L;

    /**
     * Determines if the supplied array contains all numeric values
     * 
     * @param arr the array to be evaluated.
     * @return true if the supplied array contains all numeric values. If arr is not
     *         an array, returns false.
     */
    public static boolean isArrayOfNumbers(JsonNode arr) {
        boolean result = false;
        if (arr.isArray()) {
            ArrayNode array = (ArrayNode) arr;
            int i = 0;
            for (i = 0; i < array.size(); i++) {
                JsonNode cell = array.get(i);
                if (!cell.isNumber()) {
                    break;
                }
            }
            if (i == array.size()) {
                result = true;
            }
        }
        return result;
    }

    /**
     * Determines if the supplied array contains all String values
     * 
     * @param arr the array to be evaluated.
     * @return true if the supplied array contains all String values. If arr is not
     *         an array, returns false.
     */
    public static boolean isArrayOfStrings(JsonNode arr) {
        boolean result = false;
        if (arr.isArray()) {
            ArrayNode array = (ArrayNode) arr;
            int i = 0;
            for (i = 0; i < array.size(); i++) {
                JsonNode cell = array.get(i);
                if (!cell.isTextual()) {
                    break;
                }
            }
            if (i == array.size()) {
                result = true;
            }
        }
        return result;
    }

    /**
     * Default comparison function for $sort
     * 
     * @param left  string or number to be compared
     * @param right string or number to be compared
     * @return true is left is greater than right
     * @throws EvaluateRuntimeException if left and right cannot be converted to
     *                                  numbers
     */
    public static boolean compare(JsonNode left, JsonNode right) throws EvaluateRuntimeException {
        boolean result = false;
        if (left.isTextual() && right.isTextual()) {
            result = left.asText().compareTo(right.asText()) > 0;
        } else {
            ValueNode a = NumberUtils.convertNumberToValueNode(left.asText());
            ValueNode b = NumberUtils.convertNumberToValueNode(right.asText());
            result = Double.valueOf(a.doubleValue()).compareTo(Double.valueOf(b.doubleValue())) > 0;
        }
        return result;
    }

    /**
     * If n is an array, it will be returned untouched. Otherwise, return a
     * singleton array containing the value
     * 
     * @param n JsonNode being checked
     * @return corresponding array made from the input n
     */
    public static ArrayNode ensureArray(JsonNode n) {
        // null ("*no match*) maps to an empty array
        if (n == null) {
            return JsonNodeFactory.instance.arrayNode();
        } else if (n.isNull()) {
            return JsonNodeFactory.instance.arrayNode().add((NullNode) null);
        } else if (n.getNodeType() == JsonNodeType.ARRAY) {
            return (ArrayNode) n;
        } else {
            return JsonNodeFactory.instance.arrayNode().add(n);
        }
    }

    /**
     * Merges the left, then the right array into the resulting array, preserving
     * the element's order
     * 
     * @param left  first array to be merged
     * @param right second array to be merged
     * @return merged array containing the elements of left then the elements of
     *         right
     */
    public static ArrayNode push(ArrayNode left, JsonNode right) {
        ArrayNode result = JsonNodeFactory.instance.arrayNode();
        if (left != null && right != null && left.isArray()) {
            for (int i = 0; i < left.size(); i++) {
                result.add(left.get(i));
            }
            if (right.isArray()) {
                for (int i = 0; i < right.size(); i++) {
                    result.add(right.get(i));
                }
            } else {
                result.add(right);
            }
        }
        return result;
    }

    /**
     * Returns a shallow copy of the portion of the supplied array from start to end
     * (not including end)
     * 
     * @param array
     * @param start
     * @param end
     * @return a shallow copy of the portion of the supplied array from start to end
     *         (not including end)
     */
    public static ArrayNode slice(ArrayNode array, int start, int end) {
        ArrayNode result = JsonNodeFactory.instance.arrayNode();
        if (array != null && array.isArray() && start < end && end < array.size()) {
            for (int i = start; i < end; i++) {
                result.add(array.get(i));
            }
        }
        return result;
    }

    /**
     * Returns a shallow copy of the portion of the supplied array from start
     * through the end of the array
     * 
     * @param array input array to be sliced
     * @param start starting index where the slice should occur
     * @return a shallow copy of the portion of the supplied array from start
     *         through the end of the array
     */
    public static ArrayNode slice(ArrayNode array, int start) {
        ArrayNode result = JsonNodeFactory.instance.arrayNode();
        if (array != null && array.isArray() && start < array.size()) {
            for (int i = start; i < array.size(); i++) {
                result.add(array.get(i));
            }
        }
        return result;
    }
}
