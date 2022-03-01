package envision.interpreter.expressions;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.InvalidOperationError;
import envision.exceptions.errors.InvalidTargetError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.OperatorOverloadHandler;
import envision.interpreter.util.creationUtil.VariableUtil;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.datatypes.EnvisionVariable;
import envision.lang.objects.EnvisionList;
import envision.parser.expressions.Expression;
import envision.parser.expressions.expressions.CompoundExpression;
import envision.parser.expressions.expressions.UnaryExpression;
import envision.parser.expressions.expressions.VarExpression;
import envision.tokenizer.Operator;
import eutil.datatypes.EArrayList;
import eutil.math.NumberUtil;

public class IE_Unary extends ExpressionExecutor<UnaryExpression> {

	public IE_Unary(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(UnaryExpression expression) {
		Operator op = expression.operator;
		Expression left = expression.left;
		Expression right = expression.right;
		
		//handle left hand operator (!x)
		if (left == null) {
			Object r = EnvisionObject.convert(evaluate(right));
			
			//check for class level operator overloading
			if (r instanceof ClassInstance) return OperatorOverloadHandler.handleOverload(interpreter, op, (ClassInstance) r, null);
			
			//logical or arithmetic negation
			if (op == Operator.NEGATE) {
				return !isTruthy(evaluate(right));
			}
			//operations on numbers
			else {
				checkNumberOperand(expression.operator, r);
				
				if (op == Operator.SUB) {
					if (right instanceof CompoundExpression) throw new InvalidTargetError("Cannot apply a negation across multiple values!");
					Object o = (isInteger(r)) ? -((Number) r).longValue() : -((Number) r).doubleValue();
					return o;
				}
				
				//check for pre increment/decrement (++x)
				if (right instanceof VarExpression) {
					VarExpression var = (VarExpression) right;
					//String name = var.name.lexeme;
					EnvisionObject obj = interpreter.getIfDefined(var.getName());
					
					if (op == Operator.INC) return VariableUtil.incrementValue(obj, false);
					else if (op == Operator.DEC) return VariableUtil.decrementValue(obj, false);
				}
			}
		}
		//check for post increment/decrement (x++)
		else if (left instanceof VarExpression) {
			Object l = EnvisionVariable.convert(evaluate(left));
			
			//check for class level operator overloading
			if (l instanceof ClassInstance class_inst)
				return OperatorOverloadHandler.handleOverload(interpreter,
															  Operator.makePost(op),
															  class_inst,
															  null);
			
			checkNumberOperand(expression.operator, l);
			VarExpression var = (VarExpression) left;
			//String name = var.name.lexeme;
			EnvisionObject obj = interpreter.getIfDefined(var.getName());
				
			if (op == Operator.INC) return VariableUtil.incrementValue(obj, true);
			else if (op == Operator.DEC) return VariableUtil.decrementValue(obj, true);
		}
		else if (left instanceof CompoundExpression) {
			CompoundExpression ce = (CompoundExpression) left;
			EArrayList<Expression> expressions = ce.expressions;
			EnvisionList l = new EnvisionList();
			
			for (Expression e : expressions) {
				if (e == null) throw new EnvisionError("The expression is null! This really shouldn't be possible!");
				if (!(e instanceof VarExpression)) throw new InvalidTargetError("Expected a Variable Expression but got a " + e.getClass().getSimpleName() + "!");
				l.add(handleExpression(e, expression.operator));
			}
			
			return l;
		}
		
		return null;
	}
	
	private Object handleExpression(Expression e, Operator operator) {
		Object o = EnvisionObject.convert(evaluate(e));
		
		//check for class level operator overloading
		if (o instanceof ClassInstance) return OperatorOverloadHandler.handleOverload(interpreter, operator, (ClassInstance) o, null);
		
		checkNumberOperand(operator, o);
		VarExpression var = (VarExpression) e;
		EnvisionObject obj = interpreter.getIfDefined(var.getName());
		
		if (operator == Operator.INC) return VariableUtil.incrementValue(obj, true);
		else if (operator == Operator.DEC) return VariableUtil.decrementValue(obj, true);
		
		throw new InvalidOperationError("The operator: '" + operator + "' is invalid for unary expressions!");
	}
	
	private boolean isInteger(Object a) {
		return (a instanceof Number) ? NumberUtil.isInteger((Number) a) : false;
	}
	
	public static Object run(EnvisionInterpreter in, UnaryExpression e) {
		return new IE_Unary(in).run(e);
	}
	
}