package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.Expression;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;

public class Stmt_If implements Statement {

	public final Expression condition;
	public final Statement thenBranch;
	public final Statement elseBranch;
	
	public Stmt_If(Expression conditionIn, Statement thenIn, Statement elseIn) {
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
