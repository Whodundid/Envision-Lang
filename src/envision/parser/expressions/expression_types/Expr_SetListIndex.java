package envision.parser.expressions.expression_types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;

public class Expr_SetListIndex implements Expression {

	public final Expr_ListIndex list;
	public final Expression value;
	
	public Expr_SetListIndex(Expr_ListIndex listIn, Expression valueIn) {
		list = listIn;
		value = valueIn;
	}
	
	@Override
	public String toString() {
		return list + " = " + value;
	}
	
	@Override
	public Expr_SetListIndex copy() {
		return new Expr_SetListIndex(list.copy(), value.copy());
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		return handler.handleListIndexSet_E(this);
	}
	
}
