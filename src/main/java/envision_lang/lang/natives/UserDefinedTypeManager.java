package envision_lang.lang.natives;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.tokenizer.Token;
import eutil.debug.Inefficient;

/**
 * Interpreter Utility to help keep track of user-defined types.
 * 
 * @author Hunter Bragg
 */
@Inefficient(reason = "This should probably move to exist in a per-scope"
    + "basis to account for clases under the same name but"
    + "defined in different scopes."
    + ""
    + "NOTE: This class is still necessary to discern user-defined"
    + "types for typed-variable-declarations: 'V v = V()'.")
public class UserDefinedTypeManager {
    
    /**
     * A collection of all user defined datatypes along with the
     * EnvisionClass associated with them.
     */
    private final Map<String, EnvisionClass> userClassTypes = new HashMap<>();
    
    private final Map<String, EnvisionDatatype> userDefinedTypes = new HashMap<>();
    
    /**
     * Defines a new user-defined datatype within this TypeManager. The
     * user-defined type must include the EnvisionClass to associate with
     * the type.
     * 
     * @param userClass
     */
    public void defineUserClass(EnvisionClass userClass) {
        if (userClass == null) throw new EnvisionLangError("TypeManager: Java::NULL user defined type!");
        String className = userClass.getClassName();
        
        userClassTypes.put(className, userClass);
    }
    
    /**
     * Returns true if the given type is currently defined within this
     * TypeManager.
     * 
     * @param typeIn the type to check for
     * 
     * @return true if defined
     */
    public boolean isTypeDefined(IDatatype typeIn) {
        if (typeIn == null) return false;
        String strVal = typeIn.getStringValue();
        if (NativeTypeManager.doesTypeExist(strVal)) return true; 
        return userDefinedTypes.containsKey(strVal);
    }
    
    /**
     * Returns true if the given type is currently defined within this
     * TypeManager.
     * 
     * @param typeIn the type to check for
     * 
     * @return true if defined
     */
    public boolean isTypeDefined(String typeIn) {
        if (typeIn == null) return false;
        var value = userClassTypes.get(typeIn);
        if (value == null) return NativeTypeManager.doesTypeExist(typeIn);
        return true;
    }
    
    /**
     * Returned the associated EnvisionClass for the given user-defined
     * type. If the type is not defined, null is returned instead.
     * 
     * @param typeIn the type to grab
     * 
     * @return the EnvisionClass of the user-defined type
     */
    public EnvisionClass getTypeClass(IDatatype typeIn) {
        if (typeIn.isPrimitive()) return NativeTypeManager.getClassTypeFor(typeIn.getPrimitive());
        return userClassTypes.get(typeIn.getStringValue());
    }
    
    /**
     * Returned the associated EnvisionClass for the given user-defined
     * type. If the type is not defined, null is returned instead.
     * 
     * @param typeIn the type to grab
     * 
     * @return the EnvisionClass of the user-defined type
     */
    public EnvisionClass getTypeClass(String typeIn) {
        return userClassTypes.get(typeIn);
    }
    
    /**
     * Returns a set of all user defined datatypes within this TypeManager.
     * 
     * @return a set of all types
     */
    public Set<EnvisionClass> getUserDefinedTypeSet() {
        return userClassTypes.values().stream().collect(Collectors.toCollection(HashSet::new));
    }
    
    public EnvisionDatatype getOrCreateDatatypeFor(Token<?> type) {
        if (type == null) throw new NullPointerException("Error! Given type is Java::NULL!");
        return getOrCreateDatatypeFor(type.getLexeme());
    }
    
    public EnvisionDatatype getOrCreateDatatypeFor(String type) {
        // first check if the given type is a primitive type
        if (NativeTypeManager.doesTypeExist(type)) {
            return NativeTypeManager.getDatatypeFor(type);
        }
        
        // otherwise, check user defined types
        var t = userDefinedTypes.get(type);
        // return singleton instance if it exists
        if (t != null) return t;
        // create new singleton
        t = new EnvisionDatatype(type);
        // add it
        userDefinedTypes.put(type, t);
        return t;
    }
    
}
