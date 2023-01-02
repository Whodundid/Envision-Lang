package envision_lang.parser.expressions;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.ParsedObject;
import envision_lang.tokenizer.Token;

public abstract class ParsedExpression extends ParsedObject {

	//==============
	// Constructors
	//==============
	
	protected ParsedExpression(ParsedObject startingObject) {
		this(startingObject.getStartingToken());
	}
	
	protected ParsedExpression(Token startingTokenIn) {
		super(startingTokenIn);
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public ParsedExpression copy() {
		// returns nothing by default
		return null;
	}
	
	//===========
	// Abstracts
	//===========
	
	public abstract EnvisionObject evaluate(ExpressionHandler handler);
	
}
