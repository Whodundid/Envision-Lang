package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.tokenizer.Token;

public class Stmt_While extends ParsedStatement {

	//========
	// Fields
	//========
	
	public final boolean isDo;
	public final ParsedExpression condition;
	public final ParsedStatement body;
	
	//==============
	// Constructors
	//==============
	
	public Stmt_While(Token start, boolean isDoIn, ParsedExpression conditionIn, ParsedStatement bodyIn) {
		super(start);
		isDo = isDoIn;
		condition = conditionIn;
		body = bodyIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		String n = (isDo) ? "Do While" : "While";
		String b = (body != null) ? " " + body + " " : "";
		return n + " (" + condition + ") {" + b + "}";
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleWhileStatement(this);
	}
	
}
