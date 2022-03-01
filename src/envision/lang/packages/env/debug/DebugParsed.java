package envision.lang.packages.env.debug;

import envision.EnvisionCodeFile;
import envision.exceptions.errors.ArgLengthError;
import envision.interpreter.EnvisionInterpreter;
import envision.lang.objects.EnvisionFunction;
import envision.lang.util.Primitives;
import envision.parser.statements.Statement;
import envision.parser.statements.statements.ExpressionStatement;

public class DebugParsed extends EnvisionFunction {
		
	public DebugParsed() {
		super(Primitives.VOID, "parsed");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, Object[] args) {
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
