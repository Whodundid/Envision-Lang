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
    
    private Class<?> wrappedClass;
    
	private IScope instanceScope;
	
    /** Used to 'bridge' the gap between Java class members and Envision. */
    private EnvisionBridge bridge;
	
	private boolean initialized = false;
	
	//private Set<>
	
	//==============
	// Constructors
	//==============
	
	public static EnvisionJavaClass wrapJavaClass(EnvisionInterpreter interpreter, Class<?> classToWrap) {
	    UserDefinedTypeManager typeMan = interpreter.getTypeManager();
	    String className = classToWrap.getSimpleName();
	    EnvisionDatatype datatype = typeMan.getOrCreateDatatypeFor(className);
	    EnvisionJavaClass wrappedClass = new EnvisionJavaClass(classToWrap, datatype);
	    //typeMan.defineUserClass(wrappedClass);
	    return wrappedClass;
	}
	
	private EnvisionJavaClass(Class<?> classToWrap, IDatatype typeIn) {
		super(typeIn);
		
		wrappedClass = classToWrap;
		
		bridge = new EnvisionBridge(this);
		bridge.init_bridge();
	}
	
	public EnvisionJavaObject buildInstance(EnvisionInterpreter interpreter, Object javaObjectInstance) {
	    IScope buildScope = instanceScope.copy();
	    EnvisionJavaObject instance = new EnvisionJavaObject(this, wrappedClass, javaObjectInstance);
	    instance.setScope(buildScope);
	    
	    //define 'this'
        buildScope.define("this", instance);
        
        //build the body of the new instance
        interpreter.executeStatements(bodyStatements, buildScope);
        
        //define scope members
        defineScopeMembers(instance);
        
        instance.bindToJavaInstance();
        return instance;
	}
	
	/**
	 * Default class construction procedure. This path will only be taken if a
	 * ClassConstruct is not present.
	 * 
	 * @param interpreter The active working interpreter
	 * @param args Any arguments to be passed to the new object instance
	 * @return The newly created object instance
	 */
	@Override
	protected ClassInstance buildInstance(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		IScope buildScope = instanceScope.copy();
		EnvisionJavaObject instance = new EnvisionJavaObject(this, wrappedClass);
		
		//define 'this'
		buildScope.define("this", instance);
		
		//build the body of the new instance
		interpreter.executeStatements(bodyStatements, buildScope);
		
		//extract operator overloads from scope
		//set the overloaded operators onto the class instance
		buildScope.operators().forEach(op -> instance.addOperatorOverload(op.getOperator(), op));
		
		//execute constructor -- if there is one
		if (constructor != null) {
			constructor.setScope(buildScope);
			constructor.invoke(interpreter, args);
		}
		
		//define scope members
		defineScopeMembers(instance);
		
		return instance;
	}
	
	public void setNativeJavaScope(IScope scope) {
		instanceScope = scope;
	}
	
	//=========
	// Getters
	//=========
	
	/** Returns the underlying Java class that this EnvisionClass is wrapping. */
	public Class<?> getWrappedJavaClass() { return wrappedClass; }
	
}
