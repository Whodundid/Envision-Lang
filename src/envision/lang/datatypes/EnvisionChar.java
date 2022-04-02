package envision.lang.datatypes;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.FinalVarReassignmentError;
import envision.exceptions.errors.InvalidDatatypeError;
import envision.exceptions.errors.NullVariableError;
import envision.exceptions.errors.StrongVarReassignmentError;
import envision.exceptions.errors.objects.UnsupportedOverloadError;
import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.util.EnvisionDatatype;
import envision.tokenizer.Operator;

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
	public boolean equals(Object obj) {
		return (obj instanceof EnvisionChar env_char && env_char.char_val == char_val);
	}
	
	@Override
	public String toString() {
		return String.valueOf(char_val);
	}
	
	@Override
	public EnvisionChar copy() {
		return EnvisionCharClass.newChar(this);
	}
	
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
	public boolean supportsOperator(Operator op) {
		return switch (op) {
		case ADD, MUL -> true;
		default -> false;
		};
	}
	
	@Override
	public EnvisionObject handleOperatorOverloads
		(EnvisionInterpreter interpreter, String scopeName, Operator op, EnvisionObject obj)
			throws UnsupportedOverloadError
	{
		//dont allow null expression objects
		if (obj == null) throw new NullVariableError();
		
		//only accept if an EnvisionVariable type
		if (!(obj instanceof EnvisionVariable))
			throw new InvalidDatatypeError("Expected a variable but got '" + obj.getDatatype() + "' instead!");
		
		//extract incomming object datatype
		EnvisionDatatype obj_type = obj.getDatatype();
		
		//only natively support '+' and '*'
		switch (op) {
		case ADD:
		{
			//due to the fact that char additions require the datatype being
			//upgraded to a string, check for strong char and error if true
			if (isStrong()) throw new StrongVarReassignmentError(this, "");
			
			//only accept char or string objects
			if (obj_type != EnvisionDatatype.CHAR_TYPE && obj_type != EnvisionDatatype.STRING_TYPE)
				throw new InvalidDatatypeError(EnvisionDatatype.STRING_TYPE, obj_type);
			
			//char additions require the char to be upgraded to a string
			String new_val = String.valueOf(char_val);
			if (obj instanceof EnvisionChar env_char) new_val += env_char.char_val;
			if (obj instanceof EnvisionString env_str) new_val += env_str.string_val;
			
			//to upgrade the datatype from char -> string requires creating new string object
			EnvisionString new_obj = EnvisionStringClass.newString(new_val);
			
			//assign new value to vars and immediately return created object
			interpreter.scope().set(scopeName, new_obj.getDatatype(), new_obj);
			return new_obj;
		}
		
		case MUL:
		{
			//due to the fact that char mul_additions require the datatype being
			//upgraded to a string, check for strong char and error if true
			if (isStrong()) throw new StrongVarReassignmentError(this, "");
			
			//only accept int objects
			if (obj_type != EnvisionDatatype.INT_TYPE)
				throw new InvalidDatatypeError(EnvisionDatatype.INT_TYPE, obj_type);
			
			//char mul_additions require the char to be upgraded to a string
			StringBuilder new_val = new StringBuilder();
			long multiply_val = ((EnvisionInt) obj).int_val;
			for (int i = 0; i < multiply_val; i++) new_val.append(char_val);
			
			//to upgrade the datatype from char -> string requires creating new string object
			EnvisionString new_obj = EnvisionStringClass.newString(new_val);
			
			//assign new value to vars and immediately return created object
			interpreter.scope().set(scopeName, new_obj.getDatatype(), new_obj);
			return new_obj;
		}
		
		//throw error if this point is reached
		default: throw new UnsupportedOverloadError(this, op, "[" + obj.getDatatype() + ":" + obj + "]");
		}
	}
	
}