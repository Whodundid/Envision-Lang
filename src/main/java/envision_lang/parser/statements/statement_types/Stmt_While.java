package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.Expression;
import envision_lang.parser.statements.BasicStatement;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;

public class Stmt_While extends BasicStatement {

	public final boolean isDo;
	public final Expression condition;
	public final Statement body;
	
	public Stmt_While(Token start, boolean isDoIn, Expression conditionIn, Statement bodyIn) {
		super(start);
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
	
	@Override
	public Token definingToken() {
		return definingToken;
	}
	
}
