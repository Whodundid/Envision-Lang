package envision.parser.statements.statement_types;

import envision.parser.expressions.Expression;
import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.util.ParserDeclaration;

public class Stmt_While implements Statement {

	public final boolean isDo;
	public final Expression condition;
	public final Statement body;
	
	public Stmt_While(boolean isDoIn, Expression conditionIn, Statement bodyIn) {
		isDo = isDoIn;
		condition = conditionIn;
		body = bodyIn;
	}
	
	@Override
	public String toString() {
		String n = (isDo) ? "Do While" : "While";
		String b = (body != null) ? " " + body.toString() + " " : "";
		return n + " (" + condition + ") {" + b + "}";
	}
	
	@Override
	public ParserDeclaration getDeclaration() {
		return null;
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleWhileStatement(this);
	}
	
}
