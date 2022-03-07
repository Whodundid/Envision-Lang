package envision.parser.statements.statement_types;

import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.util.ParserDeclaration;
import eutil.datatypes.EArrayList;

public class TryStatement implements Statement {
	
	public final Statement tryBlock;
	public final EArrayList<CatchStatement> catches;
	public final Statement finallyBlock;
	
	public TryStatement(Statement tryIn, EArrayList<CatchStatement> catchesIn, Statement finallyIn) {
		tryBlock = tryIn;
		catches = catchesIn;
		finallyBlock = finallyIn;
	}
	
	@Override
	public String toString() {
		String c = "";
		for (CatchStatement s : catches) {
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
	
}
