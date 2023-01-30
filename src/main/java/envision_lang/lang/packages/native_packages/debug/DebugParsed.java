package envision_lang.lang.packages.native_packages.debug;

import envision_lang._launch.EnvisionCodeFile;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.exceptions.errors.ArgLengthError;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.lang.natives.StaticTypes;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.statement_types.Stmt_Expression;

public class DebugParsed extends EnvisionFunction {
		
	public DebugParsed() {
		super(StaticTypes.VOID_TYPE, "parsed");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		if (args.length > 0) throw new ArgLengthError(this, 0, args.length);
		
		System.out.println("DEBUG: -- Printing Parsed Statements --");
		EnvisionCodeFile codeFile = interpreter.codeFile();
		for (ParsedStatement s : codeFile.getStatements()) {
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
