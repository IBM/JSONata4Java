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
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprValuesContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Fct_chainContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.IdContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.NullContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Object_constructorContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.PathContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Root_pathContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.SeqContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.VarListContext;
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

	private static final String CLASS = ExpressionsVisitor.class.getName();
	public static String ERR_NEGATE_NON_NUMERIC = "Cannot negate a non-numeric value";
	public static String ERR_SEQ_LHS_INTEGER = "The left side of the range operator (..) must evaluate to an integer";

	public static String ERR_SEQ_RHS_INTEGER = "The right side of the range operator (..) must evaluate to an integer";
	public static String ERR_MSG_INVALID_PATH_ENTRY = String.format(Constants.ERR_MSG_INVALID_PATH_ENTRY,
			(Object[]) null);

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
	 * @param node JsonNode whose content is to be cast as a String
	 * @throws EvaluateRuntimeException if json serialization fails
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
	 * @return
	 */
	private static JsonNode unwrapArray(JsonNode input) {
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
	private Map<String, DeclaredFunction> functionMap = new HashMap<String, DeclaredFunction>();

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

	protected ExpressionsVisitor(JsonNode rootContext) {
		if (rootContext != null) {
			this.stack.push(rootContext);
			// add a variable for the rootContext
			this.variableMap.put("$", rootContext);
		}
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

		final JsonNode output;

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
			// return unwrapArray(result);

			// JSONata appears to apply two levels of flattening to singleton
			// arrays pulled out of arrays, e.g.:
			// [1][0]==1
			// [[1]][0]==1
			// [[[1]]][0]==[1]
			JsonNode result = output;
			result = unwrapArray(result);
			result = unwrapArray(result);
			return result;
		}
	}

	@Override
	// public JsonNode visitArray_constructor(Array_constructorContext ctx) {
	// final String METHOD = "visitArray_constructor";
	// if (LOG.isLoggable(Level.FINEST))
	// LOG.entering(CLASS, METHOD, new Object[] {
	// ctx.getText(), ctx.depth()
	// });
	//
	// JsonNode result = factory.arrayNode();
	//
	// if (ctx.exprOrSeqList() == null) {
	// // empty array: []
	// } else {
	// ExprOrSeqListContext exprOrSeqList = ctx.exprOrSeqList();
	// result = visit(exprOrSeqList);
	// }
	//
	// if (LOG.isLoggable(Level.FINEST))
	// LOG.exiting(CLASS, METHOD, result.toString());
	// return result;
	// }
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
					JsonNode result = visit(expr);
					// result = unwrapArrayIfProducesArray(result);
					output.add(result);
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

		// in all cases, if either are *no match*, JSONata returns *no match*
		if (left == null || right == null) {
			return null;
		}

		if (ctx.op.getType() == MappingExpressionParser.EQ) {

			result = areJsonNodesEqual(left, right) ? BooleanNode.TRUE : BooleanNode.FALSE;

		} else if (ctx.op.getType() == MappingExpressionParser.NOT_EQ) {

			result = areJsonNodesEqual(left, right) ? BooleanNode.FALSE : BooleanNode.TRUE;

		} else if (ctx.op.getType() == MappingExpressionParser.LT) {
			if (left.isNull() || right.isNull()) {
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
			if (left.isNull() || right.isNull()) {
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
			if (left.isNull() || right.isNull()) {
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
			if (left.isNull() || right.isNull()) {
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

		return BooleanUtils.convertJsonNodeToBoolean(cond) ? visit(ctx.expr(1)) : visit(ctx.expr(2));
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
						break;
					}
					case MISSING:
					case NULL: {
						token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.NULL, null);
						TerminalNodeImpl tn = new TerminalNodeImpl(token);
						child0 = tn;
						break;
					}
					case NUMBER: {
						token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.NUMBER, context.asText());
						TerminalNodeImpl tn = new TerminalNodeImpl(token);
						child0 = tn;
						break;
					}
					case OBJECT: {
						child0 = FunctionUtils.getObjectConstructorContext(ctx, (ObjectNode) context);
						break;
					}
					case STRING:
					default: {
						token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.STRING, context.asText());
						TerminalNodeImpl tn = new TerminalNodeImpl(token);
						child0 = tn;
						break;
					}
					} // end switch
					result = visit(expr);
				}
			} else {
				result = visit(ctx);
			}
		}
		return result;
	}

	@Override
	public JsonNode visitEach_function(MappingExpressionParser.Each_functionContext ctx) {
		// $each(obj,function($value, $key){...} returns an ArrayNode
		ArrayNode resultArray = new ArrayNode(JsonNodeFactory.instance);
		List<ExprListContext> exprListContext = ctx.exprList();
		ExprListContext exprList = exprListContext.get(0);
		ExprContext objCtx = exprList.expr(0);
		JsonNode element = visit(objCtx);
		ExprListContext fctBody = null;
		if (exprListContext.size() > 1) {
			fctBody = exprListContext.get(1);
		}
		VarListContext varList = ctx.varList();
		TerminalNode functionid = ctx.FUNCTIONID();
		TerminalNode varid = ctx.VAR_ID();
		if (varid != null) {
			// is this a known function reference?
			Function function = Constants.FUNCTIONS.get(varid.getText());
			if (function != null) {
				Function_callContext callCtx = new Function_callContext(ctx);
				// note: callCtx.children should be empty unless carrying an
				// exception
				resultArray.add(FunctionUtils.processFctCallVariables(this, function, varid, callCtx, element));
			} else {
				// get the function to be executed from the functionMap and execute
				DeclaredFunction fct = functionMap.get(functionid.getText());
				if (fct == null) {
					throw new EvaluateRuntimeException("Expected function variable reference " + varid.getText()
							+ " to resolve to a declared function.");
				}
			}
		} else {
			// we have a declared function for each
			DeclaredFunction fct = new DeclaredFunction(varList, fctBody);
			// the each function expects the value and key from each element in the
			// object
			for (Iterator<String> it = element.fieldNames(); it.hasNext();) {
				String key = it.next();
				JsonNode value = element.get(key);
				resultArray.add(fct.invoke(this, FunctionUtils.fillExprVarContext(ctx, key, value)));
			}
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
	public JsonNode visitFilter_function(MappingExpressionParser.Filter_functionContext ctx) {
		ArrayNode resultArray = new ArrayNode(JsonNodeFactory.instance);
		// expect something that evaluates to an array and either a variable
		// pointing to a function, or a function declaration
		VarListContext varList = ctx.varList();
		List<ExprListContext> exprListContext = ctx.exprList();
		ExprListContext exprList = exprListContext.get(0);
		boolean useContext = ((ctx.getParent() instanceof MappingExpressionParser.Fct_chainContext)
				|| (ctx.getParent() instanceof MappingExpressionParser.PathContext));
		JsonNode arrayObj = null;
		if (useContext) {
			arrayObj = FunctionUtils.getContextVariable(this);
		} else {
			arrayObj = visit(exprList.expr(0));
		}
		if (arrayObj == null || !arrayObj.isArray()) {
			throw new EvaluateRuntimeException(
					String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_FILTER));
		}
		ArrayNode mapArray = (ArrayNode) arrayObj;

		ExprListContext fctBody = null;
		if (exprListContext.size() > (useContext ? 0 : 1)) {
			fctBody = exprListContext.get(useContext ? 0 : 1);
		}

		// below are mutually exclusive
		TerminalNode varid = ctx.VAR_ID();
		if (varid != null) {
			// is this a known function reference?
			Function function = Constants.FUNCTIONS.get(varid.getText());
			if (function != null) {
				for (int i = 0; i < mapArray.size(); i++) {
					Function_callContext callCtx = new Function_callContext(ctx);
					// note: callCtx.children should be empty unless carrying an
					// exception
					JsonNode element = mapArray.get(i);
					if (FunctionUtils.processFctCallVariables(this, function, varid, callCtx, element).asBoolean()) {
						resultArray.add(element);
					}
				}
			} else {
				// get the function to be executed from the functionMap and execute
				DeclaredFunction fct = functionMap.get(varid.getText());
				if (fct == null) {
					throw new EvaluateRuntimeException("Expected function variable reference " + varid.getText()
							+ " to resolve to a declared function.");
				}
				int varCount = fct.getVariableCount();
				for (int i = 0; i < mapArray.size(); i++) {
					JsonNode element = mapArray.get(i);
					ExprValuesContext evc = new ExprValuesContext(ctx, ctx.invokingState);
					switch (varCount) {
					case 1: {
						// just pass the mapArray variable
						evc = FunctionUtils.fillExprVarContext(ctx, element);
						break;
					}
					case 2: {
						// pass the mapArray variable and index
						evc = FunctionUtils.fillExprVarContext(ctx, element);
						evc = FunctionUtils.addIndexExprVarContext(ctx, evc, i);
						break;
					}
					case 3: {
						// pass the mapArray variable, index, and array
						evc = FunctionUtils.fillExprVarContext(ctx, element);
						evc = FunctionUtils.addIndexExprVarContext(ctx, evc, i);
						evc = FunctionUtils.addArrayExprVarContext(ctx, evc, mapArray);
						break;
					}
					}
					if (fct.invoke(this, evc).asBoolean()) {
						resultArray.add(element);
					}
				}
			}
		} else {
			// we have a declared function for filtering
			DeclaredFunction fct = new DeclaredFunction(varList, fctBody);
			int varCount = fct.getVariableCount();
			for (int i = 0; i < mapArray.size(); i++) {
				JsonNode element = mapArray.get(i);
				ExprValuesContext evc = new ExprValuesContext(ctx, ctx.invokingState);
				switch (varCount) {
				case 1: {
					// just pass the mapArray variable
					evc = FunctionUtils.fillExprVarContext(ctx, element);
					break;
				}
				case 2: {
					// pass the mapArray variable and index
					evc = FunctionUtils.fillExprVarContext(ctx, element);
					evc = FunctionUtils.addIndexExprVarContext(ctx, evc, i);
					break;
				}
				case 3: {
					// pass the mapArray variable, index, and array
					evc = FunctionUtils.fillExprVarContext(ctx, element);
					evc = FunctionUtils.addIndexExprVarContext(ctx, evc, i);
					evc = FunctionUtils.addArrayExprVarContext(ctx, evc, mapArray);
					break;
				}
				}
				if (fct.invoke(this, evc).asBoolean()) {
					resultArray.add(element);
				}
			}
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
			throw new EvaluateRuntimeException("Unknown function: " + functionName);
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

		// does the ID have [] after it? If so, we need to suppress the array
		// flattening behaviour
		// e.g. {"a":[1]}.a==1, but {"a":[1]}.a[]==[1]
		// flattenOutput = (ctx.ARR_OPEN() == null) && flattenOutput; // only
		// flatten output if this ID and all IDs after it in this path DO NOT have
		// [] after them

		JsonNode result;
		if (context == null) {
			result = null;
		} else {
			result = context.get(id);
		}

		result = unwrapArray(result);

		if (LOG.isLoggable(Level.FINEST))
			LOG.exiting(CLASS, METHOD, result);
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

	@Override
	public JsonNode visitMap_function(MappingExpressionParser.Map_functionContext ctx) {
		ArrayNode resultArray = new ArrayNode(JsonNodeFactory.instance);
		// expect something that evaluates to an array and either a variable
		// pointing to a function, or a function declaration
		VarListContext varList = ctx.varList();
		List<ExprListContext> exprListContext = ctx.exprList();
		ExprListContext exprList = exprListContext.get(0);
		boolean useContext = ((ctx.getParent() instanceof MappingExpressionParser.Fct_chainContext)
				|| (ctx.getParent() instanceof MappingExpressionParser.PathContext));
		JsonNode arrayObj = null;
		if (useContext) {
			arrayObj = FunctionUtils.getContextVariable(this);
		} else {
			arrayObj = visit(exprList.expr(0));
		}
		if (arrayObj == null || !arrayObj.isArray()) {
			throw new EvaluateRuntimeException(
					String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_FILTER));
		}
		ArrayNode mapArray = (ArrayNode) arrayObj;

		ExprListContext fctBody = null;
		if (exprListContext.size() > (useContext ? 0 : 1)) {
			fctBody = exprListContext.get(useContext ? 0 : 1);
		}

		// below are mutually exclusive
		TerminalNode varid = ctx.VAR_ID();
		if (varid != null) {
			// is this a known function reference?
			Function function = Constants.FUNCTIONS.get(varid.getText());
			if (function != null) {
				for (int i = 0; i < mapArray.size(); i++) {
					Function_callContext callCtx = new Function_callContext(ctx);
					// note: callCtx.children should be empty unless carrying an
					// exception
					JsonNode element = mapArray.get(i);
					resultArray.add(FunctionUtils.processFctCallVariables(this, function, varid, callCtx, element));
				}
			} else {
				// get the function to be executed from the functionMap and execute
				DeclaredFunction fct = functionMap.get(varid.getText());
				if (fct == null) {
					throw new EvaluateRuntimeException("Expected function variable reference " + varid.getText()
							+ " to resolve to a declared function.");
				}
				int varCount = fct.getVariableCount();
				for (int i = 0; i < mapArray.size(); i++) {
					JsonNode element = mapArray.get(i);
					ExprValuesContext evc = new ExprValuesContext(ctx, ctx.invokingState);
					switch (varCount) {
					case 1: {
						// just pass the mapArray variable
						evc = FunctionUtils.fillExprVarContext(ctx, element);
						break;
					}
					case 2: {
						// pass the mapArray variable and index
						evc = FunctionUtils.fillExprVarContext(ctx, element);
						evc = FunctionUtils.addIndexExprVarContext(ctx, evc, i);
						break;
					}
					case 3: {
						// pass the mapArray variable, index, and array
						evc = FunctionUtils.fillExprVarContext(ctx, element);
						evc = FunctionUtils.addIndexExprVarContext(ctx, evc, i);
						evc = FunctionUtils.addArrayExprVarContext(ctx, evc, mapArray);
						break;
					}
					}
					resultArray.add(fct.invoke(this, evc));
				}
			}
		} else {
			// we have a declared function for mapping
			DeclaredFunction fct = new DeclaredFunction(varList, fctBody);
			int varCount = fct.getVariableCount();
			for (int i = 0; i < mapArray.size(); i++) {
				JsonNode element = mapArray.get(i);
				ExprValuesContext evc = new ExprValuesContext(ctx, ctx.invokingState);
				switch (varCount) {
				case 1: {
					// just pass the mapArray variable
					evc = FunctionUtils.fillExprVarContext(ctx, element);
					break;
				}
				case 2: {
					// pass the mapArray variable and index
					evc = FunctionUtils.fillExprVarContext(ctx, element);
					evc = FunctionUtils.addIndexExprVarContext(ctx, evc, i);
					break;
				}
				case 3: {
					// pass the mapArray variable, index, and array
					evc = FunctionUtils.fillExprVarContext(ctx, element);
					evc = FunctionUtils.addIndexExprVarContext(ctx, evc, i);
					evc = FunctionUtils.addArrayExprVarContext(ctx, evc, mapArray);
					break;
				}
				}
				resultArray.add(fct.invoke(this, evc));
			}
		}
		return resultArray;
	}

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
			result = left / right;
		} else if (ctx.op.getType() == MappingExpressionParser.REM) {
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

	// @Override
	// public JsonNode
	// visitExprOrSeqList(MappingExpressionParser.ExprOrSeqListContext ctx) {
	// JsonNode result = visitChildren(ctx);
	//// ArrayNode array = JsonNodeFactory.instance.arrayNode(); //
	// visitChildren(ctx);
	//// for (int i = 0; i < ctx.getChildCount(); i++) {
	//// ParseTree child = ctx.getChild(i);
	//// if (child instanceof ExprOrSeqContext) {
	//// ExprOrSeqContext expr = (ExprOrSeqContext)child;
	//// result = visit(expr);
	//// array.add(result);
	//// }
	//// } // end for each child in the list
	// return result;
	// }
	//
	// @Override
	// public JsonNode visitExprOrSeq(
	// MappingExpressionParser.ExprOrSeqContext ctx) {
	//
	// JsonNode result = visitChildren(ctx);
	//// if (ctx.seq() == null) {
	//// // this is an expression
	//// int childCount = ctx.getChildCount();
	//// if (childCount == 1) {
	//// result = visit(ctx.expr());
	//// } else {
	//// ArrayNode array = JsonNodeFactory.instance.arrayNode();
	//// for (int i=0;i<childCount;i++) {
	//// result = visit(ctx.getChild(i));
	//// array.add(result);
	//// }
	//// result = array;
	//// }
	//// } else {
	//// // this is a sequence
	//// result = visit(ctx.seq());
	//// }
	// return result;
	// }

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

		final JsonNode lhs = visit(lhsCtx);
		if (lhs == null) {
			return null; // throw new
							// EvaluateRuntimeException(String.format(Constants.ERR_MSG_INVALID_PATH_ENTRY,"null"));
		}
		// reject path entries that are numbers or values
		switch (lhs.getNodeType()) {
		case NUMBER:
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
		if (rhs == null) {
			result = null;
		} else if ((rhs instanceof SelectorArrayNode) && rhs.size() == 0) {
			// if no results are present (i.e. results is empty) we need to return
			// null (i.e. *no match*)
			result = null;
		} else {
			result = rhs;
		}

		// JSONata appears to apply two levels of flattening to singleton arrays
		// pulled out by paths, e.g.:
		// {"a": [1]}.a==1
		// {"a": [[1]]}.a==1
		// {"a": [[[1]]]}.a==[1]
		// (the other level of flattening is performed on the return of visitId())
		result = unwrapArray(result);

		if (LOG.isLoggable(Level.FINEST))
			LOG.exiting(CLASS, METHOD, result);
		return result;
	}

	/*
	 * ==================== = UTILITY FUNCTIONS ====================
	 */

	@Override
	public JsonNode visitReduce_function(MappingExpressionParser.Reduce_functionContext ctx) {

		JsonNode result = null;
		// expect something that evaluates to an array and either a variable
		// pointing to a function, or a function declaration
		VarListContext varList = ctx.varList();
		List<ExprListContext> exprListContext = ctx.exprList();
		ExprListContext exprList = exprListContext.get(0);
		boolean useContext = ((ctx.getParent() instanceof MappingExpressionParser.Fct_chainContext)
				|| (ctx.getParent() instanceof MappingExpressionParser.PathContext));
		JsonNode arrayObj = null;
		if (useContext) {
			arrayObj = FunctionUtils.getContextVariable(this);
		} else {
			arrayObj = visit(exprList.expr(0));
		}
		if (arrayObj == null || !arrayObj.isArray()) {
			throw new EvaluateRuntimeException(
					String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_FILTER));
		}
		ArrayNode mapArray = (ArrayNode) arrayObj;

		ExprListContext fctBody = null;
		if (exprListContext.size() > (useContext ? 0 : 1)) {
			fctBody = exprListContext.get(useContext ? 0 : 1);
		}

		int startIndex = 1;
		ExprOrSeqContext init = null;
		JsonNode prevResult = mapArray.get(0);
		List<ExprOrSeqContext> exprSeqList = ctx.exprOrSeq();
		if (exprSeqList.size() > 0) {
			init = exprSeqList.get(0);
			prevResult = visit(init);
			startIndex = 0;
		}

		// below are mutually exclusive
		TerminalNode varid = ctx.VAR_ID();
		if (varid != null) {
			// is this a known function reference?
			Function function = Constants.FUNCTIONS.get(varid.getText());
			if (function != null) {
				for (int i = startIndex; i < mapArray.size(); i++) {
					Function_callContext callCtx = new Function_callContext(ctx);
					// note: callCtx.children should be empty unless carrying an
					// exception
					JsonNode element = mapArray.get(i);
					prevResult = (FunctionUtils.processFctCallVariables(this, function, varid, callCtx, prevResult,
							element));
				}
			} else {
				// get the function to be executed from the functionMap and execute
				DeclaredFunction fct = functionMap.get(varid.getText());
				if (fct == null) {
					throw new EvaluateRuntimeException("Expected function variable reference " + varid.getText()
							+ " to resolve to a declared function.");
				}
				for (int i = startIndex; i < mapArray.size(); i++) {
					JsonNode element = mapArray.get(i);
					ExprValuesContext evc = new ExprValuesContext(ctx, ctx.invokingState);
					evc = FunctionUtils.fillExprVarContext(ctx, prevResult, element);
					prevResult = fct.invoke(this, evc);
				}
			}
		} else {
			// we have a declared function for mapping
			DeclaredFunction fct = new DeclaredFunction(varList, fctBody);
			for (int i = startIndex; i < mapArray.size(); i++) {
				JsonNode element = mapArray.get(i);
				ExprValuesContext evc = new ExprValuesContext(ctx, ctx.invokingState);
				evc = FunctionUtils.fillExprVarContext(ctx, prevResult, element);
				prevResult = fct.invoke(this, evc);
			}
		}
		result = prevResult;
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

		// JSONata appears to apply two levels of flattening to singleton arrays
		// pulled out by paths, e.g.:
		// {"a": [1]}.a==1
		// {"a": [[1]]}.a==1
		// {"a": [[[1]]]}.a==[1]
		// (the other level of flattening is performed on the return of visitId())
		result = unwrapArray(result);

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
	public JsonNode visitSift_function(MappingExpressionParser.Sift_functionContext ctx) {
		ObjectNode resultObject = new ObjectNode(JsonNodeFactory.instance);
		// expect something that evaluates to an object and either a variable
		// pointing to a function, or a function declaration
		VarListContext varList = ctx.varList();
		List<ExprListContext> exprListContext = ctx.exprList();
		ExprListContext exprList = exprListContext.get(0);
		boolean useContext = ((ctx.getParent() instanceof MappingExpressionParser.Fct_chainContext)
				|| (ctx.getParent() instanceof MappingExpressionParser.PathContext));
		JsonNode objNode = null;
		if (useContext) {
			objNode = FunctionUtils.getContextVariable(this);
		} else {
			objNode = visit(exprList.expr(0));
		}
		if (objNode == null || !objNode.isObject()) {
			throw new EvaluateRuntimeException(
					String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_FILTER));
		}
		ObjectNode object = (ObjectNode) objNode;

		ExprListContext fctBody = null;
		if (exprListContext.size() > (useContext ? 0 : 1)) {
			fctBody = exprListContext.get(useContext ? 0 : 1);
		}

		// below are mutually exclusive
		TerminalNode varid = ctx.VAR_ID();
		if (varid != null) {
			// is this a known function reference?
			Function function = Constants.FUNCTIONS.get(varid.getText());
			if (function != null) {
				for (Iterator<String> it = object.fieldNames(); it.hasNext();) {
					String key = it.next();
					JsonNode field = object.get(key);
					Function_callContext callCtx = new Function_callContext(ctx);
					// note: callCtx.children should be empty unless carrying an
					// exception
					if (FunctionUtils.processFctCallVariables(this, function, varid, callCtx, field, key, object)
							.asBoolean()) {
						resultObject.set(key, field);
					}
				}
			} else {
				// get the function to be executed from the functionMap and execute
				DeclaredFunction fct = functionMap.get(varid.getText());
				if (fct == null) {
					throw new EvaluateRuntimeException("Expected function variable reference " + varid.getText()
							+ " to resolve to a declared function.");
				}
				int varCount = fct.getVariableCount();
				for (Iterator<String> it = object.fieldNames(); it.hasNext();) {
					String key = it.next();
					JsonNode field = object.get(key);
					ExprValuesContext evc = new ExprValuesContext(ctx, ctx.invokingState);
					switch (varCount) {
					case 1: {
						// just pass the field value
						evc = FunctionUtils.fillExprVarContext(ctx, field);
						break;
					}
					case 2: {
						// pass the field value and index
						evc = FunctionUtils.fillExprVarContext(ctx, field);
						evc = FunctionUtils.addStringExprVarContext(ctx, evc, key);
						break;
					}
					case 3: {
						// pass the mapArray variable, index, and array
						evc = FunctionUtils.fillExprVarContext(ctx, field);
						evc = FunctionUtils.addStringExprVarContext(ctx, evc, key);
						evc = FunctionUtils.addObjectExprVarContext(ctx, evc, object);
						break;
					}
					}
					if (fct.invoke(this, evc).asBoolean()) {
						resultObject.set(key, field);
					}
				}
			}
		} else {
			// we have a declared function for sifting
			DeclaredFunction fct = new DeclaredFunction(varList, fctBody);
			int varCount = fct.getVariableCount();
			for (Iterator<String> it = object.fieldNames(); it.hasNext();) {
				String key = it.next();
				JsonNode field = object.get(key);
				ExprValuesContext evc = new ExprValuesContext(ctx, ctx.invokingState);
				switch (varCount) {
				case 1: {
					// just pass the field value
					evc = FunctionUtils.fillExprVarContext(ctx, field);
					break;
				}
				case 2: {
					// pass the field value and key
					evc = FunctionUtils.fillExprVarContext(ctx, field);
					evc = FunctionUtils.addStringExprVarContext(ctx, evc, key);
					break;
				}
				case 3: {
					// pass the field value, key, and object
					evc = FunctionUtils.fillExprVarContext(ctx, field);
					evc = FunctionUtils.addStringExprVarContext(ctx, evc, key);
					evc = FunctionUtils.addObjectExprVarContext(ctx, evc, object);
					break;
				}
				}
				if (fct.invoke(this, evc).asBoolean()) {
					resultObject.set(key, field);
				}
			}
		}
		return resultObject;
	}

	@Override
	public JsonNode visitString(MappingExpressionParser.StringContext ctx) {
		String val = ctx.getText();

		// strip quotes and unescape any special chars
		val = sanitise(val);

		return TextNode.valueOf(val);
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
