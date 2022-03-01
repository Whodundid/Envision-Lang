package envision.lang.datatypes;

import envision.exceptions.errors.FinalVarReassignmentError;
import envision.lang.classes.EnvisionClass;
import envision.lang.util.EnvisionDatatype;

/**
 * The EnvisionVariable is a specific EnvisionObject type
 * which is primarily responsible for providing a direct
 * mapping between Envision datatype values and actual Java
 * Object values.
 * <p>
 * Due to the fact that Envision:Java is written in Java, all
 * corresponding Envision datatypes must be backed by some
 * kind of equivalent datatype structure within Java. The
 * EnvisionVariable class is intended to not only back that
 * object value but also directly manage how that data
 * interacts within the rest of the EnvisionInterpreter.
 * <p>
 * EnvisionVariable, at its core, is intended to be used
 * purely as a superclass structure for which other specific
 * datatype models are built from. While Envision:java does
 * directly support generic 'var' values for both variable
 * instantiation and assignment, these are not truely
 * generic values on the backend of things. As such, even
 * 'var' value types are still directly mapped to a specific,
 * strongly typed Java datatype under the hood. Due to this
 * fact, generic 'var' values are not technically generic
 * at all in nature. However, any 'var' value may be
 * dynamically reassigned at runtime by simply assigning a
 * new value to the variable. Upon assigning a new value
 * to an existing generic 'var' EnvisionVariable, The new
 * value's datatype is first automatically determined by the
 * interpreter and is assigned to the variable.
 */
public abstract class EnvisionVariable extends EnvisionClass {

	/**
	 * The backing object stored by all EnvisionVariables.
	 * 
	 * <p>
	 * This Object is responsible for managing the internal
	 * Java datatype values which are the primary backing
	 * for all datatypes in Envision:Java.
	 * 
	 * <p>
	 * For instnace, an EnvisionBoolean would directly be
	 * backed with an actual Java Boolean at its core.
	 * 
	 * @author Hunter
	 */
	protected Object var_value;
	
	//-----------------------------
	// ScriptVariable Constructors
	//-----------------------------
	
	protected EnvisionVariable(EnvisionDatatype type) {
		this(type, DEFAULT_NAME);
	}
	
	protected EnvisionVariable(EnvisionDatatype type, String nameIn) {
		super(type, nameIn);
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public String toString() {
		return "" + var_value;
	}
	
	@Override
	public boolean equals(Object in) {
		if (in instanceof EnvisionVariable v) {
			Object val = v.get();
			return (val == null) ? (var_value == null) : (val.equals(var_value));
		}
		return false;
	}
	
	//------------------------
	// ScriptVariable Getters
	//------------------------
	
	/**
	 * Returns the underlying Java Object which actually backs
	 * this EnvisionVariable.
	 * 
	 * @return The backing Java Object.
	 */
	public Object get() { return var_value; }
	
	//------------------------
	// ScriptVariable Setters
	//------------------------
	
	/*
	public EnvisionVariable set(EnvisionVariable valIn) throws FinalVarReassignmentError {
		if (isFinal()) throw new FinalVarReassignmentError(this, valIn);
		var_value = valIn.var_value;
		return this;
	}
	*/
	
	public EnvisionVariable set(Object valIn) throws FinalVarReassignmentError {
		if (isFinal()) throw new FinalVarReassignmentError(this, valIn);
		var_value = valIn;
		return this;
	}
	
}
