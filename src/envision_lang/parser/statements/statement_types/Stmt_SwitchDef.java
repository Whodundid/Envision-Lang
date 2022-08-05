package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.Expression;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;
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
