package envision_lang.parser;

import envision_lang.tokenizer.Token;

public abstract class ParsedObject {
	
	//========
	// Fields
	//========
	
	private Token startingToken;
	private int startingLine;
	
	//==============
	// Constructors
	//==============
	
	protected ParsedObject(Token startingTokenIn) {
		startingToken = startingTokenIn;
		startingLine = startingToken.getLineNum();
	}
	
	//=========
	// Methods
	//=========
	
	public abstract ParsedObject copy();
	
	public static ParsedObject copy(ParsedObject o) {
		return (o != null) ? o.copy() : null;
	}
	
	//=========
	// Getters
	//=========
	
	public Token getStartingToken() { return startingToken; }
	public int getStartingLine() { return startingLine; }
	
}
