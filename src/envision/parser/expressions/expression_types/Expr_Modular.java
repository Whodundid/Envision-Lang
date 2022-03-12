package envision.parser.expressions.expression_types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.tokenizer.Token;
import eutil.datatypes.util.BoxList;

public class Expr_Modular implements Expression {
	
	public final BoxList<Token, Token> modulars;
	
	public Expr_Modular(BoxList<Token, Token> modularsIn) {
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