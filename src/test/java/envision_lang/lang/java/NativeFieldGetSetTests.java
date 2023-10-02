package envision_lang.lang.java;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionBoolean;
import envision_lang.lang.datatypes.EnvisionBooleanClass;
import envision_lang.lang.datatypes.EnvisionChar;
import envision_lang.lang.datatypes.EnvisionCharClass;
import envision_lang.lang.datatypes.EnvisionDouble;
import envision_lang.lang.datatypes.EnvisionDoubleClass;
import envision_lang.lang.datatypes.EnvisionInt;
import envision_lang.lang.datatypes.EnvisionIntClass;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionString;
import envision_lang.lang.datatypes.EnvisionStringClass;
import envision_lang.lang.natives.NativeTypeManager;

class NativeFieldGetSetTests extends NativeFieldTests {
    
    @BeforeEach
    void setupNativeTypes() {
        NativeTypeManager.init();
    }
    
    @Test
    void test_set_nonArray() {
        checkSetJava("field_boolean", true, true, env_bool(true));
        checkSetJava("field_char", 'c', 'c', env_char('c'));
        checkSetJava("field_byte", (byte) 1, (byte) 1, env_int((byte) 1));
        checkSetJava("field_short", (short) 2, (short) 2, env_int((short) 2));
        checkSetJava("field_int", (int) 3, (int) 3, env_int((int) 3));
        checkSetJava("field_long", (long) 4, (long) 4, env_int((long) 4));
        checkSetJava("field_float", (float) 5, (float) 5, env_double((float) 5));
        checkSetJava("field_double", (double) 6, (double) 6, env_double((double) 6));
        
        checkSetEnvision("field_boolean", env_bool(true), true, env_bool(true));
        checkSetEnvision("field_char", env_char('c'), 'c', env_char('c'));
        checkSetEnvision("field_byte", env_int((byte) 1), (byte) 1, env_int((byte) 1));
        checkSetEnvision("field_short", env_int((short) 2), (short) 2, env_int((short) 2));
        checkSetEnvision("field_int", env_int((int) 3), (int) 3, env_int((int) 3));
        checkSetEnvision("field_long", env_int((long) 4), (long) 4, env_int((long) 4));
        checkSetEnvision("field_float", env_double((float) 5), (float) 5, env_double((float) 5));
        checkSetEnvision("field_double", env_double((double) 6), (double) 6, env_double((double) 6));
        
        checkSetJava("field_Boolean", true, true, env_bool(true));
        checkSetJava("field_Character", 'c', 'c', env_char('c'));
        checkSetJava("field_Byte", (byte) 1, (byte) 1, env_int((byte) 1));
        checkSetJava("field_Short", (short) 2, (short) 2, env_int((short) 2));
        checkSetJava("field_Integer", (int) 3, (int) 3, env_int((int) 3));
        checkSetJava("field_Long", (long) 4, (long) 4, env_int((long) 4));
        checkSetJava("field_Float", (float) 5, (float) 5, env_double((float) 5));
        checkSetJava("field_Double", (double) 6, (double) 6, env_double((double) 6));
        
        checkSetEnvision("field_Boolean", env_bool(true), true, env_bool(true));
        checkSetEnvision("field_Character", env_char('c'), 'c', env_char('c'));
        checkSetEnvision("field_Byte", env_int((byte) 1), (byte) 1, env_int((byte) 1));
        checkSetEnvision("field_Short", env_int((short) 2), (short) 2, env_int((short) 2));
        checkSetEnvision("field_Integer", env_int((int) 3), (int) 3, env_int((int) 3));
        checkSetEnvision("field_Long", env_int((long) 4), (long) 4, env_int((long) 4));
        checkSetEnvision("field_Float", env_double((float) 5), (float) 5, env_double((float) 5));
        checkSetEnvision("field_Double", env_double((double) 6), (double) 6, env_double((double) 6));
        
        checkSetJava("field_String", "Banana", "Banana", env_str("Banana"));
        checkSetEnvision("field_String", env_str("Banana"), "Banana", env_str("Banana"));
    }
    
    @Test
    void test_set_object() {
        checkSetJava("field_Object", true, true, env_bool(true));
        checkSetJava("field_Object", 'c', 'c', env_char('c'));
        checkSetJava("field_Object", (byte) 1, (byte) 1, env_int((byte) 1));
        checkSetJava("field_Object", (short) 2, (short) 2, env_int((short) 2));
        checkSetJava("field_Object", (int) 3, (int) 3, env_int((int) 3));
        checkSetJava("field_Object", (long) 4, (long) 4, env_int((long) 4));
        checkSetJava("field_Object", (float) 5, (float) 5, env_double((float) 5));
        checkSetJava("field_Object", (double) 6, (double) 6, env_double((double) 6));
        
        checkSetEnvision("field_Object", env_bool(true), true, env_bool(true));
        checkSetEnvision("field_Object", env_char('c'), 'c', env_char('c'));
        checkSetEnvision("field_Object", env_int((byte) 1), 1L, env_int((byte) 1));
        checkSetEnvision("field_Object", env_int((short) 2), 2L, env_int((short) 2));
        checkSetEnvision("field_Object", env_int((int) 3), 3L, env_int((int) 3));
        checkSetEnvision("field_Object", env_int((long) 4), 4L, env_int((long) 4));
        checkSetEnvision("field_Object", env_double((float) 5), 5D, env_double((float) 5));
        checkSetEnvision("field_Object", env_double((double) 6), 6D, env_double((double) 6));
        
        checkSetJava("field_Object", "Banana", "Banana", env_str("Banana"));
        checkSetEnvision("field_Object", env_str("Banana"), "Banana", env_str("Banana"));
    }

    @Test
    void test_set_arrays() {
        boolean[] bool_array = arrayOf(true, false);
        char[] char_array = arrayOf('a', 'b');
        byte[] byte_array = arrayOf((byte) 1, (byte) 2);
        short[] short_array = arrayOf((short) 1, (short) 2);
        int[] int_array = arrayOf((int) 1, (int) 2);
        long[] long_array = arrayOf((long) 1, (long) 2);
        float[] float_array = arrayOf((float) 1, (float) 2);
        double[] double_array = arrayOf((double) 1, (double) 2);
        String[] string_array = arrayOf("Big", "Chungus");
        
        checkSetJavaArray("field_booleanArray", bool_array, bool_array, envisionListOf(true, false));
        checkSetJavaArray("field_charArray", char_array, char_array, envisionListOf('a', 'b'));
        checkSetJavaArray("field_byteArray", byte_array, byte_array, envisionListOf(1, 2));
        checkSetJavaArray("field_shortArray", short_array, short_array, envisionListOf(1, 2));
        checkSetJavaArray("field_intArray", int_array, int_array, envisionListOf(1, 2));
        checkSetJavaArray("field_longArray", long_array, long_array, envisionListOf(1, 2));
        checkSetJavaArray("field_floatArray", float_array, float_array, envisionListOf(1.0, 2.0));
        checkSetJavaArray("field_doubleArray", double_array, double_array, envisionListOf(1.0, 2.0));
        checkSetJavaArray("field_StringArray", string_array, string_array, envisionListOf("Big", "Chungus"));
    }
    
    //=========================
    // Internal Helper Methods
    //=========================
    
    private static EnvisionBoolean env_bool(boolean val) { return EnvisionBooleanClass.valueOf(val); }
    private static EnvisionChar env_char(char val) { return EnvisionCharClass.valueOf(val); }
    private static EnvisionInt env_int(Number val) { return EnvisionIntClass.valueOf(val); }
    private static EnvisionDouble env_double(Number val) { return EnvisionDoubleClass.valueOf(val); }
    private static EnvisionString env_str(String val) { return EnvisionStringClass.valueOf(val); }
    
    private void checkSetJava(String fieldName, Object valueToSet, Object expectedValue, EnvisionObject expectedEnvision) {
        NativeField f = field(fieldName);
        assertNotNull(f);
        f.setFieldValue(valueToSet);
        Object javaObject = f.getFieldValue_Java();
        EnvisionObject envisionObject = f.getFieldValue_Envision();
        assertEquals(expectedValue, javaObject);
        assertEquals(expectedEnvision, envisionObject);
    }
    
    private void checkSetEnvision(String fieldName, EnvisionObject valueToSet, Object expectedValue, EnvisionObject expectedEnvision) {
        NativeField f = field(fieldName);
        assertNotNull(f);
        f.setFieldValue(valueToSet);
        Object javaObject = f.getFieldValue_Java();
        EnvisionObject envisionObject = f.getFieldValue_Envision();
        assertEquals(expectedValue, javaObject);
        assertEquals(expectedEnvision, envisionObject);
    }
    
    private void checkSetJavaArray(String fieldName, boolean[] toSet, boolean[] expectedArray, EnvisionList expectedEnvision) {
        NativeField f = field(fieldName);
        assertNotNull(f);
        f.setFieldValue(toSet);
        Object javaObject = f.getFieldValue_Java();
        EnvisionObject envisionObject = f.getFieldValue_Envision();
        assertNotNull(javaObject);
        assertEquals(expectedArray.getClass(), javaObject.getClass());
        boolean[] array = (boolean[]) javaObject;
        for (int i = 0; i < array.length; i++) {
            boolean expected = expectedArray[i];
            boolean got = array[i];
            assertEquals(expected, got);
        }
        checkEnvisionList(envisionObject, expectedEnvision);
    }
    
    private void checkSetJavaArray(String fieldName, char[] toSet, char[] expectedArray, EnvisionList expectedEnvision) {
        NativeField f = field(fieldName);
        assertNotNull(f);
        f.setFieldValue(toSet);
        Object javaObject = f.getFieldValue_Java();
        EnvisionObject envisionObject = f.getFieldValue_Envision();
        assertNotNull(javaObject);
        assertEquals(expectedArray.getClass(), javaObject.getClass());
        char[] array = (char[]) javaObject;
        for (int i = 0; i < array.length; i++) {
            char expected = expectedArray[i];
            char got = array[i];
            assertEquals(expected, got);
        }
        checkEnvisionList(envisionObject, expectedEnvision);
    }
    
    private void checkSetJavaArray(String fieldName, byte[] toSet, byte[] expectedArray, EnvisionList expectedEnvision) {
        NativeField f = field(fieldName);
        assertNotNull(f);
        f.setFieldValue(toSet);
        Object javaObject = f.getFieldValue_Java();
        EnvisionObject envisionObject = f.getFieldValue_Envision();
        assertNotNull(javaObject);
        assertEquals(expectedArray.getClass(), javaObject.getClass());
        byte[] array = (byte[]) javaObject;
        for (int i = 0; i < array.length; i++) {
            byte expected = expectedArray[i];
            byte got = array[i];
            assertEquals(expected, got);
        }
        checkEnvisionList(envisionObject, expectedEnvision);
    }
    
    private void checkSetJavaArray(String fieldName, short[] toSet, short[] expectedArray, EnvisionList expectedEnvision) {
        NativeField f = field(fieldName);
        assertNotNull(f);
        f.setFieldValue(toSet);
        Object javaObject = f.getFieldValue_Java();
        EnvisionObject envisionObject = f.getFieldValue_Envision();
        assertNotNull(javaObject);
        assertEquals(expectedArray.getClass(), javaObject.getClass());
        short[] array = (short[]) javaObject;
        for (int i = 0; i < array.length; i++) {
            short expected = expectedArray[i];
            short got = array[i];
            assertEquals(expected, got);
        }
        checkEnvisionList(envisionObject, expectedEnvision);
    }
    
    private void checkSetJavaArray(String fieldName, int[] toSet, int[] expectedArray, EnvisionList expectedEnvision) {
        NativeField f = field(fieldName);
        assertNotNull(f);
        f.setFieldValue(toSet);
        Object javaObject = f.getFieldValue_Java();
        EnvisionObject envisionObject = f.getFieldValue_Envision();
        assertNotNull(javaObject);
        assertEquals(expectedArray.getClass(), javaObject.getClass());
        int[] array = (int[]) javaObject;
        for (int i = 0; i < array.length; i++) {
            int expected = expectedArray[i];
            int got = array[i];
            assertEquals(expected, got);
        }
        checkEnvisionList(envisionObject, expectedEnvision);
    }
    
    private void checkSetJavaArray(String fieldName, long[] toSet, long[] expectedArray, EnvisionList expectedEnvision) {
        NativeField f = field(fieldName);
        assertNotNull(f);
        f.setFieldValue(toSet);
        Object javaObject = f.getFieldValue_Java();
        EnvisionObject envisionObject = f.getFieldValue_Envision();
        assertNotNull(javaObject);
        assertEquals(expectedArray.getClass(), javaObject.getClass());
        long[] array = (long[]) javaObject;
        for (int i = 0; i < array.length; i++) {
            long expected = expectedArray[i];
            long got = array[i];
            assertEquals(expected, got);
        }
        checkEnvisionList(envisionObject, expectedEnvision);
    }
    
    private void checkSetJavaArray(String fieldName, float[] toSet, float[] expectedArray, EnvisionList expectedEnvision) {
        NativeField f = field(fieldName);
        assertNotNull(f);
        f.setFieldValue(toSet);
        Object javaObject = f.getFieldValue_Java();
        EnvisionObject envisionObject = f.getFieldValue_Envision();
        assertNotNull(javaObject);
        assertEquals(expectedArray.getClass(), javaObject.getClass());
        float[] array = (float[]) javaObject;
        for (int i = 0; i < array.length; i++) {
            float expected = expectedArray[i];
            float got = array[i];
            assertEquals(expected, got);
        }
        checkEnvisionList(envisionObject, expectedEnvision);
    }
    
    private void checkSetJavaArray(String fieldName, double[] toSet, double[] expectedArray, EnvisionList expectedEnvision) {
        NativeField f = field(fieldName);
        assertNotNull(f);
        f.setFieldValue(toSet);
        Object javaObject = f.getFieldValue_Java();
        EnvisionObject envisionObject = f.getFieldValue_Envision();
        assertNotNull(javaObject);
        assertEquals(expectedArray.getClass(), javaObject.getClass());
        double[] array = (double[]) javaObject;
        for (int i = 0; i < array.length; i++) {
            double expected = expectedArray[i];
            double got = array[i];
            assertEquals(expected, got);
        }
        checkEnvisionList(envisionObject, expectedEnvision);
    }
    
    private void checkSetJavaArray(String fieldName, Object[] toSet, Object[] expectedArray, EnvisionList expectedEnvision) {
        NativeField f = field(fieldName);
        assertNotNull(f);
        f.setFieldValue(toSet);
        Object javaObject = f.getFieldValue_Java();
        EnvisionObject envisionObject = f.getFieldValue_Envision();
        assertNotNull(javaObject);
        assertEquals(expectedArray.getClass(), javaObject.getClass());
        Object[] array = (Object[]) javaObject;
        for (int i = 0; i < array.length; i++) {
            Object expected = expectedArray[i];
            Object got = array[i];
            assertEquals(expected, got);
        }
        checkEnvisionList(envisionObject, expectedEnvision);
    }
    
    private void checkJavaList(Object obj, List<?> expectedList) {
        assertNotNull(obj);
        assertEquals(expectedList.getClass(), obj.getClass());
        List<?> list = (List<?>) obj;
        assertEquals(expectedList.size(), list.size());
        for (int i = 0; i < expectedList.size(); i++) {
            Object expected = expectedList.get(i);
            Object got = list.get(i);
            assertEquals(expected, got);
        }
    }
    
    private void checkEnvisionList(EnvisionObject obj, EnvisionList expectedList) {
        assertNotNull(obj);
        assertEquals(expectedList.getClass(), obj.getClass());
        EnvisionList list = (EnvisionList) obj;
        assertEquals(expectedList.size(), list.size());
        for (int i = 0; i < (int) expectedList.size_i(); i++) {
            EnvisionObject expected = expectedList.get(i);
            EnvisionObject got = list.get(i);
            assertEquals(expected, got);
        }
    }
    
}
