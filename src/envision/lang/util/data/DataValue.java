package envision.lang.util.data;

import envision.interpreter.util.creationUtil.VariableUtil;
import envision.lang.EnvisionObject;
import envision.lang.util.EnvisionDataType;
import envision.lang.variables.EnvisionVariable;

public class DataValue {
	
	public final String type;
	public final Object object;
	
	public DataValue() {
		type = EnvisionDataType.NULL.type;
		object = null;
	}
	
	public DataValue(Object objectIn) {
		type = EnvisionDataType.getTypeName(objectIn);
		object = objectIn;
	}
	
	public DataValue(String typeIn, Object objectIn) {
		type = typeIn;
		object = objectIn;
	}
	
	@Override
	public String toString() {
		return type + ":" + object;
	}
	
	public static DataValue nil() { return new DataValue(EnvisionDataType.NULL.type, null); }
	public static DataValue of(EnvisionVariable var) { return new DataValue(var.getInternalType().type, var.get()); }
	public static DataValue of(EnvisionObject obj) { return new DataValue(VariableUtil.getTypeString(obj), obj); }
	
}
