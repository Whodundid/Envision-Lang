package envision.lang.util.data;

import envision.interpreter.util.creationUtil.VariableUtil;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionVariable;
import envision.lang.util.Primitives;

public class DataValue {
	
	public final String type;
	public final Object object;
	
	public DataValue() {
		type = Primitives.NULL.string_type;
		object = null;
	}
	
	public DataValue(Object objectIn) {
		type = Primitives.getTypeString(objectIn);
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
	
	//--------
	// Static
	//--------
	
	public static DataValue nil() {
		return new DataValue(Primitives.NULL.string_type, null);
	}
	
	public static DataValue of(EnvisionVariable var) {
		return new DataValue(var.getDatatype().getType(), var.get());
	}
	
	public static DataValue of(EnvisionObject obj) {
		return new DataValue(VariableUtil.getTypeString(obj), obj);
	}
	
}
