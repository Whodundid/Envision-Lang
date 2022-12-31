package envision_lang.lang.natives;

import java.util.HashMap;

import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.datatypes.EnvisionBooleanClass;
import envision_lang.lang.datatypes.EnvisionCharClass;
import envision_lang.lang.datatypes.EnvisionDoubleClass;
import envision_lang.lang.datatypes.EnvisionIntClass;
import envision_lang.lang.datatypes.EnvisionListClass;
import envision_lang.lang.datatypes.EnvisionNumberClass;
import envision_lang.lang.datatypes.EnvisionStringClass;
import envision_lang.lang.datatypes.EnvisionTupleClass;
import envision_lang.lang.internal.EnvisionFunctionClass;
import envision_lang.tokenizer.Token;

/**
 * Used to keep a constant running log of all native Envision datatypes. Only
 * one instance of each native type should exist within a single Envision
 * instance.
 * <p>
 * All Envision datatypes are strictly case-sensitive. Therefore, Int != int.
 * <p>
 * <u><b>!!!! ---- NOT THE SAME AS [ {@code TypeManager} ] ---- !!!!</u></b>
 * <li> The TypeManager manages user-defined class types.
 * <li> The NativeTypeManager manages Envision's native class/object types.
 */
public class NativeTypeManager {

	/** The internal native Envision class list. */
	private static final HashMap<Primitives, EnvisionClass> native_classes = new HashMap<>();
	/** This map is used to manage each singleton native datatype in one place. */
	private static final HashMap<String, EnvisionDatatype> native_types = new HashMap<>();
	/** True if Envision's natives have been registered. */
	private static boolean nativesRegistered = false;
	
	//register each native Envision class
	public static final void initNativeClasses() {
		if (nativesRegistered) return;
		
		for (Primitives p : Primitives.values()) {
			native_types.put(p.string_value, p.toDatatype());
		}
		
		native_classes.put(Primitives.FUNCTION, EnvisionFunctionClass.FUNC_CLASS);
		native_classes.put(Primitives.BOOLEAN, EnvisionBooleanClass.BOOLEAN_CLASS);
		native_classes.put(Primitives.CHAR, EnvisionCharClass.CHAR_CLASS);
		native_classes.put(Primitives.DOUBLE, EnvisionDoubleClass.DOUBLE_CLASS);
		native_classes.put(Primitives.INT, EnvisionIntClass.INT_CLASS);
		native_classes.put(Primitives.NUMBER, EnvisionNumberClass.NUMBER_CLASS);
		native_classes.put(Primitives.STRING, EnvisionStringClass.STRING_CLASS);
		native_classes.put(Primitives.LIST, EnvisionListClass.LIST_CLASS);
		native_classes.put(Primitives.TUPLE, EnvisionTupleClass.TUPLE_CLASS);
		
		//load native classes into native types
		//for (EnvisionClass c : native_classes) {
		//	native_types.put(c.getClassName(), c.getDatatype().toDatatype());
		//}
		
		//native_types.put(Primitives.VAR.string_type, Primitives.VAR.toDatatype());
		//native_types.put(Primitives.BOOLEAN.string_type, Primitives.BOOLEAN.toDatatype());
		
		//load static natives on each native class
		for (EnvisionClass c : native_classes.values()) {
			c.initClassNatives();
		}
		
		nativesRegistered = true;
	}
	
	//--------------
	// Constructors
	//--------------
	
	/** Hide Constructor. */
	private NativeTypeManager() {}
	
	public static EnvisionDatatype datatypeOf(Token type) {
		if (type == null) return datatypeOf(Primitives.VAR);
		else return datatypeOf(type.getLexeme());
	}
	
	public static EnvisionDatatype datatypeOf(Primitives type) {
		var t = native_types.get(type.string_value);
		//return singleton instance if it exists
		if (t != null) return t;
		//create new singleton
		t = new EnvisionDatatype(type);
		native_types.put(type.string_value, t);
		return t;
	}
	
	public static EnvisionDatatype datatypeOf(String type) {
		var t = native_types.get(type);
		//return singleton instance if it exists
		if (t != null) return t;
		//check if a primitive of the given type exists
		Primitives p = Primitives.getPrimitiveType(type);
		//create new singleton
		if (p != null) t = new EnvisionDatatype(p);
		else t = new EnvisionDatatype(type);
		//add it
		native_types.put(type, t);
		return t;
	}
	
	public static EnvisionClass getClassTypeOf(Primitives type) {
		return native_classes.get(type);
	}
	
	public static boolean doesTypeExist(String type) {
		return native_types.containsKey(type);
	}
	
}
