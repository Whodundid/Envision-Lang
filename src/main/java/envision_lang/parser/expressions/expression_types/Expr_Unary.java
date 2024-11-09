package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.tokenizer.Operator;
import envision_lang.tokenizer.Token;

public class Expr_Unary extends ParsedExpression {
    
    //========
    // Fields
    //========
    
    public final Operator operator;
    public final ParsedExpression left, right;    
    //==============
    // Constructors
    //==============
    
    public Expr_Unary(Token<?> start, Operator operatorIn, ParsedExpression leftIn, ParsedExpression rightIn) {
        super(start);
        operator = operatorIn;
        left = leftIn;
        right = rightIn;
    }    
    //===========
    // Overrides
    //===========
    
    @Override
    public String toString() {
        String r = (left != null) ? operator.operatorString + "(" + left + ")" : "(" + right + ")" + operator.operatorString;
        return r;
    }
    
    @Override
    public Expr_Unary copy() {
        ParsedExpression l = (left != null) ? left.copy() : null;
        ParsedExpression r = (right != null) ? right.copy() : null;
        return new Expr_Unary(getStartingToken(), operator, l, r);
    }
    
    @Override
    public EnvisionObject evaluate(ExpressionHandler handler) {
        return handler.handleUnary_E(this);
    }
    
}
