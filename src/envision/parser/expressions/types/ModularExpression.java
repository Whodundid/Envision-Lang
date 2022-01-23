package envision.parser.expressions.types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.tokenizer.Token;
import eutil.datatypes.util.BoxHolder;

public class ModularExpression implements Expression {
	
	public final BoxHolder<Token, Token> modulars;
	
	public ModularExpression(BoxHolder<Token, Token> modularsIn) {
		modulars = modularsIn;
	}
	
	@Override
	public String toString() {
		return "Modulars: " + modulars;
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		//return handler.handleModular_E(this);
		return null;
	}
	
}
