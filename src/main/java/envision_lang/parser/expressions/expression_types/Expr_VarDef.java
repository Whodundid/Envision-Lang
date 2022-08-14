package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.Token;
import eutil.datatypes.EArrayList;
import eutil.strings.StringUtil;

/** Used to declare variables within expressions. */
public class Expr_VarDef implements Expression {

	public final Token type;
	public final EArrayList<Token> params;
	
	public Expr_VarDef(Token typeIn, EArrayList<Token> paramsIn) {
		type = typeIn;
		params = paramsIn;
	}
	
	@Override
	public String toString() {
		String p = (params != null) ? "<" + StringUtil.toString(params, ", ") + ">": "";
		return type.lexeme + p;
	}
	
	@Override
	public Expr_VarDef copy() {
		return new Expr_VarDef(Token.copy(type), params);
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleVarDec_E(this);
	}
	
}
