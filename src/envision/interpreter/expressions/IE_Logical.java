package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.parser.expressions.types.LogicalExpression;

public class IE_Logical extends ExpressionExecutor<LogicalExpression> {

	public IE_Logical(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(LogicalExpression expression) {
		Object left = evaluate(expression.left);
		
		switch (expression.operator.keyword) {
		case AND: return (!isTruthy(left)) ? false : isTruthy(evaluate(expression.right));
		case OR: return (isTruthy(left)) ? true : isTruthy(evaluate(expression.right));
		case BITWISE_AND:
		case BITWISE_OR:
		case BITWISE_XOR:
		//case BITWISE_NOT:
		default: //ERROR
		}
		
		return null;
	}
	
	public static Object run(EnvisionInterpreter in, LogicalExpression e) {
		return new IE_Logical(in).run(e);
	}
}