package envision_lang.lang.java;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

import envision_lang.interpreter.util.creationUtil.ObjectCreator;
import envision_lang.interpreter.util.scope.IScope;
import envision_lang.interpreter.util.scope.Scope;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.datatypes.EnvisionNull;
import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.functions.IPrototypeHandler;
import envision_lang.lang.java.annotations.EConstructor;
import envision_lang.lang.java.annotations.EField;
import envision_lang.lang.java.annotations.EFunction;
import envision_lang.lang.java.annotations.EOperator;
import envision_lang.lang.natives.DataModifier;
import envision_lang.lang.natives.EnvisionParameter;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.ParameterData;
import envision_lang.lang.natives.Primitives;
import envision_lang.lang.natives.EnvisionStaticTypes;
import envision_lang.lang.natives.EnvisionVisibilityModifier;
import eutil.datatypes.EArrayList;
import eutil.datatypes.boxes.BoxList;
import eutil.datatypes.util.JavaDatatype;
import eutil.reflection.EModifier;
import eutil.reflection.EReflectionUtil;
import eutil.reflection.ObjectVisibility;

abstract class EnvisionBridge {
	
	//--------
	// Fields
	//--------
	
	private boolean init = false;
	private EModifier mods;
		
	private Class<? extends EnvisionBridge> javaObject;
	private EnvisionJavaClass classObject;
	private Object javaObjectInstance;
	private ClassInstance envisionObjectInstance;
	
	private EArrayList<EnvisionFunction> constructors = new EArrayList<>();
	private IScope nativeInstanceScope;
	
	private IPrototypeHandler prototypes = new IPrototypeHandler();
	
	//--------------
	// Constructors
	//--------------
	
	EnvisionBridge() {
		javaObject = getClass();
	}
	
	public void init_bridge() {
		if (init) return;
		parseJava();
	}
	
	private void parseJava() {
		classObject = new EnvisionJavaClass(javaObject.getSimpleName());
		nativeInstanceScope = new Scope(classObject.getClassScope());
		classObject.setNativeJavaScope(nativeInstanceScope);
		mods = EModifier.of(javaObject.getModifiers());
		
		classObject.setModifier(DataModifier.ABSTRACT, mods.isAbstract());
		classObject.setModifier(DataModifier.STATIC, mods.isStatic());
		
		if (mods.isPublic()) classObject.setPublic();
		else if (mods.isProtected()) classObject.setProtected();
		else if (mods.isPrivate()) classObject.setPrivate();
		
		var fieldsToProcess = javaObject.getDeclaredFields();
		var methodsToProcess = javaObject.getDeclaredMethods();
		var constructorsToProcess = javaObject.getDeclaredConstructors();
		
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
		EModifier mods = EModifier.of(theField);
		String name = theField.getName();
		JavaDatatype type = JavaDatatype.of(theField.getType());
		EnvisionVisibilityModifier visibility = EnvisionVisibilityModifier.of(ObjectVisibility.of(theField));
		boolean isFinal = mods.isFinal();
		boolean isStatic = mods.isStatic();
		Object value = EReflectionUtil.forceGet(theField, this);
		
		IDatatype envisionType = Primitives.getPrimitiveType(type).toDatatype();
		boolean strong = false;
		
		//check if null
		if (value == null) {
			value = EnvisionNull.NULL;
		}
		//parse var values separately
		else if (value instanceof EnvisionVar v) {
			envisionType = EnvisionStaticTypes.VAR_TYPE;
			value = v.getEnvisionObject();
			strong = v.isStrong();
		}
		
		EnvisionObject obj = ObjectCreator.createObject(envisionType, value);
		if (strong) obj.setStrong();
		if (isFinal) obj.setFinal();
		obj.setVisibility(visibility);
		
		if (isStatic) {
			obj.setStatic();
			classObject.getClassScope().define(name, envisionType, obj);
		}
		else {
			nativeInstanceScope.define(name, envisionType, obj);
		}
	}
	
	private void processConstructor(EConstructor descriptor, Constructor<?> theConstructor) {
		EModifier mods = EModifier.of(theConstructor);
		EnvisionVisibilityModifier visibility = EnvisionVisibilityModifier.of(ObjectVisibility.of(theConstructor));
		boolean isFinal = mods.isFinal();
		
		// map java parameter types to envision parameter types
		Map<IDatatype, Class<?>> argMapper = new HashMap<>();
		ParameterData params = convertJavaParameters(theConstructor.getParameters(), argMapper);
		
		NativeFunctionWrapper func = new NativeFunctionWrapper(params, this, theConstructor, argMapper);
		if (isFinal) func.setFinal();
		func.setVisibility(visibility);
		func.setStatic();
		
		classObject.addConstructor(func);
	}
	
	private void processFunction(EFunction descriptor, Method theFunction) {
		EModifier mods = EModifier.of(theFunction);
		String name = theFunction.getName();
		EnvisionVisibilityModifier visibility = EnvisionVisibilityModifier.of(ObjectVisibility.of(theFunction));
		boolean isFinal = mods.isFinal();
		boolean isStatic = mods.isStatic();
		
		String rtString = theFunction.getReturnType().getName();
		IDatatype rt = Primitives.getPrimitiveType(rtString);
		
		Map<IDatatype, Class<?>> argMapper = new HashMap<>();
		ParameterData params = convertJavaParameters(theFunction.getParameters(), argMapper);
		
		NativeFunctionWrapper func = new NativeFunctionWrapper(rt, name, params, this, theFunction, argMapper);
		func.setVisibility(visibility);
		if (isFinal) func.setFinal();
		
		if (isStatic) {
			func.setStatic();
			classObject.getClassScope().defineFunction(func);
		}
		else nativeInstanceScope.defineFunction(func);
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
		return (E) classObject.getClassScope().get(name);
	}
	
	//=========
	// Getters
	//=========
	
	public EnvisionClass getInternalClass() {
		return classObject;
	}
	
	public boolean isNativeInit() { return init; }
	
}
