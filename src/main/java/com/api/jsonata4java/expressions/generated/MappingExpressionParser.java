// Generated from com/api/jsonata4java/expressions/generated/MappingExpression.g4 by ANTLR 4.9.2
package com.api.jsonata4java.expressions.generated;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MappingExpressionParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, TRUE=12, FALSE=13, STRING=14, NULL=15, ARR_OPEN=16, 
		ARR_CLOSE=17, OBJ_OPEN=18, OBJ_CLOSE=19, DOLLAR=20, ROOT=21, DESCEND=22, 
		NUMBER=23, FUNCTIONID=24, WS=25, COMMENT=26, CHAIN=27, ASSIGN=28, MUL=29, 
		DIV=30, ADD=31, SUB=32, REM=33, EQ=34, NOT_EQ=35, LT=36, LE=37, GT=38, 
		GE=39, CONCAT=40, VAR_ID=41, ID=42;
	public static final int
		RULE_expr = 0, RULE_fieldList = 1, RULE_exprList = 2, RULE_varList = 3, 
		RULE_exprValues = 4, RULE_emptyValues = 5, RULE_seq = 6, RULE_exprOrSeq = 7, 
		RULE_exprOrSeqList = 8;
	private static String[] makeRuleNames() {
		return new String[] {
			"expr", "fieldList", "exprList", "varList", "exprValues", "emptyValues", 
			"seq", "exprOrSeq", "exprOrSeqList"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'.'", "'in'", "'and'", "'or'", "'?'", "':'", "'('", "';'", "')'", 
			"','", "'..'", "'true'", "'false'", null, "'null'", "'['", "']'", "'{'", 
			"'}'", "'$'", "'$$'", "'**'", null, null, null, null, "'~>'", "':='", 
			"'*'", "'/'", "'+'", "'-'", "'%'", "'='", "'!='", "'<'", "'<='", "'>'", 
			"'>='", "'&'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			"TRUE", "FALSE", "STRING", "NULL", "ARR_OPEN", "ARR_CLOSE", "OBJ_OPEN", 
			"OBJ_CLOSE", "DOLLAR", "ROOT", "DESCEND", "NUMBER", "FUNCTIONID", "WS", 
			"COMMENT", "CHAIN", "ASSIGN", "MUL", "DIV", "ADD", "SUB", "REM", "EQ", 
			"NOT_EQ", "LT", "LE", "GT", "GE", "CONCAT", "VAR_ID", "ID"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "MappingExpression.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MappingExpressionParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ExprContext extends ParserRuleContext {
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	 
		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ParensContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ParensContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterParens(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitParens(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitParens(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Muldiv_opContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode MUL() { return getToken(MappingExpressionParser.MUL, 0); }
		public TerminalNode DIV() { return getToken(MappingExpressionParser.DIV, 0); }
		public TerminalNode REM() { return getToken(MappingExpressionParser.REM, 0); }
		public Muldiv_opContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterMuldiv_op(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitMuldiv_op(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitMuldiv_op(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LogorContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public LogorContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterLogor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitLogor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitLogor(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StringContext extends ExprContext {
		public TerminalNode STRING() { return getToken(MappingExpressionParser.STRING, 0); }
		public StringContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitString(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LogandContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public LogandContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterLogand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitLogand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitLogand(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ConditionalContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ConditionalContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterConditional(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitConditional(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitConditional(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Function_callContext extends ExprContext {
		public TerminalNode VAR_ID() { return getToken(MappingExpressionParser.VAR_ID, 0); }
		public EmptyValuesContext emptyValues() {
			return getRuleContext(EmptyValuesContext.class,0);
		}
		public ExprValuesContext exprValues() {
			return getRuleContext(ExprValuesContext.class,0);
		}
		public Function_callContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterFunction_call(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitFunction_call(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitFunction_call(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Var_assignContext extends ExprContext {
		public TerminalNode VAR_ID() { return getToken(MappingExpressionParser.VAR_ID, 0); }
		public TerminalNode ASSIGN() { return getToken(MappingExpressionParser.ASSIGN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode FUNCTIONID() { return getToken(MappingExpressionParser.FUNCTIONID, 0); }
		public VarListContext varList() {
			return getRuleContext(VarListContext.class,0);
		}
		public TerminalNode OBJ_OPEN() { return getToken(MappingExpressionParser.OBJ_OPEN, 0); }
		public TerminalNode OBJ_CLOSE() { return getToken(MappingExpressionParser.OBJ_CLOSE, 0); }
		public ExprListContext exprList() {
			return getRuleContext(ExprListContext.class,0);
		}
		public Var_assignContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterVar_assign(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitVar_assign(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitVar_assign(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DescendantContext extends ExprContext {
		public TerminalNode DESCEND() { return getToken(MappingExpressionParser.DESCEND, 0); }
		public DescendantContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterDescendant(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitDescendant(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitDescendant(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MembershipContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public MembershipContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterMembership(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitMembership(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitMembership(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Addsub_opContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode ADD() { return getToken(MappingExpressionParser.ADD, 0); }
		public TerminalNode SUB() { return getToken(MappingExpressionParser.SUB, 0); }
		public Addsub_opContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterAddsub_op(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitAddsub_op(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitAddsub_op(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Function_declContext extends ExprContext {
		public TerminalNode FUNCTIONID() { return getToken(MappingExpressionParser.FUNCTIONID, 0); }
		public VarListContext varList() {
			return getRuleContext(VarListContext.class,0);
		}
		public TerminalNode OBJ_OPEN() { return getToken(MappingExpressionParser.OBJ_OPEN, 0); }
		public TerminalNode OBJ_CLOSE() { return getToken(MappingExpressionParser.OBJ_CLOSE, 0); }
		public ExprListContext exprList() {
			return getRuleContext(ExprListContext.class,0);
		}
		public Function_declContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterFunction_decl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitFunction_decl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitFunction_decl(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NumberContext extends ExprContext {
		public TerminalNode NUMBER() { return getToken(MappingExpressionParser.NUMBER, 0); }
		public NumberContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitNumber(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PathContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public PathContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterPath(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitPath(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitPath(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class To_arrayContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode ARR_OPEN() { return getToken(MappingExpressionParser.ARR_OPEN, 0); }
		public TerminalNode ARR_CLOSE() { return getToken(MappingExpressionParser.ARR_CLOSE, 0); }
		public To_arrayContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterTo_array(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitTo_array(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitTo_array(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ArrayContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode ARR_OPEN() { return getToken(MappingExpressionParser.ARR_OPEN, 0); }
		public TerminalNode ARR_CLOSE() { return getToken(MappingExpressionParser.ARR_CLOSE, 0); }
		public ArrayContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterArray(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitArray(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitArray(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IdContext extends ExprContext {
		public TerminalNode ID() { return getToken(MappingExpressionParser.ID, 0); }
		public IdContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitId(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Object_constructorContext extends ExprContext {
		public TerminalNode OBJ_OPEN() { return getToken(MappingExpressionParser.OBJ_OPEN, 0); }
		public TerminalNode OBJ_CLOSE() { return getToken(MappingExpressionParser.OBJ_CLOSE, 0); }
		public FieldListContext fieldList() {
			return getRuleContext(FieldListContext.class,0);
		}
		public Object_constructorContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterObject_constructor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitObject_constructor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitObject_constructor(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Context_refContext extends ExprContext {
		public TerminalNode DOLLAR() { return getToken(MappingExpressionParser.DOLLAR, 0); }
		public Context_refContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterContext_ref(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitContext_ref(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitContext_ref(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Array_constructorContext extends ExprContext {
		public TerminalNode ARR_OPEN() { return getToken(MappingExpressionParser.ARR_OPEN, 0); }
		public TerminalNode ARR_CLOSE() { return getToken(MappingExpressionParser.ARR_CLOSE, 0); }
		public ExprOrSeqListContext exprOrSeqList() {
			return getRuleContext(ExprOrSeqListContext.class,0);
		}
		public Array_constructorContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterArray_constructor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitArray_constructor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitArray_constructor(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Unary_opContext extends ExprContext {
		public Token op;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode SUB() { return getToken(MappingExpressionParser.SUB, 0); }
		public Unary_opContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterUnary_op(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitUnary_op(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitUnary_op(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Var_recallContext extends ExprContext {
		public TerminalNode VAR_ID() { return getToken(MappingExpressionParser.VAR_ID, 0); }
		public Var_recallContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterVar_recall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitVar_recall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitVar_recall(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Concat_opContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode CONCAT() { return getToken(MappingExpressionParser.CONCAT, 0); }
		public Concat_opContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterConcat_op(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitConcat_op(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitConcat_op(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Root_pathContext extends ExprContext {
		public TerminalNode ROOT() { return getToken(MappingExpressionParser.ROOT, 0); }
		public Root_pathContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterRoot_path(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitRoot_path(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitRoot_path(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Fct_chainContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode CHAIN() { return getToken(MappingExpressionParser.CHAIN, 0); }
		public Fct_chainContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterFct_chain(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitFct_chain(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitFct_chain(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BooleanContext extends ExprContext {
		public Token op;
		public TerminalNode TRUE() { return getToken(MappingExpressionParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(MappingExpressionParser.FALSE, 0); }
		public BooleanContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterBoolean(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitBoolean(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitBoolean(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NullContext extends ExprContext {
		public TerminalNode NULL() { return getToken(MappingExpressionParser.NULL, 0); }
		public NullContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterNull(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitNull(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitNull(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Comp_opContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode LT() { return getToken(MappingExpressionParser.LT, 0); }
		public TerminalNode LE() { return getToken(MappingExpressionParser.LE, 0); }
		public TerminalNode GT() { return getToken(MappingExpressionParser.GT, 0); }
		public TerminalNode GE() { return getToken(MappingExpressionParser.GE, 0); }
		public TerminalNode NOT_EQ() { return getToken(MappingExpressionParser.NOT_EQ, 0); }
		public TerminalNode EQ() { return getToken(MappingExpressionParser.EQ, 0); }
		public Comp_opContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterComp_op(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitComp_op(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitComp_op(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Function_execContext extends ExprContext {
		public ExprValuesContext exprValues() {
			return getRuleContext(ExprValuesContext.class,0);
		}
		public TerminalNode FUNCTIONID() { return getToken(MappingExpressionParser.FUNCTIONID, 0); }
		public VarListContext varList() {
			return getRuleContext(VarListContext.class,0);
		}
		public TerminalNode OBJ_OPEN() { return getToken(MappingExpressionParser.OBJ_OPEN, 0); }
		public TerminalNode OBJ_CLOSE() { return getToken(MappingExpressionParser.OBJ_CLOSE, 0); }
		public ExprListContext exprList() {
			return getRuleContext(ExprListContext.class,0);
		}
		public Function_execContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterFunction_exec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitFunction_exec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitFunction_exec(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Field_valuesContext extends ExprContext {
		public TerminalNode MUL() { return getToken(MappingExpressionParser.MUL, 0); }
		public Field_valuesContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterField_values(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitField_values(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitField_values(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ObjectContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode OBJ_OPEN() { return getToken(MappingExpressionParser.OBJ_OPEN, 0); }
		public TerminalNode OBJ_CLOSE() { return getToken(MappingExpressionParser.OBJ_CLOSE, 0); }
		public FieldListContext fieldList() {
			return getRuleContext(FieldListContext.class,0);
		}
		public ObjectContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterObject(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitObject(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitObject(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 0;
		enterRecursionRule(_localctx, 0, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(91);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				_localctx = new IdContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(19);
				match(ID);
				}
				break;
			case 2:
				{
				_localctx = new Field_valuesContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(20);
				match(MUL);
				}
				break;
			case 3:
				{
				_localctx = new DescendantContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(21);
				match(DESCEND);
				}
				break;
			case 4:
				{
				_localctx = new Context_refContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(22);
				match(DOLLAR);
				}
				break;
			case 5:
				{
				_localctx = new Root_pathContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(23);
				match(ROOT);
				}
				break;
			case 6:
				{
				_localctx = new Array_constructorContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(24);
				match(ARR_OPEN);
				setState(26);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__6) | (1L << TRUE) | (1L << FALSE) | (1L << STRING) | (1L << NULL) | (1L << ARR_OPEN) | (1L << OBJ_OPEN) | (1L << DOLLAR) | (1L << ROOT) | (1L << DESCEND) | (1L << NUMBER) | (1L << FUNCTIONID) | (1L << MUL) | (1L << SUB) | (1L << VAR_ID) | (1L << ID))) != 0)) {
					{
					setState(25);
					exprOrSeqList();
					}
				}

				setState(28);
				match(ARR_CLOSE);
				}
				break;
			case 7:
				{
				_localctx = new Object_constructorContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(29);
				match(OBJ_OPEN);
				setState(31);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__6) | (1L << TRUE) | (1L << FALSE) | (1L << STRING) | (1L << NULL) | (1L << ARR_OPEN) | (1L << OBJ_OPEN) | (1L << DOLLAR) | (1L << ROOT) | (1L << DESCEND) | (1L << NUMBER) | (1L << FUNCTIONID) | (1L << MUL) | (1L << SUB) | (1L << VAR_ID) | (1L << ID))) != 0)) {
					{
					setState(30);
					fieldList();
					}
				}

				setState(33);
				match(OBJ_CLOSE);
				}
				break;
			case 8:
				{
				_localctx = new Function_callContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(34);
				match(VAR_ID);
				setState(37);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
				case 1:
					{
					setState(35);
					emptyValues();
					}
					break;
				case 2:
					{
					setState(36);
					exprValues();
					}
					break;
				}
				}
				break;
			case 9:
				{
				_localctx = new Function_declContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(39);
				match(FUNCTIONID);
				setState(40);
				varList();
				setState(41);
				match(OBJ_OPEN);
				setState(43);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__6) | (1L << TRUE) | (1L << FALSE) | (1L << STRING) | (1L << NULL) | (1L << ARR_OPEN) | (1L << OBJ_OPEN) | (1L << DOLLAR) | (1L << ROOT) | (1L << DESCEND) | (1L << NUMBER) | (1L << FUNCTIONID) | (1L << MUL) | (1L << SUB) | (1L << VAR_ID) | (1L << ID))) != 0)) {
					{
					setState(42);
					exprList();
					}
				}

				setState(45);
				match(OBJ_CLOSE);
				}
				break;
			case 10:
				{
				_localctx = new Var_assignContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(47);
				match(VAR_ID);
				setState(48);
				match(ASSIGN);
				setState(58);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
				case 1:
					{
					setState(49);
					expr(0);
					}
					break;
				case 2:
					{
					{
					setState(50);
					match(FUNCTIONID);
					setState(51);
					varList();
					setState(52);
					match(OBJ_OPEN);
					setState(54);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__6) | (1L << TRUE) | (1L << FALSE) | (1L << STRING) | (1L << NULL) | (1L << ARR_OPEN) | (1L << OBJ_OPEN) | (1L << DOLLAR) | (1L << ROOT) | (1L << DESCEND) | (1L << NUMBER) | (1L << FUNCTIONID) | (1L << MUL) | (1L << SUB) | (1L << VAR_ID) | (1L << ID))) != 0)) {
						{
						setState(53);
						exprList();
						}
					}

					setState(56);
					match(OBJ_CLOSE);
					}
					}
					break;
				}
				}
				break;
			case 11:
				{
				_localctx = new Function_execContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				{
				setState(60);
				match(FUNCTIONID);
				setState(61);
				varList();
				setState(62);
				match(OBJ_OPEN);
				setState(64);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__6) | (1L << TRUE) | (1L << FALSE) | (1L << STRING) | (1L << NULL) | (1L << ARR_OPEN) | (1L << OBJ_OPEN) | (1L << DOLLAR) | (1L << ROOT) | (1L << DESCEND) | (1L << NUMBER) | (1L << FUNCTIONID) | (1L << MUL) | (1L << SUB) | (1L << VAR_ID) | (1L << ID))) != 0)) {
					{
					setState(63);
					exprList();
					}
				}

				setState(66);
				match(OBJ_CLOSE);
				}
				setState(68);
				exprValues();
				}
				break;
			case 12:
				{
				_localctx = new BooleanContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(70);
				((BooleanContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==TRUE || _la==FALSE) ) {
					((BooleanContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 13:
				{
				_localctx = new Unary_opContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(71);
				((Unary_opContext)_localctx).op = match(SUB);
				setState(72);
				expr(15);
				}
				break;
			case 14:
				{
				_localctx = new ParensContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(73);
				match(T__6);
				setState(84);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__6) | (1L << TRUE) | (1L << FALSE) | (1L << STRING) | (1L << NULL) | (1L << ARR_OPEN) | (1L << OBJ_OPEN) | (1L << DOLLAR) | (1L << ROOT) | (1L << DESCEND) | (1L << NUMBER) | (1L << FUNCTIONID) | (1L << MUL) | (1L << SUB) | (1L << VAR_ID) | (1L << ID))) != 0)) {
					{
					setState(74);
					expr(0);
					setState(81);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__7) {
						{
						{
						setState(75);
						match(T__7);
						setState(77);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__6) | (1L << TRUE) | (1L << FALSE) | (1L << STRING) | (1L << NULL) | (1L << ARR_OPEN) | (1L << OBJ_OPEN) | (1L << DOLLAR) | (1L << ROOT) | (1L << DESCEND) | (1L << NUMBER) | (1L << FUNCTIONID) | (1L << MUL) | (1L << SUB) | (1L << VAR_ID) | (1L << ID))) != 0)) {
							{
							setState(76);
							expr(0);
							}
						}

						}
						}
						setState(83);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(86);
				match(T__8);
				}
				break;
			case 15:
				{
				_localctx = new Var_recallContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(87);
				match(VAR_ID);
				}
				break;
			case 16:
				{
				_localctx = new NumberContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(88);
				match(NUMBER);
				}
				break;
			case 17:
				{
				_localctx = new StringContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(89);
				match(STRING);
				}
				break;
			case 18:
				{
				_localctx = new NullContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(90);
				match(NULL);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(143);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(141);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
					case 1:
						{
						_localctx = new PathContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(93);
						if (!(precpred(_ctx, 24))) throw new FailedPredicateException(this, "precpred(_ctx, 24)");
						setState(94);
						match(T__0);
						setState(95);
						expr(25);
						}
						break;
					case 2:
						{
						_localctx = new Muldiv_opContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(96);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(97);
						((Muldiv_opContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << MUL) | (1L << DIV) | (1L << REM))) != 0)) ) {
							((Muldiv_opContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(98);
						expr(15);
						}
						break;
					case 3:
						{
						_localctx = new Addsub_opContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(99);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(100);
						((Addsub_opContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==ADD || _la==SUB) ) {
							((Addsub_opContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(101);
						expr(14);
						}
						break;
					case 4:
						{
						_localctx = new Concat_opContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(102);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(103);
						match(CONCAT);
						setState(104);
						expr(13);
						}
						break;
					case 5:
						{
						_localctx = new Comp_opContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(105);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(106);
						((Comp_opContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << EQ) | (1L << NOT_EQ) | (1L << LT) | (1L << LE) | (1L << GT) | (1L << GE))) != 0)) ) {
							((Comp_opContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(107);
						expr(12);
						}
						break;
					case 6:
						{
						_localctx = new MembershipContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(108);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(109);
						match(T__1);
						setState(110);
						expr(11);
						}
						break;
					case 7:
						{
						_localctx = new LogandContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(111);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(112);
						match(T__2);
						setState(113);
						expr(10);
						}
						break;
					case 8:
						{
						_localctx = new LogorContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(114);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(115);
						match(T__3);
						setState(116);
						expr(9);
						}
						break;
					case 9:
						{
						_localctx = new Fct_chainContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(117);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(118);
						match(CHAIN);
						setState(119);
						expr(7);
						}
						break;
					case 10:
						{
						_localctx = new To_arrayContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(120);
						if (!(precpred(_ctx, 23))) throw new FailedPredicateException(this, "precpred(_ctx, 23)");
						setState(121);
						match(ARR_OPEN);
						setState(122);
						match(ARR_CLOSE);
						}
						break;
					case 11:
						{
						_localctx = new ArrayContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(123);
						if (!(precpred(_ctx, 22))) throw new FailedPredicateException(this, "precpred(_ctx, 22)");
						setState(124);
						match(ARR_OPEN);
						setState(125);
						expr(0);
						setState(126);
						match(ARR_CLOSE);
						}
						break;
					case 12:
						{
						_localctx = new ObjectContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(128);
						if (!(precpred(_ctx, 21))) throw new FailedPredicateException(this, "precpred(_ctx, 21)");
						setState(129);
						match(OBJ_OPEN);
						setState(131);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__6) | (1L << TRUE) | (1L << FALSE) | (1L << STRING) | (1L << NULL) | (1L << ARR_OPEN) | (1L << OBJ_OPEN) | (1L << DOLLAR) | (1L << ROOT) | (1L << DESCEND) | (1L << NUMBER) | (1L << FUNCTIONID) | (1L << MUL) | (1L << SUB) | (1L << VAR_ID) | (1L << ID))) != 0)) {
							{
							setState(130);
							fieldList();
							}
						}

						setState(133);
						match(OBJ_CLOSE);
						}
						break;
					case 13:
						{
						_localctx = new ConditionalContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(134);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(135);
						match(T__4);
						setState(136);
						expr(0);
						setState(139);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
						case 1:
							{
							setState(137);
							match(T__5);
							setState(138);
							expr(0);
							}
							break;
						}
						}
						break;
					}
					} 
				}
				setState(145);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class FieldListContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> STRING() { return getTokens(MappingExpressionParser.STRING); }
		public TerminalNode STRING(int i) {
			return getToken(MappingExpressionParser.STRING, i);
		}
		public FieldListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterFieldList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitFieldList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitFieldList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldListContext fieldList() throws RecognitionException {
		FieldListContext _localctx = new FieldListContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_fieldList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(148);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				{
				setState(146);
				match(STRING);
				}
				break;
			case 2:
				{
				setState(147);
				expr(0);
				}
				break;
			}
			setState(150);
			match(T__5);
			setState(151);
			expr(0);
			setState(161);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__9) {
				{
				{
				setState(152);
				match(T__9);
				setState(155);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
				case 1:
					{
					setState(153);
					match(STRING);
					}
					break;
				case 2:
					{
					setState(154);
					expr(0);
					}
					break;
				}
				setState(157);
				match(T__5);
				setState(158);
				expr(0);
				}
				}
				setState(163);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprListContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ExprListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterExprList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitExprList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitExprList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprListContext exprList() throws RecognitionException {
		ExprListContext _localctx = new ExprListContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_exprList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(164);
			expr(0);
			setState(169);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__9) {
				{
				{
				setState(165);
				match(T__9);
				setState(166);
				expr(0);
				}
				}
				setState(171);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VarListContext extends ParserRuleContext {
		public List<TerminalNode> VAR_ID() { return getTokens(MappingExpressionParser.VAR_ID); }
		public TerminalNode VAR_ID(int i) {
			return getToken(MappingExpressionParser.VAR_ID, i);
		}
		public VarListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterVarList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitVarList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitVarList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarListContext varList() throws RecognitionException {
		VarListContext _localctx = new VarListContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_varList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(172);
			match(T__6);
			setState(183);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==VAR_ID) {
				{
				{
				setState(173);
				match(VAR_ID);
				setState(178);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__9) {
					{
					{
					setState(174);
					match(T__9);
					setState(175);
					match(VAR_ID);
					}
					}
					setState(180);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				}
				setState(185);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(186);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprValuesContext extends ParserRuleContext {
		public ExprListContext exprList() {
			return getRuleContext(ExprListContext.class,0);
		}
		public List<ExprOrSeqContext> exprOrSeq() {
			return getRuleContexts(ExprOrSeqContext.class);
		}
		public ExprOrSeqContext exprOrSeq(int i) {
			return getRuleContext(ExprOrSeqContext.class,i);
		}
		public ExprValuesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprValues; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterExprValues(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitExprValues(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitExprValues(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprValuesContext exprValues() throws RecognitionException {
		ExprValuesContext _localctx = new ExprValuesContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_exprValues);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(188);
			match(T__6);
			setState(189);
			exprList();
			setState(190);
			match(T__8);
			setState(199);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				{
				setState(195);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__9) {
					{
					{
					setState(191);
					match(T__9);
					setState(192);
					exprOrSeq();
					}
					}
					setState(197);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(198);
				match(T__8);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EmptyValuesContext extends ParserRuleContext {
		public EmptyValuesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_emptyValues; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterEmptyValues(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitEmptyValues(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitEmptyValues(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EmptyValuesContext emptyValues() throws RecognitionException {
		EmptyValuesContext _localctx = new EmptyValuesContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_emptyValues);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(201);
			match(T__6);
			setState(202);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SeqContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public SeqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_seq; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterSeq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitSeq(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitSeq(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SeqContext seq() throws RecognitionException {
		SeqContext _localctx = new SeqContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_seq);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(204);
			expr(0);
			setState(205);
			match(T__10);
			setState(206);
			expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprOrSeqContext extends ParserRuleContext {
		public SeqContext seq() {
			return getRuleContext(SeqContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ExprOrSeqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprOrSeq; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterExprOrSeq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitExprOrSeq(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitExprOrSeq(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprOrSeqContext exprOrSeq() throws RecognitionException {
		ExprOrSeqContext _localctx = new ExprOrSeqContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_exprOrSeq);
		try {
			setState(210);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(208);
				seq();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(209);
				expr(0);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprOrSeqListContext extends ParserRuleContext {
		public List<ExprOrSeqContext> exprOrSeq() {
			return getRuleContexts(ExprOrSeqContext.class);
		}
		public ExprOrSeqContext exprOrSeq(int i) {
			return getRuleContext(ExprOrSeqContext.class,i);
		}
		public ExprOrSeqListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprOrSeqList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterExprOrSeqList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitExprOrSeqList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitExprOrSeqList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprOrSeqListContext exprOrSeqList() throws RecognitionException {
		ExprOrSeqListContext _localctx = new ExprOrSeqListContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_exprOrSeqList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(212);
			exprOrSeq();
			setState(217);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__9) {
				{
				{
				setState(213);
				match(T__9);
				setState(214);
				exprOrSeq();
				}
				}
				setState(219);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 0:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 24);
		case 1:
			return precpred(_ctx, 14);
		case 2:
			return precpred(_ctx, 13);
		case 3:
			return precpred(_ctx, 12);
		case 4:
			return precpred(_ctx, 11);
		case 5:
			return precpred(_ctx, 10);
		case 6:
			return precpred(_ctx, 9);
		case 7:
			return precpred(_ctx, 8);
		case 8:
			return precpred(_ctx, 6);
		case 9:
			return precpred(_ctx, 23);
		case 10:
			return precpred(_ctx, 22);
		case 11:
			return precpred(_ctx, 21);
		case 12:
			return precpred(_ctx, 7);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3,\u00df\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\3\2\3\2"+
		"\3\2\3\2\3\2\3\2\3\2\3\2\5\2\35\n\2\3\2\3\2\3\2\5\2\"\n\2\3\2\3\2\3\2"+
		"\3\2\5\2(\n\2\3\2\3\2\3\2\3\2\5\2.\n\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2"+
		"\3\2\5\29\n\2\3\2\3\2\5\2=\n\2\3\2\3\2\3\2\3\2\5\2C\n\2\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\5\2P\n\2\7\2R\n\2\f\2\16\2U\13\2\5\2W\n"+
		"\2\3\2\3\2\3\2\3\2\3\2\5\2^\n\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2"+
		"\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\5\2\u0086\n\2\3\2\3\2\3\2"+
		"\3\2\3\2\3\2\5\2\u008e\n\2\7\2\u0090\n\2\f\2\16\2\u0093\13\2\3\3\3\3\5"+
		"\3\u0097\n\3\3\3\3\3\3\3\3\3\3\3\5\3\u009e\n\3\3\3\3\3\7\3\u00a2\n\3\f"+
		"\3\16\3\u00a5\13\3\3\4\3\4\3\4\7\4\u00aa\n\4\f\4\16\4\u00ad\13\4\3\5\3"+
		"\5\3\5\3\5\7\5\u00b3\n\5\f\5\16\5\u00b6\13\5\7\5\u00b8\n\5\f\5\16\5\u00bb"+
		"\13\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\7\6\u00c4\n\6\f\6\16\6\u00c7\13\6\3"+
		"\6\5\6\u00ca\n\6\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\t\3\t\5\t\u00d5\n\t\3\n"+
		"\3\n\3\n\7\n\u00da\n\n\f\n\16\n\u00dd\13\n\3\n\2\3\2\13\2\4\6\b\n\f\16"+
		"\20\22\2\6\3\2\16\17\4\2\37 ##\3\2!\"\3\2$)\2\u0109\2]\3\2\2\2\4\u0096"+
		"\3\2\2\2\6\u00a6\3\2\2\2\b\u00ae\3\2\2\2\n\u00be\3\2\2\2\f\u00cb\3\2\2"+
		"\2\16\u00ce\3\2\2\2\20\u00d4\3\2\2\2\22\u00d6\3\2\2\2\24\25\b\2\1\2\25"+
		"^\7,\2\2\26^\7\37\2\2\27^\7\30\2\2\30^\7\26\2\2\31^\7\27\2\2\32\34\7\22"+
		"\2\2\33\35\5\22\n\2\34\33\3\2\2\2\34\35\3\2\2\2\35\36\3\2\2\2\36^\7\23"+
		"\2\2\37!\7\24\2\2 \"\5\4\3\2! \3\2\2\2!\"\3\2\2\2\"#\3\2\2\2#^\7\25\2"+
		"\2$\'\7+\2\2%(\5\f\7\2&(\5\n\6\2\'%\3\2\2\2\'&\3\2\2\2(^\3\2\2\2)*\7\32"+
		"\2\2*+\5\b\5\2+-\7\24\2\2,.\5\6\4\2-,\3\2\2\2-.\3\2\2\2./\3\2\2\2/\60"+
		"\7\25\2\2\60^\3\2\2\2\61\62\7+\2\2\62<\7\36\2\2\63=\5\2\2\2\64\65\7\32"+
		"\2\2\65\66\5\b\5\2\668\7\24\2\2\679\5\6\4\28\67\3\2\2\289\3\2\2\29:\3"+
		"\2\2\2:;\7\25\2\2;=\3\2\2\2<\63\3\2\2\2<\64\3\2\2\2=^\3\2\2\2>?\7\32\2"+
		"\2?@\5\b\5\2@B\7\24\2\2AC\5\6\4\2BA\3\2\2\2BC\3\2\2\2CD\3\2\2\2DE\7\25"+
		"\2\2EF\3\2\2\2FG\5\n\6\2G^\3\2\2\2H^\t\2\2\2IJ\7\"\2\2J^\5\2\2\21KV\7"+
		"\t\2\2LS\5\2\2\2MO\7\n\2\2NP\5\2\2\2ON\3\2\2\2OP\3\2\2\2PR\3\2\2\2QM\3"+
		"\2\2\2RU\3\2\2\2SQ\3\2\2\2ST\3\2\2\2TW\3\2\2\2US\3\2\2\2VL\3\2\2\2VW\3"+
		"\2\2\2WX\3\2\2\2X^\7\13\2\2Y^\7+\2\2Z^\7\31\2\2[^\7\20\2\2\\^\7\21\2\2"+
		"]\24\3\2\2\2]\26\3\2\2\2]\27\3\2\2\2]\30\3\2\2\2]\31\3\2\2\2]\32\3\2\2"+
		"\2]\37\3\2\2\2]$\3\2\2\2])\3\2\2\2]\61\3\2\2\2]>\3\2\2\2]H\3\2\2\2]I\3"+
		"\2\2\2]K\3\2\2\2]Y\3\2\2\2]Z\3\2\2\2][\3\2\2\2]\\\3\2\2\2^\u0091\3\2\2"+
		"\2_`\f\32\2\2`a\7\3\2\2a\u0090\5\2\2\33bc\f\20\2\2cd\t\3\2\2d\u0090\5"+
		"\2\2\21ef\f\17\2\2fg\t\4\2\2g\u0090\5\2\2\20hi\f\16\2\2ij\7*\2\2j\u0090"+
		"\5\2\2\17kl\f\r\2\2lm\t\5\2\2m\u0090\5\2\2\16no\f\f\2\2op\7\4\2\2p\u0090"+
		"\5\2\2\rqr\f\13\2\2rs\7\5\2\2s\u0090\5\2\2\ftu\f\n\2\2uv\7\6\2\2v\u0090"+
		"\5\2\2\13wx\f\b\2\2xy\7\35\2\2y\u0090\5\2\2\tz{\f\31\2\2{|\7\22\2\2|\u0090"+
		"\7\23\2\2}~\f\30\2\2~\177\7\22\2\2\177\u0080\5\2\2\2\u0080\u0081\7\23"+
		"\2\2\u0081\u0090\3\2\2\2\u0082\u0083\f\27\2\2\u0083\u0085\7\24\2\2\u0084"+
		"\u0086\5\4\3\2\u0085\u0084\3\2\2\2\u0085\u0086\3\2\2\2\u0086\u0087\3\2"+
		"\2\2\u0087\u0090\7\25\2\2\u0088\u0089\f\t\2\2\u0089\u008a\7\7\2\2\u008a"+
		"\u008d\5\2\2\2\u008b\u008c\7\b\2\2\u008c\u008e\5\2\2\2\u008d\u008b\3\2"+
		"\2\2\u008d\u008e\3\2\2\2\u008e\u0090\3\2\2\2\u008f_\3\2\2\2\u008fb\3\2"+
		"\2\2\u008fe\3\2\2\2\u008fh\3\2\2\2\u008fk\3\2\2\2\u008fn\3\2\2\2\u008f"+
		"q\3\2\2\2\u008ft\3\2\2\2\u008fw\3\2\2\2\u008fz\3\2\2\2\u008f}\3\2\2\2"+
		"\u008f\u0082\3\2\2\2\u008f\u0088\3\2\2\2\u0090\u0093\3\2\2\2\u0091\u008f"+
		"\3\2\2\2\u0091\u0092\3\2\2\2\u0092\3\3\2\2\2\u0093\u0091\3\2\2\2\u0094"+
		"\u0097\7\20\2\2\u0095\u0097\5\2\2\2\u0096\u0094\3\2\2\2\u0096\u0095\3"+
		"\2\2\2\u0097\u0098\3\2\2\2\u0098\u0099\7\b\2\2\u0099\u00a3\5\2\2\2\u009a"+
		"\u009d\7\f\2\2\u009b\u009e\7\20\2\2\u009c\u009e\5\2\2\2\u009d\u009b\3"+
		"\2\2\2\u009d\u009c\3\2\2\2\u009e\u009f\3\2\2\2\u009f\u00a0\7\b\2\2\u00a0"+
		"\u00a2\5\2\2\2\u00a1\u009a\3\2\2\2\u00a2\u00a5\3\2\2\2\u00a3\u00a1\3\2"+
		"\2\2\u00a3\u00a4\3\2\2\2\u00a4\5\3\2\2\2\u00a5\u00a3\3\2\2\2\u00a6\u00ab"+
		"\5\2\2\2\u00a7\u00a8\7\f\2\2\u00a8\u00aa\5\2\2\2\u00a9\u00a7\3\2\2\2\u00aa"+
		"\u00ad\3\2\2\2\u00ab\u00a9\3\2\2\2\u00ab\u00ac\3\2\2\2\u00ac\7\3\2\2\2"+
		"\u00ad\u00ab\3\2\2\2\u00ae\u00b9\7\t\2\2\u00af\u00b4\7+\2\2\u00b0\u00b1"+
		"\7\f\2\2\u00b1\u00b3\7+\2\2\u00b2\u00b0\3\2\2\2\u00b3\u00b6\3\2\2\2\u00b4"+
		"\u00b2\3\2\2\2\u00b4\u00b5\3\2\2\2\u00b5\u00b8\3\2\2\2\u00b6\u00b4\3\2"+
		"\2\2\u00b7\u00af\3\2\2\2\u00b8\u00bb\3\2\2\2\u00b9\u00b7\3\2\2\2\u00b9"+
		"\u00ba\3\2\2\2\u00ba\u00bc\3\2\2\2\u00bb\u00b9\3\2\2\2\u00bc\u00bd\7\13"+
		"\2\2\u00bd\t\3\2\2\2\u00be\u00bf\7\t\2\2\u00bf\u00c0\5\6\4\2\u00c0\u00c9"+
		"\7\13\2\2\u00c1\u00c2\7\f\2\2\u00c2\u00c4\5\20\t\2\u00c3\u00c1\3\2\2\2"+
		"\u00c4\u00c7\3\2\2\2\u00c5\u00c3\3\2\2\2\u00c5\u00c6\3\2\2\2\u00c6\u00c8"+
		"\3\2\2\2\u00c7\u00c5\3\2\2\2\u00c8\u00ca\7\13\2\2\u00c9\u00c5\3\2\2\2"+
		"\u00c9\u00ca\3\2\2\2\u00ca\13\3\2\2\2\u00cb\u00cc\7\t\2\2\u00cc\u00cd"+
		"\7\13\2\2\u00cd\r\3\2\2\2\u00ce\u00cf\5\2\2\2\u00cf\u00d0\7\r\2\2\u00d0"+
		"\u00d1\5\2\2\2\u00d1\17\3\2\2\2\u00d2\u00d5\5\16\b\2\u00d3\u00d5\5\2\2"+
		"\2\u00d4\u00d2\3\2\2\2\u00d4\u00d3\3\2\2\2\u00d5\21\3\2\2\2\u00d6\u00db"+
		"\5\20\t\2\u00d7\u00d8\7\f\2\2\u00d8\u00da\5\20\t\2\u00d9\u00d7\3\2\2\2"+
		"\u00da\u00dd\3\2\2\2\u00db\u00d9\3\2\2\2\u00db\u00dc\3\2\2\2\u00dc\23"+
		"\3\2\2\2\u00dd\u00db\3\2\2\2\33\34!\'-8<BOSV]\u0085\u008d\u008f\u0091"+
		"\u0096\u009d\u00a3\u00ab\u00b4\u00b9\u00c5\u00c9\u00d4\u00db";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}