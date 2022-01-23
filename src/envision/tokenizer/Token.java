package envision.tokenizer;

import envision.lang.util.EnvisionDataType;
import eutil.datatypes.util.EDataType;
import eutil.math.NumberUtil;

public class Token {
	
	public static int total = 0;
	
	public int id;
	public String lexeme;
	public Object literal;
	public Keyword keyword;
	public int line;
	
	//---------------------------------------------------------------------------
	
	public Token(Keyword in, int lineIn) { this(in, "", lineIn); }
	public Token(Keyword in, String lexemeIn, int lineIn) {
		lexeme = lexemeIn;
		literal = null;
		keyword = in;
		line = lineIn;
		id = total++;
	}
	
	public Token(String lexemeIn, int lineIn) {
		lexeme = lexemeIn;
		line = lineIn;
		
		Keyword k = getTokenKeyword(lexemeIn);
		if (k == null) { k = Keyword.getOperator(lexemeIn); }
		
		if (k != null) {
			keyword = k;
			literal = null;
		}
		else if (lexeme.startsWith("\"") && lexeme.endsWith("\"")) {
			literal = lexeme;
			keyword = Keyword.STRING_LITERAL;
		}
		else if (NumberUtil.isNumber(lexeme)) {
			switch (EDataType.getNumberType(lexeme)) {
			case LONG: literal = Long.parseLong(lexeme); break;
			case DOUBLE: literal = Double.parseDouble(lexeme); break;
			default: literal = null;
			}
			keyword = Keyword.NUMBER_LITERAL;
		}
		else {
			literal = null;
			keyword = Keyword.IDENTIFIER;
		}
		
		id = total++;
	}
	
	public Token(Token in) {
		lexeme = (in != null) ? in.lexeme : null;
		literal = (in != null) ? in.literal : null;
		keyword = (in != null) ? in.keyword : null;
		
		if (in != null) {
			line = in.line;
			id = in.id;
		}
		else {
			line = -1;
			id = -1;
		}
	}
	
	//---------------------------------------------------------------------------
	
	private Keyword getTokenKeyword(String tokenIn) { return Keyword.getKeyword(tokenIn); }
	
	//---------------------------------------------------------------------------
	
	@Override
	public String toString() {
		if (isEOF()) {
			return "EOF";
		}
		else if (isNewLine()) {
			return "\\n";
		}
		else if (isReference()) {
			//return "'" + lexeme + "' | IDENTIFIER";
			return "'" + lexeme + "'";
		}
		/*
		else if (keyword != null) {
			if (keyword.isLiteral()) {
				return "'" + lexeme + "' | literal: " + literal;
			}
			return "'" + lexeme + "' | [" + StringUtil.toString(keyword.types, ", ") + "] | " + keyword;
		}
		*/
		
		//return "'" + lexeme + "' | OBJECT";
		return lexeme;
	}
	
	@Override
	public boolean equals(Object in) {
		if (in instanceof Integer) { return in.equals(in); }
		if (in instanceof String) { return lexeme.equals(in); }
		return keyword == in;
	}
	
	//---------------------------------------------------------------------------
	
	public boolean isKeyword() { return keyword != null; }
	public boolean isReference() { return keyword == Keyword.IDENTIFIER; }
	public boolean isModular() { return keyword == Keyword.MODULAR_VALUE; }
	public boolean isLiteral() { return (keyword != null && keyword.isLiteral()); }
	public boolean isEOF() { return keyword == Keyword.EOF; }
	public boolean isNewLine() { return keyword == Keyword.NEWLINE; }
	
	public boolean checkID(int idIn) { return id == idIn; }
	
	public String getToken() { return lexeme; }
	public Keyword getKeyword() { return keyword; }
	
	public EnvisionDataType getDataType() { return EnvisionDataType.getDataType(this); }
	
	//---------------------------------------------------------------------------
	
	public static Token EOF(int lineIn) { return new Token(Keyword.EOF, lineIn); }
	public static Token newLine(int lineIn) { return new Token(Keyword.NEWLINE, lineIn); }
	
	public static Token create(String lexemeIn, int lineIn) {
		return new Token(lexemeIn, lineIn);
	}
	
	public static Token create(Keyword keywordIn, String lexemeIn, int lineIn) {
		return new Token(keywordIn, lexemeIn, lineIn);
	}
	
	public static Token create(Keyword keywordIn, int lineIn) {
		return new Token(keywordIn, null, lineIn);
	}
	
	public static Token create(Keyword keywordIn, Token in) {
		return new Token(keywordIn, in.line);
	}
	
	public static Token copy(Token in) { return (in != null) ? new Token(in) : null; }
	
}
