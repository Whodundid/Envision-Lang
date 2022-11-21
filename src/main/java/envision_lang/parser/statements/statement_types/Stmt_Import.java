package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.expression_types.Expr_Import;
import envision_lang.parser.statements.BasicStatement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;

public class Stmt_Import extends BasicStatement {

	public final Expr_Import imp;
	public final Token asName;
	public final boolean importAll;
	
	public Stmt_Import(Token start, Expr_Import nameIn, Token asNameIn, boolean all) {
		super(start);
		imp = nameIn;
		asName = asNameIn;
		importAll = all;
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
	
	@Override
	public Token definingToken() {
		return definingToken;
	}
	
}
