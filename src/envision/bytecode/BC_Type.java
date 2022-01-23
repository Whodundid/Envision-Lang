package envision.bytecode;

import envision.lang.util.EnvisionDataType;

import java.util.HashMap;
import java.util.Map;

public enum BC_Type {
	
	OBJECT('O', EnvisionDataType.OBJECT),
	INT('I', EnvisionDataType.INT),
	DOUBLE('D', EnvisionDataType.DOUBLE),
	CHAR('C', EnvisionDataType.CHAR),
	STRING('S', EnvisionDataType.STRING),
	ARRAY('[', EnvisionDataType.OBJECT_A),
	LIST('U', EnvisionDataType.LIST),
	CLASS('L', EnvisionDataType.CLASS),
	METH('M', EnvisionDataType.METHOD),
	NUMBER('N', EnvisionDataType.NUMBER),
	ENUM('E', EnvisionDataType.ENUM),
	
	;
	
	private final char typeChar;
	private final EnvisionDataType envisionType;
	private static final Map<Character, BC_Type> types = new HashMap();
	
	private BC_Type(char typeCharIn, EnvisionDataType envisionTypeIn) {
		typeChar = typeCharIn;
		envisionType = envisionTypeIn;
	}
	
	static {
		for (BC_Type t : values()) types.put(t.typeChar, t);
	}
	
	public char getTypeChar() { return typeChar; }
	public EnvisionDataType getDataType() { return envisionType; }
	
	public static BC_Type getType(char in) { return types.get(in); }
	
}
