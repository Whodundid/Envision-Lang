package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;

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
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleListIndex_E(this);
	}
	
}
