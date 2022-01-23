package envision.interpreter.util.creationUtil;

import static envision.lang.util.EnvisionDataType.*;

import envision.exceptions.errors.InvalidDataTypeError;
import envision.exceptions.errors.VariableCastError;
import envision.lang.EnvisionObject;
import envision.lang.util.EnvisionDataType;
import envision.lang.variables.EnvisionBoolean;
import envision.lang.variables.EnvisionChar;
import envision.lang.variables.EnvisionDouble;
import envision.lang.variables.EnvisionInt;
import envision.lang.variables.EnvisionNumber;
import envision.lang.variables.EnvisionString;
import envision.lang.variables.EnvisionVariable;
import eutil.EUtil;
import eutil.math.NumberUtil;

/** Contains functions which pertain to object types and potential conversions of them. */
public class CastingUtil {
	
	public static EnvisionObject numberCast(EnvisionObject in, String toType) { return numberCast(in, EnvisionDataType.getDataType(toType)); }
	public static EnvisionObject numberCast(EnvisionObject in, EnvisionDataType toType) {
		if (in instanceof EnvisionNumber) {
			EnvisionNumber num = (EnvisionNumber) in;
			
			//if the types are already the same, just return the original object
			if (num.getInternalType() == toType) { return in; }
			
			//check what the type is being cast to
			switch (toType) {
			case INT: return castVariable(num, INT);
			case DOUBLE: 
			case NUMBER: return castVariable(num, NUMBER);
			default: throw new InvalidDataTypeError("Invalid type: '" + toType + "' expected a number!");
			}
		}
		if (in != null) throw new InvalidDataTypeError("Invalid type: '" + in.getTypeString() + "' expected a number!");
		throw new InvalidDataTypeError("Invalid type: 'null' expected a number!");
	}
	
	public static boolean compare(String a, String b) {
		if (EUtil.isEqual(a, b)) return true;
		
		EnvisionDataType typeA = getDataType(a);
		EnvisionDataType typeB = getDataType(b);
		
		//if (a.equals("number")) { return b.equals("int") || b.equals("double"); }
		
		if (typeA != null) {
			switch (typeA) {
			case NUMBER: return typeB == INT || typeB == DOUBLE;
			case OBJECT: return true;
			default: return false;
			}
		}
		
		return false;
	}
	
	/** Checks to make sure the internal types of the two given objects are of the same type. */
	public static void checkType(Object expected, Object checking) {
		String e = ObjectCreator.wrap(expected).getTypeString();
		String c = ObjectCreator.wrap(checking).getTypeString();
		if (!e.equals(c)) {
			throw new InvalidDataTypeError("Invalid type: '" + c + "' expected '" + e + "'!");
		}
		//should test for casting conversions as well..
	}
	
	public static void checkType(EnvisionDataType type, Object o) { checkType(type.type, o); }
	public static void checkType(String type, Object o) {
		String t = ObjectCreator.wrap(o).getTypeString();
		
		//accept any type if the base is 'object'
		if (type.equals("object")) { return; }
		
		//check if a char is going into a string
		if (type.equals(EnvisionDataType.STRING.type) && t.equals(EnvisionDataType.CHAR.type)) {
			return;
		}
		
		//check if an int or double is going into number
		if (type.equals(EnvisionDataType.NUMBER.type) && (t.equals(EnvisionDataType.INT.type) || t.equals(EnvisionDataType.DOUBLE.type))) {
			return;
		}
		
		//check if an int is going into a double
		if (type.equals(EnvisionDataType.DOUBLE.type) && t.equals(EnvisionDataType.INT.type)) {
			return;
		}
		
		if (!type.equals(t)) {
			throw new InvalidDataTypeError("Invalid type: '" + t + "' expected '" + type + "'!");
		}
	}
	
	/** Attempts to cast a variable type to another variable type. Note, this method is not intended for class casting! */
	public static EnvisionVariable castVariable(EnvisionVariable varIn, EnvisionDataType typeIn) {
		if (varIn != null) {
			if (typeIn.equals(EnvisionDataType.NULL)) { }
			try {
				switch (varIn.getInternalType()) {
				case CHAR:
					switch (typeIn) {
					case CHAR: return new EnvisionChar((Character) varIn.get());
					case BOOLEAN: return new EnvisionBoolean(Character.toLowerCase((Character) varIn.get()) == 't');
					case INT: return new EnvisionInt((int) ((Character) varIn.get()));
					case NUMBER:
					case DOUBLE: return new EnvisionDouble((int) ((Character) varIn.get()));
					case STRING: return new EnvisionString(String.valueOf(varIn.get()));
					default: throw new VariableCastError("Invalid cast type: " + typeIn + "!");
					}
				case BOOLEAN:
					switch (typeIn) {
					case CHAR: return new EnvisionChar(((Boolean) varIn.get()) ? 't' : 'f');
					case BOOLEAN: return new EnvisionBoolean((Boolean) varIn.get());
					case INT: return new EnvisionInt(((Boolean) varIn.get()) ? 1 : 0);
					case NUMBER:
					case DOUBLE: return new EnvisionDouble(((Boolean) varIn.get()) ? 1.0 : 0.0);
					case STRING: return new EnvisionString(String.valueOf(varIn.get()));
					default: throw new VariableCastError("Invalid cast type: " + typeIn + "!");
					}
				case INT:
					switch (typeIn) {
					case CHAR: return new EnvisionChar(Character.valueOf((char) ((Number) varIn.get()).longValue()));
					case BOOLEAN: return new EnvisionBoolean((Math.signum(NumberUtil.clamp(((Number) varIn.get()).longValue(), 0, Long.MAX_VALUE)) == 1) ? true : false);
					case INT: return new EnvisionInt(((Number) varIn.get()).longValue());
					case NUMBER:
					case DOUBLE: return new EnvisionDouble(((Number) varIn.get()).doubleValue());
					case STRING: return new EnvisionString(String.valueOf(varIn.get()));
					default: throw new VariableCastError("Invalid cast type: " + typeIn + "!");
					}
				case DOUBLE:
					switch (typeIn) {
					case CHAR: return new EnvisionChar(Character.valueOf((char) Math.floor(((Number) varIn.get()).doubleValue())));
					case BOOLEAN: return new EnvisionBoolean((Math.signum(NumberUtil.clamp(((Number) varIn.get()).doubleValue(), 0, Double.MAX_VALUE)) == 1) ? true : false);
					case INT: return new EnvisionInt(((Number) varIn.get()).longValue());
					case NUMBER:
					case DOUBLE: return new EnvisionDouble(((Number) varIn.get()).doubleValue());
					case STRING: return new EnvisionString(String.valueOf(varIn.get()));
					default: throw new VariableCastError("Invalid cast type: " + typeIn + "!");
					}
				case STRING:
					switch (typeIn) {
					case CHAR: return new EnvisionChar(((String) varIn.get()).isEmpty() ? '\0' : ((String) varIn.get()).charAt(0));
					case BOOLEAN: return new EnvisionBoolean(((String) varIn.get()).isEmpty() ? false : true);
					case INT: return new EnvisionInt(((String) varIn.get()).isEmpty() ? 0 : 1);
					case NUMBER:
					case DOUBLE: return new EnvisionDouble(((String) varIn.get()).isEmpty() ? 0.0 : 1.0);
					case STRING: return new EnvisionString(String.valueOf(varIn.get()));
					default: throw new VariableCastError("Invalid cast type: " + typeIn + "!");
					}
				default: throw new VariableCastError(varIn, typeIn);
				}
			}
			catch (ClassCastException e) {
				e.printStackTrace();
				throw new VariableCastError(varIn, typeIn);
			}
		}
		return varIn;
	}
	
}
