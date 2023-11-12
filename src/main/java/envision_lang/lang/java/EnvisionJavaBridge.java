package envision_lang.lang.java;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

import envision_lang.interpreter.util.scope.IScope;
import envision_lang.interpreter.util.scope.Scope;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.java.annotations.EClass;
import envision_lang.lang.java.annotations.EConstructor;
import envision_lang.lang.java.annotations.EField;
import envision_lang.lang.java.annotations.EFunction;
import envision_lang.lang.java.annotations.EIgnore;
import envision_lang.lang.java.annotations.EOperator;
import envision_lang.lang.natives.DataModifier;
import envision_lang.lang.natives.EnvisionParameter;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.ParameterData;
import eutil.datatypes.boxes.BoxList;
import eutil.reflection.EModifier;
import eutil.reflection.EReflectionUtil;

class EnvisionJavaBridge {
    
    //========
    // Fields
    //========
    
    private final EnvisionJavaClass wrapperClass;
    private final Class<?> javaObjectClass;
    
    private boolean init = false;
    private EModifier mods;
    
    //private EList<EnvisionFunction> constructors = EList.newList();
    private IScope nativeInstanceScope;
    
    //private IPrototypeHandler prototypes = new IPrototypeHandler();
    
    //==============
    // Constructors
    //==============
    
    EnvisionJavaBridge(EnvisionJavaClass wrapperClassIn) {
        wrapperClass = wrapperClassIn;
        javaObjectClass = wrapperClassIn.getWrappedJavaClass();
    }
    
    void init_bridge() {
        if (init) return;
        parseJava();
        init = true;
    }
    
    private void parseJava() {
        //wrapperClass = new EnvisionJavaClass(javaObjectClass);
        nativeInstanceScope = new Scope(wrapperClass.getClassScope());
        wrapperClass.setNativeJavaScope(nativeInstanceScope);
        wrapperClass.setScope(nativeInstanceScope);
        mods = EModifier.of(javaObjectClass.getModifiers());
        
        wrapperClass.setModifier(DataModifier.ABSTRACT, mods.isAbstract());
        wrapperClass.setModifier(DataModifier.STATIC, mods.isStatic());
        
        if (mods.isPublic()) wrapperClass.setPublic();
        else if (mods.isProtected()) wrapperClass.setProtected();
        else if (mods.isPrivate()) wrapperClass.setPrivate();
        
        // Check if the class itself defines the 'EClass' annotation
        // if so, attempt to define/map all fields/constructors/methods
        // that don't have the 'EIgnore' tag on them.
        var annotationsOnClass = javaObjectClass.getDeclaredAnnotations();
        boolean processEntireClass = false;
        boolean ignoreEntireClass = false;
        for (var annotation : annotationsOnClass) {
            if (annotation instanceof EIgnore) { ignoreEntireClass = true; break; }
            if (annotation instanceof EClass) processEntireClass = true;
        }
        
        // if the class itself was marked with 'EIgnore', ignore the whole class :)
        if (ignoreEntireClass) return;
        
        //-------------------------------------------------------------
        
        var fieldsToProcess = javaObjectClass.getDeclaredFields();
        var methodsToProcess = javaObjectClass.getDeclaredMethods();
        var constructorsToProcess = javaObjectClass.getDeclaredConstructors();
        
        BoxList<EField, Field> fields = new BoxList<>();
        BoxList<EConstructor, Constructor<?>> constructors = new BoxList<>();
        BoxList<EFunction, Method> functions = new BoxList<>();
        BoxList<EOperator, Method> operators = new BoxList<>();
        
        //-------------------------------------------------------------
        
        // grab all declared envision fields
        for (var field : fieldsToProcess) {
            var annotations = field.getDeclaredAnnotations();
            
            EField target = null;
            boolean ignore = false;
            for (Annotation a : annotations) {
                if (a instanceof EIgnore) { ignore = true; break; }
                else if (a instanceof EField t) target = t;
            }
            
            if (ignore) continue;
            else if (processEntireClass || target != null) fields.add(target, field);
        }
        
        //-------------------------------------------------------------
        
        // grab all declared envision constructors
        for (var constructor : constructorsToProcess) {
            var annotations = constructor.getDeclaredAnnotations();
            
            EConstructor target = null;
            boolean ignore = false;
            for (Annotation a : annotations) {
                if (a instanceof EIgnore) { ignore = true; break; }
                else if (a instanceof EConstructor t) target = t;
            }
            
            if (ignore) continue;
            else if (processEntireClass || target != null) constructors.add(target, constructor);
        }
        
        //-------------------------------------------------------------
        
        // grab all declared envision functions and operator overloads
        for (var meth : methodsToProcess) {
            var annotations = meth.getDeclaredAnnotations();
            
            EFunction methodTarget = null;
            EOperator operatorTarget = null;
            boolean ignore = false;
            for (Annotation a : annotations) {
                if (a instanceof EIgnore) { ignore = true; break; }
                else if (a instanceof EFunction t) methodTarget = t;
                else if (a instanceof EOperator t) operatorTarget = t;
            }
            
            if (ignore) continue;
            else if (processEntireClass || methodTarget != null) functions.add(methodTarget, meth);
            else if (processEntireClass || operatorTarget != null) operators.add(operatorTarget, meth);
        }
        
        //-------------------------------------------------------------
        
        // process parsed elements
        for (var f : fields) processField(f.getA(), f.getB());
        for (var c : constructors) processConstructor(c.getA(), c.getB());
        for (var f : functions) processFunction(f.getA(), f.getB());
        for (var o : operators) processOperatorOverload(o.getA(), o.getB());
    }
    
    private void processField(EField descriptor, Field theField) {
        NativeField wrappedField = new NativeField(theField);
        IScope scopeToDefineOn;
        
        if (EModifier.isStatic(theField)) {
            wrappedField.setStatic();
            scopeToDefineOn = wrapperClass.getClassScope();
        }
        else {
            scopeToDefineOn = nativeInstanceScope;
        }
        
        String name = theField.getName();
        IDatatype type = wrappedField.getDatatype();
        scopeToDefineOn.define(name, type, wrappedField);
    }
    
    private void processConstructor(EConstructor descriptor, Constructor<?> theConstructor) {
        NativeFunction wrappedConstructor = new NativeFunction(wrapperClass, theConstructor);
        IScope scopeToDefineOn;
        
        wrappedConstructor.setStatic();
        scopeToDefineOn = wrapperClass.getClassScope();
        
        String name = theConstructor.getName();
        IDatatype type = wrappedConstructor.getDatatype();
        
        // attempt to find any already existing base constructor within the given scope
        EnvisionFunction base = getBaseFunction(name, scopeToDefineOn);
        //System.out.println("CON: " + wrappedConstructor + ((base != null) ? ("    -    BASE: " + base) : ""));
        // if there is a base constructor, try to add the newly built constructor as an overload
        if (base != null) base.addOverload(wrappedConstructor);
        // if there was not a base constructor, define the newly built constructor as is
        else {
            wrapperClass.addConstructor(wrappedConstructor);
            scopeToDefineOn.define("init", type, wrappedConstructor);
        }
    }
    
    private void processFunction(EFunction descriptor, Method theFunction) {
        NativeFunction wrappedFunction = new NativeFunction(wrapperClass, theFunction);
        IScope scopeToDefineOn;
        
        if (EModifier.isStatic(theFunction)) {
            wrappedFunction.setStatic();
            scopeToDefineOn = wrapperClass.getClassScope();
        }
        else {
            scopeToDefineOn = nativeInstanceScope;
        }
        
        String name = theFunction.getName();
        IDatatype type = wrappedFunction.getDatatype();
        
        // attempt to find any already existing base method within the given scope
        EnvisionFunction base = getBaseFunction(name, scopeToDefineOn);
        //System.out.println("FUNC: " + wrappedFunction + ((base != null) ? ("    -    BASE: " + base) : ""));
        // if there is a base method, try to add the newly built method as an overload
        if (base != null) base.addOverload(wrappedFunction);
        // if there was not a base method, define the newly built method as is
        else scopeToDefineOn.define(name, type, wrappedFunction);
    }
    
    private void processOperatorOverload(EOperator descriptor, Method theOperator) {
        
    }
    
    /**
     * Internal function used to convert a set of Java parameter types to
     * Envision parameter types.
     * 
     * @param params    The Java parameters to convert
     * @param argMapper The argument mapper which maps Java types to
     *                  EnvisionTypes
     * 
     * @return The converted set of Envision parameters
     */
    private ParameterData convertJavaParameters(Parameter[] params, Map<IDatatype, Class<?>> argMapper) {
        int size = params.length;
        EnvisionParameter[] parsedParams = new EnvisionParameter[size];
        
        for (int i = 0; i < size; i++) {
            Parameter p = params[i];
            IDatatype pType = IDatatype.of(p.getType().getSimpleName());
            argMapper.putIfAbsent(pType, p.getType());
            EnvisionParameter param = new EnvisionParameter(pType, p.getName());
            parsedParams[i] = param;
        }
        
        // build parameter data from parsed java params
        return ParameterData.from(parsedParams);
    }
    
    /** Attempts to find a method of the same name within the given scope. */
    public static EnvisionFunction getBaseFunction(String name, IScope scopeIn) {
        // placeholder variable check
        EnvisionObject o = scopeIn.getLocal(name);
        
        // check if a function with that name is already defined
        if (o instanceof EnvisionFunction func) return func;
        
        return null;
    }
    
    //=========
    // Methods
    //=========
    
    final void set_class_i(String name, Object value) {
        try {
            EReflectionUtil.setField(this, name, value);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    final <E> E get_class_i(String name) {
        System.out.println("GET: " + name);
        
        return (E) wrapperClass.getClassScope().get(name);
    }
    
    //=========
    // Getters
    //=========
    
    public Class<?> getJavaClass() { return javaObjectClass; }
    public EnvisionClass getInternalClass() { return wrapperClass; }
    public boolean isNativeInit() { return init; }
    
}
