package envision_lang.tokenizer;

import static envision_lang.tokenizer.Operator.*;
import static envision_lang.tokenizer.ReservedWord.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import envision_lang.EnvisionLang;
import envision_lang._launch.EnvisionCodeFile;
import envision_lang.lang.language_errors.EnvisionLangError;
import eutil.datatypes.boxes.BoxList;
import eutil.datatypes.util.EList;
import eutil.debug.Broken;
import eutil.file.LineReader;

public class Tokenizer {
	
	/** The code file being tokenized. */
	private EnvisionCodeFile theFile;
	/** Each tokenized line in the code file. */
	private final BoxList<Integer, EList<Token<?>>> lineTokens = new BoxList<>();
	/** All of the parsed tokens from the given code file. */
	private final EList<Token<?>> tokens = EList.newList();
	/** Each line in string form. */
	private final EList<String> lines = EList.newList();
	/** Comment tokens. */
	private final EList<Token<?>> commentTokens = EList.newList();
	/** Used to keep track of multi-line comments. */
	private boolean inComment = false;
	/** Used to keep track of multi-line comments. */
	private boolean inString = false;
	/** Used to keep track of the current line internally. */
	private int lineNum = 1;
	/** Used to keep track of the exact line index for where this token came from. */
	private int lineIndex = 0;
	/** Used to keep track of the index of this token on the line it was parsed from. */
	private int lineTokenIndex = 0;
	/** The actual line actively being parsed. */
	private String currentLineSource;
	/** The 'start' position for where a token begins. */
	private int start = 0;
	/** The 'current' position on the line that is being tracked to parse a token. */
	private int cur = 0;
	
	/** The (active) set of tokens currently being parsed for a given line. */
	private EList<Token<?>> parsedLineTokens;
	
	//--------------------------------------------------------------------------------------------------------------------
	
//	public Tokenizer() {}
	public Tokenizer(EnvisionCodeFile codeFileIn) {
		theFile = codeFileIn;
	}
	public Tokenizer(String line) {
		theFile = null;
		tokenizeLine(line);
		if (inString) throw new EnvisionLangError("Envision: Tokenization failed -> incomplete string!");
	}
	
	//--------------------------------------------------------------------------------------------------------------------
	
	/** Separates a single input line's characters into valid Envision tokens. */
	private EList<Token<?>> tokenizeLine(String line, int lineNum) {
		parsedLineTokens = EList.newList();
		currentLineSource = line.trim();
		cur = 0;
		lineIndex = lineNum;
		
		//check for basic comment
		if (currentLineSource.startsWith(COMMENT_SINGLE.operatorString)) return parsedLineTokens;
		
		while (!atEnd()) {
			start = cur;
			scanToken();
		}
		
		return parsedLineTokens;
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
		
		//advance to the next char in the source string
		char c = advance();
		
		//ignore whitespace and jump to the next non-whitespace character
		if (isWhiteSpace(c)) c = consumeWhiteSpace();
		
		//match char to token
		switch (c) {
		
		//break on null chars
		case '\0': break;
		
		case '#':																	// '#'
			if (EnvisionLang.enableBlockStatementParsing) addToken(BLOCK_STATEMENT_TOKEN);
			else throw new EnvisionLangError("Block statement parsing not enabled!");
			break;
		
		case '{': addToken(CURLY_L); break;											// '{'
		case '}': addToken(CURLY_R); break;											// '}'
		case '(': addToken(PAREN_L); break;											// '('
		case ')': addToken(PAREN_R); break;											// ')'
		case '[': addToken(BRACKET_L); break;										// '['
		case ']': addToken(BRACKET_R); break;										// ']'
		case ';': addToken(SEMICOLON); break;										// ';'
		case ',': addToken(COMMA); break;											// ','
		case ':': addToken(COLON); break;											// ':'
		case '?': addToken(TERNARY); break;											// '?'
		
		case '.': addToken((matchInOrder('.', '.') ? VARARGS : PERIOD)); break;		// '...', '.'
		case '=': addToken((match('=') ? EQUALS : ASSIGN)); break;					// '==', '='
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
			if (match('>')) {
				if (match('>')) addToken(match('=') ? SHR_AR_ASSIGN : SHR_AR);		// '>>>=', '>>>'
				else addToken((match('=') ? SHR_ASSIGN : SHR));						// '>>=', '>>'
			}
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
		
		case '"': string(); break;													// string literals
		case '\'': parse_char(); break;												// char literals
		
		default:
			if (isDigit(c)) number();												// number literals
			else if (isLetter(c)) identifier();										// variable/object names
			else throw new EnvisionLangError("Envision: Tokenization failed -> unexpected character! -> Line: " +
										      lineNum + " + '" + c + "'");
		}
	}
	
	private boolean isWhiteSpace(char c) { return c == ' ' || c == '\r' || c == '\t'; }
	private char consumeWhiteSpace() {
		char next = peek();
		boolean endCheck = atEnd();
		boolean whiteSpaceCheck = isWhiteSpace(next);
		
		while (!endCheck && whiteSpaceCheck) {
			advance();
			endCheck = atEnd();
			next = peek();
			whiteSpaceCheck = isWhiteSpace(next);
		}
		
		//advance start past the (now) consumed whitespace
		start = cur++;
		
		return next;
	}
	
	/**
	 * Parses an identifier from tokens.
	 * <p>
	 * Identifiers are used as names for variables, objects, etc. within Envision.
	 */
	private void identifier() {
		while (isLetterOrNum(peek())) advance();
		
		String text = currentLineSource.substring(start, cur);
		ReservedWord k = ReservedWord.getKeyword(text);
		
		if (k == null) {
			k = IDENTIFIER;
			addToken(k);
			return;
		}
		
		if (k == TRUE) addToken(k, true);
		else if (k == FALSE) addToken(k, false);
		else addToken(k);
	}
	
	/**
	 * Parses a single number from tokens.
	 * <p>
	 * This can either parse a decimal value if a '.' is detected,
	 * or it will simply parse a standard integer value.
	 */
	private void number() {
		while (isDigit(peek())) advance();
		
		boolean decimal = false;
		
		if (peek() == '.' && isDigit(peekNext())) {
			//consume the '.'
			advance();
			//consume any additional digits
			while (isDigit(peek())) advance();
			decimal = true;
			
			//check if Exponential notation
			if (peek() == 'E' && (peekNext() == '-' && isDigit(peekNext(2))) || isDigit(peekNext())) {
				//consume the E
				advance();
				//consume a '-' if there is one
				if (peek() == '-') advance();
				//consume any additional digits
				while (isDigit(peek())) advance();
			}
		}
		
		if (decimal) addToken(ReservedWord.DOUBLE_LITERAL, Double.parseDouble(currentLineSource.substring(start, cur)));
		else addToken(ReservedWord.INT_LITERAL, Long.parseLong(currentLineSource.substring(start, cur)));
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
		String value = currentLineSource.substring(start + 1, cur - 1);
		addToken(ReservedWord.STRING_LITERAL, value);
	}
	
	/**
	 * Parses a single char from tokens.
	 */
	@Broken(reason="This method is effectively not complete because it does not enforce the 'u' in '\\u0000'.")
	private void parse_char() {
		//consume 1st '
		advance();
		//error if at end
		if (atEnd()) throw new EnvisionLangError("Incomplete char tokenization!");
		
		boolean isUnicode = false;
		if (getCurrent() == '\'') {
			isUnicode = true;
			//get char (could be up to 6 characters long ex. '\u4444'
			int count = 0;
			while (peek() != '\'' && count < 5 && !atEnd()) {
				advance();
			}
		}
		//consume the 2nd '
		if (peek() != '\'' || atEnd()) throw new EnvisionLangError("Incomplete char tokenization!");
		else advance();
		
		// convert the string version of the parsed char to a char
		String charStr = currentLineSource.substring(start + 1, cur - 1);
		String charVal;
		
		if (isUnicode) {
			// convert unicode values to chars
			charStr = charStr.replace("\\", "");
			charStr = charStr.replace("u", "");
			int hex = Integer.parseInt(charStr, 16);
			charVal = String.valueOf(Character.toChars(hex));
		}
		else {
			charVal = charStr;
		}
		
		addToken(ReservedWord.CHAR_LITERAL, charVal);
	}
	
	/**
	 * Returns true if the 'expected' char is at the current token position.
	 * Returns false if at the end.
	 * 
	 * @param expected The char to check
	 * @return true if the same
	 */
	private boolean check(char expected) {
		return (!atEnd() && currentLineSource.charAt(cur) == expected);
	}
	
	/**
	 * Performs the same 'check' operation as check(char expected), but requires
	 * that the given string of chars is present exactly as given.
	 * Similarly, if the end is reached during the char matching, false is
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
		if (atEnd() || currentLineSource.charAt(cur) != expected) return false;
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
	
	private char peek() { return (atEnd()) ? '\0' : currentLineSource.charAt(cur); }
	private char peekNext() { return (cur + 1 >= currentLineSource.length()) ? '\0' : currentLineSource.charAt(cur + 1); }
	
	/**
	 * Peeks at the char at the (cur + nextAmount) position.
	 * 
	 * @param nextAmount Offset amount
	 * @return The char at the offset position
	 */
	private char peekNext(int nextAmount) {
		return (cur + nextAmount >= currentLineSource.length()) ? '\0' : currentLineSource.charAt(cur + nextAmount); 
	}
	
	private char advance() { return currentLineSource.charAt(cur++); }
	private boolean isLetter(char c) { return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_'; }
	private boolean isDigit(char c) { return c >= '0' && c <= '9'; }
	private boolean isLetterOrNum(char c) { return isLetter(c) || isDigit(c); }
	private boolean atEnd() { return cur >= currentLineSource.length(); }
	private int getCurrent() { return cur; }
	private void setCurrent(int in) { cur = in; }
	
	//--------------------------------------------------------------------------------------------------------------------
	
	public void setCodeFile(EnvisionCodeFile codeFileIn) { theFile = codeFileIn; }
	public boolean hasFile() { return theFile != null; }
	
	//--------------------------------------------------------------------------------------------------------------------
	
	public boolean tokenizeFile() throws Exception {
		if (theFile == null) throw new FileNotFoundException("No file specified!");
		return tokenizeFile_I();
	}
	
	private boolean tokenizeFile_I() throws IOException {
		if (!theFile.isValid()) return false;
		
		try (var reader = new LineReader(theFile.getSystemFile())) {
			while (reader.hasNextLine()) {
				String l = reader.nextLine();
				l = l.replace("\t", "");
				boolean empty = l.isBlank();
				boolean hasNextLine = reader.hasNextLine();
				
				lines.add(l);
				
				if (!empty) {
					var list = tokenizeLine(l, lineNum);
					
					// this can only happen if the tokenized line was a comment
					if (list.isNotEmpty()) {
						if (hasNextLine) {
							var lastToken = list.getLast();
							var lastIndex = (lastToken != null) ? lastToken.getLineIndex() + 1 : 0;
							var lastLineT = (lastToken != null) ? lastToken.getLineTokenIndex() + 1 : 0;
							
							var nl = Token.newLine(lineNum, lastIndex, lastLineT);
							list.add(nl);
						}
						
						lineTokenIndex = 0;
						lineTokens.add(lineNum, list);
						tokens.addAll(list);
					}
				}
				
				//check for end of file
				if (!hasNextLine) {
					var lastToken = lineTokens.getLastB().getLast();
					var lastIndex = (lastToken != null) ? lastToken.getLineIndex() + 1 : 0;
					var lastLineT = (lastToken != null) ? lastToken.getLineTokenIndex() + 1 : 0;
					
					var EOF = Token.EOF(lineNum, lastIndex, lastLineT);
					lineTokens.getLastB().add(EOF);
					tokens.add(EOF);
					lines.add("EOF");
				}
				
				lineNum++;
			}
			
			if (inString) throw new EnvisionLangError("Envision: Tokenization failed -> incomplete string!");
			
			return true;
		}
	}
	
	/**
	 * Tokenizes a single line instead of an entire file.
	 * <p>
	 * NOTE: One line could contain potentially many lines due to line breaks.
	 */
	public boolean tokenizeLine(String lineIn) {
		lineIn = lineIn.replace("\t", "");
		String[] subLines = lineIn.split("\n");
		
		for (int i = 0; i < subLines.length; i++, lineNum++) {
			String curLine = subLines[i];
			lines.add(curLine);
			
			//ignore empty lines
			if (curLine.isBlank()) continue;
			
			var list = tokenizeLine(curLine, i + 1);
			if (list.isNotEmpty()) {
				if (i + 1 < subLines.length) {
					var lastToken = list.getLast();
					var lastIndex = (lastToken != null) ? lastToken.getLineIndex() + 1 : 0;
					var lastLineT = (lastToken != null) ? lastToken.getLineTokenIndex() + 1 : 0;
					
					var nl = Token.newLine(lineNum, lastIndex, lastLineT);
					list.add(nl);
				}
				
				lineTokens.add(lineNum, list);
				tokens.addAll(list);
			}
		}
		
		if (inString) throw new EnvisionLangError("Envision: Tokenization failed -> incomplete string!");

		var EOF = Token.EOF(lineNum);
		lineTokens.getLast().getB().add(EOF);
		tokens.add(EOF);
		lines.add("EOF");
		
		return true;
	}
	
	public static Tokenizer tokenize(String line) {
		return new Tokenizer(line);
	}
	
	//--------------------------------------------------------------------------------------------------------------------
	// Getters
	//--------------------------------------------------------------------------------------------------------------------
	
	public BoxList<Integer, EList<Token<?>>> getLineTokens() { return lineTokens; }
	public EList<Token<?>> getTokens() { return tokens; }
	public EList<String> getLines() { return lines; }
	public EList<Token<?>> getCommentTokens() { return commentTokens; }
	
	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------
	
	private void addToken(IKeyword keyword) { addToken(keyword, null); }
	private <TYPE> void addToken(IKeyword keyword, TYPE literal) {
		String text = currentLineSource.substring(start, cur);
		parsedLineTokens.add(new Token<>(keyword, text, literal, lineNum, start, lineTokenIndex++));
	}
	
	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------
	
	/** Removes both single line and multi-line comments from strings. */
//	public static String stripComments(String in) {
//		if (in.startsWith("//")) return "";
//		EStringBuilder cur = new EStringBuilder();
//		
//		boolean start = false;
//		boolean multistart = false;
//		boolean inMultiComment = false;
//		
//		for (int i = 0; i < in.length(); i++) {
//			char c = in.charAt(i);
//			
//			if (inMultiComment) {
//				if (multistart) {
//					if (c == '/') {
//						inMultiComment = false;
//						if (i + 1 < in.length() && in.charAt(i + 1) == ' ') {
//							i++;
//							continue;
//						}
//					}
//					else start = false;
//				}
//				else if (c == '*') multistart = true;
//			}
//			else {
//				if (start) {
//					if (c == '/') break;
//					if (c == '*') {
//						inMultiComment = true;
//						cur.setSubstring(0, cur.length() - 1);
//					}
//				}
//				else if (c == '/') start = true;
//			}
//			
//			if (!inMultiComment) cur.append(c);
//		}
//		
//		return cur.toString();
//	}
	
}
