package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class Expr_ListInitializer implements Expression {

	public final EArrayList<Expression> values;
	public final Token<?> definingToken;
	
	public Expr_ListInitializer(Token<?> start) { this(start, new EArrayList<>()); }
	public Expr_ListInitializer(Token<?> start, EArrayList<Expression> valuesIn) {
		values = valuesIn;
		definingToken = start;
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
	
	@Override
	public Token<?> definingToken() {
		return definingToken;
	}
	
}
