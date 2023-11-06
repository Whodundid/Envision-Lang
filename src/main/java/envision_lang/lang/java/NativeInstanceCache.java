package envision_lang.lang.java;

import java.util.HashMap;
import java.util.Map;

/**
 * Effectively, an internal cache to keep track of all Java instances made
 * in Java and then injected (or passed) into Envision. This cache helps to
 * identify when an existing EnvisionJavaObject has already been mapped to
 * some existing Java Object. Without this cache, there could potentially
 * be numerous duplicate EnvisionJavaObjects built that are all wrapping
 * the same Java Object.
 * 
 * <p>
 * Take the following scenario for instance:
 * 
 * <pre>
 * {@code
 * =============
 * -- IN JAVA --
 * =============
 * 
 * @EClass
 * public class TestPoint {
 *     
 *     int x, y;
 *     
 *     public TestPoint(int x, int y) {
 *         this.x = x;
 *         this.y = y;
 *     }
 *     
 *     public TestPoint add(TestPoint p) {
 *         return new TestPoint(x + p.x, y + p.y);
 *     }
 *     
 *     public TestPoint example(TestPoint p) {
 *         return p;
 *     }
 * }
 * 
 * {@code
 * =================
 * -- IN ENVISION --
 * =================
 * 
 * t1 = TestPoint(5, 5)
 * t2 = TestPoint(10, 10)
 * 
 * t3 = t1.add(t2)
 * 
 * t4 = t3.example(t3)
 * }
 * </pre>
 * 
 * In this example: The Envision code attempts to call the 'add(TestPoint)'
 * method on t1 which relates back to the Java 'public TestPoint
 * add(TestPoint p)'. The code for the Java side creates a new TestPoint
 * and then returns this new instance back to the Envision side through the
 * EnvisionBridge. In order to return this object back, the resultant Java
 * TestPoint will need to be wrapped in a new EnvisionJavaObject in order
 * for it to be referenced natively within Envision. In this scenario, it
 * is correct to wrap the resulting TestPoint into a new wrapper instance
 * as it never existed before this method was called. But in the next
 * example, the following code will demonstrate what happens when this
 * isn't the case.
 * <p>
 * The Envision 't4 = t3.example(t3)' statement directly references the
 * Java side of the 'TestPoint.example(TestPoint)' method. In essence, this
 * method will effectively return its own input. To Java, this operation is
 * straight forward, simply place the exact same reference back onto the
 * return position of the stack frame and then complete the method call. To
 * Envision however, there is practically no way of knowing that the
 * resulting object returned from the 'example(TestPoint)' method on the
 * Java side is the same instance that was passed in the first place.
 * Naively, Envision would wrap this returned TestPoint into another
 * EnvisionJavaObject instance resulting in multiple unique Envision
 * objects created to represent the exact same object in Java. Furthermore,
 * calling such code multiple times over, in a loop for instance, would
 * result in a duplicate wrapper being created each time the function is
 * called when in actuality, there was only ever one object to even exist
 * at any time.
 * <p>
 * Realistically speaking, this example can be expanded to any situation in
 * which objects in Java are handed across to the Envision side or
 * vice-versa. As such, the added memory and CPU overhead of these actions
 * can start to degrade performance on the host Java Virtual Machine which
 * is having to manage all of these duplicate entries in terms of garbage
 * collection and pointer allocation.
 * <p>
 * In order to address this issue of incorrectly creating duplicate
 * EnvisionJavaObjects to wrap the exact same pointer when passed across
 * the Envision/Java bridge, this cache will be used to keep track of all
 * known instances of Java Objects that exist within Envision.
 */
public class NativeInstanceCache {
    
    //========
    // Fields
    //========
    
    private EnvisionJavaClass wrapperClass;
    
    /**
     * The internal instance map which maps each EnvisionJavaObject to the
     * wrapped Java Object's integer hash code.
     */
    private Map<Integer, EnvisionJavaObject> instanceMap = new HashMap<>();
    
    //==============
    // Constructors
    //==============
    
    NativeInstanceCache(EnvisionJavaClass theClassIn) {
        wrapperClass = theClassIn;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public String toString() {
        return instanceMap.toString();
    }
    
    //=========
    // Methods
    //=========
    
    /**
     * Returns an EnvisionJavaObject that is mapped to the given 'javaObject'
     * by hashCode value.
     * <p>
     * If this cache does not contain an EnvisionJavaClass for the given
     * 'javaObject' then this is very likely the first time Envision has seen
     * the given Java object.
     * 
     * @param javaObject The Java object to find a wrapping EnvisionJavaObject
     *                   for
     * 
     * @return An EnvisionJavaObject which already wraps the given Java object
     *         instance
     */
    public EnvisionJavaObject get(Object javaObject) {
        return instanceMap.get(javaObject.hashCode());
    }
    
    /**
     * Stores the given EnvisionJavaObject instance in this cache for future
     * lookup.
     * 
     * @param envisionJavaObject The wrapper to store
     * 
     * @return Any existing EnvisionJavaObject that was already under the
     *         wrapped Java Object's hashCode (should almost always be null)
     */
    public EnvisionJavaObject store(EnvisionJavaObject envisionJavaObject) {
        final Object javaObject = envisionJavaObject.getJavaObjectInstance();
        final int hashCode = javaObject.hashCode();
        return instanceMap.put(hashCode, envisionJavaObject);
    }
    
    /**
     * Returns true if this cache contains an EnvisionJavaObject which already
     * wraps the given Java object instance.
     * 
     * @param javaObject The object to check for an existing wrapper with
     * 
     * @return True if there is an existing EnvisionJavaObject for the given
     *         Java object
     */
    public boolean containsInstance(Object javaObject) {
        return instanceMap.containsKey(javaObject.hashCode());
    }
    
    //=========
    // Getters
    //=========
    
    /** @return The EnvisionJavaClass for which this cache applies to. */
    public EnvisionJavaClass getWrapperClass() {
        return wrapperClass;
    }
    
    /** @return The Java class for which this cache applies to. */
    public Class<?> getJavaClass() {
        return wrapperClass.getWrappedJavaClass();
    }
    
}
