package envision_lang.lang.datatypes;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.scope.ScopeEntry;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.exceptions.errors.InvalidDatatypeError;
import envision_lang.lang.exceptions.errors.NoOverloadError;
import envision_lang.lang.exceptions.errors.NullVariableError;
import envision_lang.lang.exceptions.errors.objects.ClassCastError;
import envision_lang.lang.exceptions.errors.objects.UnsupportedOverloadError;
import envision_lang.lang.functions.FunctionPrototype;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.EnvisionStaticTypes;
import envision_lang.tokenizer.Operator;

/**
 * A script variable representing a number without a decimal place.
 * Encoded with Java Longs.
 * 
 * @author Hunter Bragg
 */
public final class EnvisionInt extends EnvisionNumber<Long> {
	
	public static final IDatatype INT_TYPE = EnvisionStaticTypes.INT_TYPE;

	public static final EnvisionInt MIN_VALUE = EnvisionIntClass.newInt(Long.MIN_VALUE);
	public static final EnvisionInt MAX_VALUE = EnvisionIntClass.newInt(Long.MAX_VALUE);
	public static final EnvisionInt ZERO = EnvisionIntClass.valueOf(0L);
	
	//========
	// Fields
	//========
	
	public final long int_val;
	
	//--------------
	// Constructors
	//--------------
	
	EnvisionInt() { this(0L); }
	EnvisionInt(Number in) { this(in.longValue()); }
	EnvisionInt(long in) {
		super(EnvisionIntClass.INT_CLASS);
		int_val = in;
	}
	
	EnvisionInt(boolean val) {
		super(EnvisionIntClass.INT_CLASS);
		int_val = (val) ? 1L : 0L;
	}
	
	EnvisionInt(EnvisionInt in) {
		super(EnvisionIntClass.INT_CLASS);
		int_val = in.int_val;
	}
	
	EnvisionInt(EnvisionNumber in) {
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
	
	/**
	 * Return this exact same integer.
	 */
	@Override
	public EnvisionInt copy() {
		return this;
	}
	
	@Override
	public String toString() {
		return String.valueOf(int_val);
	}
	
	@Override public EnvisionInt negate() { return EnvisionIntClass.valueOf(-int_val); }
	
	@Override public Long convertToJavaObject() { return int_val; }
	@Override public long intVal_i() { return int_val; }
	@Override public double doubleVal_i() { return (double) int_val; }
	@Override public EnvisionInt intVal() { return this; }
	@Override public EnvisionDouble doubleVal() { return EnvisionDoubleClass.valueOf(int_val); }
	
	@Override public EnvisionInt get() { return this; }
	@Override public Long get_i() { return int_val; }
	
	@Override
	public boolean supportsOperator(Operator op) {
		return switch (op) {
		//unary
		case NEGATE, INC, DEC, POST_INC, POST_DEC -> true;
		//relational
		case EQUALS, NOT_EQUALS, GT, LT, GTE, LTE -> true;
		//arithmetic
		case ADD, SUB, MUL, DIV, MOD -> true;
		//binary
		case SHL, SHR, SHR_AR, BW_AND, BW_OR, BW_XOR -> true;
		//assignment
		case ADD_ASSIGN, SUB_ASSIGN, MUL_ASSIGN, DIV_ASSIGN, MOD_ASSIGN -> true;
		case SHL_ASSIGN, SHR_ASSIGN, SHR_AR_ASSIGN, BW_AND_ASSIGN, BW_OR_ASSIGN, BW_XOR_ASSIGN -> true;
		//don't accept any other operator types
		default -> false;
		};
	}
	
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
				throw new InvalidDatatypeError(EnvisionStaticTypes.NUMBER_TYPE, obj.getDatatype());
			}
		}
		
		// extract this variable's direct scope entry so that assignment operations can effectively update the value
		ScopeEntry scopeEntry = interpreter.scope().getTyped(scopeName);
		
		// convert the incoming binary operation target object into an EnvisionNumber
		EnvisionNumber numIn = (EnvisionNumber) obj;
		
		switch (op) {
		case NEGATE:			return negate();
		//relational operators
		case NOT_EQUALS:		return EnvisionBooleanClass.valueOf(int_val != numIn.intVal_i());
		case GT:				return EnvisionBooleanClass.valueOf(int_val > numIn.intVal_i());
		case LT:				return EnvisionBooleanClass.valueOf(int_val < numIn.intVal_i());
		case GTE:				return EnvisionBooleanClass.valueOf(int_val >= numIn.intVal_i());
		case LTE:				return EnvisionBooleanClass.valueOf(int_val <= numIn.intVal_i());
		//arithmetic operators
		case ADD:				return EnvisionIntClass.valueOf(int_val + numIn.intVal_i());
		case SUB:				return EnvisionIntClass.valueOf(int_val - numIn.intVal_i());
		case MUL:				return EnvisionIntClass.valueOf(int_val * numIn.intVal_i());
		case DIV:				div0_l(int_val, numIn.intVal_i()); //check for div by zero errors
								return EnvisionIntClass.valueOf(int_val / numIn.intVal_i());
		case MOD:				return EnvisionIntClass.valueOf(int_val % numIn.intVal_i());
		//binary operators
		case SHL:				return EnvisionIntClass.valueOf(int_val << numIn.intVal_i());
		case SHR:				return EnvisionIntClass.valueOf(int_val >> numIn.intVal_i());
		case SHR_AR:			return EnvisionIntClass.valueOf(int_val >>> numIn.intVal_i());
		case BW_AND:			return EnvisionIntClass.valueOf(int_val & numIn.intVal_i());
		case BW_OR:				return EnvisionIntClass.valueOf(int_val | numIn.intVal_i());
		case BW_XOR:			return EnvisionIntClass.valueOf(int_val ^ numIn.intVal_i());
		//assignment operators
		case ADD_ASSIGN:		return scopeEntry.setR(EnvisionIntClass.valueOf(int_val + numIn.intVal_i()));
		case SUB_ASSIGN:		return scopeEntry.setR(EnvisionIntClass.valueOf(int_val - numIn.intVal_i()));
		case MUL_ASSIGN:		return scopeEntry.setR(EnvisionIntClass.valueOf(int_val * numIn.intVal_i()));
		case DIV_ASSIGN:		return scopeEntry.setR(EnvisionIntClass.valueOf(int_val / numIn.intVal_i()));
		case MOD_ASSIGN:		return scopeEntry.setR(EnvisionIntClass.valueOf(int_val % numIn.intVal_i()));
		case SHL_ASSIGN:		return scopeEntry.setR(EnvisionIntClass.valueOf(int_val << numIn.intVal_i()));
		case SHR_ASSIGN:		return scopeEntry.setR(EnvisionIntClass.valueOf(int_val >> numIn.intVal_i()));
		case SHR_AR_ASSIGN:		return scopeEntry.setR(EnvisionIntClass.valueOf(int_val >>> numIn.intVal_i()));
		case BW_AND_ASSIGN:		return scopeEntry.setR(EnvisionIntClass.valueOf(int_val & numIn.intVal_i()));
		case BW_OR_ASSIGN:		return scopeEntry.setR(EnvisionIntClass.valueOf(int_val | numIn.intVal_i()));
		case BW_XOR_ASSIGN:		return scopeEntry.setR(EnvisionIntClass.valueOf(int_val ^ numIn.intVal_i()));
		//inc/dec
		case INC:				return scopeEntry.setR(EnvisionIntClass.valueOf(int_val + 1L));
		case DEC:				return scopeEntry.setR(EnvisionIntClass.valueOf(int_val - 1L));
		//post inc/dec
		case POST_INC: 			scopeEntry.set(EnvisionIntClass.valueOf(int_val + 1L)); return this;
		case POST_DEC: 			scopeEntry.set(EnvisionIntClass.valueOf(int_val - 1L)); return this;
		
		default: return super.handleOperatorOverloads(interpreter, scopeName, op, obj);
		}
	}
	
	@Override
	public EnvisionObject handleObjectCasts(IDatatype castType) throws ClassCastError {
		//determine specific cast types
		if (EnvisionStaticTypes.DOUBLE_TYPE.compare(castType)) return doubleVal();
		if (EnvisionStaticTypes.BOOL_TYPE.compare(castType)) return EnvisionBooleanClass.valueOf(int_val != 0);
		if (EnvisionStaticTypes.STRING_TYPE.compare(castType)) return EnvisionStringClass.valueOf(int_val);
		
		if (EnvisionStaticTypes.LIST_TYPE.compare(castType)) {
			EnvisionList list = EnvisionListClass.newList(EnvisionStaticTypes.CHAR_TYPE);
			String str = String.valueOf(int_val);
			
			for (int i = 0; i < str.length(); i++) {
				list.add(EnvisionCharClass.newChar(str.charAt(i)));
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
		// NO INT FUNCTIONS
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