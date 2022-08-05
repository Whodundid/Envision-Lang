package envision_lang.exceptions.errors;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.lang.natives.IDatatype;

public class InvalidArgumentError extends EnvisionLangError {
	
	public InvalidArgumentError(String message) {
		super(message);
	}
	
	public InvalidArgumentError(Object obj, String funcName) {
		super("'" + obj + "' is not a valid argument for the given function: " + funcName + "!");
	}
	
	public InvalidArgumentError(Object obj, EnvisionFunction function) {
		super("'" + obj + "' is not a valid argument for the given function: " + function + "!");
	}
	
	public InvalidArgumentError(String funcName, IDatatype expected, IDatatype got) {
		super("'"+funcName+":"+"' Invalid argument! Expected '"+expected+"' but got '"+got+"'!");
	}
	
	//--------------
	// Static Types
	//--------------
	
	public static InvalidArgumentError nullArgument() {
		return new InvalidArgumentError("Passed argument cannot be null!");
	}
	
	public static InvalidArgumentError expectedExactlyOne() {
		return new InvalidArgumentError("Expected exactly one argument passed!");
	}
	
	public static InvalidArgumentError expectedAtLeastOne() {
		return new InvalidArgumentError("Too few arguments passed! Expected at least one argument!");
	}
	
	public static InvalidArgumentError expectedAtLeast(int amount) {
		return new InvalidArgumentError("Too few arguments passed! Expected at least " + amount + " argument(s)!");
	}
	
	public static InvalidArgumentError expectedAtMost(int amount) {
		return new InvalidArgumentError("Too many arguments passed! Expected at most " + amount + " argument(s)!");
	}
	
	/**
	 * Throw whenever a ClassInstance is attempting to be created but the creation failed
	 * due to a miss-match of expected argument types.
	 * 
	 * @param arg The argument attempting to be used for creation
	 * @param toType The class type attempting to be created
	 * @return A built InvalidArgumentError with a conversion error message
	 */
	public static InvalidArgumentError conversionError(EnvisionObject arg, IDatatype toType) {
		return new InvalidArgumentError("Cannot convert the value '"+arg+"' to an "+toType+"!");
	}
	
	/**
	 * Throw whenever a ClassInstance is attempting to be created but the creation failed
	 * due to a miss-match of expected argument types.
	 * 
	 * @param toType The class type attempting to be created
	 * @return A built InvalidArgumentError with a conversion error message
	 */
	public static InvalidArgumentError conversionError(IDatatype toType) {
		return new InvalidArgumentError("Cannot create an instance of the given type '" + toType + "' from the given arguments!");
	}
	
}
