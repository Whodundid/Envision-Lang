package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.Operator;
import envision_lang.tokenizer.Token;

public class Expr_SetListIndex implements Expression {

	public final Expr_ListIndex list;
	public final Expression value;
	public final Operator operator;
	public final Token definingToken;
	
	public Expr_SetListIndex(Expr_ListIndex listIn, Operator operatorIn, Expression valueIn) {
		list = listIn;
		operator = operatorIn;
		value = valueIn;
		definingToken = listIn.definingToken;
	}
	
	@Override
	public String toString() {
		return list + " = " + value;
	}
	
	@Override
	public Expr_SetListIndex copy() {
		return new Expr_SetListIndex(list.copy(), operator, value.copy());
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleListIndexSet_E(this);
	}
	
	@Override
	public Token definingToken() {
		return definingToken;
	}
	
}
