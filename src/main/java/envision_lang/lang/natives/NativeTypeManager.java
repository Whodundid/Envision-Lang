package envision_lang.lang.natives;

import java.util.HashMap;

import envision_lang.tokenizer.Token;

/**
 * Used to keep a constant running log of all native Envision datatypes. Only
 * one instance of each native type should exist within a single Envision
 * instance.
 * <p>
 * All Envision datatypes are strictly case-sensitive. Therefore, Int != int.
 */
public class NativeTypeManager {

	/**
	 * This map is used to manage each singleton native datatype in one place.
	 */
	private static final HashMap<String, EnvisionDatatype> natives = new HashMap<>();
	
	//--------------
	// Constructors
	//--------------
	
	/** Hide Constructor. */
	private NativeTypeManager() {}
	
	public static EnvisionDatatype datatypeOf(Token type) {
		if (type == null) return datatypeOf(Primitives.VAR);
		else return datatypeOf(type.lexeme);
	}
	
	public static EnvisionDatatype datatypeOf(Primitives type) {
		var t = natives.get(type.string_type);
		//return singleton instance if it exists
		if (t != null) return t;
		//create new singleton
		t = new EnvisionDatatype(type);
		natives.put(type.string_type, t);
		return t;
	}
	
	public static EnvisionDatatype datatypeOf(String type) {
		var t = natives.get(type);
		//return singleton instance if it exists
		if (t != null) return t;
		//create new singleton
		t = new EnvisionDatatype(type);
		natives.put(type, t);
		return t;
	}
	
	public static boolean doesTypeExist(String type) {
		return natives.containsKey(type);
	}
	
}
