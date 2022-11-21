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
import envision_lang.lang.natives.StaticTypes;
import envision_lang.tokenizer.Operator;

/**
 * A script variable representing a number with a decimal point.
 * Backed internally by Java:Double values.
 * 
 * @author Hunter Bragg
 */
public class EnvisionDouble extends EnvisionNumber {
	
	public static final EnvisionDouble MIN_VALUE = EnvisionDoubleClass.newDouble(Double.MIN_VALUE);
	public static final EnvisionDouble MAX_VALUE = EnvisionDoubleClass.newDouble(Double.MAX_VALUE);
	
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
	
	@Override
	public Object convertToJavaObject() {
		return double_val;
	}
	
	@Override public EnvisionDouble negate() { double_val = -double_val; return this; }
	
	@Override public long intVal_i() { return (long) double_val; }
	@Override public double doubleVal_i() { return double_val; }
	@Override public EnvisionInt intVal() { return EnvisionIntClass.newInt(double_val); }
	@Override public EnvisionDouble doubleVal() { return this; }
	
	@Override public EnvisionDouble get() { return this; }
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
		throw new EnvisionLangError("Attempted to internally set non-double value to a double!");
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
		throw new EnvisionLangError("Attempted to internally set non-double value to a double!");
	}
	
	@Override
	public boolean supportsOperator(Operator op) {
		return switch (op) {
		case EQUALS, NOT_EQUALS -> true;
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
		//don't allow null expression objects
		if (obj == null) throw new NullVariableError();
		
		//only allow numbers
		if (!obj.getPrimitiveType().isNumber())
			throw new InvalidDatatypeError(StaticTypes.NUMBER_TYPE, obj.getDatatype());
		
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
		//default: throw new UnsupportedOverloadError(this, op, "[" + obj.getDatatype() + ":" + obj + "]");
		default: return super.handleOperatorOverloads(interpreter, scopeName, op, obj);
		}
	}
	
	@Override
	public EnvisionObject handleObjectCasts(IDatatype castType) throws ClassCastError {
		//determine specific cast types
		if (StaticTypes.BOOL_TYPE.compare(castType)) return EnvisionBooleanClass.newBoolean(double_val != 0);
		if (StaticTypes.INT_TYPE.compare(castType)) return intVal();
		if (StaticTypes.STRING_TYPE.compare(castType)) return EnvisionStringClass.newString(double_val);
		if (StaticTypes.LIST_TYPE.compare(castType)) {
			EnvisionList list = EnvisionListClass.newList(StaticTypes.CHAR_TYPE);
			String str = String.valueOf(double_val);
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
		case "min" -> min((EnvisionDouble) args[0], (EnvisionDouble) args[1]);
		case "max" -> max((EnvisionDouble) args[0], (EnvisionDouble) args[1]);
		default -> super.handlePrimitive(proto, args);
		};
	}
	
	//---------
	// Methods
	//---------
	
	public EnvisionDouble min(EnvisionDouble a, EnvisionDouble b) {
		return (a.double_val <= b.double_val) ? a : b;
	}
	
	public EnvisionDouble max(EnvisionDouble a, EnvisionDouble b) {
		return (a.double_val >= b.double_val) ? a : b;
	}
	
}