package envision_lang.tokenizer;

import envision_lang.lang.natives.Primitives;

/**
 * A programmatic building block from which logical code statements can be
 * parsed together from.
 * 
 * @author Hunter Bragg
 */
public class Token<TYPE> {
	
	/** The string representation of this token. */
	private String lexeme;
	/** The literal object value that this token holds (if applicable). */
	private TYPE literal;
	/** The internal keyword that represents this token. */
	private IKeyword keyword;
	/** The line number that this token was found on. */
	private int line;
	/** The index (position) of where this token is on the line (0 is the first token on the line). */
	private int lineIndex;
	/** The index of this token on the line it was originally parsed from. */
	private int lineTokenIndex;
	/** True if the keyword representing this token is a reservedWord instead of an Operator. */
	private boolean isReservedWord;
	
	//---------------------------------------------------------------------------
	
	public Token(IKeyword k, String lexemeIn, TYPE literalIn, int lineIn) {
		this(k, lexemeIn, literalIn, lineIn, -1, -1);
	}
	
	public Token(IKeyword k, String lexemeIn, TYPE literalIn, int lineIn, int lineIndexIn, int lineTokenIndexIn) {
		keyword = k;
		lexeme = lexemeIn;
		literal = literalIn;
		line = lineIn;
		lineIndex = lineIndexIn;
		lineTokenIndex = lineTokenIndexIn;
		isReservedWord = k.isReservedWord();
	}
	
	public Token(Token<TYPE> in) {
		lexeme = (in != null) ? in.lexeme : null;
		literal = (in != null) ? in.literal : null;
		keyword = (in != null) ? in.keyword : null;
		
		if (in != null) {
			line = in.line;
			lineIndex = in.lineIndex;
			lineTokenIndex = in.lineTokenIndex;
		}
		else {
			line = -1;
			lineIndex = -1;
			lineTokenIndex = -1;
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
	
	public boolean compareLexeme(Token t) { return lexeme.equals(t.lexeme); }
	
	public String getLexeme() { return lexeme; }
	public TYPE getLiteral() { return literal; }
	public IKeyword getKeyword() { return keyword; }
	public int getLineNum() { return line; }
	public int getLineIndex() { return lineIndex; }
	public int getLineTokenIndex() { return lineTokenIndex; }
	
	public Primitives getPrimitiveDataType() { return Primitives.getPrimitiveType(this); }
	public Operator asOperator() { return keyword.asOperator(); }
	public ReservedWord asReservedWord() { return keyword.asReservedWord();	}
	
	public Token<TYPE> copy() {
		return copy(this);
	}
	
	//---------------------------------------------------------------------------
	
	public static Token<Void> EOF(int lineIn) {
		return new Token<>(ReservedWord.EOF, ReservedWord.EOF.typeString, null, lineIn);
	}
	
	public static Token<Void> newLine(int lineIn) {
		return new Token<>(ReservedWord.NEWLINE, ReservedWord.NEWLINE.typeString, null, lineIn);
	}
	
	public static Token<Void> newLine(int lineIn, int lineIndex, int lineTokenIndex) {
		return new Token<>(ReservedWord.NEWLINE, ReservedWord.NEWLINE.typeString, null, lineIn, lineIndex, lineTokenIndex);
	}
	
	public static Token<String> create(String lexemeIn, int lineIn) {
		return new Token<>(ReservedWord.STRING_LITERAL, lexemeIn, lexemeIn, lineIn);
	}
	
	public static Token<Number> create(Number literalIn, int lineIn) {
		return new Token<>(ReservedWord.NUMBER_LITERAL, String.valueOf(literalIn), literalIn, lineIn);
	}
	
	public static Token<Void> create(IKeyword keywordIn, String lexemeIn, int lineIn) {
		return new Token<>(keywordIn, lexemeIn, null, lineIn);
	}
	
	public static Token<Void> create(IKeyword keywordIn, int lineIn) {
		return new Token<>(keywordIn, keywordIn.typeString(), null, lineIn);
	}
	
	public static <TYPE> Token<TYPE> create(IKeyword keywordIn, Token<TYPE> in) {
		return new Token<>(keywordIn, in.lexeme, in.literal, in.line);
	}
	
	public static <TYPE> Token<TYPE> copy(Token<TYPE> in) {
		return (in != null) ? new Token<>(in) : null;
	}
	
}
