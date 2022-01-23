package envision.interpreter.util.creationUtil;

import envision.lang.EnvisionObject;
import envision.lang.objects.EnvisionList;
import envision.lang.objects.EnvisionNullObject;
import envision.lang.objects.EnvisionOperator;
import envision.lang.util.EnvisionDataType;
import envision.lang.variables.EnvisionDouble;
import envision.tokenizer.Keyword;
import eutil.datatypes.EArrayList;

/** Utility class designed to help with general object creation. */
public class ObjectCreator {

	//hide the constructor
	private ObjectCreator() {}
	
	//----------------------------------------------------------------------
	
	/** Wraps the given set of arguments in coresponding EnvisionObjects for internal language data communication. */
	public static EArrayList<EnvisionObject> createArgs(Object[] args) {
		EArrayList<EnvisionObject> callArgs = new EArrayList();
		for (Object o : args) {
			callArgs.add((o instanceof EnvisionObject) ? (EnvisionObject) o : createObject(o));
		}
		return callArgs;
	}
	
	/** Wraps the unknown object into an EnvisionObject. If the object was already an EnvisionObject, a casting is performed on the input instead. */
	public static EnvisionObject wrap(Object in) {
		return (in instanceof EnvisionObject) ? (EnvisionObject) in : createObject(in);
	}
	
	//----------------------------------------------------------------------
	
	public static EnvisionObject createObject(String nameIn) { return new EnvisionObject(nameIn); }
	public static EnvisionObject createObject(Object valueIn) { return createObject("noname", valueIn); }
	public static EnvisionObject createObject(Object valueIn, boolean strong) { return createObject("noname", valueIn, strong); }
	public static EnvisionObject createObject(EnvisionDataType typeIn, Object valueIn) { return createObject(typeIn.type, "noname", valueIn); }
	public static EnvisionObject createObject(String nameIn, Object valueIn) { return createObject(nameIn, valueIn, false); }
	
	public static EnvisionObject createObject(String nameIn, Object valueIn, boolean strong) {
		EnvisionDataType type = EnvisionDataType.getDataType(valueIn);
		if (type == EnvisionDataType.OBJECT) { type = EnvisionDataType.getDataType(valueIn); }
		if (type != null) { return createObject(nameIn, type, valueIn, strong); }
		return null;
	}
	
	public static EnvisionObject createObject(String typeIn, String nameIn, Object valueIn) {
		EnvisionDataType type = EnvisionDataType.getDataType(typeIn);
		if (type == EnvisionDataType.OBJECT) { type = EnvisionDataType.getDataType(valueIn); }
		if (type != null) { return createObject(nameIn, type, valueIn, true); }
		return null;
	}
	
	public static EnvisionObject createObject(String typeIn, String nameIn, Object valueIn, boolean strong) {
		EnvisionDataType type = EnvisionDataType.getDataType(typeIn);
		if (type == EnvisionDataType.OBJECT) { type = EnvisionDataType.getDataType(valueIn); }
		if (type != null) {
			EnvisionObject obj = createObject(nameIn, type, valueIn);
			if (strong) obj.setStrong();
			return obj;
		}
		return null;
	}
	
	public static EnvisionObject createObject(EnvisionDataType typeIn) { return createObject(typeIn, true); }
	public static EnvisionObject createObject(EnvisionDataType typeIn, boolean strong) {
		EnvisionObject obj = null;
		
		switch (typeIn) {
		case BOOLEAN:
		case INT:
		case DOUBLE:
		case CHAR:
		case STRING:
		case CLASS: obj = VariableCreator.createVar(typeIn); break;
		
		case ENUM: break;
		case ENUM_TYPE: break;
		case LIST: obj = VariableCreator.createList(typeIn);
		case NULL: return new EnvisionNullObject();
		case NUMBER: obj = new EnvisionDouble();
		case OBJECT: obj = new EnvisionObject();
		default: break;
		}
		
		if (strong) obj.setStrong();
		if (obj != null) return obj;
		
		return new EnvisionNullObject();
	}
	
	public static EnvisionObject createObject(String nameIn, EnvisionDataType typeIn) { return createObject(nameIn, typeIn, true); }
	public static EnvisionObject createObject(String nameIn, EnvisionDataType typeIn, boolean strong) {
		EnvisionObject obj = null;
		
		switch (typeIn) {
		case BOOLEAN:
		case INT:
		case DOUBLE:
		case CHAR:
		case STRING:
		case CLASS: obj = VariableCreator.createVar(nameIn, typeIn);
		
		case ENUM: break;
		case ENUM_TYPE: break;
		case LIST: obj = VariableCreator.createList(nameIn);
		case NULL: break;
		case NUMBER: obj = new EnvisionDouble();
		case OBJECT: obj = new EnvisionObject();
		default: break;
		}
		
		if (strong) obj.setStrong();
		if (obj != null) return obj;
		
		return new EnvisionNullObject();
	}
	
	public static EnvisionObject createObject(String nameIn, EnvisionDataType typeIn, Object valueIn) { return createObject(nameIn, typeIn, valueIn, true); }
	public static EnvisionObject createObject(String nameIn, EnvisionDataType typeIn, Object valueIn, boolean strong) {
		EnvisionObject obj = null;
		
		switch (typeIn) {
		case BOOLEAN:
		case INT:
		case DOUBLE:
		case CHAR:
		case STRING:
		case CLASS: obj = VariableCreator.createVar(nameIn, typeIn, valueIn);
		
		case ENUM: break;
		case ENUM_TYPE: break;
		case LIST: obj = new EnvisionList(typeIn, nameIn).addAll((EnvisionList) valueIn);
		case NULL: break;
		case NUMBER: obj = new EnvisionDouble((Number) valueIn);
		case OBJECT: obj = new EnvisionObject();
		case OPERATOR: obj = new EnvisionOperator(nameIn, (Keyword) valueIn);
		default: break;
		}
		
		if (strong) obj.setStrong();
		if (obj != null) return obj;
		
		return new EnvisionNullObject();
	}
	
	public static EnvisionObject createObject(String nameIn, String typeIn) {
		return new EnvisionNullObject();
	}
	
	public static EnvisionObject createDefault(String nameIn, String typeIn) {
		EnvisionDataType type = EnvisionDataType.getDataType(typeIn);
		if (type != null) { return createDefault(nameIn, type); }
		return null;
	}
	
	public static EnvisionObject createDefault(String nameIn, EnvisionDataType typeIn) {
		switch (typeIn) {
		case BOOLEAN:
		case INT:
		case DOUBLE:
		case CHAR:
		case STRING:
		case CLASS: return VariableCreator.createVar(nameIn, typeIn, null);
		default: return new EnvisionNullObject();
		}
	}

	public static EnvisionObject createCopy(EnvisionObject f) {
		if (f == null) return null;
		return f.copy();
	}
	
}
