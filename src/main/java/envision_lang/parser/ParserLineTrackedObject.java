package envision_lang.parser;

import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;

public class ParserLineTrackedObject {
	
	//========
	// Fields
	//========
	
	private Token startingToken;
	private int startingLine;
	private int endingLine;
	private EList<Token> tokens;
	
	//==============
	// Constructors
	//==============
	
	protected ParserLineTrackedObject(Token startingTokenIn) {
		startingToken = startingTokenIn;
		startingLine = startingToken.getLineNum();
		tokens.add(startingToken);
	}
	
	//=========
	// Methods
	//=========
	
	public void addToken(Token toAdd) {
		if (toAdd == null) return;
		tokens.add(toAdd);
		endingLine = toAdd.getLineNum();
	}
	
	//=========
	// Getters
	//=========
	
	public Token getStartingToken() { return startingToken; }
	public int getStartingLine() { return startingLine; }
	public int getEndingLine() { return endingLine; }
	public EList<Token> getTokens() { return tokens; }
	
}
