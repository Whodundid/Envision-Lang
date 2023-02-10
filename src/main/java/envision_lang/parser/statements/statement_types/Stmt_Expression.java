package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.StatementHandler;

public class Stmt_Expression extends ParsedStatement {
	
	//========
	// Fields
	//========
	
	public final ParsedExpression expression;
	
	//==============
	// Constructors
	//==============
	
	public Stmt_Expression(ParsedExpression expressionIn) {
		super(expressionIn.getStartingToken());
		expression = expressionIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		return expression + "";
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleExpressionStatement(this);
	}
	
	@Override
	public Stmt_Expression copy() {
		return new Stmt_Expression(expression.copy());
	}
	
}
