package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;
import eutil.strings.EStringUtil;

/** Used to declare variables within expressions. */
public class Expr_VarDef implements Expression {

	public final Token<?> type;
	public final EList<Token<?>> params;
	public final Token<?> definingToken;
	
	public Expr_VarDef(Token<?> typeIn, EList<Token<?>> paramsIn) {
		type = typeIn;
		params = paramsIn;
		definingToken = typeIn;
	}
	
	@Override
	public String toString() {
		String p = (params != null) ? "<" + EStringUtil.toString(params, ", ") + ">": "";
		return type.getLexeme() + p;
	}
	
	@Override
	public Expr_VarDef copy() {
		return new Expr_VarDef(Token.copy(type), params);
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleVarDec_E(this);
	}
	
	@Override
	public Token<?> definingToken() {
		return definingToken;
	}
	
}
