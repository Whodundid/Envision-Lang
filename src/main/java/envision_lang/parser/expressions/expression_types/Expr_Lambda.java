package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.parser.expressions.ParsedExpression;

public class Expr_Lambda extends ParsedExpression {
    
    //========
    // Fields
    //========
    
    public ParsedExpression callee;
    public Expr_Compound inputs;
    public Expr_Compound production;
    
    //==============
    // Constructors
    //==============
    
    //	public Expr_Lambda(ParsedExpression calleeIn, ParsedExpression inputsIn, ParsedExpression productionIn) {
    //		super(calleeIn.getStartingToken());
    //		callee = calleeIn;
    //		inputs = Expr_Compound.wrap(inputsIn.getStartingToken(), inputsIn);
    //		production = Expr_Compound.wrap(productionIn.getStartingToken(), productionIn);
    //	}
    
    public Expr_Lambda(ParsedExpression inputsIn) {
        super(inputsIn);
        inputs = Expr_Compound.wrap(inputsIn.getStartingToken(), inputsIn);
    }
    
    public Expr_Lambda(ParsedExpression inputsIn, ParsedExpression productionIn) {
        super(inputsIn.getStartingToken());
        inputs = Expr_Compound.wrap(inputsIn.getStartingToken(), inputsIn);
        production = Expr_Compound.wrap(productionIn.getStartingToken(), productionIn);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public String toString() {
        String is;
        if (inputs == null) is = String.valueOf(inputs);
        else if (inputs.isEmpty() || inputs.size() > 1 || inputs.getFirst() instanceof Expr_Lambda) {
            is = "(" + inputs + ")";
        }
        else is = String.valueOf(inputs.getFirst());
        
        String ps;
        if (production == null) ps = String.valueOf(production);
        else if (production.isEmpty() || production.size() > 1 || production.getFirst() instanceof Expr_Lambda) {
            ps = "(" + production + ")";
        }
        else ps = String.valueOf(production.getFirst());
        
        return is + " -> " + ps;
    }
    
    @Override
    public EnvisionObject evaluate(ExpressionHandler handler) {
        return handler.handleLambda_E(this);
    }
    
    //=========
    // Setters
    //=========
    
    public void setProduction(ParsedExpression productionIn) {
        production = Expr_Compound.wrap(productionIn.getStartingToken(), productionIn);
    }
    
}
