package envision.lang.datatypes;

import static envision.lang.util.Primitives.*;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.util.InternalFunction;
import envision.lang.util.Primitives;

/**
 * A script variable representing a number without a decimal place.
 * Encoded with Java Longs.
 */
public class EnvisionInt extends EnvisionNumber {

	//--------------
	// Constructors
	//--------------
	
	public EnvisionInt() { this(DEFAULT_NAME, 0l); }
	public EnvisionInt(long in) { this(DEFAULT_NAME, in); }
	public EnvisionInt(Number in) { this(DEFAULT_NAME, in.longValue()); }
	public EnvisionInt(String nameIn) { this(nameIn, 0l); }
	public EnvisionInt(String nameIn, Number in) { this(nameIn, in.longValue()); }
	public EnvisionInt(String nameIn, long in) {
		super(Primitives.INT.toDatatype(), nameIn);
		var_value = in;
	}
	
	public EnvisionInt(boolean val) { this(DEFAULT_NAME, val); }
	public EnvisionInt(String nameIn, boolean val) {
		super(Primitives.INT.toDatatype(), nameIn);
		var_value = (val) ? 1 : 0;
	}
	
	public EnvisionInt(EnvisionInt in) {
		super(Primitives.INT.toDatatype(), in.name);
		var_value = in.var_value;
	}
	
	public EnvisionInt(EnvisionNumber in) {
		super(Primitives.INT.toDatatype(), in.getName());
		var_value = (long) in.get();
	}
	
	//-----------
	// Overrides
	//-----------
	
	public long getLong() {
		return (long) var_value;
	}
	
	@Override
	public EnvisionInt copy() {
		 return new EnvisionInt(this);
	}
	
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
	
	public static EnvisionInt of(int val) { return new EnvisionInt(val); }
	public static EnvisionInt of(String val) { return new EnvisionInt(Long.parseLong(val)); }
	
}
