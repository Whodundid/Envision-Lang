package envision.lang.util.structureTypes;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.util.EnvisionDatatype;

/**
 * An instantiable object is any Envision object capable of creating
 * new instances of itself.
 * <p>
 * The most noteworthy example of an InstantiableObject is the
 * EnvisionClass which is capable of creating new instances of itself.
 * Another, less recognized, object with instantiable properties is
 * the EnvisionEnum which, while not directly, instantiable offers the
 * user the ability to create many different values of the same enum.
 * <p>
 * This class is inherently abstract.
 * 
 * @author Hunter Bragg
 */
public abstract class InstantiableObject extends InheritableObject {

	//--------
	// Fields
	//--------
	
	/**
	 * True by default but can be set to false to account for
	 * abstract classes as well as interfaces.
	 */
	protected boolean isInstantiable = true;
	
	/**
	 * True if this object is a primitive object class.
	 * For example: int, double, string, etc. are primitive objects
	 * which are instantiable by nature.
	 */
	protected final boolean isPrimitive;
	
	//--------------
	// Constructors
	//--------------
	
	protected InstantiableObject(EnvisionDatatype internalTypeIn, String nameIn) {
		super(internalTypeIn, nameIn);
		
		isPrimitive = internalTypeIn.isPrimitiveVariableType();
	}
	
	//------------------
	// Abstract Methods
	//------------------
	
	/**
	 * Creates a new instance of this object.
	 * 
	 * All inheriting classes must directly override this method to
	 * specify the exact process of the child's object instantiation.
	 * 
	 * @param interpreter The active working interpreter
	 * @param args Any arguments to be passed to the new object instance
	 * @return The newly created object instance
	 */
	public abstract EnvisionObject newInstance(EnvisionInterpreter interpreter, Object[] args);
	
	/**
	 * Used to manually set whether or not this object should be instantiable.
	 * Abstract classes and interfaces will want to set insantiability to false
	 * to prevent an abstract class instance from being created.
	 * 
	 * @param val The boolean value to be assigned
	 */
	public void setInstantiable(boolean val) {
		isInstantiable = val;
	}
	
	/**
	 * Returns true if this object can actually be instantiated.
	 * The only reason this should ever return false is if this
	 * object represents an abstract class or an interface.
	 * 
	 * @return true if actually instantiable
	 */
	public boolean isInstantiable() { return isInstantiable; }
	
	/**
	 * In the event that this object represents a primitive object
	 * type such as an int, double, string, etc. The object should
	 * be created using the specific Envision type class.
	 * 
	 * @return true if this is a primitive object class
	 */
	public boolean isPrimitive() { return isPrimitive; }
	
}
