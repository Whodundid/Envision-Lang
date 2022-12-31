package envision_lang.parser.statements.statement_types;

import envision_lang.parser.statements.BasicStatement;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;

public class Stmt_Package extends BasicStatement {
	
	public final ParserDeclaration declaration;
	public final Token<?> name;
	public final EList<Statement> body;
	
	public Stmt_Package(Token<?> start, ParserDeclaration declarationIn, Token<?> nameIn, EList<Statement> bodyIn) {
		super(start);
		declaration = declarationIn;
		name = nameIn;
		body = bodyIn;
	}
	
	@Override
	public String toString() {
		String b = (body != null && body.isEmpty()) ? "" : " " + body + " ";
		return declaration + "package " + name.getLexeme() + " {" + b + "}";
	}
	
	@Override
	public ParserDeclaration getDeclaration() {
		return declaration;
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handlePackageStatement(this);
	}
	
	@Override
	public Token<?> definingToken() {
		return definingToken;
	}
	
}
