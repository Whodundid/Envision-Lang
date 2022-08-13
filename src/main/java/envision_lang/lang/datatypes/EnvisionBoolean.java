package envision_lang.lang.datatypes;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.exceptions.errors.FinalVarReassignmentError;
import envision_lang.exceptions.errors.InvalidDatatypeError;
import envision_lang.exceptions.errors.NoOverloadError;
import envision_lang.exceptions.errors.NullVariableError;
import envision_lang.exceptions.errors.objects.ClassCastError;
import envision_lang.exceptions.errors.objects.UnsupportedOverloadError;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.internal.FunctionPrototype;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.util.StaticTypes;
import envision_lang.tokenizer.Operator;

/**
 * The EnvisionBoolean class wraps the value of a Java:Boolean
 * value to be used as the method to represent boolean objects
 * within the Envision:Java Scripting Language.
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
	public boolean equals(Object obj) {
		return (obj instanceof EnvisionBoolean env_bool && env_bool.bool_val == bool_val);
	}
	
	@Override
	public String toString() {
		return String.valueOf(bool_val);
	}
	
	@Override
	public EnvisionBoolean get() {
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
		throw new EnvisionLangError("Attempted to internally set non-boolean value to a boolean!");
	}
	
	@Override
	public EnvisionVariable set_i(Object valIn) throws FinalVarReassignmentError {
		if (isFinal()) throw new FinalVarReassignmentError(this, valIn);
		if (valIn instanceof Boolean bool) {
			bool_val = bool;
			return this;
		}
		throw new EnvisionLangError("Attempted to internally set non-boolean value to a boolean!");
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
	
	@Override
	public boolean supportsOperator(Operator op) {
		return switch (op) {
		//unary
		case NEGATE -> true;
		//relational
		case EQUALS, NOT_EQUALS -> true;
		//logical
		case AND, OR -> true;
		//bit-wise
		case BW_AND, BW_OR, BW_XOR -> true;
		//bit-wise assignment
		case BW_AND_ASSIGN, BW_OR_ASSIGN, BW_XOR_ASSIGN -> true;
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
		
		//only accept if an EnvisionBoolean type
		if (!(obj instanceof EnvisionBoolean))
			throw new InvalidDatatypeError(internalType, obj.getDatatype());
		
		EnvisionBoolean in = (EnvisionBoolean) obj;
		
		//only support '!', '&&' and '||', '&', '|', '^', '&=', '|=', '^='
		
		switch (op) {
		//unary operators
		case NEGATE:		return EnvisionBooleanClass.newBoolean(!bool_val);
		//logical operators
		case AND:			return EnvisionBooleanClass.newBoolean(bool_val && in.bool_val);
		case OR:			return EnvisionBooleanClass.newBoolean(bool_val || in.bool_val);
		//bit-wise operators
		case BW_AND:		return EnvisionBooleanClass.newBoolean(bool_val & in.bool_val);
		case BW_OR:			return EnvisionBooleanClass.newBoolean(bool_val | in.bool_val);
		case BW_XOR:		return EnvisionBooleanClass.newBoolean(bool_val ^ in.bool_val);
		//bit-wise assignment operators
		case BW_AND_ASSIGN:		bool_val &= in.bool_val; return this;
		case BW_OR_ASSIGN:		bool_val |= in.bool_val; return this;
		case BW_XOR_ASSIGN:		bool_val ^= in.bool_val; return this;
			
		//throw error if this point is reached
		//default: throw new UnsupportedOverloadError(this, op, "[" + obj.getDatatype() + ":" + obj + "]");
		default: return super.handleOperatorOverloads(interpreter, scopeName, op, obj);
		}
	}
	
	@Override
	public EnvisionObject handleObjectCasts(IDatatype castType) throws ClassCastError {
		//determine specific cast types
		if (StaticTypes.BOOL_TYPE.compare(castType)) return this;
		if (StaticTypes.INT_TYPE.compare(castType)) return EnvisionIntClass.newInt(bool_val);
		if (StaticTypes.DOUBLE_TYPE.compare(castType)) return EnvisionDoubleClass.newDouble(bool_val);
		if (StaticTypes.STRING_TYPE.compare(castType)) return EnvisionStringClass.newString(bool_val);
		
		throw new ClassCastError(this, castType);
	}
	
	@Override
	protected EnvisionObject handlePrimitive(FunctionPrototype proto, EnvisionObject[] args) {
		String funcName = proto.getFunctionName();
		if (!proto.hasOverload(args)) throw new NoOverloadError(funcName, args);
		
		return switch (funcName) {
		case "get" -> get();
		case "set" -> set(args[0]);
		default -> super.handlePrimitive(proto, args);
		};
	}
	
	//---------
	// Methods
	//---------
	
	public EnvisionBoolean negate() {
		bool_val = !bool_val;
		return this;
	}
	
}