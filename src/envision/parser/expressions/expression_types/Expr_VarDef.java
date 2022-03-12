package envision.parser.expressions.expression_types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.tokenizer.Token;
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
	public Object execute(ExpressionHandler handler) {
		return handler.handleVarDec_E(this);
	}
	
}
