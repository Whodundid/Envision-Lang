package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.expression_types.Expr_Import;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.tokenizer.Token;

public class Stmt_Import extends ParsedStatement {

	//========
	// Fields
	//========
	
	public final Expr_Import imp;
	public final Token<?> asName;
	public final boolean importAll;
	
	//==============
	// Constructors
	//==============
	
	public Stmt_Import(Token<?> start, Expr_Import nameIn, Token<?> asNameIn, boolean all) {
		super(start);
		imp = nameIn;
		asName = asNameIn;
		importAll = all;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		String a = (asName != null) ? " as " + asName.getLexeme() : "";
		return "import " + imp + a;
	}

	@Override
	public void execute(StatementHandler handler) {
		handler.handleImportStatement(this);
	}
	
}
