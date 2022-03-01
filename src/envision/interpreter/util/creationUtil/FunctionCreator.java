package envision.interpreter.util.creationUtil;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.scope.Scope;
import envision.lang.EnvisionObject;
import envision.lang.objects.EnvisionFunction;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.data.Parameter;
import envision.lang.util.data.ParameterData;
import envision.parser.expressions.Expression;
import envision.parser.statements.statementUtil.StatementParameter;
import envision.parser.statements.statements.MethodDeclarationStatement;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

/** Utility class designed to help with method creation and overloading. */
public class FunctionCreator {
	
	//hide the constructor
	private FunctionCreator() {}
	
	//----------------------------------------------------------------------
	
	/** Returns a new EnvisionMethod built from the given method declaration statement with the given scope as its base of reference. */
	public static EnvisionFunction buildMethod(EnvisionInterpreter in, MethodDeclarationStatement s, Scope scopeIn) {
		
		//---------------------------------------------------------
		
		
		boolean isConstructor = s.isConstructor;
		boolean isOperator = s.isOperator;
		EnvisionDatatype function_return_datatype = null;
		
		//constructors don't have return types
		if (isConstructor) function_return_datatype = null;
		else {
			var dec_return_type = s.declaration.getReturnType();
			//wrap the return type if not null
			if (dec_return_type != null) function_return_datatype = new EnvisionDatatype(dec_return_type);
			//otherwise, assign var as retun type
			else function_return_datatype = EnvisionDatatype.prim_var();
		}
		
		
		//---------------------------------------------------------
		
		
		//build the parameters
		ParameterData data = buildParameters(in, s.methodParams);
		//the function being created
		EnvisionFunction m = null;
		
		//create operator overload function if operator
		if (isOperator) 		m = new EnvisionFunction(s.operator.keyword.asOperator(), data);
		else if (isConstructor) m = new EnvisionFunction(data);
		else 					m = new EnvisionFunction(function_return_datatype, s.name.lexeme, data);
		
		
		//---------------------------------------------------------
		
		
		m.setScope(scopeIn);
		m.applyDeclaration(s.declaration);
		if (s.body != null) m.setBody(s.body);
		
		
		return m;
	}
	
	/** Returns a new EnvisionMethod built from the given method declaration statement with the given scope as its base of reference. */
	/*
	public static EnvisionFunction buildMethod(EnvisionInterpreter in, ModularMethodStatement s, String modularName, EArrayList<Statement> modularBody, Scope scopeIn) {
		String returnType = s.declaration.getReturnType().lexeme;
		
		//build the parameters
		ParameterData data = buildParameters(in, s.methodParams);
		EnvisionFunction m = new EnvisionFunction(returnType, modularName, data);
		
		m.setScope(scopeIn);
		m.applyDeclaration(s.declaration);
		if (modularBody != null) m.setBody(modularBody);
		
		//if the method declares a return type, ensure it actually returns something -- this is a complete half assed approach..
		/*
		if (!(EnvisionDataType.VOID.type).equals(m.getReturnType())) {
			boolean hasRet = false;
			for (Statement stmt : modularBody) {
				if (stmt instanceof ReturnStatement) {
					hasRet = true;
					break;
				}
			}
			if (!hasRet) throw new NoReturnStatementError(m);
		}
		
		
		return m;
	}
	*/
	
	/** Builds the method parameter data from the given method declaration statement. */
	public static ParameterData buildParameters(EnvisionInterpreter in, EArrayList<StatementParameter> params) {
		ParameterData parameterData = new ParameterData();
		
		for (StatementParameter p : params) {
			Token name = p.name;
			Token type = p.type;
			
			String theName = name.lexeme;
			EnvisionDatatype theType = (type != null) ? new EnvisionDatatype(type) : EnvisionDatatype.prim_var();
			
			Expression assign = p.assignment;
			
			Parameter newParam = null;
			if (assign != null) newParam = new Parameter(theType, theName, in.evaluate(assign));
			else newParam = new Parameter(theType, theName);
			
			parameterData.add(newParam);
		}
		
		return parameterData;
	}
	
	/** Attempts to find a method of the same name within the given scope. */
	public static EnvisionFunction getBaseMethod(Token name, Token operator, Scope scopeIn) {
		String n = (name != null) ? name.lexeme : "OP(" + operator.lexeme + ")";
		
		//placeholder variable check
		EnvisionObject o = scopeIn.get(n);
		
		//check if a method with that name is already defined
		if (o instanceof EnvisionFunction) return (EnvisionFunction) o;
		
		return null;
	}
	
}
