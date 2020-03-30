// Generated from com/api/jsonata4java/expressions/path/generated/PathExpressionParser.g4 by ANTLR 4.8
package com.api.jsonata4java.expressions.path.generated;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PathExpressionParser}.
 */
public interface PathExpressionParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code path}
	 * labeled alternative in {@link PathExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterPath(PathExpressionParser.PathContext ctx);
	/**
	 * Exit a parse tree produced by the {@code path}
	 * labeled alternative in {@link PathExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitPath(PathExpressionParser.PathContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathExpressionParser#id}.
	 * @param ctx the parse tree
	 */
	void enterId(PathExpressionParser.IdContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathExpressionParser#id}.
	 * @param ctx the parse tree
	 */
	void exitId(PathExpressionParser.IdContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathExpressionParser#array_index}.
	 * @param ctx the parse tree
	 */
	void enterArray_index(PathExpressionParser.Array_indexContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathExpressionParser#array_index}.
	 * @param ctx the parse tree
	 */
	void exitArray_index(PathExpressionParser.Array_indexContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathExpressionParser#function_path}.
	 * @param ctx the parse tree
	 */
	void enterFunction_path(PathExpressionParser.Function_pathContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathExpressionParser#function_path}.
	 * @param ctx the parse tree
	 */
	void exitFunction_path(PathExpressionParser.Function_pathContext ctx);
}