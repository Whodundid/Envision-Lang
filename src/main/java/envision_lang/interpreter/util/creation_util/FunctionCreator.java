package envision_lang.interpreter.util.creation_util;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.scope.IScope;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.functions.EnvisionFunctionClass;
import envision_lang.lang.natives.EnvisionParameter;
import envision_lang.lang.natives.EnvisionStaticTypes;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.ParameterData;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.statements.statement_types.Stmt_FuncDef;
import envision_lang.parser.util.StatementParameter;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;

/** Utility class designed to help with function creation and overloading. */
public class FunctionCreator {
	
	//hide the constructor
	private FunctionCreator() {}
	
	//----------------------------------------------------------------------
	
	/**
	 * Returns a new EnvisionMethod built from the given function declaration
	 * statement with the given scope as its base of reference.
	 */
	public static EnvisionFunction buildFunction(EnvisionInterpreter in, Stmt_FuncDef s, IScope scopeIn) {
		
		//---------------------------------------------------------
		
		var typeMan = in.getTypeManager();
		boolean isConstructor = s.isConstructor;
		boolean isOperator = s.isOperator;
		IDatatype function_return_datatype = null;
		
		//constructors don't have return types
		if (isConstructor) function_return_datatype = null;
		else {
			var dec_return_type = s.getDeclaration().getReturnType();
			//wrap the return type if not null
			if (dec_return_type != null) function_return_datatype = typeMan.getOrCreateDatatypeFor(dec_return_type);
			//otherwise, assign var as return type
			else function_return_datatype = EnvisionStaticTypes.VAR_TYPE;
		}
		
		
		//---------------------------------------------------------
		
		
		//build the parameters
		ParameterData data = buildParameters(in, s.methodParams);
		//the function being created
		EnvisionFunction f = null;
		
		//create operator overload function if operator
		if (isOperator) 		f = new EnvisionFunction(s.operator.getKeyword().asOperator(), data);
		else if (isConstructor) f = new EnvisionFunction(data);
		else 					f = new EnvisionFunction(function_return_datatype, s.name.getLexeme(), data);
		
		
		//---------------------------------------------------------
		
		
		f.setScope(scopeIn);
		f.setVisibility(s.getDeclaration().getVisibility());
		
		var mods = s.getDeclaration().getMods();
		int modSize = mods.size();
		for (int i = 0; i < modSize; i++) {
			f.setModifier(mods.get(i), true);
		}
		
		if (s.body != null) f.setBody(s.body);
		
		EnvisionFunctionClass.FUNC_CLASS.defineFunctionScopeMembers(f);
		
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
	public static ParameterData buildParameters(EnvisionInterpreter in, EList<StatementParameter> params) {
	    var typeMan = in.getTypeManager();
		int size = params.size();
		var builtParams = new EnvisionParameter[size];
		
		for (int i = 0; i < size; i++) {
			var p = params.get(i);
			
			Token<?> name = p.name;
			Token<?> type = p.type;
			
			String theName = name.getLexeme();
			IDatatype theType = (type != null) ? typeMan.getOrCreateDatatypeFor(type) : EnvisionStaticTypes.VAR_TYPE;
			
			ParsedExpression assign = p.assignment;
			
			EnvisionParameter newParam = null;
			if (assign != null) newParam = new EnvisionParameter(theType, theName, in.evaluate(assign));
			else newParam = new EnvisionParameter(theType, theName);
			
			builtParams[i] = newParam;
		}
		
		return ParameterData.from(builtParams);
	}
	
	/** Attempts to find a method of the same name within the given scope. */
	public static EnvisionFunction getBaseFunction(Token<?> name, Token<?> operator, IScope scopeIn) {
		String n = (name != null) ? name.getLexeme() : "OP(" + operator.getLexeme() + ")";
		
		//placeholder variable check
		EnvisionObject o = scopeIn.getLocal(n);
		
		//check if a function with that name is already defined
		if (o instanceof EnvisionFunction func) return func;
		
		return null;
	}
	
}
