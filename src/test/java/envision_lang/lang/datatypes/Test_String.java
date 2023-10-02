package envision_lang.lang.datatypes;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import envision_lang.EnvisionLangTest;

class Test_String extends EnvisionLangTest {
    
    //==================================================
    
    @BeforeEach
    protected void setup() {
        scope().clear();
    }
    
    //==================================================
    
    /**
     * Tests that a new string defaults to "".
     */
    @Test
    void test_string_empty() {
        EnvisionString s = EnvisionStringClass.newString();
        assertNotNull(s);
        assertNotNull(s.string_val);
        assertEquals("", s.toString());
    }
    
    //=========================================================================================
    
    /**
     * Tests that the value of "" results in the EMPTY_STRING.
     */
    @Test
    void test_empty_valueOf() {
        EnvisionString s = EnvisionStringClass.valueOf("");
        
        assertNotNull(s);
        assertNotNull(s.string_val);
        assertEquals("", s.toString());
        assertEquals(EnvisionString.EMPTY_STRING, s);
    }
    
    //=========================================================================================
    
    /**
     * Tests that a value of "Banana" results in the EnvisionString of "Banana".
     */
    @Test
    void test_valueOf() {
        EnvisionString s = EnvisionStringClass.valueOf("Banana");
        
        assertNotNull(s);
        assertNotNull(s.string_val);
        assertEquals("Banana", s.toString());
    }
    
    //=========================================================================================
    
    /**
     * Tests that strings can be properly concatenated with other strings.
     */
    @Test
    void test_concatenation_with_string() {
        scope().defString("base", "BASE");
        scope().defString("target", "_TARGET");
        
        EnvisionString base = get("base");
        EnvisionString target = get("target");
        EnvisionString result = get("result");
        
        assertNotNull(base);
        assertNotNull(target);
        assertNull(result);
        
        assertNotNull(base.string_val);
        assertNotNull(target.string_val);
        assertEquals("BASE", base.toString());
        assertEquals("_TARGET", target.toString());
        
        var add_stmt = stmt("""
                            
                            string result = base + target
                            
                            """);
        
        interpreter.execute(add_stmt);
        
        
        base = get("base");
        target = get("target");
        result = get("result");
        
        assertNotNull(base);
        assertNotNull(target);
        assertNotNull(result);
        assertNotNull(base.string_val);
        assertNotNull(target.string_val);
        assertNotNull(result.string_val);
        assertEquals("BASE", base.toString());
        assertEquals("_TARGET", target.toString());
        assertEquals("BASE_TARGET", result.toString());
    }
    
    //=========================================================================================
    
    /**
     * Tests that strings can be properly concatenated with other strings.
     */
    @Test
    void test_concatenation_with_boolean() {
        scope().defString("base", "BASE");
        scope().defBool("target", true);
        
        EnvisionString base = get("base");
        EnvisionBoolean target = get("target");
        EnvisionString result = get("result");
        
        assertNotNull(base);
        assertNotNull(target);
        assertNull(result);
        
        assertNotNull(base.string_val);
        assertEquals("BASE", base.toString());
        assertEquals(true, target.bool_val);
        
        var add_stmt = stmt("""
                            
                            string result = base + target
                            
                            """);
        
        interpreter.execute(add_stmt);
        
        
        base = get("base");
        target = get("target");
        result = get("result");
        
        assertNotNull(base);
        assertNotNull(target);
        assertNotNull(result);
        assertNotNull(base.string_val);
        assertNotNull(result.string_val);
        assertEquals("BASE", base.toString());
        assertEquals(true, target.bool_val);
        assertEquals("BASEtrue", result.toString());
    }
    
}
