package envision_lang.parser.statements.statement_types;

import envision_lang.parser.statements.BasicStatement;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.parser.util.StatementParameter;
import envision_lang.tokenizer.Token;
import eutil.datatypes.BoxList;
import eutil.datatypes.EArrayList;

public class Stmt_ModularFuncDef extends BasicStatement {
	
	public final Token name;
	public final BoxList<Token, Token> associations;
	public final EArrayList<StatementParameter> methodParams;
	public final EArrayList<Statement> body;
	public final ParserDeclaration declaration;
	
	public Stmt_ModularFuncDef(Token start, 
							   Token nameIn,
						   	   BoxList<Token, Token> associationsIn,
						   	   EArrayList<StatementParameter> paramsIn,
						   	   EArrayList<Statement> bodyIn,
						   	   ParserDeclaration declarationIn)
	{
		super(start);
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
	
	@Override
	public Token definingToken() {
		return definingToken;
	}
	
}
