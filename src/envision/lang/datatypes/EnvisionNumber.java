package envision.lang.datatypes;

import envision.lang.util.EnvisionDatatype;
import envision.lang.util.Primitives;

public class EnvisionNumber extends EnvisionVariable {
	
	private boolean isInt;
	
	//--------------
	// Constructors
	//--------------
	
	protected EnvisionNumber(EnvisionDatatype type, String nameIn) {
		super(type, nameIn);
		isInt = type.getPrimitiveType() == Primitives.INT;
	}
	
	//---------
	// Methods
	//---------
	
	public int intVal() { return (int) get(); }
	public double doubleVal() { return (double) get(); }
	
	public boolean isInt() {
		return isInt;
	}
	
	//--------
	// Static
	//--------
	
	public static EnvisionNumber of(Object in) {
		in = convert(in);
		
		if (in instanceof String s) {
			Primitives type = Primitives.getNumberType((String) in);
			switch (type) {
			case INT: return new EnvisionInt(s);
			case DOUBLE: return new EnvisionDouble(s);
			default: break;
			}
		}
		
		if (in instanceof Character c) return new EnvisionInt(c);
		if (in instanceof Number n) return of(n);
		
		throw new RuntimeException("Cannot create number from given type: " + in);
	}
	
	public static EnvisionNumber of(Number in) {
		if (in instanceof Byte) return new EnvisionInt(in.longValue());
		if (in instanceof Short) return new EnvisionInt(in.longValue());
		if (in instanceof Integer) return new EnvisionInt(in.longValue());
		if (in instanceof Long) return new EnvisionInt(in.longValue());
		if (in instanceof Float) return new EnvisionDouble(in.doubleValue());
		if (in instanceof Double) return new EnvisionDouble(in.doubleValue());
		throw new RuntimeException("Failed to create number! This shouldn't be possible! " + in);
	}
	
}
