package envision_lang.interpreter;

import envision_lang._launch.EnvisionCodeFile;
import envision_lang.exceptions.errors.ExpressionError;
import envision_lang.exceptions.errors.StatementError;
import envision_lang.interpreter.util.scope.IScope;
import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;

/**
 * The primary class responsible for executing parsed Envision script
 * files.
 * 
 * @author Hunter Bragg
 */
public class EnvisionInterpreter {
	
	//=================
	// Static Instance
	//=================
	
	private static EnvisionInterpreter interpreterInstance;
	
	public static EnvisionInterpreter getInstance() {
		if (interpreterInstance != null) return interpreterInstance;
		return (interpreterInstance = new EnvisionInterpreter());
	}
	
	//--------------
	// Constructors
	//--------------
	
	private EnvisionInterpreter() {}
	
	//--------
	// Fields
	//--------
	
	private static int lastLineNum = -1;
	private static Token<?> lastLineToken = null;
	
	private static boolean runNext = false;
	private static boolean runningStatement = false;
	
	//---------------------------------------------------------------------------------
	
	public void interpret(EnvisionCodeFile codeFile, EList<String> userArgs) throws Exception {
		
	}
	
//	public IScope interpret(EnvisionCodeFile codeFile, EList<String> userArgs) throws Exception {
//		EnvPackage.ENV_PACKAGE.defineOn(this);
//		EList<Statement> statements = startingFile.getStatements();
//		
//		Package_Envision.init(envisionInstance, internalScope, userArgs);
//		
//		int cur = 0;
//		try {
//			for (Statement s : statements) {
//				execute(s);
//				cur++;
//			}
//			
//			//System.out.println("Complete: " + codeFile() + " : " + scope);
//		}
//		//exit silently on shutdown call
//		catch (LangShutdownCall shutdownCall) {}
//		//report error on statement execution error
//		catch (Exception error) {
//			//such professional error handler
//			EnvisionLang.getConsoleHandler().println("(" + startingFile.getSystemFile() + ") error at: " + statements.get(cur));
//			//error.printStackTrace();
//			throw error;
//		}
//		
//		return programScope;
//	}
	
	//---------------------
	// Interpreter Methods
	//---------------------
	
	/**
	 * Attempts to execute the given statement. Note: if the statement is null,
	 * a StatementError will be thrown instead.
	 * 
	 * @param s The statement to be executed
	 */
	public static void execute(CodeFileExecutor executor, ParsedStatement s) {
		if (s == null) throw new StatementError("The given statement is null!");
		s.execute(executor);
	}
	
	/**
	 * Attempts to evaluate the given expression. Note: if the expression is
	 * null, an ExpressionError will be thrown instead.
	 * 
	 * @param e The expression to be evaluted
	 * 
	 * @return The result of the given expression in the form of an
	 *         EnvisionObject
	 */
	public static EnvisionObject evaluate(CodeFileExecutor executor, ParsedExpression e) {
		if (e == null) throw new ExpressionError("The given expression is null!");
		return e.evaluate(executor);
	}
	
	/**
	 * Executes a series of given statements under the specific scope. The
	 * original scope is momentarily swapped out for the incoming scope.
	 * Regardless of execution sucess, the original interpreter scope will be
	 * restored once this method returns.
	 * 
	 * @param statements The list of statements to be executed
	 * @param scopeIn    The scope for which to execute the statements on
	 */
	public static void executeBlock(CodeFileExecutor executor, EList<ParsedStatement> statements, IScope scopeIn) {
		IScope prev = executor.working_scope;
		try {
			executor.working_scope = scopeIn;
			for (ParsedStatement s : statements) {
				execute(executor, s);
			}
		}
		finally {
			executor.working_scope = prev;
		}
	}
	
}
