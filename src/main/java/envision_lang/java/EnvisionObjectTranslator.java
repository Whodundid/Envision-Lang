package envision_lang.java;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.datatypes.EnvisionBoolean;
import envision_lang.lang.datatypes.EnvisionNumber;
import envision_lang.lang.datatypes.EnvisionString;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.Primitives;

public class EnvisionObjectTranslator {
	
	private EnvisionBridgeObject bridge;
	private ClassInstance envisionInstance;
	
	public EnvisionObject getEnvisionValue(String name) {
		return envisionInstance.get(name);
	}
	
	public <E> E getJavaObject(String name) {
		EnvisionObject value = getEnvisionValue(name);
		
		// figure out what kind of native java value this envision object maps to
		IDatatype type = value.getDatatype();
		Primitives primitive = type.getPrimitive();
		
		if (primitive != null) {
			switch (primitive) {
			case NULL: return null;
			case BOOLEAN: return (E) (Object) getBoolean((EnvisionBoolean) value);
			case CHAR:
			case INT:
			case DOUBLE:
			case STRING:
			case VAR:
			default: throw new IllegalArgumentException("Cannot translate this type to java! " + primitive);
			}
		}
		
		return null;
	}
	
	protected boolean getBoolean(EnvisionBoolean bool) { return bool.bool_val; }
	protected byte getByte(EnvisionNumber num) { return num.convertToJavaObject().byteValue(); }
	protected char getChar(EnvisionNumber num) { return (char) num.convertToJavaObject().intValue(); }
	protected short getShort(EnvisionNumber num) { return num.convertToJavaObject().shortValue(); }
	protected int getInt(EnvisionNumber num) { return num.convertToJavaObject().intValue(); }
	protected long getLong(EnvisionNumber num) { return num.convertToJavaObject().longValue(); }
	protected float getFloat(EnvisionNumber num) { return num.convertToJavaObject().floatValue(); }
	protected double getDouble(EnvisionNumber num) { return num.convertToJavaObject().doubleValue(); }
	protected String getString(EnvisionString s) { return s.get_i(); }
	
	public void setValue(String name, Object value) {
		
	}
	
}
