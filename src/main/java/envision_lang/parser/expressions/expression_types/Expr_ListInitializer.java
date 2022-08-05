package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import eutil.datatypes.EArrayList;

public class Expr_ListInitializer implements Expression {

	public final EArrayList<Expression> values;
	
	public Expr_ListInitializer() { this(new EArrayList<Expression>()); }
	public Expr_ListInitializer(EArrayList<Expression> valuesIn) {
		values = valuesIn;
	}
	
	public Expr_ListInitializer addValue(Expression in) {
		values.add(in);
		return this;
	}
	
	@Override
	public String toString() {
		return values.toString();
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleListInitializer_E(this);
	}
	
}
