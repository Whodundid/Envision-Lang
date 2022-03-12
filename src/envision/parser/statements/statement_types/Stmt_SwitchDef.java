package envision.parser.statements.statement_types;

import envision.parser.expressions.Expression;
import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.util.ParserDeclaration;
import eutil.datatypes.EArrayList;

public class Stmt_SwitchDef implements Statement {
	
	public final Expression expression;
	public final EArrayList<Stmt_SwitchCase> cases;
	public final Stmt_SwitchCase defaultCase;
	
	public Stmt_SwitchDef(Expression expressionIn, EArrayList<Stmt_SwitchCase> casesIn, Stmt_SwitchCase defaultCaseIn) {
		expression = expressionIn;
		cases = casesIn;
		defaultCase = defaultCaseIn;
	}
	
	@Override
	public String toString() {
		return "Switch: (" + expression + ") { " + cases + " }";
	}
	
	@Override
	public ParserDeclaration getDeclaration() {
		return null;
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleSwitchStatement(this);
	}
	
}
