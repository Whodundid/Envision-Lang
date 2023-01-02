package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;

public class Stmt_Return extends ParsedStatement {

	//========
	// Fields
	//========
	
	/** A condition for which to return on. */
	public final ParsedExpression condition;
	/** The value(s) being returned. */
	public final EList<ParsedExpression> retVals = EList.newList();
	
	//==============
	// Constructors
	//==============
	
	public Stmt_Return(Token start, ParsedExpression retValIn) { this(start, null, retValIn); }
	public Stmt_Return(Token start, ParsedExpression conditionIn, ParsedExpression retValIn) {
		super(start);
		condition = conditionIn;
		retVals.addIfNotNull(retValIn);
	}
	
	public Stmt_Return(Token start, EList<ParsedExpression> retValsIn) { this(start, null, retValsIn); }
	public Stmt_Return(Token start, ParsedExpression conditionIn, EList<ParsedExpression> retValsIn) {
		super(start);
		condition = conditionIn;
		if (retValsIn != null) retVals.addAll(retValsIn);
	}
	
	//===========
	// Overrides
	//===========
	
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
	public Stmt_Return copy() {
		ParsedExpression cond = (condition != null) ? condition.copy() : null;
		return new Stmt_Return(getStartingToken().copy(), cond, retVals);
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleReturnStatement(this);
	}
	
}
