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

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
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
import com.api.jsonata4java.expressions.functions.Function;
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
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_declContext;
// import com.api.jsonata4java.expressions.generated.MappingExpressionParser.FieldListContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.IdContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.NullContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.NumberContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Object_constructorContext;
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
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

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
	 * @param node
	 *                 JsonNode whose content is to be cast as a String
	 * @param prettify
	 *                 Whether the objects or arrays should be pretty printed
	 * @throws EvaluateRuntimeException
	 *                                  if json serialization fails
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
					int exp = new Integer(test.substring(index + 1));
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
					JsonElement jsonElt = JsonParser
							.parseString(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node));
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
	private boolean inArrayConstructor = false;
	private boolean keepSingleton = false;
	private boolean lastStep = false;
	private boolean lastStepCons = false;
	private int maxDepth = -1;
	private long maxTime = 0L;
	private long startTime = new Date().getTime();
	private List<ParseTree> steps = new ArrayList<ParseTree>();
	private ParseTreeProperty<Integer> values = new ParseTreeProperty<Integer>();

	public ExpressionsVisitor() {
		setEnvironment(null);
		setRootContext(null);
	}

	public ExpressionsVisitor(JsonNode rootContext, FrameEnvironment environment) throws EvaluateRuntimeException {
		setEnvironment(environment);
		setRootContext(rootContext);
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

	public Stack<JsonNode> getContextStack() {
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

	public Function getJsonataFunction(String fctName) {
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
				if (tree instanceof Array_constructorContext == false && inArrayConstructor && result.isArray()) {
					output.addAll((ArrayNode) result);
				} else {
					output.add(result);
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
			// output = unwrapArray(output);

		} else {
			// the LHS is just an object
			_environment.pushContext(lhs);
			output = visit(rhsCtx);
			_environment.popContext();
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

	public void setJsonataFunction(String fctName, Function fctValue) {
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
	public JsonNode visit(ParseTree tree) {
		JsonNode result = null;
		
		if (checkRuntime) {
			evaluateEntry();
		}
		if (steps.size() > 0 && tree.equals(steps.get(steps.size() - 1))) {
			lastStep = true;
		}
		
		result = super.visit(tree);
		
		if (!keepSingleton) {
			if (result != null && result instanceof SelectorArrayNode) {
//					&& ((SelectorArrayNode) result).getSelectionGroups().size() == 1) {
//				result = ((SelectorArrayNode) result).getSelectionGroups().get(0);
				if (result.size() == 1) {
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

		ArrayNode output = JsonNodeFactory.instance.arrayNode();
		boolean isPredicate = false;

		// trying changes to emulate jsonata.js behavior at 3:37pm 4/20
		// act differently if we have a SelectorArrayNode
		boolean sourceIsSAN = false;
		List<JsonNode> selGroups = new ArrayList<JsonNode>();
		if (sourceArray instanceof SelectorArrayNode) {
//			sourceIsSAN = true;
			selGroups = ((SelectorArrayNode) sourceArray).getSelectionGroups();
		}
		for (int i = 0; i < (sourceIsSAN ? selGroups.size() : sourceArray.size()); i++) {
			JsonNode e = (sourceIsSAN ? selGroups.get(i) : sourceArray.get(i));

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
//			if (indexContext instanceof Comp_opContext) {
//				ArrayNode desiredIndexes = factory.arrayNode();
//				isPredicate = true;
//				// we can evaluate the comparision by testing its parts
//				JsonNode left = visit(((Comp_opContext)indexContext).expr(0)); // get value of left subexpression
//				JsonNode right = visit(((Comp_opContext)indexContext).expr(1)); // get value of right subexpression
//				if (left!=null) {
//					left = ensureArray(left);
//					if (right != null) {
//						right = ensureArray(right);
//						// use the comparator to see which indexes to preserve
//						for (int xx = 0; xx<left.size();xx++) {
//							int type = ((Comp_opContext)indexContext).op.getType();
//							switch(type) {
//							case MappingExpressionParser.EQ: {
//								if (left.get(xx).equals(right.get(0))) {
//									desiredIndexes.add(xx);
//								}
//								break;
//							}
//							case MappingExpressionParser.NOT_EQ: {
//								if (left.get(xx).equals(right.get(0)) == false) {
//									desiredIndexes.add(xx);
//								}
//								break;
//							}
//							case MappingExpressionParser.LT: {
//								// TODO: add comparison
//								System.out.println("LT Comparator visit array");
//								break;
//							}
//							case MappingExpressionParser.LE: {
//								// TODO: add comparison
//								System.out.println("LE Comparator visit array");
//								break;
//							}
//							case MappingExpressionParser.GT: {
//								// TODO: add comparison
//								System.out.println("LT Comparator visit array");
//								break;
//							}
//							case MappingExpressionParser.GE: {
//								// TODO: add comparison
//								System.out.println("Unknown Comparator ("+type+") visit array");
//								break;
//							}
//							default: {
//								break;
//							}
//						}
//					}
//				}
//				indexesInContext = desiredIndexes;
//			} else {
			// below will resolve if the proposed indexContext applies to the current group
			indexesInContext = visit(indexContext);
//			}
			_environment.popContext();
//			if (e.isObject()) {
//				// we have what we were after so add it to the output and return
//				if (indexesInContext != null) {
//					// check to see if the visit above found what we wanted
//					if (indexesInContext.isArray()) {
//						if (inArrayConstructor) { 
//							output.add(indexesInContext);
//						} else {
//							ArrayNode array = (ArrayNode)indexesInContext;
//							JsonNode elt = null;
//							for (int j=0;j<array.size();j++) {
//								elt = array.get(j);
//								if (elt != null) {
//									output.add(elt);
//								}
//							}
//						}
//					} else {
//						// visit above found the index we wanted
//						if (indexesInContext.isNumber()) {
//							// be sure to resolve the index in case it is negative
//							int index = indexesInContext.asInt();
//							if (index < 0) {
//								index = sourceArray.size() + index;
//							}
//							if (sourceIsSAN) {
//								output.add(e);
//							} else if (index == i) {
//								output.add(e);
//							}
//						} else if (indexesInContext.isBoolean()) {
//							if (indexesInContext.asBoolean()) {
//								output.add(e);
//							}
//						}
//					}
//				}
//				continue;
//				
//			} else if (e.isArray()) {
//				ArrayNode eArray = (ArrayNode)e;
//				if (indexesInContext != null) {
//					// check to see if the visit above found what we wanted
//					if (indexesInContext.isArray()) {
//						ArrayNode array = (ArrayNode)indexesInContext;
//						JsonNode elt = null;
//						for (int j=0;j<array.size();j++) {
//							elt = array.get(j);
//							if (elt != null) {
//								output.add(eArray.get(elt.asInt()));
//							}
//						}
//					} else {
//						// visit above found the index we wanted
//						if (indexesInContext.isNumber()) {
//							// be sure to resolve the index in case it is negative
//							int index = indexesInContext.asInt();
//							if (index < 0) {
//								index = sourceArray.size() + index;
//							}
//							if (sourceIsSAN) {
//								output.add(eArray.get(index));
//							} else if (index == i) {
//								output.add(eArray.get(index));
//							}
//						} else if (indexesInContext.isBoolean()) {
//							if (indexesInContext.asBoolean()) {
//								output.add(eArray);
//							}
//						}
//					}
//				}
//				continue;
//			}

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
//				if (!sourceIsSAN) {
				isPredicate = true;
//				}

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
								return sourceArray;
							} else {
								if (indexesInContext.size() == 1) {
									return null;
								}
								return sourceArray; // null;
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
									return sourceArray;
								} else {
									return null;
								}
							}
							throw new NonNumericArrayIndexException();
						}
						// return sourceArray; // wnm3 added for case002 on multiple-array-selectors
					}

				}

				// because we know it's not a predicate we also know it cannot have
				// any path references in it
				// this means that it's value is independent of context
				// thus we only need to evaluate the index once
				break;
			}
		}

//		if (output.size() == 0) {
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
//		}	
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

		inArrayConstructor = true;
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
			inArrayConstructor = false;
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
		BooleanNode result = null;

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
			// check if the same types
			if (left.getNodeType() == right.getNodeType()) {
				return (areJsonNodesEqual(left, right) ? BooleanNode.TRUE : BooleanNode.FALSE);
				// return (left.equals(right) ? BooleanNode.TRUE : BooleanNode.FALSE);
			}
			if (!lIsComparable || !rIsComparable) {
				// signal expression
				return null;
			}
			// different types of comparables return not equal
			return BooleanNode.FALSE;
		} else if (ctx.op.getType() == MappingExpressionParser.NOT_EQ) {
			if (left == null && right != null) {
				return BooleanNode.TRUE;
			}
			if (left != null && right == null) {
				return BooleanNode.TRUE;
			}
			// else both not null
			// check if the same types
			if (left.getNodeType() == right.getNodeType()) {
				return (areJsonNodesEqual(left, right) ? BooleanNode.FALSE : BooleanNode.TRUE);
				// return (left.equals(right) ? BooleanNode.FALSE : BooleanNode.TRUE);
			}
			if (!lIsComparable || !rIsComparable) {
				// signal expression
				return null;
			}
			// different types of comparables return not equal
			return BooleanNode.TRUE;
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
					return null;
				}
				if (left.getNodeType() != right.getNodeType()) {
					throw new EvaluateRuntimeException("The values " + left.toString() + " and " + right.toString()
							+ " either side of operator \">\" must be of the same data type");
				}
				result = (left.asText().compareTo(right.asText()) < 0) ? BooleanNode.TRUE : BooleanNode.FALSE;
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
					return null;
				}
				if (left.getNodeType() != right.getNodeType()) {
					throw new EvaluateRuntimeException("The values " + left.toString() + " and " + right.toString()
							+ " either side of operator \"<=\" must be of the same data type");
				}
				result = (left.asText().compareTo(right.asText()) <= 0) ? BooleanNode.TRUE : BooleanNode.FALSE;
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
					return null;
				}
				if (left.getNodeType() != right.getNodeType()) {
					throw new EvaluateRuntimeException("The values " + left.toString() + " and " + right.toString()
							+ " either side of operator \">=\" must be of the same data type");
				}
				result = (left.asText().compareTo(right.asText()) >= 0) ? BooleanNode.TRUE : BooleanNode.FALSE;
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

		JsonNode result = new TextNode(leftStr + rightStr);

		return result;
	}

	@Override
	public JsonNode visitConditional(MappingExpressionParser.ConditionalContext ctx) {

		/* the conditional ternary operator ?: */
		JsonNode cond = visit(ctx.expr(0)); // get value of left subexpression
		if (cond == null) {
			ExprContext ctx2 = ctx.expr(2);
			if (ctx2 != null) {
				return visit(ctx2);
			} else {
				return null;
			}
		} else if (cond instanceof BooleanNode || cond instanceof NumericNode) {
			ExprContext ctx1 = ctx.expr(1);
			ExprContext ctx2 = ctx.expr(2);
			if (ctx1 != null && ctx2 != null) {
				return BooleanUtils.convertJsonNodeToBoolean(cond) ? visit(ctx.expr(1)) : visit(ctx.expr(2));
			} else {
				if (BooleanUtils.convertJsonNodeToBoolean(cond)) {
					if (ctx1 != null) {
						return visit(ctx1);
					}
					return null;
				} else {
					if (ctx2 != null) {
						return visit(ctx2);
					}
					return null;
				}
			}
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
					JsonNode context = getVariable("$");
					if (ctx.children.size() == 1) {
						// nothing left to visit
						return context;
					}
					/**
					 * can not just replace ctx child since this can be called recursively with
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
								? CommonTokenFactory.DEFAULT.create(MappingExpressionParser.TRUE, context.asText())
								: CommonTokenFactory.DEFAULT.create(MappingExpressionParser.FALSE, context.asText()));
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
						token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.NUMBER, context.asText());
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
						token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.STRING, context.asText());
						TerminalNodeImpl tn = new TerminalNodeImpl(token);
						StringContext sc = new StringContext(ctx);
						sc.children.set(0, tn);
						result = visit(sc);
						break;
					}
					} // end switch
						// result = visit(expr);
				}
			} else {
				result = visit(child0);
			}
		}
		return result;
	}

	@Override
	public JsonNode visitDescendant(MappingExpressionParser.DescendantContext ctx) {
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
			return null;
		}
		JsonNode result = unwrapArray(resultArray);
		return result;
	}

	@Override
	public JsonNode visitFct_chain(Fct_chainContext ctx) {
		JsonNode result = null;
		Object exprObj = ctx.expr(1);
		if (exprObj instanceof MappingExpressionParser.Function_callContext) {
			JsonNode test = visit(ctx.expr(0));
			// leave it to the receiving function to complain
//			if (test == null) {
//				return null;
//			}
			_environment.pushContext(test);
			result = visit(ctx.expr(1));
			_environment.popContext();
		} else if (exprObj instanceof Var_recallContext) {
			String fctName = ((Var_recallContext) exprObj).VAR_ID().getText();
			// assume this is a variable pointing to a function
			DeclaredFunction declFct = getDeclaredFunction(fctName);
			if (declFct == null) {
				Function function = getJsonataFunction(fctName);
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
		} else
			throw new EvaluateRuntimeException("Expected a function but got " + ctx.expr(1).getText());
		return result;
	}

	@Override
	public JsonNode visitField_values(MappingExpressionParser.Field_valuesContext ctx) {
		ArrayNode resultArray = new ArrayNode(JsonNodeFactory.instance);
		ArrayNode valArray = new ArrayNode(JsonNodeFactory.instance);
		if (_environment.isEmptyContext()) {
			// signal no match
			return null;
		}
		JsonNode elt = _environment.peekContext();
		if (elt == null || (elt.isObject() || elt.isArray()) == false) {
			// signal no match
			return null;
		}
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
			for (Iterator<JsonNode> it = ((ArrayNode)elt).iterator();it.hasNext();) {
				JsonNode value = it.next();
				if (value.isArray()) {
					value = flatten(value,null);
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
		if (resultArray.size() == 0) {
			return null;
		}
		JsonNode result = unwrapArray(resultArray);
		return result;
	}

	@Override
	public JsonNode visitFieldList(MappingExpressionParser.FieldListContext ctx) {
		ObjectNode resultObject = new ObjectNode(JsonNodeFactory.instance);
		if (_environment.isEmptyContext()) {
			// signal no match
			return null;
		}
		JsonNode elt = _environment.peekContext();
		if (elt == null || elt.isObject() == false) {
			// signal no match
			return null;
		}
		String key = "";
		JsonNode value = null;
		for (int i = 0; i < ctx.getChildCount(); i += 4) {
			// expect name ':' expr ,
			key = sanitise(((TerminalNodeImpl) ctx.getChild(i)).getText());
			value = visit(ctx.getChild(i + 2));
			if (value != null) {
				resultObject.set(key, value);
			}
		}
		if (resultObject.size() == 0) {
			return null;
		}
		return resultObject;
	}

	@Override
	public JsonNode visitFunction_call(MappingExpressionParser.Function_callContext ctx) {
		JsonNode result = null;
		String functionName = ctx.VAR_ID().getText();

		DeclaredFunction declFct = getDeclaredFunction(functionName);
		if (declFct == null) {
			Function function = getJsonataFunction(functionName);
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
		final String fctName = ctx.FUNCTIONID().getText();
		RuleContext expr = ctx.getRuleContext();

		JsonNode result = null;
		// build the declared function to assign to this variable in the
		// functionMap
		MappingExpressionParser.Function_declContext fctDeclCtx = (MappingExpressionParser.Function_declContext) expr;
		MappingExpressionParser.VarListContext varList = fctDeclCtx.varList();
		MappingExpressionParser.ExprListContext exprList = fctDeclCtx.exprList();
		DeclaredFunction fct = new DeclaredFunction(varList, exprList);
		setDeclaredFunction(fctName, fct);
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
			setVariable(varID, value);
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
			context = _environment.peekContext();
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
			result = lookup(context, id);
		}

		if (LOG.isLoggable(Level.FINEST))
			LOG.exiting(CLASS, METHOD, result);
		return result;
	}

	// private boolean flattenOutput = true;

	@Override
	public JsonNode visitLogand(MappingExpressionParser.LogandContext ctx) {
		BooleanNode result = null;

		JsonNode left = visit(ctx.expr(0)); // get value of left subexpression
		JsonNode right = visit(ctx.expr(1)); // get value of right subexpression

//		if (left == null || right == null)
//			return null;

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

//		if (left == null || right == null)
//			return null;

		result = BooleanUtils.convertJsonNodeToBoolean(left) || BooleanUtils.convertJsonNodeToBoolean(right)
				? BooleanNode.TRUE
				: BooleanNode.FALSE;

		return result;
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

		// unwrap left if singleton
		left = unwrapArray(left);

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

		final Double result;

		if (ctx.op.getType() == MappingExpressionParser.MUL) {
			result = left * right;
		} else if (ctx.op.getType() == MappingExpressionParser.DIV) {
			if (right == 0.0d) {
				return new DoubleNode(Double.POSITIVE_INFINITY); // null;
			}
			result = left / right;
		} else if (ctx.op.getType() == MappingExpressionParser.REM) {
			if (right == 0.0d) {
				return new DoubleNode(Double.POSITIVE_INFINITY); // null;
			}
			result = left % right;
		} else {
			// should never happen (this expression should not have parsed in the
			// first place)
			throw new EvaluateRuntimeException("Unrecognised token " + ctx.op.getText());
		}

		// check for Infinity and Nan
		if (result.isInfinite() || result.isNaN()) {
			throw new EvaluateRuntimeException("Number out of range: \"null\"");
		}
		// coerce the result to a long iff the result is exactly .0
		if (isWholeNumber(result)) {
			return new LongNode(result.longValue());
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
		if (ctx.fieldList() == null) { // (ctx.expr() == null) {
			// empty object: {}
			return object;
		} else {
			// FieldListContext fieldList = (FieldListContext)ctx.expr();
			// List<String> keys = fieldList.STRING().stream().map(k -> k.getText()).map(k
			// -> sanitise(k))
//			List<String> keys = ctx.fieldList().STRING().stream().map(k -> k.getText()).map(k -> sanitise(k))
//					.collect(Collectors.toList());
			List<TerminalNode> keyNodes = ctx.fieldList().STRING();
			List<ExprContext> valueNodes = ctx.fieldList().expr();
			if (keyNodes.size() != valueNodes.size()) {
				// this shouldn't have parsed in the first place
				throw new EvaluateRuntimeException("Object key/value count mismatch!");
			}
			ExprContext valueNode = null;
			JsonNode value = null;
			String fctName = null;
			String key = "";
			for (int i = 0; i < keyNodes.size(); i++) {
				key = keyNodes.get(i).getText();
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
							Function fct = getJsonataFunction(varName);
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
			// List<JsonNode> values = fieldList.expr().stream().map(e ->
			// visit(e)).collect(Collectors.toList());
//			List<JsonNode> values = ctx.fieldList().expr().stream().map(e -> visit(e)).collect(Collectors.toList());
//			if (keys.size() != values.size()) {
//				// this shouldn't have parsed in the first place
//				throw new EvaluateRuntimeException("Object key/value count mismatch!");
//			}
//
//			for (int i = 0; i < keys.size(); i++) {
//				String key = keys.get(i);
//				JsonNode value = values.get(i);
//				if (value != null) {
//					object.set(key, value);
//				}
//			}

			return object;
		}

	}

	@Override
	public JsonNode visitParens(MappingExpressionParser.ParensContext ctx) {
		JsonNode result = null;
		// generate a new frameEnvironment for life of this block
		FrameEnvironment oldEnvironment = _environment;
		_environment = new FrameEnvironment(_environment);
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

		_environment = oldEnvironment;
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
		if (lhs == null || lhs.isNull()) {
			return null;
			// throw new EvaluateRuntimeException(String.format(Constants.ERR_MSG_INVALID_PATH_ENTRY,"null"));
		}
		// reject path entries that are numbers or values
		switch (lhs.getNodeType()) {
		case NUMBER: {
			// leaving number as is since * can reference array values too
			// lhs = factory.textNode(lhs.asText());
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
		JsonNode result;
		if (rhsCtx == null) {
			result = lhs;
		} else {
			JsonNode rhs = null;
			// treat a StringContext as an ID Context
			if (rhsCtx instanceof MappingExpressionParser.StringContext) {
				CommonToken token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.ID, visit(rhsCtx).asText());
				TerminalNode node = new TerminalNodeImpl(token);
				IdContext idCtx = new MappingExpressionParser.IdContext(rhsCtx);
				idCtx.addChild(node);
				rhs = resolvePath(lhs, idCtx);
			} else if (rhsCtx instanceof NumberContext) {
				throw new EvaluateRuntimeException(
						"The literal value " + visit(rhsCtx) + " cannot be used as a step within a path expression");
//			} else if (rhsCtx instanceof FieldListContext) {
//				_environment.pushContext(lhs);
//				rhs = visit(rhsCtx);
//				_environment.popContext();
			} else {
				rhs = resolvePath(lhs, rhsCtx);
			}
			if (rhs == null) { // okay to return NullNode here so don't test "|| rhs.isNull()"
				result = null;
			} else if (rhs instanceof SelectorArrayNode) {
				if (rhs.size() == 0) {
					// if no results are present (i.e. results is empty) we need to return
					// null (i.e. *no match*)
					return null;
				} else {
					if ((firstStepCons && firstStep) || (lastStep && lastStepCons)) {
						List<JsonNode> cells = ((SelectorArrayNode) rhs).getSelectionGroups();
						result = new ArrayNode(JsonNodeFactory.instance);
						for (JsonNode cell : cells) {
							((ArrayNode) result).add(cell);
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
		int stackSize = _environment.sizeContext();
		Stack<JsonNode> tmpStack = new Stack<JsonNode>();
		for (; stackSize > 1; stackSize--) {
			tmpStack.push(_environment.popContext());
		}
		JsonNode result = null;
		if (lhsCtx == null) {
			result = _environment.peekContext();
		} else {
			result = visit(lhsCtx);
		}
		while (!tmpStack.isEmpty()) {
			_environment.pushContext(tmpStack.pop());
		}

		if (LOG.isLoggable(Level.FINEST))
			LOG.exiting(CLASS, METHOD, result);
		return result;
	}

	@Override
	public JsonNode visitSeq(SeqContext ctx) {
		JsonNode start = visit(ctx.expr(0));
		JsonNode end = visit(ctx.expr(1));

//      if (start == null) {
//         throw new EvaluateRuntimeException(ERR_SEQ_LHS_INTEGER);
//      }

		if (start != null && !isWholeNumber(start)) {
			throw new EvaluateRuntimeException(ERR_SEQ_LHS_INTEGER);
		}

//      if (end == null) {
//      	throw new EvaluateRuntimeException(ERR_SEQ_RHS_INTEGER);
//      }

		if (end != null && !isWholeNumber(end)) {
			throw new EvaluateRuntimeException(ERR_SEQ_RHS_INTEGER);
		}

		ArrayNode result = factory.arrayNode();
		if (start != null && end != null) {
			int iStart = start.asInt();
			int iEnd = end.asInt();
			int count = iEnd - iStart + 1;
			if (iEnd > iStart && count > 10000000) {
				// note: below should read 1e7 not 1e6...
				throw new EvaluateRuntimeException(ERR_TOO_BIG + count + ".");
			}
			for (int i = start.asInt(); i <= end.asInt(); i++) {
				result.add(new LongNode(i)); // use longs to align with the output of
				// visitNumber
			}
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
		if (expr instanceof MappingExpressionParser.PathContext || expr instanceof Context_refContext) {
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

	public JsonNode visitTree(ParseTree tree) {
		int treeSize = tree.getChildCount();
		steps.clear();
		for (int i = 0; i < treeSize; i++) {
			steps.add(tree.getChild(i));
		}
		if (tree.getChild(0) instanceof Array_constructorContext) {
			firstStepCons = true;
		}
		// test for laststep array construction child[n-1] instanceof
		// Array_constructorContext
		if (tree.getChild(treeSize - 1) instanceof Array_constructorContext) {
			lastStepCons = true;
		}
		if (tree.equals(steps.get(0))) {
			firstStep = true;
		} else {
			firstStep = false;
		}
		JsonNode result = visit(tree);
		// simulates lastStep processing
		if (result != null && result.isArray() && result.size() == 1 && ((ArrayNode) result).get(0).isArray()) {
			result = ((ArrayNode) result).get(0);
		}
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
		JsonNode result = null;
		JsonNode operand = visit(ctx.expr());

		if (ctx.op.getType() == MappingExpressionParser.SUB) {

			if (operand == null) {
				return null; // NOTE: Javascript JSONata engine actually throws an
									// exception here (but it shouldn't.. this is a bug in
									// that engine that will be fixed)
			} else if (operand.isFloatingPointNumber()) {
				result = new DoubleNode(-operand.asDouble());
			} else if (operand.isDouble()) {
				result = new DoubleNode(-operand.asDouble());
			} else if (operand.isIntegralNumber()) {
				if (operand.asLong() == Long.MAX_VALUE) {
					result = new LongNode(-operand.asLong()-1L);
				} else {
					result = new LongNode(-operand.asLong());
				}
			} else if (operand.isLong()) {
				if (operand.asLong() == Long.MAX_VALUE) {
					result = new LongNode(-operand.asLong()-1L);
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
			setDeclaredFunction(varName, fct);
		} else if (expr instanceof MappingExpressionParser.Function_callContext) {
			Function_callContext fctCallCtx = (Function_callContext) expr;

			String functionName = fctCallCtx.VAR_ID().getText();

			DeclaredFunction declFct = getDeclaredFunction(functionName);
			if (declFct == null) {
				Function function = getJsonataFunction(functionName);
				if (function != null) {
					setJsonataFunction(varName, function);
					// result = function.invoke(this, ctx);
				} else {
					throw new EvaluateRuntimeException("Unknown function: " + functionName);
				}
			} else {
				setDeclaredFunction(varName, declFct);
//				result = declFct.invoke(this, ctx);
			}

			result = visit(expr);
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
					Function fct = getJsonataFunction(fctName);
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
			}
		} else {
			result = visit(expr);
			setVariable(varName, result);
		}
		return result;
	}

	@Override
	public JsonNode visitVar_recall(MappingExpressionParser.Var_recallContext ctx) {
		final String varName = ctx.getText();
		JsonNode result = getVariable(varName);
		return result;
	}

}
