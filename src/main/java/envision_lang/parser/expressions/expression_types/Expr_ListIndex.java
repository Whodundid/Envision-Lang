package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.Token;

public class Expr_ListIndex implements Expression {
	
	public final Expression list;
	public final Expression index;
	public final Token<?> definingToken;
	
	public Expr_ListIndex(Expression listIn, Expression indexIn) {
		list = listIn;
		index = indexIn;
		definingToken = listIn.definingToken();
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
	
	@Override
	public Token<?> definingToken() {
		return definingToken;
	}
	
}
