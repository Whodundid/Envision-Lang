package envision.lang;

import envision.exceptions.errors.objects.CopyNotSupportedError;
import envision.interpreter.util.throwables.ReturnValue;
import envision.lang.classes.EnvisionClass;
import envision.lang.datatypes.EnvisionVariable;
import envision.lang.util.DataModifier;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.Primitives;
import envision.lang.util.VisibilityType;

/**
 * The underlying parent object class for which all Envision:Java
 * objects inherit from.
 * 
 * @author Hunter Bragg
 */
public abstract class EnvisionObject {

	//--------
	// Fields
	//--------
	
	/**
	 * The EnvisionClass for which this is an instance of.
	 * This MUST be assigned during class creation.
	 */
	public EnvisionClass internalClass;
	
	/**
	 * The underlying datatype of this object instance.
	 * Even though Envision:Java supports dynamic variable datatypes,
	 * Java does not. As such, every dynamic type must be converted down
	 * to an exact reference-able datatype.
	 */
	protected final EnvisionDatatype internalType;
	
	/**
	 * A byte-representation of all active modifiers for this specific
	 * object. Modifiers may include types such as 'static', 'final',
	 * 'strong', 'abstract', 'override', and visibility mods.
	 * 
	 * @see DataModifier
	 */
	protected int modifiers = 0b00000000;	// 0000-0000
	
	/**
	 * True if this object is a primitive object class.
	 * For example: int, double, string, etc. are primitive objects
	 * which are instantiable by nature.
	 */
	protected final boolean isPrimitive;
	
	//--------------
	// Constructors
	//--------------
	
	/**
	 * Assigns the internal datatype for this object.
	 * 
	 * @param internalTypeIn
	 */
	protected EnvisionObject(EnvisionDatatype internalTypeIn) {
		internalType = internalTypeIn;
		
		//assign primitive flag
		isPrimitive = internalTypeIn.isPrimitiveVariableType();
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public String toString() {
		return getDatatype() + "_" + getHexHash();
	}
	
	//---------
	// Methods
	//---------
	
	/**
	 * Creates a shallow copy of this specific object.
	 * <p>
	 * Note: not all objects inherently support copying and will throw
	 * a 'CopyNotSupportedError' in the event they cannot be copied.
	 * 
	 * @return A shallow copy of this object
	 */
	public EnvisionObject copy() {
		throw new CopyNotSupportedError();
	}
	
	/**
	 * Returns true if this object has the given data modifier.
	 */
	public boolean hasModifier(DataModifier mod) {
		return (getModifiers() & mod.byteVal) != 0;
	}
	
	/**
	 * Returns the integer containing all byte modifiers on this specific object.
	 */
	public int getModifiers() {
		return modifiers;
	}
	
	/**
	 * Applies the given modifier to this object.
	 */
	public void addModifier(DataModifier mod) {
		modifiers |= mod.byteVal;
	}
	
	/**
	 * Removes the given modifier from this object.
	 */
	public void removeModifier(DataModifier mod) {
		modifiers ^= mod.byteVal;
	}
	
	//---------
	// Getters
	//---------
	
	/**
	 * @return The underlying datatype of this object.
	 */
	public EnvisionDatatype getDatatype() {
		return internalType;
	}
	
	public int getObjectHash() { return hashCode(); }
	public String getHexHash() { return Integer.toHexString(hashCode()); }
	public Primitives getPrimitiveType() { return internalType.getPrimitiveType(); }
	public String getTypeString() { return internalType.getType(); }
	
	/**
	 * In the event that this object represents a primitive object type such as an
	 * int, double, string, etc. The object should be created using the specific
	 * Envision type class.
	 * 
	 * @return true if this is a primitive object class
	 */
	public boolean isPrimitive() { return isPrimitive; }
	
	public boolean isStatic() { return hasModifier(DataModifier.STATIC); }
	public boolean isFinal() { return hasModifier(DataModifier.FINAL); }
	public boolean isStrong() { return hasModifier(DataModifier.STRONG); }
	
	public boolean isRestricted() { return hasModifier(DataModifier.RESTRICTED); }
	public boolean isPrivate() { return hasModifier(DataModifier.PRIVATE); }
	public boolean isProtected() { return hasModifier(DataModifier.PROTECTED); }
	public boolean isPublic() { return hasModifier(DataModifier.PUBLIC); }
	/** Returns true if there are no visibility modifiers set. */
	public boolean isScopeVisibility() { return ((modifiers >> 4) & 0xf) == 0; }

	//---------
	// Setters
	//---------
	
	public EnvisionObject setModifier(DataModifier mod, boolean val) {
		if (val) addModifier(mod);
		else removeModifier(mod);
		return this;
	}
	
	/**
	 * Sets all bitwise visibility modifiers to zero.
	 */
	private void resetVisibility() {
		removeModifier(DataModifier.RESTRICTED);
		removeModifier(DataModifier.PRIVATE);
		removeModifier(DataModifier.PROTECTED);
		removeModifier(DataModifier.PUBLIC);
	}
	
	/**
	 * Assigns data-modifiers which pertain to the given Visibility type.
	 * 
	 * @param visIn Visibility to assign
	 * @return This EnvisionObject
	 */
	public EnvisionObject setVisibility(VisibilityType visIn) {
		//clear out current visibility bitwise mods
		resetVisibility();
		//assign specific visibility bitwise mods from visibility type
		switch (visIn) {
		case RESTRICTED: 	addModifier(DataModifier.RESTRICTED); 	break;
		case PRIVATE: 		addModifier(DataModifier.PRIVATE);		break;
		case PROTECTED: 	addModifier(DataModifier.PROTECTED);	break;
		case PUBLIC: 		addModifier(DataModifier.PUBLIC);		break;
		//*scope visibility does not assign bitwise mods*
		default: 													break;
		}
		return this;
	}
	
	public EnvisionObject setStatic() { addModifier(DataModifier.STATIC); return this; }
	public EnvisionObject setFinal() { addModifier(DataModifier.FINAL); return this; }
	public EnvisionObject setStrong() { addModifier(DataModifier.STRONG); return this; }
	
	public EnvisionObject setRestricted() { return setVisibility(VisibilityType.RESTRICTED); }
	public EnvisionObject setPrivate() { return setVisibility(VisibilityType.PRIVATE); }
	public EnvisionObject setProtected() { return setVisibility(VisibilityType.PROTECTED); }
	public EnvisionObject setPublic() { return setVisibility(VisibilityType.PUBLIC); }
	public EnvisionObject setScopeVisibility() { return setVisibility(VisibilityType.SCOPE); }
	
	//----------------
	// Static Methods
	//----------------
	
	/**
	 * In the event that the incomming object is an EnvisionVariable, the variable
	 * is converted to its internal Java value. Otherwise, the original object is
	 * returned unmodified.
	 * 
	 * @param in The object to potentially convert
	 * @return The converted object value
	 */
	public static Object convert(Object in) {
		return (in instanceof EnvisionVariable env_var) ? env_var.get_i() : in;
	}
	
	/**
	 * Internal shorthand for 'convert'.
	 * 
	 * @param in The object to potentially convert
	 * @return The converted object value
	 */
	protected static Object cv(Object in) {
		return convert(in);
	}
	
	/**
	 * An internal means for propagating a value through Envision scopes.
	 * 
	 * @param obj The object to return
	 */
	protected static void ret(EnvisionObject obj) {
		throw new ReturnValue(obj);
	}
	
}
