package envision.interpreter.expressions;

import envision.exceptions.errors.ArithmeticError;
import envision.exceptions.errors.InvalidOperationError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.OperatorOverloadHandler;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.objects.EnvisionList;
import envision.lang.objects.EnvisionOperator;
import envision.parser.expressions.types.BinaryExpression;
import envision.tokenizer.Keyword;
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
		Object left = evaluate(expression.left);
		Object right = evaluate(expression.right);
		
		//check if either value is a variable -- if so compare the variable's value
		left = EnvisionObject.convert(left);
		right = EnvisionObject.convert(right);
		
		//TEMPORARY FIX -- SHOULD NOT BE PERMANENT
		//check if either vlaue is a String -- if so remove "
		left = (left instanceof String) ? ((String) left).replace("\"", "") : left;
		right = (right instanceof String) ? ((String) right).replace("\"", "") : right;
		
		//determine operation being made
		Keyword op = null;
		if (expression.modular) {
			EnvisionObject opObj = scope().get(expression.operator.lexeme);
			if (opObj != null && opObj instanceof EnvisionOperator) {
				op = (Keyword) ((EnvisionOperator.Operator) ((EnvisionOperator) opObj).get()).op;
			}
			else throw new InvalidOperationError("Expected a valid modular operator but got: '" + opObj + "' instead!");
		}
		else {
			op = expression.operator.keyword;
		}
		
		//error if the operator is null
		if (op == null) throw new ArithmeticError("Null operator in arithmetic expression! '" + expression + "'!");
		
		//check if the operator is an assignment -- if so send to assign handler
		if (op.isAssignment()) return IE_Assign.handleAssign(interpreter, expression, op);
		
		//System.out.println(expression + " : [" + left + " " + left.getClass() + "] " + op + " [" + right + " " + right.getClass() + "]");
		
		//determine if the first object is a classInstance so that opeartor overloading can be handled
		if (left instanceof ClassInstance) {
			return OperatorOverloadHandler.handleOverload(interpreter, op, (ClassInstance) left, right);
		}
		else if (right instanceof ClassInstance) {
			return OperatorOverloadHandler.handleOverload(interpreter, op, (ClassInstance) right, left);
		}
		
		//otherwise, handle the basic datatype interactions
		if (op == Keyword.COMPARE) { return isEqual(left, right); }
		if (op == Keyword.NOT_EQUALS) { return !isEqual(left, right); }
		if (op == Keyword.ADD) {
			if (bothStrings(left, right)) { return (String) left + (String) right; }
			if (bothNumbers(left, right)) {
				if (isIntegers(left, right)) { return ((Number) left).longValue() + ((Number) right).longValue(); }
				else { return ((Number) left).doubleValue() + ((Number) right).doubleValue(); }
			}
			if (isString(left)) { return (String) left + right; }
			if (isString(right)) { return left + (String) right; }
			throw new ArithmeticError("'" + expression + "' : Operands must be two numbers or two strings.");
		}
		
		boolean isNum = false;
		boolean strFirst = (left instanceof String);
		
		if (strFirst && op == Keyword.MULTIPLY) {
			String l = (String) left;
			return StringUtil.repeatString(l, ((Number) right).intValue());
		}
		
		switch (op) {
		case LESS_THAN: case LESS_THAN_EQUALS:
		case GREATER_THAN: case GREATER_THAN_EQUALS:
			//convert lists to their length
			if (isList(left)) left = ((EnvisionList) left).size();
			if (isList(right)) right = ((EnvisionList) right).size();
		case SUBTRACT: case MULTIPLY: case DIVIDE: case MODULUS:
		case SHIFT_LEFT: case SHIFT_RIGHT: case SHIFT_RIGHT_ARITHMETIC:
			if ((isList(left) && isNumber(right)) || isNumber(left) && isList(right)) { isNum = true; break; }
			checkNumberOperands(expression.operator, left, right);
			isNum = true;
		default: break;
		}
		
		if (isNum) {
			if (isList(left) && isNumber(right)) return handleList(op, (EnvisionList) left, (Number) right, true);
			if (isNumber(left) && isList(right)) return handleList(op, (EnvisionList) right, (Number) left, false);
			else if (isIntegers(left, right)) return handleOpInt(op, (Number) left, (Number) right);
			else return handleOpDouble(op, (Number) left, (Number) right);
		}
		
		return null;
	}
	
	private static EnvisionList handleList(Keyword op, EnvisionList list, Number right, boolean isLeft) {
		for (int i = 0; i < list.size(); i++) {
			Object o = EnvisionObject.convert(list.get(i));
			if (o instanceof Number) {
				Number left = (Number) o;
				if (isIntegers(left, right)) list.set(i, handleOpInt(op, (isLeft) ? left : right, (isLeft) ? right : left));
				else list.set(i, handleOpDouble(op, (isLeft) ? left : right, (isLeft) ? right : left));
			}
			else throw new ArithmeticError("(" + list.getInternalType() + " " + op + " " + right + ") : Both operands must be numbers.");
		}
		
		return list;
	}
	
	private static Object handleOpInt(Keyword op, Number left, Number right) {
		long lVal = left.longValue();
		long rVal = right.longValue();
		
		switch (op) {
		case LESS_THAN: return lVal < rVal;
		case LESS_THAN_EQUALS: return lVal <= rVal;
		case GREATER_THAN: return lVal > rVal;
		case GREATER_THAN_EQUALS: return lVal >= rVal;
		case SUBTRACT: return lVal - rVal;
		case MULTIPLY: return lVal * rVal;
		case DIVIDE: div0(op, left, right); return lVal / rVal;
		case MODULUS: return lVal % rVal;
		case SHIFT_LEFT: return (int) lVal << (int) rVal;
		case SHIFT_RIGHT: return (int) lVal >> (int) rVal;
		case SHIFT_RIGHT_ARITHMETIC: return (int) lVal >>> (int) rVal;
		default: return null;
		}
	}
	
	private static Object handleOpDouble(Keyword op, Number left, Number right) {
		double lVal = left.doubleValue();
		double rVal = right.doubleValue();
		
		switch (op) {
		case LESS_THAN: return lVal < rVal;
		case LESS_THAN_EQUALS: return lVal <= rVal;
		case GREATER_THAN: return lVal > rVal;
		case GREATER_THAN_EQUALS: return lVal >= rVal;
		case SUBTRACT: return lVal - rVal;
		case MULTIPLY: return lVal * rVal;
		case DIVIDE: div0(op, left, right); return lVal / rVal;
		case MODULUS: return lVal % rVal;
		case SHIFT_LEFT: return (int) lVal << (int) rVal;
		case SHIFT_RIGHT: return (int) lVal >> (int) rVal;
		case SHIFT_RIGHT_ARITHMETIC: return (int) lVal >>> (int) rVal;
		default: return null;
		}
	}
	
	/** Throws / by zero error. */
	private static void div0(Keyword op, Number left, Number right) {
		if (right.intValue() == 0) throw new ArithmeticError("(" + left + " " + op.chars + " " + right + ") error! Division by zero!");
	}
	
	private boolean isList(Object a) { return a instanceof EnvisionList; }
	private boolean isNumber(Object a) { return a instanceof Number; }
	private boolean isString(Object a) { return a instanceof String; }
	private boolean bothNumbers(Object left, Object right) { return left instanceof Number && right instanceof Number; }
	private boolean bothStrings(Object left, Object right) { return left instanceof String && right instanceof String; }
	
	private static boolean isIntegers(Object a, Object b) {
		if (a instanceof Number && b instanceof Number) {
			return NumberUtil.isInteger((Number) a) && NumberUtil.isInteger((Number) b);
		}
		return false;
	}
	
}
