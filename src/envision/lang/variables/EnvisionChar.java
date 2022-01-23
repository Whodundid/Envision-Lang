package envision.lang.variables;

import static envision.lang.util.EnvisionDataType.*;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.util.EnvisionDataType;
import envision.lang.util.InternalMethod;

public class EnvisionChar extends EnvisionVariable {
	
	public static final char empty = '\0';
	
	public EnvisionChar() { this(empty); }
	public EnvisionChar(char val) { super(EnvisionDataType.CHAR, val); }
	public EnvisionChar(String nameIn) { this(nameIn, empty); }
	public EnvisionChar(String nameIn, char val) {
		super(EnvisionDataType.CHAR, nameIn, val);
	}
	
	public EnvisionChar(EnvisionChar in) {
		this(in.name);
		value = in.value;
	}
	
	public EnvisionChar(EnvisionInt in) { this((char) (int) in.get()); }
	
	@Override
	public EnvisionChar copy() {
		return new EnvisionChar(this);
	}
	
	@Override
	protected void registerInternalMethods() {
		super.registerInternalMethods();
		//im(new InternalMethod(CHAR, "valueOf", OBJECT) { protected void body(Object[] a) { ret(EnvisionChar.of(a[0])); }});
		im(new InternalMethod(CHAR, "get") { protected void body(Object[] a) { ret(EnvisionChar.this.get()); }});
		im(new InternalMethod(CHAR, "set", OBJECT) { protected void body(Object[] a) { ret(EnvisionChar.this.set((char) a[0])); }});
	}
	
	@Override
	protected EnvisionObject runConstructor(EnvisionInterpreter interpreter, Object[] args) {
		if (args.length == 0) { return new EnvisionChar(); }
		if (args.length == 1) {
			Object obj = args[0];
			
			if (obj instanceof Character) { return new EnvisionChar((Character) obj); }
			if (obj instanceof EnvisionChar) { return new EnvisionChar((EnvisionChar) obj); }
			if (obj instanceof EnvisionInt) { return new EnvisionChar((EnvisionInt) obj); }
		}
		return null;
	}
	
	public static EnvisionChar of(char val) { return new EnvisionChar(val); } 
	public static EnvisionChar of(String val) { return new EnvisionChar((val != null && !val.isEmpty()) ? val.charAt(0) : '\u0000'); }
	
	
}
