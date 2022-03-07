package envision.parser.statements.statement_types;

import envision.parser.expressions.Expression;
import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.util.ParserDeclaration;

public class IfStatement implements Statement {

	public final Expression condition;
	public final Statement thenBranch;
	public final Statement elseBranch;
	
	public IfStatement(Expression conditionIn, Statement thenIn, Statement elseIn) {
		condition = conditionIn;
		thenBranch = thenIn;
		elseBranch = elseIn;
	}
	
	@Override
	public String toString() {
		String e = (elseBranch != null) ? " else { " + elseBranch + " }" : "";
		return "if (" + condition + ") { " + thenBranch + " }" + e;
	}
	
	@Override
	public ParserDeclaration getDeclaration() {
		return null;
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleIfStatement(this);
	}
	
}
