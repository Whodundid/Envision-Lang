package envision.bytecode;

import java.util.HashMap;
import java.util.Map;

public enum ByteCode {	
	NEWLINE		("\n"),
	SCOPE		("scope"),
	_SCOPE		("_scope"),
	LOCAL		("local"),
	PUBLIC		("public"),
	PROTECTED	("protected"),
	PRIVATE		("private"),
	STATIC		("static"),
	CLASS		("class"),
	METH		("meth"),
	CONST		("const"),
	OP			("op"),
	_LOCAL		("_local"),
	_PUBLIC		("_public"),
	_PROTECTED	("_protected"),
	_PRIVATE	("_private"),
	_STATIC		("_static"),
	_CLASS		("_class"),
	_METH		("_meth"),
	_CONST		("_const"),
	_OP			("_op"),
	LNUM		("lnum"),
	CNUM		("cnum"),
	INUM		("inum"),
	LDEF		("ldef"),
	CDEF		("cdef"),
	IDEF		("idef"),
	ALOAD		("aload"),
	ALOAD_0		("aload_0"),
	ALOAD_1		("aload_1"),
	ALOAD_2		("aload_2"),
	ALOAD_0_1	("aload_0_1"),
	LLOAD		("lload"),
	LLOAD_0		("lload_0"),
	LLOAD_1		("lload_1"),
	LLOAD_2		("lload_2"),
	LLOAD_0_1	("lload_0_1"),
	CLOAD		("cload"),
	CLOAD_0		("cload_0"),
	CLOAD_1		("cload_1"),
	CLOAD_2		("cload_2"),
	CLOAD_0_1	("cload_0_1"),
	ILOAD		("iload"),
	ILOAD_0		("iload_0"),
	ILOAD_1		("iload_1"),
	ILOAD_2		("iload_2"),
	ILOAD_0_1	("iload_0_1"),
	LSTORE		("lstore"),
	LSTORE_0	("lstore_0"),
	LSTORE_1	("lstore_1"),
	LSTORE_2	("lstore_2"),
	CSTORE		("cstore"),
	CSTORE_0	("cstore_0"),
	CSTORE_1	("cstore_1"),
	CSTORE_2	("cstore_2"),
	ISTORE		("istore"),
	ISTORE_0	("istore_0"),
	ISTORE_1	("istore_1"),
	ISTORE_2	("istore_2"),
	GETI		("geti"),
	STOREI		("storei"),
	ALOADI		("aloadi"),
	ASTOREI		("astorei"),
	ADD			("add"),
	SUB			("sub"),
	MUL			("mul"),
	DIV			("div"),
	NEW			("new"),
	INVOKE		("invoke"),
	INVOKECLASS	("invokeclass"),
	RET			("ret"),
	PUSH_I		("push_i"),
	PUSH_D		("push_d"),
	PUSH_C		("push_c"),
	PUSH_S		("push_s"),
	POP			("pop"),
	CONCAT		("concat"),
	CMAKEGET	("cmakeget"),
	CMAKESET	("cmakeset"),
	IMAKEGET	("imakeget"),
	IMAKESET	("imakeset"),
	SPACE		(" "),
	//debug
	STACK		("stack"),
	;
	
	private final String chars;
	//private static final Map<Byte, ByteCode> codes = new HashMap(values().length);
	private static final Map<String, ByteCode> stringCodes = new HashMap(values().length);
	
	private ByteCode(String charsIn) {
		chars = charsIn;
	}
	
	static {
		//for (ByteCode bc : values()) {
		//	codes.put(bc.bytecode, bc);
		//}
		
		for (ByteCode bc : values()) {
			stringCodes.put(bc.chars, bc);
		}
	}
	
	public int code() { return ordinal(); }
	public String chars() { return chars; }
	
	public static ByteCode get(int in) { return values()[in]; }
	public static ByteCode getCodeString(String in) { return stringCodes.get(in); }
	
}
