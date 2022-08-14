package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.Token;
import eutil.datatypes.EList;
import eutil.strings.StringUtil;

public class Expr_Super implements Expression {
	
	public final Token target;
	public final EList<Expression> args;
	
	public Expr_Super(Token methodIn) { this(methodIn, null); }
	public Expr_Super(Token methodIn, EList<Expression> argsIn) {
		target = methodIn;
		args = argsIn;
	}

	@Override
	public String toString() {
		String a = (args != null) ? "(" + ((args.isNotEmpty()) ? StringUtil.toString(args, ", ") : "") + ")" : "";
		return "super." + target.lexeme + a;
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleSuper_E(this);
	}
	
}
