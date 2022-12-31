package envision_lang.lang.java;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.creationUtil.ObjectCreator;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionNumber;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.util.ParameterData;
import eutil.datatypes.EArrayList;

final class NativeFunctionWrapper extends EnvisionFunction {
	
	//========
	// Fields
	//========
	
	private Object objectInstance;
	private Constructor<?> javaConstructorTarget;
	private Method javaMethodTarget;
	private HashMap<IDatatype, Class<?>> argMapper = new HashMap<>();
	
	//==============
	// Constructors
	//==============
	
	/** Wrapper for java constructor methods. */
	public NativeFunctionWrapper(ParameterData paramsIn,
								     Object instance,
								     Constructor<?> javaConstructorTargetIn,
								     HashMap<IDatatype, Class<?>> argMapperIn)
	{
		super(paramsIn);
		objectInstance = instance;
		javaConstructorTarget = javaConstructorTargetIn;
		argMapper = argMapperIn;
	}
	
	/** Wrapper for all standard java methods. */
	public NativeFunctionWrapper(IDatatype rt,
								     String nameIn,
								     ParameterData paramsIn,
								     Object instance,
								     Method javaMethodTargetIn,
								     HashMap<IDatatype, Class<?>> argMapperIn)
	{
		super(rt, nameIn, paramsIn);
		objectInstance = instance;
		javaMethodTarget = javaMethodTargetIn;
		argMapper = argMapperIn;
	}
	
	/** Copy constructor. */
	public NativeFunctionWrapper(NativeFunctionWrapper toCopy) {
		super(toCopy.returnType, toCopy.functionName, toCopy.params);
		objectInstance = toCopy.objectInstance;
		javaConstructorTarget = toCopy.javaConstructorTarget;
		javaMethodTarget = toCopy.javaMethodTarget;
		argMapper = toCopy.argMapper;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public NativeFunctionWrapper copy() {
		return new NativeFunctionWrapper(this);
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		if (javaConstructorTarget != null) executeJavaConstructor(interpreter, args);
		else if (javaMethodTarget != null) executeJavaMethod(interpreter, args);
	}
	
	//==================
	// Internal Methods
	//==================
	
	protected void executeJavaConstructor(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		try {
			//System.out.println("argMapper: " + " : " + argMapper + " : " + EStringUtil.toString(args));
			EArrayList<Object> javaObjects = new EArrayList<>();
			for (int i = 0; i < args.length; i++) {
				IDatatype fromType = args[i].getDatatype();
				EnvisionObject toConvert = args[i];
				Class<?> toType = argMapper.get(fromType);
				Object casted = performCast(toConvert, toType);
				javaObjects.add(casted);
			}
			
			//System.out.println("Constructor: " + javaConstructorTarget);
			
			Object obj = javaConstructorTarget.newInstance(javaObjects.toArray());
			
			
			//System.out.println(obj);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void executeJavaMethod(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		//System.out.println(javaMethodTarget + " BEING INVOKED");
		EnvisionObject toReturn = null;
		try {
			//System.out.println("argMapper: " + " : " + argMapper + " : " + EStringUtil.toString(args));
			EArrayList<Object> javaObjects = new EArrayList<>();
			for (int i = 0; i < args.length; i++) {
				IDatatype fromType = args[i].getDatatype();
				EnvisionObject toConvert = args[i];
				Class<?> toType = argMapper.get(fromType);
				Object casted = performCast(toConvert, toType);
				javaObjects.add(casted);
			}
			
			//this is wrong, needs to mapped from Envision types to java types..
			Object returnValue = javaMethodTarget.invoke(objectInstance, javaObjects.toArray());
			
			toReturn = ObjectCreator.wrap(returnValue);
			//System.out.println(toReturn);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		if (toReturn != null) {
			//then need to convert returnValue from a java type back to an Envision type
			ret(toReturn);
		}
	}
	
	//=========================
	// Internal Helper Methods
	//=========================
	
	protected static Object performCast(EnvisionObject obj, Class<?> toType) {
		if (obj instanceof EnvisionNumber n) return castFromEnvisionNumber(n, toType);
		
		Object toConvert = obj.convertToJavaObject();
		return toType.cast(toConvert);
	}
	
	protected static Object castFromEnvisionNumber(EnvisionNumber toConvert, Class<?> toType) {
		Number n = (Number) toConvert.get_i();
		
		if (toType == Byte.class || toType == byte.class) return n.byteValue();
		if (toType == Short.class || toType == short.class) return n.shortValue();
		if (toType == Integer.class || toType == int.class) return n.intValue();
		if (toType == Long.class || toType == long.class) return n.longValue();
		if (toType == Float.class || toType == float.class) return n.floatValue();
		if (toType == Double.class || toType == double.class) return n.doubleValue();
		
		throw new IllegalStateException("Not possible!");
	}
	
//	protected static void updateEnvisionInstanceFields(EnvisionBridge bridge) {
//		
//	}
		
	
}
