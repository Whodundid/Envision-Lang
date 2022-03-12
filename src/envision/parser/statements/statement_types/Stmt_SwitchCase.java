package envision.parser.statements.statement_types;

import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.util.ParserDeclaration;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class Stmt_SwitchCase implements Statement {
	
	public final Token caseName;
	public final EArrayList<Statement> body;
	public final boolean isDefault;
	
	public Stmt_SwitchCase(Token caseNameIn, EArrayList<Statement> bodyIn, boolean isDefaultIn) {
		//caseName = VarExpression.of(caseNameIn);
		caseName = caseNameIn;
		body = bodyIn;
		isDefault = isDefaultIn;
	}
	
	@Override
	public String toString() {
		String d = (isDefault) ? "default" : "case " + caseName;
		return d + ": " + body;
	}
	
	@Override
	public ParserDeclaration getDeclaration() {
		return null;
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleCaseStatement(this);
	}
	
}
