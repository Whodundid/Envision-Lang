package envision.parser.statements.statements;

import envision.parser.expressions.Expression;
import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.statements.statementUtil.ParserDeclaration;

public class ExpressionStatement implements Statement {
	
	public final Expression expression;
	
	public ExpressionStatement(Expression expressionIn) {
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
	public ExpressionStatement copy() {
		return new ExpressionStatement(expression.copy());
	}
	
}
