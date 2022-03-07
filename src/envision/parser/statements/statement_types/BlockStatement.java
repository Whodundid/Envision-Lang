package envision.parser.statements.statement_types;

import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import eutil.datatypes.EArrayList;

public class BlockStatement implements Statement {

	public final EArrayList<Statement> statements;
	
	public BlockStatement() { this(new EArrayList<Statement>()); }
	public BlockStatement(EArrayList<Statement> in) {
		statements = in;
	}
	
	public BlockStatement addStatement(Statement in) {
		statements.add(in);
		return this;
	}
	
	@Override
	public String toString() {
		String b = "'BLOCK' {";
		if (statements.isNotEmpty()) { b += " "; }
		for (int i = 0; i < statements.size(); i++) {
			Statement s = statements.get(i);
			b += s + ((i < statements.size() - 1) ? "; " : ";");
		}
		return b + ((statements.isEmpty()) ? "" : " ") + "}";
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleBlockStatement(this);
	}
	
}
