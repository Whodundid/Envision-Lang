package envision.parser.statements.statements;

import envision.parser.expressions.Expression;
import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.statements.statementUtil.ParserDeclaration;
import eutil.datatypes.EArrayList;

public class ReturnStatement implements Statement {

	/** A condition for which to return on. */
	public final Expression condition;
	/** The value(s) being returned. */
	public final EArrayList<Expression> retVals;
	
	public ReturnStatement(Expression valueIn) { this(new EArrayList<Expression>(valueIn)); }
	public ReturnStatement(Expression conditionIn, Expression valueIn) { this(conditionIn, new EArrayList<Expression>(valueIn)); }
	public ReturnStatement(EArrayList<Expression> retValsIn) { this(null, retValsIn); }
	public ReturnStatement(Expression conditionIn, EArrayList<Expression> retValsIn) {
		condition = conditionIn;
		retVals = new EArrayList(retValsIn);
	}
	
	@Override
	public String toString() {
		String name = (condition != null) ? "Retif" : "Return: ";
		String cond = (condition != null) ? "(" + condition + "): " : "";
		String vals = "";
		if (retVals.hasOne()) vals += retVals.get(0);
		else {
			vals += "[";
			for (int i = 0; i < retVals.size(); i++) {
				vals += retVals.get(i);
				if (i < retVals.size() - 1) vals += " ";
			}
			vals += "]";
		}
		return name + cond + vals;
	}
	
	@Override
	public ParserDeclaration getDeclaration() {
		return null;
	}
	
	@Override
	public ReturnStatement copy() {
		Expression cond = (condition != null) ? condition.copy() : null;
		return new ReturnStatement(cond, retVals);
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleReturnStatement(this);
	}
	
}
