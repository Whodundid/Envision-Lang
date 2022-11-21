package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.Expression;
import envision_lang.parser.statements.BasicStatement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class Stmt_Return extends BasicStatement {

	/** A condition for which to return on. */
	public final Expression condition;
	/** The value(s) being returned. */
	public final EArrayList<Expression> retVals;
	
	public Stmt_Return(Token start, Expression valueIn) { this(start, null, new EArrayList<>(valueIn)); }
	public Stmt_Return(Token start, Expression conditionIn, Expression valueIn) { this(start, conditionIn, new EArrayList<>(valueIn)); }
	public Stmt_Return(Token start, EArrayList<Expression> retValsIn) { this(start, null, retValsIn); }
	public Stmt_Return(Token start, Expression conditionIn, EArrayList<Expression> retValsIn) {
		super(start);
		condition = conditionIn;
		retVals = new EArrayList<>(retValsIn);
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
	public Stmt_Return copy() {
		Expression cond = (condition != null) ? condition.copy() : null;
		return new Stmt_Return(definingToken.copy(), cond, retVals);
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleReturnStatement(this);
	}
	
	@Override
	public Token definingToken() {
		return definingToken;
	}
	
}
