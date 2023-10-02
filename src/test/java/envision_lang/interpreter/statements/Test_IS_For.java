package envision_lang.interpreter.statements;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import envision_lang.EnvisionLangTest;
import envision_lang.lang.datatypes.EnvisionInt;
import envision_lang.parser.statements.statement_types.Stmt_For;

class Test_IS_For extends EnvisionLangTest {
	
	//==================================================
	
	@BeforeEach
	protected void setup() {
		scope().clear();
	}
	
	//==================================================
	
	@Test
	void test_infinite() {
		//create for loop
		Stmt_For stmt_for = stmt("""
							
							for (;;);
							
							""");
		
		assertTestRunsForAtLeastNSeconds(1, () -> IS_For.run(interpreter, stmt_for));
	}
	
	//=========================================================================================
	
	@Test
	void test_inc_x_to_10() {
		//define 'x' ahead of time
		scope().defInt("x", 0);
		
		EnvisionInt x = get("x");
		assertNotNull(x);
		assertEquals(0L, x.int_val);
		
		//create for loop
		Stmt_For stmt_for = stmt("""
							
							for (int i = 0; i < 10; i++) {
								x++
							}
							
							""");
		
		IS_For.run(interpreter, stmt_for);
		
		x = get("x");
		assertNotNull(x);
		assertEquals(10L, x.int_val);
	}
	
	//=========================================================================================
	
	@Test
	void test_loop_break() {
		//define 'x' ahead of time
		scope().defInt("x", 0);
		
		EnvisionInt x = get("x");
		assertNotNull(x);
		assertEquals(0L, x.int_val);
		
		//create for loop
		Stmt_For stmt_for = stmt("""
							
							for (int i = 0; i < 10; i++) {
								break
								x++
							}
							
							""");
		
		IS_For.run(interpreter, stmt_for);
		
		x = get("x");
		assertNotNull(x);
		assertEquals(0L, x.int_val);
	}
	
	//=========================================================================================
	
	@Test
	void test_loop_break_post() {
		//define 'x' ahead of time
		scope().defInt("x", 0);
		
		EnvisionInt x = get("x");
		assertNotNull(x);
		assertEquals(0L, x.int_val);
		
		//create for loop
		Stmt_For stmt_for = stmt("""
							
							for (int i = 0; i < 10; i++) {
								x++
								break
							}
							
							""");
		
		IS_For.run(interpreter, stmt_for);
		
		x = get("x");
		assertNotNull(x);
		assertEquals(1L, x.int_val);
	}
	
	//=========================================================================================
	
	@Test
	void test_loop_continue() {
		//define 'x' ahead of time
		scope().defInt("x", 0);
		
		EnvisionInt x = get("x");
		assertNotNull(x);
		assertEquals(0L, x.int_val);
		
		//create for loop
		Stmt_For stmt_for = stmt("""
							
							for (int i = 0; i < 5; i++) {
								continue
								x++
							}
							
							""");
		
		IS_For.run(interpreter, stmt_for);
		
		x = get("x");
		assertNotNull(x);
		assertEquals(0L, x.int_val);
	}
	
	//=========================================================================================
	
	@Test
	void test_loop_continue_post() {
		//define 'x' ahead of time
		scope().defInt("x", 0);
		
		EnvisionInt x = get("x");
		assertNotNull(x);
		assertEquals(0L, x.int_val);
		
		//create for loop
		Stmt_For stmt_for = stmt("""
							
							for (int i = 0; i < 5; i++) {
								x++
								continue
							}
							
							""");
		
		IS_For.run(interpreter, stmt_for);
		
		x = get("x");
		assertNotNull(x);
		assertEquals(5L, x.int_val);
	}
	
	//=========================================================================================
	
	@Test
	void test_loop_breakif() {
		//define 'x' ahead of time
		scope().defInt("x", 0);
		
		EnvisionInt x = get("x");
		assertNotNull(x);
		assertEquals(0L, x.int_val);
		
		//create for loop
		Stmt_For stmt_for = stmt("""
							
							for (int i = 0; i < 10; i++) {
								x++
								breakif(x == 3)
							}
							
							""");
		
		IS_For.run(interpreter, stmt_for);
		
		x = get("x");
		assertNotNull(x);
		assertEquals(3L, x.int_val);
	}
	
	//=========================================================================================
	
	@Test
	void test_loop_contif() {
		//define 'x' ahead of time
		scope().defInt("x", 0);
		
		EnvisionInt x = get("x");
		assertNotNull(x);
		assertEquals(0L, x.int_val);
		
		//create for loop
		Stmt_For stmt_for = stmt("""
							
							for (int i = 0; i < 5; i++) {
								x++
								contif(x == 3)
								x++
							}
							
							""");
		
		IS_For.run(interpreter, stmt_for);
		
		x = get("x");
		assertNotNull(x);
		assertEquals(9L, x.int_val);
	}
	
	//=========================================================================================
	
	@Test
	void test_existing_var() {
		scope().defInt("i", 0);
		scope().defInt("x", 0);
		
		EnvisionInt i = get("i");
		EnvisionInt x = get("x");
		assertNotNull(i);
		assertNotNull(x);
		assertEquals(0L, i.int_val);
		assertEquals(0L, x.int_val);
		
		//create for loop
		Stmt_For stmt_for = stmt("""
							
							for (; i < 5; i++) {
								x++
							}
							
							""");
		
		IS_For.run(interpreter, stmt_for);
		
		i = get("i");
		x = get("x");
		assertNotNull(i);
		assertNotNull(x);
		assertEquals(5L, i.int_val);
		assertEquals(5L, x.int_val);
	}
	
	//=========================================================================================
	
	@Test
	void test_existing_var_one() {
		scope().defInt("i", 0);
		
		EnvisionInt i = get("i");
		assertNotNull(i);
		assertEquals(0L, i.int_val);
		
		//create for loop
		Stmt_For stmt_for = stmt("""
							
							for (; i < 5;) {
								i++
							}
							
							""");
		
		IS_For.run(interpreter, stmt_for);
		
		i = get("i");
		assertNotNull(i);
		assertEquals(5L, i.int_val);
	}
	
	//=========================================================================================
	
	@Test
	void test_existing_var_one2() {
		scope().defInt("i", 0);
		
		EnvisionInt i = get("i");
		assertNotNull(i);
		assertEquals(0L, i.int_val);
		
		//create for loop
		Stmt_For stmt_for = stmt("""
							
							for (; i++ < 5;);
							
							""");
		
		IS_For.run(interpreter, stmt_for);
		
		i = get("i");
		assertNotNull(i);
		assertEquals(6L, i.int_val);
	}
	
}
