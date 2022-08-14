package envision_lang.parser.statements.statement_types;

import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;
import eutil.datatypes.EList;

public class Stmt_Package implements Statement {
	
	public final ParserDeclaration declaration;
	public final Token name;
	public final EList<Statement> body;
	
	public Stmt_Package(ParserDeclaration declarationIn, Token nameIn, EList<Statement> bodyIn) {
		declaration = declarationIn;
		name = nameIn;
		body = bodyIn;
	}
	
	@Override
	public String toString() {
		String b = (body != null && body.isEmpty()) ? "" : " " + body + " ";
		return declaration + "package " + name.lexeme + " {" + b + "}";
	}
	
	@Override
	public ParserDeclaration getDeclaration() {
		return declaration;
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handlePackageStatement(this);
	}
	
}
