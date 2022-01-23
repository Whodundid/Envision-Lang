package envision.parser.expressions.types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import eutil.datatypes.EArrayList;

public class ThisConExpression implements Expression {

	public final EArrayList<Expression> args;
	
	public ThisConExpression(EArrayList<Expression> argsIn) {
		args = argsIn;
	}
	
	@Override
	public String toString() {
		String a = "";
		for (Expression e : args) {
			a += e + ", ";
		}
		a = (args.isNotEmpty()) ? a.substring(0, a.length() - 2) : a;
		return "this(" + a + ")";
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		return handler.handleThisCon_E(this);
	}
	
}
