package envision.parser.statements.statement_types;

import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.util.ParserDeclaration;
import envision.parser.util.StatementParameter;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class FuncDefStatement implements Statement {
	
	public Token name;
	public final Token operator;
	public final EArrayList<StatementParameter> methodParams;
	public final EArrayList<Statement> body;
	public final ParserDeclaration declaration;
	public final boolean isConstructor;
	public final boolean isOperator;
	
	public FuncDefStatement(Token nameIn,
						   Token operatorIn,
						   EArrayList<StatementParameter> paramsIn,
						   EArrayList<Statement> bodyIn,
						   ParserDeclaration declarationIn,
						   boolean isConstructorIn,
						   boolean isOperatorIn) {
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
		String n = (name == null) ? ((isConstructor) ? "init" : ((isOperator) ? "OPERATOR_" + operator.lexeme : " null")) : name.lexeme;
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
	
}
