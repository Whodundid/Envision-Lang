package envision.tokenizer;

import static envision.tokenizer.ReservedWord.*;
import static envision.tokenizer.Operator.*;

import envision.EnvisionCodeFile;
import envision.exceptions.EnvisionError;
import eutil.datatypes.EArrayList;
import eutil.strings.EStringBuilder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Tokenizer {
	
	/** The code file being tokenized. */
	private EnvisionCodeFile theFile;
	/** Each tokenized line in the code file. */
	private final EArrayList<EArrayList<Token>> lineTokens = new EArrayList();
	/** All of the parsed tokens from the given code file. */
	private final EArrayList<Token> tokens = new EArrayList();
	/** Each line in string form. */
	private final EArrayList<String> lines = new EArrayList();
	/** Comment tokens. */
	private final EArrayList<Token> commentTokens = new EArrayList();
	/** Used to keep track of multi-line comments. */
	private boolean inComment = false;
	/** Used to keep track of multi-line comments. */
	private boolean inString = false;
	
	/** Used to keep track of the current line internally. */
	private int lineNum = 1;
	private String source;
	private int start = 0;
	private int cur = 0;
	
	private EArrayList<Token> createdTokens;
	
	//--------------------------------------------------------------------------------------------------------------------
	
	public Tokenizer() {}
	public Tokenizer(EnvisionCodeFile codeFileIn) {
		theFile = codeFileIn;
	}
	public Tokenizer(String line) {
		theFile = null;
		tokenizeLine(line);
		if (inString) throw new EnvisionError("Envision: Tokenization failed -> incomplete string!");
	}
	
	//--------------------------------------------------------------------------------------------------------------------
	
	/** Separates a single input line's characters into valid Envision tokens. */
	private EArrayList<Token> tokenizeLine(String line, int lineNum) {
		createdTokens = new EArrayList();
		source = line.trim();
		cur = 0;
		
		//check for basic comment
		if (source.startsWith(COMMENT_SINGLE.chars)) return createdTokens;
		
		while (!atEnd()) {
			start = cur;
			scanToken();
		}
		
		return createdTokens;
	}
	
	//--------------------------------------------------------------------------------------------------------------------
	// Inspired from Lox by Bob Nystrom -> https://github.com/munificent/craftinginterpreters -- Lox.Scanner.java
	//--------------------------------------------------------------------------------------------------------------------
	
	private void scanToken() {
		//attempt to find the end of the multi-line comment
		if (inComment) {// '*/' 
			while (!atEnd()) {
				if (match('*')) inComment = !match('/');
				if (!atEnd()) advance();
			}
		}
		
		//don't continue if still in multi-line comment or if at end of line
		if (inComment || atEnd()) return;
		
		//match char to token
		char c = advance();
		switch (c) {
		case '{': addToken(CURLY_L); break;										// '{'
		case '}': addToken(CURLY_R); break;										// '}'
		case '(': addToken(PAREN_L); break;										// '('
		case ')': addToken(PAREN_R); break;										// ')'
		case '[': addToken(BRACKET_L); break;									// '['
		case ']': addToken(BRACKET_R); break;									// ']'
		case ';': addToken(SEMICOLON); break;										// ';'
		case ',': addToken(COMMA); break;											// ','
		case ':': addToken(COLON); break;											// ':'
		case '?': addToken(TERNARY); break;											// '?'
		
		case '.': addToken((matchInOrder('.', '.') ? VARARGS : PERIOD)); break;		// '...', '.'
		case '=': addToken((match('=') ? COMPARE : ASSIGN)); break;					// '==', '='
		case '!': addToken((match('=') ? NOT_EQUALS : NEGATE)); break;				// '!=', '!'
		case '&':
			if (match('&')) addToken(AND);											// '&&'
			else if (match('=')) addToken(BW_AND_ASSIGN);							// '&='
			else addToken(BW_AND);													// '&'
			break;
		case '|':
			if (match('|')) addToken(OR);											// '||'
			else if (match('=')) addToken(BW_OR_ASSIGN);							// '|='
			else addToken(BW_OR);													// '|'
			break;
		case '^': addToken((match('=')) ? BW_XOR_ASSIGN : BW_XOR); break;			// '^=', '^'
		case '<':
			if (match('<')) addToken((match('=')) ? SHL_ASSIGN : SHL);				// '<<=', '<<'
			else addToken((match('=')) ? LTE : LT);									// '<=', '<'
			break;
		case '>':
			if (match('>'))
				if (match('>')) addToken(match('=') ? SHR_AR_ASSIGN : SHR_AR);		// '>>>=', '>>>'
				else addToken((match('=') ? SHR_ASSIGN : SHR));						// '>>=', '>>'
			else addToken((match('=')) ? GTE : GT);									// '>=', '>'
			break;
		case '+':
			if (match('+')) addToken(INC);											// '++'
			else if (match('=')) addToken(ADD_ASSIGN);								// '+='
			else addToken(ADD);														// '+'
			break;
		case '-':
			if (match('-')) addToken(DEC); 											// '--'
			else if (match('>')) addToken(LAMBDA); 									// '->'
			else if (match('=')) addToken(SUB_ASSIGN); 								// '-='
			else addToken(SUB); 													// '-'
			break;
		case '*': addToken((match('=')) ? MUL_ASSIGN : MUL); break;					// '*=', '*'
		case '/':
			if (match('/')) break;													// '//'
			else if (match('*')) inComment = true;									// '/*'
			else if (match('=')) addToken(DIV_ASSIGN);								// '/='
			else addToken(DIV);														// '/'
			break; 
		case '%': addToken((match('=')) ? MOD_ASSIGN : MOD); break;					// '%=', '%'
		
		case ' ':
		case '\r':
		case '\t': break; //ignore whitespace
		
		case '"': string(); break;
		case '\'': parse_char(); break;
		
		default:
			if (isDigit(c)) number();
			else if (isLetter(c)) identifier();
			else throw new EnvisionError("Envision: Tokenization failed -> unexpected character! -> Line: "
										 + lineNum + " + '" + c + "'");
		}
	}
	
	/**
	 * Parses an identifier from tokens.
	 * <p>
	 * Identifiers are used as names for variables, objects, etc. within Envision.
	 */
	private void identifier() {
		while (isLetterOrNum(peek())) advance();
		String text = source.substring(start, cur);
		ReservedWord k = ReservedWord.getKeyword(text);
		if (k == null) k = IDENTIFIER;
		addToken(k);
	}
	
	/**
	 * Parses a single number from tokens.
	 * <p>
	 * This can either parse a decimal value if a '.' is detected,
	 * or it will simply prase a standard integer value.
	 */
	private void number() {
		while (isDigit(peek())) advance();
		
		boolean decimal = false;
		
		if (peek() == '.' && isDigit(peekNext())) {
			//consume the '.'
			advance();
			//consume any aditional digits
			while (isDigit(peek())) advance();
			decimal = true;
			
			//check if Exponential notation
			if (peek() == 'E' && (peekNext() == '-' && isDigit(peekNext(2))) || isDigit(peekNext())) {
				//consume the E
				advance();
				//consume a '-' if there is one
				if (peek() == '-') advance();
				//consume any aditional digits
				while (isDigit(peek())) advance();
			}
		}
		
		if (decimal) addToken(ReservedWord.NUMBER_LITERAL, Double.parseDouble(source.substring(start, cur)));
		else addToken(ReservedWord.NUMBER_LITERAL, Long.parseLong(source.substring(start, cur)));
	}
	
	/**
	 * Parses a string from tokens.
	 */
	private void string() {
		inString = true;
		while (peek() != '"' && !atEnd()) {
			//if (peek() == '\n') line++;
			advance();
		}
		
		//consume the '"'
		if (!atEnd()) {
			inString = false;
			advance();
		}
		
		//IF NOT HANDLED BY INTERPRETER -- USE 'start + 1, cur - 1' FOR BOUNDARY
		String value = source.substring(start + 1, cur - 1);
		addToken(ReservedWord.STRING_LITERAL, value);
	}
	
	/**
	 * Parses a single char from tokens.
	 */
	private void parse_char() {
		//error if at end
		if (atEnd()) throw new EnvisionError("Incomplete char tokenization!");
		//get char
		advance();
		//consume the 2nd '
		if (peek() != '\'' || atEnd()) throw new EnvisionError("Incomplete char tokenization!");
		else advance();
		
		addToken(ReservedWord.CHAR_LITERAL, source.substring(start + 1, cur - 1).charAt(0));
	}
	
	/**
	 * Returns true if the 'expected' char is at the current token position.
	 * Returns false if at the end.
	 * 
	 * @param expected The char to check
	 * @return true if the same
	 */
	private boolean check(char expected) {
		return (!atEnd() && source.charAt(cur) == expected);
	}
	
	/**
	 * Performs the same 'check' operation as check(char expected), but requires
	 * that the given string of chars is present exactly as given.
	 * Similarly, if the end is reached during the char maching, false is
	 * returned instead.
	 * 
	 * @param expectedString A list of chars to check in order
	 * @return true if all chars match in order
	 */
	private boolean checkInOrder(char... expectedString) {
		int prev_cur = getCurrent();
		boolean r = true;
		for (char c : expectedString) {
			//break at end regardless
			if (atEnd() || !check(c)) {
				r = false;
				break;
			}
		}
		setCurrent(prev_cur);
		return r;
	}
	
	/**
	 * If the expected is the current char, then advance the current position
	 * and return true.
	 * 
	 * @param expected The char to be checked
	 * @return true if the char matches
	 */
	private boolean match(char expected) {
		if (atEnd() || source.charAt(cur) != expected) return false;
		cur++;
		return true;
	}
	
	/**
	 * Similar to checkInOrder except performs matching instead of checking.
	 * This means that if the expectedString is matched, the current token
	 * position will be advanced each time.
	 * 
	 * @param expectedString A list of chars to check in order
	 * @return true if all chars match in order
	 */
	private boolean matchInOrder(char... expectedString) {
		int prev_cur = getCurrent();
		boolean r = true;
		for (char c : expectedString) {
			//break at end regardless
			if (atEnd() || !match(c)) {
				r = false;
				break;
			}
		}
		setCurrent(prev_cur);
		return r;
	}
	
	private boolean check(ReservedWord k) { return checkInOrder(k.chars.toCharArray()); }
	private boolean match(ReservedWord k) { return matchInOrder(k.chars.toCharArray()); }
	
	private char peek() { return (atEnd()) ? '\0' : source.charAt(cur); }
	private char peekNext() { return (cur + 1 >= source.length()) ? '\0' : source.charAt(cur + 1); }
	
	/**
	 * Peeks at the char at the (cur + nextAmount) position.
	 * 
	 * @param nextAmount Offset amount
	 * @return The char at the offset position
	 */
	private char peekNext(int nextAmount) {
		return (cur + nextAmount >= source.length()) ? '\0' : source.charAt(cur + nextAmount); 
	}
	
	private char advance() { return source.charAt(cur++); }
	private boolean isLetter(char c) { return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_'; }
	private boolean isDigit(char c) { return c >= '0' && c <= '9'; }
	private boolean isLetterOrNum(char c) { return isLetter(c) || isDigit(c); }
	private boolean atEnd() { return cur >= source.length(); }
	private int getCurrent() { return cur; }
	private void setCurrent(int in) { cur = in; }
	
	//--------------------------------------------------------------------------------------------------------------------
	
	public void setCodeFile(EnvisionCodeFile codeFileIn) { theFile = codeFileIn; }
	public boolean hasFile() { return theFile != null; }
	
	//--------------------------------------------------------------------------------------------------------------------
	
	public boolean tokenizeFile() throws IOException {
		if (theFile == null) throw new FileNotFoundException("No file specified!");
		return tokenizeFile_I();
	}
	
	private boolean tokenizeFile_I() throws IOException {
		if (theFile.isValid()) {
			
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(theFile.getSystemFile())))) {
				
				String l = reader.readLine();
				while (l != null) {
					l = l.replace("\t", "");
					boolean empty = l.isBlank();
					
					if (!empty) {
						EArrayList<Token> list = tokenizeLine(l, lineNum);
						lineTokens.add(list);
						tokens.addAll(list);
					}
					else {
						Token nl = Token.newLine(lineNum);
						lineTokens.add(new EArrayList<Token>(nl));
						tokens.add(nl);
					}
					
					lines.add(l);
					
					//check for end of file
					l = reader.readLine();
					if (l != null) {
						Token nl = Token.newLine(lineNum++);
						if (!empty) {
							lineTokens.getLast().add(nl);
							tokens.add(nl);
							//lines.add(stripComments(l));
						}
					}
					else {
						Token EOF = Token.EOF(lineNum);
						lineTokens.getLast().add(EOF);
						tokens.add(EOF);
						lines.add("EOF");
					}
				}
				
				if (inString) throw new EnvisionError("Envision: Tokenization failed -> incomplete string!");
				
				return true;
			}
		}
		return false;
	}
	
	/** Tokenizes a single line. */
	public boolean tokenizeLine(String lineIn) {
		lineIn = lineIn.replace("\t", "");
		EArrayList<Token> list = tokenizeLine(lineIn, 0);
		if (inString) throw new EnvisionError("Envision: Tokenization failed -> incomplete string!");
		lineTokens.add(list);
		tokens.addAll(list);
		tokens.add(Token.newLine(0));
		lines.add(stripComments(lineIn));
		lines.add("EOF");
		return true;
	}
	
	//--------------------------------------------------------------------------------------------------------------------
	// Getters
	//--------------------------------------------------------------------------------------------------------------------
	
	public EArrayList<EArrayList<Token>> getLineTokens() { return lineTokens; }
	public EArrayList<Token> getTokens() { return tokens; }
	public EArrayList<String> getLines() { return lines; }
	public EArrayList<Token> getCommentTokens() { return createdTokens; }
	
	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------
	
	private void addToken(IKeyword keyword) { addToken(keyword, null); }
	private void addToken(IKeyword keyword, Object literal) {
		String text = source.substring(start, cur);
		createdTokens.add(new Token(keyword, text, literal, lineNum));
	}
	
	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------
	
	/** Removes both single line and multiline comments from strings. */
	public static String stripComments(String in) {
		if (in.startsWith("//")) return "";
		EStringBuilder cur = new EStringBuilder();
		
		boolean start = false;
		boolean multistart = false;
		boolean inMultiComment = false;
		
		for (int i = 0; i < in.length(); i++) {
			char c = in.charAt(i);
			
			if (inMultiComment) {
				if (multistart) {
					if (c == '/') {
						inMultiComment = false;
						if (i + 1 < in.length() && in.charAt(i + 1) == ' ') {
							i++;
							continue;
						}
					}
					else start = false;
				}
				else if (c == '*') multistart = true;
			}
			else {
				if (start) {
					if (c == '/') break;
					if (c == '*') {
						inMultiComment = true;
						cur.setSubstring(0, cur.length() - 1);
					}
				}
				else if (c == '/') start = true;
			}
			
			if (!inMultiComment) cur.append(c);
		}
		
		return cur.toString();
	}
	
}
