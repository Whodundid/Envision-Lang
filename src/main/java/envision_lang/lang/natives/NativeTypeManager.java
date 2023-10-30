package envision_lang.lang.natives;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.datatypes.EnvisionBooleanClass;
import envision_lang.lang.datatypes.EnvisionCharClass;
import envision_lang.lang.datatypes.EnvisionDoubleClass;
import envision_lang.lang.datatypes.EnvisionIntClass;
import envision_lang.lang.datatypes.EnvisionListClass;
import envision_lang.lang.datatypes.EnvisionMapClass;
import envision_lang.lang.datatypes.EnvisionNumberClass;
import envision_lang.lang.datatypes.EnvisionStringClass;
import envision_lang.lang.datatypes.EnvisionTupleClass;
import envision_lang.lang.functions.EnvisionFunctionClass;

/**
 * Used to keep a constant running log of all native Envision datatypes.
 * Only one instance of each native type should exist within a single
 * Envision instance.
 * <p>
 * All Envision datatypes are strictly case-sensitive. Therefore, Int !=
 * int.
 * <p>
 * <u><b>!!!! ---- NOT THE SAME AS [ {@code TypeManager} ] ----
 * !!!!</u></b>
 * <li>The TypeManager manages user-defined class types.
 * <li>The NativeTypeManager manages Envision's native class/object types.
 */
public final class NativeTypeManager {
    
    //========
    // Fields
    //========
    
    /** The internal native Envision class list. */
    private static final Map<Primitives, EnvisionClass> native_classes = new EnumMap<>(Primitives.class);
    /**
     * This map is used to manage each singleton native datatype in one
     * place.
     */
    private static final Map<String, EnvisionDatatype> native_types = new HashMap<>();
    /** True if Envision's natives have been registered. */
    private static boolean nativesRegistered;
    
    //=======================
    // Static Initialization
    //=======================
    
    //register each native Envision class
    public static void init() {
        if (nativesRegistered) return;
        
        for (Primitives p : Primitives.values()) {
            p.toDatatype();
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
        native_classes.put(Primitives.MAP, EnvisionMapClass.MAP_CLASS);
        
        //load static natives on each native class
        for (EnvisionClass c : native_classes.values()) { c.initClassNatives(); }
        
        nativesRegistered = true;
    }
    
    static {
        init();
    }
    
    //==============
    // Constructors
    //==============
    
    /** Hide Constructor. */
    private NativeTypeManager() {}
    
    //=================
    // Manager Methods
    //=================
    
    public static EnvisionDatatype datatypeOf(Primitives type) {
        var t = native_types.get(type.string_value);
        //return singleton instance if it exists
        if (t != null) return t;
        //create new singleton
        t = new EnvisionDatatype(type);
        native_types.put(type.string_value, t);
        return t;
    }
    
    public static EnvisionDatatype getDatatypeFor(String type) {
        return native_types.get(type);
    }
    
    public static EnvisionClass getClassTypeFor(Primitives type) {
        return native_classes.get(type);
    }
    
    public static boolean doesTypeExist(String type) {
        return native_types.containsKey(type);
    }
    
}
