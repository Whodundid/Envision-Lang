package envision.parser.expressions.expression_types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;

public class Expr_ListIndex implements Expression {
	
	public final Expression list;
	public final Expression index;
	
	public Expr_ListIndex(Expression listIn, Expression indexIn) {
		list = listIn;
		index = indexIn;
	}
	
	@Override
	public String toString() {
		return list + "[" + index + "]";
	}
	
	@Override
	public Expr_ListIndex copy() {
		return new Expr_ListIndex(list.copy(), index.copy());
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		return handler.handleListIndex_E(this);
	}
	
}
