package envision_lang.lang.java;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import envision_lang.interpreter.util.creation_util.ObjectCreator;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionBoolean;
import envision_lang.lang.datatypes.EnvisionChar;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionNull;
import envision_lang.lang.datatypes.EnvisionNumber;
import envision_lang.lang.datatypes.EnvisionString;
import envision_lang.lang.datatypes.EnvisionTuple;
import envision_lang.lang.datatypes.EnvisionVoid;
import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.lang.natives.IDatatype;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.datatypes.util.JavaDatatype;

/**
 * A special Envision utility class that is capable of translating a Java
 * field type or Java method parameter list into Envision equivalent
 * value(s) and vice-versa.
 * <p>
 * There are some edge cases that cannot simply be addressed due to Java
 * generics type erasure when dealing with list/set translations. Additionally,
 * generic Object array fields will always translate EnvisionInts into Longs and
 * EnvisionDoubles into Doubles due to generic ambiguity.
 * <p>
 * Furthermore, unless explicitly defined otherwise, all generic Java::List
 * fields will be translated into EUtil::EList instances.
 * 
 * @author Hunter Bragg
 */
public class NativeDatatypeMapper {
    
    //========
    // Fields
    //========
    
    /** Used to translate arguments from Java to Envision in order. */
    final IDatatype[] toEnvision;
    /** Used to translate arguments from Envision to Java in order. */
    final Class<?>[] toJava;
    
    /** If this mapper is wrapping a Java method, this is the mapped method's Envision return type. */
    IDatatype envisionReturnType;
    /** If this mapper is wrapping a Java method, this is the method's Java return type. */
    Class<?> javaReturnType;
    
    /** True if this mapper is wrapping a Java Field. */
    final boolean isField;
    /** True if this mapper is wrapping a Java Method/Constructor. */
    final boolean isMethod;
    
    //==============
    // Constructors
    //==============
    
    /**
     * Used to map Java method parameters to their appropriate
     * Java/Envision types for translations.
     * 
     * @param params A Java method's parameters to map
     */
    public NativeDatatypeMapper(Parameter[] params) {
        // set the length of each to be based off of the number of parameters given
        toEnvision = new IDatatype[params.length];
        toJava = new Class<?>[params.length];
        isField = false;
        isMethod = true;
        
        initMethod(params, null);
    }
    
    /**
     * Used to map a Java constructor's parameters to their appropriate
     * Java/Envision types for translations.
     * 
     * @param params A Java object constructor to map
     */
    public NativeDatatypeMapper(Constructor<?> constructorIn) {
        Parameter[] params = constructorIn.getParameters();
        Class<?> returnType = constructorIn.getDeclaringClass();
        
        // set the length of each to be based off of the number of parameters given
        toEnvision = new IDatatype[params.length];
        toJava = new Class<?>[params.length];
        isField = false;
        isMethod = true;
        
        initMethod(params, returnType);
    }
    
    /**
     * Used to map a Java method's parameters and return type to their
     * appropriate Java/Envision types for translations.
     * 
     * @param methodIn A Java method to map
     */
    public NativeDatatypeMapper(Method methodIn) {
        Parameter[] params = methodIn.getParameters();
        Class<?> returnType = methodIn.getReturnType();
        
        // set the length of each to be based off of the number of parameters given
        toEnvision = new IDatatype[params.length];
        toJava = new Class<?>[params.length];
        isField = false;
        isMethod = true;
        
        initMethod(params, returnType);
    }
    
    /**
     * Used to map a Java field into its corresponding Java/Envision
     * datatype.
     * 
     * @param fieldToMap A Java field to map
     */
    public NativeDatatypeMapper(Field fieldToMap) {
        // set the length of each to be exactly 1 because there is just one field
        toEnvision = new IDatatype[1];
        toJava = new Class<?>[1];
        isField = true;
        isMethod = false;
        init(fieldToMap);
    }
    
    //====================
    // Initialize Methods
    //====================
    
    private void initMethod(Parameter[] params, Class<?> methodReturnType) {
        for (int i = 0; i < toJava.length; ++i) {
            final Parameter p = params[i];
            final Class<?> c = p.getType();
            
            toJava[i] = c;
            toEnvision[i] = IDatatype.fromJavaClass(c);
        }
        
        if (methodReturnType != null) {
            javaReturnType = methodReturnType;
            envisionReturnType = IDatatype.fromJavaClass(methodReturnType);
        }
    }
    
    private void init(Field field) {
        final Class<?> c = field.getType();
        
        toJava[0] = c;
        toEnvision[0] = IDatatype.fromJavaClass(c);
    }
    
    //=========
    // Methods
    //=========
    
    /**
     * Maps the given Java object into its Envision equivalent based on
     * this mapper's translation type.
     * 
     * @param value The Java object to map to Envision
     * 
     * @return A mapped Envision type for the given Java object
     */
    public EnvisionObject mapToEnvision(Object value) {
        // throw error if this mapper does not actually possess any types to map to
        if (toEnvision.length == 0) throw NativeMappingError.noMappingError();
        // throw error if the given number of arguments to map does not match this mapper's expected amount
        if (1 != toEnvision.length) throw NativeMappingError.invalidAmount(toEnvision.length, 1);
        
        // grab corresponding envision type for current arg and then map it to Envision
        IDatatype envisionType = toEnvision[0];
        
        try {
            return ObjectCreator.createObject(envisionType, value, true);
        }
        catch (ClassCastException e) {
            throw NativeMappingError.invalidEnvisionMapping(value, envisionType, e);
        }
    }
    
    /**
     * Maps the given Java arguments into their translated Envision
     * equivalents.
     * 
     * @param arguments A set of Java arguments that will be translated
     *                  into Envision values
     *                  
     * @return A set of Java objects mapped to Envision types
     */
    public EnvisionObject[] mapToEnvision(Object[] arguments) {
        // hard fail on passed a null array
        if (arguments == null) throw new IllegalArgumentException("Error! Passed Java arguments cannot be null!");
        // throw error if this mapper does not actually possess any types to map to
        if (toJava.length > 0 && toEnvision.length == 0) throw NativeMappingError.noMappingError();
        // throw error if the given number of arguments to map does not match this mapper's expected amount
        if (arguments.length != toEnvision.length) throw NativeMappingError.invalidAmount(toEnvision.length, arguments.length);
        
        final int size = arguments.length;
        final EnvisionObject[] mappedArguments = new EnvisionObject[size];
        
        // try-catch the entire operation -- if any part of this fails, assume the entire translation will fail
        try {
            for (int i = 0; i < size; ++i) {
                Object obj = arguments[i];
                
                // grab corresponding envision type for current arg and then map it to Envision
                IDatatype envisionType = toEnvision[i];
                EnvisionObject mappedArg = ObjectCreator.createObject(envisionType, obj, true);
                
                mappedArguments[i] = mappedArg;
            }
        }
        catch (Exception e) {
            throw new NativeMappingError("Error! Failed to map Java arguments to Envision!", e);
        }
        
        return mappedArguments;
    }
    
    /**
     * Attempts to translate the given Java Object into this mapper's
     * equivalent Envision method return type.
     * 
     * @param arguments The EnvisionObject argument to translate to Java
     * 
     * @return The translated Java argument
     */
    public EnvisionObject mapReturnTypeToEnvision(Object value) {
        // throw error if this mapper is not a method wrapper
        if (!isMethod) throw new NativeMappingError("Error! Cannot map a return type value because this mapper does not wrap a Java Method!");
        // throw error if this mapper doesn't have an actual type to map to
        if (javaReturnType == null) throw new NativeMappingError("Error! Cannot map return type value because the Envision return type is Java::NULL!");
        
        try {
            return ObjectCreator.createObject(envisionReturnType, value, true);
        }
        catch (ClassCastException e) {
            throw NativeMappingError.invalidEnvisionMapping(value, envisionReturnType, e);
        }
    }
    
    /**
     * Attempts to translate the given EnvisionObject into this mapper's
     * Java equivalent.
     * 
     * @param value The EnvisionObject to translate to Java
     * 
     * @return The translated Java object
     */
    public Object mapToJava(EnvisionObject value) {
        // throw error if this mapper does not actually possess any types to map to
        if (toJava.length == 0) throw NativeMappingError.noMappingError();
        // throw error if the given number of arguments to map does not match this mapper's expected amount
        if (1 != toJava.length) throw NativeMappingError.invalidAmount(toJava.length, 1);
        
        // grab corresponding envision type for current arg and then map it to Envision
        Class<?> javaType = toJava[0];
        
        try {
            return convertToJavaObject(value, javaType);
        }
        catch (ClassCastException e) {
            throw NativeMappingError.invalidJavaMapping(value, javaType, e);
        }
    }
    
    /**
     * Attempts to translate each of the given EnvisionObject arguments
     * into this mapper's Java equivalent types.
     * 
     * @param arguments The EnvisionObject arguments to translate to Java
     * 
     * @return The translated Java arguments
     */
    public Object[] mapToJava(EnvisionObject[] arguments) {
        // hard fail on passed a null array
        if (arguments == null) throw new IllegalArgumentException("Error! Passed Java arguments cannot be null!");
        // throw error if this mapper does not actually possess any types to map to
        if (toEnvision.length > 0 && toJava.length == 0) throw NativeMappingError.noMappingError();
        // throw error if the given number of arguments to map does not match this mapper's expected amount
        if (arguments.length != toJava.length) throw NativeMappingError.invalidAmount(toJava.length, arguments.length);
        
        final int size = arguments.length;
        final Object[] mappedArguments = new Object[size];
        
        // try-catch the entire operation -- if any part of this fails, assume the entire translation will fail
        try {
            for (int i = 0; i < size; ++i) {
                EnvisionObject obj = arguments[i];
                
                // grab corresponding envision type for current arg and then map it to Envision
                Class<?> javaType = toJava[i];
                Object mappedArg = convertToJavaObject(obj, javaType);
                
                mappedArguments[i] = mappedArg;
            }
        }
        catch (Exception e) {
            throw new NativeMappingError("Error! Failed to map Envision arguments to Java!", e);
        }
        
        return mappedArguments;
    }
    
    /**
     * Attempts to translate the given EnvisionObject into this mapper's
     * equivalent Java method return type.
     * 
     * @param arguments The EnvisionObject argument to translate to Java
     * 
     * @return The translated Java argument
     */
    public Object mapReturnTypeToJava(EnvisionObject value) {
        // throw error if this mapper is not a method wrapper
        if (!isMethod) throw new NativeMappingError("Error! Cannot map a return type value because this mapper does not wrap a Java Method!");
        // throw error if this mapper doesn't have an actual type to map to
        if (javaReturnType == null) throw new NativeMappingError("Error! Cannot map return type value because the Java return type is Java::NULL!");
        
        try {
            return convertToJavaObject(value, javaReturnType);
        }
        catch (ClassCastException e) {
            throw NativeMappingError.invalidJavaMapping(value, javaReturnType, e);
        }
    }
    
    //=======================
    // Static Helper Methods
    //=======================
    
    /**
     * Converts the given EnvisionObject to the given Java class type.
     * 
     * @param object The EnvisionObject to convert
     * @param asType The Java type to map to
     * 
     * @return The converted Java Object
     */
    static Object convertToJavaObject(EnvisionObject object, Class<?> asType) {
        // hard fail on given cast type being null
        if (asType == null) throw new IllegalArgumentException("Error! Null Envision to Java translation type given!");
        
        // hard fail on null EnvisionObjects -- they should never be Java::NULL
        if (object == null) {
            throw new IllegalStateException("Error! Attempted to cast a Java::NULL EnvisionObject to a Java equivalent!");
        }
        
        final Object valueToCast;
        
        // translate Envision::NULL to Java::NULL
        if (object instanceof EnvisionNull) valueToCast = null;
        // translate Envision::VOID to Java::NULL
        else if (object instanceof EnvisionVoid) valueToCast = null;
        // translate EnvisionBoolean to Java::Boolean
        else if (object instanceof EnvisionBoolean b) valueToCast = b.bool_val;
        // translate EnvisionChar to Java::Character
        else if (object instanceof EnvisionChar c) valueToCast = c.char_val;
        // translate EnvisionString to Java::String
        else if (object instanceof EnvisionString s) valueToCast = s.string_val.toString();
        // translate EnvisionNumbers to their Java Number counterpart
        else if (object instanceof EnvisionNumber<?> n) valueToCast = translateEnvisionNumberToJava(n, asType);
        // lists require more work to breakdown and translate
        else if (object instanceof EnvisionList l) valueToCast = translateFromEnvisionList(l.getInternalList(), asType);
        // tuples require more work to breakdown and translate
        else if (object instanceof EnvisionTuple t) valueToCast = translateFromEnvisionList(t.getInternalList(), asType);
        // if the given object is somehow NONE of the already checked types, error out
        else {
            throw NativeMappingError.invalidJavaMapping(object, asType);
        }
        
        // return the straight up value if it's a primitive
        if (JavaDatatype.isPrimitiveClass(asType)) return valueToCast;
        
        // perform the cast and return
        return asType.cast(valueToCast);
    }
    
    //==================
    // List Translators
    //==================
    
    /**
     * Attempts to translate the given list of EnvisionObjects into some
     * type of valid target Java type.
     * 
     * @param list The list of EnvisionObject's to translate
     * @param asType The target Java type
     * 
     * @return An translated Java array/list/set built from the given EnvisionObject list
     */
    static Object translateFromEnvisionList(EList<EnvisionObject> list, Class<?> asType) {
        if (asType.isArray()) return translateFromEnvisionListToArray(list, asType);
        if (asType == Object.class) return translateFromEnvisionListToList(list, EList.class);
        if (List.class.isAssignableFrom(asType)) return translateFromEnvisionListToList(list, asType);
        if (Set.class.isAssignableFrom(asType)) return translateFromEnvisionListToSet(list, asType);
        
        throw new NativeMappingError("Error! Invalid list translation target: '" + asType + "'!");
    }
    
    /**
     * Attempts to translate the given EnvisionObject list into a Java
     * array.
     * 
     * @param list       The EnvisionObject list to translate
     * @param arrayClass The array type to create, populate, and return
     * 
     * @return A Java array containing mapped values from the given
     *         EnvisionObject list
     */
    static Object translateFromEnvisionListToArray(EList<EnvisionObject> list, Class<?> arrayClass) {
        final int size = list.size();
        
        final Object array;
        Class<?> baseObjectType = arrayClass.componentType();
        
        // Array Fun :)
        
        if (baseObjectType == boolean.class) {
            boolean[] arr = new boolean[size];
            for (int i = 0; i < size; ++i) {
                arr[i] = (boolean) convertToJavaObject(list.get(i), baseObjectType);
            }
            array = arr;
        }
        else if (baseObjectType == char.class) {
            char[] arr = new char[size];
            for (int i = 0; i < size; ++i) {
                arr[i] = (char) convertToJavaObject(list.get(i), baseObjectType);
            }
            array = arr;
        }
        else if (baseObjectType == byte.class) {
            byte[] arr = new byte[size];
            for (int i = 0; i < size; ++i) {
                arr[i] = (byte) convertToJavaObject(list.get(i), baseObjectType);
            }
            array = arr;
        }
        else if (baseObjectType == short.class) {
            short[] arr = new short[size];
            for (int i = 0; i < size; ++i) {
                arr[i] = (short) convertToJavaObject(list.get(i), baseObjectType);
            }
            array = arr;
        }
        else if (baseObjectType == int.class) {
            int[] arr = new int[size];
            for (int i = 0; i < size; ++i) {
                arr[i] = (int) convertToJavaObject(list.get(i), baseObjectType);
            }
            array = arr;
        }
        else if (baseObjectType == long.class) {
            long[] arr = new long[size];
            for (int i = 0; i < size; ++i) {
                arr[i] = (long) convertToJavaObject(list.get(i), baseObjectType);
            }
            array = arr;
        }
        else if (baseObjectType == float.class) {
            float[] arr = new float[size];
            for (int i = 0; i < size; ++i) {
                arr[i] = (float) convertToJavaObject(list.get(i), baseObjectType);
            }
            array = arr;
        }
        else if (baseObjectType == double.class) {
            double[] arr = new double[size];
            for (int i = 0; i < size; ++i) {
                arr[i] = (double) convertToJavaObject(list.get(i), baseObjectType);
            }
            array = arr;
        }
        else {
            Object arr = Array.newInstance(baseObjectType, size);
            for (int i = 0; i < size; ++i) {
                Array.set(arr, i, convertToJavaObject(list.get(i), baseObjectType));
            }
            array = arr;
        }
        
        return array;
    }
    
    /**
     * Attempts to translate the given EnvisionObject list into a Java List
     * of some type.
     * 
     * @param list      The EnvisionObject list to translate
     * @param listClass The list type to create, populate, and return
     * 
     * @return A Java List (of some type) containing mapped values from the
     *         given EnvisionObject list
     */
    static Object translateFromEnvisionListToList(EList<EnvisionObject> list, Class<?> listClass) {
        try {
            final int size = list.size();
            
            List<Object> javaList;
            
            if (listClass == ArrayList.class) javaList = new ArrayList<>(size);
            else if (listClass == List.class) javaList = new EArrayList<>(size);
            else if (listClass == EList.class) javaList = new EArrayList<>(size);
            else if (listClass == Vector.class) javaList = new Vector<>(size);
            else javaList = (List) listClass.getConstructor().newInstance();
            
            for (int i = 0; i < size; ++i) {
                EnvisionObject object = list.get(i);
                
                Object translated = convertToJavaObject(object, Object.class);
                javaList.add(translated);
            }
            
            return javaList;
        }
        catch (Exception e) {
            throw new NativeMappingError("Error! Cannot translate EnvisionObject into Java: '" + listClass + "'!");
        }
    }
    
    /**
     * Attempts to translate the given EnvisionObject list into a Java Set
     * of some type.
     * 
     * @param list     The EnvisionObject list to translate
     * @param setClass The set type to create, populate, and return
     * 
     * @return A Java Set (of some type) containing mapped values from the
     *         given EnvisionObject list
     */
    static Object translateFromEnvisionListToSet(EList<EnvisionObject> list, Class<?> setClass) {
        try {
            final int size = list.size();
            
            Set<Object> javaSet;
            
            if (setClass == HashSet.class) javaSet = new HashSet<>(size);
            else if (setClass == LinkedHashSet.class) javaSet = new LinkedHashSet<>(size); 
            else if (setClass == Set.class) javaSet = new HashSet<>(size);
            else javaSet = (Set) setClass.getConstructor().newInstance();
            
            for (int i = 0; i < size; ++i) {
                EnvisionObject object = list.get(i);
                
                Object translated = convertToJavaObject(object, Object.class);
                javaSet.add(translated);
            }
            
            return javaSet;
        }
        catch (Exception e) {
            throw new NativeMappingError("Error! Cannot translate EnvisionObject into Java: '" + setClass + "'!");
        }
    }
    
    //=====================
    // General Translators
    //=====================
    
    /**
     * Attempts to convert an EnvisionNumber to the given class type.
     * 
     * @param toConvert The EnvisionNumber to convert
     * @param toType The Java class type to convert to
     * @return A number 
     */
    static Object translateEnvisionNumberToJava(EnvisionNumber<?> toConvert, Class<?> toType) {
        Number n = toConvert.get_i();
        
        if (toType == Object.class || toType == Number.class) return n;
        if (toType == Byte.class || toType == byte.class) return n.byteValue();
        if (toType == Short.class || toType == short.class) return n.shortValue();
        if (toType == Integer.class || toType == int.class) return n.intValue();
        if (toType == Long.class || toType == long.class) return n.longValue();
        if (toType == Float.class || toType == float.class) return n.floatValue();
        if (toType == Double.class || toType == double.class) return n.doubleValue();
        
        throw new IllegalStateException("Error! Not possible! Converting: " + toConvert + " -> " + toType);
    }
    
    //=========
    // Getters
    //=========
    
    public boolean isField() { return isField; }
    public boolean isMethod() { return isMethod; }
    
    public Class<?> getJavaReturnType() { return javaReturnType; }
    public IDatatype getEnvisionReturnType() { return envisionReturnType; }
    
    /**
     * If this mapper wraps a Java Field, this method will return the
     * mapped Envision field type that the Java Field represents.
     * 
     * @return An Envision IDatatype
     */
    public IDatatype getEnvisionFieldType() {
        // throw error if this mapper does not actually possess any types to map to
        if (toEnvision.length == 0) throw NativeMappingError.noMappingError();
        // throw error if this mapper is not a field
        if (!isField) throw new NativeMappingError("Error! This mapper does not provide mappings for a field!");
        
        return toEnvision[0];
    }
    
    /**
     * If this mapper wraps a Java Field, this method will return the
     * native Java class type that the Java Field represents.
     * 
     * @return An Java class type
     */
    public Class<?> getJavaFieldType() {
        // throw error if this mapper does not actually possess any types to map to
        if (toJava.length == 0) throw NativeMappingError.noMappingError();
        // throw error if this mapper is not a field
        if (!isField) throw new NativeMappingError("Error! This mapper does not provide mappings for a field!");
        
        return toJava[0];
    }
    
    //=========================
    // Specialized Error Types
    //=========================
    
    /**
     * A specialized type of EnvisionLangError for native mapping errors
     * specifically.
     * 
     * @author Hunter Bragg
     */
    public static class NativeMappingError extends EnvisionLangError {

        /**
         * Customizable message constructor.
         * 
         * @param message Custom message
         */
        public NativeMappingError(String message) {
            super(message);
        }
        
        /**
         * Customizable message constructor with overhead error.
         * 
         * @param message Custom message
         * @param error Overhead error that was thrown
         */
        public NativeMappingError(String message, Throwable error) {
            super(message, error);
        }
        
        /**
         * Creates a new invalid mapping error stating that there are no
         * valid mappings available.
         * 
         * @return A new NativeMappingError
         */
        public static NativeMappingError noMappingError() {
            return new NativeMappingError("Error! This mapper does not contain any mapping types!");
        }
        
        /**
         * Creates a new invalid mapping error stating that the given number
         * of arguments to map does not match the expected amount.
         * 
         * @return A new NativeMappingError
         */
        public static NativeMappingError invalidAmount(int expected, int got) {
            return new NativeMappingError(String.format("Error! Expected '%d' argument(s), but got '%d'", expected, got));
        }
        
        /**
         * Creates a new invalid mapping error stating that the given Envision Object
         * cannot be translated to the expected Java type.
         * 
         * @return A new NativeMappingError
         */
        public static NativeMappingError invalidJavaMapping(EnvisionObject object, Class<?> expected) {
            return new NativeMappingError("Error! Failed to map Envision Object: '" + object + "' to Java: '" + expected + "'");
        }
        
        /**
         * Creates a new invalid mapping error stating that the given Envision Object
         * cannot be translated to the expected Java type.
         * 
         * @return A new NativeMappingError
         */
        public static NativeMappingError invalidJavaMapping(EnvisionObject object, Class<?> expected, Throwable error) {
            return new NativeMappingError("Error! Failed to map Envision Object: '" + object + "' to Java: '" + expected + "'", error);
        }
        
        /**
         * Creates a new invalid mapping error stating that the given Java Object
         * cannot be translated to the expected Envision type.
         * 
         * @return A new NativeMappingError
         */
        public static NativeMappingError invalidEnvisionMapping(Object object, IDatatype expected) {
            return new NativeMappingError("Error! Failed to map Java Object: '" + object + "' to Envision: '" + expected + "'");
        }
        
        /**
         * Creates a new invalid mapping error stating that the given Java Object
         * cannot be translated to the expected Envision type.
         * 
         * @return A new NativeMappingError
         */
        public static NativeMappingError invalidEnvisionMapping(Object object, IDatatype expected, Throwable error) {
            return new NativeMappingError("Error! Failed to map Java Object: '" + object + "' to Envision: '" + expected + "'", error);
        }
    }
    
}
