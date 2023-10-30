package envision_lang.lang.java;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.creation_util.ObjectCreator;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.EnvisionClass;
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
    private EnvisionJavaObject wrappedObject;
    private EModifier methodMods;
    
    //==============
    // Constructors
    //==============
    
    public NativeFunction(Constructor<?> constructorIn) {
        this(null, constructorIn);
    }
    
    public NativeFunction(EnvisionJavaObject wrappedObjectIn, Constructor<?> constructorIn) {
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
    
    public NativeFunction(Method methodIn) {
        this(null, methodIn);
    }
    
    public NativeFunction(EnvisionJavaObject wrappedObjectIn, Method methodIn) {
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
        if (javaConstructorTarget != null) return new NativeFunction(wrappedObject, javaConstructorTarget);
        else return new NativeFunction(wrappedObject, javaMethodTarget);
    }
    
    @Override
    public void bindToWrappedObject(EnvisionJavaObject instance) {
        wrappedObject = instance;
    }
    
    @Override
    public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
        
        if (javaConstructorTarget != null) executeConstructor(interpreter, args);
        else if (javaMethodTarget != null) executeMethod(interpreter, args);
    }
    
    //==================
    // Internal Methods
    //==================
    
    protected void executeConstructor(EnvisionInterpreter interpreter, EnvisionObject[] args) {
        try {
            NativeFunction m = (NativeFunction) getOverloadFromArgs(args);
            m.checkArgs(args);
            
            Constructor<?> constructor = m.javaConstructorTarget;
            NativeDatatypeMapper mapperToUse = m.mapper;
            
            Object[] mappedArguments = mapperToUse.mapToJava(args);
            wrappedObject.javaObject = constructor.newInstance(mappedArguments);
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
            result = method.invoke(wrappedObject.javaObject, mappedArguments);
        }
        catch (Exception e) {
            throw new EnvisionLangError("Failed to invoke native function!", e);
        }
        
        IDatatype envisionReturnType = mapperToUse.getEnvisionReturnType();
        UserDefinedTypeManager typeMan = interpreter.getTypeManager();
        EnvisionObject toReturn;
        
        if (typeMan.isTypeDefined(envisionReturnType)) {
            EnvisionClass typeClass = typeMan.getTypeClass(envisionReturnType);
            if (typeClass instanceof EnvisionJavaClass jclass) {
                // I actually have no idea how to handle this situation right now
                // ~
                // The invoked method in question: 'Java:TestPoint:add' produces
                // a new TestPoint as a result. How on earth do I go about wrapping
                // that produced instance?
                
                //toReturn = jclass.buildInstance(interpreter, javaObjectInstance)
                toReturn = null;
            }
            else {
                toReturn = ObjectCreator.wrap(result);
                //toReturn = typeClass.newInstance(interpreter, args);
            }
        }
        else {
            toReturn = mapperToUse.mapReturnTypeToEnvision(result);                
        }
        
        ret(toReturn);
    }
    
}
