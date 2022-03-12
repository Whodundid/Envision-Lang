package envision.parser.statements.statement_types;

import envision.parser.expressions.Expression;
import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.util.ParserDeclaration;

public class Stmt_Expression implements Statement {
	
	public final Expression expression;
	
	public Stmt_Expression(Expression expressionIn) {
		expression = expressionIn;
	}
	
	@Override
	public String toString() {
		return expression + "";
	}
	
	@Override
	public ParserDeclaration getDeclaration() {
		return null;
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
