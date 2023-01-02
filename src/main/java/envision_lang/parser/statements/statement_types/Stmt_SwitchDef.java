package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;

public class Stmt_SwitchDef extends ParsedStatement {
	
	//========
	// Fields
	//========
	
	public final ParsedExpression expression;
	public final EList<Stmt_SwitchCase> cases;
	public final Stmt_SwitchCase defaultCase;
	
	//==============
	// Constructors
	//==============
	
	public Stmt_SwitchDef(Token start,
						  ParsedExpression expressionIn,
						  EList<Stmt_SwitchCase> casesIn,
						  Stmt_SwitchCase defaultCaseIn)
	{
		super(start);
		expression = expressionIn;
		cases = casesIn;
		defaultCase = defaultCaseIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		return "Switch: (" + expression + ") { " + cases + " }";
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleSwitchStatement(this);
	}
	
}
