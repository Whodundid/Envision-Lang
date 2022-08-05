package modifier_tests;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import envision_lang.EnvisionLang;
import envision_lang._launch.EnvisionCodeFile;
import envision_lang.lang.util.VisibilityType;

public class VisibilityModifierTests {
	
	private static EnvisionLang lang;
	private static Path testDir, testMain;
	private static String mainFile;
	
	//-----------------------------------
	
	@BeforeAll
	public static void pre_setup() {
		lang = new EnvisionLang();
		
		testDir = Paths.get("test_program");
		testMain = Paths.get("test_program/main.nvis");
	}
	
	@BeforeEach
	public void setupWorkspace() throws Exception {
		Files.createDirectory(testDir);
	}
	
	@AfterEach
	public void destroyWorkspace() throws Exception {
		FileUtils.deleteDirectory(testDir.toFile());
	}
	
	//-------
	// Tests
	//-------
	
	@Test
	public void scopeVisibility() throws Exception {
		mainFile = """
					int x
					""";
		
		var main = runProgram();
		var scope = main.getInterpreter().scope();
		var x = scope.get("x");
		
		assertNotNull(x);
		assertEquals(VisibilityType.SCOPE, x.getVisibility());
	}
	
	//------------------
	// Internal Methods
	//------------------
	
	private static EnvisionCodeFile runProgram() throws Exception {
		Files.write(testMain, mainFile.getBytes());
		lang.runProgram(testDir.toFile());
		
		var main = lang.getWorkingDirectory().getMain();
		
		assertTrue(main.isTokenized());
		assertTrue(main.isParsed());
		assertTrue(main.isLoaded());
		
		return main;
	}
	
}
