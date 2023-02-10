package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.parser.expressions.ParsedExpression;

public class Expr_ListIndex extends ParsedExpression {
	
	//========
	// Fields
	//========
	
	public final ParsedExpression list;
	public final ParsedExpression index;
	
	//==============
	// Constructors
	//==============
	
	public Expr_ListIndex(ParsedExpression listIn, ParsedExpression indexIn) {
		super(listIn);
		list = listIn;
		index = indexIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		return list + "[" + index + "]";
	}
	
	@Override
	public Expr_ListIndex copy() {
		return new Expr_ListIndex(list.copy(), index.copy());
	}
	
	@Override
	public EnvisionObject evaluate(ExpressionHandler handler) {
		return handler.handleListIndex_E(this);
	}
	
}
