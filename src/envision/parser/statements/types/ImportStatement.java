package envision.parser.statements.types;

import envision.parser.expressions.types.ImportExpression;
import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.statements.statementUtil.ParserDeclaration;
import envision.tokenizer.Token;

public class ImportStatement implements Statement {

	public final ImportExpression imp;
	public final Token asName;
	
	public ImportStatement(ImportExpression nameIn, Token asNameIn) {
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
