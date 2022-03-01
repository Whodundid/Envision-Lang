package envision.parser.expressions.expressions;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;
import eutil.strings.StringUtil;

/** Used to declare variables within expressions. */
public class VarDecExpression implements Expression {

	public final Token type;
	public final EArrayList<Token> params;
	
	public VarDecExpression(Token typeIn, EArrayList<Token> paramsIn) {
		type = typeIn;
		params = paramsIn;
	}
	
	@Override
	public String toString() {
		String p = (params != null) ? "<" + StringUtil.toString(params, ", ") + ">": "";
		return type.lexeme + p;
	}
	
	@Override
	public VarDecExpression copy() {
		return new VarDecExpression(Token.copy(type), params);
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		return handler.handleVarDec_E(this);
	}
	
}
