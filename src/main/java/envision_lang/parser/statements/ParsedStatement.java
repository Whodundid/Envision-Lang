package envision_lang.parser.statements;

import envision_lang.parser.ParsedObject;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;

public abstract class ParsedStatement extends ParsedObject {

	//========
	// Fields
	//========
	
	protected ParserDeclaration declaration;
	
	//==============
	// Constructors
	//==============
	
	protected ParsedStatement(Token startingTokenIn) {
		this(startingTokenIn, null);
	}
	
	protected ParsedStatement(Token startingTokenIn, ParserDeclaration declarationIn) {
		super(startingTokenIn);
		declaration = declarationIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public ParsedStatement copy() {
		// returns nothing by default
		return null;
	}
	
	//===========
	// Abstracts
	//===========
	
	public abstract void execute(StatementHandler handler);
	
	//=========
	// Getters
	//=========
	
	public ParserDeclaration getDeclaration() {
		return declaration;
	}
	
}
