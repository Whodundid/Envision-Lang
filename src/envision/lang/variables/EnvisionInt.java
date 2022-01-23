package envision.lang.variables;

import static envision.lang.util.EnvisionDataType.*;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.util.EnvisionDataType;
import envision.lang.util.InternalMethod;

/** A script variable representing a number without a decimal place. Encoded with Java Longs. */
public class EnvisionInt extends EnvisionNumber {

	public EnvisionInt() { this(0); }
	public EnvisionInt(long valIn) { super(EnvisionDataType.INT, EnvisionDataType.INT.type, valIn); }
	public EnvisionInt(String nameIn) { this(nameIn, 0); }
	public EnvisionInt(String nameIn, Number valIn) { this(nameIn, valIn.longValue()); }
	public EnvisionInt(String nameIn, long valIn) {
		super(EnvisionDataType.INT, nameIn, valIn);
	}
	
	public EnvisionInt(EnvisionInt in) {
		this(in.name);
		value = in.value;
	}
	
	public EnvisionInt(Number in) { this(in.longValue()); }
	public EnvisionInt(EnvisionNumber in) { this((Number) in.get()); }
	
	@Override
	public EnvisionInt copy() {
		 return new EnvisionInt(this);
	}
	
	@Override
	protected void registerInternalMethods() {
		super.registerInternalMethods();
		im(new InternalMethod(INT, "valueOf", OBJECT) { protected void body(Object[] a) { ret(of(a[0])); }});
		im(new InternalMethod(INT, "get") { protected void body(Object[] a) { ret(get()); }});
		im(new InternalMethod(INT, "set", OBJECT) { protected void body(Object[] a) { ret(set((long) a[0])); }});
		im(new InternalMethod(INT, "lowest") { protected void body(Object[] a) { ret(Long.MIN_VALUE); }});
		im(new InternalMethod(INT, "highest") { protected void body(Object[] a) { ret(Long.MAX_VALUE); }});
	}
	
	@Override
	protected EnvisionObject runConstructor(EnvisionInterpreter interpreter, Object[] args) {
		if (args.length == 0) { return new EnvisionInt(); }
		if (args.length == 1) {
			Object obj = args[0];
			
			if (obj instanceof Character) { return new EnvisionInt((char) obj); }
			if (obj instanceof Number) { return new EnvisionInt((Number) obj); }
			if (obj instanceof EnvisionNumber) { return new EnvisionInt((EnvisionNumber) obj); }
		}
		return null;
	}
	
	public static EnvisionInt of(int val) { return new EnvisionInt(val); }
	public static EnvisionInt of(String val) { return new EnvisionInt(Long.parseLong(val)); }
	
}
