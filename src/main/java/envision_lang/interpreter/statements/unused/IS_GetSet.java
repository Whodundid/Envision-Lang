package envision_lang.interpreter.statements.unused;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.scope.Scope;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.language_errors.error_types.AlreadyDefinedError;
import envision_lang.lang.natives.DataModifier;
import envision_lang.parser.expressions.expression_types.Expr_Var;
import envision_lang.parser.statements.statement_types.Stmt_Return;
import envision_lang.parser.statements.statement_types.unused.Stmt_GetSet;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;
import eutil.debug.Broken;
import eutil.debug.InDevelopment;

@Broken
@InDevelopment
public class IS_GetSet extends AbstractInterpreterExecutor {
	
	public static void run(EnvisionInterpreter in, Stmt_GetSet s) {
		/*
		ParserDeclaration dec = s.declaration;
		boolean get = s.get;
		boolean set = s.set;
		EArrayList<Token> vars = s.vars;
		
		//process variables
		for (Token t : vars) {
			//check that the variable exists
			EnvisionObject theVar = scope().get(t.lexeme);
			if (theVar == null) throw new NullVariableError(t.lexeme);
			
			//try to create getter/setter
			if (get) tryDefine(true, scope(), theVar, dec, "get" + StringUtil.capitalFirst(t.lexeme), t);
			if (set) tryDefine(false, scope(), theVar, dec, "set" + StringUtil.capitalFirst(t.lexeme), t);
		}
		*/
	}
	
	//--------------------------
	// Private Internal Methods
	//--------------------------
	
	private static void tryDefine(EnvisionInterpreter interpreter,
								  boolean get,
								  Scope scope,
								  EnvisionObject theVar,
								  ParserDeclaration dec,
								  String methName,
								  Token var)
	{
		//check if get method for variable already exists
		//format: get + capitalFirst(token)
		EnvisionObject checkExist = interpreter.scope().get(methName);
		if (checkExist != null) throw new AlreadyDefinedError(methName);
		
		//create new getter/setter method and define it on scope
		EnvisionFunction meth = buildMethod(get, theVar, dec, methName, var);
		meth.setScope(interpreter.scope());
		
		interpreter.scope().define(methName, meth);
	}
	
	private static EnvisionFunction buildMethod(boolean isGet, EnvisionObject theVar, ParserDeclaration dec, String methName, Token var) {
		if (isGet) return getCreator(theVar, dec, methName, var);
		return setCreator(theVar, dec, methName, var);
	}
	
	private static EnvisionFunction getCreator(EnvisionObject theVar, ParserDeclaration dec, String methName, Token var) {
		//creating getter method with this format:
		// {vis} {mods} theVarType methName() { return var; }
		EnvisionFunction meth = new EnvisionFunction(theVar.getDatatype(), methName);
		
		//set visibility and modifiers
		//meth.setVisibility(dec.getVisibility());
		for (DataModifier mod : dec.getMods()) {
			meth.setModifier(mod, true);
		}
		
		//'abstract' is going to ruin this implementation, but I really have no idea how to handle inheritance yet..
		
		//add the return statement
		meth.addStatement(new Stmt_Return(var, new Expr_Var(var)));
		
		return meth;
	}
	
	private static EnvisionFunction setCreator(EnvisionObject theVar, ParserDeclaration dec, String methName, Token var) {
		/*
		//creating setter method with this format:
		// {vis} {mods} void methName(theVarType val) { var = val; }
		
		EnvisionFunction meth = new EnvisionFunction(EnvisionDatatype.prim_void(), methName);
		
		//set visibility and modifiers
		meth.setVisibility(dec.getVisibility());
		for (DataModifier mod : dec.getMods()) {
			meth.setModifier(mod, true);
		}
		
		//create method parameters
		ParameterData params = ParameterData.of(new Parameter(theVar.getDatatype().getType(), "val"));
		meth.setParams(params);
		
		//'abstract' is going to ruin this implementation, but I really have no idea how to handle inheritance yet..
		
		//add the assignment statement
		Operator assignOp = Operator.ASSIGN;
		Token assignVal = Token.create("val", var.line);
		AssignExpression assignExpr = new AssignExpression(var, assignOp, new VarExpression(assignVal));
		meth.addStatement(new ExpressionStatement(assignExpr));
		
		return meth;
		*/
		return null;
	}
	
}