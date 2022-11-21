package envision_lang.lang.java;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.creationUtil.ObjectCreator;
import envision_lang.interpreter.util.scope.IScope;
import envision_lang.interpreter.util.scope.Scope;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.lang.internal.EnvisionNull;
import envision_lang.lang.internal.IPrototypeHandler;
import envision_lang.lang.java.annotations.EConstructor;
import envision_lang.lang.java.annotations.EField;
import envision_lang.lang.java.annotations.EFunction;
import envision_lang.lang.java.annotations.EOperator;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.Primitives;
import envision_lang.lang.natives.StaticTypes;
import envision_lang.lang.util.DataModifier;
import envision_lang.lang.util.EnvisionParameter;
import envision_lang.lang.util.EnvisionVis;
import envision_lang.lang.util.ParameterData;
import eutil.datatypes.BoxList;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EDataType;
import eutil.reflection.EModifier;
import eutil.reflection.ObjectVisibility;
import eutil.reflection.ReflectionHelper;

abstract class EnvisionBridge {
	
	//--------
	// Fields
	//--------
	
	private boolean init = false;
	
	private Class<? extends EnvisionBridge> inst;
	private EModifier mods;
	
	private EnvisionClass classObject;
	private ClassInstance instanceObject;
	
	private EArrayList<EnvisionFunction> constructors = new EArrayList<>();
	private IScope instanceScope;
	
	private IPrototypeHandler prototypes = new IPrototypeHandler();
	
	//--------------
	// Constructors
	//--------------
	
	EnvisionBridge() {
		inst = getClass();
		instanceScope = new Scope();
	}
	
	public void init_bridge() {
		if (init) return;
		parseJava();
	}
	
	private void parseJava() {
		classObject = new EnvisionClass(inst.getSimpleName());
		mods = EModifier.of(inst.getModifiers());
		
		classObject.setModifier(DataModifier.ABSTRACT, mods.isAbstract());
		classObject.setModifier(DataModifier.STATIC, mods.isStatic());
		
		if (mods.isPublic()) classObject.setPublic();
		else if (mods.isProtected()) classObject.setProtected();
		else if (mods.isPrivate()) classObject.setPrivate();
		
		var fieldsToProcess = inst.getDeclaredFields();
		var methodsToProcess = inst.getDeclaredMethods();
		
		BoxList<EField, Field> fields = new BoxList<>();
		BoxList<EConstructor, Method> constructors = new BoxList<>();
		BoxList<EFunction, Method> functions = new BoxList<>();
		BoxList<EOperator, Method> operators = new BoxList<>();
		
		//grab all declared envision fields
		for (var field : fieldsToProcess) {
			var annotations = field.getDeclaredAnnotations();
			for (Annotation a : annotations) {
				if (a instanceof EField efield) fields.add(efield, field);
			}
		}
		
		//grab all declared envision constructors, functions, and operator overloads
		for (var meth : methodsToProcess) {
			var annotations = meth.getDeclaredAnnotations();
			for (Annotation a : annotations) {
				if (a instanceof EConstructor con) constructors.add(con, meth);
				else if (a instanceof EFunction func) functions.add(func, meth);
				else if (a instanceof EOperator op) operators.add(op, meth);
			}
		}
		
		//process parsed elements
		for (var f : fields) processField(f.getA(), f.getB());
		for (var c : constructors) processConstructor(c.getA(), c.getB());
		for (var f : functions) processFunction(f.getA(), f.getB());
		for (var o : operators) processOperatorOverload(o.getA(), o.getB());
		
		System.out.println();
		System.out.println(instanceScope);
	}
	
	private enum COW {
		DOG,
		CAT,
		BIRD;
	}
	
	private void processField(EField descriptor, Field theField) {
		EModifier mods = EModifier.of(theField);
		String name = theField.getName();
		EDataType type = EDataType.of(theField.getType());
		EnvisionVis visibility = EnvisionVis.of(ObjectVisibility.of(theField));
		boolean isFinal = mods.isFinal();
		boolean isStatic = mods.isStatic();
		Object value = ReflectionHelper.forceGet(theField, this);
		
		IDatatype envisionType = Primitives.getDataType(type).toDatatype();
		boolean strong = false;
		
		//check if null
		if (value == null) {
			value = EnvisionNull.NULL;
		}
		//parse var values separately
		else if (value instanceof EnvisionVar v) {
			envisionType = StaticTypes.VAR_TYPE;
			value = v.value;
			strong = v.isStrong;
		}
		
		EnvisionObject obj = ObjectCreator.createObject(envisionType, value);
		if (strong) obj.setStrong();
		if (isFinal) obj.setFinal();
		if (isStatic) obj.setStatic();
		
		instanceScope.define(name, envisionType, obj);
	}
	
	private void processConstructor(EConstructor descriptor, Method theConstructor) {
		EModifier mods = EModifier.of(theConstructor);
		String name = "init";
		EnvisionVis visibility = EnvisionVis.of(ObjectVisibility.of(theConstructor));
		boolean isFinal = mods.isFinal();
		boolean isStatic = mods.isStatic();
		
		String rtString = theConstructor.getReturnType().getName();
		IDatatype rt = Primitives.getDataType(rtString);
		
		HashMap<IDatatype, Class<?>> argMapper = new HashMap<>();
		ParameterData params = new ParameterData();
		for (Parameter p : theConstructor.getParameters()) {
			IDatatype pType = IDatatype.of(p.getType().getSimpleName());
			argMapper.putIfAbsent(pType, p.getType());
			EnvisionParameter param = new EnvisionParameter(pType, p.getName());
			params.add(param);
		}
		
		EnvisionFunctionWrapper func = new EnvisionFunctionWrapper(rt, name, params, this, theConstructor, argMapper);
		
		constructors.add(func);
	}
	
	private void processFunction(EFunction descriptor, Method theFunction) {
		EModifier mods = EModifier.of(theFunction);
		String name = theFunction.getName();
		EnvisionVis visibility = EnvisionVis.of(ObjectVisibility.of(theFunction));
		boolean isFinal = mods.isFinal();
		boolean isStatic = mods.isStatic();
		
		String rtString = theFunction.getReturnType().getName();
		IDatatype rt = Primitives.getDataType(rtString);
		
		HashMap<IDatatype, Class<?>> argMapper = new HashMap<>();
		ParameterData params = new ParameterData();
		for (Parameter p : theFunction.getParameters()) {
			IDatatype pType = IDatatype.of(p.getType().getSimpleName());
			argMapper.putIfAbsent(pType, p.getType());
			EnvisionParameter param = new EnvisionParameter(pType, p.getName());
			params.add(param);
		}
		
		EnvisionFunctionWrapper func = new EnvisionFunctionWrapper(rt, name, params, this, theFunction, argMapper);
		
		instanceScope.defineFunction(func);
	}
	
	private void processOperatorOverload(EOperator descriptor, Method theOperator) {
		
	}
	
	//---------
	// Methods
	//---------
	
	final ClassInstance newInstance_i(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		instanceScope.setParent(interpreter.scope());
		
		return new ClassInstance(classObject, instanceScope);
	}
	
	final void set_i(String name, Object value) {
		try {
			ReflectionHelper.setField(this, name, value);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	final <E> E get_i(String name) {
		return (E) instanceScope.get(name);
	}
	
}
