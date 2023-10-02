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
import envision_lang.lang.datatypes.EnvisionListClass;
import envision_lang.lang.datatypes.EnvisionString;
import envision_lang.lang.datatypes.EnvisionStringClass;
import envision_lang.lang.java.NativeDatatypeMapper.NativeMappingError;
import envision_lang.lang.natives.NativeTypeManager;
import eutil.datatypes.util.EList;

class NativeDatatypeMapperTranslateTests extends NativeDatatypeMapperTests {
    
    @BeforeEach
    void setup() {
        NativeTypeManager.init();
    }
    
    //======================
    // Translation Failures
    //======================
    
    @Test
    void translate_fail_noMapping() {
        var mapper = fromMethod("method_noParams");
        NativeMappingError e = assertThrows(NativeMappingError.class, () -> mapper.mapToEnvision(false));
        assertEquals("Error! This mapper does not contain any mapping types!", e.getMessage());
    }
    
    @Test
    void translate_fail_field_incorrectAmount() {
        var mapper = fromField("field_boolean");
        NativeMappingError e = assertThrows(NativeMappingError.class, () -> mapper.mapToEnvision(new Boolean[] {false, false}));
        assertEquals("Error! Expected '1' argument(s), but got '2'", e.getMessage());
    }
    
    @Test
    void translate_fail_method_incorrectAmount() {
        var mapper = fromMethod("method_boolean");
        NativeMappingError e = assertThrows(NativeMappingError.class, () -> mapper.mapToEnvision(new Boolean[] {false, false}));
        assertEquals("Error! Expected '1' argument(s), but got '2'", e.getMessage());
    }
    
    @Test
    void translate_fail_field_invalidMapping() {
        var mapper = fromField("field_boolean");
        NativeMappingError e = assertThrows(NativeMappingError.class, () -> mapper.mapToEnvision(5));
        assertEquals("Error! Failed to map Java Object: '5' to Envision: 'BOOLEAN'", e.getMessage());
        assertNotNull(e.getCause());
        assertEquals("class java.lang.Integer cannot be cast to class java.lang.Boolean (java.lang.Integer and java.lang.Boolean are in module java.base of loader 'bootstrap')", e.getCause().getMessage());
    }
    
    @Test
    void translate_fail_method_invalidMapping() {
        var mapper = fromMethod("method_boolean");
        NativeMappingError e = assertThrows(NativeMappingError.class, () -> mapper.mapToEnvision(5));
        assertEquals("Error! Failed to map Java Object: '5' to Envision: 'BOOLEAN'", e.getMessage());
        assertNotNull(e.getCause());
        assertEquals("class java.lang.Integer cannot be cast to class java.lang.Boolean (java.lang.Integer and java.lang.Boolean are in module java.base of loader 'bootstrap')", e.getCause().getMessage());
    }
    
    //=============
    // Field Tests
    //=============
    
    @Test
    void translate_field_boolean() {
        checkEnvisionBoolean(fromField("field_boolean").mapToEnvision(false), false);
        checkEnvisionBoolean(fromField("field_boolean").mapToEnvision(true), true);
        checkEnvisionBoolean(fromField("field_Boolean").mapToEnvision(false), false);
        checkEnvisionBoolean(fromField("field_Boolean").mapToEnvision(true), true);
        checkJavaBoolean(fromField("field_boolean").mapToJava(EnvisionBooleanClass.valueOf(false)), false);
        checkJavaBoolean(fromField("field_boolean").mapToJava(EnvisionBooleanClass.valueOf(true)), true);
        checkJavaBoolean(fromField("field_Boolean").mapToJava(EnvisionBooleanClass.valueOf(false)), false);
        checkJavaBoolean(fromField("field_Boolean").mapToJava(EnvisionBooleanClass.valueOf(true)), true);
    }
    
    @Test
    void translate_field_char() {
        checkEnvisionChar(fromField("field_char").mapToEnvision('c'), 'c');
        checkEnvisionChar(fromField("field_Character").mapToEnvision('c'), 'c');
        checkJavaChar(fromField("field_char").mapToJava(EnvisionCharClass.valueOf('c')), 'c');
        checkJavaChar(fromField("field_Character").mapToJava(EnvisionCharClass.valueOf('c')), 'c');
    }
    
    @Test
    void translate_field_byte() {
        checkEnvisionInt(fromField("field_byte").mapToEnvision((byte) 5), (byte) 5);
        checkEnvisionInt(fromField("field_Byte").mapToEnvision((byte) 5), (byte) 5);
        checkJavaByte(fromField("field_byte").mapToJava(EnvisionIntClass.valueOf((byte) 5)), (byte) 5);
        checkJavaByte(fromField("field_Byte").mapToJava(EnvisionIntClass.valueOf((byte) 5)), (byte) 5);
    }
    
    @Test
    void translate_field_short() {
        checkEnvisionInt(fromField("field_short").mapToEnvision((short) 5), (short) 5);
        checkEnvisionInt(fromField("field_Short").mapToEnvision((short) 5), (short) 5);
        checkJavaShort(fromField("field_short").mapToJava(EnvisionIntClass.valueOf((short) 5)), (short) 5);
        checkJavaShort(fromField("field_Short").mapToJava(EnvisionIntClass.valueOf((short) 5)), (short) 5);
    }
    
    @Test
    void translate_field_int() {
        checkEnvisionInt(fromField("field_int").mapToEnvision(5), 5);
        checkEnvisionInt(fromField("field_Integer").mapToEnvision(5), 5);
        checkJavaInt(fromField("field_int").mapToJava(EnvisionIntClass.valueOf(5)), 5);
        checkJavaInt(fromField("field_Integer").mapToJava(EnvisionIntClass.valueOf(5)), 5);
    }
    
    @Test
    void translate_field_long() {
        checkEnvisionInt(fromField("field_long").mapToEnvision(5L), 5L);
        checkEnvisionInt(fromField("field_Long").mapToEnvision(5L), 5L);
        checkJavaLong(fromField("field_long").mapToJava(EnvisionIntClass.valueOf(5L)), 5L);
        checkJavaLong(fromField("field_Long").mapToJava(EnvisionIntClass.valueOf(5L)), 5L);
    }
    
    @Test
    void translate_field_float() {
        checkEnvisionDouble(fromField("field_float").mapToEnvision(5F), 5F);
        checkEnvisionDouble(fromField("field_Float").mapToEnvision(5F), 5F);
        checkJavaFloat(fromField("field_float").mapToJava(EnvisionDoubleClass.valueOf(5F)), 5F);
        checkJavaFloat(fromField("field_Float").mapToJava(EnvisionDoubleClass.valueOf(5F)), 5F);
    }
    
    @Test
    void translate_field_double() {
        checkEnvisionDouble(fromField("field_double").mapToEnvision(5D), 5D);
        checkEnvisionDouble(fromField("field_Double").mapToEnvision(5D), 5D);
        checkJavaDouble(fromField("field_double").mapToJava(EnvisionDoubleClass.valueOf(5D)), 5D);
        checkJavaDouble(fromField("field_Double").mapToJava(EnvisionDoubleClass.valueOf(5D)), 5D);
    }
    
    @Test
    void translate_field_String() {
        checkEnvisionString(fromField("field_String").mapToEnvision("Banana"), "Banana");
        checkJavaString(fromField("field_String").mapToJava(EnvisionStringClass.valueOf("Banana")), "Banana");
    }
    
    /**
     * Object/Var could realistically be anything so make sure that any type is translatable.
     */
    @Test
    void translate_field_Object() {
        checkEnvisionBoolean(fromField("field_Object").mapToEnvision(true), true);
        checkEnvisionChar(fromField("field_Object").mapToEnvision('c'), 'c');
        checkEnvisionInt(fromField("field_Object").mapToEnvision(5), 5);
        checkEnvisionDouble(fromField("field_Object").mapToEnvision(6.0), 6.0);
        checkEnvisionString(fromField("field_Object").mapToEnvision("Banana"), "Banana");
        checkEnvisionList(fromField("field_Object").mapToEnvision(longListOf(10, 20, 30, 40, 50)), envisionListOf(10, 20, 30, 40, 50));
        
        checkJavaBoolean(fromField("field_Object").mapToJava(EnvisionBooleanClass.valueOf(true)), true);
        checkJavaChar(fromField("field_Object").mapToJava(EnvisionCharClass.valueOf('c')), 'c');
        checkJavaLong(fromField("field_Object").mapToJava(EnvisionIntClass.valueOf((byte) 1)), (byte) 1);
        checkJavaLong(fromField("field_Object").mapToJava(EnvisionIntClass.valueOf((short) 2)), (short) 2);
        checkJavaLong(fromField("field_Object").mapToJava(EnvisionIntClass.valueOf((int) 3)), (int) 3);
        checkJavaLong(fromField("field_Object").mapToJava(EnvisionIntClass.valueOf((long) 4)), (long) 4);
        checkJavaDouble(fromField("field_Object").mapToJava(EnvisionDoubleClass.valueOf((float) 5)), (float) 5);
        checkJavaDouble(fromField("field_Object").mapToJava(EnvisionDoubleClass.valueOf((double) 6)), (double) 6);
        checkJavaString(fromField("field_Object").mapToJava(EnvisionStringClass.valueOf("Banana")), "Banana");
        checkJavaList(fromField("field_Object").mapToJava(envisionListOf(10, 20, 30, 40, 50)), EList.of(10L, 20L, 30L, 40L, 50L));
    }
    
    @Test
    void translate_field_listObject() {
        checkJavaList(fromField("field_List").mapToJava(envisionListOf(10, 'c', 30.0, "Banana", true)), EList.of(10L, 'c', 30.0D, "Banana", true));
        checkEnvisionList(fromField("field_ListLong").mapToEnvision(listOf(10, 'c', 30.0, "Banana", true)), envisionListOf(10, 'c', 30.0, "Banana", true));
    }
    
    @Test
    void translate_field_listLong() {
        checkJavaList(fromField("field_ListLong").mapToJava(envisionListOf(10, 20, 30, 40, 50)), EList.of(10L, 20L, 30L, 40L, 50L));
        checkEnvisionList(fromField("field_ListLong").mapToEnvision(longListOf(10, 20, 30, 40, 50)), envisionListOf(10, 20, 30, 40, 50));
    }
    
    @Test
    void translate_field_listDouble() {
        checkJavaList(fromField("field_ListDouble").mapToJava(envisionListOf(10D, 20D, 30D, 40D, 50D)), EList.of(10D, 20D, 30D, 40D, 50D));
        checkEnvisionList(fromField("field_ListDouble").mapToEnvision(doubleListOf(10D, 20D, 30D, 40D, 50D)), envisionListOf(10D, 20D, 30D, 40D, 50D));
    }
    
    @Test
    void translate_field_array() {
        checkJavaArray(fromField("field_booleanArray").mapToJava(envisionListOf(true, false)), arrayOf(true, false));
        checkJavaArray(fromField("field_charArray").mapToJava(envisionListOf('a', 'b')), arrayOf('a', 'b'));
        checkJavaArray(fromField("field_byteArray").mapToJava(envisionListOf((byte) 1, (byte) 2)), arrayOf((byte) 1, (byte) 2));
        checkJavaArray(fromField("field_shortArray").mapToJava(envisionListOf((short) 1, (short) 2)), arrayOf((short) 1, (short) 2));
        checkJavaArray(fromField("field_intArray").mapToJava(envisionListOf((int) 1, (int) 2)), arrayOf((int) 1, (int) 2));
        checkJavaArray(fromField("field_longArray").mapToJava(envisionListOf((long) 1, (long) 2)), arrayOf((long) 1, (long) 2));
        checkJavaArray(fromField("field_floatArray").mapToJava(envisionListOf((float) 1, (float) 2)), arrayOf((float) 1, (float) 2));
        checkJavaArray(fromField("field_doubleArray").mapToJava(envisionListOf((double) 1, (double) 2)), arrayOf((double) 1, (double) 2));
        checkJavaArray(fromField("field_StringArray").mapToJava(envisionListOf("ha", "lol")), arrayOf("ha", "lol"));
        checkJavaArray(fromField("field_ObjectArray").mapToJava(envisionListOf(true, 'a', (byte) 1, (short) 2, (int) 3, (long) 4, (float) 5, (double) 6, "ha")), objectArrayOf(true, 'a', 1L, 2L, 3L, 4L, 5D, 6D, "ha"));
    
        checkEnvisionList(fromField("field_booleanArray").mapToEnvision(arrayOf(true, false)), envisionListOf(true, false));
        checkEnvisionList(fromField("field_charArray").mapToEnvision(arrayOf('a', 'b')), envisionListOf('a', 'b'));
        checkEnvisionList(fromField("field_byteArray").mapToEnvision(arrayOf((byte) 1, (byte) 2)), envisionListOf((byte) 1, (byte) 2));
        checkEnvisionList(fromField("field_shortArray").mapToEnvision(arrayOf((short) 1, (short) 2)), envisionListOf((short) 1, (short) 2));
        checkEnvisionList(fromField("field_intArray").mapToEnvision(arrayOf((int) 1, (int) 2)), envisionListOf((int) 1, (int) 2));
        checkEnvisionList(fromField("field_longArray").mapToEnvision(arrayOf((long) 1, (long) 2)), envisionListOf((long) 1, (long) 2));
        checkEnvisionList(fromField("field_floatArray").mapToEnvision(arrayOf((float) 1, (float) 2)), envisionListOf((float) 1, (float) 2));
        checkEnvisionList(fromField("field_doubleArray").mapToEnvision(arrayOf((double) 1, (double) 2)), envisionListOf((double) 1, (double) 2));
        checkEnvisionList(fromField("field_StringArray").mapToEnvision((Object) arrayOf("ha", "lol")), envisionListOf("ha", "lol"));
        checkEnvisionList(fromField("field_ObjectArray").mapToEnvision((Object) objectArrayOf(true, 'a', (byte) 1, (short) 2, (int) 3, (long) 4, (float) 5, (double) 6, "ha")), envisionListOf(true, 'a', (byte) 1, (short) 2, (int) 3, (long) 4, (float) 5, (double) 6, "ha"));
    }
    
    @Test
    void translate_field_2DArray() {
        long[][] arr = new long[2][2];
        arr[0][0] = 1;
        arr[0][1] = 2;
        arr[1][0] = 3;
        arr[1][1] = 4;
        
        EnvisionList list = EnvisionListClass.newList();
        EnvisionList row1 = EnvisionListClass.newList();
        EnvisionList row2 = EnvisionListClass.newList();
        row1.add(EnvisionIntClass.valueOf(1), EnvisionIntClass.valueOf(2));
        row2.add(EnvisionIntClass.valueOf(3), EnvisionIntClass.valueOf(4));
        list.add(row1, row2);
        
        var mapper = fromField("field_2DArray");
        var mappedJava = mapper.mapToJava(list);
        var mappedEnvision = mapper.mapToEnvision((Object) arr);
        
        assertNotNull(mappedJava);
        assertNotNull(mappedEnvision);
        
        assertEquals(mappedJava.getClass(), arr.getClass());
        assertEquals(mappedEnvision.getClass(), list.getClass());
        
        long[][] mappedJavaArray = (long[][]) mappedJava;
        EnvisionList mappedEnvisionList = (EnvisionList) mappedEnvision;
        
        assertEquals(arr.length, mappedJavaArray.length);
        assertEquals(1L, mappedJavaArray[0][0]);
        assertEquals(2L, mappedJavaArray[0][1]);
        assertEquals(3L, mappedJavaArray[1][0]);
        assertEquals(4L, mappedJavaArray[1][1]);
        
        assertEquals(list.size_i(), mappedEnvisionList.size_i());
        assertEquals(EnvisionList.class, mappedEnvisionList.get(0).getClass());
        assertEquals(EnvisionList.class, mappedEnvisionList.get(1).getClass());
        assertEquals(EnvisionIntClass.valueOf(1L), ((EnvisionList) mappedEnvisionList.get(0)).get(0));
        assertEquals(EnvisionIntClass.valueOf(2L), ((EnvisionList) mappedEnvisionList.get(0)).get(1));
        assertEquals(EnvisionIntClass.valueOf(3L), ((EnvisionList) mappedEnvisionList.get(1)).get(0));
        assertEquals(EnvisionIntClass.valueOf(4L), ((EnvisionList) mappedEnvisionList.get(1)).get(1));
    }
    
    @Test
    void translate_field_3DArary() {
        long[][][] arr = new long[2][2][2];
        int count = 1;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                for (int k = 0; k < arr[i][j].length; k++) {
                    arr[i][j][k] = count++;
                }
            }
        }
        
        count = 1;
        EnvisionList list = EnvisionListClass.newList();
        for (int i = 0; i < arr.length; i++) {
            var curList1 = EnvisionListClass.newList();
            for (int j = 0; j < arr[i].length; j++) {
                var curList2 = EnvisionListClass.newList();
                for (int k = 0; k < arr[i][j].length; k++) {
                    curList2.add(EnvisionIntClass.valueOf(count++));
                }
                curList1.add(curList2);
            }
            list.add(curList1);
        }
        
        var mapper = fromField("field_3DArray");
        var mappedJava = mapper.mapToJava(list);
        var mappedEnvision = mapper.mapToEnvision((Object) arr);
        
        assertNotNull(mappedJava);
        assertNotNull(mappedEnvision);
        
        assertEquals(mappedJava.getClass(), arr.getClass());
        assertEquals(mappedEnvision.getClass(), list.getClass());
        
        long[][][] mappedJavaArray = (long[][][]) mappedJava;
        EnvisionList mappedEnvisionList = (EnvisionList) mappedEnvision;
        
        assertEquals(arr.length, mappedJavaArray.length);
        assertEquals(1L, mappedJavaArray[0][0][0]);
        assertEquals(2L, mappedJavaArray[0][0][1]);
        assertEquals(3L, mappedJavaArray[0][1][0]);
        assertEquals(4L, mappedJavaArray[0][1][1]);
        assertEquals(5L, mappedJavaArray[1][0][0]);
        assertEquals(6L, mappedJavaArray[1][0][1]);
        assertEquals(7L, mappedJavaArray[1][1][0]);
        assertEquals(8L, mappedJavaArray[1][1][1]);
        
        assertEquals(list.size_i(), mappedEnvisionList.size_i());
        assertEquals(EnvisionList.class, mappedEnvisionList.get(0).getClass());
        assertEquals(EnvisionList.class, mappedEnvisionList.get(1).getClass());
        assertEquals(EnvisionList.class, ((EnvisionList) mappedEnvisionList.get(0)).get(0).getClass());
        assertEquals(EnvisionList.class, ((EnvisionList) mappedEnvisionList.get(0)).get(1).getClass());
        assertEquals(EnvisionList.class, ((EnvisionList) mappedEnvisionList.get(1)).get(0).getClass());
        assertEquals(EnvisionList.class, ((EnvisionList) mappedEnvisionList.get(1)).get(1).getClass());
        assertEquals(EnvisionIntClass.valueOf(1L), ((EnvisionList) ((EnvisionList) mappedEnvisionList.get(0)).get(0)).get(0));
        assertEquals(EnvisionIntClass.valueOf(2L), ((EnvisionList) ((EnvisionList) mappedEnvisionList.get(0)).get(0)).get(1));
        assertEquals(EnvisionIntClass.valueOf(3L), ((EnvisionList) ((EnvisionList) mappedEnvisionList.get(0)).get(1)).get(0));
        assertEquals(EnvisionIntClass.valueOf(4L), ((EnvisionList) ((EnvisionList) mappedEnvisionList.get(0)).get(1)).get(1));
        assertEquals(EnvisionIntClass.valueOf(5L), ((EnvisionList) ((EnvisionList) mappedEnvisionList.get(1)).get(0)).get(0));
        assertEquals(EnvisionIntClass.valueOf(6L), ((EnvisionList) ((EnvisionList) mappedEnvisionList.get(1)).get(0)).get(1));
        assertEquals(EnvisionIntClass.valueOf(7L), ((EnvisionList) ((EnvisionList) mappedEnvisionList.get(1)).get(1)).get(0));
        assertEquals(EnvisionIntClass.valueOf(8L), ((EnvisionList) ((EnvisionList) mappedEnvisionList.get(1)).get(1)).get(1));
    }
    
    //==============
    // Method Tests
    //==============
    
    @Test
    void translate_method_one() {
        checkEnvisionBoolean(fromMethod("method_boolean").mapToEnvision(false), false);
        checkEnvisionChar(fromMethod("method_char").mapToEnvision('c'), 'c');
        checkEnvisionInt(fromMethod("method_byte").mapToEnvision((byte) 1), (byte) 1);
        checkEnvisionInt(fromMethod("method_short").mapToEnvision((short) 2), (short) 2);
        checkEnvisionInt(fromMethod("method_int").mapToEnvision((int) 3), (int) 3);
        checkEnvisionInt(fromMethod("method_long").mapToEnvision((long) 4), (long) 4);
        checkEnvisionDouble(fromMethod("method_float").mapToEnvision((float) 5), (float) 5);
        checkEnvisionDouble(fromMethod("method_double").mapToEnvision((double) 6), (double) 6);
        
        checkEnvisionBoolean(fromMethod("method_Boolean").mapToEnvision(Boolean.FALSE), Boolean.FALSE);
        checkEnvisionChar(fromMethod("method_Character").mapToEnvision('c'), 'c');
        checkEnvisionInt(fromMethod("method_Byte").mapToEnvision((Byte) (byte) 1), (Byte) (byte) 1);
        checkEnvisionInt(fromMethod("method_Short").mapToEnvision((Short) (short) 2), (Short) (short) 2);
        checkEnvisionInt(fromMethod("method_Integer").mapToEnvision((Integer) 3), (Integer) 3);
        checkEnvisionInt(fromMethod("method_Long").mapToEnvision((Long) 4L), (Long) 4L);
        checkEnvisionDouble(fromMethod("method_Float").mapToEnvision((Float) 5.0F), (Float) 5.0F);
        checkEnvisionDouble(fromMethod("method_Double").mapToEnvision((Double) 6.0), (Double) 6.0);
        
        checkEnvisionString(fromMethod("method_String").mapToEnvision("LOL"), "LOL");
    }
    
    @Test
    void translate_method_Object() {
        checkEnvisionBoolean(fromMethod("method_Object").mapToEnvision(true), true);
        checkEnvisionChar(fromMethod("method_Object").mapToEnvision('c'), 'c');
        checkEnvisionInt(fromMethod("method_Object").mapToEnvision(5), 5);
        checkEnvisionDouble(fromMethod("method_Object").mapToEnvision(6.0), 6.0);
        checkEnvisionString(fromMethod("method_Object").mapToEnvision("Banana"), "Banana");
        checkEnvisionList(fromMethod("method_Object").mapToEnvision(longListOf(10, 20, 30, 40, 50)), envisionListOf(10, 20, 30, 40, 50));
        
        checkJavaBoolean(fromMethod("method_Object").mapToJava(EnvisionBooleanClass.valueOf(true)), true);
        checkJavaChar(fromMethod("method_Object").mapToJava(EnvisionCharClass.valueOf('c')), 'c');
        checkJavaLong(fromMethod("method_Object").mapToJava(EnvisionIntClass.valueOf((byte) 1)), (byte) 1);
        checkJavaLong(fromMethod("method_Object").mapToJava(EnvisionIntClass.valueOf((short) 2)), (short) 2);
        checkJavaLong(fromMethod("method_Object").mapToJava(EnvisionIntClass.valueOf((int) 3)), (int) 3);
        checkJavaLong(fromMethod("method_Object").mapToJava(EnvisionIntClass.valueOf((long) 4)), (long) 4);
        checkJavaDouble(fromMethod("method_Object").mapToJava(EnvisionDoubleClass.valueOf((float) 5)), (float) 5);
        checkJavaDouble(fromMethod("method_Object").mapToJava(EnvisionDoubleClass.valueOf((double) 6)), (double) 6);
        checkJavaString(fromMethod("method_Object").mapToJava(EnvisionStringClass.valueOf("Banana")), "Banana");
        checkJavaList(fromMethod("method_Object").mapToJava(envisionListOf(10, 20, 30, 40, 50)), EList.of(10L, 20L, 30L, 40L, 50L));
    }
    
    //=======================
    // Static Helper Methods
    //=======================
    
    private static void checkJavaArray(Object obj, boolean[] expectedArray) {
        assertNotNull(obj);
        assertEquals(expectedArray.getClass(), obj.getClass());
        boolean[] array = (boolean[]) obj;
        for (int i = 0; i < array.length; i++) {
            boolean expected = expectedArray[i];
            boolean got = array[i];
            assertEquals(expected, got);
        }
    }
    
    private static void checkJavaArray(Object obj, char[] expectedArray) {
        assertNotNull(obj);
        assertEquals(expectedArray.getClass(), obj.getClass());
        char[] array = (char[]) obj;
        for (int i = 0; i < array.length; i++) {
            char expected = expectedArray[i];
            char got = array[i];
            assertEquals(expected, got);
        }
    }
    
    private static void checkJavaArray(Object obj, byte[] expectedArray) {
        assertNotNull(obj);
        assertEquals(expectedArray.getClass(), obj.getClass());
        byte[] array = (byte[]) obj;
        for (int i = 0; i < array.length; i++) {
            byte expected = expectedArray[i];
            byte got = array[i];
            assertEquals(expected, got);
        }
    }
    
    private static void checkJavaArray(Object obj, short[] expectedArray) {
        assertNotNull(obj);
        assertEquals(expectedArray.getClass(), obj.getClass());
        short[] array = (short[]) obj;
        for (int i = 0; i < array.length; i++) {
            short expected = expectedArray[i];
            short got = array[i];
            assertEquals(expected, got);
        }
    }
    
    private static void checkJavaArray(Object obj, int[] expectedArray) {
        assertNotNull(obj);
        assertEquals(expectedArray.getClass(), obj.getClass());
        int[] array = (int[]) obj;
        for (int i = 0; i < array.length; i++) {
            int expected = expectedArray[i];
            int got = array[i];
            assertEquals(expected, got);
        }
    }
    
    private static void checkJavaArray(Object obj, long[] expectedArray) {
        assertNotNull(obj);
        assertEquals(expectedArray.getClass(), obj.getClass());
        long[] array = (long[]) obj;
        for (int i = 0; i < array.length; i++) {
            long expected = expectedArray[i];
            long got = array[i];
            assertEquals(expected, got);
        }
    }
    
    private static void checkJavaArray(Object obj, float[] expectedArray) {
        assertNotNull(obj);
        assertEquals(expectedArray.getClass(), obj.getClass());
        float[] array = (float[]) obj;
        for (int i = 0; i < array.length; i++) {
            float expected = expectedArray[i];
            float got = array[i];
            assertEquals(expected, got);
        }
    }
    
    private static void checkJavaArray(Object obj, double[] expectedArray) {
        assertNotNull(obj);
        assertEquals(expectedArray.getClass(), obj.getClass());
        double[] array = (double[]) obj;
        for (int i = 0; i < array.length; i++) {
            double expected = expectedArray[i];
            double got = array[i];
            assertEquals(expected, got);
        }
    }
    
    private static void checkJavaArray(Object obj, Object[] expectedArray) {
        assertNotNull(obj);
        assertEquals(expectedArray.getClass(), obj.getClass());
        Object[] array = (Object[]) obj;
        for (int i = 0; i < array.length; i++) {
            Object expected = expectedArray[i];
            Object got = array[i];
            assertEquals(expected, got);
        }
    }
    
    private static void checkJavaList(Object obj, List<?> expectedList) {
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
    
    private static void checkEnvisionList(EnvisionObject obj, EnvisionList expectedList) {
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
    
    //------
    // java
    //------
    
    private static void checkJavaBoolean(Object obj, boolean expectedValue) {
        assertNotNull(obj);
        assertTrue(obj instanceof Boolean);
        assertEquals(expectedValue, ((Boolean) obj).booleanValue());
    }
    
    private static void checkJavaChar(Object obj, char expectedValue) {
        assertNotNull(obj);
        assertTrue(obj instanceof Character);
        assertEquals(expectedValue, ((Character) obj).charValue());
    }
    
    private static void checkJavaByte(Object obj, byte expectedValue) {
        assertNotNull(obj);
        assertTrue(obj instanceof Byte);
        assertEquals(expectedValue, ((Byte) obj).byteValue());
    }
    
    private static void checkJavaShort(Object obj, short expectedValue) {
        assertNotNull(obj);
        assertTrue(obj instanceof Short);
        assertEquals(expectedValue, ((Short) obj).shortValue());
    }
    
    private static void checkJavaInt(Object obj, int expectedValue) {
        assertNotNull(obj);
        assertTrue(obj instanceof Integer);
        assertEquals(expectedValue, ((Integer) obj).intValue());
    }
    
    private static void checkJavaLong(Object obj, long expectedValue) {
        assertNotNull(obj);
        assertTrue(obj instanceof Long);
        assertEquals(expectedValue, ((Long) obj).longValue());
    }
    
    private static void checkJavaFloat(Object obj, float expectedValue) {
        assertNotNull(obj);
        assertTrue(obj instanceof Float);
        assertEquals(expectedValue, ((Float) obj).floatValue());
    }
    
    private static void checkJavaDouble(Object obj, double expectedValue) {
        assertNotNull(obj);
        assertTrue(obj instanceof Double);
        assertEquals(expectedValue, ((Double) obj).doubleValue());
    }
    
    private static void checkJavaString(Object obj, String expectedValue) {
        assertNotNull(obj);
        assertTrue(obj instanceof String);
        assertEquals(expectedValue, ((String) obj).toString());
    }
    
    //----------
    // envision
    //----------
    
    private static void checkEnvisionBoolean(EnvisionObject obj, boolean expectedValue) {
        assertNotNull(obj);
        assertTrue(obj instanceof EnvisionBoolean);
        assertEquals(expectedValue, ((EnvisionBoolean) obj).bool_val);
    }
    
    private static void checkEnvisionChar(EnvisionObject obj, char expectedValue) {
        assertNotNull(obj);
        assertTrue(obj instanceof EnvisionChar);
        assertEquals(expectedValue, ((EnvisionChar) obj).char_val);
    }
    
    private static void checkEnvisionInt(EnvisionObject obj, long expectedValue) {
        assertNotNull(obj);
        assertTrue(obj instanceof EnvisionInt);
        assertEquals(expectedValue, ((EnvisionInt) obj).int_val);
    }
    
    private static void checkEnvisionDouble(EnvisionObject obj, double expectedValue) {
        assertNotNull(obj);
        assertTrue(obj instanceof EnvisionDouble);
        assertEquals(expectedValue, ((EnvisionDouble) obj).double_val);
    }
    
    private static void checkEnvisionString(EnvisionObject obj, String expectedValue) {
        assertNotNull(obj);
        assertTrue(obj instanceof EnvisionString);
        assertEquals(expectedValue, ((EnvisionString) obj).toString());
    }
    
}
