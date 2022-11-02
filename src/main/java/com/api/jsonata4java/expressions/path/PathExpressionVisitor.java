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

package com.api.jsonata4java.expressions.path;

import java.util.List;
import java.util.ListIterator;
import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.api.jsonata4java.expressions.path.generated.PathExpressionParser.Array_indexContext;
import com.api.jsonata4java.expressions.path.generated.PathExpressionParser.PathContext;
import com.api.jsonata4java.expressions.path.generated.PathExpressionParserBaseVisitor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@SuppressWarnings("unused")
public abstract class PathExpressionVisitor extends PathExpressionParserBaseVisitor<JsonNode> {

    public static final String ERR_FIELD_ON_NON_OBJECT(String fieldName) {
        return new StringBuilder().append("Attempt to reference field ").append(fieldName).append(" of a non-object")
            .toString();
    }

    public static final String ERR_INDEX_ON_NON_ARRAY(String fieldName, int index) {
        return new StringBuilder().append("Cannot reference index ").append(index).append(" on non-array field ")
            .append(fieldName).toString();
    }

    public static final String ERR_ARR_INDEX_OUT_OF_BOUNDS(String fieldName, int index) {
        return new StringBuilder().append("Index ").append(index).append(" is out of bounds for the array ")
            .append(fieldName).toString();
    }

    public static final String ERR_INDEX_VAR_UNSET = "Reference made to unset variable $index";

    private final Integer indexVarValue;

    private JsonNode json;

    private JsonNode currentNode;

    public PathExpressionVisitor(JsonNode json, Integer indexVarValue) {
        this.json = json;
        this.indexVarValue = indexVarValue;

        this.currentNode = json;
    }

    private void updateCurrentNodePointer(JsonNode n) {
        this.currentNode = n;
    }

    private JsonNode getCurrentNodePointer() {
        return this.currentNode;
    }

    @SuppressWarnings("deprecation")
    @Override
    public JsonNode visitPath(PathContext ctx) {
        final String fieldName = ctx.id().txt.getText();

        if (getCurrentNodePointer() == null) {
            // cannot happen
            throw new EvaluateRuntimeException("Unexpected null pointer");
        }

        if (!getCurrentNodePointer().isObject()) {
            throw new EvaluateRuntimeException(ERR_FIELD_ON_NON_OBJECT(fieldName));
        }
        final ObjectNode currentObject = (ObjectNode) getCurrentNodePointer();

        // Determine if this path specifies a chain of array indexes (e.g. [1], [1][1])
        List<Array_indexContext> ctx_indexes = ctx.array_index();

        if (ctx_indexes == null || ctx_indexes.isEmpty()) {
            // this is a terminal or non-terminal path element with no array index

            if (ctx.rhs == null) {
                // this is a terminal path element with no array index, e.g. a.b.c
                // ^
                return reachedObjectField(currentObject, fieldName);
            } else {
                // this is a non-terminal path element with no array index, e.g. a.b.c
                // ^
                JsonNode nextNode = currentObject.get(fieldName);

                if (nextNode == null) {
                    // may be null if the current position in the path corresponds to an optional
                    // property in the schema
                    // if this happens, use the
                    nextNode = new ObjectNode(JsonNodeFactory.instance);
                    currentObject.put(fieldName, nextNode);
                }

                updateCurrentNodePointer(nextNode);
                return visit(ctx.rhs);
            }

        } else {

            JsonNode nextNode = currentObject.get(fieldName);
            if (nextNode == null) {
                // may be null if the current position in the path corresponds to an optional
                // property in the schema
                // if this happens, add in an empty placeholder
                nextNode = new ArrayNode(JsonNodeFactory.instance);
                currentObject.put(fieldName, nextNode);

                // NOTE: an out-of-bounds exception is inevitable (since there is no index that
                // can be in-bounds for an empty array)
                // nevertheless, to ensure consistency we'll continue and let it be detected by
                // the code below
            }

            // as we iterate through chains of array indexes, we append them to this
            // e.g. a -> a[0] -> a[0][1]
            // this is purely so that generated error messages (e.g. out of bounds,
            // non-array field)
            // refer to the problematic index attempt (e.g. "a[0]") rather than the
            // top-level object field name (e.g. "a").

            String fieldNamePlusLeadingIndexes = fieldName;

            final ListIterator<Array_indexContext> it = ctx_indexes.listIterator();

            while (it.hasNext()) {
                // this is a terminal or non-terminal path element with an array index
                Array_indexContext ctx_index = it.next();

                Integer index = null;

                if (ctx_index.NUMBER() != null) {
                    index = Integer.valueOf(ctx_index.NUMBER().getText());
                } else {
                    // cannot happen - shouldn't have parsed
                    throw new RuntimeException();
                }

                if (!nextNode.isArray()) {
                    throw new EvaluateRuntimeException(ERR_INDEX_ON_NON_ARRAY(fieldNamePlusLeadingIndexes, index));
                }
                final ArrayNode currentArray = (ArrayNode) nextNode;

                if (index >= currentArray.size()) {
                    throw new EvaluateRuntimeException(ERR_ARR_INDEX_OUT_OF_BOUNDS(fieldNamePlusLeadingIndexes, index));
                }

                if (it.hasNext()) {
                    // indexes left in chain, e.g. a.b.c[0][1][1]
                    // ^
                    fieldNamePlusLeadingIndexes += "[" + index + "]";
                    nextNode = currentArray.get(index);
                } else {
                    // we're at the last index in the chain
                    if (ctx.rhs == null) {
                        // this is the last index in a chain on the terminal path element, e.g.
                        // a.b.c[0][0]
                        // ^
                        return reachedArrayIndex(currentArray, index);
                    } else {
                        // this is the last index in a chain on a non-terminal path element, e.g.
                        // a.b[0][0].c
                        // ^

                        updateCurrentNodePointer(currentArray.get(index));
                        return visit(ctx.rhs);
                    }
                }

            } // while

            throw new RuntimeException("This can never happen at runtime, this is just to keep the compiler happy");
        }

    }

    // allow behaviour of visitor to be adapted for both getting and setting fields
    // based on a path
    protected abstract JsonNode reachedObjectField(ObjectNode object, String fieldName);

    protected abstract JsonNode reachedArrayIndex(ArrayNode array, int index);

    /*
     * TODO: do we want to add more coercion support (and should supported coercions
     * be the same as those supported by JSONata)? Examples: string -> boolean
     * boolean -> string single value -> singleton array singleton array -> single
     * value ... etc
     * 
     */

    public static class Setter extends PathExpressionVisitor {

        private final JsonNode valueToAssign;

        public Setter(JsonNode json, Integer indexVarValue, JsonNode valueToAssign) {
            super(json, indexVarValue);
            this.valueToAssign = valueToAssign;
        }

        @Override
        protected JsonNode reachedObjectField(ObjectNode object, String fieldName) {
            object.set(fieldName, valueToAssign);
            return valueToAssign;
        }

        @Override
        protected JsonNode reachedArrayIndex(ArrayNode array, int index) {
            array.set(index, valueToAssign);
            return valueToAssign;
        }
    }

    public static class Getter extends PathExpressionVisitor {

        public Getter(JsonNode json, Integer indexVarValue) {
            super(json, indexVarValue);
        }

        @Override
        protected JsonNode reachedObjectField(ObjectNode object, String fieldName) {
            return object.get(fieldName);
        }

        @Override
        protected JsonNode reachedArrayIndex(ArrayNode array, int index) {
            return array.get(index);
        }
    }

    public static void main(String[] args) {

    }

}
