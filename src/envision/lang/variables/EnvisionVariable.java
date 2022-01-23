package envision.lang.variables;

import envision.exceptions.errors.FinalVarReassignmentError;
import envision.exceptions.errors.InvalidDataTypeError;
import envision.exceptions.errors.StrongVarReassignmentError;
import envision.interpreter.util.creationUtil.VariableCreator;
import envision.lang.classes.EnvisionClass;
import envision.lang.util.EnvisionDataType;

/** Generic super class for script objects. */
public abstract class EnvisionVariable extends EnvisionClass {

	protected Object value;
	
	//-----------------------------
	// ScriptVariable Constructors
	//-----------------------------
	
	protected EnvisionVariable() { this(EnvisionDataType.NULL, null); }
	
	// Dynamically determins type from object
	public EnvisionVariable(Object valIn) { this(EnvisionDataType.getDataType(valIn), valIn); }
	public EnvisionVariable(String nameIn, Object valIn) { this(EnvisionDataType.getDataType(valIn), nameIn, valIn); }
	public EnvisionVariable(EnvisionDataType typeIn, Object valIn) { this(typeIn, "noname", valIn); }
	public EnvisionVariable(EnvisionDataType typeIn, String nameIn, Object valIn) {
		super(typeIn, nameIn);
		value = valIn;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public String toString() {
		return "" + get();
	}
	
	@Override
	public boolean equals(Object in) {
		if (in instanceof EnvisionVariable) {
			EnvisionVariable i = (EnvisionVariable) in;
			if (i.get() == null) { return get() == null; }
			return i.get().equals(get());
		}
		return false;
	}
	
	//------------------------
	// ScriptVariable Getters
	//------------------------
	
	public Object get() { return value; }
	
	//------------------------
	// ScriptVariable Setters
	//------------------------
	
	public EnvisionVariable set(Object valIn) throws StrongVarReassignmentError, FinalVarReassignmentError, InvalidDataTypeError {
		return set(VariableCreator.createVar(valIn));
	}
	
	public EnvisionVariable set(EnvisionVariable valIn) throws StrongVarReassignmentError, FinalVarReassignmentError, InvalidDataTypeError {
		if (isFinal()) { throw new FinalVarReassignmentError(this, valIn); }
		value = valIn.value;
		return this;
	}
	
	//public static EnvisionVariable of(Object in) { return new EnvisionVariable(in); }
	//public static EnvisionVariable of(DataValue in) { return new EnvisionVariable(in.type, in.object); }
	//public static EnvisionVariable of(ReturnValue in) { return of(in.object); }
	
}
