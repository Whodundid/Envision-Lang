package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.tokenizer.Token;

public class Expr_Literal extends ParsedExpression {
    
    //========
    // Fields
    //========
    
    public final Token<?> literalToken;
    public final Object value;
    
    public final boolean isInteger;
    public final boolean isDouble;
    public final boolean isBoolean;
    public final boolean isString;
    public final boolean isCharacter;
    
    //==============
    // Constructors
    //==============
    
    public Expr_Literal(Token<?> start, Object valueIn) {
        super(start);
        literalToken = start;
        value = valueIn;
        
        if (value instanceof String) {
            isInteger = false;
            isDouble = false;
            isBoolean = false;
            isString = true;
            isCharacter = false;
        }
        else if (value instanceof Character) {
            isInteger = false;
            isDouble = false;
            isBoolean = false;
            isString = false;
            isCharacter = true;
        }
        else if (value instanceof Long) {
            isInteger = true;
            isDouble = false;
            isBoolean = false;
            isString = false;
            isCharacter = false;
        }
        else if (value instanceof Double) {
            isInteger = false;
            isDouble = true;
            isBoolean = false;
            isString = false;
            isCharacter = false;
        }
        else if (value instanceof Boolean) {
            isInteger = false;
            isDouble = false;
            isBoolean = true;
            isString = false;
            isCharacter = false;
        }
        else {
            isInteger = false;
            isDouble = false;
            isBoolean = false;
            isString = false;
            isCharacter = false;
        }
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public String toString() {
        String str;
        if (isString) str = "\"" + value + "\"";
        else if (isCharacter) str = "'" + value + "'";
        else str = String.valueOf(value);
        return str;
    }
    
    @Override
    public Expr_Literal copy() {
        return new Expr_Literal(getStartingToken(), value);
    }
    
    @Override
    public EnvisionObject evaluate(ExpressionHandler handler) {
        return handler.handleLiteral_E(this);
    }
    
}
