package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.Token;

public class Expr_Lambda implements Expression {
	
	public final Expr_Compound inputs;
	public final Expr_Compound production;
	public final Token definingToken;
	
	public Expr_Lambda(Token start, Expression inputsIn, Expression productionIn) {
		inputs = Expr_Compound.wrap(start, inputsIn);
		production = Expr_Compound.wrap(start, productionIn);
		definingToken = start;
	}
	
	@Override
	public String toString() {
		return "(" + inputs + ") -> (" + production + ")";
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleLambda_E(this);
	}
	
	@Override
	public Token definingToken() {
		return definingToken;
	}
	
}
