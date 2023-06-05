package envision_lang.interpreter.statements;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.throwables.Break;
import envision_lang.interpreter.util.throwables.Continue;
import envision_lang.interpreter.util.throwables.InternalException;
import envision_lang.interpreter.util.throwables.LangShutdownCall;
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
		catch (LangShutdownCall e) {
			throw e;
		}
		catch (Break | Continue | ReturnValue e) {
			thrownVal = e;
		}
		catch (Exception e) { //InternalException
			var frames = interpreter.getStackFrames();
			EnvisionInterpreter.StackFrame f = null;
			boolean foundTry = false;
			Stmt_Catch foundCatch = null;
			int framesToPop = 0;
			int statementsToPop = 0;
			// start at index 1 to ignore current frame (not possible to catch here)
			int i = 1;
			
			if (frames.size() > 1) {
				TOP:
				do {
					f = frames.get(i);
					final int fSize = f.statementStack.size();
					
					// each time a frame is popped, reset the statement counter
					statementsToPop = 0;
					framesToPop++;
					
					for (int j = 0; j < fSize; j++) {
						final var s = f.statementStack.get(j);
						
						// check if it's a try statement
						// if it is, check if it the try statement can catch the thrown error
						if (s instanceof Stmt_Try t) {
							// start by checking if there even is a catch
							if (t.catches.isEmpty()) {
								foundTry = true;
								break TOP;
							}
							
							// try to find a typeless catch (catch all)
							foundCatch = findTypelessCatch(t.catches);
							if (foundCatch != null) { foundTry = true; break TOP; }
							
							// otherwise try to find a catch with a matching type
							if (e instanceof EnvisionLangError ele) {
								foundCatch = findCatch(t.catches, ele);
								if (foundCatch != null) { foundTry = true; break TOP; }
							}
							else if (e instanceof InternalException exe) {
								foundCatch = findCatch(t.catches, exe.thrownException);
								if (foundCatch != null) { foundTry = true; break TOP; }
							}
						}
						
						statementsToPop++;
					}
				}
				while (f != null);
			}
			
			if (foundTry) {
				// pop off stack frames
				int fCounter = 0;
				while (fCounter++ < framesToPop) interpreter.popStackFrame();
				
				// pop off statements from current frame
				if (statementsToPop > 0) {
					int sCounter = 0;
					final var curFrame = interpreter.getCurrentStackFrame();
					while (sCounter++ < statementsToPop) curFrame.statementStack.pop();					
				}
			}
			
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
		interpreter.executeStatements(c.body, interpreter.scope());
		interpreter.popScope();
	}
	
	private static Stmt_Catch findTypelessCatch(EList<Stmt_Catch> catchBlocks) {
		for (var c : catchBlocks) {
			var type = c.type;
			
			if (type == null) return c;
		}
		
		return null;
	}
	
	/** Catches lang errors -> JAVA type. */
	private static Stmt_Catch findCatch(EList<Stmt_Catch> catchBlocks, EnvisionLangError e) {
		
		return null;
	}
	
	/** Catches envision exceptions -> ENVISION type. */
	private static Stmt_Catch findCatch(EList<Stmt_Catch> catchBlocks, EnvisionException e) {
		
		return null;
	}
	
}