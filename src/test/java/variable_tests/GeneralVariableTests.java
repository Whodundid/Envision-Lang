package variable_tests;

import org.junit.jupiter.api.Test;

import __test_util__.VariableTests;

public class GeneralVariableTests extends VariableTests {
	
	/**
	 * This test should fail by attempting to define a variable without a
	 * given type or value.
	 */
	@Test
	public void create_fail_noValue() throws Exception {
		mainFile = """
					x
					""";
		
		//run program
		runProgram(Expected.FAIL);
	}
	
}
