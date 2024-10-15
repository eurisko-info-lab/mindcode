// Generated from Schemacode.g4 by ANTLR 4.13.1
package info.teksol.schemacode.grammar;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class SchemacodeLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		As=1, At=2, Block=3, Connected=4, Command=5, Color=6, Description=7, Dimensions=8, 
		Disabled=9, Enabled=10, End=11, Facing=12, File=13, Item=14, Links=15, 
		Liquid=16, Logic=17, Mindcode=18, Mlog=19, Name=20, Processor=21, Rgba=22, 
		Schematic=23, Tag=24, Text=25, To=26, Unit=27, Virtual=28, Assign=29, 
		Colon=30, Comma=31, Dot=32, Minus=33, Plus=34, North=35, South=36, East=37, 
		West=38, LeftParen=39, RightParen=40, TextBlock1=41, TextBlock2=42, TextLine=43, 
		Int=44, Id=45, Ref=46, Pattern=47, Comment=48, SLComment=49, Ws=50, Any=51;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"As", "At", "Block", "Connected", "Command", "Color", "Description", 
			"Dimensions", "Disabled", "Enabled", "End", "Facing", "File", "Item", 
			"Links", "Liquid", "Logic", "Mindcode", "Mlog", "Name", "Processor", 
			"Rgba", "Schematic", "Tag", "Text", "To", "Unit", "Virtual", "Assign", 
			"Colon", "Comma", "Dot", "Minus", "Plus", "North", "South", "East", "West", 
			"LeftParen", "RightParen", "TextBlock1", "TextBlock2", "TextLine", "Int", 
			"Id", "Ref", "Pattern", "Comment", "SLComment", "Ws", "Any"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'as'", "'at'", "'block'", "'connected'", "'command'", "'color'", 
			"'description'", "'dimensions'", "'disabled'", "'enabled'", "'end'", 
			"'facing'", "'file'", "'item'", "'links'", "'liquid'", "'logic'", "'mindcode'", 
			"'mlog'", "'name'", "'processor'", "'rgba'", "'schematic'", "'tag'", 
			"'text'", "'to'", "'unit'", "'virtual'", "'='", "':'", "','", "'.'", 
			"'-'", "'+'", "'north'", "'south'", "'east'", "'west'", "'('", "')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "As", "At", "Block", "Connected", "Command", "Color", "Description", 
			"Dimensions", "Disabled", "Enabled", "End", "Facing", "File", "Item", 
			"Links", "Liquid", "Logic", "Mindcode", "Mlog", "Name", "Processor", 
			"Rgba", "Schematic", "Tag", "Text", "To", "Unit", "Virtual", "Assign", 
			"Colon", "Comma", "Dot", "Minus", "Plus", "North", "South", "East", "West", 
			"LeftParen", "RightParen", "TextBlock1", "TextBlock2", "TextLine", "Int", 
			"Id", "Ref", "Pattern", "Comment", "SLComment", "Ws", "Any"
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


	public SchemacodeLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Schemacode.g4"; }

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
		"\u0004\u00003\u01be\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b"+
		"\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002"+
		"\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002"+
		"\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002"+
		"\u0015\u0007\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002"+
		"\u0018\u0007\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002"+
		"\u001b\u0007\u001b\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002"+
		"\u001e\u0007\u001e\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007"+
		"!\u0002\"\u0007\"\u0002#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007"+
		"&\u0002\'\u0007\'\u0002(\u0007(\u0002)\u0007)\u0002*\u0007*\u0002+\u0007"+
		"+\u0002,\u0007,\u0002-\u0007-\u0002.\u0007.\u0002/\u0007/\u00020\u0007"+
		"0\u00021\u00071\u00022\u00072\u0001\u0000\u0001\u0000\u0001\u0000\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001"+
		"\b\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\n\u0001\n\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\f\u0001\f"+
		"\u0001\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001"+
		"\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001"+
		"\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0016\u0001"+
		"\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001"+
		"\u0016\u0001\u0016\u0001\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0017\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0001\u001a\u0001\u001a\u0001\u001a\u0001"+
		"\u001a\u0001\u001a\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001"+
		"\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001c\u0001\u001c\u0001"+
		"\u001d\u0001\u001d\u0001\u001e\u0001\u001e\u0001\u001f\u0001\u001f\u0001"+
		" \u0001 \u0001!\u0001!\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001\""+
		"\u0001#\u0001#\u0001#\u0001#\u0001#\u0001#\u0001$\u0001$\u0001$\u0001"+
		"$\u0001$\u0001%\u0001%\u0001%\u0001%\u0001%\u0001&\u0001&\u0001\'\u0001"+
		"\'\u0001(\u0001(\u0001(\u0001(\u0001(\u0005(\u014c\b(\n(\f(\u014f\t(\u0001"+
		"(\u0001(\u0005(\u0153\b(\n(\f(\u0156\t(\u0001(\u0001(\u0001(\u0001(\u0001"+
		")\u0001)\u0001)\u0001)\u0001)\u0005)\u0161\b)\n)\f)\u0164\t)\u0001)\u0001"+
		")\u0005)\u0168\b)\n)\f)\u016b\t)\u0001)\u0001)\u0001)\u0001)\u0001*\u0001"+
		"*\u0005*\u0173\b*\n*\f*\u0176\t*\u0001*\u0001*\u0001+\u0003+\u017b\b+"+
		"\u0001+\u0004+\u017e\b+\u000b+\f+\u017f\u0001,\u0001,\u0005,\u0184\b,"+
		"\n,\f,\u0187\t,\u0001-\u0001-\u0001-\u0005-\u018c\b-\n-\f-\u018f\t-\u0001"+
		".\u0001.\u0005.\u0193\b.\n.\f.\u0196\t.\u0001/\u0001/\u0001/\u0001/\u0005"+
		"/\u019c\b/\n/\f/\u019f\t/\u0001/\u0001/\u0001/\u0001/\u0001/\u00010\u0001"+
		"0\u00010\u00010\u00050\u01aa\b0\n0\f0\u01ad\t0\u00010\u00030\u01b0\b0"+
		"\u00010\u00010\u00010\u00010\u00011\u00041\u01b7\b1\u000b1\f1\u01b8\u0001"+
		"1\u00011\u00012\u00012\u0004\u0154\u0169\u0174\u019d\u00003\u0001\u0001"+
		"\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007\u000f"+
		"\b\u0011\t\u0013\n\u0015\u000b\u0017\f\u0019\r\u001b\u000e\u001d\u000f"+
		"\u001f\u0010!\u0011#\u0012%\u0013\'\u0014)\u0015+\u0016-\u0017/\u0018"+
		"1\u00193\u001a5\u001b7\u001c9\u001d;\u001e=\u001f? A!C\"E#G$I%K&M\'O("+
		"Q)S*U+W,Y-[.]/_0a1c2e3\u0001\u0000\t\u0002\u0000\t\t  \u0002\u0000\n\n"+
		"\r\r\u0002\u0000++--\u0001\u000009\u0003\u0000AZ__az\u0005\u0000--09A"+
		"Z__az\u0004\u0000**AZ__az\u0006\u0000**--09AZ__az\u0003\u0000\t\n\r\r"+
		"  \u01cb\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001\u0000\u0000"+
		"\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000"+
		"\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000"+
		"\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000\u0000\u0000\u0000"+
		"\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000\u0000\u0000\u0000"+
		"\u0015\u0001\u0000\u0000\u0000\u0000\u0017\u0001\u0000\u0000\u0000\u0000"+
		"\u0019\u0001\u0000\u0000\u0000\u0000\u001b\u0001\u0000\u0000\u0000\u0000"+
		"\u001d\u0001\u0000\u0000\u0000\u0000\u001f\u0001\u0000\u0000\u0000\u0000"+
		"!\u0001\u0000\u0000\u0000\u0000#\u0001\u0000\u0000\u0000\u0000%\u0001"+
		"\u0000\u0000\u0000\u0000\'\u0001\u0000\u0000\u0000\u0000)\u0001\u0000"+
		"\u0000\u0000\u0000+\u0001\u0000\u0000\u0000\u0000-\u0001\u0000\u0000\u0000"+
		"\u0000/\u0001\u0000\u0000\u0000\u00001\u0001\u0000\u0000\u0000\u00003"+
		"\u0001\u0000\u0000\u0000\u00005\u0001\u0000\u0000\u0000\u00007\u0001\u0000"+
		"\u0000\u0000\u00009\u0001\u0000\u0000\u0000\u0000;\u0001\u0000\u0000\u0000"+
		"\u0000=\u0001\u0000\u0000\u0000\u0000?\u0001\u0000\u0000\u0000\u0000A"+
		"\u0001\u0000\u0000\u0000\u0000C\u0001\u0000\u0000\u0000\u0000E\u0001\u0000"+
		"\u0000\u0000\u0000G\u0001\u0000\u0000\u0000\u0000I\u0001\u0000\u0000\u0000"+
		"\u0000K\u0001\u0000\u0000\u0000\u0000M\u0001\u0000\u0000\u0000\u0000O"+
		"\u0001\u0000\u0000\u0000\u0000Q\u0001\u0000\u0000\u0000\u0000S\u0001\u0000"+
		"\u0000\u0000\u0000U\u0001\u0000\u0000\u0000\u0000W\u0001\u0000\u0000\u0000"+
		"\u0000Y\u0001\u0000\u0000\u0000\u0000[\u0001\u0000\u0000\u0000\u0000]"+
		"\u0001\u0000\u0000\u0000\u0000_\u0001\u0000\u0000\u0000\u0000a\u0001\u0000"+
		"\u0000\u0000\u0000c\u0001\u0000\u0000\u0000\u0000e\u0001\u0000\u0000\u0000"+
		"\u0001g\u0001\u0000\u0000\u0000\u0003j\u0001\u0000\u0000\u0000\u0005m"+
		"\u0001\u0000\u0000\u0000\u0007s\u0001\u0000\u0000\u0000\t}\u0001\u0000"+
		"\u0000\u0000\u000b\u0085\u0001\u0000\u0000\u0000\r\u008b\u0001\u0000\u0000"+
		"\u0000\u000f\u0097\u0001\u0000\u0000\u0000\u0011\u00a2\u0001\u0000\u0000"+
		"\u0000\u0013\u00ab\u0001\u0000\u0000\u0000\u0015\u00b3\u0001\u0000\u0000"+
		"\u0000\u0017\u00b7\u0001\u0000\u0000\u0000\u0019\u00be\u0001\u0000\u0000"+
		"\u0000\u001b\u00c3\u0001\u0000\u0000\u0000\u001d\u00c8\u0001\u0000\u0000"+
		"\u0000\u001f\u00ce\u0001\u0000\u0000\u0000!\u00d5\u0001\u0000\u0000\u0000"+
		"#\u00db\u0001\u0000\u0000\u0000%\u00e4\u0001\u0000\u0000\u0000\'\u00e9"+
		"\u0001\u0000\u0000\u0000)\u00ee\u0001\u0000\u0000\u0000+\u00f8\u0001\u0000"+
		"\u0000\u0000-\u00fd\u0001\u0000\u0000\u0000/\u0107\u0001\u0000\u0000\u0000"+
		"1\u010b\u0001\u0000\u0000\u00003\u0110\u0001\u0000\u0000\u00005\u0113"+
		"\u0001\u0000\u0000\u00007\u0118\u0001\u0000\u0000\u00009\u0120\u0001\u0000"+
		"\u0000\u0000;\u0122\u0001\u0000\u0000\u0000=\u0124\u0001\u0000\u0000\u0000"+
		"?\u0126\u0001\u0000\u0000\u0000A\u0128\u0001\u0000\u0000\u0000C\u012a"+
		"\u0001\u0000\u0000\u0000E\u012c\u0001\u0000\u0000\u0000G\u0132\u0001\u0000"+
		"\u0000\u0000I\u0138\u0001\u0000\u0000\u0000K\u013d\u0001\u0000\u0000\u0000"+
		"M\u0142\u0001\u0000\u0000\u0000O\u0144\u0001\u0000\u0000\u0000Q\u0146"+
		"\u0001\u0000\u0000\u0000S\u015b\u0001\u0000\u0000\u0000U\u0170\u0001\u0000"+
		"\u0000\u0000W\u017a\u0001\u0000\u0000\u0000Y\u0181\u0001\u0000\u0000\u0000"+
		"[\u0188\u0001\u0000\u0000\u0000]\u0190\u0001\u0000\u0000\u0000_\u0197"+
		"\u0001\u0000\u0000\u0000a\u01a5\u0001\u0000\u0000\u0000c\u01b6\u0001\u0000"+
		"\u0000\u0000e\u01bc\u0001\u0000\u0000\u0000gh\u0005a\u0000\u0000hi\u0005"+
		"s\u0000\u0000i\u0002\u0001\u0000\u0000\u0000jk\u0005a\u0000\u0000kl\u0005"+
		"t\u0000\u0000l\u0004\u0001\u0000\u0000\u0000mn\u0005b\u0000\u0000no\u0005"+
		"l\u0000\u0000op\u0005o\u0000\u0000pq\u0005c\u0000\u0000qr\u0005k\u0000"+
		"\u0000r\u0006\u0001\u0000\u0000\u0000st\u0005c\u0000\u0000tu\u0005o\u0000"+
		"\u0000uv\u0005n\u0000\u0000vw\u0005n\u0000\u0000wx\u0005e\u0000\u0000"+
		"xy\u0005c\u0000\u0000yz\u0005t\u0000\u0000z{\u0005e\u0000\u0000{|\u0005"+
		"d\u0000\u0000|\b\u0001\u0000\u0000\u0000}~\u0005c\u0000\u0000~\u007f\u0005"+
		"o\u0000\u0000\u007f\u0080\u0005m\u0000\u0000\u0080\u0081\u0005m\u0000"+
		"\u0000\u0081\u0082\u0005a\u0000\u0000\u0082\u0083\u0005n\u0000\u0000\u0083"+
		"\u0084\u0005d\u0000\u0000\u0084\n\u0001\u0000\u0000\u0000\u0085\u0086"+
		"\u0005c\u0000\u0000\u0086\u0087\u0005o\u0000\u0000\u0087\u0088\u0005l"+
		"\u0000\u0000\u0088\u0089\u0005o\u0000\u0000\u0089\u008a\u0005r\u0000\u0000"+
		"\u008a\f\u0001\u0000\u0000\u0000\u008b\u008c\u0005d\u0000\u0000\u008c"+
		"\u008d\u0005e\u0000\u0000\u008d\u008e\u0005s\u0000\u0000\u008e\u008f\u0005"+
		"c\u0000\u0000\u008f\u0090\u0005r\u0000\u0000\u0090\u0091\u0005i\u0000"+
		"\u0000\u0091\u0092\u0005p\u0000\u0000\u0092\u0093\u0005t\u0000\u0000\u0093"+
		"\u0094\u0005i\u0000\u0000\u0094\u0095\u0005o\u0000\u0000\u0095\u0096\u0005"+
		"n\u0000\u0000\u0096\u000e\u0001\u0000\u0000\u0000\u0097\u0098\u0005d\u0000"+
		"\u0000\u0098\u0099\u0005i\u0000\u0000\u0099\u009a\u0005m\u0000\u0000\u009a"+
		"\u009b\u0005e\u0000\u0000\u009b\u009c\u0005n\u0000\u0000\u009c\u009d\u0005"+
		"s\u0000\u0000\u009d\u009e\u0005i\u0000\u0000\u009e\u009f\u0005o\u0000"+
		"\u0000\u009f\u00a0\u0005n\u0000\u0000\u00a0\u00a1\u0005s\u0000\u0000\u00a1"+
		"\u0010\u0001\u0000\u0000\u0000\u00a2\u00a3\u0005d\u0000\u0000\u00a3\u00a4"+
		"\u0005i\u0000\u0000\u00a4\u00a5\u0005s\u0000\u0000\u00a5\u00a6\u0005a"+
		"\u0000\u0000\u00a6\u00a7\u0005b\u0000\u0000\u00a7\u00a8\u0005l\u0000\u0000"+
		"\u00a8\u00a9\u0005e\u0000\u0000\u00a9\u00aa\u0005d\u0000\u0000\u00aa\u0012"+
		"\u0001\u0000\u0000\u0000\u00ab\u00ac\u0005e\u0000\u0000\u00ac\u00ad\u0005"+
		"n\u0000\u0000\u00ad\u00ae\u0005a\u0000\u0000\u00ae\u00af\u0005b\u0000"+
		"\u0000\u00af\u00b0\u0005l\u0000\u0000\u00b0\u00b1\u0005e\u0000\u0000\u00b1"+
		"\u00b2\u0005d\u0000\u0000\u00b2\u0014\u0001\u0000\u0000\u0000\u00b3\u00b4"+
		"\u0005e\u0000\u0000\u00b4\u00b5\u0005n\u0000\u0000\u00b5\u00b6\u0005d"+
		"\u0000\u0000\u00b6\u0016\u0001\u0000\u0000\u0000\u00b7\u00b8\u0005f\u0000"+
		"\u0000\u00b8\u00b9\u0005a\u0000\u0000\u00b9\u00ba\u0005c\u0000\u0000\u00ba"+
		"\u00bb\u0005i\u0000\u0000\u00bb\u00bc\u0005n\u0000\u0000\u00bc\u00bd\u0005"+
		"g\u0000\u0000\u00bd\u0018\u0001\u0000\u0000\u0000\u00be\u00bf\u0005f\u0000"+
		"\u0000\u00bf\u00c0\u0005i\u0000\u0000\u00c0\u00c1\u0005l\u0000\u0000\u00c1"+
		"\u00c2\u0005e\u0000\u0000\u00c2\u001a\u0001\u0000\u0000\u0000\u00c3\u00c4"+
		"\u0005i\u0000\u0000\u00c4\u00c5\u0005t\u0000\u0000\u00c5\u00c6\u0005e"+
		"\u0000\u0000\u00c6\u00c7\u0005m\u0000\u0000\u00c7\u001c\u0001\u0000\u0000"+
		"\u0000\u00c8\u00c9\u0005l\u0000\u0000\u00c9\u00ca\u0005i\u0000\u0000\u00ca"+
		"\u00cb\u0005n\u0000\u0000\u00cb\u00cc\u0005k\u0000\u0000\u00cc\u00cd\u0005"+
		"s\u0000\u0000\u00cd\u001e\u0001\u0000\u0000\u0000\u00ce\u00cf\u0005l\u0000"+
		"\u0000\u00cf\u00d0\u0005i\u0000\u0000\u00d0\u00d1\u0005q\u0000\u0000\u00d1"+
		"\u00d2\u0005u\u0000\u0000\u00d2\u00d3\u0005i\u0000\u0000\u00d3\u00d4\u0005"+
		"d\u0000\u0000\u00d4 \u0001\u0000\u0000\u0000\u00d5\u00d6\u0005l\u0000"+
		"\u0000\u00d6\u00d7\u0005o\u0000\u0000\u00d7\u00d8\u0005g\u0000\u0000\u00d8"+
		"\u00d9\u0005i\u0000\u0000\u00d9\u00da\u0005c\u0000\u0000\u00da\"\u0001"+
		"\u0000\u0000\u0000\u00db\u00dc\u0005m\u0000\u0000\u00dc\u00dd\u0005i\u0000"+
		"\u0000\u00dd\u00de\u0005n\u0000\u0000\u00de\u00df\u0005d\u0000\u0000\u00df"+
		"\u00e0\u0005c\u0000\u0000\u00e0\u00e1\u0005o\u0000\u0000\u00e1\u00e2\u0005"+
		"d\u0000\u0000\u00e2\u00e3\u0005e\u0000\u0000\u00e3$\u0001\u0000\u0000"+
		"\u0000\u00e4\u00e5\u0005m\u0000\u0000\u00e5\u00e6\u0005l\u0000\u0000\u00e6"+
		"\u00e7\u0005o\u0000\u0000\u00e7\u00e8\u0005g\u0000\u0000\u00e8&\u0001"+
		"\u0000\u0000\u0000\u00e9\u00ea\u0005n\u0000\u0000\u00ea\u00eb\u0005a\u0000"+
		"\u0000\u00eb\u00ec\u0005m\u0000\u0000\u00ec\u00ed\u0005e\u0000\u0000\u00ed"+
		"(\u0001\u0000\u0000\u0000\u00ee\u00ef\u0005p\u0000\u0000\u00ef\u00f0\u0005"+
		"r\u0000\u0000\u00f0\u00f1\u0005o\u0000\u0000\u00f1\u00f2\u0005c\u0000"+
		"\u0000\u00f2\u00f3\u0005e\u0000\u0000\u00f3\u00f4\u0005s\u0000\u0000\u00f4"+
		"\u00f5\u0005s\u0000\u0000\u00f5\u00f6\u0005o\u0000\u0000\u00f6\u00f7\u0005"+
		"r\u0000\u0000\u00f7*\u0001\u0000\u0000\u0000\u00f8\u00f9\u0005r\u0000"+
		"\u0000\u00f9\u00fa\u0005g\u0000\u0000\u00fa\u00fb\u0005b\u0000\u0000\u00fb"+
		"\u00fc\u0005a\u0000\u0000\u00fc,\u0001\u0000\u0000\u0000\u00fd\u00fe\u0005"+
		"s\u0000\u0000\u00fe\u00ff\u0005c\u0000\u0000\u00ff\u0100\u0005h\u0000"+
		"\u0000\u0100\u0101\u0005e\u0000\u0000\u0101\u0102\u0005m\u0000\u0000\u0102"+
		"\u0103\u0005a\u0000\u0000\u0103\u0104\u0005t\u0000\u0000\u0104\u0105\u0005"+
		"i\u0000\u0000\u0105\u0106\u0005c\u0000\u0000\u0106.\u0001\u0000\u0000"+
		"\u0000\u0107\u0108\u0005t\u0000\u0000\u0108\u0109\u0005a\u0000\u0000\u0109"+
		"\u010a\u0005g\u0000\u0000\u010a0\u0001\u0000\u0000\u0000\u010b\u010c\u0005"+
		"t\u0000\u0000\u010c\u010d\u0005e\u0000\u0000\u010d\u010e\u0005x\u0000"+
		"\u0000\u010e\u010f\u0005t\u0000\u0000\u010f2\u0001\u0000\u0000\u0000\u0110"+
		"\u0111\u0005t\u0000\u0000\u0111\u0112\u0005o\u0000\u0000\u01124\u0001"+
		"\u0000\u0000\u0000\u0113\u0114\u0005u\u0000\u0000\u0114\u0115\u0005n\u0000"+
		"\u0000\u0115\u0116\u0005i\u0000\u0000\u0116\u0117\u0005t\u0000\u0000\u0117"+
		"6\u0001\u0000\u0000\u0000\u0118\u0119\u0005v\u0000\u0000\u0119\u011a\u0005"+
		"i\u0000\u0000\u011a\u011b\u0005r\u0000\u0000\u011b\u011c\u0005t\u0000"+
		"\u0000\u011c\u011d\u0005u\u0000\u0000\u011d\u011e\u0005a\u0000\u0000\u011e"+
		"\u011f\u0005l\u0000\u0000\u011f8\u0001\u0000\u0000\u0000\u0120\u0121\u0005"+
		"=\u0000\u0000\u0121:\u0001\u0000\u0000\u0000\u0122\u0123\u0005:\u0000"+
		"\u0000\u0123<\u0001\u0000\u0000\u0000\u0124\u0125\u0005,\u0000\u0000\u0125"+
		">\u0001\u0000\u0000\u0000\u0126\u0127\u0005.\u0000\u0000\u0127@\u0001"+
		"\u0000\u0000\u0000\u0128\u0129\u0005-\u0000\u0000\u0129B\u0001\u0000\u0000"+
		"\u0000\u012a\u012b\u0005+\u0000\u0000\u012bD\u0001\u0000\u0000\u0000\u012c"+
		"\u012d\u0005n\u0000\u0000\u012d\u012e\u0005o\u0000\u0000\u012e\u012f\u0005"+
		"r\u0000\u0000\u012f\u0130\u0005t\u0000\u0000\u0130\u0131\u0005h\u0000"+
		"\u0000\u0131F\u0001\u0000\u0000\u0000\u0132\u0133\u0005s\u0000\u0000\u0133"+
		"\u0134\u0005o\u0000\u0000\u0134\u0135\u0005u\u0000\u0000\u0135\u0136\u0005"+
		"t\u0000\u0000\u0136\u0137\u0005h\u0000\u0000\u0137H\u0001\u0000\u0000"+
		"\u0000\u0138\u0139\u0005e\u0000\u0000\u0139\u013a\u0005a\u0000\u0000\u013a"+
		"\u013b\u0005s\u0000\u0000\u013b\u013c\u0005t\u0000\u0000\u013cJ\u0001"+
		"\u0000\u0000\u0000\u013d\u013e\u0005w\u0000\u0000\u013e\u013f\u0005e\u0000"+
		"\u0000\u013f\u0140\u0005s\u0000\u0000\u0140\u0141\u0005t\u0000\u0000\u0141"+
		"L\u0001\u0000\u0000\u0000\u0142\u0143\u0005(\u0000\u0000\u0143N\u0001"+
		"\u0000\u0000\u0000\u0144\u0145\u0005)\u0000\u0000\u0145P\u0001\u0000\u0000"+
		"\u0000\u0146\u0147\u0005\"\u0000\u0000\u0147\u0148\u0005\"\u0000\u0000"+
		"\u0148\u0149\u0005\"\u0000\u0000\u0149\u014d\u0001\u0000\u0000\u0000\u014a"+
		"\u014c\u0007\u0000\u0000\u0000\u014b\u014a\u0001\u0000\u0000\u0000\u014c"+
		"\u014f\u0001\u0000\u0000\u0000\u014d\u014b\u0001\u0000\u0000\u0000\u014d"+
		"\u014e\u0001\u0000\u0000\u0000\u014e\u0150\u0001\u0000\u0000\u0000\u014f"+
		"\u014d\u0001\u0000\u0000\u0000\u0150\u0154\u0007\u0001\u0000\u0000\u0151"+
		"\u0153\t\u0000\u0000\u0000\u0152\u0151\u0001\u0000\u0000\u0000\u0153\u0156"+
		"\u0001\u0000\u0000\u0000\u0154\u0155\u0001\u0000\u0000\u0000\u0154\u0152"+
		"\u0001\u0000\u0000\u0000\u0155\u0157\u0001\u0000\u0000\u0000\u0156\u0154"+
		"\u0001\u0000\u0000\u0000\u0157\u0158\u0005\"\u0000\u0000\u0158\u0159\u0005"+
		"\"\u0000\u0000\u0159\u015a\u0005\"\u0000\u0000\u015aR\u0001\u0000\u0000"+
		"\u0000\u015b\u015c\u0005\'\u0000\u0000\u015c\u015d\u0005\'\u0000\u0000"+
		"\u015d\u015e\u0005\'\u0000\u0000\u015e\u0162\u0001\u0000\u0000\u0000\u015f"+
		"\u0161\u0007\u0000\u0000\u0000\u0160\u015f\u0001\u0000\u0000\u0000\u0161"+
		"\u0164\u0001\u0000\u0000\u0000\u0162\u0160\u0001\u0000\u0000\u0000\u0162"+
		"\u0163\u0001\u0000\u0000\u0000\u0163\u0165\u0001\u0000\u0000\u0000\u0164"+
		"\u0162\u0001\u0000\u0000\u0000\u0165\u0169\u0007\u0001\u0000\u0000\u0166"+
		"\u0168\t\u0000\u0000\u0000\u0167\u0166\u0001\u0000\u0000\u0000\u0168\u016b"+
		"\u0001\u0000\u0000\u0000\u0169\u016a\u0001\u0000\u0000\u0000\u0169\u0167"+
		"\u0001\u0000\u0000\u0000\u016a\u016c\u0001\u0000\u0000\u0000\u016b\u0169"+
		"\u0001\u0000\u0000\u0000\u016c\u016d\u0005\'\u0000\u0000\u016d\u016e\u0005"+
		"\'\u0000\u0000\u016e\u016f\u0005\'\u0000\u0000\u016fT\u0001\u0000\u0000"+
		"\u0000\u0170\u0174\u0005\"\u0000\u0000\u0171\u0173\b\u0001\u0000\u0000"+
		"\u0172\u0171\u0001\u0000\u0000\u0000\u0173\u0176\u0001\u0000\u0000\u0000"+
		"\u0174\u0175\u0001\u0000\u0000\u0000\u0174\u0172\u0001\u0000\u0000\u0000"+
		"\u0175\u0177\u0001\u0000\u0000\u0000\u0176\u0174\u0001\u0000\u0000\u0000"+
		"\u0177\u0178\u0005\"\u0000\u0000\u0178V\u0001\u0000\u0000\u0000\u0179"+
		"\u017b\u0007\u0002\u0000\u0000\u017a\u0179\u0001\u0000\u0000\u0000\u017a"+
		"\u017b\u0001\u0000\u0000\u0000\u017b\u017d\u0001\u0000\u0000\u0000\u017c"+
		"\u017e\u0007\u0003\u0000\u0000\u017d\u017c\u0001\u0000\u0000\u0000\u017e"+
		"\u017f\u0001\u0000\u0000\u0000\u017f\u017d\u0001\u0000\u0000\u0000\u017f"+
		"\u0180\u0001\u0000\u0000\u0000\u0180X\u0001\u0000\u0000\u0000\u0181\u0185"+
		"\u0007\u0004\u0000\u0000\u0182\u0184\u0007\u0005\u0000\u0000\u0183\u0182"+
		"\u0001\u0000\u0000\u0000\u0184\u0187\u0001\u0000\u0000\u0000\u0185\u0183"+
		"\u0001\u0000\u0000\u0000\u0185\u0186\u0001\u0000\u0000\u0000\u0186Z\u0001"+
		"\u0000\u0000\u0000\u0187\u0185\u0001\u0000\u0000\u0000\u0188\u0189\u0005"+
		"@\u0000\u0000\u0189\u018d\u0007\u0004\u0000\u0000\u018a\u018c\u0007\u0005"+
		"\u0000\u0000\u018b\u018a\u0001\u0000\u0000\u0000\u018c\u018f\u0001\u0000"+
		"\u0000\u0000\u018d\u018b\u0001\u0000\u0000\u0000\u018d\u018e\u0001\u0000"+
		"\u0000\u0000\u018e\\\u0001\u0000\u0000\u0000\u018f\u018d\u0001\u0000\u0000"+
		"\u0000\u0190\u0194\u0007\u0006\u0000\u0000\u0191\u0193\u0007\u0007\u0000"+
		"\u0000\u0192\u0191\u0001\u0000\u0000\u0000\u0193\u0196\u0001\u0000\u0000"+
		"\u0000\u0194\u0192\u0001\u0000\u0000\u0000\u0194\u0195\u0001\u0000\u0000"+
		"\u0000\u0195^\u0001\u0000\u0000\u0000\u0196\u0194\u0001\u0000\u0000\u0000"+
		"\u0197\u0198\u0005/\u0000\u0000\u0198\u0199\u0005*\u0000\u0000\u0199\u019d"+
		"\u0001\u0000\u0000\u0000\u019a\u019c\t\u0000\u0000\u0000\u019b\u019a\u0001"+
		"\u0000\u0000\u0000\u019c\u019f\u0001\u0000\u0000\u0000\u019d\u019e\u0001"+
		"\u0000\u0000\u0000\u019d\u019b\u0001\u0000\u0000\u0000\u019e\u01a0\u0001"+
		"\u0000\u0000\u0000\u019f\u019d\u0001\u0000\u0000\u0000\u01a0\u01a1\u0005"+
		"*\u0000\u0000\u01a1\u01a2\u0005/\u0000\u0000\u01a2\u01a3\u0001\u0000\u0000"+
		"\u0000\u01a3\u01a4\u0006/\u0000\u0000\u01a4`\u0001\u0000\u0000\u0000\u01a5"+
		"\u01a6\u0005/\u0000\u0000\u01a6\u01a7\u0005/\u0000\u0000\u01a7\u01ab\u0001"+
		"\u0000\u0000\u0000\u01a8\u01aa\b\u0001\u0000\u0000\u01a9\u01a8\u0001\u0000"+
		"\u0000\u0000\u01aa\u01ad\u0001\u0000\u0000\u0000\u01ab\u01a9\u0001\u0000"+
		"\u0000\u0000\u01ab\u01ac\u0001\u0000\u0000\u0000\u01ac\u01af\u0001\u0000"+
		"\u0000\u0000\u01ad\u01ab\u0001\u0000\u0000\u0000\u01ae\u01b0\u0005\r\u0000"+
		"\u0000\u01af\u01ae\u0001\u0000\u0000\u0000\u01af\u01b0\u0001\u0000\u0000"+
		"\u0000\u01b0\u01b1\u0001\u0000\u0000\u0000\u01b1\u01b2\u0005\n\u0000\u0000"+
		"\u01b2\u01b3\u0001\u0000\u0000\u0000\u01b3\u01b4\u00060\u0000\u0000\u01b4"+
		"b\u0001\u0000\u0000\u0000\u01b5\u01b7\u0007\b\u0000\u0000\u01b6\u01b5"+
		"\u0001\u0000\u0000\u0000\u01b7\u01b8\u0001\u0000\u0000\u0000\u01b8\u01b6"+
		"\u0001\u0000\u0000\u0000\u01b8\u01b9\u0001\u0000\u0000\u0000\u01b9\u01ba"+
		"\u0001\u0000\u0000\u0000\u01ba\u01bb\u00061\u0000\u0000\u01bbd\u0001\u0000"+
		"\u0000\u0000\u01bc\u01bd\t\u0000\u0000\u0000\u01bdf\u0001\u0000\u0000"+
		"\u0000\u000f\u0000\u014d\u0154\u0162\u0169\u0174\u017a\u017f\u0185\u018d"+
		"\u0194\u019d\u01ab\u01af\u01b8\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}