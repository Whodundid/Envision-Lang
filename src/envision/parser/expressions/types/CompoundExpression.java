package envision.parser.expressions.types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import eutil.datatypes.EArrayList;
import eutil.strings.StringUtil;

/** A grouping of multiple expressions to be executed together. */
public class CompoundExpression implements Expression {
	
	public final EArrayList<Expression> expressions;
	
	//--------------
	// Constructors
	//--------------
	
	public CompoundExpression() { this(new EArrayList<Expression>()); }
	public CompoundExpression(EArrayList<Expression> expressionsIn) {
		expressions = expressionsIn;
	}
	
	public CompoundExpression(Expression in) {
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
	public CompoundExpression copy() {
		CompoundExpression n = new CompoundExpression();
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
	
	public CompoundExpression add(Expression in) {
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
	public static CompoundExpression wrap(Expression in) {
		if (in instanceof CompoundExpression) return (CompoundExpression) in;
		else return new CompoundExpression(in);
	}
	
}
