package envision.lang.natives;

/**
 * The underlying datatype class to be used within the Envision:Java
 * scripting language.
 * <p>
 * All datatypes are encapsulated by a String to represent the exact
 * type. Furthermore, If a given type is the same as a basic Envision
 * Primitive datatype, then the primitive type will also be grabbed
 * for simplifying future type casts or conversions.
 * 
 * @see Primitives
 * @author Hunter Bragg
 */
public class EnvisionDatatype implements IDatatype {
	
	//--------
	// Fields
	//--------
	
	/**
	 * The underlying string type.
	 */
	private final String type;
	
	/**
	 * The underlying primitive type. Null if not created from a primitive.
	 */
	private final Primitives primitive_type;
	
	//--------------
	// Constructors
	//--------------
	
	protected EnvisionDatatype(String in) {
		type = in;
		primitive_type = null;
	}
	
	protected EnvisionDatatype(Primitives in) {
		type = in.string_type;
		primitive_type = in;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override public String toString() { return type; }
	@Override public boolean equals(Object o) { return (o instanceof IDatatype t) ? compare(t) : false; }
	
	@Override public Primitives getPrimitive() { return primitive_type; }
	@Override public EnvisionDatatype toDatatype() { return this; }
	@Override public String getType() { return type; }
	
}
