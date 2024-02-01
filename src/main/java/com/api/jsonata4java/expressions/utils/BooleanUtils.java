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
     * based on the casting semantics defined by JSONata.
     * 
     * <div class="block">
     *  <p>
     *    $boolean(arg)
     *  </p>
     *  <table><caption>Casts the argument to a Boolean using the following rules</caption><thead>
     *   <tr><th>Argument type</th><th>Result</th></tr>
     *  </thead><tbody>
     *    <tr><td>Boolean</td><td>unchanged</td></tr>
     *    <tr><td>string: empty</td><td>false</td></tr>
     *    <tr><td>string: non-empty</td><td>true</td></tr>
     *    <tr><td>number: 0</td><td>false</td></tr>
     *    <tr><td>number: non-zero</td><td>true</td></tr>
     *    <tr><td>null</td><td>false</td></tr>
     *    <tr><td>array: empty</td><td>false</td></tr>
     *    <tr><td>array: contains a member that casts to true</td><td>true</td></tr>
     *    <tr><td>array: all members cast to false</td><td>false</td></tr>
     *    <tr><td>object: empty</td><td>false</td></tr>
     *    <tr><td>object: non-empty</td><td>true</td></tr>
     *    <tr><td>function: (functions are not currently supported)</td><td>false</td></tr>
     *   </tbody>
     *  </table>
     * </div>
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
