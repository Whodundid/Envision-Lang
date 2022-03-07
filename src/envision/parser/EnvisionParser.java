package envision.parser;

import envision.EnvisionCodeFile;
import envision.exceptions.EnvisionError;
import envision.parser.statements.Statement;
import envision.tokenizer.IKeyword;
import envision.tokenizer.KeywordType;
import envision.tokenizer.Operator;
import envision.tokenizer.ReservedWord;
import envision.tokenizer.Token;
import envision.tokenizer.Tokenizer;
import eutil.datatypes.EArrayList;
import eutil.strings.StringUtil;

/**
 * The EnvisionParser handles the conversion of line tokens into
 * valid Envision code statements. Invalid or incomplete statements
 * are caught during parsing and are displayed to the user.
 * 
 * @author Hunter Bragg
 */
public class EnvisionParser {
	
	//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	
	/** The tokens of each line, line by line. Used to generate error messages. */
	private EArrayList<EArrayList<Token>> tokenLines;
	/** A complete list of all tokens within the file currently being parsed. */
	private EArrayList<Token> tokens;
	/** A non tokenized version of the file being parsed.
	 *  This is simply each line of the file in a list.
	 *  Used to help generate error messages. */
	private EArrayList<String> lines;
	/** A counter to keep track of the current token as parsing continues. */
	private int current = 0;
	
	//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Private so as to force use of static factory methods.
	 */
	private EnvisionParser() {}
	
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
	public static EArrayList<Statement> parse(EnvisionCodeFile codeFile) throws Exception {
		//error on invalid code files
		if (!codeFile.isValid()) throw new EnvisionError("Invalid CodeFile! Cannot parse!");
		
		//create a new isolated parser instance
		EnvisionParser p = new EnvisionParser();
		
		//unpack the code file's tokenized values
		EArrayList<Statement> statements = new EArrayList();
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
				Statement s = GenericParser.parse(p);
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
	 * lines of code which are parsed independantly from one another.
	 * <p>
	 * Invalid statements are caught during parsing and displayed as an error
	 * to the user.
	 * 
	 * @param lineIn The line to be parsed
	 * @return A valid statement
	 * @throws Exception In the event a statement is invalid or incomplete
	 */
	public static Statement parseStatement(String lineIn) throws Exception {
		Tokenizer t = new Tokenizer(lineIn);
		EnvisionParser p = new EnvisionParser();
		p.tokenLines = t.getLineTokens();
		p.tokens = t.getTokens();
		p.lines = t.getLines();
		return GenericParser.parse(p);
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
	protected Token consume(String errorMessage, IKeyword... toCheck) {
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
	protected Token consumeType(String errorMessage, KeywordType... typesToCheck) {
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
	protected boolean match(IKeyword... toCheck) {
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
	protected boolean matchType(KeywordType... type) {
		for (var t : type)
			if (checkType(t)) {
				advance();
				return true;
			}
		return false;
	}
	
	protected boolean matchBoth(IKeyword first, IKeyword second) {
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
	
	protected boolean checkAll(IKeyword... in) {
		if (in.length == 0) return false;
		int i = 0;
		for (IKeyword k : in) {
			Token t = tokens.get(current + i++);
			//System.out.println(i + " : " + t);
			if (!check(t, k)) return false;
		}
		return true;
	}
	
	protected boolean check(IKeyword... val) { return check(current(), val); }
	protected boolean checkNext(IKeyword... val) { return check(next(), val); }
	protected boolean checkPrevious(IKeyword... val) { return check(previous(), val); }
	
	protected boolean check(Token t, IKeyword... val) {
		if (atEnd()) {
			for (var k : val) if (k == ReservedWord.EOF) return true;
			return false;
		}
		for (var k : val) {
			if (t.keyword == k) return true;
		}
		return false;
	}
	
	protected boolean checkType(KeywordType... val) { return checkType(current(), val); }
	protected boolean checkNextType(KeywordType... val) { return checkType(next(), val); }
	protected boolean checkPreviousType(KeywordType... val) { return checkType(previous(), val); }
	
	private boolean checkType(Token t, KeywordType... type) {
		if (atEnd()) return false;
		for (var kt : type) {
			if (t.keyword.hasType(kt)) return true;
		}
		return false;
	}
	
	protected boolean checkAdvance(IKeyword k) {
		boolean val = check(k);
		if (val) advance();
		return val;
	}
	
	protected boolean checkAdvance(KeywordType k) {
		boolean val = checkType(k);
		if (val) advance();
		return val;
	}
	
	protected boolean atEnd() {
		return current().isEOF();
	}
	
	/**
	 * Increments the current parsing position by 1 unless already at
	 * the end of the file/line.
	 */
	protected void advance() {
		if (!atEnd()) current++;
	}
	
	/**
	 * Retrieves the current token and then advances the current parsing
	 * position by one.
	 * 
	 * @return The current token
	 */
	protected Token getAdvance() {
		Token cur = current();
		advance();
		return cur;
	}
	
	/**
	 * Continuously consumes empty lines or lines which only contain a
	 * semicolon.
	 */
	protected void consumeEmptyLines() {
		while (match(ReservedWord.NEWLINE, Operator.SEMICOLON));
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
	protected Token current() {
		return tokens.get(current);
	}
	
	/**
	 * Returns the token directly in front of current token position
	 * within the file/line currently being parsed.
	 * 
	 * @return The token in front of the current parsing position
	 */
	protected Token previous() {
		return tokens.get(current - 1);
	}
	
	/**
	 * Returns the token directly after the current token position within
	 * the file/line currently being parsed.
	 * 
	 * @return The token directly after the current parsing position
	 */
	protected Token next() {
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
	protected int getCurrentIndex() {
		return current;
	}
	
	/**
	 * Sets the current parsing position within the current file/line.
	 * In the event that the given position happens to be less than the
	 * start of the file, 0 is assigned instead.
	 * 
	 * @param in
	 */
	protected void setCurrentIndex(int in) {
		current = in;
		if (current < 0) current = 0;
	}
	
	/**
	 * Decrements the current parsing position by one.
	 * In the event that the current position is already at the start,
	 * no action is performed.
	 */
	protected void setPrevious() {
		if (current == 0) return;
		current--;
	}
	
	//-----------------------------------------------------------------------------------------------------
	// Error Production Hanler
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
	public EnvisionError error(String message) {
		return new EnvisionError("\n\n" + getErrorMessage(message) + "\n");
	}
	
	/**
	 * Dynamically generates an error message at the current token position.
	 * This method formats the current token line so that an '^' arrow is
	 * displayed directly below the problematic token in question.
	 * 
	 * @param message The error message to be dispalyed
	 * @return The generated error message
	 */
	public String getErrorMessage(String message) {
		//In the event that the problematic token is at the end of the file,
		//set the current token to the previous so that the problematic token
		//is not a hidden token.
		if (current().isEOF()) setPrevious();
		
		//grab the problematic token's line number
		int theLine = current().line;
		//if (theLine > tokenLines.size()) theLine -= 1;
		
		//The individual parts of the error message to be generated
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
		int problem_token_pos = 0;
		for (int i = 0; i < tokenLine.size(); i++) {
			if (tokenLine.get(i).checkID(current().id)) {
				problem_token_pos = i;
				break;
			}
		}
		
		//counter to keep track of the number of individual spaces that should
		//come before the arrow
		int spaces_before_arrow = 0;
		String actual_line = lines.get(theLine - 1);
		
		//iterate across each token in the line that is before the problem token
		//and add that token's string length to the spaces_before_arrow counter.
		for (int i = 0; i < problem_token_pos; i++) {
			Token t = tokenLine.get(i);
			int len = t.lexeme.length();
			//add the same number of spaces as the length of the token
			spaces_before_arrow += len;
			
			actual_line = actual_line.substring(len);
			StringBuilder spacer = new StringBuilder();
			
			//increment by 1 for any actual space
			for (int j = 0; j < actual_line.length(); j++) {
				if (actual_line.charAt(j) != ' ') {
					break;
				}
				spacer.append(" ");
			}
			
			//add the number of spaces
			spaces_before_arrow += spacer.length();
			//move onto the next token string-wise
			actual_line = actual_line.substring(spacer.length());
		}
		
		//position arrow in string
		arrow = "\t\t" + StringUtil.repeatString(" ", spaces_before_arrow) + "^";
		
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
