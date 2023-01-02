package envision_lang.parser.statements.statement_types;

import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;

public class Stmt_Try extends ParsedStatement {
	
	//========
	// Fields
	//========
	
	public final ParsedStatement tryBlock;
	public final EList<Stmt_Catch> catches;
	public final ParsedStatement finallyBlock;
	
	//==============
	// Constructors
	//==============
	
	public Stmt_Try(Token<?> start, ParsedStatement tryIn, EList<Stmt_Catch> catchesIn, ParsedStatement finallyIn) {
		super(start);
		tryBlock = tryIn;
		catches = catchesIn;
		finallyBlock = finallyIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		String c = "";
		for (Stmt_Catch s : catches) {
			c += " " + s;
		}
		
		String f = (finallyBlock != null) ? " finally { " + finallyBlock + " }" : "";
		
		return "Try: " + tryBlock + c + f;
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleTryStatement(this);
	}
	
}
