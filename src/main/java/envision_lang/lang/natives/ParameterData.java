package envision_lang.lang.natives;

import java.util.Collection;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionDoubleClass;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionListClass;
import envision_lang.lang.datatypes.EnvisionNull;
import envision_lang.lang.datatypes.EnvisionNumber;
import envision_lang.lang.language_errors.error_types.InvalidArgumentError;
import envision_lang.lang.language_errors.error_types.InvalidParameterError;
import eutil.datatypes.util.EList;
import eutil.strings.EStringUtil;

/**
 * A object which comprises function parameters within the Envision::Java
 * scripting language.
 * <p>
 * Parameter Data objects cannot be directly created, but rather, they
 * can be made from various static builder implementations.
 * 
 * @author Hunter Bragg
 */
public class ParameterData {
    
    //========
    // Fields
    //========
    
    /** A common singleton to represent empty parameters. */
    public static final ParameterData EMPTY_PARAMS = new ParameterData();
    
    private EnvisionParameter[] params;
    private String[] parameterNames;
    private IDatatype[] parameterTypes;
    
    //==============
    // Constructors
    //==============
    
    /**
     * Builds an empty set of parameters.
     */
    private ParameterData() {
        params = new EnvisionParameter[0];
        parameterNames = new String[0];
        parameterTypes = new IDatatype[0];
    }
    
    /**
     * Copy constructor.
     * 
     * @param dataIn
     */
    private ParameterData(ParameterData dataIn) {
        int size = dataIn.length();
        params = new EnvisionParameter[size];
        parameterNames = new String[size];
        parameterTypes = new IDatatype[size];
        
        for (int i = 0; i < size; i++) {
            EnvisionParameter p = dataIn.get(i);
            IDatatype type = p.datatype;
            String name = p.name;
            EnvisionObject defaultValue = p.defaultValue;
            boolean varargs = p.isVarA;
            
            params[i] = new EnvisionParameter(type, name, defaultValue, varargs);
            parameterNames[i] = name;
            parameterTypes[i] = type;
        }
    }
    
    private ParameterData(EnvisionParameter[] paramsIn) {
        params = (paramsIn != null) ? paramsIn : new EnvisionParameter[0];
        parameterNames = new String[params.length];
        parameterTypes = new IDatatype[params.length];
        
        for (int i = 0; i < params.length; i++) {
            EnvisionParameter p = params[i];
            
            parameterNames[i] = p.name;
            parameterTypes[i] = p.datatype;
        }
    }
    
    private ParameterData(IDatatype... typesIn) {
        int size = typesIn.length;
        params = new EnvisionParameter[size];
        parameterNames = new String[size];
        parameterTypes = new IDatatype[size];
        
        for (int i = 0; i < size; i++) {
            IDatatype type = typesIn[i];
            
            // check for valid parameter datatype
            if (type == null) throw new InvalidParameterError("Java::NULL is not a valid parameter type!");
            if (EnvisionStaticTypes.NULL_TYPE.compare(type)) throw new InvalidParameterError(
                                                                                             "Envision::NULL is not a valid parameter type!");
            
            String name = "";
            EnvisionParameter param = new EnvisionParameter(type, name);
            
            params[i] = param;
            parameterNames[i] = name;
            parameterTypes[i] = type;
        }
    }
    
    private ParameterData(EnvisionObject... objsIn) {
        int size = objsIn.length;
        params = new EnvisionParameter[size];
        parameterNames = new String[size];
        parameterTypes = new IDatatype[size];
        
        for (int i = 0; i < size; i++) {
            EnvisionObject obj = objsIn[i];
            
            // check for valid parameter datatype
            if (obj == null) throw new InvalidParameterError("Java::NULL is not a valid parameter type!");
            if (obj instanceof EnvisionNull) throw new InvalidParameterError("Envision::NULL is not a valid parameter type!");
            
            IDatatype type = obj.getDatatype();
            String name = "";
            
            EnvisionParameter param = new EnvisionParameter(type, name);
            
            params[i] = param;
            parameterNames[i] = name;
            parameterTypes[i] = type;
        }
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public String toString() {
        return EStringUtil.toString(parameterTypes);
    }
    
    //=========
    // Methods
    //=========
    
    /**
     * @return The number of parameters in this set.
     */
    public int length() {
        return params.length;
    }
    
    /**
     * Performs in-depth parameter comparision logic where given argument
     * values will attempt to be fully matched to each parameter in this
     * set of parameters.
     * 
     * @param  argsIn The arguments to map to our parameters
     * 
     * @return        True if the given arguments can be mapped to each of
     *                our parameters.
     */
    public boolean compare(ParameterData argsIn) {
        // First off check to see if this function even has any parameters to match
        if (params.length == 0) {
            // if there aren't any arguments given, then this is a match
            if (argsIn.params.length == 0) return true;
            // otherwise, if any number of arguments are given, then this can't be a match
            else return false;
        }
        
        // We need to match given datatypes to the function's datatypes in the same order as
        // they are provided.
        
        // If a given argument's dataype 'matches' either exactly or through 'var_type' the next
        // function's parameter type, advance to the next. Furthermore, if a given argument type
        // does not match the next function parameter's type, check to see if the function parameter
        // has a default value that can be substituted instead. If it doesn't have a default value,
        // then the match fails.
        
        // Function vararg parameters are slightly more involved. In order to match:
        // 1. the current given argument must be on the LAST function parameter index
        // 2. the current (last) function parameter MUST be a varargs type
        // 3. and finally EACH of the given arguments at this point MUST all match the
        //    function parameter's underlying datatype.
        
        final int paramLen = params.length;
        final int argLen = argsIn.params.length;
        
        // start counts at respective max
        int unmatchedParamCount = paramLen;
        int unmatchedArgCount = argLen;
        
        // flag to keep track if the varargs were matched
        boolean matchedVarargs = false;
        
        // counters to keep track of each respective index
        int functionParamIndex = 0;
        int givenArgIndex = 0;
        
        // Iterate across each given argument and attempt to match them.
        while (functionParamIndex < paramLen) {
            
            //-------------------------------------------------------------------------------
            
            // grab the current function parameter
            EnvisionParameter currentFunctionParameter = params[functionParamIndex];
            
            // check to make sure we haven't gone over the given argument length
            // if we have then this isn't a parameter match
            if (givenArgIndex >= argLen) {
                // if we've matched every parameter, then we can just break out of here
                if (unmatchedParamCount == 0 || matchedVarargs) {
                    // if we matched varargs, we can decrement the unmatched param count
                    if (matchedVarargs) unmatchedParamCount--;
                    break;
                }
                // we can still check to see if the current function parameter either has a
                // default value or it is a varargs -- in which case, we can decrement the
                // unmatched parameter count
                else if (currentFunctionParameter.hasDefault() || currentFunctionParameter.isVarA) {
                    functionParamIndex++;
                    unmatchedParamCount--;
                    continue;
                }
                
                break;
            }
            
            // grab the next given arg parameter
            EnvisionParameter givenArgParameter = argsIn.get(givenArgIndex);
            
            //-------------------------------------------------------------------------------
            
            // ensure that both the function's parameter and the given arg actually exist
            if (currentFunctionParameter == null)
                throw new InvalidParameterError("The function's parameter at index: '" + functionParamIndex + "' is Java::NULL!");
            if (givenArgParameter == null)
                throw new InvalidParameterError("The given argument's parameter at index: '" + givenArgIndex + "' is Java::NULL!");
            
            //-------------------------------------------------------------------------------
            
            // perform type check logic
            boolean matchFound = matchParameter(currentFunctionParameter, givenArgParameter);
            
            //-------------------------------------------------------------------------------
            
            // first, check to see if we are on the last function parameter
            // if we are, check to see if it's an array type
            if (functionParamIndex == params.length - 1 && currentFunctionParameter.isVarA) {
                // match found -- don't advance the function parameter index
                if (matchFound) {
                    givenArgIndex++;
                    unmatchedArgCount--;
                    matchedVarargs = true;
                }
                // otherwise, this isn't a parameter match -- fail the parameter match
                else {
                    return false;                    
                }
            }
            // otherwise, perform normal parameter type checking
            else if (matchFound) {
                // match found -- advance both the function parameter and given argument indices
                functionParamIndex++;
                givenArgIndex++;
                // decrement our unmatched counts
                unmatchedArgCount--;
                unmatchedParamCount--;
            }
            // if the previous checks failed, then this isn't a parameter match
            // check to see if the function parameter has a default value we can fallback on
            else if (currentFunctionParameter.hasDefault()) {
                // we can't advance the given argument's index because we haven't matched it yet
                // but still advance to the curent function parameter
                functionParamIndex++;
                // we can still decrement the unmatched parameter count though
                unmatchedParamCount--;
            }
            // if we've gotten to this point, then the parameters do not match -- fail the compare
            else {
                return false;
            }
        }
        
        // if we've reached this point then we just need to make sure that we've matched all parameters
        // to each given argument -- if either still has an unmatched value, then the match failed
        return (unmatchedArgCount == 0) && (unmatchedParamCount == 0);
    }
    
    /**
     * Attempts to map given arguments into this parameter's values.
     * 
     * @param args The arguments to map
     * 
     * @return A list of mapped arguments
     */
    public EList<EnvisionObject> mapArgumentsIntoParameters(EnvisionObject[] args) {
        EList<EnvisionObject> mappedArguments = EList.newList();
        
        final int paramLen = params.length;
        final int argLen = args.length;
        
        EnvisionList varargsList = null;
        
        // counters to keep track of each respective index
        int functionParamIndex = 0;
        int givenArgIndex = 0;
        
        // Iterate across each given argument and attempt to match them.
        while (functionParamIndex < paramLen) {
            
            //-------------------------------------------------------------------------------
            
            // grab the current function parameter
            EnvisionParameter currentFunctionParameter = params[functionParamIndex];
            
            if (givenArgIndex >= argLen) {
                if (currentFunctionParameter.isVarA) {
                    functionParamIndex++;
                    if (varargsList == null) {
                        mappedArguments.add(EnvisionListClass.newList());                        
                    }
                    continue;
                }
                else if (currentFunctionParameter.hasDefault()) {
                    functionParamIndex++;
                    EnvisionObject def = currentFunctionParameter.getDefaultValue();
                    if (def.isPassByValue()) def = def.copy();
                    mappedArguments.add(def);
                    continue;
                }
                
                break;
            }
            
            // grab the next given arg parameter
            EnvisionObject givenArgObject = args[givenArgIndex];
            
            //-------------------------------------------------------------------------------
            
            // ensure that both the function's parameter and the given arg actually exist
            if (currentFunctionParameter == null)
                throw new InvalidParameterError("The function's parameter at index: '" + functionParamIndex + "' is Java::NULL!");
            if (givenArgObject == null)
                throw new InvalidArgumentError("The given argument at index: '" + functionParamIndex + "' is Java::NULL!");
            
            //-------------------------------------------------------------------------------
            
            IDatatype paramDatatype = currentFunctionParameter.datatype;
            IDatatype argDatatype = givenArgObject.getDatatype();
            
            EnvisionObject mappedObject = mapArgument(currentFunctionParameter, givenArgObject);
            
            // first, check to see if we are on the last function parameter
            // if we are, check to see if it's an array type
            if (functionParamIndex == params.length - 1 && currentFunctionParameter.isVarA) {
                if (varargsList == null) {
                    varargsList = EnvisionListClass.newList();
                    mappedArguments.add(varargsList);
                }
                
                if (mappedObject != null) {
                    givenArgIndex++;
                    varargsList.add(mappedObject);
                }
                else {
                    throw new InvalidArgumentError("The given argument type: '" + argDatatype + "' cannot be mapped to: '" + paramDatatype + "'!");
                }
            }
            else if (mappedObject != null) {
                mappedArguments.add(mappedObject);
                functionParamIndex++;
                givenArgIndex++;
            }
            else if (currentFunctionParameter.hasDefault()) {
                EnvisionObject def = currentFunctionParameter.getDefaultValue();
                if (def.isPassByValue()) def = def.copy();
                mappedArguments.add(def);
                functionParamIndex++;
            }
            else {
                throw new InvalidArgumentError("The given argument type: '" + argDatatype + "' cannot be mapped to: '" + paramDatatype + "'!");
            }
        }
        
        return mappedArguments;
    }
    
    //=======================
    // Static Helper Methods
    //=======================
    
    /**
     * Internal method to check for parameter matches.
     * 
     * @param  currentFunctionParameter The function's current parameter to match against
     * @param  givenArgParameter        The current argument parameter to try to match
     * 
     * @return True if a match was successfully made
     */
    private static boolean matchParameter(EnvisionParameter currentFunctionParameter, EnvisionParameter givenArgParameter) {
        // perform a direct type check
        if (currentFunctionParameter.compare(givenArgParameter)) {
            return true;
        }
        // otherwise, check if the function parameter type is a number and if the given arg type is a number
        else if (compareNumber(currentFunctionParameter.datatype) && givenArgParameter.isNumber()) {
            return true;
        }
        // if the previous two failed, check if the function parameter's type is a var
        else if (currentFunctionParameter.isVar()) {
            return true;
        }
        
        // if all of the previous checks failed, then this isn't a parameter match
        return false;
    }
    
    private static EnvisionObject mapArgument(EnvisionParameter currentFunctionParameter, EnvisionObject givenArgObject) {
        IDatatype paramDatatype = currentFunctionParameter.datatype;
        IDatatype argDatatype = givenArgObject.getDatatype();
        
        if (paramDatatype.isVar()) {
            return givenArgObject;
        }
        else if (paramDatatype.compare(argDatatype)) {
            return givenArgObject;
        }
        else if (argDatatype.isNumber()) {
            if (compareNumber(paramDatatype)) {
                return givenArgObject;
            }
            else if (compareDouble(paramDatatype)) {
                return EnvisionDoubleClass.valueOf((EnvisionNumber) givenArgObject);
            }
            else if (compareInt(paramDatatype) && compareInt(argDatatype)) {
                return givenArgObject;
            }
        }
        
        return null;
    }
    
    private static boolean compareNumber(IDatatype datatype) { return EnvisionStaticTypes.NUMBER_TYPE.compare(datatype); }
    private static boolean compareDouble(IDatatype datatype) { return EnvisionStaticTypes.DOUBLE_TYPE.compare(datatype); }
    private static boolean compareInt(IDatatype datatype) { return EnvisionStaticTypes.INT_TYPE.compare(datatype); }
    
    //=========
    // Getters
    //=========
    
    public EnvisionParameter get(int i) { return params[i]; }
    public String[] getNames() { return parameterNames; }
    public IDatatype[] getDataTypes() { return parameterTypes; }
    
    //================
    // Static Methods
    //================
    
    public static ParameterData from(EnvisionParameter... params) {
        if (params.length == 0) return EMPTY_PARAMS;
        return new ParameterData(params);
    }
    
    public static ParameterData from(IDatatype... types) {
        if (types.length == 0) return EMPTY_PARAMS;
        return new ParameterData(types);
    }
    
    public static ParameterData from(EnvisionObject... objects) {
        if (objects.length == 0) return EMPTY_PARAMS;
        return new ParameterData(objects);
    }
    
    public static ParameterData fromParameters(Collection<EnvisionParameter> params) {
        if (params.isEmpty()) return EMPTY_PARAMS;
        return new ParameterData(params.toArray(new EnvisionParameter[0]));
    }
    
    public static ParameterData fromTypes(Collection<IDatatype> types) {
        if (types.isEmpty()) return EMPTY_PARAMS;
        return new ParameterData(types.toArray(new IDatatype[0]));
    }
    
    public static ParameterData fromObjects(Collection<EnvisionObject> objects) {
        if (objects.isEmpty()) return EMPTY_PARAMS;
        return new ParameterData(objects.toArray(new EnvisionObject[0]));
    }
    
    public static ParameterData from(ParameterData dataIn) {
        if (dataIn == EMPTY_PARAMS) return EMPTY_PARAMS;
        if (dataIn.length() == 0) return EMPTY_PARAMS;
        return new ParameterData(dataIn);
    }
    
    /*
    if (dataIn.length() == length()) {
        for (int i = 0; i < length(); i++) {
            EnvisionParameter a = get(i);
            EnvisionParameter b = dataIn.get(i);
            
            if (a.datatype.getPrimitive() == Primitives.NUMBER && b.isNumber()) return true;
            if (a.datatype.isVar()) continue;
            if (!a.compare(b)) return false;
        }
        return true;
    }
    // handle array types
    else if (params.length > 0 && parameterTypes[0].isArrayType()) {
        EnvisionParameter param = params[0];
        IDatatype type = parameterTypes[0];
        Primitives base = type.getPrimitive().getNonArrayType();
        
        if (base == null) throw new EnvisionLangError("Parameter type parsing failed! '" + param + "'");
        if (base == Primitives.VAR) return true;
        
        int size = length();
        for (int i = 0; i < size; i++) {
            EnvisionParameter pIn = dataIn.get(i);
            
            if (base == Primitives.NUMBER && pIn.isNumber()) return true;
            if (!type.compare(pIn.datatype)) return false;
        }
        
        return true;
    }
    */
    
}
