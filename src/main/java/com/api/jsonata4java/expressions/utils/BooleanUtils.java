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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class BooleanUtils implements Serializable {

    private static final long serialVersionUID = -5460123458186653599L;

    /**
     * See http://docs.jsonata.org/boolean-functions.html#booleanarg
     * 
     * The convertJsonNodeToBoolean method converts the node passed in to a boolean
     * based on the casting semnatics defined by JSONata.
     * 
     * Boolean: unchanged string: empty false string: non-empty true number: 0 false
     * number: non-zero true null: false array: empty false array: contains a member
     * that casts to true true array: all members cast to false false object: empty
     * false object: non-empty true function: (functions are not currently
     * supported) false
     * 
     * @param node The node to convert to a boolean
     * @return boolean The converted value
     */
    public static boolean convertJsonNodeToBoolean(JsonNode node) {

        if (node == null) {
            return false;
        }
        switch (node.getNodeType()) {
            case BOOLEAN:
                return node.asBoolean();
            case STRING:
                return !node.asText().isEmpty();
            case NUMBER: {
                Double number = node.doubleValue();
                if (number.isNaN() == false && number.isInfinite() == false) {
                    if (number - number.longValue() == 0.0) {
                        return node.asLong() != 0;
                    } else {
                        return node.asDouble() != 0.0d;
                    }
                }
                return false;
            }
            case NULL:
                return false;
            case BINARY:
                return node.asBoolean();
            case ARRAY: {
                // recurse, returning true as soon as we see an element that casts
                // to true
                for (JsonNode e : node) {
                    if (convertJsonNodeToBoolean(e)) {
                        return true;
                    }
                }
                // false, otherwise
                return false;
            }
            case OBJECT:
                return ((ObjectNode) node).elements().hasNext();
            case MISSING:
                return false;
            case POJO:
                return false;
            default:
                return false;
        }
    }
}
