// Generated from com/api/jsonata4java/expressions/path/generated/PathExpressionLexer.g4 by ANTLR 4.9.2
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
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		WS=1, NON_BACK_QUOTED_ID=2, BACK_QUOTED_ID=3, ARR_OPEN=4, ARR_CLOSE=5, 
		PATH_DELIM=6, NUMBER=7, BACK_QUOTE_CONTENT=8, BACK_QUOTE_EXIT=9;
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
			"PATH_DELIM", "NUMBER", "BACK_QUOTE_CONTENT", "BACK_QUOTE_EXIT", "INT", 
			"BACK_QUOTE", "NOT_BACK_QUOTE"
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\13M\b\1\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\3\2\6\2\36\n\2\r\2\16\2\37\3\2\3\2\3\3\3\3\7\3&"+
		"\n\3\f\3\16\3)\13\3\3\4\3\4\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3"+
		"\t\6\t8\n\t\r\t\16\t9\3\n\3\n\3\n\3\n\3\13\3\13\3\13\7\13C\n\13\f\13\16"+
		"\13F\13\13\5\13H\n\13\3\f\3\f\3\r\3\r\2\2\16\4\3\6\4\b\5\n\6\f\7\16\b"+
		"\20\t\22\n\24\13\26\2\30\2\32\2\4\2\3\b\4\2\13\f\"\"\4\2C\\c|\6\2\62;"+
		"C\\aac|\3\2\63;\3\2\62;\4\2))bb\2M\2\4\3\2\2\2\2\6\3\2\2\2\2\b\3\2\2\2"+
		"\2\n\3\2\2\2\2\f\3\2\2\2\2\16\3\2\2\2\2\20\3\2\2\2\3\22\3\2\2\2\3\24\3"+
		"\2\2\2\4\35\3\2\2\2\6#\3\2\2\2\b*\3\2\2\2\n.\3\2\2\2\f\60\3\2\2\2\16\62"+
		"\3\2\2\2\20\64\3\2\2\2\22\67\3\2\2\2\24;\3\2\2\2\26G\3\2\2\2\30I\3\2\2"+
		"\2\32K\3\2\2\2\34\36\t\2\2\2\35\34\3\2\2\2\36\37\3\2\2\2\37\35\3\2\2\2"+
		"\37 \3\2\2\2 !\3\2\2\2!\"\b\2\2\2\"\5\3\2\2\2#\'\t\3\2\2$&\t\4\2\2%$\3"+
		"\2\2\2&)\3\2\2\2\'%\3\2\2\2\'(\3\2\2\2(\7\3\2\2\2)\'\3\2\2\2*+\5\30\f"+
		"\2+,\3\2\2\2,-\b\4\3\2-\t\3\2\2\2./\7]\2\2/\13\3\2\2\2\60\61\7_\2\2\61"+
		"\r\3\2\2\2\62\63\7\60\2\2\63\17\3\2\2\2\64\65\5\26\13\2\65\21\3\2\2\2"+
		"\668\5\32\r\2\67\66\3\2\2\289\3\2\2\29\67\3\2\2\29:\3\2\2\2:\23\3\2\2"+
		"\2;<\5\30\f\2<=\3\2\2\2=>\b\n\4\2>\25\3\2\2\2?H\7\62\2\2@D\t\5\2\2AC\t"+
		"\6\2\2BA\3\2\2\2CF\3\2\2\2DB\3\2\2\2DE\3\2\2\2EH\3\2\2\2FD\3\2\2\2G?\3"+
		"\2\2\2G@\3\2\2\2H\27\3\2\2\2IJ\7b\2\2J\31\3\2\2\2KL\n\7\2\2L\33\3\2\2"+
		"\2\t\2\3\37\'9DG\5\b\2\2\7\3\2\6\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}