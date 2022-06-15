package envision.parser.expressions.expression_types;

import envision.lang.EnvisionObject;
import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.tokenizer.Token;
import eutil.datatypes.BoxList;

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
	public EnvisionObject execute(ExpressionHandler handler) {
		//return handler.handleModular_E(this);
		return null;
	}
	
}
