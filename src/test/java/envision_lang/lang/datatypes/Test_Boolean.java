package envision_lang.lang.datatypes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import envision_lang.EnvisionLangTest;

public class Test_Boolean extends EnvisionLangTest {
    
    //==================================================
    
    @BeforeEach
    protected void setup() {
        scope().clear();
    }
    
    //==================================================
    
    /**
     * Tests that a new boolean defaults to 'false'.
     * <p>
     * NOTE: this should create an entirely new boolean in memory that should
     * not be equivalent to the static 'FALSE' boolean.
     */
    @Test
    void test_default_new() {
        EnvisionBoolean b = EnvisionBooleanClass.newBoolean();
        
        assertNotNull(b);
        assertNotEquals(EnvisionBoolean.FALSE.hashCode(), b.hashCode());
        assertEquals(false, b.bool_val);
    }
    
    //=========================================================================================
    
    /**
     * Tests that creating a new 'FALSE' boolean correctly initializes with
     * the value of true.
     * <p>
     * NOTE: this should create an entirely new boolean in memory that
     * should not be equivalent to the static 'FALSE' boolean.
     */
    @Test
    void test_false_new() {
        EnvisionBoolean b = EnvisionBooleanClass.newBoolean(false);
        
        assertNotNull(b);
        assertNotEquals(EnvisionBoolean.FALSE.hashCode(), b.hashCode());
        assertEquals(false, b.bool_val);
    }
    
    //=========================================================================================
    
    /**
     * Tests that creating a new 'TRUE' boolean correctly initializes with
     * the value of true.
     * <p>
     * NOTE: this should create an entirely new boolean in memory that
     * should not be equivalent to the static 'TRUE' boolean.
     */
    @Test
    void test_true_new() {
        EnvisionBoolean b = EnvisionBooleanClass.newBoolean(true);
        
        assertNotNull(b);
        assertNotEquals(EnvisionBoolean.TRUE.hashCode(), b.hashCode());
        assertEquals(true, b.bool_val);
    }
    
    //=========================================================================================
    
    /**
     * Tests that the value of 'FALSE' correctly references the existing
     * static 'FALSE' boolean in memory.
     * <p>
     * NOTE: this should NOT create an entirely new boolean in memory.
     */
    @Test
    void test_false_valueOf() {
        EnvisionBoolean b = EnvisionBooleanClass.valueOf(false);
        
        assertNotNull(b);
        assertEquals(EnvisionBoolean.FALSE.hashCode(), b.hashCode());
        assertEquals(false, b.bool_val);
    }
    
    //=========================================================================================
    
    /**
     * Tests that the value of 'TRUE' correctly references the existing
     * static 'TRUE' boolean in memory.
     * <p>
     * NOTE: this should NOT create an entirely new boolean in memory.
     */
    @Test
    void test_true_valueOf() {
        EnvisionBoolean b = EnvisionBooleanClass.valueOf(true);
        
        assertNotNull(b);
        assertEquals(EnvisionBoolean.TRUE.hashCode(), b.hashCode());
        assertEquals(true, b.bool_val);
    }
    
    //=========================================================================================
    
    /**
     * Verifies that creating two new booleans with the same 'FALSE' value
     * produces the same logical value in both but a different hash code in
     * each.
     */
    @Test
    void test_new_different_false() {
        EnvisionBoolean a = EnvisionBooleanClass.newBoolean(false);
        EnvisionBoolean b = EnvisionBooleanClass.newBoolean(false);
        
        assertNotNull(a);
        assertNotNull(b);
        assertNotEquals(EnvisionBoolean.FALSE.hashCode(), a.hashCode());
        assertNotEquals(EnvisionBoolean.FALSE.hashCode(), b.hashCode());
        // values should still match
        assertEquals(a, b);
        // but hash codes should be different
        assertNotEquals(a.hashCode(), b.hashCode());
    }
    
    //=========================================================================================
    
    /**
     * Verifies that creating two new booleans with the same 'TRUE' value
     * produces the same logical value in both but a different hash code in
     * each.
     */
    @Test
    void test_new_different_true() {
        EnvisionBoolean a = EnvisionBooleanClass.newBoolean(true);
        EnvisionBoolean b = EnvisionBooleanClass.newBoolean(true);
        
        assertNotNull(a);
        assertNotNull(b);
        assertNotEquals(EnvisionBoolean.TRUE.hashCode(), a.hashCode());
        assertNotEquals(EnvisionBoolean.TRUE.hashCode(), b.hashCode());
        // values should still match
        assertEquals(a, b);
        // but hash codes should be different
        assertNotEquals(a.hashCode(), b.hashCode());
    }
    
    //=========================================================================================
    
    /**
     * Verifies that creating two booleans with the same value of 'FALSE'
     * will result in the same reference of the static 'FALSE' boolean for
     * each.
     */
    @Test
    void test_valueOf_same_false() {
        EnvisionBoolean a = EnvisionBooleanClass.valueOf(false);
        EnvisionBoolean b = EnvisionBooleanClass.valueOf(false);
        
        assertNotNull(a);
        assertNotNull(b);
        assertEquals(EnvisionBoolean.FALSE.hashCode(), a.hashCode());
        assertEquals(EnvisionBoolean.FALSE.hashCode(), b.hashCode());
        // values should match
        assertEquals(a, b);
        // hash codes should also match
        assertEquals(a.hashCode(), b.hashCode());
    }
    
    //=========================================================================================
    
    /**
     * Verifies that creating two booleans with the same value of 'TRUE'
     * will result in the same reference of the static 'TRUE' boolean for
     * each.
     */
    @Test
    void test_valueOf_same_true() {
        EnvisionBoolean a = EnvisionBooleanClass.valueOf(true);
        EnvisionBoolean b = EnvisionBooleanClass.valueOf(true);
        
        assertNotNull(a);
        assertNotNull(b);
        assertEquals(EnvisionBoolean.TRUE.hashCode(), a.hashCode());
        assertEquals(EnvisionBoolean.TRUE.hashCode(), b.hashCode());
        // values should match
        assertEquals(a, b);
        // hash codes should also match
        assertEquals(a.hashCode(), b.hashCode());
    }
    
    //=========================================================================================
    
    @Test
    void test_code_define_false() {
        execute("""
                
                boolean b = false
                
                """);
        
        EnvisionBoolean b = get("b");
        assertNotNull(b);
        assertEquals(EnvisionBoolean.FALSE, b);
        assertEquals(false, b.bool_val);
    }
    
    //=========================================================================================
    
    @Test
    void test_code_define_true() {
        execute("""
                
                boolean b = true
                
                """);
        
        EnvisionBoolean b = get("b");
        assertNotNull(b);
        assertEquals(EnvisionBoolean.TRUE, b);
        assertEquals(true, b.bool_val);
    }
    
    //=========================================================================================
    
    @Test
    void test_negate_false() {
        execute("""
                
                boolean b = !false
                
                """);
        
        EnvisionBoolean b = get("b");
        assertNotNull(b);
        assertEquals(EnvisionBoolean.TRUE, b);
        assertEquals(true, b.bool_val);
    }
    
    //=========================================================================================
    
    @Test
    void test_negate_true() {
        execute("""
                
                boolean b = !true
                
                """);
        
        EnvisionBoolean b = get("b");
        assertNotNull(b);
        assertEquals(EnvisionBoolean.FALSE, b);
        assertEquals(false, b.bool_val);
    }
    
}
