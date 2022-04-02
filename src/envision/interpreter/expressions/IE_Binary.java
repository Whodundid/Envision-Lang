package envision.interpreter.expressions;

import envision.exceptions.errors.ArithmeticError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.OperatorOverloadHandler;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.parser.expressions.expression_types.Expr_Binary;
import envision.tokenizer.KeywordType;
import envision.tokenizer.Operator;

public class IE_Binary extends ExpressionExecutor<Expr_Binary> {

	public IE_Binary(EnvisionInterpreter in) {
		super(in);
	}

	public static EnvisionObject run(EnvisionInterpreter in, Expr_Binary e) {
		return new IE_Binary(in).run(e);
	}
	
	@Override
	public EnvisionObject run(Expr_Binary expression) {
		EnvisionObject a = evaluate(expression.left);
		EnvisionObject b = evaluate(expression.right);
		
		//determine operation being made
		Operator op = expression.operator;
		/*
		if (expression.modular) {
			EnvisionObject opObj = scope().get(expression.operator.lexeme);
			if (opObj != null && opObj instanceof EnvisionOperator env_op) {
				op = ((EnvisionOperator.Operator) env_op.get()).op;
			}
			else throw new InvalidOperationError("Expected a valid modular operator but got: '" + opObj + "' instead!");
		}
		else {
			op = expression.operator.keyword;
		}
		*/
		
		//error if the operator is null
		if (op == null) throw new ArithmeticError("Null operator in arithmetic expression! '" + expression + "'!");
		
		//check if the operator is an assignment -- if so send to assign handler
		if (op.hasType(KeywordType.ASSIGNMENT)) return IE_Assign.handleAssign(interpreter, expression, op);
		
		//determine if the first object is a classInstance so that opeartor overloading can be handled
		if (a instanceof ClassInstance inst) {
			return OperatorOverloadHandler.handleOverload(interpreter, null, op, inst, b);
		}
		
		//otherwise, throw error
		throw new ArithmeticError("Cannot perform the operation '" + op + "' on the given objects! " + a + " : " + b);
	}
}
