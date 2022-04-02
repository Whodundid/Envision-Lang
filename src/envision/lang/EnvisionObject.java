package envision.lang;

import envision.exceptions.errors.objects.CopyNotSupportedError;
import envision.interpreter.util.throwables.ReturnValue;
import envision.lang.classes.EnvisionClass;
import envision.lang.datatypes.EnvisionVariable;
import envision.lang.util.DataModifier;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.Primitives;

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
	 * to an exact referencable datatype.
	 */
	protected final EnvisionDatatype internalType;
	
	/**
	 * A byte-representation of all active modifiers for this specific
	 * object. Modifiers may include types such as 'static', 'final',
	 * 'strong', 'abstract', and 'override'.
	 */
	protected int modifiers = 0b00000000;
	
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
	 * a 'CopyNotSupportedError' in the event they cannot be coppied.
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
	 * In the event that this object represents a primitive object
	 * type such as an int, double, string, etc. The object should
	 * be created using the specific Envision type class.
	 * 
	 * @return true if this is a primitive object class
	 */
	public boolean isPrimitive() { return isPrimitive; }
	
	public boolean isStatic() { return hasModifier(DataModifier.STATIC); }
	public boolean isFinal() { return hasModifier(DataModifier.FINAL); }
	public boolean isStrong() { return hasModifier(DataModifier.STRONG); }

	//---------
	// Setters
	//---------
	
	public EnvisionObject setModifier(DataModifier mod, boolean val) {
		if (val) addModifier(mod);
		else removeModifier(mod);
		return this;
	}
	
	public EnvisionObject setStatic() { addModifier(DataModifier.STATIC); return this; }
	public EnvisionObject setFinal() { addModifier(DataModifier.FINAL); return this; }
	public EnvisionObject setStrong() { addModifier(DataModifier.STRONG); return this; }
	
	//----------------
	// Static Methods
	//----------------
	
	public static Object convert(Object in) {
		return (in instanceof EnvisionVariable env_var) ? env_var.get() : in;
	}
	
	protected static Object cv(Object in) {
		return convert(in);
	}
	
	protected static void ret(EnvisionObject obj) {
		throw new ReturnValue(obj);
	}
	
	//------------------
	// Internal Methods
	//------------------
	
	/**
	 * Used to bind any and all internal object methods.
	 * <p>
	 * This method should be overriden in child classes to specify their own
	 * specific internal methods.
	 */
	/*
	protected void registerInternalMethods() {
		im(new InternalFunction(BOOLEAN, "equals", VAR) { protected void body(Object[] a) { ret(t.equals(a[0])); }});
		im(new InternalFunction(INT, "hash") { protected void body(Object[] a) { ret(t.getObjectHash()); }});
		im(new InternalFunction(STRING, "hexHash") { protected void body(Object[] a) { ret(t.getHexHash()); }});
		im(new InternalFunction(BOOLEAN, "isStatic") { protected void body(Object[] a) { ret(t.isStatic()); }});
		im(new InternalFunction(BOOLEAN, "isFinal") { protected void body(Object[] a) { ret(t.isFinal()); }});
		im(new InternalFunction(STRING, "name") { protected void body(Object[] a) { ret(t.getName()); }});
		im(new InternalFunction(STRING, "toString") { protected void body(Object[] a) { ret(t.toString()); }});
		im(new InternalFunction(STRING, "type") { protected void body(Object[] a) { ret(t.getDatatype().getType()); }});
		im(new InternalFunction(STRING, "typeString") { protected void body(Object[] a) { ret(t.internalType + "_" + t.getHexHash()); }});
		im(new InternalFunction(STRING, "visibility") { protected void body(Object[] a) { ret(t.getVisibility().toString()); }});
		im(new InternalFunction(VOID, "setStrong", BOOLEAN) {
			protected void body(Object[] a) {
				if (t.isRestricted()) throw new RestrictedAccessError(t);
				if ((boolean) cv(a[0])) t.setStrong();
				else t.removeModifier(DataModifier.STRONG);
			}
		});
		im(new InternalFunction(LIST, "functions") {
			protected void body(Object[] a) {
				EnvisionList l = new EnvisionList(STRING);
				internalFunctions.keySet().iterator().forEachRemaining(l::add);
				ret(l);
			}
		});
		im(new InternalFunction(BOOLEAN, "setVis", STRING) {
			protected void body(Object[] a) {
				if (t.isRestricted()) throw new RestrictedAccessError(t);
				String type = (String) cv(a[0]);
				switch (type) {
				case "public": setPublic(); break;
				case "protected": setProtected(); break;
				case "private": setPrivate(); break;
				case "scope": setVisibility(VisibilityType.SCOPE); break;
				default: throw new InvalidArgumentError(type, getIFuncName());
				}
			}
		});
		im(new InternalFunction(BOOLEAN, "setFinal", BOOLEAN) {
			protected void body(Object[] a) {
				if (t.isRestricted()) throw new RestrictedAccessError(t);
				if ((boolean) cv(a[0])) t.setFinal();
				else t.removeModifier(DataModifier.FINAL);
			}
		});
	}
	*/
	
}
