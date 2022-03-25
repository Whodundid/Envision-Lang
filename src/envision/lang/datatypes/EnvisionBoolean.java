package envision.lang.datatypes;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.FinalVarReassignmentError;
import envision.lang.EnvisionObject;

/**
 * The EnvisionBoolean class wraps the value of a Java:Boolean
 * value to be used as the method to represent boolean objects
 * within the Envision:Java scriting language.
 * 
 * @author Hunter Bragg
 */
public class EnvisionBoolean extends EnvisionVariable {
	
	/**
	 * Static wrapper for the constant boolean value of 'true'.
	 */
	public static final EnvisionBoolean TRUE = EnvisionBooleanClass.newBoolean(true);
	
	/**
	 * Static wrapper for the constant boolean value of 'false'.
	 */
	public static final EnvisionBoolean FALSE = EnvisionBooleanClass.newBoolean(false);
	
	//--------
	// Fields
	//--------
	
	public boolean bool_val;
	
	//--------------
	// Constructors
	//--------------
	
	protected EnvisionBoolean() { this(false); }
	protected EnvisionBoolean(boolean val) {
		super(EnvisionBooleanClass.BOOLEAN_CLASS);
		bool_val = val;
	}
	
	protected EnvisionBoolean(EnvisionBoolean objIn) {
		super(EnvisionBooleanClass.BOOLEAN_CLASS);
		bool_val = objIn.bool_val;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public EnvisionObject get() {
		return this;
	}
	
	@Override
	public Object get_i() {
		return bool_val;
	}
	
	@Override
	public EnvisionVariable set(EnvisionObject valIn) throws FinalVarReassignmentError {
		if (isFinal()) throw new FinalVarReassignmentError(this, valIn);
		if (valIn instanceof EnvisionBoolean env_bool) {
			bool_val = env_bool.bool_val;
			return this;
		}
		throw new EnvisionError("Attempted to internally set non-boolean value to a boolean!");
	}
	
	@Override
	public EnvisionVariable set_i(Object valIn) throws FinalVarReassignmentError {
		if (isFinal()) throw new FinalVarReassignmentError(this, valIn);
		if (valIn instanceof Boolean bool) {
			bool_val = bool;
			return this;
		}
		throw new EnvisionError("Attempted to internally set non-boolean value to a boolean!");
	}
	
	/**
	 * Creates a new EnvisionBoolean with the same value as this one.
	 */
	@Override
	public EnvisionBoolean copy() {
		return new EnvisionBoolean(this);
	}
	
	/**
	 * @return True if wrapping the boolean value of true.
	 */
	public boolean isTrue() {
		return bool_val;
	}
	
	/**
	 * @return True if wrapping the boolean value of false.
	 */
	public boolean isFalse() {
		return !bool_val;
	}
	
}
