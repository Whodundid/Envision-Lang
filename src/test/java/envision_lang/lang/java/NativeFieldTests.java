package envision_lang.lang.java;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;

import envision_lang.EnvisionLangTest;
import envision_lang.interpreter.util.creation_util.ObjectCreator;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionListClass;
import eutil.datatypes.util.EList;

class NativeFieldTests extends EnvisionLangTest {
    
    //=============
    // Test Fields
    //=============
    
    boolean field_boolean = true;
    Boolean field_Boolean = Boolean.TRUE;
    char field_char = 'c';
    Character field_Character = Character.valueOf(field_char);
    byte field_byte = 1;
    Byte field_Byte = Byte.valueOf(field_byte);
    short field_short = 2;
    Short field_Short = Short.valueOf(field_short);
    int field_int = 3;
    Integer field_Integer = Integer.valueOf(field_int);
    long field_long = 4;
    Long field_Long = Long.valueOf(field_long);
    float field_float = 5.0f;
    Float field_Float = Float.valueOf(field_float);
    double field_double = 6.0d;
    Double field_Double = Double.valueOf(field_double);
    String field_String = "LOL";
    Object field_Object = new Object();
    
    //===================
    // Test Array Fields
    //===================
    
    boolean[] field_booleanArray = new boolean[0];
    char[] field_charArray = new char[0];
    byte[] field_byteArray = new byte[0];
    short[] field_shortArray = new short[0];
    int[] field_intArray = new int[0];
    long[] field_longArray = new long[0];
    float[] field_floatArray = new float[0];
    double[] field_doubleArray = new double[0];
    Boolean[] field_BooleanArray = new Boolean[0];
    Character[] field_CharacterArray = new Character[0];
    Byte[] field_ByteArray = new Byte[0];
    Short[] field_ShortArray = new Short[0];
    Integer[] field_IntegerArray = new Integer[0];
    Long[] field_LongArray = new Long[0];
    Float[] field_FloatArray = new Float[0];
    Double[] field_DoubleArray = new Double[0];
    String[] field_StringArray = new String[0];
    Object[] field_ObjectArray = new Object[0];
    long[][] field_2DArray = new long[0][0];
    long[][][] field_3DArray = new long[0][0][0];
    
    //======================
    // Test List/Set Fields
    //======================
    
    List<Object> field_List = new ArrayList<>();
    EList<Object> field_EList = EList.newList();
    Set<Object> field_Set = new HashSet<>();

    List<Long> field_ListLong = new ArrayList<>();
    EList<Long> field_EListLong = EList.newList();
    Set<Long> field_SetLong = new HashSet<>();
    
    List<Double> field_ListDouble = new ArrayList<>();
    EList<Double> field_EListDouble = EList.newList();
    Set<Double> field_SetDouble = new HashSet<>();
    
    //=======
    // Setup
    //=======
    
    protected EnvisionJavaClass javaClass;
    
    @BeforeEach
    private void setupInternal() {
        javaClass = EnvisionJavaClass.wrapJavaClass(interpreter, this.getClass());
    }
    
    //======================
    // Test Utility Methods
    //======================
    
    protected Field extractField(String type) {
        try {
            final var c = NativeFieldTests.class;
            return c.getDeclaredField(type);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    protected NativeField field(String fieldName) {
        var instance = new NativeFieldTests();
        EnvisionJavaObject wrapperObject = EnvisionJavaObject.wrapJavaObject(interpreter, instance);
        Field f = extractField(fieldName);
        NativeField wrappedField = new NativeField(f);
        wrappedField.setWrappedObject(wrapperObject);
        return wrappedField;
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
