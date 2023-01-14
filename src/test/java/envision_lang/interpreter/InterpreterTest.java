package envision_lang.interpreter;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;

import envision_lang._launch.EnvisionCodeFile;
import envision_lang.interpreter.util.scope.IScope;
import envision_lang.interpreter.util.scope.Scope;
import envision_lang.parser.EnvisionLangParser;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.expressions.expression_types.Expr_Literal;
import envision_lang.parser.expressions.expression_types.Expr_Var;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.statements.statement_types.Stmt_If;
import envision_lang.tokenizer.IKeyword;
import envision_lang.tokenizer.ReservedWord;
import envision_lang.tokenizer.Token;

public abstract class InterpreterTest {
	
	protected EnvisionCodeFile codeFile;
	protected EnvisionInterpreter interpreter;
	
	@BeforeEach
	protected void setup() {
		
	}
	
	@BeforeEach
	protected void build() {
		codeFile = new EnvisionCodeFile(new File(UUID.randomUUID().toString()));
		interpreter = new EnvisionInterpreter(codeFile);
	}
	
	protected static class DebugStatement extends ParsedStatement {
		public boolean run = false;
		
		public DebugStatement() {
			super(Token.EOF(0));
		}
		
		@Override
		public void execute(StatementHandler handler) {
			run = true;
		}
	}
	
	protected void assertTestRunsForAtLeastNSeconds(int seconds, Runnable r) {
		new MinimumExecutionTimeVerifier(seconds, r).start();
	}
	
	protected void assertTestRunsForAtLeastNTime(TimeUnit unit, long timeAmount, Runnable r) {
		new MinimumExecutionTimeVerifier(unit, timeAmount, r).start();
	}
	
	/**
	 * A specialized class that ensures that a given runnable runs for AT LEAST
	 * the specified amount of time.
	 * <p>
	 * In practice, the actual running time seems to vary wildly.. (up to a half a second difference)
	 * 
	 * @author Hunter Bragg
	 */
	protected static class MinimumExecutionTimeVerifier {
		private Thread timerThread, runnerThread;
		private TimeUnit unit;
		private volatile boolean end = false;
		
		public MinimumExecutionTimeVerifier(long seconds, Runnable r) {
			this(TimeUnit.SECONDS, seconds, r);
		}
		
		public MinimumExecutionTimeVerifier(TimeUnit unit, long timeAmount, Runnable r) {
			Objects.requireNonNull(unit);
			Objects.requireNonNull(r);
			this.unit = unit;
			
			// thread to keep track of minimum time to run for
			timerThread = new Thread(() -> {
				long start = curTime();
				runnerThread.start();
				while (!end) {
					if ((curTime() - start) > timeAmount) break;
				}
				runnerThread.interrupt();
				assertFalse(end);
			});
			
			// thread to run the actual runner target and keep
			// track of whether or not it finishes
			runnerThread = new Thread(() -> {
				r.run();
				end = true;
			});
			
			timerThread.setDaemon(false);
		}
		
		private long curTime() {
			return unit.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
		}
		
		public void start() {
			timerThread.start();
			
			// force current thread to not die until timerThread finishes
			try { timerThread.join(); }
			catch (Exception e) {}
		}
	}
	
	protected static ParsedExpression var(String name) {
		return new Expr_Var(new Token(ReservedWord.IDENTIFIER, name, name, 0));
	}
	
	protected static ParsedExpression literal(Object value) {
		IKeyword type = null;
		if (value instanceof Character) type = ReservedWord.CHAR_LITERAL;
		else if (value instanceof Number) type = ReservedWord.NUMBER_LITERAL;
		else if (value instanceof String) type = ReservedWord.STRING_LITERAL;
		else if (value == Boolean.TRUE) type = ReservedWord.TRUE;
		else if (value == Boolean.FALSE) type = ReservedWord.FALSE;
		return new Expr_Literal(new Token(type, String.valueOf(value), value, 0), value);
	}
	
	protected ScopeManager scope() { return new ScopeManager(extractScope()); }
	
	protected static <E extends ParsedStatement> E stmt(String line) { return (E) EnvisionLangParser.parseStatement(line); }
	protected static <E extends ParsedExpression> E expr(String line) { return (E) EnvisionLangParser.parseExpression(line); }
	
	protected void injectScope(IScope scope) {
		interpreter.working_scope = new Scope(scope);
	}
	
	protected IScope extractScope() {
		return interpreter.working_scope;
	}
	
	//============
	// Assertions
	//============
	
	protected static void assertRun(DebugStatement s) { assertTrue(s.run); }
	protected static void assertNotRun(DebugStatement s) { assertFalse(s.run); }
	
	//===============
	// If Statements
	//===============
	
	protected static Stmt_If stmt_if(boolean condition, ParsedStatement then_branch, ParsedStatement else_branch) {
		return stmt_if(literal(condition), then_branch, else_branch);
	}
	protected static Stmt_If stmt_if(ParsedExpression condition, ParsedStatement then_branch, ParsedStatement else_branch) {
		return new Stmt_If(Token.EOF(0), condition, then_branch, else_branch);
	}
	
}
