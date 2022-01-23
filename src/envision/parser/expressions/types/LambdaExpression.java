package envision.parser.expressions.types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;

public class LambdaExpression implements Expression {
	
	public final CompoundExpression inputs;
	public final CompoundExpression production;
	
	public LambdaExpression(Expression inputsIn, Expression productionIn) {
		inputs = CompoundExpression.wrap(inputsIn);
		production = CompoundExpression.wrap(productionIn);
	}
	
	@Override
	public String toString() {
		return "(" + inputs + ") -> (" + production + ")";
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		return handler.handleLambda_E(this);
	}
	
}
