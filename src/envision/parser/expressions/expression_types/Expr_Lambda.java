package envision.parser.expressions.expression_types;

import envision.lang.EnvisionObject;
import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;

public class Expr_Lambda implements Expression {
	
	public final Expr_Compound inputs;
	public final Expr_Compound production;
	
	public Expr_Lambda(Expression inputsIn, Expression productionIn) {
		inputs = Expr_Compound.wrap(inputsIn);
		production = Expr_Compound.wrap(productionIn);
	}
	
	@Override
	public String toString() {
		return "(" + inputs + ") -> (" + production + ")";
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleLambda_E(this);
	}
	
}
