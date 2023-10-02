package envision_lang.lang.java;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import envision_lang.interpreter.util.creation_util.ObjectCreator;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionListClass;
import eutil.EUtil;
import eutil.datatypes.util.EList;

abstract class NativeDatatypeMapperTests {
    
    //=============
    // Test Fields
    //=============
    
    private final boolean field_boolean = true;
    private final Boolean field_Boolean = Boolean.TRUE;
    private final char field_char = 'c';
    private final Character field_Character = Character.valueOf(field_char);
    private final byte field_byte = 1;
    private final Byte field_Byte = Byte.valueOf(field_byte);
    private final short field_short = 2;
    private final Short field_Short = Short.valueOf(field_short);
    private final int field_int = 3;
    private final Integer field_Integer = Integer.valueOf(field_int);
    private final long field_long = 4;
    private final Long field_Long = Long.valueOf(field_long);
    private final float field_float = 5.0f;
    private final Float field_Float = Float.valueOf(field_float);
    private final double field_double = 6.0d;
    private final Double field_Double = Double.valueOf(field_double);
    private final String field_String = "LOL";
    private final Object field_Object = new Object();
    
    //===================
    // Test Array Fields
    //===================
    
    private final boolean[] field_booleanArray = new boolean[0];
    private final char[] field_charArray = new char[0];
    private final byte[] field_byteArray = new byte[0];
    private final short[] field_shortArray = new short[0];
    private final int[] field_intArray = new int[0];
    private final long[] field_longArray = new long[0];
    private final float[] field_floatArray = new float[0];
    private final double[] field_doubleArray = new double[0];
    private final Boolean[] field_BooleanArray = new Boolean[0];
    private final Character[] field_CharacterArray = new Character[0];
    private final Byte[] field_ByteArray = new Byte[0];
    private final Short[] field_ShortArray = new Short[0];
    private final Integer[] field_IntegerArray = new Integer[0];
    private final Long[] field_LongArray = new Long[0];
    private final Float[] field_FloatArray = new Float[0];
    private final Double[] field_DoubleArray = new Double[0];
    private final String[] field_StringArray = new String[0];
    private final Object[] field_ObjectArray = new Object[0];
    private final long[][] field_2DArray = new long[0][0];
    private final long[][][] field_3DArray = new long[0][0][0];
    
    //======================
    // Test List/Set Fields
    //======================
    
    private final List<Object> field_List = new ArrayList<>();
    private final EList<Object> field_EList = EList.newList();
    private final Set<Object> field_Set = new HashSet<>();

    private final List<Long> field_ListLong = new ArrayList<>();
    private final EList<Long> field_EListLong = EList.newList();
    private final Set<Long> field_SetLong = new HashSet<>();
    
    private final List<Double> field_ListDouble = new ArrayList<>();
    private final EList<Double> field_EListDouble = EList.newList();
    private final Set<Double> field_SetDouble = new HashSet<>();
    
    //==============
    // Test Methods
    //==============
    
    private final void method_noParams() {}
    private final void method_boolean(boolean b) {}
    private final void method_char(char c) {}
    private final void method_byte(byte b) {}
    private final void method_short(short s) {}
    private final void method_int(int i) {}
    private final void method_long(long l) {}
    private final void method_float(float f) {}
    private final void method_double(double d) {}
    private final void method_Boolean(Boolean b) {}
    private final void method_Character(Character c) {}
    private final void method_Byte(Byte b) {}
    private final void method_Short(Short s) {}
    private final void method_Integer(Integer i) {}
    private final void method_Long(Long l) {}
    private final void method_Float(Float f) {}
    private final void method_Double(Double d) {}
    private final void method_String(String s) {}
    private final void method_Object(Object o) {}
    private final void method_booleanArray(boolean[] a) {}
    private final void method_charArray(char[] a) {}
    private final void method_byteArray(byte[] a) {}
    private final void method_shortArray(short[] a) {}
    private final void method_intArray(int[] a) {}
    private final void method_longArray(long[] a) {}
    private final void method_floatArray(float[] a) {}
    private final void method_doubleArray(double[] a) {}
    private final void method_BooleanArray(Boolean[] a) {}
    private final void method_CharacterArray(Character[] a) {}
    private final void method_ByteArray(Byte[] a) {}
    private final void method_ShortArray(Short[] a) {}
    private final void method_IntegerArray(Integer[] a) {}
    private final void method_LongArray(Long[] a) {}
    private final void method_FloatArray(Float[] a) {}
    private final void method_DoubleArray(Double[] a) {}
    private final void method_StringArray(String[] a) {}
    private final void method_ObjectArray(Object[] a) {}
    private final void method_primitives(boolean a, char b, byte c, short d, int e, long f, float g, double h) {}
    private final void method_Objects(Boolean a, Character b, Byte c, Short d, Integer e, Long f, Float g, Double h) {}
    private final void method_mixed(boolean a, Character b, byte c, Short d, int e, Long f, float g, Double h) {}
    private final void method_List(List<?> list) {}
    private final void method_ListLong(List<Long> list) {}
    private final void method_Set(Set<?> set) {}
    private final void method_SetLong(Set<Long> set) {}
    
    //======================
    // Test Utility Methods
    //======================
    
    protected Field extractField(String type) {
        try {
            final var c = NativeDatatypeMapperTests.class;
            return c.getDeclaredField(type);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    protected Method extractMethod(String methodName) {
        try {
            final var c = NativeDatatypeMapperTests.class;
            Method method = EUtil.getFirst(c.getDeclaredMethods(), m -> m.getName().equals(methodName));
            return method;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    protected Parameter[] extractParams(String methodName) {
        try {
            final var c = NativeDatatypeMapperTests.class;
            Method method = EUtil.getFirst(c.getDeclaredMethods(), m -> m.getName().equals(methodName));
            return method.getParameters();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    protected NativeDatatypeMapper fromField(String fieldName) {
        return new NativeDatatypeMapper(extractField(fieldName));
    }
    
    protected NativeDatatypeMapper fromMethod(String methodName) {
        return new NativeDatatypeMapper(extractMethod(methodName));
    }
    
    protected NativeDatatypeMapper fromParameters(String methodName) {
        return new NativeDatatypeMapper(extractParams(methodName));
    }
    
    protected EnvisionList envisionListOf(Object... values) {
        var list = EnvisionListClass.newList();
        for (int i = 0; i < values.length; i++) {
            list.add(ObjectCreator.wrap(values[i]));            
        }
        return list;
    }
    
    //============
    // Silly Java
    //============
    
    protected static boolean[] arrayOf(boolean... values) { return values; }
    protected static char[] arrayOf(char... values) { return values; }
    protected static byte[] arrayOf(byte... values) { return values; }
    protected static short[] arrayOf(short... values) { return values; }
    protected static int[] arrayOf(int... values) { return values; }
    protected static long[] arrayOf(long... values) { return values; }
    protected static float[] arrayOf(float... values) { return values; }
    protected static double[] arrayOf(double... values) { return values; }
    protected static String[] arrayOf(String... values) { return values; }
    protected static Object[] objectArrayOf(Object... values) { return values; }
    
    protected static List<Object> listOf(Object... values) {
        List<Object> list = new ArrayList<>();
        for (Object o : values) list.add(o);
        return list;
    }
    
    protected static List<Long> longListOf(Number... values) {
        List<Long> list = new ArrayList<>();
        for (Number n : values) list.add(n.longValue());
        return list;
    }
    
    protected static List<Double> doubleListOf(Number... values) {
        List<Double> list = new ArrayList<>();
        for (Number n : values) list.add(n.doubleValue());
        return list;
    }
    
}
