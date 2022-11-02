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
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Sequence implements Serializable {

    private static final long serialVersionUID = 9212862402936258721L;

    ArrayNode _array = JsonNodeFactory.instance.arrayNode();
    boolean _sequence = true;
    boolean _keepSingleton = false;
    int _length = 0;

    public Sequence() {
    }

    protected Sequence(List<JsonNode> arg) {
        this();
        if (arg != null && arg.size() == 1) {
            push(arg.get(0));
        }
    }

    public int push(JsonNode obj) {
        _array.add(obj);
        _length = _array.size();
        return _length;
    }

    public void keepSingleton(boolean keepSingleton) {
        _keepSingleton = keepSingleton;
    }

    public void sequence(boolean sequence) {
        _sequence = sequence;
    }

    public void setSize(int size) {
        _array = JsonNodeFactory.instance.arrayNode(size);
        _length = 0;
    }

    public void put(int index, JsonNode obj) {
        _array.set(index, obj);
    }

    public static void main(String[] args) {
    }

    public String toString() {
        ObjectNode obj = JsonNodeFactory.instance.objectNode();
        ArrayNode array = JsonNodeFactory.instance.arrayNode();
        array.addAll(_array);
        obj.set("array", array);
        obj.put("sequence", _sequence);
        obj.put("length", _length);
        obj.put("keepSingleton", _keepSingleton);
        return obj.toString();
    }
}
