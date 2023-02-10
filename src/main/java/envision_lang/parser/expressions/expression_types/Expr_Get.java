package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.tokenizer.Token;

public class Expr_Get extends ParsedExpression {
	
	//========
	// Fields
	//========
	
	public final ParsedExpression object;
	public final Token<?> name;
	
	//==============
	// Constructors
	//==============
	
	public Expr_Get(ParsedExpression objectIn, Token<?> nameIn) {
		super(objectIn);
		object = objectIn;
		name = nameIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		return object + "." + name.getLexeme();
	}
	
	@Override
	public Expr_Get copy() {
		return new Expr_Get(object.copy(), name.copy());
	}
	
	@Override
	public EnvisionObject evaluate(ExpressionHandler handler) {
		return handler.handleGet_E(this);
	}
	
}
