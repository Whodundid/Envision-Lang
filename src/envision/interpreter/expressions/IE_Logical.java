package envision.interpreter.expressions;

import envision.exceptions.errors.InvalidDatatypeError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.OperatorOverloadHandler;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.parser.expressions.expression_types.Expr_Logic;
import envision.tokenizer.Operator;

public class IE_Logical extends ExpressionExecutor<Expr_Logic> {

	public IE_Logical(EnvisionInterpreter in) {
		super(in);
	}
	
	public static EnvisionObject run(EnvisionInterpreter in, Expr_Logic e) {
		return new IE_Logical(in).run(e);
	}

	@Override
	public EnvisionObject run(Expr_Logic expression) {
		EnvisionObject a = evaluate(expression.left);
		EnvisionObject b = evaluate(expression.right);
		Operator op = expression.operator;
		
		//if a is a class instance, attempt to evaluate operator overloads
		if (a instanceof ClassInstance a_inst && a_inst.supportsOperator(op))
			return OperatorOverloadHandler.handleOverload(interpreter, null, op, a_inst, b);
		
		throw new InvalidDatatypeError("The given objects '" + a + "' and '" + b + "' cannot be logically evaluated!");
	}
	
}