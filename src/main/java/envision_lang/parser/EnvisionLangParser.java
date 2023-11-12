package envision_lang.parser;

import java.util.Scanner;

import envision_lang._launch.EnvisionCodeFile;
import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.statement_types.Stmt_Expression;
import envision_lang.tokenizer.IKeyword;
import envision_lang.tokenizer.KeywordType;
import envision_lang.tokenizer.Token;
import envision_lang.tokenizer.Tokenizer;
import eutil.datatypes.EArrayList;
import eutil.datatypes.boxes.BoxList;
import eutil.datatypes.util.EList;
import eutil.strings.EStringBuilder;
import eutil.strings.EStringUtil;

/**
 * The EnvisionParser handles the conversion of line tokens into
 * valid Envision code statements. Invalid or incomplete statements
 * are caught during parsing and are displayed to the user.
 * 
 * @author Hunter Bragg
 */
public final class EnvisionLangParser {
	
	//========
    // Fields
    //========
	
	/** The tokens of each line, line by line. Used to generate error messages. */
	private BoxList<Integer, EList<Token<?>>> tokenLines;
	/** A complete list of all tokens within the file currently being parsed. */
	private EList<Token<?>> tokens;
	/** A non tokenized version of the file being parsed. This is simply each
	 *  line of the file in a list. Used to help generate error messages. */
	private EList<String> lines;
	/** A counter to keep track of the current token index as parsing continues. */
	private int currentTokenIndex = 0;
	
    /**
     * Enables the ability to append a '#' at the beginning of a statement
     * declaration to mark the next parsed statement as a 'blocking' statement.
     * If blocking statements are enabled and one has just finished being
     * executed, the Envision interpreter will halt script execution and
     * preserve the current memory and instruction stacks and will wait until
     * the script is manually restarted.
     */
    private boolean enableBlockStatementParsing = false;
	
	//==============
    // Constructors
    //==============
	
	/**
	 * Private so as to force use of static factory methods.
	 */
	private EnvisionLangParser() {}
	
	//================
	// Static Parsers
	//================
	
	/**
     * Attempts to parse a list of valid statements from
     * the tokens within the given code file.
     * <p>
     * Invalid or incomplete statements are caught and displayed.
     * 
     * @param codeFile The code file to be parsed
     * @return The list of parsed statements
     * @throws Exception
     */
    public static EList<ParsedStatement> parse(EnvisionCodeFile codeFile) {
        return parse(codeFile, false);
    }
	
	/**
	 * Attempts to parse a list of valid statements from
	 * the tokens within the given code file.
	 * <p>
	 * Invalid or incomplete statements are caught and displayed.
	 * 
	 * @param codeFile The code file to be parsed
	 * @return The list of parsed statements
	 * @throws Exception
	 */
	public static EList<ParsedStatement> parse(EnvisionCodeFile codeFile, boolean enableBlockStatements) {
		// error on invalid code files
		if (!codeFile.isValid()) throw new EnvisionLangError("Invalid CodeFile! Cannot parse!");
		
		// create a new isolated parser instance
		EnvisionLangParser p = new EnvisionLangParser();
		p.enableBlockStatementParsing = enableBlockStatements;
		
		// unpack the code file's tokenized values
		EList<ParsedStatement> statements = new EArrayList<>();
		p.tokenLines = codeFile.getLineTokens();
		p.tokens = codeFile.getTokens();
		p.lines = codeFile.getLines();
		
		// Any error that is thrown during parsing
		//ParsingError error = null;
		
		// ignore empty files and return an empty statement list
		if (p.tokens.isEmpty()) return statements;
		
		try {
			// continue until the end of the file
			while (!p.atEnd()) {
				ParsedStatement s = ParserHead.parse(p);
				//only add non-null statements
				statements.addIf(s != null, s);
			}
		}
		catch (Exception e) {
			// wrap the thrown exception into a parsing error
			//error = new ParsingError(e);
			throw e;
		}
		
		// throw the wrapped error (if there is one)
		//if (error != null) throw error;
		// otherwise return parsed statements
		return statements;
	}
	
	//-------------------------------------------------------------------------------------------
	
	public static ParsedStatement parseStatementLive() {
		boolean needsMore = true;
		
		@SuppressWarnings("resource")
		Scanner reader = new Scanner(System.in);
		var sb = new EStringBuilder();
		
		while (needsMore) {
			sb.println(reader.nextLine());
			
			int openCurlys = 0;
			
			// check for open curlys
			for (int i = 0; i < sb.length(); i++) {
				char c = sb.charAt(i);
				if (c == '{') openCurlys++;
				else if (c == '}') {
					// if the number of open curlys somehow goes negative, fail out
					if (openCurlys <= 0) throw new EnvisionLangError("Bad live statement input!");
					openCurlys--;
				}
			}
			
			// assume that if the number of open curlys is 0, then we've potentially parsed a complete statement
			if (openCurlys == 0) {
				needsMore = false;
			}
			
			reader.reset();
		}
		
		if (sb.isBlank() || sb.isEmpty()) return null;
		
		// attempt to actually build the statement from the parsed lines
		return parseStatement(sb.toString());
	}
	
	/**
	 * Parses a single line as opposed to an entire file.
	 * This method is useful when the interpreter is being executed in a 'live'
	 * mode of operation. In that, the user is able to continuously enter new
	 * lines of code which are parsed independently from one another.
	 * <p>
	 * Invalid statements are caught during parsing and displayed as an error
	 * to the user.
	 * 
	 * @param lineIn The line to be parsed
	 * @return A valid statement
	 * @throws Exception In the event a statement is invalid or incomplete
	 */
	public static ParsedStatement parseStatement(String lineIn) {
		Tokenizer t = new Tokenizer(lineIn);
		EnvisionLangParser p = new EnvisionLangParser();
		p.tokenLines = t.getLineTokens();
		p.tokens = t.getTokens();
		p.lines = t.getLines();
		return ParserHead.parse(p);
	}
	
	public static ParsedExpression parseExpression(String lineIn) {
		Tokenizer t = new Tokenizer(lineIn);
		EnvisionLangParser p = new EnvisionLangParser();
		p.tokenLines = t.getLineTokens();
		p.tokens = t.getTokens();
		p.lines = t.getLines();
		ParsedStatement s = ParserHead.parse(p);
		if (s instanceof Stmt_Expression e) return e.expression;
		throw new RuntimeException("Parsed type was not an expression! Was a '" + s.getClass() + "' instead!");
	}
	
	//-------------------------------------------------------------------------------------------
	
	//=================
	// Consume Methods
	//=================
	
	/**
	 * Attempts to match the current token's keyword to the IKeyword.
	 * <p>
	 * Note: This method requires that the given IKeyword matches the very next
	 * non-terminator token with the one exception being that the given
	 * IKeyword is in fact a terminator itself.
	 * 
	 * @param errorMessage An error message to be displayed in the event of a
	 *                     bad parse
	 * 
	 * @param keyword      The IKeyword to check for
	 * 
	 * @return The consumed token if matching.
	 */
	Token<?> consume(String errorMessage, IKeyword keyword) {
		if (check(keyword)) return getAdvance();
		throw error(errorMessage);
	}
	
	/**
	 * Attempts to match the current token's keyword to either of the given
	 * IKeywords.
	 * <p>
	 * Note: This method requires that one of the given IKeywords matches the
	 * very next non-terminator token with the one exception being that either
	 * of the given IKeywords are in fact terminators themselves.
	 * 
	 * @param errorMessage An error message to be displayed in the event of a
	 *                     bad parse
	 * 
	 * @param keywordA     The first IKeyword to try matching against
	 * @param keywordB     The second IKeyword to try matching against
	 * 
	 * @return The consumed token if matching.
	 */
	Token<?> consume(String errorMessage, IKeyword keywordA, IKeyword keywordB) {
		if (check(keywordA, keywordB)) return getAdvance();
		throw error(errorMessage);
	}
	
	/**
	 * Attempts to match the current token to one of the IKeyword's
	 * 'toCheck'.
	 * <p>
	 * Note: this method requires the 'toCheck' keyword to be matching,
	 * otherwise an error is thrown. Errors thrown from 'consume'
	 * statements should be treated as invalid statement parses and is
	 * quite often the result of a syntax error.
	 * 
	 * @param errorMessage An error message to be displayed in the event
	 *                     of a bad parse
	 *                     
	 * @param toCheck      The IKeyword's to check for
	 * 
	 * @return The consumed if matching.
	 */
	Token<?> consume(String errorMessage, IKeyword... toCheck) {
		if (check(toCheck)) return getAdvance();
		throw error(errorMessage);
	}
	
	/**
	 * Attempts to match the current token's keyword type to the keyword types.
	 * <p>
	 * Note: This method requires that the given keyword types matches the very
	 * next non-terminator token with the one exception being that either of
	 * the given IKeywords are in fact terminators themselves.
	 * 
	 * @param errorMessage An error message to be displayed in the event of a
	 *                     bad parse
	 * 
	 * @param type         The keyword type to try matching against
	 * 
	 * @return The consumed token if matching.
	 */
	Token<?> consumeType(String errorMessage, KeywordType type) {
		if (checkType(type)) return getAdvance();
		throw error(errorMessage);
	}
	
	/**
	 * Attempts to match the current token's keyword type to either of the
	 * given keyword types.
	 * <p>
	 * Note: This method requires that one of the given keyword types matches
	 * the very next non-terminator token with the one exception being that
	 * either of the given IKeywords are in fact terminators themselves.
	 * 
	 * @param errorMessage An error message to be displayed in the event of a
	 *                     bad parse
	 * 
	 * @param typeA        The first keyword type to try matching against
	 * @param typeB        The second keyword type to try matching against
	 * 
	 * @return The consumed token if matching.
	 */
	Token<?> consumeType(String errorMessage, KeywordType typeA, KeywordType typeB) {
		if (checkType(typeA, typeB)) return getAdvance();
		throw error(errorMessage);
	}
	
	/**
	 * Attempts to match the current token type to one of the KeywordTypes
	 * 'typesToCheck'.
	 * <p>
	 * Some tokens have specific types associated with them to help aid
	 * with disambiguation during parsing. As some statements or expressions
	 * could take potentially many different IKeywords and still remain valid,
	 * consuming a specific type of token may prove to be more efficient in
	 * the long run.
	 * <p>
	 * Note: this method requires the 'toCheck' keyword type to be
	 * matching, otherwise an error is thrown. Errors thrown from
	 * 'consume' statements should be treated as invalid statement parses
	 * and is quite often the result of a syntax error.
	 * 
	 * @param errorMessage An error message to be displayed in the event
	 *                     of a bad parse
	 * 					
	 * @param toCheck      The IKeyword's to check for
	 * 					
	 * @return The consumed if matching.
	 */
	Token<?> consumeType(String errorMessage, KeywordType... typesToCheck) {
		for (KeywordType t : typesToCheck) {
			// check to see if they are consuming a TERMINATOR
			// in which case, all empty lines should NOT be ignored
			if (t == KeywordType.TERMINATOR) {
				if (checkType(t)) return getAdvance();
			}
			else {
				// otherwise, ignore all empty lines until actual tokens are found again
				if (checkType(t)) return getAdvance();
			}
		}
		throw error(errorMessage);
	}
	
	//===============
	// Match Methods
	//===============
	
	/**
	 * If the current token's keyword matches the given IKeyword, then the
	 * current parsing position is incremented by 1 and true is returned. If
	 * the given IKeyword does not match the current token's keyword, then
	 * false is returned instead.
	 * 
	 * @param type An IKeyword to compare against the current token's keyword
	 * 
	 * @return true if the current token's keyword matches the given IKeyword
	 */
	boolean match(IKeyword toCheck) {
		if (check(toCheck)) {
			advance();
			return true;
		}
		return false;
	}
	
	/**
	 * If the current token's keyword matches either of the given IKeywords,
	 * then the current parsing position is incremented by 1 and true is
	 * returned. If either of the given IKeywords do not match the current
	 * token's keyword, then false is returned instead.
	 * 
	 * @param type The first keyword to compare against the current token's keyword
	 * @param type The second keyword to compare against the current token's keyword
	 * 
	 * @return true if the current token's keyword matches either of the given IKeywords
	 */
	boolean match(IKeyword keywordA, IKeyword keywordB) {
		if (check(keywordA) || check(keywordB)) {
			advance();
			return true;
		}
		return false;
	}
	
	/**
	 * If the current token matches any of the Keywords in 'toCheck' then
	 * the current parsing position is incremented by 1 and true is
	 * returned. If none of the Keywords in 'toCheck' match the current
	 * token, then false is returned instead.
	 * 
	 * @param toCheck A list of all Keywords to compare against the
	 *                current token
	 *                
	 * @return true if the current token matches any of the given
	 *         'toCheck' Keywords
	 */
	boolean match(IKeyword... toCheck) {
		for (int i = 0; i < toCheck.length; i++) {
			var k = toCheck[i];
			if (check(k)) {
				advance();
				return true;
			}
		}
		return false;
	}
	
	/**
	 * If the current token type matches the given KeywordType, then the
	 * current parsing position is incremented by 1 and true is returned. If
	 * the given KeywordType does not match the current token type, then false
	 * is returned instead.
	 * 
	 * @param type A KeywordType to compare against the current token type
	 * 
	 * @return true if the current token type matches the given KeywordType
	 */
	boolean matchType(KeywordType type) {
		if (checkType(type)) {
			advance();
			return true;
		}
		return false;
	}
	
	/**
	 * If the current token type matches either of the given KeywordTypes, then
	 * the current parsing position is incremented by 1 and true is returned.
	 * If neither of the given KeywordTypes match the current token type, then
	 * false is returned instead.
	 * 
	 * @param typeA The first type to compare against the current token type
	 * @param typeB The second type to compare against the current token type
	 * 
	 * @return true if the current token type matches either given KeywordType
	 */
	boolean matchType(KeywordType typeA, KeywordType typeB) {
		if (checkType(typeA) || checkType(typeB)) {
			advance();
			return true;
		}
		return false;
	}
	
	/**
	 * If the current token type matches any of the KeywordTypes in
	 * 'toCheck' then the current parsing position is incremented by 1 and
	 * true is returned. If none of the KeywordTypes in 'toCheck' match
	 * the current token type, then false is returned instead.
	 * 
	 * @param toCheck A list of all KeywordTypes to compare against the
	 *                current token type
	 * 				
	 * @return true if the current token type matches any of the given
	 *         'toCheck' KeywordTypes
	 */
	boolean matchType(KeywordType... types) {
		for (int i = 0; i < types.length; i++) {
			var t = types[i];
			if (checkType(t)) {
				advance();
				return true;
			}
		}
		return false;
	}
	
	//===============
	// Check Methods
	//===============
	
	/** Returns true if the current token's keyword matches any of the given keywords. */
	boolean check(IKeyword keyword) { return check(current(), keyword); }
	boolean check(IKeyword keywordA, IKeyword keywordB) { return check(current(), keywordA, keywordB); }
	boolean check(IKeyword... keywords) { return check(current(), keywords); }
	/** Returns true if the keyword of the token immediately after the current token index matches any of the given keywords. */
	boolean checkNext(IKeyword keyword) { return check(next(), keyword); }
	boolean checkNext(IKeyword keywordA, IKeyword keywordB) { return check(next(), keywordA, keywordB); }
	boolean checkNext(IKeyword... keywords) { return check(next(), keywords); }
	/** Returns true if the keyword of the token immediately before the current token index matches any of the given keywords. */
	boolean checkPrevious(IKeyword keyword) { return check(previous(), keyword); }
	boolean checkPrevious(IKeyword keywordA, IKeyword keywordB) { return check(previous(), keywordA, keywordB); }
	boolean checkPrevious(IKeyword... keywords) { return check(previous(), keywords); }
	
	public boolean checkAtIndex(int index, IKeyword keyword) { return check(getTokenAt(index), keyword); }
	public boolean checkAtIndex(int index, IKeyword keywordA, IKeyword keywordB) { return check(getTokenAt(index), keywordA, keywordB); }
	public boolean checkAtIndex(int index, IKeyword... keywords) { return check(getTokenAt(index), keywords); }
	
	/** Returns true if the next non-terminator token has any of the given keyword types. */
	boolean checkNextNonTerminator(IKeyword keyword) { return check(nextNonTerminator(), keyword); }
	boolean checkNextNonTerminator(IKeyword keywordA, IKeyword keywordB) { return check(nextNonTerminator(), keywordA, keywordB); }
	boolean checkNextNonTerminator(IKeyword... keywords) { return check(nextNonTerminator(), keywords); }
	
	/** Returns true if the previous non-terminator token has any of the given keyword types. */
	boolean checkPreviousNonTerminator(IKeyword keyword) { return check(previousNonTerminator(), keyword); }
	boolean checkPreviousTerminator(IKeyword keywordA, IKeyword keywordB) { return check(previousNonTerminator(), keywordA, keywordB); }
	boolean checkPreviousNonTerminator(IKeyword... keywords) { return check(previousNonTerminator(), keywords); }
	
	/**
	 * Returns true if the given token's keyword matches the given keyword.
	 * 
	 * @param t       The given token to check through
	 * @param keyword The keyword to check for
	 * 
	 * @return True if the given token's keyword matches the given keyword
	 */
	public static boolean check(Token<?> t, IKeyword keyword) {
		var kw = t.getKeyword();
		return kw == keyword;
	}
	
	/**
	 * Returns true if the given token's keyword matches any of the given keywords.
	 * 
	 * @param t        The given token to check through
	 * @param keywordA The first keyword to check for
	 * @param keywordB The second keyword to check for
	 * 
	 * @return True if the given token's keyword matches either of the given keywords
	 */
	public static boolean check(Token<?> t, IKeyword keywordA, IKeyword keywordB) {
		var kw = t.getKeyword();
		return kw == keywordA || kw == keywordB;
	}
	
	/**
	 * Returns true if the given token's keyword matches any of the given keywords.
	 * 
	 * @param t The given token to check through
	 * @param types The keywords to check for
	 * @return True if the given token's keyword matches any of the given keywords
	 */
	public static boolean check(Token<?> t, IKeyword... keywordsToCheck) {
		var kw = t.getKeyword();
		for (int i = 0; i < keywordsToCheck.length; i++) {
			var k = keywordsToCheck[i];
			if (kw == k) return true;
		}
		
		return false;
	}
	
	/** Returns true if the current token has any of the given keyword types. */
	boolean checkType(KeywordType type) { return checkType(current(), type); }
	boolean checkType(KeywordType typeA, KeywordType typeB) { return checkType(current(), typeA, typeB); }
	boolean checkType(KeywordType... types) { return checkType(current(), types); }
	/** Returns true if the token immediately after the current token index has any of the given keyword types. */
	boolean checkNextType(KeywordType type) { return checkType(next(), type); }
	boolean checkNextType(KeywordType typeA, KeywordType typeB) { return checkType(next(), typeA, typeB); }
	boolean checkNextType(KeywordType... types) { return checkType(next(), types); }
	/** Returns true if the token immediately before the current token index has any of the given keyword types. */
	boolean checkPreviousType(KeywordType type) { return checkType(previous(), type); }
	boolean checkPreviousType(KeywordType typeA, KeywordType typeB) { return checkType(previous(), typeA, typeB); }
	boolean checkPreviousType(KeywordType... types) { return checkType(previous(), types); }
	
	/**
	 * Returns true if the given token has the given keyword type.
	 * 
	 * @param t    The token to check through
	 * @param type The keyword type to check for
	 * 
	 * @return True if the given token has the given keyword type
	 */
	public static boolean checkType(Token<?> t, KeywordType type) {
		var kw = t.getKeyword();
		return kw.hasType(type);
	}
	
	/**
	 * Returns true if the given token has either of the given keyword types.
	 * 
	 * @param t     The token to check through
	 * @param typeA The keyword type to check for
	 * @param typeB The keyword type to check for
	 * 
	 * @return True if the given token has either of the given keyword types
	 */
	public static boolean checkType(Token<?> t, KeywordType typeA, KeywordType typeB) {
		var kw = t.getKeyword();
		return kw.hasType(typeA) || kw.hasType(typeB);
	}
	
	/**
	 * Returns true if the given token has any of the given keyword types.
	 * 
	 * @param t The given token to check through
	 * @param types The keyword types to check for
	 * @return True if the given token has any of the given types
	 */
	public static boolean checkType(Token<?> t, KeywordType... types) {
		var kw = t.getKeyword();
		for (int i = 0; i < types.length; i++) {
			var kt = types[i];
			if (kw.hasType(kt)) return true;
		}
		
		return false;
	}
	
	//=========================
	// Parsing Position Checks
	//=========================
	
	/**
	 * Returns true if the current token is the 'EOF' token.
	 * <p>
	 * If true, then the end of the file has been reached and no more tokens
	 * should be present beyond this point.
	 */
	boolean atEnd() {
		return current().isEOF();
	}
	
	/**
	 * Increments the current parsing position by 1 unless already at
	 * the end of the file/line.
	 */
	void advance() {
		if (!atEnd()) currentTokenIndex++;
	}
	
	/**
	 * Retrieves the current token and then advances the current parsing
	 * position by one.
	 * 
	 * @return The current token
	 */
	Token<?> getAdvance() {
		Token<?> cur = current();
		advance();
		return cur;
	}
	
	
	/**
	 * Continuously consumes terminator tokens until a non-terminator token is found.
	 * <p>
	 * A terminator token is either a ';' or a 'newline'.
	 */
	void consumeEmptyLines() {
		while (!atEnd()) {
			Token<?> curToken = current();
			IKeyword keyword = curToken.getKeyword();
			
			if (keyword.isTerminator()) {
				currentTokenIndex++;
				continue;
			}
			
			// break if the current token is a non-terminator keyword
			break;
		}
	}
	
	//==========================
	// Parsing Position Helpers
	//==========================
	
	/**
	 * Returns the token at the specified index.
	 * <p>
	 * Note: No boundary checks are made so this could blow up if a bad index
	 * is given!
	 * 
	 * @param index The index to grab a token at
	 * 
	 * @return The Token at the given index
	 */
	Token<?> getTokenAt(int index) {
		return tokens.get(index);
	}
	
	/**
	 * Returns the token at the current token position within the
	 * file/line currently being parsed.
	 * 
	 * @return The token at the current parsing position
	 */
	Token<?> current() {
		return tokens.get(currentTokenIndex);
	}
	
	/**
	 * Returns the keyword of the current token.
	 */
	IKeyword currentKeyword() {
		return current().getKeyword();
	}
	
	/**
	 * Returns the token directly in front of current token position
	 * within the file/line currently being parsed.
	 * 
	 * @return The token in front of the current parsing position
	 */
	Token<?> previous() {
		if (currentTokenIndex <= 0) return null;
		return tokens.get(currentTokenIndex - 1);
	}
	
	/**
	 * Returns the keyword of the token directly before the current token position
	 * within the file/line currently being parsed.
	 * 
	 * @return The keyword of the token directly before the current parsing position
	 */
	IKeyword previousKeyword() {
		var prevToken = previous();
		return (prevToken != null) ? prevToken.getKeyword() : null;
	}
	
	/**
	 * Returns the previous non-terminator token before the current token position.
	 * 
	 * @return The previous non-terminator token
	 */
	Token<?> previousNonTerminator() {
		int i = currentTokenIndex - 1;
		if (i < 0) return tokens.getFirst();
		
		var curToken = tokens.get(i);
		
		while (i > 0) {
			if (curToken.getKeyword().isTerminator()) {
				i--;
				curToken = tokens.get(i);
				continue;
			}
			
			break;
		}
		
		return curToken;
	}
	
	/**
	 * Returns the token directly after the current token position within
	 * the file/line currently being parsed.
	 * 
	 * @return The token directly after the current parsing position
	 */
	Token<?> next() {
		if (currentTokenIndex + 1 >= tokens.size()) return null;
		return tokens.get(currentTokenIndex + 1);
	}
	
	/**
	 * Returns the keyword of the token directly after the current token position
	 * within the file/line currently being parsed.
	 * 
	 * @return The keyword of the token directly after the current parsing position
	 */
	IKeyword nextKeyword() {
		var nextToken = next();
		return (nextToken != null) ? nextToken.getKeyword() : null;
	}
	
	/**
	 * Returns the next non-terminator token after the current token position.
	 * 
	 * @return The next non-terminator token
	 */
	Token<?> nextNonTerminator() {
		int i = currentTokenIndex + 1;
		var size = tokens.size();
		
		if (i >= size) return current();
		
		var curToken = tokens.get(i);
		
		while (i < size) {
			if (curToken.getKeyword().isTerminator()) {
				i++;
				curToken = tokens.get(i);
				continue;
			}
			
			break;
		}
		
		return curToken;
	}
	
	/**
	 * @return The list of all tokens being parsed through.
	 */
	EList<Token<?>> getTokens() {
		return tokens;
	}
	
	//=====================================
	// Current Token Position Manipulators
	//=====================================
	
	/**
	 * @return The current parsing position index.
	 */
	int getCurrentParsingIndex() {
		return currentTokenIndex;
	}
	
	/**
	 * Sets the current parsing position within the current file/line.
	 * In the event that the given position happens to be less than the
	 * start of the file, 0 is assigned instead.
	 * 
	 * @param in
	 */
	void setCurrentParsingIndex(int in) {
		currentTokenIndex = in;
		if (currentTokenIndex < 0) currentTokenIndex = 0;
	}
	
	/**
	 * Decrements the current parsing position by one.
	 * In the event that the current position is already at the start,
	 * no action is performed.
	 */
	void decrementParsingIndex() {
		if (currentTokenIndex == 0) return;
		currentTokenIndex--;
	}
	
	/**
	 * Increments the current parsing position by one.
	 * In the event that current position is already at the end,
	 * no action is performed.
	 */
	void incrementParsingIndex() {
		if (atEnd()) return;
		currentTokenIndex++;
	}
	
	//=========
	// Getters
	//=========
	
	public boolean areBlockStatementsEnabled() {
	    return enableBlockStatementParsing;
	}
	
	//==================================
	// Parsing Error Production Handler
	//==================================
	
	/**
	 * Generates an error with the given message at the current token position.
	 * <p>
	 * Note: This method does not actually throw the generated error in order
	 * to allow specific parsers to decide exactly how to handle the error.
	 * 
	 * @param message The error message to be displayed
	 * @return The generated error
	 */
	EnvisionLangError error(String message) {
		return new ParsingError("\n\n" + getErrorMessage(message) + "\n");
	}
	
	/**
	 * Dynamically generates an error message at the current token position.
	 * This method formats the current token line so that an '^' arrow is
	 * displayed directly below the problematic token in question.
	 * 
	 * @param message The error message to be displayed
	 * @return The generated error message
	 */
	private String getErrorMessage(String message) {
		// In the event that the problematic token is at the end of the file,
		// set the current token to the previous so that the problematic token
		// is not a hidden token.
		if (current().isEOF()) decrementParsingIndex();
		
		// grab the problematic token's line number
		int theLine = current().getLineNum();
		// if (theLine > tokenLines.size()) theLine -= 1;
		
		String tab = "    ";
		
		// The individual parts of the error message to be generated
		String border = "";
		String title = tab + "Parsing Error!";
		String lineNumber = tab + "Line " + theLine + ":";
		String line = tab + tab + lines.get(theLine - 1);
		String arrow = "";
		String error = tab + message + "   ->   '" + current() + "'";
		
		// determine border length
		String longest = EStringUtil.getLongest(title, lineNumber, line, error);
		border = EStringUtil.repeatString("-", longest.length() + 4);
		
		// get arrow position
		int arrow_pos = current().getLineIndex();
		
		// position arrow in string
		arrow = "\t\t" + EStringUtil.repeatString(" ", arrow_pos) + "^";
		
		// assemble the generated error message parts
		var generatedError = new StringBuilder();
		generatedError.append(border).append("\n")
					  .append(title).append("\n\n")
					  .append(lineNumber).append("\n")
					  .append(line).append("\n")
					  .append(arrow).append("\n\n")
					  .append(error).append("\n")
					  .append(border);
		
		return generatedError.toString();
	}
	
}
