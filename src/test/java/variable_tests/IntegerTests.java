package variable_tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import __test_util__.VariableTest;
import envision_lang.lang.datatypes.EnvisionInt;
import envision_lang.lang.util.VisibilityType;

public class IntegerTests extends VariableTest {

	//----------------
	// Tests : Strong
	//----------------
	
	/**
	 * This test should create a single integer 'x' with no value given.
	 * This should create an integer called 'x' and have a default value
	 * of 0.
	 */
	@Test
	public void create_no_value() throws Exception {
		mainFile = """
					int x
					""";
		
		//run program
		runProgram(Expected.PASS);
		
		//test value
		testVariable("x", EnvisionInt.class, VisibilityType.SCOPE, 0L);
	}
	
	/**
	 * This test should create a single int 'x' which should be created
	 * as an int and have a value of 5.0.
	 */
	@Test
	public void create_with_value() throws Exception {
		mainFile = """
					int x = 5
					""";
		
		//run program
		runProgram(Expected.PASS);
		
		//test value
		testVariable("x", EnvisionInt.class, VisibilityType.SCOPE, 5L);
	}
	
	/**
	 * This test will fail by attempting to create an integer with a value of a double.
	 */
	@Test
	public void create_fail_double() throws Exception {
		mainFile = """
					int x = 5.0
					""";
		
		//run program
		runProgram(Expected.FAIL);
	}
	
	@Test
	public void create_multiple() throws Exception {
		mainFile = """
					int x, y
					""";
		
		//run program
		runProgram(Expected.PASS);
		
		//test value
		testVariable("x", EnvisionInt.class, VisibilityType.SCOPE, 0L);
		testVariable("y", EnvisionInt.class, VisibilityType.SCOPE, 0L);
	}
	
	@Test
	public void create_multiple_with_values() throws Exception {
		mainFile = """
					int x = 5, y = 10
					""";
		
		//run program
		runProgram(Expected.PASS);
		
		//test value
		testVariable("x", EnvisionInt.class, VisibilityType.SCOPE, 5L);
		testVariable("y", EnvisionInt.class, VisibilityType.SCOPE, 10L);
	}
	
	@Test
	public void create_multiple_with_stepped_assignment() throws Exception {
		mainFile = """
					int x = 5, y = x
					""";
		
		//run program
		runProgram(Expected.PASS);
		
		//test value
		testVariable("x", EnvisionInt.class, VisibilityType.SCOPE, 5L);
		testVariable("y", EnvisionInt.class, VisibilityType.SCOPE, 5L);
	}
	
	//-----------------
	// Tests : Dynamic
	//-----------------
	
	@Test
	public void create_dynamic() throws Exception {
		mainFile = """
					x = 5
					""";
		
		//run program
		runProgram(Expected.PASS);
		
		//test value
		testVariable("x", EnvisionInt.class, VisibilityType.SCOPE, 5L);
	}
	
	@Test
	public void create_dynamic_multiple() throws Exception {
		mainFile = """
					x = 5
					y = x
					""";
		
		//run program
		runProgram(Expected.PASS);
		
		//test value
		testVariable("x", EnvisionInt.class, VisibilityType.SCOPE, 5L);
		testVariable("y", EnvisionInt.class, VisibilityType.SCOPE, 5L);
	}
	
	//--------------
	// Tests : Math
	//--------------
	
	@Test
	public void add() throws Exception {
		mainFile = """
					int x = 5
					int y = 5
					int z = x + y
					""";
		
		//run program
		runProgram(Expected.PASS);
		
		//test value
		testVariable("x", EnvisionInt.class, VisibilityType.SCOPE, 5L);
		testVariable("y", EnvisionInt.class, VisibilityType.SCOPE, 5L);
		testVariable("z", EnvisionInt.class, VisibilityType.SCOPE, 10L);
	}
	
	@Test
	public void sub() throws Exception {
		mainFile = """
					int x = 5
					int y = 5
					int z = x - y
					""";
		
		//run program
		runProgram(Expected.PASS);
		
		//test value
		testVariable("x", EnvisionInt.class, VisibilityType.SCOPE, 5L);
		testVariable("y", EnvisionInt.class, VisibilityType.SCOPE, 5L);
		testVariable("z", EnvisionInt.class, VisibilityType.SCOPE, 0L);
	}
	
	@Test
	public void mul() throws Exception {
		mainFile = """
					int x = 5
					int y = 5
					int z = x * y
					""";
		
		//run program
		runProgram(Expected.PASS);
		
		//test value
		testVariable("x", EnvisionInt.class, VisibilityType.SCOPE, 5L);
		testVariable("y", EnvisionInt.class, VisibilityType.SCOPE, 5L);
		testVariable("z", EnvisionInt.class, VisibilityType.SCOPE, 25L);
	}
	
	@Test
	public void div() throws Exception {
		mainFile = """
					int x = 5
					int y = 5
					int z = x / y
					""";
		
		//run program
		runProgram(Expected.PASS);
		
		//test value
		testVariable("x", EnvisionInt.class, VisibilityType.SCOPE, 5L);
		testVariable("y", EnvisionInt.class, VisibilityType.SCOPE, 5L);
		testVariable("z", EnvisionInt.class, VisibilityType.SCOPE, 1L);
	}
	
	@Test
	public void div_by_zero() throws Exception {
		mainFile = """
					int x = 5
					int y = x / 0
					""";
		
		//run program
		runProgram(Expected.PASS);
		
		//test value
		testVariable("x", EnvisionInt.class, VisibilityType.SCOPE, 5L);
		assertNull(scope.get("y"));
	}
	
}
