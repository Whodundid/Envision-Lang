package envision;

import java.io.File;
import java.util.Scanner;

import envision._launch.EnvisionCodeFile;
import envision._launch.EnvisionConsoleOutputHandler;
import envision._launch.EnvisionConsoleReceiver;
import envision._launch.EnvisionErrorCallBack;
import envision._launch.EnvisionLoader;
import envision._launch.EnvisionSettings;
import envision._launch.EnvisionSettings.LaunchArgs;
import envision._launch.WorkingDirectory;
import envision.exceptions.EnvisionError;
import envision.exceptions.errors.workingDirectory.BadDirError;
import envision.exceptions.errors.workingDirectory.InterpreterCreationError;
import envision.exceptions.errors.workingDirectory.NoMainError;
import envision.packages.EnvisionLangPackage;
import envision.parser.EnvisionParser;
import eutil.datatypes.EArrayList;
import eutil.debug.Broken;
import eutil.debug.Experimental;
import eutil.debug.InDevelopment;
import eutil.debug.Unused;

/**
 * The Envision Scripting Language - Implemented in Java.
 * 
 * <p>
 * 
 * Envision - A modular, dynamically/statically-typed, object oriented general
 * purpose scripting language capable of real-time code execution built on top
 * of the Java Virtual Machine.
 * 
 * <p>
 * 
 * Envision currently supports the following features:
 * 
 * <li>Simple Inheritance
 * <li>Lambdas
 * <li>Dynamic variable creation
 * 
 * @author Hunter Bragg
 */
@InDevelopment
public class Envision {
	
	/** The current build of the Envision Scripting Language. */
	public static final String version = "0.0.###";
	/** The current build's date of the Envision Scripting Language. */
	public static final String versionDate = "6/12/2022";
	/** Global debug value -- if true, debug outputs will be enabled. */
	public static boolean debugMode = false;
	/** Enables the ability to 'talk' directly to the interpreter. */
	private static boolean liveMode = false;
	
	//----------------------------------------------------------------------------------------------------------------
	
	/**
	 * If true, normal statements will be allowed inside of class file bodies. This
	 * can very easily lead to unintended results in code which is why this is false
	 * by default.
	 */
	@Unused
	@Experimental
	private boolean allowClassFileStatements = false;
	
	/**
	 * When declaring a class, only specific statements are permitted within the
	 * actual class body. These include statements such as variable declarations
	 * (fields), methods (constructors, methods) as well as class-member specific
	 * values. Otherwise, no other statements are allowed to be executed. Class body
	 * statements are static in nature and are exclusive to the class's scope
	 * itself.
	 * 
	 * Normal Example:
	 * 
	 * <blockquote>
	 * 
	 * <pre>
	 *  +class Thing {
	 *  	-int a = 0
	 *  
	 *  	+init(a)
	 *  
	 *  	+func add(int num) -> a + num
	 *  }
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * Example with class body statements allowed: <blockquote>
	 * 
	 * <pre>
	 *  +class Thing {
	 *  	-static int x = 15
	 *  	-int a = 0
	 *  	
	 *  	//class body statement
	 *  	for (i to 5) println(x * i)
	 *  
	 *  	+init(a)
	 *  
	 *  	+func add(int num) -> a + num
	 *  }
	 * </pre>
	 * 
	 * </blockquote>
	 */
	@Unused
	@Experimental
	private boolean allowClassBodyStatements = false;
	
	/**
	 * Due to the nature of how Java loads programs on a per-class basis, the simple
	 * act of class loading will significantly slow down overall performance on
	 * start-up runs. This boolean flag is intended to improve the Envision
	 * Language's program runtime consistency by requiring all Envision java code
	 * files to be fully loaded into the Java Class Loader before any Envision code
	 * is executed. It should be noted that enabling this setting will significantly
	 * increase the amount of time required before any Envision code actually starts
	 * executing as the language now needs to be loaded in full. The trade-off,
	 * however, is that any Envision code that is executed after the language is
	 * loaded should execute much more smoothly and responsively.
	 */
	@Broken
	private boolean preloadLanguage = false;
	
	/**
	 * If enabled, bundled program code files will be tokenized and displayed in the
	 * order they are read.
	 */
	private boolean tokenize = false;
	
	/**
	 * If enabled, bundled program code files will be tokenized and then attempt to
	 * be parsed into logical Envision statements and displayed.
	 */
	private boolean parse_statements = false;
	
	/**
	 * If disabled, no program code will actually be executed at run time. This flag
	 * is primarily used for debugging and testing.
	 */
	private boolean execute_code = true;
	
	//----------------------------------------------------------------------------------------------------------------
	
	/**
	 * Packages to be loaded upon program compilation.
	 */
	private final EArrayList<EnvisionLangPackage> packages = new EArrayList<>();
	
	/**
	 * A handler which is referenced when an EnvisionError is thrown.
	 */
	private EnvisionErrorCallBack errorCallback = null;
	
	/**
	 * A receiver for all Envision code outputs which would normally be intended for
	 * some form of standard console output.
	 * <p>
	 * Note: if this receiver is null, the default Java Out PrintStream will be used
	 * instead.
	 */
	private EnvisionConsoleReceiver consoleReceiver = null;
	
	/**
	 * Used to direct all console output to either the specified console receiver
	 * (if there is one) or to the standard Java Out PrintStream.
	 */
	private final EnvisionConsoleOutputHandler consoleOutputHandler = new EnvisionConsoleOutputHandler(this);
	
	/**
	 * Settings which will be applied to the Envision Language and (or) given to
	 * programs executing at runtime.
	 */
	private EnvisionSettings launchSettings = null;
	
	//----------------------------------------------------------------------------------------------------------------
	
	/** The active working directory. */
	private WorkingDirectory dir;
	
	//--------------
	// Constructors
	//--------------
	
	/** Creates a new Envision workspace with standard launch settings. */
	public Envision() {}
	
	/** Creates a new Envision workspace with the individually provided string arguments. */
	public Envision(String... args) {
		launchSettings = new EnvisionSettings(args);
	}
	
	/** Creates a new Envision workspace with the given launch settings. */
	public Envision(EnvisionSettings launchSettingsIn) {
		launchSettings = launchSettingsIn;
	}
	
	/** Creates a new Envision workspace with the individually provided launch arguments. */
	public Envision(LaunchArgs... argsIn) {
		launchSettings = new EnvisionSettings(argsIn);
	}
	
	//------------------
	// Envision Methods
	//------------------
	
	private void applyEnvSettings() throws Exception {
		if (launchSettings == null) return;
		
		for (EnvisionSettings.LaunchArgs a : launchSettings.getEnvArgs()) {
			switch (a) {
			case PRELOAD_LANGUAGE: preloadLanguage = true; EnvisionLoader.loadLang(); break;
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
	
	public void runProgram() throws Exception { runProgramI(); }
	
	public void runProgram(String pathIn) throws Exception { runProgram(new File(pathIn)); }
	public void runProgram(File pathIn) throws Exception { runProgram(buildProgram(pathIn)); }
	public void runProgram(Envision in) throws Exception { in.runProgramI(); }
	
	public Envision buildProgram(File pathIn) {
		if (!(dir = new WorkingDirectory(pathIn)).isValid()) throw new BadDirError(dir);
		
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
			if (!main.load(this, dir)) throw new InterpreterCreationError();
			
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
			if (errorCallback != null) errorCallback.handleError(e);
		}
		catch (Exception e) {
			if (errorCallback != null) errorCallback.handleException(e);
		}
		finally {
			//display the logged running time
			System.out.println("END: " + (System.currentTimeMillis() - start_time) + " ms");
		}
	}
	
	public Envision setLaunchSettings(EnvisionSettings settingsIn) {
		launchSettings = settingsIn;
		return this;
	}
	
	public Envision addBuildPackage(EnvisionLangPackage... pkg) {
		for (var p : pkg) packages.add(p);
		return this;
	}
	
	/**
	 * Assigns the specified class as the designated error callback for which
	 * any (and all) thrown errors will be sent to during Envision's execution.
	 *  
	 * @param <T> A class extending EnvisionErrorCallback
	 * @param callbackIn The specified class to be sent thrown errors
	 * @return This Envision instance
	 */
	public <T extends EnvisionErrorCallBack> Envision setErrorCallback(T callbackIn) {
		errorCallback = callbackIn;
		return this;
	}
	
	public <T extends EnvisionConsoleReceiver> Envision setConsoleReceiver(T receiverIn) {
		consoleReceiver = receiverIn;
		return this;
	}
	
	//---------
	// Getters
	//---------
	
	public WorkingDirectory getWorkingDirectory() {
		return dir;
	}
	
	public EnvisionConsoleReceiver getConsoleReceiver() {
		return consoleReceiver;
	}
	
	public EnvisionConsoleOutputHandler getConsoleHandler() {
		return consoleOutputHandler;
	}
	
	public static String getVersionString() {
		return "Envision Scripting Language: v" + version + " - " + versionDate;
	}
	
}
