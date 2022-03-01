package envision.interpreter.statements;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.FunctionCreator;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.lang.objects.EnvisionFunction;
import envision.parser.statements.statements.MethodDeclarationStatement;

public class IS_MethodDeclaration extends StatementExecutor<MethodDeclarationStatement> {

	public IS_MethodDeclaration(EnvisionInterpreter in) {
		super(in);
	}
	
	public static void run(EnvisionInterpreter in, MethodDeclarationStatement s) {
		new IS_MethodDeclaration(in).run(s);
	}
	
	//----------------------------------------------------------------------------------
	
	@Override
	public void run(MethodDeclarationStatement s) {
		//build the method against the current scope from the given declaration
		EnvisionFunction m = FunctionCreator.buildMethod(interpreter, s, scope());
		//attempt to find any already existing base method within the given scope
		EnvisionFunction base = FunctionCreator.getBaseMethod(s.name, s.operator, scope());
		
		//if there is a base method, try to add the newly built method as an overload
		if (base != null) { base.addOverload(m.getParams(), s.body); }
		//if there was not a base method, define the newly built method as is
		else {
			String n = (s.name != null) ? s.name.lexeme : "OP(" + s.operator.lexeme + ")";
			scope().define(n, m);
		}
	}
	
}