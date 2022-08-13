package envision_lang.lang;

import envision_lang.exceptions.errors.objects.CopyNotSupportedError;
import envision_lang.interpreter.util.throwables.ReturnValue;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.datatypes.EnvisionVariable;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.Primitives;
import envision_lang.lang.util.DataModifier;
import envision_lang.lang.util.DataModifierHandler;
import envision_lang.lang.util.VisibilityType;

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
	 * This <u><b>MUST</b></u> be assigned during class creation.
	 */
	public EnvisionClass internalClass;
	
	/**
	 * The underlying datatype of this object instance.
	 * Even though Envision:Java supports dynamic variable datatypes,
	 * Java does not. As such, every dynamic type must be converted down
	 * to an exact reference-able datatype.
	 */
	protected final IDatatype internalType;
	
	/**
	 * A byte-representation of all active modifiers for this specific
	 * object. Modifiers may include types such as 'static', 'final',
	 * 'strong', 'abstract', 'override', and visibility mods.
	 * 
	 * @see DataModifier
	 */
	protected final DataModifierHandler modifierHandler = new DataModifierHandler();	// 0000-0000
	
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
	protected EnvisionObject(IDatatype internalTypeIn) {
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
	 * Note: not all objects inherently support copying and will throw a
	 * 'CopyNotSupportedError' in the event that they cannot be copied.
	 * 
	 * @return A shallow copy of this object
	 */
	public EnvisionObject copy() {
		throw new CopyNotSupportedError();
	}
	
	//---------
	// Getters
	//---------
	
	/**
	 * @return The underlying datatype of this object.
	 */
	public IDatatype getDatatype() { return internalType; }
	public Primitives getPrimitiveType() { return internalType.getPrimitive(); }
	public String getTypeString() { return internalType.getType(); }
	
	public int getObjectHash() { return hashCode(); }
	public String getHexHash() { return Integer.toHexString(hashCode()); }
	
	/**
	 * In the event that this object represents a primitive object type such as an
	 * int, double, string, etc. The object should be created using the specific
	 * Envision type class.
	 * 
	 * @return true if this is a primitive object class
	 */
	public boolean isPrimitive() { return isPrimitive; }
	
	/**
	 * @return This object's visibility
	 */
	public VisibilityType getVisibility() { return modifierHandler.getVisibility(); }
	
	public boolean isStatic() { return modifierHandler.isStatic(); }
	public boolean isFinal() { return modifierHandler.isFinal(); }
	public boolean isStrong() { return modifierHandler.isStrong(); }
	
	public boolean isRestricted() { return modifierHandler.isRestricted(); }
	public boolean isPrivate() { return modifierHandler.isPrivate(); }
	public boolean isProtected() { return modifierHandler.isProtected(); }
	public boolean isPublic() { return modifierHandler.isPublic(); }
	/** Returns true if there are no visibility modifiers set. */
	public boolean isScopeVisibility() { return modifierHandler.isScopeVisibility(); }

	//---------
	// Setters
	//---------
	
	public EnvisionObject setModifier(DataModifier mod, boolean val) {
		modifierHandler.setModifier(mod, val);
		return this;
	}
	
	/**
	 * Assigns data-modifiers which pertain to the given Visibility type.
	 * 
	 * @param visIn Visibility to assign
	 * @return This EnvisionObject
	 */
	public EnvisionObject setVisibility(VisibilityType visIn) {
		modifierHandler.setVisibility(visIn);
		return this;
	}
	
	public EnvisionObject setStatic() { modifierHandler.setStatic(); return this; }
	public EnvisionObject setFinal() { modifierHandler.setFinal(); return this; }
	public EnvisionObject setStrong() { modifierHandler.setStrong(); return this; }
	
	public EnvisionObject setRestricted() { modifierHandler.setRestricted(); return this; }
	public EnvisionObject setPrivate() { modifierHandler.setPrivate(); return this; }
	public EnvisionObject setProtected() { modifierHandler.setProtected(); return this; }
	public EnvisionObject setPublic() { modifierHandler.setPublic(); return this; }
	public EnvisionObject setScopeVisibility() { modifierHandler.setScopeVisibility(); return this; }
	
	//----------------
	// Static Methods
	//----------------
	
	/**
	 * In the event that the incoming object is an EnvisionVariable, the variable
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
		throw ReturnValue.wrap(obj);
	}
	
}
