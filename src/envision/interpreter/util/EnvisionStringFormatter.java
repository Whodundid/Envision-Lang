package envision.interpreter.util;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.NullVariableError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.throwables.ReturnValue;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.objects.EnvisionMethod;
import envision.tokenizer.EscapeCode;
import eutil.datatypes.EArrayList;
import eutil.strings.StringUtil;

public class EnvisionStringFormatter {
	
	private EnvisionStringFormatter() {}

	/** Removes unnecessary double quotes from the string. */
	public static String stripQuotes(String in) {
		String made = "";
		
		for (int i = 0; i < in.length(); i++) {
			char c = in.charAt(i);
			
			//always remove the first and last double qoute on the ends of the string
			if (i != 0 && i != in.length() - 1) {
				made += c;
			}
		}
		
		return made;
	}

	public static String formatPrint(EnvisionInterpreter interpreter, Object o) {
		return formatPrint(interpreter, new EArrayList().addRT(o));
	}
	
	/**
	 * Used for print and println functions to natively format output expressions into formatted strings
	 * accounting for escape character codes and in-line var replacements.
	 * 
	 * @param interpreter
	 * @param args
	 * @return String formatted for println
	 */
	public static String formatPrint(EnvisionInterpreter interpreter, Object[] args) {
		String out = "";
		
		for (int i = 0; i < args.length; i++) {
			Object o = args[i];
			
			//test for toString overload
			if (o instanceof ClassInstance) {
				ClassInstance inst = (ClassInstance) o;
				EnvisionMethod toString = ((ClassInstance) o).getMethod("toString", new EArrayList());
				if (toString != null) {
					try { toString.invoke(interpreter); }
					catch (ReturnValue r) {
						out += EnvisionStringFormatter.handleEscapes(interpreter, (String) EnvisionObject.convert(r.object), inst);
					}
				}
				else { out += o.toString(); }
			}
			else {
				out += EnvisionStringFormatter.handleEscapes(interpreter, StringUtil.toString(o), null);
			}
			
			//add a space in between arguments
			if (i < args.length - 1) { out += " "; }
		}
		
		return out;
	}
	
	/** Processes an incomming string and parses for escape characters and performs the accoring function if one is found. */
	public static String handleEscapes(EnvisionInterpreter interpreter, String in, ClassInstance inst) {
		String out = "";
		
		//search for escape characters and handle them accordingly
		for (int i = 0; i < in.length(); i++) {
			char c = in.charAt(i);
			
			//check start of escape code
			if (c == '\\' && (i + 1 < in.length())) {
				EscapeCode esc = EscapeCode.getCode(in.charAt(i + 1));
				
				//process the code
				if (esc != null) out += EscapeCode.convertCode(esc);
				//throw invalid escape char code
				else throw new EnvisionError("Invalid string escape character code! '\\" + in.charAt(i) + "'");
				
				//consume the '\'
				i++;
			}
			//check if the start of a var expression
			else if (c == '{' && (i + 1 < in.length())) {
				String varName = findVarName(in.substring(i + 1));
				
				String val = processObject(interpreter, varName, inst);
				out += val;
				
				//consume the '{varName}'
				i += varName.length() + 1;
			}
			//otherwise just add the char to the output
			else {
				out += c;
			}
		}
		
		return out;
	}
	
	public static String findVarName(String in) {
		String name = "";
		boolean hasEnd = false;
		for (int i = 0; i < in.length(); i++) {
			char c = in.charAt(i);
			
			//check start of escape code
			if (c == '\\' && (i + 1 < in.length())) {
				EscapeCode esc = EscapeCode.getCode(in.charAt(i + 1));
				
				//process the code
				if (esc != null) name += EscapeCode.convertCode(esc);
				//throw invalid escape char code
				else throw new EnvisionError("Invalid string escape character code! '\\" + in.charAt(i) + "'");
				
				//consume the '\'
				i++;
				continue;
			}
			if (c == '{') throw new EnvisionError("Cannot include additional '{' within a string var replacement code!");
			if (c == '}') { hasEnd = true; break; }
			
			name += c;
		}
		if (!hasEnd) throw new EnvisionError("String var replacement '{...}' has no ending '}'!");
		if (name.isEmpty() || name.trim().isEmpty()) throw new EnvisionError("String var replacement name is empty or blank!");
		return name;
	}
	
	public static String processObject(EnvisionInterpreter interpreter, String varName) { return processObject(interpreter, varName, null); }
	public static String processObject(EnvisionInterpreter interpreter, String varName, ClassInstance inst) {
		EnvisionObject obj = null;
		
		//find variable within the class instance's scope
		//if there's no class instance, try to pull the variable from the interpreter's scope
		if (inst != null) obj = inst.getScope().get(varName);
		else obj = interpreter.scope().get(varName);
		
		//if the obj returned is a class instance, recursive replacement will need to be performed
		if (obj instanceof ClassInstance) { return processClassInstance(interpreter, (ClassInstance) obj); }
		//otherwise append the object's string value
		else if (obj != null) { return StringUtil.toString(obj); }
		//if the obj is null then the variable doesn't actually exist -- throw an error
		else throw new NullVariableError(varName);
	}
	
	public static String processClassInstance(EnvisionInterpreter interpreter, ClassInstance rInst) {
		EnvisionMethod toString = rInst.getMethod("toString", new EArrayList());
		
		if (toString != null) {
			try { toString.invoke(interpreter); }
			catch (ReturnValue r) {
				//recursively replace
				return handleEscapes(interpreter, StringUtil.toString(r.object), rInst);
			}
			throw new EnvisionError("String class instance replacement: this shouldn't be possible!");
		}
		
		return rInst.toString(); 
	}
	
}
