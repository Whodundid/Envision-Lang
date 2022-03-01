package envision.lang.datatypes;

import static envision.lang.util.Primitives.*;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.util.InternalMethod;
import envision.lang.util.Primitives;

/**
 * 
 * @author Hunter Bragg
 */
public class EnvisionChar extends EnvisionVariable {
	
	public static final char NULL_CHAR = '\0';
	
	//--------------
	// Constructors
	//--------------
	
	public EnvisionChar() { this(NULL_CHAR); }
	public EnvisionChar(char val) { this(DEFAULT_NAME, val); }
	public EnvisionChar(String nameIn) { this(nameIn, NULL_CHAR); }
	public EnvisionChar(String nameIn, char val) {
		super(Primitives.CHAR.toDatatype(), nameIn);
		var_value = val;
	}
	
	public EnvisionChar(boolean val) { this(DEFAULT_NAME, val); }
	public EnvisionChar(String nameIn, boolean val) {
		super(Primitives.CHAR.toDatatype(), nameIn);
		var_value = (val) ? 'T' : 'F';
	}
	
	public EnvisionChar(EnvisionChar in) {
		super(Primitives.CHAR.toDatatype(), in.name);
		var_value = in.var_value;
	}
	
	public EnvisionChar(Object objIn) { this(DEFAULT_NAME, objIn); }
	public EnvisionChar(String nameIn, Object objIn) {
		super(Primitives.CHAR.toDatatype(), nameIn);
		String sVal = String.valueOf(objIn);
		var_value = (sVal != null && !sVal.isEmpty()) ? sVal.charAt(0) : NULL_CHAR;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public EnvisionChar copy() {
		return new EnvisionChar(this);
	}
	
	@Override
	protected void registerInternalMethods() {
		super.registerInternalMethods();
		//im(new InternalMethod(CHAR, "valueOf", OBJECT) { protected void body(Object[] a) { ret(EnvisionChar.of(a[0])); }});
		im(new InternalMethod(CHAR, "get") { protected void body(Object[] a) { ret(EnvisionChar.this.get()); }});
		im(new InternalMethod(CHAR, "set", VAR) { protected void body(Object[] a) { ret(EnvisionChar.this.set((char) a[0])); }});
	}
	
	@Override
	protected EnvisionObject runConstructor(EnvisionInterpreter interpreter, Object[] args) {
		if (args.length == 0) return new EnvisionChar(DEFAULT_NAME, NULL_CHAR);
		if (args.length == 1) {
			Object obj = args[0];
			
			if (obj instanceof Character c) return new EnvisionChar(c);
			if (obj instanceof EnvisionChar c) return new EnvisionChar(c);
			if (obj instanceof EnvisionInt i) return new EnvisionChar(i);
		}
		return null;
	}
	
	//--------
	// Static
	//--------
	
	public static EnvisionChar of(char val) {
		return new EnvisionChar(val);
	}
	
	public static EnvisionChar of(String val) {
		return new EnvisionChar((val != null && !val.isEmpty()) ? val.charAt(0) : NULL_CHAR);
	}
	
	
}
