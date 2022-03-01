package envision.bytecode;

import envision.lang.util.Primitives;

import java.util.HashMap;
import java.util.Map;

public enum BC_Type {
	
	OBJECT('O', Primitives.VAR),
	INT('I', Primitives.INT),
	DOUBLE('D', Primitives.DOUBLE),
	CHAR('C', Primitives.CHAR),
	STRING('S', Primitives.STRING),
	ARRAY('[', Primitives.VAR_A),
	LIST('U', Primitives.LIST),
	CLASS('L', Primitives.CLASS),
	METH('M', Primitives.FUNCTION),
	NUMBER('N', Primitives.NUMBER),
	ENUM('E', Primitives.ENUM),
	
	;
	
	private final char typeChar;
	private final Primitives envisionType;
	private static final Map<Character, BC_Type> types = new HashMap();
	
	private BC_Type(char typeCharIn, Primitives envisionTypeIn) {
		typeChar = typeCharIn;
		envisionType = envisionTypeIn;
	}
	
	static {
		for (BC_Type t : values()) types.put(t.typeChar, t);
	}
	
	public char getTypeChar() { return typeChar; }
	public Primitives getDataType() { return envisionType; }
	
	public static BC_Type getType(char in) { return types.get(in); }
	
}
