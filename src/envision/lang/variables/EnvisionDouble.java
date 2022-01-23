package envision.lang.variables;

import static envision.lang.util.EnvisionDataType.*;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.util.EnvisionDataType;
import envision.lang.util.InternalMethod;

/** A script variable representing a number with a decimal point. */
public class EnvisionDouble extends EnvisionNumber {

	public EnvisionDouble() { this(0.0); }
	public EnvisionDouble(double valIn) { super(EnvisionDataType.DOUBLE, valIn); }
	public EnvisionDouble(String nameIn) { this(nameIn, 0.0); }
	public EnvisionDouble(String nameIn, Number in) { this(nameIn, in.doubleValue()); }
	public EnvisionDouble(String nameIn, double valIn) {
		super(EnvisionDataType.DOUBLE, nameIn, valIn);
	}
	
	public EnvisionDouble(EnvisionDouble in) {
		this(in.name);
		value = in.value;
	}
	
	public EnvisionDouble(Number in) { this(in.doubleValue()); }
	public EnvisionDouble(EnvisionNumber in) { this((Number) in.get()); }
	public EnvisionDouble(EnvisionInt in) { this((Number) in.get()); }
	
	@Override
	public EnvisionDouble copy() {
		return new EnvisionDouble(this);
	}
	
	@Override
	protected void registerInternalMethods() {
		super.registerInternalMethods();
		im(new InternalMethod(BOOLEAN, "valueOf", OBJECT) { protected void body(Object[] a) { ret(EnvisionDouble.of(a[0])); }});
		im(new InternalMethod(BOOLEAN, "get") { protected void body(Object[] a) { ret(EnvisionDouble.this.get()); }});
		im(new InternalMethod(BOOLEAN, "valueOf", OBJECT) { protected void body(Object[] a) { ret(EnvisionDouble.this.set((double) a[0])); }});
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
