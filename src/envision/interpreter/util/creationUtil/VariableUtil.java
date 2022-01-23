package envision.interpreter.util.creationUtil;

import envision.lang.EnvisionObject;
import envision.lang.util.EnvisionDataType;
import envision.lang.variables.EnvisionBoolean;
import envision.lang.variables.EnvisionDouble;
import envision.lang.variables.EnvisionInt;
import envision.lang.variables.EnvisionNumber;
import envision.lang.variables.EnvisionString;
import eutil.datatypes.util.EDataType;
import eutil.math.NumberUtil;

public final class VariableUtil {
	
	public static String getTypeString(EnvisionObject varIn) {
		if (varIn == null) { return "null"; }
		//if the object is a class, return the name of the class as that is the object's type
		if (varIn.getInternalType() == EnvisionDataType.CLASS) { return varIn.getName(); }
		//otherwise, return the internal type's name
		return varIn.getInternalType().type;
	}
	
	public static Number incrementValue(EnvisionObject obj, boolean post) { return incrementValue(obj, 1, post);  }
	public static Number incrementValue(EnvisionObject obj, Number amount, boolean post) { return incDecVal(obj, amount, post); }
	public static Number decrementValue(EnvisionObject obj, boolean post) { return decrementValue(obj, -1, post); }
	public static Number decrementValue(EnvisionObject obj, Number amount, boolean post) { return incDecVal(obj, amount, post); }
	
	public static Number incDecVal(EnvisionObject obj, Number amount, boolean post) {
		if (isNumber(obj)) {
			EnvisionNumber num = (EnvisionNumber) obj;
			EnvisionDataType type = num.getInternalType();
			Number val = (Number) num.get();
			Number toSet = null;
			
			switch (type) {
			case INT: toSet = val.longValue() + amount.longValue(); break;
			case DOUBLE: toSet = val.doubleValue() + amount.doubleValue(); break;
			default:
			}
			
			num.set(toSet);
			return (post) ? val : toSet;
		}
		return null;
	}
	
	// I am unsure what point the following methods accomplish being here..
	
	//---------
	// Numbers
	//---------
	
	public static boolean isNumber(EnvisionObject obj) { return (obj instanceof EnvisionNumber); }
	public static boolean isNumber(String in) {
		if (in != null && !in.isEmpty()) {
			return NumberUtil.isNumber(in);
		}
		return false;
	}
	
	public static Number getNumber(EnvisionObject obj) {
		if (isNumber(obj)) {
			EnvisionNumber num = (EnvisionNumber) obj;
			return (Number) num.get();
		}
		return null;
	}
	
	public static EnvisionNumber setNumValue(EnvisionObject obj, Number value, EnvisionDataType type) {
		if (isNumber(obj)) {
			EnvisionNumber num = (EnvisionNumber) obj;
			num.set(value);
			return num;
		}
		return null;
	}
	
	//----------
	// Integers
	//----------
	
	public static boolean isInteger(EnvisionObject obj) { return (obj instanceof EnvisionInt); }
	public static boolean isInteger(String in) {
		if (in != null && !in.isEmpty()) {
			EDataType type = EDataType.getNumberType(in);
			return type == EDataType.INT || type == EDataType.LONG;
		}
		return false;
	}
	
	/** Returns the object form of Long in order to handle returning null. */
	public static Long getInteger(EnvisionObject obj) {
		if (isInteger(obj)) {
			EnvisionInt num = (EnvisionInt) obj;
			return (long) num.get();
		}
		return null;	}
	
	//---------
	// Doubles
	//---------
	
	public static boolean isDouble(EnvisionObject obj) { return (obj instanceof EnvisionDouble); }
	public static boolean isDouble(String in) {
		if (in != null && !in.isEmpty()) {
			EDataType type = EDataType.getNumberType(in);
			return type == EDataType.DOUBLE || type == EDataType.FLOAT;
		}
		return false;
	}
	
	/** Returns the object form of Double in order to handle returning null. */
	public static Double getDouble(EnvisionObject obj) {
		if (isDouble(obj)) {
			EnvisionDouble num = (EnvisionDouble) obj;
			return (double) num.get();
		}
		return null;
	}
	
	//----------
	// Booleans
	//----------
	
	public static boolean isBoolean(EnvisionObject obj) { return (obj instanceof EnvisionBoolean); }
	public static boolean isBoolean(String in) {
		if (in != null && !in.isEmpty()) {
			in = in.toLowerCase();
			return in.equals("false") || in.equals("true");
		}
		return false;
	}
	
	/** Returns the object form of Boolean in order to handle returning null. */
	public static Boolean getBoolean(EnvisionObject obj) {
		if (isBoolean(obj)) {
			EnvisionBoolean bool = (EnvisionBoolean) obj;
			return (boolean) bool.get();
		}
		return null;
	}
	
	//---------
	// Strings
	//---------
	
	public static boolean isString(EnvisionObject obj) { return (obj instanceof EnvisionString); }
	public static boolean isString(String in) {
		if (in != null && in.contains("\"")) {
			return in.chars().filter(c -> c == '"').count() % 2 == 0;
		}
		return false;
	}
	
	public static String getString(EnvisionObject obj) {
		if (isString(obj)) {
			EnvisionString str = (EnvisionString) obj;
			return (String) str.get();
		}
		return null;
	}
	
}
