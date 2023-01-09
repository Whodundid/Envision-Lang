package envision_lang.interpreter.statements;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Test_IS_If extends InterpreterStatementTest {
	
	protected DebugStatement then_branch, else_branch;
	
	@BeforeEach
	protected void setup() {
		then_branch = new DebugStatement();
		else_branch = new DebugStatement();
	}
	
	@Test
	public void test_thenBranch_noThen() {
		var if_stmt = stmt_if(literal(true), null, null);
		
		IS_If.run(interpreter, if_stmt);
		assertNotRun(then_branch);
		assertNotRun(else_branch);
	}
	
	@Test
	public void test_thenBranch() {
		var if_stmt = stmt_if(literal(true), then_branch, null);
		
		IS_If.run(interpreter, if_stmt);
		assertRun(then_branch);
		assertNotRun(else_branch);
	}
	
	@Test
	public void test_elseBranch_noElse() {
		var if_stmt = stmt_if(literal(false), then_branch, null);
		
		IS_If.run(interpreter, if_stmt);
		assertNotRun(then_branch);
		assertNotRun(else_branch);
	}
	
	@Test
	public void test_elseBranch() {
		var if_stmt = stmt_if(literal(false), null, else_branch);
		
		IS_If.run(interpreter, if_stmt);
		assertNotRun(then_branch);
		assertRun(else_branch);
	}
	
	@Test
	public void test_thenBranch_withElse() {
		var if_stmt = stmt_if(literal(true), then_branch, else_branch);
		
		IS_If.run(interpreter, if_stmt);
		assertRun(then_branch);
		assertNotRun(else_branch);
	}
	
	@Test
	public void test_elseBranch_withElse() {
		var if_stmt = stmt_if(literal(false), then_branch, else_branch);
		
		IS_If.run(interpreter, if_stmt);
		assertNotRun(then_branch);
		assertRun(else_branch);
	}
	
}
