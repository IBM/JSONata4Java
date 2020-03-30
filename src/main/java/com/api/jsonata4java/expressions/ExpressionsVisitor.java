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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.CommonTokenFactory;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.apache.commons.text.StringEscapeUtils;

import com.api.jsonata4java.expressions.functions.DeclaredFunction;
import com.api.jsonata4java.expressions.functions.Function;
import com.api.jsonata4java.expressions.generated.MappingExpressionBaseVisitor;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ArrayContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Array_constructorContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.BooleanContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprListContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprOrSeqContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Fct_chainContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.IdContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.NullContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Object_constructorContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.PathContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Root_pathContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.SeqContext;
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
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class ExpressionsVisitor extends MappingExpressionBaseVisitor<JsonNode> {
   
   int currentDepth = 0;
   long startTime = new Date().getTime();
   boolean checkRuntime = false;
   int maxDepth = -1;
   boolean keepSingleton = false;
   long maxTime = 0L;

   private void checkRunaway() {
      if (checkRuntime) {
         if (maxDepth != -1 && currentDepth > maxDepth) {
            throw new EvaluateRuntimeException("Stack overflow error: Check for non-terminating recursive function. Consider rewriting as tail-recursive.");
         }
      }
      if (maxTime != 0 && (new Date().getTime() - startTime > maxTime)) {
         throw new EvaluateRuntimeException("Expression evaluation timeout: Check for infinite loop");
      }
   }

   private void evaluateEntry() {
      currentDepth++;
      checkRunaway();
   }
   
   private void evaluateExit() {
      currentDepth--;
      checkRunaway();
   }
   
   public void timeboxExpression(long timeoutMS, int maxDepth) {
      if (timeoutMS > 0L && maxDepth > 0) {
         this.maxDepth = maxDepth;
         this.maxTime = timeoutMS;
         checkRuntime = true;
      }
   }
   
   public JsonNode visitTree(ParseTree tree) {
      JsonNode result = visit(tree);
      // simulates lastStep processing
      if (result != null && result.isArray() && result.size() == 1 && ((ArrayNode)result).get(0).isArray() /* && ((ArrayNode)result).get(0) instanceof SelectorArrayNode == false */ ) {
         result = ((ArrayNode)result).get(0);
      }
      return result;
   }
   
   @Override
   public JsonNode visit(ParseTree tree) {
      JsonNode result = null;
      if (checkRuntime) {
         evaluateEntry();
      }
      result = super.visit(tree);
      if (!keepSingleton) {
         if (result != null && result instanceof SelectorArrayNode && result.size() == 1 ) {
            result = ((SelectorArrayNode)result).getSelectionGroups().get(0);
         }
      }
      if (checkRuntime) {
         evaluateExit();
      }
      return result;
   }

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
       * @param group
       *              JsonNode containing the group definitions
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

   private static final String CLASS = ExpressionsVisitor.class.getName();
   public static String ERR_MSG_INVALID_PATH_ENTRY = String.format(Constants.ERR_MSG_INVALID_PATH_ENTRY,
         (Object[]) null);
   public static String ERR_NEGATE_NON_NUMERIC = "Cannot negate a non-numeric value";

   public static String ERR_SEQ_LHS_INTEGER = "The left side of the range operator (..) must evaluate to an integer";
   public static String ERR_SEQ_RHS_INTEGER = "The right side of the range operator (..) must evaluate to an integer";

   private static final Logger LOG = Logger.getLogger(CLASS);

   /**
    * This defines the behaviour of the "=" and "in" operators
    * 
    * @param left
    * @param right
    * @return
    */
   private static boolean areJsonNodesEqual(JsonNode left, JsonNode right) {
      if (left.isFloatingPointNumber() || right.isFloatingPointNumber()) {
         return (left.asDouble() == right.asDouble());
      } else if (left.isIntegralNumber() && right.isIntegralNumber()) {
         return left.asLong() == right.asLong();
      } else if (left.isNull()) {
         return right.isNull();
      } else if (right.isNull()) {
         return left.isNull();
      } else if (left.isTextual() && right.isTextual()) {
         return (left.asText().equals(right.asText()));
      } else if (left.isArray() && right.isArray()) {
         // arrays are never considered equal by JSONata
         return false;
      } else if (left.isObject() && right.isObject()) {
         // object are never considered equal by JSONata
         return false;
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
    * @param node
    *             JsonNode whose content is to be cast as a String
    * @throws EvaluateRuntimeException
    *                                  if json serialization fails
    * @return the String representation of the supplied node
    */
   public static String castString(JsonNode node) throws EvaluateRuntimeException {
      if (node == null) {
         return null;
      }

      switch (node.getNodeType()) {
      case STRING:
         return node.textValue();
      default:
         ObjectMapper objectMapper = new ObjectMapper();
         try {
            return objectMapper.writeValueAsString(node);
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
    * @return
    */
   private static ArrayNode ensureArray(JsonNode input) {
      if (input == null) {
         return null;
      } else if (input.isArray()) {
         return (ArrayNode) input;
      } else {
         return JsonNodeFactory.instance.arrayNode().add(input);
      }
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
      if ((str.startsWith("`") && str.endsWith("`")) || (str.startsWith("\"") && str.endsWith("\""))
            || (str.startsWith("'") && str.endsWith("'"))) {
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
    * @return single element if input is a singleton array or the input as presented
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

   JsonNodeFactory factory = JsonNodeFactory.instance;
   public Map<String, DeclaredFunction> functionMap = new HashMap<String, DeclaredFunction>();

   /**
    * This stack is used for storing the current "context" under which to evaluate
    * predicate-type array indexes, e.g. [{"a":1}, {"a":2}][a=2] -> {"a":2}
    * Specifically, it is used to resolve path expressions (a, in the above)
    * against each of the elements in the array in order to figure out which
    * elements match the predicate and which don't.
    * 
    * We use a stack (rather than just a singleton) here, since predicates can be
    * nested inside other predicates, e.g. [{"x":2}, {"x":3}] [x=(["a":101, "b":2},
    * {"a":102, "b":3}][a=101]).b] -> {"x":2}
    * 
    */
   private Stack<JsonNode> stack = new Stack<>();

   ParseTreeProperty<Integer> values = new ParseTreeProperty<Integer>();

   private Map<String, JsonNode> variableMap = new HashMap<String, JsonNode>();

   public ExpressionsVisitor(JsonNode rootContext) {
      if (rootContext != null) {
         this.stack.push(rootContext);
         // add a variable for the rootContext
         this.variableMap.put("$", rootContext);
      }
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

   public ArrayNode flatten(JsonNode arg, ArrayNode flattened) {
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

   /**
    * Grab the current data from the stack and recursively copy its content into an
    * array
    * 
    * @return array of the descendant content of the current data on the stack
    */
   JsonNode getDescendants() {
      JsonNode result = null;
      ArrayNode resultArray = new ArrayNode(JsonNodeFactory.instance);
      if (stack.empty() == false) {
         JsonNode startingElt = stack.peek();
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

   public Map<String, DeclaredFunction> getFunctionMap() {
      return functionMap;
   }

   public Stack<JsonNode> getStack() {
      return stack;
   }

   public int getValue(ParseTree node) {
      return values.get(node);
   }

   public Map<String, JsonNode> getVariableMap() {
      return variableMap;
   }

   protected void processArrayContent(ExprOrSeqContext expr, ArrayNode output) {
      // have a comma separated list so iterate through the expressions, skipping
      // commas
      String testStr = "";
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
         output.add(result);
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
         LOG.entering(CLASS, METHOD, new Object[] { lhs, rhsCtx.getText() });

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
         // added for Issue#30
//         output = unwrapArray(output);

      } else {
         // the LHS is just an object
         stack.push(lhs);
         output = visit(rhsCtx);
         stack.pop();
      }

      JsonNode result = output;

      if (LOG.isLoggable(Level.FINEST))
         LOG.exiting(CLASS, METHOD, result);
      return result;
   }

   public void setValue(ParseTree node, int value) {
      values.put(node, value);
   }

   /**
    * Recursively traverse the descendants of the input and them in the results
    * array
    * 
    * @param input
    *                the object, array, or node whose descendants are sought
    * @param results
    *                the array containing the descendants of the input and all of
    *                their descendants
    */
   void traverseDescendants(JsonNode input, ArrayNode results) {
      if (input != null) {
         if (input.isArray() == false) {
            // put this element into the results
            results.add(input);
         }
         // now process the "descendants" (array elements, or nodes from keys of an
         // object)
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
   public JsonNode visitAddsub_op(MappingExpressionParser.Addsub_opContext ctx) {
      JsonNode leftNode = visit(ctx.expr(0)); // get value of left subexpression
      JsonNode rightNode = visit(ctx.expr(1)); // get value of right
      // subexpression

      // in all cases, if either are *no match*, JSONata returns *no match*
      if (leftNode == null || rightNode == null) {
         return null;
      }

      if (!leftNode.isNumber() || !rightNode.isNumber()) {
         throw new EvaluateRuntimeException(ctx.op.getText() + " expects two numeric arguments");
      }

      // treat both inputs as doubles when performing arithmetic operations
      double left = leftNode.asDouble();
      double right = rightNode.asDouble();

      final double result;

      if (ctx.op.getType() == MappingExpressionParser.ADD) {
         result = left + right;
      } else if (ctx.op.getType() == MappingExpressionParser.SUB) {
         result = left - right;
      } else {
         // should never happen (this expression should not have parsed in the
         // first place)
         throw new EvaluateRuntimeException("Unrecognised token " + ctx.op.getText());
      }

      // coerce the result to a long iff the result is exactly .0
      if (isWholeNumber(result)) {
         return new LongNode((long) result);
      } else {
         return new DoubleNode(result);
      }
   }

   @Override
   public JsonNode visitArray(ArrayContext ctx) {
      final String METHOD = "visitArray";
      if (LOG.isLoggable(Level.FINEST))
         LOG.entering(CLASS, METHOD, new Object[] { ctx.getText() });

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
      if (sourceArray == null) {
         return null;
      }

      // Expression inside [] (e.g. [1])
      ExprContext indexContext = ctx.expr(1);

      // this will contain a list of indexes that should be pulled out of the
      // source array
      // any non-integral array indexes will rounded down in this list
      // may contain negative values (these will be normalized later)
      final List<Integer> indexesToReturn = new ArrayList<>();

      boolean isPredicate = false;

      for (int i = 0; i < sourceArray.size(); i++) {
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

         stack.push(e);
         JsonNode indexesInContext = visit(indexContext);
         stack.pop();

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
               if (indexInContext.isIntegralNumber()) {
                  indexesToReturn.add(indexInContext.asInt());
               } else if (indexInContext.isFloatingPointNumber()) {
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
                  throw new NonNumericArrayIndexException();
               }

            }

            // because we know it's not a predicate we also know it cannot have
            // any path references in it
            // this means that it's value is independent of context
            // thus we only need to evaluate the index once
            break;
         }
      }

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

      // some groups may not be arrays, e.g. [{"a":1}, {"a":2},
      // {"a":[3,4]}].a[n] ->
      // [1,2,[1,2]]
      // n=0 -> [1,2,3]
      // n=1 -> 4
      // where a group is not an array, treat it as a singleton array
      final ArrayNode output;
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
      if (output.size() == 0) {
         // if no results are present (i.e. results is empty) we need to return
         // null (i.e. *no match*)
         return null;
      } else {
         // if only a single result is present, it should be "unwrapped"
         // returned as a non-array
         // [1][0]==1
         // [[1]][0]==[1]
         // [[[1]]][0]==[[1]]
         JsonNode result = output;
         result = unwrapArray(result);
         return result;
      }
   }

   @Override
   public JsonNode visitArray_constructor(Array_constructorContext ctx) {
      final String METHOD = "visitArray_constructor";
      if (LOG.isLoggable(Level.FINEST))
         LOG.entering(CLASS, METHOD, new Object[] { ctx.getText(), ctx.depth() });

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

      if (LOG.isLoggable(Level.FINEST))
         LOG.exiting(CLASS, METHOD, output.toString());
      return output;
   }

   @Override
   public JsonNode visitBoolean(MappingExpressionParser.BooleanContext ctx) {
      BooleanNode result = null;
      if (ctx.op.getType() == MappingExpressionParser.TRUE) {
         result = BooleanNode.TRUE;
      } else if (ctx.op.getType() == MappingExpressionParser.FALSE) {
         result = BooleanNode.FALSE;
      }
      return result;
   }

   @Override
   public JsonNode visitComp_op(MappingExpressionParser.Comp_opContext ctx) {
      BooleanNode result = null;

      JsonNode left = visit(ctx.expr(0)); // get value of left subexpression
      JsonNode right = visit(ctx.expr(1)); // get value of right subexpression

      // in all cases, if both are *no match*, JSONata returns false
      if (left == null && right == null) {
         return BooleanNode.FALSE;
      }

      // below out for issue #11
      // in all cases, if either are *no match*, JSONata returns *no match*
      // if (left == null || right == null) {
      // return null;
      // }

      if (ctx.op.getType() == MappingExpressionParser.EQ) {
         if (left == null && right != null) {
            return BooleanNode.FALSE;
         }
         if (left != null && right == null) {
            return BooleanNode.FALSE;
         }
         // else both not null
         result = areJsonNodesEqual(left, right) ? BooleanNode.TRUE : BooleanNode.FALSE;
      } else if (ctx.op.getType() == MappingExpressionParser.NOT_EQ) {
         if (left == null && right != null) {
            return BooleanNode.TRUE;
         }
         if (left != null && right == null) {
            return BooleanNode.TRUE;
         }
         // else both not null
         result = areJsonNodesEqual(left, right) ? BooleanNode.FALSE : BooleanNode.TRUE;

      } else if (ctx.op.getType() == MappingExpressionParser.LT) {
         if (left == null || left.isNull() || right == null || right.isNull()) {
            throw new EvaluateRuntimeException(
                  "The expressions either side of operator \"<\" must evaluate to numeric or string values");
         } else if (left.isFloatingPointNumber() || right.isFloatingPointNumber()) {
            result = (left.asDouble() < right.asDouble()) ? BooleanNode.TRUE : BooleanNode.FALSE;
         } else if (left.isIntegralNumber() && right.isIntegralNumber()) {
            result = (left.asLong() < right.asLong()) ? BooleanNode.TRUE : BooleanNode.FALSE;
         } else {
            result = (left.asText().compareTo(right.asText()) == -1) ? BooleanNode.TRUE : BooleanNode.FALSE;
         }
      } else if (ctx.op.getType() == MappingExpressionParser.GT) {
         if (left == null || left.isNull() || right == null || right.isNull()) {
            throw new EvaluateRuntimeException(
                  "The expressions either side of operator \">\" must evaluate to numeric or string values");
         } else if (left.isFloatingPointNumber() || right.isFloatingPointNumber()) {
            result = (left.asDouble() > right.asDouble()) ? BooleanNode.TRUE : BooleanNode.FALSE;
         } else if (left.isIntegralNumber() && right.isIntegralNumber()) {
            result = (left.asLong() > right.asLong()) ? BooleanNode.TRUE : BooleanNode.FALSE;
         } else {
            result = (left.asText().compareTo(right.asText()) == 1) ? BooleanNode.TRUE : BooleanNode.FALSE;
         }
      } else if (ctx.op.getType() == MappingExpressionParser.LE) {
         if (left == null || left.isNull() || right == null || right.isNull()) {
            throw new EvaluateRuntimeException(
                  "The expressions either side of operator \"<=\" must evaluate to numeric or string values");
         } else if (left.isFloatingPointNumber() || right.isFloatingPointNumber()) {
            result = (left.asDouble() <= right.asDouble()) ? BooleanNode.TRUE : BooleanNode.FALSE;
         } else if (left.isIntegralNumber() && right.isIntegralNumber()) {
            result = (left.asLong() <= right.asLong()) ? BooleanNode.TRUE : BooleanNode.FALSE;
         } else {
            result = (left.asText().compareTo(right.asText()) != 1) ? BooleanNode.TRUE : BooleanNode.FALSE;
         }
      } else if (ctx.op.getType() == MappingExpressionParser.GE) {
         if (left == null || left.isNull() || right == null || right.isNull()) {
            throw new EvaluateRuntimeException(
                  "The expressions either side of operator \">=\" must evaluate to numeric or string values");
         } else if (left.isFloatingPointNumber() || right.isFloatingPointNumber()) {
            result = (left.asDouble() >= right.asDouble()) ? BooleanNode.TRUE : BooleanNode.FALSE;
         } else if (left.isIntegralNumber() && right.isIntegralNumber()) {
            result = (left.asLong() >= right.asLong()) ? BooleanNode.TRUE : BooleanNode.FALSE;
         } else {
            result = (left.asText().compareTo(right.asText()) != -1) ? BooleanNode.TRUE : BooleanNode.FALSE;
         }
      }

      return result;
   }

   @Override
   public JsonNode visitConcat_op(MappingExpressionParser.Concat_opContext ctx) {
      JsonNode left = visit(ctx.expr(0)); // get value of left subexpression
      JsonNode right = visit(ctx.expr(1)); // get value of right subexpression

      String leftStr;
      String rightStr;

      if (left == null) {
         leftStr = "";
      } else {
         leftStr = castString(left);
      }

      if (right == null) {
         rightStr = "";
      } else {
         rightStr = castString(right);
      }

      JsonNode result = new TextNode(leftStr + rightStr);

      return result;
   }

   @Override
   public JsonNode visitConditional(MappingExpressionParser.ConditionalContext ctx) {

      /* the conditional ternary operator ?: */
      JsonNode cond = visit(ctx.expr(0)); // get value of left subexpression
      if (cond instanceof BooleanNode) {
         return BooleanUtils.convertJsonNodeToBoolean(cond) ? visit(ctx.expr(1)) : visit(ctx.expr(2));
      }
      return cond;
   }

   @Override
   public JsonNode visitContext_ref(MappingExpressionParser.Context_refContext ctx) {
      JsonNode result = null;
      if (ctx.getChildCount() > 0) {
         ParseTree child0 = ctx.getChild(0);
         if (child0 instanceof TerminalNodeImpl) {
            if (((TerminalNodeImpl) child0).symbol.getText().equals("$")) {
               // replace first child with value of the rootContext
               JsonNode context = variableMap.get("$");
               CommonToken token = null;
               ExprContext expr = null;
               switch (context.getNodeType()) {
               case BINARY:
               case POJO: {
                  break;
               }
               case ARRAY: {
                  child0 = FunctionUtils.getArrayConstructorContext(ctx, (ArrayNode) context);
                  ctx.children.set(0, child0);
                  expr = new MappingExpressionParser.ArrayContext(ctx);
                  for (int i = 0; i < ctx.children.size(); i++) {
                     expr.children.add(ctx.children.get(i));
                  }
                  result = visit(expr);
                  break;
               }
               case BOOLEAN: {
                  token = (context.asBoolean()
                        ? CommonTokenFactory.DEFAULT.create(MappingExpressionParser.TRUE, context.asText())
                        : CommonTokenFactory.DEFAULT.create(MappingExpressionParser.FALSE, context.asText()));
                  TerminalNodeImpl tn = new TerminalNodeImpl(token);
                  BooleanContext nc = new MappingExpressionParser.BooleanContext(ctx);
                  nc.addAnyChild(tn);
                  child0 = nc;
                  ctx.children.set(0, child0);
                  result = visit(ctx);
                  break;
               }
               case MISSING:
               case NULL: {
                  token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.NULL, null);
                  TerminalNodeImpl tn = new TerminalNodeImpl(token);
                  child0 = tn;
                  ctx.children.set(0, child0);
                  result = visit(ctx);
                  break;
               }
               case NUMBER: {
                  token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.NUMBER, context.asText());
                  TerminalNodeImpl tn = new TerminalNodeImpl(token);
                  child0 = tn;
                  ctx.children.set(0, child0);
                  result = visit(ctx);
                  break;
               }
               case OBJECT: {
                  child0 = FunctionUtils.getObjectConstructorContext(ctx, (ObjectNode) context);
                  ctx.children.set(0, child0);
                  expr = new MappingExpressionParser.PathContext(ctx);
                  for (int i = 0; i < ctx.children.size(); i++) {
                     expr.children.add(ctx.children.get(i));
                  }
                  result = visit(expr);
                  break;
               }
               case STRING:
               default: {
                  token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.STRING, context.asText());
                  TerminalNodeImpl tn = new TerminalNodeImpl(token);
                  child0 = tn;
                  ctx.children.set(0, child0);
                  result = visit(ctx);
                  break;
               }
               } // end switch
                 // result = visit(expr);
            }
         } else {
            result = visit(ctx);
         }
      }
      return result;
   }

   @Override
   public JsonNode visitDescendant(MappingExpressionParser.DescendantContext ctx) {
      ArrayNode resultArray = new ArrayNode(JsonNodeFactory.instance);
      JsonNode descendants = getDescendants();
      ExprContext exprCtx = ctx.expr();
      if (descendants == null) {
         resultArray = null;
      } else {
         if (!descendants.isArray()) {
            if (exprCtx == null) {
               resultArray.add(descendants);
            } else {
               resultArray.add(visit(exprCtx));
            }
         } else {
            for (Iterator<JsonNode> it = ((ArrayNode) descendants).iterator(); it.hasNext();) {
               if (exprCtx == null) {
                  resultArray.add(it.next());
               } else {
                  stack.push(it.next());
                  JsonNode result = visit(exprCtx);
                  stack.pop();
                  if (result != null) {
                     resultArray.add(result);
                  }
               }
            }
         }
      }
      if (resultArray.size() == 0) {
         return null;
      }
      return resultArray;
   }

   @Override
   public JsonNode visitFct_chain(Fct_chainContext ctx) {
      JsonNode result = null;
      Object exprObj = ctx.expr(1);
      if (exprObj instanceof MappingExpressionParser.Function_callContext) {
         stack.push(visit(ctx.expr(0)));
         result = visit(ctx.expr(1));
         stack.pop();
      } else
         throw new EvaluateRuntimeException("Expected a function but got " + ctx.expr(1).getText());
      return result;
   }

   @Override
   public JsonNode visitField_values(MappingExpressionParser.Field_valuesContext ctx) {
      ArrayNode resultArray = new ArrayNode(JsonNodeFactory.instance);
      ArrayNode valArray = new ArrayNode(JsonNodeFactory.instance);
      ExprContext exprCtx = ctx.expr(); // may be null
      if (stack.empty()) {
         // signal no match
         return null;
      }
      JsonNode elt = stack.peek();
      if (elt == null || elt.isObject() == false) {
         // signal no match
         return null;
      }
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
      for (Iterator<JsonNode> it = valArray.iterator(); it.hasNext();) {
         JsonNode value = it.next();
         if (exprCtx == null) {
            resultArray.add(value);
         } else {
            stack.push(value);
            JsonNode result = visit(exprCtx);
            if (result != null) {
               resultArray.add(result);
            }
            stack.pop();
         }
      }
      if (resultArray.size() == 0) {
         return null;
      }
      return resultArray;
   }

   @Override
   public JsonNode visitFunction_call(MappingExpressionParser.Function_callContext ctx) {
      JsonNode result = null;
      String functionName = ctx.VAR_ID().getText();

      Function function = Constants.FUNCTIONS.get(functionName);
      if (function != null) {
         result = function.invoke(this, ctx);
      } else {
         DeclaredFunction declFct = functionMap.get(functionName);
         if (declFct == null) {
            throw new EvaluateRuntimeException("Unknown function: " + functionName);
         }
         result = declFct.invoke(this, ctx);
      }
      return result;
   }

   @Override
   public JsonNode visitFunction_decl(MappingExpressionParser.Function_declContext ctx) {
      /**
       * The goal is to create a JsonNode representing the function declaration that
       * could be stored (e.g., keyed by a variable id in the functionMap) and
       * executed later by creating a Function_execContext and visiting the
       * visitFunciton_exec method.
       */
      JsonNode result = null;
      result = visit(ctx);
      return result;
   }

   @Override
   public JsonNode visitFunction_exec(MappingExpressionParser.Function_execContext ctx) {
      // "function($l, $w, $h){ $l * $w * $h }(10, 10, 5)"
      // get the VarListContext for the variables to be assigned
      // get the ExprValuesContext for the values to be assigned to the
      // variables
      // get the ExprListContext to calculate the result
      JsonNode result = null;
      List<TerminalNode> varListCtx = ctx.varList().VAR_ID();
      List<ExprContext> exprValuesCtx = ctx.exprValues().exprList().expr();
      int varListCount = varListCtx.size();
      int exprListCount = exprValuesCtx.size();
      // ensure a direct mapping is possible
      if (varListCount != exprListCount) {
         throw new EvaluateRuntimeException(
               "Expected equal counts for varibles (" + varListCount + ") and values (" + exprListCount + ")");
      }
      for (int i = 0; i < varListCount; i++) {
         String varID = varListCtx.get(i).getText();
         JsonNode value = visit(exprValuesCtx.get(i));
         variableMap.put(varID, value);
      }
      ExprListContext exprListCtx = ctx.exprList();
      result = visit(exprListCtx);
      return result;
   }

   @Override
   public JsonNode visitId(IdContext ctx) {
      final String METHOD = "visitId";

      JsonNode context;
      try {
         context = stack.peek();
      } catch (EmptyStackException ex) {
         return null;
      }

      final String id = sanitise(ctx.ID().getText());
      if (LOG.isLoggable(Level.FINEST))
         LOG.entering(CLASS, METHOD, new Object[] { ctx.getText(), id, "(stack: " + context + ")" });

      JsonNode result = null;
      if (context == null) {
         result = null;
      } else {
         result = lookup(context,id);
      }


      if (LOG.isLoggable(Level.FINEST))
         LOG.exiting(CLASS, METHOD, result);
      return result;
   }
   
   JsonNode lookup(JsonNode input, String key) {
      JsonNode result = null;
      if (input.isArray()) {
          result = new SelectorArrayNode(factory); // factory.arrayNode();
          for(int ii = 0; ii < input.size(); ii++) {
              JsonNode res =  lookup(input.get(ii), key);
              if (res != null) {
                  if (res.isArray()) {
                      ((SelectorArrayNode)result).addAll((ArrayNode)res);
                  } else {
                     ((SelectorArrayNode)result).add(res);
                  }
              }
          }
      } else if (input != null && input instanceof ObjectNode) {
          result = ((ObjectNode)input).get(key);
      }
      return result;
  }


   @Override
   public JsonNode visitLogand(MappingExpressionParser.LogandContext ctx) {
      BooleanNode result = null;

      JsonNode left = visit(ctx.expr(0)); // get value of left subexpression
      JsonNode right = visit(ctx.expr(1)); // get value of right subexpression

      if (left == null || right == null)
         return null;

      result = BooleanUtils.convertJsonNodeToBoolean(left) && BooleanUtils.convertJsonNodeToBoolean(right)
            ? BooleanNode.TRUE
            : BooleanNode.FALSE;

      return result;
   }

   @Override
   public JsonNode visitLogor(MappingExpressionParser.LogorContext ctx) {
      BooleanNode result = null;

      JsonNode left = visit(ctx.expr(0)); // get value of left subexpression
      JsonNode right = visit(ctx.expr(1)); // get value of right subexpression

      if (left == null || right == null)
         return null;

      result = BooleanUtils.convertJsonNodeToBoolean(left) || BooleanUtils.convertJsonNodeToBoolean(right)
            ? BooleanNode.TRUE
            : BooleanNode.FALSE;

      return result;
   }

   // private boolean flattenOutput = true;

//   @Override
//   public JsonNode visitMap_function(MappingExpressionParser.Map_functionContext ctx) {
//      ArrayNode resultArray = new ArrayNode(JsonNodeFactory.instance);
//      // expect something that evaluates to an array and either a variable
//      // pointing to a function, or a function declaration
//      VarListContext varList = ctx.varList();
//      List<ExprListContext> exprListContext = ctx.exprList();
//      ExprListContext exprList = exprListContext.get(0);
//      boolean useContext = ((ctx.getParent() instanceof MappingExpressionParser.Fct_chainContext)
//            || (ctx.getParent() instanceof MappingExpressionParser.PathContext));
//      JsonNode arrayObj = null;
//      if (useContext) {
//         arrayObj = FunctionUtils.getContextVariable(this);
//      } else {
//         arrayObj = visit(exprList.expr(0));
//      }
//      if (arrayObj == null || !arrayObj.isArray()) {
//         throw new EvaluateRuntimeException(String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_FILTER));
//      }
//      ArrayNode mapArray = (ArrayNode) arrayObj;
//
//      ExprListContext fctBody = null;
//      if (exprListContext.size() > (useContext ? 0 : 1)) {
//         fctBody = exprListContext.get(useContext ? 0 : 1);
//      }
//
//      // below are mutually exclusive
//      TerminalNode varid = ctx.VAR_ID();
//      if (varid != null) {
//         // is this a known function reference?
//         Function function = Constants.FUNCTIONS.get(varid.getText());
//         if (function != null) {
//            for (int i = 0; i < mapArray.size(); i++) {
//               Function_callContext callCtx = new Function_callContext(ctx);
//               // note: callCtx.children should be empty unless carrying an
//               // exception
//               JsonNode element = mapArray.get(i);
//               resultArray.add(FunctionUtils.processFctCallVariables(this, function, varid, callCtx, element));
//            }
//         } else {
//            // get the function to be executed from the functionMap and execute
//            DeclaredFunction fct = functionMap.get(varid.getText());
//            if (fct == null) {
//               throw new EvaluateRuntimeException(
//                     "Expected function variable reference " + varid.getText() + " to resolve to a declared function.");
//            }
//            int varCount = fct.getVariableCount();
//            for (int i = 0; i < mapArray.size(); i++) {
//               JsonNode element = mapArray.get(i);
//               ExprValuesContext evc = new ExprValuesContext(ctx, ctx.invokingState);
//               switch (varCount) {
//               case 1: {
//                  // just pass the mapArray variable
//                  evc = FunctionUtils.fillExprVarContext(ctx, element);
//                  break;
//               }
//               case 2: {
//                  // pass the mapArray variable and index
//                  evc = FunctionUtils.fillExprVarContext(ctx, element);
//                  evc = FunctionUtils.addIndexExprVarContext(ctx, evc, i);
//                  break;
//               }
//               case 3: {
//                  // pass the mapArray variable, index, and array
//                  evc = FunctionUtils.fillExprVarContext(ctx, element);
//                  evc = FunctionUtils.addIndexExprVarContext(ctx, evc, i);
//                  evc = FunctionUtils.addArrayExprVarContext(ctx, evc, mapArray);
//                  break;
//               }
//               }
//               resultArray.add(fct.invoke(this, evc));
//            }
//         }
//      } else {
//         // we have a declared function for mapping
//         DeclaredFunction fct = new DeclaredFunction(varList, fctBody);
//         int varCount = fct.getVariableCount();
//         for (int i = 0; i < mapArray.size(); i++) {
//            JsonNode element = mapArray.get(i);
//            ExprValuesContext evc = new ExprValuesContext(ctx, ctx.invokingState);
//            switch (varCount) {
//            case 1: {
//               // just pass the mapArray variable
//               evc = FunctionUtils.fillExprVarContext(ctx, element);
//               break;
//            }
//            case 2: {
//               // pass the mapArray variable and index
//               evc = FunctionUtils.fillExprVarContext(ctx, element);
//               evc = FunctionUtils.addIndexExprVarContext(ctx, evc, i);
//               break;
//            }
//            case 3: {
//               // pass the mapArray variable, index, and array
//               evc = FunctionUtils.fillExprVarContext(ctx, element);
//               evc = FunctionUtils.addIndexExprVarContext(ctx, evc, i);
//               evc = FunctionUtils.addArrayExprVarContext(ctx, evc, mapArray);
//               break;
//            }
//            }
//            resultArray.add(fct.invoke(this, evc));
//         }
//      }
//      return resultArray;
//   }

   @Override
   public JsonNode visitMembership(MappingExpressionParser.MembershipContext ctx) {
      final String METHOD = "visitMembership";

      JsonNode left = visit(ctx.expr(0)); // get value of left subexpression
      JsonNode right = visit(ctx.expr(1)); // get value of right subexpression

      if (left == null || right == null)
         return null;

      // If the RHS is a single value, then it is treated as a singleton array.
      right = ensureArray(right);

      BooleanNode result = BooleanNode.FALSE;
      Iterator<JsonNode> elements = right.elements();
      while (elements.hasNext()) {
         JsonNode curElement = elements.next();
         if (areJsonNodesEqual(left, curElement)) {
            result = BooleanNode.TRUE;
            break;
         }
      }

      if (LOG.isLoggable(Level.FINEST))
         LOG.logp(Level.FINEST, CLASS, METHOD, "is " + left + " (" + left.getNodeType() + ") in " + right + " ("
               + right.getNodeType() + ")? -> " + result);

      return result;
   }

   @Override
   public JsonNode visitMuldiv_op(MappingExpressionParser.Muldiv_opContext ctx) {

      JsonNode leftNode = visit(ctx.expr(0)); // get value of left subexpression
      JsonNode rightNode = visit(ctx.expr(1)); // get value of right
      // subexpression

      // in all cases, if either are *no match*, JSONata returns *no match*
      if (leftNode == null || rightNode == null) {
         return null;
      }

      if (!leftNode.isNumber() || !rightNode.isNumber()) {
         throw new EvaluateRuntimeException(ctx.op.getText() + " expects two numeric arguments");
      }

      // treat both inputs as doubles when performing arithmetic operations
      double left = leftNode.asDouble();
      double right = rightNode.asDouble();

      final double result;

      if (ctx.op.getType() == MappingExpressionParser.MUL) {
         result = left * right;
      } else if (ctx.op.getType() == MappingExpressionParser.DIV) {
         if (right == 0.0d) {
            return null;
         }
         result = left / right;
      } else if (ctx.op.getType() == MappingExpressionParser.REM) {
         if (right == 0.0d) {
            return null;
         }
         result = left % right;
      } else {
         // should never happen (this expression should not have parsed in the
         // first place)
         throw new EvaluateRuntimeException("Unrecognised token " + ctx.op.getText());
      }

      // coerce the result to a long iff the result is exactly .0
      if (isWholeNumber(result)) {
         return new LongNode((long) result);
      } else {
         return new DoubleNode(result);
      }

   }

   @Override
   public JsonNode visitNull(NullContext ctx) {
      return NullNode.getInstance();
   }

   @Override
   public JsonNode visitNumber(MappingExpressionParser.NumberContext ctx) {
      /*
       * For consistency with the JavaScript implementation of JSONata, we limit the
       * size of the numbers that we handle to be within the range Double.MAX_VALUE
       * and -Double.MAX_VALUE. If we did not do this we would need to implement a lot
       * of extra code to handle BigInteger and BigDecimal. The
       * NumberUtils::convertNumberToValueNode will check whether the number is within
       * the valid range and throw a suitable exception if it is not.
       */
      return NumberUtils.convertNumberToValueNode(ctx.NUMBER().getText());
   }

   @Override
   public JsonNode visitObject_constructor(Object_constructorContext ctx) {

      ObjectNode object = factory.objectNode();

      // convert the keys to strings, stripping surrounding quotes and
      // unescaping any special JSON chars
      if (ctx.fieldList() == null) {
         // empty object: {}
         return object;
      } else {
         List<String> keys = ctx.fieldList().STRING().stream().map(k -> k.getText()).map(k -> sanitise(k))
               .collect(Collectors.toList());

         List<JsonNode> values = ctx.fieldList().expr().stream().map(e -> visit(e)).collect(Collectors.toList());

         if (keys.size() != values.size()) {
            // this shouldn't have parsed in the first place
            throw new EvaluateRuntimeException("Object key/value count mismatch!");
         }

         for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            JsonNode value = values.get(i);
            object.set(key, value);
         }

         return object;
      }

   }

   @Override
   public JsonNode visitParens(MappingExpressionParser.ParensContext ctx) {
      JsonNode result = null;
      List<ExprContext> expressions = ctx.expr();
      for (int i = 0; i < expressions.size(); i++) {
         result = visit(ctx.expr(i));
      }
      // result = visit(ctx.expr(1));
      // JsonNode result = visit(ctx.expr());

      // we need to drop out of selection mode if the params wrap a selection
      // statement
      // this is so that e.g. ([{"a":1}, {"a":2}].a)[0] returns 1 (not [1,2] as
      // would be returned without the parenthesis)
      if (result instanceof SelectorArrayNode) {
         ArrayNode newResult = factory.arrayNode();
         ((ArrayNode) newResult).addAll((SelectorArrayNode) result);
         result = newResult;
      }

      return result;
   }

   @Override
   public JsonNode visitPath(PathContext ctx) {
      final String METHOD = "visitPath";
      if (LOG.isLoggable(Level.FINEST))
         LOG.entering(CLASS, METHOD, ctx.getText());

      // [{"a":{"b":1}}, {"a":{"b":2}}].a
      final ExprContext lhsCtx = ctx.expr(0); // e.g. [{"a":{"b":1}},
      // {"a":{"b":2}}]
      final ExprContext rhsCtx = ctx.expr(1); // e.g. `a`

      // flattenOutput = true;

      /* final */ JsonNode lhs = visit(lhsCtx);
      if (lhs == null || lhs.isNull()) {
         return null; // throw new
                      // EvaluateRuntimeException(String.format(Constants.ERR_MSG_INVALID_PATH_ENTRY,"null"));
      }
      // reject path entries that are numbers or values
      switch (lhs.getNodeType()) {
      case NUMBER: {
         lhs = factory.textNode(lhs.asText());
         break;
      }
      case BOOLEAN:
      case NULL: {
         throw new EvaluateRuntimeException(String.format(Constants.ERR_MSG_INVALID_PATH_ENTRY, lhs.toString()));
      }
      default: {
         break;
      }
      }

      JsonNode rhs = resolvePath(lhs, rhsCtx);

      JsonNode result;
      if (rhs == null) { // okay to return NullNode here so don't test "|| rhs.isNull()"
         result = null;
      } else if ((rhs instanceof SelectorArrayNode) && rhs.size() == 0) {
         // if no results are present (i.e. results is empty) we need to return
         // null (i.e. *no match*)
         result = null;
      } else {
         result = rhs;
      }

      if (LOG.isLoggable(Level.FINEST))
         LOG.exiting(CLASS, METHOD, result);
      return result;
   }

   @Override
   public JsonNode visitRoot_path(Root_pathContext ctx) {
      final String METHOD = "visitRoot_path";
      if (LOG.isLoggable(Level.FINEST))
         LOG.entering(CLASS, METHOD, ctx.getText());

      // [{"a":{"b":1}}, {"a":{"b":2}}].a
      final ExprContext lhsCtx = ctx.expr(); // e.g. $$.a.b.c
      // reset the stack
      int stackSize = stack.size();
      Stack<JsonNode> tmpStack = new Stack<JsonNode>();
      for (; stackSize > 1; stackSize--) {
         tmpStack.push(stack.pop());
      }
      JsonNode result = visit(lhsCtx);
      while (!tmpStack.isEmpty()) {
         stack.push(tmpStack.pop());
      }

      if (LOG.isLoggable(Level.FINEST))
         LOG.exiting(CLASS, METHOD, result);
      return result;
   }

   @Override
   public JsonNode visitSeq(SeqContext ctx) {
      JsonNode start = visit(ctx.expr(0));
      JsonNode end = visit(ctx.expr(1));

      // in JSONata if either side of the seq is undefined, an empty array will
      // be produced
      // e.g. [xxx...10]==[]
      // to achieve this, we return null here and handle it appropriately in
      // visitArray_constructor
      if (start == null || end == null) {
         return null;
      }

      if (!isWholeNumber(start)) {
         throw new EvaluateRuntimeException(ERR_SEQ_LHS_INTEGER);
      }

      if (!isWholeNumber(end)) {
         throw new EvaluateRuntimeException(ERR_SEQ_RHS_INTEGER);
      }

      ArrayNode result = factory.arrayNode();
      for (int i = start.asInt(); i <= end.asInt(); i++) {
         result.add(new LongNode(i)); // use longs to align with the output of
         // visitNumber
      }

      return result;
   }

   @Override
   public JsonNode visitString(MappingExpressionParser.StringContext ctx) {
      String val = ctx.getText();

      // strip quotes and unescape any special chars
      val = sanitise(val);

      return TextNode.valueOf(val);
   }

   @Override
   public JsonNode visitTo_array(MappingExpressionParser.To_arrayContext ctx) {
      JsonNode result = null;
      ExprContext expr = ctx.expr();
      if (expr instanceof MappingExpressionParser.PathContext) {
         JsonNode tmpResult = visit(expr);
         if (tmpResult instanceof ExpressionsVisitor.SelectorArrayNode) {
            result = tmpResult;
         } else {
            result = JsonNodeFactory.instance.arrayNode();
            ((ArrayNode) result).add(visit(expr));
         }
      } else {
         result = visit(expr);
      }
      return result;
   }

   @Override
   public JsonNode visitUnary_op(MappingExpressionParser.Unary_opContext ctx) {
      JsonNode result = null;
      JsonNode operand = visit(ctx.expr());

      if (ctx.op.getType() == MappingExpressionParser.SUB) {

         if (operand == null) {
            return null; // NOTE: Javascript JSONata engine actually throws an
                         // exception here (but it shouldn't.. this is a bug in
                         // that engine that will be fixed)
         } else if (operand.isFloatingPointNumber()) {
            result = new DoubleNode(-operand.asDouble());
         } else if (operand.isIntegralNumber()) {
            result = new LongNode(-operand.asLong());
         } else {
            throw new EvaluateRuntimeException(ERR_NEGATE_NON_NUMERIC);
         }
      } else {
         // cannot happen (expression will not have parsed)
         result = null;
      }

      return result;
   }

   @Override
   public JsonNode visitVar_assign(MappingExpressionParser.Var_assignContext ctx) {
      final String varName = ctx.VAR_ID().getText();
      JsonNode result = null;
      ExprContext expr = ctx.expr();
      if (expr instanceof MappingExpressionParser.Function_declContext) {
         // build the declared function to assign to this variable in the
         // functionMap
         MappingExpressionParser.Function_declContext fctDeclCtx = (MappingExpressionParser.Function_declContext) expr;
         MappingExpressionParser.VarListContext varList = fctDeclCtx.varList();
         MappingExpressionParser.ExprListContext exprList = fctDeclCtx.exprList();
         DeclaredFunction fct = new DeclaredFunction(varList, exprList);
         this.functionMap.put(varName, fct);
      } else {
         result = visit(expr);
         this.variableMap.put(varName, result);
      }
      return result;
   }

   @Override
   public JsonNode visitVar_recall(MappingExpressionParser.Var_recallContext ctx) {
      final String varName = ctx.getText();
      JsonNode result = this.variableMap.get(varName);
      if (result == null) {
         throw new EvaluateRuntimeException(varName + " is unknown (e.g., unassigned variable)");
      }
      return result;
   }

}
