package envision_lang.lang.datatypes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import envision_lang.EnvisionLangTest;
import envision_lang.lang.EnvisionObject;

public class Test_Character extends EnvisionLangTest {
    
    //==================================================
    
    @BeforeEach
    protected void setup() {
        scope().clear();
    }
    
    //==================================================
    
    /**
     * Tests that a new char defaults to the null char.
     */
    @Test
    void test_zero_new() {
        EnvisionChar c = EnvisionCharClass.newChar();
        
        assertNotNull(c);
        assertEquals('\0', c.char_val);
    }
    
    //=========================================================================================
    
    /**
     * Tests that the value of '\0' defaults to the null char.
     */
    @Test
    void test_zero_valueOf() {
        EnvisionChar c = EnvisionCharClass.valueOf('\0');
        
        assertNotNull(c);
        assertEquals('\0', c.char_val);
    }
    
    //=========================================================================================
    
    /**
     * Verifies that creating two new chars with the same value produces the
     * same numeric value in both but a different hash code in each.
     */
    @Test
    void test_new_different() {
        EnvisionChar a = EnvisionCharClass.newChar('\0');
        EnvisionChar b = EnvisionCharClass.newChar('\0');
        
        assertNotNull(a);
        assertNotNull(b);
        // values should still match
        assertEquals(a, b);
        // but hash codes should be different
        assertNotEquals(a.hashCode(), b.hashCode());
    }
    
    //=========================================================================================
    
    @Test
    void test_add() {
        EnvisionChar a = defChar("a", 'a');
        EnvisionChar b = defChar("b", 'b');
        EnvisionObject c;
        
        assertNotNull(a);
        assertNotNull(b);
        
        assertEquals('a', a.char_val);
        assertEquals('b', b.char_val);
        
        execute("""
                
                c = a + b
                
                """);
        
        a = get("a");
        b = get("b");
        c = get("c");
        
        assertNotNull(a);
        assertNotNull(b);
        assertNotNull(c);
        
        assertInstanceOf(EnvisionString.class, c);
        
        assertEquals('a', a.char_val);
        assertEquals('b', b.char_val);
        assertEquals("ab", ((EnvisionString) c).string_val);
    }
    
    
    //=========================================================================================
    
    @Test
    void test_mulAdd() {
        EnvisionChar a = defChar("a", 'a');
        EnvisionObject b;
        
        assertNotNull(a);
        
        assertEquals('a', a.char_val);
        
        execute("""
                
                b = a * 5
                
                """);
        
        a = get("a");
        b = get("b");
        
        assertNotNull(a);
        assertNotNull(b);
        
        assertInstanceOf(EnvisionString.class, b);
        
        assertEquals('a', a.char_val);
        assertEquals("aaaaa", ((EnvisionString) b).string_val);
    }
}
