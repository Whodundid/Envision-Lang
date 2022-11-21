package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.Token;
import eutil.datatypes.EArrayList;
import eutil.strings.EStringUtil;

/** A grouping of multiple expressions to be executed together. */
public class Expr_Compound implements Expression {
	
	public final EArrayList<Expression> expressions;
	public final Token definingToken;
	
	//--------------
	// Constructors
	//--------------
	
	public Expr_Compound(Token start) { this(start, new EArrayList<Expression>()); }
	public Expr_Compound(Token start, EArrayList<Expression> expressionsIn) {
		expressions = expressionsIn;
		definingToken = start;
	}
	
	public Expr_Compound(Token start, Expression in) {
		expressions = new EArrayList();
		expressions.add(in);
		definingToken = start;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public String toString() {
		return EStringUtil.toString(expressions, ", ");
	}
	
	@Override
	public Expr_Compound copy() {
		Expr_Compound n = new Expr_Compound(definingToken.copy());
		for (Expression e : expressions) {
			n.add(e.copy());
		}
		return n;
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleCompound_E(this);
	}
	
	@Override
	public Token definingToken() {
		return definingToken;
	}
	
	//---------
	// Methods
	//---------
	
	public Expr_Compound add(Expression in) {
		expressions.add(in);
		return this;
	}
	
	public boolean isEmpty() { return expressions.isEmpty(); }
	public boolean hasOne() { return expressions.hasOne(); }
	public int size() { return expressions.size(); }
	public Expression getFirst() { return expressions.get(0); }
	
	//----------------
	// Static Methods
	//----------------
	
	/** Returns the expression wrapped inside of a compound expression.
	 *  If the expression already was a CompoundExpression, the expression
	 *  is simply returned as is. */
	public static Expr_Compound wrap(Token definingToken, Expression in) {
		if (in instanceof Expr_Compound) return (Expr_Compound) in;
		else return new Expr_Compound(definingToken, in);
	}
	
}
