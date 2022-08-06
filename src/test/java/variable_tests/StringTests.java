package variable_tests;

import org.junit.jupiter.api.Test;

import __test_util__.VariableTests;
import envision_lang.lang.datatypes.EnvisionString;
import envision_lang.lang.util.VisibilityType;

public class StringTests extends VariableTests {
	
	@Test
	public void create_no_value() throws Exception {
		mainFile = """
					string x
					""";
		
		//run program
		runProgram(Expected.PASS);
		
		//test value
		testVariable("x", EnvisionString.class, VisibilityType.SCOPE, "");
	}
	
	@Test
	public void create_with_empty_value() throws Exception {
		mainFile = """
					string x = ""
					""";
		
		//run program
		runProgram(Expected.PASS);
		
		//test value
		testVariable("x", EnvisionString.class, VisibilityType.SCOPE, "");
	}
	
	@Test
	public void create_with_value() throws Exception {
		mainFile = """
					string x = "Hello World!"
					""";
		
		//run program
		runProgram(Expected.PASS);
		
		//test value
		testVariable("x", EnvisionString.class, VisibilityType.SCOPE, "Hello World!");
	}
	
	@Test
	public void create_multiple_no_value() throws Exception {
		mainFile = """
					string x, y
					""";
		
		//run program
		runProgram(Expected.PASS);
		
		//test value
		testVariable("x", EnvisionString.class, VisibilityType.SCOPE, "");
		testVariable("y", EnvisionString.class, VisibilityType.SCOPE, "");
	}
	
}
