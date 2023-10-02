package envision_lang.lang.java;

import static envision_lang.lang.natives.EnvisionStaticTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import envision_lang.lang.natives.IDatatype;
import eutil.datatypes.util.EList;

class NativeFieldInitTests extends NativeFieldTests {
    
    @Test
    void test_creation_nonArray() {
        checkFieldType("field_boolean", boolean.class, BOOL_TYPE);
        checkFieldType("field_char", char.class, CHAR_TYPE);
        checkFieldType("field_byte", byte.class, INT_TYPE);
        checkFieldType("field_short", short.class, INT_TYPE);
        checkFieldType("field_int", int.class, INT_TYPE);
        checkFieldType("field_long", long.class, INT_TYPE);
        checkFieldType("field_float", float.class, DOUBLE_TYPE);
        checkFieldType("field_double", double.class, DOUBLE_TYPE);
        
        checkFieldType("field_Boolean", Boolean.class, BOOL_TYPE);
        checkFieldType("field_Character", Character.class, CHAR_TYPE);
        checkFieldType("field_Byte", Byte.class, INT_TYPE);
        checkFieldType("field_Short", Short.class, INT_TYPE);
        checkFieldType("field_Integer", Integer.class, INT_TYPE);
        checkFieldType("field_Long", Long.class, INT_TYPE);
        checkFieldType("field_Float", Float.class, DOUBLE_TYPE);
        checkFieldType("field_Double", Double.class, DOUBLE_TYPE);
        
        checkFieldType("field_String", String.class, STRING_TYPE);
        checkFieldType("field_Object", Object.class, VAR_TYPE);
    }
    
    @Test
    void test_creation_array() {
        checkFieldType("field_booleanArray", boolean[].class, LIST_TYPE);
        checkFieldType("field_charArray", char[].class, LIST_TYPE);
        checkFieldType("field_byteArray", byte[].class, LIST_TYPE);
        checkFieldType("field_shortArray", short[].class, LIST_TYPE);
        checkFieldType("field_intArray", int[].class, LIST_TYPE);
        checkFieldType("field_longArray", long[].class, LIST_TYPE);
        checkFieldType("field_floatArray", float[].class, LIST_TYPE);
        checkFieldType("field_doubleArray", double[].class, LIST_TYPE);
        
        checkFieldType("field_BooleanArray", Boolean[].class, LIST_TYPE);
        checkFieldType("field_CharacterArray", Character[].class, LIST_TYPE);
        checkFieldType("field_ByteArray", Byte[].class, LIST_TYPE);
        checkFieldType("field_ShortArray", Short[].class, LIST_TYPE);
        checkFieldType("field_IntegerArray", Integer[].class, LIST_TYPE);
        checkFieldType("field_LongArray", Long[].class, LIST_TYPE);
        checkFieldType("field_FloatArray", Float[].class, LIST_TYPE);
        checkFieldType("field_DoubleArray", Double[].class, LIST_TYPE);
        
        checkFieldType("field_StringArray", String[].class, LIST_TYPE);
        checkFieldType("field_ObjectArray", Object[].class, LIST_TYPE);
    }
    
    @Test
    void test_creation_multidimensionalArray() {
        checkFieldType("field_2DArray", long[][].class, LIST_TYPE);
        checkFieldType("field_3DArray", long[][][].class, LIST_TYPE);
    }
    
    @Test
    void test_creation_lists() {
        checkFieldType("field_List", List.class, LIST_TYPE);
        checkFieldType("field_ListLong", List.class, LIST_TYPE);
        checkFieldType("field_ListDouble", List.class, LIST_TYPE);
    }
    
    @Test
    void test_creation_elists() {
        checkFieldType("field_EList", EList.class, LIST_TYPE);
        checkFieldType("field_EListLong", EList.class, LIST_TYPE);
        checkFieldType("field_EListDouble", EList.class, LIST_TYPE);
    }
    
    @Test
    void test_creation_sets() {
        checkFieldType("field_Set", Set.class, LIST_TYPE);
        checkFieldType("field_SetLong", Set.class, LIST_TYPE);
        checkFieldType("field_SetDouble", Set.class, LIST_TYPE);
    }
    
    //=========================
    // Internal Helper Methods
    //=========================
    
    private void checkFieldType(String fieldName, Class<?> expectedJavaType, IDatatype expectedEnvisionType) {
        NativeField field = field(fieldName);
        assertNotNull(field);
        Class<?> javaType = field.getJavaFieldType();
        IDatatype envisionType = field.getDatatype();
        assertEquals(expectedJavaType, javaType);
        assertEquals(expectedEnvisionType, envisionType);
    }
    
}
