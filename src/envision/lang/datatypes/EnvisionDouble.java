package envision.lang.datatypes;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.FinalVarReassignmentError;
import envision.lang.EnvisionObject;

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
	public EnvisionObject get() {
		return this;
	}
	
	@Override
	public Object get_i() {
		return double_val;
	}
	
	@Override
	public EnvisionVariable set(EnvisionObject valIn) throws FinalVarReassignmentError {
		if (isFinal()) throw new FinalVarReassignmentError(this, valIn);
		if (valIn instanceof EnvisionDouble env_double) {
			this.double_val = env_double.double_val;
			return this;
		}
		throw new EnvisionError("Attempted to internally set non-double value to a double!");
	}
	
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
	
	@Override public long intVal_i() { return (long) double_val; }
	@Override public double doubleVal_i() { return double_val; }
	@Override public EnvisionInt intVal() { return EnvisionIntClass.newInt(double_val); }
	@Override public EnvisionDouble doubleVal() { return this; }
	
	@Override
	public EnvisionDouble copy() {
		return EnvisionDoubleClass.newDouble(double_val);
	}
	
	/*
	@Override
	protected void registerInternalMethods() {
		super.registerInternalMethods();
		im(new InternalFunction(BOOLEAN, "valueOf", VAR) { protected void body(Object[] a) { ret(EnvisionDouble.of(a[0])); }});
		im(new InternalFunction(BOOLEAN, "get") { protected void body(Object[] a) { ret(EnvisionDouble.this.get()); }});
		im(new InternalFunction(BOOLEAN, "valueOf", VAR) { protected void body(Object[] a) { ret(EnvisionDouble.this.set((double) a[0])); }});
		im(new InternalFunction(DOUBLE, "lowest") { protected void body(Object[] a) { ret(Double.MIN_VALUE); }});
		im(new InternalFunction(DOUBLE, "highest") { protected void body(Object[] a) { ret(Double.MAX_VALUE); }});
	}
	
	@Override
	protected EnvisionObject runConstructor(EnvisionInterpreter interpreter, Object[] args) {
		if (args.length == 0) { return new EnvisionDouble(); }
		if (args.length == 1) {
			Object obj = args[0];
			
			if (obj instanceof Number) { return new EnvisionDouble(((Number) obj).doubleValue()); }
			if (obj instanceof EnvisionNumber) { return new EnvisionDouble((EnvisionNumber) obj); }
		}
		return null;
	}
	*/
	
	public static EnvisionDouble of(double val) { return new EnvisionDouble(val); }
	public static EnvisionDouble of(String val) { return new EnvisionDouble(Double.parseDouble(val)); }
	
}
