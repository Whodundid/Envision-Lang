package envision.lang.datatypes;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.FinalVarReassignmentError;
import envision.exceptions.errors.InvalidDatatypeError;
import envision.exceptions.errors.NullVariableError;
import envision.exceptions.errors.objects.UnsupportedOverloadError;
import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.util.EnvisionDatatype;
import envision.tokenizer.Operator;

/**
 * A variable representing a number with a decimal point.
 * Backed internally by Java:Double values.
 */
public class EnvisionDouble extends EnvisionNumber {
	
	public double double_val;
	
	//--------------
	// Constructors
	//--------------
	
	protected EnvisionDouble() { this(0.0); }
	protected EnvisionDouble(Number in) { this(in.doubleValue()); }
	protected EnvisionDouble(double in) {
		super(EnvisionDoubleClass.DOUBLE_CLASS);
		double_val = in;
	}
	
	protected EnvisionDouble(boolean val) {
		super(EnvisionDoubleClass.DOUBLE_CLASS);
		double_val = (val) ? 1.0 : 0.0;
	}
	
	protected EnvisionDouble(EnvisionDouble in) {
		super(EnvisionDoubleClass.DOUBLE_CLASS);
		double_val = in.double_val;
	}
	
	protected EnvisionDouble(EnvisionNumber in) {
		super(EnvisionDoubleClass.DOUBLE_CLASS);
		double_val = in.doubleVal().double_val;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof EnvisionDouble env_double && env_double.double_val == double_val);
	}
	
	@Override public String toString() { return String.valueOf(double_val); }
	@Override public EnvisionDouble copy() { return EnvisionDoubleClass.newDouble(double_val); }
	
	@Override public EnvisionDouble negate() { double_val = -double_val; return this; }
	
	@Override public long intVal_i() { return (long) double_val; }
	@Override public double doubleVal_i() { return double_val; }
	@Override public EnvisionInt intVal() { return EnvisionIntClass.newInt(double_val); }
	@Override public EnvisionDouble doubleVal() { return this; }
	
	@Override public EnvisionObject get() { return this; }
	@Override public Object get_i() { return double_val; }
	
	/**
	 * Internally assigns this double_val from an existing EnvisionDouble's double_val.
	 */
	@Override
	public EnvisionVariable set(EnvisionObject valIn) throws FinalVarReassignmentError {
		if (isFinal()) throw new FinalVarReassignmentError(this, valIn);
		if (valIn instanceof EnvisionDouble env_double) {
			this.double_val = env_double.double_val;
			return this;
		}
		throw new EnvisionError("Attempted to internally set non-double value to a double!");
	}
	
	/**
	 * Internally assigns this double_val to either a float or a double.
	 */
	@Override
	public EnvisionVariable set_i(Object valIn) throws FinalVarReassignmentError {
		if (isFinal()) throw new FinalVarReassignmentError(this, valIn);
		if (valIn instanceof Double double_val) {
			this.double_val = double_val;
			return this;
		}
		//have to account for float in this case
		else if (valIn instanceof Float float_val) {
			this.double_val = float_val;
			return this;
		}
		throw new EnvisionError("Attempted to internally set non-double value to a double!");
	}
	
	@Override
	public boolean supportsOperator(Operator op) {
		return switch (op) {
		case GT, LT, GTE, LTE -> true;
		case ADD, SUB, MUL, DIV, MOD -> true;
		case ADD_ASSIGN, SUB_ASSIGN, MUL_ASSIGN, DIV_ASSIGN, MOD_ASSIGN -> true;
		case NEGATE, INC, DEC, POST_INC, POST_DEC -> true;
		default -> false;
		};
	}
	
	/**
	 * EnvisionDouble specific operator overloads.
	 */
	@Override
	public EnvisionObject handleOperatorOverloads
		(EnvisionInterpreter interpreter, String scopeName, Operator op, EnvisionObject obj)
			throws UnsupportedOverloadError
	{
		//dont allow null expression objects
		if (obj == null) throw new NullVariableError();
		
		//only allow numbers
		if (!obj.getPrimitiveType().isNumber())
			throw new InvalidDatatypeError(EnvisionDatatype.NUMBER_TYPE, obj.getDatatype());
		EnvisionNumber num = (EnvisionNumber) obj;
		
		switch (op) {
		//relational operators
		case GT:				return EnvisionBooleanClass.newBoolean(double_val > num.doubleVal_i());
		case LT:				return EnvisionBooleanClass.newBoolean(double_val < num.doubleVal_i());
		case GTE:				return EnvisionBooleanClass.newBoolean(double_val >= num.doubleVal_i());
		case LTE:				return EnvisionBooleanClass.newBoolean(double_val <= num.doubleVal_i());
		//binary operators
		case ADD:				return EnvisionDoubleClass.newDouble(double_val + num.doubleVal_i());
		case SUB:				return EnvisionDoubleClass.newDouble(double_val - num.doubleVal_i());
		case MUL:				return EnvisionDoubleClass.newDouble(double_val * num.doubleVal_i());
		case DIV:				div0(double_val, num.doubleVal_i()); //check for div by zero errors
								return EnvisionDoubleClass.newDouble(double_val / num.doubleVal_i());
		case MOD:				return EnvisionDoubleClass.newDouble(double_val % num.doubleVal_i());
		//assignment operators
		case ADD_ASSIGN:		double_val += num.doubleVal_i(); return this;
		case SUB_ASSIGN:		double_val -= num.doubleVal_i(); return this;
		case MUL_ASSIGN:		double_val *= num.doubleVal_i(); return this;
		case DIV_ASSIGN:		double_val /= num.doubleVal_i(); return this;
		case MOD_ASSIGN:		double_val %= num.doubleVal_i(); return this;
		//inc/dec
		case NEGATE:			return EnvisionDoubleClass.newDouble(-double_val);
		case INC:				double_val++; return this;
		case DEC:				double_val--; return this;
		case POST_INC:			return EnvisionDoubleClass.newDouble(double_val++);
		case POST_DEC:			return EnvisionDoubleClass.newDouble(double_val--);
		
		//throw error if this point is reached
		default: throw new UnsupportedOverloadError(this, op, "[" + obj.getDatatype() + ":" + obj + "]");
		}
	}
	
}