package envision_lang.parser.statements.statement_types;

import envision_lang.parser.statements.BasicStatement;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.parser.util.StatementParameter;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;

public class Stmt_FuncDef extends BasicStatement {
	
	public Token<?> name;
	public final Token<?> operator;
	public final EList<StatementParameter> methodParams;
	public final EList<Statement> body;
	public final ParserDeclaration declaration;
	public final boolean isConstructor;
	public final boolean isOperator;
	
	public Stmt_FuncDef(Token<?> start,
					    Token<?> nameIn,
						Token<?> operatorIn,
						EList<StatementParameter> paramsIn,
						EList<Statement> bodyIn,
						ParserDeclaration declarationIn,
						boolean isConstructorIn,
						boolean isOperatorIn)
	{
		super(start);
		name = nameIn;
		operator = operatorIn;
		methodParams = paramsIn;
		body = bodyIn;
		declaration = declarationIn;
		isConstructor = isConstructorIn;
		isOperator = isOperatorIn;
	}
	
	public void printValues() {
		System.out.println("Method Declaration:");
		System.out.println("\tName: " + name);
		System.out.println("\tOperator: " + operator);
		System.out.println("\tParams: " + methodParams);
		System.out.println("\tBody: " + body);
		System.out.println("\tDeclaration: " + declaration);
		System.out.println("\tIs Initializer: " + isConstructor);
		System.out.println("\tIs Operator: " + isOperator);
	}

	@Override
	public String toString() {
		//String c = (isConstructor) ? "Initializer: " : "Method: ";
		String p = (methodParams.isEmpty()) ? "" : methodParams.toString();
		String n = (name == null) ? ((isConstructor) ? "init" : ((isOperator) ? "OPERATOR_" + operator.getLexeme() : " null")) : name.getLexeme();
		String b = (body != null && body.isNotEmpty()) ? " {" + body + "}" : "";
		return declaration + "" + n + "(" + p + ")" + b;
	}
	
	@Override
	public ParserDeclaration getDeclaration() {
		return declaration;
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleMethodStatement(this);
	}
	
	@Override
	public Token<?> definingToken() {
		return definingToken;
	}
	
}
