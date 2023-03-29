/**
 * 
 */
package com.api.jsonata4java;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.api.jsonata4java.expressions.EvaluateException;
import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.api.jsonata4java.expressions.Expressions;
import com.api.jsonata4java.expressions.ExpressionsVisitor;
import com.api.jsonata4java.expressions.FrameEnvironment;
import com.api.jsonata4java.expressions.ParseException;
import com.api.jsonata4java.expressions.functions.DeclaredFunction;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

/**
 * Class to provide embedding and extending JSONata features
 */
public class Expression implements Serializable {

	private static final long serialVersionUID = -292660522621832862L;

	/**
	 * Establish a list of bindings for the given json object
	 * 
	 * @param bindingObj
	 *                   the json to calculate the bindings for
	 * @return the list of bindings for the provided json
	 * @throws ParseException
	 */
	public static List<Binding> createBindings(JsonNode bindingObj) throws ParseException {
		ObjectMapper objectMapper = new ObjectMapper();
		List<Binding> bindings = new ArrayList<Binding>();
		for (Iterator<String> it = bindingObj.fieldNames(); it.hasNext();) {
			String key = it.next();
			JsonNode testObj = bindingObj.get(key);
			String expression = "";
			try {
				if (testObj.isTextual() == false) {
					expression = testObj.toString();
					Binding binding = new Binding(key, expression);
					bindings.add(binding);
				} else {
					// if the string starts with function treat as an expression
					expression = objectMapper.writeValueAsString(testObj).trim();
					while (expression.startsWith("\"") && expression.length() > 1) {
						expression = expression.substring(1);
					}
					while (expression.endsWith("\"") && expression.length() > 1) {
						expression = expression.substring(0, expression.length() - 1);
					}
					Binding binding = null;
					if (expression.startsWith("function")) {
						binding = new Binding(key, expression);
					} else {
						binding = new Binding(key, testObj);
					}
					bindings.add(binding);

				}
			} catch (Exception e) {
				Binding binding = new Binding(key, testObj);
				bindings.add(binding);
			}
		}
		return bindings;
	}

	/**
	 * Generate a new Expression based on evaluating the supplied expression
	 * 
	 * @param expression
	 *                   the logic to be parsed for later execution via the evaluate
	 *                   methods
	 * @return new Expression object
	 * @throws ParseException
	 * @throws IOException
	 */
	public static Expression jsonata(String expression) throws ParseException, IOException {
		return new Expression(expression);
	}

	/**
	 * Testing the various methods based on
	 * https://docs.jsonata.org/embedding-extending#expressionregisterfunctionname-implementation-signature
	 * 
	 * @param args
	 *             not used
	 */
	public static void main(String[] args) {
		try {
			String exprString = "$sum(example.value)";
			String inputString = "{\"example\": [{\"value\": 4}, {\"value\": 7}, {\"value\": 13}]}";
			System.out.println("Expression is " + exprString);
			System.out.println("Input is " + inputString);
			Expression expression = Expression.jsonata(exprString);
			JsonNode obj = new ObjectMapper().readTree(inputString);
			JsonNode result = expression.evaluate(obj);
			System.out.println("Result is " + result);

			expression = Expression.jsonata("$a +$b()");
			expression.assign("a", "4");
			expression.assign("$b", "function(){1}");
			result = expression.evaluate(obj);
			System.out.println("Input is \"$a + $b()\" with assignments \"a\":4, \"$b\":\"function(){1}\"");
			System.out.println("Result is " + result);

			JsonNode bindingObj = new ObjectMapper().readTree("{\"a\":4, \"b\":\"function(){78}\"}");
			System.out.println("Input is \"$a + $b()\" with binding object: " + bindingObj.toString());
			System.out.println("Result is " + Expression.jsonata("$a + $b()").evaluate(obj, bindingObj));

			bindingObj = new ObjectMapper().readTree("{\"a\":4, \"b\":\"function($c){$c+78}\",\"c\":7}");
			System.out.println("Input is \"$a + $b($c)\" with binding object: " + bindingObj.toString());
			System.out.println("Result is " + Expression.jsonata("$a + $b($c)").evaluate(obj, bindingObj));
			try {
				expression = Expression.jsonata("$notafunction()");
				result = expression.evaluate(JsonNodeFactory.instance.objectNode());
				throw new Exception("Expression " + expression + " should have generated an exception");
			} catch (EvaluateRuntimeException ere) {
				System.out
						.println("Result is we got the expected EvaluateRuntimeException for " + ere.getLocalizedMessage());
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	Map<String, DeclaredFunction> _declaredFunctionMap = new HashMap<String, DeclaredFunction>();
	ExpressionsVisitor _eval = null;
	Expressions _expr = null;
	Map<String, ExprContext> _variableMap = new HashMap<String, ExprContext>();

	/**
	 * Constructor for Expression
	 * 
	 * @param expression
	 *                   the logic to be parsed for later execution via evaluate
	 *                   methods
	 * @throws ParseException
	 * @throws IOException
	 */
	public Expression(String expression) throws ParseException, IOException {
		_expr = Expressions.parse(expression);
		_eval = _expr.getExpr();
	}

	/**
	 * Assign the binding to the environment preparing for evaluation
	 * 
	 * @param binding
	 */
	public void assign(Binding binding) {
		if (binding.getType() == BindingType.VARIABLE) {
			_variableMap.put(binding.getVarName(), binding.getExpression());
		} else {
			_declaredFunctionMap.put(binding.getVarName(), binding.getFunction());
		}
	}

	/**
	 * Assign the expression (variable or function declaration) to the variable name
	 * supplied
	 * 
	 * @param varname
	 *                   name of the variable to map to a variable expression or
	 *                   function declaration expression
	 * @param expression
	 *                   logic to be assigned to the variable name
	 * @throws ParseException
	 * @throws IOException
	 */
	public void assign(String varname, String expression) throws ParseException, IOException {
		Binding binding = new Binding(varname, expression);
		assign(binding);
	}

	/**
	 * Remove all bindings
	 */
	public void clear() {
		_variableMap.clear();
		_declaredFunctionMap.clear();
	}

	/**
	 * Generate a result form the Expression's parsed expression and variable
	 * assignments or registered functions
	 * 
	 * @param rootContext
	 *                    JSON object specifying the content used to evaluate the
	 *                    expression
	 * @return the result from executing the Expression's parsed expression and
	 *         variable assignments or registered functions. A null will be returned
	 *         if no match is found (note a JSON null will result in a JsonNode of
	 *         type NullNode).
	 * @throws ParseException
	 */
	public JsonNode evaluate(JsonNode rootContext) throws ParseException {
		ExpressionsVisitor eval = new ExpressionsVisitor(rootContext, new FrameEnvironment(null));
		// process any stored bindings
		for (Iterator<String> it = _variableMap.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			ExprContext ctx = _variableMap.get(key);
			eval.setVariable(key, eval.visit(ctx));
		}
		for (Iterator<String> it = _declaredFunctionMap.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			DeclaredFunction fct = _declaredFunctionMap.get(key);
			eval.setDeclaredFunction(key, fct);
		}
		return eval.visit(_expr.getTree());
	}

	/**
	 * Generate a result form the Expression's parsed expression and variable
	 * assignments or registered functions specified in the bindings object
	 * 
	 * @param rootContext
	 *                    JSON object specifying the content used to evaluate the
	 *                    expression
	 * @param bindingObj
	 *                    a JSON object containing the assignments of variable names
	 *                    to variable expressions or function declarations
	 * @return the result from executing the Expression's parsed expression and
	 *         variable assignments or registered functions specified in the
	 *         bindings object
	 * @throws ParseException
	 * @throws IOException
	 */
	public JsonNode evaluate(JsonNode rootContext, JsonNode bindingObj) throws ParseException, IOException {
		return evaluate(rootContext, createBindings(bindingObj));
	}

	/**
	 * Generate a result form the Expression's parsed expression and variable
	 * assignments or registered functions specified in the bindings object
	 * 
	 * @param rootContext
	 *                    JSON object specifying the content used to evaluate the
	 *                    expression
	 * @param bindingObj
	 *                    a JSON object containing the assignments of variable names
	 *                    to variable expressions or function declarations
	 * @param timeoutMS
	 *                    milliseconds allowed for the evaluation to occur. If it
	 *                    takes longer an exception is thrown. Must be positive
	 *                    number or exception is thrown.
	 * @param maxDepth
	 *                    the maximum call stack depth allowed before an exception
	 *                    is thrown. Must be a positive number or an exception is
	 *                    thrown.
	 * @return the result from executing the Expression's parsed expression and
	 *         variable assignments or registered functions specified in the
	 *         bindings object
	 * @throws ParseException
	 * @throws IOException
	 * @throws EvaluateException
	 */
	public JsonNode evaluate(JsonNode rootContext, JsonNode bindingObj, long timeoutMS, int maxDepth)
			throws ParseException, IOException, EvaluateException {
		return evaluate(rootContext, createBindings(bindingObj), timeoutMS, maxDepth);
	}

	/**
	 * Generate a result form the Expression's parsed expression and variable
	 * assignments or registered functions specified in the list of bindings
	 * 
	 * @param rootContext
	 *                    JSON object specifying the content used to evaluate the
	 *                    expression
	 * @param bindings
	 *                    assignments of variable names to variable expressions or
	 *                    function declarations
	 * @return the result from executing the Expression's parsed expression and
	 *         variable assignments or registered functions specified in the list of
	 *         bindings. A null will be returned if no match is found (note a JSON
	 *         null will result in a JsonNode of type NullNode).
	 * @throws ParseException
	 */
	public JsonNode evaluate(JsonNode rootContext, List<Binding> bindings) throws ParseException {
		JsonNode result = null;
		// first do variables
		for (Binding binding : bindings) {
			assign(binding);
		}
		result = evaluate(rootContext);
		return result;
	}

	/**
	 * Generate a result form the Expression's parsed expression and variable
	 * assignments or registered functions specified in the list of bindings
	 * 
	 * @param rootContext
	 *                    JSON object specifying the content used to evaluate the
	 *                    expression
	 * @param bindings
	 *                    assignments of variable names to variable expressions or
	 *                    function declarations
	 * @param timeoutMS
	 *                    milliseconds allowed for the evaluation to occur. If it
	 *                    takes longer an exception is thrown. Must be positive
	 *                    number or exception is thrown.
	 * @param maxDepth
	 *                    the maximum call stack depth allowed before an exception
	 *                    is thrown. Must be a positive number or an exception is
	 *                    thrown.
	 * @return the result from executing the Expression's parsed expression and
	 *         variable assignments or registered functions specified in the list of
	 *         bindings. A null will be returned if no match is found (note a JSON
	 *         null will result in a JsonNode of type NullNode).
	 * @throws ParseException
	 * @throws EvaluateException
	 */
	public JsonNode evaluate(JsonNode rootContext, List<Binding> bindings, long timeoutMS, int maxDepth)
			throws ParseException, EvaluateException {
		JsonNode result = null;
		// first do variables
		for (Binding binding : bindings) {
			assign(binding);
		}
		result = evaluate(rootContext, timeoutMS, maxDepth);
		return result;
	}

	/**
	 * Generate a result form the Expression's parsed expression and variable
	 * assignments or registered functions
	 * 
	 * @param rootContext
	 *                    JSON object specifying the content used to evaluate the
	 *                    expression
	 * @param timeoutMS
	 *                    milliseconds allowed for the evaluation to occur. If it
	 *                    takes longer an exception is thrown. Must be positive
	 *                    number or exception is thrown.
	 * @param maxDepth
	 *                    the maximum call stack depth allowed before an exception
	 *                    is thrown. Must be a positive number or an exception is
	 *                    thrown.
	 * @return the JsonNode resulting from the expression evaluation against the
	 *         rootContext. A null will be returned if no match is found (note a
	 *         JSON null will result in a JsonNode of type NullNode).
	 * @throws EvaluateException
	 *                           If the given device event is invalid.
	 */
	public JsonNode evaluate(JsonNode rootContext, long timeoutMS, int maxDepth) throws EvaluateException {
		ExpressionsVisitor eval = new ExpressionsVisitor(rootContext, new FrameEnvironment(null));
		// process any stored bindings
		for (Iterator<String> it = _variableMap.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			ExprContext ctx = _variableMap.get(key);
			eval.setVariable(key, eval.visit(ctx));
		}
		for (Iterator<String> it = _declaredFunctionMap.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			DeclaredFunction fct = _declaredFunctionMap.get(key);
			eval.setDeclaredFunction(key, fct);
		}
		eval.timeboxExpression(timeoutMS, maxDepth);
		return eval.visit(_expr.getTree());
	}

	/**
	 * Generate a result form the Expression's parsed expression and variable
	 * assignments or registered functions. This method is synchronized to enable
	 * multi-threaded execution.
	 * 
	 * @param rootContext
	 *                    JSON object specifying the content used to evaluate the
	 *                    expression
	 * @return the result from executing the Expression's parsed expression and
	 *         variable assignments or registered functions. A null will be returned
	 *         if no match is found (note a JSON null will result in a JsonNode of
	 *         type NullNode).
	 * @throws ParseException
	 */
	public synchronized JsonNode evaluateSynced(JsonNode rootContext) throws ParseException {
		return evaluate(rootContext);
	}

	/**
	 * Generate a result form the Expression's parsed expression and variable
	 * assignments or registered functions specified in the bindings object
	 * 
	 * @param rootContext
	 *                    JSON object specifying the content used to evaluate the
	 *                    expression
	 * @param bindingObj
	 *                    a JSON object containing the assignments of variable names
	 *                    to variable expressions or function declarations
	 * @return the result from executing the Expression's parsed expression and
	 *         variable assignments or registered functions specified in the
	 *         bindings object
	 * @throws ParseException
	 * @throws IOException
	 */
	public synchronized JsonNode evaluateSynced(JsonNode rootContext, JsonNode bindingObj)
			throws ParseException, IOException {
		return evaluate(rootContext, bindingObj);
	}

	/**
	 * Generate a result form the Expression's parsed expression and variable
	 * assignments or registered functions specified in the bindings object. This
	 * method is synchronized to enable multi-threaded execution.
	 * 
	 * @param rootContext
	 *                    JSON object specifying the content used to evaluate the
	 *                    expression
	 * @param bindingObj
	 *                    a JSON object containing the assignments of variable names
	 *                    to variable expressions or function declarations
	 * @param timeoutMS
	 *                    milliseconds allowed for the evaluation to occur. If it
	 *                    takes longer an exception is thrown. Must be positive
	 *                    number or exception is thrown.
	 * @param maxDepth
	 *                    the maximum call stack depth allowed before an exception
	 *                    is thrown. Must be a positive number or an exception is
	 *                    thrown.
	 * @return the result from executing the Expression's parsed expression and
	 *         variable assignments or registered functions specified in the
	 *         bindings object
	 * @throws ParseException
	 * @throws IOException
	 * @throws EvaluateException
	 */
	public synchronized JsonNode evaluateSynced(JsonNode rootContext, JsonNode bindingObj, long timeoutMS, int maxDepth)
			throws ParseException, IOException, EvaluateException {
		return evaluate(rootContext, createBindings(bindingObj), timeoutMS, maxDepth);
	}

	/**
	 * Generate a result form the Expression's parsed expression and variable
	 * assignments or registered functions specified in the list of bindings. This
	 * method is synchronized to enable multi-threaded execution.
	 * 
	 * @param rootContext
	 *                    JSON object specifying the content used to evaluate the
	 *                    expression
	 * @param bindings
	 *                    assignments of variable names to variable expressions or
	 *                    function declarations
	 * @return the result from executing the Expression's parsed expression and
	 *         variable assignments or registered functions specified in the list of
	 *         bindings. A null will be returned if no match is found (note a JSON
	 *         null will result in a JsonNode of type NullNode).
	 * @throws ParseException
	 */
	public synchronized JsonNode evaluateSynced(JsonNode rootContext, List<Binding> bindings) throws ParseException {
		return evaluate(rootContext, bindings);
	}

	/**
	 * Generate a result form the Expression's parsed expression and variable
	 * assignments or registered functions specified in the list of bindings. This
	 * method is synchronized to enable multi-threaded execution.
	 * 
	 * @param rootContext
	 *                    JSON object specifying the content used to evaluate the
	 *                    expression
	 * @param bindings
	 *                    assignments of variable names to variable expressions or
	 *                    function declarations
	 * @param timeoutMS
	 *                    milliseconds allowed for the evaluation to occur. If it
	 *                    takes longer an exception is thrown. Must be positive
	 *                    number or exception is thrown.
	 * @param maxDepth
	 *                    the maximum call stack depth allowed before an exception
	 *                    is thrown. Must be a positive number or an exception is
	 *                    thrown.
	 * @return the result from executing the Expression's parsed expression and
	 *         variable assignments or registered functions specified in the list of
	 *         bindings. A null will be returned if no match is found (note a JSON
	 *         null will result in a JsonNode of type NullNode).
	 * @throws ParseException
	 * @throws EvaluateException
	 */
	public synchronized JsonNode evaluateSynced(JsonNode rootContext, List<Binding> bindings, long timeoutMS,
			int maxDepth) throws ParseException, EvaluateException {
		return evaluate(rootContext, bindings, timeoutMS, maxDepth);
	}

	/**
	 * Generate a result form the Expression's parsed expression and variable
	 * assignments or registered functions. This method is synchronized to enable
	 * multi-threaded execution.
	 * 
	 * @param rootContext
	 *                    JSON object specifying the content used to evaluate the
	 *                    expression
	 * @param timeoutMS
	 *                    milliseconds allowed for the evaluation to occur. If it
	 *                    takes longer an exception is thrown. Must be positive
	 *                    number or exception is thrown.
	 * @param maxDepth
	 *                    the maximum call stack depth allowed before an exception
	 *                    is thrown. Must be a positive number or an exception is
	 *                    thrown.
	 * @return the JsonNode resulting from the expression evaluation against the
	 *         rootContext. A null will be returned if no match is found (note a
	 *         JSON null will result in a JsonNode of type NullNode).
	 * @throws EvaluateException
	 *                           If the given device event is invalid.
	 */
	public synchronized JsonNode evaluateSynced(JsonNode rootContext, long timeoutMS, int maxDepth)
			throws EvaluateException {
		return evaluate(rootContext, timeoutMS, maxDepth);
	}

	/**
	 * Registers a function implementation (declaration) by name
	 * 
	 * @param fctName
	 *                       the name of the function
	 * @param implementation
	 *                       the function declaration
	 * @throws ParseException
	 * @throws IOException
	 */
	public void registerFunction(String fctName, String implementation) throws ParseException, IOException {
		Binding fctBinding = new Binding(fctName, implementation);
		_declaredFunctionMap.put(fctBinding.getVarName(), fctBinding.getFunction());
	}
}
