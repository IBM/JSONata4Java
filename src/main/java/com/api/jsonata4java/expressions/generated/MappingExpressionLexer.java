// Generated from com/api/jsonata4java/expressions/generated/MappingExpression.g4 by ANTLR 4.7
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
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "TRUE", 
		"FALSE", "STRING", "AND", "OR", "IN", "NULL", "ARR_OPEN", "ARR_CLOSE", 
		"OBJ_OPEN", "OBJ_CLOSE", "DOLLAR", "ROOT", "DESCEND", "NUMBER", "FUNCTIONID", 
		"WS", "COMMENT", "CHAIN", "ASSIGN", "MUL", "DIV", "ADD", "SUB", "REM", 
		"EQ", "NOT_EQ", "LT", "LE", "GT", "GE", "CONCAT", "EACH", "SIFT", "REDUCE", 
		"FILTER", "MAP", "VAR_ID", "ID", "ESC", "UNICODE", "HEX", "INT", "EXP", 
		"SINGLE_QUOTE", "DOUBLE_QUOTE", "BACK_QUOTE"
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\61\u0168\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"+
		"+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64"+
		"\t\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\3\2\3\2\3\3\3\3\3\4\3\4\3\5"+
		"\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\13\3\13"+
		"\3\13\3\13\3\13\3\13\3\f\3\f\3\f\7\f\u0091\n\f\f\f\16\f\u0094\13\f\3\f"+
		"\3\f\3\f\3\f\7\f\u009a\n\f\f\f\16\f\u009d\13\f\3\f\5\f\u00a0\n\f\3\r\3"+
		"\r\3\r\3\r\3\16\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\21"+
		"\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25\3\26\3\26\3\26\3\27\3\27"+
		"\3\27\3\30\3\30\3\30\6\30\u00c4\n\30\r\30\16\30\u00c5\3\30\5\30\u00c9"+
		"\n\30\3\30\3\30\3\30\3\30\5\30\u00cf\n\30\3\31\3\31\3\31\3\31\3\31\3\31"+
		"\3\31\3\31\3\31\3\32\6\32\u00db\n\32\r\32\16\32\u00dc\3\32\3\32\3\33\3"+
		"\33\3\33\3\33\7\33\u00e5\n\33\f\33\16\33\u00e8\13\33\3\33\3\33\3\33\3"+
		"\33\3\33\3\34\3\34\3\34\3\35\3\35\3\35\3\36\3\36\3\37\3\37\3 \3 \3!\3"+
		"!\3\"\3\"\3#\3#\3$\3$\3$\3%\3%\3&\3&\3&\3\'\3\'\3(\3(\3(\3)\3)\3*\3*\3"+
		"*\3*\3*\3*\3+\3+\3+\3+\3+\3+\3,\3,\3,\3,\3,\3,\3,\3,\3-\3-\3-\3-\3-\3"+
		"-\3-\3-\3.\3.\3.\3.\3.\3/\3/\3/\3\60\3\60\7\60\u0136\n\60\f\60\16\60\u0139"+
		"\13\60\3\60\3\60\7\60\u013d\n\60\f\60\16\60\u0140\13\60\3\60\3\60\5\60"+
		"\u0144\n\60\3\61\3\61\3\61\5\61\u0149\n\61\3\62\3\62\3\62\3\62\3\62\3"+
		"\62\3\63\3\63\3\64\3\64\3\64\7\64\u0156\n\64\f\64\16\64\u0159\13\64\5"+
		"\64\u015b\n\64\3\65\3\65\5\65\u015f\n\65\3\65\3\65\3\66\3\66\3\67\3\67"+
		"\38\38\3\u00e6\29\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31"+
		"\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65"+
		"\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a\2c\2e\2g"+
		"\2i\2k\2m\2o\2\3\2\16\4\2))^^\4\2$$^^\3\2\62;\4\2\13\f\"\"\4\2C\\c|\6"+
		"\2\62;C\\aac|\3\2bb\13\2$$))\61\61^^ddhhppttvv\5\2\62;CHch\3\2\63;\4\2"+
		"GGgg\4\2--//\2\u0171\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2"+
		"\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3"+
		"\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2"+
		"\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2"+
		"\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2"+
		"\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2"+
		"\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q"+
		"\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2"+
		"\2\2\2_\3\2\2\2\3q\3\2\2\2\5s\3\2\2\2\7u\3\2\2\2\tw\3\2\2\2\13y\3\2\2"+
		"\2\r{\3\2\2\2\17}\3\2\2\2\21\177\3\2\2\2\23\u0082\3\2\2\2\25\u0087\3\2"+
		"\2\2\27\u009f\3\2\2\2\31\u00a1\3\2\2\2\33\u00a5\3\2\2\2\35\u00a8\3\2\2"+
		"\2\37\u00ab\3\2\2\2!\u00b0\3\2\2\2#\u00b2\3\2\2\2%\u00b4\3\2\2\2\'\u00b6"+
		"\3\2\2\2)\u00b8\3\2\2\2+\u00ba\3\2\2\2-\u00bd\3\2\2\2/\u00ce\3\2\2\2\61"+
		"\u00d0\3\2\2\2\63\u00da\3\2\2\2\65\u00e0\3\2\2\2\67\u00ee\3\2\2\29\u00f1"+
		"\3\2\2\2;\u00f4\3\2\2\2=\u00f6\3\2\2\2?\u00f8\3\2\2\2A\u00fa\3\2\2\2C"+
		"\u00fc\3\2\2\2E\u00fe\3\2\2\2G\u0100\3\2\2\2I\u0103\3\2\2\2K\u0105\3\2"+
		"\2\2M\u0108\3\2\2\2O\u010a\3\2\2\2Q\u010d\3\2\2\2S\u010f\3\2\2\2U\u0115"+
		"\3\2\2\2W\u011b\3\2\2\2Y\u0123\3\2\2\2[\u012b\3\2\2\2]\u0130\3\2\2\2_"+
		"\u0143\3\2\2\2a\u0145\3\2\2\2c\u014a\3\2\2\2e\u0150\3\2\2\2g\u015a\3\2"+
		"\2\2i\u015c\3\2\2\2k\u0162\3\2\2\2m\u0164\3\2\2\2o\u0166\3\2\2\2qr\7\60"+
		"\2\2r\4\3\2\2\2st\7*\2\2t\6\3\2\2\2uv\7.\2\2v\b\3\2\2\2wx\7+\2\2x\n\3"+
		"\2\2\2yz\7A\2\2z\f\3\2\2\2{|\7<\2\2|\16\3\2\2\2}~\7=\2\2~\20\3\2\2\2\177"+
		"\u0080\7\60\2\2\u0080\u0081\7\60\2\2\u0081\22\3\2\2\2\u0082\u0083\7v\2"+
		"\2\u0083\u0084\7t\2\2\u0084\u0085\7w\2\2\u0085\u0086\7g\2\2\u0086\24\3"+
		"\2\2\2\u0087\u0088\7h\2\2\u0088\u0089\7c\2\2\u0089\u008a\7n\2\2\u008a"+
		"\u008b\7u\2\2\u008b\u008c\7g\2\2\u008c\26\3\2\2\2\u008d\u0092\7)\2\2\u008e"+
		"\u0091\5a\61\2\u008f\u0091\n\2\2\2\u0090\u008e\3\2\2\2\u0090\u008f\3\2"+
		"\2\2\u0091\u0094\3\2\2\2\u0092\u0090\3\2\2\2\u0092\u0093\3\2\2\2\u0093"+
		"\u0095\3\2\2\2\u0094\u0092\3\2\2\2\u0095\u00a0\7)\2\2\u0096\u009b\7$\2"+
		"\2\u0097\u009a\5a\61\2\u0098\u009a\n\3\2\2\u0099\u0097\3\2\2\2\u0099\u0098"+
		"\3\2\2\2\u009a\u009d\3\2\2\2\u009b\u0099\3\2\2\2\u009b\u009c\3\2\2\2\u009c"+
		"\u009e\3\2\2\2\u009d\u009b\3\2\2\2\u009e\u00a0\7$\2\2\u009f\u008d\3\2"+
		"\2\2\u009f\u0096\3\2\2\2\u00a0\30\3\2\2\2\u00a1\u00a2\7c\2\2\u00a2\u00a3"+
		"\7p\2\2\u00a3\u00a4\7f\2\2\u00a4\32\3\2\2\2\u00a5\u00a6\7q\2\2\u00a6\u00a7"+
		"\7t\2\2\u00a7\34\3\2\2\2\u00a8\u00a9\7k\2\2\u00a9\u00aa\7p\2\2\u00aa\36"+
		"\3\2\2\2\u00ab\u00ac\7p\2\2\u00ac\u00ad\7w\2\2\u00ad\u00ae\7n\2\2\u00ae"+
		"\u00af\7n\2\2\u00af \3\2\2\2\u00b0\u00b1\7]\2\2\u00b1\"\3\2\2\2\u00b2"+
		"\u00b3\7_\2\2\u00b3$\3\2\2\2\u00b4\u00b5\7}\2\2\u00b5&\3\2\2\2\u00b6\u00b7"+
		"\7\177\2\2\u00b7(\3\2\2\2\u00b8\u00b9\7&\2\2\u00b9*\3\2\2\2\u00ba\u00bb"+
		"\7&\2\2\u00bb\u00bc\7&\2\2\u00bc,\3\2\2\2\u00bd\u00be\7,\2\2\u00be\u00bf"+
		"\7,\2\2\u00bf.\3\2\2\2\u00c0\u00c1\5g\64\2\u00c1\u00c3\7\60\2\2\u00c2"+
		"\u00c4\t\4\2\2\u00c3\u00c2\3\2\2\2\u00c4\u00c5\3\2\2\2\u00c5\u00c3\3\2"+
		"\2\2\u00c5\u00c6\3\2\2\2\u00c6\u00c8\3\2\2\2\u00c7\u00c9\5i\65\2\u00c8"+
		"\u00c7\3\2\2\2\u00c8\u00c9\3\2\2\2\u00c9\u00cf\3\2\2\2\u00ca\u00cb\5g"+
		"\64\2\u00cb\u00cc\5i\65\2\u00cc\u00cf\3\2\2\2\u00cd\u00cf\5g\64\2\u00ce"+
		"\u00c0\3\2\2\2\u00ce\u00ca\3\2\2\2\u00ce\u00cd\3\2\2\2\u00cf\60\3\2\2"+
		"\2\u00d0\u00d1\7h\2\2\u00d1\u00d2\7w\2\2\u00d2\u00d3\7p\2\2\u00d3\u00d4"+
		"\7e\2\2\u00d4\u00d5\7v\2\2\u00d5\u00d6\7k\2\2\u00d6\u00d7\7q\2\2\u00d7"+
		"\u00d8\7p\2\2\u00d8\62\3\2\2\2\u00d9\u00db\t\5\2\2\u00da\u00d9\3\2\2\2"+
		"\u00db\u00dc\3\2\2\2\u00dc\u00da\3\2\2\2\u00dc\u00dd\3\2\2\2\u00dd\u00de"+
		"\3\2\2\2\u00de\u00df\b\32\2\2\u00df\64\3\2\2\2\u00e0\u00e1\7\61\2\2\u00e1"+
		"\u00e2\7,\2\2\u00e2\u00e6\3\2\2\2\u00e3\u00e5\13\2\2\2\u00e4\u00e3\3\2"+
		"\2\2\u00e5\u00e8\3\2\2\2\u00e6\u00e7\3\2\2\2\u00e6\u00e4\3\2\2\2\u00e7"+
		"\u00e9\3\2\2\2\u00e8\u00e6\3\2\2\2\u00e9\u00ea\7,\2\2\u00ea\u00eb\7\61"+
		"\2\2\u00eb\u00ec\3\2\2\2\u00ec\u00ed\b\33\2\2\u00ed\66\3\2\2\2\u00ee\u00ef"+
		"\7\u0080\2\2\u00ef\u00f0\7@\2\2\u00f08\3\2\2\2\u00f1\u00f2\7<\2\2\u00f2"+
		"\u00f3\7?\2\2\u00f3:\3\2\2\2\u00f4\u00f5\7,\2\2\u00f5<\3\2\2\2\u00f6\u00f7"+
		"\7\61\2\2\u00f7>\3\2\2\2\u00f8\u00f9\7-\2\2\u00f9@\3\2\2\2\u00fa\u00fb"+
		"\7/\2\2\u00fbB\3\2\2\2\u00fc\u00fd\7\'\2\2\u00fdD\3\2\2\2\u00fe\u00ff"+
		"\7?\2\2\u00ffF\3\2\2\2\u0100\u0101\7#\2\2\u0101\u0102\7?\2\2\u0102H\3"+
		"\2\2\2\u0103\u0104\7>\2\2\u0104J\3\2\2\2\u0105\u0106\7>\2\2\u0106\u0107"+
		"\7?\2\2\u0107L\3\2\2\2\u0108\u0109\7@\2\2\u0109N\3\2\2\2\u010a\u010b\7"+
		"@\2\2\u010b\u010c\7?\2\2\u010cP\3\2\2\2\u010d\u010e\7(\2\2\u010eR\3\2"+
		"\2\2\u010f\u0110\7&\2\2\u0110\u0111\7g\2\2\u0111\u0112\7c\2\2\u0112\u0113"+
		"\7e\2\2\u0113\u0114\7j\2\2\u0114T\3\2\2\2\u0115\u0116\7&\2\2\u0116\u0117"+
		"\7u\2\2\u0117\u0118\7k\2\2\u0118\u0119\7h\2\2\u0119\u011a\7v\2\2\u011a"+
		"V\3\2\2\2\u011b\u011c\7&\2\2\u011c\u011d\7t\2\2\u011d\u011e\7g\2\2\u011e"+
		"\u011f\7f\2\2\u011f\u0120\7w\2\2\u0120\u0121\7e\2\2\u0121\u0122\7g\2\2"+
		"\u0122X\3\2\2\2\u0123\u0124\7&\2\2\u0124\u0125\7h\2\2\u0125\u0126\7k\2"+
		"\2\u0126\u0127\7n\2\2\u0127\u0128\7v\2\2\u0128\u0129\7g\2\2\u0129\u012a"+
		"\7t\2\2\u012aZ\3\2\2\2\u012b\u012c\7&\2\2\u012c\u012d\7o\2\2\u012d\u012e"+
		"\7c\2\2\u012e\u012f\7r\2\2\u012f\\\3\2\2\2\u0130\u0131\7&\2\2\u0131\u0132"+
		"\5_\60\2\u0132^\3\2\2\2\u0133\u0137\t\6\2\2\u0134\u0136\t\7\2\2\u0135"+
		"\u0134\3\2\2\2\u0136\u0139\3\2\2\2\u0137\u0135\3\2\2\2\u0137\u0138\3\2"+
		"\2\2\u0138\u0144\3\2\2\2\u0139\u0137\3\2\2\2\u013a\u013e\5o8\2\u013b\u013d"+
		"\n\b\2\2\u013c\u013b\3\2\2\2\u013d\u0140\3\2\2\2\u013e\u013c\3\2\2\2\u013e"+
		"\u013f\3\2\2\2\u013f\u0141\3\2\2\2\u0140\u013e\3\2\2\2\u0141\u0142\5o"+
		"8\2\u0142\u0144\3\2\2\2\u0143\u0133\3\2\2\2\u0143\u013a\3\2\2\2\u0144"+
		"`\3\2\2\2\u0145\u0148\7^\2\2\u0146\u0149\t\t\2\2\u0147\u0149\5c\62\2\u0148"+
		"\u0146\3\2\2\2\u0148\u0147\3\2\2\2\u0149b\3\2\2\2\u014a\u014b\7w\2\2\u014b"+
		"\u014c\5e\63\2\u014c\u014d\5e\63\2\u014d\u014e\5e\63\2\u014e\u014f\5e"+
		"\63\2\u014fd\3\2\2\2\u0150\u0151\t\n\2\2\u0151f\3\2\2\2\u0152\u015b\7"+
		"\62\2\2\u0153\u0157\t\13\2\2\u0154\u0156\t\4\2\2\u0155\u0154\3\2\2\2\u0156"+
		"\u0159\3\2\2\2\u0157\u0155\3\2\2\2\u0157\u0158\3\2\2\2\u0158\u015b\3\2"+
		"\2\2\u0159\u0157\3\2\2\2\u015a\u0152\3\2\2\2\u015a\u0153\3\2\2\2\u015b"+
		"h\3\2\2\2\u015c\u015e\t\f\2\2\u015d\u015f\t\r\2\2\u015e\u015d\3\2\2\2"+
		"\u015e\u015f\3\2\2\2\u015f\u0160\3\2\2\2\u0160\u0161\5g\64\2\u0161j\3"+
		"\2\2\2\u0162\u0163\7)\2\2\u0163l\3\2\2\2\u0164\u0165\7$\2\2\u0165n\3\2"+
		"\2\2\u0166\u0167\7b\2\2\u0167p\3\2\2\2\24\2\u0090\u0092\u0099\u009b\u009f"+
		"\u00c5\u00c8\u00ce\u00dc\u00e6\u0137\u013e\u0143\u0148\u0157\u015a\u015e"+
		"\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}