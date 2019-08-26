// Generated from com/api/jsonata4java/expressions/generated/MappingExpression.g4 by ANTLR 4.7
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
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, TRUE=9, 
		FALSE=10, STRING=11, AND=12, OR=13, IN=14, NULL=15, ARR_OPEN=16, ARR_CLOSE=17, 
		OBJ_OPEN=18, OBJ_CLOSE=19, DOLLAR=20, ROOT=21, DESCEND=22, NUMBER=23, 
		FUNCTIONID=24, WS=25, COMMENT=26, CHAIN=27, ASSIGN=28, MUL=29, DIV=30, 
		ADD=31, SUB=32, REM=33, EQ=34, NOT_EQ=35, LT=36, LE=37, GT=38, GE=39, 
		CONCAT=40, EACH=41, SIFT=42, REDUCE=43, FILTER=44, MAP=45, VAR_ID=46, 
		ID=47;
	public static final int
		RULE_expr = 0, RULE_fieldList = 1, RULE_exprList = 2, RULE_varList = 3, 
		RULE_exprValues = 4, RULE_emptyValues = 5, RULE_seq = 6, RULE_exprOrSeq = 7, 
		RULE_exprOrSeqList = 8;
	public static final String[] ruleNames = {
		"expr", "fieldList", "exprList", "varList", "exprValues", "emptyValues", 
		"seq", "exprOrSeq", "exprOrSeqList"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'.'", "'('", "','", "')'", "'?'", "':'", "';'", "'..'", "'true'", 
		"'false'", null, "'and'", "'or'", "'in'", "'null'", "'['", "']'", "'{'", 
		"'}'", "'$'", "'$$'", "'**'", null, "'function'", null, null, "'~>'", 
		"':='", "'*'", "'/'", "'+'", "'-'", "'%'", "'='", "'!='", "'<'", "'<='", 
		"'>'", "'>='", "'&'", "'$each'", "'$sift'", "'$reduce'", "'$filter'", 
		"'$map'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, "TRUE", "FALSE", 
		"STRING", "AND", "OR", "IN", "NULL", "ARR_OPEN", "ARR_CLOSE", "OBJ_OPEN", 
		"OBJ_CLOSE", "DOLLAR", "ROOT", "DESCEND", "NUMBER", "FUNCTIONID", "WS", 
		"COMMENT", "CHAIN", "ASSIGN", "MUL", "DIV", "ADD", "SUB", "REM", "EQ", 
		"NOT_EQ", "LT", "LE", "GT", "GE", "CONCAT", "EACH", "SIFT", "REDUCE", 
		"FILTER", "MAP", "VAR_ID", "ID"
	};
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
	public static class Sift_functionContext extends ExprContext {
		public TerminalNode SIFT() { return getToken(MappingExpressionParser.SIFT, 0); }
		public List<ExprListContext> exprList() {
			return getRuleContexts(ExprListContext.class);
		}
		public ExprListContext exprList(int i) {
			return getRuleContext(ExprListContext.class,i);
		}
		public TerminalNode VAR_ID() { return getToken(MappingExpressionParser.VAR_ID, 0); }
		public TerminalNode FUNCTIONID() { return getToken(MappingExpressionParser.FUNCTIONID, 0); }
		public VarListContext varList() {
			return getRuleContext(VarListContext.class,0);
		}
		public Sift_functionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterSift_function(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitSift_function(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitSift_function(this);
			else return visitor.visitChildren(this);
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
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
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
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode ARR_OPEN() { return getToken(MappingExpressionParser.ARR_OPEN, 0); }
		public TerminalNode ARR_CLOSE() { return getToken(MappingExpressionParser.ARR_CLOSE, 0); }
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
	public static class Reduce_functionContext extends ExprContext {
		public TerminalNode REDUCE() { return getToken(MappingExpressionParser.REDUCE, 0); }
		public List<ExprListContext> exprList() {
			return getRuleContexts(ExprListContext.class);
		}
		public ExprListContext exprList(int i) {
			return getRuleContext(ExprListContext.class,i);
		}
		public TerminalNode VAR_ID() { return getToken(MappingExpressionParser.VAR_ID, 0); }
		public List<ExprOrSeqContext> exprOrSeq() {
			return getRuleContexts(ExprOrSeqContext.class);
		}
		public ExprOrSeqContext exprOrSeq(int i) {
			return getRuleContext(ExprOrSeqContext.class,i);
		}
		public TerminalNode FUNCTIONID() { return getToken(MappingExpressionParser.FUNCTIONID, 0); }
		public VarListContext varList() {
			return getRuleContext(VarListContext.class,0);
		}
		public Reduce_functionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterReduce_function(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitReduce_function(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitReduce_function(this);
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
	public static class Each_functionContext extends ExprContext {
		public TerminalNode EACH() { return getToken(MappingExpressionParser.EACH, 0); }
		public List<ExprListContext> exprList() {
			return getRuleContexts(ExprListContext.class);
		}
		public ExprListContext exprList(int i) {
			return getRuleContext(ExprListContext.class,i);
		}
		public TerminalNode VAR_ID() { return getToken(MappingExpressionParser.VAR_ID, 0); }
		public TerminalNode FUNCTIONID() { return getToken(MappingExpressionParser.FUNCTIONID, 0); }
		public VarListContext varList() {
			return getRuleContext(VarListContext.class,0);
		}
		public Each_functionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterEach_function(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitEach_function(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitEach_function(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Unary_opContext extends ExprContext {
		public Token op;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
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
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
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
	public static class Filter_functionContext extends ExprContext {
		public TerminalNode FILTER() { return getToken(MappingExpressionParser.FILTER, 0); }
		public List<ExprListContext> exprList() {
			return getRuleContexts(ExprListContext.class);
		}
		public ExprListContext exprList(int i) {
			return getRuleContext(ExprListContext.class,i);
		}
		public TerminalNode VAR_ID() { return getToken(MappingExpressionParser.VAR_ID, 0); }
		public TerminalNode FUNCTIONID() { return getToken(MappingExpressionParser.FUNCTIONID, 0); }
		public VarListContext varList() {
			return getRuleContext(VarListContext.class,0);
		}
		public Filter_functionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterFilter_function(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitFilter_function(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitFilter_function(this);
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
	public static class Map_functionContext extends ExprContext {
		public TerminalNode MAP() { return getToken(MappingExpressionParser.MAP, 0); }
		public List<ExprListContext> exprList() {
			return getRuleContexts(ExprListContext.class);
		}
		public ExprListContext exprList(int i) {
			return getRuleContext(ExprListContext.class,i);
		}
		public TerminalNode VAR_ID() { return getToken(MappingExpressionParser.VAR_ID, 0); }
		public TerminalNode FUNCTIONID() { return getToken(MappingExpressionParser.FUNCTIONID, 0); }
		public VarListContext varList() {
			return getRuleContext(VarListContext.class,0);
		}
		public Map_functionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).enterMap_function(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MappingExpressionListener ) ((MappingExpressionListener)listener).exitMap_function(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MappingExpressionVisitor ) return ((MappingExpressionVisitor<? extends T>)visitor).visitMap_function(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Field_valuesContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
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
			setState(198);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
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
				setState(23);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
				case 1:
					{
					setState(21);
					match(T__0);
					setState(22);
					expr(0);
					}
					break;
				}
				}
				break;
			case 3:
				{
				_localctx = new DescendantContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(25);
				match(DESCEND);
				setState(28);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
				case 1:
					{
					setState(26);
					match(T__0);
					setState(27);
					expr(0);
					}
					break;
				}
				}
				break;
			case 4:
				{
				_localctx = new Context_refContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(30);
				match(DOLLAR);
				setState(37);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__0:
					{
					{
					setState(31);
					match(T__0);
					setState(32);
					expr(0);
					}
					}
					break;
				case ARR_OPEN:
					{
					{
					setState(33);
					match(ARR_OPEN);
					setState(34);
					expr(0);
					setState(35);
					match(ARR_CLOSE);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			case 5:
				{
				_localctx = new Root_pathContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(39);
				match(ROOT);
				setState(40);
				match(T__0);
				setState(41);
				expr(31);
				}
				break;
			case 6:
				{
				_localctx = new Array_constructorContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(42);
				match(ARR_OPEN);
				setState(44);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << TRUE) | (1L << FALSE) | (1L << STRING) | (1L << NULL) | (1L << ARR_OPEN) | (1L << OBJ_OPEN) | (1L << DOLLAR) | (1L << ROOT) | (1L << DESCEND) | (1L << NUMBER) | (1L << FUNCTIONID) | (1L << MUL) | (1L << SUB) | (1L << EACH) | (1L << SIFT) | (1L << REDUCE) | (1L << FILTER) | (1L << MAP) | (1L << VAR_ID) | (1L << ID))) != 0)) {
					{
					setState(43);
					exprOrSeqList();
					}
				}

				setState(46);
				match(ARR_CLOSE);
				}
				break;
			case 7:
				{
				_localctx = new Object_constructorContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(47);
				match(OBJ_OPEN);
				setState(49);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==STRING) {
					{
					setState(48);
					fieldList();
					}
				}

				setState(51);
				match(OBJ_CLOSE);
				}
				break;
			case 8:
				{
				_localctx = new Function_callContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(52);
				match(VAR_ID);
				setState(55);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
				case 1:
					{
					setState(53);
					emptyValues();
					}
					break;
				case 2:
					{
					setState(54);
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
				setState(57);
				match(FUNCTIONID);
				setState(58);
				varList();
				setState(59);
				match(OBJ_OPEN);
				setState(61);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << TRUE) | (1L << FALSE) | (1L << STRING) | (1L << NULL) | (1L << ARR_OPEN) | (1L << OBJ_OPEN) | (1L << DOLLAR) | (1L << ROOT) | (1L << DESCEND) | (1L << NUMBER) | (1L << FUNCTIONID) | (1L << MUL) | (1L << SUB) | (1L << EACH) | (1L << SIFT) | (1L << REDUCE) | (1L << FILTER) | (1L << MAP) | (1L << VAR_ID) | (1L << ID))) != 0)) {
					{
					setState(60);
					exprList();
					}
				}

				setState(63);
				match(OBJ_CLOSE);
				}
				break;
			case 10:
				{
				_localctx = new Var_assignContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(65);
				match(VAR_ID);
				setState(66);
				match(ASSIGN);
				setState(76);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
				case 1:
					{
					setState(67);
					expr(0);
					}
					break;
				case 2:
					{
					{
					setState(68);
					match(FUNCTIONID);
					setState(69);
					varList();
					setState(70);
					match(OBJ_OPEN);
					setState(72);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << TRUE) | (1L << FALSE) | (1L << STRING) | (1L << NULL) | (1L << ARR_OPEN) | (1L << OBJ_OPEN) | (1L << DOLLAR) | (1L << ROOT) | (1L << DESCEND) | (1L << NUMBER) | (1L << FUNCTIONID) | (1L << MUL) | (1L << SUB) | (1L << EACH) | (1L << SIFT) | (1L << REDUCE) | (1L << FILTER) | (1L << MAP) | (1L << VAR_ID) | (1L << ID))) != 0)) {
						{
						setState(71);
						exprList();
						}
					}

					setState(74);
					match(OBJ_CLOSE);
					}
					}
					break;
				}
				}
				break;
			case 11:
				{
				_localctx = new Each_functionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(78);
				match(EACH);
				setState(79);
				match(T__1);
				setState(80);
				exprList();
				setState(81);
				match(T__2);
				setState(91);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case VAR_ID:
					{
					setState(82);
					match(VAR_ID);
					}
					break;
				case FUNCTIONID:
					{
					{
					setState(83);
					match(FUNCTIONID);
					setState(84);
					varList();
					setState(85);
					match(OBJ_OPEN);
					setState(87);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << TRUE) | (1L << FALSE) | (1L << STRING) | (1L << NULL) | (1L << ARR_OPEN) | (1L << OBJ_OPEN) | (1L << DOLLAR) | (1L << ROOT) | (1L << DESCEND) | (1L << NUMBER) | (1L << FUNCTIONID) | (1L << MUL) | (1L << SUB) | (1L << EACH) | (1L << SIFT) | (1L << REDUCE) | (1L << FILTER) | (1L << MAP) | (1L << VAR_ID) | (1L << ID))) != 0)) {
						{
						setState(86);
						exprList();
						}
					}

					setState(89);
					match(OBJ_CLOSE);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(93);
				match(T__3);
				}
				break;
			case 12:
				{
				_localctx = new Filter_functionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(95);
				match(FILTER);
				setState(96);
				match(T__1);
				setState(97);
				exprList();
				setState(98);
				match(T__2);
				setState(108);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case VAR_ID:
					{
					setState(99);
					match(VAR_ID);
					}
					break;
				case FUNCTIONID:
					{
					{
					setState(100);
					match(FUNCTIONID);
					setState(101);
					varList();
					setState(102);
					match(OBJ_OPEN);
					setState(104);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << TRUE) | (1L << FALSE) | (1L << STRING) | (1L << NULL) | (1L << ARR_OPEN) | (1L << OBJ_OPEN) | (1L << DOLLAR) | (1L << ROOT) | (1L << DESCEND) | (1L << NUMBER) | (1L << FUNCTIONID) | (1L << MUL) | (1L << SUB) | (1L << EACH) | (1L << SIFT) | (1L << REDUCE) | (1L << FILTER) | (1L << MAP) | (1L << VAR_ID) | (1L << ID))) != 0)) {
						{
						setState(103);
						exprList();
						}
					}

					setState(106);
					match(OBJ_CLOSE);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(110);
				match(T__3);
				}
				break;
			case 13:
				{
				_localctx = new Map_functionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(112);
				match(MAP);
				setState(113);
				match(T__1);
				setState(114);
				exprList();
				setState(115);
				match(T__2);
				setState(125);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case VAR_ID:
					{
					setState(116);
					match(VAR_ID);
					}
					break;
				case FUNCTIONID:
					{
					{
					setState(117);
					match(FUNCTIONID);
					setState(118);
					varList();
					setState(119);
					match(OBJ_OPEN);
					setState(121);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << TRUE) | (1L << FALSE) | (1L << STRING) | (1L << NULL) | (1L << ARR_OPEN) | (1L << OBJ_OPEN) | (1L << DOLLAR) | (1L << ROOT) | (1L << DESCEND) | (1L << NUMBER) | (1L << FUNCTIONID) | (1L << MUL) | (1L << SUB) | (1L << EACH) | (1L << SIFT) | (1L << REDUCE) | (1L << FILTER) | (1L << MAP) | (1L << VAR_ID) | (1L << ID))) != 0)) {
						{
						setState(120);
						exprList();
						}
					}

					setState(123);
					match(OBJ_CLOSE);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(127);
				match(T__3);
				}
				break;
			case 14:
				{
				_localctx = new Reduce_functionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(129);
				match(REDUCE);
				setState(130);
				match(T__1);
				setState(131);
				exprList();
				setState(132);
				match(T__2);
				setState(142);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case VAR_ID:
					{
					setState(133);
					match(VAR_ID);
					}
					break;
				case FUNCTIONID:
					{
					{
					setState(134);
					match(FUNCTIONID);
					setState(135);
					varList();
					setState(136);
					match(OBJ_OPEN);
					setState(138);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << TRUE) | (1L << FALSE) | (1L << STRING) | (1L << NULL) | (1L << ARR_OPEN) | (1L << OBJ_OPEN) | (1L << DOLLAR) | (1L << ROOT) | (1L << DESCEND) | (1L << NUMBER) | (1L << FUNCTIONID) | (1L << MUL) | (1L << SUB) | (1L << EACH) | (1L << SIFT) | (1L << REDUCE) | (1L << FILTER) | (1L << MAP) | (1L << VAR_ID) | (1L << ID))) != 0)) {
						{
						setState(137);
						exprList();
						}
					}

					setState(140);
					match(OBJ_CLOSE);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(148);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(144);
					match(T__2);
					setState(145);
					exprOrSeq();
					}
					}
					setState(150);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(151);
				match(T__3);
				}
				break;
			case 15:
				{
				_localctx = new Sift_functionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(153);
				match(SIFT);
				setState(154);
				match(T__1);
				setState(155);
				exprList();
				setState(156);
				match(T__2);
				setState(166);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case VAR_ID:
					{
					setState(157);
					match(VAR_ID);
					}
					break;
				case FUNCTIONID:
					{
					{
					setState(158);
					match(FUNCTIONID);
					setState(159);
					varList();
					setState(160);
					match(OBJ_OPEN);
					setState(162);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << TRUE) | (1L << FALSE) | (1L << STRING) | (1L << NULL) | (1L << ARR_OPEN) | (1L << OBJ_OPEN) | (1L << DOLLAR) | (1L << ROOT) | (1L << DESCEND) | (1L << NUMBER) | (1L << FUNCTIONID) | (1L << MUL) | (1L << SUB) | (1L << EACH) | (1L << SIFT) | (1L << REDUCE) | (1L << FILTER) | (1L << MAP) | (1L << VAR_ID) | (1L << ID))) != 0)) {
						{
						setState(161);
						exprList();
						}
					}

					setState(164);
					match(OBJ_CLOSE);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(168);
				match(T__3);
				}
				break;
			case 16:
				{
				_localctx = new Function_execContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				{
				setState(170);
				match(FUNCTIONID);
				setState(171);
				varList();
				setState(172);
				match(OBJ_OPEN);
				setState(174);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << TRUE) | (1L << FALSE) | (1L << STRING) | (1L << NULL) | (1L << ARR_OPEN) | (1L << OBJ_OPEN) | (1L << DOLLAR) | (1L << ROOT) | (1L << DESCEND) | (1L << NUMBER) | (1L << FUNCTIONID) | (1L << MUL) | (1L << SUB) | (1L << EACH) | (1L << SIFT) | (1L << REDUCE) | (1L << FILTER) | (1L << MAP) | (1L << VAR_ID) | (1L << ID))) != 0)) {
					{
					setState(173);
					exprList();
					}
				}

				setState(176);
				match(OBJ_CLOSE);
				}
				setState(178);
				exprValues();
				}
				break;
			case 17:
				{
				_localctx = new BooleanContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(180);
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
			case 18:
				{
				_localctx = new Unary_opContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(181);
				((Unary_opContext)_localctx).op = match(SUB);
				setState(182);
				expr(15);
				}
				break;
			case 19:
				{
				_localctx = new ParensContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(183);
				match(T__1);
				setState(184);
				expr(0);
				setState(189);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__6) {
					{
					{
					setState(185);
					match(T__6);
					setState(186);
					expr(0);
					}
					}
					setState(191);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(192);
				match(T__3);
				}
				break;
			case 20:
				{
				_localctx = new Var_recallContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(194);
				match(VAR_ID);
				}
				break;
			case 21:
				{
				_localctx = new NumberContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(195);
				match(NUMBER);
				}
				break;
			case 22:
				{
				_localctx = new StringContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(196);
				match(STRING);
				}
				break;
			case 23:
				{
				_localctx = new NullContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(197);
				match(NULL);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(243);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(241);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
					case 1:
						{
						_localctx = new PathContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(200);
						if (!(precpred(_ctx, 30))) throw new FailedPredicateException(this, "precpred(_ctx, 30)");
						setState(201);
						match(T__0);
						setState(202);
						expr(31);
						}
						break;
					case 2:
						{
						_localctx = new Muldiv_opContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(203);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(204);
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
						setState(205);
						expr(15);
						}
						break;
					case 3:
						{
						_localctx = new Addsub_opContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(206);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(207);
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
						setState(208);
						expr(14);
						}
						break;
					case 4:
						{
						_localctx = new Concat_opContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(209);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(210);
						match(CONCAT);
						setState(211);
						expr(13);
						}
						break;
					case 5:
						{
						_localctx = new Comp_opContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(212);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(213);
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
						setState(214);
						expr(12);
						}
						break;
					case 6:
						{
						_localctx = new MembershipContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(215);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(216);
						match(IN);
						setState(217);
						expr(11);
						}
						break;
					case 7:
						{
						_localctx = new LogandContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(218);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(219);
						match(AND);
						setState(220);
						expr(10);
						}
						break;
					case 8:
						{
						_localctx = new LogorContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(221);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(222);
						match(OR);
						setState(223);
						expr(9);
						}
						break;
					case 9:
						{
						_localctx = new ConditionalContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(224);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(225);
						match(T__4);
						setState(226);
						expr(0);
						setState(227);
						match(T__5);
						setState(228);
						expr(8);
						}
						break;
					case 10:
						{
						_localctx = new Fct_chainContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(230);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(231);
						match(CHAIN);
						setState(232);
						expr(7);
						}
						break;
					case 11:
						{
						_localctx = new To_arrayContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(233);
						if (!(precpred(_ctx, 29))) throw new FailedPredicateException(this, "precpred(_ctx, 29)");
						setState(234);
						match(ARR_OPEN);
						setState(235);
						match(ARR_CLOSE);
						}
						break;
					case 12:
						{
						_localctx = new ArrayContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(236);
						if (!(precpred(_ctx, 28))) throw new FailedPredicateException(this, "precpred(_ctx, 28)");
						setState(237);
						match(ARR_OPEN);
						setState(238);
						expr(0);
						setState(239);
						match(ARR_CLOSE);
						}
						break;
					}
					} 
				}
				setState(245);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
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
		public List<TerminalNode> STRING() { return getTokens(MappingExpressionParser.STRING); }
		public TerminalNode STRING(int i) {
			return getToken(MappingExpressionParser.STRING, i);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
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
			setState(246);
			match(STRING);
			setState(247);
			match(T__5);
			setState(248);
			expr(0);
			setState(255);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(249);
				match(T__2);
				setState(250);
				match(STRING);
				setState(251);
				match(T__5);
				setState(252);
				expr(0);
				}
				}
				setState(257);
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
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(258);
			expr(0);
			setState(263);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(259);
					match(T__2);
					setState(260);
					expr(0);
					}
					} 
				}
				setState(265);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
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
			setState(266);
			match(T__1);
			setState(277);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==VAR_ID) {
				{
				{
				setState(267);
				match(VAR_ID);
				setState(272);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(268);
					match(T__2);
					setState(269);
					match(VAR_ID);
					}
					}
					setState(274);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				}
				setState(279);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(280);
			match(T__3);
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
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(282);
			match(T__1);
			setState(283);
			exprList();
			setState(284);
			match(T__3);
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
			setState(286);
			match(T__1);
			setState(287);
			match(T__3);
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
			setState(289);
			expr(0);
			setState(290);
			match(T__7);
			setState(291);
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
			setState(295);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(293);
				seq();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(294);
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
			setState(297);
			exprOrSeq();
			setState(302);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(298);
				match(T__2);
				setState(299);
				exprOrSeq();
				}
				}
				setState(304);
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
			return precpred(_ctx, 30);
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
			return precpred(_ctx, 7);
		case 9:
			return precpred(_ctx, 6);
		case 10:
			return precpred(_ctx, 29);
		case 11:
			return precpred(_ctx, 28);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\61\u0134\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\3\2\3"+
		"\2\3\2\3\2\3\2\5\2\32\n\2\3\2\3\2\3\2\5\2\37\n\2\3\2\3\2\3\2\3\2\3\2\3"+
		"\2\3\2\5\2(\n\2\3\2\3\2\3\2\3\2\3\2\5\2/\n\2\3\2\3\2\3\2\5\2\64\n\2\3"+
		"\2\3\2\3\2\3\2\5\2:\n\2\3\2\3\2\3\2\3\2\5\2@\n\2\3\2\3\2\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\3\2\5\2K\n\2\3\2\3\2\5\2O\n\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"+
		"\2\3\2\5\2Z\n\2\3\2\3\2\5\2^\n\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"+
		"\2\3\2\5\2k\n\2\3\2\3\2\5\2o\n\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"+
		"\2\3\2\5\2|\n\2\3\2\3\2\5\2\u0080\n\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2"+
		"\3\2\3\2\3\2\5\2\u008d\n\2\3\2\3\2\5\2\u0091\n\2\3\2\3\2\7\2\u0095\n\2"+
		"\f\2\16\2\u0098\13\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\5\2\u00a5"+
		"\n\2\3\2\3\2\5\2\u00a9\n\2\3\2\3\2\3\2\3\2\3\2\3\2\5\2\u00b1\n\2\3\2\3"+
		"\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\7\2\u00be\n\2\f\2\16\2\u00c1\13"+
		"\2\3\2\3\2\3\2\3\2\3\2\3\2\5\2\u00c9\n\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2"+
		"\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\7\2\u00f4"+
		"\n\2\f\2\16\2\u00f7\13\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\7\3\u0100\n\3\f\3"+
		"\16\3\u0103\13\3\3\4\3\4\3\4\7\4\u0108\n\4\f\4\16\4\u010b\13\4\3\5\3\5"+
		"\3\5\3\5\7\5\u0111\n\5\f\5\16\5\u0114\13\5\7\5\u0116\n\5\f\5\16\5\u0119"+
		"\13\5\3\5\3\5\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\t\3\t\5\t"+
		"\u012a\n\t\3\n\3\n\3\n\7\n\u012f\n\n\f\n\16\n\u0132\13\n\3\n\2\3\2\13"+
		"\2\4\6\b\n\f\16\20\22\2\6\3\2\13\f\4\2\37 ##\3\2!\"\3\2$)\2\u0168\2\u00c8"+
		"\3\2\2\2\4\u00f8\3\2\2\2\6\u0104\3\2\2\2\b\u010c\3\2\2\2\n\u011c\3\2\2"+
		"\2\f\u0120\3\2\2\2\16\u0123\3\2\2\2\20\u0129\3\2\2\2\22\u012b\3\2\2\2"+
		"\24\25\b\2\1\2\25\u00c9\7\61\2\2\26\31\7\37\2\2\27\30\7\3\2\2\30\32\5"+
		"\2\2\2\31\27\3\2\2\2\31\32\3\2\2\2\32\u00c9\3\2\2\2\33\36\7\30\2\2\34"+
		"\35\7\3\2\2\35\37\5\2\2\2\36\34\3\2\2\2\36\37\3\2\2\2\37\u00c9\3\2\2\2"+
		" \'\7\26\2\2!\"\7\3\2\2\"(\5\2\2\2#$\7\22\2\2$%\5\2\2\2%&\7\23\2\2&(\3"+
		"\2\2\2\'!\3\2\2\2\'#\3\2\2\2(\u00c9\3\2\2\2)*\7\27\2\2*+\7\3\2\2+\u00c9"+
		"\5\2\2!,.\7\22\2\2-/\5\22\n\2.-\3\2\2\2./\3\2\2\2/\60\3\2\2\2\60\u00c9"+
		"\7\23\2\2\61\63\7\24\2\2\62\64\5\4\3\2\63\62\3\2\2\2\63\64\3\2\2\2\64"+
		"\65\3\2\2\2\65\u00c9\7\25\2\2\669\7\60\2\2\67:\5\f\7\28:\5\n\6\29\67\3"+
		"\2\2\298\3\2\2\2:\u00c9\3\2\2\2;<\7\32\2\2<=\5\b\5\2=?\7\24\2\2>@\5\6"+
		"\4\2?>\3\2\2\2?@\3\2\2\2@A\3\2\2\2AB\7\25\2\2B\u00c9\3\2\2\2CD\7\60\2"+
		"\2DN\7\36\2\2EO\5\2\2\2FG\7\32\2\2GH\5\b\5\2HJ\7\24\2\2IK\5\6\4\2JI\3"+
		"\2\2\2JK\3\2\2\2KL\3\2\2\2LM\7\25\2\2MO\3\2\2\2NE\3\2\2\2NF\3\2\2\2O\u00c9"+
		"\3\2\2\2PQ\7+\2\2QR\7\4\2\2RS\5\6\4\2S]\7\5\2\2T^\7\60\2\2UV\7\32\2\2"+
		"VW\5\b\5\2WY\7\24\2\2XZ\5\6\4\2YX\3\2\2\2YZ\3\2\2\2Z[\3\2\2\2[\\\7\25"+
		"\2\2\\^\3\2\2\2]T\3\2\2\2]U\3\2\2\2^_\3\2\2\2_`\7\6\2\2`\u00c9\3\2\2\2"+
		"ab\7.\2\2bc\7\4\2\2cd\5\6\4\2dn\7\5\2\2eo\7\60\2\2fg\7\32\2\2gh\5\b\5"+
		"\2hj\7\24\2\2ik\5\6\4\2ji\3\2\2\2jk\3\2\2\2kl\3\2\2\2lm\7\25\2\2mo\3\2"+
		"\2\2ne\3\2\2\2nf\3\2\2\2op\3\2\2\2pq\7\6\2\2q\u00c9\3\2\2\2rs\7/\2\2s"+
		"t\7\4\2\2tu\5\6\4\2u\177\7\5\2\2v\u0080\7\60\2\2wx\7\32\2\2xy\5\b\5\2"+
		"y{\7\24\2\2z|\5\6\4\2{z\3\2\2\2{|\3\2\2\2|}\3\2\2\2}~\7\25\2\2~\u0080"+
		"\3\2\2\2\177v\3\2\2\2\177w\3\2\2\2\u0080\u0081\3\2\2\2\u0081\u0082\7\6"+
		"\2\2\u0082\u00c9\3\2\2\2\u0083\u0084\7-\2\2\u0084\u0085\7\4\2\2\u0085"+
		"\u0086\5\6\4\2\u0086\u0090\7\5\2\2\u0087\u0091\7\60\2\2\u0088\u0089\7"+
		"\32\2\2\u0089\u008a\5\b\5\2\u008a\u008c\7\24\2\2\u008b\u008d\5\6\4\2\u008c"+
		"\u008b\3\2\2\2\u008c\u008d\3\2\2\2\u008d\u008e\3\2\2\2\u008e\u008f\7\25"+
		"\2\2\u008f\u0091\3\2\2\2\u0090\u0087\3\2\2\2\u0090\u0088\3\2\2\2\u0091"+
		"\u0096\3\2\2\2\u0092\u0093\7\5\2\2\u0093\u0095\5\20\t\2\u0094\u0092\3"+
		"\2\2\2\u0095\u0098\3\2\2\2\u0096\u0094\3\2\2\2\u0096\u0097\3\2\2\2\u0097"+
		"\u0099\3\2\2\2\u0098\u0096\3\2\2\2\u0099\u009a\7\6\2\2\u009a\u00c9\3\2"+
		"\2\2\u009b\u009c\7,\2\2\u009c\u009d\7\4\2\2\u009d\u009e\5\6\4\2\u009e"+
		"\u00a8\7\5\2\2\u009f\u00a9\7\60\2\2\u00a0\u00a1\7\32\2\2\u00a1\u00a2\5"+
		"\b\5\2\u00a2\u00a4\7\24\2\2\u00a3\u00a5\5\6\4\2\u00a4\u00a3\3\2\2\2\u00a4"+
		"\u00a5\3\2\2\2\u00a5\u00a6\3\2\2\2\u00a6\u00a7\7\25\2\2\u00a7\u00a9\3"+
		"\2\2\2\u00a8\u009f\3\2\2\2\u00a8\u00a0\3\2\2\2\u00a9\u00aa\3\2\2\2\u00aa"+
		"\u00ab\7\6\2\2\u00ab\u00c9\3\2\2\2\u00ac\u00ad\7\32\2\2\u00ad\u00ae\5"+
		"\b\5\2\u00ae\u00b0\7\24\2\2\u00af\u00b1\5\6\4\2\u00b0\u00af\3\2\2\2\u00b0"+
		"\u00b1\3\2\2\2\u00b1\u00b2\3\2\2\2\u00b2\u00b3\7\25\2\2\u00b3\u00b4\3"+
		"\2\2\2\u00b4\u00b5\5\n\6\2\u00b5\u00c9\3\2\2\2\u00b6\u00c9\t\2\2\2\u00b7"+
		"\u00b8\7\"\2\2\u00b8\u00c9\5\2\2\21\u00b9\u00ba\7\4\2\2\u00ba\u00bf\5"+
		"\2\2\2\u00bb\u00bc\7\t\2\2\u00bc\u00be\5\2\2\2\u00bd\u00bb\3\2\2\2\u00be"+
		"\u00c1\3\2\2\2\u00bf\u00bd\3\2\2\2\u00bf\u00c0\3\2\2\2\u00c0\u00c2\3\2"+
		"\2\2\u00c1\u00bf\3\2\2\2\u00c2\u00c3\7\6\2\2\u00c3\u00c9\3\2\2\2\u00c4"+
		"\u00c9\7\60\2\2\u00c5\u00c9\7\31\2\2\u00c6\u00c9\7\r\2\2\u00c7\u00c9\7"+
		"\21\2\2\u00c8\24\3\2\2\2\u00c8\26\3\2\2\2\u00c8\33\3\2\2\2\u00c8 \3\2"+
		"\2\2\u00c8)\3\2\2\2\u00c8,\3\2\2\2\u00c8\61\3\2\2\2\u00c8\66\3\2\2\2\u00c8"+
		";\3\2\2\2\u00c8C\3\2\2\2\u00c8P\3\2\2\2\u00c8a\3\2\2\2\u00c8r\3\2\2\2"+
		"\u00c8\u0083\3\2\2\2\u00c8\u009b\3\2\2\2\u00c8\u00ac\3\2\2\2\u00c8\u00b6"+
		"\3\2\2\2\u00c8\u00b7\3\2\2\2\u00c8\u00b9\3\2\2\2\u00c8\u00c4\3\2\2\2\u00c8"+
		"\u00c5\3\2\2\2\u00c8\u00c6\3\2\2\2\u00c8\u00c7\3\2\2\2\u00c9\u00f5\3\2"+
		"\2\2\u00ca\u00cb\f \2\2\u00cb\u00cc\7\3\2\2\u00cc\u00f4\5\2\2!\u00cd\u00ce"+
		"\f\20\2\2\u00ce\u00cf\t\3\2\2\u00cf\u00f4\5\2\2\21\u00d0\u00d1\f\17\2"+
		"\2\u00d1\u00d2\t\4\2\2\u00d2\u00f4\5\2\2\20\u00d3\u00d4\f\16\2\2\u00d4"+
		"\u00d5\7*\2\2\u00d5\u00f4\5\2\2\17\u00d6\u00d7\f\r\2\2\u00d7\u00d8\t\5"+
		"\2\2\u00d8\u00f4\5\2\2\16\u00d9\u00da\f\f\2\2\u00da\u00db\7\20\2\2\u00db"+
		"\u00f4\5\2\2\r\u00dc\u00dd\f\13\2\2\u00dd\u00de\7\16\2\2\u00de\u00f4\5"+
		"\2\2\f\u00df\u00e0\f\n\2\2\u00e0\u00e1\7\17\2\2\u00e1\u00f4\5\2\2\13\u00e2"+
		"\u00e3\f\t\2\2\u00e3\u00e4\7\7\2\2\u00e4\u00e5\5\2\2\2\u00e5\u00e6\7\b"+
		"\2\2\u00e6\u00e7\5\2\2\n\u00e7\u00f4\3\2\2\2\u00e8\u00e9\f\b\2\2\u00e9"+
		"\u00ea\7\35\2\2\u00ea\u00f4\5\2\2\t\u00eb\u00ec\f\37\2\2\u00ec\u00ed\7"+
		"\22\2\2\u00ed\u00f4\7\23\2\2\u00ee\u00ef\f\36\2\2\u00ef\u00f0\7\22\2\2"+
		"\u00f0\u00f1\5\2\2\2\u00f1\u00f2\7\23\2\2\u00f2\u00f4\3\2\2\2\u00f3\u00ca"+
		"\3\2\2\2\u00f3\u00cd\3\2\2\2\u00f3\u00d0\3\2\2\2\u00f3\u00d3\3\2\2\2\u00f3"+
		"\u00d6\3\2\2\2\u00f3\u00d9\3\2\2\2\u00f3\u00dc\3\2\2\2\u00f3\u00df\3\2"+
		"\2\2\u00f3\u00e2\3\2\2\2\u00f3\u00e8\3\2\2\2\u00f3\u00eb\3\2\2\2\u00f3"+
		"\u00ee\3\2\2\2\u00f4\u00f7\3\2\2\2\u00f5\u00f3\3\2\2\2\u00f5\u00f6\3\2"+
		"\2\2\u00f6\3\3\2\2\2\u00f7\u00f5\3\2\2\2\u00f8\u00f9\7\r\2\2\u00f9\u00fa"+
		"\7\b\2\2\u00fa\u0101\5\2\2\2\u00fb\u00fc\7\5\2\2\u00fc\u00fd\7\r\2\2\u00fd"+
		"\u00fe\7\b\2\2\u00fe\u0100\5\2\2\2\u00ff\u00fb\3\2\2\2\u0100\u0103\3\2"+
		"\2\2\u0101\u00ff\3\2\2\2\u0101\u0102\3\2\2\2\u0102\5\3\2\2\2\u0103\u0101"+
		"\3\2\2\2\u0104\u0109\5\2\2\2\u0105\u0106\7\5\2\2\u0106\u0108\5\2\2\2\u0107"+
		"\u0105\3\2\2\2\u0108\u010b\3\2\2\2\u0109\u0107\3\2\2\2\u0109\u010a\3\2"+
		"\2\2\u010a\7\3\2\2\2\u010b\u0109\3\2\2\2\u010c\u0117\7\4\2\2\u010d\u0112"+
		"\7\60\2\2\u010e\u010f\7\5\2\2\u010f\u0111\7\60\2\2\u0110\u010e\3\2\2\2"+
		"\u0111\u0114\3\2\2\2\u0112\u0110\3\2\2\2\u0112\u0113\3\2\2\2\u0113\u0116"+
		"\3\2\2\2\u0114\u0112\3\2\2\2\u0115\u010d\3\2\2\2\u0116\u0119\3\2\2\2\u0117"+
		"\u0115\3\2\2\2\u0117\u0118\3\2\2\2\u0118\u011a\3\2\2\2\u0119\u0117\3\2"+
		"\2\2\u011a\u011b\7\6\2\2\u011b\t\3\2\2\2\u011c\u011d\7\4\2\2\u011d\u011e"+
		"\5\6\4\2\u011e\u011f\7\6\2\2\u011f\13\3\2\2\2\u0120\u0121\7\4\2\2\u0121"+
		"\u0122\7\6\2\2\u0122\r\3\2\2\2\u0123\u0124\5\2\2\2\u0124\u0125\7\n\2\2"+
		"\u0125\u0126\5\2\2\2\u0126\17\3\2\2\2\u0127\u012a\5\16\b\2\u0128\u012a"+
		"\5\2\2\2\u0129\u0127\3\2\2\2\u0129\u0128\3\2\2\2\u012a\21\3\2\2\2\u012b"+
		"\u0130\5\20\t\2\u012c\u012d\7\5\2\2\u012d\u012f\5\20\t\2\u012e\u012c\3"+
		"\2\2\2\u012f\u0132\3\2\2\2\u0130\u012e\3\2\2\2\u0130\u0131\3\2\2\2\u0131"+
		"\23\3\2\2\2\u0132\u0130\3\2\2\2!\31\36\'.\639?JNY]jn{\177\u008c\u0090"+
		"\u0096\u00a4\u00a8\u00b0\u00bf\u00c8\u00f3\u00f5\u0101\u0109\u0112\u0117"+
		"\u0129\u0130";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}