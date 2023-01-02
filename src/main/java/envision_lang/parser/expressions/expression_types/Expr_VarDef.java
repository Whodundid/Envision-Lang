package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;
import eutil.strings.EStringUtil;

/** Used to declare variables within expressions. */
public class Expr_VarDef extends ParsedExpression {
	
	//========
	// Fields
	//========
	
	public final Token<?> type;
	public final EList<Token<?>> params;
	
	//==============
	// Constructors
	//==============
	
	public Expr_VarDef(Token<?> typeIn, EList<Token<?>> paramsIn) {
		super(typeIn);
		type = typeIn;
		params = paramsIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		String p = (params != null) ? "<" + EStringUtil.toString(params, ", ") + ">": "";
		return type.getLexeme() + p;
	}
	
	@Override
	public Expr_VarDef copy() {
		return new Expr_VarDef(type.copy(), params);
	}
	
	@Override
	public EnvisionObject evaluate(ExpressionHandler handler) {
		return handler.handleVarDec_E(this);
	}
	
}
