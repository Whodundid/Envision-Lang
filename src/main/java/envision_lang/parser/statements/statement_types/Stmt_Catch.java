package envision_lang.parser.statements.statement_types;

import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class Stmt_Catch implements Statement {
	
	public final Token type;
	public final Token var;
	public final EArrayList<Statement> body;
	
	public Stmt_Catch(Token typeIn, Token varIn, EArrayList<Statement> bodyIn) {
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