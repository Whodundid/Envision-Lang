package envision.interpreter.statements;

import envision.exceptions.errors.DuplicateObjectError;
import envision.exceptions.errors.NullVariableError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.Scope;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.lang.EnvisionObject;
import envision.lang.objects.EnvisionMethod;
import envision.lang.util.EnvisionDataType;
import envision.lang.util.data.DataModifier;
import envision.lang.util.data.Parameter;
import envision.lang.util.data.ParameterData;
import envision.parser.expressions.types.AssignExpression;
import envision.parser.expressions.types.VarExpression;
import envision.parser.statements.statementUtil.ParserDeclaration;
import envision.parser.statements.types.ExpressionStatement;
import envision.parser.statements.types.GetSetStatement;
import envision.parser.statements.types.ReturnStatement;
import envision.tokenizer.Keyword;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;
import eutil.strings.StringUtil;

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
		EnvisionMethod meth = buildMethod(get, theVar, dec, methName, var);
		meth.setScope(scope());
		
		scope().define(methName, meth);
	}
	
	private static EnvisionMethod buildMethod(boolean isGet, EnvisionObject theVar, ParserDeclaration dec, String methName, Token var) {
		if (isGet) return getCreator(theVar, dec, methName, var);
		return setCreator(theVar, dec, methName, var);
	}
	
	private static EnvisionMethod getCreator(EnvisionObject theVar, ParserDeclaration dec, String methName, Token var) {
		//creating getter method with this format:
		// {vis} {mods} theVarType methName() { return var; }
		EnvisionMethod meth = new EnvisionMethod(theVar.getInternalType(), methName);
		
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
	
	private static EnvisionMethod setCreator(EnvisionObject theVar, ParserDeclaration dec, String methName, Token var) {
		//creating setter method with this format:
		// {vis} {mods} void methName(theVarType val) { var = val; }
		
		EnvisionMethod meth = new EnvisionMethod(EnvisionDataType.VOID, methName);
		
		//set visibility and modifiers
		meth.setVisibility(dec.getVisibility());
		for (DataModifier mod : dec.getMods()) {
			meth.setModifier(mod, true);
		}
		
		//create method parameters
		ParameterData params = ParameterData.of(new Parameter(theVar.getInternalType().type, "val"));
		meth.setParams(params);
		
		//'abstract' is going to ruin this implementation, but I really have no idea how to handle inheritance yet..
		
		//add the assignment statement
		Token assignOp = Token.create(Keyword.ASSIGN, var.line);
		Token assignVal = Token.create("val", var.line);
		AssignExpression assignExpr = new AssignExpression(var, assignOp, new VarExpression(assignVal));
		meth.addStatement(new ExpressionStatement(assignExpr));
		
		return meth;
	}
	
}