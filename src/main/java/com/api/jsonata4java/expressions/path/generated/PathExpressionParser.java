// Generated from java-escape by ANTLR 4.11.1
package com.api.jsonata4java.expressions.path.generated;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class PathExpressionParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.11.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		WS=1, NON_BACK_QUOTED_ID=2, BACK_QUOTED_ID=3, ARR_OPEN=4, ARR_CLOSE=5, 
		PATH_DELIM=6, NUMBER=7, BACK_QUOTE_CONTENT=8, BACK_QUOTE_EXIT=9;
	public static final int
		RULE_expr = 0, RULE_id = 1, RULE_array_index = 2;
	private static String[] makeRuleNames() {
		return new String[] {
			"expr", "id", "array_index"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, "'['", "']'", "'.'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "WS", "NON_BACK_QUOTED_ID", "BACK_QUOTED_ID", "ARR_OPEN", "ARR_CLOSE", 
			"PATH_DELIM", "NUMBER", "BACK_QUOTE_CONTENT", "BACK_QUOTE_EXIT"
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
	public String getGrammarFileName() { return "java-escape"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public PathExpressionParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
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
	@SuppressWarnings("CheckReturnValue")
	public static class PathContext extends ExprContext {
		public ExprContext rhs;
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public TerminalNode EOF() { return getToken(PathExpressionParser.EOF, 0); }
		public List<Array_indexContext> array_index() {
			return getRuleContexts(Array_indexContext.class);
		}
		public Array_indexContext array_index(int i) {
			return getRuleContext(Array_indexContext.class,i);
		}
		public TerminalNode PATH_DELIM() { return getToken(PathExpressionParser.PATH_DELIM, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public PathContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathExpressionParserListener ) ((PathExpressionParserListener)listener).enterPath(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathExpressionParserListener ) ((PathExpressionParserListener)listener).exitPath(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathExpressionParserVisitor ) return ((PathExpressionParserVisitor<? extends T>)visitor).visitPath(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		ExprContext _localctx = new ExprContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_expr);
		int _la;
		try {
			_localctx = new PathContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(6);
			id();
			setState(10);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==ARR_OPEN) {
				{
				{
				setState(7);
				array_index();
				}
				}
				setState(12);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(16);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PATH_DELIM:
				{
				{
				setState(13);
				match(PATH_DELIM);
				setState(14);
				((PathContext)_localctx).rhs = expr();
				}
				}
				break;
			case EOF:
				{
				setState(15);
				match(EOF);
				}
				break;
			default:
				throw new NoViableAltException(this);
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

	@SuppressWarnings("CheckReturnValue")
	public static class IdContext extends ParserRuleContext {
		public Token txt;
		public TerminalNode BACK_QUOTED_ID() { return getToken(PathExpressionParser.BACK_QUOTED_ID, 0); }
		public TerminalNode BACK_QUOTE_EXIT() { return getToken(PathExpressionParser.BACK_QUOTE_EXIT, 0); }
		public TerminalNode BACK_QUOTE_CONTENT() { return getToken(PathExpressionParser.BACK_QUOTE_CONTENT, 0); }
		public TerminalNode NON_BACK_QUOTED_ID() { return getToken(PathExpressionParser.NON_BACK_QUOTED_ID, 0); }
		public IdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_id; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathExpressionParserListener ) ((PathExpressionParserListener)listener).enterId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathExpressionParserListener ) ((PathExpressionParserListener)listener).exitId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathExpressionParserVisitor ) return ((PathExpressionParserVisitor<? extends T>)visitor).visitId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdContext id() throws RecognitionException {
		IdContext _localctx = new IdContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_id);
		try {
			setState(22);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BACK_QUOTED_ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(18);
				match(BACK_QUOTED_ID);
				{
				setState(19);
				((IdContext)_localctx).txt = match(BACK_QUOTE_CONTENT);
				}
				setState(20);
				match(BACK_QUOTE_EXIT);
				}
				break;
			case NON_BACK_QUOTED_ID:
				enterOuterAlt(_localctx, 2);
				{
				setState(21);
				((IdContext)_localctx).txt = match(NON_BACK_QUOTED_ID);
				}
				break;
			default:
				throw new NoViableAltException(this);
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

	@SuppressWarnings("CheckReturnValue")
	public static class Array_indexContext extends ParserRuleContext {
		public TerminalNode ARR_OPEN() { return getToken(PathExpressionParser.ARR_OPEN, 0); }
		public TerminalNode ARR_CLOSE() { return getToken(PathExpressionParser.ARR_CLOSE, 0); }
		public TerminalNode NUMBER() { return getToken(PathExpressionParser.NUMBER, 0); }
		public Array_indexContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array_index; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathExpressionParserListener ) ((PathExpressionParserListener)listener).enterArray_index(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathExpressionParserListener ) ((PathExpressionParserListener)listener).exitArray_index(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathExpressionParserVisitor ) return ((PathExpressionParserVisitor<? extends T>)visitor).visitArray_index(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Array_indexContext array_index() throws RecognitionException {
		Array_indexContext _localctx = new Array_indexContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_array_index);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(24);
			match(ARR_OPEN);
			{
			setState(25);
			match(NUMBER);
			}
			setState(26);
			match(ARR_CLOSE);
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

	public static final String _serializedATN =
		"\u0004\u0001\t\u001d\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0001\u0000\u0001\u0000\u0005\u0000\t\b\u0000\n\u0000"+
		"\f\u0000\f\t\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0003\u0000\u0011"+
		"\b\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u0001\u0017"+
		"\b\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0000"+
		"\u0000\u0003\u0000\u0002\u0004\u0000\u0000\u001c\u0000\u0006\u0001\u0000"+
		"\u0000\u0000\u0002\u0016\u0001\u0000\u0000\u0000\u0004\u0018\u0001\u0000"+
		"\u0000\u0000\u0006\n\u0003\u0002\u0001\u0000\u0007\t\u0003\u0004\u0002"+
		"\u0000\b\u0007\u0001\u0000\u0000\u0000\t\f\u0001\u0000\u0000\u0000\n\b"+
		"\u0001\u0000\u0000\u0000\n\u000b\u0001\u0000\u0000\u0000\u000b\u0010\u0001"+
		"\u0000\u0000\u0000\f\n\u0001\u0000\u0000\u0000\r\u000e\u0005\u0006\u0000"+
		"\u0000\u000e\u0011\u0003\u0000\u0000\u0000\u000f\u0011\u0005\u0000\u0000"+
		"\u0001\u0010\r\u0001\u0000\u0000\u0000\u0010\u000f\u0001\u0000\u0000\u0000"+
		"\u0011\u0001\u0001\u0000\u0000\u0000\u0012\u0013\u0005\u0003\u0000\u0000"+
		"\u0013\u0014\u0005\b\u0000\u0000\u0014\u0017\u0005\t\u0000\u0000\u0015"+
		"\u0017\u0005\u0002\u0000\u0000\u0016\u0012\u0001\u0000\u0000\u0000\u0016"+
		"\u0015\u0001\u0000\u0000\u0000\u0017\u0003\u0001\u0000\u0000\u0000\u0018"+
		"\u0019\u0005\u0004\u0000\u0000\u0019\u001a\u0005\u0007\u0000\u0000\u001a"+
		"\u001b\u0005\u0005\u0000\u0000\u001b\u0005\u0001\u0000\u0000\u0000\u0003"+
		"\n\u0010\u0016";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}