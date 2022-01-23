package envision.lang.packages.env.debug;

import envision.EnvisionCodeFile;
import envision.exceptions.errors.ArgLengthError;
import envision.interpreter.EnvisionInterpreter;
import envision.lang.objects.EnvisionMethod;
import envision.lang.util.EnvisionDataType;
import envision.parser.statements.Statement;
import envision.parser.statements.types.ExpressionStatement;

public class DebugParsed extends EnvisionMethod {
		
	public DebugParsed() {
		super(EnvisionDataType.VOID, "parsed");
	}
	
	@Override
	public void call(EnvisionInterpreter interpreter, Object[] args) {
		if (args.length > 0) throw new ArgLengthError(this, args.length, 0);
		
		System.out.println("DEBUG: -- Printing Parsed Statements --");
		EnvisionCodeFile codeFile = interpreter.codeFile();
		for (Statement s : codeFile.getStatements()) {
			String out = "     " + s + " : ";
			;
			if (s instanceof ExpressionStatement) {
				ExpressionStatement es = (ExpressionStatement) s;
				out += es.expression.getClass().getSimpleName();
			}
			else {
				out += s.getClass();
			}
			
			System.out.println(out);
		}
		System.out.println();
	}
	
}
