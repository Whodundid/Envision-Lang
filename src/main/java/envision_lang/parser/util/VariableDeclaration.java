package envision_lang.parser.util;

import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.tokenizer.Token;

public class VariableDeclaration {
	
	//========
	// Fields
	//========
	
	public final Token<String> name;
	public final ParsedExpression assignment_value;
	
	//==============
	// Constructors
	//==============
	
	public VariableDeclaration(Token<String> nameIn, ParsedExpression valueIn) {
		name = nameIn;
		assignment_value = valueIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		String v = (assignment_value != null) ? " = " + assignment_value.toString() : "";
		return name.getLexeme() + v;
	}
	
	//=========
	// Getters
	//=========
	
	public String getName() {
		return name.getLexeme();
	}
	
}
