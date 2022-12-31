package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;
import eutil.strings.EStringUtil;

public class Expr_Super implements Expression {
	
	public final Token<?> target;
	public final EList<Expression> args;
	public final Token<?> definingToken;
	
	public Expr_Super(Token start, Token<?> methodIn) { this(start, methodIn, null); }
	public Expr_Super(Token start, Token<?> methodIn, EList<Expression> argsIn) {
		target = methodIn;
		args = argsIn;
		definingToken = start;
	}

	@Override
	public String toString() {
		String a = (args != null) ? "(" + ((args.isNotEmpty()) ? EStringUtil.toString(args, ", ") : "") + ")" : "";
		return "super." + target.getLexeme() + a;
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleSuper_E(this);
	}
	
	@Override
	public Token<?> definingToken() {
		return definingToken;
	}
	
}
