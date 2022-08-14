package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.Token;
import eutil.datatypes.EList;
import eutil.strings.StringUtil;

public class Expr_Enum implements Expression {
	
	public final Token name;
	public final EList<Expression> args;
	
	public Expr_Enum(Token nameIn, EList<Expression> argsIn) {
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
