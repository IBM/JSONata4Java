// Generated from com/api/jsonata4java/expressions/generated/MappingExpression.g4 by ANTLR 4.9.2
package com.api.jsonata4java.expressions.generated;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link MappingExpressionParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface MappingExpressionVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code parens}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParens(MappingExpressionParser.ParensContext ctx);
	/**
	 * Visit a parse tree produced by the {@code muldiv_op}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMuldiv_op(MappingExpressionParser.Muldiv_opContext ctx);
	/**
	 * Visit a parse tree produced by the {@code logor}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogor(MappingExpressionParser.LogorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code string}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(MappingExpressionParser.StringContext ctx);
	/**
	 * Visit a parse tree produced by the {@code logand}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogand(MappingExpressionParser.LogandContext ctx);
	/**
	 * Visit a parse tree produced by the {@code conditional}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditional(MappingExpressionParser.ConditionalContext ctx);
	/**
	 * Visit a parse tree produced by the {@code function_call}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_call(MappingExpressionParser.Function_callContext ctx);
	/**
	 * Visit a parse tree produced by the {@code var_assign}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVar_assign(MappingExpressionParser.Var_assignContext ctx);
	/**
	 * Visit a parse tree produced by the {@code descendant}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescendant(MappingExpressionParser.DescendantContext ctx);
	/**
	 * Visit a parse tree produced by the {@code membership}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMembership(MappingExpressionParser.MembershipContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addsub_op}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddsub_op(MappingExpressionParser.Addsub_opContext ctx);
	/**
	 * Visit a parse tree produced by the {@code function_decl}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_decl(MappingExpressionParser.Function_declContext ctx);
	/**
	 * Visit a parse tree produced by the {@code number}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(MappingExpressionParser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by the {@code path}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPath(MappingExpressionParser.PathContext ctx);
	/**
	 * Visit a parse tree produced by the {@code to_array}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTo_array(MappingExpressionParser.To_arrayContext ctx);
	/**
	 * Visit a parse tree produced by the {@code array}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray(MappingExpressionParser.ArrayContext ctx);
	/**
	 * Visit a parse tree produced by the {@code id}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitId(MappingExpressionParser.IdContext ctx);
	/**
	 * Visit a parse tree produced by the {@code object_constructor}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_constructor(MappingExpressionParser.Object_constructorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code context_ref}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContext_ref(MappingExpressionParser.Context_refContext ctx);
	/**
	 * Visit a parse tree produced by the {@code array_constructor}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray_constructor(MappingExpressionParser.Array_constructorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unary_op}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnary_op(MappingExpressionParser.Unary_opContext ctx);
	/**
	 * Visit a parse tree produced by the {@code var_recall}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVar_recall(MappingExpressionParser.Var_recallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code concat_op}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConcat_op(MappingExpressionParser.Concat_opContext ctx);
	/**
	 * Visit a parse tree produced by the {@code root_path}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoot_path(MappingExpressionParser.Root_pathContext ctx);
	/**
	 * Visit a parse tree produced by the {@code fct_chain}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFct_chain(MappingExpressionParser.Fct_chainContext ctx);
	/**
	 * Visit a parse tree produced by the {@code boolean}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolean(MappingExpressionParser.BooleanContext ctx);
	/**
	 * Visit a parse tree produced by the {@code null}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNull(MappingExpressionParser.NullContext ctx);
	/**
	 * Visit a parse tree produced by the {@code comp_op}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComp_op(MappingExpressionParser.Comp_opContext ctx);
	/**
	 * Visit a parse tree produced by the {@code function_exec}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_exec(MappingExpressionParser.Function_execContext ctx);
	/**
	 * Visit a parse tree produced by the {@code field_values}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitField_values(MappingExpressionParser.Field_valuesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code object}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject(MappingExpressionParser.ObjectContext ctx);
	/**
	 * Visit a parse tree produced by {@link MappingExpressionParser#fieldList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldList(MappingExpressionParser.FieldListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MappingExpressionParser#exprList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprList(MappingExpressionParser.ExprListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MappingExpressionParser#varList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarList(MappingExpressionParser.VarListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MappingExpressionParser#exprValues}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprValues(MappingExpressionParser.ExprValuesContext ctx);
	/**
	 * Visit a parse tree produced by {@link MappingExpressionParser#emptyValues}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyValues(MappingExpressionParser.EmptyValuesContext ctx);
	/**
	 * Visit a parse tree produced by {@link MappingExpressionParser#seq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSeq(MappingExpressionParser.SeqContext ctx);
	/**
	 * Visit a parse tree produced by {@link MappingExpressionParser#exprOrSeq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprOrSeq(MappingExpressionParser.ExprOrSeqContext ctx);
	/**
	 * Visit a parse tree produced by {@link MappingExpressionParser#exprOrSeqList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprOrSeqList(MappingExpressionParser.ExprOrSeqListContext ctx);
}