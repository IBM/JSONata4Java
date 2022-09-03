// Generated from com\api\jsonata4java\expressions\path\generated\PathExpressionLexer.g4 by ANTLR 4.10.1
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
	static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

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
		"\u0004\u0000\tK\u0006\uffff\uffff\u0006\uffff\uffff\u0002\u0000\u0007"+
		"\u0000\u0002\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007"+
		"\u0003\u0002\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007"+
		"\u0006\u0002\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n"+
		"\u0007\n\u0002\u000b\u0007\u000b\u0001\u0000\u0004\u0000\u001c\b\u0000"+
		"\u000b\u0000\f\u0000\u001d\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001"+
		"\u0005\u0001$\b\u0001\n\u0001\f\u0001\'\t\u0001\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004"+
		"\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0007\u0004\u0007"+
		"6\b\u0007\u000b\u0007\f\u00077\u0001\b\u0001\b\u0001\b\u0001\b\u0001\t"+
		"\u0001\t\u0001\t\u0005\tA\b\t\n\t\f\tD\t\t\u0003\tF\b\t\u0001\n\u0001"+
		"\n\u0001\u000b\u0001\u000b\u0000\u0000\f\u0002\u0001\u0004\u0002\u0006"+
		"\u0003\b\u0004\n\u0005\f\u0006\u000e\u0007\u0010\b\u0012\t\u0014\u0000"+
		"\u0016\u0000\u0018\u0000\u0002\u0000\u0001\u0006\u0002\u0000\t\n  \u0002"+
		"\u0000AZaz\u0004\u000009AZ__az\u0001\u000019\u0001\u000009\u0002\u0000"+
		"\'\'``K\u0000\u0002\u0001\u0000\u0000\u0000\u0000\u0004\u0001\u0000\u0000"+
		"\u0000\u0000\u0006\u0001\u0000\u0000\u0000\u0000\b\u0001\u0000\u0000\u0000"+
		"\u0000\n\u0001\u0000\u0000\u0000\u0000\f\u0001\u0000\u0000\u0000\u0000"+
		"\u000e\u0001\u0000\u0000\u0000\u0001\u0010\u0001\u0000\u0000\u0000\u0001"+
		"\u0012\u0001\u0000\u0000\u0000\u0002\u001b\u0001\u0000\u0000\u0000\u0004"+
		"!\u0001\u0000\u0000\u0000\u0006(\u0001\u0000\u0000\u0000\b,\u0001\u0000"+
		"\u0000\u0000\n.\u0001\u0000\u0000\u0000\f0\u0001\u0000\u0000\u0000\u000e"+
		"2\u0001\u0000\u0000\u0000\u00105\u0001\u0000\u0000\u0000\u00129\u0001"+
		"\u0000\u0000\u0000\u0014E\u0001\u0000\u0000\u0000\u0016G\u0001\u0000\u0000"+
		"\u0000\u0018I\u0001\u0000\u0000\u0000\u001a\u001c\u0007\u0000\u0000\u0000"+
		"\u001b\u001a\u0001\u0000\u0000\u0000\u001c\u001d\u0001\u0000\u0000\u0000"+
		"\u001d\u001b\u0001\u0000\u0000\u0000\u001d\u001e\u0001\u0000\u0000\u0000"+
		"\u001e\u001f\u0001\u0000\u0000\u0000\u001f \u0006\u0000\u0000\u0000 \u0003"+
		"\u0001\u0000\u0000\u0000!%\u0007\u0001\u0000\u0000\"$\u0007\u0002\u0000"+
		"\u0000#\"\u0001\u0000\u0000\u0000$\'\u0001\u0000\u0000\u0000%#\u0001\u0000"+
		"\u0000\u0000%&\u0001\u0000\u0000\u0000&\u0005\u0001\u0000\u0000\u0000"+
		"\'%\u0001\u0000\u0000\u0000()\u0003\u0016\n\u0000)*\u0001\u0000\u0000"+
		"\u0000*+\u0006\u0002\u0001\u0000+\u0007\u0001\u0000\u0000\u0000,-\u0005"+
		"[\u0000\u0000-\t\u0001\u0000\u0000\u0000./\u0005]\u0000\u0000/\u000b\u0001"+
		"\u0000\u0000\u000001\u0005.\u0000\u00001\r\u0001\u0000\u0000\u000023\u0003"+
		"\u0014\t\u00003\u000f\u0001\u0000\u0000\u000046\u0003\u0018\u000b\u0000"+
		"54\u0001\u0000\u0000\u000067\u0001\u0000\u0000\u000075\u0001\u0000\u0000"+
		"\u000078\u0001\u0000\u0000\u00008\u0011\u0001\u0000\u0000\u00009:\u0003"+
		"\u0016\n\u0000:;\u0001\u0000\u0000\u0000;<\u0006\b\u0002\u0000<\u0013"+
		"\u0001\u0000\u0000\u0000=F\u00050\u0000\u0000>B\u0007\u0003\u0000\u0000"+
		"?A\u0007\u0004\u0000\u0000@?\u0001\u0000\u0000\u0000AD\u0001\u0000\u0000"+
		"\u0000B@\u0001\u0000\u0000\u0000BC\u0001\u0000\u0000\u0000CF\u0001\u0000"+
		"\u0000\u0000DB\u0001\u0000\u0000\u0000E=\u0001\u0000\u0000\u0000E>\u0001"+
		"\u0000\u0000\u0000F\u0015\u0001\u0000\u0000\u0000GH\u0005`\u0000\u0000"+
		"H\u0017\u0001\u0000\u0000\u0000IJ\b\u0005\u0000\u0000J\u0019\u0001\u0000"+
		"\u0000\u0000\u0007\u0000\u0001\u001d%7BE\u0003\u0006\u0000\u0000\u0005"+
		"\u0001\u0000\u0004\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}