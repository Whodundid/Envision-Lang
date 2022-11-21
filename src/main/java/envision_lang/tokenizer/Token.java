package envision_lang.tokenizer;

import envision_lang.lang.natives.Primitives;

/**
 * A programmatic building block from which logical code statements can be
 * parsed together from.
 * 
 * @author Hunter Bragg
 */
public class Token<TYPE> {
	
	/** The global total number of created tokens. */
	public static int total = 0;
	/** The specific ID of this token. */
	public int id;
	/** The string representation of this token. */
	public String lexeme;
	/** The literal object value that this token holds (if applicable). */
	public TYPE literal;
	/** The internal keyword that represents this token. */
	public IKeyword keyword;
	/** The line number that this token was found on. */
	public int line;
	/** True if the keyword representing this token is a reservedWord instead of an Operator. */
	private boolean isReservedWord;
	
	//---------------------------------------------------------------------------
	
	public Token(IKeyword k, String lexemeIn, TYPE literalIn, int lineIn) {
		id = total++;
		keyword = k;
		lexeme = lexemeIn;
		literal = literalIn;
		line = lineIn;
		isReservedWord = k.isReservedWord();
	}
	
	public Token(Token<TYPE> in) {
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
		
		isReservedWord = keyword.isReservedWord();
	}
	
	//---------------------------------------------------------------------------
	
	private ReservedWord getTokenKeyword(String tokenIn) {
		return ReservedWord.getKeyword(tokenIn);
	}
	
	//---------------------------------------------------------------------------
	
	@Override
	public String toString() {
		if (isEOF()) 				return "EOF";
		else if (isNewLine()) 		return "\\n";
		else if (isReference()) 	return "'" + lexeme + "'";
		
		return lexeme;
	}
	
	@Override
	public boolean equals(Object in) {
		if (in instanceof Integer i) return i == id;
		if (in instanceof String s) return lexeme.equals(s);
		return keyword == in;
	}
	
	//---------------------------------------------------------------------------
	
	public boolean isKeyword() { return keyword != null; }
	public boolean isReference() { return keyword == ReservedWord.IDENTIFIER; }
	//public boolean isModular() { return keyword == Keyword.MODULAR_VALUE; }
	public boolean isLiteral() { return (keyword != null && keyword.isLiteral()); }
	public boolean isEOF() { return keyword == ReservedWord.EOF; }
	public boolean isNewLine() { return keyword == ReservedWord.NEWLINE; }
	public boolean isDatatype() { return (keyword != null && keyword.isDataType()); }
	public boolean isReservedWord() { return isReservedWord; }
	
	public boolean checkID(int idIn) { return id == idIn; }
	public boolean compareLexeme(Token t) { return lexeme.equals(t.lexeme); }
	
	public String getToken() { return lexeme; }
	public IKeyword getKeyword() { return keyword; }
	public TYPE getLiteral() { return literal; }
	
	public Primitives getPrimitiveDataType() { return Primitives.getDataType(this); }
	public Operator asOperator() { return keyword.asOperator(); }
	public ReservedWord asReservedWord() { return keyword.asReservedWord();	}
	
	public Token<TYPE> copy() {
		return copy(this);
	}
	
	//---------------------------------------------------------------------------
	
	public static Token<Void> EOF(int lineIn) {
		return new Token<Void>(ReservedWord.EOF, ReservedWord.EOF.typeString, null, lineIn);
	}
	
	public static Token<Void> newLine(int lineIn) {
		return new Token<Void>(ReservedWord.NEWLINE, ReservedWord.NEWLINE.typeString, null, lineIn);
	}
	
	public static Token<String> create(String lexemeIn, int lineIn) {
		return new Token<String>(ReservedWord.STRING_LITERAL, lexemeIn, lexemeIn, lineIn);
	}
	
	public static Token<Number> create(Number literalIn, int lineIn) {
		return new Token<Number>(ReservedWord.NUMBER_LITERAL, String.valueOf(literalIn), literalIn, lineIn);
	}
	
	public static Token<Void> create(IKeyword keywordIn, String lexemeIn, int lineIn) {
		return new Token<Void>(keywordIn, lexemeIn, null, lineIn);
	}
	
	public static Token<Void> create(IKeyword keywordIn, int lineIn) {
		return new Token<Void>(keywordIn, keywordIn.typeString(), null, lineIn);
	}
	
	public static <TYPE> Token<TYPE> create(IKeyword keywordIn, Token<TYPE> in) {
		return new Token<TYPE>(keywordIn, in.lexeme, in.literal, in.line);
	}
	
	public static <TYPE> Token<TYPE> copy(Token<TYPE> in) {
		return (in != null) ? new Token<TYPE>(in) : null;
	}
	
}
