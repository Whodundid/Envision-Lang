package variable_tests;

import org.junit.jupiter.api.Test;

import __test_util__.VariableTest;

public class GeneralVariableTests extends VariableTest {
	
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
