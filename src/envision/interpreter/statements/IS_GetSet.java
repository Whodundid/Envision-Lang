package envision.interpreter.statements;

import envision.exceptions.errors.DuplicateObjectError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.interpreter.util.scope.Scope;
import envision.lang.EnvisionObject;
import envision.lang.objects.EnvisionFunction;
import envision.lang.util.data.DataModifier;
import envision.parser.expressions.expression_types.VarExpression;
import envision.parser.statements.statement_types.GetSetStatement;
import envision.parser.statements.statement_types.ReturnStatement;
import envision.parser.util.ParserDeclaration;
import envision.tokenizer.Token;

public class IS_GetSet extends StatementExecutor<GetSetStatement> {

	public IS_GetSet(EnvisionInterpreter in) {
		super(in);
	}

	public static void run(EnvisionInterpreter in, GetSetStatement s) {
		new IS_GetSet(in).run(s);
	}
	
	//----------------------------------------------------------------------
	
	@Override
	public void run(GetSetStatement s) {
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
		if (checkExist != null) throw new DuplicateObjectError(methName);
		
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
		meth.setVisibility(dec.getVisibility());
		for (DataModifier mod : dec.getMods()) {
			meth.setModifier(mod, true);
		}
		
		//'abstract' is going to ruin this implementation, but I really have no idea how to handle inheritance yet..
		
		//add the return statement
		meth.addStatement(new ReturnStatement(new VarExpression(var)));
		
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