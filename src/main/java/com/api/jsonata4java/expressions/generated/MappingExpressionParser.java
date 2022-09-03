// Generated from com\api\jsonata4java\expressions\generated\MappingExpression.g4 by ANTLR 4.10.1
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
	static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		TRUE=10, FALSE=11, STRING=12, NULL=13, ARR_OPEN=14, ARR_CLOSE=15, OBJ_OPEN=16, 
		OBJ_CLOSE=17, DOLLAR=18, ROOT=19, DESCEND=20, NUMBER=21, FUNCTIONID=22, 
		WS=23, COMMENT=24, CHAIN=25, ASSIGN=26, MUL=27, DIV=28, ADD=29, SUB=30, 
		REM=31, EQ=32, NOT_EQ=33, LT=34, LE=35, GT=36, GE=37, CONCAT=38, AND=39, 
		OR=40, VAR_ID=41, ID=42;
	public static final int
		RULE_expr_to_eof = 0, RULE_expr = 1, RULE_fieldList = 2, RULE_exprList = 3, 
		RULE_varList = 4, RULE_exprValues = 5, RULE_emptyValues = 6, RULE_seq = 7, 
		RULE_exprOrSeq = 8, RULE_exprOrSeqList = 9, RULE_regularExpression = 10, 
		RULE_regexPattern = 11;
	private static String[] makeRuleNames() {
		return new String[] {
			"expr_to_eof", "expr", "fieldList", "exprList", "varList", "exprValues", 
			"emptyValues", "seq", "exprOrSeq", "exprOrSeqList", "regularExpression", 
			"regexPattern"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'.'", "'in'", "'?'", "':'", "'('", "';'", "')'", "','", "'..'", 
			"'true'", "'false'", null, "'null'", "'['", "']'", "'{'", "'}'", "'$'", 
			"'$$'", "'**'", null, null, null, null, "'~>'", "':='", "'*'", "'/'", 
			"'+'", "'-'", "'%'", "'='", "'!='", "'<'", "'<='", "'>'", "'>='", "'&'", 
			"'and'", "'or'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, "TRUE", "FALSE", 
			"STRING", "NULL", "ARR_OPEN", "ARR_CLOSE", "OBJ_OPEN", "OBJ_CLOSE", "DOLLAR", 
			"ROOT", "DESCEND", "NUMBER", "FUNCTIONID", "WS", "COMMENT", "CHAIN", 
			"ASSIGN", "MUL", "DIV", "ADD", "SUB", "REM", "EQ", "NOT_EQ", "LT", "LE", 
			"GT", "GE", "CONCAT", "AND", "OR", "VAR_ID", "ID"
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

	public static class Expr_to_eofContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode EOF() { return getToken(MappingExpressionParser.EOF, 0); }
		public Expr_to_eofContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr_to_eof; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterExpr_to_eof(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitExpr_to_eof(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitExpr_to_eof(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Expr_to_eofContext expr_to_eof() throws RecognitionException {
		Expr_to_eofContext _localctx = new Expr_to_eofContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_expr_to_eof);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(24);
			expr(0);
			setState(25);
			match(EOF);
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
		public TerminalNode OR() { return getToken(MappingExpressionParser.OR, 0); }
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
	public static class Regular_expressionContext extends ExprContext {
		public RegularExpressionContext regularExpression() {
			return getRuleContext(RegularExpressionContext.class,0);
		}
		public Regular_expressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterRegular_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitRegular_expression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitRegular_expression(this);
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
		public TerminalNode AND() { return getToken(MappingExpressionParser.AND, 0); }
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
		public Token op;
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
		int _startState = 2;
		enterRecursionRule(_localctx, 2, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(101);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				_localctx = new IdContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(28);
				match(ID);
				}
				break;
			case 2:
				{
				_localctx = new Field_valuesContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(29);
				match(MUL);
				}
				break;
			case 3:
				{
				_localctx = new DescendantContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(30);
				match(DESCEND);
				}
				break;
			case 4:
				{
				_localctx = new Context_refContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(31);
				match(DOLLAR);
				}
				break;
			case 5:
				{
				_localctx = new Root_pathContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(32);
				match(ROOT);
				}
				break;
			case 6:
				{
				_localctx = new Array_constructorContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(33);
				match(ARR_OPEN);
				setState(35);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << TRUE) | (1L << FALSE) | (1L << STRING) | (1L << NULL) | (1L << ARR_OPEN) | (1L << OBJ_OPEN) | (1L << DOLLAR) | (1L << ROOT) | (1L << DESCEND) | (1L << NUMBER) | (1L << FUNCTIONID) | (1L << MUL) | (1L << DIV) | (1L << SUB) | (1L << VAR_ID) | (1L << ID))) != 0)) {
					{
					setState(34);
					exprOrSeqList();
					}
				}

				setState(37);
				match(ARR_CLOSE);
				}
				break;
			case 7:
				{
				_localctx = new Object_constructorContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(38);
				match(OBJ_OPEN);
				setState(40);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << TRUE) | (1L << FALSE) | (1L << STRING) | (1L << NULL) | (1L << ARR_OPEN) | (1L << OBJ_OPEN) | (1L << DOLLAR) | (1L << ROOT) | (1L << DESCEND) | (1L << NUMBER) | (1L << FUNCTIONID) | (1L << MUL) | (1L << DIV) | (1L << SUB) | (1L << VAR_ID) | (1L << ID))) != 0)) {
					{
					setState(39);
					fieldList();
					}
				}

				setState(42);
				match(OBJ_CLOSE);
				}
				break;
			case 8:
				{
				_localctx = new Function_callContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(43);
				match(VAR_ID);
				setState(46);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
				case 1:
					{
					setState(44);
					emptyValues();
					}
					break;
				case 2:
					{
					setState(45);
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
				setState(48);
				match(FUNCTIONID);
				setState(49);
				varList();
				setState(50);
				match(OBJ_OPEN);
				setState(52);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << TRUE) | (1L << FALSE) | (1L << STRING) | (1L << NULL) | (1L << ARR_OPEN) | (1L << OBJ_OPEN) | (1L << DOLLAR) | (1L << ROOT) | (1L << DESCEND) | (1L << NUMBER) | (1L << FUNCTIONID) | (1L << MUL) | (1L << DIV) | (1L << SUB) | (1L << VAR_ID) | (1L << ID))) != 0)) {
					{
					setState(51);
					exprList();
					}
				}

				setState(54);
				match(OBJ_CLOSE);
				}
				break;
			case 10:
				{
				_localctx = new Var_assignContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(56);
				match(VAR_ID);
				setState(57);
				match(ASSIGN);
				setState(67);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
				case 1:
					{
					setState(58);
					expr(0);
					}
					break;
				case 2:
					{
					{
					setState(59);
					match(FUNCTIONID);
					setState(60);
					varList();
					setState(61);
					match(OBJ_OPEN);
					setState(63);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << TRUE) | (1L << FALSE) | (1L << STRING) | (1L << NULL) | (1L << ARR_OPEN) | (1L << OBJ_OPEN) | (1L << DOLLAR) | (1L << ROOT) | (1L << DESCEND) | (1L << NUMBER) | (1L << FUNCTIONID) | (1L << MUL) | (1L << DIV) | (1L << SUB) | (1L << VAR_ID) | (1L << ID))) != 0)) {
						{
						setState(62);
						exprList();
						}
					}

					setState(65);
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
				setState(69);
				match(FUNCTIONID);
				setState(70);
				varList();
				setState(71);
				match(OBJ_OPEN);
				setState(73);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << TRUE) | (1L << FALSE) | (1L << STRING) | (1L << NULL) | (1L << ARR_OPEN) | (1L << OBJ_OPEN) | (1L << DOLLAR) | (1L << ROOT) | (1L << DESCEND) | (1L << NUMBER) | (1L << FUNCTIONID) | (1L << MUL) | (1L << DIV) | (1L << SUB) | (1L << VAR_ID) | (1L << ID))) != 0)) {
					{
					setState(72);
					exprList();
					}
				}

				setState(75);
				match(OBJ_CLOSE);
				}
				setState(77);
				exprValues();
				}
				break;
			case 12:
				{
				_localctx = new BooleanContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(79);
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
				setState(80);
				((Unary_opContext)_localctx).op = match(SUB);
				setState(81);
				expr(16);
				}
				break;
			case 14:
				{
				_localctx = new Regular_expressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(82);
				regularExpression();
				}
				break;
			case 15:
				{
				_localctx = new ParensContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(83);
				match(T__4);
				setState(94);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << TRUE) | (1L << FALSE) | (1L << STRING) | (1L << NULL) | (1L << ARR_OPEN) | (1L << OBJ_OPEN) | (1L << DOLLAR) | (1L << ROOT) | (1L << DESCEND) | (1L << NUMBER) | (1L << FUNCTIONID) | (1L << MUL) | (1L << DIV) | (1L << SUB) | (1L << VAR_ID) | (1L << ID))) != 0)) {
					{
					setState(84);
					expr(0);
					setState(91);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__5) {
						{
						{
						setState(85);
						match(T__5);
						setState(87);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << TRUE) | (1L << FALSE) | (1L << STRING) | (1L << NULL) | (1L << ARR_OPEN) | (1L << OBJ_OPEN) | (1L << DOLLAR) | (1L << ROOT) | (1L << DESCEND) | (1L << NUMBER) | (1L << FUNCTIONID) | (1L << MUL) | (1L << DIV) | (1L << SUB) | (1L << VAR_ID) | (1L << ID))) != 0)) {
							{
							setState(86);
							expr(0);
							}
						}

						}
						}
						setState(93);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(96);
				match(T__6);
				}
				break;
			case 16:
				{
				_localctx = new Var_recallContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(97);
				match(VAR_ID);
				}
				break;
			case 17:
				{
				_localctx = new NumberContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(98);
				match(NUMBER);
				}
				break;
			case 18:
				{
				_localctx = new StringContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(99);
				match(STRING);
				}
				break;
			case 19:
				{
				_localctx = new NullContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(100);
				match(NULL);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(153);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(151);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
					case 1:
						{
						_localctx = new PathContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(103);
						if (!(precpred(_ctx, 25))) throw new FailedPredicateException(this, "precpred(_ctx, 25)");
						setState(104);
						match(T__0);
						setState(105);
						expr(26);
						}
						break;
					case 2:
						{
						_localctx = new Muldiv_opContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(106);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(107);
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
						setState(108);
						expr(16);
						}
						break;
					case 3:
						{
						_localctx = new Addsub_opContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(109);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(110);
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
						setState(111);
						expr(15);
						}
						break;
					case 4:
						{
						_localctx = new Concat_opContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(112);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(113);
						((Concat_opContext)_localctx).op = match(CONCAT);
						setState(114);
						expr(14);
						}
						break;
					case 5:
						{
						_localctx = new Comp_opContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(115);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(116);
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
						setState(117);
						expr(13);
						}
						break;
					case 6:
						{
						_localctx = new MembershipContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(118);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(119);
						match(T__1);
						setState(120);
						expr(12);
						}
						break;
					case 7:
						{
						_localctx = new LogandContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(121);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(122);
						match(AND);
						setState(123);
						expr(11);
						}
						break;
					case 8:
						{
						_localctx = new LogorContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(124);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(125);
						match(OR);
						setState(126);
						expr(10);
						}
						break;
					case 9:
						{
						_localctx = new Fct_chainContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(127);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(128);
						match(CHAIN);
						setState(129);
						expr(8);
						}
						break;
					case 10:
						{
						_localctx = new To_arrayContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(130);
						if (!(precpred(_ctx, 24))) throw new FailedPredicateException(this, "precpred(_ctx, 24)");
						setState(131);
						match(ARR_OPEN);
						setState(132);
						match(ARR_CLOSE);
						}
						break;
					case 11:
						{
						_localctx = new ArrayContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(133);
						if (!(precpred(_ctx, 23))) throw new FailedPredicateException(this, "precpred(_ctx, 23)");
						setState(134);
						match(ARR_OPEN);
						setState(135);
						expr(0);
						setState(136);
						match(ARR_CLOSE);
						}
						break;
					case 12:
						{
						_localctx = new ObjectContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(138);
						if (!(precpred(_ctx, 22))) throw new FailedPredicateException(this, "precpred(_ctx, 22)");
						setState(139);
						match(OBJ_OPEN);
						setState(141);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << TRUE) | (1L << FALSE) | (1L << STRING) | (1L << NULL) | (1L << ARR_OPEN) | (1L << OBJ_OPEN) | (1L << DOLLAR) | (1L << ROOT) | (1L << DESCEND) | (1L << NUMBER) | (1L << FUNCTIONID) | (1L << MUL) | (1L << DIV) | (1L << SUB) | (1L << VAR_ID) | (1L << ID))) != 0)) {
							{
							setState(140);
							fieldList();
							}
						}

						setState(143);
						match(OBJ_CLOSE);
						}
						break;
					case 13:
						{
						_localctx = new ConditionalContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(144);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(145);
						match(T__2);
						setState(146);
						expr(0);
						setState(149);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
						case 1:
							{
							setState(147);
							match(T__3);
							setState(148);
							expr(0);
							}
							break;
						}
						}
						break;
					}
					} 
				}
				setState(155);
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
		enterRule(_localctx, 4, RULE_fieldList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(158);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				{
				setState(156);
				match(STRING);
				}
				break;
			case 2:
				{
				setState(157);
				expr(0);
				}
				break;
			}
			setState(160);
			match(T__3);
			setState(161);
			expr(0);
			setState(171);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__7) {
				{
				{
				setState(162);
				match(T__7);
				setState(165);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
				case 1:
					{
					setState(163);
					match(STRING);
					}
					break;
				case 2:
					{
					setState(164);
					expr(0);
					}
					break;
				}
				setState(167);
				match(T__3);
				setState(168);
				expr(0);
				}
				}
				setState(173);
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
		enterRule(_localctx, 6, RULE_exprList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(174);
			expr(0);
			setState(179);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__7) {
				{
				{
				setState(175);
				match(T__7);
				setState(176);
				expr(0);
				}
				}
				setState(181);
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
		enterRule(_localctx, 8, RULE_varList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(182);
			match(T__4);
			setState(193);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==VAR_ID) {
				{
				{
				setState(183);
				match(VAR_ID);
				setState(188);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__7) {
					{
					{
					setState(184);
					match(T__7);
					setState(185);
					match(VAR_ID);
					}
					}
					setState(190);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				}
				setState(195);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(196);
			match(T__6);
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
		enterRule(_localctx, 10, RULE_exprValues);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(198);
			match(T__4);
			setState(199);
			exprList();
			setState(200);
			match(T__6);
			setState(209);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				{
				setState(205);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__7) {
					{
					{
					setState(201);
					match(T__7);
					setState(202);
					exprOrSeq();
					}
					}
					setState(207);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(208);
				match(T__6);
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
		enterRule(_localctx, 12, RULE_emptyValues);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(211);
			match(T__4);
			setState(212);
			match(T__6);
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
		enterRule(_localctx, 14, RULE_seq);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(214);
			expr(0);
			setState(215);
			match(T__8);
			setState(216);
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
		enterRule(_localctx, 16, RULE_exprOrSeq);
		try {
			setState(220);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(218);
				seq();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(219);
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
		enterRule(_localctx, 18, RULE_exprOrSeqList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(222);
			exprOrSeq();
			setState(227);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__7) {
				{
				{
				setState(223);
				match(T__7);
				setState(224);
				exprOrSeq();
				}
				}
				setState(229);
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

	public static class RegularExpressionContext extends ParserRuleContext {
		public List<TerminalNode> DIV() { return getTokens(MappingExpressionParser.DIV); }
		public TerminalNode DIV(int i) {
			return getToken(MappingExpressionParser.DIV, i);
		}
		public RegexPatternContext regexPattern() {
			return getRuleContext(RegexPatternContext.class,0);
		}
		public RegularExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_regularExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterRegularExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitRegularExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitRegularExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RegularExpressionContext regularExpression() throws RecognitionException {
		RegularExpressionContext _localctx = new RegularExpressionContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_regularExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(230);
			match(DIV);
			setState(231);
			regexPattern();
			setState(232);
			match(DIV);
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

	public static class RegexPatternContext extends ParserRuleContext {
		public List<TerminalNode> DIV() { return getTokens(MappingExpressionParser.DIV); }
		public TerminalNode DIV(int i) {
			return getToken(MappingExpressionParser.DIV, i);
		}
		public RegexPatternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_regexPattern; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterRegexPattern(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitRegexPattern(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitRegexPattern(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RegexPatternContext regexPattern() throws RecognitionException {
		RegexPatternContext _localctx = new RegexPatternContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_regexPattern);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(237);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << TRUE) | (1L << FALSE) | (1L << STRING) | (1L << NULL) | (1L << ARR_OPEN) | (1L << ARR_CLOSE) | (1L << OBJ_OPEN) | (1L << OBJ_CLOSE) | (1L << DOLLAR) | (1L << ROOT) | (1L << DESCEND) | (1L << NUMBER) | (1L << FUNCTIONID) | (1L << WS) | (1L << COMMENT) | (1L << CHAIN) | (1L << ASSIGN) | (1L << MUL) | (1L << ADD) | (1L << SUB) | (1L << REM) | (1L << EQ) | (1L << NOT_EQ) | (1L << LT) | (1L << LE) | (1L << GT) | (1L << GE) | (1L << CONCAT) | (1L << AND) | (1L << OR) | (1L << VAR_ID) | (1L << ID))) != 0)) {
				{
				{
				setState(234);
				_la = _input.LA(1);
				if ( _la <= 0 || (_la==DIV) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				}
				setState(239);
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
		case 1:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 25);
		case 1:
			return precpred(_ctx, 15);
		case 2:
			return precpred(_ctx, 14);
		case 3:
			return precpred(_ctx, 13);
		case 4:
			return precpred(_ctx, 12);
		case 5:
			return precpred(_ctx, 11);
		case 6:
			return precpred(_ctx, 10);
		case 7:
			return precpred(_ctx, 9);
		case 8:
			return precpred(_ctx, 7);
		case 9:
			return precpred(_ctx, 24);
		case 10:
			return precpred(_ctx, 23);
		case 11:
			return precpred(_ctx, 22);
		case 12:
			return precpred(_ctx, 8);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001*\u00f1\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u0001$\b"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u0001)\b\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u0001/\b\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u00015\b\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0003\u0001@\b\u0001\u0001\u0001\u0001"+
		"\u0001\u0003\u0001D\b\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0003\u0001J\b\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0003\u0001X\b\u0001\u0005\u0001Z\b\u0001"+
		"\n\u0001\f\u0001]\t\u0001\u0003\u0001_\b\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u0001f\b\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0003\u0001\u008e\b\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u0001\u0096\b\u0001\u0005\u0001"+
		"\u0098\b\u0001\n\u0001\f\u0001\u009b\t\u0001\u0001\u0002\u0001\u0002\u0003"+
		"\u0002\u009f\b\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0003\u0002\u00a6\b\u0002\u0001\u0002\u0001\u0002\u0005\u0002\u00aa"+
		"\b\u0002\n\u0002\f\u0002\u00ad\t\u0002\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0005\u0003\u00b2\b\u0003\n\u0003\f\u0003\u00b5\t\u0003\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0005\u0004\u00bb\b\u0004\n\u0004\f\u0004"+
		"\u00be\t\u0004\u0005\u0004\u00c0\b\u0004\n\u0004\f\u0004\u00c3\t\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0005\u0005\u00cc\b\u0005\n\u0005\f\u0005\u00cf\t\u0005\u0001"+
		"\u0005\u0003\u0005\u00d2\b\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0003\b\u00dd"+
		"\b\b\u0001\t\u0001\t\u0001\t\u0005\t\u00e2\b\t\n\t\f\t\u00e5\t\t\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\u000b\u0005\u000b\u00ec\b\u000b\n\u000b"+
		"\f\u000b\u00ef\t\u000b\u0001\u000b\u0000\u0001\u0002\f\u0000\u0002\u0004"+
		"\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0000\u0005\u0001\u0000\n\u000b"+
		"\u0002\u0000\u001b\u001c\u001f\u001f\u0001\u0000\u001d\u001e\u0001\u0000"+
		" %\u0001\u0000\u001c\u001c\u011a\u0000\u0018\u0001\u0000\u0000\u0000\u0002"+
		"e\u0001\u0000\u0000\u0000\u0004\u009e\u0001\u0000\u0000\u0000\u0006\u00ae"+
		"\u0001\u0000\u0000\u0000\b\u00b6\u0001\u0000\u0000\u0000\n\u00c6\u0001"+
		"\u0000\u0000\u0000\f\u00d3\u0001\u0000\u0000\u0000\u000e\u00d6\u0001\u0000"+
		"\u0000\u0000\u0010\u00dc\u0001\u0000\u0000\u0000\u0012\u00de\u0001\u0000"+
		"\u0000\u0000\u0014\u00e6\u0001\u0000\u0000\u0000\u0016\u00ed\u0001\u0000"+
		"\u0000\u0000\u0018\u0019\u0003\u0002\u0001\u0000\u0019\u001a\u0005\u0000"+
		"\u0000\u0001\u001a\u0001\u0001\u0000\u0000\u0000\u001b\u001c\u0006\u0001"+
		"\uffff\uffff\u0000\u001cf\u0005*\u0000\u0000\u001df\u0005\u001b\u0000"+
		"\u0000\u001ef\u0005\u0014\u0000\u0000\u001ff\u0005\u0012\u0000\u0000 "+
		"f\u0005\u0013\u0000\u0000!#\u0005\u000e\u0000\u0000\"$\u0003\u0012\t\u0000"+
		"#\"\u0001\u0000\u0000\u0000#$\u0001\u0000\u0000\u0000$%\u0001\u0000\u0000"+
		"\u0000%f\u0005\u000f\u0000\u0000&(\u0005\u0010\u0000\u0000\')\u0003\u0004"+
		"\u0002\u0000(\'\u0001\u0000\u0000\u0000()\u0001\u0000\u0000\u0000)*\u0001"+
		"\u0000\u0000\u0000*f\u0005\u0011\u0000\u0000+.\u0005)\u0000\u0000,/\u0003"+
		"\f\u0006\u0000-/\u0003\n\u0005\u0000.,\u0001\u0000\u0000\u0000.-\u0001"+
		"\u0000\u0000\u0000/f\u0001\u0000\u0000\u000001\u0005\u0016\u0000\u0000"+
		"12\u0003\b\u0004\u000024\u0005\u0010\u0000\u000035\u0003\u0006\u0003\u0000"+
		"43\u0001\u0000\u0000\u000045\u0001\u0000\u0000\u000056\u0001\u0000\u0000"+
		"\u000067\u0005\u0011\u0000\u00007f\u0001\u0000\u0000\u000089\u0005)\u0000"+
		"\u00009C\u0005\u001a\u0000\u0000:D\u0003\u0002\u0001\u0000;<\u0005\u0016"+
		"\u0000\u0000<=\u0003\b\u0004\u0000=?\u0005\u0010\u0000\u0000>@\u0003\u0006"+
		"\u0003\u0000?>\u0001\u0000\u0000\u0000?@\u0001\u0000\u0000\u0000@A\u0001"+
		"\u0000\u0000\u0000AB\u0005\u0011\u0000\u0000BD\u0001\u0000\u0000\u0000"+
		"C:\u0001\u0000\u0000\u0000C;\u0001\u0000\u0000\u0000Df\u0001\u0000\u0000"+
		"\u0000EF\u0005\u0016\u0000\u0000FG\u0003\b\u0004\u0000GI\u0005\u0010\u0000"+
		"\u0000HJ\u0003\u0006\u0003\u0000IH\u0001\u0000\u0000\u0000IJ\u0001\u0000"+
		"\u0000\u0000JK\u0001\u0000\u0000\u0000KL\u0005\u0011\u0000\u0000LM\u0001"+
		"\u0000\u0000\u0000MN\u0003\n\u0005\u0000Nf\u0001\u0000\u0000\u0000Of\u0007"+
		"\u0000\u0000\u0000PQ\u0005\u001e\u0000\u0000Qf\u0003\u0002\u0001\u0010"+
		"Rf\u0003\u0014\n\u0000S^\u0005\u0005\u0000\u0000T[\u0003\u0002\u0001\u0000"+
		"UW\u0005\u0006\u0000\u0000VX\u0003\u0002\u0001\u0000WV\u0001\u0000\u0000"+
		"\u0000WX\u0001\u0000\u0000\u0000XZ\u0001\u0000\u0000\u0000YU\u0001\u0000"+
		"\u0000\u0000Z]\u0001\u0000\u0000\u0000[Y\u0001\u0000\u0000\u0000[\\\u0001"+
		"\u0000\u0000\u0000\\_\u0001\u0000\u0000\u0000][\u0001\u0000\u0000\u0000"+
		"^T\u0001\u0000\u0000\u0000^_\u0001\u0000\u0000\u0000_`\u0001\u0000\u0000"+
		"\u0000`f\u0005\u0007\u0000\u0000af\u0005)\u0000\u0000bf\u0005\u0015\u0000"+
		"\u0000cf\u0005\f\u0000\u0000df\u0005\r\u0000\u0000e\u001b\u0001\u0000"+
		"\u0000\u0000e\u001d\u0001\u0000\u0000\u0000e\u001e\u0001\u0000\u0000\u0000"+
		"e\u001f\u0001\u0000\u0000\u0000e \u0001\u0000\u0000\u0000e!\u0001\u0000"+
		"\u0000\u0000e&\u0001\u0000\u0000\u0000e+\u0001\u0000\u0000\u0000e0\u0001"+
		"\u0000\u0000\u0000e8\u0001\u0000\u0000\u0000eE\u0001\u0000\u0000\u0000"+
		"eO\u0001\u0000\u0000\u0000eP\u0001\u0000\u0000\u0000eR\u0001\u0000\u0000"+
		"\u0000eS\u0001\u0000\u0000\u0000ea\u0001\u0000\u0000\u0000eb\u0001\u0000"+
		"\u0000\u0000ec\u0001\u0000\u0000\u0000ed\u0001\u0000\u0000\u0000f\u0099"+
		"\u0001\u0000\u0000\u0000gh\n\u0019\u0000\u0000hi\u0005\u0001\u0000\u0000"+
		"i\u0098\u0003\u0002\u0001\u001ajk\n\u000f\u0000\u0000kl\u0007\u0001\u0000"+
		"\u0000l\u0098\u0003\u0002\u0001\u0010mn\n\u000e\u0000\u0000no\u0007\u0002"+
		"\u0000\u0000o\u0098\u0003\u0002\u0001\u000fpq\n\r\u0000\u0000qr\u0005"+
		"&\u0000\u0000r\u0098\u0003\u0002\u0001\u000est\n\f\u0000\u0000tu\u0007"+
		"\u0003\u0000\u0000u\u0098\u0003\u0002\u0001\rvw\n\u000b\u0000\u0000wx"+
		"\u0005\u0002\u0000\u0000x\u0098\u0003\u0002\u0001\fyz\n\n\u0000\u0000"+
		"z{\u0005\'\u0000\u0000{\u0098\u0003\u0002\u0001\u000b|}\n\t\u0000\u0000"+
		"}~\u0005(\u0000\u0000~\u0098\u0003\u0002\u0001\n\u007f\u0080\n\u0007\u0000"+
		"\u0000\u0080\u0081\u0005\u0019\u0000\u0000\u0081\u0098\u0003\u0002\u0001"+
		"\b\u0082\u0083\n\u0018\u0000\u0000\u0083\u0084\u0005\u000e\u0000\u0000"+
		"\u0084\u0098\u0005\u000f\u0000\u0000\u0085\u0086\n\u0017\u0000\u0000\u0086"+
		"\u0087\u0005\u000e\u0000\u0000\u0087\u0088\u0003\u0002\u0001\u0000\u0088"+
		"\u0089\u0005\u000f\u0000\u0000\u0089\u0098\u0001\u0000\u0000\u0000\u008a"+
		"\u008b\n\u0016\u0000\u0000\u008b\u008d\u0005\u0010\u0000\u0000\u008c\u008e"+
		"\u0003\u0004\u0002\u0000\u008d\u008c\u0001\u0000\u0000\u0000\u008d\u008e"+
		"\u0001\u0000\u0000\u0000\u008e\u008f\u0001\u0000\u0000\u0000\u008f\u0098"+
		"\u0005\u0011\u0000\u0000\u0090\u0091\n\b\u0000\u0000\u0091\u0092\u0005"+
		"\u0003\u0000\u0000\u0092\u0095\u0003\u0002\u0001\u0000\u0093\u0094\u0005"+
		"\u0004\u0000\u0000\u0094\u0096\u0003\u0002\u0001\u0000\u0095\u0093\u0001"+
		"\u0000\u0000\u0000\u0095\u0096\u0001\u0000\u0000\u0000\u0096\u0098\u0001"+
		"\u0000\u0000\u0000\u0097g\u0001\u0000\u0000\u0000\u0097j\u0001\u0000\u0000"+
		"\u0000\u0097m\u0001\u0000\u0000\u0000\u0097p\u0001\u0000\u0000\u0000\u0097"+
		"s\u0001\u0000\u0000\u0000\u0097v\u0001\u0000\u0000\u0000\u0097y\u0001"+
		"\u0000\u0000\u0000\u0097|\u0001\u0000\u0000\u0000\u0097\u007f\u0001\u0000"+
		"\u0000\u0000\u0097\u0082\u0001\u0000\u0000\u0000\u0097\u0085\u0001\u0000"+
		"\u0000\u0000\u0097\u008a\u0001\u0000\u0000\u0000\u0097\u0090\u0001\u0000"+
		"\u0000\u0000\u0098\u009b\u0001\u0000\u0000\u0000\u0099\u0097\u0001\u0000"+
		"\u0000\u0000\u0099\u009a\u0001\u0000\u0000\u0000\u009a\u0003\u0001\u0000"+
		"\u0000\u0000\u009b\u0099\u0001\u0000\u0000\u0000\u009c\u009f\u0005\f\u0000"+
		"\u0000\u009d\u009f\u0003\u0002\u0001\u0000\u009e\u009c\u0001\u0000\u0000"+
		"\u0000\u009e\u009d\u0001\u0000\u0000\u0000\u009f\u00a0\u0001\u0000\u0000"+
		"\u0000\u00a0\u00a1\u0005\u0004\u0000\u0000\u00a1\u00ab\u0003\u0002\u0001"+
		"\u0000\u00a2\u00a5\u0005\b\u0000\u0000\u00a3\u00a6\u0005\f\u0000\u0000"+
		"\u00a4\u00a6\u0003\u0002\u0001\u0000\u00a5\u00a3\u0001\u0000\u0000\u0000"+
		"\u00a5\u00a4\u0001\u0000\u0000\u0000\u00a6\u00a7\u0001\u0000\u0000\u0000"+
		"\u00a7\u00a8\u0005\u0004\u0000\u0000\u00a8\u00aa\u0003\u0002\u0001\u0000"+
		"\u00a9\u00a2\u0001\u0000\u0000\u0000\u00aa\u00ad\u0001\u0000\u0000\u0000"+
		"\u00ab\u00a9\u0001\u0000\u0000\u0000\u00ab\u00ac\u0001\u0000\u0000\u0000"+
		"\u00ac\u0005\u0001\u0000\u0000\u0000\u00ad\u00ab\u0001\u0000\u0000\u0000"+
		"\u00ae\u00b3\u0003\u0002\u0001\u0000\u00af\u00b0\u0005\b\u0000\u0000\u00b0"+
		"\u00b2\u0003\u0002\u0001\u0000\u00b1\u00af\u0001\u0000\u0000\u0000\u00b2"+
		"\u00b5\u0001\u0000\u0000\u0000\u00b3\u00b1\u0001\u0000\u0000\u0000\u00b3"+
		"\u00b4\u0001\u0000\u0000\u0000\u00b4\u0007\u0001\u0000\u0000\u0000\u00b5"+
		"\u00b3\u0001\u0000\u0000\u0000\u00b6\u00c1\u0005\u0005\u0000\u0000\u00b7"+
		"\u00bc\u0005)\u0000\u0000\u00b8\u00b9\u0005\b\u0000\u0000\u00b9\u00bb"+
		"\u0005)\u0000\u0000\u00ba\u00b8\u0001\u0000\u0000\u0000\u00bb\u00be\u0001"+
		"\u0000\u0000\u0000\u00bc\u00ba\u0001\u0000\u0000\u0000\u00bc\u00bd\u0001"+
		"\u0000\u0000\u0000\u00bd\u00c0\u0001\u0000\u0000\u0000\u00be\u00bc\u0001"+
		"\u0000\u0000\u0000\u00bf\u00b7\u0001\u0000\u0000\u0000\u00c0\u00c3\u0001"+
		"\u0000\u0000\u0000\u00c1\u00bf\u0001\u0000\u0000\u0000\u00c1\u00c2\u0001"+
		"\u0000\u0000\u0000\u00c2\u00c4\u0001\u0000\u0000\u0000\u00c3\u00c1\u0001"+
		"\u0000\u0000\u0000\u00c4\u00c5\u0005\u0007\u0000\u0000\u00c5\t\u0001\u0000"+
		"\u0000\u0000\u00c6\u00c7\u0005\u0005\u0000\u0000\u00c7\u00c8\u0003\u0006"+
		"\u0003\u0000\u00c8\u00d1\u0005\u0007\u0000\u0000\u00c9\u00ca\u0005\b\u0000"+
		"\u0000\u00ca\u00cc\u0003\u0010\b\u0000\u00cb\u00c9\u0001\u0000\u0000\u0000"+
		"\u00cc\u00cf\u0001\u0000\u0000\u0000\u00cd\u00cb\u0001\u0000\u0000\u0000"+
		"\u00cd\u00ce\u0001\u0000\u0000\u0000\u00ce\u00d0\u0001\u0000\u0000\u0000"+
		"\u00cf\u00cd\u0001\u0000\u0000\u0000\u00d0\u00d2\u0005\u0007\u0000\u0000"+
		"\u00d1\u00cd\u0001\u0000\u0000\u0000\u00d1\u00d2\u0001\u0000\u0000\u0000"+
		"\u00d2\u000b\u0001\u0000\u0000\u0000\u00d3\u00d4\u0005\u0005\u0000\u0000"+
		"\u00d4\u00d5\u0005\u0007\u0000\u0000\u00d5\r\u0001\u0000\u0000\u0000\u00d6"+
		"\u00d7\u0003\u0002\u0001\u0000\u00d7\u00d8\u0005\t\u0000\u0000\u00d8\u00d9"+
		"\u0003\u0002\u0001\u0000\u00d9\u000f\u0001\u0000\u0000\u0000\u00da\u00dd"+
		"\u0003\u000e\u0007\u0000\u00db\u00dd\u0003\u0002\u0001\u0000\u00dc\u00da"+
		"\u0001\u0000\u0000\u0000\u00dc\u00db\u0001\u0000\u0000\u0000\u00dd\u0011"+
		"\u0001\u0000\u0000\u0000\u00de\u00e3\u0003\u0010\b\u0000\u00df\u00e0\u0005"+
		"\b\u0000\u0000\u00e0\u00e2\u0003\u0010\b\u0000\u00e1\u00df\u0001\u0000"+
		"\u0000\u0000\u00e2\u00e5\u0001\u0000\u0000\u0000\u00e3\u00e1\u0001\u0000"+
		"\u0000\u0000\u00e3\u00e4\u0001\u0000\u0000\u0000\u00e4\u0013\u0001\u0000"+
		"\u0000\u0000\u00e5\u00e3\u0001\u0000\u0000\u0000\u00e6\u00e7\u0005\u001c"+
		"\u0000\u0000\u00e7\u00e8\u0003\u0016\u000b\u0000\u00e8\u00e9\u0005\u001c"+
		"\u0000\u0000\u00e9\u0015\u0001\u0000\u0000\u0000\u00ea\u00ec\b\u0004\u0000"+
		"\u0000\u00eb\u00ea\u0001\u0000\u0000\u0000\u00ec\u00ef\u0001\u0000\u0000"+
		"\u0000\u00ed\u00eb\u0001\u0000\u0000\u0000\u00ed\u00ee\u0001\u0000\u0000"+
		"\u0000\u00ee\u0017\u0001\u0000\u0000\u0000\u00ef\u00ed\u0001\u0000\u0000"+
		"\u0000\u001a#(.4?CIW[^e\u008d\u0095\u0097\u0099\u009e\u00a5\u00ab\u00b3"+
		"\u00bc\u00c1\u00cd\u00d1\u00dc\u00e3\u00ed";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}