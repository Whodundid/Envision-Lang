package envision.parser.expressions.expressions;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.tokenizer.Token;
import eutil.datatypes.util.BoxList;

public class ModularExpression implements Expression {
	
	public final BoxList<Token, Token> modulars;
	
	public ModularExpression(BoxList<Token, Token> modularsIn) {
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
