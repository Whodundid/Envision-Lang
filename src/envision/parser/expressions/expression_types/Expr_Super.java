package envision.parser.expressions.expression_types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;
import eutil.strings.StringUtil;

public class Expr_Super implements Expression {
	
	public final Token target;
	public final EArrayList<Expression> args;
	
	public Expr_Super(Token methodIn) { this(methodIn, null); }
	public Expr_Super(Token methodIn, EArrayList<Expression> argsIn) {
		target = methodIn;
		args = argsIn;
	}

	@Override
	public String toString() {
		String a = (args != null) ? "(" + ((args.isNotEmpty()) ? StringUtil.toString(args, ", ") : "") + ")" : "";
		return "super." + target.lexeme + a;
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		return handler.handleSuper_E(this);
	}
	
}
