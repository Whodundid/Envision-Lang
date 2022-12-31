package envision_lang.parser.statements.statement_types;

import envision_lang.parser.statements.BasicStatement;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.tokenizer.Token;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

public class Stmt_Block extends BasicStatement {

	public final EList<Statement> statements;
	
	public Stmt_Block(Token<?> start) { this(start, new EArrayList<>()); }
	public Stmt_Block(Token<?> start, EList<Statement> in) {
		super(start);
		statements = in;
	}
	
	public Stmt_Block addStatement(Statement in) {
		statements.add(in);
		return this;
	}
	
	@Override
	public String toString() {
		String b = "{";
		if (statements.isNotEmpty()) { b += " "; }
		for (int i = 0; i < statements.size(); i++) {
			Statement s = statements.get(i);
			b += s + ((i < statements.size() - 1) ? " " : "");
		}
		return b + ((statements.isEmpty()) ? "" : " ") + "}";
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleBlockStatement(this);
	}
	
	@Override
	public Token<?> definingToken() {
		return definingToken;
	}
	
}
