package envision_lang.interpreter.util.creationUtil;

import envision_lang.exceptions.errors.ArithmeticError;
import envision_lang.exceptions.errors.NullVariableError;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionDouble;
import envision_lang.lang.datatypes.EnvisionDoubleClass;
import envision_lang.lang.datatypes.EnvisionInt;
import envision_lang.lang.datatypes.EnvisionIntClass;
import envision_lang.lang.datatypes.EnvisionNumber;

public final class NumberHelper {
	
	/**
	 * Performs number negation on the given object.
	 * Note: if the object is not a number, an Arithmetic error is thrown instead.
	 * 
	 * @param obj The number object to negate
	 * @return The negated object
	 */
	public static EnvisionObject negate(EnvisionObject obj) {
		if (obj instanceof EnvisionNumber env_num) return env_num.negate();
		throw new ArithmeticError("Cannot negate the given object: '" + obj + "'!");
	}
	
	public static EnvisionInt increment_l(EnvisionInt obj, boolean post) { return increment_l(obj, 1l, post); }
	public static EnvisionInt increment_l(EnvisionInt obj, long amount, boolean post) {
		if (obj == null) throw new NullVariableError("Error! Cannot increment object, it is NULL!");
		
		long toReturn = obj.int_val;
		
		if (!post) toReturn += amount;
		obj.int_val = toReturn;
		
		EnvisionIntClass.newInt(toReturn);
		
		return obj;
	}
	
	public static EnvisionDouble increment_d(EnvisionDouble obj, boolean post) { return increment_d(obj, 1l, post); }
	public static EnvisionDouble increment_d(EnvisionDouble obj, double amount, boolean post) {
		if (obj == null) throw new NullVariableError("Error! Cannot increment object, it is NULL!");
		
		double toReturn = obj.double_val;
		
		if (!post) toReturn += amount;
		obj.double_val = toReturn;
		
		EnvisionDoubleClass.newDouble(toReturn);
		
		return obj;
	}
	
	public static EnvisionObject increment(EnvisionObject obj, boolean post) { return increment(obj, 1, post);  }
	public static EnvisionObject increment(EnvisionObject obj, Number amount, boolean post) { return incDecVal(obj, amount, post); }
	public static EnvisionObject decrement(EnvisionObject obj, boolean post) { return decrement(obj, -1, post); }
	public static EnvisionObject decrement(EnvisionObject obj, Number amount, boolean post) { return incDecVal(obj, amount, post); }
	
	public static EnvisionObject incDecVal(EnvisionObject obj, Number amount, boolean post) {
		if (!(obj instanceof EnvisionNumber)) {
			throw new ArithmeticError("Cannot increment/decrement the given object: '" + obj + "'!");
		}
		
		if (obj instanceof EnvisionInt i) return increment_l(i, amount.longValue(), post);
		else return increment_d((EnvisionDouble) obj, amount.doubleValue(), post);
			
//			Primitives type = num.getPrimitiveType();
//			Number val = (Number) num.get_i();
//			Number toSet = null;
//			
//			switch (type) {
//			case INT: toSet = val.longValue() + amount.longValue(); break;
//			case DOUBLE: toSet = val.doubleValue() + amount.doubleValue(); break;
//			default:
//				throw new ArithmeticError("Invalid datatype! Can only increment/decrement " +
//										  "an int or a double! " + type);
//			}
//			
//			num.set_i(toSet);
//			Number toReturn = (post) ? val : toSet;
//			return EnvisionNumberClass.newNumber(toReturn);
		
		
	}
	
}
