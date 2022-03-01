package envision.parser.statements.statementUtil;

import envision.parser.expressions.Expression;
import envision.tokenizer.Token;

public class VariableDeclaration {
	
	public final Token name;
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
