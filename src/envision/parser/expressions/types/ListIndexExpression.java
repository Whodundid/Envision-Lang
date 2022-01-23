package envision.parser.expressions.types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;

public class ListIndexExpression implements Expression {
	
	public final Expression list;
	public final Expression index;
	
	public ListIndexExpression(Expression listIn, Expression indexIn) {
		list = listIn;
		index = indexIn;
	}
	
	@Override
	public String toString() {
		return list + "[" + index + "]";
	}
	
	@Override
	public ListIndexExpression copy() {
		return new ListIndexExpression(list.copy(), index.copy());
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		return handler.handleListIndex_E(this);
	}
	
}
