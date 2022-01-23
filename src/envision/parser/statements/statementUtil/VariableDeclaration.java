package envision.parser.statements.statementUtil;

import envision.parser.expressions.Expression;
import envision.tokenizer.Token;

public class VariableDeclaration {
	
	public final Token name;
	public final Expression value;
	
	public VariableDeclaration(Token nameIn, Expression valueIn) {
		name = nameIn;
		value = valueIn;
	}
	
	public String getName() { return name.lexeme; }
	
	@Override
	public String toString() {
		String v = (value != null) ? " = " + value.toString() : "";
		return name.lexeme + v;
	}
	
}
