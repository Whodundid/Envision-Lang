package envision_lang.lang.natives;

import envision_lang.lang.EnvisionObject;

public class EnvisionParameter {
	
	public final IDatatype datatype;
	public final String name;
	public final Object defaultValue;
	public final boolean hasDefault;
	
	//--------------
	// Constructors
	//--------------
	
	public EnvisionParameter(IDatatype typeIn, String nameIn) {
		datatype = typeIn;
		name = nameIn;
		defaultValue = null;
		hasDefault = false;
	}
	
	public EnvisionParameter(IDatatype typeIn, String nameIn, Object defaultValueIn) {
		datatype = typeIn;
		name = nameIn;
		defaultValue = defaultValueIn;
		hasDefault = true;
	}
	
	public EnvisionParameter(EnvisionObject typeIn) {
		if (typeIn == null) datatype = null;
		else datatype = typeIn.getDatatype();
		
		name = null;
		defaultValue = null;
		hasDefault = false;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public String toString() {
		String d = (hasDefault) ? " = " + defaultValue : "";
		return "[" + datatype + ", " + name + d + "]";
	}
	
	//---------
	// Methods
	//---------
	
	/** Compares the datatypes of each parameter, name is irrelevant when comparing. */
	public boolean compare(EnvisionParameter paramIn) {
		return paramIn != null && paramIn.datatype.equals(datatype);
	}
	
	public boolean isNumber() {
		return datatype.isNumber();
	}
	
}
