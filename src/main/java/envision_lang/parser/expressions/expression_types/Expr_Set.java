package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.tokenizer.Token;

public class Expr_Set extends ParsedExpression {
	
	//========
	// Fields
	//========
	
	public final ParsedExpression object, value;
	public final Token<?> name;
	
	//==============
	// Constructors
	//==============
	
	public Expr_Set(ParsedExpression objectIn, Token<?> nameIn, ParsedExpression valueIn) {
		super(objectIn);
		object = objectIn;
		name = nameIn;
		value = valueIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		return object + "." + name + " = " + value;
	}
	
	@Override
	public Expr_Set copy() {
		return new Expr_Set(object.copy(), Token.copy(name), value.copy());
	}
	
	@Override
	public EnvisionObject evaluate(ExpressionHandler handler) {
		return handler.handleSet_E(this);
	}
	
}
