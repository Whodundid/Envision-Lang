package envision_lang.parser.util;

import envision_lang.parser.expressions.Expression;
import envision_lang.tokenizer.Token;

public class VariableDeclaration {
	
	public final Token<String> name;
	public final Expression assignment_value;
	
	public VariableDeclaration(Token nameIn, Expression valueIn) {
		name = nameIn;
		assignment_value = valueIn;
	}
	
	public String getName() {
		return name.lexeme;
	}
	
	@Override
	public String toString() {
		String v = (assignment_value != null) ? " = " + assignment_value.toString() : "";
		return name.lexeme + v;
	}
	
}
