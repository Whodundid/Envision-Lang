package envision_lang.lang.java;

import static envision_lang.lang.natives.EnvisionStaticTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import envision_lang.lang.natives.IDatatype;
import eutil.datatypes.util.EList;

class NativeDatatypeMapperInitTests extends NativeDatatypeMapperTests {
    
    //========
    // Fields
    //========
    
    @Test
    void init_field_null() {
        assertThrows(NullPointerException.class, () -> new NativeDatatypeMapper((Field) null));
    }
    
    @Test
    void init_field_boolean() {
        checkField("field_boolean", boolean.class, BOOL_TYPE);
        checkField("field_Boolean", Boolean.class, BOOL_TYPE);
    }
    
    @Test
    void init_field_char() {
        checkField("field_char", char.class, CHAR_TYPE);
        checkField("field_Character", Character.class, CHAR_TYPE);
    }
    
    @Test
    void init_field_byte() {
        checkField("field_byte", byte.class, INT_TYPE);
        checkField("field_Byte", Byte.class, INT_TYPE);
    }
    
    @Test
    void init_field_short() {
        checkField("field_short", short.class, INT_TYPE);
        checkField("field_Short", Short.class, INT_TYPE);
    }
    
    @Test
    void init_field_int() {
        checkField("field_int", int.class, INT_TYPE);
        checkField("field_Integer", Integer.class, INT_TYPE);
    }
    
    @Test
    void init_field_long() {
        checkField("field_long", long.class, INT_TYPE);
        checkField("field_Long", Long.class, INT_TYPE);
    }
    
    @Test
    void init_field_float() {
        checkField("field_float", float.class, DOUBLE_TYPE);
        checkField("field_Float", Float.class, DOUBLE_TYPE);
    }
    
    @Test
    void init_field_double() {
        checkField("field_double", double.class, DOUBLE_TYPE);
        checkField("field_Double", Double.class, DOUBLE_TYPE);
    }
    
    @Test
    void init_field_String() {
        checkField("field_String", String.class, STRING_TYPE);
    }
    
    @Test
    void init_field_Object() {
        checkField("field_Object", Object.class, VAR_TYPE);
    }
    
    @Test
    void init_field_List() {
        checkField("field_ListLong", List.class, LIST_TYPE);
        checkField("field_EListLong", EList.class, LIST_TYPE);
        checkField("field_SetLong", Set.class, LIST_TYPE);
    }
    
    @Test
    void init_field_arrays() {
        checkField("field_booleanArray", boolean[].class, LIST_TYPE);
        checkField("field_charArray", char[].class, LIST_TYPE);
        checkField("field_byteArray", byte[].class, LIST_TYPE);
        checkField("field_shortArray", short[].class, LIST_TYPE);
        checkField("field_intArray", int[].class, LIST_TYPE);
        checkField("field_longArray", long[].class, LIST_TYPE);
        checkField("field_floatArray", float[].class, LIST_TYPE);
        checkField("field_doubleArray", double[].class, LIST_TYPE);
        
        checkField("field_BooleanArray", Boolean[].class, LIST_TYPE);
        checkField("field_CharacterArray", Character[].class, LIST_TYPE);
        checkField("field_ByteArray", Byte[].class, LIST_TYPE);
        checkField("field_ShortArray", Short[].class, LIST_TYPE);
        checkField("field_IntegerArray", Integer[].class, LIST_TYPE);
        checkField("field_LongArray", Long[].class, LIST_TYPE);
        checkField("field_FloatArray", Float[].class, LIST_TYPE);
        checkField("field_DoubleArray", Double[].class, LIST_TYPE);
        
        checkField("field_StringArray", String[].class, LIST_TYPE);
        checkField("field_ObjectArray", Object[].class, LIST_TYPE);
        
        checkField("field_2DArray", long[][].class, LIST_TYPE);
        checkField("field_3DArray", long[][][].class, LIST_TYPE);
    }
    
    //===================
    // Method Parameters
    //===================
    
    @Test
    void init_params_null() {
        assertThrows(NullPointerException.class, () -> new NativeDatatypeMapper((Parameter[]) null));
    }
    
    @Test
    void init_params_noParams() {
        checkMethod(fromMethod("method_noParams"), EList.of(), EList.of());
    }
    
    @Test
    void init_params_one() {
        checkMethod("method_boolean", boolean.class, BOOL_TYPE);
        checkMethod("method_char", char.class, CHAR_TYPE);
        checkMethod("method_byte", byte.class, INT_TYPE);
        checkMethod("method_short", short.class, INT_TYPE);
        checkMethod("method_int", int.class, INT_TYPE);
        checkMethod("method_long", long.class, INT_TYPE);
        checkMethod("method_float", float.class, DOUBLE_TYPE);
        checkMethod("method_double", double.class, DOUBLE_TYPE);
        
        checkMethod("method_Boolean", Boolean.class, BOOL_TYPE);
        checkMethod("method_Character", Character.class, CHAR_TYPE);
        checkMethod("method_Byte", Byte.class, INT_TYPE);
        checkMethod("method_Short", Short.class, INT_TYPE);
        checkMethod("method_Integer", Integer.class, INT_TYPE);
        checkMethod("method_Long", Long.class, INT_TYPE);
        checkMethod("method_Float", Float.class, DOUBLE_TYPE);
        checkMethod("method_Double", Double.class, DOUBLE_TYPE);
        
        checkMethod("method_String", String.class, STRING_TYPE);
        checkMethod("method_Object", Object.class, VAR_TYPE);
        
        checkMethod("method_List", List.class, LIST_TYPE);
        checkMethod("method_ListLong", List.class, LIST_TYPE);
        checkMethod("method_Set", Set.class, LIST_TYPE);
        checkMethod("method_SetLong", Set.class, LIST_TYPE);
    }
    
    @Test
    void init_params_arrays() {
        checkMethod("method_booleanArray", boolean[].class, LIST_TYPE);
        checkMethod("method_charArray", char[].class, LIST_TYPE);
        checkMethod("method_byteArray", byte[].class, LIST_TYPE);
        checkMethod("method_shortArray", short[].class, LIST_TYPE);
        checkMethod("method_intArray", int[].class, LIST_TYPE);
        checkMethod("method_longArray", long[].class, LIST_TYPE);
        checkMethod("method_floatArray", float[].class, LIST_TYPE);
        checkMethod("method_doubleArray", double[].class, LIST_TYPE);
        
        checkMethod("method_BooleanArray", Boolean[].class, LIST_TYPE);
        checkMethod("method_CharacterArray", Character[].class, LIST_TYPE);
        checkMethod("method_ByteArray", Byte[].class, LIST_TYPE);
        checkMethod("method_ShortArray", Short[].class, LIST_TYPE);
        checkMethod("method_IntegerArray", Integer[].class, LIST_TYPE);
        checkMethod("method_LongArray", Long[].class, LIST_TYPE);
        checkMethod("method_FloatArray", Float[].class, LIST_TYPE);
        checkMethod("method_DoubleArray", Double[].class, LIST_TYPE);
        
        checkMethod("method_StringArray", String[].class, LIST_TYPE);
        checkMethod("method_ObjectArray", Object[].class, LIST_TYPE);
    }
    
    @Test
    void init_params_primitives() {
        EList<Class<?>> primitives = EList.of(boolean.class, char.class, byte.class, short.class, int.class, long.class, float.class, double.class);
        EList<IDatatype> envisions = EList.of(BOOL_TYPE, CHAR_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, DOUBLE_TYPE, DOUBLE_TYPE);
        checkMethod("method_primitives", primitives, envisions);
    }
    
    @Test
    void init_params_Objects() {
        EList<Class<?>> primitives = EList.of(Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class);
        EList<IDatatype> envisions = EList.of(BOOL_TYPE, CHAR_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, DOUBLE_TYPE, DOUBLE_TYPE);
        checkMethod("method_Objects", primitives, envisions);
    }
    
    @Test
    void init_params_mixed() {
        EList<Class<?>> primitives = EList.of(boolean.class, Character.class, byte.class, Short.class, int.class, Long.class, float.class, Double.class);
        EList<IDatatype> envisions = EList.of(BOOL_TYPE, CHAR_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, DOUBLE_TYPE, DOUBLE_TYPE);
        checkMethod("method_mixed", primitives, envisions);
    }
    
    //=======================
    // Static Helper Methods
    //=======================
    
    private void checkField(String fieldName, Class<?> expectedJavaClass, IDatatype expectedEnvisionType) {
        checkField(fromField(fieldName), expectedJavaClass, expectedEnvisionType);
    }
    private static void checkField(NativeDatatypeMapper mapper, Class<?> expectedJavaClass, IDatatype expectedEnvisionType) {
        assertNotNull(mapper);
        assertNotNull(mapper.toEnvision);
        assertNotNull(mapper.toJava);
        assertEquals(1, mapper.toEnvision.length);
        assertEquals(1, mapper.toJava.length);
        
        assertEquals(expectedJavaClass, mapper.toJava[0]);
        assertEquals(expectedEnvisionType, mapper.toEnvision[0]);
    }
    
    private void checkMethod(String methodName, Class<?> expectedJavaClass, IDatatype expectedEnvisionType) {
        checkMethod(fromMethod(methodName), EList.of(expectedJavaClass), EList.of(expectedEnvisionType));
    }
    private void checkMethod(String methodName, EList<Class<?>> expectedJavaClasses, EList<IDatatype> expectedEnvisionTypes) {
        checkMethod(fromMethod(methodName), expectedJavaClasses, expectedEnvisionTypes);
    }
    private static void checkMethod(NativeDatatypeMapper mapper, Class<?> expectedJavaClass, IDatatype expectedEnvisionType) {
        checkMethod(mapper, EList.of(expectedJavaClass), EList.of(expectedEnvisionType));
    }
    private static void checkMethod(NativeDatatypeMapper mapper, EList<Class<?>> expectedJavaClasses, EList<IDatatype> expectedEnvisionTypes) {
        final int size = expectedJavaClasses.size();
        assertEquals(size, expectedEnvisionTypes.size());
        
        assertNotNull(mapper);
        assertNotNull(mapper.toEnvision);
        assertNotNull(mapper.toJava);
        assertEquals(size, mapper.toEnvision.length);
        assertEquals(size, mapper.toJava.length);
        
        for (int i = 0; i < size; i++) {
            Class<?> expectedJavaClass = expectedJavaClasses.get(i);
            IDatatype expectedEnvisionType = expectedEnvisionTypes.get(i);
            
            assertEquals(expectedJavaClass, mapper.toJava[i]);
            assertEquals(expectedEnvisionType, mapper.toEnvision[i]);
        }
    }
    
}
