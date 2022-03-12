package envision.parser.statements.statement_types;

import envision.parser.expressions.expression_types.Expr_Import;
import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.util.ParserDeclaration;
import envision.tokenizer.Token;

public class Stmt_Import implements Statement {

	public final Expr_Import imp;
	public final Token asName;
	
	public Stmt_Import(Expr_Import nameIn, Token asNameIn) {
		imp = nameIn;
		asName = asNameIn;
	}
	
	@Override
	public String toString() {
		String a = (asName != null) ? " as " + asName.lexeme : "";
		return "import " + imp + a;
	}
	
	@Override public ParserDeclaration getDeclaration() { return null; }

	@Override
	public void execute(StatementHandler handler) {
		handler.handleImportStatement(this);
	}
	
}
