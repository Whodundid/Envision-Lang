package envision.lang.variables;

import envision.lang.util.EnvisionDataType;

public class EnvisionNumber extends EnvisionVariable {

	public EnvisionNumber(String typeIn, Number valIn) { super(typeIn, valIn); }
	public EnvisionNumber(EnvisionDataType typeIn, Number valIn) { this(typeIn, "noname", valIn); }
	public EnvisionNumber(EnvisionDataType typeIn, String nameIn, Number valIn) {
		super(typeIn, nameIn, valIn);
	}
	
	public int intVal() { return (int) get(); }
	public double doubleVal() { return (double) get(); }
	
	public static EnvisionNumber of(Object in) {
		in = convert(in);
		
		if (in instanceof String) {
			EnvisionDataType type = EnvisionDataType.getNumberType((String) in);
			switch (type) {
			case INT: return EnvisionInt.of((String) in);
			case DOUBLE: return EnvisionDouble.of((String) in);
			default: break;
			}
		}
		
		if (in instanceof Character) {
			return new EnvisionInt((char) in);
		}
		
		if (in instanceof Number) {
			return of((Number) in);
		}
		
		throw new RuntimeException("Cannot create number from given type: " + in);
	}
	
	public static EnvisionNumber of(Number in) {
		if (in instanceof Byte) { return new EnvisionInt(in.longValue()); }
		if (in instanceof Short) { return new EnvisionInt(in.longValue()); }
		if (in instanceof Integer) { return new EnvisionInt(in.longValue()); }
		if (in instanceof Long) { return new EnvisionInt(in.longValue()); }
		if (in instanceof Float) { return new EnvisionDouble(in.doubleValue()); }
		if (in instanceof Double) { return new EnvisionDouble(in.doubleValue()); }
		throw new RuntimeException("Failed to create number! This shouldn't be possible! " + in);
	}
	
}
