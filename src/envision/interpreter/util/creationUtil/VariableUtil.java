package envision.interpreter.util.creationUtil;

import envision.exceptions.errors.ArithmeticError;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionNumber;
import envision.lang.util.Primitives;

public final class VariableUtil {
	
	public static String getTypeString(EnvisionObject varIn) {
		if (varIn == null) return "null";
		//if the object is a class, return the name of the class as that is the object's type
		if (varIn.getDatatype().isClass()) return varIn.getTypeString();
		//otherwise, return the internal type's name
		return varIn.getDatatype().getType();
	}
	
	public static Number incrementValue(EnvisionObject obj, boolean post) { return incrementValue(obj, 1, post);  }
	public static Number incrementValue(EnvisionObject obj, Number amount, boolean post) { return incDecVal(obj, amount, post); }
	public static Number decrementValue(EnvisionObject obj, boolean post) { return decrementValue(obj, -1, post); }
	public static Number decrementValue(EnvisionObject obj, Number amount, boolean post) { return incDecVal(obj, amount, post); }
	
	public static Number incDecVal(EnvisionObject obj, Number amount, boolean post) {
		if (obj instanceof EnvisionNumber num) {
			Primitives type = num.getPrimitiveType();
			Number val = (Number) num.get();
			Number toSet = null;
			
			switch (type) {
			case INT: toSet = val.longValue() + amount.longValue(); break;
			case DOUBLE: toSet = val.doubleValue() + amount.doubleValue(); break;
			default: throw new ArithmeticError("Invalid datatype! Can only be an int or a double! " + type);
			}
			
			num.set(toSet);
			return (post) ? val : toSet;
		}
		return null;
	}
	
}
