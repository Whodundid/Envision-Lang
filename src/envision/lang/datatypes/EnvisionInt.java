package envision.lang.datatypes;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.FinalVarReassignmentError;
import envision.exceptions.errors.InvalidDatatypeError;
import envision.exceptions.errors.NoOverloadError;
import envision.exceptions.errors.NullVariableError;
import envision.exceptions.errors.objects.ClassCastError;
import envision.exceptions.errors.objects.UnsupportedOverloadError;
import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.FunctionPrototype;
import envision.tokenizer.Operator;

/**
 * A script variable representing a number without a decimal place.
 * Encoded with Java Longs.
 */
public class EnvisionInt extends EnvisionNumber {
	
	public static final EnvisionInt MIN_VALUE = EnvisionIntClass.newInt(Long.MIN_VALUE);
	public static final EnvisionInt MAX_VALUE = EnvisionIntClass.newInt(Long.MAX_VALUE);
	
	public long int_val;
	
	//--------------
	// Constructors
	//--------------
	
	protected EnvisionInt() { this(0l); }
	protected EnvisionInt(Number in) { this(in.longValue()); }
	protected EnvisionInt(long in) {
		super(EnvisionIntClass.INT_CLASS);
		int_val = in;
	}
	
	protected EnvisionInt(boolean val) {
		super(EnvisionIntClass.INT_CLASS);
		int_val = (val) ? 1 : 0;
	}
	
	protected EnvisionInt(EnvisionInt in) {
		super(EnvisionIntClass.INT_CLASS);
		int_val = in.int_val;
	}
	
	protected EnvisionInt(EnvisionNumber in) {
		super(EnvisionIntClass.INT_CLASS);
		int_val = in.intVal().int_val;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof EnvisionInt env_int && env_int.int_val == int_val);
	}
	
	@Override public String toString() { return String.valueOf(int_val); }
	@Override public EnvisionInt copy() { return EnvisionIntClass.newInt(int_val); }
	
	@Override public EnvisionInt negate() { int_val = -int_val; return this; }
	
	@Override public long intVal_i() { return int_val; }
	@Override public double doubleVal_i() { return (double) int_val; }
	@Override public EnvisionInt intVal() { return this; }
	@Override public EnvisionDouble doubleVal() { return EnvisionDoubleClass.newDouble(int_val); }
	
	@Override public EnvisionInt get() { return this; }
	@Override public Object get_i() { return int_val; }
	
	@Override
	public EnvisionVariable set(EnvisionObject valIn) throws FinalVarReassignmentError {
		if (isFinal()) throw new FinalVarReassignmentError(this, valIn);
		if (valIn instanceof EnvisionInt env_int) {
			this.int_val = env_int.int_val;
			return this;
		}
		throw new EnvisionError("Attempted to internally set non-long value to a long!");
	}
	
	@Override
	public EnvisionVariable set_i(Object valIn) throws FinalVarReassignmentError {
		if (isFinal()) throw new FinalVarReassignmentError(this, valIn);
		if (valIn instanceof Long long_val) {
			this.int_val = long_val;
			return this;
		}
		//have to account for integers in this case
		else if (valIn instanceof Integer int_val) {
			this.int_val = int_val;
			return this;
		}
		throw new EnvisionError("Attempted to internally set non-long value to a long!");
	}
	
	@Override
	public boolean supportsOperator(Operator op) {
		return switch (op) {
		case NOT_EQUALS, GT, LT, GTE, LTE -> true;
		case ADD, SUB, MUL, DIV, MOD -> true;
		case SHL, SHR, SHR_AR, BW_AND, BW_OR, BW_XOR -> true;
		case ADD_ASSIGN, SUB_ASSIGN, MUL_ASSIGN, DIV_ASSIGN, MOD_ASSIGN -> true;
		case SHL_ASSIGN, SHR_ASSIGN, SHR_AR_ASSIGN, BW_AND_ASSIGN, BW_OR_ASSIGN, BW_XOR_ASSIGN -> true;
		case NEGATE, INC, DEC, POST_INC, POST_DEC -> true;
		default -> false;
		};
	}
	
	@Override
	public EnvisionObject handleOperatorOverloads
		(EnvisionInterpreter interpreter, String scopeName, Operator op, EnvisionObject obj)
			throws UnsupportedOverloadError
	{
		//unary operators
		if (op.isUnary()) {
			if (obj != null) throw new EnvisionError("The given operator object should be null! Unary Operator!");
			
			switch (op) {
			//inc/dec
			case NEGATE:			return EnvisionIntClass.newInt(-int_val);
			case INC:				int_val++; return this;
			case DEC:				int_val--; return this;
			case POST_INC:			return EnvisionIntClass.newInt(int_val++);
			case POST_DEC:			return EnvisionIntClass.newInt(int_val--);
			
			//throw error if this point is reached
			default: throw new UnsupportedOverloadError(this, op);
			}
		}
		
		//don't allow object to be null
		if (obj == null) throw new NullVariableError();
		
		//only allow numbers
		if (!obj.getPrimitiveType().isNumber())
			throw new InvalidDatatypeError(EnvisionDatatype.NUMBER_TYPE, obj.getDatatype());
		EnvisionNumber num = (EnvisionNumber) obj;
		
		switch (op) {
		//relational operators
		case NOT_EQUALS:		return EnvisionBooleanClass.newBoolean(int_val != num.intVal_i());
		case GT:				return EnvisionBooleanClass.newBoolean(int_val > num.intVal_i());
		case LT:				return EnvisionBooleanClass.newBoolean(int_val < num.intVal_i());
		case GTE:				return EnvisionBooleanClass.newBoolean(int_val >= num.intVal_i());
		case LTE:				return EnvisionBooleanClass.newBoolean(int_val <= num.intVal_i());
		//arithmetic operators
		case ADD:				return EnvisionIntClass.newInt(int_val + num.intVal_i());
		case SUB:				return EnvisionIntClass.newInt(int_val - num.intVal_i());
		case MUL:				return EnvisionIntClass.newInt(int_val * num.intVal_i());
		case DIV:				div0(int_val, num.intVal_i()); //check for div by zero errors
								return EnvisionIntClass.newInt(int_val / num.intVal_i());
		case MOD:				return EnvisionIntClass.newInt(int_val % num.intVal_i());
		//binary operators
		case SHL:				return EnvisionIntClass.newInt(int_val << num.intVal_i());
		case SHR:				return EnvisionIntClass.newInt(int_val >> num.intVal_i());
		case SHR_AR:			return EnvisionIntClass.newInt(int_val >>> num.intVal_i());
		case BW_AND:			return EnvisionIntClass.newInt(int_val & num.intVal_i());
		case BW_OR:				return EnvisionIntClass.newInt(int_val | num.intVal_i());
		case BW_XOR:			return EnvisionIntClass.newInt(int_val ^ num.intVal_i());
		//assignment operators
		case ADD_ASSIGN:		int_val += num.intVal_i(); return this;
		case SUB_ASSIGN:		int_val -= num.intVal_i(); return this;
		case MUL_ASSIGN:		int_val *= num.intVal_i(); return this;
		case DIV_ASSIGN:		int_val /= num.intVal_i(); return this;
		case MOD_ASSIGN:		int_val %= num.intVal_i(); return this;
		case SHL_ASSIGN:		int_val <<= num.intVal_i(); return this;
		case SHR_ASSIGN:		int_val >>= num.intVal_i(); return this;
		case SHR_AR_ASSIGN:		int_val >>>= num.intVal_i(); return this;
		case BW_AND_ASSIGN:		int_val &= num.intVal_i(); return this;
		case BW_OR_ASSIGN:		int_val |= num.intVal_i(); return this;
		case BW_XOR_ASSIGN:		int_val ^= num.intVal_i(); return this;
		
		//throw error if this point is reached
		default: throw new UnsupportedOverloadError(this, op, "[" + obj.getDatatype() + ":" + obj + "]");
		}
	}
	
	@Override
	public EnvisionObject handleObjectCasts(EnvisionDatatype castType) throws ClassCastError {
		//determine specific cast types
		if (EnvisionDatatype.BOOL_TYPE.compare(castType)) return EnvisionBooleanClass.newBoolean(int_val != 0);
		if (EnvisionDatatype.DOUBLE_TYPE.compare(castType)) return doubleVal();
		if (EnvisionDatatype.STRING_TYPE.compare(castType)) return EnvisionStringClass.newString(int_val);
		if (EnvisionDatatype.CHAR_TYPE.compare(castType)) return EnvisionCharClass.newChar(int_val);
		if (EnvisionDatatype.LIST_TYPE.compare(castType)) {
			EnvisionList list = EnvisionListClass.newList(EnvisionDatatype.CHAR_TYPE);
			String str = String.valueOf(int_val);
			for (int i = 0; i < str.length(); i++) list.add(new EnvisionChar(str.charAt(i)));
			return list;
		}
		
		return super.handleObjectCasts(castType);
	}
	
	@Override
	protected EnvisionObject handlePrimitive(FunctionPrototype proto, EnvisionObject[] args) {
		String funcName = proto.getFunctionName();
		if (!proto.hasOverload(args)) throw new NoOverloadError(funcName, args);
		
		return switch (funcName) {
		case "get" -> get();
		case "set" -> set(args[0]);
		case "min" -> min((EnvisionInt) args[0], (EnvisionInt) args[1]);
		case "max" -> max((EnvisionInt) args[0], (EnvisionInt) args[1]);
		default -> super.handlePrimitive(proto, args);
		};
	}
	
	//---------
	// Methods
	//---------
	
	public EnvisionInt min(EnvisionInt a, EnvisionInt b) {
		return (a.int_val <= b.int_val) ? a : b;
	}
	
	public EnvisionInt max(EnvisionInt a, EnvisionInt b) {
		return (a.int_val >= b.int_val) ? a : b;
	}
	
}