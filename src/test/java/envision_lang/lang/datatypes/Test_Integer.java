package envision_lang.lang.datatypes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import envision_lang.EnvisionLangTest;
import eutil.random.ERandomUtil;

public class Test_Integer extends EnvisionLangTest {
	
	//==================================================
	
	@BeforeEach
	public void setup() {
		scope().clear();
	}
	
	//==================================================
	
	/**
	 * Tests that a new int defaults to zero.
	 */
	@Test
	public void test_zero_new() {
		EnvisionInt i = EnvisionIntClass.newInt();
		
		assertNotNull(i);
		assertEquals(0L, i.int_val);
	}
	
	//=========================================================================================
	
	/**
	 * Tests that the value of '0' defaults to zero.
	 */
	@Test
	public void test_zero_valueOf() {
		EnvisionInt i = EnvisionIntClass.valueOf(0);
		
		assertNotNull(i);
		assertEquals(0L, i.int_val);
	}
	
	//=========================================================================================
	
	/**
	 * Tests that a randomly generated long will be created and assigned to an
	 * int.
	 */
	@Test
	public void test_random_new() {
		long randomLong = ERandomUtil.getRoll(Long.MIN_VALUE, Long.MAX_VALUE);
		EnvisionInt i = EnvisionIntClass.newInt(randomLong);
		
		assertNotNull(i);
		assertEquals(randomLong, i.int_val);
	}
	
	//=========================================================================================
	
	/**
	 * Tests that the value of a randomly generated long will be created and
	 * assigned to an int.
	 */
	@Test
	public void test_random_valueOf() {
		long randomLong = ERandomUtil.getRoll(Long.MIN_VALUE, Long.MAX_VALUE);
		EnvisionInt i = EnvisionIntClass.valueOf(randomLong);
		
		assertNotNull(i);
		assertEquals(randomLong, i.int_val);
	}
	
	//=========================================================================================
	
	/**
	 * Verifies that creating two new ints with the same value produces the
	 * same numeric value in both but a different hash code in each.
	 */
	@Test
	public void test_new_different() {
		EnvisionInt a = EnvisionIntClass.newInt(5);
		EnvisionInt b = EnvisionIntClass.newInt(5);
		
		assertNotNull(a);
		assertNotNull(b);
		//values should still match
		assertEquals(a, b);
		//but hash codes should be different
		assertNotEquals(a.hashCode(), b.hashCode());
	}
	
	//=========================================================================================
	
	/**
	 * Verifies that creating two ints using 'valueOf' with the same value
	 * produces the same numeric value but with the same hash code in each.
	 */
	@Test
	public void test_valueOf_same() {
		EnvisionInt a = EnvisionIntClass.valueOf(5);
		EnvisionInt b = EnvisionIntClass.valueOf(5);
		
		assertNotNull(a);
		assertNotNull(b);
		//values should still match
		assertEquals(a, b);
		//but hash codes should be the same
		assertEquals(a.hashCode(), b.hashCode());
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
	public void test_add_zero_typed() {
		scope().defInt("a", 0);
		scope().defInt("b", 0);
		
		EnvisionInt a = get("a");
		EnvisionInt b = get("b");
		EnvisionInt c = get("c");
		
		assertNotNull(a);
		assertNotNull(b);
		assertNull(c);
		
		assertEquals(0L, a.int_val);
		assertEquals(0L, b.int_val);

		assertEquals(a.hashCode(), b.hashCode());
		
		var add_stmt = stmt("""
							
							int c = a + b
							
							""");
		
		interpreter.execute(add_stmt);
		
		a = get("a");
		b = get("b");
		c = get("c");
		
		assertNotNull(a);
		assertNotNull(b);
		assertNotNull(c);
		
		assertEquals(0L, a.int_val);
		assertEquals(0L, b.int_val);
		assertEquals(0L, c.int_val);
		
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
	public void test_add_three_typed() {
		scope().defInt("a", 1);
		scope().defInt("b", 2);
		
		EnvisionInt a = get("a");
		EnvisionInt b = get("b");
		EnvisionInt c = get("c");
		
		assertNotNull(a);
		assertNotNull(b);
		assertNull(c);
		
		assertEquals(1L, a.int_val);
		assertEquals(2L, b.int_val);

		assertNotEquals(a.hashCode(), b.hashCode());
		
		var add_stmt = stmt("""
							
							int c = a + b
							
							""");
		
		interpreter.execute(add_stmt);
		
		a = get("a");
		b = get("b");
		c = get("c");
		
		assertNotNull(a);
		assertNotNull(b);
		assertNotNull(c);
		
		assertEquals(1L, a.int_val);
		assertEquals(2L, b.int_val);
		assertEquals(3L, c.int_val);
		
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
	public void test_add_zero_typeless() {
		scope().defInt("a", 0);
		scope().defInt("b", 0);
		
		EnvisionInt a = get("a");
		EnvisionInt b = get("b");
		EnvisionInt c = get("c");
		
		assertNotNull(a);
		assertNotNull(b);
		assertNull(c);
		
		assertEquals(0L, a.int_val);
		assertEquals(0L, b.int_val);

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
		
		assertEquals(0L, a.int_val);
		assertEquals(0L, b.int_val);
		assertEquals(0L, c.int_val);
		
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
	public void test_add_three_typeless() {
		scope().defInt("a", 1);
		scope().defInt("b", 2);
		
		EnvisionInt a = get("a");
		EnvisionInt b = get("b");
		EnvisionInt c = get("c");
		
		assertNotNull(a);
		assertNotNull(b);
		assertNull(c);
		
		assertEquals(1L, a.int_val);
		assertEquals(2L, b.int_val);

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
		
		assertEquals(1L, a.int_val);
		assertEquals(2L, b.int_val);
		assertEquals(3L, c.int_val);
		
		assertNotEquals(a.hashCode(), b.hashCode());
		assertNotEquals(a.hashCode(), c.hashCode());
	}
	
	//=========================================================================================
	
	@Test
	public void test_getMaxValue() {
		execute("int max = int.MAX_VALUE");
		
		var max = get_i("max");
		assertNotNull(max);
		assertEquals(Long.MAX_VALUE, max);
	}
	
	//=========================================================================================
	
	@Test
	public void test_getMinValue() {
		execute("int min = int.MIN_VALUE");
		
		var min = get_i("min");
		assertNotNull(min);
		assertEquals(Long.MIN_VALUE, min);
	}
	
	//=========================================================================================
	
	@Test
	public void test_maxValue_minus_1() {
		execute("""
				
				int max = int.MAX_VALUE
				max = max - 1
				
				""");
		
		var max = get_i("max");
		assertNotNull(max);
		assertEquals(Long.MAX_VALUE - 1, max);
	}
	
	//=========================================================================================
	
	@Test
	public void test_maxValue_plus_1() {
		execute("""
				
				int max = int.MAX_VALUE
				max = max + 1
				
				""");
		
		var max = get_i("max");
		assertNotNull(max);
		assertEquals(Long.MAX_VALUE + 1, max);
	}
	
}
