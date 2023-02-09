package envision_lang.interpreter.util;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionTuple;
import envision_lang.lang.exceptions.EnvisionLangError;
import envision_lang.lang.exceptions.errors.NullVariableError;
import envision_lang.tokenizer.EscapeCode;
import eutil.strings.EStringUtil;

public class EnvisionStringFormatter {
	
	private EnvisionStringFormatter() {}

	public static String formatPrint(EnvisionInterpreter interpreter, EnvisionObject o) {
		return formatPrint(interpreter, new EnvisionObject[] {o}, false);
	}
	
	public static String formatPrint(EnvisionInterpreter interpreter, EnvisionObject o, boolean format) {
		return formatPrint(interpreter, new EnvisionObject[] {o}, format);
	}
	
	public static String formatPrint(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		return formatPrint(interpreter, args, false);
	}
	
	/**
	 * Used for print and println functions to natively format output expressions into formatted strings
	 * accounting for escape character codes and in-line var replacements.
	 * 
	 * @param interpreter
	 * @param args
	 * @return String formatted for println
	 */
	public static String formatPrint(EnvisionInterpreter interpreter, EnvisionObject[] args, boolean format) {
		StringBuilder out = new StringBuilder();
		
		for (int i = 0; i < args.length; i++) {
			EnvisionObject o = args[i];
			
			//check for lists/tuples separately because they can have several 
			//layers 'toString' argument formatting and variable inserts.
			if (o instanceof EnvisionList list) {
				int cur = 0;
				var l = list.getInternalList();
				out.append("[");
				for (var listObj : l) {
					out.append(formatPrint(interpreter, listObj, format));
					if (++cur < l.size()) out.append(", ");
				}
				out.append("]");
			}
			else if (o instanceof EnvisionTuple tuple) {
				int cur = 0;
				var l = tuple.getInternalList();
				out.append("(");
				for (var listObj : l) {
					out.append(formatPrint(interpreter, listObj, format));
					if (++cur < l.size()) out.append(", ");
				}
				out.append(")");
			}
			//test for toString overload
			else if (o instanceof ClassInstance inst) {
				//grab the toString of the instance as well as the instance itself
				String str = inst.executeToString_i(interpreter);
				
				ClassInstance to_pass = null;
				if (!inst.isPrimitive()) to_pass = inst;
				
				out.append(handleEscapes(interpreter, str, to_pass, format));
			}
			else {
				out.append(handleEscapes(interpreter, EStringUtil.toString(o), null, format));
			}
			
			//add a space in between arguments
			if (i < args.length - 1) out.append(" ");
		}
		
		return out.toString();
	}
	
	/** Processes an incomming string and parses for escape characters and performs the according function if one is found. */
	public static String handleEscapes(EnvisionInterpreter interpreter, String in, ClassInstance inst, boolean format) {
		StringBuilder out = new StringBuilder();
		
		//search for escape characters and handle them accordingly
		for (int i = 0; i < in.length(); i++) {
			char c = in.charAt(i);
			
			//check start of escape code
			if (c == '\\' && (i + 1 < in.length())) {
				EscapeCode esc = EscapeCode.getCode(in.charAt(i + 1));
				
				//process the code
				if (esc != null) out.append(EscapeCode.convertCode(esc));
				//throw invalid escape char code
				else {
					//System.out.println((int) in.charAt(i + 1));
					throw new EnvisionLangError("Invalid string escape character code! '\\" + in.charAt(i + 1) + "'");
				}
				
				//consume the '\'
				i++;
			}
			//check if the start of a var expression
			else if (format && c == '{' && (i + 1 < in.length())) {
				String varName = findVarName(in.substring(i + 1));
				
				String val = processObject(interpreter, varName, inst, format);
				out.append(val);
				
				//consume the '{varName}'
				i += varName.length() + 1;
			}
			//otherwise just add the char to the output
			else {
				out.append(c);
			}
		}
		
		return out.toString();
	}
	
	public static String findVarName(String in) {
		var name = new StringBuilder();
		boolean hasEnd = false;
		for (int i = 0; i < in.length(); i++) {
			char c = in.charAt(i);
			
			//check start of escape code
			if (c == '\\' && (i + 1 < in.length())) {
				EscapeCode esc = EscapeCode.getCode(in.charAt(i + 1));
				
				//process the code
				if (esc != null) name.append(EscapeCode.convertCode(esc));
				//throw invalid escape char code
				else throw new EnvisionLangError("Invalid string escape character code! '\\" + in.charAt(i) + "'");
				
				//consume the '\'
				i++;
				continue;
			}
			if (c == '{') throw new EnvisionLangError("Cannot include additional '{' within a string var replacement code!");
			if (c == '}') {
				hasEnd = true;
				break;
			}
			
			name.append(c);
		}
		
		if (!hasEnd) throw new EnvisionLangError("String var replacement '{...}' has no ending '}'!");
		if (name.isEmpty() || name.toString().trim().isEmpty()) {
			throw new EnvisionLangError("String var replacement name is empty or blank!");
		}
		
		return name.toString();
	}
	
	public static String processObject(EnvisionInterpreter interpreter, String varName, ClassInstance inst, boolean format) {
		EnvisionObject obj = null;
		
		//find variable within the class instance's scope
		//if there's no class instance, try to pull the variable from the interpreter's scope
		if (inst != null) obj = inst.getScope().get(varName);
		else obj = interpreter.scope().get(varName);
		
		//if the obj returned is a class instance, recursive replacement will need to be performed
		if (obj instanceof ClassInstance obj_inst) {
			String r_str = obj_inst.executeToString_i(interpreter);
			return handleEscapes(interpreter, r_str, obj_inst, format);
		}
		//otherwise append the object's string value
		else if (obj != null) return EStringUtil.toString(obj);
		//if the obj is null then the variable doesn't actually exist -- throw an error
		else throw new NullVariableError(varName);
	}
	
}
