package envision_lang.parser.statements.statement_types;

import envision_lang.parser.statements.BasicStatement;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;

public class Stmt_Try extends BasicStatement {
	
	public final Statement tryBlock;
	public final EList<Stmt_Catch> catches;
	public final Statement finallyBlock;
	
	public Stmt_Try(Token<?> start, Statement tryIn, EList<Stmt_Catch> catchesIn, Statement finallyIn) {
		super(start);
		tryBlock = tryIn;
		catches = catchesIn;
		finallyBlock = finallyIn;
	}
	
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
	public ParserDeclaration getDeclaration() {
		return null;
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleTryStatement(this);
	}
	
	@Override
	public Token<?> definingToken() {
		return definingToken;
	}
	
}
