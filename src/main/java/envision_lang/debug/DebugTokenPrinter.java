package envision_lang.debug;

import java.io.IOException;

import envision_lang._launch.EnvisionCodeFile;
import envision_lang.tokenizer.Token;
import eutil.datatypes.boxes.BoxList;
import eutil.datatypes.util.EList;
import eutil.strings.EStringBuilder;
import eutil.strings.EStringUtil;

public class DebugTokenPrinter {
	
	public static void printTokensBasic(EnvisionCodeFile codeFile) {
		printTokensBasic(codeFile.getFileName(), codeFile.getLines(), codeFile.getLineTokens());
	}
	
	public static void printTokensBasic(String fileName,
										EList<String> lines,
										BoxList<Integer, EList<Token<?>>> lineTokens)
	{
		var sb = new EStringBuilder();
		sb.println("'", fileName, "' Tokens:");
		for (int i = 0; i < lineTokens.size(); i++) {
			var line = lineTokens.get(i);
			var toPrint = new EStringBuilder(line.getA());
			toPrint.append("\t");
			for (var t : line.getB()) toPrint.append(t.toString()).append(" ");
			if (lines.isNotEmpty()) toPrint.deleteCharAt(toPrint.length() - 1);
			if (i < lineTokens.size() - 1) sb.println(toPrint);
			else sb.print(toPrint);
		}
		sb.println();
		
		System.out.println(sb);
	}
	
	/**
	 * Prints out a code file's tokens with formatted metadata.
	 * 
	 * @throws IOException
	 */
	public static void printTokensInDepth(EnvisionCodeFile codeFile) {
		printTokensInDepth(codeFile.getFileName(), codeFile.getTokens());
	}
	
	/**
	 * Prints out the following tokens with formatted metadata.
	 * 
	 * @throws IOException
	 */
	public static void printTokensInDepth(String fileName, EList<Token<?>> tokens) {
		class Longest {
			String longestToken() { return EStringUtil.getLongest(tokens); }
			String longestKeyword() { return EStringUtil.getLongest(tokens.map(o -> o.getKeyword())); }
			String longestLexeme() { return EStringUtil.getLongest(tokens.map(o -> o.getLexeme())); }
			String longestLiteral() { return EStringUtil.getLongest(tokens.map(o -> o.getLiteral())); }
			String longestLineNum() { return EStringUtil.getLongest(tokens.map(o -> o.getLineNum())); }
			String longestLineIndex() { return EStringUtil.getLongest(tokens.map(o -> o.getLineIndex())); }
			String longestLineTokenIndex() { return EStringUtil.getLongest(tokens.map(o -> o.getLineTokenIndex())); }
		}
		
		var TLen = new Longest();
		
		var sb = new EStringBuilder();
		sb.println("'", fileName, "' Tokens:");
		
		int longestIndex = EStringUtil.getLongestLength("  i=" + String.valueOf(tokens.size()), "Index");
		int longestLine = EStringUtil.getLongestLength(TLen.longestLineNum(), "Line");
		int longestToken = EStringUtil.getLongestLength(TLen.longestToken(), "Token");
		int longestKeyword = EStringUtil.getLongestLength(TLen.longestKeyword(), "Keyword");
		int longestLexeme = EStringUtil.getLongestLength(TLen.longestLexeme(), "Lexeme");
		int longestLiteral = EStringUtil.getLongestLength(TLen.longestLiteral(), "Literal");
		int longestTokenIndex = EStringUtil.getLongestLength(TLen.longestLineIndex(), "Line Index");
		int longestLineTokenIndex = EStringUtil.getLongestLength(TLen.longestLineTokenIndex(), "Line Token Index");
		
		String indexFormat = "%-" + longestIndex + "s  | ";
		String lineFormat = "%-" + longestLine + "s | ";
		String tokenFormat = "%-" + longestToken + "s | ";
		String keywordFormat = "%-" + longestKeyword + "s | ";
		String lexemeFormat = "%-" + longestLexeme + "s | ";
		String literalFormat = "%-" + longestLiteral + "s | ";
		String lineIndexFormat = "%-" + longestTokenIndex + "s | ";
		String lineTokenIndexFormat = "%-" + longestLineTokenIndex + "s";
		
		String hIndex = String.format("\t| " + indexFormat, "Index");
		String hLine = String.format(lineFormat, "Line");
		String hToken = String.format(tokenFormat, "Token");
		String hKeyword = String.format(keywordFormat, "Keyword");
		String hLexeme = String.format(lexemeFormat, "Lexeme");
		String hLiteral = String.format(literalFormat, "Literal");
		String hLineIndex = String.format(lineIndexFormat, "Line Index");
		String hLineTokenIndex = String.format(lineTokenIndexFormat, "Line Token Index") + " |";
		
		var header = new EStringBuilder();
		header.a(hIndex, hLine, hToken, hKeyword, hLexeme, hLiteral, hLineIndex, hLineTokenIndex);
		var dashes = "\t|" + EStringUtil.repeatString("-", header.length() - 3) + "|";
		sb.println(dashes);
		sb.println(header);
		sb.println(dashes);
		
		int lastLineNum = 1;
		
		for (int i = 0; i < tokens.size(); i++) {
			var token = tokens.get(i);
			
			if (lastLineNum != token.getLineNum()) {
				lastLineNum = token.getLineNum();
				//sb.println(dashes);
			}
			
			String fIndex = String.format("\t| " + indexFormat, i);
			String fLine = String.format(lineFormat, token.getLineNum());
			String fToken = String.format(tokenFormat, token);
			String fKeyword = String.format(keywordFormat, token.getKeyword());
			String fLexeme = String.format(lexemeFormat, token.getLexeme());
			String fLiteral = String.format(literalFormat, token.getLiteral());
			String fLineIndex = String.format(lineIndexFormat, token.getLineIndex());
			String fLineTokenIndex = String.format(lineTokenIndexFormat, token.getLineTokenIndex()) + " |";
			
			sb.println(fIndex, fLine, fToken, fKeyword, fLexeme, fLiteral, fLineIndex, fLineTokenIndex);
		}
		
		sb.println(dashes);
		
		System.out.println(sb);
	}
	
}
