package envision.parser.statements.statement_types;

import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.util.ParserDeclaration;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class Stmt_Package implements Statement {
	
	public final ParserDeclaration declaration;
	public final Token name;
	public final EArrayList<Statement> body;
	
	public Stmt_Package(ParserDeclaration declarationIn, Token nameIn, EArrayList<Statement> bodyIn) {
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