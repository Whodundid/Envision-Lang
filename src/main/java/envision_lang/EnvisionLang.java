package envision_lang;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import envision_lang._launch.EnvisionCodeFile;
import envision_lang._launch.EnvisionConsoleOutputReceiver;
import envision_lang._launch.EnvisionLangErrorCallBack;
import envision_lang._launch.EnvisionLaunchSettings;
import envision_lang._launch.EnvisionLaunchSettings.LaunchSetting;
import envision_lang._launch.EnvisionLoader;
import envision_lang._launch.EnvisionProgram;
import envision_lang._launch.WorkingDirectory;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.java.EnvisionJavaObject;
import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.lang.language_errors.error_types.workingDirectory.InterpreterCreationError;
import envision_lang.lang.language_errors.error_types.workingDirectory.NoMainError;
import envision_lang.lang.packages.EnvisionLangPackage;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
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
public class EnvisionLang {
	
	/** The current build of the Envision Scripting Language. */
	public static final String version = "0.0.###";
	/** The current build's date of the Envision Scripting Language. */
	public static final String versionDate = "10/9/2022";
	/** Global debug value -- if true, debug outputs will be enabled. */
	public static boolean debugMode = false;
	/** Enables the ability to 'talk' directly to the interpreter. */
	private static boolean liveMode = false;
	/** The directory of the loaded program. */
	public static File programDir = null;
	
	public static final Logger envisionLogger = LoggerFactory.getLogger(EnvisionLang.class);
	
	//=================
	// Static Instance
	//=================
	
	private static EnvisionLang langInstance;
	
	public static EnvisionLang getInstance() {
		if (langInstance != null) return langInstance;
		return (langInstance = new EnvisionLang());
	}
	
	//--------------
	// Constructors
	//--------------
	
	/** Creates a new Envision workspace with standard launch settings. */
	private EnvisionLang() {}
	
	//----------------------------------------------------------------------------------------------------------------
	
	/**
	 * If true, normal statements will be allowed inside of class file bodies. This
	 * can very easily lead to unintended results in code which is why this is false
	 * by default.
	 */
	@Unused
	@Experimental
	private static boolean allowClassFileStatements = false;
	
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
	private static boolean allowClassBodyStatements = false;
	
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
	private static boolean preloadLanguage = false;
	
	/**
	 * If enabled, bundled program code files will be tokenized and displayed in the
	 * order they are read.
	 */
	private static boolean tokenize = false;
	
	/**
	 * If enabled, bundled program code files will be tokenized and displayed in the
	 * order they are read along with each token's full metadata as well.
	 */
	private static boolean tokenize_in_depth = false;
	
	/**
	 * If enabled, bundled program code files will be tokenized and then attempt to
	 * be parsed into logical Envision statements and displayed.
	 */
	private static boolean parse_statements = false;
	
	/**
	 * If disabled, no program code will actually be executed at run time. This flag
	 * is primarily used for debugging and testing.
	 */
	private static boolean execute_code = true;
	
	//----------------------------------------------------------------------------------------------------------------
	
	/**
	 * Packages to be loaded upon program compilation.
	 */
	private static final EList<EnvisionLangPackage> packages = EList.newList();
	
	/**
	 * A handler which is referenced when an EnvisionError is thrown.
	 */
	private static EnvisionLangErrorCallBack errorCallback = null;
	
	/**
	 * A receiver for all Envision code outputs which would normally be intended for
	 * some form of standard console output.
	 * <p>
	 * Note: if this receiver is null, the default Java Out PrintStream will be used
	 * instead.
	 */
	private static EnvisionConsoleOutputReceiver consoleReceiver = null;
	
	/**
	 * Settings which will be applied to the Envision Scripting Language and (or) given to
	 * programs executing at runtime.
	 */
	private static EnvisionLaunchSettings launchSettings = null;
	
	//----------------------------------------------------------------------------------------------------------------
	
	/** The actively bound program. */
	private static EnvisionProgram program;
	
	/** The active working directory. */
	private static WorkingDirectory dir;
	
	//------------------
	// Envision Methods
	//------------------
	
	private static void applyEnvSettings() throws Exception {
		if (launchSettings == null) return;
		
		for (var arg : launchSettings.getEnvArgs()) {
			switch (arg) {
			case PRELOAD_LANGUAGE: preloadLanguage = true; EnvisionLoader.loadLang(); break;
			case CLASS_BODY_STATEMENTS: allowClassBodyStatements = true; break;
			case CLASS_FILE_STATEMENTS: allowClassFileStatements = true; break;
			case DEBUG_MODE: debugMode = true; break;
			case LIVE_MODE: liveMode = true; break;
			case TOKENIZE: tokenize = true; break;
			case TOKENIZE_IN_DEPTH: tokenize_in_depth = true; break;
			case PARSE_STATEMENTS: parse_statements = true; break;
			case DONT_EXECUTE: execute_code = false; break;
			default: break;
			}
		}
	}
	
	public static EnvisionProgram buildProgram(String pathIn) throws Exception {
		return new EnvisionProgram(pathIn);
	}
	
	public static void runProgram(String pathIn) throws Exception { runProgram(new File(pathIn)); }
	public static void runProgram(File pathIn) throws Exception { runProgram(new EnvisionProgram(pathIn)); }
	public static void runProgram(EnvisionProgram in) throws Exception { runProgramI(in); }
	
	/** Internal run program call. */
	private static void runProgramI(EnvisionProgram programIn) throws Exception {
		program = programIn;
		dir = programIn.getWorkingDir();
		programDir = dir.getDirFile();
		
		//load build packages into dir
		try {
			packages.addAll(program.getBundledPackages());
			packages.forEach(dir::addBuildPackage);
		}
		catch (Exception err) {
			envisionLogger.error("Failed to build program!", err);
			throw err;
			//handle working directory error
		}
		
		//check main
		EnvisionCodeFile main = dir.getMain();
		
		//apply launch environment settings (if any)
		applyEnvSettings();
		
		//check if file tokens will be displayed
		if (tokenize) {
			dir.debugTokenize();
		}
		//check if file tokens and their metadata should be displayed
		if (tokenize_in_depth) {
			dir.debugTokenizeInDepth();
		}
		//check if parsed file statements will be displayed
		if (parse_statements) {
			dir.debugParsedStatements();
		}
		
		//if disabled, don't continue with program execution
		if (!execute_code) return;
		
		//check null main
		if (main == null) throw new NoMainError(dir);
		long start_time = System.currentTimeMillis();
		
		//interpret the program starting at the main file
		try {
			launchSettings = program.getLaunchArgs();
			
			//get user args
			var programArgs = (launchSettings != null) ? launchSettings.getUserArgs() : new EArrayList<String>();
			
			//throw interpret error if the file could not be loaded
			if (!main.load(dir)) throw new InterpreterCreationError();
			
			//load any program bundled envision java objects into the main's interpreter scope
			var mainScope = dir.getMain().scope();
			for (EnvisionJavaObject obj : program.getEnvisionJavaObjects()) {
				var name = obj.getClass().getSimpleName();
				var type = obj.getInternalClass().getDatatype();
				var classObj = obj.getInternalClass();
				
				mainScope.defineIfNotPresent(name, type, classObj);
			}
			
			//track program start time
			start_time = System.currentTimeMillis();
			//actually execute the built program
			EnvisionInterpreter.interpret(main, programArgs);
			
			//do live interpreting
			//if (liveMode) liveMode(main);
		}
		catch (EnvisionLangError e) {
			if (errorCallback != null) errorCallback.handleError(e);
		}
		catch (Exception e) {
			if (errorCallback != null) errorCallback.handleException(e);
		}
		finally {
			//debug log the program's total running time
			envisionLogger.debug("ENVISION-END: " + (System.currentTimeMillis() - start_time) + " ms");
		}
	}
	
	@Broken(since="Forever")
	private static void liveMode(EnvisionCodeFile main) throws Exception {
//		Scanner reader = new Scanner(System.in);
//		while (liveMode) {
//			String line = reader.nextLine();
//			if (!line.isEmpty()) main.getInterpreter().execute(EnvisionLangParser.parseStatement(line));
//			reader.reset();
//		}
//		reader.close();
	}
	
	public static void setLaunchSettings(EnvisionLaunchSettings settingsIn) {
		launchSettings = settingsIn;
	}
	
	public static void setLaunchSettings(LaunchSetting... settings) {
		launchSettings = EnvisionLaunchSettings.of(settings);
	}
	
	public static void addBuildPackage(EnvisionLangPackage... pkg) {
		for (var p : pkg) packages.add(p);
	}
	
	/**
	 * Assigns the specified class as the designated error callback for which
	 * any (and all) thrown errors will be sent to during Envision's execution.
	 *  
	 * @param <T> A class extending EnvisionErrorCallback
	 * @param callbackIn The specified class to be sent thrown errors
	 * @return This Envision instance
	 */
	public static <T extends EnvisionLangErrorCallBack> void setErrorCallback(T callbackIn) {
		errorCallback = callbackIn;
	}
	
	public static <T extends EnvisionConsoleOutputReceiver> void setConsoleReceiver(T receiverIn) {
		consoleReceiver = receiverIn;
	}
	
	//---------
	// Getters
	//---------
	
	public static WorkingDirectory getWorkingDirectory() {
		return dir;
	}
	
	public static EnvisionConsoleOutputReceiver getConsoleReceiver() {
		return consoleReceiver;
	}
	
	public static String getVersionString() {
		return "Envision Scripting Language: v" + version + " - " + versionDate;
	}
	
}
