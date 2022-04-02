package envision.parser.expressions.expression_types;

import envision.lang.EnvisionObject;
import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;
import eutil.strings.StringUtil;

public class Expr_Enum implements Expression {
	
	public final Token name;
	public final EArrayList<Expression> args;
	
	public Expr_Enum(Token nameIn, EArrayList<Expression> argsIn) {
		name = nameIn;
		args = argsIn;
	}
	
	@Override
	public String toString() {
		String a = (args != null && args.isNotEmpty()) ? "(" + StringUtil.toString(args, ", ") + ")" : "";
		return name.lexeme + a;
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleEnum_E(this);
	}
	
}
