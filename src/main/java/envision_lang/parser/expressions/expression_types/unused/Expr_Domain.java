package envision_lang.parser.expressions.expression_types.unused;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.tokenizer.Token;

public class Expr_Domain extends ParsedExpression {
	
	//========
	// Fields
	//========
	
	public final ParsedExpression left, middle, right;
	public final Token<?> lower, upper;
	
	//==============
	// Constructors
	//==============
	
	public Expr_Domain(ParsedExpression leftIn,
					   Token<?> lowerIn,
					   ParsedExpression middleIn,
					   Token<?> upperIn,
					   ParsedExpression rightIn)
	{
		super(leftIn);
		left = leftIn;
		lower = lowerIn;
		middle = middleIn;
		upper = upperIn;
		right = rightIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		return left + " " + lower.getLexeme() + " " + middle + " " + upper.getLexeme() + " " + right;
	}
	
	@Override
	public EnvisionObject evaluate(ExpressionHandler handler) {
		return null;
//		return handler.handleDomain_E(this);
	}
	
}