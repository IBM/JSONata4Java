// Generated from com/api/jsonata4java/expressions/generated/MappingExpression.g4 by ANTLR 4.8
package com.api.jsonata4java.expressions.generated;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MappingExpressionLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, TRUE=9, 
		FALSE=10, STRING=11, AND=12, OR=13, IN=14, NULL=15, ARR_OPEN=16, ARR_CLOSE=17, 
		OBJ_OPEN=18, OBJ_CLOSE=19, DOLLAR=20, ROOT=21, DESCEND=22, NUMBER=23, 
		FUNCTIONID=24, WS=25, COMMENT=26, CHAIN=27, ASSIGN=28, MUL=29, DIV=30, 
		ADD=31, SUB=32, REM=33, EQ=34, NOT_EQ=35, LT=36, LE=37, GT=38, GE=39, 
		CONCAT=40, VAR_ID=41, ID=42;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "TRUE", 
			"FALSE", "STRING", "AND", "OR", "IN", "NULL", "ARR_OPEN", "ARR_CLOSE", 
			"OBJ_OPEN", "OBJ_CLOSE", "DOLLAR", "ROOT", "DESCEND", "NUMBER", "FUNCTIONID", 
			"WS", "COMMENT", "CHAIN", "ASSIGN", "MUL", "DIV", "ADD", "SUB", "REM", 
			"EQ", "NOT_EQ", "LT", "LE", "GT", "GE", "CONCAT", "VAR_ID", "ID", "ESC", 
			"UNICODE", "HEX", "INT", "EXP", "SINGLE_QUOTE", "DOUBLE_QUOTE", "BACK_QUOTE"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'.'", "'?'", "':'", "'('", "';'", "')'", "','", "'..'", "'true'", 
			"'false'", null, "'and'", "'or'", "'in'", "'null'", "'['", "']'", "'{'", 
			"'}'", "'$'", "'$$'", "'**'", null, "'function'", null, null, "'~>'", 
			"':='", "'*'", "'/'", "'+'", "'-'", "'%'", "'='", "'!='", "'<'", "'<='", 
			"'>'", "'>='", "'&'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, "TRUE", "FALSE", 
			"STRING", "AND", "OR", "IN", "NULL", "ARR_OPEN", "ARR_CLOSE", "OBJ_OPEN", 
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


	public MappingExpressionLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "MappingExpression.g4"; }

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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2,\u013d\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\3\2\3\2"+
		"\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\t\3\n\3\n\3"+
		"\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\7\f\u0087\n\f\f\f"+
		"\16\f\u008a\13\f\3\f\3\f\3\f\3\f\7\f\u0090\n\f\f\f\16\f\u0093\13\f\3\f"+
		"\5\f\u0096\n\f\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\17\3\17\3\17\3\20\3\20"+
		"\3\20\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25\3\26"+
		"\3\26\3\26\3\27\3\27\3\27\3\30\3\30\3\30\6\30\u00ba\n\30\r\30\16\30\u00bb"+
		"\3\30\5\30\u00bf\n\30\3\30\3\30\3\30\3\30\5\30\u00c5\n\30\3\31\3\31\3"+
		"\31\3\31\3\31\3\31\3\31\3\31\3\31\3\32\6\32\u00d1\n\32\r\32\16\32\u00d2"+
		"\3\32\3\32\3\33\3\33\3\33\3\33\7\33\u00db\n\33\f\33\16\33\u00de\13\33"+
		"\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3\35\3\35\3\35\3\36\3\36\3\37"+
		"\3\37\3 \3 \3!\3!\3\"\3\"\3#\3#\3$\3$\3$\3%\3%\3&\3&\3&\3\'\3\'\3(\3("+
		"\3(\3)\3)\3*\3*\3*\3+\3+\7+\u010b\n+\f+\16+\u010e\13+\3+\3+\7+\u0112\n"+
		"+\f+\16+\u0115\13+\3+\3+\5+\u0119\n+\3,\3,\3,\5,\u011e\n,\3-\3-\3-\3-"+
		"\3-\3-\3.\3.\3/\3/\3/\7/\u012b\n/\f/\16/\u012e\13/\5/\u0130\n/\3\60\3"+
		"\60\5\60\u0134\n\60\3\60\3\60\3\61\3\61\3\62\3\62\3\63\3\63\3\u00dc\2"+
		"\64\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35"+
		"\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36"+
		";\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W\2Y\2[\2]\2_\2a\2c\2e\2\3\2\16\4\2))"+
		"^^\4\2$$^^\3\2\62;\4\2\13\f\"\"\4\2C\\c|\6\2\62;C\\aac|\3\2bb\13\2$$)"+
		")\61\61^^ddhhppttvv\5\2\62;CHch\3\2\63;\4\2GGgg\4\2--//\2\u0146\2\3\3"+
		"\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2"+
		"\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3"+
		"\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2"+
		"%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61"+
		"\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2"+
		"\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I"+
		"\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2"+
		"\2\2\3g\3\2\2\2\5i\3\2\2\2\7k\3\2\2\2\tm\3\2\2\2\13o\3\2\2\2\rq\3\2\2"+
		"\2\17s\3\2\2\2\21u\3\2\2\2\23x\3\2\2\2\25}\3\2\2\2\27\u0095\3\2\2\2\31"+
		"\u0097\3\2\2\2\33\u009b\3\2\2\2\35\u009e\3\2\2\2\37\u00a1\3\2\2\2!\u00a6"+
		"\3\2\2\2#\u00a8\3\2\2\2%\u00aa\3\2\2\2\'\u00ac\3\2\2\2)\u00ae\3\2\2\2"+
		"+\u00b0\3\2\2\2-\u00b3\3\2\2\2/\u00c4\3\2\2\2\61\u00c6\3\2\2\2\63\u00d0"+
		"\3\2\2\2\65\u00d6\3\2\2\2\67\u00e4\3\2\2\29\u00e7\3\2\2\2;\u00ea\3\2\2"+
		"\2=\u00ec\3\2\2\2?\u00ee\3\2\2\2A\u00f0\3\2\2\2C\u00f2\3\2\2\2E\u00f4"+
		"\3\2\2\2G\u00f6\3\2\2\2I\u00f9\3\2\2\2K\u00fb\3\2\2\2M\u00fe\3\2\2\2O"+
		"\u0100\3\2\2\2Q\u0103\3\2\2\2S\u0105\3\2\2\2U\u0118\3\2\2\2W\u011a\3\2"+
		"\2\2Y\u011f\3\2\2\2[\u0125\3\2\2\2]\u012f\3\2\2\2_\u0131\3\2\2\2a\u0137"+
		"\3\2\2\2c\u0139\3\2\2\2e\u013b\3\2\2\2gh\7\60\2\2h\4\3\2\2\2ij\7A\2\2"+
		"j\6\3\2\2\2kl\7<\2\2l\b\3\2\2\2mn\7*\2\2n\n\3\2\2\2op\7=\2\2p\f\3\2\2"+
		"\2qr\7+\2\2r\16\3\2\2\2st\7.\2\2t\20\3\2\2\2uv\7\60\2\2vw\7\60\2\2w\22"+
		"\3\2\2\2xy\7v\2\2yz\7t\2\2z{\7w\2\2{|\7g\2\2|\24\3\2\2\2}~\7h\2\2~\177"+
		"\7c\2\2\177\u0080\7n\2\2\u0080\u0081\7u\2\2\u0081\u0082\7g\2\2\u0082\26"+
		"\3\2\2\2\u0083\u0088\7)\2\2\u0084\u0087\5W,\2\u0085\u0087\n\2\2\2\u0086"+
		"\u0084\3\2\2\2\u0086\u0085\3\2\2\2\u0087\u008a\3\2\2\2\u0088\u0086\3\2"+
		"\2\2\u0088\u0089\3\2\2\2\u0089\u008b\3\2\2\2\u008a\u0088\3\2\2\2\u008b"+
		"\u0096\7)\2\2\u008c\u0091\7$\2\2\u008d\u0090\5W,\2\u008e\u0090\n\3\2\2"+
		"\u008f\u008d\3\2\2\2\u008f\u008e\3\2\2\2\u0090\u0093\3\2\2\2\u0091\u008f"+
		"\3\2\2\2\u0091\u0092\3\2\2\2\u0092\u0094\3\2\2\2\u0093\u0091\3\2\2\2\u0094"+
		"\u0096\7$\2\2\u0095\u0083\3\2\2\2\u0095\u008c\3\2\2\2\u0096\30\3\2\2\2"+
		"\u0097\u0098\7c\2\2\u0098\u0099\7p\2\2\u0099\u009a\7f\2\2\u009a\32\3\2"+
		"\2\2\u009b\u009c\7q\2\2\u009c\u009d\7t\2\2\u009d\34\3\2\2\2\u009e\u009f"+
		"\7k\2\2\u009f\u00a0\7p\2\2\u00a0\36\3\2\2\2\u00a1\u00a2\7p\2\2\u00a2\u00a3"+
		"\7w\2\2\u00a3\u00a4\7n\2\2\u00a4\u00a5\7n\2\2\u00a5 \3\2\2\2\u00a6\u00a7"+
		"\7]\2\2\u00a7\"\3\2\2\2\u00a8\u00a9\7_\2\2\u00a9$\3\2\2\2\u00aa\u00ab"+
		"\7}\2\2\u00ab&\3\2\2\2\u00ac\u00ad\7\177\2\2\u00ad(\3\2\2\2\u00ae\u00af"+
		"\7&\2\2\u00af*\3\2\2\2\u00b0\u00b1\7&\2\2\u00b1\u00b2\7&\2\2\u00b2,\3"+
		"\2\2\2\u00b3\u00b4\7,\2\2\u00b4\u00b5\7,\2\2\u00b5.\3\2\2\2\u00b6\u00b7"+
		"\5]/\2\u00b7\u00b9\7\60\2\2\u00b8\u00ba\t\4\2\2\u00b9\u00b8\3\2\2\2\u00ba"+
		"\u00bb\3\2\2\2\u00bb\u00b9\3\2\2\2\u00bb\u00bc\3\2\2\2\u00bc\u00be\3\2"+
		"\2\2\u00bd\u00bf\5_\60\2\u00be\u00bd\3\2\2\2\u00be\u00bf\3\2\2\2\u00bf"+
		"\u00c5\3\2\2\2\u00c0\u00c1\5]/\2\u00c1\u00c2\5_\60\2\u00c2\u00c5\3\2\2"+
		"\2\u00c3\u00c5\5]/\2\u00c4\u00b6\3\2\2\2\u00c4\u00c0\3\2\2\2\u00c4\u00c3"+
		"\3\2\2\2\u00c5\60\3\2\2\2\u00c6\u00c7\7h\2\2\u00c7\u00c8\7w\2\2\u00c8"+
		"\u00c9\7p\2\2\u00c9\u00ca\7e\2\2\u00ca\u00cb\7v\2\2\u00cb\u00cc\7k\2\2"+
		"\u00cc\u00cd\7q\2\2\u00cd\u00ce\7p\2\2\u00ce\62\3\2\2\2\u00cf\u00d1\t"+
		"\5\2\2\u00d0\u00cf\3\2\2\2\u00d1\u00d2\3\2\2\2\u00d2\u00d0\3\2\2\2\u00d2"+
		"\u00d3\3\2\2\2\u00d3\u00d4\3\2\2\2\u00d4\u00d5\b\32\2\2\u00d5\64\3\2\2"+
		"\2\u00d6\u00d7\7\61\2\2\u00d7\u00d8\7,\2\2\u00d8\u00dc\3\2\2\2\u00d9\u00db"+
		"\13\2\2\2\u00da\u00d9\3\2\2\2\u00db\u00de\3\2\2\2\u00dc\u00dd\3\2\2\2"+
		"\u00dc\u00da\3\2\2\2\u00dd\u00df\3\2\2\2\u00de\u00dc\3\2\2\2\u00df\u00e0"+
		"\7,\2\2\u00e0\u00e1\7\61\2\2\u00e1\u00e2\3\2\2\2\u00e2\u00e3\b\33\2\2"+
		"\u00e3\66\3\2\2\2\u00e4\u00e5\7\u0080\2\2\u00e5\u00e6\7@\2\2\u00e68\3"+
		"\2\2\2\u00e7\u00e8\7<\2\2\u00e8\u00e9\7?\2\2\u00e9:\3\2\2\2\u00ea\u00eb"+
		"\7,\2\2\u00eb<\3\2\2\2\u00ec\u00ed\7\61\2\2\u00ed>\3\2\2\2\u00ee\u00ef"+
		"\7-\2\2\u00ef@\3\2\2\2\u00f0\u00f1\7/\2\2\u00f1B\3\2\2\2\u00f2\u00f3\7"+
		"\'\2\2\u00f3D\3\2\2\2\u00f4\u00f5\7?\2\2\u00f5F\3\2\2\2\u00f6\u00f7\7"+
		"#\2\2\u00f7\u00f8\7?\2\2\u00f8H\3\2\2\2\u00f9\u00fa\7>\2\2\u00faJ\3\2"+
		"\2\2\u00fb\u00fc\7>\2\2\u00fc\u00fd\7?\2\2\u00fdL\3\2\2\2\u00fe\u00ff"+
		"\7@\2\2\u00ffN\3\2\2\2\u0100\u0101\7@\2\2\u0101\u0102\7?\2\2\u0102P\3"+
		"\2\2\2\u0103\u0104\7(\2\2\u0104R\3\2\2\2\u0105\u0106\7&\2\2\u0106\u0107"+
		"\5U+\2\u0107T\3\2\2\2\u0108\u010c\t\6\2\2\u0109\u010b\t\7\2\2\u010a\u0109"+
		"\3\2\2\2\u010b\u010e\3\2\2\2\u010c\u010a\3\2\2\2\u010c\u010d\3\2\2\2\u010d"+
		"\u0119\3\2\2\2\u010e\u010c\3\2\2\2\u010f\u0113\5e\63\2\u0110\u0112\n\b"+
		"\2\2\u0111\u0110\3\2\2\2\u0112\u0115\3\2\2\2\u0113\u0111\3\2\2\2\u0113"+
		"\u0114\3\2\2\2\u0114\u0116\3\2\2\2\u0115\u0113\3\2\2\2\u0116\u0117\5e"+
		"\63\2\u0117\u0119\3\2\2\2\u0118\u0108\3\2\2\2\u0118\u010f\3\2\2\2\u0119"+
		"V\3\2\2\2\u011a\u011d\7^\2\2\u011b\u011e\t\t\2\2\u011c\u011e\5Y-\2\u011d"+
		"\u011b\3\2\2\2\u011d\u011c\3\2\2\2\u011eX\3\2\2\2\u011f\u0120\7w\2\2\u0120"+
		"\u0121\5[.\2\u0121\u0122\5[.\2\u0122\u0123\5[.\2\u0123\u0124\5[.\2\u0124"+
		"Z\3\2\2\2\u0125\u0126\t\n\2\2\u0126\\\3\2\2\2\u0127\u0130\7\62\2\2\u0128"+
		"\u012c\t\13\2\2\u0129\u012b\t\4\2\2\u012a\u0129\3\2\2\2\u012b\u012e\3"+
		"\2\2\2\u012c\u012a\3\2\2\2\u012c\u012d\3\2\2\2\u012d\u0130\3\2\2\2\u012e"+
		"\u012c\3\2\2\2\u012f\u0127\3\2\2\2\u012f\u0128\3\2\2\2\u0130^\3\2\2\2"+
		"\u0131\u0133\t\f\2\2\u0132\u0134\t\r\2\2\u0133\u0132\3\2\2\2\u0133\u0134"+
		"\3\2\2\2\u0134\u0135\3\2\2\2\u0135\u0136\5]/\2\u0136`\3\2\2\2\u0137\u0138"+
		"\7)\2\2\u0138b\3\2\2\2\u0139\u013a\7$\2\2\u013ad\3\2\2\2\u013b\u013c\7"+
		"b\2\2\u013cf\3\2\2\2\24\2\u0086\u0088\u008f\u0091\u0095\u00bb\u00be\u00c4"+
		"\u00d2\u00dc\u010c\u0113\u0118\u011d\u012c\u012f\u0133\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}