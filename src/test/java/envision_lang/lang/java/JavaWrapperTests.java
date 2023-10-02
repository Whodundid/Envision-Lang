package envision_lang.lang.java;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import envision_lang.EnvisionLangTest;
import envision_lang.lang.java.objects.TestPoint;

class JavaWrapperTests extends EnvisionLangTest {
    
    @BeforeEach
    void prepare() {
        //interpreter
    }
    
    @Test
    void wrapJavaObject_TestPoint_init_noArgs() {
        TestPoint t = new TestPoint();
        
        // perform assertions on Java side
        assertNotNull(t);
        assertEquals(0, t.getX());
        assertEquals(0, t.getY());
        
        TestPoint t2 = t.add(30, 30);
        assertNotEquals(t, t2);
        assertEquals(30, t2.getX());
        assertEquals(30, t2.getY());
        
        // perform assertions on Envision side
        code("""
             assertNotNull(TestPoint)
             assertNotNull(t)
             assertEquals(t.type(), TestPoint)
             assertNotNull(t.x, t.y)
             assertEquals(0, t.x)
             assertEquals(0, t.y)
             
             TestPoint t2 = t.add(30, 30)
             assertNotEquals(t, t2)
             assertEquals(30, t2.x)
             assertEquals(30, t2.y)
             """);
        
        injectJavaObject("t", t);
        execute();
    }
    
    @Test
    void wrapJavaObject_TestPoint_init_args() {
        TestPoint t = new TestPoint(7, 33);
        
        // perform assertions on Java side
        assertNotNull(t);
        assertEquals(7, t.getX());
        assertEquals(33, t.getY());
        
        // perform assertions on Envision side
        code("""
             assertNotNull(TestPoint)
             assertNotNull(t)
             assertEquals(t.type(), TestPoint)
             assertNotNull(t.x, t.y)
             assertEquals(7, t.x)
             assertEquals(33, t.y)
             """);
        
        injectJavaObject("t", t);
        execute();
    }
    
}
