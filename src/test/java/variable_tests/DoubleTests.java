package variable_tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import __test_util__.VariableTests;
import envision_lang.lang.datatypes.EnvisionDouble;
import envision_lang.lang.util.VisibilityType;

public class DoubleTests extends VariableTests {
	
	//----------------
	// Tests : Strong
	//----------------
	
	/**
	 * This test should create a single double 'x' with no value given.
	 * This should create an double called 'x' and have a default value
	 * of 0.0.
	 */
	@Test
	public void create_no_value() throws Exception {
		mainFile = """
					double x
					""";
		
		//run program
		runProgram(Expected.PASS);
		
		//test value
		testVariable("x", EnvisionDouble.class, VisibilityType.SCOPE, 0.0D);
	}
	
	/**
	 * This test should create a single double 'x' which should be created
	 * as an double and have a value of 5.0.
	 */
	@Test
	public void create_with_value() throws Exception {
		mainFile = """
					double x = 5.0
					""";
		
		//run program
		runProgram(Expected.PASS);
		
		//test value
		testVariable("x", EnvisionDouble.class, VisibilityType.SCOPE, 5.0D);
	}
	
	/**
	 * This test should create a single double 'x' which should be created
	 * as an double and have a value of 5.0.
	 * <p>
	 * This test is intended to demonstrate that integer values will be
	 * up-casted as doubles when being assigned to a double var.
	 */
	@Test
	public void create_with_int_val() throws Exception {
		mainFile = """
					double x = 5
					""";
		
		//run program
		runProgram(Expected.PASS);
		
		//test value
		testVariable("x", EnvisionDouble.class, VisibilityType.SCOPE, 5.0D);
	}
	
	@Test
	public void create_multiple() throws Exception {
		mainFile = """
					double x, y
					""";
		
		//run program
		runProgram(Expected.PASS);
		
		//test value
		testVariable("x", EnvisionDouble.class, VisibilityType.SCOPE, 0.0D);
		testVariable("y", EnvisionDouble.class, VisibilityType.SCOPE, 0.0D);
	}
	
	@Test
	public void create_multiple_with_values() throws Exception {
		mainFile = """
					double x = 5, y = 10
					""";
		
		//run program
		runProgram(Expected.PASS);
		
		//test value
		testVariable("x", EnvisionDouble.class, VisibilityType.SCOPE, 5.0D);
		testVariable("y", EnvisionDouble.class, VisibilityType.SCOPE, 10.0D);
	}
	
	@Test
	public void create_multiple_with_stepped_assignment() throws Exception {
		mainFile = """
					double x = 5, y = x
					""";
		
		//run program
		runProgram(Expected.PASS);
		
		//test value
		testVariable("x", EnvisionDouble.class, VisibilityType.SCOPE, 5.0D);
		testVariable("y", EnvisionDouble.class, VisibilityType.SCOPE, 5.0D);
	}
	
	//-----------------
	// Tests : Dynamic
	//-----------------
	
	@Test
	public void create_dynamic() throws Exception {
		mainFile = """
					x = 5.0
					""";
		
		//run program
		runProgram(Expected.PASS);
		
		//test value
		testVariable("x", EnvisionDouble.class, VisibilityType.SCOPE, 5.0D);
	}
	
	@Test
	public void create_dynamic_multiple() throws Exception {
		mainFile = """
					x = 5.0
					y = x
					""";
		
		//run program
		runProgram(Expected.PASS);
		
		//test value
		testVariable("x", EnvisionDouble.class, VisibilityType.SCOPE, 5.0D);
		testVariable("y", EnvisionDouble.class, VisibilityType.SCOPE, 5.0D);
	}
	
	//--------------
	// Tests : Math
	//--------------
	
	@Test
	public void add() throws Exception {
		mainFile = """
					double x = 5
					double y = 5
					double z = x + y
					""";
		
		//run program
		runProgram(Expected.PASS);
		
		//test value
		testVariable("x", EnvisionDouble.class, VisibilityType.SCOPE, 5.0D);
		testVariable("y", EnvisionDouble.class, VisibilityType.SCOPE, 5.0D);
		testVariable("z", EnvisionDouble.class, VisibilityType.SCOPE, 10.0D);
	}
	
	@Test
	public void sub() throws Exception {
		mainFile = """
					double x = 5
					double y = 5
					double z = x - y
					""";
		
		//run program
		runProgram(Expected.PASS);
		
		//test value
		testVariable("x", EnvisionDouble.class, VisibilityType.SCOPE, 5.0D);
		testVariable("y", EnvisionDouble.class, VisibilityType.SCOPE, 5.0D);
		testVariable("z", EnvisionDouble.class, VisibilityType.SCOPE, 0.0D);
	}
	
	@Test
	public void mul() throws Exception {
		mainFile = """
					double x = 5
					double y = 5
					double z = x * y
					""";
		
		//run program
		runProgram(Expected.PASS);
		
		//test value
		testVariable("x", EnvisionDouble.class, VisibilityType.SCOPE, 5.0D);
		testVariable("y", EnvisionDouble.class, VisibilityType.SCOPE, 5.0D);
		testVariable("z", EnvisionDouble.class, VisibilityType.SCOPE, 25.0D);
	}
	
	@Test
	public void div() throws Exception {
		mainFile = """
					double x = 5
					double y = 5
					double z = x / y
					""";
		
		//run program
		runProgram(Expected.PASS);
		
		//test value
		testVariable("x", EnvisionDouble.class, VisibilityType.SCOPE, 5.0D);
		testVariable("y", EnvisionDouble.class, VisibilityType.SCOPE, 5.0D);
		testVariable("z", EnvisionDouble.class, VisibilityType.SCOPE, 1.0D);
	}
	
	@Test
	public void div_by_zero() throws Exception {
		mainFile = """
					double x = 5
					double y = x / 0
					""";
		
		//run program
		runProgram(Expected.PASS);
		
		//test value
		testVariable("x", EnvisionDouble.class, VisibilityType.SCOPE, 5.0D);
		assertNull(scope.get("y"));
	}
	
}
