package envision.lang.datatypes;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.FinalVarReassignmentError;
import envision.lang.EnvisionObject;

/**
 * 
 * @author Hunter Bragg
 */
public class EnvisionChar extends EnvisionVariable {
	
	public static final char NULL_CHAR = '\0';
	
	public char char_val;
	
	//--------------
	// Constructors
	//--------------
	
	protected EnvisionChar() { this(NULL_CHAR); }
	protected EnvisionChar(char val) {
		super(EnvisionCharClass.CHAR_CLASS);
		char_val = val;
	}
	
	protected EnvisionChar(boolean val) {
		super(EnvisionCharClass.CHAR_CLASS);
		char_val = (val) ? 'T' : 'F';
	}
	
	protected EnvisionChar(EnvisionChar in) {
		super(EnvisionCharClass.CHAR_CLASS);
		char_val = in.char_val;
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
		return char_val;
	}
	
	@Override
	public EnvisionVariable set(EnvisionObject valIn) throws FinalVarReassignmentError {
		if (isFinal()) throw new FinalVarReassignmentError(this, valIn);
		if (valIn instanceof EnvisionChar env_char) {
			char_val = env_char.char_val;
			return this;
		}
		throw new EnvisionError("Attempted to internally set non-char value to a char!");
	}
	
	@Override
	public EnvisionVariable set_i(Object valIn) throws FinalVarReassignmentError {
		if (isFinal()) throw new FinalVarReassignmentError(this, valIn);
		if (valIn instanceof Character char_val) {
			this.char_val = char_val;
			return this;
		}
		throw new EnvisionError("Attempted to internally set non-char value to a char!");
	}
	
	@Override
	public EnvisionChar copy() {
		return EnvisionCharClass.newChar(this);
	}
	
}
