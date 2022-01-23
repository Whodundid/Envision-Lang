package envision.parser.expressions.types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import eutil.datatypes.EArrayList;

public class ListInitializerExpression implements Expression {

	public final EArrayList<Expression> values;
	
	public ListInitializerExpression() { this(new EArrayList<Expression>()); }
	public ListInitializerExpression(EArrayList<Expression> valuesIn) {
		values = valuesIn;
	}
	
	public ListInitializerExpression addValue(Expression in) {
		values.add(in);
		return this;
	}
	
	@Override
	public String toString() {
		return values.toString();
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		return handler.handleListInitializer_E(this);
	}
	
}
