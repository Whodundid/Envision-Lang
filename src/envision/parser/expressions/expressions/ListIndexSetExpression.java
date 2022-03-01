package envision.parser.expressions.expressions;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;

public class ListIndexSetExpression implements Expression {

	public final ListIndexExpression list;
	public final Expression value;
	
	public ListIndexSetExpression(ListIndexExpression listIn, Expression valueIn) {
		list = listIn;
		value = valueIn;
	}
	
	@Override
	public String toString() {
		return list + " = " + value;
	}
	
	@Override
	public ListIndexSetExpression copy() {
		return new ListIndexSetExpression(list.copy(), value.copy());
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		return handler.handleListIndexSet_E(this);
	}
	
}
