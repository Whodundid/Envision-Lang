package envision.parser.expressions.expression_types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import eutil.datatypes.EArrayList;
import eutil.strings.StringUtil;

/** A grouping of multiple expressions to be executed together. */
public class Expr_Compound implements Expression {
	
	public final EArrayList<Expression> expressions;
	
	//--------------
	// Constructors
	//--------------
	
	public Expr_Compound() { this(new EArrayList<Expression>()); }
	public Expr_Compound(EArrayList<Expression> expressionsIn) {
		expressions = expressionsIn;
	}
	
	public Expr_Compound(Expression in) {
		expressions = new EArrayList();
		expressions.add(in);
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public String toString() {
		return StringUtil.toString(expressions, ", ");
	}
	
	@Override
	public Expr_Compound copy() {
		Expr_Compound n = new Expr_Compound();
		for (Expression e : expressions) {
			n.add(e.copy());
		}
		return n;
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		return handler.handleCompound_E(this);
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
	public static Expr_Compound wrap(Expression in) {
		if (in instanceof Expr_Compound) return (Expr_Compound) in;
		else return new Expr_Compound(in);
	}
	
}
