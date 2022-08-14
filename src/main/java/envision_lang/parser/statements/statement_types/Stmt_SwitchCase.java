package envision_lang.parser.statements.statement_types;

import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;
import eutil.datatypes.EList;

public class Stmt_SwitchCase implements Statement {
	
	public final Token caseName;
	public final EList<Statement> body;
	public final boolean isDefault;
	
	public Stmt_SwitchCase(Token caseNameIn, EList<Statement> bodyIn, boolean isDefaultIn) {
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
