package envision.lang.variables;

import static envision.lang.util.EnvisionDataType.*;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.util.EnvisionDataType;
import envision.lang.util.InternalMethod;

public class EnvisionBoolean extends EnvisionVariable {

	public static final EnvisionBoolean TRUE = new EnvisionBoolean(true);
	public static final EnvisionBoolean FALSE = new EnvisionBoolean(false);
	
	public EnvisionBoolean() { this(false); }
	public EnvisionBoolean(boolean val) { super(EnvisionDataType.BOOLEAN, val); }
	public EnvisionBoolean(String nameIn) { this(nameIn, false); }
	public EnvisionBoolean(String nameIn, boolean val) {
		super(EnvisionDataType.BOOLEAN, nameIn, val);
	}
	
	public EnvisionBoolean(EnvisionBoolean in) {
		this(in.name);
		value = in.value;
	}
	
	public EnvisionBoolean(EnvisionInt in) { this(((int) in.get() == 1) ? true : false); }
	
	@Override
	public EnvisionBoolean copy() {
		return new EnvisionBoolean(this);
	}
	
	@Override
	protected void registerInternalMethods() {
		super.registerInternalMethods();
		//im(new InternalMethod(BOOLEAN, "valueOf", OBJECT) { protected void body(Object[] a) { ret(of(a[0])); }});
		im(new InternalMethod(BOOLEAN, "get") { protected void body(Object[] a) { ret(get()); }});
		im(new InternalMethod(BOOLEAN, "set", OBJECT) { protected void body(Object[] a) { ret(set((boolean) a[0])); }});
	}
	
	@Override
	protected EnvisionObject runConstructor(EnvisionInterpreter interpreter, Object[] args) {
		if (args.length == 0) { return new EnvisionBoolean(); }
		else if (args.length == 1) {
			Object obj = args[0];
			if (obj instanceof Boolean) { return new EnvisionBoolean((Boolean) obj); }
			if (obj instanceof EnvisionBoolean) { return new EnvisionBoolean((EnvisionBoolean) obj); }
			if (obj instanceof Integer) { return new EnvisionBoolean(((Integer) obj).intValue() == 1); }
			if (obj instanceof EnvisionInt) { return new EnvisionBoolean((EnvisionInt) obj); }
		}
		return null;
	}
	
	//@Override public EnvisionBoolean base() { return new EnvisionBoolean(); }
	
	public boolean isTrue() { return (boolean) get(); }
	public boolean isFalse() { return !(boolean) get(); }
	
	public static EnvisionBoolean of(boolean val) { return new EnvisionBoolean(val); }
	public static EnvisionBoolean of(String val) { return new EnvisionBoolean(Boolean.parseBoolean(val)); }
	public static EnvisionBoolean of(EnvisionVariable var) {
		/*
		switch (var.getDataType()) {
		case BOOLEAN: return new EnvisionBoolean((boolean) var.get());
		case INT: return new EnvisionBoolean((int) var.get() == 1);
		case DOUBLE: return new EnvisionBoolean((double) var.get() == 1.0);
		case STRING: return new EnvisionBoolean(Boolean.valueOf((String) var.get()));
		default: return FALSE;
		}
		*/
		return new EnvisionBoolean();
	}
	
}
