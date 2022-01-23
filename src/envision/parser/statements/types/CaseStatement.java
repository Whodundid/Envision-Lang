package envision.parser.statements.types;

import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.statements.statementUtil.ParserDeclaration;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class CaseStatement implements Statement {
	
	public final Token caseName;
	public final EArrayList<Statement> body;
	public final boolean isDefault;
	
	public CaseStatement(Token caseNameIn, EArrayList<Statement> bodyIn, boolean isDefaultIn) {
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
