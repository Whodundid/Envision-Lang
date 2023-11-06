package envision_lang.lang.functions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import envision_lang.EnvisionLangTest;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionInt;
import envision_lang.lang.datatypes.EnvisionNull;
import envision_lang.lang.datatypes.EnvisionVoid;

public class Test_Function extends EnvisionLangTest {
    
    //==================================================
    
    @BeforeEach
    protected void setup() {
        scope().clear();
    }
    
    //==================================================
    
    @Test
    void test_void() {
        EnvisionObject r = get("r");
        assertNull(r);
        
        execute("""
                
                func test() {
                    // nothing
                }
                
                r = test()
                
                """);
        
        r = get("r");
        assertNotNull(r);
        assertEquals(EnvisionVoid.VOID, r);
    }
    
    //==================================================
    
    @Test
    void test_return_null() {
        EnvisionObject r = get("r");
        assertNull(r);
        
        execute("""
                
                func test() {
                    return null
                }
                
                r = test()
                
                """);
        
        r = get("r");
        assertNotNull(r);
        assertEquals(EnvisionNull.NULL, r);
    }
    
    //==================================================
    
    @Test
    void test_return_null_lambda() {
        EnvisionObject r = get("r");
        assertNull(r);
        
        execute("""
                
                func test() -> null
                
                r = test()
                
                """);
        
        r = get("r");
        assertNotNull(r);
        assertEquals(EnvisionNull.NULL, r);
    }
    
    
    //==================================================
    
    @Test
    void test_nestedFunction() {
        EnvisionObject r = get("r");
        assertNull(r);
        
        execute("""
                
                func test() {
                    func banana() -> 42
                    return banana()
                }
                
                r = test()
                
                """);
        
        r = get("r");
        assertNotNull(r);
        assertInstanceOf(EnvisionInt.class, r);
        assertEquals(42, ((EnvisionInt) r).int_val);
    }
    
}
