package envision_lang.lang.datatypes;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.exceptions.errors.FinalVarReassignmentError;
import envision_lang.exceptions.errors.InvalidDatatypeError;
import envision_lang.exceptions.errors.NoOverloadError;
import envision_lang.exceptions.errors.NullVariableError;
import envision_lang.exceptions.errors.StrongVarReassignmentError;
import envision_lang.exceptions.errors.objects.ClassCastError;
import envision_lang.exceptions.errors.objects.UnsupportedOverloadError;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.internal.FunctionPrototype;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.StaticTypes;
import envision_lang.tokenizer.Operator;

/**
 * A script variable representing a single character.
 * 
 * @author Hunter Bragg
 */
public class EnvisionChar extends EnvisionVariable {
	
	public static final EnvisionChar NULL_CHAR = EnvisionCharClass.newChar('\0');
	
	public char char_val;
	
	//--------------
	// Constructors
	//--------------
	
	protected EnvisionChar() { this('\0'); }
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
	public Object convertToJavaObject() {
		return char_val;
	}
	
	@Override
	public EnvisionChar get() {
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
		throw new EnvisionLangError("Attempted to internally set non-char value to a char!");
	}
	
	@Override
	public EnvisionVariable set_i(Object valIn) throws FinalVarReassignmentError {
		if (isFinal()) throw new FinalVarReassignmentError(this, valIn);
		if (valIn instanceof Character char_val) {
			this.char_val = char_val;
			return this;
		}
		throw new EnvisionLangError("Attempted to internally set non-char value to a char!");
	}
	
	@Override
	public boolean supportsOperator(Operator op) {
		return switch (op) {
		case EQUALS, NOT_EQUALS -> true;
		case ADD, MUL -> true;
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
			if (isStrong()) throw new StrongVarReassignmentError(this, "");
			
			//only accept char or string objects
			if (obj_type != StaticTypes.CHAR_TYPE && obj_type != StaticTypes.STRING_TYPE)
				throw new InvalidDatatypeError(StaticTypes.STRING_TYPE, obj_type);
			
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
			if (obj_type != StaticTypes.INT_TYPE)
				throw new InvalidDatatypeError(StaticTypes.INT_TYPE, obj_type);
			
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
		//default: throw new UnsupportedOverloadError(this, op, "[" + obj.getDatatype() + ":" + obj + "]");
		default: return super.handleOperatorOverloads(interpreter, scopeName, op, obj);
		}
	}
	
	@Override
	public EnvisionObject handleObjectCasts(IDatatype castType) throws ClassCastError {
		//determine specific cast types
		if (StaticTypes.BOOL_TYPE.compare(castType)) return EnvisionBooleanClass.newBoolean(char_val);
		if (StaticTypes.INT_TYPE.compare(castType)) return EnvisionIntClass.newInt(char_val);
		if (StaticTypes.DOUBLE_TYPE.compare(castType)) return EnvisionDoubleClass.newDouble(char_val);
		if (StaticTypes.STRING_TYPE.compare(castType)) return EnvisionStringClass.newString(char_val);
		
		return super.handleObjectCasts(castType);
	}
	
	@Override
	protected EnvisionObject handlePrimitive(FunctionPrototype proto, EnvisionObject[] args) {
		String funcName = proto.getFunctionName();
		if (!proto.hasOverload(args)) throw new NoOverloadError(funcName, args);
		
		return switch (funcName) {
		case "get" -> get();
		case "set" -> set(args[0]);
		case "toUpperCase" -> EnvisionCharClass.newChar(Character.toUpperCase(char_val));
		case "toLowerCase" -> EnvisionCharClass.newChar(Character.toLowerCase(char_val));
		default -> super.handlePrimitive(proto, args);
		};
	}
	
}