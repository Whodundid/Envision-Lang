package envision_lang.parser.statements.statement_types;

import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.StatementHandler;
import eutil.datatypes.EArrayList;

public class Stmt_Block implements Statement {

	public final EArrayList<Statement> statements;
	
	public Stmt_Block() { this(new EArrayList<Statement>()); }
	public Stmt_Block(EArrayList<Statement> in) {
		statements = in;
	}
	
	public Stmt_Block addStatement(Statement in) {
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
