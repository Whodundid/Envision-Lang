package envision.tokenizer;

import envision.EnvisionCodeFile;
import eutil.datatypes.EArrayList;

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
	/** Used to keep track of when in a descriptor comment. */
	private boolean inDescriptor = false;
	/** Used to keep track of the current line internally. */
	private int lineNum = 1;
	
	private EArrayList<Token> createdTokens;
	
	//--------------------------------------------------------------------------------------------------------------------
	
	public Tokenizer() {}
	
	public Tokenizer(EnvisionCodeFile codeFileIn) {
		theFile = codeFileIn;
	}
	
	public Tokenizer(String line) {
		theFile = null;
		tokenizeLine(line);
	}
	
	//--------------------------------------------------------------------------------------------------------------------
	
	public void setCodeFile(EnvisionCodeFile codeFileIn) {
		theFile = codeFileIn;
	}
	
	public boolean hasFile() {
		return theFile != null;
	}
	
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
					boolean empty = l.trim().isEmpty();
					
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
				}
				
				Token EOF = Token.EOF(lineNum);
				lineTokens.add(new EArrayList<Token>(EOF));
				tokens.add(EOF);
				lines.add("EOF");
				
				return true;
			}
		}
		return false;
	}
	
	/** Tokenizes a single line. */
	public boolean tokenizeLine(String lineIn) {
		lineIn = lineIn.replace("\t", "");
		EArrayList<Token> list = tokenizeLine(lineIn, 0);
		lineTokens.add(list);
		tokens.addAll(list);
		tokens.add(Token.newLine(0));
		lines.add(stripComments(lineIn));
		lines.add("EOF");
		return true;
	}
	
	/** Separates a single input line's characters into valid Envision tokens. */
	private EArrayList<Token> tokenizeLine(String line, int lineNum) {
		createdTokens = new EArrayList();
		
		//the character position the line should start at
		int lineStart = 0;
		
		//check for basic comment
		if (line.startsWith(Keyword.COMMENT.chars)) { return createdTokens; }
		
		//check for multi-line comment start
		if (line.startsWith(Keyword.COMMENT_START.chars)) {
			lineStart = Keyword.COMMENT_START.chars.length();
			inComment = true;
		}
		else if (line.startsWith(Keyword.COMMENT_DESCRIPTOR.chars)) {
			lineStart = Keyword.COMMENT_DESCRIPTOR.chars.length();
			inComment = true;
			inDescriptor = true;
		}
		
		//check for multi-line comment end
		if (inComment && line.startsWith(Keyword.COMMENT_END.chars)) {
			lineStart = Keyword.COMMENT_END.chars.length();
			inComment = false;
			inDescriptor = false;
		}
		
		String cur = ""; //the current partial token being isolated
		String op = ""; //the current operator token being isolated
		char c = '\u0000'; //the current char that the tokenizer is on
		boolean inStr = false; //flag to indicate if the current token is a string or not
		boolean inNumber = false; //flag to indicate if the current token is a number
		boolean append = true; //flag to indicate if the current character should be appended to the current token in progress
		boolean atStart = true; //flag to indicate if there was a space
		boolean operatorCheck = false; //flag for when there is currently an operator being parsed
		boolean endComment = false; //flag for when a comment is declared somewhere at the end of a line statement
		boolean multiFirst = false; //flag when the first char of the end multi-line comment operater is found 
		
		for (int i = lineStart; i < line.length(); i++) {
			c = line.charAt(i);
			
			//System.out.println("the char: '" + c + "'  cur: '" + cur + "'  op: '" + op + "'  str: " + inStr + "  num: " + inNumber);
			
			if (inComment) {
				if (multiFirst) {
					if (Keyword.COMMENT_END.chars.charAt(1) == c) {
						inComment = false;
					}
					else { multiFirst = Keyword.COMMENT_END.chars.charAt(0) == c; }
				}
				else { multiFirst = Keyword.COMMENT_END.chars.charAt(0) == c; }
			}
			else {
				append = true;
				
				//first check to see if this is the start of a new token
				if (atStart) {
					atStart = false;
					//check if the first character of this partial token is a number
					if (Character.isDigit(c) || (c == '-' && i + 1 < line.length() - 1) && Character.isDigit(line.charAt(i + 1))) {
						inNumber = true;
						cur += c;
						continue;
					}
				}
				
				//next check if there are strings to deal with
				if (c == '"') {
					if (inStr) {
						inStr = false;
						addToken(!cur.trim().isEmpty(), cur + c, lineNum);
						cur = "";
						op = "";
						inNumber = false;
						atStart = true;
						operatorCheck = false;
						continue;
					}
					else {
						inStr = true;
						addToken(!cur.trim().isEmpty(), cur.trim(), lineNum);
						cur = "" + c;
						op = "";
						inNumber = false;
						atStart = true;
						operatorCheck = false;
						continue;
					}
				}
				
				if (!inStr) {
					/*
					if (inNumber) {
						if (!Character.isDigit(c)) {
							addToken(!cur.trim().isEmpty(), cur.trim(), lineNum);
							cur = "" + c;
							inNumber = false;
							continue;
						}
						
						cur += c;
						continue;
					}
					*/
					
					//check for continuing operators
					if (operatorCheck) {
						Keyword operator = Keyword.isStillOperator(cur);
						
						//check if the operator is a basic comment start
						if (Keyword.getOperator(cur + c) == Keyword.COMMENT) {
							//System.out.println("CUR: " + cur + c);
							endComment = true;
							break;
						}
						
						//check if a multi comment is starting
						if (Keyword.getOperator(cur + c) == Keyword.COMMENT_START) {
							cur = "";
							op = "";
							inComment = true;
							continue;
						}
						
						//check for periods when dealing with numbers
						if (operator == Keyword.PERIOD) {
							if (inNumber) {
								cur += c;
								op = "";
								operatorCheck = false;
								continue;
							}
						}
						
						//if the operator is now null, the end of an operator has been found
						if (operator == null) {
							cur = cur.substring(0, cur.length() - op.length());
							addToken(!cur.trim().isEmpty(), cur.trim(), lineNum);
							addToken(!op.trim().isEmpty(), op.trim(), lineNum);
							
							//System.out.println("CUR: " + c);
							//System.out.println("CUR CUR: " + cur);
							//System.out.println("CUR OP: " + op);
							
							cur = "" + c;
							op = "";
							append = false;
							atStart = true;
							operatorCheck = Keyword.isOperatorStart(c);
						}
						else {
							operator = Keyword.isStillOperator(op + c);
							
							//System.out.println("not null: " + cur);
							
							if (operator == null) {
								addToken(!cur.trim().isEmpty(), cur.trim(), lineNum);
								inNumber = Character.isDigit(c);
								cur = (c == ' ') ? "" : c + "";
								op = "";
								operatorCheck = Keyword.isOperatorStart(c);
								atStart = true;
								append = false;
								continue;
							}
							
							op += c;
						}
					}
					//check if operator start
					else if (Keyword.isOperatorStart(c)) {
						operatorCheck = true;
						
						if (inNumber && Keyword.PERIOD.chars.equals("" + c)) {
							cur += c;
							operatorCheck = false;
							continue;
						}
						
						//add the current string as a token
						addToken(!cur.trim().isEmpty(), cur.trim(), lineNum);
						cur = c + "";
						op = c + "";
						continue;
					}
					
					//check for spaces
					if (c == ' ') {
						addToken(!cur.trim().isEmpty(), cur.trim(), lineNum);
						cur = "";
						op = "";
						append = false;
						atStart = true;
						operatorCheck = false;
					}
				}
				
				if (append) { cur += c; }
			}

		}
		
		if (!inComment && !endComment && !cur.isEmpty()) {
			//first check if cur is an operator all by itself
			Keyword operator = Keyword.getOperator(cur);
			
			if (operator != null) {
				addToken(!cur.trim().isEmpty(), cur.trim(), lineNum);
				cur = "";
			}
			//if cur wasn't an operator, check to see if the very last character is an operator instead
			else {
				operator = Keyword.getOperator(c + "");
				
				//if there was actually an operator, isolate cur from the operator and add both if they aren't empty
				if (operator != null) {
					
					//remove the operator from the current string
					cur = cur.substring(0, cur.length() - 1);
					
					//add the current string as a token
					addToken(!cur.trim().isEmpty(), cur.trim(), lineNum);
					
					String opStr = c + "";
					
					//now add the operator
					addToken(!opStr.trim().isEmpty(), opStr.trim(), lineNum);
				}
			}
			
			//no operator was found, add the current string if it's not empty
			if (operator == null) {
				addToken(!cur.trim().isEmpty(), cur.trim(), lineNum);
			}
		}
		
		return createdTokens;
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
	
	private void addToken(boolean cond, String tokenIn, int line) {
		if (cond) {
			createdTokens.addIf(cond, new Token(tokenIn, line));
		}
	}
	
	//--------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------
	
	/** Removes both single line and multiline comments from strings. */
	public static String stripComments(String in) {
		if (in.startsWith("//")) { return ""; }
		String cur = "";
		
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
					else { start = false; }
				}
				else if (c == '*') { multistart = true; }
			}
			else {
				if (start) {
					if (c == '/') { break; }
					if (c == '*') {
						inMultiComment = true;
						cur = cur.substring(0, cur.length() - 1);
					}
				}
				else if (c == '/') { start = true; }
			}
			
			if (!inMultiComment) { cur += c; }
		}
		
		return cur;
	}
	
}
