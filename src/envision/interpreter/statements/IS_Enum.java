package envision.interpreter.statements;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.parser.statements.statement_types.Stmt_EnumDef;

public class IS_Enum extends StatementExecutor<Stmt_EnumDef> {

	public IS_Enum(EnvisionInterpreter in) {
		super(in);
	}

	// this needs a lot of work..
	
	@Override
	public void run(Stmt_EnumDef s) {
		/*
		ParserDeclaration dec = s.declaration;
		String name = s.name.lexeme;
		EArrayList<Expr_Enum> values = s.values;
		
		//check if already defined
		if (scope().get(name) != null) throw new AlreadyDefinedError(name);
		
		//create the enum instance
		EnvisionEnum en = new EnvisionEnum(dec.getVisibility(), name);
		
		//read in declared enum values
		for (int i = 0; i < values.size(); i++) {
			Expr_Enum expr = values.get(i);
			
			String valueName = expr.name.lexeme;
			EArrayList<Expression> args = expr.args;
			
			//evaluate the given args
			EArrayList<EnvisionObject> parsedArgs = new EArrayList();
			for (Expression e : args) {
				Object arg_value = evaluate(e);
				EnvisionDatatype arg_type = EnvisionDatatype.dynamicallyDetermineType(arg_value);
				EnvisionObject arg_obj = ObjectCreator.createObject(valueName + "_" + arg_type, arg_type, valueName, true);
				parsedArgs.add(arg_obj);
			}
			
			en.addValue(valueName, parsedArgs);
		}
		
		scope().define(name, en);
		*/
	}
	
	public static void run(EnvisionInterpreter in, Stmt_EnumDef s) {
		new IS_Enum(in).run(s);
	}
	
}