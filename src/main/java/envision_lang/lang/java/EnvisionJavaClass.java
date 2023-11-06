package envision_lang.lang.java;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.scope.IScope;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.natives.EnvisionDatatype;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.UserDefinedTypeManager;

public class EnvisionJavaClass extends EnvisionClass {
    
    //========
    // Fields
    //========
    
    /** The actual Java class for which this is wrapping. */
    private Class<?> wrappedClass;
    
    /**
     * The internal scope which contains all mapped native Java
     * fields/methods/constructors/operators.
     */
    private IScope instanceScope;
    
    /** Used to 'bridge' the gap between Java class members and Envision. */
    private EnvisionJavaBridge bridge;
    
    /**
     * An internal cache used to keep track of all Java instances of this class
     * type in Envision.
     */
    private NativeInstanceCache instanceCache;
    
    //=================
    // Static Wrappers
    //=================
    
    public static EnvisionJavaClass wrapJavaClass(EnvisionInterpreter interpreter, Class<?> classToWrap) {
        UserDefinedTypeManager typeMan = interpreter.getTypeManager();
        String className = classToWrap.getSimpleName();
        EnvisionDatatype datatype = typeMan.getOrCreateDatatypeFor(className);
        EnvisionJavaClass wrappedClass = new EnvisionJavaClass(classToWrap, datatype);
        //typeMan.defineUserClass(wrappedClass);
        return wrappedClass;
    }
    
    //==============
    // Constructors
    //==============
    
    private EnvisionJavaClass(Class<?> classToWrap, IDatatype typeIn) {
        super(typeIn);
        
        wrappedClass = classToWrap;
        instanceCache = new NativeInstanceCache(this);
        
        bridge = new EnvisionJavaBridge(this);
        bridge.init_bridge();
    }
    
    //===================
    // Instance Creation
    //===================
    
    /**
     * Instance creation path that can be safely called from Java.
     * <p>
     * If there is already an existing EnvisionJavaObject that wraps
     * the given 'javaObjectInstance' then that existing wrapper will
     * be returned instead. Otherwise, a new wrapper will be created.
     * 
     * @param interpreter        The EnvisionInterpreter executing this code
     * @param javaObjectInstance The Java object to wrap
     * 
     * @return An EnvisionJavaObject that wraps the given 'javaObjectInstance'
     */
    public EnvisionJavaObject buildInstance(EnvisionInterpreter interpreter, Object javaObjectInstance) {
        // check the cache for an already existing object
        EnvisionJavaObject existingInstance = instanceCache.get(javaObjectInstance);
        if (existingInstance != null) return existingInstance;
        
        // otherwise, continue with creating a new wrapper instance
        IScope buildScope = instanceScope.copy();
        EnvisionJavaObject instance = new EnvisionJavaObject(this, javaObjectInstance);
        instance.setScope(buildScope);
        
        // define 'this'
        buildScope.define("this", instance);
        
        // build the body of the new instance
        interpreter.executeStatements(bodyStatements, buildScope);
        
        // define scope members
        defineScopeMembers(instance);
        instance.bindToJavaInstance();
        
        // store the newly created instance in the cache for future lookup
        instanceCache.store(instance);
        
        return instance;
    }
    
    /**
     * Default class construction procedure. This path will only be taken if a
     * ClassConstruct is not present.
     * 
     * @param interpreter The active working interpreter
     * @param args        Any arguments to be passed to the new object instance
     * 
     * @return The newly created object instance
     */
    @Override
    protected ClassInstance buildInstance(EnvisionInterpreter interpreter, EnvisionObject[] args) {
        // otherwise, continue with creating a new wrapper instance
        IScope buildScope = instanceScope.copy();
        EnvisionJavaObject instance = new EnvisionJavaObject(this);
        instance.setScope(buildScope);
        
        // define 'this'
        buildScope.define("this", instance);
        
        // build the body of the new instance
        interpreter.executeStatements(bodyStatements, buildScope);
        
        // extract operator overloads from scope
        // set the overloaded operators onto the class instance
        buildScope.operators().forEach(op -> instance.addOperatorOverload(op.getOperator(), op));
        
        // execute constructor -- if there is one
        if (constructor != null) {
            constructor.setScope(buildScope);
            
            // Frankly, I am not sure that there ever could a constructor for an EnvisionJavaClass
            // that isn't a NativeFunction, but adding this check in just in case for now..
            if (constructor instanceof NativeFunction nativeConstructor) {
                nativeConstructor.executeConstructor(interpreter, instance, args);
            }
            else {
                constructor.invoke(interpreter, args);                
            }
        }
        
        // define scope members
        defineScopeMembers(instance);
        instance.bindToJavaInstance();
        
        // store the newly created instance in the cache for future lookup
        instanceCache.store(instance);
        
        return instance;
    }
    
    //=========
    // Getters
    //=========
    
    /**
     * Returns the underlying Java class that this EnvisionClass is wrapping.
     */
    public Class<?> getWrappedJavaClass() {
        return wrappedClass;
    }
    
    public NativeInstanceCache getInstanceCache() {
        return instanceCache;
    }
    
    //=========
    // Setters
    //=========
    
    public void setNativeJavaScope(IScope scope) {
        instanceScope = scope;
    }
    
}
