package envision_lang.interpreter.statements;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.creationUtil.FunctionCreator;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.parser.statements.statement_types.Stmt_FuncDef;

public class IS_FuncDef extends AbstractInterpreterExecutor {
	
	public static void run(EnvisionInterpreter interpreter, Stmt_FuncDef s) {
		//build the method against the current scope from the given declaration
		EnvisionFunction m = FunctionCreator.buildFunction(interpreter, s, interpreter.scope());
		//attempt to find any already existing base method within the given scope
		EnvisionFunction base = FunctionCreator.getBaseFunction(s.name, s.operator, interpreter.scope());
		
		//if there is a base method, try to add the newly built method as an overload
		if (base != null) base.addOverload(m.getParams(), s.body);
		//if there was not a base method, define the newly built method as is
		else {
			String n = (s.name != null) ? s.name.getLexeme() : "OP(" + s.operator.getLexeme() + ")";
			interpreter.scope().define(n, m);
		}
	}
	
}