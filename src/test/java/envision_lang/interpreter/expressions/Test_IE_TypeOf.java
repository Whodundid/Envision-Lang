package envision_lang.interpreter.expressions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import envision_lang.EnvisionLangTest;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.natives.IDatatype;
import envision_lang.parser.expressions.expression_types.Expr_Assign;
import envision_lang.parser.expressions.expression_types.Expr_TypeOf;

class Test_IE_TypeOf extends EnvisionLangTest {
	
	//======================================================================================================
	
	@BeforeEach
	protected void setup() {
		scope().clear();
		// custom class to test typeof on random class types
		IDatatype aType = IDatatype.of("A");
		scope().def("A", new EnvisionClass(aType));
	}
	
	//======================================================================================================
	
	@Test
	void test_typeOf_null(TestInfo testInfo) {
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
	void test_typeOf_void(TestInfo testInfo) {
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
	void test_typeOf_char(TestInfo testInfo) {
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
	
	@Test
	void test_typeOf_boolean_true(TestInfo testInfo) {
		assertTypeOf("b = (true typeof boolean)", 		true); // boolean == boolean
		assertTypeOf("b = (true typeof char)", 			false);
		assertTypeOf("b = (true typeof double)", 		false);
		assertTypeOf("b = (true typeof int)", 			false);
		assertTypeOf("b = (true typeof list)", 			false);
		assertTypeOf("b = (true typeof null)", 			false);
		assertTypeOf("b = (true typeof number)", 		false);
		assertTypeOf("b = (true typeof string)", 		false);
		assertTypeOf("b = (true typeof tuple)", 		false);
		assertTypeOf("b = (true typeof var)", 			true); // boolean == var
		assertTypeOf("b = (true typeof void)", 			false);
		assertTypeOf("b = (true typeof A)", 			false);
		//---------------------------------------------------------------------------------------
		assertTypeOf("b = (true !typeof boolean)", 		false); // boolean == boolean
		assertTypeOf("b = (true !typeof char)", 		true);
		assertTypeOf("b = (true !typeof double)", 		true);
		assertTypeOf("b = (true !typeof int)", 			true);
		assertTypeOf("b = (true !typeof list)", 		true);
		assertTypeOf("b = (true !typeof null)", 		true);
		assertTypeOf("b = (true !typeof number)", 		true);
		assertTypeOf("b = (true !typeof string)", 		true);
		assertTypeOf("b = (true !typeof tuple)", 		true);
		assertTypeOf("b = (true !typeof var)", 			false); // boolean != var
		assertTypeOf("b = (true !typeof void)", 		true);
		assertTypeOf("b = (true !typeof A)", 			true);
	}
	
	//======================================================================================================
	
	@Test
	void test_typeOf_boolean_false(TestInfo testInfo) {
		assertTypeOf("b = (false typeof boolean)", 		true); // boolean == boolean
		assertTypeOf("b = (false typeof char)", 		false);
		assertTypeOf("b = (false typeof double)", 		false);
		assertTypeOf("b = (false typeof int)", 			false);
		assertTypeOf("b = (false typeof list)", 		false);
		assertTypeOf("b = (false typeof null)", 		false);
		assertTypeOf("b = (false typeof number)", 		false);
		assertTypeOf("b = (false typeof string)", 		false);
		assertTypeOf("b = (false typeof tuple)", 		false);
		assertTypeOf("b = (false typeof var)", 			true); // boolean == var
		assertTypeOf("b = (false typeof void)", 		false);
		assertTypeOf("b = (false typeof A)", 			false);
		//---------------------------------------------------------------------------------------
		assertTypeOf("b = (false !typeof boolean)", 	false); // boolean == boolean
		assertTypeOf("b = (false !typeof char)", 		true);
		assertTypeOf("b = (false !typeof double)", 		true);
		assertTypeOf("b = (false !typeof int)", 		true);
		assertTypeOf("b = (false !typeof list)", 		true);
		assertTypeOf("b = (false !typeof null)", 		true);
		assertTypeOf("b = (false !typeof number)", 		true);
		assertTypeOf("b = (false !typeof string)", 		true);
		assertTypeOf("b = (false !typeof tuple)", 		true);
		assertTypeOf("b = (false !typeof var)", 		false); // boolean != var
		assertTypeOf("b = (false !typeof void)", 		true);
		assertTypeOf("b = (false !typeof A)", 			true);
	}
	
	//======================================================================================================
	
	@Test
	void test_typeOf_int(TestInfo testInfo) {
		assertTypeOf("b = (10 typeof boolean)", 	false);
		assertTypeOf("b = (10 typeof char)", 		false);
		assertTypeOf("b = (10 typeof double)", 		false);
		assertTypeOf("b = (10 typeof int)", 		true); // int == int
		assertTypeOf("b = (10 typeof list)", 		false);
		assertTypeOf("b = (10 typeof null)", 		false);
		assertTypeOf("b = (10 typeof number)", 		true); // int == number
		assertTypeOf("b = (10 typeof string)", 		false);
		assertTypeOf("b = (10 typeof tuple)", 		false);
		assertTypeOf("b = (10 typeof var)", 		true); // int == var
		assertTypeOf("b = (10 typeof void)", 		false);
		assertTypeOf("b = (10 typeof A)", 			false);
		//---------------------------------------------------------------------------------------
		assertTypeOf("b = (10 !typeof boolean)", 	true);
		assertTypeOf("b = (10 !typeof char)", 		true);
		assertTypeOf("b = (10 !typeof double)", 	true);
		assertTypeOf("b = (10 !typeof int)", 		false); // int != int
		assertTypeOf("b = (10 !typeof list)", 		true);
		assertTypeOf("b = (10 !typeof null)", 		true);
		assertTypeOf("b = (10 !typeof number)", 	false); // int != number
		assertTypeOf("b = (10 !typeof string)", 	true);
		assertTypeOf("b = (10 !typeof tuple)", 		true);
		assertTypeOf("b = (10 !typeof var)", 		false); // int != var
		assertTypeOf("b = (10 !typeof void)", 		true);
		assertTypeOf("b = (10 !typeof A)", 			true);
	}
	
	//======================================================================================================
	
	@Test
	void test_typeOf_double(TestInfo testInfo) {
		assertTypeOf("b = (10.0 typeof boolean)", 		false);
		assertTypeOf("b = (10.0 typeof char)", 			false);
		assertTypeOf("b = (10.0 typeof double)", 		true); // double == double
		assertTypeOf("b = (10.0 typeof int)", 			false);
		assertTypeOf("b = (10.0 typeof list)", 			false);
		assertTypeOf("b = (10.0 typeof null)", 			false);
		assertTypeOf("b = (10.0 typeof number)", 		true); // double == number
		assertTypeOf("b = (10.0 typeof string)", 		false);
		assertTypeOf("b = (10.0 typeof tuple)", 		false);
		assertTypeOf("b = (10.0 typeof var)", 			true); // double == var
		assertTypeOf("b = (10.0 typeof void)", 			false);
		assertTypeOf("b = (10.0 typeof A)", 			false);
		//---------------------------------------------------------------------------------------
		assertTypeOf("b = (10.0 !typeof boolean)", 		true);
		assertTypeOf("b = (10.0 !typeof char)", 		true);
		assertTypeOf("b = (10.0 !typeof double)", 		false); // double != double
		assertTypeOf("b = (10.0 !typeof int)", 			true);
		assertTypeOf("b = (10.0 !typeof list)", 		true);
		assertTypeOf("b = (10.0 !typeof null)", 		true);
		assertTypeOf("b = (10.0 !typeof number)", 		false); // double != number
		assertTypeOf("b = (10.0 !typeof string)", 		true);
		assertTypeOf("b = (10.0 !typeof tuple)", 		true);
		assertTypeOf("b = (10.0 !typeof var)", 			false); // double != var
		assertTypeOf("b = (10.0 !typeof void)", 		true);
		assertTypeOf("b = (10.0 !typeof A)", 			true);
	}
	
	//======================================================================================================
	
	@Test
	void test_typeOf_number_int(TestInfo testInfo) {
		execute("number n = 10");
		
		assertTypeOf("b = (n typeof boolean)", 		false);
		assertTypeOf("b = (n typeof char)", 		false);
		assertTypeOf("b = (n typeof double)", 		false);
		assertTypeOf("b = (n typeof int)", 			true); // number::int == int
		assertTypeOf("b = (n typeof list)", 		false);
		assertTypeOf("b = (n typeof null)", 		false);
		assertTypeOf("b = (n typeof number)", 		true); // number::int == number
		assertTypeOf("b = (n typeof string)", 		false);
		assertTypeOf("b = (n typeof tuple)", 		false);
		assertTypeOf("b = (n typeof var)", 			true); // number::int == var
		assertTypeOf("b = (n typeof void)", 		false);
		assertTypeOf("b = (n typeof A)", 			false);
		//---------------------------------------------------------------------------------------
		assertTypeOf("b = (n !typeof boolean)", 	true);
		assertTypeOf("b = (n !typeof char)", 		true);
		assertTypeOf("b = (n !typeof double)", 		true);
		assertTypeOf("b = (n !typeof int)", 		false); // number::int != int
		assertTypeOf("b = (n !typeof list)", 		true);
		assertTypeOf("b = (n !typeof null)", 		true);
		assertTypeOf("b = (n !typeof number)", 		false); // number::int != number
		assertTypeOf("b = (n !typeof string)", 		true);
		assertTypeOf("b = (n !typeof tuple)", 		true);
		assertTypeOf("b = (n !typeof var)", 		false); // number::int != var
		assertTypeOf("b = (n !typeof void)", 		true);
		assertTypeOf("b = (n !typeof A)", 			true);
	}
	
	//======================================================================================================
	
	@Test
	void test_typeOf_number_double(TestInfo testInfo) {
		execute("number n = 10.0");
		
		assertTypeOf("b = (n typeof boolean)", 		false);
		assertTypeOf("b = (n typeof char)", 		false);
		assertTypeOf("b = (n typeof double)", 		true); // number::double == double
		assertTypeOf("b = (n typeof int)", 			false);
		assertTypeOf("b = (n typeof list)", 		false);
		assertTypeOf("b = (n typeof null)", 		false);
		assertTypeOf("b = (n typeof number)", 		true); // number::double == double
		assertTypeOf("b = (n typeof string)", 		false);
		assertTypeOf("b = (n typeof tuple)", 		false);
		assertTypeOf("b = (n typeof var)", 			true); // number::double == var
		assertTypeOf("b = (n typeof void)", 		false);
		assertTypeOf("b = (n typeof A)", 			false);
		//---------------------------------------------------------------------------------------
		assertTypeOf("b = (n !typeof boolean)", 	true);
		assertTypeOf("b = (n !typeof char)", 		true);
		assertTypeOf("b = (n !typeof double)", 		false); // number::double != double
		assertTypeOf("b = (n !typeof int)", 		true);
		assertTypeOf("b = (n !typeof list)", 		true);
		assertTypeOf("b = (n !typeof null)", 		true);
		assertTypeOf("b = (n !typeof number)", 		false); // number::double != number
		assertTypeOf("b = (n !typeof string)", 		true);
		assertTypeOf("b = (n !typeof tuple)", 		true);
		assertTypeOf("b = (n !typeof var)", 		false); // number::double != var
		assertTypeOf("b = (n !typeof void)", 		true);
		assertTypeOf("b = (n !typeof A)", 			true);
	}
	
	//======================================================================================================
	
	@Test
	void test_typeOf_string(TestInfo testInfo) {
		assertTypeOf("b = (\"s\" typeof boolean)", 		false);
		assertTypeOf("b = (\"s\" typeof char)", 		false);
		assertTypeOf("b = (\"s\" typeof double)", 		false);
		assertTypeOf("b = (\"s\" typeof int)", 			false);
		assertTypeOf("b = (\"s\" typeof list)", 		false);
		assertTypeOf("b = (\"s\" typeof null)", 		false);
		assertTypeOf("b = (\"s\" typeof number)", 		false);
		assertTypeOf("b = (\"s\" typeof string)", 		true); // string == string
		assertTypeOf("b = (\"s\" typeof tuple)", 		false);
		assertTypeOf("b = (\"s\" typeof var)", 			true); // string == var
		assertTypeOf("b = (\"s\" typeof void)", 		false);
		assertTypeOf("b = (\"s\" typeof A)", 			false);
		//---------------------------------------------------------------------------------------
		assertTypeOf("b = (\"s\" !typeof boolean)", 	true);
		assertTypeOf("b = (\"s\" !typeof char)", 		true);
		assertTypeOf("b = (\"s\" !typeof double)", 		true);
		assertTypeOf("b = (\"s\" !typeof int)", 		true);
		assertTypeOf("b = (\"s\" !typeof list)", 		true);
		assertTypeOf("b = (\"s\" !typeof null)", 		true);
		assertTypeOf("b = (\"s\" !typeof number)", 		true);
		assertTypeOf("b = (\"s\" !typeof string)", 		false); // string != string
		assertTypeOf("b = (\"s\" !typeof tuple)", 		true);
		assertTypeOf("b = (\"s\" !typeof var)", 		false); // string != var
		assertTypeOf("b = (\"s\" !typeof void)", 		true);
		assertTypeOf("b = (\"s\" !typeof A)", 			true);
	}
	
	//======================================================================================================
	
	@Test
	void test_typeOf_list(TestInfo testInfo) {
		// create list in scope
		execute("l = []");
		
		assertTypeOf("b = (l typeof boolean)", 		false);
		assertTypeOf("b = (l typeof char)", 		false);
		assertTypeOf("b = (l typeof double)", 		false);
		assertTypeOf("b = (l typeof int)", 			false);
		assertTypeOf("b = (l typeof list)", 		true); // l == list
		assertTypeOf("b = (l typeof null)", 		false);
		assertTypeOf("b = (l typeof number)", 		false);
		assertTypeOf("b = (l typeof string)", 		false);
		assertTypeOf("b = (l typeof tuple)", 		false);
		assertTypeOf("b = (l typeof var)", 			true); // l == var
		assertTypeOf("b = (l typeof void)", 		false);
		assertTypeOf("b = (l typeof A)", 			false);
		//---------------------------------------------------------------------------------------
		assertTypeOf("b = (l !typeof boolean)", 	true);
		assertTypeOf("b = (l !typeof char)", 		true);
		assertTypeOf("b = (l !typeof double)", 		true);
		assertTypeOf("b = (l !typeof int)", 		true);
		assertTypeOf("b = (l !typeof list)", 		false); // l != list
		assertTypeOf("b = (l !typeof null)", 		true);
		assertTypeOf("b = (l !typeof number)", 		true);
		assertTypeOf("b = (l !typeof string)", 		true);
		assertTypeOf("b = (l !typeof tuple)", 		true);
		assertTypeOf("b = (l !typeof var)", 		false); // l != var
		assertTypeOf("b = (l !typeof void)", 		true);
		assertTypeOf("b = (l !typeof A)", 			true);
	}
	
	//======================================================================================================
	
	@Test
	void test_typeOf_tuple(TestInfo testInfo) {
		// create tuple in scope
		execute("t = tuple()");
		
		assertTypeOf("b = (t typeof boolean)", 		false);
		assertTypeOf("b = (t typeof char)", 		false);
		assertTypeOf("b = (t typeof double)", 		false);
		assertTypeOf("b = (t typeof int)", 			false);
		assertTypeOf("b = (t typeof list)", 		false);
		assertTypeOf("b = (t typeof null)", 		false);
		assertTypeOf("b = (t typeof number)", 		false);
		assertTypeOf("b = (t typeof string)", 		false);
		assertTypeOf("b = (t typeof tuple)", 		true); // t == tuple
		assertTypeOf("b = (t typeof var)", 			true); // t == var
		assertTypeOf("b = (t typeof void)", 		false);
		assertTypeOf("b = (t typeof A)", 			false);
		//---------------------------------------------------------------------------------------
		assertTypeOf("b = (t !typeof boolean)", 	true);
		assertTypeOf("b = (t !typeof char)", 		true);
		assertTypeOf("b = (t !typeof double)", 		true);
		assertTypeOf("b = (t !typeof int)", 		true);
		assertTypeOf("b = (t !typeof list)", 		true);
		assertTypeOf("b = (t !typeof null)", 		true);
		assertTypeOf("b = (t !typeof number)", 		true);
		assertTypeOf("b = (t !typeof string)", 		true);
		assertTypeOf("b = (t !typeof tuple)", 		false); // t != tuple
		assertTypeOf("b = (t !typeof var)", 		false); // t != var
		assertTypeOf("b = (t !typeof void)", 		true);
		assertTypeOf("b = (t !typeof A)", 			true);
	}
	
	//======================================================================================================
	
	@Test
	void test_typeOf_object(TestInfo testInfo) {
		// add an instance of 'A' to the scope
		execute("a = A()");
		
		assertTypeOf("b = (a typeof boolean)", 		false);
		assertTypeOf("b = (a typeof char)", 		false);
		assertTypeOf("b = (a typeof double)",	 	false);
		assertTypeOf("b = (a typeof int)", 			false);
		assertTypeOf("b = (a typeof list)", 		false);
		assertTypeOf("b = (a typeof null)", 		false);
		assertTypeOf("b = (a typeof number)", 		false);
		assertTypeOf("b = (a typeof string)", 		false);
		assertTypeOf("b = (a typeof tuple)", 		false);
		assertTypeOf("b = (a typeof var)", 			true); // a == var
		assertTypeOf("b = (a typeof void)", 		false);
		assertTypeOf("b = (a typeof A)", 			true); // a == A
		//---------------------------------------------------------------------------------------
		assertTypeOf("b = (a !typeof boolean)", 	true);
		assertTypeOf("b = (a !typeof char)", 		true);
		assertTypeOf("b = (a !typeof double)", 		true);
		assertTypeOf("b = (a !typeof int)", 		true);
		assertTypeOf("b = (a !typeof list)", 		true);
		assertTypeOf("b = (a !typeof null)", 		true);
		assertTypeOf("b = (a !typeof number)", 		true);
		assertTypeOf("b = (a !typeof string)", 		true);
		assertTypeOf("b = (a !typeof tuple)", 		true);
		assertTypeOf("b = (a !typeof var)", 		false); // a != var
		assertTypeOf("b = (a !typeof void)", 		true);
		assertTypeOf("b = (a !typeof A)", 			false); // a != A
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
