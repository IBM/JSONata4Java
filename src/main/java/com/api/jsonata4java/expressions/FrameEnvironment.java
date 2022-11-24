/**
 * (c) Copyright 2018-2020 IBM Corporation
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
 *	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.api.jsonata4java.expressions;

import java.io.Serializable;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Logger;
import com.api.jsonata4java.expressions.functions.DeclaredFunction;
import com.api.jsonata4java.expressions.functions.FunctionBase;
import com.api.jsonata4java.expressions.utils.Constants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * {@link ExpressionsVisitor} to manage the current block's environment.
 */
public class FrameEnvironment implements Serializable {

    private static final long serialVersionUID = 8451009715230117298L;
    private static final Logger LOG = Logger.getLogger(FrameEnvironment.class.getName());

    private Map<String, DeclaredFunction> _declFunctionMap = new HashMap<String, DeclaredFunction>();
    private FrameEnvironment _enclosingFrame = null;
    private boolean _isAsync = false;
    private Map<String, FunctionBase> _jsonataFunctionMap = new HashMap<>();

    /**
     * This stack is used for storing the current "context" under which to evaluate
     * predicate-type array indexes, e.g. [{"a":1}, {"a":2}][a=2] results in {"a":2}
     * Specifically, it is used to resolve path expressions (a, in the above)
     * against each of the elements in the array in order to figure out which
     * elements match the predicate and which don't.
     * 
     * We use a stack (rather than just a singleton) here, since predicates can be
     * nested inside other predicates, e.g. [{"x":2}, {"x":3}] [x=(["a":101, "b":2},
     * {"a":102, "b":3}][a=101]).b] results in {"x":2}
     */
    private Deque<JsonNode> _stack = new LinkedList<>();
    private JS4JDate _timestamp = null;
    private Map<String, JsonNode> _variableMap = new HashMap<String, JsonNode>();
    private Map<JsonNode, JsonNode> _parentNodeMap = null;

    /**
     * Constructor taking a null enclosing frame to form the root of the environment
     * hierarchy, or an existing enclosing frame
     * 
     * @param enclosingFrame
     */
    public FrameEnvironment(FrameEnvironment enclosingFrame) {
        _enclosingFrame = enclosingFrame;
        _declFunctionMap = new HashMap<String, DeclaredFunction>();
        _jsonataFunctionMap = new HashMap<>();
        _variableMap = new HashMap<String, JsonNode>();
        if (enclosingFrame == null) {
            _timestamp = new JS4JDate();
        }
    }

    public boolean async() {
        return _isAsync;
    }

    public void clearContext() {
        if (_enclosingFrame != null) {
            Deque<JsonNode> stack = _enclosingFrame.getContextStack();
            stack.clear();
            return;
        }
        _stack.clear();
    }

    public Deque<JsonNode> getContextStack() {
        // jump back to original environment to get the "real" stack
        while (_enclosingFrame != null) {
            return _enclosingFrame.getContextStack();
        }
        return _stack;
    }

    public DeclaredFunction getDeclaredFunction(String fctName) {
        DeclaredFunction retFct = _declFunctionMap.get(fctName);
        if (retFct != null || _declFunctionMap.containsKey(fctName)) {
            return retFct;
        }
        if (_enclosingFrame != null) {
            return _enclosingFrame.getDeclaredFunction(fctName);
        }
        return null;
    }

    public FunctionBase getJsonataFunction(String fctName) {
        FunctionBase fct = _jsonataFunctionMap.get(fctName);
        if (fct != null || _jsonataFunctionMap.containsKey(fctName)) {
            return fct;
        }
        if (_enclosingFrame != null) {
            return _enclosingFrame.getJsonataFunction(fctName);
        }
        /**
         * try standard Jsonata functions as last resort having checked all the
         * environments
         */
        fct = Constants.FUNCTIONS.get(fctName);
        if (fct != null) {
            return fct;
        }
        return null;
    }

    public JsonNode getVariable(String varName) {
        if ("$".equals(varName)) {
            // get this from the stack
            if (getContextStack().isEmpty()) {
                return null;
            }
            JsonNode result = getContextStack().peek();
            return result;
        }

        if ("$$".equals(varName)) {
            Deque<JsonNode> stack = getContextStack();
            if (stack.isEmpty()) {
                return null;
            }
            JsonNode result = getContextStack().peekLast();
            return result;
        }

        JsonNode retVar = _variableMap.get(varName);
        if (retVar != null || _variableMap.containsKey(varName)) {
            return retVar;
        }
        if (_enclosingFrame != null) {
            return _enclosingFrame.getVariable(varName);
        }
        return null;
    }

    public boolean isEmptyContext() {
        while (_enclosingFrame != null) {
            return _enclosingFrame.isEmptyContext();
        }
        return _stack.isEmpty();
    }

    public Long millis() {
        if (_timestamp != null) {
            return _timestamp.getTime();
        }
        if (_enclosingFrame != null) {
            return _enclosingFrame.millis();
        }
        return null;
    }

    public String now() {
        if (_timestamp != null) {
            return _timestamp.toString();
        }
        if (_enclosingFrame != null) {
            return _enclosingFrame.now();
        }
        return null;
    }

    public JsonNode peekContext() {
        while (_enclosingFrame != null) {
            return _enclosingFrame.peekContext();
        }
        if (isEmptyContext()) {
            return null;
        }
        return (JsonNode) _stack.peek();
    }

    public JsonNode popContext() {
        while (_enclosingFrame != null) {
            return _enclosingFrame.popContext();
        }
        if (!_stack.isEmpty()) {
            return _stack.pop();
        }
        return null;
    }

    public JsonNode pushContext(JsonNode context) {
        if (_enclosingFrame != null) {
            return _enclosingFrame.pushContext(context);
        }
        _stack.push(context);
        return context;
    }

    public void setAsync(boolean isAsync) {
        _isAsync = isAsync;
    }

    public void setDeclaredFunction(String fctName, DeclaredFunction fctValue) throws EvaluateRuntimeException {
        if (fctName == null) {
            throw new EvaluateRuntimeException("function name is null.");
        }
        if (fctValue == null) {
            throw new EvaluateRuntimeException("function value is null.");
        }
        _declFunctionMap.put(fctName, fctValue);
    }

    public void setJsonataFunction(String fctName, FunctionBase fctValue) throws EvaluateRuntimeException {
        if (fctName == null) {
            throw new EvaluateRuntimeException("function name is null.");
        }
        if (fctValue == null) {
            throw new EvaluateRuntimeException("function value is null.");
        }
        _jsonataFunctionMap.put(fctName, fctValue);
    }

    public void setVariable(String varName, JsonNode varValue) throws EvaluateRuntimeException {
        if (varName == null) {
            throw new EvaluateRuntimeException("variable name is null.");
        }
        // we can allow variables to reference undefined / be null
        // (function-typeOf:case001)
        // so we do not throw a EvaluateRuntimeException in this case
        _variableMap.put(varName, varValue);
    }

    public int sizeContext() {
        while (_enclosingFrame != null) {
            return _enclosingFrame.sizeContext();
        }
        return _stack.size();
    }

    public JsonNode getParentNode(final int depth) {
        if (_parentNodeMap == null) {
            initParentNodeMap();
        }
        if (_parentNodeMap == null) {
            LOG.severe("Problem to evaluate parent path operator \"%\": could not build a parent node map.");
            return null;
        }
        JsonNode parentNode = getContextStack().peek();
        int i = 0;
        while (parentNode != null && i < depth) {
            parentNode = _parentNodeMap.get(parentNode);
            if (parentNode instanceof ArrayNode) {
                parentNode = _parentNodeMap.get(parentNode);
            }
            i++;
        }
        if (i >= depth && parentNode == null) {
            throw new EvaluateRuntimeException(
                "The object representing the 'parent' cannot be derived from this expression");
        }
        return parentNode;
    }

    private void initParentNodeMap() {
        if (getContextStack().peekLast() != null) {
            _parentNodeMap = new HashMap<>();
            indexNodes(_parentNodeMap, getContextStack().peekLast());
        }
    }

    private void indexNodes(Map<JsonNode, JsonNode> parents, JsonNode parent) {
        if (parent.isObject()) {
            for (final JsonNode child : parent) {
                indexNodes(parents, child);
                parents.put(child, parent);
            }
        } else if (parent.isArray()) {
            for (final JsonNode element : parent) {
                indexNodes(parents, element);
                parents.put(element, parent);
            }
        }
    }
}
