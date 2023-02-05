package envision_lang.interpreter.statements;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.parser.statements.statement_types.unused.Stmt_EnumDef;
import eutil.debug.Broken;
import eutil.debug.InDevelopment;
import eutil.debug.Unused;

@Unused
@Broken
@InDevelopment
public class IS_Enum extends AbstractInterpreterExecutor {

	// this needs a lot of work..
	
	public static void run(EnvisionInterpreter interpreter, Stmt_EnumDef s) {
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
	
}