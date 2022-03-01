package envision.parser.statements.statements;

import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.statements.statementUtil.ParserDeclaration;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class CatchStatement implements Statement {
	
	public final Token type;
	public final Token var;
	public final EArrayList<Statement> body;
	
	public CatchStatement(Token typeIn, Token varIn, EArrayList<Statement> bodyIn) {
		type = typeIn;
		var = varIn;
		body = bodyIn;
	}
	
	@Override
	public String toString() {
		String b = (body != null && body.isEmpty()) ? "" : " " + body + " ";
		return "catch (" + type.lexeme + " " + var.lexeme + ") {" + b + "}";
	}
	
	@Override
	public ParserDeclaration getDeclaration() {
		return null;
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleCatchStatement(this);
	}
	
}
