package envision.parser.statements.statements;

import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.statements.statementUtil.ParserDeclaration;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class PackageStatement implements Statement {
	
	public final ParserDeclaration declaration;
	public final Token name;
	public final EArrayList<Statement> body;
	
	public PackageStatement(ParserDeclaration declarationIn, Token nameIn, EArrayList<Statement> bodyIn) {
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
