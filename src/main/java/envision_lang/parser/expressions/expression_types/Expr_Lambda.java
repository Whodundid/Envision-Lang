package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.tokenizer.Token;

public class Expr_Lambda extends ParsedExpression {
	
	//========
	// Fields
	//========
	
	public final Expr_Compound inputs;
	public final Expr_Compound production;
	
	//==============
	// Constructors
	//==============
	
	public Expr_Lambda(Token<?> start, ParsedExpression inputsIn, ParsedExpression productionIn) {
		super(start);
		inputs = Expr_Compound.wrap(start, inputsIn);
		production = Expr_Compound.wrap(start, productionIn);
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		return "(" + inputs + ") -> (" + production + ")";
	}
	
	@Override
	public EnvisionObject evaluate(ExpressionHandler handler) {
		return handler.handleLambda_E(this);
	}
	
}
