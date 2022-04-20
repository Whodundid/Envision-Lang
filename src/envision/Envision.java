package envision;

import java.io.File;
import java.util.Scanner;

import envision.EnvisionSettings.LaunchArgs;
import envision.exceptions.EnvisionError;
import envision.exceptions.errors.workingDirectory.BadDirError;
import envision.exceptions.errors.workingDirectory.InterpreterCreationError;
import envision.exceptions.errors.workingDirectory.NoMainError;
import envision.packages.EnvisionLangPackage;
import envision.parser.EnvisionParser;
import eutil.datatypes.EArrayList;

public class Envision {
	
	/** The current build of the Envision Scripting Language. */
	public static final String version = "0.0.###";
	/** The current build's date of the Envision Scripting Language. */
	public static final String versionDate = "4/19/2022";
	/** Global debug value -- if true, debug outputs will be enabled. */
	public static boolean debugMode = false;
	/** Enables the ability to 'talk' directly to the interpreter. */
	private static boolean liveMode = false;
	
	//----------------------------------------------------------------------------------------------------------------
	
	/** If true, normal statements will be allowed inside of class file bodies.
	 *  This can very easily lead to unintended results in code which is why this is false by default. */
	private boolean allowClassFileStatements = false;
	
	/** 
	 * When declaring a class, only specific statements are permitted within the actual class body.
	 * These include statements such as variable declarations (fields), methods (constructors, methods)
	 * as well as class-member specific values. Otherwise, no other statements are allowed to be executed.
	 * Class body statements are static in nature and are exclusive to the class's scope itself.
	 *  
	 * Normal Example:
	 *  
	 *  <blockquote><pre>
	 *  +class Thing {
	 *  	-int a = 0
	 *  
	 *  	+init(a)
	 *  
	 *  	+func add(int num) -> a + num
	 *  }
	 *  </pre></blockquote>
	 *  
	 *  Example with class body statements allowed:
	 *  <blockquote><pre>
	 *  +class Thing {
	 *  	-^int x = 15
	 *  	-int a = 0
	 *  	
	 *  	//class body statement
	 *  	for (i to 5) println(x * i)
	 *  
	 *  	+init(a)
	 *  
	 *  	+func add(int num) -> a + num
	 *  }
	 *  </pre></blockquote>
	 */
	private boolean allowClassBodyStatements = false;
	
	/**
	 * Due to the nature of how Java loads programs on a per-class basis,
	 * the simple act of class loading will significantly slow down language performance
	 * on first runs. This setting seeks to improve Envision program runtime consistency
	 * specifically by requiring the language to be fully loaded into the Java class loader before
	 * any Envision code is executed. It should be noted that enabling this setting will actually
	 * take longer for any Envision code to start executing because the language needs to be
	 * loaded in full. What this will achieve however, is any Envision code that is executed
	 * after the language is loaded will execute much more smoothly and responsively.
	 */
	private boolean preloadLanguage = false;
	
	/**
	 * If enabled, bundled program code files will be tokenized and displayed in the order they are read.
	 */
	private boolean tokenize = false;
	
	/**
	 * If enabled, bundled program code files will be tokenized and then attempt to be parsed
	 * into logical Envision statements and displayed.
	 */
	private boolean parse_statements = false;
	
	/**
	 * If disabled, no program code will actually be executed at run time.
	 */
	private boolean execute_code = true;
	
	//----------------------------------------------------------------------------------------------------------------
	
	/** Packages to be loaded upon program compilation. */
	private EArrayList<EnvisionLangPackage> packages = new EArrayList<>();
	/** A handler which is referenced when an EnvisionError is thrown. */
	private EnvisionErrorCallback callback = null;
	/** Settings which will be applied to the Envision Language and (or) given to programs executing at runtime. */
	private EnvisionSettings launchSettings = null;
	
	//----------------------------------------------------------------------------------------------------------------
	
	/** The active working directory. */
	private WorkingDirectory dir;
	
	//--------------
	// Constructors
	//--------------
	
	/** Creates a new Envision workspace. */
	public Envision() {}
	
	public Envision(String... args) {
		launchSettings = new EnvisionSettings(args);
	}
	
	/** Creates a new Envision workspace. */
	public Envision(EnvisionSettings launchSettingsIn) {
		launchSettings = launchSettingsIn;
	}
	
	public Envision(LaunchArgs... argsIn) {
		launchSettings = new EnvisionSettings(argsIn);
	}
	
	//------------------
	// Envision Methods
	//------------------
	
	private void applyEnvSettings() throws Exception {
		if (launchSettings != null) {
			for (EnvisionSettings.LaunchArgs a : launchSettings.getEnvArgs()) {
				switch (a) {
				case PRELOAD_LANGUAGE:
					preloadLanguage = true;
					EnvisionLoader.loadLang();
					break;
				case CLASS_BODY_STATEMENTS: allowClassBodyStatements = true; break;
				case CLASS_FILE_STATEMENTS: allowClassFileStatements = true; break;
				case DEBUG_MODE: debugMode = true; break;
				case LIVE_MODE: liveMode = true; break;
				case TOKENIZE: tokenize = true; break;
				case PARSE_STATEMENTS: parse_statements = true; break;
				case DONT_EXECUTE: execute_code = false; break;
				default: break;
				}
			}
		}
	}
	
	public void runProgram() throws Exception { runProgramI(); }
	
	public void runProgram(String pathIn) throws Exception { runProgram(new File(pathIn)); }
	public void runProgram(File pathIn) throws Exception { runProgram(buildProgram(pathIn)); }
	public void runProgram(Envision in) throws Exception { in.runProgramI(); }
	
	public Envision buildProgram(File pathIn) {
		if (!(dir = new WorkingDirectory(pathIn)).isValid()) { throw new BadDirError(dir); }
		//build the working directory
		try {
			packages.forEach(dir::addBuildPackage);
			dir.discoverFiles();
		}
		catch (Exception err) {
			err.printStackTrace();
			//throw e;
			//handle working directory error
		}
		
		return this;
	}
	
	/** Internal run program call. */
	private void runProgramI() throws Exception {
		final long start_time = System.currentTimeMillis();
		
		//check main
		EnvisionCodeFile main = dir.getMain();
		
		//apply launch environment settings (if any)
		applyEnvSettings();
		
		//check if file tokens will be displayed
		if (tokenize) {
			dir.debugTokenize();
		}
		//check if parsed file statements will be displayed
		if (parse_statements) {
			dir.debugParsedStatements();
		}
		
		//if disabled, don't continue with program execution
		if (!execute_code) return;
		
		//check null main
		if (main == null) throw new NoMainError(dir);
		
		//interpret the program starting at the main file
		try {
			//get user args
			EArrayList<String> programArgs = (launchSettings != null) ? launchSettings.getUserArgs() : new EArrayList<>();
			
			//throw interpret error if the file could not be loaded
			if (!main.load(dir)) throw new InterpreterCreationError();
			
			//actually execute the built program
			main.execute(programArgs);
			
			if (liveMode) {
				Scanner reader = new Scanner(System.in);
				while (liveMode) {
					String line = reader.nextLine();
					if (!line.isEmpty()) main.getInterpreter().execute(EnvisionParser.parseStatement(line));
					reader.reset();
				}
				reader.close();
			}
		}
		catch (EnvisionError e) {
			System.out.println("error");
			if (callback != null) callback.handleError(e);
		}
		catch (Exception e) {
			System.out.println("exception");
			if (callback != null) callback.handleException(e);
		}
		finally {
			System.out.println("END: " + (System.currentTimeMillis() - start_time) + " ms");
		}
	}
	
	public Envision setLaunchSettings(EnvisionSettings settingsIn) {
		launchSettings = settingsIn;
		return this;
	}
	
	public Envision addBuildPackage(EnvisionLangPackage... pkg) {
		for (int i = 0; i < pkg.length; i++) packages.add(pkg[i]);
		return this;
	}
	
	public <T extends EnvisionErrorCallback> Envision setErrorCallback(T callbackIn) {
		callback = callbackIn;
		return this;
	}
	
	//---------
	// Getters
	//---------
	
	public WorkingDirectory getWorkingDirectory() { return dir; }
	
	public static String getVersionString() { return "Envision Scripting Language: v" + version + " - " + versionDate; }
	
}
