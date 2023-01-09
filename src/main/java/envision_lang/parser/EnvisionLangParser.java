package envision_lang.parser;

import envision_lang._launch.EnvisionCodeFile;
import envision_lang.exceptions.EnvisionLangError;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.statement_types.Stmt_Expression;
import envision_lang.tokenizer.IKeyword;
import envision_lang.tokenizer.KeywordType;
import envision_lang.tokenizer.ReservedWord;
import envision_lang.tokenizer.Token;
import envision_lang.tokenizer.Tokenizer;
import eutil.EUtil;
import eutil.datatypes.BoxList;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.strings.EStringUtil;

/**
 * The EnvisionParser handles the conversion of line tokens into
 * valid Envision code statements. Invalid or incomplete statements
 * are caught during parsing and are displayed to the user.
 * 
 * @author Hunter Bragg
 */
public final class EnvisionLangParser {
	
	//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	
	/** The tokens of each line, line by line. Used to generate error messages. */
	private BoxList<Integer, EList<Token<?>>> tokenLines;
	/** A complete list of all tokens within the file currently being parsed. */
	private EList<Token<?>> tokens;
	/** A non tokenized version of the file being parsed.
	 *  This is simply each line of the file in a list.
	 *  Used to help generate error messages. */
	private EList<String> lines;
	/** A counter to keep track of the current token as parsing continues. */
	private int current = 0;
	
	//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Private so as to force use of static factory methods.
	 */
	private EnvisionLangParser() {}
	
	//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	
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
	public static EList<ParsedStatement> parse(EnvisionCodeFile codeFile) throws Exception {
		//error on invalid code files
		if (!codeFile.isValid()) throw new EnvisionLangError("Invalid CodeFile! Cannot parse!");
		
		//create a new isolated parser instance
		EnvisionLangParser p = new EnvisionLangParser();
		
		//unpack the code file's tokenized values
		EList<ParsedStatement> statements = new EArrayList<>();
		p.tokenLines = codeFile.getLineTokens();
		p.tokens = codeFile.getTokens();
		p.lines = codeFile.getLines();
		
		//Any error that is thrown during parsing
		//ParsingError error = null;
		
		//ignore empty files and return an empty statement list
		if (p.tokens.isEmpty()) return statements;
		
		try {
			//continue until the end of the file
			while (!p.atEnd()) {
				ParsedStatement s = ParserHead.parse(p);
				//only add non-null statements
				statements.addIf(s != null, s);
			}
		}
		catch (Exception e) {
			//wrap the thrown exception into a parsing error
			//error = new ParsingError(e);
			throw e;
		}
		
		//throw the wrapped error (if there is one)
		//if (error != null) throw error;
		//otherwise return parsed statements
		return statements;
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
	
	//-----------------------------------------------------------------------------------------------------
	// Factory Creation Methods
	//-----------------------------------------------------------------------------------------------------
	
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
		consumeEmptyLines();
		if (check(toCheck)) return getAdvance();
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
		consumeEmptyLines();
		if (checkType(typesToCheck)) return getAdvance();
		throw error(errorMessage);
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
		for (var o : toCheck)
			if (check(o)) {
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
	boolean matchType(KeywordType... type) {
		for (var t : type)
			if (checkType(t)) {
				advance();
				return true;
			}
		return false;
	}
	
	boolean matchBoth(IKeyword first, IKeyword second) {
		if (check(first)) {
			Token<?> n = next();
			if (n != null && n.getKeyword() == second) {
				advance();
				advance();
				return true;
			}
		}
		return false;
	}
	
	boolean checkAll(IKeyword... in) {
		if (in.length == 0) return false;
		int i = 0;
		for (IKeyword k : in) {
			Token<?> t = tokens.get(current + i++);
			//System.out.println(i + " : " + t);
			if (!check(t, k)) return false;
		}
		return true;
	}
	
	boolean check(IKeyword... val) { return check(current(), val); }
	boolean checkNext(IKeyword... val) { return check(next(), val); }
	boolean checkPrevious(IKeyword... val) { return check(previous(), val); }
	
	boolean check(Token<?> t, IKeyword... val) {
		if (atEnd()) {
			for (var k : val) {
				if (k == ReservedWord.EOF) return true;
			}
			return false;
		}
		for (var k : val) {
			if (t.getKeyword() == k) return true;
		}
		return false;
	}
	
	boolean checkType(KeywordType... val) { return checkType(current(), val); }
	boolean checkNextType(KeywordType... val) { return checkType(next(), val); }
	boolean checkPreviousType(KeywordType... val) { return checkType(previous(), val); }
	
	private boolean checkType(Token<?> t, KeywordType... type) {
		//if at end of file, check if the given keyword types contains TERMINATOR
		if (atEnd()) return EUtil.contains(type, KeywordType.TERMINATOR);
		for (var kt : type) {
			if (t.getKeyword().hasType(kt)) return true;
		}
		return false;
	}
	
	boolean checkAdvance(IKeyword k) {
		boolean val = check(k);
		if (val) advance();
		return val;
	}
	
	boolean checkAdvance(KeywordType k) {
		boolean val = checkType(k);
		if (val) advance();
		return val;
	}
	
	boolean atEnd() {
		return current().isEOF();
	}
	
	/**
	 * Increments the current parsing position by 1 unless already at
	 * the end of the file/line.
	 */
	void advance() {
		if (!atEnd()) current++;
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
	 * Continuously consumes empty lines or lines which only contain a
	 * semicolon.
	 */
	void consumeEmptyLines() {
		while (!atEnd() && matchType(KeywordType.TERMINATOR));
	}
	
	//-----------------------------------------------------------------------------------------------------
	// Simple Parsing Position Helpers
	//-----------------------------------------------------------------------------------------------------
	
	/**
	 * Returns the token at the current token position within the
	 * file/line currently being parsed.
	 * 
	 * @return The token at the current parsing position
	 */
	Token<?> current() {
		return tokens.get(current);
	}
	
	/**
	 * Returns the token directly in front of current token position
	 * within the file/line currently being parsed.
	 * 
	 * @return The token in front of the current parsing position
	 */
	Token<?> previous() {
		return tokens.get(current - 1);
	}
	
	/**
	 * Returns the token directly after the current token position within
	 * the file/line currently being parsed.
	 * 
	 * @return The token directly after the current parsing position
	 */
	Token<?> next() {
		return tokens.get(current + 1);
	}
	
	//-----------------------------------------------------------------------------------------------------
	// Current Token Position Manipulators
	//-----------------------------------------------------------------------------------------------------
	
	/**
	 * Returns the current parsing position.
	 * 
	 * @return the current parsing position.
	 */
	int getCurrentIndex() {
		return current;
	}
	
	/**
	 * Sets the current parsing position within the current file/line.
	 * In the event that the given position happens to be less than the
	 * start of the file, 0 is assigned instead.
	 * 
	 * @param in
	 */
	void setCurrentIndex(int in) {
		current = in;
		if (current < 0) current = 0;
	}
	
	/**
	 * Decrements the current parsing position by one.
	 * In the event that the current position is already at the start,
	 * no action is performed.
	 */
	void setPrevious() {
		if (current == 0) return;
		current--;
	}
	
	//-----------------------------------------------------------------------------------------------------
	// Error Production Handler
	//-----------------------------------------------------------------------------------------------------
	
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
		return new EnvisionLangError("\n\n" + getErrorMessage(message) + "\n");
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
		//In the event that the problematic token is at the end of the file,
		//set the current token to the previous so that the problematic token
		//is not a hidden token.
		if (current().isEOF()) setPrevious();
		
		//grab the problematic token's line number
		int theLine = current().getLineNum();
		//if (theLine > tokenLines.size()) theLine -= 1;
		
		String tab = "    ";
		
		//The individual parts of the error message to be generated
		String border = "";
		String title = tab + "Parsing Error!";
		String lineNumber = tab + "Line " + theLine + ":";
		String line = tab + tab + lines.get(theLine - 1);
		String arrow = "";
		String error = tab + message + "   ->   '" + current() + "'";
		
		//determine border length
		String longest = EStringUtil.getLongest(title, lineNumber, line, error);
		border = EStringUtil.repeatString("-", longest.length() + 4);
		
		//get arrow position
		int arrow_pos = current().getLineIndex();
		
		//position arrow in string
		arrow = "\t\t" + EStringUtil.repeatString(" ", arrow_pos) + "^";
		
		//assemble the generated error message parts
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
