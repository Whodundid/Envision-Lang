package envision.interpreter.util;

import static envision.lang.natives.Primitives.BOOLEAN;
import static envision.lang.natives.Primitives.CHAR;
import static envision.lang.natives.Primitives.DOUBLE;
import static envision.lang.natives.Primitives.INT;
import static envision.lang.natives.Primitives.NUMBER;
import static envision.lang.natives.Primitives.STRING;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.InvalidDatatypeError;
import envision.exceptions.errors.VariableCastError;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionBooleanClass;
import envision.lang.datatypes.EnvisionCharClass;
import envision.lang.datatypes.EnvisionDoubleClass;
import envision.lang.datatypes.EnvisionIntClass;
import envision.lang.datatypes.EnvisionNumber;
import envision.lang.datatypes.EnvisionStringClass;
import envision.lang.datatypes.EnvisionVariable;
import envision.lang.natives.IDatatype;
import envision.lang.natives.Primitives;

/**
 * Contains functions which pertain to object types and potential
 * conversions of them.
 */
public class CastingUtil {
	
	public static EnvisionObject castToNumber(EnvisionObject in, IDatatype toType) {
		final class errC {
			static String err(Object in) { return "Invalid type: '" + in + "' expected a number!"; }
		}
		
		//only allow valid number cast types
		if (toType == null || !toType.isNumber()) {
			throw new InvalidDatatypeError(errC.err(toType));
		}
		
		//only allow nubers to be cast
		if (!(in instanceof EnvisionNumber)) {
			throw new InvalidDatatypeError(errC.err(in.getTypeString()));
		}
		
		//cast input as number to work with
		EnvisionNumber n = (EnvisionNumber) in;
		
		//if the types are already the same, just return the original object
		if (n.getDatatype() == toType) return in;
		
		//check what the type is being cast to
		return switch (toType.getPrimitive()) {
		case INT, DOUBLE, NUMBER -> castVariable(n, toType);
		default -> throw new InvalidDatatypeError(errC.err(toType));
		};
	}
	
	/**
	 * This method will check that the given 'expected' datatype is
	 * a valid candidate for the incomming 'toCheck' datatype.
	 * <p>
	 * If the 'toCheck' datatype is valid, then this method will
	 * execute quietly. Otherwise, an InvalidDatatypeError will be
	 * thrown.
	 * 
	 * @param expected
	 * @param toCheck
	 */
	public static void assert_expected_datatype(IDatatype expected, IDatatype toCheck) {
		if (expected == null || toCheck == null) {
			throw new EnvisionError("CastingUtil checkType: null type!");
		}
		
		//grab primitive types
		Primitives expected_ptype = expected.getPrimitive();
		Primitives toCheck_ptype = toCheck.getPrimitive();
		
		//check for null passes
		if (toCheck_ptype == Primitives.NULL) {
			return;
		}
		
		//-----------------------------------------------------------------------------
		// While this system works, it does not account for these kinds of situtations
		//
		// i = 5
		// int x = i
		//
		// this will error because 'i' is technically a VAR holding an int,
		// and 'x' is an 'int' holding an int. NOT THE SAME - error thrown
		//-----------------------------------------------------------------------------
		
		//accept any type if the base is 'var'
		if (expected_ptype == Primitives.VAR) return;
		
		//check if a char is going into a string
		if (expected_ptype == STRING && toCheck_ptype == CHAR) return;
		
		//check if an int or double is going into number
		if (expected_ptype == NUMBER && toCheck_ptype.isNumber() || toCheck_ptype == BOOLEAN) return;
		
		//check if an int is going into a double
		if (expected_ptype == DOUBLE && toCheck_ptype == INT) return;
		
		//if none of above, check for exact datatype match
		if (!expected.compare(toCheck)) {
			throw new InvalidDatatypeError("Invalid type: '" + toCheck + "' expected '" + expected + "'!");
		}
	}
	
	/**
	 * Attempts to cast a variable type to another variable type.
	 * <p>
	 * Note: this method is not intended for class casting!
	 */
	public static EnvisionVariable castVariable(EnvisionVariable varIn, IDatatype typeIn) {
		//ignore if varIn is null
		if (varIn == null) return varIn;
		
		Primitives from = varIn.getDatatype().getPrimitive();
		Primitives to = typeIn.getPrimitive();
		
		//check for non-primitive type casts
		if (from == null || to == null) return varIn;
		
		//determine casting procedure regarding 'from' -> 'to' types
		switch (from) {
		case CHAR:
		{
			var charVal = (char) varIn.get_i();
			
			return switch (to) {
			case CHAR -> EnvisionCharClass.newChar(charVal);
			case BOOLEAN -> EnvisionBooleanClass.newBoolean(charVal);
			case INT -> EnvisionIntClass.newInt(charVal);
			case NUMBER, DOUBLE -> EnvisionDoubleClass.newDouble(charVal);
			case STRING -> EnvisionStringClass.newString(charVal);
			default -> throw new VariableCastError("Invalid cast type: " + typeIn + "!");
			};
		}
			
		case BOOLEAN:
		{
			var boolVal = (boolean) varIn.get_i();
			
			return switch (to) {
			case CHAR -> EnvisionCharClass.newChar(boolVal);
			case BOOLEAN -> EnvisionBooleanClass.newBoolean(boolVal);
			case INT -> EnvisionIntClass.newInt(boolVal);
			case NUMBER, DOUBLE -> EnvisionDoubleClass.newDouble(boolVal);
			case STRING -> EnvisionStringClass.newString(boolVal);
			default -> throw new VariableCastError("Invalid cast type: " + typeIn + "!");
			};
		}
		case INT:
		{
			long longVal = ((EnvisionNumber) varIn).intVal_i();
			
			return switch (to) {
			//case CHAR -> EnvisionCharClass.newChar(longVal);
			case BOOLEAN -> EnvisionBooleanClass.newBoolean(longVal != 0);
			case INT -> EnvisionIntClass.newInt(longVal);
			case NUMBER, DOUBLE -> EnvisionDoubleClass.newDouble(longVal);
			case STRING -> EnvisionStringClass.newString(longVal);
			default -> throw new VariableCastError("Invalid cast type: " + typeIn + "!");
			};
		}
		case DOUBLE:
		{
			double doubleVal = ((EnvisionNumber) varIn).doubleVal_i();
			
			return switch (to) {
			//case CHAR -> EnvisionCharClass.newChar(doubleVal);
			//case BOOLEAN -> EnvisionBooleanClass.newBoolean(doubleVal);
			case INT -> EnvisionIntClass.newInt(doubleVal);
			case NUMBER, DOUBLE -> EnvisionDoubleClass.newDouble(doubleVal);
			case STRING -> EnvisionStringClass.newString(doubleVal);
			default -> throw new VariableCastError("Invalid cast type: " + typeIn + "!");
			};
		}
		case STRING:
		{
			//String strVal = (String) varIn.get_i();
			
			//return switch (to) {
			//case CHAR -> EnvisionCharClass.newChar(strVal);
			//case BOOLEAN -> EnvisionBooleanClass.newBoolean(strVal);
			//case INT -> EnvisionIntClass.newInt(strVal);
			//case NUMBER, DOUBLE -> new EnvisionDouble(strVal);
			//case STRING -> EnvisionStringClass.newString(strVal);
			//default -> 
			//};
			throw new VariableCastError("Invalid cast type: " + typeIn + "!");
		}
		default: throw new VariableCastError(varIn, typeIn);
		}
	}
	
}
