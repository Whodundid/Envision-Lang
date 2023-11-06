package envision_lang.interpreter.util;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.scope.IScope;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.datatypes.EnvisionBoolean;
import envision_lang.lang.datatypes.EnvisionChar;
import envision_lang.lang.datatypes.EnvisionDouble;
import envision_lang.lang.datatypes.EnvisionInt;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionString;
import envision_lang.lang.datatypes.EnvisionTuple;
import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.lang.language_errors.error_types.NullVariableError;
import envision_lang.parser.EnvisionLangParser;
import envision_lang.parser.ParsingError;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.tokenizer.EscapeCode;
import eutil.EUtil;
import eutil.strings.EStringBuilder;
import eutil.strings.EStringUtil;

public class EnvisionStringFormatter {
    
    private EnvisionStringFormatter() {}
    
    public static String formatPrint(EnvisionInterpreter interpreter, EnvisionObject o) {
        return formatPrint(interpreter, new EnvisionObject[] { o }, true);
    }
    
    public static String formatPrint(EnvisionInterpreter interpreter, EnvisionObject o, boolean format) {
        return formatPrint(interpreter, new EnvisionObject[] { o }, format);
    }
    
    public static String formatPrint(EnvisionInterpreter interpreter, EnvisionObject[] args) {
        return formatPrint(interpreter, args, true);
    }
    
    /**
     * Used for print and println functions to natively format output
     * expressions into formatted strings accounting for escape character
     * codes and in-line var replacements.
     * 
     * @param interpreter
     * @param args
     * 
     * @return String formatted for println
     */
    public static String formatPrint(EnvisionInterpreter interpreter, EnvisionObject[] args, boolean format) {
        var out = new EStringBuilder();
        
        for (int i = 0; i < args.length; i++) {
            EnvisionObject o = args[i];
            
            out.append(getStringValueFor(interpreter, o, format));
            
            // add a space in between arguments
            if (i < args.length - 1) out.append(" ");
        }
        
        return out.toString();
    }
    
    public static String getStringValueFor(EnvisionInterpreter interpreter, EnvisionObject object, boolean format) {
        var sb = new EStringBuilder();
        
        // check for lists/tuples separately because they can have several 
        // layers 'toString' argument formatting and variable inserts.
        if (object instanceof EnvisionList list) {
            int cur = 0;
            var l = list.getInternalList();
            sb.append("[");
            for (var listObj : l) {
                sb.append(formatPrint(interpreter, listObj, format));
                if (++cur < l.size()) sb.append(", ");
            }
            sb.append("]");
        }
        else if (object instanceof EnvisionTuple tuple) {
            int cur = 0;
            var l = tuple.getInternalList();
            sb.append("(");
            for (var listObj : l) {
                sb.append(formatPrint(interpreter, listObj, format));
                if (++cur < l.size()) sb.append(", ");
            }
            sb.append(")");
        }
        // test for toString overload
        else if (object instanceof ClassInstance inst) {
            // grab the toString of the instance as well as the instance itself
            String str = inst.executeToString_i(interpreter);
            
            ClassInstance to_pass = null;
            if (!inst.isPrimitive()) to_pass = inst;
            
            String parsedEscapes = handleEscapes(str);
            String varsInjected = injectVariable(interpreter, parsedEscapes, to_pass, format);
            sb.append(varsInjected);
        }
        else {
            String parsedEscapes = handleEscapes(EStringUtil.toString(object));
            String varsInjected = injectVariable(interpreter, parsedEscapes, null, format);
            sb.append(varsInjected);
        }
        
        return sb.toString();
    }
    
    /**
     * Processes an incomming string and parses for escape characters and
     * performs the according function if one is found.
     */
    public static String handleEscapes(String in) {
        var out = new EStringBuilder();
        
        // search for escape characters and handle them accordingly
        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            
            // check start of escape code
            if (c == '\\' && (i + 1 < in.length())) {
                EscapeCode esc = EscapeCode.getCode(in.charAt(i + 1));
                
                // process the code
                if (esc != null) out.append(EscapeCode.convertCode(esc));
                // throw invalid escape char code
                else {
                    //System.out.println((int) in.charAt(i + 1));
                    throw new EnvisionLangError("Invalid string escape character code! '\\" + in.charAt(i + 1) + "'");
                }
                
                // consume the '\'
                i++;
            }
            // otherwise just add the char to the output
            else {
                out.append(c);
            }
        }
        
        return out.toString();
    }
    
    public static String injectVariable(EnvisionInterpreter interpreter, String in, EnvisionObject object, boolean format) {
        var sb = new EStringBuilder();
        
        // search for escape characters and handle them accordingly
        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            
            // check if the start of a var expression
            if (format && c == '{' && (i + 1 < in.length())) {
                String varName = findVarName(in.substring(i + 1));
                
                String val = processObject(interpreter, varName, object, format);
                sb.append(val);
                
                // consume the '{varName}'
                i += (varName.length() + 1);
            }
            // otherwise just add the char to the output
            else {
                sb.append(c);
            }
        }
        
        return sb.toString();
    }
    
    public static String findVarName(String in) {
        var name = new EStringBuilder();
        boolean hasEnd = false;
        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            
            // check start of escape code
            if (c == '\\' && (i + 1 < in.length())) {
                EscapeCode esc = EscapeCode.getCode(in.charAt(i + 1));
                
                // process the code
                if (esc != null) name.append(EscapeCode.convertCode(esc));
                // throw invalid escape char code
                else throw new EnvisionLangError("Invalid string escape character code! '\\" + in.charAt(i) + "'");
                
                // consume the '\'
                i++;
                continue;
            }
            if (c == '{')
                throw new EnvisionLangError("Cannot include additional '{' within a string var replacement code!");
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
    
    public static String processObject(EnvisionInterpreter interpreter,
                                       String varName,
                                       EnvisionObject object,
                                       boolean format)
    {
        EnvisionObject obj = null;
        
        // try to find matching variable by name within the class instance's scope
        // if there's no class instance, try to pull the variable from the interpreter's scope
//        if (object instanceof ClassInstance ci) {
//            IScope scope = ci.getScope();
//            obj = scope.get(varName);
//        }
//        else {
//            
//        }
        
        try {
            ParsedExpression expr = EnvisionLangParser.parseExpression(varName);
            obj = interpreter.evaluate(expr);
        }
        catch (ParsingError e) {
            // if the parser failed, then it probably wasn't valid code (probably)
            
            // try pulling the variable out of the interpreter's working scope
            try {
                IScope scope = interpreter.scope();
                obj = scope.get(varName);
            }
            catch (Exception q) {
                // if the scope didn't have it then it might just not exist
            }
        }
        
        // if the obj returned is a primitive, return its internal value
        if (obj instanceof EnvisionBoolean b) return String.valueOf(b.bool_val);
        else if (obj instanceof EnvisionChar c) return String.valueOf(c.char_val);
        else if (obj instanceof EnvisionInt i) return String.valueOf(i.int_val);
        else if (obj instanceof EnvisionDouble d) return String.valueOf(d.double_val);
        // if the obj returned is a class instance, recursive replacement will need to be performed
        else if (obj instanceof ClassInstance obj_inst) {
            String r_str = obj_inst.executeToString_i(interpreter);
            String parsedEscapes = handleEscapes(r_str);
            String varsInjected = injectVariable(interpreter, parsedEscapes, obj_inst, format);
            return varsInjected;
        }
        // otherwise append the object's string value
        else if (obj != null) return EStringUtil.toString(obj);
        // if the obj is null then the variable doesn't actually exist -- throw an error
        else throw new NullVariableError(varName);
    }
    
    //-----------------------------------------------------------------------------------------
    
    public static String injectLambdaInputs(EnvisionInterpreter interpreter,
                                            String inputName,
                                            EnvisionObject[] inputs,
                                            EnvisionString target)
    {
        return injectLambdaInputs(interpreter, interpreter.scope(), inputName, inputs, target);
    }
    
    public static String injectLambdaInputs(EnvisionInterpreter interpreter,
                                            IScope scope,
                                            String inputName,
                                            EnvisionObject[] inputs,
                                            EnvisionString target)
    {
        // don't allow null inputs
        if (EUtil.anyNull(scope, target, inputs)) {
            throw new EnvisionLangError("");
        }
        
        var sb = new EStringBuilder();
        
        String targetString = target.string_val;
        //int lambdaTargets = countLambdaTargets(targetString);
        
        // format the incoming target string to the given inputs
        if (inputName != null) {
            // TODO: this needs to process in-line {$...} as there could be actual dollar signs in the intended output
            targetString = targetString.replace("$", inputName);
        }
//        else {
//            throw new EnvisionLangError("Cannot map lambda targets to given inputs, length mismatch! " +
//                                        "expected '" + lambdaTargets + "' but got '" + inputNames.length +
//                                        "' instead!");
//        }
        
        // process each variable input and attempt to inject it into the result string
        for (var input : inputs) {
            var result = injectVariable(interpreter, targetString, input, true);
            
            sb.append(result);
        }
        
        return sb.toString();
    }
    
    private static int countLambdaTargets(String input) {
        return (int) input.chars().filter(c -> c == '$').count();
    }
    
}
