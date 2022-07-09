package envision.interpreter.util.creationUtil;

import envision.exceptions.errors.ArithmeticError;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionNumber;
import envision.lang.datatypes.EnvisionNumberClass;
import envision.lang.natives.Primitives;

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
	
	public static EnvisionObject increment(EnvisionObject obj, boolean post) { return increment(obj, 1, post);  }
	public static EnvisionObject increment(EnvisionObject obj, Number amount, boolean post) { return incDecVal(obj, amount, post); }
	public static EnvisionObject decrement(EnvisionObject obj, boolean post) { return decrement(obj, -1, post); }
	public static EnvisionObject decrement(EnvisionObject obj, Number amount, boolean post) { return incDecVal(obj, amount, post); }
	
	public static EnvisionObject incDecVal(EnvisionObject obj, Number amount, boolean post) {
		if (obj instanceof EnvisionNumber num) {
			Primitives type = num.getPrimitiveType();
			Number val = (Number) num.get_i();
			Number toSet = null;
			
			switch (type) {
			case INT: toSet = val.longValue() + amount.longValue(); break;
			case DOUBLE: toSet = val.doubleValue() + amount.doubleValue(); break;
			default:
				throw new ArithmeticError("Invalid datatype! Can only increment/decrement " +
										  "an int or a double! " + type);
			}
			
			num.set_i(toSet);
			Number toReturn = (post) ? val : toSet;
			return EnvisionNumberClass.newNumber(toReturn);
		}
		
		throw new ArithmeticError("Cannot increment/decrement the given object: '" + obj + "'!");
	}
	
}
