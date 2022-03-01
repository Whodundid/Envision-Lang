package envision.interpreter.util;

import static envision.lang.util.Primitives.*;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.InvalidDatatypeError;
import envision.exceptions.errors.VariableCastError;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionBoolean;
import envision.lang.datatypes.EnvisionChar;
import envision.lang.datatypes.EnvisionDouble;
import envision.lang.datatypes.EnvisionInt;
import envision.lang.datatypes.EnvisionNumber;
import envision.lang.datatypes.EnvisionString;
import envision.lang.datatypes.EnvisionVariable;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.Primitives;

/**
 * Contains functions which pertain to object types and potential
 * conversions of them.
 */
public class CastingUtil {
	
	public static EnvisionObject castToNumber(EnvisionObject in, Primitives toType) {
		return castToNumber(in, new EnvisionDatatype(toType));
	}
	
	public static EnvisionObject castToNumber(EnvisionObject in, EnvisionDatatype toType) {
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
		return switch (toType.getPrimitiveType()) {
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
	public static void assert_expected_datatype(EnvisionDatatype expected, EnvisionDatatype toCheck) {
		if (expected == null || toCheck == null) {
			throw new EnvisionError("CastingUtil checkType: null type!");
		}
		
		//grab primitive types
		Primitives expected_ptype = expected.getPrimitiveType();
		Primitives toCheck_ptype = toCheck.getPrimitiveType();
		
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
		if (expected_ptype == NUMBER && toCheck_ptype.isNumber()) return;
		
		//check if an int is going into a double
		if (expected_ptype == DOUBLE && toCheck_ptype == INT) return;
		
		//if none of above, check for exact datatype match
		if (!expected.compareType(toCheck)) {
			throw new InvalidDatatypeError("Invalid type: '" + toCheck + "' expected '" + expected + "'!");
		}
	}
	
	/**
	 * Attempts to cast a variable type to another variable type.
	 * <p>
	 * Note: this method is not intended for class casting!
	 */
	public static EnvisionVariable castVariable(EnvisionVariable varIn, EnvisionDatatype typeIn) {
		//ignore if varIn is null
		if (varIn == null) return varIn;
		
		Primitives from = varIn.getDatatype().getPrimitiveType();
		Primitives to = typeIn.getPrimitiveType();
		
		//check for non-primitive type casts
		if (from == null || to == null) return varIn;
		
		//determine casting procedure regarding 'from' -> 'to' types
		switch (from) {
		case CHAR:
		{
			var charVal = (char) varIn.get();
			
			return switch (to) {
			case CHAR -> new EnvisionChar(charVal);
			case BOOLEAN -> new EnvisionBoolean(charVal);
			case INT -> new EnvisionInt(charVal);
			case NUMBER, DOUBLE -> new EnvisionDouble(charVal);
			case STRING -> new EnvisionString(charVal);
			default -> throw new VariableCastError("Invalid cast type: " + typeIn + "!");
			};
		}
			
		case BOOLEAN:
		{
			var boolVal = (boolean) varIn.get();
			
			return switch (to) {
			case CHAR -> new EnvisionChar(boolVal);
			case BOOLEAN -> new EnvisionBoolean(boolVal);
			case INT -> new EnvisionInt(boolVal);
			case NUMBER, DOUBLE -> new EnvisionDouble(boolVal);
			case STRING -> new EnvisionString(boolVal);
			default -> throw new VariableCastError("Invalid cast type: " + typeIn + "!");
			};
		}
		case INT:
		{
			var longVal = ((Number) varIn.get()).longValue();
			
			return switch (to) {
			case CHAR -> new EnvisionChar(longVal);
			case BOOLEAN -> new EnvisionBoolean(longVal);
			case INT -> new EnvisionInt(longVal);
			case NUMBER, DOUBLE -> new EnvisionDouble(longVal);
			case STRING -> new EnvisionString(longVal);
			default -> throw new VariableCastError("Invalid cast type: " + typeIn + "!");
			};
		}
		case DOUBLE:
		{
			var doubleVal = ((Number) varIn.get()).doubleValue();
			
			return switch (to) {
			case CHAR -> new EnvisionChar(doubleVal);
			case BOOLEAN -> new EnvisionBoolean(doubleVal);
			case INT -> new EnvisionBoolean(doubleVal);
			case NUMBER, DOUBLE -> new EnvisionDouble(doubleVal);
			case STRING -> new EnvisionString(doubleVal);
			default -> throw new VariableCastError("Invalid cast type: " + typeIn + "!");
			};
		}
		case STRING:
		{
			var strVal = (String) varIn.get();
			
			return switch (to) {
			case CHAR -> new EnvisionChar(strVal);
			case BOOLEAN -> new EnvisionBoolean(strVal);
			case INT -> new EnvisionInt(strVal);
			case NUMBER, DOUBLE -> new EnvisionDouble(strVal);
			case STRING -> new EnvisionString(strVal);
			default -> throw new VariableCastError("Invalid cast type: " + typeIn + "!");
			};
		}
		default: throw new VariableCastError(varIn, typeIn);
		}
	}
	
}
