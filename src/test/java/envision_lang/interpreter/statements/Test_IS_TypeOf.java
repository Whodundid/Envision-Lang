package envision_lang.interpreter.statements;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import envision_lang.interpreter.InterpreterTest;
import envision_lang.interpreter.expressions.IE_Assign;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.parser.expressions.expression_types.Expr_Assign;
import envision_lang.parser.expressions.expression_types.Expr_TypeOf;

public class Test_IS_TypeOf extends InterpreterTest {
	
	//======================================================================================================
	
	@BeforeEach
	protected void setup() {
		scope().clear();
		// custom class to test typeof on random class types
		scope().def("A", new EnvisionClass("A"));
	}
	
	//======================================================================================================
	
	@Test
	public void test_typeOf_null() {
		assertTypeOf("b = (null typeof boolean)", 	false);
		assertTypeOf("b = (null typeof char)", 		false);
		assertTypeOf("b = (null typeof double)", 	false);
		assertTypeOf("b = (null typeof int)", 		false);
		assertTypeOf("b = (null typeof list)", 		false);
		assertTypeOf("b = (null typeof null)", 		true); // null == null
		assertTypeOf("b = (null typeof number)", 	false);
		assertTypeOf("b = (null typeof string)", 	false);
		assertTypeOf("b = (null typeof tuple)", 	false);
		assertTypeOf("b = (null typeof var)", 		false);
		assertTypeOf("b = (null typeof void)", 		false);
		assertTypeOf("b = (null typeof A)", 		false);
		//---------------------------------------------------------------------------------------
		assertTypeOf("b = (null !typeof boolean)", 	true);
		assertTypeOf("b = (null !typeof char)", 	true);
		assertTypeOf("b = (null !typeof double)", 	true);
		assertTypeOf("b = (null !typeof int)", 		true);
		assertTypeOf("b = (null !typeof list)", 	true);
		assertTypeOf("b = (null !typeof null)", 	false); // null != null
		assertTypeOf("b = (null !typeof number)", 	true);
		assertTypeOf("b = (null !typeof string)", 	true);
		assertTypeOf("b = (null !typeof tuple)", 	true);
		assertTypeOf("b = (null !typeof var)", 		true);
		assertTypeOf("b = (null !typeof void)", 	true);
		assertTypeOf("b = (null !typeof A)", 		true);
	}
	
	//======================================================================================================
	
	@Test
	public void test_typeOf_void() {
		assertTypeOf("b = (void typeof boolean)", 	false);
		assertTypeOf("b = (void typeof char)", 		false);
		assertTypeOf("b = (void typeof double)", 	false);
		assertTypeOf("b = (void typeof int)", 		false);
		assertTypeOf("b = (void typeof list)", 		false);
		assertTypeOf("b = (void typeof null)", 		false);
		assertTypeOf("b = (void typeof number)", 	false);
		assertTypeOf("b = (void typeof string)", 	false);
		assertTypeOf("b = (void typeof tuple)", 	false);
		assertTypeOf("b = (void typeof var)", 		false);
		assertTypeOf("b = (void typeof void)", 		true); // void == void
		assertTypeOf("b = (void typeof A)", 		false);
		//---------------------------------------------------------------------------------------
		assertTypeOf("b = (void !typeof boolean)", 	true);
		assertTypeOf("b = (void !typeof char)", 	true);
		assertTypeOf("b = (void !typeof double)", 	true);
		assertTypeOf("b = (void !typeof int)", 		true);
		assertTypeOf("b = (void !typeof list)", 	true);
		assertTypeOf("b = (void !typeof null)", 	true);
		assertTypeOf("b = (void !typeof number)", 	true);
		assertTypeOf("b = (void !typeof string)", 	true);
		assertTypeOf("b = (void !typeof tuple)", 	true);
		assertTypeOf("b = (void !typeof var)", 		true);
		assertTypeOf("b = (void !typeof void)", 	false); // void != void
		assertTypeOf("b = (void !typeof A)", 		true);
	}
	
	//======================================================================================================
	
	@Test
	public void test_typeOf_char() {
		assertTypeOf("b = ('a' typeof boolean)", 	false);
		assertTypeOf("b = ('a' typeof char)", 		true); // char == char
		assertTypeOf("b = ('a' typeof double)", 	false);
		assertTypeOf("b = ('a' typeof int)", 		false);
		assertTypeOf("b = ('a' typeof list)", 		false);
		assertTypeOf("b = ('a' typeof null)", 		false);
		assertTypeOf("b = ('a' typeof number)", 	false);
		assertTypeOf("b = ('a' typeof string)", 	false);
		assertTypeOf("b = ('a' typeof tuple)", 		false);
		assertTypeOf("b = ('a' typeof var)", 		true); // char == var
		assertTypeOf("b = ('a' typeof void)", 		false);
		assertTypeOf("b = ('a' typeof A)", 			false);
		//---------------------------------------------------------------------------------------
		assertTypeOf("b = ('a' !typeof boolean)", 	true);
		assertTypeOf("b = ('a' !typeof char)", 		false); // char != char
		assertTypeOf("b = ('a' !typeof double)", 	true);
		assertTypeOf("b = ('a' !typeof int)", 		true);
		assertTypeOf("b = ('a' !typeof list)", 		true);
		assertTypeOf("b = ('a' !typeof null)", 		true);
		assertTypeOf("b = ('a' !typeof number)", 	true);
		assertTypeOf("b = ('a' !typeof string)", 	true);
		assertTypeOf("b = ('a' !typeof tuple)", 	true);
		assertTypeOf("b = ('a' !typeof var)", 		false); // char != var
		assertTypeOf("b = ('a' !typeof void)", 		true);
		assertTypeOf("b = ('a' !typeof A)", 		true);
	}
	
	//======================================================================================================
	
	private void assertTypeOf(String expr, boolean expected) {
		String withPar = expr;
		String withoutPar = expr.replace("(", "").replace(")", "");
		
		Expr_Assign withParExpr = expr(withPar);
		IE_Assign.run(interpreter, withParExpr);
		assertTrue(withParExpr.value instanceof Expr_TypeOf);
		assertEquals(expected, get_i("b"));
		
		Expr_Assign withoutParExpr = expr(withoutPar);
		IE_Assign.run(interpreter, withoutParExpr);
		assertTrue(withoutParExpr.value instanceof Expr_TypeOf);
		assertEquals(expected, get_i("b"));
	}
	
}
