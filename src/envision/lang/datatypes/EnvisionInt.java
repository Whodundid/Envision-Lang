package envision.lang.datatypes;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.FinalVarReassignmentError;
import envision.lang.EnvisionObject;

/**
 * A script variable representing a number without a decimal place.
 * Encoded with Java Longs.
 */
public class EnvisionInt extends EnvisionNumber {
	
	public long long_val;
	
	//--------------
	// Constructors
	//--------------
	
	protected EnvisionInt() { this(0l); }
	protected EnvisionInt(Number in) { this(in.longValue()); }
	protected EnvisionInt(long in) {
		super(EnvisionIntClass.INT_CLASS);
		long_val = in;
	}
	
	protected EnvisionInt(boolean val) {
		super(EnvisionIntClass.INT_CLASS);
		long_val = (val) ? 1 : 0;
	}
	
	protected EnvisionInt(EnvisionInt in) {
		super(EnvisionIntClass.INT_CLASS);
		long_val = in.long_val;
	}
	
	protected EnvisionInt(EnvisionNumber in) {
		super(EnvisionIntClass.INT_CLASS);
		long_val = in.intVal().long_val;
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
		return long_val;
	}
	
	@Override
	public EnvisionVariable set(EnvisionObject valIn) throws FinalVarReassignmentError {
		if (isFinal()) throw new FinalVarReassignmentError(this, valIn);
		if (valIn instanceof EnvisionInt env_int) {
			this.long_val = env_int.long_val;
			return this;
		}
		throw new EnvisionError("Attempted to internally set non-long value to a long!");
	}
	
	@Override
	public EnvisionVariable set_i(Object valIn) throws FinalVarReassignmentError {
		if (isFinal()) throw new FinalVarReassignmentError(this, valIn);
		if (valIn instanceof Long long_val) {
			this.long_val = long_val;
			return this;
		}
		//have to account for integers in this case
		else if (valIn instanceof Integer int_val) {
			this.long_val = int_val;
			return this;
		}
		throw new EnvisionError("Attempted to internally set non-long value to a long!");
	}
	
	@Override public long intVal_i() { return long_val; }
	@Override public double doubleVal_i() { return (double) long_val; }
	@Override public EnvisionInt intVal() { return this; }
	@Override public EnvisionDouble doubleVal() { return EnvisionDoubleClass.newDouble(long_val); }
	
	@Override
	public EnvisionInt copy() {
		 return EnvisionIntClass.newInt(long_val);
	}
	
	/*
	@Override
	protected void registerInternalMethods() {
		super.registerInternalMethods();
		im(new InternalFunction(INT, "valueOf", VAR) { protected void body(Object[] a) { ret(of(a[0])); }});
		im(new InternalFunction(INT, "get") { protected void body(Object[] a) { ret(get()); }});
		im(new InternalFunction(INT, "set", VAR) { protected void body(Object[] a) { ret(set((long) a[0])); }});
		im(new InternalFunction(INT, "lowest") { protected void body(Object[] a) { ret(Long.MIN_VALUE); }});
		im(new InternalFunction(INT, "highest") { protected void body(Object[] a) { ret(Long.MAX_VALUE); }});
	}
	
	@Override
	protected EnvisionObject runConstructor(EnvisionInterpreter interpreter, Object[] args) {
		if (args.length == 0) return new EnvisionInt();
		if (args.length == 1) {
			Object obj = args[0];
			
			if (obj instanceof Character c) return new EnvisionInt(DEFAULT_NAME, c);
			if (obj instanceof Number n) return new EnvisionInt(DEFAULT_NAME, n);
			if (obj instanceof EnvisionNumber n) return new EnvisionInt(DEFAULT_NAME, (long) n.get());
		}
		return null;
	}
	*/
	
	public static EnvisionInt of(int val) { return new EnvisionInt(val); }
	public static EnvisionInt of(String val) { return new EnvisionInt(Long.parseLong(val)); }
	
}
