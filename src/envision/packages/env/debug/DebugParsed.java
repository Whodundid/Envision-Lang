package envision.packages.env.debug;

import envision.EnvisionCodeFile;
import envision.exceptions.errors.ArgLengthError;
import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.internal.EnvisionFunction;
import envision.lang.util.Primitives;
import envision.parser.statements.Statement;
import envision.parser.statements.statement_types.Stmt_Expression;

public class DebugParsed extends EnvisionFunction {
		
	public DebugParsed() {
		super(Primitives.VOID, "parsed");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		if (args.length > 0) throw new ArgLengthError(this, 0, args.length);
		
		System.out.println("DEBUG: -- Printing Parsed Statements --");
		EnvisionCodeFile codeFile = interpreter.codeFile();
		for (Statement s : codeFile.getStatements()) {
			String out = "     " + s + " : ";
			;
			if (s instanceof Stmt_Expression) {
				Stmt_Expression es = (Stmt_Expression) s;
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
