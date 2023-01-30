package envision_lang.interpreter.util.creationUtil;

import envision_lang.exceptions.errors.ArithmeticError;
import envision_lang.exceptions.errors.NullVariableError;
import envision_lang.interpreter.util.scope.IScope;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionDouble;
import envision_lang.lang.datatypes.EnvisionDoubleClass;
import envision_lang.lang.datatypes.EnvisionInt;
import envision_lang.lang.datatypes.EnvisionIntClass;
import envision_lang.lang.datatypes.EnvisionNumber;
import envision_lang.lang.datatypes.EnvisionNumberClass;
import envision_lang.lang.natives.Primitives;

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
	
	//===================
	// Increment Numbers
	//===================
	
	public static EnvisionNumber increment(IScope scope, String varName, boolean post) {
		return increment(scope, varName, 1D, post);
	}
	public static EnvisionNumber increment(IScope scope, String varName, double amount, boolean post) {
		var obj = scope.get(varName);
		if (obj == null) throw new NullVariableError("Error! Cannot increment object, it is Java::NULL!");
		
		if (obj instanceof EnvisionInt i) return increment_l(scope, varName, i, (long) amount, post);
		if (obj instanceof EnvisionDouble d) return increment_d(scope, varName, d, amount, post);
		
		throw new ArithmeticError("Cannot increment the given object type: '" + obj.getDatatype() + "'!");
	}
	
	//===================
	// Decrement Numbers
	//===================
	
	public static EnvisionNumber decrement(IScope scope, String varName, boolean post) {
		return decrement(scope, varName, -1D, post);
	}
	public static EnvisionNumber decrement(IScope scope, String varName, double amount, boolean post) {
		var obj = scope.get(varName);
		if (obj == null) throw new NullVariableError("Error! Cannot decrement object, it is Java::NULL!");
		
		if (obj instanceof EnvisionInt i) return increment_l(scope, varName, i, (long) amount, post);
		if (obj instanceof EnvisionDouble d) return increment_d(scope, varName, d, amount, post);
		
		throw new ArithmeticError("Cannot decrement the given object type: '" + obj.getDatatype() + "'!");
	}
	
	//=======================================
	// Internal Increment/Decrement Handlers
	//=======================================
	
	static EnvisionInt increment_l(IScope scope, String varName, EnvisionInt theInt, long amount, boolean post) {
		var obj = scope.get(varName);
		if (obj == null) throw new NullVariableError("Error! Cannot increment object, it is Java::NULL!");
		
		if (!(obj instanceof EnvisionInt)) {
			throw new ArithmeticError("Invalid datatype! Expected an int but got a '" + obj.getDatatype() + "' instead!");
		}
		
		long toReturn = theInt.int_val;
		
		if (!post) toReturn += amount;
		// modify the var's value in scope
		//theInt.int_val = toReturn;
		//scope.set(varName, theDouble);
		
		return EnvisionIntClass.valueOf(toReturn);
	}
	
	static EnvisionDouble increment_d(IScope scope, String varName, EnvisionDouble theDouble, double amount, boolean post) {
		var obj = scope.get(varName);
		if (obj == null) throw new NullVariableError("Error! Cannot increment object, it is Java::NULL!");
		
		if (!(obj instanceof EnvisionDouble)) {
			throw new ArithmeticError("Invalid datatype! Expected a double but got a '" + obj.getDatatype() + "' instead!");
		}
		
		double toReturn = theDouble.double_val;
		
		if (!post) toReturn += amount;
		// modify the var's value in scope
		//theDouble.double_val = toReturn;
		//scope.set(varName, theDouble);
		
		return EnvisionDoubleClass.valueOf(toReturn);
	}
	
	//=================
	// Deprecated Code
	//=================
	
	@Deprecated
	public static EnvisionObject increment(EnvisionObject obj, boolean post) { return increment(obj, 1, post);  }
	@Deprecated
	public static EnvisionObject increment(EnvisionObject obj, Number amount, boolean post) { return incDecVal(obj, amount, post); }
	@Deprecated
	public static EnvisionObject decrement(EnvisionObject obj, boolean post) { return decrement(obj, -1, post); }
	@Deprecated
	public static EnvisionObject decrement(EnvisionObject obj, Number amount, boolean post) { return incDecVal(obj, amount, post); }
	
	@Deprecated
	public static EnvisionObject incDecVal(EnvisionObject obj, Number amount, boolean post) {
		if (!(obj instanceof EnvisionNumber)) {
			throw new ArithmeticError("Cannot increment/decrement the given object: '" + obj + "'!");
		}
			
		EnvisionNumber num = (EnvisionNumber) obj;
		
		Primitives type = obj.getPrimitiveType();
		Number val = (Number) num.get_i();
		Number toSet = null;
		
		switch (type) {
		case INT: toSet = val.longValue() + amount.longValue(); break;
		case DOUBLE: toSet = val.doubleValue() + amount.doubleValue(); break;
		default:
			throw new ArithmeticError("Invalid datatype! Can only increment/decrement " +
									  "an int or a double! " + type);
		}
		
		//num.set_i(toSet);
		Number toReturn = (post) ? val : toSet;
		return EnvisionNumberClass.newNumber(toReturn);
	}
	
}
