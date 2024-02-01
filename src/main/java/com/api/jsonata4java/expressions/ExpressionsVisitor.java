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

package com.api.jsonata4java.expressions;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.CommonTokenFactory;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.apache.commons.text.StringEscapeUtils;
import com.api.jsonata4java.expressions.functions.DeclaredFunction;
import com.api.jsonata4java.expressions.functions.FunctionBase;
import com.api.jsonata4java.expressions.generated.MappingExpressionBaseVisitor;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ArrayContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Array_constructorContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.BooleanContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Context_refContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprListContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprOrSeqContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Fct_chainContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.FieldListContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_declContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.IdContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.NullContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.NumberContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ObjectContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Object_constructorContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Parent_pathContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.PathContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Root_pathContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.SeqContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.StringContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Var_recallContext;
import com.api.jsonata4java.expressions.utils.BooleanUtils;
import com.api.jsonata4java.expressions.utils.Constants;
import com.api.jsonata4java.expressions.utils.FunctionUtils;
import com.api.jsonata4java.expressions.utils.NumberUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class ExpressionsVisitor extends MappingExpressionBaseVisitor<JsonNode> implements Serializable {

    private static final long serialVersionUID = -9182723073149137352L;

    /**
     * This is how we indicate to upstream operators that we are currently inside a
     * selection statement. E.g. [{"a":1}, {"a":2}}].a will return this special
     * subclass of array node. This is so that upstream operators (e.g. array
     * indexes) can have special handling applied to them.
     * 
     * For instance: [{"a":1}, {"a":2}}].a[0] does not return [1], but rather [1,2]
     * 
     * Parenthesis can be used to drop this context, e.g.: ([{"a":1},
     * {"a":2}}]).a[0] returns [1]
     */
    public static class SelectorArrayNode extends ArrayNode {

        private static final long serialVersionUID = -641395411309729158L;

        private List<JsonNode> selectionGroups = new ArrayList<>();

        public SelectorArrayNode(JsonNodeFactory nc) {
            super(nc);
        }

        /**
         * Adds the specified elements to two lists: 1. Adds flattened results to array
         * - operators that don't care about selection groups will just see this as a
         * normal array
         * 
         * 2. Adds non-flattened results to the separately-maintained selectionGroups
         * list. This will be used selector-aware operators (e.g. array index) to apply
         * appropriate semantics
         * 
         * @param group JsonNode containing the group definitions
         */
        public void addAsSelectionGroup(JsonNode group) {
            if (group.isArray()) {
                this.addAll((ArrayNode) group);
            } else {
                this.add(group);
            }
            selectionGroups.add(group);
        }

        public List<JsonNode> getSelectionGroups() {
            return Collections.unmodifiableList(this.selectionGroups);
        }

    }

    static private final String _pattern = "#0.##";
    static private final DecimalFormat _decimalFormat = new DecimalFormat(_pattern);
    static private final String CLASS = ExpressionsVisitor.class.getName();
    static public final String ERR_MSG_INVALID_PATH_ENTRY = String.format(Constants.ERR_MSG_INVALID_PATH_ENTRY,
        (Object[]) null);
    static public final String ERR_NEGATE_NON_NUMERIC = "Cannot negate a non-numeric value";
    static public final String ERR_SEQ_LHS_INTEGER = "The left side of the range operator (..) must evaluate to an integer";
    static public final String ERR_SEQ_RHS_INTEGER = "The right side of the range operator (..) must evaluate to an integer";
    // note: below should read 1e7 not 1e6
    static public final String ERR_TOO_BIG = "The size of the sequence allocated by the range operator (..) must not exceed 1e6.  Attempted to allocate ";
    static private final Logger LOG = Logger.getLogger(CLASS);
    static private final Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    static private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * This defines the behavior of the "=" and "in" operators
     * 
     * @param left
     * @param right
     * @return
     */
    private static boolean areJsonNodesEqual(JsonNode left, JsonNode right) {
        if (left.isFloatingPointNumber() || right.isFloatingPointNumber()) {
            return (left.asDouble() == right.asDouble());
        } else if (left.isDouble() || right.isDouble()) {
            return (left.asDouble() == right.asDouble());
        } else if (left.isIntegralNumber() && right.isIntegralNumber()) {
            return left.asLong() == right.asLong();
        } else if (left.isLong() && right.isLong()) {
            return left.asLong() == right.asLong();
        } else if (left.isNull()) {
            return right.isNull();
        } else if (right.isNull()) {
            return left.isNull();
        } else if (left.isTextual() && right.isTextual()) {
            return (left.asText().equals(right.asText()));
        } else if (left.isArray() && right.isArray()) {
            return (left.equals(right));
        } else if (left.isObject() && right.isObject()) {
            return (left.equals(right));
        } else if (left.isBoolean() && right.isBoolean()) {
            return left == right;
        } else {
            // any other permutation of types can never be equal
            return false;
        }
    }

    /**
     * Encodes JSONata string casting semantics. See
     * http://docs.jsonata.org/string-functions.html:
     * 
     * $string(arg)
     * 
     * Casts the arg parameter to a string using the following casting rules
     * 
     * Strings are unchanged Functions are converted to an empty string Numeric
     * infinity and NaN throw an error because they cannot be represented as a JSON
     * number All other values are converted to a JSON string using the
     * JSON.stringify function If arg is not specified (i.e. this function is
     * invoked with no arguments), then the context value is used as the value of
     * arg.
     * 
     * Examples
     * 
     * $string(5) == "5" [1..5].$string() == ["1", "2", "3", "4", "5"]
     * 
     * @param node     JsonNode whose content is to be cast as a String
     * @param prettify Whether the objects or arrays should be pretty printed
     * @throws EvaluateRuntimeException if json serialization fails
     * @return the String representation of the supplied node
     */
    public static String castString(JsonNode node, boolean prettify) throws EvaluateRuntimeException {
        if (node == null) {
            return null;
        }

        switch (node.getNodeType()) {
            case STRING: {
                return node.textValue();
            }
            case NUMBER: {
                if (node.isDouble()) {
                    Double dValue = node.asDouble();
                    if (Double.isInfinite(dValue) || Double.isNaN(dValue)) {
                        throw new EvaluateRuntimeException("Attempting to invoke string function on Infinity or NaN");
                    }
                    String test = dValue.toString();
                    // try to determine precision
                    String strVal = "";
                    int index = test.indexOf("E");
                    String expStr = ".e+";
                    if (index >= 0) {
                        int minusSize = dValue < 0.0d ? 1 : 0;
                        int exp = Integer.parseInt(test.substring(index + 1));
                        if (exp < 0) {
                            expStr = ".e";
                        }
                        // how many digits before E?
                        int len = test.indexOf(".");
                        if (len - minusSize + Math.abs(exp) <= 21) {
                            if (exp > 0) {
                                strVal = _decimalFormat.format(dValue);
                                return strVal;
                            }
                            if (exp > -7) {
                                strVal = new BigDecimal(dValue, new MathContext(15)).toString().toLowerCase();
                                if (strVal.indexOf(".") >= 0) {
                                    while (strVal.endsWith("0") && strVal.length() > 0) {
                                        strVal = strVal.substring(0, strVal.length() - 1);
                                    }
                                }
                                return strVal;
                            }
                        }
                        // need to check for ".0E" to make it the whole number e+exp
                        if (test.indexOf(".0E") > 0) {
                            strVal = test.substring(0, len) + expStr.substring(1) + test.substring(index + 1);
                        } else {
                            strVal = test.substring(0, index) + expStr + test.substring(index + 1);
                        }
                    } else {
                        if (prettify) {
                            strVal = _decimalFormat.format(dValue);
                        } else {
                            strVal = new BigDecimal(dValue, new MathContext(15)).toString().toLowerCase();
                            if (strVal.indexOf(".") >= 0) {
                                while (strVal.endsWith("0") && strVal.length() > 0) {
                                    strVal = strVal.substring(0, strVal.length() - 1);
                                }
                            }
                        }
                    }
                    return strVal;
                }
            }

            default:
                // arrays and objects
                try {
                    if (prettify) {
                        JsonElement jsonElt = JsonParser.parseString(objectMapper.writeValueAsString(node));
                        return gson.toJson(jsonElt);

                    } else {
                        return objectMapper.writeValueAsString(node);
                    }
                } catch (JsonProcessingException e) {
                    throw new EvaluateRuntimeException(
                        "Failed to cast value " + node + " to a string. Reason: " + e.getMessage());
                }
        }

    }

    /**
     * If input is an array, return it. If input is not an array, wrap it in a
     * singleton array and return it.
     * 
     * @param input
     * @return wrapped content ensured to be an array
     */
    public static ArrayNode ensureArray(JsonNode input) {
        if (input == null) {
            return null;
        } else if (input.isArray()) {
            return (ArrayNode) input;
        } else {
            return JsonNodeFactory.instance.arrayNode().add(input);
        }
    }

    /**
     * If input is an array, return it. If input is not an array, wrap it in a
     * singleton array and return it.
     * 
     * @param input
     * @return wrapped content ensured to be an array
     */
    public static SelectorArrayNode ensureSelectorNodeArray(JsonNode input) {
        if (input == null) {
            return null;
        } else if (input.isArray()) {
            if (input instanceof SelectorArrayNode) {
                return (SelectorArrayNode) input;
            } else {
                SelectorArrayNode result = new SelectorArrayNode(JsonNodeFactory.instance);
                for (Object elt : (ArrayNode) input) {
                    if (elt != null) {
                        result.addAsSelectionGroup((JsonNode) elt);
                    }
                }
                return result;
            }
        } else {
            SelectorArrayNode result = new SelectorArrayNode(JsonNodeFactory.instance);
            result.addAsSelectionGroup(input);
            return result;
        }
    }

    static public ArrayNode flatten(JsonNode arg, ArrayNode flattened) {
        if (flattened == null) {
            flattened = new ArrayNode(JsonNodeFactory.instance);
        }
        if (arg.isArray()) {
            for (Iterator<JsonNode> it = ((ArrayNode) arg).iterator(); it.hasNext();) {
                flatten(it.next(), flattened);
            }
        } else {
            flattened.add(arg);
        }
        return flattened;
    }

    private static boolean isWholeNumber(double n) {
        return n == Math.rint(n) && !Double.isInfinite(n) && !Double.isNaN(n);
    }

    /**
     * Returns true iff the input is already an integral number OR it is a float
     * that is exactly equal to an integral number (e.g. 2.0 NOT 2.00001)
     * 
     * Any other type of input will return false
     * 
     * @param n
     * @return
     */
    private static boolean isWholeNumber(JsonNode n) {
        if (n == null) {
            return false;
        } else if (n.isInt() || n.isLong()) {
            return true;
        } else if ((n.isFloat() || n.isDouble()) && (n.asInt() == n.asDouble())) {
            return true;
        } else {
            return false;
        }
    }

    private static String sanitise(String str) {

        // strip any surrounding quotes
        // issue #247 added length check
        if (str.length() > 1 && ((str.startsWith("`") && str.endsWith("`")) || (str.startsWith("\"") && str.endsWith("\""))
            || (str.startsWith("'") && str.endsWith("'")))) {
            str = str.substring(1, str.length() - 1);
        }

        // unescape any special chars
        str = StringEscapeUtils.unescapeJson(str);

        return str;
    }

    /**
     * If input is a singleton array, return its single element - otherwise return
     * the input as is.
     * 
     * @param input
     * @return single element if input is a singleton array or the input as
     *         presented
     */
    public static JsonNode unwrapArray(JsonNode input) {
        if (input == null) {
            return null;
        } else if (input.isArray()) {
            if (input.size() == 1) {
                return input.get(0);
            } else {
                return input;
            }
        } else {
            return input;
        }
    }

    private FrameEnvironment _environment = null;
    private boolean checkRuntime = false;
    private int currentDepth = 0;
    private JsonNodeFactory factory = JsonNodeFactory.instance;
    private boolean firstStep = false;
    private boolean firstStepCons = false;
    private Deque<Boolean> inArrayConstructStack = new LinkedList<Boolean>();
    private boolean keepArray = false;
    private boolean lastStep = false;
    private boolean lastStepCons = false;
    private int maxDepth = -1;
    private long maxTime = 0L;
    private long startTime = System.currentTimeMillis();
    private List<ParseTree> steps = new ArrayList<ParseTree>();
    private ParseTreeProperty<Integer> values = new ParseTreeProperty<Integer>();
    private String lastVisited = "";

    public ExpressionsVisitor() {
        setEnvironment(null);
        setRootContext(null);
    }

    public ExpressionsVisitor(JsonNode rootContext, FrameEnvironment environment) throws EvaluateRuntimeException {
        setEnvironment(environment);
        setRootContext(rootContext);
    }

    public FrameEnvironment setNewEnvironment() {
        // generate a new frameEnvironment for life of this block
        FrameEnvironment oldEnvironment = _environment;
        _environment = new FrameEnvironment(_environment);
        return oldEnvironment;
    }

    public void resetOldEnvironment(FrameEnvironment oldEnvironment) {
        _environment = oldEnvironment;
    }

    JsonNode append(JsonNode arg1, JsonNode arg2) {
        // disregard undefined args
        if (arg1 == null) {
            return arg2;
        }
        if (arg2 == null) {
            return arg1;
        }
        // if either argument is not an array, make it so
        if (!arg1.isArray()) {
            ArrayNode tempArray = new ArrayNode(JsonNodeFactory.instance);
            tempArray.add(arg1);
            arg1 = tempArray;
        }
        if (!arg2.isArray()) {
            ArrayNode tempArray = new ArrayNode(JsonNodeFactory.instance);
            tempArray.add(arg2);
            arg2 = tempArray;
        }
        return ((ArrayNode) arg1).add(((ArrayNode) arg2));
    }

    private void checkRunaway() {
        if (checkRuntime) {
            if (maxDepth != -1 && currentDepth > maxDepth) {
                throw new EvaluateRuntimeException(
                    "Stack overflow error: Check for non-terminating recursive function. Consider rewriting as tail-recursive.");
            }
        }
        if (maxTime != 0 && (System.currentTimeMillis() - startTime > maxTime)) {
            throw new EvaluateRuntimeException("Expression evaluation timeout: Check for infinite loop");
        }
    }

    private void evaluateEntry() {
        currentDepth++;
        checkRunaway();
    }

    private void evaluateExit() {
        currentDepth--;
        // should not happen but never say never...
        if (currentDepth < 0) {
            currentDepth = 0;
        }
        checkRunaway();
    }

    public Deque<JsonNode> getContextStack() {
        return _environment.getContextStack();
    }

    public DeclaredFunction getDeclaredFunction(String fctName) {
        return _environment.getDeclaredFunction(fctName);
    }

    /**
     * Grab the current data from the stack and recursively copy its content into an
     * array
     * 
     * @return array of the descendant content of the current data on the stack
     */
    JsonNode getDescendants() {
        JsonNode result = null;
        ArrayNode resultArray = new ArrayNode(JsonNodeFactory.instance);
        if (_environment.isEmptyContext() == false) {
            JsonNode startingElt = _environment.peekContext();
            if (startingElt != null) {
                traverseDescendants(startingElt, resultArray);
                if (resultArray.size() == 1) {
                    result = resultArray.get(0);
                } else {
                    result = resultArray;
                }
            }
        }
        return result;
    }

    protected FrameEnvironment getEnvironment() {
        return _environment;
    }

    public FunctionBase getJsonataFunction(String fctName) {
        return _environment.getJsonataFunction(fctName);
    }

    public int getValue(ParseTree node) {
        return values.get(node);
    }

    public JsonNode getVariable(String varName) {
        return _environment.getVariable(varName);
    }

    boolean isSequence(JsonNode node) {
        if (node != null && node instanceof SelectorArrayNode) {
            return true;
        }
        return false;
    }

    JsonNode lookup(JsonNode input, String key) {
        JsonNode result = null;
        if (input.isArray()) {
            result = new SelectorArrayNode(factory); // factory.arrayNode();
            for (int ii = 0; ii < input.size(); ii++) {
                JsonNode res = lookup(input.get(ii), key);
                if (res != null) {
                    if (res.isArray()) {
                        ((SelectorArrayNode) result).addAll((ArrayNode) res);
                    } else {
                        ((SelectorArrayNode) result).add(res);
                    }
                }
            }
        } else if (input != null && input instanceof ObjectNode) {
            result = ((ObjectNode) input).get(key);
        }
        return result;
    }

    protected void processArrayContent(ExprOrSeqContext expr, ArrayNode output) {
        // have a comma separated list so iterate through the expressions, skipping
        // commas
        String testStr = "";
        if (expr.children != null) {
            for (Iterator<ParseTree> it = expr.children.iterator(); it.hasNext();) {
                ParseTree tree = it.next();
                if (tree == null) {
                    System.out.println("Got null in array values.");
                    continue;
                }
                if (tree instanceof ExprOrSeqContext) {
                    processArrayContent((ExprOrSeqContext) tree, output);
                }
                if (tree instanceof TerminalNodeImpl) {
                    testStr = tree.toString();
                    if (",[]".indexOf(testStr) >= 0) {
                        continue;
                    }
                }
                JsonNode result = visit(tree);
                if (result != null) {
                    if ("visitArray_constructor".equals(lastVisited)) {
                        output.add(result);
                        //					} else if ("visitId".equals(lastVisited)) {
                        //						output.add(result);
                        //					} else if ("visitPath".equals(lastVisited)) {
                        //								output.add(result);
                    } else {
                        result = ensureArray(result);
                        output.addAll((ArrayNode) result);
                    }

                    // if (/* tree instanceof Array_constructorContext == false && */
                    // inArrayConstructStack.isEmpty() && result.isArray()) {
                    // output.addAll((ArrayNode) result);
                    // } else {
                    // output.add(result);
                    // }
                }
            }
        }
    }

    /**
     * Any negative indexes are resolved to start from sizeOfSourceArray. Once
     * resolved, the indexes are then sorted into ascending order
     * 
     * @param input
     * @param sizeOfSourceArray
     * @return
     */
    private List<Integer> resolveIndexes(List<Integer> input, int sizeOfSourceArray) {
        // first we need to resolve negative indexes (they should start from the
        // end of this selection group)
        List<Integer> resolvedIndexes = new ArrayList<>();
        for (int i : input) {
            if (i < 0) {
                resolvedIndexes.add(sizeOfSourceArray + i);// remember, index is
                // negative
            } else {
                resolvedIndexes.add(i);
            }
        }

        // now, sort the resolved indexes into ascending order.
        // This is required so that the order of the resulting array is the same
        // as the order of the source array these indexes are references into.
        // e.g. ["a","b","c"][[2,1,0]] -> ["a","b","c"] (not ["c", "b", "a"])
        Collections.sort(resolvedIndexes);

        return resolvedIndexes;

    }

    /**
     * Given some expression on the lhs of a path, return all values that match the
     * rhs of the path. (Pulled out to a separate method so that I can use recursion
     * for handling nested arrays - see below)
     * 
     * If lhs=null, return null (e.g. null.a==null)
     * 
     * If lhs is an object, push LHS onto the stack and visit RHS. Ultimately, this
     * will result in visitId being called with LHS at the top of the stack. E.g.
     * {"a":1}.a==1
     * 
     * if lhs is an array, recurse on each element of the array (e.g. [[{"a":1},
     * {"a":2}]].a==[1,2])
     * 
     * @param lhs
     * @param rhsCtx
     * @return
     */
    private JsonNode resolvePath(JsonNode lhs, ExprContext rhsCtx) {
        final String METHOD = "resolvePath";
        if (LOG.isLoggable(Level.FINEST))
            LOG.entering(CLASS, METHOD, new Object[] {
                lhs, rhsCtx.getText()
            });

        // If the LHS is an array we need to select over all of its elements
        // push each onto the context stack and visit the RHS

        JsonNode output;

        if (lhs == null) {
            output = null;
        } else if (lhs.isArray()) {

            // we need to return this result as a special type of array node
            // this is so we know to apply special handling if the (array) result
            // of this is indexed
            // e.g. [ {"a":[1,2]}, {"a":[3,4]} ].a==[1,2,3,4] (not 1 as one might
            // expect)

            SelectorArrayNode arr = new SelectorArrayNode(factory);
            output = arr;

            for (JsonNode lhsE : lhs) {
                JsonNode rhsE = resolvePath(lhsE, rhsCtx);
                if (rhsE != null) {
                    arr.addAsSelectionGroup(rhsE);
                }
            }
        } else {
            // the LHS is just an object
            _environment.pushContext(lhs);
            output = visit(rhsCtx);
            _environment.popContext();
            if (output != null && output.isArray() == false) {
                SelectorArrayNode arr = new SelectorArrayNode(factory);
                arr.addAsSelectionGroup(output);
                output = arr;
            }
        }

        JsonNode result = output;

        if (LOG.isLoggable(Level.FINEST))
            LOG.exiting(CLASS, METHOD, result);
        return result;
    }

    public void setDeclaredFunction(String fctName, DeclaredFunction fctValue) {
        _environment.setDeclaredFunction(fctName, fctValue);
    }

    protected void setEnvironment(FrameEnvironment environment) {
        if (environment == null) {
            environment = new FrameEnvironment(null);
        }
        _environment = environment;
    }

    public void setJsonataFunction(String fctName, FunctionBase fctValue) {
        _environment.setJsonataFunction(fctName, fctValue);
    }

    protected void setRootContext(JsonNode rootContext) {
        this._environment.clearContext();
        if (rootContext != null) {
            this._environment.pushContext(rootContext);
        }
    }

    public void setValue(ParseTree node, int value) {
        values.put(node, value);
    }

    public void setVariable(String varName, JsonNode varValue) throws EvaluateRuntimeException {
        _environment.setVariable(varName, varValue);
    }

    public void timeboxExpression(long timeoutMS, int maxDepth) {
        // issue 245 reset startTime for new delay measurement
        startTime = System.currentTimeMillis();
        // issue 245 reset currentDepth for new delay measurement
        currentDepth = 0;
        if (timeoutMS > 0L && maxDepth > 0) {
            this.maxDepth = maxDepth;
            this.maxTime = timeoutMS;
            checkRuntime = true;
        }
    }

    /**
     * Recursively traverse the descendants of the input and them in the results
     * array
     * 
     * @param input   the object, array, or node whose descendants are sought
     * @param results the array containing the descendants of the input and all of
     *                their descendants
     */
    void traverseDescendants(JsonNode input, ArrayNode results) {
        if (input != null) {
            if (!input.isArray()) {
                // put this element into the results
                results.add(input);
            }
            // now process the "descendants" (array elements, or nodes from keys of an object)
            if (input.isArray()) {
                for (Iterator<JsonNode> it = ((ArrayNode) input).iterator(); it.hasNext();) {
                    JsonNode member = it.next();
                    traverseDescendants(member, results);
                }
            } else if (input.isObject()) {
                for (Iterator<String> it = ((ObjectNode) input).fieldNames(); it.hasNext();) {
                    String key = it.next();
                    traverseDescendants(((ObjectNode) input).get(key), results);
                }
            }
        } // else don't process null
    }

    @Override
    public JsonNode visit(ParseTree tree) {
        JsonNode result = null;

        if (checkRuntime) {
            evaluateEntry();
        }
        if (steps.size() > 0 && tree.equals(steps.get(steps.size() - 1))) {
            lastStep = true;
        }

        result = super.visit(tree);

        if (!keepArray && tree instanceof MappingExpressionParser.To_arrayContext == false) {
            if (result != null && result instanceof SelectorArrayNode) {
                SelectorArrayNode san = (SelectorArrayNode) result;
                if (san.size() == 1 && san.selectionGroups.size() == 1) {
                    JsonNode test = san.selectionGroups.get(0);
                    if (test instanceof SelectorArrayNode) {
                        result = result.get(0);
                    } else {
                        result = test;
                    }
                } else if (result.size() == 1) {
                    result = result.get(0);
                }
            }
        }
        if (checkRuntime) {
            evaluateExit();
        }
        return result;
    }

    @Override
    public JsonNode visitAddsub_op(MappingExpressionParser.Addsub_opContext ctx) {
        final String METHOD = "visitAddsub_op";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                ctx.getText(), ctx.depth()
            });
        }
        JsonNode result = null;
        JsonNode leftNode = visit(ctx.expr(0)); // get value of left subexpression
        JsonNode rightNode = visit(ctx.expr(1)); // get value of right
        // subexpression

        // in all cases, if either are *no match*, JSONata returns *no match*
        if (leftNode != null && rightNode != null) {

            if (!leftNode.isNumber() || !rightNode.isNumber()) {
                throw new EvaluateRuntimeException(ctx.op.getText() + " expects two numeric arguments");
            }

            // treat both inputs as doubles when performing arithmetic operations
            double left = leftNode.asDouble();
            double right = rightNode.asDouble();

            final double dResult;

            if (ctx.op.getType() == MappingExpressionParser.ADD) {
                dResult = left + right;
            } else if (ctx.op.getType() == MappingExpressionParser.SUB) {
                dResult = left - right;
            } else {
                // should never happen (this expression should not have parsed in the
                // first place)
                throw new EvaluateRuntimeException("Unrecognised token " + ctx.op.getText());
            }

            // coerce the result to a long iff the result is exactly .0
            if (isWholeNumber(dResult)) {
                result = new LongNode((long) dResult);
            } else {
                result = new DoubleNode(dResult);
            }
        }
        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    @Override
    public JsonNode visitArray(ArrayContext ctx) {
        final String METHOD = "visitArray";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                ctx.getText(), ctx.depth()
            });
        }
        JsonNode result = null;

        // we expect exactly two expressions
        // expr ARR_START expr ARR_END
        // $state.arr[$event.i]
        if (ctx.expr().size() != 2) {
            // belt&braces - expressions like this should not parse in the first
            // place
            throw new EvaluateRuntimeException("invalid array expression");
        }

        // LHS expression (e.g. $event.aaa, [1,2,3,4,5])
        // If the LHS is not an array, it will be treated as a singleton array
        // e.g. {"a":0}[0] = [{"a":0}][0] = {"a":0}
        ArrayNode sourceArray = ensureArray(visit(ctx.expr(0)));
        if (sourceArray != null) {
            // Expression inside [] (e.g. [1])
            ExprContext indexContext = ctx.expr(1);

            // this will contain a list of indexes that should be pulled out of the
            // source array
            // any non-integral array indexes will rounded down in this list
            // may contain negative values (these will be normalized later)
            final List<Integer> indexesToReturn = new ArrayList<>();

            ArrayNode output = JsonNodeFactory.instance.arrayNode();
            boolean isPredicate = false;

            boolean haveResult = false;
            for (int i = 0; ((haveResult == false) && (i < sourceArray.size())); i++) {
                JsonNode e = sourceArray.get(i);

                if (LOG.isLoggable(Level.FINEST))
                    LOG.logp(Level.FINEST, CLASS, METHOD, "Evaluating array index expression '" + indexContext.getText()
                        + "' against element at index " + i + " ('" + e + "') of source array");

                // this will cause any expressions to be evaluated with this element of
                // the array as context
                // in particular, path expressions will resolve relative to the element
                // e.g. [{"a":1}, {"a":2}][a=1] - the path expression "a" will resolve
                // to 1 (for the first element) and 2 (for the second)
                // we need a stack because predicates can be used inside predicates,
                // e.g. [{"x":2}, {"x":3}] [x=(["a":101, "b":2}, {"a":102,
                // "b":3}][a=101]).b] -> {"x":2}

                // check for predicate
                _environment.pushContext(e);
                JsonNode indexesInContext = factory.arrayNode();
                // below will resolve if the proposed indexContext applies to the current group
                indexesInContext = visit(indexContext);
                _environment.popContext();
                if (indexesInContext == null) {
                    // this means that the index is a predicate, but one or more of the
                    // path expressions used in it
                    // do not match anything in the current context - e.g.
                    // [{"b":1}][a=1]
                    // skip, and move on to the next element
                    isPredicate = true;
                } else if (indexesInContext.isBoolean()) {
                    // if, in context the index evaluates to a boolean node, we know to
                    // treat this index as a predicate statement
                    isPredicate = true;

                    // if it's true then the predicate matches this element, so include
                    // it
                    if (indexesInContext == BooleanNode.TRUE) {
                        indexesToReturn.add(i);
                    }
                } else {
                    // if it resolves to any other type of node, we know that the index
                    // cannot be a predicate statement

                    // wrap it in an array (if it's not already an array)
                    indexesInContext = ensureArray(indexesInContext);

                    // now round down any non-integral indexes
                    for (JsonNode indexInContext : indexesInContext) {
                        // if it's an integral number, just add it as is
                        if (indexInContext.isIntegralNumber() || indexInContext.isLong()) {
                            indexesToReturn.add(indexInContext.asInt());
                        } else if (indexInContext.isFloatingPointNumber() || indexInContext.isDouble()) {
                            // If the number is not an integer it is rounded down to an
                            // integer according to JSONATA spec
                            indexesToReturn.add((int) Math.floor(indexInContext.asDouble()));
                        } else {
                            // JSONata has some weird rules regarding non-numeric array
                            // indexes
                            // e.g. [0,1]["h"] -> [0,1] ... [0,1][""] -> null
                            // I think we should just throw an exception when the
                            // expression language
                            // is used in such an odd way
                            // if we need strict JSONata compliance in this respect then
                            // we can change this
                            if (indexInContext.isBoolean()) {
                                if (indexInContext.asBoolean()) {
                                    result = sourceArray;
                                    haveResult = true;
                                    break;
                                } else {
                                    if (indexesInContext.size() == 1) {
                                        result = null;
                                        haveResult = true;
                                        break;
                                    }
                                    result = sourceArray; // null;
                                    haveResult = true;
                                    break;
                                }
                            } else {
                                if (indexInContext.isArray()) {
                                    ArrayNode indexArray = (ArrayNode) indexInContext;
                                    boolean isOkay = false;
                                    for (int j = 0; j < indexArray.size(); j++) {
                                        if (indexArray.get(j).asBoolean()) {
                                            isOkay = true;
                                            break;
                                        }
                                    }
                                    if (isOkay) {
                                        result = sourceArray;
                                        haveResult = true;
                                        break;
                                    } else {
                                        result = null;
                                        haveResult = true;
                                        break;
                                    }
                                }
                                throw new NonNumericArrayIndexException();
                            }
                        }
                    }

                    // because we know it's not a predicate we also know it cannot have
                    // any path references in it
                    // this means that it's value is independent of context
                    // thus we only need to evaluate the index once
                    break;
                }
            }

            if (!haveResult) {
                // now construct the return value based on the indexes computed above

                // Is this a (non-predicate) index into the result of a selection? If so,
                // the semantics are slightly different to
                // an ordinary array index:
                //
                // [[{"a":1}, {"a":2}, {"a":3}], [{"a":4}, {"a":5}], [{"a":6}]].a[n] ->
                // our selection result will look like this:
                // [ [1,2,3], [4,5], [6] ]
                // ^ group
                // n specifies the index (or set of indexes) to pull out from each group,
                // e.g.:
                // n=0 -> [1,4,6]
                // n=1 -> [2,5]
                // n=2 -> [3]
                // n=[0,1] -> [1,2,4,5,6]
                // n=[0,1,2] -> [1,2,3,4,5,6]
                // n=[1,2] -> [2,3,5]

                // some groups may not be arrays, e.g. [{"a":1}, {"a":2},{"a":[3,4]}].a[n] ->
                // [1,2,[1,2]]
                // n=0 -> [1,2,3]
                // n=1 -> 4
                // where a group is not an array, treat it as a singleton array
                if (!isPredicate && sourceArray instanceof SelectorArrayNode) {
                    // ^ when predicates are used on selector results, normal array index
                    // semantics are applied

                    SelectorArrayNode sourceArraySel = (SelectorArrayNode) sourceArray;

                    // we need to stay in "selector" mode so that subsequent array index
                    // references are also treated specially
                    // e.g. [{"a":1}, {"a":2}}].a[0][0] -> returns [1,2]
                    SelectorArrayNode resultAsSel = new SelectorArrayNode(factory);
                    output = resultAsSel;

                    for (JsonNode group : sourceArraySel.getSelectionGroups()) {
                        // System.out.println(" group: "+group);

                        // resolve negative indexes (they should start from the end of this
                        // selection group)
                        group = ensureArray(group);
                        List<Integer> resolvedIndexes = resolveIndexes(indexesToReturn, group.size());

                        for (int index : resolvedIndexes) {

                            // System.out.println(" index: "+index);
                            if (group.isArray()) {
                                JsonNode atIndex = group.get(index);
                                if (atIndex == null) {
                                    // we're done with this group
                                    break;
                                }
                                resultAsSel.addAsSelectionGroup(atIndex);
                            } else {
                                // treat non-array groups as singleton arrays
                                if (index == 0) { // non-array groups only have an element at
                                    // index 0
                                    resultAsSel.addAsSelectionGroup(group);
                                }
                            }
                        }
                    }

                } else {
                    output = factory.arrayNode();

                    // resolve negative indexes (they should start from the end of the
                    // source array)
                    List<Integer> resolvedIndexes = resolveIndexes(indexesToReturn, sourceArray.size());

                    // otherwise we just select from the array as normal
                    for (int index : resolvedIndexes) {
                        // ignore out-of-bounds indexes
                        if (index >= 0 && index < sourceArray.size()) {
                            output.add(sourceArray.get(index));
                        }
                    }
                }
                // results now holds a sub-array of the source array
                // containing only those values that are either at the specified indexes
                // or match the predicate statement
                if (output.size() != 0) {
                    // if only a single result is present, it should be "unwrapped"
                    // returned as a non-array
                    // [1][0]==1
                    // [[1]][0]==[1]
                    // [[[1]]][0]==[[1]]
                    result = output;
                    result = unwrapArray(result);
                }
            }
        }
        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    @Override
    public JsonNode visitArray_constructor(Array_constructorContext ctx) {
        final String METHOD = "visitArray_constructor";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                ctx.getText(), ctx.depth()
            });
        }
        JsonNode result = null;

        inArrayConstructStack.push(true);
        // System.out.println("========");
        // for(ParseTree child : ctx.children) {
        // System.out.println(child.getText());
        // }
        // System.out.println("========");

        ArrayNode output = factory.arrayNode();

        if (ctx.exprOrSeqList() == null) {
            // empty array: []
        } else {
            for (ExprOrSeqContext expr : ctx.exprOrSeqList().exprOrSeq()) {
                if (expr.seq() == null) {
                    processArrayContent(expr, output);
                } else {
                    // this is a seq, e.g. [1..2]
                    ArrayNode seq = (ArrayNode) visit(expr);

                    if (seq == null) {
                        // in JSONata if either side of the seq is undefined, an empty
                        // array will be produced
                        // e.g. [xxx...10]==[]
                        // in these cases, visitSeq will return null - we just need to
                        // skip addition to the resulting array in this case
                    } else {
                        // we don't want to double-nest the array ([[1,2]]) here so
                        // addAll, not add()
                        output.addAll(seq);
                    }
                }
            }
        }
        if (!inArrayConstructStack.isEmpty()) {
            inArrayConstructStack.pop();
        }
        result = output;
        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    @Override
    public JsonNode visitBoolean(MappingExpressionParser.BooleanContext ctx) {
        final String METHOD = "visitBoolean";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                ctx.getText(), ctx.depth()
            });
        }
        JsonNode result = null;
        if (ctx.op.getType() == MappingExpressionParser.TRUE) {
            result = BooleanNode.TRUE;
        } else if (ctx.op.getType() == MappingExpressionParser.FALSE) {
            result = BooleanNode.FALSE;
        }
        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    //	@Override
    //	public JsonNode visitExpr_to_eof(MappingExpressionParser.Expr_to_eofContext ctx) {
    //		ParseTree tree = ctx.expr();
    //		int treeSize = tree.getChildCount();
    //		steps.clear();
    //		for (int i = 0; i < treeSize; i++) {
    //			steps.add(tree.getChild(i));
    //		}
    //		if (tree.getChild(0) instanceof Array_constructorContext) {
    //			firstStepCons = true;
    //		}
    //		// test for laststep array construction child[n-1] instanceof
    //		// Array_constructorContext
    //		if (tree.getChild(treeSize - 1) instanceof Array_constructorContext) {
    //			lastStepCons = true;
    //		}
    //		if (tree.equals(steps.get(0))) {
    //			firstStep = true;
    //		} else {
    //			firstStep = false;
    //		}
    //
    //		JsonNode result = visit(tree);
    //		return result;
    //	}

    @Override
    public JsonNode visitComp_op(MappingExpressionParser.Comp_opContext ctx) {
        final String METHOD = "visitComp_op";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                (ctx.expr(0) == null ? "null" : ctx.expr(0).getText()),
                (ctx.expr(1) == null ? "null" : ctx.expr(1).getText()), ctx.depth()
            });
        }
        JsonNode result = null;

        JsonNode left = visit(ctx.expr(0)); // get value of left subexpression
        JsonNode right = visit(ctx.expr(1)); // get value of right subexpression

        // in all cases, if both are *no match*, JSONata returns false
        if (left == null && right == null) {
            return BooleanNode.FALSE;
        }

        boolean lIsComparable = false;
        boolean rIsComparable = false;
        if (left != null && right != null) {
            JsonNodeType ltype = left.getNodeType();
            JsonNodeType rtype = right.getNodeType();
            lIsComparable = ltype == JsonNodeType.NULL || ltype == JsonNodeType.STRING || ltype == JsonNodeType.NUMBER;
            rIsComparable = rtype == JsonNodeType.NULL || rtype == JsonNodeType.STRING || rtype == JsonNodeType.NUMBER;
        }

        if (ctx.op.getType() == MappingExpressionParser.EQ) {
            if ((left == null || left.isNull()) && (right != null && right.isNull() == false)) {
                result = BooleanNode.FALSE;
            } else if ((left != null && left.isNull() == false) && (right == null || right.isNull())) {
                result = BooleanNode.FALSE;
            } else if ((left == null || left.isNull()) && (right == null || right.isNull())) {
                result = BooleanNode.TRUE;
            } else if (left.getNodeType() == right.getNodeType()) {
                result = (areJsonNodesEqual(left, right) ? BooleanNode.TRUE : BooleanNode.FALSE);
            } else if (!lIsComparable || !rIsComparable) {
                // signal expression
                result = null;
            } else {
                // different types of comparables return not equal
                result = BooleanNode.FALSE;
            }
        } else if (ctx.op.getType() == MappingExpressionParser.NOT_EQ) {
            if ((left == null || left.isNull()) && (right != null && right.isNull() == false)) {
                result = BooleanNode.TRUE;
            } else if ((left != null && left.isNull() == false) && (right == null || right.isNull())) {
                result = BooleanNode.TRUE;
            } else if ((left == null || left.isNull()) && (right == null || right.isNull())) {
                result = BooleanNode.FALSE;
            } else if (left.getNodeType() == right.getNodeType()) {
                result = (areJsonNodesEqual(left, right) ? BooleanNode.FALSE : BooleanNode.TRUE);
            } else if (!lIsComparable || !rIsComparable) {
                // signal expression
                result = null;
            } else {
                // different types of comparables return not equal
                result = BooleanNode.TRUE;
            }
        } else if (ctx.op.getType() == MappingExpressionParser.LT) {
            if (left == null || right == null) {
                result = null;
            } else if (left.isNull() || right.isNull()) {
                throw new EvaluateRuntimeException(
                    "The expressions either side of operator \"<\" must evaluate to numeric or string values");
            } else if (left.isBoolean() || right.isBoolean()) {
                result = null;
            } else if (left.isFloatingPointNumber() || right.isFloatingPointNumber()) {
                result = (left.asDouble() < right.asDouble()) ? BooleanNode.TRUE : BooleanNode.FALSE;
            } else if (left.isDouble() || right.isDouble()) {
                result = (left.asDouble() < right.asDouble()) ? BooleanNode.TRUE : BooleanNode.FALSE;
            } else if (left.isIntegralNumber() && right.isIntegralNumber()) {
                result = (left.asLong() < right.asLong()) ? BooleanNode.TRUE : BooleanNode.FALSE;
            } else if (left.isLong() && right.isLong()) {
                result = (left.asLong() < right.asLong()) ? BooleanNode.TRUE : BooleanNode.FALSE;
            } else {
                if (!lIsComparable || !rIsComparable) {
                    // signal expression
                    result = null;
                } else if (left.getNodeType() != right.getNodeType()) {
                    throw new EvaluateRuntimeException("The values " + left.toString() + " and " + right.toString()
                        + " either side of operator \">\" must be of the same data type");
                } else {
                    result = (left.asText().compareTo(right.asText()) < 0) ? BooleanNode.TRUE : BooleanNode.FALSE;
                }
            }
        } else if (ctx.op.getType() == MappingExpressionParser.GT) {
            if (left == null || right == null) {
                result = null;
            } else if (left.isNull() || right.isNull()) {
                throw new EvaluateRuntimeException(
                    "The expressions either side of operator \">\" must evaluate to numeric or string values");
            } else if (left.isBoolean() || right.isBoolean()) {
                result = null;
            } else if (left.isFloatingPointNumber() || right.isFloatingPointNumber()) {
                result = (left.asDouble() > right.asDouble()) ? BooleanNode.TRUE : BooleanNode.FALSE;
            } else if (left.isDouble() || right.isDouble()) {
                result = (left.asDouble() > right.asDouble()) ? BooleanNode.TRUE : BooleanNode.FALSE;
            } else if (left.isIntegralNumber() && right.isIntegralNumber()) {
                result = (left.asLong() > right.asLong()) ? BooleanNode.TRUE : BooleanNode.FALSE;
            } else if (left.isLong() && right.isLong()) {
                result = (left.asLong() > right.asLong()) ? BooleanNode.TRUE : BooleanNode.FALSE;
            } else {
                if (!lIsComparable || !rIsComparable) {
                    // signal expression
                    return null;
                }
                if (left.getNodeType() != right.getNodeType()) {
                    throw new EvaluateRuntimeException("The values " + left.toString() + " and " + right.toString()
                        + " either side of operator \">\" must be of the same data type");
                }
                result = (left.asText().compareTo(right.asText()) > 0) ? BooleanNode.TRUE : BooleanNode.FALSE;
            }
        } else if (ctx.op.getType() == MappingExpressionParser.LE) {
            if (left == null || right == null) {
                result = null;
            } else if (left.isNull() || right.isNull()) {
                throw new EvaluateRuntimeException(
                    "The expressions either side of operator \"<=\" must evaluate to numeric or string values");
            } else if (left.isBoolean() || right.isBoolean()) {
                result = null;
            } else if (left.isFloatingPointNumber() || right.isFloatingPointNumber()) {
                result = (left.asDouble() <= right.asDouble()) ? BooleanNode.TRUE : BooleanNode.FALSE;
            } else if (left.isDouble() || right.isDouble()) {
                result = (left.asDouble() <= right.asDouble()) ? BooleanNode.TRUE : BooleanNode.FALSE;
            } else if (left.isIntegralNumber() && right.isIntegralNumber()) {
                result = (left.asLong() <= right.asLong()) ? BooleanNode.TRUE : BooleanNode.FALSE;
            } else if (left.isLong() && right.isLong()) {
                result = (left.asLong() <= right.asLong()) ? BooleanNode.TRUE : BooleanNode.FALSE;
            } else {
                if (!lIsComparable || !rIsComparable) {
                    // signal expression
                    result = null;
                } else if (left.getNodeType() != right.getNodeType()) {
                    throw new EvaluateRuntimeException("The values " + left.toString() + " and " + right.toString()
                        + " either side of operator \"<=\" must be of the same data type");
                } else {
                    result = (left.asText().compareTo(right.asText()) <= 0) ? BooleanNode.TRUE : BooleanNode.FALSE;
                }
            }
        } else if (ctx.op.getType() == MappingExpressionParser.GE) {
            if (left == null || right == null) {
                result = null;
            } else if (left.isNull() || right.isNull()) {
                throw new EvaluateRuntimeException(
                    "The expressions either side of operator \">=\" must evaluate to numeric or string values");
            } else if (left.isBoolean() || right.isBoolean()) {
                result = null;
            } else if (left.isFloatingPointNumber() || right.isFloatingPointNumber()) {
                result = (left.asDouble() >= right.asDouble()) ? BooleanNode.TRUE : BooleanNode.FALSE;
            } else if (left.isDouble() || right.isDouble()) {
                result = (left.asDouble() >= right.asDouble()) ? BooleanNode.TRUE : BooleanNode.FALSE;
            } else if (left.isIntegralNumber() && right.isIntegralNumber()) {
                result = (left.asLong() >= right.asLong()) ? BooleanNode.TRUE : BooleanNode.FALSE;
            } else if (left.isLong() && right.isLong()) {
                result = (left.asLong() >= right.asLong()) ? BooleanNode.TRUE : BooleanNode.FALSE;
            } else {
                if (!lIsComparable || !rIsComparable) {
                    // signal expression
                    result = null;
                } else if (left.getNodeType() != right.getNodeType()) {
                    throw new EvaluateRuntimeException("The values " + left.toString() + " and " + right.toString()
                        + " either side of operator \">=\" must be of the same data type");
                } else {
                    result = (left.asText().compareTo(right.asText()) >= 0) ? BooleanNode.TRUE : BooleanNode.FALSE;
                }
            }
        }
        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    @Override
    public JsonNode visitConcat_op(MappingExpressionParser.Concat_opContext ctx) {
        final String METHOD = "visitConcat_op";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                (ctx.expr(0) == null ? "null" : ctx.expr(0).getText()),
                (ctx.expr(1) == null ? "null" : ctx.expr(1).getText()), ctx.depth()
            });
        }
        JsonNode result = null;

        JsonNode left = visit(ctx.expr(0)); // get value of left subexpression
        JsonNode right = visit(ctx.expr(1)); // get value of right subexpression

        String leftStr;
        String rightStr;

        if (left == null) {
            leftStr = "";
        } else {
            if (left != null && left.isDouble()) {
                leftStr = castString(left, false);
            } else {
                leftStr = castString(left, false);
            }
        }

        if (right == null) {
            rightStr = "";
        } else {
            if (right != null && right.isDouble()) {
                rightStr = castString(right, false);
            } else {
                rightStr = castString(right, false);
            }
        }

        result = new TextNode(leftStr + rightStr);

        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    @Override
    public JsonNode visitConditional(MappingExpressionParser.ConditionalContext ctx) {
        final String METHOD = "visitConditional";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD,
                new Object[] {
                    (ctx.expr(0) == null ? "null" : ctx.expr(0).getText()),
                    (ctx.expr(1) == null ? "null" : ctx.expr(1).getText()),
                    (ctx.expr(2) == null ? "null" : ctx.expr(2).getText()), ctx.depth()
                });
        }
        JsonNode result = null;

        /* the conditional ternary operator ?: */
        JsonNode cond = visit(ctx.expr(0)); // get value of left subexpression
        result = cond;
        if (cond == null) {
            ExprContext ctx2 = ctx.expr(2);
            if (ctx2 != null) {
                result = visit(ctx2);
            } else {
                result = null;
            }
        } else if (cond instanceof BooleanNode || cond instanceof NumericNode || cond instanceof TextNode) {
            ExprContext ctx1 = ctx.expr(1);
            ExprContext ctx2 = ctx.expr(2);
            if (ctx1 != null && ctx2 != null) {
                result = BooleanUtils.convertJsonNodeToBoolean(cond) ? visit(ctx.expr(1)) : visit(ctx.expr(2));
            } else {
                if (BooleanUtils.convertJsonNodeToBoolean(cond)) {
                    if (ctx1 != null) {
                        result = visit(ctx1);
                    } else {
                        result = null;
                    }
                } else {
                    if (ctx2 != null) {
                        result = visit(ctx2);
                    } else {
                        result = null;
                    }
                }
            }
        }
        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    @Override
    public JsonNode visitContext_ref(MappingExpressionParser.Context_refContext ctx) {
        final String METHOD = "visitContext_ref";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                ctx.getText(), ctx.depth()
            });
        }
        JsonNode result = null;
        if (ctx.getChildCount() > 0) {
            ParseTree child0 = ctx.getChild(0);
            if (child0 instanceof TerminalNodeImpl) {
                if (((TerminalNodeImpl) child0).symbol.getText().equals("$")) {
                    // replace first child with value of the rootContext
                    JsonNode context = getVariable("$");
                    if (context != null) {
                        if (ctx.children.size() == 1) {
                            // nothing left to visit
                            result = context;
                        } else {
                            /**
                             * cannot just replace ctx child since this can be called recursively with
                             * different stack values so need to replace child[0] in the new context we
                             * create below
                             */
                            CommonToken token = null;
                            ExprContext expr = null;
                            switch (context.getNodeType()) {
                                case BINARY:
                                case POJO: {
                                    break;
                                }
                                case ARRAY: {
                                    child0 = FunctionUtils.getArrayConstructorContext(ctx, (ArrayNode) context);
                                    expr = new MappingExpressionParser.ArrayContext(ctx);
                                    for (int i = 0; i < ctx.children.size(); i++) {
                                        expr.children.add(ctx.children.get(i));
                                    }
                                    expr.children.set(0, child0);
                                    result = visit(expr);
                                    break;
                                }
                                case BOOLEAN: {
                                    token = (context.asBoolean()
                                        ? CommonTokenFactory.DEFAULT.create(MappingExpressionParser.TRUE,
                                            context.asText())
                                        : CommonTokenFactory.DEFAULT.create(MappingExpressionParser.FALSE,
                                            context.asText()));
                                    TerminalNodeImpl tn = new TerminalNodeImpl(token);
                                    BooleanContext bc = new MappingExpressionParser.BooleanContext(ctx);
                                    bc.children.set(0, tn);
                                    result = visit(bc);
                                    break;
                                }
                                case MISSING:
                                case NULL: {
                                    token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.NULL, null);
                                    TerminalNodeImpl tn = new TerminalNodeImpl(token);
                                    NullContext nc = new MappingExpressionParser.NullContext(ctx);
                                    nc.children.set(0, tn);
                                    result = visit(nc);
                                    break;
                                }
                                case NUMBER: {
                                    token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.NUMBER,
                                        context.asText());
                                    TerminalNodeImpl tn = new TerminalNodeImpl(token);
                                    NumberContext nc = new NumberContext(ctx);
                                    nc.children.set(0, tn);
                                    result = visit(nc);
                                    break;
                                }
                                case OBJECT: {
                                    child0 = FunctionUtils.getObjectConstructorContext(ctx, (ObjectNode) context);
                                    expr = new MappingExpressionParser.PathContext(ctx);
                                    for (int i = 0; i < ctx.children.size(); i++) {
                                        expr.children.add(ctx.children.get(i));
                                    }
                                    expr.children.set(0, child0);
                                    result = visit(expr);
                                    break;
                                }
                                case STRING:
                                default: {
                                    token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.STRING,
                                        context.asText());
                                    TerminalNodeImpl tn = new TerminalNodeImpl(token);
                                    StringContext sc = new StringContext(ctx);
                                    sc.children.set(0, tn);
                                    result = visit(sc);
                                    break;
                                }
                            } // end switch
                        }
                    } else {
                        result = context;
                    }
                }
            } else {
                result = visit(child0);
            }
        }
        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    @Override
    public JsonNode visitDescendant(MappingExpressionParser.DescendantContext ctx) {
        final String METHOD = "visitDescendant";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                ctx.getText(), ctx.depth()
            });
        }
        JsonNode result = null;
        ArrayNode resultArray = new ArrayNode(JsonNodeFactory.instance);
        JsonNode descendants = getDescendants();
        if (descendants == null) {
            resultArray = null;
        } else {
            if (!descendants.isArray()) {
                resultArray.add(descendants);
            } else {
                for (Iterator<JsonNode> it = ((ArrayNode) descendants).iterator(); it.hasNext();) {
                    resultArray.add(it.next());
                }
            }
        }
        if (resultArray == null || resultArray.size() == 0) {
            result = null;
        } else {
            result = unwrapArray(resultArray);
        }
        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    @Override
    public JsonNode visitFct_chain(Fct_chainContext ctx) {
        final String METHOD = "visitFct_chain";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                ctx.getText(), ctx.depth()
            });
        }
        JsonNode result = null;
        Object exprObj = ctx.expr(1);
        if (exprObj instanceof MappingExpressionParser.Function_callContext) {
            JsonNode test = visit(ctx.expr(0));
            _environment.pushContext(test);
            result = visit(ctx.expr(1));
            _environment.popContext();
        } else if (exprObj instanceof Var_recallContext) {
            String fctName = ((Var_recallContext) exprObj).VAR_ID().getText();
            // assume this is a variable pointing to a function
            DeclaredFunction declFct = getDeclaredFunction(fctName);
            if (declFct == null) {
                FunctionBase function = getJsonataFunction(fctName);
                if (function == null) {
                    function = Constants.FUNCTIONS.get(fctName);
                }
                if (function != null) {
                    result = function.invoke(this, (Function_callContext) ctx.expr(1));
                } else {
                    throw new EvaluateRuntimeException("Unknown function: " + fctName);
                }
            } else {
                result = declFct.invoke(this, ctx.expr(1));
            }
        } else {
            throw new EvaluateRuntimeException("Expected a function but got " + ctx.expr(1).getText());
        }
        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    @Override
    public JsonNode visitField_values(MappingExpressionParser.Field_valuesContext ctx) {
        final String METHOD = "visitField_values";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                ctx.getText(), ctx.depth()
            });
        }
        JsonNode result = null;
        ArrayNode resultArray = new ArrayNode(JsonNodeFactory.instance);
        ArrayNode valArray = new ArrayNode(JsonNodeFactory.instance);
        if (_environment.isEmptyContext() == false) {
            JsonNode elt = _environment.peekContext();
            if (elt == null || (elt.isObject() || elt.isArray()) == false) {
                // signal no match
                result = null;
            } else {
                if (elt.isObject()) {
                    for (Iterator<String> it = ((ObjectNode) elt).fieldNames(); it.hasNext();) {
                        JsonNode value = ((ObjectNode) elt).get(it.next());
                        if (value.isArray()) {
                            value = flatten(value, null);
                            // remove outer array
                            for (Iterator<JsonNode> it2 = ((ArrayNode) value).iterator(); it2.hasNext();) {
                                valArray.add(it2.next());
                            }
                            // valArray = (ArrayNode)append(valArray, value);
                        } else {
                            valArray.add(value);
                        }
                    }
                } else { // isArray
                    for (Iterator<JsonNode> it = ((ArrayNode) elt).iterator(); it.hasNext();) {
                        JsonNode value = it.next();
                        if (value.isArray()) {
                            value = flatten(value, null);
                            // remove outer array
                            for (Iterator<JsonNode> it2 = ((ArrayNode) value).iterator(); it2.hasNext();) {
                                valArray.add(it2.next());
                            }
                        } else {
                            valArray.add(value);
                        }
                    }
                }
                for (Iterator<JsonNode> it = valArray.iterator(); it.hasNext();) {
                    JsonNode value = it.next();
                    resultArray.add(value);
                }
            }
            if (resultArray.size() == 0) {
                result = null;
            } else {
                result = unwrapArray(resultArray);
            }
        }
        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    @Override
    public JsonNode visitFieldList(MappingExpressionParser.FieldListContext ctx) {
        final String METHOD = "visitFieldList";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                ctx.getText(), ctx.depth()
            });
        }
        JsonNode result = null;
        ObjectNode resultObject = new ObjectNode(JsonNodeFactory.instance);
        if (_environment.isEmptyContext() == false) {
            JsonNode elt = _environment.peekContext();
            if (elt != null && elt.isObject() != false) {
                String key = "";
                JsonNode value = null;
                ParseTree keyChild;
                JsonNode keyNode;
                for (int i = 0; i < ctx.getChildCount(); i += 4) {
                    // expect name ':' expr ,
                    keyChild = ctx.getChild(i);
                    if (keyChild instanceof TerminalNode) {
                        key = sanitise(((TerminalNodeImpl) ctx.getChild(i)).getText());
                    } else {
                        keyNode = visit(keyChild);
                        if (keyNode != null) {
                            key = sanitise(keyNode.asText());
                        } else {
                            throw new EvaluateRuntimeException("key resolved to null");
                        }
                    }
                    // #228 return the element associated with the key
                    // further processing is handled upstream for the 
                    // fieldList's i+2 child
                    value = elt; // was visit(ctx.getChild(i + 2));

                    if (value != null) {
                        resultObject.set(key, value);
                    }
                }
                if (resultObject.size() > 0) {
                    result = resultObject;
                }
            }
        }
        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    @Override
    public JsonNode visitFunction_call(MappingExpressionParser.Function_callContext ctx) {
        final String METHOD = "visitFunction_call";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                ctx.getText(), ctx.depth()
            });
        }
        JsonNode result = null;
        String functionName = ctx.VAR_ID().getText();

        DeclaredFunction declFct = getDeclaredFunction(functionName);
        if (declFct == null) {
            FunctionBase function = getJsonataFunction(functionName);
            if (function == null) {
                function = Constants.FUNCTIONS.get(functionName);
            }
            if (function != null) {
                result = function.invoke(this, ctx);
            } else {
                throw new EvaluateRuntimeException("Unknown function: " + functionName);
            }
        } else {
            result = declFct.invoke(this, ctx);
        }
        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    @Override
    public JsonNode visitFunction_decl(MappingExpressionParser.Function_declContext ctx) {
        final String METHOD = "visitFunction_decl";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                ctx.getText(), ctx.depth()
            });
        }
        JsonNode result = null;

        /**
         * The goal is to create a JsonNode representing the function declaration that
         * could be stored (e.g., keyed by a variable id in the functionMap) and
         * executed later by creating a Function_execContext and visiting the
         * visitFunction_exec method.
         */
        final String fctName = ctx.FUNCTIONID().getText();
        RuleContext expr = ctx.getRuleContext();

        // build the declared function to assign to this variable in the
        // functionMap
        MappingExpressionParser.Function_declContext fctDeclCtx = (MappingExpressionParser.Function_declContext) expr;
        MappingExpressionParser.VarListContext varList = fctDeclCtx.varList();
        MappingExpressionParser.ExprListContext exprList = fctDeclCtx.exprList();
        DeclaredFunction fct = new DeclaredFunction(varList, exprList);
        setDeclaredFunction(fctName, fct);

        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    @Override
    public JsonNode visitFunction_exec(MappingExpressionParser.Function_execContext ctx) {
        final String METHOD = "visitFunction_exec";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                ctx.getText(), ctx.depth()
            });
        }
        JsonNode result = null;
        // "function($l, $w, $h){ $l * $w * $h }(10, 10, 5)"
        // get the VarListContext for the variables to be assigned
        // get the ExprValuesContext for the values to be assigned to the
        // variables
        // get the ExprListContext to calculate the result
        List<TerminalNode> varListCtx = ctx.varList().VAR_ID();
        List<ExprContext> exprValuesCtx = ctx.exprValues().exprList().expr();
        int varListCount = varListCtx.size();
        int exprListCount = exprValuesCtx.size();
        // ensure a direct mapping is possible
        if (varListCount < exprListCount) {
            throw new EvaluateRuntimeException(
                "Expected equal counts for variables (" + varListCount + ") and values (" + exprListCount + ")");
        }
        for (int i = 0; i < varListCount; i++) {
            String varID = varListCtx.get(i).getText();
            if (i < exprListCount) {
                JsonNode value = visit(exprValuesCtx.get(i));
                setVariable(varID, value);
            } else {
                setVariable(varID, null);
            }
        }
        ExprListContext exprListCtx = ctx.exprList();

        result = visit(exprListCtx);
        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    @Override
    public JsonNode visitId(IdContext ctx) {
        final String METHOD = "visitId";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                ctx.getText(), ctx.depth()
            });
        }
        JsonNode result = null;

        JsonNode context;
        if (_environment.isEmptyContext() == false) {
            context = _environment.peekContext();

            final String id = sanitise(ctx.ID().getText());
            if (LOG.isLoggable(Level.FINEST)) {
                LOG.entering(CLASS, METHOD, new Object[] {
                    ctx.getText(), id, "(stack: " + context + ")"
                });
            }

            if (context == null) {
                result = null;
            } else {
                result = lookup(context, id);
            }
        }
        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    // private boolean flattenOutput = true;

    @Override
    public JsonNode visitLogand(MappingExpressionParser.LogandContext ctx) {
        final String METHOD = "visitLogand";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                (ctx.expr(0) == null ? "null" : ctx.expr(0).getText()),
                (ctx.expr(1) == null ? "null" : ctx.expr(1).getText()), ctx.depth()
            });
        }
        JsonNode result = null;

        JsonNode left = visit(ctx.expr(0)); // get value of left subexpression
        if (BooleanUtils.convertJsonNodeToBoolean(left)) {
            JsonNode right = visit(ctx.expr(1)); // get value of right subexpression

            result = BooleanUtils.convertJsonNodeToBoolean(left) && BooleanUtils.convertJsonNodeToBoolean(right)
                ? BooleanNode.TRUE
                : BooleanNode.FALSE;
        } else {
            result = BooleanNode.FALSE;
        }
        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    @Override
    public JsonNode visitLogor(MappingExpressionParser.LogorContext ctx) {
        final String METHOD = "visitLogor";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                (ctx.expr(0) == null ? "null" : ctx.expr(0).getText()),
                (ctx.expr(1) == null ? "null" : ctx.expr(1).getText()), ctx.depth()
            });
        }
        JsonNode result = null;

        JsonNode left = visit(ctx.expr(0)); // get value of left subexpression
        if (BooleanUtils.convertJsonNodeToBoolean(left) == false) {
            JsonNode right = visit(ctx.expr(1)); // get value of right subexpression

            result = BooleanUtils.convertJsonNodeToBoolean(left) || BooleanUtils.convertJsonNodeToBoolean(right)
                ? BooleanNode.TRUE
                : BooleanNode.FALSE;
        } else {
            result = BooleanNode.TRUE;
        }
        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    @Override
    public JsonNode visitMembership(MappingExpressionParser.MembershipContext ctx) {
        final String METHOD = "visitMembership";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                (ctx.expr(0) == null ? "null" : ctx.expr(0).getText()),
                (ctx.expr(1) == null ? "null" : ctx.expr(1).getText()), ctx.depth()
            });
        }
        JsonNode result = null;

        JsonNode left = visit(ctx.expr(0)); // get value of left subexpression
        JsonNode right = visit(ctx.expr(1)); // get value of right subexpression

        if (left != null && right != null) {

            // If the RHS is a single value, then it is treated as a singleton array.
            right = ensureArray(right);

            // unwrap left if singleton
            left = unwrapArray(left);

            result = BooleanNode.FALSE;
            Iterator<JsonNode> elements = right.elements();
            while (elements.hasNext()) {
                JsonNode curElement = elements.next();
                if (areJsonNodesEqual(left, curElement)) {
                    result = BooleanNode.TRUE;
                    break;
                }
            }
        }
        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    @Override
    public JsonNode visitMuldiv_op(MappingExpressionParser.Muldiv_opContext ctx) {
        final String METHOD = "visitMuldiv_op";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                (ctx.expr(0) == null ? "null" : ctx.expr(0).getText()),
                (ctx.expr(1) == null ? "null" : ctx.expr(1).getText()), ctx.depth()
            });
        }
        JsonNode result = null;

        JsonNode leftNode = visit(ctx.expr(0)); // get value of left subexpression
        JsonNode rightNode = visit(ctx.expr(1)); // get value of right
        // subexpression

        // in all cases, if either are *no match*, JSONata returns *no match*
        if (leftNode != null && rightNode != null) {

            if (!leftNode.isNumber() || !rightNode.isNumber()) {
                throw new EvaluateRuntimeException(ctx.op.getText() + " expects two numeric arguments");
            }

            // treat both inputs as doubles when performing arithmetic operations
            double left = leftNode.asDouble();
            double right = rightNode.asDouble();

            Double dresult = 0.0d;

            if (ctx.op.getType() == MappingExpressionParser.MUL) {
                dresult = left * right;
            } else if (ctx.op.getType() == MappingExpressionParser.DIV) {
                if (right == 0.0d) {
                    result = new DoubleNode(Double.POSITIVE_INFINITY); // null;
                } else {
                    dresult = left / right;
                }
            } else if (ctx.op.getType() == MappingExpressionParser.REM) {
                if (right == 0.0d) {
                    result = new DoubleNode(Double.NaN); // null;
                } else {
                    dresult = left % right;
                }
            } else {
                // should never happen (this expression should not have parsed in the
                // first place)
                throw new EvaluateRuntimeException("Unrecognised token " + ctx.op.getText());
            }

            if (result == null) {
                // check for Infinity and Nan
                if (dresult.isInfinite() || dresult.isNaN()) {
                    throw new EvaluateRuntimeException("Number out of range: \"null\"");
                }
                // coerce the result to a long iff the result is exactly .0
                if (isWholeNumber(dresult)) {
                    result = new LongNode(dresult.longValue());
                } else {
                    result = new DoubleNode(dresult);
                }
            }
        }
        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    @Override
    public JsonNode visitNull(NullContext ctx) {
        final String METHOD = "visitNull";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                ctx.getText(), ctx.depth()
            });
        }
        JsonNode result = NullNode.getInstance();
        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    @Override
    public JsonNode visitNumber(MappingExpressionParser.NumberContext ctx) {
        final String METHOD = "visitNumber";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                ctx.getText(), ctx.depth()
            });
        }
        /*
         * For consistency with the JavaScript implementation of JSONata, we limit the
         * size of the numbers that we handle to be within the range Double.MAX_VALUE
         * and -Double.MAX_VALUE. If we did not do this we would need to implement a lot
         * of extra code to handle BigInteger and BigDecimal. The
         * NumberUtils::convertNumberToValueNode will check whether the number is within
         * the valid range and throw a suitable exception if it is not.
         */
        JsonNode result = NumberUtils.convertNumberToValueNode(ctx.NUMBER().getText());

        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    //	/**
    //	 * Don't process the <EOF> token as it returns null and overwrites the result
    //	 */
    //	@Override
    //	public JsonNode visitChildren(RuleNode node) {
    //		JsonNode result = defaultResult();
    //		int n = node.getChildCount();
    //		for (int i=0; i<n; i++) {
    //			if (!shouldVisitNextChild(node, result)) {
    //				break;
    //			}
    //
    //			ParseTree c = node.getChild(i);
    //			// don't change result if we've reached EOF
    //			if (c instanceof TerminalNodeImpl) {
    //				TerminalNodeImpl tni = (TerminalNodeImpl)c;
    //				if (tni.symbol.getType() != Token.EOF) {
    //					JsonNode childResult = c.accept(this);
    //					result = aggregateResult(result, childResult);
    //				}
    //			} else {
    //				JsonNode childResult = c.accept(this);
    //				result = aggregateResult(result, childResult);				
    //			}
    //		}
    //
    //		return result;
    //	}
    //

    @Override
    public JsonNode visitExprList(MappingExpressionParser.ExprListContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public JsonNode visitObject(ObjectContext ctx) {
        final String METHOD = "visitObject";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                ctx.getText(), ctx.depth()
            });
        }
        JsonNode result = null;

        ObjectNode object = factory.objectNode();
        if (ctx.fieldList() == null) {
            // empty object: {}
            result = object;
        } else {
            JsonNode context = null;
            // check to see if there is a referenced variable use that as the context
            ParseTree firstChild = ctx.getChild(0);
            if (firstChild instanceof Var_recallContext) {
                context = visit(firstChild);
            } else if (firstChild instanceof PathContext) {
                context = visit(firstChild);
            } else if (_environment.isEmptyContext() == false) {
                context = _environment.peekContext();
                final String id = sanitise(ctx.getChild(0).getText());
                if (LOG.isLoggable(Level.FINEST)) {
                    LOG.entering(CLASS, METHOD, new Object[] {
                        ctx.getText(), id, "(stack: " + context + ")"
                    });
                }
                context = ctx.getChild(0).accept(this);
            }
            if (context != null && !context.isNull()) {
                if (context.isArray()) {
                    final Map<String, ArrayNode> grouping = new LinkedHashMap<>();
                    for (JsonNode element : context) {
                        _environment.pushContext(element);
                        final JsonNode fieldList = visit(ctx.fieldList());
                        fieldList.fields().forEachRemaining(e -> grouping
                            .computeIfAbsent(e.getKey(), ignore -> factory.arrayNode()).add(e.getValue()));
                        _environment.popContext();
                    }
                    // #228 now process the fieldList child#2 to get the expected value
                    // grouping.forEach((k, v) -> object.set(k, unwrapArray(v)));
                    // replaced with below
                    grouping.forEach((k, v) -> {
                        _environment.pushContext(v);
                        final JsonNode valueX = visit(ctx.fieldList().getChild(2));
                        _environment.popContext();
                        object.set(k, unwrapArray(valueX));
                    });
                    // end replacement

                } else {
                    _environment.pushContext(context);
                    final JsonNode fieldList = visit(ctx.fieldList());
                    if (fieldList != null && fieldList.isObject()) {
                        object.setAll((ObjectNode) fieldList);
                    }
                    _environment.popContext();
                }
            }

            result = object;
        }

        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    @Override
    public JsonNode visitObject_constructor(Object_constructorContext ctx) {
        final String METHOD = "visitObject_constructor";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                ctx.getText(), ctx.depth()
            });
        }
        JsonNode result = null;

        ObjectNode object = factory.objectNode();

        // convert the keys to strings, stripping surrounding quotes and
        // un-escaping any special JSON chars
        if (ctx.fieldList() == null) {
            // empty object: {}
            result = object;
        } else {
            // no longer required after patch applied
            // JsonNode context = null;
            // // check to see if there is a referenced variable use that as the context
            // ParseTree firstChild = ctx.getChild(0);
            // if (firstChild instanceof Var_recallContext) {
            // context = visit(firstChild);
            // } else if (_environment.isEmptyContext() == false) {
            // context = _environment.peekContext();
            // }
            // List<TerminalNode> keyNodes = new ArrayList<TerminalNode>(); //
            // ctx.fieldList().STRING();
            List<String> keys = new ArrayList<String>();
            List<ExprContext> valueNodes = new ArrayList<ExprContext>(); // ctx.fieldList().expr();
            // run through the ctx.fieldList() processing pairs of key/value separated by
            // colon
            FieldListContext fieldList = ctx.fieldList();
            // pattern is expr or String, colon, expr or String, comma
            ParseTree keyField;
            ParseTree colon;
            ParseTree valueField;
            ParseTree comma;
            for (int f = 0; f < fieldList.getChildCount(); f++) {
                switch (f % 4) {
                    case 0: {
                        keyField = fieldList.getChild(f);
                        // determine if this is already a string or attempt to evaluate to a String
                        if (keyField instanceof TerminalNode) {
                            keys.add(((TerminalNode) keyField).getText());
                        } else {
                            // attempt to resolve to a String
                            JsonNode key = visit(keyField);
                            if (key == null) {
                                throw new EvaluateRuntimeException(
                                    "Key in object structure must evaluate to a string; got: undefined");
                            }
                            if (key.isNull()) {
                                throw new EvaluateRuntimeException(
                                    "Key in object structure must evaluate to a string; got: null");
                            }
                            if (key.isTextual()) {
                                keys.add(key.asText());
                            } else {
                                throw new EvaluateRuntimeException(
                                    "Key in object structure must evaluate to a string; got: "
                                        + key.toPrettyString());
                            }
                        }
                        break;
                    }
                    case 1: {
                        colon = fieldList.getChild(f);
                        if (colon instanceof TerminalNode) {
                            if (":".equals(colon.getText())) {
                                break;
                            }
                        }
                        throw new EvaluateRuntimeException("Expected \":\" got \"" + colon.getText() + "\"");
                    }
                    case 2: {
                        valueField = fieldList.getChild(f);
                        if (valueField instanceof ExprContext) {
                            valueNodes.add((ExprContext) valueField);
                        } else if (valueField instanceof TerminalNode) {
                            valueNodes.add((ExprContext) valueField);
                        } else if (valueField instanceof Function_declContext) {
                            valueNodes.add((ExprContext) valueField);
                        } else if (valueField instanceof Var_recallContext) {
                            valueNodes.add((ExprContext) valueField);
                        } else {
                            // ignore this entirely from our object
                            keys.remove(keys.size() - 1);
                        }
                        break;
                    }
                    case 3: {
                        comma = fieldList.getChild(f);
                        if (comma instanceof TerminalNode) {
                            if (",".equals(comma.getText())) {
                                break;
                            }
                        }
                        throw new EvaluateRuntimeException("Expected \"}\" got \"" + comma.getText() + "\"");
                    }
                }
            }
            if (keys.size() != valueNodes.size()) {
                // this shouldn't have parsed in the first place
                throw new EvaluateRuntimeException("Object key/value count mismatch!");
            }
            ExprContext valueNode = null;
            JsonNode value = null;
            String fctName = null;
            String key = "";
            for (int i = 0; i < /* keyNodes */keys.size(); i++) {
                key = /* keyNodes */keys.get(i); // .getText();
                key = sanitise(key);
                // try to get the value for the corresponding expression
                valueNode = valueNodes.get(i);
                if (valueNode instanceof Function_declContext) {
                    visit(valueNode);
                    fctName = ((Function_declContext) valueNode).FUNCTIONID().getText();
                    if (getDeclaredFunction(fctName) != null) {
                        value = new TextNode("");
                    }
                } else if (valueNode instanceof Var_recallContext) {
                    value = visit(valueNode);
                    if (value == null) {
                        String varName = ((Var_recallContext) valueNode).VAR_ID().getText();
                        DeclaredFunction declFct = getDeclaredFunction(varName);
                        if (declFct != null) {
                            value = new TextNode("");
                        } else {
                            FunctionBase fct = getJsonataFunction(varName);
                            if (fct != null) {
                                value = new TextNode("");
                            } else {
                                value = null;
                            }
                        }
                    }
                } else {
                    value = visit(valueNode);
                    // check for double infinity
                    if (value != null && value.isDouble()) {
                        Double d = value.asDouble();
                        if (Double.isNaN(d) || Double.isInfinite(d)) {
                            throw new EvaluateRuntimeException("Number out of range: null");
                        }
                    }
                }
                if (value != null) {
                    object.set(key, value);
                }
            }
            result = object;
        }

        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    @Override
    public JsonNode visitParens(MappingExpressionParser.ParensContext ctx) {
        final String METHOD = "visitParens";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                ctx.getText(), ctx.depth()
            });
        }
        JsonNode result = null;

        // generate a new frameEnvironment for life of this block
        FrameEnvironment oldEnvironment = setNewEnvironment();
        List<ExprContext> expressions = ctx.expr();
        try {
            for (int i = 0; i < expressions.size(); i++) {
                result = visit(ctx.expr(i));
            }
        } catch (Exception e) {
            _environment = oldEnvironment;
            throw e;
        }

        // we need to drop out of selection mode if the params wrap a selection
        // statement this is so that e.g. ([{"a":1}, {"a":2}].a)[0] returns 1
        // (not [1,2] as would be returned without the parenthesis)
        if (result instanceof SelectorArrayNode) {
            ArrayNode newResult = factory.arrayNode();
            ((ArrayNode) newResult).addAll((SelectorArrayNode) result);
            result = newResult;
        }

        resetOldEnvironment(oldEnvironment);
        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    @Override
    public JsonNode visitPath(PathContext ctx) {
        // System.out.println("@@@ visitPath: -> PathContext = \"" + ctx.toString() +
        // "\"");
        final String METHOD = "visitPath";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                (ctx.expr(0) == null ? "null" : ctx.expr(0).getText()),
                (ctx.expr(1) == null ? "null" : ctx.expr(1).getText()), ctx.depth()
            });
        }
        JsonNode result = null;

        // [{"a":{"b":1}}, {"a":{"b":2}}].a
        final ExprContext lhsCtx = ctx.expr(0); // e.g. [{"a":{"b":1}},
        // {"a":{"b":2}}]
        final ExprContext rhsCtx = ctx.expr(1); // e.g. `a`

        if (lhsCtx instanceof NullContext || rhsCtx instanceof NullContext) {
            throw new EvaluateRuntimeException(String.format(Constants.ERR_MSG_INVALID_PATH_ENTRY, "null"));
        }
        // treat a StringContext as an ID context
        JsonNode lhs = null;
        if (lhsCtx instanceof MappingExpressionParser.StringContext) {
            CommonToken token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.ID, visit(lhsCtx).asText());
            TerminalNode node = new TerminalNodeImpl(token);
            IdContext idCtx = new MappingExpressionParser.IdContext(lhsCtx);
            idCtx.addChild(node);
            lhs = visit(idCtx);
        } else {
            lhs = visit(lhsCtx);
        }
        if (lhs != null && lhs.isNull() == false) {
            // reject path entries that are numbers or values
            switch (lhs.getNodeType()) {
                case NUMBER: {
                    // leaving number as is since * can reference array values too
                    // lhs = factory.textNode(lhs.asText());
                    break;
                }
                case BOOLEAN:
                case NULL: {
                    throw new EvaluateRuntimeException(
                        String.format(Constants.ERR_MSG_INVALID_PATH_ENTRY, lhs.toString()));
                }
                default: {
                    break;
                }
            }
            if (rhsCtx == null) {
                result = lhs;
            } else {
                JsonNode rhs = null;
                // treat a StringContext as an ID Context
                if (rhsCtx instanceof MappingExpressionParser.StringContext) {
                    CommonToken token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.ID,
                        visit(rhsCtx).asText());
                    TerminalNode node = new TerminalNodeImpl(token);
                    IdContext idCtx = new MappingExpressionParser.IdContext(rhsCtx);
                    idCtx.addChild(node);
                    rhs = resolvePath(lhs, idCtx);
                } else if (rhsCtx instanceof NumberContext) {
                    throw new EvaluateRuntimeException("The literal value " + visit(rhsCtx)
                        + " cannot be used as a step within a path expression");
                } else {
                    rhs = resolvePath(lhs, rhsCtx);
                }
                if (rhs == null) { // okay to return NullNode here so don't test "|| rhs.isNull()"
                    result = null;
                } else if (rhs instanceof SelectorArrayNode) {
                    if (rhs.size() == 0) {
                        // if no results are present (i.e. results is empty) we need to return
                        // null (i.e. *no match*)
                        result = null;
                    } else {
                        if ((firstStepCons && firstStep) || (lastStep && lastStepCons)) {
                            List<JsonNode> cells = ((SelectorArrayNode) rhs).getSelectionGroups();
                            result = new ArrayNode(JsonNodeFactory.instance);
                            for (JsonNode cell : cells) {
                                if (cell.isArray() == false || "visitArray_constructor".equals(lastVisited)) {
                                    ((ArrayNode) result).add(cell);
                                } else {
                                    for (JsonNode elt : cell) {
                                        ((ArrayNode) result).add(elt);
                                    }
                                }
                            }
                            if (!keepArray) {
                                if (result.size() == 1) {
                                    result = result.get(0);
                                }
                            }
                            firstStepCons = false;
                        } else {
                            result = rhs;
                        }
                    }
                } else {
                    result = rhs;
                }
            }
        }
        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    @Override
    public JsonNode visitParent_path(Parent_pathContext ctx) {
        final String METHOD = "visitParent_path";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                ctx.getText(), ctx.depth()
            });
        }

        final JsonNode result = determineParentPath(ctx);

        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    @Override
    public JsonNode visitParent_path_solitary(MappingExpressionParser.Parent_path_solitaryContext ctx) {
        final String METHOD = "visitParent_path_solitary";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                ctx.getText(), ctx.depth()
            });
        }

        final JsonNode result = _environment.getParentNode(1);

        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    private JsonNode determineParentPath(final ExprContext ctx) {
        JsonNode result = null;
        if (ctx.getChildCount() >= 3 && (/* ctx.getChildCount() is odd */ ctx.getChildCount() % 2 == 1)) {
            final int depth = (ctx.getChildCount() - 1) / 2;
            final JsonNode parentNode = _environment.getParentNode(depth);
            if (parentNode != null && ctx.getChildCount() == ((2 * depth) + 1)
                && (ctx.getChild(2 * depth) instanceof ExprContext)) {
                _environment.pushContext(parentNode);
                result = resolvePath(parentNode, (ExprContext) ctx.getChild((2 * depth)));
                _environment.popContext();
            } else {
                LOG.warning("Unexpected class of last child of current context: "
                    + ctx.getChild(2 * depth).getClass().getName());
            }
        }
        return result;
    }

    @Override
    public JsonNode visitRoot_path(Root_pathContext ctx) {
        final String METHOD = "visitRoot_path";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                ctx.getText(), ctx.depth()
            });
        }
        JsonNode result = null;

        int stackSize = _environment.sizeContext();
        Deque<JsonNode> tmpStack = new LinkedList<JsonNode>();
        JsonNode tmpNode = null;
        for (; stackSize > 1; stackSize--) {
            tmpNode = _environment.popContext();
            if (tmpNode != null) {
                tmpStack.push(tmpNode);
            }
        }
        try {
            result = _environment.peekContext();
        } catch (Exception e) {
            throw e;
        } finally {
            while (!tmpStack.isEmpty()) {
                _environment.pushContext(tmpStack.pop());
            }
        }
        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    @Override
    public JsonNode visitSeq(SeqContext ctx) {
        final String METHOD = "visitSeq";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                (ctx.expr(0) == null ? "null" : ctx.expr(0).getText()),
                (ctx.expr(1) == null ? "null" : ctx.expr(1).getText()), ctx.depth()
            });
        }
        JsonNode result = null;
        JsonNode start = visit(ctx.expr(0));
        JsonNode end = visit(ctx.expr(1));

        if (start != null && !isWholeNumber(start)) {
            throw new EvaluateRuntimeException(ERR_SEQ_LHS_INTEGER);
        }

        if (end != null && !isWholeNumber(end)) {
            throw new EvaluateRuntimeException(ERR_SEQ_RHS_INTEGER);
        }

        result = factory.arrayNode();
        if (start != null && end != null) {
            int iStart = start.asInt();
            int iEnd = end.asInt();
            int count = iEnd - iStart + 1;
            if (iEnd > iStart && count > 10000000) {
                // note: below should read 1e7 not 1e6...
                throw new EvaluateRuntimeException(ERR_TOO_BIG + count + ".");
            }
            for (int i = start.asInt(); i <= end.asInt(); i++) {
                ((ArrayNode) result).add(new LongNode(i)); // use longs to align with the output of
                // visitNumber
            }
        }
        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    @Override
    public JsonNode visitString(MappingExpressionParser.StringContext ctx) {
        final String METHOD = "visitString";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                ctx.getText(), ctx.depth()
            });
        }
        JsonNode result = null;

        String val = ctx.getText();

        // strip quotes and unescape any special chars
        val = sanitise(val);

        result = TextNode.valueOf(val);
        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    @Override
    public JsonNode visitRegular_expression(MappingExpressionParser.Regular_expressionContext ctx) {
        final String METHOD = "visitRegular_expression";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                ctx.getText(), ctx.depth()
            });
        }
        JsonNode result = null;
        if (ctx.getText() != null) {
            final RegularExpression regex = new RegularExpression(ctx.getText());
            result = new POJONode(regex);
            lastVisited = METHOD;
        }
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    @Override
    public JsonNode visitRegular_expression_caseinsensitive(
        MappingExpressionParser.Regular_expression_caseinsensitiveContext ctx) {
        final String METHOD = "visitRegular_expression_caseinsensitive";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                ctx.getText(), ctx.depth()
            });
        }
        JsonNode result = null;
        if (ctx.getText() != null) {
            final RegularExpression regex = new RegularExpression(RegularExpression.Type.CASEINSENSITIVE,
                ctx.getText());
            result = new POJONode(regex);
            lastVisited = METHOD;
        }
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    @Override
    public JsonNode visitRegular_expression_multiline(MappingExpressionParser.Regular_expression_multilineContext ctx) {
        final String METHOD = "visitRegular_expression_multiline";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                ctx.getText(), ctx.depth()
            });
        }
        JsonNode result = null;
        if (ctx.getText() != null) {
            final RegularExpression regex = new RegularExpression(RegularExpression.Type.MULTILINE, ctx.getText());
            result = new POJONode(regex);
            lastVisited = METHOD;
        }
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    @Override
    public JsonNode visitOp_orderby(MappingExpressionParser.Op_orderbyContext ctx) {
        final String METHOD = "visitOp_orderby";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                ctx.getText(), ctx.depth()
            });
        }
        JsonNode result = visit(ctx.expr());
        if (result instanceof ArrayNode) {
            result = new OrderByOperator().evaluate(ctx, (ArrayNode) result);
        }
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    @Override
    public JsonNode visitTo_array(MappingExpressionParser.To_arrayContext ctx) {
        final String METHOD = "visitTo_array";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                ctx.getText(), ctx.depth()
            });
        }
        JsonNode result = null;
        keepArray = true;
        ExprContext expr = ctx.expr();
        if (expr instanceof MappingExpressionParser.PathContext || expr instanceof Context_refContext) {
            JsonNode tmpResult = visit(expr);
            if (tmpResult instanceof ExpressionsVisitor.SelectorArrayNode) {
                result = tmpResult;
            } else {
                result = visit(expr);
                result = ensureSelectorNodeArray(result);
                //				((ArrayNode) result).add(visit(expr));
            }
        } else {
            result = visit(expr);
        }
        keepArray = false;
        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    public JsonNode visitTree(ParseTree tree) {
        JsonNode result = null;
        int treeSize = tree.getChildCount();
        steps.clear();
        for (int i = 0; i < treeSize; i++) {
            steps.add(tree.getChild(i));
        }
        firstStepCons = false;
        lastStepCons = false;
        firstStep = false;
        if (tree.getChildCount() > 0) {
            if (tree.getChild(0) instanceof Array_constructorContext) {
                firstStepCons = true;
            }
            // test for laststep array construction child[n-1] instanceof
            // Array_constructorContext
            ParseTree lastStep = tree.getChild(treeSize - 1);
            if (lastStep instanceof Array_constructorContext) {
                lastStepCons = true;
            }
            if (tree.equals(steps.get(0))) {
                firstStep = true;
            } else {
                firstStep = false;
            }
        }
        lastVisited = "";
        result = visit(tree);
        if (result != null && result.isDouble()) {
            Double d = result.asDouble();
            if (Double.isInfinite(d) || Double.isNaN(d)) {
                result = JsonNodeFactory.instance.nullNode();
            }
        }
        return result;
    }

    @Override
    public JsonNode visitUnary_op(MappingExpressionParser.Unary_opContext ctx) {
        final String METHOD = "visitUnary_op";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                ctx.getText(), ctx.depth()
            });
        }
        JsonNode result = null;
        JsonNode operand = visit(ctx.expr());

        if (ctx.op.getType() == MappingExpressionParser.SUB) {

            if (operand == null) {
                result = null;
            } else if (operand.isFloatingPointNumber()) {
                result = new DoubleNode(-operand.asDouble());
            } else if (operand.isDouble()) {
                result = new DoubleNode(-operand.asDouble());
            } else if (operand.isIntegralNumber()) {
                if (operand.asLong() == Long.MAX_VALUE) {
                    result = new LongNode(-operand.asLong() - 1L);
                } else {
                    result = new LongNode(-operand.asLong());
                }
            } else if (operand.isLong()) {
                if (operand.asLong() == Long.MAX_VALUE) {
                    result = new LongNode(-operand.asLong() - 1L);
                } else {
                    result = new LongNode(-operand.asLong());
                }
            } else {
                throw new EvaluateRuntimeException(ERR_NEGATE_NON_NUMERIC);
            }
        } else {
            // cannot happen (expression will not have parsed)
            result = null;
        }

        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    @Override
    public JsonNode visitVar_assign(MappingExpressionParser.Var_assignContext ctx) {
        final String METHOD = "visitVar_assign";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                ctx.getText(), ctx.depth()
            });
        }
        JsonNode result = null;
        final String varName = ctx.VAR_ID().getText();
        ExprContext expr = ctx.expr();
        if (expr instanceof MappingExpressionParser.Function_declContext) {
            // build the declared function to assign to this variable in the
            // functionMap
            MappingExpressionParser.Function_declContext fctDeclCtx = (MappingExpressionParser.Function_declContext) expr;
            MappingExpressionParser.VarListContext varList = fctDeclCtx.varList();
            MappingExpressionParser.ExprListContext exprList = fctDeclCtx.exprList();
            DeclaredFunction fct = new DeclaredFunction(varList, exprList);
            setDeclaredFunction(varName, fct);
        } else if (expr instanceof MappingExpressionParser.Function_callContext) {
            Function_callContext fctCallCtx = (Function_callContext) expr;

            String functionName = fctCallCtx.VAR_ID().getText();

            DeclaredFunction declFct = getDeclaredFunction(functionName);
            if (declFct == null) {
                FunctionBase function = getJsonataFunction(functionName);
                if (function != null) {
                    setJsonataFunction(varName, function);
                    // result = function.invoke(this, ctx);
                } else {
                    throw new EvaluateRuntimeException("Unknown function: " + functionName);
                }
            } else {
                setDeclaredFunction(varName, declFct);
            }

            result = visit(expr);
            setVariable(varName, result);
        } else if (expr instanceof Fct_chainContext) {
            /**
             * 
             * 
             * HERE HERE HERE -- consider adding a map of Fct_chainContext keyed by varname
             * 
             * 
             */
            // need to call the first
            ExprContext exprCtx = ((Fct_chainContext) expr).expr(0);
            if (exprCtx instanceof Var_recallContext) {
                final String fctName = exprCtx.getText();
                DeclaredFunction declFct = getDeclaredFunction(fctName);
                if (declFct != null) {
                    setDeclaredFunction(varName, declFct);
                    // TODO set up an ExprValuesContext with the variable value(s) then call below
                    // _environment.pushContext(declFct.invoke(this, ruleValues));
                    result = visit(((Fct_chainContext) expr).expr(1));
                    // _environment.popContext();
                } else {
                    FunctionBase fct = getJsonataFunction(fctName);
                    if (fct != null) {
                        setJsonataFunction(varName, fct);
                        // TODO set up a Function_callContext with an ExprValuesContext and call below
                        // with it instead of ctx
                        // _environment.pushContext(fct.invoke(this, ctx));
                        result = visit(((Fct_chainContext) expr).expr(1));
                        // _environment.popContext();
                    } else {
                        result = null;
                    }
                }
            } else if (exprCtx instanceof Function_callContext) {
                Function_callContext fctCallCtx = (Function_callContext) exprCtx;

                String functionName = fctCallCtx.VAR_ID().getText();

                DeclaredFunction declFct = getDeclaredFunction(functionName);
                if (declFct == null) {
                    FunctionBase function = getJsonataFunction(functionName);
                    if (function != null) {
                        setJsonataFunction(varName, function);
                        // result = function.invoke(this, ctx);
                    } else {
                        throw new EvaluateRuntimeException("Unknown function: " + functionName);
                    }
                } else {
                    setDeclaredFunction(varName, declFct);
                }

                result = visit(expr);
                setVariable(varName, result);
            }
        } else {
            result = visit(expr);
            setVariable(varName, result);
        }
        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

    @Override
    public JsonNode visitVar_recall(MappingExpressionParser.Var_recallContext ctx) {
        final String METHOD = "visitVar_assign";
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.entering(CLASS, METHOD, new Object[] {
                ctx.getText(), ctx.depth()
            });
        }
        final String varName = ctx.getText();
        JsonNode result = getVariable(varName);
//        if (result == null) {
//          // see if this is calling a declared function with no parameters
//          DeclaredFunction declFct = getDeclaredFunction(varName);
//          if (declFct != null) {
//            ExprListContext exprListCtx = declFct.getExpressionList();
//            result = visit(exprListCtx);
//          }
//        }
        lastVisited = METHOD;
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.exiting(CLASS, METHOD, (result == null ? "null" : result.toString()));
        }
        return result;
    }

}
