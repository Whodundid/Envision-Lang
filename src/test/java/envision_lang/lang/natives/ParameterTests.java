package envision_lang.lang.natives;

import static org.junit.jupiter.api.Assertions.*;
import static envision_lang.lang.natives.Primitives.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionBoolean;
import envision_lang.lang.datatypes.EnvisionInt;
import eutil.datatypes.util.EList;

/**
 * A series of tests to ensure that function parameter mapping is correctly
 * working when given various sets of arguments.
 * 
 * @author Hunter Bragg
 */
public class ParameterTests {
    
    //========
    // Fields
    //========
    
    /**
     * Effectively a working stack that stores temporary parameters until
     * they are built into parameter data objects.
     */
    protected final EList<EnvisionParameter> params = EList.newList();
    
    protected ParameterData func;
    protected ParameterData args;
    
    //=======
    // Setup
    //=======
    
    @BeforeEach
    void setup() {
        NativeTypeManager.init();
        
        params.clear();
        func = null;
        args = null;
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
    void test_pass_varargs_empty() {
        varargs(VAR);
        buildFunc();
        
        // none
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_oneDefault_empty() {
        param(BOOLEAN, EnvisionBoolean.TRUE);
        buildFunc();
        
        // none
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_one_one() {
        param(BOOLEAN);
        buildFunc();
        
        param(BOOLEAN);
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_oneDefault_one() {
        param(BOOLEAN, EnvisionBoolean.TRUE);
        buildFunc();
        
        param(BOOLEAN);
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_var_one() {
        param(VAR);
        buildFunc();
        
        param(BOOLEAN);
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_varA_one() {
        varargs(VAR);
        buildFunc();
        
        param(BOOLEAN);
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_oneA_empty() {
        varargs(BOOLEAN);
        buildFunc();
        
        // none
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_oneA_two() {
        varargs(BOOLEAN);
        buildFunc();
        
        param(BOOLEAN);
        param(BOOLEAN);
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_two_two() {
        param(BOOLEAN);
        param(INT);
        buildFunc();
        
        param(BOOLEAN);
        param(INT);
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_varA_two() {
        varargs(VAR);
        buildFunc();
        
        param(BOOLEAN);
        param(INT);
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_twoDeafult_empty() {
        param(BOOLEAN, EnvisionBoolean.TRUE);
        param(INT, EnvisionInt.MAX_VALUE);
        buildFunc();
        
        // none
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_twoDeafult_two() {
        param(BOOLEAN, EnvisionBoolean.TRUE);
        param(INT, EnvisionInt.MAX_VALUE);
        buildFunc();
        
        param(BOOLEAN);
        param(INT);
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_twoDefault_one_A() {
        param(BOOLEAN, EnvisionBoolean.TRUE);
        param(INT, EnvisionInt.MAX_VALUE);
        buildFunc();
        
        param(BOOLEAN);
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_twoDefault_one_B() {
        param(BOOLEAN, EnvisionBoolean.TRUE);
        param(INT, EnvisionInt.MAX_VALUE);
        buildFunc();
        
        param(INT);
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_twoWithVarargs_one() {
        param(BOOLEAN);
        varargs(INT);
        buildFunc();
        
        param(BOOLEAN);
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_twoDefaultWithVarargs_one_A() {
        param(BOOLEAN, EnvisionBoolean.TRUE);
        varargs(INT);
        buildFunc();
        
        param(BOOLEAN);
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_twoDefaultWithVarargs_one_B() {
        param(BOOLEAN, EnvisionBoolean.TRUE);
        varargs(INT);
        buildFunc();
        
        param(INT);
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_twoWithVarargs_two_A() {
        param(BOOLEAN);
        varargs(INT);
        buildFunc();
        
        param(BOOLEAN);
        param(INT);
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_twoWithVarargs_three() {
        param(BOOLEAN);
        varargs(INT);
        buildFunc();
        
        param(BOOLEAN);
        param(INT);
        param(INT);
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_twoDefaultWithVarargs_two_A() {
        param(BOOLEAN, EnvisionBoolean.TRUE);
        varargs(INT);
        buildFunc();
        
        param(BOOLEAN);
        param(INT);
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_twoDefaultWithVarargs_two_B() {
        param(BOOLEAN, EnvisionBoolean.TRUE);
        varargs(INT);
        buildFunc();
        
        param(INT);
        param(INT);
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_twoDefaultWithVarargs_empty() {
        param(BOOLEAN, EnvisionBoolean.TRUE);
        varargs(INT);
        buildFunc();
        
        // none
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_three_three() {
        param(BOOLEAN);
        param(CHAR);
        param(INT);
        buildFunc();
        
        param(BOOLEAN);
        param(CHAR);
        param(INT);
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_four_four() {
        param(BOOLEAN);
        param(CHAR);
        param(INT);
        param(DOUBLE);
        buildFunc();
        
        param(BOOLEAN);
        param(CHAR);
        param(INT);
        param(DOUBLE);
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_number_number() {
        param(NUMBER);
        buildFunc();
        
        param(NUMBER);
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_number_int() {
        param(NUMBER);
        buildFunc();
        
        param(INT);
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_number_double() {
        param(NUMBER);
        buildFunc();
        
        param(DOUBLE);
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_numberA_empty() {
        varargs(NUMBER);
        buildFunc();
        
        // none
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_numberA_int() {
        varargs(NUMBER);
        buildFunc();
        
        param(INT);
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_numberA_double() {
        varargs(NUMBER);
        buildFunc();
        
        param(DOUBLE);
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_numberA_number() {
        varargs(NUMBER);
        buildFunc();
        
        param(NUMBER);
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_numberA_two() {
        varargs(NUMBER);
        buildFunc();
        
        param(INT);
        param(DOUBLE);
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_numberNumberA_int() {
        param(NUMBER);
        varargs(NUMBER);
        buildFunc();
        
        param(INT);
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_numberNumberA_double() {
        param(NUMBER);
        varargs(NUMBER);
        buildFunc();
        
        param(DOUBLE);
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_numberNumberA_number() {
        param(NUMBER);
        varargs(NUMBER);
        buildFunc();
        
        param(NUMBER);
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_intNumberA_int() {
        param(INT);
        varargs(NUMBER);
        buildFunc();
        
        param(INT);
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_doubleNumberA_double() {
        param(DOUBLE);
        varargs(NUMBER);
        buildFunc();
        
        param(DOUBLE);
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_intNumberA_intInt() {
        param(INT);
        varargs(NUMBER);
        buildFunc();
        
        param(INT);
        param(INT);
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_intNumberA_intDouble() {
        param(INT);
        varargs(NUMBER);
        buildFunc();
        
        param(INT);
        param(DOUBLE);
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_doubleNumberA_intInt() {
        param(DOUBLE);
        varargs(NUMBER);
        buildFunc();
        
        param(DOUBLE);
        param(INT);
        buildArgs();
        
        assertPass();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_pass_doubleNumberA_intDouble() {
        param(DOUBLE);
        varargs(NUMBER);
        buildFunc();
        
        param(DOUBLE);
        param(DOUBLE);
        buildArgs();
        
        assertPass();
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
        // none
        buildFunc();
        
        param(BOOLEAN);
        buildArgs();
        
        assertFail();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_fail_one_one() {
        param(BOOLEAN);
        buildFunc();
        
        param(INT);
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
        
        param(BOOLEAN);
        param(INT);
        buildArgs();
        
        assertFail();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_fail_varargs_two_A() {
        varargs(BOOLEAN);
        buildFunc();
        
        param(BOOLEAN);
        param(INT);
        buildArgs();
        
        assertFail();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_fail_varargs_two_B() {
        varargs(INT);
        buildFunc();
        
        param(BOOLEAN);
        param(INT);
        buildArgs();
        
        assertFail();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_fail_oneA_one() {
        varargs(BOOLEAN);
        buildFunc();
        
        param(CHAR);
        buildArgs();
        
        assertFail();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_fail_twoDefault_one() {
        param(BOOLEAN, EnvisionBoolean.TRUE);
        param(INT, EnvisionInt.MAX_VALUE);
        buildFunc();
        
        param(CHAR);
        buildArgs();
        
        assertFail();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_fail_twoWithVarargs_one() {
        param(BOOLEAN);
        varargs(INT);
        buildFunc();
        
        param(INT);
        buildArgs();
        
        assertFail();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_fail_twoDefaultWithVarargs_one() {
        param(BOOLEAN, EnvisionBoolean.TRUE);
        varargs(INT);
        buildFunc();
        
        param(CHAR);
        buildArgs();
        
        assertFail();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_fail_twoDefaultWithVarargs_two_A() {
        param(BOOLEAN, EnvisionBoolean.TRUE);
        varargs(INT);
        buildFunc();
        
        param(CHAR);
        param(BOOLEAN);
        buildArgs();
        
        assertFail();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_fail_twoDefaultWithVarargs_two_B() {
        param(BOOLEAN, EnvisionBoolean.TRUE);
        varargs(INT);
        buildFunc();
        
        param(BOOLEAN);
        param(CHAR);
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
        
        param(INT);
        buildArgs();
        
        assertFail();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_fail_number_notNumber() {
        param(NUMBER);
        buildFunc();
        
        param(BOOLEAN);
        buildArgs();
        
        assertFail();
    }
    
    //----------------------------------------------------------
    
    @Test
    void test_fail_numberA_notNumber() {
        varargs(NUMBER);
        buildFunc();
        
        param(BOOLEAN);
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
    
    protected ParameterData buildFunc() { return func = build(); }
    protected ParameterData buildArgs() { return args = build(); }
    protected ParameterData build() {
        var data = ParameterData.fromParameters(params);
        params.clear();
        return data;
    }
    
    protected void assertPass() { assertTrue(func.compare(args)); }
    protected void assertFail() { assertFalse(func.compare(args)); }
    
}
