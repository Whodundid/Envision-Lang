package envision_lang.parser.expressions.expression_types.unused;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.parser.expressions.ParsedExpression;

public class Expr_Grouping extends ParsedExpression {
	
	//========
	// Fields
	//========
	
	public final ParsedExpression expression;
	
	//==============
	// Constructors
	//==============
	
	public Expr_Grouping(ParsedExpression expressionIn) {
		super(expressionIn);
		expression = expressionIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		String e = (expression != null) ? expression.toString() : "";
		return "(" + e + ")";
	}
	
	@Override
	public EnvisionObject evaluate(ExpressionHandler handler) {
		return null;
//		return handler.handleGrouping_E(this);
	}
	
}
