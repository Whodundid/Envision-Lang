package envision_lang.interpreter.statements;

import envision_lang.exceptions.errors.AlreadyDefinedError;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.interpreterBase.StatementExecutor;
import envision_lang.interpreter.util.scope.Scope;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.lang.util.DataModifier;
import envision_lang.parser.expressions.expression_types.Expr_Var;
import envision_lang.parser.statements.statement_types.Stmt_GetSet;
import envision_lang.parser.statements.statement_types.Stmt_Return;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;
import eutil.debug.Broken;
import eutil.debug.InDevelopment;

@Broken
@InDevelopment
public class IS_GetSet extends StatementExecutor<Stmt_GetSet> {

	public IS_GetSet(EnvisionInterpreter in) {
		super(in);
	}

	public static void run(EnvisionInterpreter in, Stmt_GetSet s) {
		new IS_GetSet(in).run(s);
	}
	
	//----------------------------------------------------------------------
	
	@Override
	public void run(Stmt_GetSet s) {
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
	
	private void tryDefine(boolean get, Scope scope, EnvisionObject theVar, ParserDeclaration dec, String methName, Token var) {
		//check if get method for variable already exists
		//format: get + capitalFirst(token)
		EnvisionObject checkExist = scope().get(methName);
		if (checkExist != null) throw new AlreadyDefinedError(methName);
		
		//create new getter/setter method and define it on scope
		EnvisionFunction meth = buildMethod(get, theVar, dec, methName, var);
		meth.setScope(scope());
		
		scope().define(methName, meth);
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
		meth.addStatement(new Stmt_Return(new Expr_Var(var)));
		
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