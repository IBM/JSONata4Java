// Generated from com/api/jsonata4java/expressions/path/generated/PathExpressionLexer.g4 by ANTLR 4.8
package com.api.jsonata4java.expressions.path.generated;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PathExpressionLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		WS=1, NON_BACK_QUOTED_ID=2, BACK_QUOTED_ID=3, ARR_OPEN=4, ARR_CLOSE=5, 
		PATH_DELIM=6, DOLLAR_PREFIX=7, NUMBER=8, BACK_QUOTE_CONTENT=9, BACK_QUOTE_EXIT=10;
	public static final int
		MODE_BACK_QUOTE=1;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE", "MODE_BACK_QUOTE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"WS", "NON_BACK_QUOTED_ID", "BACK_QUOTED_ID", "ARR_OPEN", "ARR_CLOSE", 
			"PATH_DELIM", "DOLLAR_PREFIX", "NUMBER", "BACK_QUOTE_CONTENT", "BACK_QUOTE_EXIT", 
			"INT", "BACK_QUOTE", "NOT_BACK_QUOTE"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, "'['", "']'", "'.'", "'$'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "WS", "NON_BACK_QUOTED_ID", "BACK_QUOTED_ID", "ARR_OPEN", "ARR_CLOSE", 
			"PATH_DELIM", "DOLLAR_PREFIX", "NUMBER", "BACK_QUOTE_CONTENT", "BACK_QUOTE_EXIT"
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


	public PathExpressionLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "PathExpressionLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\fQ\b\1\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\3\2\6\2 \n\2\r\2\16\2!\3\2\3\2\3\3\3\3"+
		"\7\3(\n\3\f\3\16\3+\13\3\3\4\3\4\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3"+
		"\b\3\t\3\t\3\n\6\n<\n\n\r\n\16\n=\3\13\3\13\3\13\3\13\3\f\3\f\3\f\7\f"+
		"G\n\f\f\f\16\fJ\13\f\5\fL\n\f\3\r\3\r\3\16\3\16\2\2\17\4\3\6\4\b\5\n\6"+
		"\f\7\16\b\20\t\22\n\24\13\26\f\30\2\32\2\34\2\4\2\3\b\4\2\13\f\"\"\4\2"+
		"C\\c|\6\2\62;C\\aac|\3\2\63;\3\2\62;\4\2))bb\2Q\2\4\3\2\2\2\2\6\3\2\2"+
		"\2\2\b\3\2\2\2\2\n\3\2\2\2\2\f\3\2\2\2\2\16\3\2\2\2\2\20\3\2\2\2\2\22"+
		"\3\2\2\2\3\24\3\2\2\2\3\26\3\2\2\2\4\37\3\2\2\2\6%\3\2\2\2\b,\3\2\2\2"+
		"\n\60\3\2\2\2\f\62\3\2\2\2\16\64\3\2\2\2\20\66\3\2\2\2\228\3\2\2\2\24"+
		";\3\2\2\2\26?\3\2\2\2\30K\3\2\2\2\32M\3\2\2\2\34O\3\2\2\2\36 \t\2\2\2"+
		"\37\36\3\2\2\2 !\3\2\2\2!\37\3\2\2\2!\"\3\2\2\2\"#\3\2\2\2#$\b\2\2\2$"+
		"\5\3\2\2\2%)\t\3\2\2&(\t\4\2\2\'&\3\2\2\2(+\3\2\2\2)\'\3\2\2\2)*\3\2\2"+
		"\2*\7\3\2\2\2+)\3\2\2\2,-\5\32\r\2-.\3\2\2\2./\b\4\3\2/\t\3\2\2\2\60\61"+
		"\7]\2\2\61\13\3\2\2\2\62\63\7_\2\2\63\r\3\2\2\2\64\65\7\60\2\2\65\17\3"+
		"\2\2\2\66\67\7&\2\2\67\21\3\2\2\289\5\30\f\29\23\3\2\2\2:<\5\34\16\2;"+
		":\3\2\2\2<=\3\2\2\2=;\3\2\2\2=>\3\2\2\2>\25\3\2\2\2?@\5\32\r\2@A\3\2\2"+
		"\2AB\b\13\4\2B\27\3\2\2\2CL\7\62\2\2DH\t\5\2\2EG\t\6\2\2FE\3\2\2\2GJ\3"+
		"\2\2\2HF\3\2\2\2HI\3\2\2\2IL\3\2\2\2JH\3\2\2\2KC\3\2\2\2KD\3\2\2\2L\31"+
		"\3\2\2\2MN\7b\2\2N\33\3\2\2\2OP\n\7\2\2P\35\3\2\2\2\t\2\3!)=HK\5\b\2\2"+
		"\7\3\2\6\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}