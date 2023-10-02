package envision_lang.interpreter.statements;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import envision_lang.EnvisionLangTest;
import envision_lang.lang.datatypes.EnvisionInt;
import envision_lang.parser.statements.statement_types.Stmt_If;

class Test_IS_If extends EnvisionLangTest {
	
	protected DebugStatement then_branch, else_branch;
	
	//==================================================
	
	@BeforeEach
	protected void setup() {
		scope().clear();
		
		then_branch = new DebugStatement();
		else_branch = new DebugStatement();
	}
	
	//==================================================
	
	@Test
	void test_thenBranch_noThen() {
		var if_stmt = stmt_if(literal(true), null, null);
		
		IS_If.run(interpreter, if_stmt);
		assertNotRun(then_branch);
		assertNotRun(else_branch);
	}
	
	@Test
	void test_thenBranch() {
		var if_stmt = stmt_if(literal(true), then_branch, null);
		
		IS_If.run(interpreter, if_stmt);
		assertRun(then_branch);
		assertNotRun(else_branch);
	}
	
	@Test
	void test_elseBranch_noElse() {
		var if_stmt = stmt_if(literal(false), then_branch, null);
		
		IS_If.run(interpreter, if_stmt);
		assertNotRun(then_branch);
		assertNotRun(else_branch);
	}
	
	@Test
	void test_elseBranch() {
		var if_stmt = stmt_if(literal(false), null, else_branch);
		
		IS_If.run(interpreter, if_stmt);
		assertNotRun(then_branch);
		assertRun(else_branch);
	}
	
	@Test
	void test_thenBranch_withElse() {
		var if_stmt = stmt_if(literal(true), then_branch, else_branch);
		
		IS_If.run(interpreter, if_stmt);
		assertRun(then_branch);
		assertNotRun(else_branch);
	}
	
	@Test
	void test_elseBranch_withElse() {
		var if_stmt = stmt_if(literal(false), then_branch, else_branch);
		
		IS_If.run(interpreter, if_stmt);
		assertNotRun(then_branch);
		assertRun(else_branch);
	}
	
	@Test
	void test_elseIf() {
		scope().defInt("x", 0);
		
		EnvisionInt x = get("x");
		assertNotNull(x);
		assertEquals(0L, x.int_val);
		
		Stmt_If if_stmt = stmt("""
								
								if (false) {
									x = 1
								}
								else if (true) {
									x = 2
								}
								
								""");
		
		IS_If.run(interpreter, if_stmt);
		
		x = get("x");
		assertNotNull(x);
		assertEquals(2L, x.int_val);
	}
	
	@Test
	void test_elseIf_else() {
		scope().defInt("x", 5);
		
		EnvisionInt x = get("x");
		assertNotNull(x);
		assertEquals(5L, x.int_val);
		
		Stmt_If if_stmt = stmt("""
								
								if (x > 10) {
									x = 1
								}
								else if (x > 5) {
									x = 2
								}
								else {
									x = 3
								}
								
								""");
		
		IS_If.run(interpreter, if_stmt);
		
		x = get("x");
		assertNotNull(x);
		assertEquals(3L, x.int_val);
	}
	
}
