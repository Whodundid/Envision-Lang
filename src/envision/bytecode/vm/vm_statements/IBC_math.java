package envision.bytecode.vm.vm_statements;

import envision.bytecode.ByteCode;
import envision.bytecode.util.BC_ArgParser;
import envision.bytecode.vm.EnvisionVM;
import envision.exceptions.EnvisionError;
import envision.exceptions.errors.ArithmeticError;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionNumber;
import envision.lang.objects.EnvisionList;
import eutil.math.NumberUtil;

public class IBC_math {
	
	public static void math(EnvisionVM vm, ByteCode code, String[] args) {
		switch (code) {
		case ADD:
		case SUB:
		case MUL:
		case DIV: math0(vm, code, args); break;
		default: throw new EnvisionError("Invalid Bytecode statement! " + code);
		}
	}
	
	private static void math0(EnvisionVM vm, ByteCode code, String[] args) {
		BC_ArgParser.expect(args, 0);
		
		EnvisionObject a = vm.stack().pop();
		EnvisionObject b = vm.stack().pop();
		
		if (a instanceof EnvisionNumber && b instanceof EnvisionNumber) {
			Object left = EnvisionObject.convert(a);
			Object right = EnvisionObject.convert(b);
			
			boolean isInt = bothIntegers(left, right);
			Number result = null;
			
			switch (code) {
			case ADD:
				if (isInt) result = ((Number) left).longValue() + ((Number) right).longValue();
				else result = ((Number) left).doubleValue() + ((Number) right).doubleValue();
				break;
			case SUB:
				if (isInt) result = ((Number) left).longValue() - ((Number) right).longValue();
				else result = ((Number) left).doubleValue() - ((Number) right).doubleValue();
				break;
			case MUL:
				if (isInt) result = ((Number) left).longValue() * ((Number) right).longValue();
				else result = ((Number) left).doubleValue() * ((Number) right).doubleValue();
				break;
			case DIV:
				div0((Number) left, (Number) right);
				if (isInt) result = ((Number) left).longValue() / ((Number) right).longValue();
				else result = ((Number) left).doubleValue() / ((Number) right).doubleValue();
				break;
			default: break;
			}
			
			vm.stack().push(EnvisionNumber.of(result));
		}
	}
	
	/** Throws / by zero error. */
	private static void div0(Number left, Number right) {
		if (right.intValue() == 0) throw new ArithmeticError("(" + left + " / 0) error! Division by zero!");
	}
	
	private static boolean isList(Object a) { return a instanceof EnvisionList; }
	private static boolean isNumber(Object a) { return a instanceof Number; }
	private static boolean isString(Object a) { return a instanceof String; }
	private static boolean bothNumbers(Object a, Object b) { return a instanceof Number && b instanceof Number; }
	private static boolean bothStrings(Object a, Object b) { return a instanceof String && b instanceof String; }
	
	private static boolean bothIntegers(Object a, Object b) {
		if (a instanceof Number && b instanceof Number) {
			return NumberUtil.isInteger((Number) a) && NumberUtil.isInteger((Number) b);
		}
		return false;
	}
	
}
