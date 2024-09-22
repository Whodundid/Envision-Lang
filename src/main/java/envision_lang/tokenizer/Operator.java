package envision_lang.tokenizer;

import java.util.HashMap;
import java.util.Map;

public enum Operator implements IKeyword {
    
    //---------
    // special
    //---------
    
    /**
     *  Addition arithmetic operator.
     * 
     *  <p>
     *  Can be used to:
     *     <pre>
     *  1. Indicate public statement visibility
     *  2. Arithmetic addition expressions
     *  3. Addition Operator Overloading for classes
     *     </pre>
     */
    ADD("+", KeywordType.ARITHMETIC, KeywordType.OPERATOR, KeywordType.VISIBILITY_MODIFIER),
    
    /**
     *  Subtraction arithmetic operator.
     * 
     *  <p>
     *  Can be used to:
     *     <pre>
     *  1. Indicate private statement visibility
     *  2. Arithmetic subtraction expressions
     *  3. Subtraction Operator Overloading for classes
     *     </pre>
     */
    
    SUB("-", KeywordType.ARITHMETIC, KeywordType.OPERATOR, KeywordType.VISIBILITY_MODIFIER),
    
    /**
     *  Restricts visibility to the scope of the current class instance along with any inherited children.
     */
    PROTECTED("_", KeywordType.VISIBILITY_MODIFIER),
    
    /** A special type of operator used to override the array brackets. */
    ARRAY_OP("\\[]", KeywordType.OPERATOR),
    
    /** A special type of operator that is capable of parsing a block statement (if enabled). */
    BLOCK_STATEMENT_TOKEN("#"),
    
    //--------------------
    // enclosing operator
    //--------------------
    
    CURLY_L("{"), //Start scope blocks.
    CURLY_R("}"), //Ends scope blocks.
    PAREN_L("("), //Starts expressions or parameter/argument data.
    PAREN_R(")"), //Ends expressions or parameter/argument data.
    BRACKET_L("["), //Starts list initializer values.
    BRACKET_R("]"), //End list initializer values.
    
    //------------
    // terminator
    //------------
    
    SEMICOLON(";", KeywordType.TERMINATOR), //Concludes the current statement. (generally unnecessary but still permitted)
    
    //------------
    // separators
    //------------
    
    COMMA(","),
    COLON(":"),
    PERIOD("."),
    VARARGS("..."),
    
    //---------
    // comment
    //---------
    
    COMMENT_SINGLE("//"),
    COMMENT_START("/*"),
    COMMENT_DESCRIPTOR("/**"),
    COMMENT_END("*/"),
    
    //-------------------
    // logical operators
    //-------------------
    
    EQUALS("==", KeywordType.OPERATOR),
    NOT_EQUALS("!=", KeywordType.OPERATOR),
    LOGICAL_NEGATE("!", KeywordType.OPERATOR),
    AND("&&", KeywordType.OPERATOR),
    OR("||", KeywordType.OPERATOR),
    BW_AND("&", KeywordType.OPERATOR),
    BW_OR("|", KeywordType.OPERATOR),
    BW_XOR("^", KeywordType.OPERATOR),
    BW_NOT("~", KeywordType.OPERATOR),
    TERNARY("?"),
    LT("<", KeywordType.OPERATOR),
    GT(">", KeywordType.OPERATOR),
    LTE("<=", KeywordType.OPERATOR),
    GTE(">=", KeywordType.OPERATOR),
    LAMBDA("->"),
    
    //------------
    // arithmetic
    //------------
    
    MUL("*", KeywordType.ARITHMETIC, KeywordType.OPERATOR),
    DIV("/", KeywordType.ARITHMETIC, KeywordType.OPERATOR),
    MOD("%", KeywordType.ARITHMETIC, KeywordType.OPERATOR),
    SHL("<<", KeywordType.ARITHMETIC, KeywordType.OPERATOR),
    SHR(">>", KeywordType.ARITHMETIC, KeywordType.OPERATOR),
    SHR_AR(">>>", KeywordType.ARITHMETIC, KeywordType.OPERATOR),
    UNARY_ADD("+_", KeywordType.ARITHMETIC, KeywordType.OPERATOR),
    UNARY_SUB("-_", KeywordType.ARITHMETIC, KeywordType.OPERATOR),
    
    //------------
    // assignment
    //------------
    
    ASSIGN("=", KeywordType.ASSIGNMENT, KeywordType.OPERATOR),
    ADD_ASSIGN("+=", KeywordType.ARITHMETIC, KeywordType.ASSIGNMENT, KeywordType.OPERATOR),
    SUB_ASSIGN("-=", KeywordType.ARITHMETIC, KeywordType.ASSIGNMENT, KeywordType.OPERATOR),
    MUL_ASSIGN("*=", KeywordType.ARITHMETIC, KeywordType.ASSIGNMENT, KeywordType.OPERATOR),
    DIV_ASSIGN("/=", KeywordType.ARITHMETIC, KeywordType.ASSIGNMENT, KeywordType.OPERATOR),
    MOD_ASSIGN("%=", KeywordType.ARITHMETIC, KeywordType.ASSIGNMENT, KeywordType.OPERATOR),
    BW_AND_ASSIGN("&=", KeywordType.ARITHMETIC, KeywordType.ASSIGNMENT, KeywordType.OPERATOR),
    BW_OR_ASSIGN("|=", KeywordType.ARITHMETIC, KeywordType.ASSIGNMENT, KeywordType.OPERATOR),
    BW_XOR_ASSIGN("^=", KeywordType.ARITHMETIC, KeywordType.ASSIGNMENT, KeywordType.OPERATOR),
    SHL_ASSIGN("<<=", KeywordType.ARITHMETIC, KeywordType.ASSIGNMENT, KeywordType.OPERATOR),
    SHR_ASSIGN(">>=", KeywordType.ARITHMETIC, KeywordType.ASSIGNMENT, KeywordType.OPERATOR),
    SHR_AR_ASSIGN(">>>=", KeywordType.ARITHMETIC, KeywordType.ASSIGNMENT, KeywordType.OPERATOR),
    
    /** PRE_FIX increment operator: '++i' */
    PRE_INC("++_", KeywordType.ARITHMETIC, KeywordType.ASSIGNMENT, KeywordType.OPERATOR),
    /** PRE_FIX decrement operator: '--i' */
    PRE_DEC("--_", KeywordType.ARITHMETIC, KeywordType.ASSIGNMENT, KeywordType.OPERATOR),
    /** POST_FIX increment operator: 'i++' */
    POST_INC("_++", KeywordType.ASSIGNMENT, KeywordType.OPERATOR),
    /** POST_FIX decrement operator: 'i--' */
    POST_DEC("_--", KeywordType.ASSIGNMENT, KeywordType.OPERATOR),
    
    ;
    
    public final String operatorString;
    private final int types;
    
    private Operator(String keywordIn, KeywordType... typeIn) {
        operatorString = keywordIn;

        int m = 0b00000000;
        for (var t : typeIn) m |= t.byte_val;
        types = m;
    }
    
    //-----------------------------------------------------------------------------------------------------------------------------
    
    private static Map<String, Operator> operators = new HashMap<>();
    
    static {
        for (var o : values()) operators.put(o.operatorString, o);
    }
    
    /** Returns a keyword from the given input String. If no keywords match, null is returned instead. */
//    public static Operator getOperator(String in) {
//        return (in != null) ? operators.get(in) : null;
//    }
    
    //-----------------------------------------------------------------------------------------------------------------------------
    
    /**
     * Converts this operator to its post equivalent if it is either the
     * operator: 'INC' or 'DEC'.
     * 
     * @return The post version of this operator
     */
    public Operator makePost() {
        return makePost(this);
    }
    
    /**
     * Used to switch the operator position in increment and decrement statements.
     * 
     * <p>For example:<pre>
     * ++x -> x++
     * </pre>
     * 
     * @param k The increment or decrement operator
     * @return The post version of the given operator
     */
    public static Operator makePost(Operator k) {
        if (k == PRE_INC) return POST_INC;
        if (k == PRE_DEC) return POST_DEC;
        throw new RuntimeException("Not a valid operator for 'post' conversion!");
    }
    
    /**
     * Converts this operator to its unary equivalent if it is either the
     * operator: 'ADD' or 'SUB'.
     * 
     * @return The unary version of this numeric operator
     */
    public Operator convertToUnary() {
        return convertToUnary(this);
    }
    
    /**
     * Converts the following operators '+' and '-' (ADD, SUB) to the unary
     * equivalents of 'UNARY_ADD' and 'UNARY_NEGATE' respectively.
     * 
     * @param k The 'ADD' or 'SUB' operator to convert from
     * @return The unary version of the given operator
     */
    public static Operator convertToUnary(Operator k) {
        if (k == ADD) return UNARY_ADD;
        if (k == SUB) return UNARY_SUB;
        throw new RuntimeException("Not a valid operator for 'numeric unary' conversion!");
    }

    @Override
    public boolean hasType(KeywordType type) {
        return (types & type.byte_val) == type.byte_val;
    }
    
    /**
     * Returns true if this operator is a unary expression operator.
     * 
     * @return true if unary
     */
    public boolean isUnary() {
        return switch (this) {
        case LOGICAL_NEGATE, UNARY_ADD, UNARY_SUB, PRE_INC, PRE_DEC, POST_INC, POST_DEC -> true;
        default -> false;
        };
    }

    @Override public boolean isOperator() { return true; }
    @Override public boolean isReservedWord() { return false; }
    
}
