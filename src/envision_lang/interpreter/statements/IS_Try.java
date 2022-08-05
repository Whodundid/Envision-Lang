package envision_lang.interpreter.statements;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.interpreterBase.StatementExecutor;
import envision_lang.interpreter.util.throwables.Break;
import envision_lang.interpreter.util.throwables.Continue;
import envision_lang.interpreter.util.throwables.ReturnValue;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.statement_types.Stmt_Try;
import eutil.debug.Broken;
import eutil.debug.InDevelopment;

@Broken
@InDevelopment
public class IS_Try extends StatementExecutor<Stmt_Try> {

	public IS_Try(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public void run(Stmt_Try statement) {
		Statement tryBlock = statement.tryBlock;
		Statement finallyBlock = statement.finallyBlock;
		
		RuntimeException thrownVal = null;
		
		try {
			interpreter.execute(tryBlock);
		}
		catch (Break | Continue | ReturnValue e) {
			thrownVal = e;
		}
		catch (Exception e) {
			System.out.println(e);
			//e.printStackTrace();
			// I am not sure how to handle user defined catch statements atm..
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
	
	public static void run(EnvisionInterpreter in, Stmt_Try s) {
		new IS_Try(in).run(s);
	}
	
}