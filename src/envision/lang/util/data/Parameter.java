package envision.lang.util.data;

import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.classes.EnvisionClass;

public class Parameter {
	
	public final String datatype;
	public final String name;
	public final Object defaultValue;
	public final boolean hasDefault;
	
	public Parameter(String typeIn, String nameIn) {
		datatype = typeIn;
		name = nameIn;
		defaultValue = null;
		hasDefault = false;
	}
	
	public Parameter(String typeIn, String nameIn, Object defaultValueIn) {
		datatype = typeIn;
		name = nameIn;
		defaultValue = defaultValueIn;
		hasDefault = true;
	}
	
	public Parameter(EnvisionObject typeIn) {
		if (typeIn == null) { datatype = null; }
		else if (typeIn instanceof EnvisionClass) { datatype = typeIn.getTypeString(); }
		else if (typeIn instanceof ClassInstance) { datatype = typeIn.getTypeString(); }
		//else if (typeIn instanceof EnvisionClass) { datatype = ((EnvisionClass) typeIn).getName(); }
		//else if (typeIn instanceof ClassInstance) { datatype = ((ClassInstance) typeIn).getTypeString(); }
		else { datatype = typeIn.getInternalType().type; }
		
		name = null;
		defaultValue = null;
		hasDefault = false;
	}
	
	@Override
	public String toString() {
		String d = (hasDefault) ? " = " + defaultValue : "";
		return "[" + datatype + ", " + name + d + "]";
	}
	
	/** Compares the datatypes of each parameter, name is irrelevant when comparing. */
	public boolean compare(Parameter paramIn) {
		return (paramIn != null) ? paramIn.datatype.equals(datatype) : false;
	}
	
	public boolean isNumber() {
		switch (datatype) {
		case "int": case "double": return true;
		default: return false;
		}
	}
	
}
