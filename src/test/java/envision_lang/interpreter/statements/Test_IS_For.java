package envision_lang.interpreter.statements;

import org.junit.jupiter.api.Test;

import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.statements.statement_types.Stmt_For;
import envision_lang.tokenizer.Token;

public class Test_IS_For extends InterpreterStatementTest {
	
	@Test
	public void test_infiniteEmpty() {
		var stmt_for = new Stmt_For(Token.EOF(0), null, null, (ParsedExpression) null, null);
		
		assertTestRunsForAtLeastNSeconds(5, () -> IS_For.run(interpreter, stmt_for));
	}
	
	@Test
	public void test_loopTo10() {
		Stmt_For stmt_for = stmt("""
							for (int i = 0; i < 10; i++);
						    """);
		
		IS_For.run(interpreter, stmt_for);
		System.out.println(scope());
	}
	
}
