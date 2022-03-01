package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.parser.expressions.expressions.LogicalExpression;
import envision.tokenizer.Operator;

public class IE_Logical extends ExpressionExecutor<LogicalExpression> {

	public IE_Logical(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(LogicalExpression expression) {
		Object left = evaluate(expression.left);
		Operator op = expression.operator;
		
		switch (op) {
		case AND: return (!isTruthy(left)) ? false : isTruthy(evaluate(expression.right));
		case OR: return (isTruthy(left)) ? true : isTruthy(evaluate(expression.right));
		case BW_AND:
		case BW_OR:
		case BW_XOR:
		//case BITWISE_NOT:
		default: //ERROR
		}
		
		return null;
	}
	
	public static Object run(EnvisionInterpreter in, LogicalExpression e) {
		return new IE_Logical(in).run(e);
	}
}