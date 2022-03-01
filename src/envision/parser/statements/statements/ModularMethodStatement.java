package envision.parser.statements.statements;

import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.statements.statementUtil.ParserDeclaration;
import envision.parser.statements.statementUtil.StatementParameter;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.BoxList;

public class ModularMethodStatement implements Statement {
	
	public final Token name;
	public final BoxList<Token, Token> associations;
	public final EArrayList<StatementParameter> methodParams;
	public final EArrayList<Statement> body;
	public final ParserDeclaration declaration;
	
	public ModularMethodStatement(Token nameIn,
						   BoxList<Token, Token> associationsIn,
						   EArrayList<StatementParameter> paramsIn,
						   EArrayList<Statement> bodyIn,
						   ParserDeclaration declarationIn) {
		name = nameIn;
		associations = associationsIn;
		methodParams = paramsIn;
		body = bodyIn;
		declaration = declarationIn;
	}
	
	public void printValues() {
		System.out.println("Method Declaration:");
		System.out.println("\tName: " + name);
		System.out.println("\tAssociations: " + associations);
		System.out.println("\tParams: " + methodParams);
		System.out.println("\tBody: " + body);
		System.out.println("\tDeclaration: " + declaration);
	}

	@Override
	public String toString() {
		String p = (methodParams.isEmpty()) ? "" : methodParams.toString();
		String n = (name != null) ? name.lexeme : "";
		String b = (body != null && body.isNotEmpty()) ? " { " + body + " }" : "";
		return declaration + " " + n + "(" + p + ")" + b;
	}
	
	@Override
	public ParserDeclaration getDeclaration() {
		return declaration;
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleModularMethodStatement(this);
	}
	
}
