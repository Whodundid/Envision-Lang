package envision_lang.parser.statements.statement_types;

import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.parser.util.StatementParameter;
import envision_lang.tokenizer.Token;
import eutil.datatypes.BoxList;
import eutil.datatypes.EList;

public class Stmt_ModularFuncDef implements Statement {
	
	public final Token name;
	public final BoxList<Token, Token> associations;
	public final EList<StatementParameter> methodParams;
	public final EList<Statement> body;
	public final ParserDeclaration declaration;
	
	public Stmt_ModularFuncDef(Token nameIn,
						   BoxList<Token, Token> associationsIn,
						   EList<StatementParameter> paramsIn,
						   EList<Statement> bodyIn,
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
