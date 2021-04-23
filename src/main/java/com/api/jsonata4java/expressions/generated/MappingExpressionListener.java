// Generated from com/api/jsonata4java/expressions/generated/MappingExpression.g4 by ANTLR 4.9.2
package com.api.jsonata4java.expressions.generated;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MappingExpressionParser}.
 */
public interface MappingExpressionListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code parens}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterParens(MappingExpressionParser.ParensContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parens}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitParens(MappingExpressionParser.ParensContext ctx);
	/**
	 * Enter a parse tree produced by the {@code muldiv_op}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterMuldiv_op(MappingExpressionParser.Muldiv_opContext ctx);
	/**
	 * Exit a parse tree produced by the {@code muldiv_op}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitMuldiv_op(MappingExpressionParser.Muldiv_opContext ctx);
	/**
	 * Enter a parse tree produced by the {@code logor}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLogor(MappingExpressionParser.LogorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code logor}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLogor(MappingExpressionParser.LogorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code string}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterString(MappingExpressionParser.StringContext ctx);
	/**
	 * Exit a parse tree produced by the {@code string}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitString(MappingExpressionParser.StringContext ctx);
	/**
	 * Enter a parse tree produced by the {@code logand}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLogand(MappingExpressionParser.LogandContext ctx);
	/**
	 * Exit a parse tree produced by the {@code logand}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLogand(MappingExpressionParser.LogandContext ctx);
	/**
	 * Enter a parse tree produced by the {@code conditional}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterConditional(MappingExpressionParser.ConditionalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code conditional}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitConditional(MappingExpressionParser.ConditionalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code function_call}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterFunction_call(MappingExpressionParser.Function_callContext ctx);
	/**
	 * Exit a parse tree produced by the {@code function_call}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitFunction_call(MappingExpressionParser.Function_callContext ctx);
	/**
	 * Enter a parse tree produced by the {@code var_assign}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterVar_assign(MappingExpressionParser.Var_assignContext ctx);
	/**
	 * Exit a parse tree produced by the {@code var_assign}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitVar_assign(MappingExpressionParser.Var_assignContext ctx);
	/**
	 * Enter a parse tree produced by the {@code descendant}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterDescendant(MappingExpressionParser.DescendantContext ctx);
	/**
	 * Exit a parse tree produced by the {@code descendant}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitDescendant(MappingExpressionParser.DescendantContext ctx);
	/**
	 * Enter a parse tree produced by the {@code membership}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterMembership(MappingExpressionParser.MembershipContext ctx);
	/**
	 * Exit a parse tree produced by the {@code membership}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitMembership(MappingExpressionParser.MembershipContext ctx);
	/**
	 * Enter a parse tree produced by the {@code addsub_op}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAddsub_op(MappingExpressionParser.Addsub_opContext ctx);
	/**
	 * Exit a parse tree produced by the {@code addsub_op}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAddsub_op(MappingExpressionParser.Addsub_opContext ctx);
	/**
	 * Enter a parse tree produced by the {@code function_decl}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterFunction_decl(MappingExpressionParser.Function_declContext ctx);
	/**
	 * Exit a parse tree produced by the {@code function_decl}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitFunction_decl(MappingExpressionParser.Function_declContext ctx);
	/**
	 * Enter a parse tree produced by the {@code number}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterNumber(MappingExpressionParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by the {@code number}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitNumber(MappingExpressionParser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by the {@code path}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterPath(MappingExpressionParser.PathContext ctx);
	/**
	 * Exit a parse tree produced by the {@code path}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitPath(MappingExpressionParser.PathContext ctx);
	/**
	 * Enter a parse tree produced by the {@code to_array}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterTo_array(MappingExpressionParser.To_arrayContext ctx);
	/**
	 * Exit a parse tree produced by the {@code to_array}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitTo_array(MappingExpressionParser.To_arrayContext ctx);
	/**
	 * Enter a parse tree produced by the {@code array}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterArray(MappingExpressionParser.ArrayContext ctx);
	/**
	 * Exit a parse tree produced by the {@code array}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitArray(MappingExpressionParser.ArrayContext ctx);
	/**
	 * Enter a parse tree produced by the {@code id}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterId(MappingExpressionParser.IdContext ctx);
	/**
	 * Exit a parse tree produced by the {@code id}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitId(MappingExpressionParser.IdContext ctx);
	/**
	 * Enter a parse tree produced by the {@code object_constructor}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterObject_constructor(MappingExpressionParser.Object_constructorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code object_constructor}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitObject_constructor(MappingExpressionParser.Object_constructorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code context_ref}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterContext_ref(MappingExpressionParser.Context_refContext ctx);
	/**
	 * Exit a parse tree produced by the {@code context_ref}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitContext_ref(MappingExpressionParser.Context_refContext ctx);
	/**
	 * Enter a parse tree produced by the {@code array_constructor}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterArray_constructor(MappingExpressionParser.Array_constructorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code array_constructor}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitArray_constructor(MappingExpressionParser.Array_constructorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unary_op}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterUnary_op(MappingExpressionParser.Unary_opContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unary_op}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitUnary_op(MappingExpressionParser.Unary_opContext ctx);
	/**
	 * Enter a parse tree produced by the {@code var_recall}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterVar_recall(MappingExpressionParser.Var_recallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code var_recall}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitVar_recall(MappingExpressionParser.Var_recallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code concat_op}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterConcat_op(MappingExpressionParser.Concat_opContext ctx);
	/**
	 * Exit a parse tree produced by the {@code concat_op}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitConcat_op(MappingExpressionParser.Concat_opContext ctx);
	/**
	 * Enter a parse tree produced by the {@code root_path}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterRoot_path(MappingExpressionParser.Root_pathContext ctx);
	/**
	 * Exit a parse tree produced by the {@code root_path}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitRoot_path(MappingExpressionParser.Root_pathContext ctx);
	/**
	 * Enter a parse tree produced by the {@code fct_chain}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterFct_chain(MappingExpressionParser.Fct_chainContext ctx);
	/**
	 * Exit a parse tree produced by the {@code fct_chain}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitFct_chain(MappingExpressionParser.Fct_chainContext ctx);
	/**
	 * Enter a parse tree produced by the {@code boolean}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBoolean(MappingExpressionParser.BooleanContext ctx);
	/**
	 * Exit a parse tree produced by the {@code boolean}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBoolean(MappingExpressionParser.BooleanContext ctx);
	/**
	 * Enter a parse tree produced by the {@code null}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterNull(MappingExpressionParser.NullContext ctx);
	/**
	 * Exit a parse tree produced by the {@code null}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitNull(MappingExpressionParser.NullContext ctx);
	/**
	 * Enter a parse tree produced by the {@code comp_op}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterComp_op(MappingExpressionParser.Comp_opContext ctx);
	/**
	 * Exit a parse tree produced by the {@code comp_op}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitComp_op(MappingExpressionParser.Comp_opContext ctx);
	/**
	 * Enter a parse tree produced by the {@code function_exec}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterFunction_exec(MappingExpressionParser.Function_execContext ctx);
	/**
	 * Exit a parse tree produced by the {@code function_exec}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitFunction_exec(MappingExpressionParser.Function_execContext ctx);
	/**
	 * Enter a parse tree produced by the {@code field_values}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterField_values(MappingExpressionParser.Field_valuesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code field_values}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitField_values(MappingExpressionParser.Field_valuesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code object}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterObject(MappingExpressionParser.ObjectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code object}
	 * labeled alternative in {@link MappingExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitObject(MappingExpressionParser.ObjectContext ctx);
	/**
	 * Enter a parse tree produced by {@link MappingExpressionParser#fieldList}.
	 * @param ctx the parse tree
	 */
	void enterFieldList(MappingExpressionParser.FieldListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MappingExpressionParser#fieldList}.
	 * @param ctx the parse tree
	 */
	void exitFieldList(MappingExpressionParser.FieldListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MappingExpressionParser#exprList}.
	 * @param ctx the parse tree
	 */
	void enterExprList(MappingExpressionParser.ExprListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MappingExpressionParser#exprList}.
	 * @param ctx the parse tree
	 */
	void exitExprList(MappingExpressionParser.ExprListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MappingExpressionParser#varList}.
	 * @param ctx the parse tree
	 */
	void enterVarList(MappingExpressionParser.VarListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MappingExpressionParser#varList}.
	 * @param ctx the parse tree
	 */
	void exitVarList(MappingExpressionParser.VarListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MappingExpressionParser#exprValues}.
	 * @param ctx the parse tree
	 */
	void enterExprValues(MappingExpressionParser.ExprValuesContext ctx);
	/**
	 * Exit a parse tree produced by {@link MappingExpressionParser#exprValues}.
	 * @param ctx the parse tree
	 */
	void exitExprValues(MappingExpressionParser.ExprValuesContext ctx);
	/**
	 * Enter a parse tree produced by {@link MappingExpressionParser#emptyValues}.
	 * @param ctx the parse tree
	 */
	void enterEmptyValues(MappingExpressionParser.EmptyValuesContext ctx);
	/**
	 * Exit a parse tree produced by {@link MappingExpressionParser#emptyValues}.
	 * @param ctx the parse tree
	 */
	void exitEmptyValues(MappingExpressionParser.EmptyValuesContext ctx);
	/**
	 * Enter a parse tree produced by {@link MappingExpressionParser#seq}.
	 * @param ctx the parse tree
	 */
	void enterSeq(MappingExpressionParser.SeqContext ctx);
	/**
	 * Exit a parse tree produced by {@link MappingExpressionParser#seq}.
	 * @param ctx the parse tree
	 */
	void exitSeq(MappingExpressionParser.SeqContext ctx);
	/**
	 * Enter a parse tree produced by {@link MappingExpressionParser#exprOrSeq}.
	 * @param ctx the parse tree
	 */
	void enterExprOrSeq(MappingExpressionParser.ExprOrSeqContext ctx);
	/**
	 * Exit a parse tree produced by {@link MappingExpressionParser#exprOrSeq}.
	 * @param ctx the parse tree
	 */
	void exitExprOrSeq(MappingExpressionParser.ExprOrSeqContext ctx);
	/**
	 * Enter a parse tree produced by {@link MappingExpressionParser#exprOrSeqList}.
	 * @param ctx the parse tree
	 */
	void enterExprOrSeqList(MappingExpressionParser.ExprOrSeqListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MappingExpressionParser#exprOrSeqList}.
	 * @param ctx the parse tree
	 */
	void exitExprOrSeqList(MappingExpressionParser.ExprOrSeqListContext ctx);
}