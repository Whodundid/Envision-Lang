package envision_lang.lang.datatypes;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.functions.FunctionPrototype;
import envision_lang.lang.language_errors.error_types.InvalidDatatypeError;
import envision_lang.lang.language_errors.error_types.NoOverloadError;
import envision_lang.lang.language_errors.error_types.NullVariableError;
import envision_lang.lang.language_errors.error_types.objects.ClassCastError;
import envision_lang.lang.language_errors.error_types.objects.UnsupportedOverloadError;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.EnvisionStaticTypes;
import envision_lang.tokenizer.Operator;

/**
 * A script variable representing a single character.
 * 
 * @author Hunter Bragg
 */
public final class EnvisionChar extends EnvisionVariable<Character> {
	
	public static final IDatatype CHAR_TYPE = EnvisionStaticTypes.CHAR_TYPE;
	
	public static final EnvisionChar NULL_CHAR = EnvisionCharClass.valueOf('\0');
	
	//========
	// Fields
	//========
	
	public final char char_val;
	
	//--------------
	// Constructors
	//--------------
	
	EnvisionChar() { this('\0'); }
	EnvisionChar(char val) {
		super(EnvisionCharClass.CHAR_CLASS);
		char_val = val;
	}
	
	EnvisionChar(boolean val) {
		super(EnvisionCharClass.CHAR_CLASS);
		char_val = (val) ? 'T' : 'F';
	}
	
	EnvisionChar(EnvisionChar in) {
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
		return "'" + String.valueOf(char_val) + "'";
	}
	
	/**
	 * Returns this exact same char.
	 */
	@Override
	public EnvisionChar copy() {
		return this;
	}
	
	@Override
	public Object convertToJavaObject() {
		return char_val;
	}
	
	@Override
	public EnvisionChar get() {
		return this;
	}
	
	@Override
	public Character get_i() {
		return char_val;
	}
	
	@Override
	public boolean supportsOperator(Operator op) {
		return switch (op) {
		case EQUALS, NOT_EQUALS -> true;
		case ADD, MUL -> true;
		//don't accept any other operator types
		default -> false;
		};
	}
	
	@Override
	public EnvisionObject handleOperatorOverloads
		(EnvisionInterpreter interpreter, String scopeName, Operator op, EnvisionObject obj)
			throws UnsupportedOverloadError
	{
		//don't allow null expression objects
		if (obj == null) throw new NullVariableError();
		
		//only accept if an EnvisionVariable type
		if (!(obj instanceof EnvisionVariable))
			throw new InvalidDatatypeError("Expected a variable but got '" + obj.getDatatype() + "' instead!");
		
		//extract incoming object datatype
		IDatatype obj_type = obj.getDatatype();
		
		//only natively support '+' and '*'
		switch (op) {
		case ADD:
		{
			//due to the fact that char additions require the datatype being
			//upgraded to a string, check for strong char and error if true
			//if (isStrong()) throw new StrongVarReassignmentError(this, "");
			
			//only accept char or string objects
			if (!EnvisionStaticTypes.CHAR_TYPE.compare(obj_type) && !EnvisionStaticTypes.STRING_TYPE.compare(obj_type))
				throw new InvalidDatatypeError(EnvisionStaticTypes.STRING_TYPE, obj_type);
			
			//char additions require the char to be upgraded to a string
			String new_val = String.valueOf(char_val);
			if (obj instanceof EnvisionChar env_char) new_val += env_char.char_val;
			if (obj instanceof EnvisionString env_str) new_val += env_str.string_val;
			
			//to upgrade the datatype from char -> string requires creating new string object
			EnvisionString new_obj = EnvisionStringClass.valueOf(new_val);
			
			//assign new value to vars and immediately return created object
			interpreter.scope().setFast(scopeName, EnvisionStaticTypes.STRING_TYPE, new_obj);
			return new_obj;
		}
		
		case MUL:
		{
			//due to the fact that char mul_additions require the datatype being
			//upgraded to a string, check for strong char and error if true
			//if (isStrong()) throw new StrongVarReassignmentError(this, "");
			
			//only accept int objects
			if (!EnvisionStaticTypes.INT_TYPE.compare(obj_type))
				throw new InvalidDatatypeError(EnvisionStaticTypes.INT_TYPE, obj_type);
			
			//char mul_additions require the char to be upgraded to a string
			StringBuilder new_val = new StringBuilder();
			long multiply_val = ((EnvisionInt) obj).int_val;
			for (int i = 0; i < multiply_val; i++) new_val.append(char_val);
			
			//to upgrade the datatype from char -> string requires creating new string object
			EnvisionString new_obj = EnvisionStringClass.valueOf(new_val);
			
			//assign new value to vars and immediately return created object
			interpreter.scope().setFast(scopeName, new_obj.getDatatype(), new_obj);
			return new_obj;
		}
		
		//throw error if this point is reached
		//default: throw new UnsupportedOverloadError(this, op, "[" + obj.getDatatype() + ":" + obj + "]");
		default: return super.handleOperatorOverloads(interpreter, scopeName, op, obj);
		}
	}
	
	@Override
	public EnvisionObject handleObjectCasts(IDatatype castType) throws ClassCastError {
		//determine specific cast types
		if (EnvisionStaticTypes.BOOL_TYPE.compare(castType)) return EnvisionBooleanClass.valueOf(char_val);
		if (EnvisionStaticTypes.INT_TYPE.compare(castType)) return EnvisionIntClass.valueOf(char_val);
		if (EnvisionStaticTypes.DOUBLE_TYPE.compare(castType)) return EnvisionDoubleClass.valueOf(char_val);
		if (EnvisionStaticTypes.STRING_TYPE.compare(castType)) return EnvisionStringClass.valueOf(char_val);
		
		return super.handleObjectCasts(castType);
	}
	
	@Override
	protected EnvisionObject handlePrimitive(FunctionPrototype proto, EnvisionObject[] args) {
		String funcName = proto.getFunctionName();
		if (!proto.hasOverload(args)) throw new NoOverloadError(funcName, args);
		
		return switch (funcName) {
		case "toUpperCase" -> EnvisionCharClass.valueOf(Character.toUpperCase(char_val));
		case "toLowerCase" -> EnvisionCharClass.valueOf(Character.toLowerCase(char_val));
		default -> super.handlePrimitive(proto, args);
		};
	}
	
}