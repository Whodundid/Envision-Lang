package envision_lang.lang.java;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.language_errors.EnvisionLangError;

/**
 * A utility class that provides functionality to translate.
 * 
 * @author Hunter Bragg
 */
public class NativeArrayUtil {
    
    private NativeArrayUtil() {}
    
    //=============================
    // Get At Index -> Java Object
    //=============================
    
    /**
     * Attempts to return the Object at the given index of the
     * 'arrayObject'. If the arrayObject is not actually an array (or some
     * type that can be indexed), then an unrecognized target error is
     * thrown.
     * <p>
     * The mapper object is truly only necessary if the given 'arrayObject'
     * is an EnvisionList as there would be no way to effectively translate
     * from Envision to Java otherwise.
     * 
     * @param mapper      Performs Envision to Java translations (only
     *                    necessary if extracting from an EnvisionList)
     * @param arrayObject The object to extract from
     * @param index       The index within the given arrayObject to extract
     *                    at
     * 
     * @return The translated Java Object at the given arrayObject's index
     */
    public static Object getJavaObjectAtIndex(NativeDatatypeMapper mapper, Object arrayObject, int index) {
        // prevent null array objects
        if (arrayObject == null) {
            throw new NullPointerException("Error! Cannot get object at index array -- given array object is NULL!");
        }
        // prevent invalid array indexes
        if (index < 0) {
            throw new ArrayIndexOutOfBoundsException("Error! Cannot get object at a negative array index! '" + index + "'");
        }
        
        // the extracted object to return
        Object objectAtIndex;
        
        // determine what type the 'arrayObject' is and attempt to extract accordingly
        if (arrayObject instanceof EnvisionList env_list) {
            // fail if the required datatype mapper is null -- there would be no way to translate to Java if it was Envision
            if (mapper == null) {
                throw new IllegalArgumentException("Error! Cannot properly translate to Java object without a valid datatype mapper!");
            }
            
            EnvisionObject extractedObject = fromEnvisionList(env_list, index);
            objectAtIndex = mapper.mapToJava(extractedObject);
        }
        else if (arrayObject instanceof List<?> list)          objectAtIndex = fromList(list, index);
        else if (arrayObject instanceof Vector<?> vec)         objectAtIndex = fromVector(vec, index);
        else if (arrayObject instanceof Collection<?> col)     objectAtIndex = fromCollection(col, index);
        else if (arrayObject.getClass().isArray())          objectAtIndex = fromArray(arrayObject, index);
        else {
            throw new EnvisionLangError("Error! Unrecognized array/list target to extract from! '" + arrayObject.getClass() + "'");
        }
        
        return objectAtIndex;
    }
    
    //================================
    // Get At Index -> EnvisionObject
    //================================
    
    /**
     * Attempts to return the Object at the given index of the
     * 'arrayObject'. If the arrayObject is not actually an array (or some
     * type that can be indexed), then an unrecognized target error is
     * thrown.
     * <p>
     * The mapper object is necessary if the given 'arrayObject'
     * is NOT an EnvisionList as there would be no way to effectively translate
     * from Java to Envision otherwise.
     * 
     * @param mapper      Performs Java to Envision translations (only
     *                    necessary if not extracting from an EnvisionList)
     * @param arrayObject The object to extract from
     * @param index       The index within the given arrayObject to extract
     *                    at
     * 
     * @return The translated EnvisionObject at the given arrayObject's index
     */
    public static EnvisionObject getEnvisionObjectAtIndex(NativeDatatypeMapper mapper, Object arrayObject, int index) {
        // prevent null array objects
        if (arrayObject == null) {
            throw new NullPointerException("Error! Cannot get object at index array -- given array object is NULL!");
        }
        // prevent invalid array indexes
        if (index < 0) {
            throw new ArrayIndexOutOfBoundsException("Error! Cannot get object at a negative array index! '" + index + "'");
        }
        
        // the extracted object to return
        EnvisionObject objectAtIndex;
        
        // determine what type the 'arrayObject' is and attempt to extract accordingly
        if (arrayObject instanceof EnvisionList env_list) {
            objectAtIndex = fromEnvisionList(env_list, index);
        }
        else {
            // fail if the required datatype mapper is null -- there would be no way to translate to Java if it was Envision
            if (mapper == null) {
                throw new IllegalArgumentException("Error! Cannot properly translate to Java object without a valid datatype mapper!");
            }
            
            Object extractedObject;
            
            if (arrayObject instanceof List<?> list)            extractedObject = fromList(list, index);
            else if (arrayObject instanceof Vector<?> vec)      extractedObject = fromVector(vec, index);
            else if (arrayObject instanceof Collection<?> col)  extractedObject = fromCollection(col, index);
            else if (arrayObject.getClass().isArray())          extractedObject = fromArray(arrayObject, index);
            else {
                throw new EnvisionLangError("Error! Unrecognized array/list target to extract from! '" + arrayObject.getClass() + "'");
            }
            
            objectAtIndex = mapper.mapToEnvision(extractedObject);
        }
        
        return objectAtIndex;
    }
    
    //=======================
    // Static Helper Methods
    //=======================
    
    private static EnvisionObject fromEnvisionList(EnvisionList env_list, int index) {
        return env_list.get(index);
    }
    
    private static Object fromList(List<?> list, int index) {
        return list.get(index);
    }
    
    private static Object fromVector(Vector<?> vec, int index) {
        return vec.get(index);
    }
    
    private static Object fromCollection(Collection<?> col, int index) {
        var it = col.iterator();
        int i = 0;
        while (it.hasNext() && i < index) {
            it.next();
            i++;
        }
        
        if (it.hasNext() && i < col.size()) {
            return it.next();
        }
        
        return null;
    }
    
    private static Object fromArray(Object arrayObject, int index) {
        return Array.get(arrayObject, index);
    }
    
}
