package envision.interpreter.util.creationUtil;

import envision.exceptions.errors.NoReturnStatementError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.Scope;
import envision.lang.EnvisionObject;
import envision.lang.objects.EnvisionMethod;
import envision.lang.util.EnvisionDataType;
import envision.lang.util.data.Parameter;
import envision.lang.util.data.ParameterData;
import envision.parser.expressions.Expression;
import envision.parser.statements.Statement;
import envision.parser.statements.statementUtil.StatementParameter;
import envision.parser.statements.types.MethodDeclarationStatement;
import envision.parser.statements.types.ModularMethodStatement;
import envision.parser.statements.types.ReturnStatement;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

/** Utility class designed to help with method creation and overloading. */
public class MethodCreator {
	
	//hide the constructor
	private MethodCreator() {}
	
	//----------------------------------------------------------------------
	
	/** Returns a new EnvisionMethod built from the given method declaration statement with the given scope as its base of reference. */
	public static EnvisionMethod buildMethod(EnvisionInterpreter in, MethodDeclarationStatement s, Scope scopeIn) {
		boolean isConstructor = s.isConstructor;
		boolean isOperator = s.isOperator;
		Token returnType = (!isConstructor) ? s.declaration.getReturnType() : null;
		String returnTypeString = (returnType != null) ? returnType.lexeme : null;
		
		//build the parameters
		ParameterData data = buildParameters(in, s.methodParams);
		EnvisionMethod m = null;
		
		if (isOperator) {
			m = new EnvisionMethod(s.operator.keyword, data);
		}
		else {
			m = (isConstructor) ? new EnvisionMethod(data) : new EnvisionMethod(returnTypeString, s.name.lexeme, data);
		}
		
		m.setScope(scopeIn);
		m.applyDeclaration(s.declaration);
		if (s.body != null) { m.setBody(s.body); }
		
		//if the method declares a return type, ensure it actually returns something -- this is a complete half assed approach..
		if (!s.isConstructor && !(EnvisionDataType.VOID.type).equals(m.getReturnType())) {
			//boolean hasRet = false;
			//for (Statement stmt : s.body) {
			//	if (stmt instanceof ReturnStatement) { hasRet = true; break; }
			//}
			//if (!hasRet) { throw new NoReturnStatementError(m); }
		}
		
		return m;
	}
	
	/** Returns a new EnvisionMethod built from the given method declaration statement with the given scope as its base of reference. */
	public static EnvisionMethod buildMethod(EnvisionInterpreter in, ModularMethodStatement s, String modularName, EArrayList<Statement> modularBody, Scope scopeIn) {
		String returnType = s.declaration.getReturnType().lexeme;
		
		//build the parameters
		ParameterData data = buildParameters(in, s.methodParams);
		EnvisionMethod m = new EnvisionMethod(returnType, modularName, data);
		
		m.setScope(scopeIn);
		m.applyDeclaration(s.declaration);
		if (modularBody != null) { m.setBody(modularBody); }
		
		//if the method declares a return type, ensure it actually returns something -- this is a complete half assed approach..
		if (!(EnvisionDataType.VOID.type).equals(m.getReturnType())) {
			boolean hasRet = false;
			for (Statement stmt : modularBody) {
				if (stmt instanceof ReturnStatement) { hasRet = true; break; }
			}
			if (!hasRet) { throw new NoReturnStatementError(m); }
		}
		
		return m;
	}
	
	/** Builds the method parameter data from the given method declaration statement. */
	public static ParameterData buildParameters(EnvisionInterpreter in, EArrayList<StatementParameter> params) {
		EArrayList<Parameter> parameters = new EArrayList();
		
		for (StatementParameter p : params) {
			Token name = p.name;
			Token type = p.type;
			
			String theName = name.lexeme;
			String theType = (type != null) ? type.lexeme : "var";
			
			Expression assign = p.assignment;
			
			Parameter newParam = null;
			if (assign != null) { newParam = new Parameter(theType, theName, in.evaluate(assign)); }
			else { newParam = new Parameter(theType, theName); }
			
			parameters.add(newParam);
		}
		
		return new ParameterData(parameters);
	}
	
	/** Attempts to find a method of the same name within the given scope. */
	public static EnvisionMethod getBaseMethod(Token name, Token operator, Scope scopeIn) {
		String n = (name != null) ? name.lexeme : "OP(" + operator.lexeme + ")";
		
		//placeholder variable check
		EnvisionObject o = scopeIn.get(n);
		
		//check if a method with that name is already defined
		if (o instanceof EnvisionMethod) { return (EnvisionMethod) o; }
		
		return null;
	}
	
}
