package envision_lang.interpreter;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.datatypes.EnvisionBoolean;
import envision_lang.lang.datatypes.EnvisionBooleanClass;
import envision_lang.lang.datatypes.EnvisionInt;
import envision_lang.lang.exceptions.errors.NullVariableError;
import envision_lang.lang.natives.EnvisionNull;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.EnvisionStaticTypes;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.statements.ParsedStatement;

public class AbstractInterpreterExecutor {
	
	protected static void execute(EnvisionInterpreter interpreter, ParsedStatement statement) {
		interpreter.execute(statement);
	}
	
	protected static EnvisionObject evaluate(EnvisionInterpreter interpreter, ParsedExpression expression) {
		return interpreter.evaluate(expression);
	}
	
	protected static void pushScope(EnvisionInterpreter interpreter) {
		interpreter.pushScope();
	}
	
	protected static void popScope(EnvisionInterpreter interpreter) {
		interpreter.popScope();
	}
	
	/**
	 * Returns true if the given object is an EnvisionBoolean and the
	 * boolean value is true.
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isTrue(EnvisionObject obj) {
		if (obj instanceof EnvisionBoolean bool) return bool.bool_val;
		if (obj instanceof EnvisionInt env_int) return env_int.int_val != 0;
		return false;
	}
	
	/**
	 * Returns true if the given objects are equivalent in some regard.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean isEqual_i(EnvisionInterpreter interpreter, EnvisionObject a, EnvisionObject b) {
		//do not allow null objects!
		if (a == null) throw new NullVariableError(a);
		if (b == null) throw new NullVariableError(b);
		
		//check for null objects
		if (a == EnvisionNull.NULL) return b == EnvisionNull.NULL;
		//check for class instances
		if (a instanceof ClassInstance a_inst) {
			//if b is a class instance -- check 'equals' for overrides
			if (b instanceof ClassInstance b_inst) return a_inst.executeEquals_i(interpreter, b);
			//if b is not a class instance, then these cannot be equal
			return false;
		}
		//if a was not a class instance, return false if b is an instance
		else if (b instanceof ClassInstance b_inst) return false;
		//otherwise check for direct object equivalence
		return a.equals(b);
	}
	
	public static EnvisionBoolean isEqual(EnvisionInterpreter interpreter, EnvisionObject a, EnvisionObject b) {
		return EnvisionBooleanClass.valueOf(isEqual_i(interpreter, a, b));
	}
	
	/**
	 * Returns true if the given object is either null by Java terms,
	 * or is an instance of an EnvisionNullObject by Envision terms.
	 * 
	 * @param in The object being compared to null
	 * @return true if the given object is in fact null
	 */
	protected static boolean isNull(EnvisionObject in) {
		return (in == null || in == EnvisionNull.NULL);
	}
	
	/**
	 * Returns true if the given datatype is either null by Java terms,
	 * or is equivalent to the EnvisionDataType.NULL by Envision
	 * terms.
	 * 
	 * @param typeIn The datatype being compared to null
	 * @return true if the given datatype is in fact null
	 */
	protected static boolean isNull(IDatatype typeIn) {
		return (typeIn == null || typeIn == EnvisionStaticTypes.NULL_TYPE);
	}
	
	/**
	 * This method will throw a NullVariableError if the given object 
	 * is found to actually be null by either Java or Envision:Java's
	 * terms.
	 * <p>
	 * If the given value is found to NOT be null, then this method
	 * will execute quietly and return to normal code execution.
	 * 
	 * @param in The object being compared to null.
	 */
	protected static void assertNotNull(EnvisionObject in) {
		if (isNull(in)) throw new NullVariableError(in);
	}
	
}
