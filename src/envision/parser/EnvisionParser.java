package envision.parser;

import envision.Envision;
import envision.EnvisionCodeFile;
import envision.exceptions.EnvisionError;
import envision.parser.statements.Statement;
import envision.tokenizer.Keyword;
import envision.tokenizer.Token;
import envision.tokenizer.Tokenizer;
import eutil.datatypes.EArrayList;
import eutil.strings.StringUtil;

import java.io.IOException;

public class EnvisionParser {
	
	//---------------------------------
	
	private EArrayList<EArrayList<Token>> tokenLines;
	private EArrayList<Token> tokens;
	private EArrayList<String> lines;
	private int current = 0;
	
	//---------------------------------
	
	private EnvisionParser() {}
	
	//---------------------------------
	
	public static EArrayList<Statement> parse(EnvisionCodeFile codeFile) throws Exception {
		if (codeFile.isValid()) {
			EnvisionParser p = new EnvisionParser();
			
			EArrayList<Statement> statements = new EArrayList();
			p.tokenLines = codeFile.getLineTokens();
			p.tokens = codeFile.getTokens();
			p.lines = codeFile.getLines();
			
			Exception error = null;
			
			try {
				if (p.tokens.isNotEmpty()) {
					while (!p.atEnd()) {
						Statement s = ParserStage.parse(p);
						statements.addIf(s != null, s);
					}
				}
			}
			catch (Exception e) {
				error = e;
			}
			
			if (error != null) { throw error; }
			return statements;
		}
		return null;
	}
	
	public static Statement parseStatement(String lineIn) throws IOException {
		Tokenizer t = new Tokenizer(lineIn);
		EnvisionParser p = new EnvisionParser();
		p.tokenLines = t.getLineTokens();
		p.tokens = t.getTokens();
		p.lines = t.getLines();
		return ParserStage.parse(p);
	}
	
	//---------------------------------
	
	protected Token consume(String requirementMessage, Keyword... type) {
		if (check(type)) { return advance(); }
		throw error(requirementMessage);
	}
	
	protected Token consume(String requirementMessage, String methodName, Keyword... type) {
		if (check(type)) { return advance(); }
		throw error(requirementMessage);
	}
	
	protected Token consume(Keyword type, String methodName, String requirementMessage) {
		if (check(type)) { return advance(); }
		throw error(requirementMessage);
	}
	
	protected Token consume(Keyword type, String requirementMessage) {
		if (check(type)) { return advance(); }
		throw error(requirementMessage);
	}
	
	protected Token consumeType(String requirementMessage, Keyword.KeywordType... type) {
		if (checkType(type)) { return advance(); }
		throw error(requirementMessage);
	}
	
	protected Token consumeType(String requirementMessage, String methodName, Keyword.KeywordType... type) {
		if (checkType(type)) { return advance(); }
		throw error(requirementMessage);
	}
	
	protected Token consumeType(Keyword.KeywordType type, String methodName, String requirementMessage) {
		if (checkType(type)) { return advance(); }
		throw error(requirementMessage);
	}
	
	protected Token consumeType(Keyword.KeywordType type, String requirementMessage) {
		if (checkType(type)) { return advance(); }
		throw error(requirementMessage);
	}
	
	protected Token consumeAny(String requirementMessage, Keyword... types) {
		for (Keyword t : types) {
			if (check(t)) return advance();
		}
		throw error(requirementMessage);
	}
	
	protected Token consumeAnyType(String requirementMessage, Keyword.KeywordType... types) {
		for (Keyword.KeywordType t : types) {
			if (checkType(t)) return advance();
		}
		throw error(requirementMessage);
	}
	
	protected boolean match(Keyword... val) {
		for (Keyword o : val) if (check(o)) { advance(); return true; }
		return false;
	}
	
	protected boolean match(String methodName, Keyword... val) {
		for (Keyword o : val) if (check(o)) { advance(); return true; }
		return false;
	}
	
	protected boolean matchType(Keyword.KeywordType... type) {
		for (Keyword.KeywordType t : type) if (checkType(t)) { advance(); return true; }
		return false;
	}
	
	protected boolean matchType(String methodName, Keyword.KeywordType... type) {
		for (Keyword.KeywordType t : type) if (checkType(t)) { advance(); return true; }
		return false;
	}
	
	protected boolean matchBoth(Keyword first, Keyword second) {
		if (check(first)) {
			Token n = next();
			if (n != null && n.keyword == second) {
				advance();
				advance();
				return true;
			}
		}
		return false;
	}
	
	protected boolean checkAll(Keyword... in) {
		if (in.length == 0) return false;
		int i = 0;
		for (Keyword k : in) {
			Token t = tokens.get(current + i++);
			//System.out.println(i + " : " + t);
			if (!check(t, k)) return false;
		}
		return true;
	}
	
	protected boolean check(Keyword... val) { return check(current(), val); }
	protected boolean checkNext(Keyword... val) { return check(next(), val); }
	protected boolean checkPrevious(Keyword... val) { return check(previous(), val); }
	
	protected boolean check(Token t, Keyword... val) {
		if (atEnd()) {
			for (Keyword k : val) if (k == Keyword.EOF) return true;
			return false;
		}
		for (Keyword k : val) if (t.keyword == k) return true;
		return false;
	}
	
	protected boolean checkType(Keyword.KeywordType... val) { return checkType(current(), val); }
	protected boolean checkNextType(Keyword.KeywordType... val) { return checkType(next(), val); }
	protected boolean checkPreviousType(Keyword.KeywordType... val) { return checkType(previous(), val); }
	
	private boolean checkType(Token t, Keyword.KeywordType... type) {
		if (atEnd()) { return false; }
		for (Keyword.KeywordType kt : type) {
			if (t.keyword.hasType(kt)) { return true; }
		}
		return false;
	}
	
	protected boolean checkAdvance(Keyword k) {
		boolean val = check(k);
		if (val) { advance(); }
		return val;
	}
	
	protected boolean checkAdvance(Keyword.KeywordType k) {
		boolean val = checkType(k);
		if (val) { advance(); }
		return val;
	}
	
	protected boolean atEnd() {
		return current().isEOF();
	}
	
	protected Token advance() {
		if (!atEnd()) { current++; }
		return previous();
	}
	
	protected Token getAdvance() {
		Token cur = current();
		advance();
		return cur;
	}
	
	protected Token current() { return tokens.get(current); }
	protected Token previous() { return tokens.get(current - 1); }
	protected Token next() { return tokens.get(current + 1); }
	
	protected int getCurrentNum() { return current; }
	protected void setCurrentNum(int in) { current = in; }
	protected void setPrevious() { current--; }
	
	protected String getLineString(int lineNum) { return lines.get(lineNum); }
	
	public String getErrorMessage(String message) {
		if (current().isEOF()) setPrevious();
		
		int theLine = current().line;
		if (theLine > tokenLines.size()) theLine -= 1;
		
		String border = "";
		String title = "\tParsing Error!";
		String lineNumber = "\tLine " + theLine + ":";
		String line = "\t\t" + lines.get(theLine - 1);
		String arrow = "";
		String error = "\t" + message + "   ->   '" + current() + "'";
		
		//determine border length
		String longest = StringUtil.getLongest(title, lineNumber, line, error);
		border = StringUtil.repeatString("-", longest.length() + 16);
		
		//find arrow position
		EArrayList<Token> tokenLine = tokenLines.get(theLine - 1);
		int pos = 0;
		for (int i = 0; i < tokenLine.size(); i++) {
			if (tokenLine.get(i).checkID(current().id)) {
				pos = i;
				break;
			}
		}
		
		int offset = 0;
		String actual = lines.get(theLine - 1);
		
		for (int i = 0; i < pos; i++) {
			Token t = tokenLine.get(i);
			int len = t.lexeme.length();
			offset += len;
			
			actual = actual.substring(len);
			String spacer = "";
			for (int j = 0; j < actual.length(); j++) {
				if (actual.charAt(j) != ' ') {
					break;
				}
				spacer += " ";
			}
			offset += spacer.length();
			actual = actual.substring(spacer.length());
		}
		
		//position arrow in string
		arrow = "\t\t" + StringUtil.repeatString(" ", offset) + "^";
		
		String e = border + "\n" +
				   title + "\n\n" +
				   lineNumber + "\n" +
				   line + "\n" +
				   arrow + "\n\n" +
				   error + "\n" +
				   border;
		return e;
	}
	
	public EnvisionError error(String message) {
		return new EnvisionError("\n\n" + getErrorMessage(message) + "\n");
	}
	
	/** Print Debug message. */
	public void pd() { pd(""); }
	/** Print Debug message. */
	public void pd(Object o) { pd(StringUtil.toString(o)); }
	/** Print Debug message. */
	public void pd(String m) { if (Envision.debugMode) { System.out.println(m); } }
	
}
