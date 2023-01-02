package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;
import eutil.strings.EStringUtil;

/** A grouping of multiple expressions to be executed together. */
public class Expr_Compound extends ParsedExpression {
	
	//========
	// Fields
	//========
	
	public final EList<ParsedExpression> expressions;
	
	//==============
	// Constructors
	//==============
	
	public Expr_Compound(Token<?> start) { this(start, (EList<ParsedExpression>) null); }
	public Expr_Compound(Token<?> start, EList<ParsedExpression> expressionsIn) {
		super(start);
		expressions = EList.of(expressionsIn);
	}
	
	public Expr_Compound(Token start, ParsedExpression in) {
		super(start);
		expressions = EList.of(in);
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
		var n = new Expr_Compound(getStartingToken());
		for (var e : expressions) {
			n.add(e.copy());
		}
		return n;
	}
	
	@Override
	public EnvisionObject evaluate(ExpressionHandler handler) {
		return handler.handleCompound_E(this);
	}
	
	//=========
	// Methods
	//=========
	
	public void add(ParsedExpression in) {
		expressions.add(in);
	}
	
	public boolean isEmpty() { return expressions.isEmpty(); }
	public boolean hasOne() { return expressions.hasOne(); }
	public int size() { return expressions.size(); }
	public ParsedExpression getFirst() { return expressions.get(0); }
	
	//================
	// Static Methods
	//================
	
	/** Returns the expression wrapped inside of a compound expression.
	 *  If the expression already was a CompoundExpression, the expression
	 *  is simply returned as is. */
	public static Expr_Compound wrap(Token<?> definingToken, ParsedExpression in) {
		if (in instanceof Expr_Compound) return (Expr_Compound) in;
		else return new Expr_Compound(definingToken, in);
	}
	
}
