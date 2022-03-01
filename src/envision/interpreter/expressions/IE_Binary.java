package envision.interpreter.expressions;

import envision.exceptions.errors.ArithmeticError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.OperatorOverloadHandler;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.objects.EnvisionList;
import envision.parser.expressions.expressions.BinaryExpression;
import envision.tokenizer.KeywordType;
import envision.tokenizer.Operator;
import eutil.math.NumberUtil;
import eutil.strings.StringUtil;

public class IE_Binary extends ExpressionExecutor<BinaryExpression> {

	public IE_Binary(EnvisionInterpreter in) {
		super(in);
	}

	public static Object run(EnvisionInterpreter in, BinaryExpression e) {
		return new IE_Binary(in).run(e);
	}
	
	@Override
	public Object run(BinaryExpression expression) {
		Object a = evaluate(expression.left);
		Object b = evaluate(expression.right);
		
		//convert wrapped objects to their primitive forms
		a = EnvisionObject.convert(a);
		b = EnvisionObject.convert(b);
		
		//TEMPORARY FIX -- SHOULD NOT BE PERMANENT
		//check if either vlaue is a String -- if so remove "
		//a = (a instanceof String s) ? s.replace("\"", "") : a;
		//b = (b instanceof String s) ? s.replace("\"", "") : b;
		
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
			return OperatorOverloadHandler.handleOverload(interpreter, op, inst, b);
		}
		
		//otherwise, handle the basic datatype interactions
		if (op == Operator.COMPARE) return isEqual(a, b);
		else if (op == Operator.NOT_EQUALS) return !isEqual(a, b);
		else if (op == Operator.ADD) {
			//check for string concatenations
			if (a instanceof String a_str && b instanceof String b_str) return a_str + b_str;
			else if (a instanceof String a_str) return a_str + b;
			else if (a instanceof String b_str) return a + b_str;
			else if (a instanceof Number a_num && b instanceof Number b_num) {
				//check if operation should be integer or double based
				if (isIntegers(a, b)) return a_num.longValue() + b_num.longValue();
				else return a_num.doubleValue() + b_num.doubleValue();
			}
			
			throw new ArithmeticError("'" + expression + "' : Operands must be two numbers or two strings.");
		}
		
		//handle special string and char multiply cases
		else if (op == Operator.MUL) {
			//handle (char * #) case
			if (a instanceof Character l) return StringUtil.repeatString(String.valueOf(l), ((Number) b).intValue());
			//handle (string * #) case
			else if (a instanceof String l) return StringUtil.repeatString(l, ((Number) b).intValue());
		}
		
		//flag that is true if the resulting output will be a number
		//boolean isNum = false;
		
		//switch on op to determine what kind of operation to perform
		switch (op) {
		case LT:
		case GT:
		case LTE:
		case GTE:
			//convert lists to their length
			if (a instanceof EnvisionList env_list) a = env_list.size();
			if (b instanceof EnvisionList env_list) b = env_list.size();
			//isNum = true;
			break;
		case SUB:
		case MUL:
		case DIV:
		case MOD:
		case SHL:
		case SHR:
		case SHR_AR:
			if ((isList(a) && isNumber(b)) || isNumber(a) && isList(b)) {
				//isNum = true;
				break;
			}
			checkNumberOperands(expression.operator, a, b);
			//isNum = true;
			break;
		default: break;
		}
		
		//if (isNum) {
			if (isList(a) && isNumber(b)) return handleList(op, (EnvisionList) a, (Number) b, true);
			if (isNumber(a) && isList(b)) return handleList(op, (EnvisionList) b, (Number) a, false);
			else if (isIntegers(a, b)) return handleOpLong(op, ((Number) a).longValue(), ((Number) b).longValue());
			else return handleOpDouble(op, ((Number) a).doubleValue(), ((Number) b).doubleValue());
		//}
		
		//return null;
	}
	
	private static EnvisionList handleList(Operator op, EnvisionList list, Number b, boolean isLeft) {
		for (int i = 0; i < list.size(); i++) {
			var o = EnvisionObject.convert(list.get(i));
			
			if (o instanceof Number a) {
				if (isIntegers(a, b)) {
					var left = (isLeft) ? a.longValue() : b.longValue();
					var right = (isLeft) ? b.longValue() : a.longValue();
					var new_val = handleOpLong(op, left, right);
					list.set(i, new_val);
				}
				else {
					var left = (isLeft) ? a.doubleValue() : b.doubleValue();
					var right = (isLeft) ? b.doubleValue() : a.doubleValue();
					var new_val = handleOpDouble(op, left, right);
					list.set(i, new_val);
				}
			}
			else throw new ArithmeticError("("+list.getDatatype()+" "+op+" "+b+") : Both operands must be numbers.");
		}
		
		return list;
	}
	
	private static Object handleOpLong(Operator op, long a, long b) {
		//if divide, check for divide / zero possibility
		if (op == Operator.DIV) div0(op, a, b);
		
		switch (op) {
		case LT: 		return a < b;
		case LTE: 		return a <= b;
		case GT: 		return a > b;
		case GTE: 		return a >= b;
		case SUB: 		return a - b;
		case MUL: 		return a * b;
		case DIV:  		return a / b;
		case MOD: 		return a % b;
		case SHL: 		return a << b;
		case SHR: 		return a >> b;
		case SHR_AR: 	return a >>> b;
		default: 		return null;
		}
	}
	
	private static Object handleOpDouble(Operator op, double a, double b) {
		//if divide, check for divide / zero possibility
		if (op == Operator.DIV) div0(op, a, b);
		
		switch (op) {
		case LT: 		return a < b;
		case LTE: 		return a <= b;
		case GT: 		return a > b;
		case GTE: 		return a >= b;
		case SUB: 		return a - b;
		case MUL: 		return a * b;
		case DIV:  		return a / b;
		case MOD: 		return a % b;
		case SHL: 		return (long) a << (long) b;
		case SHR: 		return (long) a >> (long) b;
		case SHR_AR: 	return (long) a >>> (long) b;
		default: 		return null;
		}
	}
	
	/** Throws / by zero error. */
	private static void div0(Operator op, Number a, Number b) {
		if (b.intValue() == 0) {
			throw new ArithmeticError("("+a+" "+op.chars+" "+b+") error! Division by zero!");
		}
	}
	
	private boolean isList(Object a) { return a instanceof EnvisionList; }
	private boolean isNumber(Object a) { return a instanceof Number; }
	private boolean isString(Object a) { return a instanceof String; }
	
	/**
	 * Returns true if both 'a' and 'b' are both Numbers.
	 * 
	 * @param a
	 * @param b
	 * @return true if both are numbers
	 */
	private boolean bothNumbers(Object a, Object b) {
		return a instanceof Number && b instanceof Number;
	}
	
	/**
	 * Returns true if both 'a' and 'b' are both strings.
	 * 
	 * @param a
	 * @param b
	 * @return true if both are strings
	 */
	private boolean bothStrings(Object a, Object b) {
		return a instanceof String && b instanceof String;
	}
	
	/**
	 * Returns true if both 'a' and 'b' are both integers (or longs).
	 * 
	 * @param a
	 * @param b
	 * @return true if both are ints or longs
	 */
	private static boolean isIntegers(Object a, Object b) {
		return NumberUtil.isInteger(a) && NumberUtil.isInteger(b);
	}
	
}
