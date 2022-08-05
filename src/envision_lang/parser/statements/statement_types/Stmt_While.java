package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.Expression;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;

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
