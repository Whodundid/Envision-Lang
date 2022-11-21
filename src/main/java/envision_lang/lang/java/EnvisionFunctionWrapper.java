package envision_lang.lang.java;

import java.lang.reflect.Method;
import java.util.HashMap;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.creationUtil.ObjectCreator;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.util.ParameterData;
import eutil.datatypes.EArrayList;
import eutil.strings.EStringUtil;

final class EnvisionFunctionWrapper extends EnvisionFunction {
	
	private Object objectInstance;
	private Method javaMethodTarget;
	private HashMap<IDatatype, Class<?>> argMapper = new HashMap<>();
	
	public EnvisionFunctionWrapper(IDatatype rt,
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
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		System.out.println(javaMethodTarget + " BEING INVOKED");
		try {
			System.out.println("argMapper: " + " : " + argMapper + " : " + EStringUtil.toString(args));
			EArrayList<Object> javaObjects = new EArrayList<>();
			for (int i = 0; i < args.length; i++) {
				IDatatype fromType = args[i].getDatatype();
				Class<?> toType = argMapper.get(fromType);
				Object conversion = args[i].convertToJavaObject();
				javaObjects.add(toType.cast(conversion));
			}
			
			//this is wrong, needs to mapped from Envision types to java types..
			Object returnValue = javaMethodTarget.invoke(objectInstance, javaObjects.toArray());
			
			EnvisionObject r = ObjectCreator.wrap(returnValue);
			System.out.println(r);
			
			//then need to convert returnValue from a java type back to an Envision type
			ret(r);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
