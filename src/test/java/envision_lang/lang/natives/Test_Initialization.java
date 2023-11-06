package envision_lang.lang.natives;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

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

public class Test_Initialization {
    
    @Test
    void test_initializeNativeTypes() {
        NativeTypeManager.init();
        
        for (Primitives p : Primitives.values()) {
            var type = NativeTypeManager.datatypeOf(p);
            // ensure that each type got registered
            assertNotNull(type);
            assertEquals(p.string_value, type.getStringValue());
        }
        
        // non-array types
        EnvisionClass func_class = NativeTypeManager.getClassTypeFor(Primitives.FUNCTION);
        EnvisionClass bool_class = NativeTypeManager.getClassTypeFor(Primitives.BOOLEAN);
        EnvisionClass char_class = NativeTypeManager.getClassTypeFor(Primitives.CHAR);
        EnvisionClass double_class = NativeTypeManager.getClassTypeFor(Primitives.DOUBLE);
        EnvisionClass int_class = NativeTypeManager.getClassTypeFor(Primitives.INT);
        EnvisionClass number_class = NativeTypeManager.getClassTypeFor(Primitives.NUMBER);
        EnvisionClass string_class = NativeTypeManager.getClassTypeFor(Primitives.STRING);
        EnvisionClass list_class = NativeTypeManager.getClassTypeFor(Primitives.LIST);
        EnvisionClass tuple_class = NativeTypeManager.getClassTypeFor(Primitives.TUPLE);
        EnvisionClass map_class = NativeTypeManager.getClassTypeFor(Primitives.MAP);
        
        assertNotNull(func_class);
        assertNotNull(bool_class);
        assertNotNull(char_class);
        assertNotNull(double_class);
        assertNotNull(int_class);
        assertNotNull(number_class);
        assertNotNull(string_class);
        assertNotNull(list_class);
        assertNotNull(tuple_class);
        assertNotNull(map_class);
        
        assertEquals(EnvisionFunctionClass.FUNC_CLASS, func_class);
        assertEquals(EnvisionBooleanClass.BOOLEAN_CLASS, bool_class);
        assertEquals(EnvisionCharClass.CHAR_CLASS, char_class);
        assertEquals(EnvisionDoubleClass.DOUBLE_CLASS, double_class);
        assertEquals(EnvisionIntClass.INT_CLASS, int_class);
        assertEquals(EnvisionNumberClass.NUMBER_CLASS, number_class);
        assertEquals(EnvisionStringClass.STRING_CLASS, string_class);
        assertEquals(EnvisionListClass.LIST_CLASS, list_class);
        assertEquals(EnvisionTupleClass.TUPLE_CLASS, tuple_class);
        assertEquals(EnvisionMapClass.MAP_CLASS, map_class);
        
        // array types
        EnvisionClass bool_a_class = NativeTypeManager.getClassTypeFor(Primitives.BOOLEAN_A);
        EnvisionClass char_a_class = NativeTypeManager.getClassTypeFor(Primitives.CHAR_A);
        EnvisionClass double_a_class = NativeTypeManager.getClassTypeFor(Primitives.DOUBLE_A);
        EnvisionClass int_a_class = NativeTypeManager.getClassTypeFor(Primitives.INT_A);
        EnvisionClass number_a_class = NativeTypeManager.getClassTypeFor(Primitives.NUMBER_A);
        EnvisionClass string_a_class = NativeTypeManager.getClassTypeFor(Primitives.STRING_A);
        EnvisionClass var_a_class = NativeTypeManager.getClassTypeFor(Primitives.VAR_A);
        
        assertNotNull(bool_a_class);
        assertNotNull(char_a_class);
        assertNotNull(double_a_class);
        assertNotNull(int_a_class);
        assertNotNull(number_a_class);
        assertNotNull(string_a_class);
        assertNotNull(var_a_class);
        
        assertEquals(EnvisionListClass.LIST_CLASS, bool_a_class);
        assertEquals(EnvisionListClass.LIST_CLASS, char_a_class);
        assertEquals(EnvisionListClass.LIST_CLASS, double_a_class);
        assertEquals(EnvisionListClass.LIST_CLASS, int_a_class);
        assertEquals(EnvisionListClass.LIST_CLASS, number_a_class);
        assertEquals(EnvisionListClass.LIST_CLASS, string_a_class);
        assertEquals(EnvisionListClass.LIST_CLASS, var_a_class);
        
        // ensure that static types match the primitive datatypes
        assertEquals(Primitives.FUNCTION.toDatatype(), EnvisionStaticTypes.FUNC_TYPE);
        assertEquals(Primitives.BOOLEAN.toDatatype(), EnvisionStaticTypes.BOOL_TYPE);
        assertEquals(Primitives.CHAR.toDatatype(), EnvisionStaticTypes.CHAR_TYPE);
        assertEquals(Primitives.DOUBLE.toDatatype(), EnvisionStaticTypes.DOUBLE_TYPE);
        assertEquals(Primitives.INT.toDatatype(), EnvisionStaticTypes.INT_TYPE);
        assertEquals(Primitives.NUMBER.toDatatype(), EnvisionStaticTypes.NUMBER_TYPE);
        assertEquals(Primitives.STRING.toDatatype(), EnvisionStaticTypes.STRING_TYPE);
        assertEquals(Primitives.LIST.toDatatype(), EnvisionStaticTypes.LIST_TYPE);
        assertEquals(Primitives.TUPLE.toDatatype(), EnvisionStaticTypes.TUPLE_TYPE);
        assertEquals(Primitives.MAP.toDatatype(), EnvisionStaticTypes.MAP_TYPE);
        
        assertEquals(Primitives.BOOLEAN_A.toDatatype(), EnvisionStaticTypes.BOOL_A_TYPE);
        assertEquals(Primitives.CHAR_A.toDatatype(), EnvisionStaticTypes.CHAR_A_TYPE);
        assertEquals(Primitives.DOUBLE_A.toDatatype(), EnvisionStaticTypes.DOUBLE_A_TYPE);
        assertEquals(Primitives.INT_A.toDatatype(), EnvisionStaticTypes.INT_A_TYPE);
        assertEquals(Primitives.NUMBER_A.toDatatype(), EnvisionStaticTypes.NUMBER_A_TYPE);
        assertEquals(Primitives.STRING_A.toDatatype(), EnvisionStaticTypes.STRING_A_TYPE);
        assertEquals(Primitives.VAR_A.toDatatype(), EnvisionStaticTypes.VAR_A_TYPE);
    }
    
}
