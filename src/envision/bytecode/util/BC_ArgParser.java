package envision.bytecode.util;

import envision.bytecode.BC_Type;
import envision.exceptions.EnvisionError;

/** Extrats name, return type and parameters from bytecode arguments. */
public class BC_ArgParser {
	
	public static enum ArgParserMode {
		DEF, // "start;I"
		METH, // "cross;LV;|LV;LV;"
		AMOUNT, // imakeget 3 1 2 3
	}
	
	public static String[] parseArgs(ArgParserMode mode, String in) {
		switch (mode) {
		case DEF: return parseDef(in);
		case METH: return parseMeth(in);
		case AMOUNT: return parseAmount(in);
		default: return null;
		}
	}
	
	public static String[] parseDef(String in) {
		in = (in.length() >= 2) ? in.substring(1, in.length() - 1) : in;
		String name = null, type = null;
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < in.length(); i++) {
			char c = in.charAt(i);
			
			if (name == null) {
				if (c == ';') {
					name = builder.toString();
					builder = new StringBuilder();
				}
				else builder.append(c);
			}
			else if (type == null) {
				if (c == ';') type = builder.toString();
				else if (i == in.length() - 1) { builder.append(c); type = builder.toString(); }
				else builder.append(c);
			}
		}
		
		return new String[] { name, BC_Type.getType(type.charAt(0)).getDataType().string_type };
	}
	
	public static String[] parseMeth(String in) {
		String name = null, rtype = null;
		//String[] params;
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < in.length(); i++) {
			char c = in.charAt(i);
			
			if (name == null) {
				if (c == ';') {
					name = builder.toString();
					builder = new StringBuilder();
				}
				else builder.append(c);
			}
			else if (rtype == null) {
				if (c == ';' || c == '|') rtype = builder.toString();
				else builder.append(c);
			}
			else {
				
			}
		}
		
		//return new String[] { name, type };
		return null;
	}
	
	public static String[] parseAmount(String in) {
		return in.split(" ");
	}
	
	/** Errors if the arg count does not equal amount. If the arg array is null but the amount is zero, no error is thrown. */
	public static void expect(String[] in, int amount) {
		int size = (in != null) ? in.length : 0;
		if (size != amount) throw new EnvisionError("Invalid bytecode argument count! " + size + " : expected " + amount);
	}
	
}
