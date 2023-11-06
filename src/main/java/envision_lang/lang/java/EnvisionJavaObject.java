package envision_lang.lang.java;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.functions.FunctionPrototype;
import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.lang.language_errors.error_types.objects.ClassCastError;
import envision_lang.lang.language_errors.error_types.objects.UnsupportedOverloadError;
import envision_lang.lang.natives.EnvisionStaticTypes;
import envision_lang.lang.natives.IDatatype;
import envision_lang.tokenizer.Operator;
import eutil.datatypes.util.EList;

public class EnvisionJavaObject extends ClassInstance {
    
    /** The wrapped Java class. */
    private EnvisionJavaClass classObject;
    /** The object in Java. */
    private Object javaObject;
    
    //==============
    // Constructors
    //==============
    
    public EnvisionJavaObject(EnvisionJavaClass wrappedClass) {
        super(wrappedClass);
        
        classObject = wrappedClass;
    }
    
    public EnvisionJavaObject(EnvisionJavaClass wrappedClass, Object javaInstance) {
        super(wrappedClass);
        
        classObject = wrappedClass;
        javaObject = javaInstance;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public String toString() {
        return "Envision[" + getClassName() + "]" + getHexHash();
    }
    
    @Override
    public EnvisionObject handleOperatorOverloads
        (EnvisionInterpreter interpreter, String scopeName, Operator op, EnvisionObject obj)
            throws UnsupportedOverloadError
    {
        return null;
    }
    
    @Override
    public EnvisionObject handleObjectCasts(IDatatype castType) throws ClassCastError {
        throw new ClassCastError("Wrapped Java objects cannot be cast!");
    }
    
    @Override
    public Object convertToJavaObject() {
        return javaObject;
    }
    
    /**
     * Returns a field value from this instance's scope.
     */
    @Override
    public EnvisionObject get(String name) {
        EnvisionObject obj = instanceScope.get(name);
        // if native field, get EnvisionObject equivalent value
        if (obj instanceof NativeField f) return f.getFieldValue_Envision();
        // if function prototype, build dynamic function
        if (obj instanceof FunctionPrototype proto) return proto.build(this);
        // otherwise, just return scope object
        return obj;
    }
    
    /**
     * Assigns a field value within this instance's scope.
     */
    @Override
    public EnvisionObject set(String name, EnvisionObject in) {
        EnvisionObject instanceMember = instanceScope.get(name);
        
        if (instanceMember instanceof NativeField f) f.setFieldValue(in);
        else instanceScope.set(name, in);
        
        return in;
    }
    
    /**
     * Assigns a field value within this instance's scope.
     */
    @Override
    public EnvisionObject set(String name, IDatatype type, EnvisionObject in) {
        instanceScope.setFast(name, type, in);
        return in;
    }
    
    //=========
    // Methods
    //=========
    
	public String getClassName() {
	    return classObject.getClassName();
	}
	
    private EList<NativeField> findNativeFields() {
        return instanceScope.values().values().stream()
            .filter(entry -> entry.getObject() instanceof NativeField)
            .map(entry -> (NativeField) entry.getObject())
            .collect(EList.toEList());
    }
    
    private EList<NativeFunction> findNativeConstructors() {
        return instanceScope.values().values().stream()
            .filter(entry -> entry.getObject() instanceof NativeFunction)
            .map(entry -> (NativeFunction) entry.getObject())
            .filter(EnvisionFunction::isConstructor)
            .collect(EList.toEList());
    }
    
    private EList<NativeFunction> findNativeFunctions() {
        return instanceScope.values().values().stream()
            .filter(entry -> entry.getObject() instanceof NativeFunction)
            .map(entry -> (NativeFunction) entry.getObject())
            .collect(EList.toEList());
    }
    
    private EList<NativeFunction> findNativeOperators() {
        return instanceScope.values().values().stream()
            .filter(entry -> entry.getObject() instanceof NativeFunction)
            .map(entry -> (NativeFunction) entry.getObject())
            .filter(EnvisionFunction::isOperator)
            .collect(EList.toEList());
    }
	
    public void bindToJavaInstance() {
        var nativeFields = findNativeFields();
        var nativeConstructors = findNativeConstructors();
        var nativeFunctions = findNativeFunctions();
        var nativeOperatorOverloads = findNativeOperators();
        
        for (int i = 0; i < nativeFields.size(); i++) {
            nativeFields.get(i).bindToWrappedObject(this);
        }
        for (int i = 0; i < nativeConstructors.size(); i++) {
            nativeConstructors.get(i).bindToWrappedObject(this);
        }
        for (int i = 0; i < nativeFunctions.size(); i++) {
            nativeFunctions.get(i).bindToWrappedObject(this);
        }
        for (int i = 0; i < nativeOperatorOverloads.size(); i++) {
            nativeOperatorOverloads.get(i).bindToWrappedObject(this);
        }
    }
	
    //=========
    // Getters
    //=========
    
    public Object getJavaObjectInstance() { return javaObject; }
    
    //=========
    // Setters
    //=========
    
    // there should probably be some more checks going on to ensure type compatibility but ya know~
    public void setJavaObjectInstance(Object instance) { javaObject = instance; }
    
	//=================
	// Static Builders
	//=================
	
	public static EnvisionJavaObject wrapJavaObject(EnvisionInterpreter interpreter, Object obj) {
	    Class<?> objectClass = obj.getClass();
	    String className = objectClass.getSimpleName();
	    var typeManager = interpreter.getTypeManager();
	    IDatatype envisionType = IDatatype.of(className);
	    
	    // the envision class to bind to
	    EnvisionJavaClass wrappedClass = null;
	    boolean willRegister = false;
	    
	    // check if wrapped Java class type already exists
	    EnvisionClass existingClass = typeManager.getTypeClass(envisionType);
	    // if the class doesn't exist -- easy, just make a new one and register it
	    if (existingClass == null) {
	        wrappedClass = EnvisionJavaClass.wrapJavaClass(interpreter, objectClass);
	        willRegister = true;
	    }
	    // if the existing class isn't null and is a Java class wrapper, check that the wrapped class matches this one
	    else if (existingClass instanceof EnvisionJavaClass c && c.getWrappedJavaClass().equals(objectClass)) {
	        wrappedClass = c;
	    }
	    else {
	        throw new EnvisionLangError("Duplicate native Java class already defined under: '"
	                                    + envisionType + "' for: " + className);
	    }
	    
	    // if this is a new class to the interpreter, define it
	    if (willRegister) {
	        wrappedClass.setFinal();
	        typeManager.defineUserClass(wrappedClass);
	        interpreter.scope().define(className, EnvisionStaticTypes.CLASS_TYPE, wrappedClass);
	    }
	    
	    // create the actual wrapped Java object
	    return wrappedClass.buildInstance(interpreter, obj);
	}
	
}
