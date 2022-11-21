package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.Expression;
import envision_lang.parser.statements.BasicStatement;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;

public class Stmt_If extends BasicStatement {

	public final Expression condition;
	public final Statement thenBranch;
	public final Statement elseBranch;
	
	public Stmt_If(Token start, Expression conditionIn, Statement thenIn, Statement elseIn) {
		super(start);
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
	
	@Override
	public Token definingToken() {
		return definingToken;
	}
	
}
