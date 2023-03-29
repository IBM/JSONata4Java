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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.ErrorNodeImpl;
import org.antlr.v4.runtime.tree.ParseTree;
import com.api.jsonata4java.expressions.generated.MappingExpressionLexer;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Expr_to_eofContext;
import com.api.jsonata4java.expressions.utils.Constants;
import com.fasterxml.jackson.databind.JsonNode;

public class Expressions implements Serializable {

	private static final long serialVersionUID = -2995139816481454905L;

	/**
	 * Returns a list of $something references in the given expression, using the
	 * given Pattern object (typically patterns should match on $state or $event)
	 *
	 * @param refPattern
	 *                   reference pattern
	 * @param expression
	 *                   expression to be searched for references
	 * @return list of references
	 */
	public static List<String> getRefsInExpression(Pattern refPattern, String expression) {
		// eg if expression = "$state.x.y + $event.a + ($state.c/2)
		// then return values should be ["x.y", "c"]
		Matcher matcher = refPattern.matcher(expression);

		LinkedList<String> matches = new LinkedList<>();

		while (matcher.find()) {
			matches.add(matcher.group(1));
		}

		return matches;
	}

	// Convert a mapping expression string into a pre-processed expression ready
	// for evaluation
	public static Expressions parse(String mappingExpression) throws ParseException, IOException {

		// Expressions can include references to properties within an
		// application interface ("state"),
		// properties within an event, and various operators and functions.
		InputStream targetStream = new ByteArrayInputStream(mappingExpression.getBytes(StandardCharsets.UTF_8));
		CharStream input = CharStreams.fromStream(targetStream, StandardCharsets.UTF_8);

		MappingExpressionLexer lexer = new MappingExpressionLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		MappingExpressionParser parser = new MappingExpressionParser(tokens);
		ParseTree tree = null;
		ExprContext newTree = null;
		BufferingErrorListener errorListener = new BufferingErrorListener();

		try {
			// remove the default error listeners which print to stderr
			parser.removeErrorListeners();
			lexer.removeErrorListeners();

			// replace with error listener that buffer errors and allow us to retrieve them
			// later
			parser.addErrorListener(errorListener);
			lexer.addErrorListener(errorListener);

			tree = parser.expr_to_eof(); // _to_eof();
			if (errorListener.heardErrors()) {
				if (tree != null && tree.getChildCount() > 0) {
					ParseTree error = tree.getChild(0);
					if (error instanceof ErrorNodeImpl) {
						if (((ErrorNodeImpl) error).getSymbol().getType() == MappingExpressionLexer.CHAIN) {
							throw new EvaluateRuntimeException(Constants.ERR_MSG_FCT_CHAIN_NOT_UNARY);

						}
					}
				}
				throw new ParseException(errorListener.getErrorsAsString());
			}
			newTree = ((Expr_to_eofContext) tree).expr();
			// edit the tree's children to remove the last TerminalNodeImpl containing the
			// Token.EOF
		} catch (RecognitionException e) {
			throw new ParseException(e.getMessage());
		}

		return new Expressions(newTree, mappingExpression);
	}

	ExpressionsVisitor _eval = null;

	String _expression = null;

	ParseTree _tree = null;

	public Expressions(ParseTree aTree, String anExpression) {
		_eval = new ExpressionsVisitor(null, new FrameEnvironment(null));
		_tree = aTree;
		_expression = anExpression;
	}

	/**
	 * Evaluate the stored expression against the supplied event and application
	 * interface data.
	 * 
	 * @param rootContext
	 *                    bound to root context ($$ and paths that don't start with
	 *                    $event, $state or $instance) when evaluating expressions.
	 *                    May be null.
	 * @return the JsonNode resulting from the expression evaluation against the
	 *         rootContext. A null will be returned if no match is found (note a
	 *         JSON null will result in a JsonNode of type NullNode).
	 * @throws EvaluateException
	 *                           If the given device event is invalid.
	 */
	public JsonNode evaluate(JsonNode rootContext) throws EvaluateException {

		JsonNode result = null;

		_eval.setRootContext(rootContext);

		try {
			result = _eval.visitTree(_tree); // was eval.visit();
		} catch (EvaluateRuntimeException e) {
			throw new EvaluateException(e.getMessage(), e);
		}

		// prevent a NPE when expression evaluates to null (which is a legitimate return
		// value for an expression)
		if (result == null) {
			return null;
		}
		// else return JsonNode as null
		return result;
	}

	/**
	 * Evaluate the stored expression against the supplied event and application
	 * interface data.
	 * 
	 * @param rootContext
	 *                    bound to root context ($$ and paths that don't start with
	 *                    $event, $state or $instance) when evaluating expressions.
	 *                    May be null.
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

		JsonNode result = null;

		_eval.setRootContext(rootContext);
		if (timeoutMS <= 0L) {
			throw new EvaluateException("The timeoutMS must be a positive number. Received " + timeoutMS);
		}
		if (maxDepth <= 0) {
			throw new EvaluateException("The maxDepth must be a positive number. Received " + maxDepth);
		}
		_eval.timeboxExpression(timeoutMS, maxDepth);

		try {
			result = _eval.visitTree(_tree); // was eval.visit();
		} catch (EvaluateRuntimeException e) {
			throw new EvaluateException(e.getMessage(), e);
		}

		// prevent a NPE when expression evaluates to null (which is a legitimate return
		// value for an expression)
		if (result == null) {
			return null;
		}

		return result;

	}

	/**
	 * Evaluate the stored expression with synchronization to enable multi-threaded
	 * execution against the supplied event and application interface data.
	 * 
	 * @param rootContext
	 *                    bound to root context ($$ and paths that don't start with
	 *                    $event, $state or $instance) when evaluating expressions.
	 *                    May be null.
	 * @return the JsonNode resulting from the expression evaluation against the
	 *         rootContext. A null will be returned if no match is found (note a
	 *         JSON null will result in a JsonNode of type NullNode).
	 * @throws EvaluateException
	 *                           If the given device event is invalid.
	 */
	public synchronized JsonNode evaluateSynced(JsonNode rootContext) throws EvaluateException {
		return evaluate(rootContext);
	}

	/**
	 * Evaluate the stored expression with synchronization to enable multi-threaded
	 * execution against the supplied event and application interface data.
	 * 
	 * @param rootContext
	 *                    bound to root context ($$ and paths that don't start with
	 *                    $event, $state or $instance) when evaluating expressions.
	 *                    May be null.
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

	public FrameEnvironment getEnvironment() {
		return _eval.getEnvironment();
	}

	public ExpressionsVisitor getExpr() {
		return _eval;
	}

	public ParseTree getTree() {
		return _tree;
	}

	public void setExpr(ExpressionsVisitor expr) {
		_eval = expr;
	}

	public void setTree(ParseTree parsetree) {
		_tree = parsetree;
	}

	public void timeboxExpression(long timeoutMS, int maxDepth) {
		_eval.timeboxExpression(timeoutMS, maxDepth);
	}

	public String toString() {
		return _expression;
	}

}
