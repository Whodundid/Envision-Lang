package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.Token;
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
