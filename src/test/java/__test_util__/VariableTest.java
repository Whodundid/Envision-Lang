package __test_util__;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import envision_lang.EnvisionLang;
import envision_lang._launch.EnvisionCodeFile;
import envision_lang.interpreter.util.scope.Scope;
import envision_lang.lang.datatypes.EnvisionVariable;
import envision_lang.lang.util.VisibilityType;

public class VariableTest {
	
	//---------------
	// Shared Fields
	//---------------
	
	protected static EnvisionLang lang = new EnvisionLang();
	protected static Path testDir = Paths.get("test_program");
	protected static Path testMain = Paths.get("test_program/main.nvis");
	protected static String mainFile;
	protected static EnvisionCodeFile main;
	protected static Scope scope;
	
	//---------------
	// Expected Enum
	//---------------
	
	protected static enum Expected { PASS, FAIL; }
	
	@BeforeEach
	public void setupWorkspace() throws Exception {
		Files.createDirectory(testDir);
	}
	
	@AfterEach
	public void destroyWorkspace() throws Exception {
		FileUtils.deleteDirectory(testDir.toFile());
		main = null;
		scope = null;
	}
	
	//------------------
	// Internal Methods
	//------------------
	
	protected static EnvisionCodeFile runProgram(Expected result) throws Exception {
		Files.write(testMain, mainFile.getBytes());
		lang.runProgram(testDir.toFile());
		
		main = lang.getWorkingDirectory().getMain();
		
		if (result == Expected.PASS) {
			assertTrue(main.isTokenized());
			assertTrue(main.isParsed());
			assertTrue(main.isLoaded());
			
			scope = main.getInterpreter().scope();
		}
		
		return main;
	}
	
	protected static void testVariable
		(String varName, Class<?> expectedType, VisibilityType expectedVis, Object expectedValue)
	{
		var theVar = scope.get(varName);
		
		//test that the variable was actually created and is the right type and visibility
		assertNotNull(theVar);
		assertInstanceOf(expectedType, theVar);
		assertEquals(expectedVis, theVar.getVisibility());
		
		//test that its value matches the expected
		var envVar = (EnvisionVariable) theVar;
		
		assertEquals(expectedValue, envVar.get_i());
	}
	
}
