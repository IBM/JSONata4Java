// Generated from com/api/jsonata4java/expressions/path/generated/PathExpressionParser.g4 by ANTLR 4.9.2
package com.api.jsonata4java.expressions.path.generated;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PathExpressionParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PathExpressionParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code path}
	 * labeled alternative in {@link PathExpressionParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPath(PathExpressionParser.PathContext ctx);
	/**
	 * Visit a parse tree produced by {@link PathExpressionParser#id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitId(PathExpressionParser.IdContext ctx);
	/**
	 * Visit a parse tree produced by {@link PathExpressionParser#array_index}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray_index(PathExpressionParser.Array_indexContext ctx);
}