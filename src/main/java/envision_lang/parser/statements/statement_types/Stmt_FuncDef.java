package envision_lang.parser.statements.statement_types;

import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.parser.util.StatementParameter;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;

public class Stmt_FuncDef extends ParsedStatement {
	
	//========
	// Fields
	//========
	
	public Token<?> name;
	public final Token<?> operator;
	public final EList<StatementParameter> methodParams;
	public final EList<ParsedStatement> body;
	public final boolean isConstructor;
	public final boolean isOperator;
	
	//==============
	// Constructors
	//==============
	
	public Stmt_FuncDef(Token<?> start,
					    Token<?> nameIn,
						Token<?> operatorIn,
						EList<StatementParameter> paramsIn,
						EList<ParsedStatement> bodyIn,
						ParserDeclaration declarationIn,
						boolean isConstructorIn,
						boolean isOperatorIn)
	{
		super(start, declarationIn);
		name = nameIn;
		operator = operatorIn;
		methodParams = paramsIn;
		body = bodyIn;
		declaration = declarationIn;
		isConstructor = isConstructorIn;
		isOperator = isOperatorIn;
	}
	
	//===========
	// Overrides
	//===========

	@Override
	public String toString() {
		//String c = (isConstructor) ? "Initializer: " : "Method: ";
		String p = (methodParams.isEmpty()) ? "" : methodParams.toString();
		String n = (name == null) ? ((isConstructor) ? "init" : ((isOperator) ? "OPERATOR_" + operator.getLexeme() : " null")) : name.getLexeme();
		String b = (body != null && body.isNotEmpty()) ? " {" + body + "}" : "";
		return declaration + "" + n + "(" + p + ")" + b;
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleMethodStatement(this);
	}
	
	//=========
	// Methods
	//=========
	
	public void debug_printValues() {
		System.out.println("Method Declaration:");
		System.out.println("\tName: " + name);
		System.out.println("\tOperator: " + operator);
		System.out.println("\tParams: " + methodParams);
		System.out.println("\tBody: " + body);
		System.out.println("\tDeclaration: " + declaration);
		System.out.println("\tIs Initializer: " + isConstructor);
		System.out.println("\tIs Operator: " + isOperator);
	}
	
}
