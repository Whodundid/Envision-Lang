package envision.interpreter.util;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.NullVariableError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.throwables.ReturnValue;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.internal.EnvisionFunction;
import envision.lang.util.ParameterData;
import envision.tokenizer.EscapeCode;
import eutil.strings.StringUtil;

public class EnvisionStringFormatter {
	
	private EnvisionStringFormatter() {}

	public static String formatPrint(EnvisionInterpreter interpreter, Object o) {
		return formatPrint(interpreter, new Object[] {o});
	}
	
	/**
	 * Used for print and println functions to natively format output expressions into formatted strings
	 * accounting for escape character codes and in-line var replacements.
	 * 
	 * @param interpreter
	 * @param args
	 * @return String formatted for println
	 */
	public static String formatPrint(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		var out = new StringBuilder();
		
		for (int i = 0; i < args.length; i++) {
			Object o = args[i];
			
			//test for toString overload
			if (o instanceof ClassInstance inst) {
				EnvisionFunction toString = inst.getFunction("toString", new ParameterData());
				if (toString != null) {
					try {
						toString.invoke(interpreter, new EnvisionObject[0]);
					}
					catch (ReturnValue r) {
						String str_r = (String) EnvisionObject.convert(r.object);
						out.append(handleEscapes(interpreter, str_r, inst));
					}
				}
				else out.append(o.toString());
			}
			else {
				out.append(handleEscapes(interpreter, StringUtil.toString(o), null));
			}
			
			//add a space in between arguments
			if (i < args.length - 1) out.append(" ");
		}
		
		return out.toString();
	}
	
	/** Processes an incomming string and parses for escape characters and performs the accoring function if one is found. */
	public static String handleEscapes(EnvisionInterpreter interpreter, String in, ClassInstance inst) {
		var out = new StringBuilder();
		
		//search for escape characters and handle them accordingly
		for (int i = 0; i < in.length(); i++) {
			char c = in.charAt(i);
			
			//check start of escape code
			if (c == '\\' && (i + 1 < in.length())) {
				EscapeCode esc = EscapeCode.getCode(in.charAt(i + 1));
				
				//process the code
				if (esc != null) out.append(EscapeCode.convertCode(esc));
				//throw invalid escape char code
				else throw new EnvisionError("Invalid string escape character code! '\\" + in.charAt(i) + "'");
				
				//consume the '\'
				i++;
			}
			//check if the start of a var expression
			else if (c == '{' && (i + 1 < in.length())) {
				String varName = findVarName(in.substring(i + 1));
				
				String val = processObject(interpreter, varName, inst);
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
				else throw new EnvisionError("Invalid string escape character code! '\\" + in.charAt(i) + "'");
				
				//consume the '\'
				i++;
				continue;
			}
			if (c == '{') throw new EnvisionError("Cannot include additional '{' within a string var replacement code!");
			if (c == '}') {
				hasEnd = true;
				break;
			}
			
			name.append(c);
		}
		
		if (!hasEnd) throw new EnvisionError("String var replacement '{...}' has no ending '}'!");
		if (name.isEmpty() || name.toString().trim().isEmpty()) {
			throw new EnvisionError("String var replacement name is empty or blank!");
		}
		
		return name.toString();
	}
	
	public static String processObject(EnvisionInterpreter interpreter, String varName) { return processObject(interpreter, varName, null); }
	public static String processObject(EnvisionInterpreter interpreter, String varName, ClassInstance inst) {
		EnvisionObject obj = null;
		
		//find variable within the class instance's scope
		//if there's no class instance, try to pull the variable from the interpreter's scope
		if (inst != null) obj = inst.getScope().get(varName);
		else obj = interpreter.scope().get(varName);
		
		//if the obj returned is a class instance, recursive replacement will need to be performed
		if (obj instanceof ClassInstance obj_inst) return processClassInstance(interpreter, obj_inst);
		//otherwise append the object's string value
		else if (obj != null) return StringUtil.toString(obj);
		//if the obj is null then the variable doesn't actually exist -- throw an error
		else throw new NullVariableError(varName);
	}
	
	public static String processClassInstance(EnvisionInterpreter interpreter, ClassInstance rInst) {
		EnvisionFunction toString = rInst.getFunction("toString", new ParameterData());
		
		if (toString != null) {
			try {
				toString.invoke(interpreter, new EnvisionObject[0]);
			}
			catch (ReturnValue r) {
				//recursively replace
				return handleEscapes(interpreter, StringUtil.toString(r.object), rInst);
			}
			throw new EnvisionError("String class instance replacement: this shouldn't be possible!");
		}
		
		return rInst.toString();
	}
	
}
