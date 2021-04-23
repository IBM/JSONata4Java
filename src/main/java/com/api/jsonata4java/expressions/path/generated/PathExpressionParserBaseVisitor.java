// Generated from com/api/jsonata4java/expressions/path/generated/PathExpressionParser.g4 by ANTLR 4.9.2
package com.api.jsonata4java.expressions.path.generated;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

/**
 * This class provides an empty implementation of {@link PathExpressionParserVisitor},
 * which can be extended to create a visitor which only needs to handle a subset
 * of the available methods.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public class PathExpressionParserBaseVisitor<T> extends AbstractParseTreeVisitor<T> implements PathExpressionParserVisitor<T> {
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitPath(PathExpressionParser.PathContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitId(PathExpressionParser.IdContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitArray_index(PathExpressionParser.Array_indexContext ctx) { return visitChildren(ctx); }
}