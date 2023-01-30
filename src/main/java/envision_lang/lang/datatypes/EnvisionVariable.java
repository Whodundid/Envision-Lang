package envision_lang.lang.datatypes;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;

/**
 * The EnvisionVariable is a specific EnvisionObject type which is
 * primarily responsible for providing a direct mapping between
 * Envision datatype and an actual Java Object value.
 * <p>
 * Due to the fact that Envision:Java is written in Java, all
 * corresponding Envision datatypes must be backed by some kind of
 * equivalent datatype structure within Java. The EnvisionVariable
 * class is intended to not only back that object value but also
 * directly manage how that data interacts within the rest of the
 * EnvisionInterpreter.
 * <p>
 * EnvisionVariable, at its core, is intended to be used purely as a
 * superclass structure for which other specific datatype models are
 * built from. While Envision:java does directly support generic 'var'
 * values for both variable instantiation and assignment, these are
 * not truly generic values on the back-end of things. As such, even
 * 'var' value types are still directly mapped to a specific, strongly
 * typed Java datatype under the hood. Due to this fact, generic 'var'
 * values are not technically generic at all in nature. However, any
 * 'var' value may be dynamically reassigned at runtime by simply
 * assigning a new value to the variable. Upon assigning a new value
 * to an existing generic 'var' EnvisionVariable, The new value's
 * datatype is first automatically determined by the interpreter and
 * is assigned to the variable.
 * <p>
 * Every EnvisionVariable is intended to wrap some Envision primitive
 * datatype.
 * <ul>
 * The following are examples of native EnvisionVariables:
 * <p>
 * <li>EnvisionBoolean
 * <li>EnvisionChar
 * <li>EnvisionInt
 * <li>EnvisionDouble
 * <li>EnvisionString
 * <li>EnvisionList
 * </ul>
 * These primitive types are designed to not support any kind of
 * instance scope as they are technically primitive values at their
 * core. Because of this, no instance scope is passed during a
 * variable's creation. Instead, the only scope a primitive variable
 * inherits is the static scope of the class it is derived from. This
 * ensures that the primitive object instance still maintains access
 * to all internal member fields and functions. Furthermore, unlike a
 * class instance created from a user-defined class, the internal
 * variable value is managed using Java:Objects instead of Envision for
 * improved performance.
 * 
 * @author Hunter Bragg
 */
public abstract sealed class EnvisionVariable<TYPE> extends ClassInstance
	permits EnvisionBoolean, EnvisionChar, EnvisionNumber, EnvisionString
{
	
	//--------------
	// Constructors
	//--------------
	
	/**
	 * Native variable types do not have any discernible instance scope
	 * and will simply utilize the deriving class's static class scope for
	 * access to all internal member fields and functions.
	 * 
	 * @param parentClass The class for which this variable is an instance
	 */
	protected EnvisionVariable(EnvisionClass parentClass) {
		super(parentClass);
	}
	
	//---------
	// Getters
	//---------
	
	/**
	 * Returns the Envision Object wrapping some underlying Java object.
	 * Used for in-language passes.
	 * 
	 * @return The wrapping Envision object
	 */
	public abstract EnvisionObject get();
	
	/**
	 * Returns the underlying Java Object which actually backs
	 * this EnvisionVariable.
	 * 
	 * @return The backing Java Object
	 */
	public abstract TYPE get_i();
	
}
