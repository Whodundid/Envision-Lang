package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.Token;
import eutil.datatypes.EArrayList;
import eutil.strings.EStringUtil;

public class Expr_Enum implements Expression {
	
	public final Token name;
	public final EArrayList<Expression> args;
	public final Token definingToken;
	
	public Expr_Enum(Token nameIn, EArrayList<Expression> argsIn) {
		name = nameIn;
		args = argsIn;
		definingToken = nameIn;
	}
	
	@Override
	public String toString() {
		String a = (args != null && args.isNotEmpty()) ? "(" + EStringUtil.toString(args, ", ") + ")" : "";
		return name.lexeme + a;
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleEnum_E(this);
	}
	
	@Override
	public Token definingToken() {
		return definingToken;
	}
	
}
