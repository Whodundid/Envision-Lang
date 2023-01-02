package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.tokenizer.Token;

public class Expr_Var extends ParsedExpression {
	
	//========
	// Fields
	//========
	
	public final Token<?> name;
	
	//==============
	// Constructors
	//==============
	
	public Expr_Var(Token<?> nameIn) {
		super(nameIn);
		name = nameIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		return name.getLexeme() + "";
	}
	
	@Override
	public Expr_Var copy() {
		return new Expr_Var(name.copy());
	}
	
	@Override
	public EnvisionObject evaluate(ExpressionHandler handler) {
		return handler.handleVar_E(this);
	}
	
	//=========
	// Getters
	//=========
	
	public String getName() {
		return (name != null) ? name.getLexeme() : null;
	}
	
	//================
	// Static Methods
	//================
	
	public static Expr_Var of(Token in) {
		return new Expr_Var(in);
	}
	
}
