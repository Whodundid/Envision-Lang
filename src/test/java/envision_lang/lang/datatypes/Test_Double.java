package envision_lang.lang.datatypes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import envision_lang.EnvisionLangTest;

class Test_Double extends EnvisionLangTest {
	
	//==================================================
	
	@BeforeEach
	protected void setup() {
		scope().clear();
	}
	
	//==================================================
	
	/**
	 * Tests that a new int defaults to zero.
	 */
	@Test
	void test_zero_new() {
		EnvisionDouble i = EnvisionDoubleClass.newDouble();
		
		assertNotNull(i);
		assertEquals(0.0, i.double_val);
	}
	
	//=========================================================================================
	
	/**
	 * Tests that the value of '0' defaults to zero.
	 */
	@Test
	void test_zero_valueOf() {
		EnvisionDouble i = EnvisionDoubleClass.valueOf(0);
		
		assertNotNull(i);
		assertEquals(0.0, i.double_val);
	}
	
	//=========================================================================================
	
	/**
	 * Verifies that creating two new doubles with the same value produces the
	 * same numeric value in both but a different hash code in each.
	 */
	@Test
	void test_new_different() {
		EnvisionDouble a = EnvisionDoubleClass.newDouble(5);
		EnvisionDouble b = EnvisionDoubleClass.newDouble(5);
		
		assertNotNull(a);
		assertNotNull(b);
		//values should still match
		assertEquals(a, b);
		//but hash codes should be different
		assertNotEquals(a.hashCode(), b.hashCode());
	}
	
	//=========================================================================================
	
	/**
	 * Adds 2 numbers by performing:
	 * 
	 * 		int a = 0
	 * 		int b = 0
	 * 		int c = a + b
	 * 
	 * and verifies that:
	 * 
	 * 		c = 0
	 */
	@Test
	void test_add_zero_typed() {
		scope().defDouble("a", 0);
		scope().defDouble("b", 0);
		
		EnvisionDouble a = get("a");
		EnvisionDouble b = get("b");
		EnvisionDouble c = get("c");
		
		assertNotNull(a);
		assertNotNull(b);
		assertNull(c);
		
		assertEquals(0.0, a.double_val);
		assertEquals(0.0, b.double_val);

		assertEquals(a.hashCode(), b.hashCode());
		
		var add_stmt = stmt("""
							
							double c = a + b
							
							""");
		
		interpreter.execute(add_stmt);
		
		a = get("a");
		b = get("b");
		c = get("c");
		
		assertNotNull(a);
		assertNotNull(b);
		assertNotNull(c);
		
		assertEquals(0.0, a.double_val);
		assertEquals(0.0, b.double_val);
		assertEquals(0.0, c.double_val);
		
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a.hashCode(), c.hashCode());
	}
	
	//=========================================================================================
	
	/**
	 * Adds 2 numbers by performing:
	 * 
	 * 		int a = 1
	 * 		int b = 2
	 * 		int c = a + b
	 * 
	 * and verifies that:
	 * 
	 * 		c = 3
	 */
	@Test
	void test_add_three_typed() {
		scope().defDouble("a", 1.0);
		scope().defDouble("b", 2.0);
		
		EnvisionDouble a = get("a");
		EnvisionDouble b = get("b");
		EnvisionDouble c = get("c");
		
		assertNotNull(a);
		assertNotNull(b);
		assertNull(c);
		
		assertEquals(1.0, a.double_val);
		assertEquals(2.0, b.double_val);

		assertNotEquals(a.hashCode(), b.hashCode());
		
		var add_stmt = stmt("""
							
							double c = a + b
							
							""");
		
		interpreter.execute(add_stmt);
		
		a = get("a");
		b = get("b");
		c = get("c");
		
		assertNotNull(a);
		assertNotNull(b);
		assertNotNull(c);
		
		assertEquals(1.0, a.double_val);
		assertEquals(2.0, b.double_val);
		assertEquals(3L, c.double_val);
		
		assertNotEquals(a.hashCode(), b.hashCode());
		assertNotEquals(a.hashCode(), c.hashCode());
	}
	
	//=========================================================================================
	
	/**
	 * Adds 2 numbers by performing:
	 * 
	 * 		a = 0
	 * 		b = 0
	 * 		c = a + b
	 * 
	 * and verifies that:
	 * 
	 * 		c = 0
	 */
	@Test
	void test_add_zero_typeless() {
		scope().defDouble("a", 0.0);
		scope().defDouble("b", 0.0);
		
		EnvisionDouble a = get("a");
		EnvisionDouble b = get("b");
		EnvisionDouble c = get("c");
		
		assertNotNull(a);
		assertNotNull(b);
		assertNull(c);
		
		assertEquals(0.0, a.double_val);
		assertEquals(0.0, b.double_val);

		assertEquals(a.hashCode(), b.hashCode());
		
		var add_stmt = stmt("""
							
							c = a + b
							
							""");
		
		interpreter.execute(add_stmt);
		
		a = get("a");
		b = get("b");
		c = get("c");
		
		assertNotNull(a);
		assertNotNull(b);
		assertNotNull(c);
		
		assertEquals(0.0, a.double_val);
		assertEquals(0.0, b.double_val);
		assertEquals(0.0, c.double_val);
		
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a.hashCode(), c.hashCode());
	}
	
	//=========================================================================================
	
	/**
	 * Adds 2 numbers by performing:
	 * 
	 * 		a = 1
	 * 		b = 2
	 * 		c = a + b
	 * 
	 * and verifies that:
	 * 
	 * 		c = 3
	 */
	@Test
	void test_add_three_typeless() {
		scope().defDouble("a", 1);
		scope().defDouble("b", 2);
		
		EnvisionDouble a = get("a");
		EnvisionDouble b = get("b");
		EnvisionDouble c = get("c");
		
		assertNotNull(a);
		assertNotNull(b);
		assertNull(c);
		
		assertEquals(1.0, a.double_val);
		assertEquals(2.0, b.double_val);

		assertNotEquals(a.hashCode(), b.hashCode());
		
		var add_stmt = stmt("""
							
							c = a + b
							
							""");
		
		interpreter.execute(add_stmt);
		
		a = get("a");
		b = get("b");
		c = get("c");
		
		assertNotNull(a);
		assertNotNull(b);
		assertNotNull(c);
		
		assertEquals(1.0, a.double_val);
		assertEquals(2.0, b.double_val);
		assertEquals(3.0, c.double_val);
		
		assertNotEquals(a.hashCode(), b.hashCode());
		assertNotEquals(a.hashCode(), c.hashCode());
	}
	
	//=========================================================================================
	
	@Test
	void test_getMaxValue() {
		execute("double max = double.MAX_VALUE");
		
		var max = get_i("max");
		assertNotNull(max);
		assertEquals(Double.MAX_VALUE, max);
	}
	
	//=========================================================================================
	
	@Test
	void test_getMinValue() {
		execute("double min = double.MIN_VALUE");
		
		var min = get_i("min");
		assertNotNull(min);
		assertEquals(Double.MIN_VALUE, min);
	}
	
	//=========================================================================================
	
	@Test
	void test_maxValue_minus_1() {
		execute("""
				
				double max = double.MAX_VALUE
				max = max - 1
				
				""");
		
		var max = get_i("max");
		assertNotNull(max);
		assertEquals(Double.MAX_VALUE - 1, max);
	}
	
	//=========================================================================================
	
	@Test
	void test_maxValue_plus_1() {
		execute("""
				
				double max = double.MAX_VALUE
				max = max + 1
				
				""");
		
		var max = get_i("max");
		assertNotNull(max);
		assertEquals(Double.MAX_VALUE + 1, max);
	}
	
}
