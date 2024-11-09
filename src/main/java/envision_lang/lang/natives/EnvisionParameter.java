package envision_lang.lang.natives;

import envision_lang.lang.EnvisionObject;

/**
 * A parameter for a function.
 * <p>
 * Parameters can possess the following properties:
 * <li> 1. They can either be typed or typeless (VAR)
 * <li> 2. A name
 * <li> 3. A potential default value if no argument is mapped
 * <li> 4. The
 * 
 * @author Hunter
 */
public class EnvisionParameter {
    
    //========
    // Fields
    //========
    
    /**
     * The underlying datatype for this parameter. This can either be a
     * strong type or it can be completely typeless (VAR).
     */
    public final IDatatype datatype;
    
    /** The name of this parameter. */
    public final String name;
    
    /**
     * In the event that no argument is matched against this parameter's
     * type, the parameter can potentially fill in with this default value
     * if it was specified.
     */
    public final EnvisionObject defaultValue;
    
    /**
     * Specifies whether or not this parameter can take in an array of
     * values instead of just one.
     */
    public final boolean isVarA;    
    //==============
    // Constructors
    //==============
    
    public EnvisionParameter(IDatatype typeIn, String nameIn) {
        this(typeIn, nameIn, null, false);
    }
    
    public EnvisionParameter(IDatatype typeIn, String nameIn, boolean isVarargs) {
        this(typeIn, nameIn, null, isVarargs);
    }
    
    public EnvisionParameter(IDatatype typeIn, String nameIn, EnvisionObject defaultValueIn, boolean isVarargs) {
        datatype = typeIn;
        name = nameIn;
        defaultValue = defaultValueIn;
        isVarA = isVarargs;
    }    
    //===========
    // Overrides
    //===========
    
    @Override
    public String toString() {
        String v = (isVarA) ? "... " : ((name.equals("")) ? "" : " ");
        String d = (defaultValue != null) ? " = " + defaultValue : "";
        return "[" + datatype + v + name + d + "]";
    }    
    //=========
    // Methods
    //=========
    
    /**
     * Compares the datatypes of each parameter, name is irrelevant when
     * comparing.
     */
    public boolean compare(EnvisionParameter paramIn) {
        return paramIn != null && paramIn.datatype.equals(datatype);
    }
    
    public boolean hasDefault() { return defaultValue != null; }
    public boolean isNumber() { return datatype.isNumber(); }
    public boolean isVarA() { return isVarA; }
    public boolean isVar() { return datatype.isVar(); }    
    //=========
    // Getters
    //=========
    
    public EnvisionObject getDefaultValue() { return defaultValue; }
    
    public EnvisionObject getNewDefaultValueInstance() {
        return (defaultValue != null) ? ((defaultValue.isPassByValue()) ? defaultValue.copy() : defaultValue) : null;
    }
    
}
