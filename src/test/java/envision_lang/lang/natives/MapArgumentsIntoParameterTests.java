package envision_lang.lang.natives;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Array;

import static envision_lang.lang.natives.Primitives.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.*;
import envision_lang.lang.language_errors.error_types.InvalidArgumentError;
import eutil.datatypes.util.EList;

public class MapArgumentsIntoParameterTests {
    
    //========
    // Fields
    //========
    
    /**
     * Effectively a working stack that stores temporary parameters until
     * they are built into parameter data objects.
     */
    protected final EList<EnvisionParameter> params = EList.newList();
    /**
     * Effectively a working stack that stores temporary objects until
     * they are built into an array of arguments.
     */
    protected final EList<EnvisionObject> objects = EList.newList();
    
    protected ParameterData func;
    protected EnvisionObject[] args;
    protected EList<EnvisionObject> mapped;
    
    //=======
    // Setup
    //=======
    
    @BeforeEach
    void setup() {
        NativeTypeManager.init();
        
        params.clear();
        func = null;
        args = null;
        mapped = null;
    }
    
    //=======
    // Tests
    //=======
    
    //-----------------
    // Expected Passes
    //-----------------
    
    @Test
    void test_pass_empty_empty() {
        buildFunc();
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_each_each() {
        testEachType(BOOLEAN, CHAR, INT, DOUBLE, STRING);
    }

    //----------------------------------------------------------
    
    @Test
    void test_pass_number_int() {
        param(NUMBER);
        buildFunc();
        
        ADD_INT(10);
        buildArgs();
        
        assertPass(INT);
        assertArgumentValue(0, 10);
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_number_double() {
        param(NUMBER);
        buildFunc();
        
        ADD_DOUBLE(10.0);
        buildArgs();
        
        assertPass(DOUBLE);
        assertArgumentValue(0, 10.0);
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_twoWithDefault_one_A() {
        param(INT, INT(20));
        param(DOUBLE);
        buildFunc();
        
        ADD_DOUBLE(10.0);
        buildArgs();
        
        assertPass(INT, DOUBLE);
        assertArgumentValue(0, 20);
        assertArgumentValue(1, 10.0);
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_twoWithDefault_one_B() {
        param(DOUBLE);
        param(DOUBLE, DOUBLE(20.0));
        buildFunc();
        
        ADD_DOUBLE(10.0);
        buildArgs();
        
        assertPass(DOUBLE, DOUBLE);
        assertArgumentValue(0, 10.0);
        assertArgumentValue(1, 20.0);
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_varargs_empty() {
        varargs(VAR);
        buildFunc();
        
        // none
        buildArgs();
        
        assertPass(LIST);
        assertArgumentValue(0, array());
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_varargs_one() {
        varargs(VAR);
        buildFunc();
        
        ADD_INT(100);
        buildArgs();
        
        assertPass(LIST);
        assertArgumentValue(0, array(100));
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_varargs_two() {
        varargs(VAR);
        buildFunc();
        
        ADD_INT(100);
        ADD_STRING("LOL");
        buildArgs();
        
        assertPass(LIST);
        assertArgumentValue(0, array(100, "LOL"));
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_numberA_empty() {
        varargs(NUMBER);
        buildFunc();
        
        buildArgs();
        
        assertPass(LIST);
        assertArgumentValue(0, array());
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_numberA_one_A() {
        varargs(NUMBER);
        buildFunc();
        
        ADD_INT(300);
        buildArgs();
        
        assertPass(LIST);
        assertArgumentValue(0, array(300));
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_numberA_one_B() {
        varargs(NUMBER);
        buildFunc();
        
        ADD_DOUBLE(300.0);
        buildArgs();
        
        assertPass(LIST);
        assertArgumentValue(0, array(300.0));
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_numberA_two_A() {
        varargs(NUMBER);
        buildFunc();
        
        ADD_INT(300);
        ADD_INT(200);
        buildArgs();
        
        assertPass(LIST);
        assertArgumentValue(0, array(300, 200));
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_numberA_two_B() {
        varargs(NUMBER);
        buildFunc();
        
        ADD_DOUBLE(300.0);
        ADD_INT(200);
        buildArgs();
        
        assertPass(LIST);
        assertArgumentValue(0, array(300.0, 200));
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_numberA_two_C() {
        varargs(NUMBER);
        buildFunc();
        
        ADD_INT(300);
        ADD_DOUBLE(200.0);
        buildArgs();
        
        assertPass(LIST);
        assertArgumentValue(0, array(300, 200.0));
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_numberA_two_D() {
        varargs(NUMBER);
        buildFunc();
        
        ADD_DOUBLE(300.0);
        ADD_DOUBLE(200.0);
        buildArgs();
        
        assertPass(LIST);
        assertArgumentValue(0, array(300.0, 200.0));
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_twoWithDeafult_empty() {
        param(INT, INT(15));
        param(INT, INT(30));
        buildFunc();
        
        // none
        buildArgs();
        
        assertPass(INT, INT);
        assertArgumentValue(0, 15);
        assertArgumentValue(1, 30);
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_twoWithDeafult_one_A() {
        param(INT, INT(15));
        param(INT, INT(30));
        buildFunc();
        
        ADD_INT(45);
        buildArgs();
        
        assertPass(INT, INT);
        assertArgumentValue(0, 45);
        assertArgumentValue(1, 30);
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_twoWithDeafult_one_B() {
        param(INT, INT(15));
        param(INT, INT(30));
        buildFunc();
        
        ADD_INT(45);
        ADD_INT(75);
        buildArgs();
        
        assertPass(INT, INT);
        assertArgumentValue(0, 45);
        assertArgumentValue(1, 75);
    }
    
    //----------------------------------------------------------
    
    @Test
    /** This one is a really sneaky edge case, the double will absorb the int. */
    void test_pass_twoWithDeafult_one_C() {
        param(DOUBLE, DOUBLE(15.0));
        param(INT, INT(30));
        buildFunc();
        
        ADD_INT(75);
        buildArgs();
        
        assertPass(DOUBLE, INT);
        assertArgumentValue(0, 75.0);
        assertArgumentValue(1, 30);
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_twoWithDeafult_one_D() {
        param(INT, INT(15));
        param(DOUBLE, DOUBLE(30.0));
        buildFunc();
        
        ADD_INT(75);
        buildArgs();
        
        assertPass(INT, DOUBLE);
        assertArgumentValue(0, 75);
        assertArgumentValue(1, 30.0);
    }
    
    //-------------------
    // Expected Failures
    //-------------------
    
    @Test
    void test_fail_one_empty() {
        param(BOOLEAN);
        buildFunc();
        
        // none
        buildArgs();
        
        assertFail();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_fail_empty_one() {
        buildFunc();
        
        ADD_BOOLEAN(true);
        buildArgs();
        
        assertFail();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_fail_one_one() {
        param(BOOLEAN);
        buildFunc();
        
        ADD_INT(10);
        buildArgs();
        
        assertFail();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_fail_var_empty() {
        param(VAR);
        buildFunc();
        
        // none
        buildArgs();
        
        assertFail();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_fail_var_two() {
        param(VAR);
        buildFunc();
        
        ADD_BOOLEAN(true);
        ADD_INT(34030);
        buildArgs();
        
        assertFail();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_fail_varargs_two_A() {
        varargs(BOOLEAN);
        buildFunc();
        
        ADD_BOOLEAN(true);
        ADD_INT(10);
        buildArgs();
        
        assertFail();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_fail_varargs_two_B() {
        param(INT);
        buildFunc();
        
        ADD_INT(10);
        ADD_BOOLEAN(true);
        buildArgs();
        
        assertFail();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_fail_oneA_one() {
        varargs(BOOLEAN);
        buildFunc();
        
        ADD_CHAR('c');
        buildArgs();
        
        assertFail();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_fail_twoDefault_one() {
        param(BOOLEAN, EnvisionBoolean.TRUE);
        param(INT, EnvisionInt.MAX_VALUE);
        buildFunc();
        
        ADD_CHAR('c');
        buildArgs();
        
        assertFail();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_fail_twoWithVarargs_one() {
        param(BOOLEAN);
        varargs(INT);
        buildFunc();
        
        ADD_INT(1203);
        buildArgs();
        
        assertFail();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_fail_twoDefaultWithVarargs_one() {
        param(BOOLEAN, EnvisionBoolean.TRUE);
        varargs(INT);
        buildFunc();
        
        ADD_CHAR('c');
        buildArgs();
        
        assertFail();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_fail_twoDefaultWithVarargs_two_A() {
        param(BOOLEAN, EnvisionBoolean.TRUE);
        varargs(INT);
        buildFunc();
        
        ADD_CHAR('c');
        ADD_BOOLEAN(true);
        buildArgs();
        
        assertFail();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_fail_twoDefaultWithVarargs_two_B() {
        param(BOOLEAN, EnvisionBoolean.TRUE);
        varargs(INT);
        buildFunc();
        
        ADD_BOOLEAN(true);
        ADD_CHAR('c');
        buildArgs();
        
        assertFail();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_fail_two_empty() {
        param(BOOLEAN);
        param(INT);
        buildFunc();
        
        // none
        buildArgs();
        
        assertFail();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_fail_three_empty() {
        param(BOOLEAN);
        param(CHAR);
        param(INT);
        buildFunc();
        
        // none
        buildArgs();
        
        assertFail();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_fail_four_empty() {
        param(BOOLEAN);
        param(CHAR);
        param(INT);
        param(DOUBLE);
        buildFunc();
        
        // none
        buildArgs();
        
        assertFail();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_fail_five_empty() {
        param(BOOLEAN);
        param(CHAR);
        param(INT);
        param(DOUBLE);
        param(LIST);
        buildFunc();
        
        // none
        buildArgs();
        
        assertFail();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_fail_varargsTypeMismatch() {
        varargs(BOOLEAN);
        buildFunc();
        
        ADD_INT(234);
        buildArgs();
        
        assertFail();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_fail_number_notNumber() {
        param(NUMBER);
        buildFunc();
        
        ADD_CHAR('c');
        buildArgs();
        
        assertFail();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_fail_numberA_notNumber() {
        varargs(NUMBER);
        buildFunc();
        
        ADD_BOOLEAN(true);
        buildArgs();
        
        assertFail();
    }
    
    //=========================
    // Internal Helper Methods
    //=========================
    
    protected EnvisionParameter varargs(IDatatype type) {
        return param(type, "TEST_VARARGS", null, true);
    }
    
    protected EnvisionParameter param(IDatatype type) { return param(type, "TEST_PARAM", null, false); }
    protected EnvisionParameter param(IDatatype type, boolean isVarargs) { return param(type, "TEST_PARAM", null, isVarargs); }
    protected EnvisionParameter param(IDatatype type, EnvisionObject defaultVal) { return param(type, "TEST_PARAM", defaultVal, false); }
    protected EnvisionParameter param(IDatatype type, String name, EnvisionObject defaultVal, boolean isVarargs) {
        return params.addR(new EnvisionParameter(type, name, defaultVal, isVarargs));
    }
    
    protected ParameterData buildFunc() {
        func = ParameterData.fromParameters(params);
        params.clear();
        return func;
    }
    
    protected EnvisionObject[] buildArgs() {
        args = new EnvisionObject[objects.size()];
        for (int i = 0; i < objects.size(); i++) {
            args[i] = objects.get(i);
        }
        objects.clear();
        return args;
    }
    
    protected EnvisionBoolean ADD_BOOLEAN(boolean value) { var r = BOOLEAN(value); objects.add(r); return r; }
    protected EnvisionChar ADD_CHAR(char value) { var r = CHAR(value); objects.add(r); return r; }
    protected EnvisionInt ADD_INT(Number num) { var r = INT(num); objects.add(r); return r; }
    protected EnvisionDouble ADD_DOUBLE(Number num) { var r = DOUBLE(num); objects.add(r); return r; }
    protected EnvisionString ADD_STRING(String value) { var r = STRING(value); objects.add(r); return r; }
    protected EnvisionNull ADD_NULL() { var r = NULL(); objects.add(r); return r; }
    
    protected EnvisionBoolean BOOLEAN(boolean value) { return EnvisionBooleanClass.valueOf(value); }
    protected EnvisionChar CHAR(char value) { return EnvisionCharClass.valueOf(value); }
    protected EnvisionInt INT(Number num) { return EnvisionIntClass.valueOf(num); }
    protected EnvisionDouble DOUBLE(Number num) { return EnvisionDoubleClass.valueOf(num); }
    protected EnvisionString STRING(String value) { return EnvisionStringClass.valueOf(value); }
    protected EnvisionNull NULL() { return EnvisionNull.NULL; }
    
    protected Object[] array(Object... values) {
        return values;
    }
    
    protected void testEachType(IDatatype... types) {
        for (IDatatype type : types) {
            param(type);
            buildFunc();
            if (Primitives.BOOLEAN.compare(type)) ADD_BOOLEAN(true);
            else if (Primitives.CHAR.compare(type)) ADD_CHAR('a');
            else if (Primitives.INT.compare(type)) ADD_INT(10);
            else if (Primitives.DOUBLE.compare(type)) ADD_DOUBLE(Math.PI);
            else if (Primitives.STRING.compare(type)) ADD_STRING("Banana");
            buildArgs();
            assertPass(type);
            if (Primitives.BOOLEAN.compare(type)) assertArgumentValue(0, true);
            else if (Primitives.CHAR.compare(type)) assertArgumentValue(0, 'a');
            else if (Primitives.INT.compare(type)) assertArgumentValue(0, 10);
            else if (Primitives.DOUBLE.compare(type)) assertArgumentValue(0, Math.PI);
            else if (Primitives.STRING.compare(type)) assertArgumentValue(0, "Banana");
        }
    }
    
    protected void assertPass(IDatatype... expectedTypes) {
        mapped = func.mapArgumentsIntoParameters(args);
        assertEquals(expectedTypes.length, mapped.size());
        for (int i = 0; i < expectedTypes.length; i++) {
            IDatatype expected = expectedTypes[i];
            IDatatype actual = mapped.get(i).getDatatype();
            assertTrue(expected.compare(actual));
        }
    }
    
    protected void assertArgumentValue(int index, Object expected) {
        EnvisionObject argument = mapped.get(index);
        assertValueMatch(argument, expected);
    }
    
    protected void assertValueMatch(EnvisionObject given, Object expected) {
        if (given instanceof EnvisionBoolean b) assertEquals(expected, b.bool_val);
        else if (given instanceof EnvisionChar c) assertEquals(expected, c.char_val);
        else if (given instanceof EnvisionInt i) assertEquals(((Number) expected).longValue(), i.int_val);
        else if (given instanceof EnvisionDouble d) assertEquals(((Number) expected).doubleValue(), d.double_val);
        else if (given instanceof EnvisionString s) assertEquals(expected, s.string_val);
        else if (given instanceof EnvisionList l) {
            final int len = Array.getLength(expected);
            assertEquals(len, (int) l.size_i());
            for (int i = 0; i < len; i++) {
                EnvisionObject listValue = l.get(i);
                assertValueMatch(listValue, Array.get(expected, i));
            }
        }
    }
    
    protected void assertFail(IDatatype... expectedTypes) {
        try {
            mapped = func.mapArgumentsIntoParameters(args);
            if (expectedTypes.length != mapped.size()) return;
            boolean passed = true;
            if (expectedTypes.length == 0) passed = false;
            for (int i = 0; i < expectedTypes.length; i++) {
                IDatatype expected = expectedTypes[i];
                IDatatype actual = mapped.get(i).getDatatype();
                passed &= (expected.compare(actual));
            }
            assertFalse(passed); 
        }
        catch (InvalidArgumentError e) {
            // if it threw this, that's good :D
        }
    }
    
}
