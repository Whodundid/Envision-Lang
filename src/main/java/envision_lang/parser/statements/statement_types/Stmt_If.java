package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.tokenizer.Token;

public class Stmt_If extends ParsedStatement {

	//========
	// Fields
	//========
	
	public final ParsedExpression condition;
	public final ParsedStatement thenBranch;
	public final ParsedStatement elseBranch;
	
	//==============
	// Constructors
	//==============
	
	public Stmt_If(Token start, ParsedExpression conditionIn, ParsedStatement thenIn, ParsedStatement elseIn) {
		super(start);
		condition = conditionIn;
		thenBranch = thenIn;
		elseBranch = elseIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		String e = (elseBranch != null) ? " else " + elseBranch : "";
		return "if (" + condition + ") " + thenBranch + e;
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleIfStatement(this);
	}
	
}
