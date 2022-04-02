package envision.interpreter.expressions;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.InvalidTargetError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.OperatorOverloadHandler;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.datatypes.EnvisionList;
import envision.lang.datatypes.EnvisionListClass;
import envision.parser.expressions.Expression;
import envision.parser.expressions.expression_types.Expr_Compound;
import envision.parser.expressions.expression_types.Expr_Unary;
import envision.parser.expressions.expression_types.Expr_Var;
import envision.tokenizer.Operator;
import eutil.datatypes.EArrayList;

public class IE_Unary extends ExpressionExecutor<Expr_Unary> {

	public IE_Unary(EnvisionInterpreter in) {
		super(in);
	}

	public static EnvisionObject run(EnvisionInterpreter in, Expr_Unary e) {
		return new IE_Unary(in).run(e);
	}
	
	@Override
	public EnvisionObject run(Expr_Unary expression) {
		Operator op = expression.operator;
		Expression left = expression.left;
		Expression right = expression.right;
		
		//handle left hand operator (!x)
		if (left == null) {
			//check for class level operator overloading
			if (evaluate(right) instanceof ClassInstance inst) {
				return OperatorOverloadHandler.handleOverload(interpreter, null, op, inst, null);
			}
		}
		//check for post increment/decrement (x++)
		else if (left instanceof Expr_Var var) {
			String scopeName = var.getName();
			EnvisionObject l = evaluate(var);
			
			//check for class level operator overloading
			if (l instanceof ClassInstance class_inst) {
				EnvisionObject obj = OperatorOverloadHandler.handleOverload(interpreter,
															  scopeName,
															  Operator.makePost(op),
															  class_inst,
															  null);
				return obj;
			}
		}
		else if (left instanceof Expr_Compound ce) {
			EArrayList<Expression> expressions = ce.expressions;
			EnvisionList l = EnvisionListClass.newList();
			
			for (Expression e : expressions) {
				if (e == null) throw new EnvisionError("The expression is null! This really shouldn't be possible!");
				if (!(e instanceof Expr_Var)) throw new InvalidTargetError("Expected a Variable Expression but got a " + e.getClass().getSimpleName() + "!");
				EnvisionObject result = evaluate(e);
				l.add(result);
			}
			
			return l;
		}
		
		throw new InvalidTargetError("Cannot perform the given unary operator '" + op + "' on the given objects!");
	}
	
}