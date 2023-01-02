package envision_lang.parser.statements.statement_types;

import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;

public class Stmt_SwitchCase extends ParsedStatement {
	
	//========
	// Fields
	//========
	
	public final Token<?> caseName;
	public final EList<ParsedStatement> body;
	public final boolean isDefault;
	
	//==============
	// Constructors
	//==============
	
	public Stmt_SwitchCase(Token<?> start, Token<?> caseNameIn, EList<ParsedStatement> bodyIn, boolean isDefaultIn) {
		super(start);
		caseName = caseNameIn;
		body = bodyIn;
		isDefault = isDefaultIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		String d = (isDefault) ? "default" : "case " + caseName;
		return d + ": " + body;
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleCaseStatement(this);
	}
	
}
