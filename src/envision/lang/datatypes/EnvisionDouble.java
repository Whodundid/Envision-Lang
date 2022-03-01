package envision.lang.datatypes;

import static envision.lang.util.Primitives.*;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.util.InternalMethod;
import envision.lang.util.Primitives;

/**
 * A script variable representing a number with a decimal point.
 * Backed by Java double values.
 */
public class EnvisionDouble extends EnvisionNumber {

	//--------------
	// Constructors
	//--------------
	
	public EnvisionDouble() { this(DEFAULT_NAME, 0.0); }
	public EnvisionDouble(double in) { this(DEFAULT_NAME, in); }
	public EnvisionDouble(Number in) { this(DEFAULT_NAME, in.doubleValue()); }
	public EnvisionDouble(String nameIn) { this(nameIn, 0.0); }
	public EnvisionDouble(String nameIn, Number in) { this(nameIn, in.doubleValue()); }
	public EnvisionDouble(String nameIn, double in) {
		super(Primitives.DOUBLE.toDatatype(), nameIn);
		var_value = in;
	}
	
	public EnvisionDouble(boolean val) { this(DEFAULT_NAME, val); }
	public EnvisionDouble(String nameIn, boolean val) {
		super(Primitives.DOUBLE.toDatatype(), nameIn);
		var_value = (val) ? 1.0 : 0.0;
	}
	
	public EnvisionDouble(EnvisionDouble in) {
		super(Primitives.DOUBLE.toDatatype(), in.name);
		var_value = in.var_value;
	}
	
	public EnvisionDouble(EnvisionNumber in) {
		super(Primitives.DOUBLE.toDatatype(), in.getName());
		var_value = in.var_value;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public EnvisionDouble copy() {
		return new EnvisionDouble(name, (double) var_value);
	}
	
	@Override
	protected void registerInternalMethods() {
		super.registerInternalMethods();
		im(new InternalMethod(BOOLEAN, "valueOf", VAR) { protected void body(Object[] a) { ret(EnvisionDouble.of(a[0])); }});
		im(new InternalMethod(BOOLEAN, "get") { protected void body(Object[] a) { ret(EnvisionDouble.this.get()); }});
		im(new InternalMethod(BOOLEAN, "valueOf", VAR) { protected void body(Object[] a) { ret(EnvisionDouble.this.set((double) a[0])); }});
		im(new InternalMethod(DOUBLE, "lowest") { protected void body(Object[] a) { ret(Double.MIN_VALUE); }});
		im(new InternalMethod(DOUBLE, "highest") { protected void body(Object[] a) { ret(Double.MAX_VALUE); }});
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
	
	public static EnvisionDouble of(double val) { return new EnvisionDouble(val); }
	public static EnvisionDouble of(String val) { return new EnvisionDouble(Double.parseDouble(val)); }
	
}
