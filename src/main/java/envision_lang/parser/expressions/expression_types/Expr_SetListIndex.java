package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.tokenizer.Operator;

public class Expr_SetListIndex extends ParsedExpression {
	
	//========
	// Fields
	//========
	
	public final Expr_ListIndex list;
	public final ParsedExpression value;
	public final Operator operator;
	
	//==============
	// Constructors
	//==============
	
	public Expr_SetListIndex(Expr_ListIndex listIn, Operator operatorIn, ParsedExpression valueIn) {
		super(listIn);
		list = listIn;
		operator = operatorIn;
		value = valueIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		return list + " = " + value;
	}
	
	@Override
	public Expr_SetListIndex copy() {
		return new Expr_SetListIndex(list.copy(), operator, value.copy());
	}
	
	@Override
	public EnvisionObject evaluate(ExpressionHandler handler) {
		return handler.handleListIndexSet_E(this);
	}
	
}
