package envision_lang.lang.java;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

import envision_lang.interpreter.util.scope.IScope;
import envision_lang.interpreter.util.scope.Scope;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.java.annotations.EConstructor;
import envision_lang.lang.java.annotations.EField;
import envision_lang.lang.java.annotations.EFunction;
import envision_lang.lang.java.annotations.EOperator;
import envision_lang.lang.natives.DataModifier;
import envision_lang.lang.natives.EnvisionParameter;
import envision_lang.lang.natives.EnvisionVisibilityModifier;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.ParameterData;
import eutil.datatypes.boxes.BoxList;
import eutil.reflection.EModifier;
import eutil.reflection.EReflectionUtil;
import eutil.reflection.ObjectVisibility;

class EnvisionBridge {
	
	//--------
	// Fields
	//--------
	
    private final EnvisionJavaClass wrapperClass;
    private final Class<?> javaObjectClass;
    
	private boolean init = false;
	private EModifier mods;
	
	//private EList<EnvisionFunction> constructors = EList.newList();
	private IScope nativeInstanceScope;
	
	//private IPrototypeHandler prototypes = new IPrototypeHandler();
	
	//--------------
	// Constructors
	//--------------
	
	EnvisionBridge(EnvisionJavaClass wrapperClassIn) {
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
		mods = EModifier.of(javaObjectClass.getModifiers());
		
		wrapperClass.setModifier(DataModifier.ABSTRACT, mods.isAbstract());
		wrapperClass.setModifier(DataModifier.STATIC, mods.isStatic());
		
		if (mods.isPublic()) wrapperClass.setPublic();
		else if (mods.isProtected()) wrapperClass.setProtected();
		else if (mods.isPrivate()) wrapperClass.setPrivate();
		
		var fieldsToProcess = javaObjectClass.getDeclaredFields();
		var methodsToProcess = javaObjectClass.getDeclaredMethods();
		var constructorsToProcess = javaObjectClass.getDeclaredConstructors();
		
		BoxList<EField, Field> fields = new BoxList<>();
		BoxList<EConstructor, Constructor<?>> constructors = new BoxList<>();
		BoxList<EFunction, Method> functions = new BoxList<>();
		BoxList<EOperator, Method> operators = new BoxList<>();
		
		//grab all declared envision fields
		for (var field : fieldsToProcess) {
			var annotations = field.getDeclaredAnnotations();
			for (Annotation a : annotations) {
				if (a instanceof EField efield) fields.add(efield, field);
			}
		}
		
		//grab all declared envision constructors
		for (var constructor : constructorsToProcess) {
			var annotations = constructor.getDeclaredAnnotations();
			for (Annotation a : annotations) {
				if (a instanceof EConstructor con) constructors.add(con, constructor);
			}
		}
		
		//grab all declared envision functions and operator overloads
		for (var meth : methodsToProcess) {
			var annotations = meth.getDeclaredAnnotations();
			for (Annotation a : annotations) {
				if (a instanceof EFunction func) functions.add(func, meth);
				else if (a instanceof EOperator op) operators.add(op, meth);
			}
		}
		
		//process parsed elements
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
		EModifier mods = EModifier.of(theConstructor);
		EnvisionVisibilityModifier visibility = EnvisionVisibilityModifier.of(ObjectVisibility.of(theConstructor));
		boolean isFinal = mods.isFinal();
		
		// map java parameter types to envision parameter types
		Map<IDatatype, Class<?>> argMapper = new HashMap<>();
		ParameterData params = convertJavaParameters(theConstructor.getParameters(), argMapper);
		
//		NativeFunction func = new NativeFunction(params, null, theConstructor, argMapper);
//		if (isFinal) func.setFinal();
//		func.setVisibility(visibility);
//		func.setStatic();
//		
//		wrapperClass.addConstructor(func);
	}
	
	private void processFunction(EFunction descriptor, Method theFunction) {
        NativeFunction wrappedFunction = new NativeFunction(theFunction);
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
        scopeToDefineOn.define(name, type, wrappedFunction);
	}
	
	private void processOperatorOverload(EOperator descriptor, Method theOperator) {
	    
	}
	
	/**
	 * Internal function used to convert a set of Java parameter types to Envision parameter types.
	 * 
	 * @param params The Java parameters to convert
	 * @param argMapper The argument mapper which maps Java types to EnvisionTypes
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
	
	//---------
	// Methods
	//---------
	
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
	
	public Class<?> getJavaClass() {
	    return javaObjectClass;
	}
	
	public EnvisionClass getInternalClass() {
		return wrapperClass;
	}
	
	public boolean isNativeInit() { return init; }
	
}
