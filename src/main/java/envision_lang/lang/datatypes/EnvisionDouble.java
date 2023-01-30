package envision_lang.lang.datatypes;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.scope.ScopeEntry;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.exceptions.errors.InvalidDatatypeError;
import envision_lang.lang.exceptions.errors.NoOverloadError;
import envision_lang.lang.exceptions.errors.NullVariableError;
import envision_lang.lang.exceptions.errors.objects.ClassCastError;
import envision_lang.lang.exceptions.errors.objects.UnsupportedOverloadError;
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
public final class EnvisionDouble extends EnvisionNumber<Double> {
	
	public static final EnvisionDouble MIN_VALUE = EnvisionDoubleClass.newDouble(Double.MIN_VALUE);
	public static final EnvisionDouble MIN_NORMAL = EnvisionDoubleClass.newDouble(Double.MIN_NORMAL);
	public static final EnvisionDouble MIN_EXPONENT = EnvisionDoubleClass.newDouble(Double.MIN_EXPONENT);
	public static final EnvisionDouble MAX_VALUE = EnvisionDoubleClass.newDouble(Double.MAX_VALUE);
	public static final EnvisionDouble MAX_EXPONENT = EnvisionDoubleClass.newDouble(Double.MAX_EXPONENT);
	public static final EnvisionDouble NaN = EnvisionDoubleClass.newDouble(Double.NaN);
	public static final EnvisionDouble POSITIVE_INFINITY = EnvisionDoubleClass.newDouble(Double.POSITIVE_INFINITY);
	public static final EnvisionDouble NEGATIVE_INFINITY = EnvisionDoubleClass.newDouble(Double.NEGATIVE_INFINITY);
	
	public static final EnvisionDouble ZERO = EnvisionDoubleClass.newDouble(0.0D);
	public static final EnvisionDouble ONE = EnvisionDoubleClass.newDouble(1.0D);
	
	//========
	// Fields
	//========
	
	public final double double_val;
	
	//--------------
	// Constructors
	//--------------
	
	EnvisionDouble() { this(0.0); }
	EnvisionDouble(Number in) { this(in.doubleValue()); }
	EnvisionDouble(double in) {
		super(EnvisionDoubleClass.DOUBLE_CLASS);
		double_val = in;
	}
	
	EnvisionDouble(boolean val) {
		super(EnvisionDoubleClass.DOUBLE_CLASS);
		double_val = (val) ? 1.0 : 0.0;
	}
	
	EnvisionDouble(EnvisionDouble in) {
		super(EnvisionDoubleClass.DOUBLE_CLASS);
		double_val = in.double_val;
	}
	
	EnvisionDouble(EnvisionNumber in) {
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
	
	/**
	 * Returns this exact same double.
	 */
	@Override
	public EnvisionDouble copy() {
		return this;
	}
	
	@Override
	public String toString() {
		return String.valueOf(double_val);
	}
	
	@Override public EnvisionDouble negate() { return EnvisionDoubleClass.valueOf(-double_val); }
	
	@Override public Double convertToJavaObject() { return double_val; }
	@Override public long intVal_i() { return (long) double_val; }
	@Override public double doubleVal_i() { return double_val; }
	@Override public EnvisionInt intVal() { return EnvisionIntClass.valueOf((long) double_val); }
	@Override public EnvisionDouble doubleVal() { return this; }
	
	@Override public EnvisionDouble get() { return this; }
	@Override public Double get_i() { return double_val; }
	
	@Override
	public boolean supportsOperator(Operator op) {
		return switch (op) {
		case EQUALS, NOT_EQUALS -> true;
		case GT, LT, GTE, LTE -> true;
		case ADD, SUB, MUL, DIV, MOD -> true;
		case ADD_ASSIGN, SUB_ASSIGN, MUL_ASSIGN, DIV_ASSIGN, MOD_ASSIGN -> true;
		case NEGATE, INC, DEC, POST_INC, POST_DEC -> true;
		//don't accept any other operator types
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
		// if the operator isn't a unary operation, don't allow object to be null
		if (!op.isUnary()) {
			if (obj == null) throw new NullVariableError();
			
			//only allow numbers
			if (!obj.getPrimitiveType().isNumber()) {
				throw new InvalidDatatypeError(StaticTypes.NUMBER_TYPE, obj.getDatatype());
			}
		}
		
		// extract this variable's direct scope entry so that assignment operations can effectively update the value
		ScopeEntry scopeEntry = interpreter.scope().getTyped(scopeName);
		
		// convert the incoming binary operation target object into an EnvisionNumber
		EnvisionNumber numIn = (EnvisionNumber) obj;
		
		switch (op) {
		case NEGATE:			return negate();
		//relational operators
		case GT:				return EnvisionBooleanClass.valueOf(double_val > numIn.doubleVal_i());
		case LT:				return EnvisionBooleanClass.valueOf(double_val < numIn.doubleVal_i());
		case GTE:				return EnvisionBooleanClass.valueOf(double_val >= numIn.doubleVal_i());
		case LTE:				return EnvisionBooleanClass.valueOf(double_val <= numIn.doubleVal_i());
		//binary operators
		case ADD:				return EnvisionDoubleClass.valueOf(double_val + numIn.doubleVal_i());
		case SUB:				return EnvisionDoubleClass.valueOf(double_val - numIn.doubleVal_i());
		case MUL:				return EnvisionDoubleClass.valueOf(double_val * numIn.doubleVal_i());
		case DIV:				div0_d(double_val, numIn.doubleVal_i()); //check for div by zero errors
								return EnvisionDoubleClass.valueOf(double_val / numIn.doubleVal_i());
		case MOD:				return EnvisionDoubleClass.valueOf(double_val % numIn.doubleVal_i());
		//assignment operators
		case ADD_ASSIGN:		return scopeEntry.setR(EnvisionDoubleClass.valueOf(double_val + numIn.doubleVal_i()));
		case SUB_ASSIGN:		return scopeEntry.setR(EnvisionDoubleClass.valueOf(double_val - numIn.doubleVal_i()));
		case MUL_ASSIGN:		return scopeEntry.setR(EnvisionDoubleClass.valueOf(double_val * numIn.doubleVal_i()));
		case DIV_ASSIGN:		return scopeEntry.setR(EnvisionDoubleClass.valueOf(double_val / numIn.doubleVal_i()));
		case MOD_ASSIGN:		return scopeEntry.setR(EnvisionDoubleClass.valueOf(double_val % numIn.doubleVal_i()));
		//inc/dec
		case INC:				return scopeEntry.setR(EnvisionDoubleClass.valueOf(double_val + 1.0D));
		case DEC:				return scopeEntry.setR(EnvisionDoubleClass.valueOf(double_val - 1.0D));
		//post inc/dec
		case POST_INC:			scopeEntry.set(EnvisionDoubleClass.valueOf(double_val + 1.0D)); return this;
		case POST_DEC:			scopeEntry.set(EnvisionDoubleClass.valueOf(double_val - 1.0D)); return this;
		
		default: return super.handleOperatorOverloads(interpreter, scopeName, op, obj);
		}
	}
	
	@Override
	public EnvisionObject handleObjectCasts(IDatatype castType) throws ClassCastError {
		//determine specific cast types
		if (StaticTypes.INT_TYPE.compare(castType)) return intVal();
		if (StaticTypes.BOOL_TYPE.compare(castType)) return EnvisionBooleanClass.valueOf(double_val != 0);
		if (StaticTypes.STRING_TYPE.compare(castType)) return EnvisionStringClass.valueOf(double_val);
		
		if (StaticTypes.LIST_TYPE.compare(castType)) {
			EnvisionList list = EnvisionListClass.newList(StaticTypes.CHAR_TYPE);
			String str = String.valueOf(double_val);
			
			for (int i = 0; i < str.length(); i++) {
				list.add(EnvisionCharClass.valueOf(str.charAt(i)));
			}
			
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