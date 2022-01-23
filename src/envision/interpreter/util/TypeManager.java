package envision.interpreter.util;

import envision.interpreter.util.creationUtil.ObjectCreator;
import envision.lang.EnvisionObject;
import envision.lang.classes.EnvisionClass;
import envision.lang.util.EnvisionDataType;

import java.util.HashMap;
import java.util.HashSet;

/** Keeps track of user defined types. */
public class TypeManager {
	
	private HashMap<String, EnvisionClass> types = new HashMap();
	
	public void defineType(String typeIn, EnvisionClass objectType) {
		if (!types.containsKey(typeIn)) {
			types.put(typeIn, objectType);
		}
	}
	
	public boolean isTypeDefined(String typeIn) {
		return types.containsKey(typeIn);
	}
	
	public EnvisionClass getTypeClass(String typeIn) {
		return types.get(typeIn);
	}
	
	public HashSet getTypes() { return new HashSet(types.keySet()); }
	
	public EnvisionObject getPrimitiveType(EnvisionDataType typeIn) {
		return ObjectCreator.createDefault(typeIn.type + "_primitive", typeIn);
	}
	
}
