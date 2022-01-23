package envision.interpreter.statements;

import envision.exceptions.errors.DuplicateObjectError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.ObjectCreator;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.lang.EnvisionObject;
import envision.lang.enums.EnvisionEnum;
import envision.parser.expressions.Expression;
import envision.parser.expressions.types.EnumExpression;
import envision.parser.statements.statementUtil.ParserDeclaration;
import envision.parser.statements.types.EnumStatement;
import eutil.datatypes.EArrayList;

public class IS_Enum extends StatementExecutor<EnumStatement> {

	public IS_Enum(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public void run(EnumStatement s) {
		ParserDeclaration dec = s.declaration;
		String name = s.name.lexeme;
		EArrayList<EnumExpression> values = s.values;
		
		//check if already defined
		if (scope().get(name) != null) { throw new DuplicateObjectError(name); }
		
		//create the enum instance
		EnvisionEnum en = new EnvisionEnum(dec.getVisibility(), name);
		
		//read in declared enum values
		for (int i = 0; i < values.size(); i++) {
			EnumExpression expr = values.get(i);
			
			String valueName = expr.name.lexeme;
			EArrayList<Expression> args = expr.args;
			
			//evaluate the given args
			EArrayList<EnvisionObject> parsedArgs = new EArrayList();
			for (Expression e : args) {
				Object o = evaluate(e);
				parsedArgs.add(ObjectCreator.createObject(o));
			}
			
			en.addValue(valueName, parsedArgs);
		}
		
		scope().define(name, en);
	}
	
	public static void run(EnvisionInterpreter in, EnumStatement s) {
		new IS_Enum(in).run(s);
	}
	
}