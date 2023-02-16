package envision_lang.interpreter.statements;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.throwables.Break;
import envision_lang.interpreter.util.throwables.Continue;
import envision_lang.interpreter.util.throwables.ReturnValue;
import envision_lang.lang.exceptions.EnvisionException;
import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.lang.natives.EnvisionStaticTypes;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.statement_types.Stmt_Catch;
import envision_lang.parser.statements.statement_types.Stmt_Try;
import eutil.datatypes.util.EList;
import eutil.debug.Broken;
import eutil.debug.InDevelopment;

@Broken
@InDevelopment
public class IS_Try extends AbstractInterpreterExecutor {

	public static void run(EnvisionInterpreter interpreter, Stmt_Try statement) {
		ParsedStatement tryBlock = statement.tryBlock;
		ParsedStatement finallyBlock = statement.finallyBlock;
		EList<Stmt_Catch> catchBlocks = statement.catches;
		
		//Stmt_Catch typelessCatch = findTypelessCatch(catchBlocks);
		
		int catchSize = catchBlocks.size();
		RuntimeException thrownVal = null;
		
		try {
			interpreter.execute(tryBlock);
		}
		catch (Break | Continue | ReturnValue e) {
			thrownVal = e;
		}
		catch (Exception e) { //InternalException
			if (catchSize == 0) return;
			//if (typelessCatch != null) handleCatch(interpreter, e.thrownException, typelessCatch);	
		}
		
		//handle the finally block before handling early try statement breakouts
		//the finally block MUST happen!
		if (finallyBlock != null) {
			interpreter.execute(finallyBlock);
		}
		
		//last but not least, if there was some kind of early breakout executed from the
		//try block -- throw it again so that it actually gets out!
		if (thrownVal != null) throw thrownVal;
	}
	
	private static void handleCatch(EnvisionInterpreter interpreter, EnvisionException e, Stmt_Catch c) {
		interpreter.pushScope();
		interpreter.scope().define(c.name.getLexeme(), EnvisionStaticTypes.EXCEPTION_TYPE, e);
		interpreter.executeBlock(c.body, interpreter.scope());
		interpreter.popScope();
	}
	
	private static Stmt_Catch findTypelessCatch(EList<Stmt_Catch> catchBlocks) {
		for (var c : catchBlocks) {
			var type = c.type;
			
			if (type == null) return c;
		}
		
		return null;
	}
	
	private static Stmt_Catch findCatch(EList<Stmt_Catch> catchBlocks, EnvisionLangError e) {
		
		return null;
	}
	
	private static Stmt_Catch findCatch(EList<Stmt_Catch> catchBlocks, EnvisionException e) {
		
		return null;
	}
	
}