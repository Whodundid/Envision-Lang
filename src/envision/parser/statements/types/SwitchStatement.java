package envision.parser.statements.types;

import envision.parser.expressions.Expression;
import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.statements.statementUtil.ParserDeclaration;
import eutil.datatypes.EArrayList;

public class SwitchStatement implements Statement {
	
	public final Expression expression;
	public final EArrayList<CaseStatement> cases;
	public final CaseStatement defaultCase;
	
	public SwitchStatement(Expression expressionIn, EArrayList<CaseStatement> casesIn, CaseStatement defaultCaseIn) {
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
