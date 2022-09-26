package envision_lang.interpreter.util.creationUtil;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.scope.Scope;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.NativeTypeManager;
import envision_lang.lang.util.DataModifier;
import envision_lang.lang.util.Parameter;
import envision_lang.lang.util.ParameterData;
import envision_lang.lang.util.StaticTypes;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.statements.statement_types.Stmt_FuncDef;
import envision_lang.parser.util.StatementParameter;
import envision_lang.tokenizer.Token;
import eutil.datatypes.EArrayList;

/** Utility class designed to help with function creation and overloading. */
public class FunctionCreator {
	
	//hide the constructor
	private FunctionCreator() {}
	
	//----------------------------------------------------------------------
	
	/**
	 * Returns a new EnvisionMethod built from the given function declaration
	 * statement with the given scope as its base of reference.
	 */
	public static EnvisionFunction buildFunction(EnvisionInterpreter in, Stmt_FuncDef s, Scope scopeIn) {
		
		//---------------------------------------------------------
		
		
		boolean isConstructor = s.isConstructor;
		boolean isOperator = s.isOperator;
		IDatatype function_return_datatype = null;
		
		//constructors don't have return types
		if (isConstructor) function_return_datatype = null;
		else {
			var dec_return_type = s.declaration.getReturnType();
			//wrap the return type if not null
			if (dec_return_type != null) function_return_datatype = NativeTypeManager.datatypeOf(dec_return_type);
			//otherwise, assign var as return type
			else function_return_datatype = StaticTypes.VAR_TYPE;
		}
		
		
		//---------------------------------------------------------
		
		
		//build the parameters
		ParameterData data = buildParameters(in, s.methodParams);
		//the function being created
		EnvisionFunction f = null;
		
		//create operator overload function if operator
		if (isOperator) 		f = new EnvisionFunction(s.operator.keyword.asOperator(), data);
		else if (isConstructor) f = new EnvisionFunction(data);
		else 					f = new EnvisionFunction(function_return_datatype, s.name.lexeme, data);
		
		
		//---------------------------------------------------------
		
		
		f.setScope(scopeIn);
		f.setVisibility(s.declaration.getVisibility());
		for (DataModifier mod : s.declaration.getMods()) f.setModifier(mod, true);
		if (s.body != null) f.setBody(s.body);
		
		return f;
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
			IDatatype theType = (type != null) ? NativeTypeManager.datatypeOf(type) : StaticTypes.VAR_TYPE;
			
			Expression assign = p.assignment;
			
			Parameter newParam = null;
			if (assign != null) newParam = new Parameter(theType, theName, in.evaluate(assign));
			else newParam = new Parameter(theType, theName);
			
			parameterData.add(newParam);
		}
		
		return parameterData;
	}
	
	/** Attempts to find a method of the same name within the given scope. */
	public static EnvisionFunction getBaseFunction(Token name, Token operator, Scope scopeIn) {
		String n = (name != null) ? name.lexeme : "OP(" + operator.lexeme + ")";
		
		//placeholder variable check
		EnvisionObject o = scopeIn.get(n);
		
		//check if a function with that name is already defined
		if (o instanceof EnvisionFunction) return (EnvisionFunction) o;
		
		return null;
	}
	
}
