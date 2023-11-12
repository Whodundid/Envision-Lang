package envision_lang;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import envision_lang._launch.EnvisionProgram;
import envision_lang._launch.EnvisionProgramRunner;
import envision_lang.lang.natives.NativeTypeManager;
import eutil.debug.InDevelopment;
import eutil.debug.PlannedForRefactor;

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
@PlannedForRefactor(reason="Several fields defined here REALLY have no right to be static as these should be " +
                           "moved to the direction of the executing program itself.")
public class EnvisionLang {
	
    /** The logger instance for Envision. */
    private static final Logger logger = LoggerFactory.getLogger(EnvisionLang.class);
    
	/** The current build of the Envision Scripting Language. */
	public static final String VERSION = "0.0.6";
	/** The current build's date of the Envision Scripting Language. */
	public static final String VERSION_DATE = "11/11/2023";
	
	/** Global debug value -- if true, debug outputs will be enabled. */
	public static boolean debugMode = false;
	
	//=================
	// Static Instance
	//=================
	
	/** The single static language instance of the Envision Scripting Language. */
	private static EnvisionLang langInstance;
	
	public static EnvisionLang getInstance() {
		if (langInstance != null) return langInstance;
		langInstance = new EnvisionLang();
		return langInstance;
	}
	
	//==============
    // Constructors
    //==============
	
    /**
     * Creates a new instance of the Envision Scripting Language by
     * initializing back-end language datatype structures.
     */
    private EnvisionLang() {
        NativeTypeManager.init();
	}
	
	//----------------------------------------------------------------------------------------------------------------
	
	//==================
	// Envision Methods
	//==================
	
	public static EnvisionProgram buildProgram(String pathIn) {
		return new EnvisionProgram(pathIn);
	}
	
	public static EnvisionProgramRunner runProgram(String pathIn) throws Exception {
	    return runProgram(new File(pathIn));
	}
	
	public static EnvisionProgramRunner runProgram(File pathIn) throws Exception {
	    return runProgram(new EnvisionProgram(pathIn));
	}
	
	public static EnvisionProgramRunner runProgram(EnvisionProgram in) throws Exception {
	    return runProgramI(in);
	}
	
	/** Internal run program call. */
	private static EnvisionProgramRunner runProgramI(EnvisionProgram program) throws Exception {
	    EnvisionProgramRunner runner = new EnvisionProgramRunner(program);
		
		runner.start();
	    
		return runner;
	}
	
//	@Broken(since="Forever")
//	private static void liveMode(EnvisionCodeFile main) throws Exception {
//		//get user args
//		var programArgs = (launchSettings != null) ? launchSettings.getUserArgs() : new EArrayList<String>();
//		
//		EnvisionInterpreter interpreter = EnvisionInterpreter.build(main, programArgs);
//		
//		while (liveMode) {
//			try {
//				var stmt = EnvisionLangParser.parseStatementLive();
//				if (stmt == null) continue;
//				
//				interpreter.execute(stmt);
//			}
//			catch (EnvisionLangError e) {
//				//e.printStackTrace();
//				errorCallback.handleError(e);
//			}
//			catch (Exception e) {
//				//e.printStackTrace();
//				errorCallback.handleException(e);
//			}
//		}
//	}
	
	//=========
    // Getters
    //=========
	
	public static String getVersionString() {
		return "Envision Scripting Language: v" + VERSION + " - " + VERSION_DATE;
	}
	
}
