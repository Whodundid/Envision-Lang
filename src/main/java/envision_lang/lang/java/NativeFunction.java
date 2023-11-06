package envision_lang.lang.java;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.creation_util.ObjectCreator;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.datatypes.EnvisionNull;
import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.ParameterData;
import envision_lang.lang.natives.UserDefinedTypeManager;
import eutil.reflection.EModifier;

final class NativeFunction extends EnvisionFunction implements INativeEnvision {
    
    //========
    // Fields
    //========
    
    private final NativeDatatypeMapper mapper;
    private final Constructor<?> javaConstructorTarget;
    private final Method javaMethodTarget;
    private EnvisionJavaClass wrapperClass;
    private EnvisionJavaObject wrappedObject;
    private EModifier methodMods;
    
    //==============
    // Constructors
    //==============
    
    public NativeFunction(EnvisionJavaClass wrapperClassIn, Constructor<?> constructorIn) {
        this(wrapperClassIn, null, constructorIn);
    }
    
    public NativeFunction(EnvisionJavaClass wrapperClassIn, EnvisionJavaObject wrappedObjectIn, Constructor<?> constructorIn) {
        wrapperClass = wrapperClassIn;
        wrappedObject = wrappedObjectIn;
        javaConstructorTarget = constructorIn;
        javaMethodTarget = null;
        isConstructor = true;
        functionName = "init";
        
        Parameter[] params = constructorIn.getParameters();
        mapper = new NativeDatatypeMapper(params);
        methodMods = EModifier.of(constructorIn);
        
        IDatatype[] mappedParams = new IDatatype[params.length];
        for (int i = 0; i < params.length; i++) {
            Class<?> paramClass = params[i].getType();
            mappedParams[i] = IDatatype.fromJavaClass(paramClass);
        }
        parameters = ParameterData.from(mappedParams);
        
        if (methodMods.isFinal()) modifierHandler.setFinal();
        if (methodMods.isStatic()) modifierHandler.setStatic();
        if (methodMods.isPublic()) modifierHandler.setPublic();
        if (methodMods.isProtected()) modifierHandler.setProtected();
        if (methodMods.isPrivate()) modifierHandler.setPrivate();
    }
    
    public NativeFunction(EnvisionJavaClass wrapperClassIn, Method methodIn) {
        this(wrapperClassIn, null, methodIn);
    }
    
    public NativeFunction(EnvisionJavaClass wrapperClassIn, EnvisionJavaObject wrappedObjectIn, Method methodIn) {
        wrapperClass = wrapperClassIn;
        wrappedObject = wrappedObjectIn;
        javaConstructorTarget = null;
        javaMethodTarget = methodIn;
        isConstructor = false;
        functionName = methodIn.getName();
        returnType = IDatatype.fromJavaClass(methodIn.getReturnType());
        
        mapper = new NativeDatatypeMapper(methodIn);
        methodMods = EModifier.of(methodIn);
        
        Parameter[] params = methodIn.getParameters();
        IDatatype[] mappedParams = new IDatatype[params.length];
        for (int i = 0; i < params.length; i++) {
            Class<?> paramClass = params[i].getType();
            mappedParams[i] = IDatatype.fromJavaClass(paramClass);
        }
        parameters = ParameterData.from(mappedParams);
        
        if (methodMods.isFinal()) modifierHandler.setFinal();
        if (methodMods.isStatic()) modifierHandler.setStatic();
        if (methodMods.isPublic()) modifierHandler.setPublic();
        if (methodMods.isProtected()) modifierHandler.setProtected();
        if (methodMods.isPrivate()) modifierHandler.setPrivate();
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public NativeFunction copy() {
        NativeFunction copy;
        
        if (javaConstructorTarget != null) {
            copy = new NativeFunction(wrapperClass, wrappedObject, javaConstructorTarget);
        }
        else {
            copy = new NativeFunction(wrapperClass, wrappedObject, javaMethodTarget);
        }

        copy.instanceScope = instanceScope;
        
        for (EnvisionFunction overload : overloads) {
            var overloadCopy = overload.copy();
            copy.overloads.add(overloadCopy);
        }
        
        return copy;
    }
    
    @Override
    public void bindToWrappedObject(EnvisionJavaObject instance) {
        wrappedObject = instance;
    }
    
    @Override
    public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
        
        if (javaConstructorTarget != null) executeConstructor(interpreter, null, args);
        else if (javaMethodTarget != null) executeMethod(interpreter, args);
    }
    
    //==================
    // Internal Methods
    //==================
    
    /**
     * 
     * @param interpreter
     * @param instance The new object instance being build
     * @param args
     */
    protected void executeConstructor(EnvisionInterpreter interpreter, EnvisionJavaObject instance, EnvisionObject[] args) {
        try {
            NativeFunction m = (NativeFunction) getOverloadFromArgs(args);
            m.checkArgs(args);
            
            Constructor<?> constructor = m.javaConstructorTarget;
            NativeDatatypeMapper mapperToUse = m.mapper;
            
            Object[] mappedArguments = mapperToUse.mapToJava(args);
            
            Object newJavaObject = constructor.newInstance(mappedArguments);
            instance.setJavaObjectInstance(newJavaObject);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected void executeMethod(EnvisionInterpreter interpreter, EnvisionObject[] args) {
        NativeFunction m = (NativeFunction) getOverloadFromArgs(args);
        m.checkArgs(args);
        
        Method method = m.javaMethodTarget;
        NativeDatatypeMapper mapperToUse = m.mapper;
        
        Object[] mappedArguments = mapperToUse.mapToJava(args);
        Object result;
        
        try {
            result = method.invoke(wrappedObject.getJavaObjectInstance(), mappedArguments);
        }
        catch (Exception e) {
            throw new EnvisionLangError("Failed to invoke native function!", e);
        }
        
        IDatatype envisionReturnType = mapperToUse.getEnvisionReturnType();
        UserDefinedTypeManager typeMan = interpreter.getTypeManager();
        EnvisionObject toReturn = EnvisionNull.NULL;
        
        if (typeMan.isTypeDefined(envisionReturnType)) {
            EnvisionClass typeClass = typeMan.getTypeClass(envisionReturnType);
            // if the type class is a native java wrapper class, refer to the
            // wrapper class on what object to return
            if (typeClass instanceof EnvisionJavaClass jclass) {
                // use the EnvisionJavaClass to either return an already existing
                // wrapper instance or create a new one altogether
                toReturn = jclass.buildInstance(interpreter, result);
            }
            // if the type is a primitive, simply use the ObjectCreator
            else if (typeClass.isPrimitive()) {
                toReturn = ObjectCreator.wrap(result);
            }
            // otherwise, create a new Envision class instance from the found type
            else {
                toReturn = typeClass.newInstance(interpreter, args);
            }
        }
        // in the event that we don't already have an Envision mapped type for the
        // given Java returned object, attempt to create a mapping for it on the fly
        else if (result != null) {
            // wrap the object and then grab its built class from the interpreter's type manager
            EnvisionJavaObject.wrapJavaObject(interpreter, result);
            EnvisionClass typeClass = typeMan.getTypeClass(envisionReturnType);
            
            if (typeClass instanceof EnvisionJavaClass jclass) {
                // use the EnvisionJavaClass to either return an already existing
                // wrapper instance or create a new one altogether
                toReturn = jclass.buildInstance(interpreter, result);
            }
            else {
                throw new IllegalStateException("On-The-Fly native type definition somehow" +
                                                "returned a non-wrapper class type!");
            }
        }
        
        ret(toReturn);
    }
    
}
